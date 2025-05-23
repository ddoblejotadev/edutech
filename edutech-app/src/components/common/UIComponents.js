import React from 'react';
import { View, Text, TouchableOpacity, StyleSheet, ActivityIndicator } from 'react-native';
import { COLORS, BORDER_RADIUS, FONT_SIZE, FONT_WEIGHT, SPACING } from '../../config/theme';

// Botón personalizado con diferentes variantes
export const Button = ({ 
  title, 
  onPress, 
  variant = 'filled', // filled, outlined, text
  color = 'primary', 
  size = 'md', // sm, md, lg
  disabled = false,
  loading = false,
  icon = null,
  style = {},
}) => {
  // Determinar estilos según las props
  const getButtonStyles = () => {
    let buttonStyle = { ...styles.button };
    
    // Variante
    if (variant === 'outlined') {
      buttonStyle.backgroundColor = 'transparent';
      buttonStyle.borderWidth = 1;
      buttonStyle.borderColor = disabled ? COLORS.muted : COLORS[color];
    } else if (variant === 'text') {
      buttonStyle.backgroundColor = 'transparent';
      buttonStyle.elevation = 0;
      buttonStyle.shadowOpacity = 0;
    } else {
      // filled (default)
      buttonStyle.backgroundColor = disabled ? COLORS.muted : COLORS[color];
    }
    
    // Tamaño
    if (size === 'sm') {
      buttonStyle.paddingVertical = SPACING.xs;
      buttonStyle.paddingHorizontal = SPACING.md;
    } else if (size === 'lg') {
      buttonStyle.paddingVertical = SPACING.md;
      buttonStyle.paddingHorizontal = SPACING.xl;
    }
    
    return buttonStyle;
  };
  
  const getTextStyles = () => {
    let textStyle = { ...styles.buttonText };
    
    if (variant === 'text' || variant === 'outlined') {
      textStyle.color = disabled ? COLORS.muted : COLORS[color];
    }
    
    if (size === 'sm') {
      textStyle.fontSize = FONT_SIZE.sm;
    } else if (size === 'lg') {
      textStyle.fontSize = FONT_SIZE.lg;
    }
    
    return textStyle;
  };
  
  return (
    <TouchableOpacity
      onPress={onPress}
      disabled={disabled || loading}
      style={[getButtonStyles(), style]}
      activeOpacity={0.8}
    >
      <View style={styles.buttonContent}>
        {loading ? (
          <ActivityIndicator 
            color={variant === 'filled' ? COLORS.white : COLORS[color]} 
            size="small" 
            style={styles.buttonIcon} 
          />
        ) : icon ? (
          <View style={styles.buttonIcon}>{icon}</View>
        ) : null}
        <Text style={getTextStyles()}>{title}</Text>
      </View>
    </TouchableOpacity>
  );
};

// Componente de tarjeta
export const Card = ({ 
  children, 
  style = {}, 
  onPress = null, 
  elevation = 'md' // none, sm, md, lg
}) => {
  const cardStyle = [
    styles.card,
    elevation !== 'none' && styles[`shadow${elevation.toUpperCase()}`],
    style
  ];
  
  if (onPress) {
    return (
      <TouchableOpacity 
        style={cardStyle} 
        onPress={onPress}
        activeOpacity={0.8}
      >
        {children}
      </TouchableOpacity>
    );
  }
  
  return <View style={cardStyle}>{children}</View>;
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
  // Estilos para Button
  button: {
    borderRadius: BORDER_RADIUS.md,
    paddingVertical: SPACING.sm,
    paddingHorizontal: SPACING.lg,
    alignItems: 'center',
    justifyContent: 'center',
    minWidth: 100,
  },
  buttonContent: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
  },
  buttonText: {
    color: COLORS.white,
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.semibold,
    textAlign: 'center',
  },
  buttonIcon: {
    marginRight: SPACING.xs,
  },
  
  // Estilos para Card
  card: {
    backgroundColor: COLORS.card,
    borderRadius: BORDER_RADIUS.md,
    padding: SPACING.lg,
    marginVertical: SPACING.sm,
  },
  shadowSM: {
    ...SHADOWS.sm,
  },
  shadowMD: {
    ...SHADOWS.md,
  },
  shadowLG: {
    ...SHADOWS.lg,
  },
  
  // Estilos para ErrorMessage
  errorContainer: {
    backgroundColor: `${COLORS.error}22`, // 22 es opacidad en hex
    borderRadius: BORDER_RADIUS.sm,
    padding: SPACING.md,
    marginVertical: SPACING.sm,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  errorText: {
    color: COLORS.error,
    fontSize: FONT_SIZE.sm,
    marginTop: SPACING.xs,
  },
  retryText: {
    color: COLORS.primary,
    fontWeight: FONT_WEIGHT.semibold,
    marginLeft: SPACING.sm,
  },
  
  // Estilos para Input
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
    borderColor: COLORS.border,
    borderRadius: BORDER_RADIUS.md,
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
  
  // Estilos para Divider
  dividerContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginVertical: SPACING.lg,
  },
  dividerLine: {
    flex: 1,
    height: 1,
    backgroundColor: COLORS.border,
  },
  dividerText: {
    color: COLORS.muted,
    paddingHorizontal: SPACING.md,
    fontSize: FONT_SIZE.sm,
  },
});

// Importar TextInput aquí para evitar problemas de dependencia circular
import { TextInput } from 'react-native';
