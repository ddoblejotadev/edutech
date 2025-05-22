/**
 * Learn more about light and dark modes:
 * https://docs.expo.dev/guides/color-schemes/
 */

import { Colors } from '@/constants/Colors';
import { useColorScheme } from '@/hooks/useColorScheme';

export function useThemeColor(
  props: { light?: string; dark?: string },
  colorName: keyof typeof Colors.light & keyof typeof Colors.dark,
  opacity?: number
) {
  const theme = useColorScheme() ?? 'light';
  const colorFromProps = props[theme];
  let color: string;

  if (colorFromProps) {
    color = colorFromProps;
  } else {
    color = Colors[theme][colorName];
  }

  // Si se proporciona opacidad, aplicarla
  if (opacity !== undefined && !isNaN(opacity)) {
    return color; // Devolver color con el formato adecuado para la opacidad
  }

  return color;
}
