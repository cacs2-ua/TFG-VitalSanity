package vitalsanity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vitalsanity.model.SolicitudAutorizacion;

import java.util.Optional;

public interface SolicitudAutorizacionRepository extends JpaRepository<SolicitudAutorizacion, Long> {
    Optional<SolicitudAutorizacion> findTopByProfesionalMedicoIdOrderByIdDesc(Long profesionalMedicoId);
}
