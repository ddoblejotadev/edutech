package cl.edutech.api_gateway.controller;

import cl.edutech.api_gateway.dto.LoginRequest;
import cl.edutech.api_gateway.dto.JwtResponse;
import cl.edutech.api_gateway.security.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para manejar la autenticación y generar tokens JWT
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * Endpoint de login que autentica y devuelve un token JWT
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        // Autenticar usando el AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Actualizar el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Generar el token JWT
        String jwt = jwtTokenUtil.generateToken(authentication);
        
        // Devolver el token en la respuesta
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
