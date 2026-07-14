package robertoCafagna.U5W2D5test.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import robertoCafagna.U5W2D5test.entities.Dipendente;
import robertoCafagna.U5W2D5test.exceptions.UnauthorizedException;
import robertoCafagna.U5W2D5test.services.DipendenteService;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {
    private final JWTTools jwtTools;
    private final DipendenteService dipendenteService;

    public TokenFilter(JWTTools jwtTools, DipendenteService dipendenteService) {
        this.jwtTools = jwtTools;
        this.dipendenteService = dipendenteService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("inserire il token del giusto formato");

        String accessToken = authHeader.replace("Bearer ", "");
        System.out.println(accessToken);

        this.jwtTools.verifyToken(accessToken);


        //1.
        Long dipendenteId = this.jwtTools.extractIdFromToken(accessToken);
        Dipendente found = this.dipendenteService.findById(dipendenteId);

        Authentication authentication = new UsernamePasswordAuthenticationToken(found, null, found.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        filterChain.doFilter(request, response);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());

    }
}
