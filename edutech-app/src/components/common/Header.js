import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT, SHADOWS } from '../../config/theme';

const Header = ({ 
  title, 
  leftIcon, 
  rightIcon, 
  onLeftPress, 
  onRightPress,
  transparent = false,
  titleColor = COLORS.white,
  iconColor = COLORS.white,
}) => {
  return (
    <View style={[
      styles.container,
      transparent ? styles.transparentContainer : styles.solidContainer
    ]}>
      <View style={styles.leftContainer}>
        {leftIcon && (
          <TouchableOpacity 
            onPress={onLeftPress} 
            style={styles.iconButton}
            hitSlop={{ top: 10, bottom: 10, left: 10, right: 10 }}
          >
            <Ionicons name={leftIcon} size={24} color={iconColor} />
          </TouchableOpacity>
        )}
      </View>
      
      <Text style={[styles.title, { color: titleColor }]} numberOfLines={1}>
        {title}
      </Text>
      
      <View style={styles.rightContainer}>
        {rightIcon && (
          <TouchableOpacity 
            onPress={onRightPress} 
            style={styles.iconButton}
            hitSlop={{ top: 10, bottom: 10, left: 10, right: 10 }}
          >
            <Ionicons name={rightIcon} size={24} color={iconColor} />
          </TouchableOpacity>
        )}
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    height: 56,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: SPACING.md,
  },
  solidContainer: {
    backgroundColor: COLORS.primary,
    ...SHADOWS.sm,
  },
  transparentContainer: {
    backgroundColor: 'transparent',
  },
  leftContainer: {
    width: 40,
    alignItems: 'flex-start',
  },
  rightContainer: {
    width: 40,
    alignItems: 'flex-end',
  },
  title: {
    flex: 1,
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.semibold,
    textAlign: 'center',
  },
  iconButton: {
    padding: 4,
  },
});

export default Header;
