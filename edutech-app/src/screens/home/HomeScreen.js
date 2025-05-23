import React, { useState, useEffect, useContext } from 'react';
import { View, Text, StyleSheet, ScrollView, Image, TouchableOpacity, ActivityIndicator } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT, SHADOWS } from '../../config/theme';
import { Card } from '../../components/common/UIComponents';
import Header from '../../components/common/Header';
import { AuthContext } from '../../context/AuthContext';
import { Courses, User } from '../../services/apiService';

const HomeScreen = ({ navigation }) => {
  const [loading, setLoading] = useState(true);
  const [courses, setCourses] = useState([]);
  const [userProgress, setUserProgress] = useState(0);
  const [nextClasses, setNextClasses] = useState([]);
  const [error, setError] = useState(null);
  
  const { user, token } = useContext(AuthContext);
  
  // Cargar datos desde la API
  useEffect(() => {
    const loadHomeData = async () => {
      setLoading(true);
      setError(null);
      
      try {
        // Obtener cursos del usuario
        if (token) {
          // En una implementación real, estas serían llamadas a la API
          const coursesResponse = await Courses.getAll(token, { enrolled: true, limit: 5 });
          const userProfileResponse = await User.getProfile(token);
          
          setCourses(coursesResponse?.data || []);
          
          // Calcular progreso general del usuario basado en todos sus cursos
          if (userProfileResponse?.data?.progress) {
            setUserProgress(userProfileResponse.data.progress);
          } else {
            // Si no hay datos de progreso reales, calcular un promedio simulado
            const totalProgress = coursesResponse?.data?.reduce((sum, course) => sum + (course.progress || 0), 0) || 0;
            const averageProgress = coursesResponse?.data?.length ? Math.round(totalProgress / coursesResponse.data.length) : 0;
            setUserProgress(averageProgress);
          }
          
          // Obtener próximas clases (simulado por ahora)
          setNextClasses([
            {
              id: '1',
              title: 'Fundamentos de programación',
              time: '10:00 AM',
              duration: '90 min',
              teacher: 'Prof. Martínez',
            },
            {
              id: '2',
              title: 'Estructuras de datos',
              time: '2:30 PM',
              duration: '60 min',
              teacher: 'Prof. Rodríguez',
            },
          ]);
        }
      } catch (error) {
        console.error('Error cargando datos de inicio:', error);
        setError('No se pudieron cargar los datos. Por favor, inténtalo de nuevo más tarde.');
      } finally {
        setLoading(false);
      }
    };
    
    loadHomeData();
  }, [token]);
    // Componente de cursos activos
  const CourseCard = ({ course, index }) => {
    // Usar imagen del servidor si existe, de lo contrario usar el placeholder
    const courseImage = course.imageUrl 
      ? { uri: course.imageUrl }
      : require('../../../assets/images/course-placeholder.png');
      
    return (
      <TouchableOpacity 
        onPress={() => navigation.navigate('CourseDetail', { courseId: course.id })}
        style={[
          styles.courseCard,
          { marginLeft: index === 0 ? SPACING.md : 0 }
        ]}
      >
        <Image source={courseImage} style={styles.courseImage} />
        <View style={styles.courseInfo}>
          <Text style={styles.courseTitle} numberOfLines={2}>{course.title}</Text>
          <Text style={styles.courseInstructor}>{course.instructor}</Text>
          
          <View style={styles.progressContainer}>
            <View style={styles.progressBar}>
              <View 
                style={[
                  styles.progressBarFill, 
                  { width: `${course.progress || 0}%` }
                ]} 
              />
            </View>
            <Text style={styles.progressText}>{course.progress || 0}%</Text>
          </View>
        </View>
      </TouchableOpacity>
    );
  };
  
  // Componente de clase próxima
  const ClassCard = ({ classItem }) => (
    <Card style={styles.classCard}>
      <View style={styles.classTimeContainer}>
        <Text style={styles.classTime}>{classItem.time}</Text>
        <Text style={styles.classDuration}>{classItem.duration}</Text>
      </View>
      
      <View style={styles.classInfoContainer}>
        <Text style={styles.classTitle}>{classItem.title}</Text>
        <Text style={styles.classTeacher}>{classItem.teacher}</Text>
      </View>
      
      <TouchableOpacity style={styles.classActionButton}>
        <Ionicons name="chevron-forward" size={22} color={COLORS.primary} />
      </TouchableOpacity>
    </Card>
  );
    return (
    <View style={styles.container}>
      <Header 
        title="Inicio" 
        rightIcon="notifications-outline" 
        onRightPress={() => {}} 
      />
      
      {loading ? (
        <View style={styles.loadingContainer}>
          <ActivityIndicator size="large" color={COLORS.primary} />
          <Text style={styles.loadingText}>Cargando...</Text>
        </View>
      ) : error ? (
        <View style={styles.errorContainer}>
          <Ionicons name="alert-circle-outline" size={48} color={COLORS.error} />
          <Text style={styles.errorText}>{error}</Text>
          <TouchableOpacity 
            style={styles.retryButton}
            onPress={() => {
              // Recargar los datos
              setLoading(true);
              // Aquí iría la lógica para recargar los datos
              setTimeout(() => setLoading(false), 1000);
            }}
          >
            <Text style={styles.retryButtonText}>Reintentar</Text>
          </TouchableOpacity>
        </View>
      ) : (
        <ScrollView 
          style={styles.scrollView}
          showsVerticalScrollIndicator={false}
        >
          {/* Bienvenida con tarjeta de usuario */}
          <Card style={styles.userCard}>
            <View style={styles.userCardContent}>
              <View>
                <Text style={styles.greeting}>¡Hola, {user?.name || 'Estudiante'}!</Text>
                <Text style={styles.welcomeText}>Bienvenido a EduTech</Text>
              </View>
              
              <View style={styles.avatar}>
                {user?.avatarUrl ? (
                  <Image 
                    source={{ uri: user.avatarUrl }} 
                    style={styles.avatarImage} 
                  />
                ) : (
                  <View style={styles.avatarPlaceholder}>
                    <Text style={styles.avatarText}>
                      {(user?.name || 'E').charAt(0).toUpperCase()}
                    </Text>
                  </View>
                )}
              </View>
            </View>
            
            <View style={styles.progressSection}>
              <View style={styles.progressInfo}>
                <Text style={styles.progressTitle}>Tu progreso general</Text>
                <Text style={styles.progressPercentage}>{userProgress}%</Text>
              </View>
              
              <View style={styles.progressBarContainer}>
                <View 
                  style={[
                    styles.progressBarFillHome, 
                    { width: `${userProgress}%` }
                  ]} 
                />
              </View>
            </View>
          </Card>
          
          {/* Sección de próximas clases */}
          <View style={styles.section}>
            <View style={styles.sectionHeader}>
              <Text style={styles.sectionTitle}>Próximas Clases</Text>
              <TouchableOpacity>
                <Text style={styles.seeAllButton}>Ver todo</Text>
              </TouchableOpacity>
            </View>
            
            {nextClasses.length > 0 ? (
              nextClasses.map((classItem) => (
                <ClassCard key={classItem.id} classItem={classItem} />
              ))
            ) : (
              <Card style={styles.emptyStateCard}>
                <Ionicons name="calendar-outline" size={32} color={COLORS.muted} />
                <Text style={styles.emptyStateText}>
                  No tienes clases programadas para hoy
                </Text>
              </Card>
            )}
          </View>
          
          {/* Sección de cursos activos */}
          <View style={styles.section}>
            <View style={styles.sectionHeader}>
              <Text style={styles.sectionTitle}>Mis Cursos</Text>
              <TouchableOpacity onPress={() => navigation.navigate('Courses')}>
                <Text style={styles.seeAllButton}>Ver todo</Text>
              </TouchableOpacity>
            </View>
            
            {courses.length > 0 ? (
              <ScrollView 
                horizontal 
                showsHorizontalScrollIndicator={false}
                contentContainerStyle={styles.coursesScrollView}
              >
                {courses.map((course, index) => (
                  <CourseCard 
                    key={course.id} 
                    course={course} 
                    index={index} 
                  />
                ))}
              </ScrollView>
            ) : (
              <Card style={styles.emptyStateCard}>
                <Ionicons name="book-outline" size={32} color={COLORS.muted} />
                <Text style={styles.emptyStateText}>
                  No estás inscrito en ningún curso
                </Text>
                <TouchableOpacity 
                  style={styles.actionButton}
                  onPress={() => navigation.navigate('Courses')}
                >
                  <Text style={styles.actionButtonText}>Explorar cursos</Text>
                </TouchableOpacity>
              </Card>
            )}
          </View>
          
          {/* Espacio adicional al final */}
          <View style={{ height: 20 }} />
        </ScrollView>
      )}
    </View>
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
  userCard: {
    marginHorizontal: SPACING.md,
    marginTop: SPACING.md,
    padding: SPACING.lg,
  },
  userCardContent: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  greeting: {
    fontSize: FONT_SIZE.xl,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  welcomeText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.lightText,
  },
  avatar: {
    width: 50,
    height: 50,
    borderRadius: 25,
    overflow: 'hidden',
  },
  avatarImage: {
    width: '100%',
    height: '100%',
  },
  avatarPlaceholder: {
    width: '100%',
    height: '100%',
    backgroundColor: COLORS.primary,
    alignItems: 'center',
    justifyContent: 'center',
  },
  avatarText: {
    color: COLORS.white,
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
  },
  progressSection: {
    marginTop: SPACING.md,
  },
  progressInfo: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: SPACING.xs,
  },
  progressTitle: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.lightText,
  },
  progressPercentage: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.primary,
  },
  progressBarContainer: {
    height: 8,
    backgroundColor: COLORS.border,
    borderRadius: 4,
    overflow: 'hidden',
  },
  progressBarFillHome: {
    height: '100%',
    backgroundColor: COLORS.primary,
    borderRadius: 4,
  },
  section: {
    marginTop: SPACING.xl,
    paddingHorizontal: SPACING.md,
  },
  sectionHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: SPACING.md,
  },
  sectionTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
  },
  seeAllButton: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.primary,
    fontWeight: FONT_WEIGHT.medium,
  },
  classCard: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: SPACING.md,
    padding: SPACING.md,
  },
  classTimeContainer: {
    backgroundColor: `${COLORS.primary}15`,
    padding: SPACING.sm,
    borderRadius: 8,
    alignItems: 'center',
    justifyContent: 'center',
    width: 80,
  },
  classTime: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.primary,
  },
  classDuration: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.primary,
    marginTop: 2,
  },
  classInfoContainer: {
    flex: 1,
    marginLeft: SPACING.md,
  },
  classTitle: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.semibold,
    color: COLORS.text,
    marginBottom: 2,
  },
  classTeacher: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.lightText,
  },
  classActionButton: {
    padding: SPACING.xs,
  },
  emptyStateCard: {
    alignItems: 'center',
    padding: SPACING.lg,
  },
  emptyStateText: {
    color: COLORS.lightText,
    marginTop: SPACING.sm,
    textAlign: 'center',
    marginBottom: SPACING.sm,
  },
  coursesScrollView: {
    paddingRight: SPACING.md,
    paddingBottom: SPACING.md,
  },
  courseCard: {
    width: 200,
    backgroundColor: COLORS.white,
    borderRadius: 12,
    overflow: 'hidden',
    ...SHADOWS.sm,
    marginRight: SPACING.md,
  },
  courseImage: {
    width: '100%',
    height: 100,
    backgroundColor: COLORS.muted,
  },
  courseInfo: {
    padding: SPACING.md,
  },
  courseTitle: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.semibold,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  courseInstructor: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.lightText,
    marginBottom: SPACING.md,
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
    overflow: 'hidden',
  },
  progressBarFill: {
    height: '100%',
    backgroundColor: COLORS.primary,
  },
  progressText: {
    fontSize: FONT_SIZE.xs,
    fontWeight: FONT_WEIGHT.semibold,
    color: COLORS.primary,
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  loadingText: {
    marginTop: SPACING.md,
    color: COLORS.text,
    fontSize: FONT_SIZE.md,
  },
  errorContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: SPACING.xl,
  },
  errorText: {
    color: COLORS.error,
    fontSize: FONT_SIZE.md,
    textAlign: 'center',
    marginTop: SPACING.md,
    marginBottom: SPACING.lg,
  },
  retryButton: {
    backgroundColor: COLORS.primary,
    paddingVertical: SPACING.sm,
    paddingHorizontal: SPACING.lg,
    borderRadius: BORDER_RADIUS.lg,
  },
  retryButtonText: {
    color: COLORS.white,
    fontWeight: FONT_WEIGHT.medium,
  },
  actionButton: {
    backgroundColor: COLORS.primary,
    paddingVertical: SPACING.sm,
    paddingHorizontal: SPACING.lg,
    borderRadius: BORDER_RADIUS.lg,
  },
  actionButtonText: {
    color: COLORS.white,
    fontWeight: FONT_WEIGHT.medium,
  },
});

export default HomeScreen;
