package lk.ijse.dep11.edupanel.service.util;

import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.entity.LinkedIn;
import lk.ijse.dep11.edupanel.to.LecturerTO;
import lk.ijse.dep11.edupanel.to.request.LecturerReqTO;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class Transformer {

    private final ModelMapper mapper = new ModelMapper();

    public Transformer() {
//        mapper.typeMap(Lecturer.class, LecturerTO.class)
//                .addMapping(lecturer -> lecturer.getLinkedIn().getUrl(), LecturerTO::setLinkedin);
        mapper.typeMap(LinkedIn.class, String.class)
                .setConverter(ctx -> ctx.getSource().getUrl());
    }

    Lecturer fromLecturerReqTO(LecturerReqTO lecturerReqTO){
        return mapper.map(lecturerReqTO, Lecturer.class);
    }

    Lecturer fromLecturerTO(LecturerTO lecturerTO){
        return mapper.map(lecturerTO, Lecturer.class);
    }

    LecturerTO toLecturerTO(Lecturer lecturer){
        return mapper.map(lecturer, LecturerTO.class);
    }

    List<LecturerTO> toLectureTOList(List<Lecturer> lecturerList){
        return lecturerList.stream().map(this::toLecturerTO).collect(Collectors.toList());
    }

}
