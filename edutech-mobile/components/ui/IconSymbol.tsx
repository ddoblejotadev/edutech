// Fallback for using MaterialIcons on Android and web.

import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { SymbolWeight } from 'expo-symbols';
import { ComponentProps } from 'react';
import { OpaqueColorValue, type StyleProp, type TextStyle } from 'react-native';

// Tipo más específico que incluye todas las posibles opciones
// de nombres para Material Icons
interface IconMapping {
  [key: string]: ComponentProps<typeof MaterialIcons>['name'];
}

type IconSymbolName = keyof typeof MAPPING;

/**
 * Add your SF Symbols to Material Icons mappings here.
 * - see Material Icons in the [Icons Directory](https://icons.expo.fyi).
 * - see SF Symbols in the [SF Symbols](https://developer.apple.com/sf-symbols/) app.
 */
const MAPPING: IconMapping = {
  // Navegación
  'house.fill': 'home',
  'magnifyingglass': 'search',
  'calendar': 'calendar-today',
  'person.fill': 'person',
  'bookmark.fill': 'bookmark',
  'gearshape.fill': 'settings',
  'bell.fill': 'notifications',
  
  // Flechas
  'chevron.left': 'chevron-left',
  'chevron.right': 'chevron-right',
  'chevron.down': 'keyboard-arrow-down',
  'chevron.up': 'keyboard-arrow-up',
  'arrow.left': 'arrow-back',
  'arrow.right': 'arrow-forward',
  'arrow.up': 'arrow-upward',
  'arrow.down': 'arrow-downward',
  'arrow.up.arrow.down': 'swap-vert',
  
  // Utilidades
  'xmark': 'close',
  'plus': 'add',
  'checkmark': 'check',
  'ellipsis': 'more-horiz',
  'trash.fill': 'delete',
  'pencil': 'edit',
  'paperplane.fill': 'send',
  'share': 'share',
  
  // Educación
  'book.fill': 'book',
  'graduationcap.fill': 'school',
  'doc.fill': 'description',
  'play.circle.fill': 'play-circle',
  'questionmark.circle.fill': 'help',
  'exclamationmark.circle': 'error',
  'questionmark.circle': 'help-outline',
  
  // Categorías
  'paintbrush.fill': 'brush',
  'laptopcomputer': 'laptop',
  'brain': 'psychology',
  'lock.fill': 'lock',
  'chart.bar.fill': 'bar-chart',
  'briefcase.fill': 'work',
  
  // Autenticación y usuario
  'envelope.fill': 'email',
  'user.lock': 'lock', // Cambiado a una clave diferente para evitar duplicados
  'eye.fill': 'visibility',
  'eye.slash.fill': 'visibility-off',
  'creditcard.fill': 'credit-card',
  
  // Desarrollo y código
  'chevron.left.forwardslash.chevron.right': 'code',
  'hammer.fill': 'build',
  
  // Multimedia
  'video.fill': 'videocam',
  'mic.fill': 'mic',
  'photo.fill': 'photo',
  
  // Social
  'heart.fill': 'favorite',
  'message.fill': 'message',
  'star.fill': 'star'
} as IconMapping;

/**
 * An icon component that uses native SF Symbols on iOS, and Material Icons on Android and web.
 * This ensures a consistent look across platforms, and optimal resource usage.
 * Icon `name`s are based on SF Symbols and require manual mapping to Material Icons.
 */
export function IconSymbol({
  name,
  size = 24,
  color,
  style,
}: {
  name: IconSymbolName;
  size?: number;
  color: string | OpaqueColorValue;
  style?: StyleProp<TextStyle>;
  weight?: SymbolWeight;
}) {
  return <MaterialIcons color={color} size={size} name={MAPPING[name]} style={style} />;
}
