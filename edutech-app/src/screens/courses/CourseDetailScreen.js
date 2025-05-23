import React, { useState, useEffect, useContext } from 'react';
import { View, Text, StyleSheet, ScrollView, Image, TouchableOpacity, SafeAreaView, Alert } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT, SHADOWS } from '../../config/theme';
import { Button, Card } from '../../components/common/UIComponents';
import { LoadingState, ErrorState } from '../../components/common/StateComponents';
import { AuthContext } from '../../context/AuthContext';
import { Courses } from '../../services/apiService';

const CourseDetailScreen = ({ route, navigation }) => {
  const { courseId } = route.params;
  const [activeTab, setActiveTab] = useState('contenido');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [courseData, setCourseData] = useState(null);
  const [enrolling, setEnrolling] = useState(false);
  
  const { token } = useContext(AuthContext);
  
  // Cargar detalles del curso
  useEffect(() => {
    const loadCourseDetails = async () => {
      setLoading(true);
      setError(null);
      
      try {
        const data = await Courses.getDetails(token, courseId);
        setCourseData(data);
      } catch (error) {
        console.error('Error cargando detalles del curso:', error);
        setError(error.message || 'No se pudo cargar la información del curso');
      } finally {
        setLoading(false);
      }
    };
    
    loadCourseDetails();
  }, [courseId, token]);
  
  // Función para inscribirse al curso
  const handleEnroll = async () => {
    setEnrolling(true);
    
    try {
      await Courses.enroll(token, courseId);
      Alert.alert(
        "Inscripción Exitosa", 
        "Te has inscrito al curso exitosamente",
        [{ text: "OK", onPress: () => {
          // Recargar los detalles del curso con el estado actualizado
          const loadCourseDetails = async () => {
            try {
              const data = await Courses.getDetails(token, courseId);
              setCourseData(data);
            } catch (error) {
              console.error('Error recargando detalles del curso:', error);
            }
          };
          
          loadCourseDetails();
        }}]
      );
    } catch (error) {
      Alert.alert(
        "Error", 
        error.message || "No se pudo completar la inscripción. Inténtalo más tarde."
      );
    } finally {
      setEnrolling(false);
    }
  };
  
  const renderTabs = () => {
    return (
      <View style={styles.tabsContainer}>
        <TouchableOpacity 
          style={[styles.tab, activeTab === 'contenido' && styles.activeTab]} 
          onPress={() => setActiveTab('contenido')}
        >
          <Text style={[styles.tabText, activeTab === 'contenido' && styles.activeTabText]}>
            Contenido
          </Text>
        </TouchableOpacity>
        
        <TouchableOpacity 
          style={[styles.tab, activeTab === 'materiales' && styles.activeTab]} 
          onPress={() => setActiveTab('materiales')}
        >
          <Text style={[styles.tabText, activeTab === 'materiales' && styles.activeTabText]}>
            Materiales
          </Text>
        </TouchableOpacity>
        
        <TouchableOpacity 
          style={[styles.tab, activeTab === 'evaluaciones' && styles.activeTab]} 
          onPress={() => setActiveTab('evaluaciones')}
        >
          <Text style={[styles.tabText, activeTab === 'evaluaciones' && styles.activeTabText]}>
            Evaluaciones
          </Text>
        </TouchableOpacity>
      </View>
    );
  };
  
  const renderModules = () => {
    if (!courseData || !courseData.modules || courseData.modules.length === 0) {
      return (
        <View style={styles.emptyContentContainer}>
          <Ionicons name="book-outline" size={48} color={COLORS.muted} />
          <Text style={styles.emptyContentText}>No hay contenido disponible para este curso</Text>
        </View>
      );
    }
    
    return courseData.modules.map((module, index) => (
      <Card key={module.id} style={styles.moduleCard}>
        <View style={styles.moduleHeader}>
          <Text style={styles.moduleTitle}>
            Módulo {index + 1}: {module.title}
          </Text>
          <TouchableOpacity>
            <Ionicons 
              name="chevron-down-outline" 
              size={20} 
              color={COLORS.text} 
            />
          </TouchableOpacity>
        </View>
        
        <View style={styles.lessonsList}>
          {module.lessons.map(lesson => (
            <TouchableOpacity 
              key={lesson.id} 
              style={styles.lessonItem}
              disabled={!courseData.isEnrolled}
            >
              <View style={styles.lessonInfo}>
                <View style={styles.lessonStatusContainer}>
                  {lesson.completed ? (
                    <Ionicons name="checkmark-circle" size={20} color={COLORS.success} />
                  ) : (
                    <View style={styles.lessonIncomplete} />
                  )}
                </View>
                
                <View>
                  <Text style={styles.lessonTitle}>{lesson.title}</Text>
                  <Text style={styles.lessonDuration}>
                    <Ionicons name="time-outline" size={14} color={COLORS.muted} />
                    {' '}{lesson.duration}
                  </Text>
                </View>
              </View>
              
              <Ionicons 
                name={courseData.isEnrolled ? "play-circle-outline" : "lock-closed-outline"} 
                size={24} 
                color={courseData.isEnrolled ? COLORS.primary : COLORS.muted} 
              />
            </TouchableOpacity>
          ))}
        </View>
      </Card>
    ));
  };
  
  const renderMaterials = () => {
    if (!courseData || !courseData.materials || courseData.materials.length === 0) {
      return (
        <View style={styles.emptyContentContainer}>
          <Ionicons name="document-outline" size={48} color={COLORS.muted} />
          <Text style={styles.emptyContentText}>No hay materiales disponibles para este curso</Text>
        </View>
      );
    }
    
    return (
      <Card style={styles.materialsCard}>
        <View style={styles.materialsHeader}>
          <Text style={styles.materialsTitle}>Recursos del curso</Text>
        </View>
        
        {courseData.materials.map(material => (
          <TouchableOpacity 
            key={material.id} 
            style={styles.materialItem}
            disabled={!courseData.isEnrolled}
          >
            <View style={styles.materialIcon}>
              <Ionicons 
                name={material.type === 'pdf' ? 'document-text-outline' : 'folder-outline'} 
                size={24} 
                color={courseData.isEnrolled ? COLORS.primary : COLORS.muted} 
              />
            </View>
            
            <View style={styles.materialInfo}>
              <Text style={styles.materialTitle}>{material.title}</Text>
              <Text style={styles.materialMeta}>
                {material.type.toUpperCase()} • {material.size}
              </Text>
            </View>
            
            <TouchableOpacity 
              style={styles.materialDownload}
              disabled={!courseData.isEnrolled}
            >
              <Ionicons 
                name={courseData.isEnrolled ? "download-outline" : "lock-closed-outline"} 
                size={22} 
                color={courseData.isEnrolled ? COLORS.primary : COLORS.muted} 
              />
            </TouchableOpacity>
          </TouchableOpacity>
        ))}
      </Card>
    );
  };
  
  const renderEvaluations = () => {
    if (!courseData || !courseData.evaluations || courseData.evaluations.length === 0) {
      return (
        <View style={styles.emptyContentContainer}>
          <Ionicons name="clipboard-outline" size={48} color={COLORS.muted} />
          <Text style={styles.emptyContentText}>No hay evaluaciones disponibles para este curso</Text>
        </View>
      );
    }
    
    return (
      <Card style={styles.evaluationsCard}>
        <View style={styles.evaluationsHeader}>
          <Text style={styles.evaluationsTitle}>Evaluaciones del curso</Text>
        </View>
        
        {courseData.evaluations.map(evaluation => (
          <TouchableOpacity 
            key={evaluation.id} 
            style={styles.evaluationItem}
            onPress={() => {
              if (courseData.isEnrolled && !evaluation.completed) {
                navigation.navigate('Evaluation', { 
                  evaluationId: evaluation.id,
                  courseId: courseData.id,
                  title: evaluation.title
                });
              }
            }}
            disabled={!courseData.isEnrolled}
          >
            <View style={styles.evaluationStatus}>
              {evaluation.completed ? (
                <View style={styles.evaluationCompleted}>
                  <Text style={styles.evaluationScore}>{evaluation.score}</Text>
                </View>
              ) : (
                <View style={[
                  styles.evaluationPending,
                  !courseData.isEnrolled && styles.evaluationLocked
                ]} />
              )}
            </View>
            
            <View style={styles.evaluationInfo}>
              <Text style={styles.evaluationTitle}>{evaluation.title}</Text>
              
              <View style={styles.evaluationMeta}>
                <Text style={styles.evaluationDate}>
                  <Ionicons name="calendar-outline" size={14} color={COLORS.muted} />
                  {' '}Fecha límite: {evaluation.dueDate}
                </Text>
                
                <Text style={[
                  styles.evaluationState,
                  evaluation.completed ? styles.evaluationStateCompleted : 
                  courseData.isEnrolled ? styles.evaluationStatePending : styles.evaluationStateLocked
                ]}>
                  {evaluation.completed ? 'Completado' : 
                   courseData.isEnrolled ? 'Pendiente' : 'Bloqueado'}
                </Text>
              </View>
            </View>
            
            <Ionicons 
              name={evaluation.completed ? "checkmark-circle-outline" : 
                   courseData.isEnrolled ? "create-outline" : "lock-closed-outline"} 
              size={24} 
              color={evaluation.completed ? COLORS.success : 
                   courseData.isEnrolled ? COLORS.primary : COLORS.muted} 
            />
          </TouchableOpacity>
        ))}
      </Card>
    );
  };
  
  // Si está cargando, mostrar estado de carga
  if (loading) {
    return <LoadingState message="Cargando detalles del curso..." />;
  }
  
  // Si hay un error, mostrar estado de error
  if (error) {
    return <ErrorState message={error} onRetry={() => navigation.goBack()} />;
  }
  
  // Si no hay datos del curso, mostrar mensaje
  if (!courseData) {
    return <ErrorState message="No se encontró información del curso" onRetry={() => navigation.goBack()} />;
  }
  
  return (
    <SafeAreaView style={styles.container}>
      <ScrollView showsVerticalScrollIndicator={false}>
        <View style={styles.header}>
          <TouchableOpacity 
            style={styles.backButton}
            onPress={() => navigation.goBack()}
          >
            <Ionicons name="arrow-back" size={24} color={COLORS.white} />
          </TouchableOpacity>
        </View>
        
        <Image 
          source={courseData.imageUrl ? { uri: courseData.imageUrl } : require('../../../assets/images/course-placeholder.png')} 
          style={styles.courseImage} 
        />
        
        <View style={styles.courseInfo}>
          <View style={styles.courseHeader}>
            <Text style={styles.courseCategory}>{courseData.category}</Text>
            
            <View style={styles.courseRating}>
              <Ionicons name="star" size={18} color={COLORS.warning} />
              <Text style={styles.ratingText}>{courseData.rating.toFixed(1)}</Text>
              <Text style={styles.reviewsText}>({courseData.reviews} reseñas)</Text>
            </View>
          </View>
          
          <Text style={styles.courseTitle}>{courseData.title}</Text>
          <Text style={styles.instructorName}>{courseData.instructor}</Text>
          
          <View style={styles.courseStats}>
            <View style={styles.statItem}>
              <Ionicons name="people-outline" size={20} color={COLORS.primary} />
              <Text style={styles.statText}>{courseData.students} estudiantes</Text>
            </View>
            
            <View style={styles.statItem}>
              <Ionicons name="time-outline" size={20} color={COLORS.primary} />
              <Text style={styles.statText}>{courseData.duration}</Text>
            </View>
            
            <View style={styles.statItem}>
              <Ionicons name="school-outline" size={20} color={COLORS.primary} />
              <Text style={styles.statText}>{courseData.level}</Text>
            </View>
          </View>
          
          {courseData.isEnrolled ? (
            <View style={styles.progressContainer}>
              <View style={styles.progressHeader}>
                <Text style={styles.progressTitle}>Tu progreso</Text>
                <Text style={styles.progressPercentage}>{courseData.progress}%</Text>
              </View>
              
              <View style={styles.progressBarContainer}>
                <View 
                  style={[
                    styles.progressBar, 
                    { width: `${courseData.progress}%` }
                  ]} 
                />
              </View>
              
              <Button 
                title="Continuar curso" 
                onPress={() => {
                  // Lógica para continuar con la última lección
                }}
                style={styles.continueButton}
              />
            </View>
          ) : (
            <Button 
              title={enrolling ? "Inscribiéndose..." : "Inscribirse ahora"} 
              onPress={handleEnroll}
              disabled={enrolling}
              style={styles.enrollButton}
            />
          )}
          
          <Card style={styles.descriptionCard}>
            <Text style={styles.descriptionTitle}>Descripción del curso</Text>
            <Text style={styles.descriptionText}>{courseData.description}</Text>
          </Card>
          
          {renderTabs()}
          
          <View style={styles.tabContent}>
            {activeTab === 'contenido' && renderModules()}
            {activeTab === 'materiales' && renderMaterials()}
            {activeTab === 'evaluaciones' && renderEvaluations()}
          </View>
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
  header: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    zIndex: 10,
    paddingTop: 20,
    paddingHorizontal: SPACING.md,
  },
  backButton: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: 'rgba(0,0,0,0.3)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  courseImage: {
    width: '100%',
    height: 200,
    resizeMode: 'cover',
  },
  courseInfo: {
    paddingTop: SPACING.md,
    paddingHorizontal: SPACING.md,
  },
  courseHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: SPACING.xs,
  },
  courseCategory: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.primary,
    fontWeight: FONT_WEIGHT.medium,
  },
  courseRating: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  ratingText: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginLeft: 4,
  },
  reviewsText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
    marginLeft: 4,
  },
  courseTitle: {
    fontSize: FONT_SIZE.xl,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  instructorName: {
    fontSize: FONT_SIZE.md,
    color: COLORS.muted,
    marginBottom: SPACING.md,
  },
  courseStats: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: SPACING.lg,
  },
  statItem: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  statText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
    marginLeft: SPACING.xs,
  },
  progressContainer: {
    marginBottom: SPACING.lg,
  },
  progressHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: SPACING.xs,
  },
  progressTitle: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.text,
  },
  progressPercentage: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.primary,
  },
  progressBarContainer: {
    height: 8,
    backgroundColor: COLORS.lightGray,
    borderRadius: 4,
    marginBottom: SPACING.md,
  },
  progressBar: {
    height: '100%',
    backgroundColor: COLORS.primary,
    borderRadius: 4,
  },
  continueButton: {
    marginBottom: SPACING.md,
  },
  enrollButton: {
    marginBottom: SPACING.lg,
  },
  descriptionCard: {
    marginBottom: SPACING.lg,
    padding: SPACING.md,
  },
  descriptionTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.md,
  },
  descriptionText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    lineHeight: 22,
  },
  tabsContainer: {
    flexDirection: 'row',
    marginBottom: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  tab: {
    flex: 1,
    paddingVertical: SPACING.md,
    alignItems: 'center',
  },
  activeTab: {
    borderBottomWidth: 2,
    borderBottomColor: COLORS.primary,
  },
  tabText: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.muted,
  },
  activeTabText: {
    color: COLORS.primary,
  },
  tabContent: {
    marginBottom: SPACING.xl,
  },
  moduleCard: {
    marginBottom: SPACING.md,
  },
  moduleHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  moduleTitle: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
  },
  lessonsList: {
    paddingTop: SPACING.xs,
  },
  lessonItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  lessonInfo: {
    flexDirection: 'row',
    alignItems: 'center',
    flex: 1,
  },
  lessonStatusContainer: {
    marginRight: SPACING.sm,
  },
  lessonIncomplete: {
    width: 20,
    height: 20,
    borderRadius: 10,
    borderWidth: 2,
    borderColor: COLORS.lightGray,
  },
  lessonTitle: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    marginBottom: 2,
  },
  lessonDuration: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
  },
  materialsCard: {
    marginBottom: SPACING.md,
  },
  materialsHeader: {
    padding: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  materialsTitle: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
  },
  materialItem: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  materialIcon: {
    marginRight: SPACING.md,
  },
  materialInfo: {
    flex: 1,
  },
  materialTitle: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    marginBottom: 2,
  },
  materialMeta: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
  },
  materialDownload: {
    padding: SPACING.xs,
  },
  evaluationsCard: {
    marginBottom: SPACING.md,
  },
  evaluationsHeader: {
    padding: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  evaluationsTitle: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
  },
  evaluationItem: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  evaluationStatus: {
    marginRight: SPACING.md,
  },
  evaluationCompleted: {
    width: 28,
    height: 28,
    borderRadius: 14,
    backgroundColor: COLORS.success,
    justifyContent: 'center',
    alignItems: 'center',
  },
  evaluationScore: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.white,
  },
  evaluationPending: {
    width: 28,
    height: 28,
    borderRadius: 14,
    borderWidth: 2,
    borderColor: COLORS.primary,
  },
  evaluationLocked: {
    borderColor: COLORS.muted,
  },
  evaluationInfo: {
    flex: 1,
  },
  evaluationTitle: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    marginBottom: 2,
  },
  evaluationMeta: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  evaluationDate: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
  },
  evaluationState: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.medium,
  },
  evaluationStateCompleted: {
    color: COLORS.success,
  },
  evaluationStatePending: {
    color: COLORS.primary,
  },
  evaluationStateLocked: {
    color: COLORS.muted,
  },
  emptyContentContainer: {
    alignItems: 'center',
    justifyContent: 'center',
    padding: SPACING.xl,
  },
  emptyContentText: {
    marginTop: SPACING.md,
    fontSize: FONT_SIZE.md,
    color: COLORS.muted,
    textAlign: 'center',
  },
});

export default CourseDetailScreen;
