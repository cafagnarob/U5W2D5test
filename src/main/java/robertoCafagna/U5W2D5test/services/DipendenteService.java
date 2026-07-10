package robertoCafagna.U5W2D5test.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import robertoCafagna.U5W2D5test.DTO.DipendenteDTO;
import robertoCafagna.U5W2D5test.entities.Dipendente;
import robertoCafagna.U5W2D5test.exceptions.BadRequestException;
import robertoCafagna.U5W2D5test.exceptions.NotFoundException;
import robertoCafagna.U5W2D5test.repositories.DipendenteRepository;

@Service
@Slf4j
public class DipendenteService {
    private final DipendenteRepository dipendenteRepository;

    public DipendenteService(DipendenteRepository dipendenteRepository) {
        this.dipendenteRepository = dipendenteRepository;
    }


    public Dipendente save(DipendenteDTO body) {
        if (this.dipendenteRepository.existsByEmail(body.email())) {
            throw new BadRequestException("L'indirizzo email " + body.email() + " è già utilizzato!");
        }
        if (this.dipendenteRepository.existsByUsername(body.username())) {
            throw new BadRequestException("Lo username " + body.username() + " è già utilizzato!");
        }
        Dipendente newDipendente = new Dipendente(body.name().trim(),
                body.surname().trim(),
                body.username().trim(),
                body.email().trim().toLowerCase());

        Dipendente saved = this.dipendenteRepository.save(newDipendente);

        log.info("la risorsa " + saved.getId() + " salvato");

        return saved;
    }

    public Page<Dipendente> getAll(int page, int size, String orderBy) {
        if (size > 20) size = 20;
        if (size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.dipendenteRepository.findAll(pageable);
    }

    public Dipendente findById(Long dipendenteId) {
        return this.dipendenteRepository.findById(dipendenteId).orElseThrow(() -> new NotFoundException(dipendenteId));
    }


    public Dipendente findAndUpdate(Long dipendenteId, DipendenteDTO body) {
        Dipendente found = this.findById(dipendenteId);

        if (!found.getEmail().equals(body.email()))
            if (this.dipendenteRepository.existsByEmail(body.email())) {

                throw new BadRequestException("L'indirizzo email " + body.email() + " è già utilizzato!");
            }

        if (!found.getUsername().equals(body.username()))
            if (this.dipendenteRepository.existsByUsername(body.username())) {

                throw new BadRequestException("L'username " + body.username() + " è già utilizzato!");
            }

        found.setName(body.name().trim());
        found.setSurname(body.surname().trim());
        found.setUsername(body.username().trim());
        found.setEmail(body.email().trim().toLowerCase());

        Dipendente update = this.dipendenteRepository.save(found);
        return update;

    }


    public void findAndDelete(Long dipendenteId) {
        Dipendente found = this.findById(dipendenteId);
        this.dipendenteRepository.delete(found);
    }


}
