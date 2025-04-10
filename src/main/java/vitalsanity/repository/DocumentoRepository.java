package vitalsanity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vitalsanity.model.Documento;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {
}
