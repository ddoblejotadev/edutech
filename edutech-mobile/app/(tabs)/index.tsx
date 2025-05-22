import { Image } from 'expo-image';
import { Platform, StyleSheet, TouchableOpacity, View } from 'react-native';
import { router } from 'expo-router';
import { useEffect, useState } from 'react';

import { HelloWave } from '@/components/HelloWave';
import ParallaxScrollView from '@/components/ParallaxScrollView';
import { ThemedText } from '@/components/ThemedText';
import { ThemedView } from '@/components/ThemedView';
import { notificacionesApi } from '../../api/notificacionesService';
import { useThemeColor } from '../../hooks/useThemeColor';

export default function HomeScreen() {
  const [notificacionesCount, setNotificacionesCount] = useState(0);
  // Usuario simulado
  const rutUsuario = '12345678-9';
  
  const accentColor = useThemeColor({}, 'tint');
  
  useEffect(() => {
    cargarNotificacionesNoLeidas();
  }, []);
  
  const cargarNotificacionesNoLeidas = async () => {
    try {
      const response = await notificacionesApi.getNoLeidas(rutUsuario);
      setNotificacionesCount(response.data.length);
    } catch (error) {
      console.error('Error al cargar notificaciones no leídas:', error);
    }
  };

  return (
    <ParallaxScrollView
      headerBackgroundColor={{ light: '#A1CEDC', dark: '#1D3D47' }}
      headerImage={
        <Image
          source={require('@/assets/images/partial-react-logo.png')}
          style={styles.reactLogo}
        />
      }>
      <ThemedView style={styles.titleContainer}>
        <ThemedText type="title">¡Bienvenido a EduTech!</ThemedText>
        <HelloWave />
      </ThemedView>
      
      <ThemedView style={styles.statsContainer}>
        {notificacionesCount > 0 && (
          <TouchableOpacity 
            style={[styles.statCard, { backgroundColor: accentColor }]}
            onPress={() => router.push('/notificaciones')}
          >
            <ThemedText type="defaultSemiBold" style={styles.statText}>
              {notificacionesCount} Notificaciones nuevas
            </ThemedText>
          </TouchableOpacity>
        )}
      </ThemedView>
      
      <ThemedView style={styles.menuContainer}>
        <ThemedText type="subtitle">Mi Aprendizaje</ThemedText>
        
        <View style={styles.menuGrid}>
          <TouchableOpacity 
            style={styles.menuItem}
            onPress={() => router.push('/inscripciones')}
          >
            <View style={[styles.menuIcon, {backgroundColor: '#4A90E2'}]}>
              <ThemedText style={styles.menuIconText}>📚</ThemedText>
            </View>
            <ThemedText>Mis Cursos</ThemedText>
          </TouchableOpacity>
          
          <TouchableOpacity 
            style={styles.menuItem}
            onPress={() => router.push('/explore')}
          >
            <View style={[styles.menuIcon, {backgroundColor: '#50C878'}]}>
              <ThemedText style={styles.menuIconText}>🔍</ThemedText>
            </View>
            <ThemedText>Explorar Cursos</ThemedText>
          </TouchableOpacity>
          
          <TouchableOpacity 
            style={styles.menuItem}
            onPress={() => router.push('/calendario')}
          >
            <View style={[styles.menuIcon, {backgroundColor: '#FFA500'}]}>
              <ThemedText style={styles.menuIconText}>📅</ThemedText>
            </View>
            <ThemedText>Calendario</ThemedText>
          </TouchableOpacity>
          
          <TouchableOpacity 
            style={styles.menuItem}
            onPress={() => router.push('/perfil')}
          >
            <View style={[styles.menuIcon, {backgroundColor: '#9370DB'}]}>
              <ThemedText style={styles.menuIconText}>👤</ThemedText>
            </View>
            <ThemedText>Mi Perfil</ThemedText>
          </TouchableOpacity>
        </View>
      </ThemedView>
      
      <ThemedView style={styles.stepContainer}>
        <ThemedText type="subtitle">Sobre EduTech</ThemedText>
        <ThemedText>
          Plataforma educativa que te permite acceder a cursos y capacitaciones de manera sencilla y eficiente.
        </ThemedText>
      </ThemedView>
    </ParallaxScrollView>
  );
}
}

const styles = StyleSheet.create({
  titleContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
    marginBottom: 20,
  },
  stepContainer: {
    gap: 8,
    marginBottom: 20,
  },
  reactLogo: {
    height: 178,
    width: 290,
    bottom: 0,
    left: 0,
    position: 'absolute',
  },
  statsContainer: {
    marginBottom: 20,
  },
  statCard: {
    padding: 12,
    borderRadius: 8,
    marginBottom: 10,
  },
  statText: {
    color: 'white',
    textAlign: 'center',
  },
  menuContainer: {
    marginBottom: 20,
  },
  menuGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
    marginTop: 12,
  },
  menuItem: {
    width: '48%',
    alignItems: 'center',
    marginBottom: 16,
  },
  menuIcon: {
    width: 60,
    height: 60,
    borderRadius: 30,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 8,
  },
  menuIconText: {
    fontSize: 24,
  },
});
