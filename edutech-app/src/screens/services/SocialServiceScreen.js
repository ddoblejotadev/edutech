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
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT } from '../../config/theme';

const SocialServiceScreen = ({ navigation, route }) => {
  const { title } = route.params || { title: 'Práctica Profesional' };
  const [activeTab, setActiveTab] = useState('estado');
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [practiceData, setPracticeData] = useState({
    estado: 'EN_BUSQUEDA',
    empresaActual: null,
    horasCompletas: 0,
    horasRequeridas: 720,
    evaluaciones: []
  });

  const [newApplication, setNewApplication] = useState({
    empresa: '',
    cargo: '',
    supervisor: '',
    descripcion: ''
  });

  useEffect(() => {
    loadPracticeData();
  }, []);

  const loadPracticeData = async () => {
    setLoading(true);
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    // Datos demo de práctica profesional
    setPracticeData({
      estado: 'EN_CURSO',
      empresaActual: {
        nombre: 'TechSoft Solutions SpA',
        supervisor: 'Ing. Patricia Morales',
        cargo: 'Desarrollador Junior',
        fechaInicio: '2024-03-15',
        telefono: '+56 2 2345 6789',
        direccion: 'Av. Providencia 1234, Providencia'
      },
      horasCompletas: 456,
      horasRequeridas: 720,
      evaluaciones: [
        {
          id: 1,
          periodo: 'Primer Mes',
          nota: 6.5,
          fecha: '2024-04-15',
          comentarios: 'Excelente adaptación al equipo y metodologías de trabajo.'
        },
        {
          id: 2,
          periodo: 'Segundo Mes',
          nota: 6.8,
          fecha: '2024-05-15',
          comentarios: 'Muestra iniciativa y capacidad de resolución de problemas.'
        }
      ]
    });
    setLoading(false);
  };

  const submitApplication = () => {
    if (!newApplication.empresa || !newApplication.cargo) {
      Alert.alert('Error', 'Por favor completa todos los campos obligatorios');
      return;
    }

    Alert.alert(
      'Postulación Enviada',
      'Tu postulación ha sido enviada exitosamente. Te contactaremos pronto.',
      [{ text: 'OK', onPress: () => setModalVisible(false) }]
    );

    setNewApplication({
      empresa: '',
      cargo: '',
      supervisor: '',
      descripcion: ''
    });
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'EN_BUSQUEDA': return COLORS.warning;
      case 'EN_CURSO': return COLORS.success;
      case 'COMPLETADA': return COLORS.primary;
      default: return COLORS.gray;
    }
  };

  const getStatusText = (status) => {
    switch (status) {
      case 'EN_BUSQUEDA': return 'Buscando Práctica';
      case 'EN_CURSO': return 'En Curso';
      case 'COMPLETADA': return 'Completada';
      default: return 'Sin Estado';
    }
  };

  const renderStatusTab = () => (
    <View style={styles.tabContent}>
      {/* Estado actual */}
      <View style={styles.statusCard}>
        <View style={styles.statusHeader}>
          <View style={[styles.statusBadge, { backgroundColor: getStatusColor(practiceData.estado) }]}>
            <Text style={styles.statusBadgeText}>{getStatusText(practiceData.estado)}</Text>
          </View>
        </View>

        {practiceData.empresaActual && (
          <View style={styles.companyInfo}>
            <Text style={styles.companyName}>{practiceData.empresaActual.nombre}</Text>
            <Text style={styles.position}>{practiceData.empresaActual.cargo}</Text>
            
            <View style={styles.detailRow}>
              <Ionicons name="person" size={16} color={COLORS.gray} />
              <Text style={styles.detailText}>Supervisor: {practiceData.empresaActual.supervisor}</Text>
            </View>
            
            <View style={styles.detailRow}>
              <Ionicons name="calendar" size={16} color={COLORS.gray} />
              <Text style={styles.detailText}>Inicio: {practiceData.empresaActual.fechaInicio}</Text>
            </View>
            
            <View style={styles.detailRow}>
              <Ionicons name="call" size={16} color={COLORS.gray} />
              <Text style={styles.detailText}>{practiceData.empresaActual.telefono}</Text>
            </View>
          </View>
        )}

        {/* Progreso de horas */}
        <View style={styles.progressSection}>
          <Text style={styles.progressTitle}>Progreso de Horas</Text>
          <View style={styles.progressBar}>
            <View 
              style={[
                styles.progressFill, 
                { width: `${(practiceData.horasCompletas / practiceData.horasRequeridas) * 100}%` }
              ]} 
            />
          </View>
          <Text style={styles.progressText}>
            {practiceData.horasCompletas} / {practiceData.horasRequeridas} horas
          </Text>
        </View>
      </View>

      {/* Evaluaciones */}
      {practiceData.evaluaciones.length > 0 && (
        <View style={styles.evaluationsCard}>
          <Text style={styles.cardTitle}>Evaluaciones</Text>
          {practiceData.evaluaciones.map((evaluation) => (
            <View key={evaluation.id} style={styles.evaluationItem}>
              <View style={styles.evaluationHeader}>
                <Text style={styles.evaluationPeriod}>{evaluation.periodo}</Text>
                <Text style={styles.evaluationGrade}>Nota: {evaluation.nota}</Text>
              </View>
              <Text style={styles.evaluationDate}>{evaluation.fecha}</Text>
              <Text style={styles.evaluationComments}>{evaluation.comentarios}</Text>
            </View>
          ))}
        </View>
      )}
    </View>
  );

  const renderOffersTab = () => (
    <View style={styles.tabContent}>
      <Text style={styles.sectionTitle}>Ofertas Disponibles</Text>
      
      {[1, 2, 3].map((item) => (
        <View key={item} style={styles.offerCard}>
          <View style={styles.offerHeader}>
            <Text style={styles.offerCompany}>Empresa Tecnológica {item}</Text>
            <Text style={styles.offerPosition}>Desarrollador Junior</Text>
          </View>
          
          <View style={styles.offerDetails}>
            <View style={styles.detailRow}>
              <Ionicons name="location" size={16} color={COLORS.gray} />
              <Text style={styles.detailText}>Santiago, Región Metropolitana</Text>
            </View>
            
            <View style={styles.detailRow}>
              <Ionicons name="time" size={16} color={COLORS.gray} />
              <Text style={styles.detailText}>720 horas - 6 meses</Text>
            </View>
          </View>
          
          <TouchableOpacity style={styles.applyButton}>
            <Text style={styles.applyButtonText}>Postular</Text>
          </TouchableOpacity>
        </View>
      ))}
      
      <TouchableOpacity 
        style={styles.addButton}
        onPress={() => setModalVisible(true)}
      >
        <Ionicons name="add" size={24} color={COLORS.white} />
        <Text style={styles.addButtonText}>Agregar Nueva Postulación</Text>
      </TouchableOpacity>
    </View>
  );

  if (loading) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color={COLORS.primary} />
        <Text style={styles.loadingText}>Cargando información...</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      {/* Header */}
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()} style={styles.backButton}>
          <Ionicons name="arrow-back" size={24} color={COLORS.white} />
        </TouchableOpacity>
        <Text style={styles.headerTitle}>{title}</Text>
      </View>

      {/* Tabs */}
      <View style={styles.tabsContainer}>
        <TouchableOpacity 
          style={[styles.tab, activeTab === 'estado' && styles.activeTab]}
          onPress={() => setActiveTab('estado')}
        >
          <Text style={[styles.tabText, activeTab === 'estado' && styles.activeTabText]}>
            Mi Práctica
          </Text>
        </TouchableOpacity>
        
        <TouchableOpacity 
          style={[styles.tab, activeTab === 'ofertas' && styles.activeTab]}
          onPress={() => setActiveTab('ofertas')}
        >
          <Text style={[styles.tabText, activeTab === 'ofertas' && styles.activeTabText]}>
            Ofertas
          </Text>
        </TouchableOpacity>
      </View>
      
      <ScrollView style={styles.content}>
        {activeTab === 'estado' ? renderStatusTab() : renderOffersTab()}
      </ScrollView>

      {/* Modal para nueva postulación */}
      <Modal visible={modalVisible} animationType="slide" transparent>
        <View style={styles.modalOverlay}>
          <View style={styles.modalContent}>
            <View style={styles.modalHeader}>
              <Text style={styles.modalTitle}>Nueva Postulación</Text>
              <TouchableOpacity onPress={() => setModalVisible(false)}>
                <Ionicons name="close" size={24} color={COLORS.gray} />
              </TouchableOpacity>
            </View>
            
            <ScrollView style={styles.modalBody}>
              <View style={styles.formGroup}>
                <Text style={styles.label}>Empresa *</Text>
                <TextInput
                  style={styles.input}
                  value={newApplication.empresa}
                  onChangeText={(text) => setNewApplication({...newApplication, empresa: text})}
                  placeholder="Nombre de la empresa"
                />
              </View>
              
              <View style={styles.formGroup}>
                <Text style={styles.label}>Cargo *</Text>
                <TextInput
                  style={styles.input}
                  value={newApplication.cargo}
                  onChangeText={(text) => setNewApplication({...newApplication, cargo: text})}
                  placeholder="Cargo a desempeñar"
                />
              </View>
              
              <View style={styles.formGroup}>
                <Text style={styles.label}>Supervisor</Text>
                <TextInput
                  style={styles.input}
                  value={newApplication.supervisor}
                  onChangeText={(text) => setNewApplication({...newApplication, supervisor: text})}
                  placeholder="Nombre del supervisor"
                />
              </View>
              
              <View style={styles.formGroup}>
                <Text style={styles.label}>Descripción</Text>
                <TextInput
                  style={[styles.input, styles.textArea]}
                  value={newApplication.descripcion}
                  onChangeText={(text) => setNewApplication({...newApplication, descripcion: text})}
                  placeholder="Descripción de las actividades"
                  multiline
                  numberOfLines={4}
                />
              </View>
              
              <TouchableOpacity style={styles.submitButton} onPress={submitApplication}>
                <Text style={styles.submitButtonText}>Enviar Postulación</Text>
              </TouchableOpacity>
            </ScrollView>
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
    marginTop: SPACING.md,
    fontSize: FONT_SIZE.md,
    color: COLORS.gray,
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: COLORS.primary,
    paddingHorizontal: SPACING.md,
    paddingVertical: SPACING.lg,
    paddingTop: SPACING.xl,
  },
  backButton: {
    marginRight: SPACING.md,
  },
  headerTitle: {
    fontSize: FONT_SIZE.xl,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.white,
    flex: 1,
  },
  tabsContainer: {
    flexDirection: 'row',
    backgroundColor: COLORS.white,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  tab: {
    flex: 1,
    paddingVertical: SPACING.md,
    alignItems: 'center',
    borderBottomWidth: 2,
    borderBottomColor: 'transparent',
  },
  activeTab: {
    borderBottomColor: COLORS.primary,
  },
  tabText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.gray,
    fontWeight: FONT_WEIGHT.medium,
  },
  activeTabText: {
    color: COLORS.primary,
    fontWeight: FONT_WEIGHT.bold,
  },
  content: {
    flex: 1,
    padding: SPACING.md,
  },
  tabContent: {
    flex: 1,
  },
  statusCard: {
    backgroundColor: COLORS.white,
    borderRadius: 12,
    padding: SPACING.lg,
    marginBottom: SPACING.md,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  statusHeader: {
    alignItems: 'center',
    marginBottom: SPACING.md,
  },
  statusBadge: {
    paddingHorizontal: SPACING.md,
    paddingVertical: SPACING.sm,
    borderRadius: 20,
  },
  statusBadgeText: {
    color: COLORS.white,
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.bold,
  },
  companyInfo: {
    marginBottom: SPACING.lg,
  },
  companyName: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  position: {
    fontSize: FONT_SIZE.md,
    color: COLORS.primary,
    fontWeight: FONT_WEIGHT.medium,
    marginBottom: SPACING.md,
  },
  detailRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: SPACING.sm,
  },
  detailText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
    marginLeft: SPACING.sm,
  },
  progressSection: {
    marginTop: SPACING.md,
  },
  progressTitle: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.sm,
  },
  progressBar: {
    height: 8,
    backgroundColor: COLORS.lightGray,
    borderRadius: 4,
    overflow: 'hidden',
    marginBottom: SPACING.sm,
  },
  progressFill: {
    height: '100%',
    backgroundColor: COLORS.success,
  },
  progressText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.gray,
    textAlign: 'center',
  },
  evaluationsCard: {
    backgroundColor: COLORS.white,
    borderRadius: 12,
    padding: SPACING.lg,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  cardTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.md,
  },
  evaluationItem: {
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
    paddingBottom: SPACING.md,
    marginBottom: SPACING.md,
  },
  evaluationHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: SPACING.xs,
  },
  evaluationPeriod: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
  },
  evaluationGrade: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.success,
  },
  evaluationDate: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.gray,
    marginBottom: SPACING.xs,
  },
  evaluationComments: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
  },
  sectionTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.md,
  },
  offerCard: {
    backgroundColor: COLORS.white,
    borderRadius: 12,
    padding: SPACING.lg,
    marginBottom: SPACING.md,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  offerHeader: {
    marginBottom: SPACING.md,
  },
  offerCompany: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  offerPosition: {
    fontSize: FONT_SIZE.md,
    color: COLORS.primary,
    fontWeight: FONT_WEIGHT.medium,
  },
  offerDetails: {
    marginBottom: SPACING.md,
  },
  applyButton: {
    backgroundColor: COLORS.primary,
    paddingVertical: SPACING.sm,
    paddingHorizontal: SPACING.lg,
    borderRadius: 8,
    alignItems: 'center',
  },
  applyButtonText: {
    color: COLORS.white,
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
  },
  addButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: COLORS.success,
    paddingVertical: SPACING.md,
    borderRadius: 12,
    marginTop: SPACING.md,
  },
  addButtonText: {
    color: COLORS.white,
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    marginLeft: SPACING.sm,
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
  formGroup: {
    marginBottom: SPACING.md,
  },
  label: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.text,
    marginBottom: SPACING.sm,
  },
  input: {
    borderWidth: 1,
    borderColor: COLORS.lightGray,
    borderRadius: 8,
    paddingHorizontal: SPACING.md,
    paddingVertical: SPACING.sm,
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
  },
  textArea: {
    height: 100,
    textAlignVertical: 'top',
  },
  submitButton: {
    backgroundColor: COLORS.primary,
    paddingVertical: SPACING.md,
    borderRadius: 8,
    alignItems: 'center',
    marginTop: SPACING.md,
  },
  submitButtonText: {
    color: COLORS.white,
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
  },
});

export default SocialServiceScreen;