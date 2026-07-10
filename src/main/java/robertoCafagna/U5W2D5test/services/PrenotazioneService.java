package robertoCafagna.U5W2D5test.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import robertoCafagna.U5W2D5test.DTO.PrenotazioneDTO;
import robertoCafagna.U5W2D5test.Enum.StatoViaggio;
import robertoCafagna.U5W2D5test.entities.Dipendente;
import robertoCafagna.U5W2D5test.entities.Prenotazione;
import robertoCafagna.U5W2D5test.entities.Viaggio;
import robertoCafagna.U5W2D5test.exceptions.BadRequestException;
import robertoCafagna.U5W2D5test.exceptions.NotFoundException;
import robertoCafagna.U5W2D5test.repositories.DipendenteRepository;
import robertoCafagna.U5W2D5test.repositories.PrenotazioneRepository;
import robertoCafagna.U5W2D5test.repositories.ViaggioRepository;

import java.time.LocalDate;

@Service
@Slf4j
public class PrenotazioneService {
    private final PrenotazioneRepository prenotazioneRepository;
    private final DipendenteService dipendenteService;
    private final ViaggioService viaggioService;

    public PrenotazioneService(PrenotazioneRepository prenotazioneRepository, DipendenteRepository dipendenteRepository, ViaggioRepository viaggioRepository, DipendenteService dipendenteService, ViaggioService viaggioService) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.dipendenteService = dipendenteService;
        this.viaggioService = viaggioService;
    }


    public Prenotazione save(PrenotazioneDTO body) {
        Dipendente dFromDB = this.dipendenteService.findById(body.dipendenteId());
        Viaggio vFromDB = this.viaggioService.findById(body.viaggioId());

        if (vFromDB.getStatoViaggio() == StatoViaggio.COMPLETATO) {
            throw new BadRequestException(
                    "Non è possibile prenotare un viaggio completato"
            );
        }

        if (prenotazioneRepository.existsByDipendenteIdAndViaggio_DataViaggio(
                body.dipendenteId(),
                vFromDB.getDataViaggio()
        )) {
            throw new BadRequestException(
                    "Il dipendente " + dFromDB.getUsername() +
                            " è già impegnato in un altro viaggio il " + vFromDB.getDataViaggio()
            );
        }
        Prenotazione newPrenotazione = new Prenotazione(body.note(), dFromDB, vFromDB);

        Prenotazione saved = this.prenotazioneRepository.save(newPrenotazione);

        log.info("La risorsa " + saved.getId() + " salvato");

        return saved;

    }

    public Page<Prenotazione> getAll(int page, int size, String orderBy) {
        if (size > 20) size = 20;
        if (size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.prenotazioneRepository.findAll(pageable);
    }

    public Prenotazione findById(Long prenotazioneId) {
        return this.prenotazioneRepository.findById(prenotazioneId).orElseThrow(() ->
                new NotFoundException(prenotazioneId));
    }

    public Prenotazione findByIdAndUpdate(Long prenotazioneId, PrenotazioneDTO body) {
        Prenotazione found = this.findById(prenotazioneId);
        Dipendente dFromDB = this.dipendenteService.findById(body.dipendenteId());
        Viaggio vFromDB = this.viaggioService.findById(body.viaggioId());

        if (vFromDB.getStatoViaggio() == StatoViaggio.COMPLETATO) {
            throw new BadRequestException(
                    "Non puoi modificare una prenotazione di un viaggio completato"
            );
        }
        if (prenotazioneRepository.existsByDipendenteIdAndViaggio_DataViaggioAndIdNot(
                body.dipendenteId(),
                vFromDB.getDataViaggio(),
                prenotazioneId
        )) {
            throw new BadRequestException(
                    "Il dipendente " + dFromDB.getUsername() +
                            " è già impegnato in un altro viaggio il " + vFromDB.getDataViaggio()
            );
        }


        found.setNote(body.note());
        found.setDipendente(dFromDB);
        found.setViaggio(vFromDB);

        Prenotazione update = this.prenotazioneRepository.save(found);

        return update;
    }

    public void findByIdAndDelete(Long prenotazioneId) {
        Prenotazione found = this.findById(prenotazioneId);

        if (found.getViaggio().getDataViaggio()
                .isBefore(LocalDate.now())) {

            throw new BadRequestException(
                    "Non puoi eliminare una prenotazione per un viaggio già passato"
            );
        }
        this.prenotazioneRepository.delete(found);
    }

}
