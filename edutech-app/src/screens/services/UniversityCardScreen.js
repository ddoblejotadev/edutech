import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  SafeAreaView,
  TouchableOpacity,
  Alert,
  Image
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT } from '../../config/theme';
import { Button, Card } from '../../components/common/UIComponents';

const UniversityCardScreen = ({ route, navigation }) => {
  const { title } = route.params;
  const [requestStatus, setRequestStatus] = useState('En proceso'); // 'pending', 'in_progress', 'ready', 'delivered'

  const cardInfo = {
    student: {
      name: 'Juan Pérez García',
      id: '20210001',
      program: 'Ingeniería en Sistemas',
      photo: null, // En demo no hay foto
      validUntil: '2025-12-31',
      bloodType: 'O+',
      emergency: 'María García - Tel: 555-1234'
    },
    currentRequest: {
      id: 'REQ-2024-001',
      requestDate: '2024-05-20',
      reason: 'Pérdida de credencial',
      status: 'En proceso',
      estimatedDate: '2024-06-10',
      cost: 150.00,
      paymentStatus: 'Pagado'
    }
  };

  const statusInfo = {
    'Pendiente': {
      color: COLORS.warning,
      icon: 'time-outline',
      message: 'Tu solicitud está pendiente de revisión'
    },
    'En proceso': {
      color: COLORS.primary,
      icon: 'construct-outline',
      message: 'Tu credencial está siendo procesada'
    },
    'Lista': {
      color: COLORS.success,
      icon: 'checkmark-circle-outline',
      message: 'Tu credencial está lista para recoger'
    },
    'Entregada': {
      color: COLORS.muted,
      icon: 'shield-checkmark-outline',
      message: 'Credencial entregada exitosamente'
    }
  };

  const handleNewRequest = () => {
    Alert.alert(
      'Nueva Solicitud',
      '¿Deseas solicitar una nueva credencial universitaria?',
      [
        { text: 'Cancelar', style: 'cancel' },
        { 
          text: 'Solicitar', 
          onPress: () => {
            Alert.alert(
              'Solicitud Creada',
              'Tu solicitud ha sido creada exitosamente. Recibirás una notificación cuando esté lista.',
              [{ text: 'OK' }]
            );
          }
        }
      ]
    );
  };

  const handlePayment = () => {
    Alert.alert(
      'Realizar Pago',
      `El costo de la credencial es $${cardInfo.currentRequest.cost.toLocaleString('es-CL', {
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
      })} CLP`,
      [
        { text: 'Cancelar', style: 'cancel' },
        { 
          text: 'Pagar Ahora', 
          onPress: () => {
            Alert.alert('Pago Exitoso', 'Tu pago ha sido procesado correctamente.');
          }
        }
      ]
    );
  };

  const renderCardPreview = () => (
    <Card style={styles.cardPreview}>
      <Text style={styles.cardTitle}>Vista Previa de la Credencial</Text>
      
      <View style={styles.credentialCard}>
        <View style={styles.cardHeader}>
          <Text style={styles.universityName}>Universidad EduTech</Text>
          <Text style={styles.cardType}>CREDENCIAL ESTUDIANTIL</Text>
        </View>
        
        <View style={styles.cardBody}>
          <View style={styles.photoContainer}>
            {cardInfo.student.photo ? (
              <Image source={{ uri: cardInfo.student.photo }} style={styles.photo} />
            ) : (
              <View style={styles.photoPlaceholder}>
                <Ionicons name="person" size={40} color={COLORS.muted} />
              </View>
            )}
          </View>
          
          <View style={styles.studentInfo}>
            <Text style={styles.studentName}>{cardInfo.student.name}</Text>
            <Text style={styles.studentId}>ID: {cardInfo.student.id}</Text>
            <Text style={styles.program}>{cardInfo.student.program}</Text>
            <Text style={styles.validity}>Válida hasta: {cardInfo.student.validUntil}</Text>
          </View>
        </View>
        
        <View style={styles.cardFooter}>
          <Text style={styles.footerText}>Documento oficial de identificación estudiantil</Text>
        </View>
      </View>
    </Card>
  );

  const renderRequestStatus = () => {
    const status = statusInfo[requestStatus];
    
    return (
      <Card style={styles.statusCard}>
        <Text style={styles.cardTitle}>Estado de la Solicitud</Text>
        
        <View style={styles.statusContainer}>
          <View style={[styles.statusIcon, { backgroundColor: status.color + '20' }]}>
            <Ionicons name={status.icon} size={24} color={status.color} />
          </View>
          
          <View style={styles.statusInfo}>
            <Text style={[styles.statusText, { color: status.color }]}>
              {requestStatus}
            </Text>
            <Text style={styles.statusMessage}>{status.message}</Text>
          </View>
        </View>
        
        <View style={styles.requestDetails}>
          <View style={styles.detailRow}>
            <Text style={styles.detailLabel}>No. Solicitud:</Text>
            <Text style={styles.detailValue}>{cardInfo.currentRequest.id}</Text>
          </View>
          
          <View style={styles.detailRow}>
            <Text style={styles.detailLabel}>Fecha de solicitud:</Text>
            <Text style={styles.detailValue}>{cardInfo.currentRequest.requestDate}</Text>
          </View>
          
          <View style={styles.detailRow}>
            <Text style={styles.detailLabel}>Fecha estimada:</Text>
            <Text style={styles.detailValue}>{cardInfo.currentRequest.estimatedDate}</Text>
          </View>
          
          <View style={styles.detailRow}>
            <Text style={styles.detailLabel}>Costo:</Text>
            <Text style={styles.detailValue}>
              ${cardInfo.currentRequest.cost.toLocaleString('es-CL', {
                minimumFractionDigits: 0,
                maximumFractionDigits: 0
              })} CLP
            </Text>
          </View>
          
          <View style={styles.detailRow}>
            <Text style={styles.detailLabel}>Estado del pago:</Text>
            <Text style={[
              styles.detailValue,
              { color: cardInfo.currentRequest.paymentStatus === 'Pagado' ? COLORS.success : COLORS.error }
            ]}>
              {cardInfo.currentRequest.paymentStatus}
            </Text>
          </View>
        </View>
      </Card>
    );
  };

  const renderRequirements = () => (
    <Card style={styles.requirementsCard}>
      <Text style={styles.cardTitle}>Requisitos para Reposición</Text>
      
      <View style={styles.requirementsList}>
        <View style={styles.requirementItem}>
          <Ionicons name="checkmark-circle" size={20} color={COLORS.success} />
          <Text style={styles.requirementText}>Acta de nacimiento (copia)</Text>
        </View>
        
        <View style={styles.requirementItem}>
          <Ionicons name="checkmark-circle" size={20} color={COLORS.success} />
          <Text style={styles.requirementText}>Identificación oficial (INE/Pasaporte)</Text>
        </View>
        
        <View style={styles.requirementItem}>
          <Ionicons name="checkmark-circle" size={20} color={COLORS.success} />
          <Text style={styles.requirementText}>Fotografía tamaño infantil</Text>
        </View>
        
        <View style={styles.requirementItem}>
          <Ionicons name="checkmark-circle" size={20} color={COLORS.success} />
          <Text style={styles.requirementText}>Comprobante de pago</Text>
        </View>
        
        <View style={styles.requirementItem}>
          <Ionicons name="checkmark-circle" size={20} color={COLORS.success} />
          <Text style={styles.requirementText}>Formato de solicitud</Text>
        </View>
      </View>
      
      <Text style={styles.noteText}>
        * Los documentos deben ser entregados en original y copia
      </Text>
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
        {renderCardPreview()}
        {renderRequestStatus()}
        {renderRequirements()}
      </ScrollView>

      {/* Actions */}
      <View style={styles.actionsContainer}>
        {cardInfo.currentRequest.paymentStatus !== 'Pagado' && (
          <Button
            title="Realizar Pago"
            onPress={handlePayment}
            style={styles.actionButton}
            icon="card-outline"
          />
        )}
        
        <Button
          title="Nueva Solicitud"
          onPress={handleNewRequest}
          variant="outlined"
          style={styles.actionButton}
          icon="add-circle-outline"
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
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: COLORS.primary,
    padding: SPACING.md,
    paddingTop: SPACING.xl,
  },
  backButton: {
    marginRight: SPACING.md,
  },
  headerTitle: {
    fontSize: FONT_SIZE.lg,
    color: COLORS.white,
    fontWeight: FONT_WEIGHT.bold,
    flex: 1,
    textAlign: 'center',
  },
  content: {
    flex: 1,
    padding: SPACING.md,
  },
  cardPreview: {
    marginBottom: SPACING.lg,
  },
  cardTitle: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.md,
  },
  credentialCard: {
    backgroundColor: COLORS.white,
    borderRadius: 12,
    padding: SPACING.md,
    borderWidth: 2,
    borderColor: COLORS.primary,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.1,
    shadowRadius: 8,
    elevation: 5,
  },
  cardHeader: {
    alignItems: 'center',
    marginBottom: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
    paddingBottom: SPACING.sm,
  },
  universityName: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.primary,
  },
  cardType: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
    marginTop: SPACING.xs,
  },
  cardBody: {
    flexDirection: 'row',
    marginBottom: SPACING.md,
  },
  photoContainer: {
    marginRight: SPACING.md,
  },
  photo: {
    width: 80,
    height: 100,
    borderRadius: 8,
  },
  photoPlaceholder: {
    width: 80,
    height: 100,
    backgroundColor: COLORS.lightGray,
    borderRadius: 8,
    justifyContent: 'center',
    alignItems: 'center',
  },
  studentInfo: {
    flex: 1,
    justifyContent: 'center',
  },
  studentName: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  studentId: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.primary,
    fontWeight: FONT_WEIGHT.medium,
    marginBottom: SPACING.xs,
  },
  program: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  validity: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.muted,
  },
  cardFooter: {
    borderTopWidth: 1,
    borderTopColor: COLORS.lightGray,
    paddingTop: SPACING.sm,
    alignItems: 'center',
  },
  footerText: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.muted,
    textAlign: 'center',
  },
  statusCard: {
    marginBottom: SPACING.lg,
  },
  statusContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: SPACING.lg,
  },
  statusIcon: {
    width: 50,
    height: 50,
    borderRadius: 25,
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: SPACING.md,
  },
  statusInfo: {
    flex: 1,
  },
  statusText: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    marginBottom: SPACING.xs,
  },
  statusMessage: {
    fontSize: FONT_SIZE.md,
    color: COLORS.muted,
  },
  requestDetails: {
    borderTopWidth: 1,
    borderTopColor: COLORS.lightGray,
    paddingTop: SPACING.md,
  },
  detailRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: SPACING.sm,
  },
  detailLabel: {
    fontSize: FONT_SIZE.md,
    color: COLORS.muted,
  },
  detailValue: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    fontWeight: FONT_WEIGHT.medium,
  },
  requirementsCard: {
    marginBottom: SPACING.xl,
  },
  requirementsList: {
    marginBottom: SPACING.md,
  },
  requirementItem: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: SPACING.sm,
  },
  requirementText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    marginLeft: SPACING.sm,
    flex: 1,
  },
  noteText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
    fontStyle: 'italic',
    textAlign: 'center',
  },
  actionsContainer: {
    flexDirection: 'row',
    padding: SPACING.md,
    backgroundColor: COLORS.white,
    borderTopWidth: 1,
    borderTopColor: COLORS.lightGray,
  },
  actionButton: {
    flex: 1,
    marginHorizontal: SPACING.xs,
  },
});

export default UniversityCardScreen;
