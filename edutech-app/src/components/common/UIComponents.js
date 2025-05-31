import React from 'react';
import { View, Text, TouchableOpacity, StyleSheet, ActivityIndicator, TextInput } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT } from '../../config/theme';

// Botón personalizado con diferentes variantes
export const Button = ({ 
  title, 
  onPress, 
  variant = 'primary', 
  disabled = false,
  loading = false,
  icon = null,
  style = {},
}) => {
  const buttonStyle = [
    styles.button,
    variant === 'outlined' ? styles.buttonOutlined : styles.buttonPrimary,
    disabled && styles.buttonDisabled,
    style
  ];

  const textStyle = [
    styles.buttonText,
    variant === 'outlined' ? styles.buttonTextOutlined : styles.buttonTextPrimary,
    disabled && styles.buttonTextDisabled
  ];

  return (
    <TouchableOpacity 
      style={buttonStyle} 
      onPress={onPress} 
      disabled={disabled || loading}
      activeOpacity={0.8}
    >
      <View style={styles.buttonContent}>
        {loading ? (
          <ActivityIndicator 
            color={variant === 'outlined' ? COLORS.primary : COLORS.white} 
            size="small" 
            style={styles.buttonIcon}
          />
        ) : icon ? (
          <Ionicons 
            name={icon} 
            size={20} 
            color={variant === 'outlined' ? COLORS.primary : COLORS.white} 
            style={styles.buttonIcon}
          />
        ) : null}
        <Text style={textStyle}>{title}</Text>
      </View>
    </TouchableOpacity>
  );
};

// Componente de tarjeta
export const Card = ({ children, style = {}, onPress }) => {
  const CardComponent = onPress ? TouchableOpacity : View;
  
  return (
    <CardComponent style={[styles.card, style]} onPress={onPress} activeOpacity={0.8}>
      {children}
    </CardComponent>
  );
};

// Componente de mensaje de error
export const ErrorMessage = ({ message, onRetry = null }) => {
  if (!message) return null;
  
  return (
    <View style={styles.errorContainer}>
      <Text style={styles.errorText}>{message}</Text>
      {onRetry && (
        <TouchableOpacity onPress={onRetry}>
          <Text style={styles.retryText}>Reintentar</Text>
        </TouchableOpacity>
      )}
    </View>
  );
};

// Componente de entrada de texto estilizada
export const Input = ({ 
  label, 
  error, 
  touched, 
  leftIcon = null,
  rightIcon = null,
  style = {},
  ...props 
}) => {
  return (
    <View style={[styles.inputContainer, style]}>
      {label && <Text style={styles.inputLabel}>{label}</Text>}
      
      <View style={[
        styles.inputWrapper,
        error && touched && styles.inputError
      ]}>
        {leftIcon && <View style={styles.inputLeftIcon}>{leftIcon}</View>}
        
        <TextInput 
          style={[
            styles.input,
            leftIcon && styles.inputWithLeftIcon,
            rightIcon && styles.inputWithRightIcon
          ]}
          placeholderTextColor={COLORS.muted}
          {...props}
        />
        
        {rightIcon && <View style={styles.inputRightIcon}>{rightIcon}</View>}
      </View>
      
      {error && touched && <Text style={styles.errorText}>{error}</Text>}
    </View>
  );
};

// Componente de división con texto
export const Divider = ({ text }) => (
  <View style={styles.dividerContainer}>
    <View style={styles.dividerLine} />
    {text && <Text style={styles.dividerText}>{text}</Text>}
    <View style={styles.dividerLine} />
  </View>
);

const styles = StyleSheet.create({
  button: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: SPACING.md,
    paddingHorizontal: SPACING.lg,
    borderRadius: 8,
    minHeight: 48,
  },
  buttonPrimary: {
    backgroundColor: COLORS.primary,
  },
  buttonOutlined: {
    backgroundColor: 'transparent',
    borderWidth: 1,
    borderColor: COLORS.primary,
  },
  buttonDisabled: {
    opacity: 0.6,
  },
  buttonContent: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
  },
  buttonText: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
  },
  buttonTextPrimary: {
    color: COLORS.white,
  },
  buttonTextOutlined: {
    color: COLORS.primary,
  },
  buttonTextDisabled: {
    color: COLORS.muted,
  },
  buttonIcon: {
    marginRight: SPACING.sm,
  },
  card: {
    backgroundColor: COLORS.white,
    borderRadius: 12,
    padding: SPACING.lg,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  errorContainer: {
    backgroundColor: `${COLORS.error}22`,
    borderRadius: 8,
    padding: SPACING.md,
    marginVertical: SPACING.sm,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  errorText: {
    color: COLORS.error,
    fontSize: FONT_SIZE.sm,
  },
  retryText: {
    color: COLORS.primary,
    fontWeight: FONT_WEIGHT.medium,
    marginLeft: SPACING.sm,
  },
  inputContainer: {
    marginBottom: SPACING.md,
  },
  inputLabel: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
    marginBottom: SPACING.xs,
    fontWeight: FONT_WEIGHT.medium,
  },
  inputWrapper: {
    flexDirection: 'row',
    alignItems: 'center',
    borderWidth: 1,
    borderColor: COLORS.lightGray,
    borderRadius: 8,
    backgroundColor: COLORS.white,
  },
  input: {
    flex: 1,
    paddingVertical: SPACING.sm,
    paddingHorizontal: SPACING.md,
    color: COLORS.text,
    fontSize: FONT_SIZE.md,
  },
  inputWithLeftIcon: {
    paddingLeft: 0,
  },
  inputWithRightIcon: {
    paddingRight: 0,
  },
  inputLeftIcon: {
    paddingLeft: SPACING.md,
  },
  inputRightIcon: {
    paddingRight: SPACING.md,
  },
  inputError: {
    borderColor: COLORS.error,
  },
  dividerContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginVertical: SPACING.lg,
  },
  dividerLine: {
    flex: 1,
    height: 1,
    backgroundColor: COLORS.lightGray,
  },
  dividerText: {
    color: COLORS.muted,
    paddingHorizontal: SPACING.md,
    fontSize: FONT_SIZE.sm,
  },
});
