import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  SafeAreaView,
  TouchableOpacity,
  Alert,
  RefreshControl
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT, FONTS } from '../../config/theme';
import { Card, LoadingState } from '../../components/common/UIComponents';

const ServicesScreen = ({ navigation }) => {
  const [loading, setLoading] = useState(false);
  const [refreshing, setRefreshing] = useState(false);
  
  // Estados de servicios para mostrar notificaciones
  const serviceStatuses = {
    3: { status: 'En proceso', color: COLORS.warning, message: 'Tu credencial está siendo procesada' },
    6: { status: 'Documentos pendientes', color: COLORS.error, message: 'Falta entregar documentos' }
  };

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
      cost: '$8.500 CLP'
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

  const quickActions = [
    { 
      id: 'academic-transcript', 
      title: 'Certificado Alumno Regular', 
      icon: 'document-text', 
      color: COLORS.primary,
      action: () => navigation.navigate('AcademicTranscript', { title: 'Certificado de Alumno Regular' })
    },
    { 
      id: 'grades', 
      title: 'Ver Notas', 
      icon: 'school', 
      color: COLORS.success,
      action: () => navigation.navigate('Grades')
    },
    { 
      id: 'schedule', 
      title: 'Mi Horario', 
      icon: 'calendar', 
      color: COLORS.info,
      action: () => navigation.navigate('Schedule')
    },
    { 
      id: 'scholarships', 
      title: 'Becas', 
      icon: 'star', 
      color: COLORS.warning,
      action: () => navigation.navigate('Scholarships', { title: 'Becas y Ayudas Estudiantiles' })
    }
  ];

  const handleServicePress = (service) => {
    try {
      if (service.screen === 'AcademicTranscript') {
        navigation.navigate('AcademicTranscript', { title: service.title });
      } else if (service.screen === 'AcademicHistory') {
        navigation.navigate('Grades');
      } else if (service.screen === 'UniversityCard') {
        navigation.navigate('UniversityCard', { title: service.title });
      } else if (service.screen === 'ExamRegistration') {
        navigation.navigate('ExamRegistration', { title: service.title });
      } else if (service.screen === 'Scholarships') {
        navigation.navigate('Scholarships', { title: service.title });
      } else if (service.screen === 'SocialService') {
        navigation.navigate('SocialService', { title: service.title });
      } else {
        // Fallback para pantallas no implementadas
        Alert.alert(
          'Servicio Temporalmente No Disponible', 
          `El servicio "${service.title}" está temporalmente fuera de servicio.\n\nIntenta nuevamente más tarde o contacta a servicios estudiantiles.`,
          [{ text: 'OK' }]
        );
      }
    } catch (error) {
      console.error('Error navegando a servicio:', error);
      Alert.alert(
        'Error de Navegación', 
        'Ocurrió un error al acceder al servicio. Por favor intenta nuevamente.',
        [{ text: 'OK' }]
      );
    }
  };

  const onRefresh = async () => {
    setRefreshing(true);
    // Simular recarga de datos
    await new Promise(resolve => setTimeout(resolve, 1000));
    setRefreshing(false);
  };

  const renderServiceCard = (service) => (
    <TouchableOpacity
      key={service.id}
      style={styles.serviceCard}
      onPress={() => handleServicePress(service)}
      activeOpacity={0.7}
    >
      <View style={styles.serviceHeader}>
        <View style={[styles.iconContainer, { backgroundColor: `${COLORS.primary}15` }]}>
          <Ionicons name={service.icon} size={24} color={COLORS.primary} />
        </View>
        
        <View style={styles.serviceInfo}>
          <Text style={styles.serviceTitle}>{service.title}</Text>
          <Text style={styles.serviceDescription}>{service.description}</Text>
          
          {service.status && (
            <View style={[styles.statusContainer, { backgroundColor: `${service.status.color}15` }]}>
              <View style={[styles.statusDot, { backgroundColor: service.status.color }]} />
              <Text style={[styles.statusText, { color: service.status.color }]}>
                {service.status.status}
              </Text>
            </View>
          )}
        </View>
        
        <View style={styles.serviceRight}>
          {service.free ? (
            <View style={styles.freeBadge}>
              <Text style={styles.freeText}>Gratuito</Text>
            </View>
          ) : (
            <Text style={styles.costText}>{service.cost}</Text>
          )}
          <Ionicons name="chevron-forward" size={20} color={COLORS.muted} />
        </View>
      </View>
    </TouchableOpacity>
  );

  const renderQuickAction = (action) => (
    <TouchableOpacity
      key={action.id}
      style={[styles.quickActionCard, { borderLeftColor: action.color }]}
      onPress={action.action}
      activeOpacity={0.7}
    >
      <View style={[styles.quickActionIcon, { backgroundColor: `${action.color}15` }]}>
        <Ionicons name={action.icon} size={20} color={action.color} />
      </View>
      <Text style={styles.quickActionTitle}>{action.title}</Text>
    </TouchableOpacity>
  );

  if (loading) {
    return <LoadingState message="Cargando servicios..." />;
  }

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.headerTitle}>Servicios Estudiantiles</Text>
        <Text style={styles.headerSubtitle}>DUOC UC - Plaza Vespucio</Text>
      </View>

      <ScrollView 
        style={styles.content}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
      >
        {/* Banner informativo */}
        <View style={styles.infoBanner}>
          <Ionicons name="information-circle" size={20} color={COLORS.info} />
          <Text style={styles.infoBannerText}>
            Algunos servicios pueden requerir documentación adicional
          </Text>
        </View>

        {/* Acciones rápidas */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Acciones Rápidas</Text>
          <View style={styles.quickActionsGrid}>
            {quickActions.map(renderQuickAction)}
          </View>
        </View>

        {/* Lista de servicios */}
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Todos los Servicios</Text>
          {studentServices.map(renderServiceCard)}
        </View>

        {/* Información de contacto */}
        <Card style={styles.contactCard}>
          <View style={styles.contactHeader}>
            <Ionicons name="help-circle" size={24} color={COLORS.primary} />
            <Text style={styles.contactTitle}>¿Necesitas Ayuda?</Text>
          </View>
          <Text style={styles.contactText}>
            Si tienes dudas sobre algún servicio, puedes contactar a:
          </Text>
          <Text style={styles.contactDetail}>
            📧 servicios.estudiantes@duocuc.cl{'\n'}
            📞 +56 2 2518 9000{'\n'}
            📍 Oficina de Servicios Estudiantiles, 1er piso
          </Text>
        </Card>
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
    paddingVertical: SPACING.xl,
    alignItems: 'center',
  },
  headerTitle: {
    ...FONTS.h2,
    color: COLORS.white,
    textAlign: 'center',
  },
  headerSubtitle: {
    ...FONTS.body2,
    color: COLORS.white,
    opacity: 0.9,
    marginTop: SPACING.xs,
  },
  content: {
    flex: 1,
    padding: SPACING.md,
  },
  infoBanner: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: `${COLORS.info}15`,
    padding: SPACING.md,
    borderRadius: 8,
    marginBottom: SPACING.lg,
  },
  infoBannerText: {
    ...FONTS.body2,
    color: COLORS.info,
    marginLeft: SPACING.sm,
    flex: 1,
  },
  section: {
    marginBottom: SPACING.xl,
  },
  sectionTitle: {
    ...FONTS.h4,
    color: COLORS.text,
    marginBottom: SPACING.md,
  },
  quickActionsGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
  },
  quickActionCard: {
    backgroundColor: COLORS.white,
    borderRadius: 8,
    padding: SPACING.md,
    width: '48%',
    marginBottom: SPACING.sm,
    borderLeftWidth: 3,
    elevation: 2,
    shadowColor: COLORS.shadow,
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
  },
  quickActionIcon: {
    width: 40,
    height: 40,
    borderRadius: 20,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: SPACING.sm,
  },
  quickActionTitle: {
    ...FONTS.body2,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.text,
  },
  serviceCard: {
    backgroundColor: COLORS.white,
    borderRadius: 12,
    padding: SPACING.md,
    marginBottom: SPACING.md,
    elevation: 3,
    shadowColor: COLORS.shadow,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
  },
  serviceHeader: {
    flexDirection: 'row',
    alignItems: 'flex-start',
  },
  iconContainer: {
    width: 48,
    height: 48,
    borderRadius: 24,
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: SPACING.md,
  },
  serviceInfo: {
    flex: 1,
  },
  serviceTitle: {
    ...FONTS.h5,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  serviceDescription: {
    ...FONTS.body2,
    color: COLORS.muted,
    lineHeight: 20,
    marginBottom: SPACING.sm,
  },
  statusContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: SPACING.sm,
    paddingVertical: SPACING.xs,
    borderRadius: 12,
    alignSelf: 'flex-start',
  },
  statusDot: {
    width: 6,
    height: 6,
    borderRadius: 3,
    marginRight: SPACING.xs,
  },
  statusText: {
    ...FONTS.caption,
    fontWeight: FONT_WEIGHT.medium,
  },
  serviceRight: {
    alignItems: 'flex-end',
    justifyContent: 'center',
  },
  freeBadge: {
    backgroundColor: COLORS.success,
    paddingHorizontal: SPACING.sm,
    paddingVertical: SPACING.xs,
    borderRadius: 12,
    marginBottom: SPACING.xs,
  },
  freeText: {
    ...FONTS.caption,
    color: COLORS.white,
    fontWeight: FONT_WEIGHT.bold,
  },
  costText: {
    ...FONTS.body2,
    color: COLORS.primary,
    fontWeight: FONT_WEIGHT.medium,
    marginBottom: SPACING.xs,
  },
  contactCard: {
    marginTop: SPACING.md,
  },
  contactHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: SPACING.md,
  },
  contactTitle: {
    ...FONTS.h5,
    color: COLORS.text,
    marginLeft: SPACING.sm,
  },
  contactText: {
    ...FONTS.body2,
    color: COLORS.text,
    marginBottom: SPACING.sm,
  },
  contactDetail: {
    ...FONTS.body2,
    color: COLORS.muted,
    lineHeight: 22,
  },
});

export default ServicesScreen;
