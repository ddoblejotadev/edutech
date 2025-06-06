import React, { useState, useEffect, useContext } from 'react';
import {
  View,
  Text,
  StyleSheet,
  FlatList,
  ActivityIndicator,
  RefreshControl,
  TouchableOpacity,
  Alert,
  ScrollView,
  SafeAreaView,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT } from '../../config/theme';
import { StudentApiService } from '../../services/studentApiService';
import { AuthContext } from '../../context/AuthContext';
import { DEMO_GRADES } from '../../data/demoData';

const GradesScreen = ({ navigation }) => {
  const [grades, setGrades] = useState([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [selectedPeriod, setSelectedPeriod] = useState('current');
  const [summary, setSummary] = useState(null);
  const [error, setError] = useState(null);
  const [networkError, setNetworkError] = useState(false);
  const [retryCount, setRetryCount] = useState(0);
  
  const { token } = useContext(AuthContext);

  useEffect(() => {
    loadGrades();
  }, [selectedPeriod]);

  const loadGrades = async (isRetry = false) => {
    try {
      if (!isRetry) {
        setLoading(true);
      }
      setError(null);
      setNetworkError(false);
      
      console.log('📊 Cargando calificaciones...');
      
      // Timeout para requests de red
      const timeoutPromise = new Promise((_, reject) =>
        setTimeout(() => reject(new Error('Timeout de conexión')), 10000)
      );
      
      const gradePromise = StudentApiService.getGrades(token);
      const response = await Promise.race([gradePromise, timeoutPromise]);
      
      if (response && response.success) {
        console.log('✅ Calificaciones cargadas exitosamente');
        setGrades(response.data?.grades || []);
        setSummary(response.data?.summary || {});
        setRetryCount(0);
      } else {
        throw new Error(response?.message || 'Respuesta inválida del servicio');
      }
    } catch (error) {
      console.error('❌ Error al cargar calificaciones:', error.message);
      
      // Detectar errores de red específicos
      const isNetworkIssue = error.message.includes('Network request failed') || 
                           error.message.includes('Timeout') ||
                           error.message.includes('fetch') ||
                           !navigator.onLine;
      
      setNetworkError(isNetworkIssue);
      
      if (isNetworkIssue) {
        setError('Sin conexión a internet. Mostrando datos offline.');
        console.log('🌐 Error de red detectado, usando datos demo');
      } else {
        setError('Error del servidor. Mostrando datos de demostración.');
        console.log('🔄 Error del servidor, usando datos demo como fallback');
      }
      
      // Usar datos demo como fallback
      setGrades(DEMO_GRADES?.grades || []);
      setSummary(DEMO_GRADES?.summary || {});
      setRetryCount(prev => prev + 1);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  const onRefresh = async () => {
    setRefreshing(true);
    await loadGrades();
  };

  const handleRetry = () => {
    if (retryCount < 3) {
      loadGrades(true);
    } else {
      Alert.alert(
        'Problemas de Conexión',
        'No se puede conectar al servidor. Verifica tu conexión a internet e intenta nuevamente más tarde.',
        [{ text: 'Entendido' }]
      );
    }
  };

  const getGradeColor = (grade) => {
    if (grade >= 6.0) return COLORS.success;  // Escala chilena
    if (grade >= 5.0) return COLORS.primary;
    if (grade >= 4.0) return COLORS.warning;
    return COLORS.error;
  };

  const getGradeLetter = (grade) => {
    if (grade >= 6.5) return 'MB'; // Muy Bueno
    if (grade >= 5.5) return 'B';  // Bueno
    if (grade >= 4.5) return 'S';  // Suficiente
    if (grade >= 4.0) return 'I';  // Insuficiente
    return 'M'; // Malo
  };

  const getGradeStatus = (grade) => {
    if (grade >= 4.0) return 'Aprobado';
    return 'Reprobado';
  };

  const filteredGrades = grades.filter(grade => {
    if (selectedPeriod === 'current') return grade.current;
    return !grade.current;
  });

  const periods = [
    { key: 'current', label: 'Actual', icon: 'calendar' },
    { key: 'history', label: 'Historial', icon: 'time' },
  ];

  const renderPeriodButton = (period) => (
    <TouchableOpacity
      key={period.key}
      style={[
        styles.periodButton,
        selectedPeriod === period.key && styles.periodButtonActive
      ]}
      onPress={() => setSelectedPeriod(period.key)}
    >
      <Ionicons
        name={period.icon}
        size={16}
        color={selectedPeriod === period.key ? COLORS.white : COLORS.primary}
      />
      <Text style={[
        styles.periodButtonText,
        selectedPeriod === period.key && styles.periodButtonTextActive
      ]}>
        {period.label}
      </Text>
    </TouchableOpacity>
  );

  const renderErrorState = () => (
    <View style={styles.errorContainer}>
      <Ionicons 
        name={networkError ? "wifi-outline" : "server-outline"} 
        size={48} 
        color={COLORS.error} 
      />
      <Text style={styles.errorTitle}>
        {networkError ? 'Sin Conexión' : 'Error del Servidor'}
      </Text>
      <Text style={styles.errorMessage}>{error}</Text>
      
      {retryCount < 3 && (
        <TouchableOpacity style={styles.retryButton} onPress={handleRetry}>
          <Ionicons name="refresh-outline" size={16} color={COLORS.white} />
          <Text style={styles.retryButtonText}>Reintentar</Text>
        </TouchableOpacity>
      )}
    </View>
  );

  const renderSummaryCard = () => {
    if (!summary || selectedPeriod !== 'current') return null;

    return (
      <View style={styles.summaryCard}>
        <Text style={styles.summaryTitle}>Resumen Académico</Text>
        <View style={styles.summaryGrid}>
          <View style={styles.summaryItem}>
            <Text style={styles.summaryValue}>{summary.gpa || '0.0'}</Text>
            <Text style={styles.summaryLabel}>PPA</Text>
          </View>
          <View style={styles.summaryItem}>
            <Text style={styles.summaryValue}>{summary.totalCredits || 0}</Text>
            <Text style={styles.summaryLabel}>Créditos</Text>
          </View>
          <View style={styles.summaryItem}>
            <Text style={styles.summaryValue}>{summary.completedCourses || 0}</Text>
            <Text style={styles.summaryLabel}>Ramos</Text>
          </View>
          <View style={styles.summaryItem}>
            <Text style={styles.summaryValue} numberOfLines={1} adjustsFontSizeToFit>
              {summary.currentSemester || 'N/A'}
            </Text>
            <Text style={styles.summaryLabel}>Semestre</Text>
          </View>
        </View>
      </View>
    );
  };

  const renderGradeCard = ({ item }) => (
    <TouchableOpacity style={styles.gradeCard}>
      <View style={styles.gradeHeader}>
        <View style={styles.courseInfo}>
          <Text style={styles.courseCode}>{item.courseCode}</Text>
          <Text style={styles.courseName} numberOfLines={2}>
            {item.courseName}
          </Text>
          {item.semester && (
            <Text style={styles.semester}>{item.semester}</Text>
          )}
        </View>
        
        <View style={styles.gradeContainer}>
          <View style={[styles.gradeBadge, { backgroundColor: getGradeColor(item.grade) }]}>
            <Text style={styles.gradeNumber}>{item.grade}</Text>
            <Text style={styles.gradeLetter}>{getGradeLetter(item.grade)}</Text>
          </View>
        </View>
      </View>

      <View style={styles.gradeDetails}>
        <View style={styles.detailRow}>
          <Text style={styles.detailLabel}>Créditos:</Text>
          <Text style={styles.detailValue}>{item.credits}</Text>
        </View>
        
        <View style={styles.detailRow}>
          <Text style={styles.detailLabel}>Estado:</Text>
          <Text style={[
            styles.detailValue,
            { color: item.grade >= 4.0 ? COLORS.success : COLORS.error }
          ]}>
            {getGradeStatus(item.grade)}
          </Text>
        </View>
      </View>
    </TouchableOpacity>
  );

  if (loading && !refreshing) {
    return (
      <SafeAreaView style={styles.container}>
        <View style={styles.loadingContainer}>
          <ActivityIndicator size="large" color={COLORS.primary} />
          <Text style={styles.loadingText}>Cargando calificaciones...</Text>
        </View>
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      {/* Header con navegación */}
      <View style={styles.header}>
        <TouchableOpacity 
          onPress={() => navigation?.goBack()}
          style={styles.backButton}
        >
          <Ionicons name="arrow-back" size={24} color={COLORS.text} />
        </TouchableOpacity>
        <Text style={styles.headerTitle}>Calificaciones</Text>
        <View style={styles.placeholder} />
      </View>

      {/* Banner de estado */}
      {(error || networkError) && (
        <View style={[styles.statusBanner, { 
          backgroundColor: networkError ? `${COLORS.error}15` : `${COLORS.warning}15` 
        }]}>
          <Ionicons 
            name={networkError ? "wifi-outline" : "information-circle-outline"} 
            size={16} 
            color={networkError ? COLORS.error : COLORS.warning} 
          />
          <Text style={[styles.statusText, { 
            color: networkError ? COLORS.error : COLORS.warning 
          }]}>
            {error}
          </Text>
          {networkError && retryCount < 3 && (
            <TouchableOpacity onPress={handleRetry} style={styles.miniRetryButton}>
              <Ionicons name="refresh-outline" size={14} color={COLORS.error} />
            </TouchableOpacity>
          )}
        </View>
      )}

      {/* Selector de período */}
      <View style={styles.periodSelector}>
        {periods.map(renderPeriodButton)}
      </View>

      {/* Contenido principal */}
      <ScrollView
        style={styles.content}
        refreshControl={
          <RefreshControl
            refreshing={refreshing}
            onRefresh={onRefresh}
            colors={[COLORS.primary]}
            tintColor={COLORS.primary}
          />
        }
      >
        {renderSummaryCard()}

        {/* Lista de calificaciones */}
        {filteredGrades.length > 0 ? (
          <View style={styles.gradesList}>
            {filteredGrades.map((item) => (
              <View key={item.id}>
                {renderGradeCard({ item })}
              </View>
            ))}
          </View>
        ) : (
          <View style={styles.emptyContainer}>
            <Ionicons name="school-outline" size={64} color={COLORS.textSecondary} />
            <Text style={styles.emptyTitle}>No hay calificaciones</Text>
            <Text style={styles.emptySubtitle}>
              {selectedPeriod === 'current' 
                ? 'Las calificaciones del período actual aparecerán aquí'
                : 'No hay historial académico disponible'
              }
            </Text>
          </View>
        )}
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
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: SPACING.lg,
    paddingVertical: SPACING.md,
    backgroundColor: COLORS.white,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
    elevation: 2,
    shadowColor: COLORS.black,
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
  },
  backButton: {
    padding: SPACING.sm,
    borderRadius: 8,
  },
  headerTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
  },
  placeholder: {
    width: 40,
  },
  statusBanner: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: SPACING.md,
    paddingVertical: SPACING.sm,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  statusText: {
    fontSize: FONT_SIZE.sm,
    marginLeft: SPACING.xs,
    flex: 1,
  },
  miniRetryButton: {
    padding: SPACING.xs,
    borderRadius: 4,
  },
  errorContainer: {
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: SPACING.xl * 2,
    paddingHorizontal: SPACING.lg,
  },
  errorTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.error,
    marginTop: SPACING.md,
    marginBottom: SPACING.sm,
    textAlign: 'center',
  },
  errorMessage: {
    fontSize: FONT_SIZE.md,
    color: COLORS.textSecondary,
    textAlign: 'center',
    marginBottom: SPACING.lg,
    lineHeight: FONT_SIZE.md * 1.4,
  },
  retryButton: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: COLORS.primary,
    paddingHorizontal: SPACING.lg,
    paddingVertical: SPACING.md,
    borderRadius: 8,
  },
  retryButtonText: {
    color: COLORS.white,
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
    marginLeft: SPACING.sm,
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: COLORS.background,
  },
  loadingText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    marginTop: SPACING.md,
  },
  periodSelector: {
    flexDirection: 'row',
    paddingHorizontal: SPACING.md,
    paddingVertical: SPACING.md,
    backgroundColor: COLORS.white,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  periodButton: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: SPACING.md,
    paddingVertical: SPACING.sm,
    borderRadius: 20,
    borderWidth: 1,
    borderColor: COLORS.primary,
    marginRight: SPACING.sm,
    backgroundColor: COLORS.white,
  },
  periodButtonActive: {
    backgroundColor: COLORS.primary,
  },
  periodButtonText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.primary,
    marginLeft: SPACING.xs,
    fontWeight: FONT_WEIGHT.medium,
  },
  periodButtonTextActive: {
    color: COLORS.white,
  },
  content: {
    flex: 1,
  },
  summaryCard: {
    backgroundColor: COLORS.white,
    margin: SPACING.md,
    borderRadius: 12,
    padding: SPACING.lg,
    shadowColor: COLORS.black,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 3.84,
    elevation: 5,
  },
  summaryTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.lg,
    textAlign: 'center',
  },
  summaryGrid: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  summaryItem: {
    alignItems: 'center',
    flex: 1,
    paddingHorizontal: SPACING.xs,
  },
  summaryValue: {
    fontSize: FONT_SIZE.xl,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.primary,
    marginBottom: SPACING.xs,
    textAlign: 'center',
  },
  summaryLabel: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
    textAlign: 'center',
    fontWeight: FONT_WEIGHT.medium,
  },
  gradesList: {
    paddingHorizontal: SPACING.md,
    paddingBottom: SPACING.xl,
  },
  gradeCard: {
    backgroundColor: COLORS.white,
    borderRadius: 12,
    padding: SPACING.lg,
    marginBottom: SPACING.md,
    shadowColor: COLORS.black,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 3.84,
    elevation: 5,
  },
  gradeHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    marginBottom: SPACING.md,
  },
  courseInfo: {
    flex: 1,
    marginRight: SPACING.md,
  },
  courseCode: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.primary,
    marginBottom: SPACING.xs,
  },
  courseName: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    marginBottom: SPACING.xs,
    lineHeight: FONT_SIZE.md * 1.3,
    fontWeight: FONT_WEIGHT.medium,
  },
  semester: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
  },
  gradeContainer: {
    alignItems: 'center',
  },
  gradeBadge: {
    borderRadius: 10,
    padding: SPACING.md,
    alignItems: 'center',
    minWidth: 65,
    minHeight: 65,
    justifyContent: 'center',
  },
  gradeNumber: {
    fontSize: FONT_SIZE.xl,
    color: COLORS.white,
    fontWeight: FONT_WEIGHT.bold,
  },
  gradeLetter: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.white,
    marginTop: 2,
    fontWeight: FONT_WEIGHT.medium,
  },
  gradeDetails: {
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
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
    fontWeight: FONT_WEIGHT.medium,
  },
  detailValue: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
    fontWeight: FONT_WEIGHT.bold,
  },
  emptyContainer: {
    alignItems: 'center',
    paddingVertical: SPACING.xl * 2,
    paddingHorizontal: SPACING.lg,
  },
  emptyTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginTop: SPACING.lg,
    marginBottom: SPACING.md,
    textAlign: 'center',
  },
  emptySubtitle: {
    fontSize: FONT_SIZE.md,
    color: COLORS.textSecondary,
    textAlign: 'center',
    lineHeight: FONT_SIZE.md * 1.4,
  },
});

export default GradesScreen;
