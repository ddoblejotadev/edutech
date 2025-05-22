import React from 'react';
import { 
  StyleSheet, 
  TouchableOpacity, 
  TouchableOpacityProps, 
  ActivityIndicator, 
  View 
} from 'react-native';

import { ThemedText } from '../ThemedText';
import { Colors } from '@/constants/Colors';
import { useColorScheme } from '@/hooks/useColorScheme';

export type ButtonProps = TouchableOpacityProps & {
  title: string;
  variant?: 'primary' | 'secondary' | 'outline' | 'ghost';
  size?: 'small' | 'medium' | 'large';
  fullWidth?: boolean;
  loading?: boolean;
  leftIcon?: React.ReactNode;
  rightIcon?: React.ReactNode;
};

export function Button({
  title,
  variant = 'primary',
  size = 'medium',
  fullWidth = false,
  loading = false,
  disabled = false,
  leftIcon,
  rightIcon,
  style,
  ...props
}: ButtonProps) {
  const colorScheme = useColorScheme();
  const colors = Colors[colorScheme ?? 'light'];

  // Determinar colores según la variante
  const getColors = () => {
    switch (variant) {
      case 'primary':
        return {
          background: colors.tint,
          text: '#FFFFFF',
          border: 'transparent',
        };
      case 'secondary':
        return {
          background: colors.accent,
          text: '#1A1A2E',
          border: 'transparent',
        };
      case 'outline':
        return {
          background: 'transparent',
          text: colors.tint,
          border: colors.tint,
        };
      case 'ghost':
        return {
          background: 'transparent',
          text: colors.tint,
          border: 'transparent',
        };
      default:
        return {
          background: colors.tint,
          text: '#FFFFFF',
          border: 'transparent',
        };
    }
  };

  // Determinar dimensiones según el tamaño
  const getDimensions = () => {
    switch (size) {
      case 'small':
        return {
          height: 36,
          paddingHorizontal: 16,
          borderRadius: 8,
          fontSize: 14,
        };
      case 'medium':
        return {
          height: 48,
          paddingHorizontal: 24,
          borderRadius: 10,
          fontSize: 16,
        };
      case 'large':
        return {
          height: 56,
          paddingHorizontal: 32,
          borderRadius: 12,
          fontSize: 18,
        };
      default:
        return {
          height: 48,
          paddingHorizontal: 24,
          borderRadius: 10,
          fontSize: 16,
        };
    }
  };

  const btnColors = getColors();
  const btnDimensions = getDimensions();

  // Estado deshabilitado
  const isDisabled = disabled || loading;
  const opacityStyle = isDisabled ? { opacity: 0.6 } : {};

  return (
    <TouchableOpacity
      activeOpacity={0.7}
      disabled={isDisabled}
      style={[
        styles.container,
        {
          backgroundColor: btnColors.background,
          borderColor: btnColors.border,
          borderWidth: variant === 'outline' ? 1.5 : 0,
          height: btnDimensions.height,
          paddingHorizontal: btnDimensions.paddingHorizontal,
          borderRadius: btnDimensions.borderRadius,
          width: fullWidth ? '100%' : 'auto',
        },
        opacityStyle,
        style,
      ]}
      {...props}>
      <View style={styles.contentContainer}>
        {loading ? (
          <ActivityIndicator 
            color={btnColors.text} 
            size="small" 
            style={styles.loader} 
          />
        ) : (
          <>
            {leftIcon && <View style={styles.iconLeft}>{leftIcon}</View>}
            <ThemedText
              style={[styles.text, { color: btnColors.text, fontSize: btnDimensions.fontSize }]}
              type="button">
              {title}
            </ThemedText>
            {rightIcon && <View style={styles.iconRight}>{rightIcon}</View>}
          </>
        )}
      </View>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
  },
  contentContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
  },
  text: {
    textAlign: 'center',
  },
  loader: {
    marginHorizontal: 8,
  },
  iconLeft: {
    marginRight: 8,
  },
  iconRight: {
    marginLeft: 8,
  },
});
