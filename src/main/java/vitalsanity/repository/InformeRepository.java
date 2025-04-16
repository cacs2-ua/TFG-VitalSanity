package vitalsanity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vitalsanity.model.Informe;

import java.util.Optional;

public interface InformeRepository extends JpaRepository<Informe, Long> {

    boolean existsByUuid(String uuid);

    Optional<Informe> findByUuid(String uuid);
}
