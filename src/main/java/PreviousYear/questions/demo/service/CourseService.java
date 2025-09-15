package PreviousYear.questions.demo.service;

import PreviousYear.questions.demo.entity.Course;
import PreviousYear.questions.demo.entity.Department;
import PreviousYear.questions.demo.repository.CourseRepository;
import PreviousYear.questions.demo.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, DepartmentRepository departmentRepository) {
        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<Course> getCoursesByDepartment(Long departmentId) {
        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        return courseRepository.findByDepartment(dept);
    }

    public Course addCourse(Long departmentId, Course course) {
        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        if (courseRepository.existsByNameAndDepartment(course.getName(), dept)) {
            throw new RuntimeException("Course already exists in this department");
        }

        course.setDepartment(dept);
        return courseRepository.save(course);
    }

    public void deleteCourse(Long courseId) {
        courseRepository.deleteById(courseId);
    }

    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }
}
