import React, { useContext } from 'react';
import { View, ActivityIndicator } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { Ionicons } from '@expo/vector-icons';

import { AuthContext } from '../context/AuthContext';
import { COLORS, SPACING } from '../config/theme';

// Pantallas de autenticación
import LoginScreen from '../screens/auth/LoginScreen';
import RegisterScreen from '../screens/auth/RegisterScreen';

// Pantallas principales
import SimpleHomeScreen from '../screens/home/SimpleHomeScreen';
import CoursesScreen from '../screens/courses/CoursesScreen';
import ProfileScreen from '../screens/profile/ProfileScreen';
import ServicesScreen from '../screens/services/ServicesScreen';

// Pantallas de cursos
import CourseDetailScreen from '../screens/courses/CourseDetailScreen';

// Pantallas académicas
import ScheduleScreen from '../screens/academic/ScheduleScreen';
import GradesScreen from '../screens/academic/GradesScreen';

// Pantallas de servicios
import AcademicHistoryScreen from '../screens/services/AcademicHistoryScreen';
import UniversityCardScreen from '../screens/services/UniversityCardScreen';
import AcademicTranscriptScreen from '../screens/services/AcademicTranscriptScreen';
import ExamRegistrationScreen from '../screens/services/ExamRegistrationScreen';
import ScholarshipsScreen from '../screens/services/ScholarshipsScreen';
import SocialServiceScreen from '../screens/services/SocialServiceScreen';

// Pantallas de perfil
import FinancialStatusScreen from '../screens/profile/FinancialStatusScreen';

const Stack = createStackNavigator();
const Tab = createBottomTabNavigator();

// Navegador de pestañas para la aplicación principal
function MainTabNavigator() {
  return (
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
            case 'Services':
              iconName = focused ? 'briefcase' : 'briefcase-outline';
              break;
            case 'Profile':
              iconName = focused ? 'person' : 'person-outline';
              break;
            default:
              iconName = 'circle';
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
      <Tab.Screen
        name="Home"
        component={SimpleHomeScreen}
        options={{ title: 'Inicio' }}
      />
      <Tab.Screen
        name="Courses"
        component={CoursesScreen}
        options={{ title: 'Cursos' }}
      />
      <Tab.Screen
        name="Services"
        component={ServicesScreen}
        options={{ title: 'Servicios' }}
      />
      <Tab.Screen
        name="Profile"
        component={ProfileScreen}
        options={{ title: 'Perfil' }}
      />
    </Tab.Navigator>
  );
}

// Navegador principal de la aplicación
function AppNavigator() {
  const { isAuthenticated, loading } = useContext(AuthContext);

  // Mostrar pantalla de carga mientras se verifica la autenticación
  if (loading) {
    return (
      <View
        style={{
          flex: 1,
          justifyContent: 'center',
          alignItems: 'center',
          backgroundColor: COLORS.white,
        }}
      >
        <ActivityIndicator size="large" color={COLORS.primary} />
      </View>
    );
  }

  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={{ headerShown: false }}>
        {isAuthenticated ? (
          // Rutas para usuarios autenticados
          <React.Fragment>
            <Stack.Screen name="Main" component={MainTabNavigator} />
            <Stack.Screen name="CourseDetail" component={CourseDetailScreen} />
            <Stack.Screen name="Schedule" component={ScheduleScreen} />
            <Stack.Screen name="Grades" component={GradesScreen} />
            <Stack.Screen name="AcademicHistory" component={AcademicHistoryScreen} />
            <Stack.Screen name="UniversityCard" component={UniversityCardScreen} />
            <Stack.Screen name="AcademicTranscript" component={AcademicTranscriptScreen} />
            <Stack.Screen name="ExamRegistration" component={ExamRegistrationScreen} />
            <Stack.Screen name="Scholarships" component={ScholarshipsScreen} />
            <Stack.Screen name="SocialService" component={SocialServiceScreen} />
            <Stack.Screen name="FinancialStatus" component={FinancialStatusScreen} />
          </React.Fragment>
        ) : (
          // Rutas para usuarios no autenticados
          <React.Fragment>
            <Stack.Screen name="Login" component={LoginScreen} />
            <Stack.Screen name="Register" component={RegisterScreen} />
          </React.Fragment>
        )}
      </Stack.Navigator>
    </NavigationContainer>
  );
}

export default AppNavigator;
