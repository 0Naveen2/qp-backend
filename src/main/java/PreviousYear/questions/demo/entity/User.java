package PreviousYear.questions.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime; // <-- Add this import

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    public enum Role {
        STUDENT, ADMIN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // --- NEW FIELDS FOR EMAIL VERIFICATION ---

    @Column(nullable = false)
    private boolean verified = false; // Default to not verified

    private String verificationToken;

    private LocalDateTime tokenExpiryDate;

    // --- END OF NEW FIELDS ---
}