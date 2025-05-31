import React, { useState, useEffect, useContext } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  ActivityIndicator,
  RefreshControl,
  TouchableOpacity,
  FlatList
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT, BORDER_RADIUS } from '../../config/theme';
import { Card } from '../../components/common/UIComponents';
import { AuthContext } from '../../context/AuthContext';
import { CommunicationService } from '../../services/studentApiService';

const CommunicationsScreen = ({ navigation }) => {
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [communications, setCommunications] = useState([]);
  const [filter, setFilter] = useState('all'); // 'all', 'unread', 'important'
  const [error, setError] = useState(null);
  const { token } = useContext(AuthContext);

  const loadCommunications = async () => {
    try {
      setError(null);
      const response = await CommunicationService.getAnnouncements(token);
      
      if (response.success) {
        setCommunications(response.data);
      }
    } catch (error) {
      console.error('Error cargando comunicaciones:', error);
      setError(error.message);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  useEffect(() => {
    loadCommunications();
  }, [token]);

  const onRefresh = () => {
    setRefreshing(true);
    loadCommunications();
  };

  const markAsRead = async (communicationId) => {
    try {
      await CommunicationService.markAsRead(token, communicationId);
      // Actualizar localmente
      setCommunications(prev => 
        prev.map(comm => 
          comm.id === communicationId 
            ? { ...comm, leido: true }
            : comm
        )
      );
    } catch (error) {
      console.error('Error marcando como leído:', error);
    }
  };

  const getFilteredCommunications = () => {
    switch (filter) {
      case 'unread':
        return communications.filter(comm => !comm.leido);
      case 'important':
        return communications.filter(comm => comm.importante);
      default:
        return communications;
    }
  };

  const getTypeIcon = (tipo) => {
    switch (tipo) {
      case 'ANUNCIO': return 'megaphone-outline';
      case 'MENSAJE_CURSO': return 'book-outline';
      case 'RECORDATORIO': return 'alarm-outline';
      default: return 'mail-outline';
    }
  };

  const getTypeColor = (tipo) => {
    switch (tipo) {
      case 'ANUNCIO': return COLORS.primary;
      case 'MENSAJE_CURSO': return COLORS.info;
      case 'RECORDATORIO': return COLORS.warning;
      default: return COLORS.textSecondary;
    }
  };

  const CommunicationItem = ({ item }) => {
    const handlePress = () => {
      if (!item.leido) {
        markAsRead(item.id);
      }
      // Aquí se podría navegar a una vista detallada
    };

    return (
      <TouchableOpacity onPress={handlePress}>
        <Card style={[
          styles.communicationCard,
          !item.leido && styles.unreadCard,
          item.importante && styles.importantCard
        ]}>
          <View style={styles.communicationHeader}>
            <View style={styles.iconContainer}>
              <Ionicons 
                name={getTypeIcon(item.tipo)} 
                size={20} 
                color={getTypeColor(item.tipo)} 
              />
              {item.importante && (
                <Ionicons 
                  name="star" 
                  size={14} 
                  color={COLORS.warning} 
                  style={styles.importantIcon}
                />
              )}
            </View>
            
            <View style={styles.communicationInfo}>
              <Text style={[
                styles.communicationTitle,
                !item.leido && styles.unreadTitle
              ]} numberOfLines={2}>
                {item.titulo}
              </Text>
              
              <Text style={styles.communicationMeta}>
                {item.remitente} • {new Date(item.fechaPublicacion).toLocaleDateString('es-ES')}
              </Text>
            </View>
            
            {!item.leido && <View style={styles.unreadIndicator} />}
          </View>
          
          <Text style={styles.communicationContent} numberOfLines={3}>
            {item.contenido}
          </Text>
          
          {item.cursoId && (
            <View style={styles.courseTag}>
              <Text style={styles.courseTagText}>Curso específico</Text>
            </View>
          )}
        </Card>
      </TouchableOpacity>
    );
  };

  const FilterButton = ({ filterType, title, count }) => (
    <TouchableOpacity
      style={[
        styles.filterButton,
        filter === filterType && styles.activeFilterButton
      ]}
      onPress={() => setFilter(filterType)}
    >
      <Text style={[
        styles.filterButtonText,
        filter === filterType && styles.activeFilterButtonText
      ]}>
        {title}
      </Text>
      {count > 0 && (
        <View style={styles.filterBadge}>
          <Text style={styles.filterBadgeText}>{count}</Text>
        </View>
      )}
    </TouchableOpacity>
  );

  if (loading) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color={COLORS.primary} />
        <Text style={styles.loadingText}>Cargando comunicaciones...</Text>
      </View>
    );
  }

  if (error) {
    return (
      <View style={styles.errorContainer}>
        <Ionicons name="mail-outline" size={64} color={COLORS.error} />
        <Text style={styles.errorText}>{error}</Text>
        <TouchableOpacity style={styles.retryButton} onPress={loadCommunications}>
          <Text style={styles.retryText}>Reintentar</Text>
        </TouchableOpacity>
      </View>
    );
  }

  const filteredCommunications = getFilteredCommunications();
  const unreadCount = communications.filter(comm => !comm.leido).length;
  const importantCount = communications.filter(comm => comm.importante).length;

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity 
          onPress={() => navigation.goBack()}
          style={styles.backButton}
        >
          <Ionicons name="arrow-back" size={24} color={COLORS.text} />
        </TouchableOpacity>
        <Text style={styles.headerTitle}>Comunicaciones</Text>
        <View style={styles.placeholder} />
      </View>

      {/* Filtros */}
      <View style={styles.filtersContainer}>
        <ScrollView horizontal showsHorizontalScrollIndicator={false}>
          <FilterButton 
            filterType="all" 
            title="Todos" 
            count={communications.length} 
          />
          <FilterButton 
            filterType="unread" 
            title="No leídos" 
            count={unreadCount} 
          />
          <FilterButton 
            filterType="important" 
            title="Importantes" 
            count={importantCount} 
          />
        </ScrollView>
      </View>

      {/* Lista de comunicaciones */}
      <FlatList
        data={filteredCommunications}
        renderItem={({ item }) => <CommunicationItem item={item} />}
        keyExtractor={(item) => item.id.toString()}
        style={styles.list}
        contentContainerStyle={styles.listContent}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
        ListEmptyComponent={
          <View style={styles.emptyContainer}>
            <Ionicons name="mail-outline" size={64} color={COLORS.textSecondary} />
            <Text style={styles.emptyText}>
              {filter === 'unread' ? 'No hay mensajes sin leer' :
               filter === 'important' ? 'No hay mensajes importantes' :
               'No hay comunicaciones'}
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
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: SPACING.lg,
    paddingVertical: SPACING.md,
    backgroundColor: COLORS.white,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  backButton: {
    padding: SPACING.sm,
  },
  headerTitle: {
    fontSize: FONT_SIZE.xl,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
  },
  placeholder: {
    width: 24,
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
  errorContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: COLORS.background,
    padding: SPACING.lg,
  },
  errorText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.error,
    textAlign: 'center',
    marginVertical: SPACING.md,
  },
  retryButton: {
    backgroundColor: COLORS.primary,
    paddingHorizontal: SPACING.lg,
    paddingVertical: SPACING.sm,
    borderRadius: BORDER_RADIUS.md,
  },
  retryText: {
    color: COLORS.white,
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
  },
  filtersContainer: {
    backgroundColor: COLORS.white,
    paddingVertical: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  filterButton: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: SPACING.md,
    paddingVertical: SPACING.sm,
    marginHorizontal: SPACING.xs,
    backgroundColor: COLORS.lightGray,
    borderRadius: BORDER_RADIUS.lg,
  },
  activeFilterButton: {
    backgroundColor: COLORS.primary,
  },
  filterButtonText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
    fontWeight: FONT_WEIGHT.medium,
  },
  activeFilterButtonText: {
    color: COLORS.white,
  },
  filterBadge: {
    backgroundColor: COLORS.error,
    borderRadius: 10,
    minWidth: 20,
    height: 20,
    justifyContent: 'center',
    alignItems: 'center',
    marginLeft: SPACING.xs,
  },
  filterBadgeText: {
    color: COLORS.white,
    fontSize: 12,
    fontWeight: FONT_WEIGHT.bold,
  },
  list: {
    flex: 1,
  },
  listContent: {
    padding: SPACING.lg,
  },
  communicationCard: {
    marginBottom: SPACING.md,
  },
  unreadCard: {
    borderLeftWidth: 4,
    borderLeftColor: COLORS.primary,
  },
  importantCard: {
    borderTopWidth: 2,
    borderTopColor: COLORS.warning,
  },
  communicationHeader: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    marginBottom: SPACING.sm,
  },
  iconContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginRight: SPACING.md,
  },
  importantIcon: {
    position: 'absolute',
    top: -5,
    right: -5,
  },
  communicationInfo: {
    flex: 1,
  },
  communicationTitle: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  unreadTitle: {
    fontWeight: FONT_WEIGHT.bold,
  },
  communicationMeta: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
  },
  unreadIndicator: {
    width: 8,
    height: 8,
    borderRadius: 4,
    backgroundColor: COLORS.primary,
    marginLeft: SPACING.sm,
  },
  communicationContent: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
    lineHeight: 20,
    marginBottom: SPACING.sm,
  },
  courseTag: {
    backgroundColor: COLORS.info,
    paddingHorizontal: SPACING.sm,
    paddingVertical: SPACING.xs,
    borderRadius: BORDER_RADIUS.sm,
    alignSelf: 'flex-start',
  },
  courseTagText: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.white,
    fontWeight: FONT_WEIGHT.medium,
  },
  emptyContainer: {
    alignItems: 'center',
    paddingVertical: SPACING.xl * 2,
  },
  emptyText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.textSecondary,
    textAlign: 'center',
    marginTop: SPACING.md,
  },
});

export default CommunicationsScreen;
