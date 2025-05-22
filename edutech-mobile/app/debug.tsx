import React, { useState } from 'react';
import { ActivityIndicator, StyleSheet, TouchableOpacity, View, Alert, ScrollView } from 'react-native';
import { Stack } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';

import { ThemedText } from '@/components/ThemedText';
import { ThemedView } from '@/components/ThemedView';
import { useThemeColor } from '@/hooks/useThemeColor';
import axios from 'axios';
import apiClient from '../api/apiClient';

export default function DebugScreen() {
  const [isLoadingStatus, setIsLoadingStatus] = useState(false);
  const [isLoadingAuth, setIsLoadingAuth] = useState(false);
  const [apiStatus, setApiStatus] = useState<any>(null);
  const [statusError, setStatusError] = useState<string | null>(null);
  const [authResult, setAuthResult] = useState<any>(null);
  const [authError, setAuthError] = useState<string | null>(null);

  const primaryColor = useThemeColor({}, 'tint');
  const cardBgColor = useThemeColor({}, 'card');
  const errorColor = '#E53935';
  const successColor = '#4CAF50';
  
  const checkApiStatus = async () => {
    setIsLoadingStatus(true);
    setApiStatus(null);
    setStatusError(null);
    
    try {
      // Obtener URL base de la API
      const baseUrl = apiClient.defaults.baseURL || '';
      
      // Hacer una petición directa para verificar conectividad
      const response = await axios.get(`${baseUrl}/api/status`);
      
      setApiStatus(response.data);
      Alert.alert('Conexión exitosa', 'La conexión con el API Gateway funciona correctamente');
    } catch (error: any) {
      console.error('Error al verificar estado de la API:', error);
      setStatusError(error.message);
      Alert.alert('Error de conexión', `No se pudo conectar con el API: ${error.message}`);
    } finally {
      setIsLoadingStatus(false);
    }
  };
  
  const testAuthentication = async () => {
    setIsLoadingAuth(true);
    setAuthResult(null);
    setAuthError(null);
    
    try {
      // Prueba de credenciales de prueba
      const response = await apiClient.post('/api/auth/login', {
        username: 'estudiante',
        password: 'estudiante123'
      });
      
      setAuthResult(response.data);
      Alert.alert('Autenticación exitosa', 'Las credenciales de prueba funcionan correctamente');
    } catch (error: any) {
      console.error('Error al probar autenticación:', error);
      setAuthError(error.message);
      Alert.alert('Error de autenticación', `No se pudo autenticar: ${error.message}`);
    } finally {
      setIsLoadingAuth(false);
    }
  };

  return (
    <ThemedView style={styles.container}>
      <Stack.Screen 
        options={{ 
          title: 'Herramientas de Depuración', 
          headerShown: true,
          headerStyle: {
            backgroundColor: useThemeColor({}, 'background'),
          },
          headerTitleStyle: {
            color: useThemeColor({}, 'text'),
          },
          headerTintColor: primaryColor,
        }} 
      />

      <ScrollView style={styles.scrollContainer} showsVerticalScrollIndicator={false}>
        <View style={styles.header}>
          <Ionicons 
            name="code-working" 
            size={48} 
            color={primaryColor}
          />
          <ThemedText style={styles.title}>Herramientas de Depuración</ThemedText>
          <ThemedText style={styles.description}>
            Esta pantalla te permite probar la conexión con el backend y verificar la 
            configuración del sistema.
          </ThemedText>
        </View>
        
        <View style={styles.section}>
          <View style={styles.sectionHeader}>
            <Ionicons name="server-outline" size={24} color={primaryColor} />
            <ThemedText style={styles.sectionTitle}>Estado del API</ThemedText>
          </View>
          
          <View style={[styles.card, { backgroundColor: cardBgColor }]}>
            <TouchableOpacity
              style={[styles.button, { backgroundColor: primaryColor }]}
              onPress={checkApiStatus}
              disabled={isLoadingStatus}
            >
              {isLoadingStatus ? (
                <ActivityIndicator color="white" />
              ) : (
                <>
                  <Ionicons name="refresh-outline" size={20} color="white" style={styles.buttonIcon} />
                  <ThemedText style={styles.buttonText}>
                    Verificar Conexión
                  </ThemedText>
                </>
              )}
            </TouchableOpacity>
            
            {apiStatus && (
              <View style={[styles.resultCard, { backgroundColor: successColor + '20', borderColor: successColor + '50' }]}>
                <View style={styles.resultHeader}>
                  <Ionicons name="checkmark-circle" size={24} color={successColor} />
                  <ThemedText style={[styles.resultTitle, { color: successColor }]}>
                    Estado: {apiStatus.status}
                  </ThemedText>
                </View>
                <ThemedText style={styles.resultItem}>{apiStatus.message}</ThemedText>
                <ThemedText style={styles.resultItem}>Timestamp: {new Date(apiStatus.timestamp).toLocaleString()}</ThemedText>
                  <ThemedText style={[styles.resultSectionTitle, { color: successColor }]}>Servicios:</ThemedText>
                {Object.entries(apiStatus.services).map(([service, status]) => (
                  <View key={service} style={styles.serviceItem}>
                    <Ionicons 
                      name={status === 'UP' ? "checkmark-circle" : "alert-circle"} 
                      size={16} 
                      color={status === 'UP' ? successColor : errorColor} 
                      style={styles.serviceIcon}
                    />
                    <ThemedText style={styles.serviceName}>{service}:</ThemedText>
                    <ThemedText 
                      style={[
                        styles.serviceStatus, 
                        { color: status === 'UP' ? successColor : errorColor }
                      ]}
                    >
                      {String(status)}
                    </ThemedText>
                  </View>
                ))}
              </View>
            )}
            
            {statusError && (
              <View style={[styles.resultCard, { backgroundColor: errorColor + '15', borderColor: errorColor + '40' }]}>
                <View style={styles.resultHeader}>
                  <Ionicons name="alert-circle" size={24} color={errorColor} />
                  <ThemedText style={[styles.resultTitle, { color: errorColor }]}>Error de Conexión</ThemedText>
                </View>
                <ThemedText style={styles.resultItem}>{statusError}</ThemedText>
              </View>
            )}
          </View>
        </View>
        
        <View style={styles.section}>
          <View style={styles.sectionHeader}>
            <Ionicons name="key-outline" size={24} color={primaryColor} />
            <ThemedText style={styles.sectionTitle}>Prueba de Autenticación</ThemedText>
          </View>
          
          <View style={[styles.card, { backgroundColor: cardBgColor }]}>
            <TouchableOpacity
              style={[styles.button, { backgroundColor: primaryColor }]}
              onPress={testAuthentication}
              disabled={isLoadingAuth}
            >
              {isLoadingAuth ? (
                <ActivityIndicator color="white" />
              ) : (
                <>
                  <Ionicons name="log-in-outline" size={20} color="white" style={styles.buttonIcon} />
                  <ThemedText style={styles.buttonText}>
                    Probar Credenciales
                  </ThemedText>
                </>
              )}
            </TouchableOpacity>
            
            {authResult && (
              <View style={[styles.resultCard, { backgroundColor: successColor + '20', borderColor: successColor + '50' }]}>
                <View style={styles.resultHeader}>
                  <Ionicons name="checkmark-circle" size={24} color={successColor} />
                  <ThemedText style={[styles.resultTitle, { color: successColor }]}>
                    Autenticación Exitosa
                  </ThemedText>
                </View>
                
                <View style={styles.tokenContainer}>
                  <ThemedText style={styles.resultLabel}>Token JWT:</ThemedText>
                  <ThemedText style={styles.tokenText} numberOfLines={1} ellipsizeMode="middle">
                    {authResult.token}
                  </ThemedText>
                </View>
                
                <View style={styles.userInfoContainer}>
                  <ThemedText style={styles.resultSectionTitle}>Información del Usuario:</ThemedText>
                  
                  <View style={styles.infoRow}>
                    <Ionicons name="person" size={18} color={primaryColor} style={styles.infoIcon} />
                    <ThemedText style={styles.infoLabel}>Nombre:</ThemedText>
                    <ThemedText style={styles.infoValue}>
                      {authResult.usuario?.nombres} {authResult.usuario?.apellidos}
                    </ThemedText>
                  </View>
                  
                  <View style={styles.infoRow}>
                    <Ionicons name="mail" size={18} color={primaryColor} style={styles.infoIcon} />
                    <ThemedText style={styles.infoLabel}>Email:</ThemedText>
                    <ThemedText style={styles.infoValue}>{authResult.usuario?.email}</ThemedText>
                  </View>
                  
                  <View style={styles.infoRow}>
                    <Ionicons name="shield" size={18} color={primaryColor} style={styles.infoIcon} />
                    <ThemedText style={styles.infoLabel}>Roles:</ThemedText>
                    <ThemedText style={styles.infoValue}>
                      {authResult.usuario?.roles?.join(', ')}
                    </ThemedText>
                  </View>
                </View>
              </View>
            )}
            
            {authError && (
              <View style={[styles.resultCard, { backgroundColor: errorColor + '15', borderColor: errorColor + '40' }]}>
                <View style={styles.resultHeader}>
                  <Ionicons name="alert-circle" size={24} color={errorColor} />
                  <ThemedText style={[styles.resultTitle, { color: errorColor }]}>
                    Error de Autenticación
                  </ThemedText>
                </View>
                <ThemedText style={styles.resultItem}>{authError}</ThemedText>
              </View>
            )}
          </View>
        </View>
      </ScrollView>
    </ThemedView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  scrollContainer: {
    flex: 1,
    paddingHorizontal: 16,
  },
  header: {
    alignItems: 'center',
    marginVertical: 20,
    paddingHorizontal: 12,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginTop: 12,
    marginBottom: 8,
    textAlign: 'center',
  },
  description: {
    fontSize: 15,
    lineHeight: 22,
    textAlign: 'center',
    marginBottom: 8,
    opacity: 0.7,
  },
  section: {
    marginBottom: 24,
  },
  sectionHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 12,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginLeft: 8,
  },
  card: {
    borderRadius: 16,
    padding: 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 2,
  },
  button: {
    height: 48,
    borderRadius: 24,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 3,
    elevation: 2,
  },
  buttonText: {
    color: 'white',
    fontSize: 16,
    fontWeight: '600',
  },
  buttonIcon: {
    marginRight: 8,
  },
  resultCard: {
    padding: 16,
    borderRadius: 12,
    marginBottom: 8,
    borderWidth: 1,
  },
  resultHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 12,
  },
  resultTitle: {
    fontWeight: 'bold',
    fontSize: 16,
    marginLeft: 8,
  },
  resultSectionTitle: {
    fontWeight: '600',
    fontSize: 15,
    marginBottom: 8,
    marginTop: 12,
  },
  resultItem: {
    marginBottom: 8,
    fontSize: 14,
    lineHeight: 20,
  },
  serviceItem: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 6,
    paddingVertical: 4,
  },
  serviceIcon: {
    marginRight: 8,
  },
  serviceName: {
    flex: 1,
    fontWeight: '500',
  },
  serviceStatus: {
    fontWeight: '600',
  },
  tokenContainer: {
    backgroundColor: 'rgba(0,0,0,0.05)',
    borderRadius: 8,
    padding: 12,
    marginVertical: 8,
  },
  tokenText: {
    fontSize: 13,
    fontFamily: 'monospace',
  },
  userInfoContainer: {
    marginTop: 8,
  },
  infoRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 8,
    paddingVertical: 4,
  },
  infoIcon: {
    marginRight: 8,
    width: 22,
  },
  infoLabel: {
    width: 70,
    fontWeight: '500',
  },
  infoValue: {
    flex: 1,
  },
  resultLabel: {
    fontWeight: '500',
    marginBottom: 4,
  },
});
