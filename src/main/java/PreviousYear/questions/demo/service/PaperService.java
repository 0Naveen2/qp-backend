package PreviousYear.questions.demo.service;

import PreviousYear.questions.demo.entity.QuestionPaper;
import PreviousYear.questions.demo.entity.User;
import PreviousYear.questions.demo.repository.QuestionPaperRepository;
import PreviousYear.questions.demo.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service
public class PaperService {

    private final QuestionPaperRepository paperRepo;
    private final FileStorageService fileStorageService;
    private final SubjectRepository subjectRepo;

    @Autowired
    public PaperService(QuestionPaperRepository paperRepo,
            FileStorageService fileStorageService,
            SubjectRepository subjectRepo) {
        this.paperRepo = paperRepo;
        this.fileStorageService = fileStorageService;
        this.subjectRepo = subjectRepo;
    }

    public QuestionPaper uploadPaper(int semester, int year, String session, String setNumber,
            Long subjectId, MultipartFile file, User uploader) {

        var subject = subjectRepo.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + subjectId));

        String storedFileName = fileStorageService.storeFile(file);

        QuestionPaper paper = QuestionPaper.builder()
                .semester(semester)
                .year(year)
                .session(session)
                .setNumber(setNumber)
                .subject(subject)
                .fileName(storedFileName)
                .originalName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .contentType(file.getContentType())
                .uploadedAt(LocalDateTime.now()) // Set the upload time
                .uploadedBy(uploader)
                .build();
        return paperRepo.save(paper);
    }

    // --- THIS IS THE NEW, SMARTER DELETE LOGIC ---
    public void deletePaper(Long id, User currentUser) {
        QuestionPaper paper = paperRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Paper not found with id: " + id));

        boolean isAdmin = currentUser.getRole() == User.Role.ADMIN;
        boolean isOwner = Objects.equals(paper.getUploadedBy().getId(), currentUser.getId());

        // An admin can delete any paper, anytime.
        if (isAdmin) {
            paperRepo.delete(paper);
            fileStorageService.deleteFile(paper.getFileName());
            return;
        }

        // A regular user (student) can only delete if they are the owner AND it's
        // within 1 hour.
        if (isOwner) {
            long minutesSinceUpload = ChronoUnit.MINUTES.between(paper.getUploadedAt(), LocalDateTime.now());

            if (minutesSinceUpload <= 60) {
                paperRepo.delete(paper);
                fileStorageService.deleteFile(paper.getFileName());
            } else {
                // If it's past the 1-hour window, deny access.
                throw new AccessDeniedException(
                        "Deletion window has expired. You can only delete papers within 1 hour of uploading.");
            }
        } else {
            // If they are not the owner, deny access.
            throw new AccessDeniedException("You do not have permission to delete this paper.");
        }
    }

    public List<QuestionPaper> getPapersBySubject(Long subjectId) {
        var subject = subjectRepo.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + subjectId));
        return paperRepo.findBySubject(subject);
    }

    public QuestionPaper getPaperMetadata(Long id) {
        return paperRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Paper not found with id: " + id));
    }

    public Resource getPaperResource(Long id) {
        QuestionPaper paper = getPaperMetadata(id);
        return fileStorageService.loadAsResource(paper.getFileName());
    }

    // (We removed the old listPapers methods as they are no longer needed by the
    // frontend)
}