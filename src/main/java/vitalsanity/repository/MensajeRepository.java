// repository/ComercioRepository.java

package vitalsanity.repository;

import vitalsanity.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

}
