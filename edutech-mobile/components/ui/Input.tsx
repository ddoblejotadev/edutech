import React, { useState } from 'react';
import { 
  View, 
  TextInput, 
  StyleSheet, 
  TextInputProps, 
  TouchableOpacity,
  StyleProp,
  ViewStyle
} from 'react-native';
import { IconSymbol } from './IconSymbol';

import { useThemeColor } from '@/hooks/useThemeColor';
import { ThemedText } from '@/components/ThemedText';
import { Colors } from '@/constants/Colors';
import { useColorScheme } from '@/hooks/useColorScheme';

export type InputProps = TextInputProps & {
  label?: string;
  error?: string;
  leftIcon?: React.ReactNode;
  rightIcon?: React.ReactNode;
  secureToggle?: boolean;
  containerStyle?: StyleProp<ViewStyle>;
  variant?: 'outline' | 'filled';
};

export function Input({
  label,
  error,
  leftIcon,
  rightIcon,
  secureToggle = false,
  containerStyle,
  variant = 'outline',
  secureTextEntry,
  value,
  style,
  ...props
}: InputProps) {
  const [isFocused, setIsFocused] = useState(false);
  const [isSecureVisible, setIsSecureVisible] = useState(!secureTextEntry);
  
  const colorScheme = useColorScheme();
  const colors = Colors[colorScheme ?? 'light'];
  
  const inputBackground = useThemeColor({}, variant === 'filled' ? 'backgroundSecondary' : 'background');
  const dividerColor = useThemeColor({}, 'divider');
  const borderColor = error 
    ? colors.error 
    : isFocused 
      ? colors.tint 
      : dividerColor;
  
  const textColor = useThemeColor({}, 'text');
  const placeholderColor = useThemeColor({}, 'textSecondary');

  const toggleSecureEntry = () => {
    setIsSecureVisible(!isSecureVisible);
  };

  const hasContent = value && value.length > 0;

  return (
    <View style={[styles.container, containerStyle]}>
      {label && (
        <ThemedText 
          type="caption" 
          style={[
            styles.label, 
            isFocused && { color: colors.tint },
            error && { color: colors.error },
          ]}
        >
          {label}
        </ThemedText>
      )}

      <View 
        style={[
          styles.inputContainer,
          {
            backgroundColor: inputBackground,
            borderColor: borderColor,
            borderWidth: variant === 'outline' ? 1.5 : 0,
          },
          variant === 'filled' && styles.filledInput,
          isFocused && styles.focused,
          error && styles.errorInput,
        ]}
      >
        {leftIcon && <View style={styles.leftIcon}>{leftIcon}</View>}

        <TextInput
          style={[
            styles.input,
            {
              color: textColor,
            },
            style,
          ]}
          placeholderTextColor={placeholderColor}
          secureTextEntry={secureToggle ? !isSecureVisible : secureTextEntry}
          onFocus={() => setIsFocused(true)}
          onBlur={() => setIsFocused(false)}
          value={value}
          {...props}
        />

        {secureToggle && hasContent && (
          <TouchableOpacity 
            style={styles.rightIcon} 
            onPress={toggleSecureEntry}
            activeOpacity={0.7}
          >
            <IconSymbol 
              name={isSecureVisible ? "eye.slash.fill" : "eye.fill"} 
              color={textColor} 
              size={18} 
            />
          </TouchableOpacity>
        )}

        {rightIcon && !secureToggle && <View style={styles.rightIcon}>{rightIcon}</View>}
      </View>

      {error && (
        <ThemedText 
          type="caption" 
          style={[styles.errorText, { color: colors.error }]}
        >
          {error}
        </ThemedText>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    marginBottom: 16,
    width: '100%',
  },
  label: {
    marginBottom: 6,
  },
  inputContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    borderRadius: 10,
    overflow: 'hidden',
    height: 50,
  },
  filledInput: {
    borderBottomWidth: 1.5,
    borderRadius: 8,
    borderBottomLeftRadius: 0,
    borderBottomRightRadius: 0,
  },
  input: {
    flex: 1,
    height: 50,
    paddingHorizontal: 12,
    fontSize: 16,
  },
  focused: {
    borderWidth: 1.5,
  },
  errorInput: {
    borderWidth: 1.5,
  },
  leftIcon: {
    paddingLeft: 12,
  },
  rightIcon: {
    paddingRight: 12,
  },
  errorText: {
    marginTop: 4,
  },
});
