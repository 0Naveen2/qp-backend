package PreviousYear.questions.demo.repository;

import PreviousYear.questions.demo.entity.Course;
import PreviousYear.questions.demo.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    // For the STUDENT dashboard (finds subjects by semester)
    List<Subject> findByCourseAndSemester(Course course, int semester);

    // NEW method for the ADMIN page (finds all subjects in a course)
    List<Subject> findByCourse(Course course);

    boolean existsByNameAndCourseAndSemester(String name, Course course, int semester);

    Optional<Subject> findByCode(String code);
}