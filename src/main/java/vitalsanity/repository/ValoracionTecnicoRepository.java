package vitalsanity.repository;

import vitalsanity.model.Incidencia;
import vitalsanity.model.ValoracionTecnico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ValoracionTecnicoRepository extends JpaRepository<ValoracionTecnico, Long> {
    List<ValoracionTecnico> findValoracionTecnicosByTecnicoId(Long id);

}

