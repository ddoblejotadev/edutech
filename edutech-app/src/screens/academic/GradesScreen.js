import React, { useState } from 'react';
import { View, StyleSheet, ScrollView } from 'react-native';
import { Text, Card, Title, SegmentedButtons, Divider, ProgressBar, DataTable } from 'react-native-paper';

const GradesScreen = () => {
  const [period, setPeriod] = useState('current');
  
  // Datos de ejemplo - Calificaciones actuales
  const currentCourses = [
    { id: 1, code: 'CS101', name: 'Introducción a la Programación', credits: 4, grade: 95, status: 'En progreso' },
    { id: 2, code: 'DB202', name: 'Bases de Datos Avanzadas', credits: 4, grade: 88, status: 'En progreso' },
    { id: 3, code: 'AI303', name: 'Inteligencia Artificial', credits: 3, grade: 92, status: 'En progreso' },
    { id: 4, code: 'WD204', name: 'Desarrollo Web', credits: 3, grade: 90, status: 'En progreso' },
  ];
  
  // Datos de ejemplo - Historial académico
  const academicHistory = [
    { id: 5, code: 'CS100', name: 'Fundamentos de Computación', credits: 3, grade: 85, semester: 'Otoño 2024' },
    { id: 6, code: 'MT101', name: 'Cálculo I', credits: 4, grade: 78, semester: 'Otoño 2024' },
    { id: 7, code: 'ENG201', name: 'Comunicación Técnica', credits: 2, grade: 92, semester: 'Primavera 2024' },
    { id: 8, code: 'PH103', name: 'Física para Informática', credits: 4, grade: 81, semester: 'Primavera 2024' },
    { id: 9, code: 'DB101', name: 'Introducción a Bases de Datos', credits: 3, grade: 94, semester: 'Otoño 2023' },
  ];
  
  const getGradeColor = (grade) => {
    if (grade >= 90) return '#4CAF50';
    if (grade >= 80) return '#2196F3';
    if (grade >= 70) return '#FF9800';
    return '#F44336';
  };
  
  const getGradeLetter = (grade) => {
    if (grade >= 90) return 'A';
    if (grade >= 80) return 'B';
    if (grade >= 70) return 'C';
    if (grade >= 60) return 'D';
    return 'F';
  };
  
  const calculateGPA = (courses) => {
    if (courses.length === 0) return 0;
    
    const totalPoints = courses.reduce((sum, course) => {
      return sum + (course.grade / 20) * course.credits;
    }, 0);
    
    const totalCredits = courses.reduce((sum, course) => sum + course.credits, 0);
    
    return (totalPoints / totalCredits).toFixed(2);
  };
  
  const currentGPA = calculateGPA(currentCourses);
  const cumulativeGPA = calculateGPA([...currentCourses, ...academicHistory]);
  
  return (
    <ScrollView style={styles.container}>
      <Card style={styles.summaryCard}>
        <Card.Content>
          <Title style={styles.cardTitle}>Resumen Académico</Title>
          
          <View style={styles.gpaContainer}>
            <View style={styles.gpaItem}>
              <Text style={styles.gpaLabel}>GPA Actual</Text>
              <Text style={styles.gpaValue}>{currentGPA}</Text>
            </View>
            <View style={styles.gpaItem}>
              <Text style={styles.gpaLabel}>GPA Acumulado</Text>
              <Text style={styles.gpaValue}>{cumulativeGPA}</Text>
            </View>
            <View style={styles.gpaItem}>
              <Text style={styles.gpaLabel}>Créditos</Text>
              <Text style={styles.gpaValue}>47/120</Text>
            </View>
          </View>
          
          <View style={styles.progressContainer}>
            <Text style={styles.progressLabel}>Progreso del grado</Text>
            <ProgressBar progress={47/120} color="#00008B" style={styles.progressBar} />
            <Text style={styles.progressText}>39% completado</Text>
          </View>
        </Card.Content>
      </Card>
      
      <View style={styles.segmentContainer}>
        <SegmentedButtons
          value={period}
          onValueChange={setPeriod}
          buttons={[
            { value: 'current', label: 'Semestre Actual' },
            { value: 'history', label: 'Historial Académico' },
          ]}
          style={styles.segmentButtons}
        />
      </View>
      
      {period === 'current' ? (
        <>
          <Text style={styles.sectionTitle}>Semestre Actual (Primavera 2025)</Text>
          {currentCourses.map(course => (
            <Card key={course.id} style={styles.courseCard}>
              <Card.Content>
                <View style={styles.courseHeader}>
                  <View>
                    <Text style={styles.courseCode}>{course.code}</Text>
                    <Title style={styles.courseName}>{course.name}</Title>
                  </View>
                  <View style={styles.gradeContainer}>
                    <Text style={[styles.gradeText, { color: getGradeColor(course.grade) }]}>
                      {course.grade}%
                    </Text>
                    <Text style={[styles.gradeLetter, { backgroundColor: getGradeColor(course.grade) }]}>
                      {getGradeLetter(course.grade)}
                    </Text>
                  </View>
                </View>
                <Divider style={styles.divider} />
                <View style={styles.courseFooter}>
                  <Text>Créditos: {course.credits}</Text>
                  <Text>{course.status}</Text>
                </View>
              </Card.Content>
            </Card>
          ))}
        </>
      ) : (
        <>
          <Text style={styles.sectionTitle}>Historial Académico</Text>
          <Card style={styles.historyCard}>
            <DataTable>
              <DataTable.Header>
                <DataTable.Title>Curso</DataTable.Title>
                <DataTable.Title numeric>Créditos</DataTable.Title>
                <DataTable.Title numeric>Nota</DataTable.Title>
                <DataTable.Title>Semestre</DataTable.Title>
              </DataTable.Header>
              
              {academicHistory.map(course => (
                <DataTable.Row key={course.id}>
                  <DataTable.Cell>{course.code} - {course.name}</DataTable.Cell>
                  <DataTable.Cell numeric>{course.credits}</DataTable.Cell>
                  <DataTable.Cell 
                    numeric 
                    textStyle={{ color: getGradeColor(course.grade) }}
                  >
                    {course.grade}%
                  </DataTable.Cell>
                  <DataTable.Cell>{course.semester}</DataTable.Cell>
                </DataTable.Row>
              ))}
            </DataTable>
          </Card>
        </>
      )}
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
    padding: 16,
  },
  summaryCard: {
    marginBottom: 16,
  },
  cardTitle: {
    fontSize: 18,
    marginBottom: 10,
  },
  gpaContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginVertical: 10,
  },
  gpaItem: {
    alignItems: 'center',
  },
  gpaLabel: {
    fontSize: 14,
    color: '#666',
    marginBottom: 5,
  },
  gpaValue: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#00008B',
  },
  progressContainer: {
    marginTop: 10,
  },
  progressLabel: {
    fontSize: 14,
    color: '#666',
    marginBottom: 5,
  },
  progressBar: {
    height: 8,
    borderRadius: 4,
  },
  progressText: {
    fontSize: 12,
    color: '#666',
    textAlign: 'right',
    marginTop: 5,
  },
  segmentContainer: {
    marginBottom: 16,
  },
  segmentButtons: {
    backgroundColor: '#fff',
  },
  sectionTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 10,
  },
  courseCard: {
    marginBottom: 10,
  },
  courseHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  courseCode: {
    fontSize: 14,
    color: '#666',
  },
  courseName: {
    fontSize: 16,
  },
  gradeContainer: {
    alignItems: 'center',
  },
  gradeText: {
    fontSize: 16,
    fontWeight: 'bold',
  },
  gradeLetter: {
    fontSize: 14,
    fontWeight: 'bold',
    color: 'white',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 4,
    marginTop: 4,
  },
  divider: {
    marginVertical: 10,
  },
  courseFooter: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  historyCard: {
    marginBottom: 20,
  },
});

export default GradesScreen;
