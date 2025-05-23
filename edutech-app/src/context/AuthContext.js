import React, { createContext, useState, useEffect } from 'react';
import * as SecureStore from 'expo-secure-store';
import { Auth } from '../services/apiService';

export const AuthContext = createContext({
  isAuthenticated: false,
  user: null,
  token: null,
  login: () => {},
  logout: () => {},
  register: () => {},
  loading: true,
  error: null
});

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [token, setToken] = useState(null);
  const [error, setError] = useState(null);

  // Limpiar el error después de 5 segundos
  useEffect(() => {
    if (error) {
      const timeout = setTimeout(() => {
        setError(null);
      }, 5000);
      return () => clearTimeout(timeout);
    }
  }, [error]);

  useEffect(() => {
    // Cargar usuario desde almacenamiento local al iniciar
    const loadUser = async () => {
      try {
        const storedToken = await SecureStore.getItemAsync('userToken');
        const storedUser = await SecureStore.getItemAsync('userData');
        
        if (storedToken && storedUser) {
          setToken(storedToken);
          setUser(JSON.parse(storedUser));
        }
      } catch (error) {
        console.error('Error cargando datos del usuario:', error);
        setError('No se pudieron cargar los datos de usuario.');
      } finally {
        setLoading(false);
      }
    };

    loadUser();
  }, []);

  const handleLogin = async (username, password) => {
    setLoading(true);
    setError(null);
    try {
      // Usar el servicio de API para iniciar sesión
      const response = await Auth.login(username, password);
      
      if (response.token && response.user) {
        // Guardar datos en almacenamiento seguro
        await SecureStore.setItemAsync('userToken', response.token);
        await SecureStore.setItemAsync('userData', JSON.stringify(response.user));
        
        setToken(response.token);
        setUser(response.user);
        return { success: true };
      } else {
        throw new Error('Respuesta de autenticación inválida');
      }
    } catch (error) {
      console.error('Error en handleLogin:', error);
      setError(error.message || 'Error de autenticación');
      return { 
        success: false, 
        error: error.message || 'Error de autenticación'
      };
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = async () => {
    setLoading(true);
    try {
      await SecureStore.deleteItemAsync('userToken');
      await SecureStore.deleteItemAsync('userData');
      setUser(null);
      setToken(null);
      return { success: true };
    } catch (error) {
      console.error('Error en handleLogout:', error);
      setError('Error al cerrar sesión');
      return { success: false, error: 'Error al cerrar sesión' };
    } finally {
      setLoading(false);
    }
  };

  const handleRegister = async (username, email, password, name) => {
    setLoading(true);
    setError(null);
    try {
      // Usar el servicio de API para registrar usuario
      const response = await Auth.register({
        username,
        email,
        password,
        name
      });
      
      if (response.success) {
        // Iniciar sesión automáticamente después del registro
        return await handleLogin(username, password);
      }
      
      throw new Error(response.message || 'Error en el registro');
    } catch (error) {
      console.error('Error en handleRegister:', error);
      setError(error.message || 'Error en el registro');
      return { 
        success: false, 
        error: error.message || 'Error en el registro'
      };
    } finally {
      setLoading(false);
    }
  };
  return (
    <AuthContext.Provider
      value={{
        isAuthenticated: !!user,
        user,
        token,
        login: handleLogin,
        logout: handleLogout,
        register: handleRegister,
        loading,
        error
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};
