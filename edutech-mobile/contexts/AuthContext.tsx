import { useState, useEffect, createContext, useContext } from 'react';
import { router, useSegments } from 'expo-router';
import { authService } from '../api/authService';

// Tipo para el usuario autenticado
interface User {
  rut: string;
  nombres: string;
  apellidos: string;
  email: string;
  roles: string[];
  rol?: string;
  avatar?: string;
}

// Tipo para el contexto de autenticación
interface AuthContextType {
  user: User | null;
  isLoading: boolean;
  signIn: (username: string, password: string) => Promise<void>;
  signOut: () => Promise<void>;
  register: (userData: any) => Promise<void>;
  error: string | null;
}

// Crear el contexto de autenticación
const AuthContext = createContext<AuthContextType>({
  user: null,
  isLoading: false,
  signIn: async () => {},
  signOut: async () => {},
  register: async () => {},
  error: null
});

// Hook para usar el contexto de autenticación
export function useAuth() {
  return useContext(AuthContext);
}

// Provider para el contexto de autenticación
export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const segments = useSegments();

  // Verificar si el usuario está autenticado al iniciar la app
  useEffect(() => {
    const loadUser = async () => {
      setIsLoading(true);
      try {
        const isAuthenticated = await authService.isAuthenticated();
        if (isAuthenticated) {
          const userData = await authService.getUserData();
          setUser(userData);
        }
      } catch (error) {
        console.error('Error al cargar usuario:', error);
      } finally {
        setIsLoading(false);
      }
    };

    loadUser();
  }, []);

  // Redirigir según el estado de autenticación
  useEffect(() => {
    if (!isLoading) {
      const inAuthGroup = segments[0] === '(auth)';
      
      if (!user && !inAuthGroup) {
        // Si no hay usuario y no estamos en el grupo de autenticación, redirigir al login
        router.replace('/login');
      } else if (user && inAuthGroup) {
        // Si hay usuario y estamos en el grupo de autenticación, redirigir al inicio
        router.replace('/');
      }
    }
  }, [user, segments, isLoading]);
  // Función para iniciar sesión
  const signIn = async (username: string, password: string) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await authService.login({ username, password });
      // Añadir propiedad rol y avatar al usuario
      const userWithExtras = {
        ...response.usuario,
        rol: response.usuario.roles[0] || 'Estudiante',
        avatar: 'https://via.placeholder.com/150' // Avatar por defecto
      };
      setUser(userWithExtras);
    } catch (error: any) {
      setError(error.message || 'Error al iniciar sesión');
      console.error('Error al iniciar sesión:', error);
    } finally {
      setIsLoading(false);
    }
  };

  // Función para cerrar sesión
  const signOut = async () => {
    setIsLoading(true);
    try {
      await authService.logout();
      setUser(null);
    } catch (error) {
      console.error('Error al cerrar sesión:', error);
    } finally {
      setIsLoading(false);
    }
  };

  // Función para registrarse
  const register = async (userData: any) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await authService.register(userData);
      setUser(response.usuario);
    } catch (error: any) {
      setError(error.message || 'Error al registrarse');
      console.error('Error al registrarse:', error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        isLoading,
        signIn,
        signOut,
        register,
        error
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}
