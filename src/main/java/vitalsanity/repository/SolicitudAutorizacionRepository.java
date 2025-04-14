package vitalsanity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vitalsanity.model.ProfesionalMedico;
import vitalsanity.model.SolicitudAutorizacion;

import java.util.List;
import java.util.Optional;

public interface SolicitudAutorizacionRepository extends JpaRepository<SolicitudAutorizacion, Long> {
    Optional<SolicitudAutorizacion> findTopByProfesionalMedicoIdOrderByIdDesc(Long profesionalMedicoId);

    Optional<SolicitudAutorizacion> findTopByProfesionalMedicoIdAndPacienteIdOrderByIdDesc(Long profesionalMedicoId, Long pacienteId);

    List<SolicitudAutorizacion> findByPacienteIdAndDenegadaFalse(Long pacienteId);





}
