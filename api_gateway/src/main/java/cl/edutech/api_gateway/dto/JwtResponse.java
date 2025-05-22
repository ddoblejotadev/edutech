package cl.edutech.api_gateway.dto;

/**
 * DTO para la respuesta después de la autenticación exitosa
 */
public class JwtResponse {
    private String token;
    private String type = "Bearer";

    // Constructores
    public JwtResponse(String token) {
        this.token = token;
    }

    // Getters y setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
