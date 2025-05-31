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
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT } from '../../config/theme';
import { Button, Card } from '../../components/common/UIComponents';

const ScholarshipsScreen = ({ route, navigation }) => {
  const { title } = route.params || { title: 'Becas y Ayudas Estudiantiles' };
  const [selectedCategory, setSelectedCategory] = useState('all');

  const scholarships = [
    {
      id: 1,
      name: 'Beca de Excelencia Académica',
      description: 'Para estudiantes con promedio superior a 6.0',
      amount: 'Hasta $500.000',
      deadline: '30 de Julio 2024',
      requirements: ['PPA mínimo 6.0', 'Situación socioeconómica acreditada'],
      status: 'Abierta'
    },
    {
      id: 2,
      name: 'Beca JUNAEB',
      description: 'Ayuda estatal para alimentación y mantención',
      amount: 'Variable según tramo',
      deadline: '15 de Agosto 2024',
      requirements: ['Ficha de Protección Social', 'Matrícula vigente'],
      status: 'Abierta'
    },
    {
      id: 3,
      name: 'Beca de Apoyo Socioeconómico',
      description: 'Apoyo financiero para estudiantes vulnerables',
      amount: '$300.000 semestral',
      deadline: '10 de Septiembre 2024',
      requirements: ['Documentación socioeconómica', 'Entrevista social'],
      status: 'Próximamente'
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

  const getStatusInfo = (status) => {
    switch (status) {
      case 'available':
        return { color: COLORS.success, text: 'Disponible', icon: 'checkmark-circle' };
      case 'applied':
        return { color: COLORS.warning, text: 'Aplicaste', icon: 'time' };
      case 'expired':
        return { color: COLORS.error, text: 'Expirada', icon: 'close-circle' };
      default:
        return { color: COLORS.muted, text: 'Desconocido', icon: 'help-circle' };
    }
  };

  const handleApply = (scholarship) => {
    if (scholarship.status === 'applied') {
      Alert.alert(
        'Ya aplicaste',
        'Ya has aplicado a esta beca. Puedes verificar el estado en tu panel de aplicaciones.',
        [{ text: 'OK' }]
      );
      return;
    }

    if (scholarship.status === 'expired') {
      Alert.alert(
        'Beca expirada',
        'Esta beca ya no está disponible. La fecha límite ha pasado.',
        [{ text: 'OK' }]
      );
      return;
    }

    Alert.alert(
      'Aplicar a Beca',
      `¿Deseas aplicar a la ${scholarship.name}?\n\nMonto: $${scholarship.amount} MXN (${scholarship.percentage}% de la colegiatura)\n\nDeberás completar los requisitos solicitados.`,
      [
        { text: 'Cancelar', style: 'cancel' },
        { 
          text: 'Aplicar', 
          onPress: () => processApplication(scholarship)
        }
      ]
    );
  };

  const processApplication = (scholarship) => {
    // Simular proceso de aplicación
    setTimeout(() => {
      Alert.alert(
        'Aplicación Exitosa',
        `Has aplicado exitosamente a la ${scholarship.name}.\n\nFolio: BECA-2024-${Math.floor(Math.random() * 1000)}\n\nRecibirás información sobre los siguientes pasos por email.`,
        [{ text: 'OK' }]
      );
    }, 1000);
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
            <Text style={styles.amountValue}>${scholarship.amount} MXN</Text>
            <Text style={styles.percentageValue}>({scholarship.percentage}% colegiatura)</Text>
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
            title={scholarship.status === 'applied' ? 'Ver Estado' : 'Aplicar'}
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
        <Text style={styles.applicationsTitle}>Mis Aplicaciones</Text>
        
        {myApplications.map((app) => (
          <View key={app.id} style={styles.applicationItem}>
            <View style={styles.applicationInfo}>
              <Text style={styles.applicationName}>{app.name}</Text>
              <Text style={styles.applicationStatus}>En revisión</Text>
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
      {/* Header */}
      <View style={styles.header}>
        <TouchableOpacity 
          style={styles.backButton}
          onPress={() => navigation.goBack()}
        >
          <Ionicons name="arrow-back" size={24} color={COLORS.white} />
        </TouchableOpacity>
        <Text style={styles.headerTitle}>{title}</Text>
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
          <Text style={styles.infoTitle}>Información Importante</Text>
          <Text style={styles.infoText}>
            • Las postulaciones deben realizarse dentro de los plazos establecidos{'\n'}
            • Todos los documentos deben estar vigentes y legalizados{'\n'}
            • El proceso de evaluación puede tomar entre 15 a 30 días hábiles{'\n'}
            • Para más información, contacta a Bienestar Estudiantil
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
    paddingTop: SPACING.sm,
  },
  backButton: {
    marginRight: SPACING.md,
  },
  headerTitle: {
    fontSize: FONT_SIZE.lg,
    color: COLORS.white,
    fontWeight: FONT_WEIGHT.bold,
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
    borderBottomColor: COLORS.lightGray,
  },
  applicationInfo: {
    flex: 1,
  },
  applicationName: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.text,
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
    color: COLORS.primary,
    marginLeft: SPACING.xs,
    fontSize: FONT_SIZE.sm,
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
    flex: 1,
    marginRight: SPACING.sm,
  },
  statusBadge: {
    paddingHorizontal: SPACING.sm,
    paddingVertical: SPACING.xs,
    borderRadius: 12,
  },
  statusText: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.white,
    fontWeight: FONT_WEIGHT.medium,
  },
  scholarshipDescription: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
    marginBottom: SPACING.md,
    lineHeight: 18,
  },
  scholarshipDetails: {
    marginBottom: SPACING.md,
  },
  amountContainer: {
    marginBottom: SPACING.sm,
  },
  amountLabel: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
  },
  amountValue: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.success,
  },
  percentageValue: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
  },
  deadlineContainer: {
    flexDirection: 'row',
    alignItems: 'center',
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
  },
  cardActions: {
    borderTopWidth: 1,
    borderTopColor: COLORS.lightGray,
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
  infoTitle: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.primary,
    marginBottom: SPACING.sm,
  },
  infoText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
    lineHeight: 18,
  },
});

export default ScholarshipsScreen;
