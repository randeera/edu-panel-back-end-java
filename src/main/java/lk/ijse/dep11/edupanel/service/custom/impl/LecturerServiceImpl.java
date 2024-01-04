package lk.ijse.dep11.edupanel.service.custom.impl;

import lk.ijse.dep11.edupanel.repository.RepositoryFactory;
import lk.ijse.dep11.edupanel.repository.custom.LecturerRepository;
import lk.ijse.dep11.edupanel.repository.custom.LinkedInRepository;
import lk.ijse.dep11.edupanel.repository.custom.PictureRepository;
import lk.ijse.dep11.edupanel.service.custom.LecturerService;
import lk.ijse.dep11.edupanel.store.AppStore;
import lk.ijse.dep11.edupanel.to.LecturerTO;
import lk.ijse.dep11.edupanel.to.request.LecturerReqTO;
import lk.ijse.dep11.edupanel.util.LecturerType;

import java.util.List;

public class LecturerServiceImpl implements LecturerService {

    private final LecturerRepository lecturerRepository = RepositoryFactory.getInstance()
            .getRepository(RepositoryFactory.RepositoryType.LECTURER);
    private final LinkedInRepository linkedInRepository = RepositoryFactory.getInstance()
            .getRepository(RepositoryFactory.RepositoryType.LINKEDIN);
    private final PictureRepository pictureRepository = RepositoryFactory.getInstance()
            .getRepository(RepositoryFactory.RepositoryType.PICTURE);

    public LecturerServiceImpl() {
        lecturerRepository.setEntityManager(AppStore.getEntityManager());
        linkedInRepository.setEntityManager(AppStore.getEntityManager());
        pictureRepository.setEntityManager(AppStore.getEntityManager());
    }

    @Override
    public LecturerTO saveLecturer(LecturerReqTO lecturerReqTO) {
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
