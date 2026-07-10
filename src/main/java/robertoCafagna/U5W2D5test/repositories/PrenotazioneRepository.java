package robertoCafagna.U5W2D5test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import robertoCafagna.U5W2D5test.entities.Prenotazione;

import java.time.LocalDate;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {


    boolean existsByDipendenteIdAndViaggio_DataViaggio(Long dipendenteId, LocalDate dataViaggio);

    boolean existsByDipendenteIdAndViaggio_DataViaggioAndIdNot(
            Long dipendenteId,
            LocalDate dataViaggio,
            Long id
    );
}
