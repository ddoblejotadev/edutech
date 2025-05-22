import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, FlatList, ActivityIndicator, TouchableOpacity, RefreshControl } from 'react-native';
import { inscripcionesApi } from '../../api/inscripcionesService';
import { EjecucionPersona, Ejecucion } from '../../types';
import { ThemedView, ThemedText } from '../../components';
import { router } from 'expo-router';
import { useThemeColor } from '../../hooks/useThemeColor';

export default function InscripcionesScreen() {
  const [inscripciones, setInscripciones] = useState<EjecucionPersona[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  
  // Usuario simulado - en una app real esto vendría de la autenticación
  const rutUsuario = '12345678-9';
  
  const backgroundColor = useThemeColor({}, 'background');
  const textColor = useThemeColor({}, 'text');
  const cardColor = useThemeColor({}, 'card');
  const accentColor = useThemeColor({}, 'tint');
  
  useEffect(() => {
    cargarInscripciones();
  }, []);
  
  const cargarInscripciones = async () => {
    try {
      setLoading(true);
      const response = await inscripcionesApi.getInscripcionesByPersona(rutUsuario);
      setInscripciones(response.data);
    } catch (error) {
      console.error('Error al cargar inscripciones:', error);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };
  
  const onRefresh = () => {
    setRefreshing(true);
    cargarInscripciones();
  };
  
  const cancelarInscripcion = async (idEjecucion: number) => {
    try {
      await inscripcionesApi.cancelarInscripcion(rutUsuario, idEjecucion);
      // Actualizar la lista local
      setInscripciones(inscripciones.filter(
        inscripcion => inscripcion.idEjecucion !== idEjecucion
      ));
    } catch (error) {
      console.error('Error al cancelar inscripción:', error);
    }
  };
  
  const renderInscripcion = ({ item }: { item: EjecucionPersona }) => (
    <View style={[styles.inscripcionContainer, { backgroundColor: cardColor }]}>
      <ThemedText style={styles.cursoNombre} lightColor="#000" darkColor="#fff">
        Curso #{item.idEjecucion}
      </ThemedText>
      <ThemedText style={styles.fecha} lightColor="#444" darkColor="#bbb">
        Inscrito: {new Date(item.fechaInscripcion).toLocaleDateString()}
      </ThemedText>
      <ThemedText style={styles.estado} lightColor="#444" darkColor="#bbb">
        Estado: {item.estado}
      </ThemedText>
      
      <View style={styles.actions}>
        <TouchableOpacity 
          style={[styles.button, styles.viewButton]} 
          onPress={() => router.push(`/curso/${item.idEjecucion}`)}
        >
          <Text style={styles.buttonText}>Ver Curso</Text>
        </TouchableOpacity>
        
        <TouchableOpacity 
          style={[styles.button, styles.cancelButton]} 
          onPress={() => cancelarInscripcion(item.idEjecucion)}
        >
          <Text style={styles.buttonText}>Cancelar</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
  
  return (
    <ThemedView style={styles.container}>
      <ThemedText style={styles.titulo} lightColor="#000" darkColor="#fff">
        Mis Inscripciones
      </ThemedText>
      
      {loading && !refreshing ? (
        <ActivityIndicator size="large" color={accentColor} style={styles.loader} />
      ) : (
        <FlatList
          data={inscripciones}
          renderItem={renderInscripcion}
          keyExtractor={item => item.id.toString()}
          contentContainerStyle={styles.listContainer}
          refreshControl={
            <RefreshControl
              refreshing={refreshing}
              onRefresh={onRefresh}
              colors={[accentColor]}
              tintColor={accentColor}
            />
          }
          ListEmptyComponent={
            <ThemedText style={styles.emptyText} lightColor="#777" darkColor="#999">
              No tienes inscripciones activas
            </ThemedText>
          }
        />
      )}
    </ThemedView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
  },
  titulo: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 16,
  },
  loader: {
    marginTop: 20,
  },
  listContainer: {
    paddingBottom: 20,
  },
  inscripcionContainer: {
    padding: 16,
    marginBottom: 12,
    borderRadius: 12,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  cursoNombre: {
    fontSize: 18,
    fontWeight: 'bold',
  },
  fecha: {
    marginTop: 8,
    fontSize: 14,
  },
  estado: {
    marginTop: 4,
    fontSize: 14,
  },
  actions: {
    marginTop: 16,
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  button: {
    paddingVertical: 8,
    paddingHorizontal: 16,
    borderRadius: 8,
    flex: 1,
    marginHorizontal: 4,
    alignItems: 'center',
  },
  viewButton: {
    backgroundColor: '#4A90E2',
  },
  cancelButton: {
    backgroundColor: '#E53935',
  },
  buttonText: {
    color: 'white',
    fontWeight: 'bold',
  },
  emptyText: {
    textAlign: 'center',
    marginTop: 40,
    fontSize: 16,
  }
});
