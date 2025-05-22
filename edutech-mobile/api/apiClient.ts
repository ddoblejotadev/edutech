import axios from 'axios';
import { Platform } from 'react-native';
import * as SecureStore from 'expo-secure-store';

// Token key para el almacenamiento seguro
const TOKEN_KEY = 'edutech_auth_token';

// Función para obtener la URL del servidor en ambiente de desarrollo
const getDevelopmentBaseUrl = () => {
  if (Platform.OS === 'android') {
    // En el emulador de Android, localhost apunta al emulador, no a la máquina host
    return 'http://10.0.2.2:8080';
  } else if (Platform.OS === 'ios') {
    // En iOS el simulador comparte la red con la máquina host
    return 'http://localhost:8080';
  } else {
    // Para dispositivos físicos, usa variable de entorno o la IP configurada
    // IMPORTANTE: Cambiar esta IP a la IP local de tu máquina en la red actual
    return process.env.API_URL || 'http://192.168.1.3:8080';
  }
};

// Configuración de las URLs según el entorno
const API_URLS = {
  development: getDevelopmentBaseUrl(),
  production: 'https://api.edutech.com' // Cambiar por tu dominio de producción
};

// Determina qué URL usar
const getBaseUrl = () => {
  const environment = process.env.NODE_ENV || 'development';
  return environment === 'production' 
    ? API_URLS.production 
    : API_URLS.development;
};

// Crea el cliente Axios con la configuración
const apiClient = axios.create({
  baseURL: getBaseUrl(),
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

// Interceptor para manejar tokens de autenticación
apiClient.interceptors.request.use(
  async config => {
    // Agregar el token de autenticación si existe
    try {
      const token = await SecureStore.getItemAsync(TOKEN_KEY);
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    } catch (error) {
      console.error('Error al obtener el token:', error);
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// Interceptor para manejar respuestas y errores globalmente
apiClient.interceptors.response.use(
  response => {
    return response;
  },
  error => {
    // Manejo centralizado de errores
    console.error('API Error:', error.response?.data || error.message);
    
    // Aquí puedes manejar códigos de error específicos
    // if (error.response && error.response.status === 401) {
    //   // Redirigir al login si hay error de autenticación
    // }
    
    return Promise.reject(error);
  }
);

export default apiClient;
