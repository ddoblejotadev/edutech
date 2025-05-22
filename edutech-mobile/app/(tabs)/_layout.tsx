import { Tabs } from 'expo-router';
import { useColorScheme } from 'react-native';
import { Colors } from '@/constants/Colors';
import { IconSymbol } from '@/components/ui/IconSymbol';

export default function TabLayout() {
  const colorScheme = useColorScheme();
  const colors = Colors[colorScheme ?? 'light'];

  return (
    <Tabs
      screenOptions={{
        tabBarActiveTintColor: colors.tint,
        tabBarInactiveTintColor: colors.tabIconDefault,
        tabBarStyle: {
          borderTopWidth: 1,
          borderTopColor: colors.cardBorder, // Usar cardBorder en lugar de border
          elevation: 0,
          shadowOpacity: 0,
        },
        headerShown: false,
      }}
    >
      <Tabs.Screen
        name="index"
        options={{
          title: 'Inicio',
          tabBarIcon: ({ color }) => <IconSymbol name="house.fill" color={color} size={22} />,
        }}
      />
      <Tabs.Screen
        name="explore"
        options={{
          title: 'Explorar',
          tabBarIcon: ({ color }) => <IconSymbol name="safari.fill" color={color} size={22} />,
        }}
      />
      <Tabs.Screen
        name="inscripciones"
        options={{
          title: 'Mis Cursos',
          tabBarIcon: ({ color }) => <IconSymbol name="book.fill" color={color} size={22} />,
        }}
      />
      <Tabs.Screen
        name="calendario"
        options={{
          title: 'Calendario',
          tabBarIcon: ({ color }) => <IconSymbol name="calendar" color={color} size={22} />,
        }}
      />
      <Tabs.Screen
        name="perfil"
        options={{
          title: 'Perfil',
          tabBarIcon: ({ color }) => <IconSymbol name="person.fill" color={color} size={22} />,
        }}
      />
      <Tabs.Screen
        name="notificaciones"
        options={{
          title: 'Notificaciones',
          tabBarIcon: ({ color }) => <IconSymbol name="bell.fill" color={color} size={22} />,
          tabBarButton: () => null,  // Esto oculta la pestaña pero mantiene la ruta accesible
        }}
      />
    </Tabs>
  );
}
