import React, { useContext } from 'react';
import { View, StyleSheet, ScrollView, TouchableOpacity } from 'react-native';
import { Text, Card, Title, Paragraph, Button } from 'react-native-paper';
import { AuthContext } from '../context/AuthContext';

const HomeScreen = ({ navigation }) => {
  const { user } = useContext(AuthContext);
  
  // Datos de ejemplo (reemplazar con datos reales de la API)
  const featuredCourses = [
    { id: 1, title: 'Introducción a React Native', category: 'Desarrollo móvil', level: 'Principiante', image: null },
    { id: 2, title: 'Bases de datos avanzadas', category: 'Base de datos', level: 'Avanzado', image: null },
    { id: 3, title: 'Diseño UX/UI', category: 'Diseño', level: 'Intermedio', image: null },
  ];
  
  const announcements = [
    { id: 1, title: 'Nuevo curso disponible', date: '2 de junio, 2025', content: 'Hemos lanzado un nuevo curso de Inteligencia Artificial.' },
    { id: 2, title: 'Mantenimiento programado', date: '5 de junio, 2025', content: 'La plataforma estará en mantenimiento el domingo de 2am a 5am.' },
  ];
  
  const handleCoursePress = (courseId) => {
    navigation.navigate('Courses', {
      screen: 'CourseDetails',
      params: { courseId }
    });
  };
  
  return (
    <ScrollView style={styles.container}>
      <View style={styles.welcomeSection}>
        <Text style={styles.welcomeText}>Hola, {user?.name || 'Estudiante'}</Text>
        <Text style={styles.welcomeSubtext}>¿Qué vas a aprender hoy?</Text>
      </View>
      
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Continuar aprendiendo</Text>
        <Card style={styles.continueLearningCard}>
          <Card.Content>
            <Title>Desarrollo web con Spring Boot</Title>
            <Paragraph>Progreso: 60%</Paragraph>
            <View style={styles.progressBar}>
              <View style={[styles.progressFill, { width: '60%' }]} />
            </View>
          </Card.Content>
          <Card.Actions>
            <Button mode="contained" onPress={() => handleCoursePress(4)}>Continuar</Button>
          </Card.Actions>
        </Card>
      </View>
      
      <View style={styles.section}>
        <View style={styles.sectionHeader}>
          <Text style={styles.sectionTitle}>Cursos destacados</Text>
          <TouchableOpacity onPress={() => navigation.navigate('Courses')}>
            <Text style={styles.seeAllText}>Ver todos</Text>
          </TouchableOpacity>
        </View>
        
        <ScrollView horizontal showsHorizontalScrollIndicator={false}>
          {featuredCourses.map(course => (
            <Card 
              key={course.id} 
              style={styles.courseCard}
              onPress={() => handleCoursePress(course.id)}
            >
              <Card.Content>
                <Title style={styles.courseTitle}>{course.title}</Title>
                <Paragraph>{course.category}</Paragraph>
                <View style={styles.courseLevel}>
                  <Text style={styles.courseLevelText}>{course.level}</Text>
                </View>
              </Card.Content>
            </Card>
          ))}
        </ScrollView>
      </View>
      
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Anuncios importantes</Text>
        {announcements.map(announcement => (
          <Card key={announcement.id} style={styles.announcementCard}>
            <Card.Content>
              <Title>{announcement.title}</Title>
              <Text style={styles.dateText}>{announcement.date}</Text>
              <Paragraph>{announcement.content}</Paragraph>
            </Card.Content>
          </Card>
        ))}
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  welcomeSection: {
    backgroundColor: '#00008B',
    padding: 20,
    paddingTop: 40,
  },
  welcomeText: {
    fontSize: 24,
    fontWeight: 'bold',
    color: 'white',
  },
  welcomeSubtext: {
    fontSize: 16,
    color: 'rgba(255, 255, 255, 0.8)',
    marginTop: 5,
  },
  section: {
    padding: 16,
    marginBottom: 10,
  },
  sectionHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 10,
  },
  seeAllText: {
    color: '#00008B',
    fontWeight: 'bold',
  },
  continueLearningCard: {
    marginBottom: 10,
    elevation: 2,
  },
  progressBar: {
    height: 10,
    backgroundColor: '#E0E0E0',
    borderRadius: 5,
    marginTop: 10,
    marginBottom: 10,
  },
  progressFill: {
    height: 10,
    backgroundColor: '#00008B',
    borderRadius: 5,
  },
  courseCard: {
    width: 200,
    marginRight: 10,
    elevation: 2,
  },
  courseTitle: {
    fontSize: 16,
  },
  courseLevel: {
    marginTop: 10,
    backgroundColor: 'rgba(0, 0, 139, 0.1)',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 4,
    alignSelf: 'flex-start',
  },
  courseLevelText: {
    color: '#00008B',
    fontSize: 12,
    fontWeight: 'bold',
  },
  announcementCard: {
    marginBottom: 10,
    elevation: 2,
  },
  dateText: {
    fontSize: 12,
    color: 'gray',
    marginBottom: 5,
  },
});

export default HomeScreen;
