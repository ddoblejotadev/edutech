import React, { useState, useEffect, useContext } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  ActivityIndicator,
  RefreshControl,
  TouchableOpacity,
  FlatList,
  Alert
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT, BORDER_RADIUS } from '../../config/theme';
import { Card } from '../../components/common/UIComponents';
import { AuthContext } from '../../context/AuthContext';
import { AssignmentService } from '../../services/studentApiService';

const AssignmentsScreen = ({ navigation }) => {
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [assignments, setAssignments] = useState([]);
  const [filter, setFilter] = useState('all'); // 'all', 'pending', 'submitted', 'overdue'
  const [error, setError] = useState(null);
  const { token } = useContext(AuthContext);

  const loadAssignments = async () => {
    try {
      setError(null);
      const response = await AssignmentService.getAssignments(token);
      
      if (response.success) {
        setAssignments(response.data);
      }
    } catch (error) {
      console.error('Error cargando tareas:', error);
      setError(error.message);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  useEffect(() => {
    loadAssignments();
  }, [token]);

  const onRefresh = () => {
    setRefreshing(true);
    loadAssignments();
  };

  const getFilteredAssignments = () => {
    const now = new Date();
    
    switch (filter) {
      case 'pending':
        return assignments.filter(assignment => assignment.estado === 'EN_PROGRESO');
      case 'submitted':
        return assignments.filter(assignment => assignment.estado === 'ENTREGADO');
      case 'overdue':
        return assignments.filter(assignment => 
          assignment.estado === 'EN_PROGRESO' && 
          new Date(assignment.fechaEntrega) < now
        );
      default:
        return assignments;
    }
  };

  const getStatusColor = (assignment) => {
    const now = new Date();
    const dueDate = new Date(assignment.fechaEntrega);
    
    if (assignment.estado === 'ENTREGADO') {
      return COLORS.success;
    }
    
    if (dueDate < now) {
      return COLORS.error; // Vencida
    }
    
    const daysLeft = Math.ceil((dueDate - now) / (1000 * 60 * 60 * 24));
    if (daysLeft <= 2) {
      return COLORS.warning; // Próxima a vencer
    }
    
    return COLORS.info; // En progreso
  };

  const getStatusText = (assignment) => {
    const now = new Date();
    const dueDate = new Date(assignment.fechaEntrega);
    
    if (assignment.estado === 'ENTREGADO') {
      return 'Entregado';
    }
    
    if (dueDate < now) {
      return 'Vencida';
    }
    
    const daysLeft = Math.ceil((dueDate - now) / (1000 * 60 * 60 * 24));
    if (daysLeft === 0) {
      return 'Vence hoy';
    }
    if (daysLeft === 1) {
      return 'Vence mañana';
    }
    
    return `${daysLeft} días restantes`;
  };

  const handleSubmitAssignment = (assignmentId) => {
    Alert.alert(
      'Entregar Tarea',
      'Selecciona un archivo para entregar la tarea',
      [
        {
          text: 'Cancelar',
          style: 'cancel'
        },
        {
          text: 'Seleccionar Archivo',
          onPress: () => {
            // Aquí iría la lógica para seleccionar y subir archivo
            Alert.alert('Demo', 'En una app real, aquí se abriría el selector de archivos');
          }
        }
      ]
    );
  };

  const AssignmentItem = ({ item }) => {
    const statusColor = getStatusColor(item);
    const statusText = getStatusText(item);
    const isOverdue = item.estado === 'EN_PROGRESO' && new Date(item.fechaEntrega) < new Date();
    
    return (
      <Card style={[
        styles.assignmentCard,
        isOverdue && styles.overdueCard
      ]}>
        <View style={styles.assignmentHeader}>
          <View style={styles.assignmentIcon}>
            <Ionicons 
              name="document-text-outline" 
              size={24} 
              color={statusColor} 
            />
          </View>
          
          <View style={styles.assignmentInfo}>
            <Text style={styles.assignmentTitle} numberOfLines={2}>
              {item.titulo}
            </Text>
            <Text style={styles.assignmentCourse}>
              {item.cursoNombre}
            </Text>
            <Text style={styles.assignmentDescription} numberOfLines={2}>
              {item.descripcion}
            </Text>
          </View>
          
          <View style={[styles.statusBadge, { backgroundColor: statusColor }]}>
            <Text style={styles.statusText}>{statusText}</Text>
          </View>
        </View>
        
        <View style={styles.assignmentDates}>
          <View style={styles.dateItem}>
            <Ionicons name="calendar-outline" size={16} color={COLORS.textSecondary} />
            <Text style={styles.dateText}>
              Asignado: {new Date(item.fechaAsignacion).toLocaleDateString('es-ES')}
            </Text>
          </View>
          
          <View style={styles.dateItem}>
            <Ionicons name="time-outline" size={16} color={statusColor} />
            <Text style={[styles.dateText, { color: statusColor }]}>
              Entrega: {new Date(item.fechaEntrega).toLocaleDateString('es-ES')}
            </Text>
          </View>
        </View>
        
        {item.calificacion !== null && (
          <View style={styles.gradeContainer}>
            <Text style={styles.gradeLabel}>Calificación:</Text>
            <Text style={[styles.gradeValue, { color: item.calificacion >= 7 ? COLORS.success : COLORS.error }]}>
              {item.calificacion}/10
            </Text>
          </View>
        )}
        
        {item.comentarios && (
          <View style={styles.commentsContainer}>
            <Text style={styles.commentsLabel}>Comentarios:</Text>
            <Text style={styles.commentsText}>{item.comentarios}</Text>
          </View>
        )}
        
        <View style={styles.assignmentActions}>
          {item.estado === 'EN_PROGRESO' && (
            <TouchableOpacity
              style={[styles.actionButton, styles.submitButton]}
              onPress={() => handleSubmitAssignment(item.id)}
            >
              <Ionicons name="cloud-upload-outline" size={16} color={COLORS.white} />
              <Text style={styles.actionButtonText}>Entregar</Text>
            </TouchableOpacity>
          )}
          
          <TouchableOpacity
            style={[styles.actionButton, styles.detailButton]}
            onPress={() => {
              // Navegar a detalles de la tarea
              Alert.alert('Demo', 'Navegar a detalles de la tarea');
            }}
          >
            <Ionicons name="eye-outline" size={16} color={COLORS.primary} />
            <Text style={[styles.actionButtonText, { color: COLORS.primary }]}>Ver Detalles</Text>
          </TouchableOpacity>
        </View>
      </Card>
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
        <Text style={styles.loadingText}>Cargando tareas...</Text>
      </View>
    );
  }

  if (error) {
    return (
      <View style={styles.errorContainer}>
        <Ionicons name="document-text-outline" size={64} color={COLORS.error} />
        <Text style={styles.errorText}>{error}</Text>
        <TouchableOpacity style={styles.retryButton} onPress={loadAssignments}>
          <Text style={styles.retryText}>Reintentar</Text>
        </TouchableOpacity>
      </View>
    );
  }

  const filteredAssignments = getFilteredAssignments();
  const pendingCount = assignments.filter(a => a.estado === 'EN_PROGRESO').length;
  const submittedCount = assignments.filter(a => a.estado === 'ENTREGADO').length;
  const overdueCount = assignments.filter(a => 
    a.estado === 'EN_PROGRESO' && new Date(a.fechaEntrega) < new Date()
  ).length;

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity 
          onPress={() => navigation.goBack()}
          style={styles.backButton}
        >
          <Ionicons name="arrow-back" size={24} color={COLORS.text} />
        </TouchableOpacity>
        <Text style={styles.headerTitle}>Mis Tareas</Text>
        <View style={styles.placeholder} />
      </View>

      {/* Filtros */}
      <View style={styles.filtersContainer}>
        <ScrollView horizontal showsHorizontalScrollIndicator={false}>
          <FilterButton 
            filterType="all" 
            title="Todas" 
            count={assignments.length} 
          />
          <FilterButton 
            filterType="pending" 
            title="Pendientes" 
            count={pendingCount} 
          />
          <FilterButton 
            filterType="submitted" 
            title="Entregadas" 
            count={submittedCount} 
          />
          <FilterButton 
            filterType="overdue" 
            title="Vencidas" 
            count={overdueCount} 
          />
        </ScrollView>
      </View>

      {/* Lista de tareas */}
      <FlatList
        data={filteredAssignments}
        renderItem={({ item }) => <AssignmentItem item={item} />}
        keyExtractor={(item) => item.id.toString()}
        style={styles.list}
        contentContainerStyle={styles.listContent}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
        ListEmptyComponent={
          <View style={styles.emptyContainer}>
            <Ionicons name="document-text-outline" size={64} color={COLORS.textSecondary} />
            <Text style={styles.emptyText}>
              {filter === 'pending' ? 'No hay tareas pendientes' :
               filter === 'submitted' ? 'No hay tareas entregadas' :
               filter === 'overdue' ? 'No hay tareas vencidas' :
               'No hay tareas asignadas'}
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
  assignmentCard: {
    marginBottom: SPACING.lg,
  },
  overdueCard: {
    borderLeftWidth: 4,
    borderLeftColor: COLORS.error,
  },
  assignmentHeader: {
    flexDirection: 'row',
    marginBottom: SPACING.md,
  },
  assignmentIcon: {
    marginRight: SPACING.md,
    alignItems: 'center',
    justifyContent: 'center',
  },
  assignmentInfo: {
    flex: 1,
  },
  assignmentTitle: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  assignmentCourse: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.primary,
    fontWeight: FONT_WEIGHT.medium,
    marginBottom: SPACING.xs,
  },
  assignmentDescription: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
    lineHeight: 18,
  },
  statusBadge: {
    paddingHorizontal: SPACING.sm,
    paddingVertical: SPACING.xs,
    borderRadius: BORDER_RADIUS.sm,
    alignSelf: 'flex-start',
  },
  statusText: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.white,
    fontWeight: FONT_WEIGHT.bold,
    textTransform: 'uppercase',
  },
  assignmentDates: {
    marginBottom: SPACING.md,
  },
  dateItem: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: SPACING.xs,
  },
  dateText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
    marginLeft: SPACING.xs,
  },
  gradeContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: SPACING.sm,
    padding: SPACING.sm,
    backgroundColor: COLORS.lightGray,
    borderRadius: BORDER_RADIUS.sm,
  },
  gradeLabel: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
    fontWeight: FONT_WEIGHT.medium,
  },
  gradeValue: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    marginLeft: SPACING.sm,
  },
  commentsContainer: {
    marginBottom: SPACING.md,
    padding: SPACING.sm,
    backgroundColor: COLORS.lightGray,
    borderRadius: BORDER_RADIUS.sm,
  },
  commentsLabel: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
    fontWeight: FONT_WEIGHT.medium,
    marginBottom: SPACING.xs,
  },
  commentsText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
    fontStyle: 'italic',
  },
  assignmentActions: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  actionButton: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: SPACING.md,
    paddingVertical: SPACING.sm,
    borderRadius: BORDER_RADIUS.md,
    flex: 0.48,
    justifyContent: 'center',
  },
  submitButton: {
    backgroundColor: COLORS.success,
  },
  detailButton: {
    backgroundColor: 'transparent',
    borderWidth: 1,
    borderColor: COLORS.primary,
  },
  actionButtonText: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.medium,
    marginLeft: SPACING.xs,
    color: COLORS.white,
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

export default AssignmentsScreen;
