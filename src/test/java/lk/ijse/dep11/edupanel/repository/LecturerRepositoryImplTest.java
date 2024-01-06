package lk.ijse.dep11.edupanel.repository;

import lk.ijse.dep11.edupanel.entity.Lecturer;
import lk.ijse.dep11.edupanel.entity.LinkedIn;
import lk.ijse.dep11.edupanel.entity.Picture;
import lk.ijse.dep11.edupanel.repository.LecturerRepository;
import lk.ijse.dep11.edupanel.util.LecturerType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {WebAppConfig.class, WebRootConfig.class})
//@WebAppConfiguration
//@SpringJUnitConfig(classes = {WebRootConfig.class})
@SpringBootTest
@Transactional
class LecturerRepositoryImplTest {

    @Autowired
    private LecturerRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void save() {
        Lecturer lecturer = new Lecturer("Kasun Sampath",
                "Senior Trainer",
                "BSc (Hons) in Computing",
                LecturerType.FULL_TIME,
                0);
        Lecturer savedLecturer = repository.save(lecturer);

        assertTrue(savedLecturer.getId() > 0);
        savedLecturer = entityManager.find(Lecturer.class, savedLecturer.getId());
        assertNotNull(savedLecturer);
    }

    @Test
    void update() {
        Lecturer lecturer = new Lecturer("Kasun Sampath",
                "Senior Trainer",
                "BSc (Hons) in Computing",
                LecturerType.FULL_TIME,
                0);
        Lecturer savedLecturer = repository.save(lecturer);
        savedLecturer.setName("Nuwan Ramindu");
        savedLecturer.setQualifications("DEP");
        savedLecturer.setType(LecturerType.VISITING);
        repository.save(savedLecturer);

        Lecturer actualLecturer = entityManager.find(Lecturer.class, savedLecturer.getId());
        assertEquals(savedLecturer, actualLecturer);
    }

    @Test
    void deleteById() {
        Lecturer lecturer = new Lecturer("Kasun Sampath",
                "Senior Trainer",
                "BSc (Hons) in Computing",
                LecturerType.FULL_TIME,
                0);
        Lecturer savedLecturer = repository.save(lecturer);
        repository.deleteById(savedLecturer.getId());

        Lecturer dbLecturer = entityManager.find(Lecturer.class, savedLecturer.getId());
        assertNull(dbLecturer);
    }

    @Test
    void existsById() {
        Lecturer lecturer = new Lecturer("Kasun Sampath",
                "Senior Trainer",
                "BSc (Hons) in Computing",
                LecturerType.FULL_TIME,
                0);
        Lecturer savedLecturer = repository.save(lecturer);
        boolean result = repository.existsById(savedLecturer.getId());

        assertTrue(result);
    }

    @Test
    void findById() {
        Lecturer lecturer = new Lecturer("Kasun Sampath",
                "Senior Trainer",
                "BSc (Hons) in Computing",
                LecturerType.FULL_TIME,
                0);
        Lecturer savedLecturer = repository.save(lecturer);
        Optional<Lecturer> optLecturer1 = repository.findById(savedLecturer.getId());
        Optional<Lecturer> optLecturer2 = repository.findById(-2500);

        assertTrue(optLecturer1.isPresent());
        assertTrue(optLecturer2.isEmpty());
    }

    @Test
    void findAll() {
        for (int i = 0; i < 8; i++) {
            Lecturer lecturer = new Lecturer("Kasun Sampath",
                    "Senior Trainer",
                    "BSc (Hons) in Computing",
                    LecturerType.FULL_TIME,
                    0);
            repository.save(lecturer);
        }
        List<Lecturer> lecturerList = repository.findAll();

        assertTrue(lecturerList.size() >= 8);
        lecturerList.forEach(System.out::println);
    }

    @Test
    void count() {
        for (int i = 0; i < 120; i++) {
            Lecturer lecturer = new Lecturer("Kasun Sampath",
                    "Senior Trainer",
                    "BSc (Hons) in Computing",
                    LecturerType.FULL_TIME,
                    0);
            repository.save(lecturer);
        }
        long count = repository.count();

        assertTrue(count >= 120);
    }

    @Commit
    @Test
    void experiment(@Autowired LinkedInRepository linkedInRepository) {
        Optional<Lecturer> optLecturer = repository.findById(2);
        Lecturer lecturer = optLecturer.orElseThrow();
//        Lecturer newLecturer = new Lecturer(2, "Suranga", "Trainer", "BSc", LecturerType.FULL_TIME, 5);
//        //newLecturer.setLinkedIn(new LinkedIn(2, newLecturer, "http://ijse.lk"));
//        Lecturer merge = entityManager.merge(newLecturer);
//        System.out.println(merge.getLinkedIn());
        LinkedIn linkedIn = new LinkedIn(2, lecturer, "http://ijse.lk");
        entityManager.merge(linkedIn);
        //repository.save(newLecturer);
        //linkedInRepository.save(newLecturer.getLinkedIn());
    }
}