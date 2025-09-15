package PreviousYear.questions.demo.controller;

import PreviousYear.questions.demo.entity.Subject;
import PreviousYear.questions.demo.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
@CrossOrigin(origins = "*")
public class SubjectController {

    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    // Endpoint for the STUDENT dashboard (requires semester)
    @GetMapping("/course/{courseId}/semester/{semester}")
    public ResponseEntity<List<Subject>> getSubjectsByCourseAndSemester(@PathVariable Long courseId,
            @PathVariable int semester) {
        return ResponseEntity.ok(subjectService.getSubjectsByCourseAndSemester(courseId, semester));
    }

    // --- THIS IS THE NEW ENDPOINT FOR THE ADMIN PAGE ---
    // It is simpler and gets all subjects for a course.
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Subject>> getSubjectsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(subjectService.getSubjectsByCourse(courseId));
    }
    // --- END OF NEW ENDPOINT ---

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/course/{courseId}")
    public ResponseEntity<Subject> addSubject(@PathVariable Long courseId, @RequestBody Subject subject) {
        return ResponseEntity.ok(subjectService.addSubject(courseId, subject));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{subjectId}")
    public ResponseEntity<?> deleteSubject(@PathVariable Long subjectId) {
        subjectService.deleteSubject(subjectId);
        return ResponseEntity.ok("Subject deleted successfully");
    }
}