import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  Alert,
  Modal,
  TextInput,
  ActivityIndicator
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT, BORDER_RADIUS, FONTS } from '../../config/theme';
import { DEMO_STUDENT_SERVICES } from '../../data/demoData';

const StudentServicesScreen = ({ navigation }) => {
  const [services, setServices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedCategory, setSelectedCategory] = useState('TODOS');
  const [modalVisible, setModalVisible] = useState(false);
  const [selectedService, setSelectedService] = useState(null);
  const [requestForm, setRequestForm] = useState({
    observaciones: '',
    formato: 'DIGITAL',
    urgente: false
  });

  const categories = [
    { key: 'TODOS', label: 'Todos' },
    { key: 'CERTIFICADOS', label: 'Certificados' },
    { key: 'CREDENCIALES', label: 'Credenciales' },
    { key: 'TRAMITES', label: 'Trámites' }
  ];

  useEffect(() => {
    loadServices();
  }, []);

  const loadServices = async () => {
    try {
      setLoading(true);
      await new Promise(resolve => setTimeout(resolve, 800));
      setServices(DEMO_STUDENT_SERVICES);
    } catch (error) {
      Alert.alert('Error', 'No se pudieron cargar los servicios');
    } finally {
      setLoading(false);
    }
  };

  const filteredServices = selectedCategory === 'TODOS' 
    ? services 
    : services.filter(service => service.categoria === selectedCategory);

  const handleServiceRequest = (service) => {
    setSelectedService(service);
    setModalVisible(true);
  };

  const submitRequest = () => {
    Alert.alert(
      'Solicitud Enviada',
      `Su solicitud de "${selectedService.nombre}" ha sido enviada exitosamente. Recibirá una confirmación por email.`,
      [
        {
          text: 'OK',
          onPress: () => {
            setModalVisible(false);
            setRequestForm({ observaciones: '', formato: 'DIGITAL', urgente: false });
          }
        }
      ]
    );
  };

  const getServiceIcon = (categoria) => {
    switch (categoria) {
      case 'CERTIFICADOS': return 'document-text';
      case 'CREDENCIALES': return 'card';
      case 'TRAMITES': return 'folder';
      default: return 'documents';
    }
  };

  const formatPrice = (price) => {
    return `$${price.toLocaleString('es-CL')}`;
  };

  if (loading) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color={COLORS.primary} />
        <Text style={styles.loadingText}>Cargando servicios...</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      {/* Header */}
      <View style={styles.header}>
        <Text style={styles.headerTitle}>Servicios Estudiantiles</Text>
        <Text style={styles.headerSubtitle}>DUOC UC - Plaza Vespucio</Text>
      </View>

      {/* Filtros de categoría */}
      <ScrollView 
        horizontal 
        showsHorizontalScrollIndicator={false}
        style={styles.categoriesContainer}
      >
        {categories.map((category) => (
          <TouchableOpacity
            key={category.key}
            style={[
              styles.categoryButton,
              selectedCategory === category.key && styles.categoryButtonActive
            ]}
            onPress={() => setSelectedCategory(category.key)}
          >
            <Text style={[
              styles.categoryText,
              selectedCategory === category.key && styles.categoryTextActive
            ]}>
              {category.label}
            </Text>
          </TouchableOpacity>
        ))}
      </ScrollView>

      {/* Lista de servicios */}
      <ScrollView style={styles.servicesList}>
        {filteredServices.map((service) => (
          <View key={service.id} style={styles.serviceCard}>
            <View style={styles.serviceHeader}>
              <View style={styles.serviceIcon}>
                <Ionicons 
                  name={getServiceIcon(service.categoria)} 
                  size={24} 
                  color={COLORS.primary} 
                />
              </View>
              <View style={styles.serviceInfo}>
                <Text style={styles.serviceName}>{service.nombre}</Text>
                <Text style={styles.serviceDescription}>{service.descripcion}</Text>
              </View>
            </View>

            <View style={styles.serviceDetails}>
              <View style={styles.detailRow}>
                <Ionicons name="cash" size={16} color={COLORS.gray} />
                <Text style={styles.detailText}>Precio: {formatPrice(service.precio)}</Text>
              </View>
              
              <View style={styles.detailRow}>
                <Ionicons name="time" size={16} color={COLORS.gray} />
                <Text style={styles.detailText}>Entrega: {service.tiempoEntrega}</Text>
              </View>
              
              <View style={styles.detailRow}>
                <Ionicons name="document" size={16} color={COLORS.gray} />
                <Text style={styles.detailText}>Formato: {service.formato}</Text>
              </View>
            </View>

            {service.requisitos && (
              <View style={styles.requirements}>
                <Text style={styles.requirementsTitle}>Requisitos:</Text>
                {service.requisitos.map((req, index) => (
                  <Text key={index} style={styles.requirementItem}>• {req}</Text>
                ))}
              </View>
            )}

            <TouchableOpacity 
              style={styles.requestButton}
              onPress={() => handleServiceRequest(service)}
            >
              <Ionicons name="send" size={16} color={COLORS.white} />
              <Text style={styles.requestButtonText}>Solicitar</Text>
            </TouchableOpacity>
          </View>
        ))}
      </ScrollView>

      {/* Modal de solicitud */}
      <Modal
        animationType="slide"
        transparent={true}
        visible={modalVisible}
        onRequestClose={() => setModalVisible(false)}
      >
        <View style={styles.modalOverlay}>
          <View style={styles.modalContent}>
            <View style={styles.modalHeader}>
              <Text style={styles.modalTitle}>Solicitar Servicio</Text>
              <TouchableOpacity onPress={() => setModalVisible(false)}>
                <Ionicons name="close" size={24} color={COLORS.gray} />
              </TouchableOpacity>
            </View>

            {selectedService && (
              <ScrollView style={styles.modalBody}>
                <Text style={styles.selectedServiceName}>{selectedService.nombre}</Text>
                <Text style={styles.selectedServicePrice}>
                  Precio: {formatPrice(selectedService.precio)}
                </Text>

                <View style={styles.formGroup}>
                  <Text style={styles.formLabel}>Formato de entrega:</Text>
                  <View style={styles.formatOptions}>
                    <TouchableOpacity
                      style={[
                        styles.formatOption,
                        requestForm.formato === 'DIGITAL' && styles.formatOptionActive
                      ]}
                      onPress={() => setRequestForm({...requestForm, formato: 'DIGITAL'})}
                    >
                      <Text style={[
                        styles.formatOptionText,
                        requestForm.formato === 'DIGITAL' && styles.formatOptionTextActive
                      ]}>Digital</Text>
                    </TouchableOpacity>
                    
                    <TouchableOpacity
                      style={[
                        styles.formatOption,
                        requestForm.formato === 'FISICO' && styles.formatOptionActive
                      ]}
                      onPress={() => setRequestForm({...requestForm, formato: 'FISICO'})}
                    >
                      <Text style={[
                        styles.formatOptionText,
                        requestForm.formato === 'FISICO' && styles.formatOptionTextActive
                      ]}>Físico</Text>
                    </TouchableOpacity>
                  </View>
                </View>

                <View style={styles.formGroup}>
                  <Text style={styles.formLabel}>Observaciones (opcional):</Text>
                  <TextInput
                    style={styles.textInput}
                    multiline
                    numberOfLines={3}
                    placeholder="Agregar comentarios adicionales..."
                    value={requestForm.observaciones}
                    onChangeText={(text) => setRequestForm({...requestForm, observaciones: text})}
                  />
                </View>

                <TouchableOpacity
                  style={styles.urgentOption}
                  onPress={() => setRequestForm({...requestForm, urgente: !requestForm.urgente})}
                >
                  <Ionicons 
                    name={requestForm.urgente ? "checkbox" : "square-outline"} 
                    size={20} 
                    color={COLORS.primary} 
                  />
                  <Text style={styles.urgentText}>
                    Tramitación urgente (+50% del valor)
                  </Text>
                </TouchableOpacity>

                <View style={styles.modalActions}>
                  <TouchableOpacity 
                    style={styles.cancelButton}
                    onPress={() => setModalVisible(false)}
                  >
                    <Text style={styles.cancelButtonText}>Cancelar</Text>
                  </TouchableOpacity>
                  
                  <TouchableOpacity 
                    style={styles.confirmButton}
                    onPress={submitRequest}
                  >
                    <Text style={styles.confirmButtonText}>Confirmar Solicitud</Text>
                  </TouchableOpacity>
                </View>
              </ScrollView>
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
    marginTop: 10,
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
  categoriesContainer: {
    backgroundColor: COLORS.white,
    paddingVertical: SPACING.sm,
  },
  categoryButton: {
    paddingHorizontal: SPACING.lg,
    paddingVertical: SPACING.sm,
    marginHorizontal: 4,
    borderRadius: 20,
    backgroundColor: COLORS.lightGray,
  },
  categoryButtonActive: {
    backgroundColor: COLORS.primary,
  },
  categoryText: {
    fontSize: FONT_SIZE.xs,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.textSecondary,
  },
  categoryTextActive: {
    color: COLORS.white,
  },
  servicesList: {
    flex: 1,
    padding: SPACING.sm,
  },
  serviceCard: {
    backgroundColor: COLORS.white,
    borderRadius: 12,
    padding: SPACING.lg,
    marginBottom: SPACING.sm,
    elevation: 3,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
  },
  serviceHeader: {
    flexDirection: 'row',
    marginBottom: SPACING.sm,
  },
  serviceIcon: {
    width: 48,
    height: 48,
    borderRadius: 24,
    backgroundColor: COLORS.primaryLight,
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: SPACING.sm,
  },
  serviceInfo: {
    flex: 1,
  },
  serviceName: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: 4,
  },
  serviceDescription: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.textSecondary,
  },
  serviceDetails: {
    marginBottom: SPACING.sm,
  },
  detailRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 4,
  },
  detailText: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.textSecondary,
    marginLeft: 8,
  },
  requirements: {
    backgroundColor: COLORS.lightGray,
    padding: SPACING.sm,
    borderRadius: 8,
    marginBottom: SPACING.sm,
  },
  requirementsTitle: {
    fontSize: FONT_SIZE.xs,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: 4,
  },
  requirementItem: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.textSecondary,
    marginLeft: 8,
  },
  requestButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: COLORS.primary,
    paddingVertical: SPACING.sm,
    borderRadius: 8,
  },
  requestButtonText: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.white,
    marginLeft: 8,
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
  selectedServiceName: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: 4,
  },
  selectedServicePrice: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.primary,
    marginBottom: SPACING.lg,
  },
  formGroup: {
    marginBottom: SPACING.lg,
  },
  formLabel: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.text,
    marginBottom: SPACING.sm,
  },
  formatOptions: {
    flexDirection: 'row',
    gap: SPACING.sm,
  },
  formatOption: {
    flex: 1,
    paddingVertical: SPACING.sm,
    paddingHorizontal: SPACING.lg,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: COLORS.lightGray,
    alignItems: 'center',
  },
  formatOptionActive: {
    borderColor: COLORS.primary,
    backgroundColor: COLORS.primaryLight,
  },
  formatOptionText: {
    fontSize: FONT_SIZE.xs,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.textSecondary,
  },
  formatOptionTextActive: {
    color: COLORS.primary,
  },
  textInput: {
    borderWidth: 1,
    borderColor: COLORS.lightGray,
    borderRadius: 8,
    padding: SPACING.sm,
    fontSize: FONT_SIZE.xs,
    color: COLORS.text,
    textAlignVertical: 'top',
  },
  urgentOption: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: SPACING.lg,
  },
  urgentText: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.text,
    marginLeft: 8,
  },
  modalActions: {
    flexDirection: 'row',
    gap: SPACING.sm,
  },
  cancelButton: {
    flex: 1,
    paddingVertical: SPACING.sm,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: COLORS.textSecondary,
    alignItems: 'center',
  },
  cancelButtonText: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.textSecondary,
  },
  confirmButton: {
    flex: 1,
    paddingVertical: SPACING.sm,
    borderRadius: 8,
    backgroundColor: COLORS.primary,
    alignItems: 'center',
  },
  confirmButtonText: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.white,
  },
});

export default StudentServicesScreen;