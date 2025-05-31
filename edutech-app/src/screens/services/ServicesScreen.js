import React, { useState } from 'react';
import { View, StyleSheet, ScrollView, Alert } from 'react-native';
import { Text, Card, Title, Button } from 'react-native-paper';
import { Ionicons } from '@expo/vector-icons';

const ServicesScreen = ({ navigation }) => {
  const [serviceStatuses, setServiceStatuses] = useState({
    3: 'En proceso',
    6: 'Pendiente'
  });

  // Datos de ejemplo - Servicios universitarios chilenos
  const studentServices = [
    { 
      id: 1, 
      title: 'Certificado de alumno regular', 
      description: 'Solicita tu certificado de alumno regular sin costo', 
      icon: 'document-text-outline', 
      status: null,
      screen: 'AcademicTranscript',
      free: true
    },
    { 
      id: 2, 
      title: 'Concentración de notas', 
      description: 'Descarga tu concentración de notas oficial sin costo', 
      icon: 'school-outline', 
      status: null,
      screen: 'AcademicHistory',
      free: true
    },
    { 
      id: 3, 
      title: 'Credencial universitaria', 
      description: 'Reposición de credencial universitaria', 
      icon: 'card-outline', 
      status: serviceStatuses[3] || null,
      screen: 'UniversityCard',
      cost: '$5.000'
    },
    { 
      id: 4, 
      title: 'Inscripción de ramos', 
      description: 'Inscribe tus asignaturas para el próximo semestre', 
      icon: 'create-outline', 
      status: null,
      screen: 'ExamRegistration',
      free: true
    },
    { 
      id: 5, 
      title: 'Becas y ayudas estudiantiles', 
      description: 'Consulta las becas y beneficios disponibles', 
      icon: 'cash-outline', 
      status: null,
      screen: 'Scholarships',
      free: true
    },
    { 
      id: 6, 
      title: 'Práctica profesional', 
      description: 'Gestiona tu práctica profesional', 
      icon: 'people-outline', 
      status: serviceStatuses[6] || null,
      screen: 'SocialService',
      free: true
    },
  ];

  const handleServicePress = (service) => {
    if (service.screen) {
      navigation.navigate(service.screen, { 
        serviceId: service.id,
        title: service.title 
      });
    } else {
      Alert.alert(
        service.title,
        'Este servicio estará disponible próximamente.',
        [{ text: 'OK' }]
      );
    }
  };

  const renderServiceCard = (service) => {
    return (
      <Card key={service.id} style={styles.serviceCard}>
        <Card.Content>
          <View style={styles.serviceHeader}>
            <View style={styles.serviceIcon}>
              <Ionicons name={service.icon} size={24} color="#4f46e5" />
            </View>
            <View style={styles.serviceInfo}>
              <Title style={styles.serviceTitle}>{service.title}</Title>
              <Text style={styles.serviceDescription}>{service.description}</Text>
              
              {service.cost && (
                <Text style={styles.serviceCost}>Costo: {service.cost}</Text>
              )}
              {service.free && (
                <Text style={styles.serviceFree}>Gratuito</Text>
              )}
              
              {service.status && (
                <Text style={[styles.serviceStatus, 
                  service.status === 'En proceso' ? styles.statusInProgress : styles.statusPending
                ]}>
                  Estado: {service.status}
                </Text>
              )}
            </View>
          </View>
        </Card.Content>
        <Card.Actions>
          <Button 
            mode="outlined" 
            onPress={() => handleServicePress(service)}
          >
            {service.status ? 'Ver estado' : 'Solicitar'}
          </Button>
        </Card.Actions>
      </Card>
    );
  };

  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.headerTitle}>Servicios Estudiantiles</Text>
        <Text style={styles.headerSubtitle}>Gestiona tus trámites académicos en línea</Text>
      </View>
      
      <View style={styles.servicesContainer}>
        {studentServices.map(renderServiceCard)}
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  header: {
    backgroundColor: '#4f46e5',
    padding: 24,
    paddingTop: 48,
  },
  headerTitle: {
    fontSize: 24,
    fontWeight: 'bold',
    color: 'white',
    marginBottom: 4,
  },
  headerSubtitle: {
    fontSize: 16,
    color: 'white',
    opacity: 0.9,
  },
  servicesContainer: {
    padding: 16,
  },
  serviceCard: {
    marginBottom: 16,
    elevation: 2,
  },
  serviceHeader: {
    flexDirection: 'row',
    alignItems: 'flex-start',
  },
  serviceIcon: {
    width: 48,
    height: 48,
    borderRadius: 24,
    backgroundColor: '#f0f0ff',
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: 16,
  },
  serviceInfo: {
    flex: 1,
  },
  serviceTitle: {
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 4,
    color: '#1f2937',
  },
  serviceDescription: {
    fontSize: 14,
    color: '#6b7280',
    marginBottom: 8,
    lineHeight: 20,
  },
  serviceCost: {
    fontSize: 12,
    color: '#d97706',
    fontWeight: '600',
    marginBottom: 4,
  },
  serviceFree: {
    fontSize: 12,
    color: '#10b981',
    fontWeight: '600',
    marginBottom: 4,
  },
  serviceStatus: {
    fontSize: 12,
    fontWeight: '600',
    marginTop: 4,
  },
  statusInProgress: {
    color: '#3b82f6',
  },
  statusPending: {
    color: '#f59e0b',
  },
});

export default ServicesScreen;
