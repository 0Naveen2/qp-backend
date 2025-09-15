package PreviousYear.questions.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "question_papers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionPaper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // semester 1-8
    @Column(nullable = false)
    private int semester;

    // year of exam (e.g., 2023)
    @Column(nullable = false)
    private int year;

    // exam session (e.g., "Nov/Dec", "Apr/May")
    @Column(nullable = false)
    private String session;

    // set number (A, B, C)
    private String setNumber;

    // stored file name (UUID.pdf)
    @Column(nullable = false)
    private String fileName;

    // original name uploaded
    @Column(nullable = false)
    private String originalName;

    private long fileSize;

    private String contentType;

    private LocalDateTime uploadedAt;

    // This says many papers can belong to one subject
    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    // link to uploader (only that user can delete it, admin can delete all)
    @ManyToOne
    @JoinColumn(name = "uploaded_by", nullable = false)
    private User uploadedBy;
}
