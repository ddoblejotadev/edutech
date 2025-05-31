import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  SafeAreaView,
  TouchableOpacity,
  Alert
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT, FONTS } from '../../config/theme';
import { Button, Card } from '../../components/common/UIComponents';

const ScholarshipsScreen = ({ route, navigation }) => {
  const { title } = route.params || { title: 'Becas y Ayudas Estudiantiles' };
  const [selectedCategory, setSelectedCategory] = useState('all');

  // Datos de becas DUOC UC actualizados con formatos correctos
  const scholarships = [
    {
      id: 1,
      name: 'Beca de Excelencia Académica DUOC UC',
      description: 'Para estudiantes con promedio ponderado superior a 6.0',
      amount: 2225000,
      percentage: 50,
      deadline: '30 de Julio 2025',
      requirements: ['Promedio mínimo 6.0', 'Sin reprobaciones', 'Situación socioeconómica acreditada'],
      status: 'available',
      type: 'academic'
    },
    {
      id: 2,
      name: 'Beca Hijo de Profesional de la Educación',
      description: 'Descuento para hijos de profesores y educadores',
      amount: 1112500,
      percentage: 25,
      deadline: '15 de Agosto 2025',
      requirements: ['Padre/madre trabajando en educación', 'Certificado laboral vigente'],
      status: 'available',
      type: 'economic'
    },
    {
      id: 3,
      name: 'Beca CAE - Crédito con Aval del Estado',
      description: 'Crédito estatal para financiar estudios superiores',
      amount: 2800000,
      percentage: 70,
      deadline: '10 de Septiembre 2025',
      requirements: ['Situación socioeconómica vulnerable', 'Puntaje PSU mínimo'],
      status: 'applied',
      type: 'economic'
    },
    {
      id: 4,
      name: 'Beca Deportiva DUOC UC',
      description: 'Para estudiantes destacados en deportes',
      amount: 1335000,
      percentage: 30,
      deadline: '20 de Junio 2025',
      requirements: ['Seleccionado deportivo nacional/regional', 'Mantener rendimiento deportivo'],
      status: 'available',
      type: 'sports'
    }
  ];

  const categories = [
    { id: 'all', name: 'Todas', icon: 'apps-outline' },
    { id: 'academic', name: 'Académicas', icon: 'school-outline' },
    { id: 'economic', name: 'Socioeconómicas', icon: 'cash-outline' },
    { id: 'sports', name: 'Deportivas', icon: 'fitness-outline' },
    { id: 'research', name: 'Investigación', icon: 'flask-outline' },
    { id: 'cultural', name: 'Culturales', icon: 'color-palette-outline' }
  ];

  const filteredScholarships = selectedCategory === 'all' 
    ? scholarships 
    : scholarships.filter(s => s.type === selectedCategory);

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('es-CL', {
      style: 'currency',
      currency: 'CLP',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(amount);
  };

  const getStatusInfo = (status) => {
    switch (status) {
      case 'available':
        return { color: COLORS.success, text: 'Disponible', icon: 'checkmark-circle' };
      case 'applied':
        return { color: COLORS.warning, text: 'Postulaste', icon: 'time' };
      case 'expired':
        return { color: COLORS.error, text: 'Expirada', icon: 'close-circle' };
      default:
        return { color: COLORS.muted, text: 'Información', icon: 'information-circle' };
    }
  };

  const handleApply = (scholarship) => {
    Alert.alert(
      'Postular a Beca',
      `¿Deseas postular a la ${scholarship.name}?\n\nMonto: ${formatCurrency(scholarship.amount)} (${scholarship.percentage}% del arancel anual)\n\nDeberás completar los requisitos solicitados.`,
      [
        { text: 'Cancelar', style: 'cancel' },
        { 
          text: 'Postular', 
          onPress: () => {
            Alert.alert(
              'Postulación Enviada', 
              'Tu postulación a la beca ha sido enviada exitosamente. Recibirás una notificación con el resultado del proceso.',
              [{ text: 'OK' }]
            );
          }
        }
      ]
    );
  };

  const renderCategoryFilter = () => (
    <View style={styles.categoryContainer}>
      <ScrollView horizontal showsHorizontalScrollIndicator={false}>
        {categories.map((category) => (
          <TouchableOpacity
            key={category.id}
            style={[
              styles.categoryButton,
              selectedCategory === category.id && styles.categoryButtonActive
            ]}
            onPress={() => setSelectedCategory(category.id)}
          >
            <Ionicons 
              name={category.icon} 
              size={16} 
              color={selectedCategory === category.id ? COLORS.white : COLORS.primary} 
            />
            <Text style={[
              styles.categoryButtonText,
              selectedCategory === category.id && styles.categoryButtonTextActive
            ]}>
              {category.name}
            </Text>
          </TouchableOpacity>
        ))}
      </ScrollView>
    </View>
  );

  const renderScholarshipCard = (scholarship) => {
    const statusInfo = getStatusInfo(scholarship.status);

    return (
      <Card key={scholarship.id} style={styles.scholarshipCard}>
        <View style={styles.scholarshipHeader}>
          <View style={styles.scholarshipInfo}>
            <Text style={styles.scholarshipName}>{scholarship.name}</Text>
            <Text style={styles.scholarshipDescription}>{scholarship.description}</Text>
          </View>
          
          <View style={[styles.statusBadge, { backgroundColor: statusInfo.color + '20' }]}>
            <Ionicons name={statusInfo.icon} size={16} color={statusInfo.color} />
            <Text style={[styles.statusText, { color: statusInfo.color }]}>
              {statusInfo.text}
            </Text>
          </View>
        </View>

        <View style={styles.scholarshipDetails}>
          <View style={styles.amountContainer}>
            <Text style={styles.amountLabel}>Monto del apoyo:</Text>
            <Text style={styles.amountValue}>{formatCurrency(scholarship.amount)}</Text>
            <Text style={styles.percentageValue}>({scholarship.percentage}% del arancel anual)</Text>
          </View>

          <View style={styles.deadlineContainer}>
            <Ionicons name="calendar-outline" size={16} color={COLORS.muted} />
            <Text style={styles.deadlineText}>Fecha límite: {scholarship.deadline}</Text>
          </View>
        </View>

        <View style={styles.requirementsContainer}>
          <Text style={styles.requirementsTitle}>Requisitos:</Text>
          {scholarship.requirements.map((req, index) => (
            <View key={index} style={styles.requirementItem}>
              <Text style={styles.requirementBullet}>•</Text>
              <Text style={styles.requirementText}>{req}</Text>
            </View>
          ))}
        </View>

        <View style={styles.cardActions}>
          <Button
            title={scholarship.status === 'applied' ? 'Ver Estado' : 'Postular'}
            onPress={() => handleApply(scholarship)}
            variant={scholarship.status === 'available' ? 'primary' : 'outlined'}
            disabled={scholarship.status === 'expired'}
            style={styles.applyButton}
            icon={scholarship.status === 'applied' ? 'eye-outline' : 'send-outline'}
          />
        </View>
      </Card>
    );
  };

  const renderMyApplications = () => {
    const myApplications = scholarships.filter(s => s.status === 'applied');
    
    if (myApplications.length === 0) return null;

    return (
      <Card style={styles.applicationsCard}>
        <Text style={styles.applicationsTitle}>Mis Postulaciones</Text>
        
        {myApplications.map((app) => (
          <View key={app.id} style={styles.applicationItem}>
            <View style={styles.applicationInfo}>
              <Text style={styles.applicationName}>{app.name}</Text>
              <Text style={styles.applicationStatus}>En evaluación</Text>
            </View>
            <TouchableOpacity style={styles.viewButton}>
              <Ionicons name="eye-outline" size={20} color={COLORS.primary} />
            </TouchableOpacity>
          </View>
        ))}
      </Card>
    );
  };

  return (
    <SafeAreaView style={styles.container}>
      {/* Header corregido con tamaño proporcionado */}
      <View style={styles.header}>
        <TouchableOpacity 
          style={styles.backButton}
          onPress={() => navigation.goBack()}
        >
          <Ionicons name="arrow-back" size={24} color={COLORS.white} />
        </TouchableOpacity>
        <View style={styles.headerTitleContainer}>
          <Text style={styles.headerTitle}>Becas y Ayudas Estudiantiles</Text>
          <Text style={styles.headerSubtitle}>DUOC UC</Text>
        </View>
        <View style={styles.placeholder} />
      </View>

      <ScrollView style={styles.content}>
        {renderMyApplications()}
        {renderCategoryFilter()}

        <View style={styles.scholarshipsSection}>
          <Text style={styles.sectionTitle}>
            Becas Disponibles ({filteredScholarships.length})
          </Text>
          {filteredScholarships.map(renderScholarshipCard)}
        </View>

        <View style={styles.infoCard}>
          <View style={styles.infoHeader}>
            <Ionicons name="information-circle" size={24} color={COLORS.primary} />
            <Text style={styles.infoTitle}>Información Importante</Text>
          </View>
          <Text style={styles.infoText}>
            • Las postulaciones deben realizarse dentro de los plazos establecidos{'\n'}
            • Todos los documentos deben estar vigentes y legalizados{'\n'}
            • El proceso de evaluación puede tomar entre 15 a 30 días hábiles{'\n'}
            • Para más información, contacta a Bienestar Estudiantil DUOC UC
          </Text>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  header: {
    backgroundColor: COLORS.primary,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: SPACING.md,
    paddingVertical: SPACING.md,
    paddingTop: SPACING.lg,
  },
  backButton: {
    padding: SPACING.sm,
  },
  headerTitleContainer: {
    flex: 1,
    alignItems: 'center',
  },
  headerTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.white,
    textAlign: 'center',
  },
  headerSubtitle: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.white,
    opacity: 0.9,
    textAlign: 'center',
    marginTop: 2,
  },
  placeholder: {
    width: 40,
  },
  content: {
    flex: 1,
    padding: SPACING.md,
  },
  applicationsCard: {
    marginBottom: SPACING.lg,
  },
  applicationsTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.md,
  },
  applicationItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: SPACING.sm,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.border,
  },
  applicationInfo: {
    flex: 1,
  },
  applicationName: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    fontWeight: FONT_WEIGHT.medium,
  },
  applicationStatus: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.warning,
    marginTop: SPACING.xs,
  },
  viewButton: {
    padding: SPACING.sm,
  },
  categoryContainer: {
    marginBottom: SPACING.lg,
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
    fontSize: FONT_SIZE.sm,
    color: COLORS.primary,
    marginLeft: SPACING.xs,
    fontWeight: FONT_WEIGHT.medium,
  },
  categoryButtonTextActive: {
    color: COLORS.white,
  },
  scholarshipsSection: {
    marginBottom: SPACING.xl,
  },
  sectionTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.md,
  },
  scholarshipCard: {
    backgroundColor: COLORS.white,
    borderRadius: 12,
    padding: SPACING.md,
    marginBottom: SPACING.md,
    elevation: 3,
    shadowColor: COLORS.shadow,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
  },
  scholarshipHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    marginBottom: SPACING.sm,
  },
  scholarshipInfo: {
    flex: 1,
    marginRight: SPACING.md,
  },
  scholarshipName: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  scholarshipDescription: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
    lineHeight: 20,
  },
  statusBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: SPACING.sm,
    paddingVertical: SPACING.xs,
    borderRadius: 12,
  },
  statusText: {
    fontSize: FONT_SIZE.xs,
    fontWeight: FONT_WEIGHT.medium,
    marginLeft: SPACING.xs,
  },
  scholarshipDetails: {
    marginVertical: SPACING.md,
  },
  amountContainer: {
    marginBottom: SPACING.sm,
  },
  amountLabel: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
    marginBottom: SPACING.xs,
  },
  amountValue: {
    fontSize: FONT_SIZE.lg,
    color: COLORS.success,
    fontWeight: FONT_WEIGHT.bold,
  },
  percentageValue: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
    marginTop: SPACING.xs,
  },
  deadlineContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: SPACING.sm,
  },
  deadlineText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
    marginLeft: SPACING.sm,
  },
  requirementsContainer: {
    backgroundColor: COLORS.background,
    padding: SPACING.sm,
    borderRadius: 8,
    marginBottom: SPACING.md,
  },
  requirementsTitle: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  requirementItem: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    marginBottom: SPACING.xs,
  },
  requirementBullet: {
    fontSize: FONT_SIZE.md,
    color: COLORS.primary,
    marginRight: SPACING.sm,
    marginTop: 2,
  },
  requirementText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
    flex: 1,
    lineHeight: 18,
  },
  cardActions: {
    borderTopWidth: 1,
    borderTopColor: COLORS.border,
    paddingTop: SPACING.md,
  },
  applyButton: {
    width: '100%',
  },
  infoCard: {
    backgroundColor: '#e3f2fd',
    borderRadius: 12,
    padding: SPACING.md,
    marginTop: SPACING.md,
  },
  infoHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: SPACING.sm,
  },
  infoTitle: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.primary,
    marginLeft: SPACING.sm,
  },
  infoText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
    lineHeight: 20,
  },
});

export default ScholarshipsScreen;
