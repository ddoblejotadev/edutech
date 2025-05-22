import React from 'react';
import { 
  StyleSheet, 
  TouchableOpacity, 
  View,
  TouchableOpacityProps,
  ViewStyle,
  StyleProp
} from 'react-native';
import { Image } from 'expo-image';

import { ThemedText } from '../ThemedText';
import { useThemeColor } from '@/hooks/useThemeColor';
import { Colors } from '@/constants/Colors';
import { useColorScheme } from '@/hooks/useColorScheme';

export type CardProps = TouchableOpacityProps & {
  title: string;
  subtitle?: string;
  description?: string;
  imageUri?: string;
  imagePlaceholder?: string;
  badge?: string;
  progress?: number;
  containerStyle?: StyleProp<ViewStyle>;
  compact?: boolean;
  onPress?: () => void;
};

export function Card({
  title,
  subtitle,
  description,
  imageUri,
  imagePlaceholder = 'https://placehold.co/600x400/jpeg',
  badge,
  progress,
  containerStyle,
  compact = false,
  onPress,
  style,
  ...rest
}: CardProps) {  const colorScheme = useColorScheme();
  const colors = Colors[colorScheme ?? 'light'];
  const backgroundColor = useThemeColor({}, 'card');
  const borderColor = useThemeColor({}, 'cardBorder');

  const imageHeight = compact ? 120 : 140;

  return (
    <TouchableOpacity
      activeOpacity={0.7}
      onPress={onPress}
      style={[
        styles.container,
        {
          backgroundColor,
          borderColor,
          height: compact ? 'auto' : undefined,
        },
        style,
      ]}
      {...rest}>
      <View style={[styles.contentWrapper, containerStyle]}>
        {imageUri && (
          <Image
            source={{ uri: imageUri }}
            placeholder={imagePlaceholder}
            contentFit="cover"
            transition={300}
            style={[
              styles.image,
              {
                height: imageHeight,
                borderTopLeftRadius: 12,
                borderTopRightRadius: 12,
                ...(compact && {
                  width: 100,
                  height: '100%',
                  borderTopLeftRadius: 12,
                  borderBottomLeftRadius: 12,
                  borderTopRightRadius: 0,
                }),
              },
            ]}
          />
        )}

        <View
          style={[
            styles.textContainer,
            compact && { paddingLeft: 12, flex: 1, justifyContent: 'center' },
          ]}>
          {badge && (
            <View
              style={[
                styles.badge,
                { backgroundColor: colors.accent },
              ]}>
              <ThemedText type="label" style={styles.badgeText}>
                {badge}
              </ThemedText>
            </View>
          )}

          <ThemedText type={compact ? 'heading3' : 'heading2'} numberOfLines={2} style={styles.title}>
            {title}
          </ThemedText>

          {subtitle && (
            <ThemedText type="caption" secondary={true} style={styles.subtitle}>
              {subtitle}
            </ThemedText>
          )}

          {description && !compact && (
            <ThemedText type="body" secondary={true} numberOfLines={2} style={styles.description}>
              {description}
            </ThemedText>
          )}

          {progress !== undefined && (
            <View style={styles.progressContainer}>
              <View
                style={[
                  styles.progressBar,
                  {
                    backgroundColor: colors.backgroundSecondary,
                  },
                ]}>
                <View
                  style={[
                    styles.progressFill,
                    {
                      width: `${progress}%`,
                      backgroundColor: colors.tint,
                    },
                  ]}
                />
              </View>
              <ThemedText type="caption" secondary={true} style={styles.progressText}>
                {progress}% Completado
              </ThemedText>
            </View>
          )}
        </View>
      </View>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  container: {
    borderRadius: 12,
    overflow: 'hidden',
    marginBottom: 16,
    borderWidth: 1,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.05,
    shadowRadius: 8,
    elevation: 2,
  },
  contentWrapper: {
    flexDirection: 'column',
  },
  image: {
    width: '100%',
  },
  textContainer: {
    padding: 16,
  },
  badge: {
    alignSelf: 'flex-start',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 4,
    marginBottom: 8,
  },
  badgeText: {
    fontSize: 10,
    color: '#1A1A2E',
  },
  title: {
    marginBottom: 4,
  },
  subtitle: {
    marginBottom: 8,
  },
  description: {
    marginBottom: 16,
  },
  progressContainer: {
    marginTop: 8,
  },
  progressBar: {
    height: 6,
    borderRadius: 3,
    marginBottom: 4,
  },
  progressFill: {
    height: '100%',
    borderRadius: 3,
  },
  progressText: {
    textAlign: 'right',
  },
});
