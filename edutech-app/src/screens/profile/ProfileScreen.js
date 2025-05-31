import React, { useState, useContext, useEffect } from 'react';
import { View, Text, StyleSheet, ScrollView, TouchableOpacity, Alert, RefreshControl, Image } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT, BORDER_RADIUS } from '../../config/theme';
import { AuthContext } from '../../context/AuthContext';
import { Card } from '../../components/common/UIComponents';
import { LoadingState, ErrorState } from '../../components/common/StateComponents';
import { DEMO_USERS, DEMO_FINANCIAL_STATUS, DEMO_SCHOLARSHIPS } from '../../data/demoData';

const ProfileScreen = ({ navigation }) => {
  const { user, logout } = useContext(AuthContext);
  const [loading, setLoading] = useState(false);
  const [refreshing, setRefreshing] = useState(false);
  const [profileData, setProfileData] = useState(null);
  const [financialData, setFinancialData] = useState(null);

  // Cargar datos del perfil
  useEffect(() => {
    loadProfileData();
  }, []);

  const loadProfileData = async () => {
    setLoading(true);
    try {
      // Simular carga de datos (en app real vendría de API)
      await new Promise(resolve => setTimeout(resolve, 1000));
      setProfileData(DEMO_USERS[0]);
      setFinancialData(DEMO_FINANCIAL_STATUS);
    } catch (error) {
      console.error('Error cargando perfil:', error);
    } finally {
      setLoading(false);
    }
  };

  const onRefresh = () => {
    setRefreshing(true);
    loadProfileData().finally(() => setRefreshing(false));
  };

  const handleLogout = () => {
    Alert.alert(
      'Cerrar Sesión',
      '¿Estás seguro de que quieres cerrar sesión?',
      [
        { text: 'Cancelar', style: 'cancel' },
        { text: 'Cerrar Sesión', onPress: logout, style: 'destructive' }
      ]
    );
  };

  const handleEditProfile = () => {
    Alert.alert('Editar Perfil', 'Funcionalidad en desarrollo');
  };

  const handleViewFinancialStatus = () => {
    navigation.navigate('FinancialStatus');
  };

  const handleViewScholarships = () => {
    navigation.navigate('Scholarships');
  };

  const handleRequestCertificate = () => {
    navigation.navigate('StudentServices');
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('es-CL', {
      style: 'currency',
      currency: 'CLP',
      minimumFractionDigits: 0
    }).format(amount);
  };

  const getProgressColor = (average) => {
    if (average >= 6.0) return COLORS.success;
    if (average >= 5.0) return COLORS.warning;
    return COLORS.error;
  };

  if (loading) {
    return <LoadingState message="Cargando perfil..." />;
  }

  if (!profileData) {
    return <ErrorState message="Error cargando perfil" onRetry={loadProfileData} />;
  }

  return (
    <ScrollView 
      style={styles.container}
      refreshControl={<RefreshControl refreshing={refreshing} onRefresh={onRefresh} />}
    >
      {/* Header con información principal */}
      <View style={styles.header}>
        <View style={styles.headerContent}>
          <View style={styles.avatarContainer}>
            {profileData.foto ? (
              <Image source={{ uri: profileData.foto }} style={styles.avatarImage} />
            ) : (
              <Ionicons name="person" size={64} color={COLORS.white} />
            )}
          </View>
          
          <View style={styles.userInfo}>
            <Text style={styles.userName}>{profileData.nombre}</Text>
            <Text style={styles.userEmail}>{profileData.email}</Text>
            <Text style={styles.userCareer}>{profileData.carrera}</Text>
            <Text style={styles.userSede}>{profileData.sede}</Text>
          </View>

          <TouchableOpacity style={styles.editButton} onPress={handleEditProfile}>
            <Ionicons name="create-outline" size={20} color={COLORS.white} />
          </TouchableOpacity>
        </View>
      </View>

      <View style={styles.content}>
        {/* Información Académica */}
        <Card style={styles.card}>
          <View style={styles.cardHeader}>
            <Ionicons name="school-outline" size={24} color={COLORS.primary} />
            <Text style={styles.cardTitle}>Información Académica</Text>
          </View>
          
          <View style={styles.infoGrid}>
            <View style={styles.infoItem}>
              <Text style={styles.infoLabel}>RUT</Text>
              <Text style={styles.infoValue}>{profileData.rut}</Text>
            </View>
            
            <View style={styles.infoItem}>
              <Text style={styles.infoLabel}>Estado</Text>
              <View style={[styles.statusBadge, { backgroundColor: COLORS.success }]}>
                <Text style={styles.statusText}>{profileData.estadoAcademico}</Text>
              </View>
            </View>
            
            <View style={styles.infoItem}>
              <Text style={styles.infoLabel}>Año / Semestre</Text>
              <Text style={styles.infoValue}>{profileData.año}º año - Semestre {profileData.semestre}</Text>
            </View>
            
            <View style={styles.infoItem}>
              <Text style={styles.infoLabel}>Modalidad</Text>
              <Text style={styles.infoValue}>{profileData.modalidad} - {profileData.jornada}</Text>
            </View>
            
            <View style={styles.infoItem}>
              <Text style={styles.infoLabel}>Fecha Ingreso</Text>
              <Text style={styles.infoValue}>{new Date(profileData.fechaIngreso).toLocaleDateString('es-CL')}</Text>
            </View>
            
            <View style={styles.infoItem}>
              <Text style={styles.infoLabel}>Cohorte</Text>
              <Text style={styles.infoValue}>{profileData.cohorte}</Text>
            </View>
          </View>
        </Card>

        {/* Rendimiento Académico */}
        <Card style={styles.card}>
          <View style={styles.cardHeader}>
            <Ionicons name="analytics-outline" size={24} color={COLORS.primary} />
            <Text style={styles.cardTitle}>Rendimiento Académico</Text>
          </View>
          
          <View style={styles.progressContainer}>
            <View style={styles.progressItem}>
              <Text style={styles.progressLabel}>Promedio General</Text>
              <View style={styles.progressRow}>
                <Text style={[styles.progressValue, { color: getProgressColor(profileData.promedio) }]}>
                  {profileData.promedio.toFixed(1)}
                </Text>
                <View style={styles.progressBar}>
                  <View 
                    style={[
                      styles.progressFill, 
                      { 
                        width: `${(profileData.promedio / 7.0) * 100}%`,
                        backgroundColor: getProgressColor(profileData.promedio)
                      }
                    ]} 
                  />
                </View>
              </View>
            </View>
            
            <View style={styles.progressItem}>
              <Text style={styles.progressLabel}>Progreso de Carrera</Text>
              <View style={styles.progressRow}>
                <Text style={styles.progressValue}>
                  {profileData.creditosAprobados}/{profileData.creditosTotales} créditos
                </Text>
                <View style={styles.progressBar}>
                  <View 
                    style={[
                      styles.progressFill, 
                      { 
                        width: `${(profileData.creditosAprobados / profileData.creditosTotales) * 100}%`,
                        backgroundColor: COLORS.info
                      }
                    ]} 
                  />
                </View>
              </View>
            </View>
          </View>

          <View style={styles.statsGrid}>
            <View style={styles.statItem}>
              <Text style={styles.statNumber}>{profileData.historialAcademico.asignaturasAprobadas}</Text>
              <Text style={styles.statLabel}>Asignaturas Aprobadas</Text>
            </View>
            
            <View style={styles.statItem}>
              <Text style={[styles.statNumber, { color: COLORS.warning }]}>
                {profileData.historialAcademico.asignaturasReprobadas}
              </Text>
              <Text style={styles.statLabel}>Reprobadas</Text>
            </View>
            
            <View style={styles.statItem}>
              <Text style={[styles.statNumber, { color: COLORS.info }]}>
                {profileData.historialAcademico.asignaturasPendientes}
              </Text>
              <Text style={styles.statLabel}>Pendientes</Text>
            </View>
          </View>
        </Card>

        {/* Estado Financiero */}
        <Card style={styles.card}>
          <View style={styles.cardHeader}>
            <Ionicons name="card-outline" size={24} color={COLORS.primary} />
            <Text style={styles.cardTitle}>Estado Financiero</Text>
            <TouchableOpacity onPress={handleViewFinancialStatus}>
              <Text style={styles.viewMoreText}>Ver detalles</Text>
            </TouchableOpacity>
          </View>
          
          <View style={styles.financialSummary}>
            <View style={styles.financialItem}>
              <Text style={styles.financialLabel}>Próximo Vencimiento</Text>
              <Text style={styles.financialDate}>
                {new Date(financialData.estadoCuenta.proximoVencimiento.fecha).toLocaleDateString('es-CL')}
              </Text>
              <Text style={styles.financialAmount}>
                {formatCurrency(financialData.estadoCuenta.proximoVencimiento.monto)}
              </Text>
            </View>
            
            {financialData.estadoCuenta.saldoTotal < 0 && (
              <View style={[styles.alertBanner, { backgroundColor: `${COLORS.warning}22` }]}>
                <Ionicons name="warning-outline" size={20} color={COLORS.warning} />
                <Text style={[styles.alertText, { color: COLORS.warning }]}>
                  Tienes cuotas pendientes por {formatCurrency(Math.abs(financialData.estadoCuenta.saldoTotal))}
                </Text>
              </View>
            )}
          </View>
        </Card>

        {/* Beneficios Activos */}
        <Card style={styles.card}>
          <View style={styles.cardHeader}>
            <Ionicons name="gift-outline" size={24} color={COLORS.primary} />
            <Text style={styles.cardTitle}>Beneficios Activos</Text>
            <TouchableOpacity onPress={handleViewScholarships}>
              <Text style={styles.viewMoreText}>Ver todos</Text>
            </TouchableOpacity>
          </View>
          
          {profileData.beneficios.map((beneficio, index) => (
            <View key={index} style={styles.benefitItem}>
              <View style={styles.benefitInfo}>
                <Text style={styles.benefitName}>{beneficio.nombre}</Text>
                {beneficio.porcentaje && (
                  <Text style={styles.benefitDetail}>{beneficio.porcentaje}% de descuento</Text>
                )}
                {beneficio.monto && (
                  <Text style={styles.benefitDetail}>{formatCurrency(beneficio.monto)}</Text>
                )}
              </View>
              <View style={[styles.benefitStatus, 
                { backgroundColor: beneficio.estado === 'ACTIVA' || beneficio.estado === 'ACTIVO' ? COLORS.success : COLORS.warning }
              ]}>
                <Text style={styles.benefitStatusText}>{beneficio.estado}</Text>
              </View>
            </View>
          ))}
        </Card>

        {/* Acciones Rápidas */}
        <Card style={styles.card}>
          <Text style={styles.cardTitle}>Servicios Estudiantiles</Text>
          
          <View style={styles.actionsGrid}>
            <TouchableOpacity style={styles.actionItem} onPress={handleRequestCertificate}>
              <Ionicons name="document-text-outline" size={24} color={COLORS.primary} />
              <Text style={styles.actionText}>Certificados</Text>
            </TouchableOpacity>
            
            <TouchableOpacity style={styles.actionItem} onPress={() => navigation.navigate('AcademicHistory')}>
              <Ionicons name="library-outline" size={24} color={COLORS.primary} />
              <Text style={styles.actionText}>Historial</Text>
            </TouchableOpacity>
            
            <TouchableOpacity style={styles.actionItem} onPress={() => navigation.navigate('UniversityCard')}>
              <Ionicons name="card-outline" size={24} color={COLORS.primary} />
              <Text style={styles.actionText}>Credencial</Text>
            </TouchableOpacity>
            
            <TouchableOpacity style={styles.actionItem} onPress={handleViewScholarships}>
              <Ionicons name="star-outline" size={24} color={COLORS.primary} />
              <Text style={styles.actionText}>Becas</Text>
            </TouchableOpacity>
          </View>
        </Card>

        {/* Información Personal */}
        <Card style={styles.card}>
          <View style={styles.cardHeader}>
            <Ionicons name="person-outline" size={24} color={COLORS.primary} />
            <Text style={styles.cardTitle}>Información Personal</Text>
          </View>
          
          <View style={styles.personalInfo}>
            <View style={styles.infoRow}>
              <Text style={styles.infoLabel}>Teléfono:</Text>
              <Text style={styles.infoValue}>{profileData.telefono}</Text>
            </View>
            
            <View style={styles.infoRow}>
              <Text style={styles.infoLabel}>Fecha de Nacimiento:</Text>
              <Text style={styles.infoValue}>
                {new Date(profileData.fechaNacimiento).toLocaleDateString('es-CL')}
              </Text>
            </View>
            
            <View style={styles.infoRow}>
              <Text style={styles.infoLabel}>Dirección:</Text>
              <Text style={styles.infoValue}>{profileData.direccion}</Text>
            </View>
            
            <View style={styles.infoRow}>
              <Text style={styles.infoLabel}>Región:</Text>
              <Text style={styles.infoValue}>{profileData.region}</Text>
            </View>
          </View>
        </Card>

        {/* Botón de Cerrar Sesión */}
        <TouchableOpacity style={styles.logoutButton} onPress={handleLogout}>
          <Ionicons name="log-out-outline" size={24} color={COLORS.white} />
          <Text style={styles.logoutText}>Cerrar Sesión</Text>
        </TouchableOpacity>
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  header: {
    backgroundColor: COLORS.primary,
    paddingTop: 50,
    paddingBottom: SPACING.xl,
  },
  headerContent: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: SPACING.lg,
  },
  avatarContainer: {
    width: 80,
    height: 80,
    borderRadius: 40,
    backgroundColor: 'rgba(255,255,255,0.2)',
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: SPACING.md,
  },
  avatarImage: {
    width: 80,
    height: 80,
    borderRadius: 40,
  },
  userInfo: {
    flex: 1,
  },
  userName: {
    fontSize: FONT_SIZE.lg,
    color: COLORS.white,
    fontWeight: FONT_WEIGHT.bold,
    marginBottom: 2,
  },
  userEmail: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.white,
    opacity: 0.9,
    marginBottom: 2,
  },
  userCareer: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.white,
    opacity: 0.8,
    marginBottom: 2,
  },
  userSede: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.white,
    opacity: 0.7,
  },
  editButton: {
    padding: SPACING.sm,
  },
  content: {
    padding: SPACING.md,
  },
  card: {
    marginBottom: SPACING.lg,
  },
  cardHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: SPACING.md,
  },
  cardTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginLeft: SPACING.sm,
    flex: 1,
  },
  viewMoreText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.primary,
    fontWeight: FONT_WEIGHT.medium,
  },
  infoGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
  },
  infoItem: {
    width: '48%',
    marginBottom: SPACING.md,
  },
  infoLabel: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
    marginBottom: 4,
  },
  infoValue: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    fontWeight: FONT_WEIGHT.medium,
  },
  statusBadge: {
    paddingHorizontal: SPACING.sm,
    paddingVertical: 4,
    borderRadius: BORDER_RADIUS.sm,
    alignSelf: 'flex-start',
  },
  statusText: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.white,
    fontWeight: FONT_WEIGHT.medium,
  },
  progressContainer: {
    marginBottom: SPACING.lg,
  },
  progressItem: {
    marginBottom: SPACING.md,
  },
  progressLabel: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
    marginBottom: 4,
  },
  progressRow: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  progressValue: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    minWidth: 80,
  },
  progressBar: {
    flex: 1,
    height: 8,
    backgroundColor: COLORS.borderLight,
    borderRadius: 4,
    marginLeft: SPACING.sm,
  },
  progressFill: {
    height: '100%',
    borderRadius: 4,
  },
  statsGrid: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    borderTopWidth: 1,
    borderTopColor: COLORS.borderLight,
    paddingTop: SPACING.md,
  },
  statItem: {
    alignItems: 'center',
  },
  statNumber: {
    fontSize: FONT_SIZE.xl,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.success,
  },
  statLabel: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.textSecondary,
    textAlign: 'center',
    marginTop: 4,
  },
  financialSummary: {
    padding: SPACING.md,
    backgroundColor: COLORS.primaryLight,
    borderRadius: BORDER_RADIUS.md,
  },
  financialItem: {
    alignItems: 'center',
    marginBottom: SPACING.sm,
  },
  financialLabel: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
  },
  financialDate: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    fontWeight: FONT_WEIGHT.medium,
  },
  financialAmount: {
    fontSize: FONT_SIZE.lg,
    color: COLORS.primary,
    fontWeight: FONT_WEIGHT.bold,
  },
  alertBanner: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: SPACING.sm,
    borderRadius: BORDER_RADIUS.sm,
    marginTop: SPACING.sm,
  },
  alertText: {
    fontSize: FONT_SIZE.sm,
    marginLeft: SPACING.sm,
    flex: 1,
  },
  benefitItem: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: SPACING.sm,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.borderLight,
  },
  benefitInfo: {
    flex: 1,
  },
  benefitName: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    fontWeight: FONT_WEIGHT.medium,
  },
  benefitDetail: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
    marginTop: 2,
  },
  benefitStatus: {
    paddingHorizontal: SPACING.sm,
    paddingVertical: 4,
    borderRadius: BORDER_RADIUS.sm,
  },
  benefitStatusText: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.white,
    fontWeight: FONT_WEIGHT.medium,
  },
  actionsGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
  },
  actionItem: {
    width: '22%',
    alignItems: 'center',
    paddingVertical: SPACING.md,
  },
  actionText: {
    fontSize: FONT_SIZE.xs,
    color: COLORS.text,
    textAlign: 'center',
    marginTop: SPACING.xs,
  },
  personalInfo: {},
  infoRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: SPACING.sm,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.borderLight,
  },
  logoutButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: COLORS.error,
    borderRadius: BORDER_RADIUS.md,
    padding: SPACING.md,
    marginVertical: SPACING.lg,
  },
  logoutText: {
    color: COLORS.white,
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
    marginLeft: SPACING.sm,
  },
});

export default ProfileScreen;
