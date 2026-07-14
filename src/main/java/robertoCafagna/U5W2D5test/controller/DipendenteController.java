package robertoCafagna.U5W2D5test.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import robertoCafagna.U5W2D5test.DTO.DipendenteDTO;
import robertoCafagna.U5W2D5test.DTO.DipendenteResponseDTO;
import robertoCafagna.U5W2D5test.DTO.PasswordChangeDTO;
import robertoCafagna.U5W2D5test.entities.Dipendente;
import robertoCafagna.U5W2D5test.exceptions.ValidationException;
import robertoCafagna.U5W2D5test.services.DipendenteService;

import java.util.List;

@RestController
@RequestMapping("/dipendenti")
public class DipendenteController {
    private final DipendenteService dipendenteService;

    public DipendenteController(DipendenteService dipendenteService) {
        this.dipendenteService = dipendenteService;
    }

    // 1. GET http://localhost:3001/dipendenti?page=1&size=3&orderBy=name --> 200 OK    ARRAY DI DIPENDENTI
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public Page<Dipendente> getUsers(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size,
                                     @RequestParam(defaultValue = "name") String orderBy) {
        return this.dipendenteService.getAll(page, size, orderBy);
    }

    @GetMapping("/me")
    public Dipendente getOwnProfile(@AuthenticationPrincipal Dipendente authDipendente) {
        return authDipendente;
    }


    @PutMapping("/me")
    public Dipendente updateOwnProfile(@AuthenticationPrincipal Dipendente authDipendente, @RequestBody DipendenteDTO body) {
        return this.dipendenteService.findAndUpdate(authDipendente.getId(), body);
    }


    @DeleteMapping("/me")
    public void deleteOwnProfile(@AuthenticationPrincipal Dipendente authDipendente) {
        this.dipendenteService.findAndDelete(authDipendente.getId());
    }

    @PatchMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(@AuthenticationPrincipal Dipendente authDipendente, @RequestBody PasswordChangeDTO body) {
        this.dipendenteService.updatePass(authDipendente.getId(), body);
    }

    // 2. POST http://localhost:3001/dipendenti (+req.body) --> 201 CREATED    ID DIPENDENTI APPENA CREATO
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public DipendenteResponseDTO saveDipendente(@Valid @RequestBody DipendenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new ValidationException(errorsList);
        }
        Dipendente saved = this.dipendenteService.save(body);
        return new DipendenteResponseDTO(saved.getId());
    }

    // 3. GET http://locahost:3001/dipendenti/{dipendenteId} --> 200 OK  DIPENDENTE TROVATO
    @GetMapping("/{dipendenteId}")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public Dipendente getById(@PathVariable Long dipendenteId) {
        return this.dipendenteService.findById(dipendenteId);
    }

    // 4. PUT http://localhost:3001/dipendenti/{dipendenteId} (+payload) --> 200 OK  DIPENDENTE AGGIORNATO
    @PutMapping("/{dipendenteId}")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public Dipendente findByIdAndUpdate(@PathVariable long dipendenteId, @Valid @RequestBody DipendenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new ValidationException(errorsList);
        }
        return this.dipendenteService.findAndUpdate(dipendenteId, body);
    }

    //5.DELETE http://localhost:3001/dipendenti/{dipendenteId} --> 204 NO CONTENT
    @DeleteMapping("/{dipendenteId}")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable long dipendenteId) {
        this.dipendenteService.findAndDelete(dipendenteId);
    }

    //6. PATCH (su avatar) http://localhost:3001/dipendenti/{dipendenteId}/avatar --> 200 OK  DIPENDENTE AGGIORNATO
    @PatchMapping("/{dipendenteId}/avatar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void uploadAvatar(@PathVariable Long dipendenteId,
                             @RequestParam("profile_picture") MultipartFile file) {
        this.dipendenteService.updatePic(dipendenteId, file);
    }
}
