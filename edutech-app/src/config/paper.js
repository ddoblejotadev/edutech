import React from 'react';
import { PaperProvider, DefaultTheme } from 'react-native-paper';
import { COLORS } from './src/config/theme';

// Personalizar el tema de React Native Paper
const theme = {
  ...DefaultTheme,
  colors: {
    ...DefaultTheme.colors,
    primary: COLORS.primary,
    accent: COLORS.secondary,
    background: COLORS.background,
    text: COLORS.text,
    error: COLORS.error,
  },
};

// Envolver la aplicación con el proveedor de Paper
export const withPaperProvider = (Component) => {
  return (props) => (
    <PaperProvider theme={theme}>
      <Component {...props} />
    </PaperProvider>
  );
};
