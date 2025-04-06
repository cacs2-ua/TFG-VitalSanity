package vitalsanity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vitalsanity.model.CentroMedico;

import java.util.List;
import java.util.Optional;

public interface CentroMedicoRepository extends JpaRepository<CentroMedico, Long> {
}
