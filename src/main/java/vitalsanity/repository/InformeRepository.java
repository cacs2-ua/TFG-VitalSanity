package vitalsanity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vitalsanity.model.Informe;

public interface InformeRepository extends JpaRepository<Informe, Long> {

    boolean existsByUuid(String uuid);
}
