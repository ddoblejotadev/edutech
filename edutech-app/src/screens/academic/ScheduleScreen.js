import React, { useState } from 'react';
import { View, StyleSheet, ScrollView } from 'react-native';
import { Text, Card, Chip, SegmentedButtons, Divider } from 'react-native-paper';
import { Ionicons } from '@expo/vector-icons';

const ScheduleScreen = () => {
  const [viewType, setViewType] = useState('daily');
  const daysOfWeek = ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes'];
  const currentDay = 'Lunes'; // En una app real, obtener el día actual
  
  // Datos de ejemplo - Horario de clases
  const classSchedule = {
    Lunes: [
      { id: 1, code: 'CS101', name: 'Introducción a la Programación', timeStart: '08:00', timeEnd: '09:30', location: 'Edificio A, Aula 101', professor: 'Dr. García' },
      { id: 2, code: 'DB202', name: 'Bases de Datos Avanzadas', timeStart: '10:00', timeEnd: '11:30', location: 'Edificio B, Lab 203', professor: 'Dra. Rodríguez' },
    ],
    Martes: [
      { id: 3, code: 'AI303', name: 'Inteligencia Artificial', timeStart: '09:00', timeEnd: '10:30', location: 'Edificio C, Aula 305', professor: 'Dr. Martínez' },
    ],
    Miércoles: [
      { id: 4, code: 'CS101', name: 'Introducción a la Programación', timeStart: '08:00', timeEnd: '09:30', location: 'Edificio A, Aula 101', professor: 'Dr. García' },
      { id: 5, code: 'WD204', name: 'Desarrollo Web', timeStart: '13:00', timeEnd: '14:30', location: 'Edificio B, Lab 204', professor: 'Lic. Hernández' },
    ],
    Jueves: [
      { id: 6, code: 'AI303', name: 'Inteligencia Artificial', timeStart: '09:00', timeEnd: '10:30', location: 'Edificio C, Aula 305', professor: 'Dr. Martínez' },
      { id: 7, code: 'DB202', name: 'Bases de Datos Avanzadas', timeStart: '10:00', timeEnd: '11:30', location: 'Edificio B, Lab 203', professor: 'Dra. Rodríguez' },
    ],
    Viernes: [
      { id: 8, code: 'WD204', name: 'Desarrollo Web', timeStart: '13:00', timeEnd: '14:30', location: 'Edificio B, Lab 204', professor: 'Lic. Hernández' },
    ],
  };
  
  // Colores para los cursos
  const courseColors = {
    'CS101': '#4CAF50',
    'DB202': '#2196F3',
    'AI303': '#F44336',
    'WD204': '#FF9800',
  };
  
  const getClassColor = (code) => {
    return courseColors[code] || '#9E9E9E';
  };
  
  const renderDailySchedule = (day) => {
    const classes = classSchedule[day] || [];
    
    if (classes.length === 0) {
      return (
        <Card style={styles.emptyCard}>
          <Card.Content>
            <Text style={styles.emptyText}>No hay clases programadas</Text>
          </Card.Content>
        </Card>
      );
    }
    
    return classes.map(cls => (
      <Card 
        key={cls.id} 
        style={[styles.classCard, {borderLeftColor: getClassColor(cls.code)}]}
      >
        <Card.Content>
          <View style={styles.classHeader}>
            <View>
              <Text style={styles.classTime}>
                {cls.timeStart} - {cls.timeEnd}
              </Text>
              <Text style={styles.className}>{cls.name}</Text>
              <Text style={styles.classCode}>{cls.code}</Text>
            </View>
          </View>
          <Divider style={styles.divider} />
          <View style={styles.classDetails}>
            <View style={styles.detailItem}>
              <Ionicons name="location-outline" size={16} color="#666" />
              <Text style={styles.detailText}>{cls.location}</Text>
            </View>
            <View style={styles.detailItem}>
              <Ionicons name="person-outline" size={16} color="#666" />
              <Text style={styles.detailText}>{cls.professor}</Text>
            </View>
          </View>
        </Card.Content>
      </Card>
    ));
  };
  
  const renderWeeklySchedule = () => {
    return daysOfWeek.map(day => (
      <View key={day} style={styles.dayContainer}>
        <Text style={[
          styles.dayHeader, 
          day === currentDay ? styles.currentDayHeader : null
        ]}>
          {day}
        </Text>
        {renderDailySchedule(day)}
      </View>
    ));
  };
  
  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <SegmentedButtons
          value={viewType}
          onValueChange={setViewType}
          buttons={[
            { value: 'daily', label: 'Diario' },
            { value: 'weekly', label: 'Semanal' },
          ]}
          style={styles.segmentButtons}
        />
      </View>
      
      <ScrollView style={styles.scheduleContainer}>
        {viewType === 'daily' ? (
          <>
            <View style={styles.currentDayContainer}>
              <Text style={styles.currentDayText}>{currentDay}</Text>
              <Chip mode="outlined" style={styles.dateChip}>15 de Mayo, 2025</Chip>
            </View>
            {renderDailySchedule(currentDay)}
          </>
        ) : (
          renderWeeklySchedule()
        )}
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
    padding: 16,
    backgroundColor: '#fff',
    borderBottomWidth: 1,
    borderBottomColor: '#eee',
  },
  segmentButtons: {
    backgroundColor: '#fff',
  },
  scheduleContainer: {
    flex: 1,
    padding: 16,
  },
  currentDayContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 16,
  },
  currentDayText: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#00008B',
  },
  dateChip: {
    backgroundColor: '#E8EAF6',
  },
  emptyCard: {
    marginBottom: 16,
    backgroundColor: '#f9f9f9',
  },
  emptyText: {
    textAlign: 'center',
    color: '#888',
    fontStyle: 'italic',
  },
  classCard: {
    marginBottom: 16,
    borderLeftWidth: 4,
  },
  classHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
  },
  classTime: {
    fontSize: 14,
    fontWeight: 'bold',
    color: '#00008B',
  },
  className: {
    fontSize: 16,
    fontWeight: 'bold',
    marginTop: 4,
  },
  classCode: {
    fontSize: 14,
    color: '#666',
  },
  divider: {
    marginVertical: 10,
  },
  classDetails: {
    gap: 8,
  },
  detailItem: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  detailText: {
    fontSize: 14,
    color: '#666',
  },
  dayContainer: {
    marginBottom: 24,
  },
  dayHeader: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 10,
    color: '#555',
  },
  currentDayHeader: {
    color: '#00008B',
  },
});

export default ScheduleScreen;
