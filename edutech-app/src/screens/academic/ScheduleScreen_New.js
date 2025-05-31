import React, { useState, useEffect, useContext } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  RefreshControl,
  SafeAreaView
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONTS, BORDER_RADIUS } from '../../config/theme';
import { Card } from '../../components/common/UIComponents';
import { AuthContext } from '../../context/AuthContext';
import { ScheduleService } from '../../services/studentApiService';

const ScheduleScreen = ({ navigation }) => {
  const [loading, setLoading] = useState(false);
  const [refreshing, setRefreshing] = useState(false);
  const [viewType, setViewType] = useState('daily');
  const [selectedDay, setSelectedDay] = useState('Lunes');
  const { user } = useContext(AuthContext);

  // Días de la semana
  const daysOfWeek = ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes'];
  
  // Datos de horario universitario realista
  const universitySchedule = {
    Lunes: [
      {
        id: 1,
        subject: 'Programación Avanzada',
        code: 'INF-401',
        time: '08:00 - 09:30',
        professor: 'Dr. Carlos Mendoza',
        classroom: 'Lab. Informática A',
        building: 'Edificio Tecnológico',
        type: 'Laboratorio'
      },
      {
        id: 2,
        subject: 'Base de Datos II',
        code: 'INF-302',
        time: '10:00 - 11:30',
        professor: 'Ing. María González',
        classroom: 'Aula 205',
        building: 'Edificio Central',
        type: 'Teórica'
      },
      {
        id: 3,
        subject: 'Inglés Técnico',
        code: 'ING-201',
        time: '14:00 - 15:30',
        professor: 'Lic. Ana Torres',
        classroom: 'Aula 301',
        building: 'Edificio de Idiomas',
        type: 'Práctica'
      }
    ],
    Martes: [
      {
        id: 4,
        subject: 'Arquitectura de Software',
        code: 'INF-403',
        time: '09:00 - 10:30',
        professor: 'Dr. Roberto Silva',
        classroom: 'Aula 102',
        building: 'Edificio Tecnológico',
        type: 'Teórica'
      },
      {
        id: 5,
        subject: 'Redes de Computadoras',
        code: 'INF-304',
        time: '11:00 - 12:30',
        professor: 'Ing. Luis Vargas',
        classroom: 'Lab. Redes',
        building: 'Edificio Tecnológico',
        type: 'Laboratorio'
      }
    ],
    Miércoles: [
      {
        id: 6,
        subject: 'Programación Avanzada',
        code: 'INF-401',
        time: '08:00 - 09:30',
        professor: 'Dr. Carlos Mendoza',
        classroom: 'Lab. Informática A',
        building: 'Edificio Tecnológico',
        type: 'Laboratorio'
      },
      {
        id: 7,
        subject: 'Gestión de Proyectos',
        code: 'ADM-205',
        time: '15:00 - 16:30',
        professor: 'MBA. Patricia Ruiz',
        classroom: 'Aula 401',
        building: 'Edificio Central',
        type: 'Teórica'
      }
    ],
    Jueves: [
      {
        id: 8,
        subject: 'Arquitectura de Software',
        code: 'INF-403',
        time: '09:00 - 10:30',
        professor: 'Dr. Roberto Silva',
        classroom: 'Aula 102',
        building: 'Edificio Tecnológico',
        type: 'Teórica'
      },
      {
        id: 9,
        subject: 'Seminario de Tesis',
        code: 'INV-501',
        time: '13:00 - 14:30',
        professor: 'Dr. Miguel Herrera',
        classroom: 'Sala de Conferencias',
        building: 'Edificio de Investigación',
        type: 'Seminario'
      }
    ],
    Viernes: [
      {
        id: 10,
        subject: 'Base de Datos II',
        code: 'INF-302',
        time: '10:00 - 11:30',
        professor: 'Ing. María González',
        classroom: 'Lab. Informática B',
        building: 'Edificio Tecnológico',
        type: 'Laboratorio'
      }
    ]
  };

  // Colores para diferentes tipos de clases
  const getClassColor = (type) => {
    const colors = {
      'Teórica': '#1565C0',
      'Laboratorio': '#2E7D32',
      'Práctica': '#F57C00',
      'Seminario': '#7B1FA2'
    };
    return colors[type] || '#455A64';
  };

  const renderDaySelector = () => {
    return (
      <View style={styles.daySelectorContainer}>
        <ScrollView horizontal showsHorizontalScrollIndicator={false}>
          {daysOfWeek.map((day) => (
            <TouchableOpacity
              key={day}
              style={[
                styles.dayButton,
                selectedDay === day && styles.selectedDayButton
              ]}
              onPress={() => setSelectedDay(day)}
            >
              <Text style={[
                styles.dayButtonText,
                selectedDay === day && styles.selectedDayButtonText
              ]}>
                {day}
              </Text>
            </TouchableOpacity>
          ))}
        </ScrollView>
      </View>
    );
  };

  const renderClassCard = (classItem) => {
    return (
      <Card key={classItem.id} style={styles.classCard}>
        <View style={[styles.classIndicator, { backgroundColor: getClassColor(classItem.type) }]} />
        
        <View style={styles.classContent}>
          <View style={styles.classHeader}>
            <View style={styles.classInfo}>
              <Text style={styles.subjectName}>{classItem.subject}</Text>
              <Text style={styles.subjectCode}>{classItem.code}</Text>
            </View>
            <View style={styles.classType}>
              <Text style={[styles.typeLabel, { color: getClassColor(classItem.type) }]}>
                {classItem.type}
              </Text>
            </View>
          </View>

          <View style={styles.timeContainer}>
            <Ionicons name="time-outline" size={16} color={COLORS.textSecondary} />
            <Text style={styles.timeText}>{classItem.time}</Text>
          </View>

          <View style={styles.detailsContainer}>
            <View style={styles.detailRow}>
              <Ionicons name="person-outline" size={16} color={COLORS.textSecondary} />
              <Text style={styles.detailText}>{classItem.professor}</Text>
            </View>
            
            <View style={styles.detailRow}>
              <Ionicons name="location-outline" size={16} color={COLORS.textSecondary} />
              <Text style={styles.detailText}>
                {classItem.classroom}, {classItem.building}
              </Text>
            </View>
          </View>
        </View>
      </Card>
    );
  };

  const renderScheduleForDay = () => {
    const daySchedule = universitySchedule[selectedDay] || [];
    
    if (daySchedule.length === 0) {
      return (
        <View style={styles.emptyContainer}>
          <Ionicons name="calendar-outline" size={64} color={COLORS.textSecondary} />
          <Text style={styles.emptyTitle}>Sin clases programadas</Text>
          <Text style={styles.emptySubtitle}>
            No tienes clases programadas para {selectedDay.toLowerCase()}
          </Text>
        </View>
      );
    }

    return daySchedule.map(renderClassCard);
  };

  const onRefresh = () => {
    setRefreshing(true);
    // Simular carga
    setTimeout(() => {
      setRefreshing(false);
    }, 1000);
  };

  return (
    <SafeAreaView style={styles.container}>
      {/* Header */}
      <View style={styles.header}>
        <Text style={styles.headerTitle}>Mi Horario</Text>
        <Text style={styles.headerSubtitle}>Semestre 2025-I</Text>
      </View>

      {/* Day Selector */}
      {renderDaySelector()}

      {/* Schedule Content */}
      <ScrollView
        style={styles.scheduleContainer}
        refreshControl={
          <RefreshControl
            refreshing={refreshing}
            onRefresh={onRefresh}
            colors={[COLORS.primary]}
          />
        }
      >
        <View style={styles.dayHeader}>
          <Text style={styles.dayTitle}>{selectedDay}</Text>
          <Text style={styles.dayDate}>30 de Mayo, 2025</Text>
        </View>

        {renderScheduleForDay()}

        {/* Footer spacing */}
        <View style={styles.footerSpacing} />
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
    paddingHorizontal: SPACING.lg,
    paddingTop: SPACING.md,
    paddingBottom: SPACING.lg,
  },
  headerTitle: {
    ...FONTS.h2,
    color: COLORS.white,
    fontWeight: '600',
  },
  headerSubtitle: {
    ...FONTS.body2,
    color: COLORS.white,
    opacity: 0.9,
    marginTop: SPACING.xs,
  },
  daySelectorContainer: {
    backgroundColor: COLORS.white,
    paddingVertical: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.border,
  },
  dayButton: {
    paddingHorizontal: SPACING.lg,
    paddingVertical: SPACING.sm,
    marginHorizontal: SPACING.xs,
    borderRadius: BORDER_RADIUS.lg,
    backgroundColor: COLORS.background,
  },
  selectedDayButton: {
    backgroundColor: COLORS.primary,
  },
  dayButtonText: {
    ...FONTS.body2,
    color: COLORS.text,
    fontWeight: '500',
  },
  selectedDayButtonText: {
    color: COLORS.white,
  },
  scheduleContainer: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  dayHeader: {
    padding: SPACING.lg,
    backgroundColor: COLORS.white,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.border,
  },
  dayTitle: {
    ...FONTS.h3,
    color: COLORS.text,
    fontWeight: '600',
  },
  dayDate: {
    ...FONTS.body2,
    color: COLORS.textSecondary,
    marginTop: SPACING.xs,
  },
  classCard: {
    marginHorizontal: SPACING.lg,
    marginVertical: SPACING.sm,
    padding: 0,
    overflow: 'hidden',
  },
  classIndicator: {
    width: 4,
    position: 'absolute',
    left: 0,
    top: 0,
    bottom: 0,
  },
  classContent: {
    padding: SPACING.lg,
    paddingLeft: SPACING.lg + 8,
  },
  classHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    marginBottom: SPACING.sm,
  },
  classInfo: {
    flex: 1,
  },
  subjectName: {
    ...FONTS.h4,
    color: COLORS.text,
    fontWeight: '600',
    marginBottom: SPACING.xs,
  },
  subjectCode: {
    ...FONTS.caption,
    color: COLORS.textSecondary,
    fontWeight: '500',
  },
  classType: {
    backgroundColor: COLORS.background,
    paddingHorizontal: SPACING.sm,
    paddingVertical: SPACING.xs,
    borderRadius: BORDER_RADIUS.sm,
  },
  typeLabel: {
    ...FONTS.caption,
    fontWeight: '600',
  },
  timeContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: SPACING.md,
  },
  timeText: {
    ...FONTS.body2,
    color: COLORS.text,
    fontWeight: '500',
    marginLeft: SPACING.sm,
  },
  detailsContainer: {
    gap: SPACING.sm,
  },
  detailRow: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  detailText: {
    ...FONTS.body2,
    color: COLORS.textSecondary,
    marginLeft: SPACING.sm,
    flex: 1,
  },
  emptyContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: SPACING.xl,
    marginTop: SPACING.xl * 2,
  },
  emptyTitle: {
    ...FONTS.h3,
    color: COLORS.textSecondary,
    marginTop: SPACING.lg,
    marginBottom: SPACING.sm,
  },
  emptySubtitle: {
    ...FONTS.body2,
    color: COLORS.textSecondary,
    textAlign: 'center',
    lineHeight: 20,
  },
  footerSpacing: {
    height: SPACING.xl,
  },
});

export default ScheduleScreen;
