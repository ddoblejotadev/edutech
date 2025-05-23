import React, { useState, useEffect, useContext, useCallback } from 'react';
import { View, Text, StyleSheet, ScrollView, SafeAreaView, TouchableOpacity, Alert } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT, SHADOWS } from '../../config/theme';
import { Button, Card } from '../../components/common/UIComponents';
import { LoadingState, ErrorState } from '../../components/common/StateComponents';
import { AuthContext } from '../../context/AuthContext';
import { Evaluations } from '../../services/apiService';

const EvaluationScreen = ({ route, navigation }) => {
  const { evaluationId, courseId, title } = route.params;
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
        const data = await Evaluations.get(token, evaluationId);
        setEvaluation(data);
        
        // Inicializar el temporizador si la evaluación tiene límite de tiempo
        if (data.timeLimit) {
          setTimeRemaining(data.timeLimit * 60); // Convertir minutos a segundos
        }
        
        // Inicializar el objeto de respuestas
        const initialAnswers = {};
        data.questions.forEach(question => {
          if (question.type === 'checkbox') {
            initialAnswers[question.id] = [];
          } else {
            initialAnswers[question.id] = null;
          }
        });
        setAnswers(initialAnswers);
        
      } catch (error) {
        console.error('Error cargando evaluación:', error);
        setError(error.message || 'No se pudo cargar la evaluación');
      } finally {
        setLoading(false);
      }
    };
    
    loadEvaluation();
  }, [evaluationId, token]);
  
  // Manejar el temporizador
  useEffect(() => {
    if (!loading && evaluation && evaluation.timeLimit && timeRemaining > 0 && !isFinished) {
      const timer = setInterval(() => {
        setTimeRemaining(prev => {
          if (prev <= 1) {
            clearInterval(timer);
            handleSubmit(true); // Enviar automáticamente al acabarse el tiempo
            return 0;
          }
          return prev - 1;
        });
      }, 1000);
      
      return () => clearInterval(timer);
    }
  }, [loading, evaluation, isFinished, timeRemaining]);
  
  const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  };
  
  const handleAnswer = (questionId, answer) => {
    setAnswers(prev => {
      const newAnswers = { ...prev };
      
      // Manejar preguntas de checkbox (selección múltiple)
      if (evaluation.questions[currentQuestion].type === 'checkbox') {
        if (newAnswers[questionId].includes(answer)) {
          // Si ya está seleccionada, quitarla
          newAnswers[questionId] = newAnswers[questionId].filter(item => item !== answer);
        } else {
          // Si no está seleccionada, agregarla
          newAnswers[questionId] = [...newAnswers[questionId], answer];
        }
      } else {
        // Para preguntas de opción única o verdadero/falso
        newAnswers[questionId] = answer;
      }
      
      return newAnswers;
    });
  };
  
  const handleNavigation = (direction) => {
    if (direction === 'next' && currentQuestion < evaluation.questions.length - 1) {
      setCurrentQuestion(currentQuestion + 1);
    } else if (direction === 'prev' && currentQuestion > 0) {
      setCurrentQuestion(currentQuestion - 1);
    }
  };
  
  const handleSubmit = async (isTimeout = false) => {
    // Verificar si todas las preguntas tienen respuesta
    const unansweredQuestions = Object.entries(answers).filter(([_, answer]) => {
      return answer === null || (Array.isArray(answer) && answer.length === 0);
    }).length;
    
    if (unansweredQuestions > 0 && !isTimeout) {
      Alert.alert(
        "Preguntas sin responder",
        `Aún hay ${unansweredQuestions} pregunta(s) sin responder. ¿Deseas enviar la evaluación de todos modos?`,
        [
          { text: "Cancelar", style: "cancel" },
          { text: "Enviar", onPress: () => submitEvaluation() }
        ]
      );
    } else {
      if (isTimeout) {
        Alert.alert(
          "Tiempo agotado",
          "El tiempo para completar la evaluación ha terminado. Tus respuestas serán enviadas automáticamente.",
          [{ text: "OK", onPress: () => submitEvaluation() }]
        );
      } else {
        Alert.alert(
          "Confirmar envío",
          "¿Estás seguro de que deseas enviar tus respuestas? No podrás modificarlas después.",
          [
            { text: "Cancelar", style: "cancel" },
            { text: "Enviar", onPress: () => submitEvaluation() }
          ]
        );
      }
    }
  };
  
  const submitEvaluation = async () => {
    setSubmitting(true);
    
    try {
      const result = await Evaluations.submit(token, evaluationId, answers);
      setIsFinished(true);
      
      // Mostrar resultado
      Alert.alert(
        "Evaluación Enviada",
        `Tu calificación es: ${result.score}/${result.totalScore} (${result.percentage}%)`,
        [
          { 
            text: "Ver detalles", 
            onPress: () => {
              // Aquí podría ir la navegación a una pantalla de resultados detallados
            } 
          },
          { 
            text: "Volver al curso", 
            onPress: () => navigation.navigate('CourseDetail', { courseId }) 
          }
        ]
      );
    } catch (error) {
      Alert.alert(
        "Error",
        error.message || "No se pudo enviar la evaluación. Inténtalo nuevamente.",
        [{ text: "OK" }]
      );
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
        
        {question.type === 'multiple' && renderMultipleChoice(question)}
        {question.type === 'checkbox' && renderCheckboxes(question)}
        {question.type === 'truefalse' && renderTrueFalse(question)}
      </Card>
    );
  };
  
  const renderMultipleChoice = (question) => {
    return (
      <View style={styles.optionsContainer}>
        {question.options.map(option => (
          <TouchableOpacity
            key={option.id}
            style={[
              styles.optionItem,
              answers[question.id] === option.id && styles.selectedOption
            ]}
            onPress={() => handleAnswer(question.id, option.id)}
            disabled={isFinished}
          >
            <View style={[
              styles.radioButton,
              answers[question.id] === option.id && styles.radioButtonSelected
            ]}>
              {answers[question.id] === option.id && (
                <View style={styles.radioButtonInner} />
              )}
            </View>
            
            <Text style={styles.optionText}>{option.text}</Text>
          </TouchableOpacity>
        ))}
      </View>
    );
  };
  
  const renderCheckboxes = (question) => {
    return (
      <View style={styles.optionsContainer}>
        {question.options.map(option => (
          <TouchableOpacity
            key={option.id}
            style={[
              styles.optionItem,
              answers[question.id]?.includes(option.id) && styles.selectedOption
            ]}
            onPress={() => handleAnswer(question.id, option.id)}
            disabled={isFinished}
          >
            <View style={[
              styles.checkbox,
              answers[question.id]?.includes(option.id) && styles.checkboxSelected
            ]}>
              {answers[question.id]?.includes(option.id) && (
                <Ionicons name="checkmark" size={16} color={COLORS.white} />
              )}
            </View>
            
            <Text style={styles.optionText}>{option.text}</Text>
          </TouchableOpacity>
        ))}
      </View>
    );
  };
  
  const renderTrueFalse = (question) => {
    return (
      <View style={styles.optionsContainer}>
        <TouchableOpacity
          style={[
            styles.optionItem,
            answers[question.id] === true && styles.selectedOption
          ]}
          onPress={() => handleAnswer(question.id, true)}
          disabled={isFinished}
        >
          <View style={[
            styles.radioButton,
            answers[question.id] === true && styles.radioButtonSelected
          ]}>
            {answers[question.id] === true && (
              <View style={styles.radioButtonInner} />
            )}
          </View>
          
          <Text style={styles.optionText}>Verdadero</Text>
        </TouchableOpacity>
        
        <TouchableOpacity
          style={[
            styles.optionItem,
            answers[question.id] === false && styles.selectedOption
          ]}
          onPress={() => handleAnswer(question.id, false)}
          disabled={isFinished}
        >
          <View style={[
            styles.radioButton,
            answers[question.id] === false && styles.radioButtonSelected
          ]}>
            {answers[question.id] === false && (
              <View style={styles.radioButtonInner} />
            )}
          </View>
          
          <Text style={styles.optionText}>Falso</Text>
        </TouchableOpacity>
      </View>
    );
  };
  
  const renderNavigation = () => {
    return (
      <View style={styles.navigationContainer}>
        <Button
          title="Anterior"
          onPress={() => handleNavigation('prev')}
          disabled={currentQuestion === 0 || isFinished}
          style={[styles.navigationButton, styles.previousButton]}
          variant="outlined"
        />
        
        {currentQuestion < evaluation.questions.length - 1 ? (
          <Button
            title="Siguiente"
            onPress={() => handleNavigation('next')}
            disabled={isFinished}
            style={[styles.navigationButton, styles.nextButton]}
          />
        ) : (
          <Button
            title={submitting ? "Enviando..." : "Enviar evaluación"}
            onPress={() => handleSubmit()}
            disabled={submitting || isFinished}
            style={[styles.navigationButton, styles.submitButton]}
          />
        )}
      </View>
    );
  };
  
  const renderProgressIndicator = () => {
    return (
      <View style={styles.progressContainer}>
        {evaluation.questions.map((_, index) => (
          <TouchableOpacity 
            key={index}
            style={[
              styles.progressDot,
              currentQuestion === index && styles.progressDotActive,
              answers[evaluation.questions[index].id] !== null && 
              !(Array.isArray(answers[evaluation.questions[index].id]) && 
                answers[evaluation.questions[index].id].length === 0) && 
              styles.progressDotAnswered
            ]}
            onPress={() => setCurrentQuestion(index)}
            disabled={isFinished}
          />
        ))}
      </View>
    );
  };
  
  // Si está cargando, mostrar pantalla de carga
  if (loading) {
    return <LoadingState message="Cargando evaluación..." />;
  }
  
  // Si hay un error, mostrar pantalla de error
  if (error) {
    return <ErrorState message={error} onRetry={() => navigation.goBack()} />;
  }
  
  // Si no hay evaluación, mostrar mensaje
  if (!evaluation) {
    return <ErrorState message="No se encontró la evaluación" onRetry={() => navigation.goBack()} />;
  }
  
  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity 
          style={styles.backButton}
          onPress={() => {
            if (isFinished) {
              navigation.goBack();
            } else {
              Alert.alert(
                "Salir de la evaluación",
                "Si sales ahora, perderás tu progreso en esta evaluación. ¿Estás seguro?",
                [
                  { text: "Cancelar", style: "cancel" },
                  { text: "Salir", onPress: () => navigation.goBack() }
                ]
              );
            }
          }}
        >
          <Ionicons name="arrow-back" size={24} color={COLORS.text} />
        </TouchableOpacity>
        
        <Text style={styles.headerTitle}>{title}</Text>
        
        {evaluation.timeLimit > 0 && (
          <View style={[
            styles.timerContainer, 
            timeRemaining < 300 && styles.timerWarning // Menos de 5 minutos
          ]}>
            <Ionicons name="time-outline" size={18} color={timeRemaining < 300 ? COLORS.white : COLORS.text} />
            <Text style={[
              styles.timerText,
              timeRemaining < 300 && styles.timerTextWarning
            ]}>
              {formatTime(timeRemaining)}
            </Text>
          </View>
        )}
      </View>
      
      {isFinished ? (
        <View style={styles.finishedContainer}>
          <Ionicons name="checkmark-circle" size={80} color={COLORS.success} />
          <Text style={styles.finishedTitle}>¡Evaluación completada!</Text>
          <Text style={styles.finishedMessage}>Tus respuestas han sido enviadas correctamente.</Text>
          <Button
            title="Volver al curso"
            onPress={() => navigation.navigate('CourseDetail', { courseId })}
            style={styles.finishedButton}
          />
        </View>
      ) : (
        <ScrollView style={styles.content}>
          <View style={styles.evaluationInfo}>
            <Text style={styles.evaluationTitle}>{evaluation.title}</Text>
            <Text style={styles.evaluationDescription}>{evaluation.description}</Text>
            
            <View style={styles.evaluationMeta}>
              <View style={styles.metaItem}>
                <Ionicons name="help-circle-outline" size={18} color={COLORS.primary} />
                <Text style={styles.metaText}>{evaluation.totalQuestions} preguntas</Text>
              </View>
              
              {evaluation.passingScore && (
                <View style={styles.metaItem}>
                  <Ionicons name="trophy-outline" size={18} color={COLORS.primary} />
                  <Text style={styles.metaText}>Aprobado: {evaluation.passingScore}%</Text>
                </View>
              )}
              
              {evaluation.timeLimit > 0 && (
                <View style={styles.metaItem}>
                  <Ionicons name="time-outline" size={18} color={COLORS.primary} />
                  <Text style={styles.metaText}>Tiempo: {evaluation.timeLimit} min</Text>
                </View>
              )}
            </View>
          </View>
          
          {renderProgressIndicator()}
          {renderQuestion()}
          {renderNavigation()}
        </ScrollView>
      )}
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
  timerContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: SPACING.xs,
    paddingHorizontal: SPACING.sm,
    borderRadius: 16,
    backgroundColor: COLORS.lightGray,
  },
  timerWarning: {
    backgroundColor: COLORS.error,
  },
  timerText: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginLeft: 4,
  },
  timerTextWarning: {
    color: COLORS.white,
  },
  content: {
    flex: 1,
    padding: SPACING.md,
  },
  evaluationInfo: {
    backgroundColor: COLORS.white,
    borderRadius: 8,
    padding: SPACING.md,
    marginBottom: SPACING.md,
    ...SHADOWS.sm,
  },
  evaluationTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  evaluationDescription: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    marginBottom: SPACING.md,
  },
  evaluationMeta: {
    flexDirection: 'row',
    flexWrap: 'wrap',
  },
  metaItem: {
    flexDirection: 'row',
    alignItems: 'center',
    marginRight: SPACING.lg,
    marginBottom: SPACING.xs,
  },
  metaText: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
    marginLeft: 4,
  },
  progressContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'center',
    marginBottom: SPACING.md,
  },
  progressDot: {
    width: 12,
    height: 12,
    borderRadius: 6,
    backgroundColor: COLORS.lightGray,
    margin: 4,
  },
  progressDotActive: {
    backgroundColor: COLORS.primary,
    transform: [{ scale: 1.2 }],
  },
  progressDotAnswered: {
    backgroundColor: COLORS.success,
  },
  questionCard: {
    padding: SPACING.md,
    marginBottom: SPACING.md,
  },
  questionNumber: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.primary,
    fontWeight: FONT_WEIGHT.bold,
    marginBottom: SPACING.sm,
  },
  questionText: {
    fontSize: FONT_SIZE.lg,
    color: COLORS.text,
    marginBottom: SPACING.lg,
  },
  optionsContainer: {
    marginBottom: SPACING.sm,
  },
  optionItem: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: SPACING.md,
    borderWidth: 1,
    borderColor: COLORS.lightGray,
    borderRadius: 8,
    marginBottom: SPACING.sm,
  },
  selectedOption: {
    borderColor: COLORS.primary,
    backgroundColor: COLORS.primaryLight,
  },
  radioButton: {
    width: 20,
    height: 20,
    borderRadius: 10,
    borderWidth: 2,
    borderColor: COLORS.muted,
    marginRight: SPACING.md,
    justifyContent: 'center',
    alignItems: 'center',
  },
  radioButtonSelected: {
    borderColor: COLORS.primary,
  },
  radioButtonInner: {
    width: 10,
    height: 10,
    borderRadius: 5,
    backgroundColor: COLORS.primary,
  },
  checkbox: {
    width: 20,
    height: 20,
    borderRadius: 4,
    borderWidth: 2,
    borderColor: COLORS.muted,
    marginRight: SPACING.md,
    justifyContent: 'center',
    alignItems: 'center',
  },
  checkboxSelected: {
    backgroundColor: COLORS.primary,
    borderColor: COLORS.primary,
  },
  optionText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    flex: 1,
  },
  navigationContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: SPACING.lg,
  },
  navigationButton: {
    flex: 1,
  },
  previousButton: {
    marginRight: SPACING.sm,
  },
  nextButton: {
    marginLeft: SPACING.sm,
  },
  submitButton: {
    marginLeft: SPACING.sm,
    backgroundColor: COLORS.success,
  },
  finishedContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: SPACING.xl,
  },
  finishedTitle: {
    fontSize: FONT_SIZE.xl,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginTop: SPACING.lg,
    marginBottom: SPACING.sm,
  },
  finishedMessage: {
    fontSize: FONT_SIZE.md,
    color: COLORS.muted,
    textAlign: 'center',
    marginBottom: SPACING.xl,
  },
  finishedButton: {
    width: '80%',
  },
});

export default EvaluationScreen;
