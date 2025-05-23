import React, { useState } from 'react';
import { View, StyleSheet, ScrollView } from 'react-native';
import { Text, Card, Title, Paragraph, Chip, Menu, Button } from 'react-native-paper';
import { Ionicons } from '@expo/vector-icons';

const CalendarScreen = () => {
  const [selectedMonth, setSelectedMonth] = useState('Mayo');
  const [menuVisible, setMenuVisible] = useState(false);
  
  // Datos de ejemplo - eventos académicos
  const academicEvents = [
    { id: 1, title: 'Inicio de semestre', date: '15 Mayo, 2025', type: 'general' },
    { id: 2, title: 'Examen parcial: Programación Avanzada', date: '20 Mayo, 2025', type: 'examen' },
    { id: 3, title: 'Entrega de proyecto: Bases de Datos', date: '22 Mayo, 2025', type: 'tarea' },
    { id: 4, title: 'Conferencia: Inteligencia Artificial', date: '28 Mayo, 2025', type: 'evento' },
    { id: 5, title: 'Plazo final de inscripción a talleres', date: '30 Mayo, 2025', type: 'administrativo' },
  ];
  
  const months = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
  
  const getEventColor = (type) => {
    switch(type) {
      case 'examen': return '#D32F2F';
      case 'tarea': return '#1976D2';
      case 'evento': return '#388E3C';
      case 'administrativo': return '#FFA000';
      default: return '#757575';
    }
  };
  
  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.headerTitle}>Calendario Académico</Text>
        
        <Menu
          visible={menuVisible}
          onDismiss={() => setMenuVisible(false)}
          anchor={
            <Button 
              mode="outlined" 
              onPress={() => setMenuVisible(true)}
              style={styles.monthSelector}
            >
              {selectedMonth} <Ionicons name="chevron-down" size={18} />
            </Button>
          }
        >
          {months.map(month => (
            <Menu.Item 
              key={month} 
              onPress={() => { 
                setSelectedMonth(month); 
                setMenuVisible(false); 
              }} 
              title={month} 
            />
          ))}
        </Menu>
      </View>
      
      <ScrollView style={styles.eventsContainer}>
        <View style={styles.todayContainer}>
          <Text style={styles.todayText}>HOY, 15 MAYO</Text>
          <Card style={styles.todayCard}>
            <Card.Content>
              <Title>Inicio de semestre</Title>
              <Paragraph>Bienvenida a nuevos estudiantes - Auditorio Principal</Paragraph>
              <View style={styles.timeContainer}>
                <Ionicons name="time-outline" size={18} color="#00008B" />
                <Text style={styles.timeText}>08:00 - 10:00</Text>
              </View>
            </Card.Content>
          </Card>
        </View>
        
        <Text style={styles.upcomingText}>PRÓXIMOS EVENTOS</Text>
        
        {academicEvents.map(event => (
          <Card key={event.id} style={styles.eventCard}>
            <Card.Content>
              <View style={styles.eventHeader}>
                <Title style={styles.eventTitle}>{event.title}</Title>
                <Chip 
                  style={[styles.eventTypeChip, { backgroundColor: `${getEventColor(event.type)}20` }]}
                  textStyle={{ color: getEventColor(event.type) }}
                >
                  {event.type.charAt(0).toUpperCase() + event.type.slice(1)}
                </Chip>
              </View>
              <Text style={styles.eventDate}>{event.date}</Text>
            </Card.Content>
          </Card>
        ))}
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: 16,
    backgroundColor: '#fff',
    borderBottomWidth: 1,
    borderBottomColor: '#eee',
  },
  headerTitle: {
    fontSize: 18,
    fontWeight: 'bold',
  },
  monthSelector: {
    borderColor: '#ddd',
  },
  eventsContainer: {
    flex: 1,
    padding: 16,
  },
  todayContainer: {
    marginBottom: 20,
  },
  todayText: {
    fontSize: 14,
    fontWeight: 'bold',
    color: '#00008B',
    marginBottom: 8,
  },
  todayCard: {
    backgroundColor: '#E8EAF6',
    borderLeftWidth: 4,
    borderLeftColor: '#00008B',
  },
  timeContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 10,
  },
  timeText: {
    marginLeft: 5,
    color: '#00008B',
    fontWeight: 'bold',
  },
  upcomingText: {
    fontSize: 14,
    fontWeight: 'bold',
    color: '#555',
    marginBottom: 8,
  },
  eventCard: {
    marginBottom: 10,
    borderLeftWidth: 4,
    borderLeftColor: '#ccc',
  },
  eventHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
  },
  eventTitle: {
    fontSize: 16,
    flex: 1,
    marginRight: 10,
  },
  eventTypeChip: {
    height: 28,
  },
  eventDate: {
    fontSize: 14,
    color: '#666',
    marginTop: 5,
  },
});

export default CalendarScreen;
