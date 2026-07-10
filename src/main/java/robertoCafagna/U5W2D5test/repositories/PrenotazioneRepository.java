package robertoCafagna.U5W2D5test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import robertoCafagna.U5W2D5test.entities.Prenotazione;

import java.time.LocalDate;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

    boolean existsByDipendenteIdAndDataPrenotazione(
            Long dipendenteId,
            LocalDate dataPrenotazione
    );

    boolean existsByDipendenteIdAndDataPrenotazioneAndIdNot(
            Long dipendenteId,
            LocalDate dataPrenotazione,
            Long id
    );
}
