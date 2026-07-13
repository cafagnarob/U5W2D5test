package robertoCafagna.U5W2D5test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import robertoCafagna.U5W2D5test.entities.Dipendente;

import java.util.Optional;

@Repository
public interface DipendenteRepository extends JpaRepository<Dipendente, Long> {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<Dipendente> findByEmail(String email);
    
}
