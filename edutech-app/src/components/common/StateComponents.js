import React from 'react';
import { View, Text, StyleSheet, ActivityIndicator, TouchableOpacity } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT, BORDER_RADIUS } from '../../config/theme';

/**
 * Componente para gestionar los estados de carga, error y vacío de forma consistente
 * en toda la aplicación.
 */
export const LoadingState = ({ message = 'Cargando...' }) => (
  <View style={styles.centerContainer}>
    <ActivityIndicator size="large" color={COLORS.primary} />
    <Text style={styles.loadingText}>{message}</Text>
  </View>
);

export const ErrorState = ({ 
  message = 'Ha ocurrido un error', 
  onRetry = null, 
  retryText = 'Reintentar' 
}) => (
  <View style={styles.centerContainer}>
    <Ionicons name="alert-circle-outline" size={48} color={COLORS.error} />
    <Text style={styles.errorText}>{message}</Text>
    {onRetry && (
      <TouchableOpacity style={styles.button} onPress={onRetry}>
        <Text style={styles.buttonText}>{retryText}</Text>
      </TouchableOpacity>
    )}
  </View>
);

export const EmptyState = ({ 
  message = 'No hay datos disponibles', 
  icon = 'information-circle-outline',
  actionText = null,
  onAction = null
}) => (
  <View style={styles.centerContainer}>
    <Ionicons name={icon} size={48} color={COLORS.muted} />
    <Text style={styles.emptyText}>{message}</Text>
    {actionText && onAction && (
      <TouchableOpacity style={styles.button} onPress={onAction}>
        <Text style={styles.buttonText}>{actionText}</Text>
      </TouchableOpacity>
    )}
  </View>
);

const styles = StyleSheet.create({
  centerContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: SPACING.xl,
  },
  loadingText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    marginTop: SPACING.md,
  },
  errorText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.error,
    textAlign: 'center',
    marginTop: SPACING.md,
    marginBottom: SPACING.lg,
  },
  emptyText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.muted,
    textAlign: 'center',
    marginTop: SPACING.md,
    marginBottom: SPACING.lg,
  },
  button: {
    backgroundColor: COLORS.primary,
    paddingVertical: SPACING.sm,
    paddingHorizontal: SPACING.lg,
    borderRadius: BORDER_RADIUS.lg,
  },
  buttonText: {
    color: COLORS.white,
    fontWeight: FONT_WEIGHT.medium,
  },
});

export default {
  LoadingState,
  ErrorState,
  EmptyState
};
