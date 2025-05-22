import React from 'react';
import { StyleSheet, Image, TouchableOpacity, View } from 'react-native';
import { ThemedText } from '@/components/ThemedText';
import { ThemedView } from '@/components/ThemedView';
import { Stack, useRouter } from 'expo-router';
import { useColorScheme } from '@/hooks/useColorScheme';
import { Colors } from '@/constants/Colors';
import { IconSymbol } from '@/components/ui/IconSymbol';
import { useAuth } from '@/contexts/AuthContext';

export default function PerfilScreen() {
  const router = useRouter();
  const colorScheme = useColorScheme();
  const colors = Colors[colorScheme ?? 'light'];
  const { user } = useAuth();

  return (
    <ThemedView style={styles.container}>
      <Stack.Screen
        options={{
          title: 'Mi Perfil',
          headerShown: true,
          headerStyle: {
            backgroundColor: colors.background,
          },
          headerTintColor: colors.text,
        }}
      />
      
      <View style={styles.profileHeader}>
        <Image
          source={{ uri: user?.avatar || 'https://via.placeholder.com/150' }}
          style={styles.avatar}
        />
        <ThemedText style={styles.name}>
          {user ? `${user.nombres} ${user.apellidos}` : 'Usuario Ejemplo'}
        </ThemedText>
        <ThemedText style={styles.email}>
          {user ? user.email : 'usuario@ejemplo.com'}
        </ThemedText>
        <ThemedText style={styles.role}>
          {user ? user.rol : 'Estudiante'}
        </ThemedText>
      </View>
      
      <View style={styles.optionsContainer}>
        <TouchableOpacity style={styles.option}>
          <IconSymbol name="person.fill" size={20} color={colors.text} />
          <ThemedText style={styles.optionText}>Editar perfil</ThemedText>
        </TouchableOpacity>
        
        <TouchableOpacity style={styles.option}>
          <IconSymbol name="lock.fill" size={20} color={colors.text} />
          <ThemedText style={styles.optionText}>Cambiar contraseña</ThemedText>
        </TouchableOpacity>
        
        <TouchableOpacity style={styles.option}>
          <IconSymbol name="gear" size={20} color={colors.text} />
          <ThemedText style={styles.optionText}>Configuración</ThemedText>
        </TouchableOpacity>
        
        <TouchableOpacity 
          style={[styles.option, styles.logoutOption]}
          onPress={() => router.push('/(auth)/login')}
        >
          <IconSymbol name="arrow.right.square" size={20} color={colors.error || 'red'} />
          <ThemedText style={[styles.optionText, { color: colors.error || 'red' }]}>
            Cerrar sesión
          </ThemedText>
        </TouchableOpacity>
      </View>
    </ThemedView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  profileHeader: {
    alignItems: 'center',
    padding: 20,
    paddingTop: 30,
  },
  avatar: {
    width: 100,
    height: 100,
    borderRadius: 50,
    marginBottom: 16,
  },
  name: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 4,
  },
  email: {
    fontSize: 16,
    marginBottom: 4,
    opacity: 0.8,
  },
  role: {
    fontSize: 14,
    opacity: 0.6,
    marginBottom: 16,
  },
  optionsContainer: {
    flex: 1,
    padding: 20,
  },
  option: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 16,
    borderBottomWidth: 1,
    borderBottomColor: 'rgba(150, 150, 150, 0.2)',
  },
  optionText: {
    fontSize: 16,
    marginLeft: 12,
  },
  logoutOption: {
    marginTop: 20,
    borderBottomWidth: 0,
  },
});