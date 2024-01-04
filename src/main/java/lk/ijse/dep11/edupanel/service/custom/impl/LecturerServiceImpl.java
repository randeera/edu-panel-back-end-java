package lk.ijse.dep11.edupanel.service.custom.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.entity.LinkedIn;
import lk.ijse.dep11.edupanel.entity.Picture;
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
import java.util.concurrent.TimeUnit;

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
            lecturer = lecturerRepository.save(lecturer);

            if (lecturerReqTO.getLinkedin() != null) {
                linkedInRepository.save(new LinkedIn(lecturer, lecturerReqTO.getLinkedin()));
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
            throw t;
        }
    }

    @Override
    public void updateLecturerDetailsWithImage(LecturerReqTO lecturerReqTO) {
        AppStore.getEntityManager().getTransaction().begin();
        try {

            AppStore.getEntityManager().getTransaction().commit();
        }catch (Throwable t){
            AppStore.getEntityManager().getTransaction().rollback();
            throw t;
        }
    }

    @Override
    public void updateLecturerDetailsWithoutImage(LecturerTO lecturerTO) {
        AppStore.getEntityManager().getTransaction().begin();
        try {

            AppStore.getEntityManager().getTransaction().commit();
        }catch (Throwable t){
            AppStore.getEntityManager().getTransaction().rollback();
            throw t;
        }
    }

    @Override
    public void deleteLecturer(Integer lecturerId) {
        AppStore.getEntityManager().getTransaction().begin();
        try {

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

            AppStore.getEntityManager().getTransaction().commit();
            return null;
        }catch (Throwable t){
            AppStore.getEntityManager().getTransaction().rollback();
            throw t;
        }
    }

    @Override
    public List<LecturerTO> getLecturers(LecturerType type) {
        AppStore.getEntityManager().getTransaction().begin();
        try {

            AppStore.getEntityManager().getTransaction().commit();
            return null;
        }catch (Throwable t){
            AppStore.getEntityManager().getTransaction().rollback();
            throw t;
        }
    }
}
