package cl.edutech.api_gateway.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para cargar los detalles del usuario desde el microservicio de personas
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Consultar el microservicio de personas para obtener datos del usuario
        try {
            // Esta URL debe coincidir con la configuración en microservicios.yml
            String userServiceUrl = "http://localhost:8081/api/personas/auth/" + username;
            
            // Llamada al servicio de personas para obtener la información del usuario
            Map<String, Object> userData = restTemplate.getForObject(userServiceUrl, Map.class);
            
            if (userData == null) {
                throw new UsernameNotFoundException("Usuario no encontrado: " + username);
            }
            
            // Extraer los datos necesarios para construir un UserDetails
            String rut = (String) userData.get("rut");
            String password = (String) userData.get("password"); // Debe estar hasheada en BD
            List<String> roles = (List<String>) userData.getOrDefault("roles", Collections.emptyList());
            
            // Convertir roles a GrantedAuthority
            List<GrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
            
            // Construir y retornar el objeto UserDetails
            return new User(rut, password, authorities);
            
        } catch (Exception e) {
            throw new UsernameNotFoundException("Error al cargar usuario: " + e.getMessage(), e);
        }
    }
}
