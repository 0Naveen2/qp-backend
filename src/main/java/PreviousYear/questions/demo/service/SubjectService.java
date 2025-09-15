package PreviousYear.questions.demo.service;

import PreviousYear.questions.demo.entity.Course;
import PreviousYear.questions.demo.entity.Subject;
import PreviousYear.questions.demo.repository.CourseRepository;
import PreviousYear.questions.demo.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository, CourseRepository courseRepository) {
        this.subjectRepository = subjectRepository;
        this.courseRepository = courseRepository;
    }

    public List<Subject> getSubjectsByCourseAndSemester(Long courseId, int semester) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return subjectRepository.findByCourseAndSemester(course, semester);
    }

    // --- THIS IS THE NEW METHOD FOR THE ADMIN PAGE ---
    public List<Subject> getSubjectsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return subjectRepository.findByCourse(course);
    }
    // --- END OF NEW METHOD ---

    public Subject addSubject(Long courseId, Subject subject) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (subjectRepository.existsByNameAndCourseAndSemester(subject.getName(), course, subject.getSemester())) {
            throw new RuntimeException("Subject already exists in this course for this semester");
        }

        subject.setCourse(course);
        return subjectRepository.save(subject);
    }

    public void deleteSubject(Long subjectId) {
        subjectRepository.deleteById(subjectId);
    }
}