package lk.ijse.dep11.edupanel.service.custom.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.entity.LinkedIn;
import lk.ijse.dep11.edupanel.entity.Picture;
import lk.ijse.dep11.edupanel.exception.AppException;
import lk.ijse.dep11.edupanel.repository.RepositoryFactory;
import lk.ijse.dep11.edupanel.repository.custom.LecturerRepository;
import lk.ijse.dep11.edupanel.repository.custom.LinkedInRepository;
import lk.ijse.dep11.edupanel.repository.custom.PictureRepository;
import lk.ijse.dep11.edupanel.service.custom.LecturerService;
import lk.ijse.dep11.edupanel.service.util.Transformer;
import lk.ijse.dep11.edupanel.store.AppStore;
import lk.ijse.dep11.edupanel.to.LecturerTO;
import lk.ijse.dep11.edupanel.to.request.LecturerReqTO;
import lk.ijse.dep11.edupanel.util.LecturerType;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class LecturerServiceImpl implements LecturerService {

    private final LecturerRepository lecturerRepository = RepositoryFactory.getInstance()
            .getRepository(RepositoryFactory.RepositoryType.LECTURER);
    private final LinkedInRepository linkedInRepository = RepositoryFactory.getInstance()
            .getRepository(RepositoryFactory.RepositoryType.LINKEDIN);
    private final PictureRepository pictureRepository = RepositoryFactory.getInstance()
            .getRepository(RepositoryFactory.RepositoryType.PICTURE);
    private final Transformer transformer = new Transformer();

    public LecturerServiceImpl() {
        lecturerRepository.setEntityManager(AppStore.getEntityManager());
        linkedInRepository.setEntityManager(AppStore.getEntityManager());
        pictureRepository.setEntityManager(AppStore.getEntityManager());
    }

    @Override
    public LecturerTO saveLecturer(LecturerReqTO lecturerReqTO) {
        AppStore.getEntityManager().getTransaction().begin();
        try {
            Lecturer lecturer = transformer.fromLecturerReqTO(lecturerReqTO);
            lecturerRepository.save(lecturer);

            if (lecturerReqTO.getLinkedin() != null) {
                linkedInRepository.save(lecturer.getLinkedIn());
            }

            String signUrl = null;
            if (lecturerReqTO.getPicture() != null) {
                Picture picture = new Picture(lecturer, "lecturers/" + lecturer.getId());
                pictureRepository.save(picture);

                Blob blobRef = AppStore.getBucket().create(picture.getPicturePath(),
                        lecturerReqTO.getPicture().getInputStream(), lecturerReqTO.getPicture().getContentType());
                signUrl = (blobRef.signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());
            }

            AppStore.getEntityManager().getTransaction().commit();
            LecturerTO lecturerTO = transformer.toLecturerTO(lecturer);
            lecturerTO.setPicture(signUrl);
            return lecturerTO;
        }catch (Throwable t){
            AppStore.getEntityManager().getTransaction().rollback();
            throw new AppException(500, "Failed to save the lecturer", t);
        }
    }

    @Override
    public void updateLecturerDetails(LecturerReqTO lecturerReqTO) {
        Optional<Lecturer> optLecturer = lecturerRepository.findById(lecturerReqTO.getId());
        if (optLecturer.isEmpty()) throw new AppException(404, "No lecturer found");
        Lecturer currentLecturer = optLecturer.get();

        AppStore.getEntityManager().getTransaction().begin();
        try {
            Lecturer newLecturer = transformer.fromLecturerReqTO(lecturerReqTO);
            if (lecturerReqTO.getPicture() != null) {
                newLecturer.setPicture(new Picture(newLecturer, "lecturers/" + currentLecturer.getId()));
            }
            if (lecturerReqTO.getLinkedin() != null) {
                newLecturer.setLinkedIn(new LinkedIn(newLecturer, lecturerReqTO.getLinkedin()));
            }

            updateLinkedIn(currentLecturer, newLecturer);

            if (newLecturer.getPicture() != null && currentLecturer.getPicture() == null) {
                pictureRepository.save(newLecturer.getPicture());
                AppStore.getBucket().create(newLecturer.getPicture().getPicturePath(), lecturerReqTO.getPicture().getInputStream(), lecturerReqTO.getPicture().getContentType());
            } else if (newLecturer.getPicture() == null && currentLecturer.getPicture() != null) {
                pictureRepository.deleteById(currentLecturer.getId());
                AppStore.getBucket().get(currentLecturer.getPicture().getPicturePath()).delete();
            } else if (newLecturer.getPicture() != null) {
                pictureRepository.update(newLecturer.getPicture());
                AppStore.getBucket().create(newLecturer.getPicture().getPicturePath(), lecturerReqTO.getPicture().getInputStream(), lecturerReqTO.getPicture().getContentType());
            }

            lecturerRepository.update(newLecturer);
            AppStore.getEntityManager().getTransaction().commit();
        }catch (Throwable t){
            AppStore.getEntityManager().getTransaction().rollback();
            throw new AppException(500, "Failed to update the lecturer details", t);
        }
    }

    @Override
    public void updateLecturerDetails(LecturerTO lecturerTO) {
        Optional<Lecturer> optLecturer = lecturerRepository.findById(lecturerTO.getId());
        if (optLecturer.isEmpty()) throw new AppException(404, "No lecturer found");
        Lecturer currentLecturer = optLecturer.get();
        AppStore.getEntityManager().getTransaction().begin();
        try {
            Lecturer newLecturer = transformer.fromLecturerTO(lecturerTO);
            newLecturer.setPicture(currentLecturer.getPicture());
            updateLinkedIn(currentLecturer, newLecturer);
            lecturerRepository.update(newLecturer);
            AppStore.getEntityManager().getTransaction().commit();
        }catch (Throwable t){
            AppStore.getEntityManager().getTransaction().rollback();
            throw t;
        }
    }

    @Override
    public void deleteLecturer(Integer lecturerId) {
        if (!lecturerRepository.existsById(lecturerId)) throw new AppException(404, "No lecturer found");
        AppStore.getEntityManager().getTransaction().begin();
        try {
            lecturerRepository.deleteById(lecturerId);
            AppStore.getEntityManager().getTransaction().commit();
        }catch (Throwable t){
            AppStore.getEntityManager().getTransaction().rollback();
            throw t;
        }
    }

    @Override
    public LecturerTO getLecturerDetails(Integer lecturerId) {
        AppStore.getEntityManager().getTransaction().begin();
        try {
            Optional<Lecturer> optLecturer = lecturerRepository.findById(lecturerId);
            if (optLecturer.isEmpty()) throw new AppException(404, "No lecturer found");
            AppStore.getEntityManager().getTransaction().commit();
            LecturerTO lecturerTO = transformer.toLecturerTO(optLecturer.get());
            if (optLecturer.get().getPicture() != null) {
                lecturerTO.setPicture(AppStore.getBucket().get(optLecturer.get().getPicture().getPicturePath()).signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());
            }
            return lecturerTO;
        }catch (Throwable t){
            AppStore.getEntityManager().getTransaction().rollback();
            throw t;
        }
    }

    @Override
    public List<LecturerTO> getLecturers(LecturerType type) {
        AppStore.getEntityManager().getTransaction().begin();
        try {
            List<Lecturer> lecturerList;
            if (type == LecturerType.FULL_TIME){
                lecturerList = lecturerRepository.findFullTimeLecturers();
            }else if (type == LecturerType.VISITING){
                lecturerList = lecturerRepository.findVisitingLectures();
            }else {
                lecturerList = lecturerRepository.findAll();
            }
            AppStore.getEntityManager().getTransaction().commit();
            return lecturerList.stream().map(l -> {
                LecturerTO lecturerTO = transformer.toLecturerTO(l);
                if (l.getPicture() != null) {
                    lecturerTO.setPicture(AppStore.getBucket().get(l.getPicture().getPicturePath()).signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString());
                }
                return lecturerTO;
            }).collect(Collectors.toList());
        }catch (Throwable t){
            AppStore.getEntityManager().getTransaction().rollback();
            throw t;
        }
    }

    private void updateLinkedIn(Lecturer currentLecturer, Lecturer newLecturer){
        if (newLecturer.getLinkedIn() != null && currentLecturer.getLinkedIn() == null) {
            linkedInRepository.save(newLecturer.getLinkedIn());
        } else if (newLecturer.getLinkedIn() == null && currentLecturer.getLinkedIn() != null) {
            linkedInRepository.deleteById(currentLecturer.getId());
        } else if (newLecturer.getLinkedIn() != null) {
            linkedInRepository.update(newLecturer.getLinkedIn());
        }
    }
}
