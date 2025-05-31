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

const AcademicTranscriptScreen = ({ route, navigation }) => {
  const { title } = route.params;
  const [selectedType, setSelectedType] = useState('regular');
  const [loading, setLoading] = useState(false);

  const transcriptTypes = [
    {
      id: 'regular',
      title: 'Constancia Regular',
      description: 'Constancia de estudios básica',
      cost: 50,
      deliveryTime: '1-2 días hábiles',
      uses: ['Trámites bancarios', 'Verificación de estudios', 'Becas externas']
    },
    {
      id: 'official',
      title: 'Constancia Oficial',
      description: 'Con sello y firma oficial',
      cost: 75,
      deliveryTime: '3-5 días hábiles',
      uses: ['Trámites gubernamentales', 'Embajadas', 'Instituciones oficiales']
    },
    {
      id: 'english',
      title: 'Constancia en Inglés',
      description: 'Traducción oficial al inglés',
      cost: 150,
      deliveryTime: '5-7 días hábiles',
      uses: ['Estudios en el extranjero', 'Visa de estudiante', 'Universidades internacionales']
    }
  ];

  const studentInfo = {
    name: 'Juan Pérez García',
    id: '20210001',
    program: 'Ingeniería en Sistemas Computacionales',
    currentSemester: 7,
    status: 'Estudiante Regular',
    enrollmentDate: 'Agosto 2021',
    gpa: 8.75
  };

  const handleRequest = () => {
    const selected = transcriptTypes.find(t => t.id === selectedType);
    
    Alert.alert(
      'Confirmar Solicitud',
      `¿Confirmas la solicitud de ${selected.title}?\n\nCosto: $${selected.cost} MXN\nTiempo de entrega: ${selected.deliveryTime}`,
      [
        { text: 'Cancelar', style: 'cancel' },
        { 
          text: 'Confirmar', 
          onPress: () => processRequest(selected)
        }
      ]
    );
  };

  const processRequest = (transcriptType) => {
    setLoading(true);
    
    // Simular procesamiento
    setTimeout(() => {
      setLoading(false);
      Alert.alert(
        'Solicitud Creada',
        `Tu solicitud de ${transcriptType.title} ha sido creada exitosamente.\n\nFolio: CON-2024-${Math.floor(Math.random() * 1000)}\n\nRecibirás una notificación cuando esté lista para recoger.`,
        [{ text: 'OK', onPress: () => navigation.goBack() }]
      );
    }, 2000);
  };

  const renderTypeSelector = () => (
    <Card style={styles.selectorCard}>
      <Text style={styles.cardTitle}>Tipo de Constancia</Text>
      
      {transcriptTypes.map((type) => (
        <TouchableOpacity
          key={type.id}
          style={[
            styles.typeOption,
            selectedType === type.id && styles.selectedType
          ]}
          onPress={() => setSelectedType(type.id)}
        >
          <View style={styles.radioContainer}>
            <View style={[
              styles.radio,
              selectedType === type.id && styles.radioSelected
            ]}>
              {selectedType === type.id && <View style={styles.radioDot} />}
            </View>
          </View>
          
          <View style={styles.typeInfo}>
            <Text style={styles.typeTitle}>{type.title}</Text>
            <Text style={styles.typeDescription}>{type.description}</Text>
            
            <View style={styles.typeDetails}>
              <View style={styles.detailItem}>
                <Ionicons name="cash-outline" size={16} color={COLORS.success} />
                <Text style={styles.detailText}>${type.cost} MXN</Text>
              </View>
              
              <View style={styles.detailItem}>
                <Ionicons name="time-outline" size={16} color={COLORS.primary} />
                <Text style={styles.detailText}>{type.deliveryTime}</Text>
              </View>
            </View>
            
            <View style={styles.usesContainer}>
              <Text style={styles.usesTitle}>Usos comunes:</Text>
              {type.uses.map((use, index) => (
                <Text key={index} style={styles.useItem}>• {use}</Text>
              ))}
            </View>
          </View>
        </TouchableOpacity>
      ))}
    </Card>
  );

  const renderStudentInfo = () => (
    <Card style={styles.infoCard}>
      <Text style={styles.cardTitle}>Información del Estudiante</Text>
      
      <View style={styles.infoGrid}>
        <View style={styles.infoRow}>
          <Text style={styles.infoLabel}>Nombre:</Text>
          <Text style={styles.infoValue}>{studentInfo.name}</Text>
        </View>
        
        <View style={styles.infoRow}>
          <Text style={styles.infoLabel}>Matrícula:</Text>
          <Text style={styles.infoValue}>{studentInfo.id}</Text>
        </View>
        
        <View style={styles.infoRow}>
          <Text style={styles.infoLabel}>Programa:</Text>
          <Text style={styles.infoValue}>{studentInfo.program}</Text>
        </View>
        
        <View style={styles.infoRow}>
          <Text style={styles.infoLabel}>Semestre:</Text>
          <Text style={styles.infoValue}>{studentInfo.currentSemester}°</Text>
        </View>
        
        <View style={styles.infoRow}>
          <Text style={styles.infoLabel}>Estado:</Text>
          <Text style={[styles.infoValue, { color: COLORS.success }]}>
            {studentInfo.status}
          </Text>
        </View>
        
        <View style={styles.infoRow}>
          <Text style={styles.infoLabel}>Promedio:</Text>
          <Text style={[styles.infoValue, { color: COLORS.primary }]}>
            {studentInfo.gpa}
          </Text>
        </View>
      </View>
    </Card>
  );

  const renderRequirements = () => (
    <Card style={styles.requirementsCard}>
      <Text style={styles.cardTitle}>Requisitos</Text>
      
      <View style={styles.requirementsList}>
        <View style={styles.requirementItem}>
          <Ionicons name="checkmark-circle" size={20} color={COLORS.success} />
          <Text style={styles.requirementText}>Estar al corriente en pagos</Text>
        </View>
        
        <View style={styles.requirementItem}>
          <Ionicons name="checkmark-circle" size={20} color={COLORS.success} />
          <Text style={styles.requirementText}>No tener materias reprobadas pendientes</Text>
        </View>
        
        <View style={styles.requirementItem}>
          <Ionicons name="checkmark-circle" size={20} color={COLORS.success} />
          <Text style={styles.requirementText}>Realizar el pago correspondiente</Text>
        </View>
      </View>
    </Card>
  );

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
        {renderStudentInfo()}
        {renderTypeSelector()}
        {renderRequirements()}
      </ScrollView>

      {/* Actions */}
      <View style={styles.actionsContainer}>
        <Button
          title={loading ? "Procesando..." : "Solicitar Constancia"}
          onPress={handleRequest}
          disabled={loading}
          style={styles.requestButton}
          icon="document-text-outline"
        />
      </View>
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
    paddingVertical: SPACING.lg,
    paddingTop: SPACING.xl,
  },
  backButton: {
    padding: SPACING.sm,
  },
  headerTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.white,
    flex: 1,
    textAlign: 'center',
  },
  placeholder: {
    width: 40,
  },
  content: {
    flex: 1,
    padding: SPACING.md,
  },
  cardTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.md,
  },
  infoCard: {
    marginBottom: SPACING.lg,
  },
  infoGrid: {
    gap: SPACING.sm,
  },
  infoRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  infoLabel: {
    fontSize: FONT_SIZE.md,
    color: COLORS.muted,
    fontWeight: FONT_WEIGHT.medium,
  },
  infoValue: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    fontWeight: FONT_WEIGHT.medium,
  },
  selectorCard: {
    marginBottom: SPACING.lg,
  },
  typeOption: {
    flexDirection: 'row',
    padding: SPACING.md,
    borderWidth: 1,
    borderColor: COLORS.lightGray,
    borderRadius: 12,
    marginBottom: SPACING.md,
  },
  selectedType: {
    borderColor: COLORS.primary,
    backgroundColor: COLORS.primary + '10',
  },
  radioContainer: {
    marginRight: SPACING.md,
    justifyContent: 'flex-start',
    paddingTop: SPACING.xs,
  },
  radio: {
    width: 20,
    height: 20,
    borderRadius: 10,
    borderWidth: 2,
    borderColor: COLORS.lightGray,
    justifyContent: 'center',
    alignItems: 'center',
  },
  radioSelected: {
    borderColor: COLORS.primary,
  },
  radioDot: {
    width: 10,
    height: 10,
    borderRadius: 5,
    backgroundColor: COLORS.primary,
  },
  typeInfo: {
    flex: 1,
  },
  typeTitle: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  typeDescription: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
    marginBottom: SPACING.sm,
  },
  typeDetails: {
    flexDirection: 'row',
    marginBottom: SPACING.sm,
  },
  detailItem: {
    flexDirection: 'row',
    alignItems: 'center',
    marginRight: SPACING.lg,
  },
  detailText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
    marginLeft: SPACING.xs,
    fontWeight: FONT_WEIGHT.medium,
  },
  usesContainer: {
    marginTop: SPACING.sm,
  },
  usesTitle: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  useItem: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
    marginLeft: SPACING.sm,
  },
  requirementsCard: {
    marginBottom: SPACING.xl,
  },
  requirementsList: {
    gap: SPACING.sm,
  },
  requirementItem: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  requirementText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    marginLeft: SPACING.sm,
    flex: 1,
  },
  actionsContainer: {
    padding: SPACING.md,
    backgroundColor: COLORS.white,
    borderTopWidth: 1,
    borderTopColor: COLORS.lightGray,
  },
  requestButton: {
    width: '100%',
  },
});

export default AcademicTranscriptScreen;
