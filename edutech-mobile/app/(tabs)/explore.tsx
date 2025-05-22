import { View, TouchableOpacity, ScrollView, RefreshControl } from 'react-native';
import { router, useLocalSearchParams } from 'expo-router';
import { useState, useCallback, useEffect } from 'react';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { Ionicons } from '@expo/vector-icons';

import { ThemedText } from '@/components/ThemedText';
import { ThemedView } from '@/components/ThemedView';
import { SearchBar } from '@/components/ui/SearchBar';
import { CourseList, CourseItem } from '@/components/ui/CourseList';
import { Button } from '@/components/ui/Button';
import { Colors } from '@/constants/Colors';
import { useColorScheme } from '@/hooks/useColorScheme';
import { styles } from './exploreStyles';

// Datos de ejemplo para mostrar
const ALL_COURSES: CourseItem[] = [
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
  },
  {
    id: '6',
    title: 'Introducción a la Ciberseguridad',
    description: 'Fundamentos de seguridad informática y protección de datos',
    imageUri: 'https://images.unsplash.com/photo-1563013544-824ae1b704d3?ixlib=rb-4.0.3&auto=format&fit=crop&w=1470&q=80',
    instructor: 'Miguel Ángel Pérez',
    category: 'Seguridad',
    duration: '6 semanas'
  },
  {
    id: '7',
    title: 'Desarrollo Mobile con React Native',
    description: 'Crea aplicaciones móviles nativas con JavaScript y React',
    imageUri: 'https://images.unsplash.com/photo-1551650975-87deedd944c3?ixlib=rb-4.0.3&auto=format&fit=crop&w=1472&q=80',
    instructor: 'Lucía Martínez',
    category: 'Desarrollo',
    duration: '10 semanas'
  }
];

// Categorías de cursos
const CATEGORIES = [
  { id: 'diseño', name: 'Diseño', color: '#00E5B3', icon: 'brush' },
  { id: 'desarrollo', name: 'Desarrollo', color: '#0066FF', icon: 'laptop' },
  { id: 'ia', name: 'IA', color: '#FFB800', icon: 'analytics' },
  { id: 'seguridad', name: 'Seguridad', color: '#FF3B5F', icon: 'shield' },
  { id: 'datos', name: 'Datos', color: '#0DE57A', icon: 'bar-chart' },
  { id: 'negocios', name: 'Negocios', color: '#A66CFF', icon: 'briefcase' }
];

export default function ExploreScreen() {
  const { search } = useLocalSearchParams();
  const [refreshing, setRefreshing] = useState(false);
  const [searchQuery, setSearchQuery] = useState(search ? String(search) : '');
  const [selectedCategories, setSelectedCategories] = useState<string[]>([]);
  const [filteredCourses, setFilteredCourses] = useState<CourseItem[]>(ALL_COURSES);
  const insets = useSafeAreaInsets();
  
  const colorScheme = useColorScheme();
  const colors = Colors[colorScheme ?? 'light'];
  
  const filterCourses = useCallback(() => {
    let filtered = [...ALL_COURSES];
    
    // Filtrar por texto de búsqueda
    if (searchQuery) {
      const query = searchQuery.toLowerCase();
      filtered = filtered.filter(
        course => 
          course.title.toLowerCase().includes(query) || 
          course.description?.toLowerCase().includes(query) ||
          course.instructor?.toLowerCase().includes(query) ||
          course.category?.toLowerCase().includes(query)
      );
    }
    
    // Filtrar por categorías seleccionadas
    if (selectedCategories.length > 0) {
      filtered = filtered.filter(course => 
        selectedCategories.some(cat => 
          course.category?.toLowerCase().includes(cat.toLowerCase())
        )
      );
    }
    
    setFilteredCourses(filtered);
  }, [searchQuery, selectedCategories]);
  
  // Aplicar filtros cuando cambia la búsqueda o categorías
  useEffect(() => {
    filterCourses();
  }, [filterCourses]);
  
  // Inicializar búsqueda desde parámetros
  useEffect(() => {
    if (search) {
      setSearchQuery(String(search));
    }
  }, [search]);
  
  const toggleCategory = (categoryId: string) => {
    setSelectedCategories(prev => {
      if (prev.includes(categoryId)) {
        return prev.filter(id => id !== categoryId);
      } else {
        return [...prev, categoryId];
      }
    });
  };
  
  const clearFilters = () => {
    setSelectedCategories([]);
    setSearchQuery('');
  };

  const onRefresh = useCallback(() => {
    setRefreshing(true);
    // Simular carga de datos
    setTimeout(() => {
      filterCourses();
      setRefreshing(false);
    }, 1000);
  }, [filterCourses]);

  const handleCoursePress = (course: CourseItem) => {
    router.push({
      pathname: '/curso/[id]',
      params: { id: course.id }
    });
  };

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
            <ThemedText type="heading1">
              Explorar
            </ThemedText>
          </View>
          
          <SearchBar
            placeholder="Buscar cursos, instructores..."
            value={searchQuery}
            onChangeText={setSearchQuery}
            onSearch={(query) => setSearchQuery(query)}
            containerStyle={styles.searchBar}
          />
        </View>

        {/* Filtros activos */}
        {(selectedCategories.length > 0 || searchQuery) && (
          <View style={styles.filterContainer}>
            {selectedCategories.map(catId => {
              const category = CATEGORIES.find(c => c.id === catId);
              return (
                <TouchableOpacity
                  key={catId}
                  style={[
                    styles.filterChip,
                    { backgroundColor: colors.backgroundSecondary }
                  ]}
                  onPress={() => toggleCategory(catId)}
                  activeOpacity={0.7}
                >
                  <Ionicons name="close" size={14} color={colors.text} />
                  <ThemedText type="caption" style={styles.filterChipText}>
                    {category?.name || catId}
                  </ThemedText>
                </TouchableOpacity>
              );
            })}
            
            {searchQuery && (
              <TouchableOpacity
                style={[
                  styles.filterChip,
                  { backgroundColor: colors.backgroundSecondary }
                ]}
                onPress={() => setSearchQuery('')}
                activeOpacity={0.7}
              >
                <Ionicons name="close" size={14} color={colors.text} />
                <ThemedText type="caption" style={styles.filterChipText}>
                  &quot;{searchQuery}&quot;
                </ThemedText>
              </TouchableOpacity>
            )}
            
            <TouchableOpacity
              style={[
                styles.filterChip,
                { backgroundColor: colors.tint }
              ]}
              onPress={clearFilters}
              activeOpacity={0.7}
            >
              <ThemedText type="caption" style={[styles.filterChipText, { color: '#FFFFFF' }]}>
                Limpiar filtros
              </ThemedText>
            </TouchableOpacity>
          </View>
        )}

        {/* Categorías */}
        {!searchQuery && selectedCategories.length === 0 && (
          <View style={styles.categoriesContainer}>
            <ThemedText type="heading2" style={styles.categoryTitle}>
              Categorías
            </ThemedText>
            
            <View style={styles.categoriesGrid}>
              {CATEGORIES.map(category => (
                <TouchableOpacity
                  key={category.id}
                  style={[
                    styles.categoryItem,
                    { backgroundColor: category.color + '22' } // Agregar transparencia
                  ]}
                  onPress={() => toggleCategory(category.id)}                  activeOpacity={0.7}
                >
                  <Ionicons name={category.icon as any} size={28} color={category.color} />
                  <ThemedText type="bodyBold">{category.name}</ThemedText>
                </TouchableOpacity>
              ))}
            </View>
          </View>
        )}

        {/* Resultados */}
        <View style={styles.resultSection}>
          <View style={styles.resultHeader}>
            <ThemedText type="heading2">
              {searchQuery || selectedCategories.length > 0 
                ? `Resultados (${filteredCourses.length})` 
                : 'Todos los cursos'}
            </ThemedText>
            
            <TouchableOpacity
              style={styles.sortButton}
              activeOpacity={0.7}
            >
              <ThemedText style={styles.sortButtonText}>
                Ordenar
              </ThemedText>
              <Ionicons name="swap-vertical" size={14} color={colors.text} />
            </TouchableOpacity>
          </View>
          
          {filteredCourses.length > 0 ? (
            <CourseList
              data={filteredCourses}
              onCoursePress={handleCoursePress}
              emptyMessage="No se encontraron cursos"
              horizontal={false}
              compact={false}
            />
          ) : (
            <View style={styles.emptyContainer}>
              <Ionicons name="help-circle" size={80} color={colors.icon} style={styles.emptyImage} />
              <ThemedText type="heading3" style={styles.emptyTitle}>
                No se encontraron cursos
              </ThemedText>
              <ThemedText type="body" secondary={true} style={styles.emptyText}>
                Intenta con otra búsqueda o filtros diferentes
              </ThemedText>
              <Button 
                title="Limpiar filtros" 
                onPress={clearFilters} 
                variant="primary"
              />
            </View>
          )}
        </View>
      </ScrollView>
    </ThemedView>
  );
}
