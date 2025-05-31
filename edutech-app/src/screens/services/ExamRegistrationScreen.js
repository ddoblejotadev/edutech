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

const ExamRegistrationScreen = ({ navigation, route }) => {
  const { title } = route.params || { title: 'Inscripción de Ramos' };
  const [selectedExams, setSelectedExams] = useState([]);
  const [loading, setLoading] = useState(false);

  const availableExams = [
    {
      id: 1,
      subject: 'Cálculo Diferencial',
      code: 'MAT-101',
      date: '2024-06-15',
      time: '09:00 AM',
      location: 'Aula 201',
      cost: 200,
      deadline: '2024-06-08',
      attempts: 1,
      maxAttempts: 3,
      status: 'available'
    },
    {
      id: 2,
      subject: 'Física I',
      code: 'FIS-101',
      date: '2024-06-17',
      time: '11:00 AM',
      location: 'Lab. Física',
      cost: 200,
      deadline: '2024-06-10',
      attempts: 2,
      maxAttempts: 3,
      status: 'available'
    },
    {
      id: 3,
      subject: 'Programación Básica',
      code: 'INF-101',
      date: '2024-06-20',
      time: '14:00 PM',
      location: 'Lab. Cómputo',
      cost: 250,
      deadline: '2024-06-13',
      attempts: 3,
      maxAttempts: 3,
      status: 'unavailable'
    },
    {
      id: 4,
      subject: 'Inglés I',
      code: 'ING-101',
      date: '2024-06-22',
      time: '10:00 AM',
      location: 'Aula 105',
      cost: 180,
      deadline: '2024-06-15',
      attempts: 0,
      maxAttempts: 3,
      status: 'available'
    }
  ];

  const handleExamToggle = (examId) => {
    const exam = availableExams.find(e => e.id === examId);
    
    if (exam.status === 'unavailable') {
      Alert.alert(
        'Examen no disponible',
        'Has agotado el número máximo de intentos para este examen.',
        [{ text: 'OK' }]
      );
      return;
    }

    setSelectedExams(prev => {
      if (prev.includes(examId)) {
        return prev.filter(id => id !== examId);
      } else {
        return [...prev, examId];
      }
    });
  };

  const calculateTotal = () => {
    return selectedExams.reduce((total, examId) => {
      const exam = availableExams.find(e => e.id === examId);
      return total + (exam?.cost || 0);
    }, 0);
  };

  const handleRegistration = () => {
    if (selectedExams.length === 0) {
      Alert.alert('Error', 'Selecciona al menos un examen para continuar.');
      return;
    }

    const selectedExamsList = availableExams.filter(exam => 
      selectedExams.includes(exam.id)
    );

    Alert.alert(
      'Confirmar Inscripción',
      `¿Confirmas la inscripción a ${selectedExams.length} examen(es)?\n\nCosto total: $${calculateTotal()} MXN`,
      [
        { text: 'Cancelar', style: 'cancel' },
        { 
          text: 'Confirmar', 
          onPress: () => processRegistration(selectedExamsList)
        }
      ]
    );
  };

  const processRegistration = (exams) => {
    setLoading(true);
    
    setTimeout(() => {
      setLoading(false);
      const folio = `EX-2024-${Math.floor(Math.random() * 10000)}`;
      
      Alert.alert(
        'Inscripción Exitosa',
        `Te has inscrito exitosamente a ${exams.length} examen(es).\n\nFolio: ${folio}\n\nRecibirás un email con los detalles y ubicaciones.`,
        [{ text: 'OK', onPress: () => navigation.goBack() }]
      );
    }, 2000);
  };

  const getStatusColor = (status, attempts, maxAttempts) => {
    if (status === 'unavailable' || attempts >= maxAttempts) return COLORS.error;
    return COLORS.success;
  };

  const getStatusText = (status, attempts, maxAttempts) => {
    if (status === 'unavailable' || attempts >= maxAttempts) {
      return 'No disponible';
    }
    return `Disponible (${attempts}/${maxAttempts} intentos)`;
  };

  const renderExamCard = (exam) => {
    const isSelected = selectedExams.includes(exam.id);
    const isUnavailable = exam.status === 'unavailable' || exam.attempts >= exam.maxAttempts;

    return (
      <TouchableOpacity
        key={exam.id}
        style={[
          styles.examCard,
          isSelected && styles.selectedExam,
          isUnavailable && styles.unavailableExam
        ]}
        onPress={() => handleExamToggle(exam.id)}
        disabled={isUnavailable}
      >
        <View style={styles.examHeader}>
          <View style={styles.examInfo}>
            <Text style={styles.subjectName}>{exam.subject}</Text>
            <Text style={styles.subjectCode}>{exam.code}</Text>
          </View>
          
          <View style={styles.checkboxContainer}>
            <View style={[
              styles.checkbox,
              isSelected && styles.checkboxSelected,
              isUnavailable && styles.checkboxDisabled
            ]}>
              {isSelected && (
                <Ionicons name="checkmark" size={16} color={COLORS.white} />
              )}
            </View>
          </View>
        </View>

        <View style={styles.examDetails}>
          <View style={styles.detailRow}>
            <Ionicons name="calendar-outline" size={16} color={COLORS.muted} />
            <Text style={styles.detailText}>{exam.date} - {exam.time}</Text>
          </View>
          
          <View style={styles.detailRow}>
            <Ionicons name="location-outline" size={16} color={COLORS.muted} />
            <Text style={styles.detailText}>{exam.location}</Text>
          </View>
          
          <View style={styles.detailRow}>
            <Ionicons name="cash-outline" size={16} color={COLORS.muted} />
            <Text style={styles.detailText}>${exam.cost} MXN</Text>
          </View>
          
          <View style={styles.detailRow}>
            <Ionicons name="time-outline" size={16} color={COLORS.muted} />
            <Text style={styles.detailText}>Límite: {exam.deadline}</Text>
          </View>
        </View>

        <View style={styles.statusContainer}>
          <Text style={[
            styles.statusText,
            { color: getStatusColor(exam.status, exam.attempts, exam.maxAttempts) }
          ]}>
            {getStatusText(exam.status, exam.attempts, exam.maxAttempts)}
          </Text>
        </View>
      </TouchableOpacity>
    );
  };

  const renderSummary = () => {
    if (selectedExams.length === 0) return null;

    return (
      <Card style={styles.summaryCard}>
        <Text style={styles.summaryTitle}>Resumen de Inscripción</Text>
        
        <View style={styles.summaryContent}>
          <Text style={styles.summaryText}>
            Exámenes seleccionados: {selectedExams.length}
          </Text>
          <Text style={styles.totalCost}>
            Total a pagar: ${calculateTotal()} MXN
          </Text>
        </View>
        
        <Text style={styles.paymentNote}>
          * El pago debe realizarse antes de la fecha límite de cada examen
        </Text>
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
        <Card style={styles.infoCard}>
          <Text style={styles.infoTitle}>Exámenes Extraordinarios</Text>
          <Text style={styles.infoText}>
            Selecciona los exámenes a los que deseas inscribirte. Recuerda que cada materia tiene un límite de 3 intentos.
          </Text>
        </Card>

        <View style={styles.examsSection}>
          <Text style={styles.sectionTitle}>Exámenes Disponibles</Text>
          {availableExams.map(renderExamCard)}
        </View>

        {renderSummary()}
      </ScrollView>

      {/* Actions */}
      <View style={styles.actionsContainer}>
        <Button
          title={loading ? "Procesando..." : "Inscribirse a Exámenes"}
          onPress={handleRegistration}
          disabled={loading || selectedExams.length === 0}
          style={styles.registerButton}
          icon="create-outline"
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
  infoCard: {
    marginBottom: SPACING.lg,
  },
  infoTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.sm,
  },
  infoText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.muted,
    lineHeight: 20,
  },
  sectionTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.md,
  },
  examsSection: {
    marginBottom: SPACING.lg,
  },
  examCard: {
    backgroundColor: COLORS.white,
    borderRadius: 12,
    padding: SPACING.md,
    marginBottom: SPACING.md,
    borderWidth: 1,
    borderColor: COLORS.lightGray,
  },
  selectedExam: {
    borderColor: COLORS.primary,
    backgroundColor: COLORS.primary + '10',
  },
  unavailableExam: {
    opacity: 0.6,
    backgroundColor: COLORS.lightGray + '50',
  },
  examHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: SPACING.md,
  },
  examInfo: {
    flex: 1,
  },
  subjectName: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  subjectCode: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.primary,
    fontWeight: FONT_WEIGHT.medium,
  },
  checkboxContainer: {
    padding: SPACING.xs,
  },
  checkbox: {
    width: 24,
    height: 24,
    borderRadius: 4,
    borderWidth: 2,
    borderColor: COLORS.lightGray,
    justifyContent: 'center',
    alignItems: 'center',
  },
  checkboxSelected: {
    backgroundColor: COLORS.primary,
    borderColor: COLORS.primary,
  },
  checkboxDisabled: {
    borderColor: COLORS.muted,
    backgroundColor: COLORS.lightGray,
  },
  examDetails: {
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
  statusContainer: {
    borderTopWidth: 1,
    borderTopColor: COLORS.lightGray,
    paddingTop: SPACING.sm,
  },
  statusText: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.medium,
    textAlign: 'center',
  },
  summaryCard: {
    marginBottom: SPACING.xl,
  },
  summaryTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.md,
  },
  summaryContent: {
    marginBottom: SPACING.md,
  },
  summaryText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    marginBottom: SPACING.sm,
  },
  totalCost: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.primary,
  },
  paymentNote: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
    fontStyle: 'italic',
  },
  actionsContainer: {
    padding: SPACING.md,
    backgroundColor: COLORS.white,
    borderTopWidth: 1,
    borderTopColor: COLORS.lightGray,
  },
  registerButton: {
    width: '100%',
  },
  infoBox: {
    backgroundColor: COLORS.background,
    padding: SPACING.md,
    borderRadius: 8,
    marginTop: SPACING.md,
  },
  title: {
    fontSize: FONT_SIZE.xl,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginTop: SPACING.md,
    marginBottom: SPACING.sm,
  },
  subtitle: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    textAlign: 'center',
    marginBottom: SPACING.md,
  },
});

export default ExamRegistrationScreen;
