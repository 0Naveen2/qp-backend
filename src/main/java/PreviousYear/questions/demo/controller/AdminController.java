package PreviousYear.questions.demo.controller;

import PreviousYear.questions.demo.dto.AdminCreateUserRequest;
import PreviousYear.questions.demo.entity.User;
import PreviousYear.questions.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin") // Base path for all admin endpoints
@CrossOrigin(origins = "*")
public class AdminController {

    private final AuthService authService;

    @Autowired
    public AdminController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')") // This ensures only admins can call this method
    public ResponseEntity<?> createUser(@RequestBody AdminCreateUserRequest request) {
        try {
            User newUser = authService.createUserByAdmin(request);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}