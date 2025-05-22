import axios from 'axios';
import * as SecureStore from 'expo-secure-store';
import { API_URL, AUTH_ENDPOINTS } from '../config/api';

// Función para iniciar sesión
export const login = async (username, password) => {
  try {
    const response = await axios.post(`${API_URL}${AUTH_ENDPOINTS.LOGIN}`, {
      username,
      password,
    });
    
    // Guardar el token en el almacenamiento seguro
    await SecureStore.setItemAsync('userToken', response.data.token);
    await SecureStore.setItemAsync('userInfo', JSON.stringify({
      id: response.data.id,
      username: response.data.username,
      email: response.data.email,
      roles: response.data.roles,
    }));
    
    return response.data;
  } catch (error) {
    console.error('Error en login:', error);
    throw error;
  }
};

// Función para registrar un nuevo usuario
export const register = async (username, email, password) => {
  try {
    const response = await axios.post(`${API_URL}${AUTH_ENDPOINTS.REGISTER}`, {
      username,
      email,
      password,
      roles: ["student"], // Por defecto, asignamos el rol de estudiante
    });
    
    return response.data;
  } catch (error) {
    console.error('Error en registro:', error);
    throw error;
  }
};

// Función para cerrar sesión
export const logout = async () => {
  try {
    await SecureStore.deleteItemAsync('userToken');
    await SecureStore.deleteItemAsync('userInfo');
    return true;
  } catch (error) {
    console.error('Error al cerrar sesión:', error);
    return false;
  }
};

// Función para obtener el token del almacenamiento
export const getToken = async () => {
  try {
    return await SecureStore.getItemAsync('userToken');
  } catch (error) {
    console.error('Error al obtener token:', error);
    return null;
  }
};

// Función para obtener información del usuario
export const getUserInfo = async () => {
  try {
    const userInfo = await SecureStore.getItemAsync('userInfo');
    return userInfo ? JSON.parse(userInfo) : null;
  } catch (error) {
    console.error('Error al obtener info de usuario:', error);
    return null;
  }
};
