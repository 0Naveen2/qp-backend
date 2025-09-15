package PreviousYear.questions.demo.repository;

import PreviousYear.questions.demo.entity.QuestionPaper;
import PreviousYear.questions.demo.entity.Subject; // Import Subject
import PreviousYear.questions.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionPaperRepository extends JpaRepository<QuestionPaper, Long> {

    List<QuestionPaper> findBySemester(int semester);

    List<QuestionPaper> findByYear(int year);

    List<QuestionPaper> findByUploadedBy(User user);

    List<QuestionPaper> findBySemesterAndYearAndSession(int semester, int year, String session);

    // --- NEW METHOD ---
    List<QuestionPaper> findBySubject(Subject subject);
}