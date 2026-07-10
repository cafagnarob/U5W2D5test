package robertoCafagna.U5W2D5test.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import robertoCafagna.U5W2D5test.DTO.StatoViaggioDTO;
import robertoCafagna.U5W2D5test.DTO.ViaggioDTO;
import robertoCafagna.U5W2D5test.DTO.ViaggioResponseDTO;
import robertoCafagna.U5W2D5test.entities.Viaggio;
import robertoCafagna.U5W2D5test.exceptions.ValidationException;
import robertoCafagna.U5W2D5test.services.ViaggioService;

import java.util.List;

@RestController
@RequestMapping("/viaggi")
public class ViaggioController {
    private final ViaggioService viaggioService;

    public ViaggioController(ViaggioService viaggioService) {
        this.viaggioService = viaggioService;
    }

    // 1. GET http://localhost:3001/viaggi?page=2&size=10&orderBy=nome --> 200 OK    ARRAY DI VIAGGI
    @GetMapping
    public Page<Viaggio> getUsers(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam(defaultValue = "dataViaggio") String orderBy) {
        return this.viaggioService.getAll(page, size, orderBy);
    }

    // 2. POST http://locahost:3001/viaggi (+req.body) --> 201 CREATED    ID VIAGGIO APPENA CREATO
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public ViaggioResponseDTO saveViaggio(@Valid @RequestBody ViaggioDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new ValidationException(errorsList);
        }
        Viaggio saved = this.viaggioService.save(body);
        return new ViaggioResponseDTO(saved.getId());
    }

    // 3. GET http://locahost:3001/viaggi/{viaggioId} --> 200 OK  VIAGGIO TROVATO
    @GetMapping("/{viaggioId}")
    public Viaggio getById(@PathVariable Long viaggioId) {
        return this.viaggioService.findById(viaggioId);
    }

    // 4. PUT http://localhost:3001/viaggi/{viaggioId} (+payload) --> 200 OK  VIAGGIO AGGIORNATO
    @PutMapping("/{viaggioId}")
    public Viaggio findByIdAndUpdate(@PathVariable long viaggioId, @Valid @RequestBody ViaggioDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new ValidationException(errorsList);
        }
        return this.viaggioService.findByIdAndUpdate(viaggioId, body);
    }

    //5.DELETE http://localhost:3001/viaggi/{viaggioId} --> 204 NO CONTENT
    @DeleteMapping("/{viaggioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable long viaggioId) {
        this.viaggioService.findByIdAndDelete(viaggioId);
    }

    //6. PATCH (su statoViaggio)  http://localhost:3001/viaggi/{viaggioId}/stato --> 200 OK  VIAGGIO AGGIORNATO
    @PatchMapping("/{viaggioId}/stato")
    public Viaggio updateStato(
            @PathVariable Long viaggioId, @Valid @RequestBody StatoViaggioDTO body) {
        return viaggioService.updateStato(viaggioId, body);
    }
}
