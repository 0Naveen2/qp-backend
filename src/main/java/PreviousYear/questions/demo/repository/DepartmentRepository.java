package PreviousYear.questions.demo.repository;

import PreviousYear.questions.demo.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByName(String name);

    Optional<Department> findByCode(String code);
}
