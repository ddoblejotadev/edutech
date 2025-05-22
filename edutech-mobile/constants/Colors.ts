/**
 * Paleta de colores 2025 - Diseño minimalista para EduTech
 * Siguiendo las tendencias de diseño de aplicaciones educativas modernas
 */

// Colores principales
const primaryLight = '#0066FF'; // Azul eléctrico moderno
const primaryDark = '#4D9FFF'; // Azul brillante para modo oscuro

// Acentos
const accentLight = '#00E5B3'; // Verde agua
const accentDark = '#00FFCE'; // Verde menta brillante

// Fondo y tipografía
export const Colors = {
  light: {
    text: '#1A1A2E', // Casi negro con un toque de azul
    textSecondary: '#505073', // Texto secundario
    background: '#FFFFFF', // Blanco puro
    backgroundSecondary: '#F7F9FC', // Gris muy claro con toque azulado
    card: '#FFFFFF', // Fondos de tarjetas
    cardBorder: '#EAEDF2', // Bordes sutiles
    tint: primaryLight,
    icon: '#6E7689', // Iconos en estado inactivo
    tabIconDefault: '#6E7689',
    tabIconSelected: primaryLight,
    accent: accentLight,
    divider: '#EAEDF2', // Líneas divisorias
    success: '#00D16E', // Verde éxito
    warning: '#FFB800', // Amarillo advertencia
    error: '#FF3B5F', // Rojo error
  },
  dark: {
    text: '#ECEDFF', // Blanco con un toque de azul
    textSecondary: '#9CA3AF', // Texto secundario
    background: '#0A0A1A', // Negro profundo con toque de azul
    backgroundSecondary: '#151528', // Fondo secundario
    card: '#1A1A2E', // Fondos de tarjetas
    cardBorder: '#272744', // Bordes sutiles
    tint: primaryDark,
    icon: '#8F95AF', // Iconos en estado inactivo
    tabIconDefault: '#8F95AF',
    tabIconSelected: primaryDark,
    accent: accentDark,
    divider: '#272744', // Líneas divisorias
    success: '#0DE57A', // Verde éxito
    warning: '#FFCB42', // Amarillo advertencia
    error: '#FF5275', // Rojo error
  },
};
