// Este archivo está obsoleto - la autenticación se maneja en AuthContext
import { API_URL, DEMO_MODE } from '../config/api';

// Función legacy para compatibilidad
export const authService = {
  login: async (email, password) => {
    if (DEMO_MODE) {
      // Credenciales demo
      if (email === 'juan.perez@alumno.edu' && password === 'demo123') {
        return {
          success: true,
          token: 'demo-token-123',
          user: {
            id: 1,
            name: 'Juan Pérez García',
            email: 'juan.perez@alumno.edu',
            role: 'student'
          }
        };
      }
      return {
        success: false,
        error: 'Credenciales incorrectas'
      };
    }

    try {
      const response = await fetch(`${API_URL}/auth/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, password })
      });

      const data = await response.json();
      return {
        success: response.ok,
        token: data.token,
        user: data.user,
        error: data.message
      };
    } catch (error) {
      return {
        success: false,
        error: 'Error de conexión'
      };
    }
  }
};
