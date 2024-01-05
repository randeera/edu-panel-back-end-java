package lk.ijse.dep11.edupanel.repository.custom.impl;

import lk.ijse.dep11.edupanel.entity.LinkedIn;
import lk.ijse.dep11.edupanel.repository.CrudRepositoryImpl;
import lk.ijse.dep11.edupanel.repository.custom.LinkedInRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LinkedInRepositoryImpl extends CrudRepositoryImpl<LinkedIn, Integer> implements LinkedInRepository {

}
