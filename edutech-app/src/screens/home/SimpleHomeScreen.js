import React, { useContext } from 'react';
import { View, Text, StyleSheet, SafeAreaView, ScrollView, TouchableOpacity } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, FONT_SIZE, SPACING, FONT_WEIGHT } from '../../config/theme';
import { AuthContext } from '../../context/AuthContext';

const SimpleHomeScreen = ({ navigation }) => {
  const { user } = useContext(AuthContext);

  const quickActions = [
    {
      id: 1,
      title: 'Mis Ramos',
      subtitle: 'Ver asignaturas inscritas',
      icon: 'book-outline',
      color: COLORS.primary,
      screen: 'Courses'
    },
    {
      id: 2,
      title: 'Horario',
      subtitle: 'Consultar horario de clases',
      icon: 'calendar-outline',
      color: '#10b981',
      screen: 'Schedule'
    },
    {
      id: 3,
      title: 'Notas',
      subtitle: 'Ver calificaciones',
      icon: 'school-outline',
      color: '#f59e0b',
      screen: 'Grades'
    },
    {
      id: 4,
      title: 'Trámites',
      subtitle: 'Servicios académicos',
      icon: 'document-text-outline',
      color: '#ef4444',
      screen: 'Services'
    },
  ];

  const renderQuickAction = (action) => (
    <TouchableOpacity
      key={action.id}
      style={[styles.actionCard, { borderLeftColor: action.color }]}
      onPress={() => navigation.navigate(action.screen)}
    >
      <View style={[styles.iconContainer, { backgroundColor: action.color + '15' }]}>
        <Ionicons name={action.icon} size={28} color={action.color} />
      </View>
      <View style={styles.actionContent}>
        <Text style={styles.actionTitle}>{action.title}</Text>
        <Text style={styles.actionSubtitle}>{action.subtitle}</Text>
      </View>
      <Ionicons name="chevron-forward" size={20} color={COLORS.muted} />
    </TouchableOpacity>
  );

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView showsVerticalScrollIndicator={false}>
        {/* Header */}
        <View style={styles.header}>
          <View>
            <Text style={styles.greeting}>¡Hola!</Text>
            <Text style={styles.userName}>{user?.name?.split(' ')[0] || 'Estudiante'}</Text>
          </View>
          <View style={styles.avatar}>
            <Ionicons name="person" size={24} color={COLORS.white} />
          </View>
        </View>

        {/* Información del estudiante */}
        <View style={styles.studentInfo}>
          <Text style={styles.studentId}>RUT: {user?.studentId || '12.345.678-9'}</Text>
          <Text style={styles.career}>{user?.career || 'Ingeniería Civil en Computación'}</Text>
        </View>

        {/* Acciones rápidas */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Acceso Rápido</Text>
          {quickActions.map(renderQuickAction)}
        </View>

        {/* Información importante */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Información Importante</Text>
          <View style={styles.infoCard}>
            <Ionicons name="information-circle" size={20} color={COLORS.primary} />
            <Text style={styles.infoText}>
              Recuerda que las inscripciones para el semestre 2024-2 están abiertas hasta el 15 de julio.
            </Text>
          </View>
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
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: COLORS.primary,
    padding: SPACING.lg,
    paddingTop: SPACING.xl,
  },
  greeting: {
    fontSize: FONT_SIZE.md,
    color: COLORS.white,
    opacity: 0.9,
  },
  userName: {
    fontSize: FONT_SIZE.xxl,
    color: COLORS.white,
    fontWeight: FONT_WEIGHT.bold,
  },
  avatar: {
    width: 50,
    height: 50,
    borderRadius: 25,
    backgroundColor: 'rgba(255,255,255,0.2)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  studentInfo: {
    backgroundColor: COLORS.white,
    padding: SPACING.lg,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.border,
  },
  studentId: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
    marginBottom: SPACING.xs,
  },
  career: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    fontWeight: FONT_WEIGHT.medium,
  },
  section: {
    padding: SPACING.lg,
  },
  sectionTitle: {
    fontSize: FONT_SIZE.lg,
    color: COLORS.text,
    fontWeight: FONT_WEIGHT.bold,
    marginBottom: SPACING.md,
  },
  actionCard: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: COLORS.white,
    padding: SPACING.md,
    borderRadius: 12,
    marginBottom: SPACING.sm,
    borderLeftWidth: 4,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  iconContainer: {
    width: 50,
    height: 50,
    borderRadius: 25,
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: SPACING.md,
  },
  actionContent: {
    flex: 1,
  },
  actionTitle: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    fontWeight: FONT_WEIGHT.semibold,
    marginBottom: SPACING.xs,
  },
  actionSubtitle: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
  },
  infoCard: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    backgroundColor: '#eff6ff',
    padding: SPACING.md,
    borderRadius: 8,
    borderLeftWidth: 4,
    borderLeftColor: COLORS.primary,
  },
  infoText: {
    flex: 1,
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
    marginLeft: SPACING.sm,
    lineHeight: 20,
  },
});

export default SimpleHomeScreen;
