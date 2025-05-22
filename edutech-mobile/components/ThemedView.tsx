import { View, type ViewProps } from 'react-native';

import { useThemeColor } from '@/hooks/useThemeColor';

export type ThemedViewProps = ViewProps & {
  lightColor?: string;
  darkColor?: string;
  variant?: 'primary' | 'secondary';
  elevate?: boolean;
};

export function ThemedView({ 
  style, 
  lightColor, 
  darkColor, 
  variant = 'primary', 
  elevate = false,
  ...otherProps 
}: ThemedViewProps) {
  // Determinar el color de fondo según la variante
  const backgroundColor = useThemeColor(
    { light: lightColor, dark: darkColor }, 
    variant === 'primary' ? 'background' : 'backgroundSecondary'
  );
  
  // Aplicar sombra si se solicita elevación
  const elevationStyle = elevate ? {
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.05,
    shadowRadius: 8,
    elevation: 3,
    borderRadius: 12,
  } : {};

  return <View style={[{ backgroundColor }, elevationStyle, style]} {...otherProps} />;
}
