import React, { useState, useEffect, useCallback } from 'react';
import { View, StyleSheet, FlatList, ActivityIndicator, TouchableOpacity, TextInput, RefreshControl, Pressable } from 'react-native';
import { Image } from 'expo-image';
import { router, Stack } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';
import * as Haptics from 'expo-haptics';

import { ThemedText } from '@/components/ThemedText';
import { ThemedView } from '@/components/ThemedView';
import { useThemeColor } from '@/hooks/useThemeColor';
import { cursosApi } from '@/api/cursosService';
import { Curso } from '@/types';

// Componente para las etiquetas de duración de los cursos
const DurationBadge = ({ duration }: { duration: number }) => {
  const accentColor = useThemeColor({}, 'tint');
  
  return (
    <View style={[styles.durationBadge, { backgroundColor: accentColor + '20', borderColor: accentColor + '40' }]}>
      <Ionicons name="time-outline" size={14} color={accentColor} style={styles.durationIcon} />
      <ThemedText style={[styles.durationText, { color: accentColor }]}>
        {duration} días
      </ThemedText>
    </View>
  );
};

// Componente para las tarjetas de curso con animación de feedback
const CourseCard = ({ item, onPress }: { item: Curso, onPress: () => void }) => {
  const cardColor = useThemeColor({}, 'card');
  
  const handlePress = useCallback(() => {
    // Proporcionar feedback táctil al usuario
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
    onPress();
  }, [onPress]);
  
  return (
    <Pressable
      style={({ pressed }) => [
        styles.courseCard, 
        { 
          backgroundColor: cardColor,
          transform: [{ scale: pressed ? 0.98 : 1 }],
        }
      ]}
      onPress={handlePress}
    >
      <Image 
        source={{ uri: item.imagen || 'https://via.placeholder.com/150' }} 
        style={styles.courseImage}
        contentFit="cover"
        transition={200}
      />
      
      <View style={styles.courseContent}>
        <View style={styles.courseInfo}>
          <ThemedText style={styles.courseTitle} numberOfLines={2} ellipsizeMode="tail">
            {item.nombre}
          </ThemedText>
          <ThemedText style={styles.courseDescription} numberOfLines={2} ellipsizeMode="tail">
            {item.descripcion}
          </ThemedText>
          
          <DurationBadge duration={item.duracion} />
        </View>
          <Ionicons 
          name="chevron-forward" 
          size={20} 
          color={useThemeColor({}, 'text')} 
          style={styles.arrowIcon}
        />
      </View>
    </Pressable>
  );
};

// Componente para el campo de búsqueda
const SearchBar = ({ 
  value, 
  onChangeText, 
  onSubmit 
}: { 
  value: string, 
  onChangeText: (text: string) => void, 
  onSubmit: () => void 
}) => {
  const inputBgColor = useThemeColor({}, 'card');
  const textColor = useThemeColor({}, 'text');
  const accentColor = useThemeColor({}, 'tint');
  
  return (
    <View style={[styles.searchContainer, { backgroundColor: inputBgColor }]}>
      <Ionicons name="search" size={20} color={textColor} style={styles.searchIcon} />
      <TextInput
        style={[styles.searchInput, { color: textColor }]}
        placeholder="Buscar cursos..."
        placeholderTextColor={useThemeColor({}, 'text', 0.5)}
        value={value}
        onChangeText={onChangeText}
        onSubmitEditing={onSubmit}
        returnKeyType="search"
        autoCapitalize="none"
        selectionColor={accentColor}
      />
      {value.length > 0 && (
        <TouchableOpacity onPress={() => onChangeText('')} style={styles.clearButton}>
          <Ionicons name="close-circle" size={18} color={textColor} />
        </TouchableOpacity>
      )}
    </View>
  );
};

// Pantalla principal de cursos
export default function ExploreCoursesScreen() {
  const [cursos, setCursos] = useState<Curso[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [searchText, setSearchText] = useState('');
  const [searching, setSearching] = useState(false);
  
  const accentColor = useThemeColor({}, 'tint');
  
  useEffect(() => {
    cargarCursos();
  }, []);
  
  const cargarCursos = async () => {
    try {
      setLoading(true);
      const data = await cursosApi.getAll();
      setCursos(data);
    } catch (error) {
      console.error('Error al cargar cursos:', error);
      // Datos de prueba si falla la API
      setCursos([
        { id: 1, nombre: 'Introducción a JavaScript', descripcion: 'Aprende los fundamentos de JavaScript y su uso en desarrollo web moderno', duracion: 15, imagen: 'https://via.placeholder.com/150' },
        { id: 2, nombre: 'React Native Avanzado', descripcion: 'Técnicas avanzadas para desarrollo móvil con React Native y Expo', duracion: 20, imagen: 'https://via.placeholder.com/150' },
        { id: 3, nombre: 'Diseño UI/UX para Aplicaciones Móviles', descripcion: 'Principios de diseño de interfaces y experiencia de usuario', duracion: 12, imagen: 'https://via.placeholder.com/150' },
        { id: 4, nombre: 'TypeScript para Desarrolladores', descripcion: 'Domina TypeScript para desarrollo de aplicaciones más robustas', duracion: 18, imagen: 'https://via.placeholder.com/150' },
        { id: 5, nombre: 'Desarrollo Backend con Node.js', descripcion: 'Construye APIs RESTful con Node.js, Express y MongoDB', duracion: 25, imagen: 'https://via.placeholder.com/150' },
      ]);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };
  
  const buscarCursos = async () => {
    if (!searchText.trim()) {
      cargarCursos();
      return;
    }
    
    try {
      setSearching(true);
      setLoading(true);
      const results = await cursosApi.buscar(searchText);
      setCursos(results);
    } catch (error) {
      console.error('Error al buscar cursos:', error);
    } finally {
      setLoading(false);
      setSearching(false);
    }
  };
  
  const onRefresh = useCallback(() => {
    setRefreshing(true);
    setSearchText('');
    cargarCursos();
  }, []);
    const handleCursoPress = (cursoId: number) => {
    router.push({
      pathname: "/curso/[id]",
      params: { id: cursoId }
    });
  };
  // Componente para mostrar el estado vacío cuando no hay cursos
const EmptyStateContent = ({ searchText, accentColor, onClearSearch }: { 
  searchText: string, 
  accentColor: string, 
  onClearSearch: () => void 
}) => {
  const textColor = useThemeColor({}, 'text');
  
  return (
    <View style={styles.emptyContainer}>
      <Ionicons 
        name={searchText ? "search-outline" : "book-outline"} 
        size={64} 
        color={textColor} 
      />
      <ThemedText style={styles.emptyTitle}>
        {searchText ? "No se encontraron resultados" : "No hay cursos disponibles"}
      </ThemedText>
      <ThemedText style={styles.emptyDescription}>
        {searchText 
          ? "Intenta con otra búsqueda o términos más generales"
          : "Los cursos aparecerán aquí cuando estén disponibles"
        }
      </ThemedText>
      {searchText && (
        <TouchableOpacity 
          style={[styles.emptyButton, { borderColor: accentColor }]}
          onPress={onClearSearch}
        >
          <ThemedText style={[styles.emptyButtonText, { color: accentColor }]}>
            Ver todos los cursos
          </ThemedText>
        </TouchableOpacity>
      )}
    </View>
  );
};

  return (
    <ThemedView style={styles.container}>
      <Stack.Screen 
        options={{ 
          title: 'Explorar Cursos',
          headerShown: true,
          headerStyle: {
            backgroundColor: useThemeColor({}, 'background'),
          },
          headerTitleStyle: {
            color: useThemeColor({}, 'text'),
          },
          headerTintColor: accentColor,
        }} 
      />
      
      <View style={styles.contentContainer}>
        <SearchBar
          value={searchText}
          onChangeText={setSearchText}
          onSubmit={buscarCursos}
        />
        
        {loading && !refreshing ? (
          <View style={styles.loadingContainer}>
            <ActivityIndicator size="large" color={accentColor} />
            <ThemedText style={styles.loadingText}>
              {searching ? "Buscando cursos..." : "Cargando cursos..."}
            </ThemedText>
          </View>
        ) : (
          <FlatList
            data={cursos}
            renderItem={({ item }) => (
              <CourseCard item={item} onPress={() => handleCursoPress(item.id)} />
            )}
            keyExtractor={(item) => item.id.toString()}
            contentContainerStyle={styles.listContainer}
            showsVerticalScrollIndicator={false}
            refreshControl={
              <RefreshControl
                refreshing={refreshing}
                onRefresh={onRefresh}
                colors={[accentColor]}
                tintColor={accentColor}
              />
            }            ListEmptyComponent={() => (
              <EmptyStateContent 
                searchText={searchText} 
                accentColor={accentColor} 
                onClearSearch={() => {
                  setSearchText('');
                  cargarCursos();
                }}
              />
            )}
            ListHeaderComponent={
              searchText ? (
                <ThemedText style={styles.resultsText}>
                  Resultados para &quot;{searchText}&quot;
                </ThemedText>
              ) : null
            }
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
  contentContainer: {
    flex: 1,
    paddingHorizontal: 16,
  },
  searchContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    height: 50,
    borderRadius: 25,
    paddingHorizontal: 16,
    marginBottom: 16,
    marginTop: 8,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 3,
    elevation: 2,
  },
  searchIcon: {
    marginRight: 8,
  },
  searchInput: {
    flex: 1,
    height: '100%',
    fontSize: 16,
  },
  clearButton: {
    padding: 4,
  },
  listContainer: {
    paddingBottom: 20,
  },
  courseCard: {
    borderRadius: 16,
    marginBottom: 16,
    overflow: 'hidden',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 3 },
    shadowOpacity: 0.1,
    shadowRadius: 6,
    elevation: 3,
  },
  courseImage: {
    width: '100%',
    height: 160,
  },
  courseContent: {
    flexDirection: 'row',
    padding: 16,
    alignItems: 'center',
  },
  courseInfo: {
    flex: 1,
  },
  courseTitle: {
    fontSize: 17,
    fontWeight: 'bold',
    marginBottom: 6,
  },
  courseDescription: {
    fontSize: 14,
    marginBottom: 12,
    opacity: 0.8,
    lineHeight: 20,
  },
  durationBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 4,
    paddingHorizontal: 10,
    borderRadius: 12,
    alignSelf: 'flex-start',
    borderWidth: 1,
  },
  durationIcon: {
    marginRight: 4,
  },
  durationText: {
    fontSize: 12,
    fontWeight: '600',
  },
  arrowIcon: {
    marginLeft: 8,
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  loadingText: {
    marginTop: 16,
    fontSize: 16,
  },
  emptyContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 80,
    paddingHorizontal: 32,
  },
  emptyTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    marginTop: 16,
    marginBottom: 8,
    textAlign: 'center',
  },
  emptyDescription: {
    fontSize: 16,
    opacity: 0.7,
    textAlign: 'center',
    marginBottom: 24,
  },
  emptyButton: {
    paddingVertical: 10,
    paddingHorizontal: 20,
    borderRadius: 20,
    borderWidth: 1,
  },
  emptyButtonText: {
    fontSize: 16,
    fontWeight: '500',
  },
  resultsText: {
    fontSize: 15,
    fontStyle: 'italic',
    marginBottom: 12,
    opacity: 0.7,
  },
  coursesTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 16,
  },
  coursesContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
  },
  courseItem: {
    width: '48%',
    marginBottom: 16,
  },
  courseImagePlaceholder: {
    width: '100%',
    height: 120,
    backgroundColor: '#f0f0f0',
    borderRadius: 12,
    marginBottom: 8,
  },
  courseInfoPlaceholder: {
    padding: 16,
    borderRadius: 12,
    backgroundColor: '#f9f9f9',
    marginBottom: 8,
  },
  courseTitlePlaceholder: {
    height: 20,
    width: '70%',
    backgroundColor: '#e0e0e0',
    borderRadius: 10,
    marginBottom: 8,
  },
  courseDescriptionPlaceholder: {
    height: 16,
    width: '90%',
    backgroundColor: '#e0e0e0',
    borderRadius: 10,
  },
});
