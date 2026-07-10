package robertoCafagna.U5W2D5test.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import robertoCafagna.U5W2D5test.DTO.DipendenteDTO;
import robertoCafagna.U5W2D5test.entities.Dipendente;
import robertoCafagna.U5W2D5test.exceptions.BadRequestException;
import robertoCafagna.U5W2D5test.exceptions.NotFoundException;
import robertoCafagna.U5W2D5test.exceptions.ValidationException;
import robertoCafagna.U5W2D5test.repositories.DipendenteRepository;
import robertoCafagna.U5W2D5test.repositories.PrenotazioneRepository;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class DipendenteService {
    private final DipendenteRepository dipendenteRepository;
    private final Cloudinary fileUploader;
    private final PrenotazioneRepository prenotazioneRepository;

    public DipendenteService(DipendenteRepository dipendenteRepository, Cloudinary fileUploader, PrenotazioneRepository prenotazioneRepository) {
        this.dipendenteRepository = dipendenteRepository;
        this.fileUploader = fileUploader;
        this.prenotazioneRepository = prenotazioneRepository;
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
        if (prenotazioneRepository.existsByDipendenteId(dipendenteId)) {
            throw new BadRequestException(
                    "Non puoi eliminare un viaggio con prenotazioni associate"
            );
        }
        this.dipendenteRepository.delete(found);
    }


    public void updatePic(Long dipendentiId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new ValidationException("inserire un file");
        }
        if (file.getSize() >= 10 * 1024 * 1024) {
            throw new ValidationException("file di dimensione troppo grande, inserire un file di massimo 10MB");
        }

        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equals("image/jpeg") &&
                        !contentType.equals("image/png") &&
                        !contentType.equals("image/gif"))) {

            throw new ValidationException("Formato file non supportato");
        }

        Dipendente found = this.findById(dipendentiId);

        try {
            Map res = fileUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = res.get("secure_url").toString();
            System.out.println(url);

            found.setAvatar(url);
            dipendenteRepository.save(found);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
