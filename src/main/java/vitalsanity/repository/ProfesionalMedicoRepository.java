package vitalsanity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vitalsanity.model.Paciente;
import vitalsanity.model.ProfesionalMedico;
import vitalsanity.model.SolicitudAutorizacion;
import vitalsanity.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface ProfesionalMedicoRepository extends JpaRepository<ProfesionalMedico, Long> {
    Optional<ProfesionalMedico> findByUsuarioId(Long usuarioId);

    Optional<ProfesionalMedico> findBySolicitudesAutorizacion_Id(Long id);

}





