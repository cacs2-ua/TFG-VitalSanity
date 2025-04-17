package vitalsanity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vitalsanity.model.Informe;

import java.util.List;
import java.util.Optional;

public interface InformeRepository extends JpaRepository<Informe, Long> {

    boolean existsByUuid(String uuid);

    Optional<Informe> findByUuid(String uuid);

    /**
     * Devuelve todos los informes del paciente cuyo id es :pacienteId
     * y que han sido emitidos por profesionales que ese mismo paciente
     * tiene en su lista de autorizados, con todas las relaciones precargadas:
     * Paciente → Usuario,
     * ProfesionalMedico → EspecialidadMedica, Usuario, CentroMedico → Usuario.
     */
    @Query("""
        SELECT DISTINCT i
          FROM Informe i
          JOIN FETCH i.paciente p
          JOIN FETCH p.usuario pu
          JOIN FETCH i.profesionalMedico pr
          JOIN FETCH pr.especialidadMedica em
          JOIN FETCH pr.usuario pru
          JOIN FETCH pr.centroMedico cm
          JOIN FETCH cm.usuario cmu
         WHERE p.id = :pacienteId
           AND pr IN (
               SELECT pm
                 FROM Paciente p2
                 JOIN p2.profesionalesMedicosAutorizados pm
                WHERE p2.id = :pacienteId
           )
        """)
    List<Informe> findAllByPacienteIdAndProfesionalesMedicosAutorizados(
            @Param("pacienteId") Long pacienteId
    );


}
