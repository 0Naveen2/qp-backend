package PreviousYear.questions.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore; // <-- ADD THIS IMPORT
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String code;

    @Column(nullable = false)
    private int semester;

    // --- THIS IS THE FIX ---
    // This annotation tells the server to ignore this field when creating JSON,
    // which prevents the infinite loop.
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnore // <-- ADD THIS LINE
    private Course course;
}