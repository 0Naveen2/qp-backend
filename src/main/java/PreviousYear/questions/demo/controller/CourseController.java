package PreviousYear.questions.demo.controller;

import PreviousYear.questions.demo.entity.Course;
import PreviousYear.questions.demo.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // List all courses by department - anyone
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Course>> getCoursesByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(courseService.getCoursesByDepartment(departmentId));
    }

    // Add course - Admin only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/department/{departmentId}")
    public ResponseEntity<Course> addCourse(@PathVariable Long departmentId, @RequestBody Course course) {
        return ResponseEntity.ok(courseService.addCourse(departmentId, course));
    }

    // Delete course - Admin only
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok("Course deleted successfully");
    }
}
