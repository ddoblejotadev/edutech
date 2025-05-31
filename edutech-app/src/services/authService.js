// Este archivo está obsoleto - la autenticación se maneja en AuthContext
import { API_URL, DEMO_MODE } from '../config/api';

// Función legacy para compatibilidad
export const authService = {
  login: async (email, password) => {
    if (DEMO_MODE) {
      // Credenciales demo
      const validateCredentials = (email, password) => {
        // Verificar en datos demo
        const user = DEMO_USERS.find(u => 
          (u.email === email || u.username === email) && u.password === password
        );
        
        if (user) {
          return {
            success: true,
            user: {
              id: user.id,
              name: user.name,
              email: user.email,
              rut: user.rut,
              role: user.role,
              sede: user.sede,
              carrera: user.carrera
            },
            token: 'demo-token-' + user.id
          };
        }

        // Credenciales demo adicionales para compatibilidad
        const demoCredentials = [
          {
            email: 'carlos.mendoza@duocuc.cl',
            password: 'duoc2024',
            user: {
              id: 1,
              name: 'Carlos Andrés Mendoza Vargas',
              email: 'carlos.mendoza@duocuc.cl',
              rut: '19.234.567-8',
              role: 'student',
              sede: 'Plaza Vespucio',
              carrera: 'Ingeniería en Informática'
            }
          }
        ];

        const demoUser = demoCredentials.find(cred => 
          cred.email === email && cred.password === password
        );

        if (demoUser) {
          return {
            success: true,
            user: demoUser.user,
            token: 'demo-token-' + demoUser.user.id
          };
        }

        return {
          success: false,
          message: 'Credenciales incorrectas. Usa: carlos.mendoza@duocuc.cl / duoc2024'
        };
      };

      const result = validateCredentials(email, password);
      return result;
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
