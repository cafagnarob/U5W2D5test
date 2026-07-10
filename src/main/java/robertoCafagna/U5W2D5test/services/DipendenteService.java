package robertoCafagna.U5W2D5test.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import robertoCafagna.U5W2D5test.entities.Dipendente;
import robertoCafagna.U5W2D5test.repositories.DipendenteRepository;

@Service
@Slf4j
public class DipendenteService {
    private final DipendenteRepository dipendenteRepository;

    public DipendenteService(DipendenteRepository dipendenteRepository) {
        this.dipendenteRepository = dipendenteRepository;
    }


    public Dipendente save()
}
