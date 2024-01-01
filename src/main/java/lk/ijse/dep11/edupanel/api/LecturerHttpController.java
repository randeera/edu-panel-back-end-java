package lk.ijse.dep11.edupanel.api;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.entity.LinkedIn;
import lk.ijse.dep11.edupanel.entity.Picture;
import lk.ijse.dep11.edupanel.to.LecturerTO;
import lk.ijse.dep11.edupanel.to.request.LecturerReqTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/lecturers")
@CrossOrigin
public class LecturerHttpController {

    @Autowired
    private EntityManager em;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private Bucket bucket;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    public LecturerTO createNewLecturer(@ModelAttribute @Validated(LecturerReqTO.Create.class)
                                      LecturerReqTO lecturerReqTo){
        em.getTransaction().begin();
        try {
            Lecturer lecturer = mapper.map(lecturerReqTo, Lecturer.class);
            lecturer.setPicture(null);
            lecturer.setLinkedIn(null);
            em.persist(lecturer);
            LecturerTO lecturerTO = mapper.map(lecturer, LecturerTO.class);

            if (lecturerReqTo.getLinkedin() != null){
                em.persist(new LinkedIn(lecturer, lecturerReqTo.getLinkedin()));
                lecturerTO.setLinkedin(lecturerReqTo.getLinkedin());
            }

            if (lecturerReqTo.getPicture() != null){
                Picture picture = new Picture(lecturer, "lectures/" + lecturer.getId());
                em.persist(picture);

                Blob blobRef = bucket.create(picture.getPicturePath(), lecturerReqTo.getPicture().getInputStream(), lecturerReqTo.getPicture().getContentType());
                lecturerTO.setPicture(blobRef.signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());
            }

            em.getTransaction().commit();
            return lecturerTO;
        }catch (Throwable t){
            em.getTransaction().rollback();
            throw new RuntimeException(t);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}", consumes = "multipart/form-data")
    public void updateLecturerDetailsViaMultipart(@PathVariable("lecturer-id") Integer lecturerId){}

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{lecturer-id}", consumes = "application/json")
    public void updateLecturerDetailsViaJson(@PathVariable("lecturer-id") Integer lecturerId){}

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{lecturer-id}")
    public void deleteLecturer(@PathVariable("lecturer-id") Integer lecturerId){
        Lecturer lecturer = em.find(Lecturer.class, lecturerId);
        if (lecturer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        em.getTransaction().begin();
        try {
            em.remove(lecturer);

            if (lecturer.getPicture() != null){
                bucket.get(lecturer.getPicture().getPicturePath()).delete();
            }

            em.getTransaction().commit();
        }catch (Throwable t){
            em.getTransaction().rollback();
            throw new RuntimeException(t);
        }
    }

    @GetMapping(produces = "application/json")
    public List<LecturerTO> getAllLecturers(){
        TypedQuery<Lecturer> query = em.createQuery("SELECT l FROM Lecturer l", Lecturer.class);
        return getLecturerTOList(query);
    }

    @GetMapping(value = "/{lecturer-id}", produces = "application/json")
    public LecturerTO getLecturerDetails(@PathVariable("lecturer-id") Integer lecturerId){
        Lecturer lecturer = em.find(Lecturer.class, lecturerId);
        if (lecturer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return getLecturerTO(lecturer);
    }

    @GetMapping(params = "type=full-time", produces = "application/json")
    public List<LecturerTO> getFullTimeLecturers(){
        TypedQuery<Lecturer> query = em.createQuery("SELECT l FROM Lecturer l WHERE l.type = lk.ijse.dep11.edupanel.util.LecturerType.FULL_TIME", Lecturer.class);
        return getLecturerTOList(query);
    }

    @GetMapping(params = "type=visiting", produces = "application/json")
    public List<LecturerTO> getVisitingLecturers(){
        TypedQuery<Lecturer> query = em.createQuery("SELECT l FROM Lecturer l WHERE l.type = lk.ijse.dep11.edupanel.util.LecturerType.VISITING", Lecturer.class);
        return getLecturerTOList(query);
    }

    private List<LecturerTO> getLecturerTOList(TypedQuery<Lecturer> query) {
        return query.getResultStream().map(this::getLecturerTO).collect(Collectors.toList());
    }

    private LecturerTO getLecturerTO(Lecturer lectureEntity) {
        LecturerTO lecturerTO = mapper.map(lectureEntity, LecturerTO.class);
        if (lectureEntity.getLinkedIn() != null) lecturerTO.setLinkedin(lectureEntity.getLinkedIn().getUrl());
        if (lectureEntity.getPicture() != null){
            lecturerTO.setPicture(bucket.get(lectureEntity.getPicture().getPicturePath()).signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());
        }
        return lecturerTO;
    }
}
