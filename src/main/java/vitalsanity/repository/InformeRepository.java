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
     * tiene en su lista de autorizados.
     */
    @Query("""
        SELECT i
          FROM Informe i
         WHERE i.paciente.id = :pacienteId
           AND i.profesionalMedico IN (
               SELECT pm
                 FROM Paciente p
                 JOIN p.profesionalesMedicosAutorizados pm
                WHERE p.id = :pacienteId
           )
        """)
    List<Informe> findAllByPacienteIdAndProfesionalesMedicosAutorizados(
            @Param("pacienteId") Long pacienteId
    );


}
