// Tema y estilos globales para la aplicación Edutech

// Paleta de colores
export const COLORS = {
  primary: '#4D7CFE',
  primaryDark: '#2E63E5',
  secondary: '#1E293B',
  accent: '#0D9488',
  background: '#F8FAFC',
  card: '#FFFFFF',
  text: '#1E293B',
  lightText: '#64748B',
  white: '#FFFFFF',
  border: '#E2E8F0',
  error: '#EF4444',
  success: '#10B981',
  warning: '#F59E0B',
  info: '#3B82F6',
  muted: '#94A3B8',
};

// Espaciado
export const SPACING = {
  xs: 4,
  sm: 8,
  md: 16,
  lg: 24,
  xl: 32,
  xxl: 48,
};

// Tamaños de fuente
export const FONT_SIZE = {
  xs: 12,
  sm: 14,
  md: 16,
  lg: 18,
  xl: 20,
  xxl: 24,
  xxxl: 32,
};

// Pesos de fuente
export const FONT_WEIGHT = {
  normal: 'normal',
  medium: '500',
  semibold: '600',
  bold: 'bold',
};

// Radios de bordes
export const BORDER_RADIUS = {
  sm: 4,
  md: 8,
  lg: 12,
  xl: 16,
  rounded: 999, // Para botones circulares
};

// Sombras
export const SHADOWS = {
  sm: {
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 1,
    },
    shadowOpacity: 0.18,
    shadowRadius: 1.0,
    elevation: 1,
  },
  md: {
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 3,
  },
  lg: {
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 4,
    },
    shadowOpacity: 0.3,
    shadowRadius: 4.65,
    elevation: 6,
  },
};

// Configuración de tema completa
export const theme = {
  colors: COLORS,
  spacing: SPACING,
  fontSize: FONT_SIZE,
  fontWeight: FONT_WEIGHT,
  borderRadius: BORDER_RADIUS,
  shadows: SHADOWS,
};

export default theme;
