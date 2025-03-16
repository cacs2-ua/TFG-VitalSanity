package vitalsanity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vitalsanity.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findUsuarioById(Long id);

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByNifNie(String nifNie);


}
