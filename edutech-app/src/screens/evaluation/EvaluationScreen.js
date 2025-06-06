import React, { useState, useEffect, useContext, useCallback } from 'react';
import { View, Text, StyleSheet, ScrollView, SafeAreaView, TouchableOpacity, Alert } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT } from '../../config/theme';
import { Button, Card } from '../../components/common/UIComponents';
import { LoadingState, ErrorState } from '../../components/common/StateComponents';
import { AuthContext } from '../../context/AuthContext';
import { StudentApiService } from '../../services/studentApiService';

const EvaluationScreen = ({ route, navigation }) => {
  const { evaluationId, courseId, title = 'Evaluación' } = route.params || {};
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [answers, setAnswers] = useState({});
  const [timeRemaining, setTimeRemaining] = useState(1800); // 30 minutos en segundos (por defecto)
  const [isFinished, setIsFinished] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [evaluation, setEvaluation] = useState(null);
  const [submitting, setSubmitting] = useState(false);
  
  const { token } = useContext(AuthContext);
  
  // Cargar evaluación
  useEffect(() => {
    const loadEvaluation = async () => {
      setLoading(true);
      setError(null);
      
      try {
        const response = await StudentApiService.getEvaluation(token, evaluationId);
        if (response.success) {
          setEvaluation(response.data);
          setTimeRemaining(response.data.timeLimit * 60); // convertir minutos a segundos
        } else {
          throw new Error(response.message || 'No se encontró la evaluación');
        }
      } catch (error) {
        console.error('Error cargando evaluación:', error);
        setError(error.message || 'No se pudo cargar la evaluación');
      } finally {
        setLoading(false);
      }
    };
    
    loadEvaluation();
  }, [evaluationId, token]);
  
  const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  };
  
  const handleAnswer = (questionId, answer) => {
    setAnswers(prev => ({
      ...prev,
      [questionId]: answer
    }));
  };
  
  const handleNavigation = (direction) => {
    if (direction === 'next' && currentQuestion < evaluation.questions.length - 1) {
      setCurrentQuestion(currentQuestion + 1);
    } else if (direction === 'prev' && currentQuestion > 0) {
      setCurrentQuestion(currentQuestion - 1);
    }
  };
  
  const handleSubmit = async () => {
    Alert.alert(
      "Confirmar envío",
      "¿Estás seguro de que quieres enviar la evaluación?",
      [
        { text: "Cancelar", style: "cancel" },
        { text: "Enviar", onPress: () => submitEvaluation() }
      ]
    );
  };
  
  const submitEvaluation = async () => {
    setSubmitting(true);
    
    try {
      const response = await EvaluationService.submitEvaluation(token, evaluationId, answers);
      if (response.success) {
        setIsFinished(true);
        Alert.alert(
          "Evaluación Enviada",
          "Tu evaluación ha sido enviada exitosamente",
          [{ 
            text: "Ver Resultados", 
            onPress: () => navigation.replace('EvaluationResult', { 
              evaluationId, 
              score: response.data.score 
            })
          }]
        );
      } else {
        throw new Error(response.message || 'Error al enviar evaluación');
      }
    } catch (error) {
      Alert.alert("Error", error.message || "No se pudo enviar la evaluación");
    } finally {
      setSubmitting(false);
    }
  };

  const renderQuestion = () => {
    if (!evaluation || !evaluation.questions || evaluation.questions.length === 0) {
      return null;
    }

    const question = evaluation.questions[currentQuestion];
    
    return (
      <Card style={styles.questionCard}>
        <Text style={styles.questionNumber}>
          Pregunta {currentQuestion + 1} de {evaluation.questions.length}
        </Text>
        <Text style={styles.questionText}>{question.text}</Text>
        
        <View style={styles.optionsContainer}>
          {question.options.map((option, index) => (
            <TouchableOpacity
              key={index}
              style={[
                styles.optionButton,
                answers[question.id] === option && styles.selectedOption
              ]}
              onPress={() => handleAnswer(question.id, option)}
            >
              <Text style={[
                styles.optionText,
                answers[question.id] === option && styles.selectedOptionText
              ]}>
                {option}
              </Text>
            </TouchableOpacity>
          ))}
        </View>
      </Card>
    );
  };

  // Si está cargando, mostrar estado de carga
  if (loading) {
    return <LoadingState message="Cargando evaluación..." />;
  }

  // Si hay un error, mostrar estado de error
  if (error) {
    return <ErrorState message={error} onRetry={() => navigation.goBack()} />;
  }

  // Si no hay datos de evaluación, mostrar mensaje
  if (!evaluation) {
    return <ErrorState message="No se encontró la evaluación" onRetry={() => navigation.goBack()} />;
  }

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>{title}</Text>
        {evaluation.timeLimit && (
          <Text style={styles.timer}>{formatTime(timeRemaining)}</Text>
        )}
      </View>

      <ScrollView style={styles.content}>
        {renderQuestion()}
      </ScrollView>

      <View style={styles.navigation}>
        <Button
          title="Anterior"
          onPress={() => handleNavigation('prev')}
          disabled={currentQuestion === 0}
          variant="outlined"
        />
        
        {currentQuestion === evaluation.questions.length - 1 ? (
          <Button
            title={submitting ? "Enviando..." : "Finalizar"}
            onPress={() => handleSubmit()}
            disabled={submitting}
          />
        ) : (
          <Button
            title="Siguiente"
            onPress={() => handleNavigation('next')}
          />
        )}
      </View>
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
    padding: SPACING.lg,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  title: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.white,
    flex: 1,
  },
  timer: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.white,
    backgroundColor: 'rgba(255,255,255,0.2)',
    paddingHorizontal: SPACING.sm,
    paddingVertical: SPACING.xs,
    borderRadius: 4,
  },
  content: {
    flex: 1,
    padding: SPACING.md,
  },
  questionCard: {
    padding: SPACING.lg,
  },
  questionNumber: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
    marginBottom: SPACING.sm,
  },
  questionText: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.text,
    marginBottom: SPACING.lg,
  },
  optionsContainer: {
    gap: SPACING.sm,
  },
  optionButton: {
    padding: SPACING.md,
    borderWidth: 1,
    borderColor: COLORS.lightGray,
    borderRadius: 8,
    backgroundColor: COLORS.white,
  },
  selectedOption: {
    borderColor: COLORS.primary,
    backgroundColor: COLORS.primaryLight,
  },
  optionText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
  },
  selectedOptionText: {
    color: COLORS.primary,
    fontWeight: FONT_WEIGHT.medium,
  },
  navigation: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    padding: SPACING.md,
    backgroundColor: COLORS.white,
    borderTopWidth: 1,
    borderTopColor: COLORS.lightGray,
  },
});

export default EvaluationScreen;
