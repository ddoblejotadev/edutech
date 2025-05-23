import React from 'react';
import { View, StyleSheet, ScrollView } from 'react-native';
import { Text, Card, Title, Button, List, Avatar } from 'react-native-paper';
import { Ionicons } from '@expo/vector-icons';

const ServicesScreen = ({ navigation }) => {
  // Datos de ejemplo - Servicios universitarios
  const studentServices = [
    { id: 1, title: 'Constancia de estudios', description: 'Solicita tu constancia de estudios actual', icon: 'document-text-outline', status: null },
    { id: 2, title: 'Historial académico', description: 'Descarga tu historial académico oficial', icon: 'school-outline', status: null },
    { id: 3, title: 'Credencial universitaria', description: 'Reposición de credencial universitaria', icon: 'card-outline', status: 'En proceso' },
    { id: 4, title: 'Inscripción a exámenes', description: 'Inscríbete a exámenes extraordinarios', icon: 'create-outline', status: null },
    { id: 5, title: 'Becas y apoyos', description: 'Consulta las becas disponibles', icon: 'cash-outline', status: null },
    { id: 6, title: 'Servicio social', description: 'Gestiona tu servicio social', icon: 'people-outline', status: 'Pendiente' },
  ];
  
  // Datos de ejemplo - pagos pendientes
  const pendingPayments = [
    { id: 1, concept: 'Colegiatura Mayo 2025', amount: 1200, dueDate: '15/05/2025' },
    { id: 2, concept: 'Laboratorio de Informática', amount: 350, dueDate: '20/05/2025' },
  ];
  
  const total = pendingPayments.reduce((sum, payment) => sum + payment.amount, 0);
  
  return (
    <ScrollView style={styles.container}>
      <Card style={styles.studentInfoCard}>
        <Card.Content>
          <View style={styles.studentInfo}>
            <Avatar.Image 
              size={60} 
              source={require('../../assets/default-profile.png')} 
              style={styles.avatar}
            />
            <View style={styles.studentDetails}>
              <Title style={styles.studentName}>Ana María García</Title>
              <Text style={styles.studentId}>Matrícula: A220584</Text>
              <Text style={styles.studentProgram}>Ingeniería en Informática - 4° Semestre</Text>
            </View>
          </View>
        </Card.Content>
      </Card>
      
      <Card style={styles.financialCard}>
        <Card.Content>
          <Title style={styles.cardTitle}>Estado Financiero</Title>
          
          {pendingPayments.length > 0 ? (
            <>
              <Text style={styles.paymentHeading}>Pagos pendientes:</Text>
              {pendingPayments.map(payment => (
                <View key={payment.id} style={styles.paymentItem}>
                  <View>
                    <Text style={styles.paymentConcept}>{payment.concept}</Text>
                    <Text style={styles.paymentDate}>Vence: {payment.dueDate}</Text>
                  </View>
                  <Text style={styles.paymentAmount}>${payment.amount.toFixed(2)}</Text>
                </View>
              ))}
              <View style={styles.totalContainer}>
                <Text style={styles.totalLabel}>Total pendiente:</Text>
                <Text style={styles.totalAmount}>${total.toFixed(2)}</Text>
              </View>
              <Button 
                mode="contained" 
                style={styles.payButton}
                icon="credit-card"
              >
                Pagar Ahora
              </Button>
            </>
          ) : (
            <Text style={styles.noPaymentsText}>
              No tienes pagos pendientes.
            </Text>
          )}
        </Card.Content>
      </Card>
      
      <Card style={styles.servicesCard}>
        <Card.Content>
          <Title style={styles.cardTitle}>Trámites y Servicios</Title>
          
          <List.Section>
            {studentServices.map(service => (
              <List.Item
                key={service.id}
                title={service.title}
                description={service.description}
                left={props => <Ionicons name={service.icon} size={24} color="#00008B" {...props} />}
                right={props => 
                  service.status ? (
                    <Chip mode="outlined" style={styles.statusChip}>
                      {service.status}
                    </Chip>
                  ) : null
                }
                style={styles.serviceItem}
                onPress={() => {}}
              />
            ))}
          </List.Section>
          
          <Button 
            mode="outlined" 
            style={styles.moreButton}
            icon="chevron-right"
          >
            Ver todos los servicios
          </Button>
        </Card.Content>
      </Card>
      
      <Card style={styles.supportCard}>
        <Card.Content>
          <Title style={styles.cardTitle}>Soporte y Ayuda</Title>
          
          <View style={styles.supportOptions}>
            <View style={styles.supportOption}>
              <Ionicons name="chatbubble-ellipses-outline" size={28} color="#00008B" />
              <Text style={styles.supportText}>Chat</Text>
            </View>
            <View style={styles.supportOption}>
              <Ionicons name="call-outline" size={28} color="#00008B" />
              <Text style={styles.supportText}>Llamada</Text>
            </View>
            <View style={styles.supportOption}>
              <Ionicons name="mail-outline" size={28} color="#00008B" />
              <Text style={styles.supportText}>Email</Text>
            </View>
            <View style={styles.supportOption}>
              <Ionicons name="help-circle-outline" size={28} color="#00008B" />
              <Text style={styles.supportText}>FAQ</Text>
            </View>
          </View>
        </Card.Content>
      </Card>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
    padding: 16,
  },
  studentInfoCard: {
    marginBottom: 16,
  },
  studentInfo: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  avatar: {
    backgroundColor: '#00008B',
  },
  studentDetails: {
    marginLeft: 16,
    flex: 1,
  },
  studentName: {
    fontSize: 18,
  },
  studentId: {
    fontSize: 14,
    color: '#666',
  },
  studentProgram: {
    fontSize: 14,
    color: '#666',
  },
  financialCard: {
    marginBottom: 16,
  },
  cardTitle: {
    fontSize: 18,
    marginBottom: 10,
  },
  paymentHeading: {
    fontSize: 14,
    fontWeight: 'bold',
    marginBottom: 10,
    color: '#666',
  },
  paymentItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 12,
  },
  paymentConcept: {
    fontSize: 14,
    fontWeight: 'bold',
  },
  paymentDate: {
    fontSize: 12,
    color: '#666',
  },
  paymentAmount: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#F44336',
  },
  totalContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    borderTopWidth: 1,
    borderTopColor: '#eee',
    paddingTop: 10,
    marginTop: 5,
    marginBottom: 15,
  },
  totalLabel: {
    fontSize: 15,
    fontWeight: 'bold',
  },
  totalAmount: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#F44336',
  },
  payButton: {
    backgroundColor: '#00008B',
  },
  noPaymentsText: {
    textAlign: 'center',
    padding: 15,
    color: '#4CAF50',
    fontStyle: 'italic',
  },
  servicesCard: {
    marginBottom: 16,
  },
  serviceItem: {
    paddingVertical: 8,
  },
  statusChip: {
    backgroundColor: '#E8EAF6',
    height: 28,
  },
  moreButton: {
    marginTop: 5,
    borderColor: '#00008B',
  },
  supportCard: {
    marginBottom: 16,
  },
  supportOptions: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: 10,
  },
  supportOption: {
    alignItems: 'center',
    flex: 1,
  },
  supportText: {
    marginTop: 8,
    fontSize: 12,
    color: '#00008B',
  },
});

export default ServicesScreen;
