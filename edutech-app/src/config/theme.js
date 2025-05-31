import { StatusBar, Platform } from 'react-native';

// Tema universitario profesional inspirado en instituciones académicas modernas
// Diseño elegante con colores institucionales y tipografía académica

export const COLORS = {
  // Colores principales - Azul universitario elegante
  primary: '#4f46e5',        // Azul académico profundo
  primaryLight: '#f0f9ff',   // Azul muy claro para fondos
  primaryDark: '#0F1B2E',    // Azul marino para acentos fuertes
  
  // Colores secundarios - Dorado universitario
  secondary: '#7c3aed',      // Dorado académico
  secondaryLight: '#FFF8DC', // Crema dorada
  accent: '#C41E3A',         // Rojo universitario para acentos
  
  // Colores de estado - Paleta académica
  success: '#10b981',        // Verde académico
  warning: '#f59e0b',        // Naranja académico
  error: '#ef4444',          // Rojo académico
  info: '#1565C0',           // Azul información
  
  // Grises y neutros - Paleta profesional
  background: '#f8fafc',     // Gris muy claro, casi blanco
  surface: '#FFFFFF',        // Blanco puro para tarjetas
  surfaceVariant: '#F1F3F4', // Gris perla para variantes
  surfaceElevated: '#FFFFFF', // Superficie elevada
  
  // Textos - Jerarquía tipográfica clara
  text: '#1f2937',           // Negro carbón para texto principal
  textSecondary: '#5F6368',  // Gris medio para texto secundario
  textTertiary: '#80868B',   // Gris claro para texto terciario
  textDisabled: '#DADCE0',   // Gris muy claro para deshabilitado
  
  // Bordes y divisores - Sutiles pero definidos
  border: '#d1d5db',         // Gris medio para bordes
  borderLight: '#E8EAED',    // Gris claro para bordes suaves
  divider: '#F1F3F4',        // Gris muy claro para divisores
    // Colores base
  white: '#ffffff',
  black: '#000000',
  
  // Compatibilidad con versiones anteriores
  lightText: '#9ca3af',      // Alias para textSecondary
  muted: '#6b7280',         // Alias para grey.500
  lightGray: '#e5e7eb',     // Alias para grey.200
  
  // Paleta de grises extendida
  grey: {
    50: '#FAFAFA',
    100: '#F5F5F5',
    200: '#EEEEEE',
    300: '#E0E0E0',
    400: '#BDBDBD',
    500: '#9E9E9E',
    600: '#757575',
    700: '#616161',
    800: '#424242',
    900: '#212121',
  },
};

export const SPACING = {
  xs: 4,
  sm: 8,
  md: 16,
  lg: 24,
  xl: 32,
  xxl: 48
};

export const FONT_SIZE = {
  xs: 11,
  sm: 13,
  md: 15,
  lg: 18,
  xl: 22,
  xxl: 28,
  xxxl: 36
};

export const FONT_WEIGHT = {
  light: '300',
  normal: '400',
  medium: '500',
  semibold: '600',
  bold: '700',
};

export const BORDER_RADIUS = {
  sm: 4,
  md: 8,
  lg: 12,
  xl: 16
};

export const FONTS = {
  h1: {
    fontSize: FONT_SIZE.xxxl,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
  },
  h2: {
    fontSize: FONT_SIZE.xxl,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
  },
  h3: {
    fontSize: FONT_SIZE.xl,
    fontWeight: FONT_WEIGHT.semibold,
    color: COLORS.text,
  },
  h4: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.semibold,
    color: COLORS.text,
  },
  body1: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.normal,
    color: COLORS.text,
  },
  body2: {
    fontSize: FONT_SIZE.sm,
    fontWeight: FONT_WEIGHT.normal,
    color: COLORS.text,
  },
  body3: {
    fontSize: FONT_SIZE.xs,
    fontWeight: FONT_WEIGHT.normal,
    color: COLORS.text,
  },
  caption: {
    fontSize: FONT_SIZE.xs,
    fontWeight: FONT_WEIGHT.normal,
    color: COLORS.textSecondary,
  },
};

// Configuración de SafeArea para diferentes dispositivos
export const SAFE_AREA = {
  paddingTop: Platform.OS === 'ios' ? 44 : StatusBar.currentHeight || 24,
  paddingBottom: Platform.OS === 'ios' ? 34 : 0,
};

// Utilidad para obtener dimensiones responsivas
import { Dimensions } from 'react-native';

const { width, height } = Dimensions.get('window');

export const SCREEN = {
  width,
  height,
  isSmall: width < 375,
  isMedium: width >= 375 && width < 414,
  isLarge: width >= 414,
};

// Funciones helper para responsividad
export const responsiveSize = (size) => {
  if (SCREEN.isSmall) return size * 0.9;
  if (SCREEN.isLarge) return size * 1.1;
  return size;
};

export const responsiveSpacing = (spacing) => {
  if (SCREEN.isSmall) return spacing * 0.8;
  if (SCREEN.isLarge) return spacing * 1.2;
  return spacing;
};
