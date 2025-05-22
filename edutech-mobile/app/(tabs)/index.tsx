import { StyleSheet, TouchableOpacity, View, ScrollView, RefreshControl } from 'react-native';
import { router } from 'expo-router';
import { useEffect, useState, useCallback } from          <TouchableOpacity 
            style={styles.quickActionItem}
            onPress={() => {/* TODO: Implementar navegación a Calendario */}}
            activeOpacity={0.7}
          >
            <View style={[styles.iconContainer, { backgroundColor: '#FF3B5F' }]}>
              <IconSymbol name="calendar" size={22} color="#1A1A2E" />
            </View>
            <ThemedText type="caption">Calendario</ThemedText>
          </TouchableOpacity>
          
          <TouchableOpacity 
            style={styles.quickActionItem}
            onPress={() => {/* TODO: Implementar navegación a Perfil */}}
            activeOpacity={0.7}
          > { useSafeAreaInsets } from 'react-native-safe-area-context';
import { IconSymbol } from '@/components/ui/IconSymbol';

import { ThemedText } from '@/components/ThemedText';
import { ThemedView } from '@/components/ThemedView';
import { SearchBar } from '@/components/ui/SearchBar';
import { CourseList, type CourseItem } from '@/components/ui/CourseList';
import { notificacionesApi } from '../../api/notificacionesService';
import { useAuth } from '@/contexts/AuthContext';
import { Colors } from '@/constants/Colors';
import { useColorScheme } from '@/hooks/useColorScheme';

// Datos de ejemplo para mostrar
const FEATURED_COURSES = [
  {
    id: '1',
    title: 'Diseño UX/UI Avanzado',
    description: 'Aprende a crear interfaces modernas y experiencias de usuario efectivas',
    imageUri: 'https://images.unsplash.com/photo-1522542550221-31fd19575a2d?ixlib=rb-4.0.3&auto=format&fit=crop&w=1470&q=80',
    instructor: 'Ana García',
    category: 'Diseño',
    duration: '8 semanas'
  },
  {
    id: '2',
    title: 'Machine Learning Aplicado',
    description: 'Implementa algoritmos de machine learning en proyectos reales',
    imageUri: 'https://images.unsplash.com/photo-1555949963-ff9fe0c870eb?ixlib=rb-4.0.3&auto=format&fit=crop&w=1470&q=80',
    instructor: 'Pedro Ramírez',
    category: 'IA',
    duration: '10 semanas'
  },
  {
    id: '3',
    title: 'Desarrollo Web Full Stack',
    description: 'Construye aplicaciones web completas con tecnologías modernas',
    imageUri: 'https://images.unsplash.com/photo-1498050108023-c5249f4df085?ixlib=rb-4.0.3&auto=format&fit=crop&w=1472&q=80',
    instructor: 'Carolina Torres',
    category: 'Desarrollo',
    duration: '12 semanas'
  }
];

const MY_COURSES = [
  {
    id: '4',
    title: 'Principios de Diseño UX/UI',
    description: 'Fundamentos de diseño centrado en el usuario',
    imageUri: 'https://images.unsplash.com/photo-1561070791-2526d30994b5?ixlib=rb-4.0.3&auto=format&fit=crop&w=1528&q=80',
    instructor: 'Ana García',
    progress: 75,
    category: 'Diseño'
  },
  {
    id: '5',
    title: 'Python para Ciencia de Datos',
    description: 'Análisis y visualización de datos con Python',
    imageUri: 'https://images.unsplash.com/photo-1526379095098-d400fd0bf935?ixlib=rb-4.0.3&auto=format&fit=crop&w=1374&q=80',
    instructor: 'Carlos Sánchez',
    progress: 30,
    category: 'Programación'
  }
];

export default function HomeScreen() {
  const [refreshing, setRefreshing] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [notificacionesCount, setNotificacionesCount] = useState(0);
  const insets = useSafeAreaInsets();
  const { user } = useAuth();
  
  const colorScheme = useColorScheme();
  const colors = Colors[colorScheme ?? 'light'];
  
  const cargarNotificacionesNoLeidas = useCallback(async () => {
    try {
      // Usuario simulado si no hay usuario en el contexto
      const rutUsuario = user?.rut || '12345678-9';
      const response = await notificacionesApi.getNoLeidas(rutUsuario);
      setNotificacionesCount(response.length);
    } catch (error) {
      console.error('Error al cargar notificaciones no leídas:', error);
    }
  }, [user]);
  
  useEffect(() => {
    cargarNotificacionesNoLeidas();
  }, [cargarNotificacionesNoLeidas]);

  const onRefresh = useCallback(async () => {
    setRefreshing(true);
    await cargarNotificacionesNoLeidas();
    // Simular carga de datos
    setTimeout(() => {
      setRefreshing(false);
    }, 1000);
  }, [cargarNotificacionesNoLeidas]);

  const handleCoursePress = useCallback((course: CourseItem) => {
    router.push({
      pathname: '/curso/[id]',
      params: { id: course.id }
    });
  }, []);

  // Extraer el nombre del usuario
  const userName = user?.nombres?.split(' ')[0] || 'Estudiante';

  return (
    <ThemedView style={[styles.container, { paddingTop: insets.top }]}>
      <ScrollView
        style={styles.scrollView}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} colors={[colors.tint]} />
        }
        showsVerticalScrollIndicator={false}
      >
        {/* Header */}
        <View style={styles.header}>
          <View style={styles.headerContent}>
            <View>
              <ThemedText type="caption" secondary={true}>
                Buen día,
              </ThemedText>
              <ThemedText type="heading1">
                Hola {userName} 👋
              </ThemedText>
            </View>
            
            <TouchableOpacity 
              onPress={() => router.push('/(tabs)/notificaciones')}
              style={styles.notificationButton}
              activeOpacity={0.7}
            >
              <IconSymbol name="bell.fill" size={22} color={colors.icon} />
              {notificacionesCount > 0 && (
                <View style={[styles.badge, { backgroundColor: colors.tint }]}>
                  <ThemedText style={styles.badgeText}>
                    {notificacionesCount}
                  </ThemedText>
                </View>
              )}
            </TouchableOpacity>
          </View>
          
          <SearchBar
            placeholder="Buscar cursos, temas..."
            value={searchQuery}
            onChangeText={setSearchQuery}
            onSearch={(query) => router.push('/(tabs)/explore?search=' + query)}
            containerStyle={styles.searchBar}
          />
        </View>

        {/* Accesos rápidos */}
        <View style={styles.quickActions}>
          <TouchableOpacity 
            style={styles.quickActionItem}
            onPress={() => router.push('/(tabs)/inscripciones')}
            activeOpacity={0.7}
          >
            <View style={[styles.iconContainer, { backgroundColor: colors.accent }]}>
              <IconSymbol name="book.fill" size={22} color="#1A1A2E" />
            </View>
            <ThemedText type="caption">Mis Cursos</ThemedText>
          </TouchableOpacity>
          
          <TouchableOpacity 
            style={styles.quickActionItem}
            onPress={() => router.push('/(tabs)/explore')}
            activeOpacity={0.7}
          >
            <View style={[styles.iconContainer, { backgroundColor: '#FFB800' }]}>
              <IconSymbol name="paperplane.fill" size={22} color="#1A1A2E" />
            </View>
            <ThemedText type="caption">Explorar</ThemedText>
          </TouchableOpacity>
          
          <TouchableOpacity 
            style={styles.quickActionItem}
            onPress={() => router.navigate('/calendario')}
            activeOpacity={0.7}
          >
            <View style={[styles.iconContainer, { backgroundColor: '#FF3B5F' }]}>
              <IconSymbol name="calendar" size={22} color="#1A1A2E" />
            </View>
            <ThemedText type="caption">Calendario</ThemedText>
          </TouchableOpacity>
          
          <TouchableOpacity 
            style={styles.quickActionItem}
            onPress={() => router.navigate('/perfil')}
            activeOpacity={0.7}
          >
            <View style={[styles.iconContainer, { backgroundColor: '#0DE57A' }]}>
              <IconSymbol name="person.fill" size={22} color="#1A1A2E" />
            </View>
            <ThemedText type="caption">Perfil</ThemedText>
          </TouchableOpacity>
        </View>

        {/* Continúa aprendiendo */}
        <View style={styles.section}>
          <View style={styles.sectionHeader}>
            <ThemedText type="heading2">Continúa aprendiendo</ThemedText>
            <TouchableOpacity onPress={() => router.push('/(tabs)/inscripciones')}>
              <ThemedText style={{ color: colors.tint }}>Ver todo</ThemedText>
            </TouchableOpacity>
          </View>
          
          <CourseList
            data={MY_COURSES}
            onCoursePress={handleCoursePress}
            emptyMessage="No tienes cursos en progreso"
            horizontal={true}
          />
        </View>

        {/* Cursos destacados */}
        <View style={styles.section}>
          <View style={styles.sectionHeader}>
            <ThemedText type="heading2">Cursos destacados</ThemedText>
            <TouchableOpacity onPress={() => router.push('/(tabs)/explore')}>
              <ThemedText style={{ color: colors.tint }}>Ver más</ThemedText>
            </TouchableOpacity>
          </View>
          
          <CourseList
            data={FEATURED_COURSES}
            onCoursePress={handleCoursePress}
            emptyMessage="No hay cursos destacados disponibles"
            horizontal={true}
          />
        </View>
      </ScrollView>
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
  header: {
    paddingHorizontal: 16,
    marginBottom: 16,
  },
  headerContent: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 16,
    marginTop: 8,
  },
  notificationButton: {
    width: 44,
    height: 44,
    borderRadius: 22,
    justifyContent: 'center',
    alignItems: 'center',
  },
  badge: {
    position: 'absolute',
    top: 0,
    right: 0,
    minWidth: 18,
    height: 18,
    borderRadius: 9,
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: 4,
  },
  badgeText: {
    fontSize: 10,
    fontWeight: 'bold',
    color: '#FFFFFF',
  },
  searchBar: {
    marginVertical: 8,
  },
  quickActions: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingHorizontal: 16,
    marginBottom: 24,
  },
  quickActionItem: {
    alignItems: 'center',
    width: '23%',
  },
  iconContainer: {
    width: 56,
    height: 56,
    borderRadius: 16,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 8,
  },
  section: {
    marginBottom: 24,
  },
  sectionHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 16,
    marginBottom: 8,
  },
});
