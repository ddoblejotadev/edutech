import React, { useState, useEffect, useCallback } from 'react';
import { View, ScrollView, TouchableOpacity, ActivityIndicator, StyleSheet } from 'react-native';
import { Image } from 'expo-image';
import { useLocalSearchParams, router } from 'expo-router';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { Ionicons } from '@expo/vector-icons';

import { ThemedText } from '../../components/ThemedText';
import { ThemedView } from '../../components/ThemedView';
import { Button } from '../../components/ui/Button';
import { Colors } from '../../constants/Colors';
import { useColorScheme } from '../../hooks/useColorScheme';
import { useAuth } from '../../contexts/AuthContext';

interface Clase {
  id: string;
  titulo: string;
  duracion: string;
  tipo: string;
}

interface Modulo {
  id: string;
  titulo: string;
  clases: Clase[];
}

interface Curso {
  id: string;
  titulo: string;
  descripcion: string;
  imagen: string;
  instructor: string;
  categoria: string;
  fechaInicio: string;
  duracion: string;
  nivel: string;
  precio: number;
  calificacion: number;
  estudiantes: number;
  modulos: Modulo[];
}

// Datos del curso de ejemplo
const COURSE_DATA: Curso = {
  id: '1',
  titulo: 'Diseño UX/UI Avanzado',
  descripcion: 'En este curso aprenderás a diseñar interfaces de usuario modernas y atractivas, siguiendo las mejores prácticas de UX. Dominarás herramientas de diseño como Figma y aprenderás a realizar pruebas de usabilidad para mejorar la experiencia del usuario.\n\nEl curso incluye proyectos prácticos donde aplicarás los conocimientos adquiridos creando prototipos interactivos y diseñando interfaces para distintos dispositivos.',
  imagen: 'https://images.unsplash.com/photo-1522542550221-31fd19575a2d?ixlib=rb-4.0.3&auto=format&fit=crop&w=1470&q=80',
  instructor: 'Ana García',
  categoria: 'Diseño',
  fechaInicio: '2025-06-15',
  duracion: '8 semanas',
  nivel: 'Avanzado',
  precio: 89000,
  calificacion: 4.8,
  estudiantes: 235,
  modulos: [
    {
      id: '1',
      titulo: 'Fundamentos de UX/UI',
      clases: [
        { id: '1-1', titulo: 'Introducción al diseño UX/UI', duracion: '45 min', tipo: 'video' },
        { id: '1-2', titulo: 'Psicología del diseño', duracion: '60 min', tipo: 'video' },
        { id: '1-3', titulo: 'Principios de usabilidad', duracion: '50 min', tipo: 'video' }
      ]
    },
    {
      id: '2',
      titulo: 'Herramientas de diseño',
      clases: [
        { id: '2-1', titulo: 'Introducción a Figma', duracion: '55 min', tipo: 'video' },
        { id: '2-2', titulo: 'Componentes y estilos', duracion: '65 min', tipo: 'video' },
        { id: '2-3', titulo: 'Proyecto: Diseño de componentes UI', duracion: '90 min', tipo: 'proyecto' }
      ]
    },
    {
      id: '3',
      titulo: 'Investigación de usuarios',
      clases: [
        { id: '3-1', titulo: 'Técnicas de investigación', duracion: '50 min', tipo: 'video' },
        { id: '3-2', titulo: 'Análisis de datos de usuarios', duracion: '45 min', tipo: 'video' },
        { id: '3-3', titulo: 'Creación de personas', duracion: '40 min', tipo: 'video' }
      ]
    }
  ]
};

export default function CursoDetailScreen() {
  const { id } = useLocalSearchParams();
  const [loading, setLoading] = useState(true);
  const [curso, setCurso] = useState<Curso | null>(null);
  const [inscrito, setInscrito] = useState(false);
  const insets = useSafeAreaInsets();
  const { user } = useAuth();
  
  const colorScheme = useColorScheme();
  const colors = Colors[colorScheme ?? 'light'];
  
  // Definimos cargarCurso como un useCallback para evitar recreaciones innecesarias
  const cargarCurso = useCallback(async () => {
    setLoading(true);
    try {
      // En un entorno real, obtendríamos el curso de la API
      // const response = await cursosApi.getCursoById(String(id));
      // setCurso(response);
      
      // Usamos datos de ejemplo para este prototipo
      setTimeout(() => {
        setCurso(COURSE_DATA);
        
        // Simulamos comprobar si el usuario está inscrito
        if (id === '4' || id === '5') {
          setInscrito(true);
        }
        
        setLoading(false);
      }, 1000);
    } catch (error) {
      console.error('Error al cargar curso:', error);
      setLoading(false);
    }
  }, [id]); // Dependencia: id
  
  // Llamamos a cargarCurso cuando cambie el id
  useEffect(() => {
    cargarCurso();
  }, [cargarCurso]);
  
  const inscribirse = async () => {
    if (!user) {
      router.push('/login');
      return;
    }
    
    setLoading(true);
    try {
      // En un entorno real, enviaríamos la solicitud de inscripción a la API
      // await inscripcionesApi.inscribir(user.rut, String(id));
      
      // Simulamos la inscripción
      setTimeout(() => {
        setInscrito(true);
        setLoading(false);
      }, 1000);
    } catch (error) {
      console.error('Error al inscribirse:', error);
      setLoading(false);
    }
  };
  
  const getLectureIcon = (tipo: string) => {
    switch (tipo) {
      case 'video':
        return 'play-circle';
      case 'proyecto':
        return 'hammer';
      case 'quiz':
        return 'help-circle';
      default:
        return 'document-text';
    }
  };
  
  if (loading) {
    return (
      <ThemedView style={[styles.container, { paddingTop: insets.top }]}>
        <View style={styles.loaderContainer}>
          <ActivityIndicator size="large" color={colors.tint} />
          <ThemedText style={{ marginTop: 16 }}>Cargando curso...</ThemedText>
        </View>
      </ThemedView>
    );
  }
  
  if (!curso) {
    return (
      <ThemedView style={[styles.container, { paddingTop: insets.top }]}>
        <View style={styles.loaderContainer}>
          <Ionicons name="alert-circle" size={60} color={colors.error} />
          <ThemedText type="heading3" style={{ marginTop: 16, marginBottom: 8 }}>
            Curso no encontrado
          </ThemedText>
          <ThemedText secondary={true}>
            No se pudo encontrar el curso solicitado
          </ThemedText>
          <Button 
            title="Volver"
            onPress={() => router.back()}
            variant="outline"
            style={{ marginTop: 24 }}
          />
        </View>
      </ThemedView>
    );
  }

  return (
    <ThemedView style={[styles.container, { paddingTop: insets.top }]}>
      <ScrollView style={styles.scrollView} showsVerticalScrollIndicator={false}>
        {/* Header con imagen */}
        <View style={styles.header}>
          <Image
            source={{ uri: curso.imagen }}
            contentFit="cover"
            transition={300}
            style={styles.headerImage}
          />
          
          <TouchableOpacity
            style={styles.backButton}
            onPress={() => router.back()}
            activeOpacity={0.7}
          >
            <Ionicons name="chevron-back" size={22} color="#FFFFFF" />
          </TouchableOpacity>
          
          {inscrito && (
            <View style={[styles.enrolledTag, { backgroundColor: colors.success }]}>
              <ThemedText style={{ color: '#FFFFFF', fontWeight: 'bold' }}>
                Inscrito
              </ThemedText>
            </View>
          )}
        </View>
        
        {/* Información del curso */}
        <View style={styles.contentContainer}>
          {/* Título y categoría */}
          <View style={styles.titleContainer}>
            <View style={[styles.categoryTag, { backgroundColor: colors.accent + '20' }]}>
              <ThemedText style={{ color: colors.accent, fontWeight: '600' }}>
                {curso.categoria}
              </ThemedText>
            </View>
            
            <ThemedText type="heading1" style={styles.title}>
              {curso.titulo}
            </ThemedText>
            
            <View style={styles.instructorContainer}>
              <Ionicons name="person" size={16} color={colors.icon} />
              <ThemedText style={styles.instructor}>
                {curso.instructor}
              </ThemedText>
            </View>
          </View>
          
          {/* Métricas */}
          <View style={styles.metricsContainer}>
            <View style={styles.metricItem}>
              <View style={styles.metricIconContainer}>
                <Ionicons name="star" size={16} color="#FFB800" />
              </View>
              <ThemedText style={styles.metricValue}>
                {curso.calificacion.toFixed(1)}
              </ThemedText>
              <ThemedText secondary={true} style={styles.metricLabel}>
                Calificación
              </ThemedText>
            </View>
            
            <View style={styles.metricItem}>
              <View style={styles.metricIconContainer}>
                <Ionicons name="people" size={16} color={colors.icon} />
              </View>
              <ThemedText style={styles.metricValue}>
                {curso.estudiantes}
              </ThemedText>
              <ThemedText secondary={true} style={styles.metricLabel}>
                Estudiantes
              </ThemedText>
            </View>
            
            <View style={styles.metricItem}>
              <View style={styles.metricIconContainer}>
                <Ionicons name="time" size={16} color={colors.icon} />
              </View>
              <ThemedText style={styles.metricValue}>
                {curso.duracion}
              </ThemedText>
              <ThemedText secondary={true} style={styles.metricLabel}>
                Duración
              </ThemedText>
            </View>
            
            <View style={styles.metricItem}>
              <View style={styles.metricIconContainer}>
                <Ionicons name="speedometer" size={16} color={colors.icon} />
              </View>
              <ThemedText style={styles.metricValue}>
                {curso.nivel}
              </ThemedText>
              <ThemedText secondary={true} style={styles.metricLabel}>
                Nivel
              </ThemedText>
            </View>
          </View>
          
          {/* Descripción */}
          <View style={styles.descriptionContainer}>
            <ThemedText type="heading2" style={styles.sectionTitle}>
              Descripción
            </ThemedText>
            <ThemedText style={styles.description}>
              {curso.descripcion}
            </ThemedText>
          </View>
          
          {/* Contenido del curso */}
          <View style={styles.contentSection}>
            <ThemedText type="heading2" style={styles.sectionTitle}>
              Contenido del curso
            </ThemedText>
            
            {curso.modulos.map((modulo: Modulo) => (
              <View key={modulo.id} style={styles.moduleContainer}>
                <ThemedText type="bodyBold" style={styles.moduleTitle}>
                  Módulo {modulo.id}: {modulo.titulo}
                </ThemedText>
                
                <View style={styles.classesList}>
                  {modulo.clases.map((clase: Clase) => (
                    <TouchableOpacity 
                      key={clase.id}
                      style={styles.classItem}
                      activeOpacity={0.7}
                    >
                      <View style={[styles.classIconContainer, { backgroundColor: colors.backgroundSecondary }]}>
                        <Ionicons name={getLectureIcon(clase.tipo)} size={20} color={colors.tint} />
                      </View>
                      <View style={styles.classInfo}>
                        <ThemedText style={styles.className}>
                          {clase.titulo}
                        </ThemedText>
                        <ThemedText secondary={true} style={styles.classDuration}>
                          {clase.duracion}
                        </ThemedText>
                      </View>
                      {inscrito && (
                        <Ionicons name="chevron-forward" size={20} color={colors.icon} />
                      )}
                    </TouchableOpacity>
                  ))}
                </View>
              </View>
            ))}
          </View>
        </View>
      </ScrollView>
      
      {/* Barra inferior con precio y botón de inscripción */}
      <View style={[styles.bottomBar, { paddingBottom: insets.bottom || 16 }]}>
        {!inscrito ? (
          <>
            <View style={styles.priceContainer}>
              <ThemedText secondary={true} style={styles.priceLabel}>
                Precio
              </ThemedText>
              <ThemedText type="heading2" style={styles.price}>
                ${curso.precio.toLocaleString('es-CL')}
              </ThemedText>
            </View>
            
            <Button
              title="Inscribirme"
              onPress={inscribirse}
              variant="primary"
              loading={loading}
              style={styles.enrollButton}
            />
          </>
        ) : (          <Button
            title="Continuar con el curso"
            onPress={() => router.push({pathname: '/cursos', params: {modulo: curso.id}})}
            variant="primary"
            fullWidth={true}
            rightIcon={<Ionicons name="arrow-forward" size={20} color="#FFFFFF" />}
          />
        )}
      </View>
    </ThemedView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  scrollView: {
    flex: 1,
  },
  loaderContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 24,
  },
  header: {
    height: 240,
    position: 'relative',
  },
  headerImage: {
    width: '100%',
    height: '100%',
  },
  backButton: {
    position: 'absolute',
    top: 16,
    left: 16,
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: 'rgba(0, 0, 0, 0.3)',
    justifyContent: 'center',
    alignItems: 'center',
    zIndex: 10,
  },
  enrolledTag: {
    position: 'absolute',
    top: 16,
    right: 16,
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 6,
  },
  contentContainer: {
    padding: 16,
  },
  titleContainer: {
    marginBottom: 24,
  },
  categoryTag: {
    alignSelf: 'flex-start',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 6,
    marginBottom: 8,
  },
  title: {
    marginBottom: 8,
  },
  instructorContainer: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  instructor: {
    marginLeft: 6,
  },
  metricsContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 24,
    backgroundColor: 'rgba(0, 0, 0, 0.02)',
    borderRadius: 12,
    padding: 16,
    elevation: 1,
  },
  metricItem: {
    alignItems: 'center',
    flex: 1,
  },
  metricIconContainer: {
    marginBottom: 4,
  },
  metricValue: {
    fontWeight: 'bold',
    fontSize: 16,
    marginBottom: 2,
  },
  metricLabel: {
    fontSize: 12,
  },
  descriptionContainer: {
    marginBottom: 24,
  },
  sectionTitle: {
    marginBottom: 12,
  },
  description: {
    lineHeight: 24,
  },
  contentSection: {
    marginBottom: 100, // Espacio para la barra inferior
  },
  moduleContainer: {
    marginBottom: 16,
  },
  moduleTitle: {
    marginBottom: 8,
  },
  classesList: {
    borderRadius: 12,
    overflow: 'hidden',
  },
  classItem: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 12,
    paddingHorizontal: 16,
    borderBottomWidth: 1,
    borderBottomColor: 'rgba(0, 0, 0, 0.05)',
  },
  classIconContainer: {
    width: 40,
    height: 40,
    borderRadius: 20,
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: 12,
  },
  classInfo: {
    flex: 1,
  },
  className: {
    marginBottom: 4,
  },
  classDuration: {
    fontSize: 12,
  },
  bottomBar: {
    position: 'absolute',
    bottom: 0,
    left: 0,
    right: 0,
    backgroundColor: '#FFFFFF',
    borderTopWidth: 1,
    borderTopColor: 'rgba(0, 0, 0, 0.05)',
    padding: 16,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    elevation: 10,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: -3 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
  },
  priceContainer: {
    flex: 1,
  },
  priceLabel: {
    fontSize: 12,
  },
  price: {
    marginTop: 2,
  },
  enrollButton: {
    marginLeft: 16,
  },
});
