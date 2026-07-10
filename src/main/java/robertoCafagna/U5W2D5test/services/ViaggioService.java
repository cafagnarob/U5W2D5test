package robertoCafagna.U5W2D5test.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import robertoCafagna.U5W2D5test.DTO.StatoViaggioDTO;
import robertoCafagna.U5W2D5test.DTO.ViaggioDTO;
import robertoCafagna.U5W2D5test.Enum.StatoViaggio;
import robertoCafagna.U5W2D5test.entities.Viaggio;
import robertoCafagna.U5W2D5test.exceptions.BadRequestException;
import robertoCafagna.U5W2D5test.exceptions.NotFoundException;
import robertoCafagna.U5W2D5test.repositories.ViaggioRepository;

import java.time.LocalDate;

import static robertoCafagna.U5W2D5test.Enum.StatoViaggio.COMPLETATO;

@Service
@Slf4j
public class ViaggioService {
    private final ViaggioRepository viaggioRepository;

    public ViaggioService(ViaggioRepository viaggioRepository) {
        this.viaggioRepository = viaggioRepository;
    }

    public Viaggio save(ViaggioDTO body) {
        if (body.dataViaggio().isBefore(LocalDate.now())) {
            throw new BadRequestException(
                    "Non è possibile creare un viaggio nel passato"
            );
        }

        if (viaggioRepository.existsByDestinazioneAndDataViaggio(
                body.destinazione().trim().toLowerCase(),
                body.dataViaggio())) {

            throw new BadRequestException(
                    "Esiste già un viaggio per questa destinazione nella stessa data"
            );
        }

        Viaggio newViaggio = new Viaggio(body.destinazione(), body.dataViaggio());

        Viaggio saved = this.viaggioRepository.save(newViaggio);

        log.info("La risorsa " + saved.getId() + " salvato");

        return saved;

    }

    public Page<Viaggio> getAll(int page, int size, String orderBy) {
        if (size > 20) size = 20;
        if (size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.viaggioRepository.findAll(pageable);
    }

    public Viaggio findById(Long viaggioId) {
        return this.viaggioRepository.findById(viaggioId).orElseThrow(() -> new NotFoundException(viaggioId));
    }

    public Viaggio findByIdAndUpdate(Long viaggioId, ViaggioDTO body) {
        Viaggio found = this.findById(viaggioId);
        

        found.setDataViaggio(body.dataViaggio());
        found.setDestinazione(body.destinazione());

        Viaggio update = this.viaggioRepository.save(found);

        return update;
    }

    public void findByIdAndDelete(Long viaggioId) {
        Viaggio found = this.findById(viaggioId);
        this.viaggioRepository.delete(found);
    }

    public Viaggio updateStato(Long viaggioId, StatoViaggioDTO body) {
        Viaggio found = this.findById(viaggioId);
        if (found.getStatoViaggio() == COMPLETATO &&
                body.statoViaggio().equals("IN_PROGRAMMA")) {
            throw new BadRequestException(
                    "Non puoi riaprire un viaggio completato"
            );
        }
        found.setStatoViaggio(StatoViaggio.valueOf(body.statoViaggio()));
        Viaggio update = this.viaggioRepository.save(found);
        return update;
    }

}
