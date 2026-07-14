package robertoCafagna.U5W2D5test.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import robertoCafagna.U5W2D5test.DTO.LoginDTO;
import robertoCafagna.U5W2D5test.entities.Dipendente;
import robertoCafagna.U5W2D5test.exceptions.UnauthorizedException;
import robertoCafagna.U5W2D5test.security.JWTTools;

@Service
public class AuthService {
    private final DipendenteService dipendenteService;
    private final JWTTools jwtTools;
    private final PasswordEncoder bcrypt;

    public AuthService(DipendenteService dipendenteService, JWTTools jwtTools, PasswordEncoder bcrypt) {
        this.dipendenteService = dipendenteService;
        this.jwtTools = jwtTools;
        this.bcrypt = bcrypt;
    }

    public String checkAndGenerate(LoginDTO body) {
        Dipendente found = this.dipendenteService.findByEmail(body.email());

        if (this.bcrypt.matches(body.password(), found.getPassword())) {
            return this.jwtTools.generateToken(found);
        } else {
            throw new UnauthorizedException("errore nelle credenziali");
        }
        
    }
}
