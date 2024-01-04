package lk.ijse.dep11.edupanel.service.custom;

import lk.ijse.dep11.edupanel.service.SuperService;
import lk.ijse.dep11.edupanel.to.LecturerTO;
import lk.ijse.dep11.edupanel.to.request.LecturerReqTO;
import lk.ijse.dep11.edupanel.util.LecturerType;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public interface LecturerService extends SuperService {

    LecturerTO saveLecturer(LecturerReqTO lecturerReqTO);

    void updateLecturerDetailsWithImage(LecturerReqTO lecturerReqTO);

    void updateLecturerDetailsWithoutImage(LecturerTO lecturerTO);

    void deleteLecturer(Integer lecturerId);

    LecturerTO getLecturerDetails(Integer lecturerId);

    List<LecturerTO> getLecturers(LecturerType type);
}
