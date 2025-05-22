import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, FlatList, ActivityIndicator, RefreshControl, TouchableOpacity } from 'react-native';
import { notificacionesApi } from '../../api/notificacionesService';
import { Notificacion } from '../../types';
import { ThemedView, ThemedText } from '../../components';
import { router } from 'expo-router';
import { useThemeColor } from '../../hooks/useThemeColor';

export default function NotificacionesScreen() {
  const [notificaciones, setNotificaciones] = useState<Notificacion[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  
  // Usuario simulado - en una app real esto vendría de la autenticación
  const rutUsuario = '12345678-9';
  
  const backgroundColor = useThemeColor({}, 'background');
  const textColor = useThemeColor({}, 'text');
  const cardColor = useThemeColor({}, 'card');
  const accentColor = useThemeColor({}, 'tint');
  
  useEffect(() => {
    cargarNotificaciones();
  }, []);
  
  const cargarNotificaciones = async () => {
    try {
      setLoading(true);
      const response = await notificacionesApi.getByPersona(rutUsuario);
      setNotificaciones(response.data);
    } catch (error) {
      console.error('Error al cargar notificaciones:', error);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };
  
  const marcarComoLeida = async (id: number) => {
    try {
      await notificacionesApi.marcarLeida(id);
      // Actualiza la lista local
      setNotificaciones(notificaciones.map(notif => 
        notif.id === id ? { ...notif, leida: true } : notif
      ));
    } catch (error) {
      console.error('Error al marcar como leída:', error);
    }
  };
  
  const onRefresh = () => {
    setRefreshing(true);
    cargarNotificaciones();
  };
  
  const renderNotificacion = ({ item }: { item: Notificacion }) => (
    <TouchableOpacity 
      style={[
        styles.notificacionContainer, 
        { backgroundColor: cardColor },
        !item.leida && { borderLeftColor: accentColor, borderLeftWidth: 4 }
      ]}
      onPress={() => marcarComoLeida(item.id)}
    >
      <ThemedText style={styles.titulo} lightColor="#000" darkColor="#fff">
        {item.titulo}
      </ThemedText>
      <ThemedText style={styles.mensaje} lightColor="#444" darkColor="#bbb">
        {item.mensaje}
      </ThemedText>
      <View style={styles.footer}>
        <ThemedText style={styles.fecha} lightColor="#777" darkColor="#999">
          {new Date(item.fechaCreacion).toLocaleString()}
        </ThemedText>
        {!item.leida && (
          <View style={[styles.noLeidaBadge, { backgroundColor: accentColor }]}>
            <Text style={styles.noLeidaText}>Nueva</Text>
          </View>
        )}
      </View>
    </TouchableOpacity>
  );
  
  return (
    <ThemedView style={styles.container}>
      <ThemedText style={styles.titulo} lightColor="#000" darkColor="#fff">
        Mis Notificaciones
      </ThemedText>
      
      {loading && !refreshing ? (
        <ActivityIndicator size="large" color={accentColor} style={styles.loader} />
      ) : (
        <FlatList
          data={notificaciones}
          renderItem={renderNotificacion}
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
              No tienes notificaciones
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
  notificacionContainer: {
    padding: 16,
    marginBottom: 12,
    borderRadius: 12,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  titulo: {
    fontSize: 16,
    fontWeight: 'bold',
  },
  mensaje: {
    marginTop: 8,
    lineHeight: 20,
  },
  footer: {
    marginTop: 12,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  fecha: {
    fontSize: 12,
  },
  noLeidaBadge: {
    paddingHorizontal: 8,
    paddingVertical: 2,
    borderRadius: 12,
  },
  noLeidaText: {
    color: 'white',
    fontSize: 12,
    fontWeight: 'bold',
  },
  emptyText: {
    textAlign: 'center',
    marginTop: 40,
    fontSize: 16,
  }
});
