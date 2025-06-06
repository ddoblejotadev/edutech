import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  TextInput,
  Alert,
  Modal,
  FlatList
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, FONTS, FONT_SIZE, SPACING, FONT_WEIGHT, BORDER_RADIUS } from '../../config/theme';
import { DEMO_LIBRARY_RESOURCES, DEMO_BORROWED_BOOKS } from '../../data/demoData';

const LibraryScreen = ({ navigation }) => {
  const [activeTab, setActiveTab] = useState('RECURSOS');
  const [searchQuery, setSearchQuery] = useState('');
  const [resources, setResources] = useState([]);
  const [borrowedBooks, setBorrowedBooks] = useState([]);
  const [selectedResource, setSelectedResource] = useState(null);
  const [modalVisible, setModalVisible] = useState(false);
  const [loading, setLoading] = useState(true);

  const tabs = [
    { key: 'RECURSOS', label: 'Recursos', icon: 'library' },
    { key: 'PRESTAMOS', label: 'Mis Préstamos', icon: 'bookmark' },
    { key: 'RESERVAS', label: 'Reservas', icon: 'calendar' }
  ];

  useEffect(() => {
    loadLibraryData();
  }, []);

  const loadLibraryData = async () => {
    try {
      setLoading(true);
      await new Promise(resolve => setTimeout(resolve, 1000));
      setResources(DEMO_LIBRARY_RESOURCES);
      setBorrowedBooks(DEMO_BORROWED_BOOKS);
    } catch (error) {
      Alert.alert('Error', 'No se pudieron cargar los recursos de biblioteca');
    } finally {
      setLoading(false);
    }
  };

  const filteredResources = resources.filter(resource =>
    resource.titulo.toLowerCase().includes(searchQuery.toLowerCase()) ||
    resource.autor.toLowerCase().includes(searchQuery.toLowerCase()) ||
    resource.categoria.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const handleResourceSelect = (resource) => {
    setSelectedResource(resource);
    setModalVisible(true);
  };

  const handleReserve = () => {
    Alert.alert(
      'Reserva Realizada',
      `Has reservado "${selectedResource.titulo}". Te notificaremos cuando esté disponible.`,
      [{ text: 'OK', onPress: () => setModalVisible(false) }]
    );
  };

  const handleRenew = (bookId) => {
    Alert.alert(
      'Renovar Préstamo',
      '¿Deseas renovar este préstamo por 7 días adicionales?',
      [
        { text: 'Cancelar', style: 'cancel' },
        { 
          text: 'Renovar', 
          onPress: () => {
            setBorrowedBooks(prevBooks =>
              prevBooks.map(book =>
                book.id === bookId
                  ? {
                      ...book,
                      fechaDevolucion: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString(),
                      renovaciones: book.renovaciones + 1
                    }
                  : book
              )
            );
            Alert.alert('Éxito', 'Préstamo renovado exitosamente');
          }
        }
      ]
    );
  };

  const getResourceIcon = (tipo) => {
    switch (tipo) {
      case 'LIBRO': return 'book';
      case 'REVISTA': return 'newspaper';
      case 'TESIS': return 'document-text';
      case 'DIGITAL': return 'tablet-portrait';
      case 'AUDIOVISUAL': return 'videocam';
      default: return 'library';
    }
  };

  const getStatusColor = (disponible) => {
    return disponible ? COLORS.success : COLORS.error;
  };

  const getDaysUntilReturn = (fechaDevolucion) => {
    const today = new Date();
    const returnDate = new Date(fechaDevolucion);
    const diffTime = returnDate - today;
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays;
  };

  const renderResourceItem = ({ item }) => (
    <TouchableOpacity
      style={styles.resourceCard}
      onPress={() => handleResourceSelect(item)}
    >
      <View style={styles.resourceHeader}>
        <View style={styles.resourceIcon}>
          <Ionicons 
            name={getResourceIcon(item.tipo)} 
            size={24} 
            color={COLORS.primary} 
          />
        </View>
        <View style={styles.resourceInfo}>
          <Text style={styles.resourceTitle} numberOfLines={2}>{item.titulo}</Text>
          <Text style={styles.resourceAuthor}>{item.autor}</Text>
          <Text style={styles.resourceDetails}>
            {item.editorial} • {item.año} • {item.categoria}
          </Text>
        </View>
      </View>
      
      <View style={styles.resourceFooter}>
        <View style={[
          styles.statusBadge,
          { backgroundColor: getStatusColor(item.disponible) }
        ]}>
          <Text style={styles.statusText}>
            {item.disponible ? 'Disponible' : 'No disponible'}
          </Text>
        </View>
        <Text style={styles.location}>Ubicación: {item.ubicacion}</Text>
      </View>
    </TouchableOpacity>
  );

  const renderBorrowedBook = ({ item }) => {
    const daysLeft = getDaysUntilReturn(item.fechaDevolucion);
    const isOverdue = daysLeft < 0;
    const isNearDue = daysLeft <= 3 && daysLeft >= 0;

    return (
      <View style={styles.borrowedCard}>
        <View style={styles.borrowedHeader}>
          <Text style={styles.borrowedTitle} numberOfLines={2}>{item.titulo}</Text>
          <Text style={styles.borrowedAuthor}>{item.autor}</Text>
        </View>
        
        <View style={styles.borrowedInfo}>
          <View style={styles.dateInfo}>
            <Text style={styles.dateLabel}>Prestado:</Text>
            <Text style={styles.dateValue}>
              {new Date(item.fechaPrestamo).toLocaleDateString('es-CL')}
            </Text>
          </View>
          
          <View style={styles.dateInfo}>
            <Text style={styles.dateLabel}>Devolver:</Text>
            <Text style={[
              styles.dateValue,
              { color: isOverdue ? COLORS.error : isNearDue ? COLORS.warning : COLORS.text }
            ]}>
              {new Date(item.fechaDevolucion).toLocaleDateString('es-CL')}
            </Text>
          </View>
        </View>

        <View style={styles.borrowedFooter}>
          <Text style={[
            styles.daysLeft,
            { color: isOverdue ? COLORS.error : isNearDue ? COLORS.warning : COLORS.success }
          ]}>
            {isOverdue ? `${Math.abs(daysLeft)} días de atraso` :
             daysLeft === 0 ? 'Vence hoy' :
             `${daysLeft} días restantes`}
          </Text>
          
          <View style={styles.borrowedActions}>
            <Text style={styles.renewalInfo}>
              Renovaciones: {item.renovaciones}/3
            </Text>
            {item.renovaciones < 3 && (
              <TouchableOpacity
                style={styles.renewButton}
                onPress={() => handleRenew(item.id)}
              >
                <Text style={styles.renewButtonText}>Renovar</Text>
              </TouchableOpacity>
            )}
          </View>
        </View>
      </View>
    );
  };

  if (loading) {
    return (
      <View style={styles.loadingContainer}>
        <Text style={styles.loadingText}>Cargando biblioteca...</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      {/* Header */}
      <View style={styles.header}>
        <Text style={styles.headerTitle}>Biblioteca Digital</Text>
        <Text style={styles.headerSubtitle}>DUOC UC - Plaza Vespucio</Text>
      </View>

      {/* Tabs */}
      <View style={styles.tabContainer}>
        {tabs.map((tab) => (
          <TouchableOpacity
            key={tab.key}
            style={[
              styles.tab,
              activeTab === tab.key && styles.activeTab
            ]}
            onPress={() => setActiveTab(tab.key)}
          >
            <Ionicons 
              name={tab.icon} 
              size={20} 
              color={activeTab === tab.key ? COLORS.primary : COLORS.gray} 
            />
            <Text style={[
              styles.tabText,
              activeTab === tab.key && styles.activeTabText
            ]}>
              {tab.label}
            </Text>
          </TouchableOpacity>
        ))}
      </View>

      {/* Content */}
      {activeTab === 'RECURSOS' && (
        <View style={styles.content}>
          {/* Search */}
          <View style={styles.searchContainer}>
            <Ionicons name="search" size={20} color={COLORS.gray} />
            <TextInput
              style={styles.searchInput}
              placeholder="Buscar libros, revistas, tesis..."
              value={searchQuery}
              onChangeText={setSearchQuery}
            />
          </View>

          {/* Quick filters */}
          <ScrollView 
            horizontal 
            showsHorizontalScrollIndicator={false}
            style={styles.filtersContainer}
          >
            {['Todos', 'Libros', 'Revistas', 'Tesis', 'Digital'].map((filter) => (
              <TouchableOpacity key={filter} style={styles.filterButton}>
                <Text style={styles.filterText}>{filter}</Text>
              </TouchableOpacity>
            ))}
          </ScrollView>

          {/* Resources list */}
          <FlatList
            data={filteredResources}
            renderItem={renderResourceItem}
            keyExtractor={(item) => item.id.toString()}
            showsVerticalScrollIndicator={false}
            style={styles.resourcesList}
          />
        </View>
      )}

      {activeTab === 'PRESTAMOS' && (
        <View style={styles.content}>
          <FlatList
            data={borrowedBooks}
            renderItem={renderBorrowedBook}
            keyExtractor={(item) => item.id.toString()}
            showsVerticalScrollIndicator={false}
            style={styles.borrowedList}
            ListEmptyComponent={
              <View style={styles.emptyState}>
                <Ionicons name="bookmark-outline" size={48} color={COLORS.gray} />
                <Text style={styles.emptyText}>No tienes préstamos activos</Text>
              </View>
            }
          />
        </View>
      )}

      {activeTab === 'RESERVAS' && (
        <View style={styles.content}>
          <View style={styles.emptyState}>
            <Ionicons name="calendar-outline" size={48} color={COLORS.gray} />
            <Text style={styles.emptyText}>No tienes reservas pendientes</Text>
            <Text style={styles.emptySubtext}>
              Reserva recursos desde la sección de búsqueda
            </Text>
          </View>
        </View>
      )}

      {/* Resource Detail Modal */}
      <Modal
        animationType="slide"
        transparent={true}
        visible={modalVisible}
        onRequestClose={() => setModalVisible(false)}
      >
        <View style={styles.modalOverlay}>
          <View style={styles.modalContent}>
            {selectedResource && (
              <>
                <View style={styles.modalHeader}>
                  <Text style={styles.modalTitle}>Detalle del Recurso</Text>
                  <TouchableOpacity onPress={() => setModalVisible(false)}>
                    <Ionicons name="close" size={24} color={COLORS.gray} />
                  </TouchableOpacity>
                </View>

                <ScrollView style={styles.modalBody}>
                  <Text style={styles.detailTitle}>{selectedResource.titulo}</Text>
                  <Text style={styles.detailAuthor}>{selectedResource.autor}</Text>
                  
                  <View style={styles.detailInfo}>
                    <View style={styles.detailRow}>
                      <Text style={styles.detailLabel}>Editorial:</Text>
                      <Text style={styles.detailValue}>{selectedResource.editorial}</Text>
                    </View>
                    <View style={styles.detailRow}>
                      <Text style={styles.detailLabel}>Año:</Text>
                      <Text style={styles.detailValue}>{selectedResource.año}</Text>
                    </View>
                    <View style={styles.detailRow}>
                      <Text style={styles.detailLabel}>Categoría:</Text>
                      <Text style={styles.detailValue}>{selectedResource.categoria}</Text>
                    </View>
                    <View style={styles.detailRow}>
                      <Text style={styles.detailLabel}>Ubicación:</Text>
                      <Text style={styles.detailValue}>{selectedResource.ubicacion}</Text>
                    </View>
                    <View style={styles.detailRow}>
                      <Text style={styles.detailLabel}>ISBN:</Text>
                      <Text style={styles.detailValue}>{selectedResource.isbn}</Text>
                    </View>
                  </View>

                  {selectedResource.descripcion && (
                    <View style={styles.descriptionContainer}>
                      <Text style={styles.descriptionTitle}>Descripción:</Text>
                      <Text style={styles.descriptionText}>
                        {selectedResource.descripcion}
                      </Text>
                    </View>
                  )}
                </ScrollView>

                <View style={styles.modalActions}>
                  {selectedResource.disponible ? (
                    <TouchableOpacity 
                      style={styles.reserveButton}
                      onPress={handleReserve}
                    >
                      <Ionicons name="bookmark" size={16} color={COLORS.white} />
                      <Text style={styles.reserveButtonText}>Reservar</Text>
                    </TouchableOpacity>
                  ) : (
                    <View style={styles.unavailableNotice}>
                      <Text style={styles.unavailableText}>
                        No disponible - Se puede reservar
                      </Text>
                      <TouchableOpacity 
                        style={styles.waitlistButton}
                        onPress={handleReserve}
                      >
                        <Text style={styles.waitlistButtonText}>Agregar a lista de espera</Text>
                      </TouchableOpacity>
                    </View>
                  )}
                </View>
              </>
            )}
          </View>
        </View>
      </Modal>
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
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
  },
  header: {
    backgroundColor: COLORS.primary,
    padding: SPACING.lg,
    paddingTop: SPACING.lg + 20,
  },
  headerTitle: {
    fontSize: FONT_SIZE.xxl,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.white,
  },
  headerSubtitle: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.white,
    opacity: 0.9,
  },
  tabContainer: {
    flexDirection: 'row',
    backgroundColor: COLORS.white,
    elevation: 2,
  },
  tab: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: SPACING.sm,
    borderBottomWidth: 2,
    borderBottomColor: 'transparent',
  },
  activeTab: {
    borderBottomColor: COLORS.primary,
  },
  tabText: {
    fontSize: FONT_SIZE.xs,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.textSecondary,
    marginLeft: 4,
  },
  activeTabText: {
    color: COLORS.primary,
  },
  content: {
    flex: 1,
    padding: SPACING.sm,
  },
  searchContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: COLORS.white,
    borderRadius: BORDER_RADIUS.md,
    paddingHorizontal: SPACING.sm,
    paddingVertical: SPACING.sm,
    marginBottom: SPACING.sm,
    elevation: 2,
  },
  searchInput: {
    flex: 1,
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
    marginLeft: SPACING.sm,
  },
  filtersContainer: {
    marginBottom: SPACING.sm,
  },
  filterButton: {
    backgroundColor: COLORS.lightGray,
    paddingHorizontal: SPACING.lg,
    paddingVertical: SPACING.sm,
    borderRadius: 20,
    marginRight: SPACING.sm,
  },
  filterText: {
    fontSize: FONT_SIZE.xs,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.textSecondary,
  },
  resourcesList: {
    flex: 1,
  },
  resourceCard: {
    backgroundColor: COLORS.white,
    borderRadius: BORDER_RADIUS.md,
    padding: SPACING.lg,
    marginBottom: SPACING.sm,
    elevation: 2,
  },
  resourceHeader: {
    flexDirection: 'row',
    marginBottom: SPACING.sm,
  },
  resourceIcon: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: COLORS.primaryLight,
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: SPACING.sm,
  },
  resourceInfo: {
    flex: 1,
  },
  resourceTitle: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: 2,
  },
  resourceAuthor: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.textSecondary,
    marginBottom: 2,
  },
  resourceDetails: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.textSecondary,
  },
  resourceFooter: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  statusBadge: {
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 12,
  },
  statusText: {
    fontSize: FONT_SIZE.xs,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.white,
  },
  location: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.textSecondary,
  },
  borrowedList: {
    flex: 1,
  },
  borrowedCard: {
    backgroundColor: COLORS.white,
    borderRadius: BORDER_RADIUS.md,
    padding: SPACING.lg,
    marginBottom: SPACING.sm,
    elevation: 2,
  },
  borrowedHeader: {
    marginBottom: SPACING.sm,
  },
  borrowedTitle: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: 2,
  },
  borrowedAuthor: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.textSecondary,
  },
  borrowedInfo: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: SPACING.sm,
  },
  dateInfo: {
    flex: 1,
  },
  dateLabel: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.textSecondary,
  },
  dateValue: {
    fontSize: FONT_SIZE.xs,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.text,
  },
  borrowedFooter: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  daysLeft: {
    fontSize: FONT_SIZE.xs,
    fontWeight: FONT_WEIGHT.bold,
  },
  borrowedActions: {
    alignItems: 'flex-end',
  },
  renewalInfo: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.textSecondary,
    marginBottom: 4,
  },
  renewButton: {
    backgroundColor: COLORS.primary,
    paddingHorizontal: 12,
    paddingVertical: 4,
    borderRadius: BORDER_RADIUS.sm,
  },
  renewButtonText: {
    fontSize: FONT_SIZE.xs,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.white,
  },
  emptyState: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingTop: 60,
  },
  emptyText: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.textSecondary,
    marginTop: SPACING.sm,
  },
  emptySubtext: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.textSecondary,
    marginTop: 4,
    textAlign: 'center',
  },
  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    justifyContent: 'flex-end',
  },
  modalContent: {
    backgroundColor: COLORS.white,
    borderTopLeftRadius: 20,
    borderTopRightRadius: 20,
    maxHeight: '80%',
  },
  modalHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: SPACING.lg,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  modalTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
  },
  modalBody: {
    padding: SPACING.lg,
  },
  detailTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: 4,
  },
  detailAuthor: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.textSecondary,
    marginBottom: SPACING.lg,
  },
  detailInfo: {
    marginBottom: SPACING.lg,
  },
  detailRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 4,
  },
  detailLabel: {
    fontSize: FONT_SIZE.xs,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.textSecondary,
  },
  detailValue: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.text,
  },
  descriptionContainer: {
    marginBottom: SPACING.lg,
  },
  descriptionTitle: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.sm,
  },
  descriptionText: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.textSecondary,
    lineHeight: 20,
  },
  modalActions: {
    padding: SPACING.lg,
    borderTopWidth: 1,
    borderTopColor: COLORS.lightGray,
  },
  reserveButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: COLORS.primary,
    paddingVertical: SPACING.sm,
    borderRadius: BORDER_RADIUS.md,
  },
  reserveButtonText: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.white,
    marginLeft: 8,
  },
  unavailableNotice: {
    alignItems: 'center',
  },
  unavailableText: {
    fontSize: FONT_SIZE.xs,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.error,
    marginBottom: SPACING.sm,
  },
  waitlistButton: {
    backgroundColor: COLORS.secondary,
    paddingVertical: SPACING.sm,
    paddingHorizontal: SPACING.lg,
    borderRadius: BORDER_RADIUS.md,
  },
  waitlistButtonText: {
    fontSize: FONT_SIZE.xs,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.white,
  },
});

export default LibraryScreen;