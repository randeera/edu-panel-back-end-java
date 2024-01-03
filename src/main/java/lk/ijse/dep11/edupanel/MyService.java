package lk.ijse.dep11.edupanel;

import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.repository.RepositoryFactory;
import lk.ijse.dep11.edupanel.repository.custom.LecturerRepository;
import lk.ijse.dep11.edupanel.repository.custom.impl.LecturerRepositoryImpl;

public class MyService {

    void saveLecturer(){
        LecturerRepository repository = RepositoryFactory.getInstance().getRepository(RepositoryFactory.RepositoryType.LECTURER);
    }
}
