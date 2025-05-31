import React, { createContext, useState, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { API_URL, DEMO_MODE } from '../config/api';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    checkAuthState();
  }, []);

  const checkAuthState = async () => {
    try {
      // Limpiar siempre el storage al iniciar para evitar sesiones persistentes en desarrollo
      await AsyncStorage.removeItem('token');
      await AsyncStorage.removeItem('user');
      
      setToken(null);
      setUser(null);
      setIsAuthenticated(false);
    } catch (error) {
      console.error('Error checking auth state:', error);
    } finally {
      setLoading(false);
    }
  };

  const login = async (email, password) => {
    try {
      console.log('🔐 Intentando iniciar sesión...');
      setError(null);
      
      // Siempre usar modo demo
      console.log('📱 Modo demo activado');
      
      // Simulación de demora para hacer más realista
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      // Verificar credenciales demo
      if (email === 'juan.perez@alumno.edu' && password === 'demo123') {
        const demoUser = {
          id: 1,
          name: 'Juan Pérez García',
          email: 'juan.perez@estudiante.uchile.cl',
          studentId: '21.123.456-7',
          career: 'Ingeniería Civil en Computación',
          role: 'student'
        };
        
        const demoToken = 'demo-token-123';
        
        // Guardar en AsyncStorage
        await AsyncStorage.setItem('token', demoToken);
        await AsyncStorage.setItem('user', JSON.stringify(demoUser));
        
        setToken(demoToken);
        setUser(demoUser);
        setIsAuthenticated(true);
        
        console.log('✅ Login exitoso (modo demo)');
        return {
          success: true,
          user: demoUser,
          token: demoToken
        };
      } else {
        const errorMsg = 'Credenciales incorrectas. Usa: juan.perez@alumno.edu / demo123';
        console.log('❌ Credenciales incorrectas');
        setError(errorMsg);
        return {
          success: false,
          error: errorMsg
        };
      }
      
    } catch (error) {
      console.error('Error en login:', error);
      const errorMsg = 'Error en autenticación. Inténtalo de nuevo.';
      
      setError(errorMsg);
      return {
        success: false,
        error: errorMsg
      };
    }
  };

  const logout = async () => {
    try {
      await AsyncStorage.removeItem('token');
      await AsyncStorage.removeItem('user');
      
      setToken(null);
      setUser(null);
      setIsAuthenticated(false);
      setError(null);
      
      console.log('👋 Sesión cerrada');
    } catch (error) {
      console.error('Error al cerrar sesión:', error);
    }
  };

  const clearError = () => {
    setError(null);
  };

  const contextValue = {
    isAuthenticated,
    user,
    token,
    loading,
    error,
    login,
    logout,
    clearError
  };

  return (
    <AuthContext.Provider value={contextValue}>
      {children}
    </AuthContext.Provider>
  );
};
