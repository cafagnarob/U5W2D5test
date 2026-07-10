package robertoCafagna.U5W2D5test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import robertoCafagna.U5W2D5test.entities.Viaggio;

import java.time.LocalDate;

@Repository
public interface ViaggioRepository extends JpaRepository<Viaggio, Long> {
    boolean existsByDestinazioneAndDataViaggio(
            String destinazione,
            LocalDate dataViaggio
    );

}
