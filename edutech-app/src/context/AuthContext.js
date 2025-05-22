import React, { createContext, useState, useEffect } from 'react';
import { getUserInfo, getToken, login, logout, register } from '../services/authService';

export const AuthContext = createContext({
  isAuthenticated: false,
  user: null,
  login: () => {},
  logout: () => {},
  register: () => {},
  loading: true
});

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Verificar si el usuario ya está autenticado al cargar la aplicación
    const loadUserFromStorage = async () => {
      try {
        const token = await getToken();
        if (token) {
          const userInfo = await getUserInfo();
          setUser(userInfo);
        }
      } catch (error) {
        console.error('Error al cargar información del usuario:', error);
      } finally {
        setLoading(false);
      }
    };

    loadUserFromStorage();
  }, []);

  const handleLogin = async (username, password) => {
    try {
      const userData = await login(username, password);
      setUser({
        id: userData.id,
        username: userData.username,
        email: userData.email,
        roles: userData.roles,
      });
      return { success: true };
    } catch (error) {
      console.error('Error en handleLogin:', error);
      return { 
        success: false, 
        error: error.response?.data?.message || 'Error de autenticación'
      };
    }
  };

  const handleLogout = async () => {
    try {
      await logout();
      setUser(null);
      return { success: true };
    } catch (error) {
      console.error('Error en handleLogout:', error);
      return { success: false, error: 'Error al cerrar sesión' };
    }
  };

  const handleRegister = async (username, email, password) => {
    try {
      await register(username, email, password);
      // Al registrarse exitosamente, iniciamos sesión con las credenciales proporcionadas
      return await handleLogin(username, password);
    } catch (error) {
      console.error('Error en handleRegister:', error);
      return { 
        success: false, 
        error: error.response?.data?.message || 'Error en el registro'
      };
    }
  };

  return (
    <AuthContext.Provider
      value={{
        isAuthenticated: !!user,
        user,
        login: handleLogin,
        logout: handleLogout,
        register: handleRegister,
        loading
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};
