package PreviousYear.questions.demo.repository;

import PreviousYear.questions.demo.entity.Course;
import PreviousYear.questions.demo.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByDepartment(Department department);
    boolean existsByNameAndDepartment(String name, Department department);
    Optional<Course> findByCode(String code);
}
