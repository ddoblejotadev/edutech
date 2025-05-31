import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  FlatList,
  ActivityIndicator,
  RefreshControl,
  TouchableOpacity,
  Alert,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT } from '../../config/theme';
import { StudentApiService } from '../../services/studentApiService';

const GradesScreen = () => {
  const [grades, setGrades] = useState([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [selectedPeriod, setSelectedPeriod] = useState('current');
  const [summary, setSummary] = useState(null);

  useEffect(() => {
    loadGrades();
  }, [selectedPeriod]);

  const loadGrades = async () => {
    try {
      setLoading(true);
      const data = await StudentApiService.getGrades();
      setGrades(data.grades || []);
      setSummary(data.summary || {});
    } catch (error) {
      Alert.alert('Error', 'No se pudieron cargar las calificaciones');
    } finally {
      setLoading(false);
    }
  };

  const onRefresh = async () => {
    setRefreshing(true);
    await loadGrades();
    setRefreshing(false);
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
    { key: 'current', label: 'Periodo Actual', icon: 'calendar' },
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
        size={18}
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

  const renderSummaryCard = () => {
    if (!summary || selectedPeriod !== 'current') return null;

    return (
      <View style={styles.summaryCard}>
        <Text style={styles.summaryTitle}>Resumen Académico</Text>
        <View style={styles.summaryGrid}>
          <View style={styles.summaryItem}>
            <Text style={styles.summaryValue}>{summary.gpa || '0.0'}</Text>
            <Text style={styles.summaryLabel}>PPA (1-7)</Text>
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
            <Text style={styles.summaryValue}>{summary.currentSemester || 'N/A'}</Text>
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
            { color: item.grade >= 70 ? COLORS.success : COLORS.error }
          ]}>
            {getGradeStatus(item.grade)}
          </Text>
        </View>
      </View>
    </TouchableOpacity>
  );

  if (loading && !refreshing) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color={COLORS.primary} />
        <Text style={styles.loadingText}>Cargando calificaciones...</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <View style={styles.periodSelector}>
        {periods.map(renderPeriodButton)}
      </View>

      {renderSummaryCard()}

      <FlatList
        data={filteredGrades}
        keyExtractor={(item) => item.id.toString()}
        renderItem={renderGradeCard}
        contentContainerStyle={styles.gradesList}
        refreshControl={
          <RefreshControl
            refreshing={refreshing}
            onRefresh={onRefresh}
            colors={[COLORS.primary]}
            tintColor={COLORS.primary}
          />
        }
        ListEmptyComponent={
          <View style={styles.emptyContainer}>
            <Ionicons name="school-outline" size={64} color={COLORS.lightText} />
            <Text style={styles.emptyTitle}>No hay calificaciones</Text>
            <Text style={styles.emptySubtitle}>
              {selectedPeriod === 'current' 
                ? 'Las calificaciones del período actual aparecerán aquí'
                : 'No hay historial académico disponible'
              }
            </Text>
          </View>
        }
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.background,
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
    padding: SPACING.md,
    backgroundColor: COLORS.white,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.border,
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
  },
  periodButtonTextActive: {
    color: COLORS.white,
  },
  summaryCard: {
    backgroundColor: COLORS.white,
    margin: SPACING.md,
    borderRadius: 12,
    padding: SPACING.md,
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
    marginBottom: SPACING.md,
  },
  summaryGrid: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  summaryItem: {
    alignItems: 'center',
    flex: 1,
  },
  summaryValue: {
    fontSize: FONT_SIZE.xl,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.primary,
    marginBottom: SPACING.xs,
  },
  summaryLabel: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.lightText,
  },
  gradesList: {
    padding: SPACING.md,
  },
  gradeCard: {
    backgroundColor: COLORS.white,
    borderRadius: 12,
    padding: SPACING.md,
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
  },
  semester: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.lightText,
  },
  gradeContainer: {
    alignItems: 'center',
  },
  gradeBadge: {
    borderRadius: 8,
    padding: SPACING.sm,
    alignItems: 'center',
    minWidth: 60,
  },
  gradeNumber: {
    fontSize: FONT_SIZE.lg,
    color: COLORS.white,
    fontWeight: FONT_WEIGHT.bold,
  },
  gradeLetter: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.white,
  },
  gradeDetails: {
    borderTopWidth: 1,
    borderTopColor: COLORS.border,
    paddingTop: SPACING.md,
  },
  detailRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: SPACING.sm,
  },
  detailLabel: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.lightText,
  },
  detailValue: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
    fontWeight: FONT_WEIGHT.medium,
  },
  emptyContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingTop: SPACING.xl * 2,
  },
  emptyTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginTop: SPACING.md,
    marginBottom: SPACING.sm,
  },
  emptySubtitle: {
    fontSize: FONT_SIZE.md,
    color: COLORS.lightText,
    textAlign: 'center',
    paddingHorizontal: SPACING.xl,
  },
});

export default GradesScreen;
