import React, { useState, useEffect, useContext } from 'react';
import { 
  View, 
  Text, 
  StyleSheet, 
  ScrollView, 
  TouchableOpacity, 
  ActivityIndicator,
  RefreshControl,
  Image,
  SafeAreaView,
  StatusBar
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONTS, SHADOWS, BORDER_RADIUS } from '../../config/theme';
import { Card } from '../../components/common/UIComponents';
import { AuthContext } from '../../context/AuthContext';
import { DEMO_MODE } from '../../config/api';
import { 
  CourseService, 
  UserService, 
  ScheduleService, 
  CommunicationService,
  EvaluationService,
  AssignmentService 
} from '../../services/studentApiService';

const MinimalistHomeScreen = ({ navigation }) => {
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [courses, setCourses] = useState([]);
  const [userProfile, setUserProfile] = useState(null);
  const [todaySchedule, setTodaySchedule] = useState([]);
  const [pendingAssignments, setPendingAssignments] = useState([]);
  const [recentAnnouncements, setRecentAnnouncements] = useState([]);
  const [upcomingEvaluations, setUpcomingEvaluations] = useState([]);
  
  const { user, token } = useContext(AuthContext);

  // Cargar todos los datos del dashboard
  const loadHomeData = async () => {
    try {
      setLoading(true);
      
      // Cargar datos en paralelo con manejo individual de errores
      const results = await Promise.allSettled([
        CourseService.getAllCourses(token).catch(err => ({ success: false, error: err.message })),
        UserService.getProfile(token).catch(err => ({ success: false, error: err.message })),
        ScheduleService.getSchedule(token).catch(err => ({ success: false, error: err.message })),
        AssignmentService.getAssignments(token).catch(err => ({ success: false, error: err.message })),
        CommunicationService.getAnnouncements(token).catch(err => ({ success: false, error: err.message })),
        EvaluationService.getEvaluations(token).catch(err => ({ success: false, error: err.message }))
      ]);

      // Extraer resultados
      const [
        coursesResponse,
        profileResponse,
        scheduleResponse,
        assignmentsResponse,
        announcementsResponse,
        evaluationsResponse
      ] = results.map(result => result.status === 'fulfilled' ? result.value : { success: false, error: result.reason?.message });

      // Procesar cursos
      if (coursesResponse.success) {
        setCourses(coursesResponse.data.slice(0, 3)); // Solo los primeros 3 para el dashboard
      }

      // Procesar perfil
      if (profileResponse.success) {
        setUserProfile(profileResponse.data);
      }

      // Procesar horario de hoy
      if (scheduleResponse.success) {
        const today = new Date().toLocaleDateString('es-ES', { weekday: 'long' });
        const todayClasses = scheduleResponse.data.find(day => 
          day.dia.toLowerCase() === today.toLowerCase()
        );
        setTodaySchedule(todayClasses?.clases?.slice(0, 2) || []);
      }

      // Procesar tareas pendientes
      if (assignmentsResponse.success) {
        const pending = assignmentsResponse.data.filter(a => a.estado === 'EN_PROGRESO');
        setPendingAssignments(pending.slice(0, 2));
      }

      // Procesar anuncios recientes
      if (announcementsResponse.success) {
        const recent = announcementsResponse.data
          .sort((a, b) => new Date(b.fechaPublicacion) - new Date(a.fechaPublicacion))
          .slice(0, 2);
        setRecentAnnouncements(recent);
      }

    } catch (error) {
      console.warn('Error cargando datos de inicio (usando fallback):', error.message || error);
      
      // Usar datos por defecto si todo falla
      setCourses([]);
      setUserProfile(null);
      setTodaySchedule([]);
      setPendingAssignments([]);
      setRecentAnnouncements([]);
      setUpcomingEvaluations([]);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  useEffect(() => {
    loadHomeData();
  }, []);

  const onRefresh = async () => {
    setRefreshing(true);
    await loadHomeData();
  };

  const WelcomeHeader = () => (
    <View style={styles.welcomeContainer}>
      <View style={styles.welcomeTextContainer}>
        <Text style={styles.welcomeText}>Hola, {user?.nombre || 'Estudiante'}</Text>
        <Text style={styles.welcomeSubtext}>¿Listo para aprender hoy?</Text>
      </View>
      <TouchableOpacity style={styles.profileButton} onPress={() => navigation.navigate('Profile')}>
        <Ionicons name="person-circle-outline" size={32} color={COLORS.primary} />
      </TouchableOpacity>
    </View>
  );

  const QuickActions = () => (
    <View style={styles.quickActionsContainer}>
      <Text style={styles.sectionTitle}>Acceso Rápido</Text>
      <View style={styles.quickActionsGrid}>
        <TouchableOpacity 
          style={styles.quickAction}
          onPress={() => navigation.navigate('Courses')}
        >
          <View style={[styles.quickActionIcon, { backgroundColor: COLORS.primary }]}>
            <Ionicons name="book-outline" size={24} color={COLORS.white} />
          </View>
          <Text style={styles.quickActionText}>Cursos</Text>
        </TouchableOpacity>
        
        <TouchableOpacity 
          style={styles.quickAction}
          onPress={() => navigation.navigate('Schedule')}
        >
          <View style={[styles.quickActionIcon, { backgroundColor: COLORS.secondary }]}>
            <Ionicons name="calendar-outline" size={24} color={COLORS.white} />
          </View>
          <Text style={styles.quickActionText}>Horario</Text>
        </TouchableOpacity>
        
        <TouchableOpacity 
          style={styles.quickAction}
          onPress={() => navigation.navigate('Assignments')}
        >
          <View style={[styles.quickActionIcon, { backgroundColor: COLORS.warning }]}>
            <Ionicons name="document-text-outline" size={24} color={COLORS.white} />
          </View>
          <Text style={styles.quickActionText}>Tareas</Text>
        </TouchableOpacity>
        
        <TouchableOpacity 
          style={styles.quickAction}
          onPress={() => navigation.navigate('Grades')}
        >
          <View style={[styles.quickActionIcon, { backgroundColor: COLORS.success }]}>
            <Ionicons name="bar-chart-outline" size={24} color={COLORS.white} />
          </View>
          <Text style={styles.quickActionText}>Notas</Text>
        </TouchableOpacity>
      </View>
    </View>
  );

  const CoursesSection = () => (
    <View style={styles.section}>
      <View style={styles.sectionHeader}>
        <Text style={styles.sectionTitle}>Mis Cursos</Text>
        <TouchableOpacity onPress={() => navigation.navigate('Courses')}>
          <Text style={styles.seeAllText}>Ver todos</Text>
        </TouchableOpacity>
      </View>
      
      {courses.length > 0 ? (
        <ScrollView 
          horizontal 
          showsHorizontalScrollIndicator={false}
          contentContainerStyle={styles.coursesScroll}
        >
          {courses.map((course, index) => (
            <TouchableOpacity
              key={course.id || index}
              style={styles.courseCard}
              onPress={() => navigation.navigate('CourseDetail', { courseId: course.id })}
            >
              <View style={styles.courseImageContainer}>
                <View style={[styles.courseImage, { backgroundColor: COLORS.primaryLight }]}>
                  <Ionicons name="book" size={32} color={COLORS.primary} />
                </View>
              </View>
              <View style={styles.courseInfo}>
                <Text style={styles.courseTitle} numberOfLines={2}>
                  {course.nombre || course.title}
                </Text>
                <Text style={styles.courseInstructor}>
                  {course.profesorNombre || course.instructor || 'Instructor'}
                </Text>
                {course.progreso !== undefined && (
                  <View style={styles.progressContainer}>
                    <View style={styles.progressBar}>
                      <View 
                        style={[
                          styles.progressFill, 
                          { width: `${course.progreso}%` }
                        ]} 
                      />
                    </View>
                    <Text style={styles.progressText}>{course.progreso}%</Text>
                  </View>
                )}
              </View>
            </TouchableOpacity>
          ))}
        </ScrollView>
      ) : (
        <View style={styles.emptyState}>
          <Ionicons name="book-outline" size={48} color={COLORS.textSecondary} />
          <Text style={styles.emptyStateText}>No hay cursos disponibles</Text>
        </View>
      )}
    </View>
  );

  const TodayScheduleSection = () => (
    <View style={styles.section}>
      <View style={styles.sectionHeader}>
        <Text style={styles.sectionTitle}>Horario de Hoy</Text>
        <TouchableOpacity onPress={() => navigation.navigate('Schedule')}>
          <Text style={styles.seeAllText}>Ver completo</Text>
        </TouchableOpacity>
      </View>
      
      {todaySchedule.length > 0 ? (
        todaySchedule.map((clase, index) => (
          <View key={index} style={styles.scheduleItem}>
            <View style={styles.scheduleTime}>
              <Text style={styles.scheduleTimeText}>{clase.hora}</Text>
            </View>
            <View style={styles.scheduleDetails}>
              <Text style={styles.scheduleTitle}>{clase.materia}</Text>
              <Text style={styles.scheduleLocation}>{clase.aula}</Text>
            </View>
          </View>
        ))
      ) : (
        <View style={styles.emptyState}>
          <Ionicons name="calendar-outline" size={48} color={COLORS.textSecondary} />
          <Text style={styles.emptyStateText}>No hay clases programadas hoy</Text>
        </View>
      )}
    </View>
  );

  if (loading) {
    return (
      <SafeAreaView style={styles.container}>
        <StatusBar barStyle="dark-content" backgroundColor={COLORS.background} />
        <View style={styles.loadingContainer}>
          <ActivityIndicator size="large" color={COLORS.primary} />
          <Text style={styles.loadingText}>Cargando...</Text>
        </View>
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor={COLORS.background} />
      
      <ScrollView 
        style={styles.scrollView}
        refreshControl={
          <RefreshControl 
            refreshing={refreshing} 
            onRefresh={onRefresh}
            colors={[COLORS.primary]}
            tintColor={COLORS.primary}
          />
        }
        showsVerticalScrollIndicator={false}
      >
        {/* Banner de modo demo */}
        {DEMO_MODE && (
          <View style={styles.demoBanner}>
            <Ionicons name="information-circle" size={20} color={COLORS.white} />
            <Text style={styles.demoBannerText}>MODO DEMO - Datos de demostración</Text>
          </View>
        )}
        
        <WelcomeHeader />
        <QuickActions />
        <CoursesSection />
        <TodayScheduleSection />
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  scrollView: {
    flex: 1,
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  loadingText: {
    ...FONTS.body2,
    color: COLORS.textSecondary,
    marginTop: SPACING.md,
  },
  demoBanner: {
    backgroundColor: COLORS.info,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: SPACING.sm,
    paddingHorizontal: SPACING.md,
  },
  demoBannerText: {
    ...FONTS.caption,
    color: COLORS.white,
    marginLeft: SPACING.xs,
    fontWeight: '600',
  },
  welcomeContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: SPACING.lg,
    paddingVertical: SPACING.xl,
    backgroundColor: COLORS.white,
  },
  welcomeTextContainer: {
    flex: 1,
  },
  welcomeText: {
    ...FONTS.h3,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  welcomeSubtext: {
    ...FONTS.body2,
    color: COLORS.textSecondary,
  },
  profileButton: {
    padding: SPACING.sm,
  },
  quickActionsContainer: {
    backgroundColor: COLORS.white,
    marginTop: SPACING.sm,
    paddingHorizontal: SPACING.lg,
    paddingVertical: SPACING.xl,
  },
  quickActionsGrid: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: SPACING.lg,
  },
  quickAction: {
    alignItems: 'center',
    flex: 1,
  },
  quickActionIcon: {
    width: 56,
    height: 56,
    borderRadius: 28,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: SPACING.sm,
  },
  quickActionText: {
    ...FONTS.caption,
    color: COLORS.text,
    textAlign: 'center',
  },
  section: {
    backgroundColor: COLORS.white,
    marginTop: SPACING.sm,
    paddingHorizontal: SPACING.lg,
    paddingVertical: SPACING.xl,
  },
  sectionHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: SPACING.lg,
  },
  sectionTitle: {
    ...FONTS.h4,
    color: COLORS.text,
  },
  seeAllText: {
    ...FONTS.body2,
    color: COLORS.primary,
    fontWeight: '600',
  },
  coursesScroll: {
    paddingRight: SPACING.lg,
  },
  courseCard: {
    width: 200,
    marginRight: SPACING.md,
    backgroundColor: COLORS.surface,
    borderRadius: BORDER_RADIUS.lg,
    ...SHADOWS.sm,
    overflow: 'hidden',
  },
  courseImageContainer: {
    height: 120,
    justifyContent: 'center',
    alignItems: 'center',
  },
  courseImage: {
    width: 64,
    height: 64,
    borderRadius: 32,
    justifyContent: 'center',
    alignItems: 'center',
  },
  courseInfo: {
    padding: SPACING.md,
  },
  courseTitle: {
    ...FONTS.h6,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  courseInstructor: {
    ...FONTS.caption,
    color: COLORS.textSecondary,
    marginBottom: SPACING.sm,
  },
  progressContainer: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  progressBar: {
    flex: 1,
    height: 4,
    backgroundColor: COLORS.border,
    borderRadius: 2,
    marginRight: SPACING.sm,
  },
  progressFill: {
    height: '100%',
    backgroundColor: COLORS.primary,
    borderRadius: 2,
  },
  progressText: {
    ...FONTS.caption,
    color: COLORS.textSecondary,
  },
  scheduleItem: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.divider,
  },
  scheduleTime: {
    width: 80,
    alignItems: 'center',
  },
  scheduleTimeText: {
    ...FONTS.h6,
    color: COLORS.primary,
  },
  scheduleDetails: {
    flex: 1,
    marginLeft: SPACING.md,
  },
  scheduleTitle: {
    ...FONTS.body1,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  scheduleLocation: {
    ...FONTS.caption,
    color: COLORS.textSecondary,
  },
  emptyState: {
    alignItems: 'center',
    paddingVertical: SPACING.xl,
  },
  emptyStateText: {
    ...FONTS.body2,
    color: COLORS.textSecondary,
    marginTop: SPACING.md,
    textAlign: 'center',
  },
});

export default MinimalistHomeScreen;
