package robertoCafagna.U5W2D5test.services;

import org.springframework.stereotype.Service;
import robertoCafagna.U5W2D5test.DTO.LoginDTO;
import robertoCafagna.U5W2D5test.entities.Dipendente;
import robertoCafagna.U5W2D5test.exceptions.UnauthorizedException;
import robertoCafagna.U5W2D5test.security.JWTTools;

@Service
public class AuthService {
    private final DipendenteService dipendenteService;
    private final JWTTools jwtTools;

    public AuthService(DipendenteService dipendenteService, JWTTools jwtTools) {
        this.dipendenteService = dipendenteService;
        this.jwtTools = jwtTools;
    }

    public String checkAndGenerate(LoginDTO body) {
        Dipendente found = this.dipendenteService.findByEmail(body.email());

        if (found.getPassword().equals(body.password())) {
            return this.jwtTools.generateToken(found);
        } else {
            throw new UnauthorizedException("errore nelle credenziali");
        }


    }
}
