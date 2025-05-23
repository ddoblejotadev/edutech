import React, { useState, useEffect, useContext, useCallback } from 'react';
import { View, StyleSheet, FlatList, Image, TouchableOpacity, TextInput, ActivityIndicator } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT, SHADOWS, BORDER_RADIUS } from '../../config/theme';
import { Card, Button, Text } from '../../components/common/UIComponents';
import { LoadingState, ErrorState, EmptyState } from '../../components/common/StateComponents';
import { AuthContext } from '../../context/AuthContext';
import Header from '../../components/common/Header';
import { Courses } from '../../services/apiService';

const CoursesScreen = ({ navigation }) => {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [courses, setCourses] = useState([]);
  const [filteredCourses, setFilteredCourses] = useState([]);
  const [categories, setCategories] = useState([]);
  const [error, setError] = useState(null);
  
  const { token } = useContext(AuthContext);
  
  // Función para cargar cursos desde la API
  const loadCourses = useCallback(async () => {
    if (refreshing) return;
    
    setLoading(true);
    setError(null);
    
    try {
      // Obtener cursos desde la API
      const response = await Courses.getAll(token);
      
      setCourses(response);
      setFilteredCourses(response);
      
      // Extraer categorías únicas
      const uniqueCategories = [...new Set(response.map(course => course.category))];
      setCategories(uniqueCategories);
      
    } catch (error) {
      console.error('Error cargando cursos:', error);
      setError(error.message || 'No se pudieron cargar los cursos. Inténtalo nuevamente.');
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  }, [token, refreshing]);
  
  // Cargar cursos al montar el componente
  useEffect(() => {
    loadCourses();
  }, [loadCourses]);
  
  // Manejar la búsqueda
  const handleSearch = (query) => {
    setSearchQuery(query);
    
    // Filtrar cursos
    const filtered = courses.filter(course => {
      // Filtrar por búsqueda
      const matchesSearch = course.title.toLowerCase().includes(query.toLowerCase()) || 
                          course.description.toLowerCase().includes(query.toLowerCase());
      
      // Filtrar por categoría
      const matchesCategory = selectedCategory ? course.category === selectedCategory : true;
      
      return matchesSearch && matchesCategory;
    });
    
    setFilteredCourses(filtered);
  };
  
  // Manejar la selección de categorías
  const handleCategorySelect = (category) => {
    const newCategory = selectedCategory === category ? null : category;
    setSelectedCategory(newCategory);
    
    // Filtrar cursos por la categoría seleccionada y búsqueda actual
    const filtered = courses.filter(course => {
      const matchesSearch = course.title.toLowerCase().includes(searchQuery.toLowerCase()) || 
                          course.description.toLowerCase().includes(searchQuery.toLowerCase());
      
      const matchesCategory = newCategory ? course.category === newCategory : true;
      
      return matchesSearch && matchesCategory;
    });
    
    setFilteredCourses(filtered);
  };
  
  // Manejar la actualización de la lista
  const handleRefresh = () => {
    setRefreshing(true);
    loadCourses();
  };
  
  const renderCategoryChips = () => {
    return (
      <FlatList
        horizontal
        data={categories}
        keyExtractor={(item) => item}
        renderItem={({ item }) => (
          <TouchableOpacity
            style={[
              styles.categoryChip,
              selectedCategory === item && styles.selectedCategoryChip
            ]}
            onPress={() => handleCategorySelect(item)}
          >
            <Text 
              style={[
                styles.categoryChipText,
                selectedCategory === item && styles.selectedCategoryChipText
              ]}
            >
              {item}
            </Text>
          </TouchableOpacity>
        )}
        showsHorizontalScrollIndicator={false}
        contentContainerStyle={styles.categoriesContainer}
      />
    );
  };
  
  const renderCourseItem = ({ item }) => (
    <Card 
      onPress={() => navigation.navigate('CourseDetail', { courseId: item.id })}
      style={styles.courseCard}
      elevation="sm"
    >
      <Image 
        source={item.imageUrl ? { uri: item.imageUrl } : require('../../../assets/images/course-placeholder.png')} 
        style={styles.courseImage} 
      />
      
      <View style={styles.courseContent}>
        <View style={styles.courseHeader}>
          <Text style={styles.courseCategory}>{item.category}</Text>
          <View style={styles.ratingContainer}>
            <Ionicons name="star" size={16} color={COLORS.warning} />
            <Text style={styles.ratingText}>{item.rating}</Text>
          </View>
        </View>
        
        <Text style={styles.courseTitle} numberOfLines={2}>{item.title}</Text>
        <Text style={styles.courseInstructor}>{item.instructor}</Text>
        
        <Text style={styles.courseDescription} numberOfLines={2}>
          {item.description}
        </Text>
        
        <View style={styles.courseFooter}>
          <View style={styles.courseInfo}>
            <View style={styles.infoItem}>
              <Ionicons name="time-outline" size={14} color={COLORS.muted} />
              <Text style={styles.infoText}>{item.duration}</Text>
            </View>
            
            <View style={styles.infoItem}>
              <Ionicons name="people-outline" size={14} color={COLORS.muted} />
              <Text style={styles.infoText}>{item.students} estudiantes</Text>
            </View>
          </View>
          
          <View style={styles.levelBadge}>
            <Text style={styles.levelText}>{item.level}</Text>
          </View>
        </View>
      </View>
    </Card>
  );
  
  // Si hay un error, mostrar pantalla de error
  if (error && !loading) {
    return <ErrorState message={error} onRetry={loadCourses} />;
  }
  
  // Si está cargando, mostrar pantalla de carga
  if (loading && !refreshing) {
    return <LoadingState message="Cargando cursos..." />;
  }
  
  return (
    <View style={styles.container}>
      <Header 
        title="Cursos" 
        showBackButton={false}
      />
      
      <View style={styles.searchContainer}>
        <View style={styles.searchBarContainer}>
          <Ionicons name="search-outline" size={20} color={COLORS.muted} style={styles.searchIcon} />
          <TextInput 
            style={styles.searchInput}
            placeholder="Buscar cursos..."
            placeholderTextColor={COLORS.muted}
            value={searchQuery}
            onChangeText={handleSearch}
          />
          {searchQuery ? (
            <TouchableOpacity 
              onPress={() => handleSearch('')}
              style={styles.clearIcon}
            >
              <Ionicons name="close-circle" size={18} color={COLORS.muted} />
            </TouchableOpacity>
          ) : null}
        </View>
      </View>
      
      {renderCategoryChips()}
      
      <FlatList
        data={filteredCourses}
        keyExtractor={(item) => item.id.toString()}
        renderItem={renderCourseItem}
        contentContainerStyle={styles.coursesList}
        showsVerticalScrollIndicator={false}
        refreshing={refreshing}
        onRefresh={handleRefresh}
        ListEmptyComponent={
          <EmptyState 
            message="No se encontraron cursos"
            icon="school-outline"
            actionText={selectedCategory || searchQuery ? "Limpiar filtros" : null}
            onAction={selectedCategory || searchQuery ? 
              () => {
                setSearchQuery('');
                setSelectedCategory(null);
                setFilteredCourses(courses);
              } : null
            }
          />
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
  searchContainer: {
    padding: SPACING.md,
    paddingBottom: SPACING.sm,
  },
  searchBarContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: COLORS.white,
    borderRadius: BORDER_RADIUS.lg,
    paddingHorizontal: SPACING.md,
    paddingVertical: SPACING.sm,
    ...SHADOWS.sm,
  },
  searchIcon: {
    marginRight: SPACING.sm,
  },
  searchInput: {
    flex: 1,
    color: COLORS.text,
    fontSize: FONT_SIZE.md,
  },
  clearIcon: {
    padding: SPACING.xs,
  },
  categoriesContainer: {
    paddingHorizontal: SPACING.md,
    marginBottom: SPACING.md,
  },
  categoryChip: {
    paddingHorizontal: SPACING.md,
    paddingVertical: SPACING.xs,
    borderRadius: BORDER_RADIUS.xl,
    backgroundColor: COLORS.lightGray,
    marginRight: SPACING.sm,
  },
  selectedCategoryChip: {
    backgroundColor: COLORS.primary,
  },
  categoryChipText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
  },
  selectedCategoryChipText: {
    color: COLORS.white,
  },
  coursesList: {
    padding: SPACING.md,
  },
  courseCard: {
    marginBottom: SPACING.md,
    overflow: 'hidden',
  },
  courseImage: {
    height: 120,
    width: '100%',
    resizeMode: 'cover',
  },
  courseContent: {
    padding: SPACING.md,
  },
  courseHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: SPACING.xs,
  },
  courseCategory: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.primary,
    fontWeight: FONT_WEIGHT.medium,
  },
  ratingContainer: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  ratingText: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.text,
    marginLeft: SPACING.xs,
  },
  courseTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    marginBottom: SPACING.xs,
    color: COLORS.text,
  },
  courseInstructor: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
    marginBottom: SPACING.sm,
  },
  courseDescription: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
    marginBottom: SPACING.md,
  },
  courseFooter: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  courseInfo: {
    flexDirection: 'row',
  },
  infoItem: {
    flexDirection: 'row',
    alignItems: 'center',
    marginRight: SPACING.md,
  },
  infoText: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.muted,
    marginLeft: SPACING.xs,
  },
  levelBadge: {
    backgroundColor: COLORS.primaryLight,
    paddingHorizontal: SPACING.sm,
    paddingVertical: SPACING.xs,
    borderRadius: BORDER_RADIUS.sm,
  },
  levelText: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.primary,
    fontWeight: FONT_WEIGHT.medium,
  },
});

export default CoursesScreen;
