import { StyleSheet, Text, type TextProps } from 'react-native';

import { useThemeColor } from '@/hooks/useThemeColor';
import { Colors } from '@/constants/Colors';

export type ThemedTextProps = TextProps & {
  lightColor?: string;
  darkColor?: string;
  type?: 'default' | 'title' | 'heading1' | 'heading2' | 'heading3' | 'body' | 'bodyBold' | 'caption' | 'label' | 'button' | 'link';
  secondary?: boolean;
};

export function ThemedText({
  style,
  lightColor,
  darkColor,
  type = 'default',
  secondary = false,
  ...rest
}: ThemedTextProps) {
  // Use secondary text color if specified
  const colorType = secondary ? 'textSecondary' : 'text';
  const color = useThemeColor({ light: lightColor, dark: darkColor }, colorType);

  return (
    <Text
      style={[
        { color },
        styles[type] || styles.default,
        style,
      ]}
      {...rest}
    />
  );
}

const styles = StyleSheet.create({
  default: {
    fontSize: 16,
    lineHeight: 24,
    fontFamily: 'System',
  },
  title: {
    fontSize: 32,
    fontWeight: 'bold',
    lineHeight: 40,
    letterSpacing: -0.5,
    fontFamily: 'System',
  },
  heading1: {
    fontSize: 28,
    fontWeight: '700',
    lineHeight: 34,
    letterSpacing: -0.4,
    fontFamily: 'System',
  },
  heading2: {
    fontSize: 24,
    fontWeight: '700',
    lineHeight: 30,
    letterSpacing: -0.3,
    fontFamily: 'System',
  },
  heading3: {
    fontSize: 20,
    fontWeight: '600',
    lineHeight: 26,
    letterSpacing: -0.2,
    fontFamily: 'System',
  },
  body: {
    fontSize: 16,
    lineHeight: 24,
    letterSpacing: 0.1,
    fontFamily: 'System',
  },
  bodyBold: {
    fontSize: 16,
    fontWeight: '600',
    lineHeight: 24,
    letterSpacing: 0.1,
    fontFamily: 'System',
  },
  caption: {
    fontSize: 14,
    lineHeight: 20,
    letterSpacing: 0.2,
    fontFamily: 'System',
  },
  label: {
    fontSize: 12,
    fontWeight: '500',
    lineHeight: 16,
    letterSpacing: 0.3,
    textTransform: 'uppercase',
    fontFamily: 'System',
  },
  button: {
    fontSize: 16,
    fontWeight: '600',
    lineHeight: 24,
    letterSpacing: 0.2,
    fontFamily: 'System',
  },
  link: {
    fontSize: 16,
    lineHeight: 24,
    color: Colors.light.tint,
    textDecorationLine: 'underline',
    fontFamily: 'System',
  },
});
