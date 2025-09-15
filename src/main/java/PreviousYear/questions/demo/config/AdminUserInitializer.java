package PreviousYear.questions.demo.config;

import PreviousYear.questions.demo.entity.User;
import PreviousYear.questions.demo.entity.User.Role;
import PreviousYear.questions.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if an admin user already exists
        if (!userRepository.existsByRole(Role.ADMIN)) {
            // If not, create a new admin user with your credentials
            User admin = new User();
            admin.setUsername("Naveen09"); // <-- Changed
            admin.setPassword(passwordEncoder.encode("Naveen@7791")); // <-- Changed
            admin.setEmail("admin@example.com"); // You can change this email too
            admin.setRole(Role.ADMIN);
            admin.setVerified(true);

            userRepository.save(admin);
            System.out.println(">>> Created default admin user 'Naveen09'!");
        }
    }
}