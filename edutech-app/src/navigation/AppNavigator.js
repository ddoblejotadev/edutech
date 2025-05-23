import React, { useContext } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { Ionicons } from '@expo/vector-icons';

// Importar el contexto de autenticación
import { AuthContext } from '../context/AuthContext';

// Importar las pantallas de autenticación
import LoginScreen from '../screens/auth/LoginScreen';
import RegisterScreen from '../screens/auth/RegisterScreen';

// Importar las pantallas principales
import HomeScreen from '../screens/home/HomeScreen';
import CoursesScreen from '../screens/courses/CoursesScreen';
import CourseDetailScreen from '../screens/courses/CourseDetailScreen';
import ProfileScreen from '../screens/profile/ProfileScreen';
import EvaluationScreen, { EvaluationResultScreen } from '../screens/evaluation/EvaluationScreen';

// Importar tema
import { COLORS, SPACING } from '../config/theme';

const Stack = createStackNavigator();
const Tab = createBottomTabNavigator();

// Navegador de pestañas para la aplicación principal
function MainTabNavigator() {
  return (
    <Tab.Navigator
      screenOptions={({ route }) => ({
        tabBarIcon: ({ focused, color, size }) => {
          let iconName;

          if (route.name === 'Home') {
            iconName = focused ? 'home' : 'home-outline';
          } else if (route.name === 'Courses') {
            iconName = focused ? 'book' : 'book-outline';
          } else if (route.name === 'Profile') {
            iconName = focused ? 'person' : 'person-outline';
          }

          return <Ionicons name={iconName} size={size} color={color} />;
        },
        tabBarActiveTintColor: COLORS.primary,
        tabBarInactiveTintColor: COLORS.lightText,
        tabBarStyle: {
          backgroundColor: COLORS.white,
          borderTopColor: COLORS.border,
          paddingTop: SPACING.xs,
          height: 60,
        },
        headerShown: false,
      })}
    >
      <Tab.Screen name="Home" component={HomeScreen} options={{ title: 'Inicio' }} />
      <Tab.Screen name="Courses" component={CoursesScreen} options={{ title: 'Cursos' }} />
      <Tab.Screen name="Profile" component={ProfileScreen} options={{ title: 'Perfil' }} />
    </Tab.Navigator>
  );
}

// Navegador principal de la aplicación
function AppNavigator() {
  const { isAuthenticated, loading } = useContext(AuthContext);

  // Mostrar pantalla de carga mientras se verifica la autenticación
  if (loading) {
    return (
      <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
        <ActivityIndicator size="large" color={COLORS.primary} />
      </View>
    );
  }

  return (
    <NavigationContainer>
      <Stack.Navigator>
        {isAuthenticated ? (
          // Rutas para usuarios autenticados
          <>
            <Stack.Screen 
              name="Main" 
              component={MainTabNavigator} 
              options={{ headerShown: false }} 
            />
            <Stack.Screen 
              name="CourseDetail" 
              component={CourseDetailScreen} 
              options={{ 
                title: 'Detalles del Curso',
                headerStyle: {
                  backgroundColor: COLORS.primary,
                },
                headerTintColor: COLORS.white,
                headerTitleStyle: {
                  fontWeight: 'bold',
                },
              }} 
            />
            <Stack.Screen 
              name="Evaluation" 
              component={EvaluationScreen} 
              options={{ 
                headerShown: false,
              }} 
            />
            <Stack.Screen 
              name="EvaluationResult" 
              component={EvaluationResultScreen} 
              options={{ 
                headerShown: false,
                gestureEnabled: false,
              }} 
            />
          </>
        ) : (
          // Rutas para usuarios no autenticados
          <>
            <Stack.Screen 
              name="Login" 
              component={LoginScreen} 
              options={{ headerShown: false }} 
            />
            <Stack.Screen 
              name="Register" 
              component={RegisterScreen} 
              options={{ 
                title: 'Registro',
                headerStyle: {
                  backgroundColor: COLORS.primary,
                },
                headerTintColor: COLORS.white,
              }} 
            />
          </>
        )}
      </Stack.Navigator>
    </NavigationContainer>
  );
}

// Importar componentes necesarios
import { View, ActivityIndicator } from 'react-native';

export default AppNavigator;
