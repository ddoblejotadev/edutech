import React, { useContext } from 'react';
import { createStackNavigator } from '@react-navigation/stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { Ionicons } from '@expo/vector-icons';
import { AuthContext } from '../context/AuthContext';

// Pantallas de autenticación
import LoginScreen from '../screens/auth/LoginScreen';
import RegisterScreen from '../screens/auth/RegisterScreen';

// Pantallas principales
import HomeScreen from '../screens/HomeScreen';
import CoursesScreen from '../screens/courses/CoursesScreen';
import CourseDetailsScreen from '../screens/courses/CourseDetailsScreen';
import ProfileScreen from '../screens/profile/ProfileScreen';
import NotificationsScreen from '../screens/NotificationsScreen';

// Nuevas pantallas académicas
import CalendarScreen from '../screens/CalendarScreen';
import GradesScreen from '../screens/academic/GradesScreen';
import ScheduleScreen from '../screens/academic/ScheduleScreen';
import ServicesScreen from '../screens/services/ServicesScreen';

const Stack = createStackNavigator();
const Tab = createBottomTabNavigator();

// Navegador para la sección de cursos
const CoursesNavigator = () => (
  <Stack.Navigator screenOptions={{ headerShown: false }}>
    <Stack.Screen name="CoursesList" component={CoursesScreen} />
    <Stack.Screen name="CourseDetails" component={CourseDetailsScreen} />
  </Stack.Navigator>
);

// Navegador de pestañas para usuarios autenticados
const TabNavigator = () => (
  <Tab.Navigator
    screenOptions={({ route }) => ({
      tabBarIcon: ({ focused, color, size }) => {
        let iconName;
        switch (route.name) {
          case 'Home':
            iconName = focused ? 'home' : 'home-outline';
            break;
          case 'Courses':
            iconName = focused ? 'book' : 'book-outline';
            break;
          case 'Schedule':
            iconName = focused ? 'calendar' : 'calendar-outline';
            break;
          case 'Grades':
            iconName = focused ? 'school' : 'school-outline';
            break;
          case 'Services':
            iconName = focused ? 'cog' : 'cog-outline';
            break;
          case 'Notifications':
            iconName = focused ? 'notifications' : 'notifications-outline';
            break;
          case 'Profile':
            iconName = focused ? 'person' : 'person-outline';
            break;
        }
        return <Ionicons name={iconName} size={size} color={color} />;
      },
      tabBarActiveTintColor: '#00008B',
      tabBarInactiveTintColor: 'gray',
    })}>
    <Tab.Screen name="Home" component={HomeScreen} options={{ title: 'Inicio' }} />
    <Tab.Screen name="Courses" component={CoursesNavigator} options={{ title: 'Cursos', headerShown: false }} />
    <Tab.Screen name="Schedule" component={ScheduleScreen} options={{ title: 'Horario' }} />
    <Tab.Screen name="Grades" component={GradesScreen} options={{ title: 'Calificaciones' }} />
    <Tab.Screen name="Services" component={ServicesScreen} options={{ title: 'Servicios' }} />
    <Tab.Screen name="Notifications" component={NotificationsScreen} options={{ title: 'Notificaciones' }} />
    <Tab.Screen name="Profile" component={ProfileScreen} options={{ title: 'Perfil' }} />
  </Tab.Navigator>
);

// Navegador principal que muestra login o contenido según el estado de autenticación
const MainNavigator = () => {
  const { isAuthenticated, loading } = useContext(AuthContext);

  if (loading) {
    // Podrías mostrar una pantalla de carga aquí
    return null;
  }

  return (
    <Stack.Navigator>
      {!isAuthenticated ? (
        // Pantallas para usuarios no autenticados
        <>
          <Stack.Screen name="Login" component={LoginScreen} options={{ headerShown: false }} />
          <Stack.Screen name="Register" component={RegisterScreen} options={{ headerShown: false }} />
        </>
      ) : (
        // Pantalla principal para usuarios autenticados
        <Stack.Screen 
          name="Main" 
          component={TabNavigator} 
          options={{ headerShown: false }} 
        />
      )}
    </Stack.Navigator>
  );
};

export default MainNavigator;
