package lk.ijse.dep11.edupanel.repository.custom.impl;

import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.entity.LinkedIn;
import lk.ijse.dep11.edupanel.entity.Picture;
import lk.ijse.dep11.edupanel.repository.CrudRepositoryImpl;
import lk.ijse.dep11.edupanel.repository.custom.PictureRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PictureRepositoryImpl extends CrudRepositoryImpl<Picture, Integer> implements PictureRepository {

}
