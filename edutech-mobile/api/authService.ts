import apiClient from './apiClient';
import * as SecureStore from 'expo-secure-store';

// Tipos para autenticación
interface LoginCredentials {
  username: string;
  password: string;
}

interface AuthResponse {
  token: string;
  usuario: {
    rut: string;
    nombres: string;
    apellidos: string;
    email: string;
    roles: string[];
  };
}

// Clase de error personalizada para el servicio de autenticación
export class AuthError extends Error {
  statusCode: number;
  data?: any;

  constructor(message: string, statusCode: number = 500, data?: any) {
    super(message);
    this.name = 'AuthError';
    this.statusCode = statusCode;
    this.data = data;
  }
}

// Nombre de las claves para almacenamiento seguro
const TOKEN_KEY = 'edutech_auth_token';
const USER_KEY = 'edutech_user_data';

// Servicio de autenticación
export const authService = {
  // Iniciar sesión
  login: async (credentials: LoginCredentials): Promise<AuthResponse> => {
    try {
      const response = await apiClient.post('/api/auth/login', credentials);
      const authData = response.data;
      
      // Guardar token y datos de usuario en almacenamiento seguro
      await SecureStore.setItemAsync(TOKEN_KEY, authData.token);
      await SecureStore.setItemAsync(USER_KEY, JSON.stringify(authData.usuario));
      
      return authData;
    } catch (error: any) {
      if (error.response?.status === 401) {
        throw new AuthError('Credenciales inválidas', 401);
      }
      throw new AuthError(
        'Error en el inicio de sesión',
        error.response?.status,
        error.response?.data
      );
    }
  },
  
  // Cerrar sesión
  logout: async (): Promise<void> => {
    try {
      // Eliminar token y datos de usuario del almacenamiento seguro
      await SecureStore.deleteItemAsync(TOKEN_KEY);
      await SecureStore.deleteItemAsync(USER_KEY);
    } catch (error) {
      console.error('Error al cerrar sesión:', error);
    }
  },
  
  // Verificar si el usuario está autenticado
  isAuthenticated: async (): Promise<boolean> => {
    try {
      const token = await SecureStore.getItemAsync(TOKEN_KEY);
      return token !== null;
    } catch (error) {
      return false;
    }
  },
  
  // Obtener el token actual
  getToken: async (): Promise<string | null> => {
    try {
      return await SecureStore.getItemAsync(TOKEN_KEY);
    } catch (error) {
      return null;
    }
  },
  
  // Obtener datos del usuario actual
  getUserData: async (): Promise<any | null> => {
    try {
      const userData = await SecureStore.getItemAsync(USER_KEY);
      return userData ? JSON.parse(userData) : null;
    } catch (error) {
      return null;
    }
  },
  
  // Registrar un nuevo usuario
  register: async (userData: any): Promise<AuthResponse> => {
    try {
      const response = await apiClient.post('/api/auth/register', userData);
      return response.data;
    } catch (error: any) {
      if (error.response?.status === 400) {
        throw new AuthError('Datos de registro inválidos', 400, error.response?.data);
      }
      throw new AuthError(
        'Error al registrar usuario',
        error.response?.status,
        error.response?.data
      );
    }
  }
};
