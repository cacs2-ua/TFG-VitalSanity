package vitalsanity.repository;

import vitalsanity.model.EstadoIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import vitalsanity.model.EstadoPago;

import java.util.Optional;

public interface EstadoIncidenciaRepository extends JpaRepository<EstadoIncidencia, Long> {
    Optional<EstadoIncidencia> findByNombre(String nombre);
}
