import React from 'react';
import { View, Text, StyleSheet, ScrollView, TouchableOpacity } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT } from '../../config/theme';

const SocialServiceScreen = ({ navigation, route }) => {
  const { title } = route.params || { title: 'Servicio Social' };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()} style={styles.backButton}>
          <Ionicons name="arrow-back" size={24} color={COLORS.white} />
        </TouchableOpacity>
        <Text style={styles.headerTitle}>{title}</Text>
      </View>
      
      <ScrollView style={styles.content}>
        <View style={styles.card}>
          <Ionicons name="people-outline" size={64} color={COLORS.primary} />
          <Text style={styles.title}>{title}</Text>
          <Text style={styles.subtitle}>Este servicio estará disponible próximamente</Text>
        </View>
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.background,
    padding: SPACING.md,
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: SPACING.lg,
  },
  backButton: {
    padding: SPACING.sm,
    borderRadius: 24,
    backgroundColor: COLORS.primary,
  },
  headerTitle: {
    fontSize: FONT_SIZE.xl,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
  },
  content: {
    flex: 1,
  },
  card: {
    backgroundColor: COLORS.white,
    borderRadius: 8,
    padding: SPACING.lg,
    alignItems: 'center',
    shadowColor: COLORS.black,
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 2,
  },
  title: {
    fontSize: FONT_SIZE.xl,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginTop: SPACING.md,
  },
  subtitle: {
    fontSize: FONT_SIZE.md,
    color: COLORS.muted,
    textAlign: 'center',
    marginTop: SPACING.sm,
  },
});

export default SocialServiceScreen;