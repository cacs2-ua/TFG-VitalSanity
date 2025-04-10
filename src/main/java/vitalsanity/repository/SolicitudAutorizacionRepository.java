package vitalsanity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vitalsanity.model.SolicitudAutorizacion;

public interface SolicitudAutorizacionRepository extends JpaRepository<SolicitudAutorizacion, Long> {
}
