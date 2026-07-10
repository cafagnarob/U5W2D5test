package robertoCafagna.U5W2D5test.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import robertoCafagna.U5W2D5test.DTO.PrenotazioneDTO;
import robertoCafagna.U5W2D5test.DTO.PrenotazioneResponseDTO;
import robertoCafagna.U5W2D5test.entities.Prenotazione;
import robertoCafagna.U5W2D5test.exceptions.ValidationException;
import robertoCafagna.U5W2D5test.services.PrenotazioneService;

import java.util.List;

@RestController
@RequestMapping("/prenotazione")
public class PrenotazioneController {
    private final PrenotazioneService prenotazioneService;

    public PrenotazioneController(PrenotazioneService prenotazioneService) {
        this.prenotazioneService = prenotazioneService;
    }

    // 1. GET http://localhost:3001/prenotazioni?page=2&size=10&orderBy=nome --> 200 OK    ARRAY DI PRENOTAZIONI
    @GetMapping
    public Page<Prenotazione> getUsers(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       @RequestParam(defaultValue = "dataPrenotazione") String orderBy) {
        return this.prenotazioneService.getAll(page, size, orderBy);
    }

    // 2. POST http://locahost:3001/prenotazioni (+req.body) --> 201 CREATED    ID PRENOTAZIONE APPENA CREATO
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public PrenotazioneResponseDTO savePrenotazione(@Valid @RequestBody PrenotazioneDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new ValidationException(errorsList);
        }
        Prenotazione saved = this.prenotazioneService.save(body);
        return new PrenotazioneResponseDTO(saved.getId());
    }

    // 3. GET http://locahost:3001/prenotazioni/{prenotazioneId} --> 200 OK  PRENOTAZIONE TROVATA
    @GetMapping("/{prenotazioneId}")
    public Prenotazione getById(@PathVariable Long prenotazioneId) {
        return this.prenotazioneService.findById(prenotazioneId);
    }

    // 4. PUT http://localhost:3001/prenotazioni/{prenotazioneId} (+payload) --> 200 OK  PRENOTAZIONE AGGIORNATO
    @PutMapping("/{prenotazioneId}")
    public Prenotazione findByIdAndUpdate(@PathVariable long prenotazioneId, @Valid @RequestBody PrenotazioneDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new ValidationException(errorsList);
        }
        return this.prenotazioneService.findByIdAndUpdate(prenotazioneId, body);
    }

    //5.DELETE http://localhost:3001/prenotazioni/{prenotazioneId} --> 204 NO CONTENT
    @DeleteMapping("/{prenotazioneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable long prenotazioneId) {
        this.prenotazioneService.findByIdAndDelete(prenotazioneId);
    }


}
