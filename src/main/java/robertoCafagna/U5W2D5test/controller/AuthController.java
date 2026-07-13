package robertoCafagna.U5W2D5test.controller;


import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import robertoCafagna.U5W2D5test.DTO.DipendenteDTO;
import robertoCafagna.U5W2D5test.DTO.DipendenteResponseDTO;
import robertoCafagna.U5W2D5test.DTO.LoginDTO;
import robertoCafagna.U5W2D5test.DTO.LoginResponseDTO;
import robertoCafagna.U5W2D5test.entities.Dipendente;
import robertoCafagna.U5W2D5test.exceptions.ValidationException;
import robertoCafagna.U5W2D5test.services.AuthService;
import robertoCafagna.U5W2D5test.services.DipendenteService;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final DipendenteService dipendenteService;


    public AuthController(AuthService authService, DipendenteService dipendenteService) {
        this.authService = authService;
        this.dipendenteService = dipendenteService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {
        return new LoginResponseDTO(this.authService.checkAndGenerate(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public DipendenteResponseDTO saveDipendente(@RequestBody @Validated DipendenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            throw new ValidationException(errorsList);
        }
        Dipendente saved = this.dipendenteService.save(body);
        return new DipendenteResponseDTO(saved.getId());
    }
}
