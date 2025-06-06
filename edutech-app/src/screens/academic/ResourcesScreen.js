import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  FlatList,
  TouchableOpacity,
  Alert,
  RefreshControl,
  ActivityIndicator,
  Linking,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, FONTS, SPACING } from '../../config/theme';
import { studentApiService } from '../../services/studentApiService';

const ResourcesScreen = () => {
  const [resources, setResources] = useState([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [filter, setFilter] = useState('all'); // all, document, video, link, other

  useEffect(() => {
    loadResources();
  }, []);

  const loadResources = async () => {
    try {
      setLoading(true);
      const data = await studentApiService.getResources();
      setResources(data);
    } catch (error) {
      Alert.alert('Error', 'No se pudieron cargar los recursos');
    } finally {
      setLoading(false);
    }
  };

  const onRefresh = async () => {
    setRefreshing(true);
    await loadResources();
    setRefreshing(false);
  };

  const handleDownload = async (resource) => {
    try {
      if (resource.type === 'link') {
        // Abrir enlaces externos
        const supported = await Linking.canOpenURL(resource.url);
        if (supported) {
          await Linking.openURL(resource.url);
        } else {
          Alert.alert('Error', 'No se puede abrir este enlace');
        }
      } else {
        // Simular descarga de archivos
        Alert.alert(
          'Descarga iniciada',
          `Descargando ${resource.title}...`,
          [{ text: 'OK' }]
        );
      }
    } catch (error) {
      Alert.alert('Error', 'No se pudo descargar el recurso');
    }
  };

  const getResourceIcon = (type) => {
    switch (type) {
      case 'document':
        return 'document-text';
      case 'video':
        return 'play-circle';
      case 'link':
        return 'link';
      case 'audio':
        return 'musical-notes';
      case 'image':
        return 'image';
      default:
        return 'folder';
    }
  };

  const getResourceColor = (type) => {
    switch (type) {
      case 'document':
        return COLORS.blue;
      case 'video':
        return COLORS.red;
      case 'link':
        return COLORS.green;
      case 'audio':
        return COLORS.purple;
      case 'image':
        return COLORS.orange;
      default:
        return COLORS.gray;
    }
  };

  const formatFileSize = (bytes) => {
    if (!bytes) return '';
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(1024));
    return Math.round(bytes / Math.pow(1024, i) * 100) / 100 + ' ' + sizes[i];
  };

  const filteredResources = resources.filter(resource => {
    if (filter === 'all') return true;
    return resource.type === filter;
  });

  const filterOptions = [
    { key: 'all', label: 'Todos', icon: 'grid' },
    { key: 'document', label: 'Documentos', icon: 'document-text' },
    { key: 'video', label: 'Videos', icon: 'play-circle' },
    { key: 'link', label: 'Enlaces', icon: 'link' },
    { key: 'other', label: 'Otros', icon: 'folder' },
  ];

  const renderFilterButton = (option) => (
    <TouchableOpacity
      key={option.key}
      style={[
        styles.filterButton,
        filter === option.key && styles.filterButtonActive
      ]}
      onPress={() => setFilter(option.key)}
    >
      <Ionicons
        name={option.icon}
        size={16}
        color={filter === option.key ? COLORS.white : COLORS.primary}
      />
      <Text style={[
        styles.filterButtonText,
        filter === option.key && styles.filterButtonTextActive
      ]}>
        {option.label}
      </Text>
    </TouchableOpacity>
  );

  const renderResource = ({ item }) => (
    <TouchableOpacity
      style={styles.resourceCard}
      onPress={() => handleDownload(item)}
    >
      <View style={styles.resourceHeader}>
        <View style={[styles.iconContainer, { backgroundColor: getResourceColor(item.type) + '20' }]}>
          <Ionicons
            name={getResourceIcon(item.type)}
            size={24}
            color={getResourceColor(item.type)}
          />
        </View>
        <View style={styles.resourceInfo}>
          <Text style={styles.resourceTitle} numberOfLines={2}>
            {item.title}
          </Text>
          <Text style={styles.resourceCourse}>{item.course}</Text>
          {item.size && (
            <Text style={styles.resourceSize}>{formatFileSize(item.size)}</Text>
          )}
        </View>
        <TouchableOpacity
          style={styles.downloadButton}
          onPress={() => handleDownload(item)}
        >
          <Ionicons
            name={item.type === 'link' ? 'open-outline' : 'download-outline'}
            size={20}
            color={COLORS.primary}
          />
        </TouchableOpacity>
      </View>
      
      {item.description && (
        <Text style={styles.resourceDescription} numberOfLines={2}>
          {item.description}
        </Text>
      )}
      
      <View style={styles.resourceFooter}>
        <Text style={styles.resourceDate}>
          {new Date(item.createdAt).toLocaleDateString('es-ES', {
            day: 'numeric',
            month: 'short',
            year: 'numeric'
          })}
        </Text>
        {item.downloads && (
          <Text style={styles.downloadCount}>
            {item.downloads} descargas
          </Text>
        )}
      </View>
    </TouchableOpacity>
  );

  if (loading && !refreshing) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color={COLORS.primary} />
        <Text style={styles.loadingText}>Cargando recursos...</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      {/* Filtros */}
      <View style={styles.filtersContainer}>
        <FlatList
          horizontal
          data={filterOptions}
          keyExtractor={(item) => item.key}
          renderItem={({ item }) => renderFilterButton(item)}
          showsHorizontalScrollIndicator={false}
          contentContainerStyle={styles.filtersList}
        />
      </View>

      {/* Lista de recursos */}
      <FlatList
        data={filteredResources}
        keyExtractor={(item) => item.id}
        renderItem={renderResource}
        contentContainerStyle={styles.resourcesList}
        refreshControl={
          <RefreshControl
            refreshing={refreshing}
            onRefresh={onRefresh}
            colors={[COLORS.primary]}
            tintColor={COLORS.primary}
          />
        }
        ListEmptyComponent={
          <View style={styles.emptyContainer}>
            <Ionicons name="folder-open-outline" size={64} color={COLORS.lightText} />
            <Text style={styles.emptyTitle}>No hay recursos disponibles</Text>
            <Text style={styles.emptySubtitle}>
              Los recursos aparecerán aquí cuando estén disponibles
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
    backgroundColor: COLORS.background,
  },
  loadingText: {
    ...FONTS.body2,
    color: COLORS.text,
    marginTop: SPACING.md,
  },
  filtersContainer: {
    backgroundColor: COLORS.white,
    paddingVertical: SPACING.sm,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.border,
  },
  filtersList: {
    paddingHorizontal: SPACING.md,
  },
  filterButton: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: SPACING.md,
    paddingVertical: SPACING.sm,
    borderRadius: 20,
    borderWidth: 1,
    borderColor: COLORS.primary,
    marginRight: SPACING.sm,
    backgroundColor: COLORS.white,
  },
  filterButtonActive: {
    backgroundColor: COLORS.primary,
  },
  filterButtonText: {
    ...FONTS.body3,
    color: COLORS.primary,
    marginLeft: SPACING.xs,
  },
  filterButtonTextActive: {
    color: COLORS.white,
  },
  resourcesList: {
    padding: SPACING.md,
  },
  resourceCard: {
    backgroundColor: COLORS.white,
    borderRadius: 12,
    padding: SPACING.md,
    marginBottom: SPACING.md,
    shadowColor: COLORS.black,
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.1,
    shadowRadius: 3.84,
    elevation: 5,
  },
  resourceHeader: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    marginBottom: SPACING.sm,
  },
  iconContainer: {
    width: 48,
    height: 48,
    borderRadius: 24,
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: SPACING.md,
  },
  resourceInfo: {
    flex: 1,
  },
  resourceTitle: {
    ...FONTS.h4,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  resourceCourse: {
    ...FONTS.body3,
    color: COLORS.primary,
    marginBottom: SPACING.xs,
  },
  resourceSize: {
    ...FONTS.caption,
    color: COLORS.lightText,
  },
  downloadButton: {
    padding: SPACING.sm,
    borderRadius: 8,
    backgroundColor: COLORS.primary + '10',
  },
  resourceDescription: {
    ...FONTS.body3,
    color: COLORS.lightText,
    marginBottom: SPACING.sm,
    lineHeight: 20,
  },
  resourceFooter: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingTop: SPACING.sm,
    borderTopWidth: 1,
    borderTopColor: COLORS.border,
  },
  resourceDate: {
    ...FONTS.caption,
    color: COLORS.lightText,
  },
  downloadCount: {
    ...FONTS.caption,
    color: COLORS.lightText,
  },
  emptyContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingTop: SPACING.xxl,
  },
  emptyTitle: {
    ...FONTS.h3,
    color: COLORS.text,
    marginTop: SPACING.md,
    marginBottom: SPACING.sm,
  },
  emptySubtitle: {
    ...FONTS.body2,
    color: COLORS.lightText,
    textAlign: 'center',
    paddingHorizontal: SPACING.xl,
  },
});

export default ResourcesScreen;
