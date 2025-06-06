import React, { useState, useEffect } from 'react';
import { 
  View, 
  Text,
  StyleSheet, 
  FlatList, 
  TouchableOpacity, 
  TextInput, 
  ActivityIndicator,
  Alert,
  RefreshControl,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT } from '../../config/theme';
import { Card } from '../../components/common/UIComponents';
import { StudentApiService } from '../../services/studentApiService';

const CoursesScreen = ({ navigation }) => {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [courses, setCourses] = useState([]);
  const [filteredCourses, setFilteredCourses] = useState([]);

  useEffect(() => {
    loadCourses();
  }, []);

  useEffect(() => {
    filterCourses();
  }, [searchQuery, selectedCategory, courses]);

  const loadCourses = async () => {
    try {
      setLoading(true);
      console.log('🔄 Iniciando carga de cursos...');
      const response = await StudentApiService.getCourses('demo-token');
      console.log('📊 Respuesta del servicio:', response);
      
      if (response && response.success && response.data) {
        console.log('✅ Cursos cargados exitosamente:', response.data.length, 'cursos');
        setCourses(response.data);
        setFilteredCourses(response.data);
      } else {
        console.warn('⚠️ Respuesta inválida del servicio');
        throw new Error('Respuesta inválida del servicio');
      }
    } catch (error) {
      console.error('❌ Error en loadCourses:', error);
      Alert.alert(
        'Error', 
        'No se pudieron cargar los cursos. Usando datos demo.',
        [{ text: 'OK' }]
      );
      // Datos de respaldo locales
      const fallbackCourses = [
        {
          id: 1,
          title: 'Algoritmos y Estructuras de Datos',
          code: 'CC3001',
          instructor: 'Prof. María González',
          rating: 4.5,
          students: 850,
          duration: '1 semestre',
          level: 'Intermedio',
          category: 'programming',
          isEnrolled: true,
          progress: 65,
          credits: 6,
          schedule: 'Lun-Mié-Vie 10:00-11:30'
        }
      ];
      setCourses(fallbackCourses);
      setFilteredCourses(fallbackCourses);
    } finally {
      setLoading(false);
    }
  };

  const onRefresh = async () => {
    setRefreshing(true);
    await loadCourses();
    setRefreshing(false);
  };

  const filterCourses = () => {
    let filtered = courses;

    if (selectedCategory !== 'all') {
      filtered = filtered.filter(course => course.category === selectedCategory);
    }

    if (searchQuery.trim()) {
      filtered = filtered.filter(course =>
        course.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
        course.description.toLowerCase().includes(searchQuery.toLowerCase()) ||
        course.instructor.toLowerCase().includes(searchQuery.toLowerCase())
      );
    }

    setFilteredCourses(filtered);
  };

  const handleCoursePress = (course) => {
    navigation.navigate('CourseDetail', { courseId: course.id });
  };

  const getProgressColor = (progress) => {
    if (progress >= 80) return COLORS.success;
    if (progress >= 50) return COLORS.warning;
    return COLORS.error;
  };

  const categories = [
    { key: 'all', label: 'Todos', icon: 'grid' },
    { key: 'programming', label: 'Programación', icon: 'code-slash' },
    { key: 'mathematics', label: 'Matemáticas', icon: 'calculator' },
    { key: 'science', label: 'Ciencias', icon: 'flask' },
    { key: 'languages', label: 'Idiomas', icon: 'language' },
  ];

  const renderCategoryButton = (category) => (
    <TouchableOpacity
      key={category.key}
      style={[
        styles.categoryButton,
        selectedCategory === category.key && styles.categoryButtonActive
      ]}
      onPress={() => setSelectedCategory(category.key)}
    >
      <Ionicons
        name={category.icon}
        size={16}
        color={selectedCategory === category.key ? COLORS.white : COLORS.primary}
      />
      <Text style={[
        styles.categoryButtonText,
        selectedCategory === category.key && styles.categoryButtonTextActive
      ]}>
        {category.label}
      </Text>
    </TouchableOpacity>
  );

  const renderCourseCard = ({ item }) => (
    <Card style={styles.courseCard} onPress={() => handleCoursePress(item)}>
      <View style={styles.courseHeader}>
        <View style={styles.courseCodeContainer}>
          <Text style={styles.courseCode}>{item.code}</Text>
          <Text style={styles.courseCredits}>{item.credits} créditos</Text>
        </View>
        {item.isEnrolled && (
          <View style={styles.enrolledBadge}>
            <Text style={styles.enrolledText}>Inscrito</Text>
          </View>
        )}
      </View>
      
      <View style={styles.courseContent}>
        <Text style={styles.courseTitle} numberOfLines={2}>{item.title}</Text>
        <Text style={styles.courseInstructor}>{item.instructor}</Text>
        
        <View style={styles.courseDetails}>
          <View style={styles.detailItem}>
            <Ionicons name="calendar-outline" size={14} color={COLORS.muted} />
            <Text style={styles.detailText}>{item.schedule}</Text>
          </View>
          
          <View style={styles.detailItem}>
            <Ionicons name="time-outline" size={14} color={COLORS.muted} />
            <Text style={styles.detailText}>{item.duration}</Text>
          </View>
        </View>
        
        <View style={styles.courseStats}>
          <View style={styles.statItem}>
            <Ionicons name="star" size={14} color={COLORS.warning} />
            <Text style={styles.statText}>{item.rating}</Text>
          </View>
          
          <View style={styles.statItem}>
            <Ionicons name="people" size={14} color={COLORS.muted} />
            <Text style={styles.statText}>{item.students}</Text>
          </View>
          
          <View style={styles.statItem}>
            <Ionicons name="school" size={14} color={COLORS.muted} />
            <Text style={styles.statText}>{item.level}</Text>
          </View>
        </View>
        
        {item.isEnrolled && item.progress !== undefined && (
          <View style={styles.progressContainer}>
            <View style={styles.progressHeader}>
              <Text style={styles.progressLabel}>Progreso</Text>
              <Text style={styles.progressPercentage}>{item.progress}%</Text>
            </View>
            <View style={styles.progressBar}>
              <View 
                style={[
                  styles.progressFill, 
                  { 
                    width: `${item.progress}%`,
                    backgroundColor: getProgressColor(item.progress)
                  }
                ]} 
              />
            </View>
          </View>
        )}
      </View>
    </Card>
  );

  if (loading) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color={COLORS.primary} />
        <Text style={styles.loadingText}>Cargando cursos...</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.headerTitle}>Mis Cursos</Text>
        
        <View style={styles.searchContainer}>
          <Ionicons name="search" size={20} color={COLORS.muted} style={styles.searchIcon} />
          <TextInput
            style={styles.searchInput}
            placeholder="Buscar cursos..."
            value={searchQuery}
            onChangeText={setSearchQuery}
            placeholderTextColor={COLORS.muted}
          />
        </View>
      </View>

      <View style={styles.categoriesContainer}>
        <FlatList
          horizontal
          showsHorizontalScrollIndicator={false}
          data={categories}
          renderItem={({ item }) => renderCategoryButton(item)}
          keyExtractor={(item) => item.key}
          contentContainerStyle={styles.categoriesList}
        />
      </View>

      <FlatList
        data={filteredCourses}
        renderItem={renderCourseCard}
        keyExtractor={(item) => item.id.toString()}
        contentContainerStyle={styles.coursesList}
        refreshControl={
          <RefreshControl
            refreshing={refreshing}
            onRefresh={onRefresh}
            colors={[COLORS.primary]}
          />
        }
        ListEmptyComponent={
          <View style={styles.emptyContainer}>
            <Ionicons name="book-outline" size={64} color={COLORS.muted} />
            <Text style={styles.emptyText}>No se encontraron cursos</Text>
            <Text style={styles.emptySubtext}>
              {searchQuery ? 'Intenta con otros términos de búsqueda' : 'No hay cursos disponibles en esta categoría'}
            </Text>
          </View>
        }
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  loadingText: {
    marginTop: SPACING.md,
    color: COLORS.text,
  },
  header: {
    backgroundColor: COLORS.white,
    padding: SPACING.lg,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.border,
  },
  headerTitle: {
    fontSize: FONT_SIZE.xl,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.md,
  },
  searchContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: COLORS.lightGray,
    borderRadius: 8,
    paddingHorizontal: SPACING.md,
  },
  searchIcon: {
    marginRight: SPACING.sm,
  },
  searchInput: {
    flex: 1,
    paddingVertical: SPACING.sm,
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
  },
  categoriesContainer: {
    backgroundColor: COLORS.white,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.border,
  },
  categoriesList: {
    paddingHorizontal: SPACING.lg,
    paddingVertical: SPACING.md,
  },
  categoryButton: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: SPACING.md,
    paddingVertical: SPACING.sm,
    marginRight: SPACING.sm,
    borderRadius: 20,
    borderWidth: 1,
    borderColor: COLORS.primary,
    backgroundColor: COLORS.white,
  },
  categoryButtonActive: {
    backgroundColor: COLORS.primary,
  },
  categoryButtonText: {
    color: COLORS.primary,
    marginLeft: SPACING.xs,
    fontSize: FONT_SIZE.sm,
  },
  categoryButtonTextActive: {
    color: COLORS.white,
  },
  coursesList: {
    padding: SPACING.lg,
  },
  courseCard: {
    marginBottom: SPACING.lg,
    overflow: 'hidden',
  },
  courseHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    marginBottom: SPACING.sm,
  },
  courseCodeContainer: {
    flex: 1,
  },
  courseCode: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.primary,
    marginBottom: 2,
  },
  courseCredits: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.muted,
  },
  enrolledBadge: {
    backgroundColor: COLORS.success,
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 12,
  },
  enrolledText: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.white,
    fontWeight: FONT_WEIGHT.medium,
  },
  courseContent: {
    padding: SPACING.md,
  },
  courseTitle: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  courseInstructor: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
    marginBottom: SPACING.md,
  },
  courseDetails: {
    marginVertical: SPACING.sm,
  },
  detailItem: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: SPACING.xs,
  },
  detailText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
    marginLeft: SPACING.xs,
  },
  courseStats: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: SPACING.md,
  },
  statItem: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  statText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
    marginLeft: SPACING.xs,
  },
  progressContainer: {
    marginTop: SPACING.sm,
  },
  progressHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: SPACING.xs,
  },
  progressLabel: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
  },
  progressPercentage: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.primary,
  },
  progressBar: {
    flex: 1,
    height: 6,
    backgroundColor: COLORS.lightGray,
    borderRadius: 3,
    marginRight: SPACING.sm,
  },
  progressFill: {
    height: '100%',
    borderRadius: 3,
  },
  emptyContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingTop: SPACING.xl * 2,
  },
  emptyText: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginTop: SPACING.lg,
    marginBottom: SPACING.sm,
  },
  emptySubtext: {
    fontSize: FONT_SIZE.md,
    color: COLORS.muted,
    textAlign: 'center',
    paddingHorizontal: SPACING.xl,
  },
});

export default CoursesScreen;
