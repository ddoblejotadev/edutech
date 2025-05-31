import React, { useState, useEffect, useContext } from 'react';
import { 
  View, 
  Text, 
  StyleSheet, 
  ScrollView, 
  TouchableOpacity, 
  ActivityIndicator,
  RefreshControl,
  SafeAreaView
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT, BORDER_RADIUS } from '../../config/theme';
import { Card } from '../../components/common/UIComponents';
import { AuthContext } from '../../context/AuthContext';

const UniversityHomeScreen = ({ navigation }) => {
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [dashboardData, setDashboardData] = useState({
    courses: [],
    userProfile: null,
    todaySchedule: [],
    announcements: [],
    stats: {}
  });
  
  const { user } = useContext(AuthContext);

  // Cargar datos del dashboard universitario
  const loadDashboardData = async () => {
    try {
      setLoading(true);
      
      // Simular datos universitarios realistas
      const universitData = {
        userProfile: {
          name: user?.name || 'Juan Carlos Pérez',
          studentId: 'EST-2024-001234',
          program: 'Ingeniería en Informática',
          semester: '8° Semestre',
          academicYear: '2024-2025',
          gpa: '8.7',
          credits: '180/240'
        },
        todaySchedule: [
          {
            id: 1,
            subject: 'Programación Avanzada',
            code: 'INF-401',
            time: '10:00 - 11:30',
            classroom: 'Lab. Informática A',
            professor: 'Dr. Carlos Mendoza',
            type: 'Laboratorio'
          },
          {
            id: 2,
            subject: 'Proyecto de Título',
            code: 'INF-499',
            time: '14:00 - 15:30',
            classroom: 'Sala de Proyectos',
            professor: 'Ing. María González',
            type: 'Taller'
          }
        ],
        announcements: [
          {
            id: 1,
            title: 'Renovación Matrícula 2025',
            content: 'El proceso de renovación de matrícula para el período 2025 estará disponible del 15 al 30 de diciembre.',
            date: 'Hace 2 días',
            priority: 'high',
            department: 'Secretaría Académica'
          },
          {
            id: 2,
            title: 'Semana de Exámenes Finales',
            content: 'La semana de exámenes finales se realizará del 10 al 17 de diciembre. Consulta tu horario en el portal.',
            date: 'Hace 3 días',
            priority: 'medium',
            department: 'Dirección de Carrera'
          }
        ],
        courses: [
          {
            id: 1,
            name: 'Programación Avanzada',
            code: 'INF-401',
            credits: 6,
            professor: 'Dr. Carlos Mendoza',
            nextClass: 'Hoy 10:00',
            progress: 75
          },
          {
            id: 2,
            name: 'Base de Datos II',
            code: 'INF-302',
            credits: 5,
            professor: 'Ing. María González',
            nextClass: 'Mañana 08:00',
            progress: 68
          }
        ],
        stats: {
          coursesEnrolled: 6,
          completedAssignments: 24,
          pendingAssignments: 3,
          attendance: 92
        }
      };
      
      setDashboardData(universitData);
    } catch (error) {
      console.warn('Error loading dashboard:', error.message);
    } finally {
      setLoading(false);
    }
  };

  // Refrescar datos
  const onRefresh = async () => {
    setRefreshing(true);
    await loadDashboardData();
    setRefreshing(false);
  };

  useEffect(() => {
    loadDashboardData();
  }, []);

  // Renderizar cabecera universitaria
  const renderUniversityHeader = () => (
    <View style={styles.universityHeader}>
      <View style={styles.headerContent}>
        <View style={styles.universityInfo}>
          <Text style={styles.universityName}>Universidad Digital</Text>
          <Text style={styles.academicPeriod}>Período Académico 2024-2025</Text>
        </View>
        <TouchableOpacity 
          style={styles.profileButton}
          onPress={() => navigation.navigate('Profile')}
        >
          <View style={styles.avatarContainer}>
            <Text style={styles.avatarText}>
              {dashboardData.userProfile?.name?.charAt(0) || 'U'}
            </Text>
          </View>
        </TouchableOpacity>
      </View>
      
      {/* Tarjeta de información del estudiante */}
      <View style={styles.studentInfoCard}>
        <View style={styles.studentDetails}>
          <Text style={styles.studentName}>{dashboardData.userProfile?.name}</Text>
          <Text style={styles.studentId}>ID: {dashboardData.userProfile?.studentId}</Text>
          <Text style={styles.studentProgram}>{dashboardData.userProfile?.program}</Text>
        </View>
        <View style={styles.academicInfo}>
          <View style={styles.academicItem}>
            <Text style={styles.academicLabel}>GPA</Text>
            <Text style={styles.academicValue}>{dashboardData.userProfile?.gpa}</Text>
          </View>
          <View style={styles.academicItem}>
            <Text style={styles.academicLabel}>Créditos</Text>
            <Text style={styles.academicValue}>{dashboardData.userProfile?.credits}</Text>
          </View>
        </View>
      </View>
    </View>
  );

  // Renderizar estadísticas académicas
  const renderAcademicStats = () => (
    <Card style={styles.statsCard}>
      <Text style={styles.sectionTitle}>Resumen Académico</Text>
      <View style={styles.statsGrid}>
        <View style={styles.statItem}>
          <View style={styles.statIcon}>
            <Ionicons name="book" size={24} color={COLORS.primary} />
          </View>
          <Text style={styles.statNumber}>{dashboardData.stats?.coursesEnrolled}</Text>
          <Text style={styles.statLabel}>Cursos</Text>
        </View>
        
        <View style={styles.statItem}>
          <View style={styles.statIcon}>
            <Ionicons name="checkmark-circle" size={24} color={COLORS.success} />
          </View>
          <Text style={styles.statNumber}>{dashboardData.stats?.completedAssignments}</Text>
          <Text style={styles.statLabel}>Completadas</Text>
        </View>
        
        <View style={styles.statItem}>
          <View style={styles.statIcon}>
            <Ionicons name="time" size={24} color={COLORS.warning} />
          </View>
          <Text style={styles.statNumber}>{dashboardData.stats?.pendingAssignments}</Text>
          <Text style={styles.statLabel}>Pendientes</Text>
        </View>
        
        <View style={styles.statItem}>
          <View style={styles.statIcon}>
            <Ionicons name="calendar" size={24} color={COLORS.info} />
          </View>
          <Text style={styles.statNumber}>{dashboardData.stats?.attendance}%</Text>
          <Text style={styles.statLabel}>Asistencia</Text>
        </View>
      </View>
    </Card>
  );

  // Renderizar horario de hoy
  const renderTodaySchedule = () => (
    <Card style={styles.scheduleCard}>
      <View style={styles.sectionHeader}>
        <Text style={styles.sectionTitle}>Horario de Hoy</Text>
        <TouchableOpacity onPress={() => navigation.navigate('Schedule')}>
          <Text style={styles.viewAllText}>Ver completo</Text>
        </TouchableOpacity>
      </View>
      
      {dashboardData.todaySchedule?.length > 0 ? (
        dashboardData.todaySchedule.map((class_, index) => (
          <View key={class_.id} style={styles.classItem}>
            <View style={styles.classTime}>
              <Text style={styles.timeText}>{class_.time}</Text>
              <View style={[styles.classTypeIndicator, { backgroundColor: getClassTypeColor(class_.type) }]} />
            </View>
            <View style={styles.classDetails}>
              <Text style={styles.className}>{class_.subject}</Text>
              <Text style={styles.classCode}>{class_.code}</Text>
              <Text style={styles.classLocation}>{class_.classroom} • {class_.professor}</Text>
            </View>
          </View>
        ))
      ) : (
        <View style={styles.emptyState}>
          <Ionicons name="calendar-outline" size={48} color={COLORS.textTertiary} />
          <Text style={styles.emptyText}>No tienes clases programadas para hoy</Text>
        </View>
      )}
    </Card>
  );

  // Renderizar anuncios importantes
  const renderAnnouncements = () => (
    <Card style={styles.announcementsCard}>
      <View style={styles.sectionHeader}>
        <Text style={styles.sectionTitle}>Anuncios Importantes</Text>
        <TouchableOpacity onPress={() => navigation.navigate('Communications')}>
          <Text style={styles.viewAllText}>Ver todos</Text>
        </TouchableOpacity>
      </View>
      
      {dashboardData.announcements?.map((announcement) => (
        <TouchableOpacity key={announcement.id} style={styles.announcementItem}>
          <View style={styles.announcementHeader}>
            <View style={[styles.priorityIndicator, { backgroundColor: getPriorityColor(announcement.priority) }]} />
            <View style={styles.announcementContent}>
              <Text style={styles.announcementTitle}>{announcement.title}</Text>
              <Text style={styles.announcementMeta}>{announcement.department} • {announcement.date}</Text>
            </View>
            <Ionicons name="chevron-forward" size={20} color={COLORS.textSecondary} />
          </View>
          <Text style={styles.announcementText} numberOfLines={2}>
            {announcement.content}
          </Text>
        </TouchableOpacity>
      ))}
    </Card>
  );

  // Renderizar cursos actuales
  const renderCurrentCourses = () => (
    <Card style={styles.coursesCard}>
      <View style={styles.sectionHeader}>
        <Text style={styles.sectionTitle}>Mis Cursos</Text>
        <TouchableOpacity onPress={() => navigation.navigate('Courses')}>
          <Text style={styles.viewAllText}>Ver todos</Text>
        </TouchableOpacity>
      </View>
      
      {dashboardData.courses?.map((course) => (
        <TouchableOpacity 
          key={course.id} 
          style={styles.courseItem}
          onPress={() => navigation.navigate('CourseDetail', { courseId: course.id })}
        >
          <View style={styles.courseHeader}>
            <View style={styles.courseInfo}>
              <Text style={styles.courseName}>{course.name}</Text>
              <Text style={styles.courseCode}>{course.code} • {course.credits} créditos</Text>
              <Text style={styles.courseProfessor}>{course.professor}</Text>
            </View>
            <View style={styles.courseProgress}>
              <Text style={styles.progressText}>{course.progress}%</Text>
              <View style={styles.progressBar}>
                <View style={[styles.progressFill, { width: `${course.progress}%` }]} />
              </View>
            </View>
          </View>
          <Text style={styles.nextClass}>Próxima clase: {course.nextClass}</Text>
        </TouchableOpacity>
      ))}
    </Card>
  );

  // Funciones helper para colores
  const getClassTypeColor = (type) => {
    switch (type) {
      case 'Laboratorio': return COLORS.success;
      case 'Taller': return COLORS.secondary;
      case 'Teórica': return COLORS.primary;
      default: return COLORS.info;
    }
  };

  const getPriorityColor = (priority) => {
    switch (priority) {
      case 'high': return COLORS.error;
      case 'medium': return COLORS.warning;
      case 'low': return COLORS.info;
      default: return COLORS.textSecondary;
    }
  };

  if (loading) {
    return (
      <SafeAreaView style={styles.container}>
        <View style={styles.loadingContainer}>
          <ActivityIndicator size="large" color={COLORS.primary} />
          <Text style={styles.loadingText}>Cargando dashboard académico...</Text>
        </View>
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView 
        style={styles.scrollView}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
        showsVerticalScrollIndicator={false}
      >
        {renderUniversityHeader()}
        {renderAcademicStats()}
        {renderTodaySchedule()}
        {renderAnnouncements()}
        {renderCurrentCourses()}
        
        <View style={styles.footer}>
          <Text style={styles.footerText}>Universidad Digital • Sistema Académico</Text>
        </View>
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
    backgroundColor: COLORS.background,
  },
  loadingText: {
    marginTop: SPACING.md,
    fontSize: FONT_SIZE.md,
    color: COLORS.textSecondary,
  },
  sectionHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: SPACING.lg,
    marginBottom: SPACING.md,
  },
  sectionTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
  },
  viewAllText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.primary,
    fontWeight: FONT_WEIGHT.medium,
  },
  universityHeader: {
    backgroundColor: COLORS.primary,
    padding: SPACING.lg,
    borderBottomLeftRadius: BORDER_RADIUS.xl,
    borderBottomRightRadius: BORDER_RADIUS.xl,
    marginBottom: SPACING.lg,
  },
  headerContent: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  universityInfo: {
    flex: 1,
  },
  universityName: {
    fontSize: FONT_SIZE.xl,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.white,
  },
  academicPeriod: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.white,
    opacity: 0.9,
  },
  profileButton: {
    width: 44,
    height: 44,
    borderRadius: 22,
    backgroundColor: COLORS.secondary,
    justifyContent: 'center',
    alignItems: 'center',
  },
  avatarContainer: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: COLORS.white,
    justifyContent: 'center',
    alignItems: 'center',
  },
  avatarText: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.primary,
  },
  studentInfoCard: {
    backgroundColor: COLORS.white,
    borderRadius: BORDER_RADIUS.md,
    padding: SPACING.md,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginTop: SPACING.md,
    elevation: 2,
    shadowColor: COLORS.black,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
  },
  studentDetails: {
    flex: 1,
  },
  studentName: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
  },
  studentId: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
    marginTop: SPACING.xs,
  },
  studentProgram: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
  },
  academicInfo: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  academicItem: {
    marginLeft: SPACING.md,
    alignItems: 'center',
  },
  academicLabel: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.textSecondary,
  },
  academicValue: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
  },
  statsCard: {
    marginHorizontal: SPACING.lg,
    padding: SPACING.lg,
    marginBottom: SPACING.lg,
  },
  statsGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
    marginTop: SPACING.md,
  },
  statItem: {
    width: '48%',
    alignItems: 'center',
    marginBottom: SPACING.md,
  },
  statIcon: {
    width: 48,
    height: 48,
    borderRadius: 24,
    backgroundColor: COLORS.lightGray,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: SPACING.xs,
  },
  statNumber: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
  },
  statLabel: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
    textAlign: 'center',
  },
  scheduleCard: {
    marginHorizontal: SPACING.lg,
    marginBottom: SPACING.lg,
  },
  classItem: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: SPACING.md,
    paddingHorizontal: SPACING.lg,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  classTime: {
    width: 80,
    alignItems: 'center',
    marginRight: SPACING.md,
  },
  timeText: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.text,
  },
  classTypeIndicator: {
    width: 4,
    height: 20,
    borderRadius: 2,
    marginTop: SPACING.xs,
  },
  classDetails: {
    flex: 1,
  },
  className: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.text,
  },
  classCode: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
  },
  classLocation: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
  },
  emptyState: {
    justifyContent: 'center',
    alignItems: 'center',
    padding: SPACING.xl,
  },
  emptyText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.textSecondary,
    textAlign: 'center',
    marginTop: SPACING.md,
  },
  announcementsCard: {
    marginHorizontal: SPACING.lg,
    marginBottom: SPACING.lg,
  },
  announcementItem: {
    paddingHorizontal: SPACING.lg,
    paddingVertical: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  announcementHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: SPACING.xs,
  },
  priorityIndicator: {
    width: 4,
    height: 24,
    borderRadius: 2,
    marginRight: SPACING.md,
  },
  announcementContent: {
    flex: 1,
  },
  announcementTitle: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.text,
  },
  announcementMeta: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.textSecondary,
    marginTop: SPACING.xs,
  },
  announcementText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
    paddingLeft: SPACING.lg,
  },
  coursesCard: {
    marginHorizontal: SPACING.lg,
    marginBottom: SPACING.lg,
  },
  courseItem: {
    paddingHorizontal: SPACING.lg,
    paddingVertical: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  courseHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  courseInfo: {
    flex: 1,
  },
  courseName: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
  },
  courseCode: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
    marginTop: SPACING.xs,
  },
  courseProfessor: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
  },
  courseProgress: {
    alignItems: 'flex-end',
    minWidth: 80,
  },
  progressText: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  progressBar: {
    width: 60,
    height: 6,
    backgroundColor: COLORS.lightGray,
    borderRadius: 3,
  },
  progressFill: {
    height: '100%',
    backgroundColor: COLORS.success,
    borderRadius: 3,
  },
  nextClass: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
    marginTop: SPACING.xs,
  },
  footer: {
    padding: SPACING.lg,
    alignItems: 'center',
    justifyContent: 'center',
    borderTopWidth: 1,
    borderTopColor: COLORS.lightGray,
    marginTop: SPACING.lg,
  },
  footerText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
  },
});

export default UniversityHomeScreen;
