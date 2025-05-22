import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { StatusBar } from 'expo-status-bar';

export default function App() {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>EduTech</Text>
      <Text style={styles.subtitle}>Plataforma de aprendizaje en línea</Text>
      
      <View style={styles.cardContainer}>
        <View style={styles.card}>
          <Text style={styles.cardTitle}>Cursos</Text>
          <Text style={styles.cardText}>Explora nuestra colección de cursos</Text>
        </View>
        
        <View style={styles.card}>
          <Text style={styles.cardTitle}>Notificaciones</Text>
          <Text style={styles.cardText}>Mantente al día con las novedades</Text>
        </View>
        
        <View style={styles.card}>
          <Text style={styles.cardTitle}>Perfil</Text>
          <Text style={styles.cardText}>Gestiona tu información personal</Text>
        </View>
      </View>
      
      <StatusBar style="auto" />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 8,
    color: '#00008B',
  },
  subtitle: {
    fontSize: 16,
    marginBottom: 30,
    color: '#555',
  },
  cardContainer: {
    width: '100%',
  },
  card: {
    backgroundColor: '#f5f5f5',
    padding: 20,
    borderRadius: 10,
    marginBottom: 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  cardTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 10,
    color: '#00008B',
  },
  cardText: {
    fontSize: 14,
    color: '#555',
  },
});
