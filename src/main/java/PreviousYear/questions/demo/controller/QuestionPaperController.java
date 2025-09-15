package PreviousYear.questions.demo.controller;

import PreviousYear.questions.demo.entity.QuestionPaper;
import PreviousYear.questions.demo.entity.User;
import PreviousYear.questions.demo.repository.UserRepository;
import PreviousYear.questions.demo.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/papers")
@CrossOrigin(origins = "*")
public class QuestionPaperController {

    private final PaperService paperService;
    private final UserRepository userRepo;

    @Autowired
    public QuestionPaperController(PaperService paperService, UserRepository userRepo) {
        this.paperService = paperService;
        this.userRepo = userRepo;
    }

    // Now accessible by both STUDENT and ADMIN
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPaper(
            @RequestParam int semester,
            @RequestParam int year,
            @RequestParam String session,
            @RequestParam(required = false) String setNumber,
            @RequestParam Long subjectId,
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {

        User uploader = userRepo.findByUsername(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in database"));

        QuestionPaper savedPaper = paperService.uploadPaper(semester, year, session, setNumber, subjectId, file,
                uploader);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPaper);
    }

    // Now accessible by both STUDENT and ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePaper(@PathVariable Long id,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        User currentUser = userRepo.findByUsername(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in database"));

        paperService.deletePaper(id, currentUser);
        return ResponseEntity.ok(Map.of("message", "Paper deleted successfully"));
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<QuestionPaper>> getPapersBySubject(@PathVariable Long subjectId) {
        List<QuestionPaper> papers = paperService.getPapersBySubject(subjectId);
        return ResponseEntity.ok(papers);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadPaper(@PathVariable Long id) {
        QuestionPaper paper = paperService.getPaperMetadata(id);
        Resource resource = paperService.getPaperResource(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(paper.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + paper.getOriginalName() + "\"")
                .body(resource);
    }
}