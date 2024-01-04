package lk.ijse.dep11.edupanel.service.custom.impl;

import com.google.cloud.storage.Bucket;
import lk.ijse.dep11.edupanel.WebAppConfig;
import lk.ijse.dep11.edupanel.WebRootConfig;
import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.entity.LinkedIn;
import lk.ijse.dep11.edupanel.repository.custom.LecturerRepository;
import lk.ijse.dep11.edupanel.repository.custom.LinkedInRepository;
import lk.ijse.dep11.edupanel.repository.custom.PictureRepository;
import lk.ijse.dep11.edupanel.service.ServiceFactory;
import lk.ijse.dep11.edupanel.service.custom.LecturerService;
import lk.ijse.dep11.edupanel.store.AppStore;
import lk.ijse.dep11.edupanel.to.LecturerTO;
import lk.ijse.dep11.edupanel.to.request.LecturerReqTO;
import lk.ijse.dep11.edupanel.util.LecturerType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;
import static org.mockito.Mockito.*;

@SpringJUnitWebConfig(classes = {WebAppConfig.class, WebRootConfig.class})
//@ExtendWith(MockitoExtension.class)
class LecturerServiceImplTest {

    private LecturerService lecturerService;
    @Autowired
    private EntityManagerFactory emf;
    @Autowired
    private Bucket bucket;
    private EntityManager entityManager;

//    @Mock
//    private LecturerRepository lecturerRepository;
//    @Mock
//    private LinkedInRepository linkedInRepository;
//    @Mock
//    private PictureRepository pictureRepository;

    @BeforeEach
    void setUp() {
        entityManager = emf.createEntityManager();
        AppStore.setEntityManager(entityManager);
        AppStore.setBucket(bucket);
        lecturerService = ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.LECTURER);

//        when(lecturerRepository.save(any(Lecturer.class))).thenAnswer(inv ->{
//            Lecturer lecturer = inv.getArgument(0);
//            lecturer.setId(1);
//            return lecturer;
//        });
//
//        when(linkedInRepository.save(any(LinkedIn.class))).thenAnswer(inv -> inv.getArgument(0));
//
//        lecturerService.setLecturerRepository(lecturerRepository);
//        lecturerService.setLinkedInRepository(linkedInRepository);
//        lecturerService.setPictureRepository(pictureRepository);
    }

    @AfterEach
    void tearDown() {
        entityManager.close();
    }

    @Test
    void saveLecturer() {
        LecturerReqTO lecturerReqTo = new LecturerReqTO("Amith",
                "Associate Lecturer", "BSc, MSc",
                LecturerType.VISITING, 5,
                null,
                "https://linkedin.com");
        LecturerTO lecturerTO = lecturerService.saveLecturer(lecturerReqTo);

        assertNotNull(lecturerTO.getId());
        assertTrue(lecturerTO.getId() > 0);
        assertEquals(lecturerReqTo.getName(), lecturerTO.getName());
        assertEquals(lecturerReqTo.getDesignation(), lecturerTO.getDesignation());
        assertEquals(lecturerReqTo.getQualifications(), lecturerTO.getQualifications());
        assertEquals(lecturerReqTo.getType(), lecturerTO.getType());
        assertEquals(lecturerReqTo.getDisplayOrder(), lecturerTO.getDisplayOrder());
        assumingThat(lecturerReqTo.getLinkedin() != null, ()-> assertEquals(lecturerReqTo.getLinkedin(), lecturerTO.getLinkedin()));
        assumingThat(lecturerReqTo.getLinkedin() == null, ()-> assertNull(lecturerTO.getLinkedin()));

//        if (lecturerReqTo.getLinkedin() != null){
//            assertEquals(lecturerReqTo.getLinkedin(), lecturerTO.getLinkedin());
//        }else{
//            assertNull(lecturerTO.getLinkedin());
//        }
    }
}