package PreviousYear.questions.demo.repository;

import PreviousYear.questions.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    // Add this new method definition inside the UserRepository interface

    Optional<User> findByVerificationToken(String token);

    // Add this new method to the interface
    boolean existsByRole(User.Role role);
}
