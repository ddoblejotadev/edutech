import React, { useState, useEffect, useContext } from 'react';
import { View, Text, StyleSheet, ScrollView, SafeAreaView, TouchableOpacity } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT } from '../../config/theme';
import { Button, Card } from '../../components/common/UIComponents';
import { LoadingState, ErrorState } from '../../components/common/StateComponents';
import { AuthContext } from '../../context/AuthContext';
import { EvaluationService } from '../../services/studentApiService';

const EvaluationResultScreen = ({ route, navigation }) => {
  const { evaluationId, courseId } = route.params || {};
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [result, setResult] = useState(null);
  
  const { token } = useContext(AuthContext);
  
  // Cargar resultados de la evaluación
  useEffect(() => {
    const loadEvaluationResult = async () => {
      setLoading(true);
      setError(null);
      
      try {
        if (token) {
          const response = await EvaluationService.getEvaluationResult(token, evaluationId);
          if (response.success) {
            setResult(response.data);
          } else {
            throw new Error(response.message || 'Error al cargar resultado');
          }
        } else {
          // Datos simulados para demostración
          setResult({
            id: evaluationId,
            title: 'Evaluación de Conocimientos',
            score: 85,
            totalQuestions: 10,
            correctAnswers: 8,
            passingScore: 70,
            passed: true,
            completedAt: new Date().toLocaleDateString(),
            timeSpent: '15:30',
            feedback: 'Excelente trabajo. Has demostrado un buen dominio de los conceptos.',
            answers: [
              { questionId: 1, question: '¿Qué es React?', userAnswer: 'Una librería de JavaScript', isCorrect: true },
              { questionId: 2, question: '¿Cuál es la sintaxis JSX?', userAnswer: 'HTML en JavaScript', isCorrect: true },
              // Más respuestas...
            ]
          });
        }
      } catch (error) {
        console.error('Error cargando resultado de evaluación:', error);
        setError(error.message || 'No se pudieron cargar los resultados');
      } finally {
        setLoading(false);
      }
    };
    
    loadEvaluationResult();
  }, [evaluationId, token]);
  
  const getScoreColor = (score, passingScore) => {
    if (score >= passingScore) {
      return COLORS.success;
    } else if (score >= passingScore * 0.7) {
      return COLORS.warning;
    } else {
      return COLORS.error;
    }
  };
  
  const getScoreMessage = (score, passingScore) => {
    if (score >= passingScore) {
      return '¡Felicitaciones! Has aprobado la evaluación.';
    } else {
      return 'No has alcanzado la puntuación mínima. Te recomendamos revisar el material y volver a intentarlo.';
    }
  };
  
  // Si está cargando, mostrar pantalla de carga
  if (loading) {
    return <LoadingState message="Cargando resultados..." />;
  }
  
  // Si hay un error, mostrar pantalla de error
  if (error) {
    return <ErrorState message={error} onRetry={() => navigation.goBack()} />;
  }
  
  // Si no hay resultado, mostrar mensaje
  if (!result) {
    return <ErrorState message="No se encontraron resultados para esta evaluación" onRetry={() => navigation.goBack()} />;
  }
  
  return (
    <SafeAreaView style={styles.container}>
      {/* Header */}
      <View style={styles.header}>
        <TouchableOpacity 
          style={styles.backButton}
          onPress={() => navigation.navigate('CourseDetail', { courseId })}
        >
          <Ionicons name="arrow-back" size={24} color={COLORS.text} />
        </TouchableOpacity>
        
        <Text style={styles.headerTitle}>Resultados de Evaluación</Text>
        
        <TouchableOpacity 
          style={styles.shareButton}
          onPress={() => {
            // Lógica para compartir resultados
          }}
        >
          <Ionicons name="share-outline" size={24} color={COLORS.primary} />
        </TouchableOpacity>
      </View>
      
      <ScrollView style={styles.content} showsVerticalScrollIndicator={false}>
        {/* Tarjeta de puntuación principal */}
        <Card style={styles.scoreCard}>
          <View style={styles.scoreHeader}>
            <View style={[
              styles.scoreIcon,
              { backgroundColor: getScoreColor(result.score, result.passingScore) }
            ]}>
              <Ionicons 
                name={result.passed ? "trophy" : "close-circle"} 
                size={40} 
                color={COLORS.white} 
              />
            </View>
            
            <View style={styles.scoreInfo}>
              <Text style={styles.evaluationTitle}>{result.title}</Text>
              <Text style={[
                styles.scoreText,
                { color: getScoreColor(result.score, result.passingScore) }
              ]}>
                {result.score}%
              </Text>
              <Text style={styles.scoreSubtext}>
                {result.correctAnswers} de {result.totalQuestions} respuestas correctas
              </Text>
            </View>
          </View>
          
          <Text style={styles.scoreMessage}>
            {getScoreMessage(result.score, result.passingScore)}
          </Text>
        </Card>
        
        {/* Detalles de la evaluación */}
        <Card style={styles.detailsCard}>
          <Text style={styles.cardTitle}>Detalles de la Evaluación</Text>
          
          <View style={styles.detailsGrid}>
            <View style={styles.detailItem}>
              <Ionicons name="time-outline" size={20} color={COLORS.primary} />
              <Text style={styles.detailLabel}>Tiempo empleado</Text>
              <Text style={styles.detailValue}>{result.timeSpent}</Text>
            </View>
            
            <View style={styles.detailItem}>
              <Ionicons name="calendar-outline" size={20} color={COLORS.primary} />
              <Text style={styles.detailLabel}>Fecha de finalización</Text>
              <Text style={styles.detailValue}>{result.completedAt}</Text>
            </View>
            
            <View style={styles.detailItem}>
              <Ionicons name="trophy-outline" size={20} color={COLORS.primary} />
              <Text style={styles.detailLabel}>Puntuación mínima</Text>
              <Text style={styles.detailValue}>{result.passingScore}%</Text>
            </View>
            
            <View style={styles.detailItem}>
              <Ionicons name="checkmark-circle-outline" size={20} color={COLORS.primary} />
              <Text style={styles.detailLabel}>Estado</Text>
              <Text style={[
                styles.detailValue,
                { color: result.passed ? COLORS.success : COLORS.error }
              ]}>
                {result.passed ? 'Aprobado' : 'No aprobado'}
              </Text>
            </View>
          </View>
        </Card>
        
        {/* Retroalimentación */}
        {result.feedback && (
          <Card style={styles.feedbackCard}>
            <Text style={styles.cardTitle}>Retroalimentación</Text>
            <Text style={styles.feedbackText}>{result.feedback}</Text>
          </Card>
        )}
        
        {/* Acciones */}
        <View style={styles.actionsContainer}>
          <Button
            title="Volver al curso"
            onPress={() => navigation.navigate('CourseDetail', { courseId })}
            style={styles.primaryButton}
          />
          
          {!result.passed && (
            <Button
              title="Volver a intentar"
              onPress={() => navigation.navigate('Evaluation', { 
                evaluationId, 
                courseId,
                title: result.title 
              })}
              variant="outlined"
              style={styles.secondaryButton}
            />
          )}
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
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
    backgroundColor: COLORS.white,
  },
  backButton: {
    padding: SPACING.xs,
  },
  headerTitle: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    flex: 1,
    textAlign: 'center',
  },
  shareButton: {
    padding: SPACING.xs,
  },
  content: {
    flex: 1,
    padding: SPACING.md,
  },
  scoreCard: {
    padding: SPACING.lg,
    marginBottom: SPACING.md,
    alignItems: 'center',
  },
  scoreHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: SPACING.md,
  },
  scoreIcon: {
    width: 80,
    height: 80,
    borderRadius: 40,
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: SPACING.md,
  },
  scoreInfo: {
    flex: 1,
  },
  evaluationTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  scoreText: {
    fontSize: 32,
    fontWeight: FONT_WEIGHT.bold,
    marginBottom: SPACING.xs,
  },
  scoreSubtext: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
  },
  scoreMessage: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    textAlign: 'center',
    lineHeight: 22,
  },
  detailsCard: {
    padding: SPACING.md,
    marginBottom: SPACING.md,
  },
  cardTitle: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.md,
  },
  detailsGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
  },
  detailItem: {
    width: '48%',
    alignItems: 'center',
    padding: SPACING.sm,
    marginBottom: SPACING.md,
  },
  detailLabel: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
    textAlign: 'center',
    marginTop: SPACING.xs,
    marginBottom: SPACING.xs,
  },
  detailValue: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.text,
    textAlign: 'center',
  },
  feedbackCard: {
    padding: SPACING.md,
    marginBottom: SPACING.md,
  },
  feedbackText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    lineHeight: 22,
  },
  actionsContainer: {
    marginTop: SPACING.md,
    marginBottom: SPACING.xl,
  },
  primaryButton: {
    marginBottom: SPACING.md,
  },
  secondaryButton: {
    marginBottom: SPACING.md,
  },
});

export default EvaluationResultScreen;
