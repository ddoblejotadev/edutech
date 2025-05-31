import React, { useContext } from 'react';
import { View, Text, StyleSheet, ScrollView, TouchableOpacity, Alert } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT } from '../../config/theme';
import { AuthContext } from '../../context/AuthContext';

const ProfileScreen = () => {
  const { user, logout } = useContext(AuthContext);

  const handleLogout = () => {
    Alert.alert(
      'Cerrar Sesión',
      '¿Estás seguro de que quieres cerrar sesión?',
      [
        { text: 'Cancelar', style: 'cancel' },
        { text: 'Cerrar Sesión', onPress: logout, style: 'destructive' }
      ]
    );
  };

  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
        <View style={styles.avatarContainer}>
          <Ionicons name="person" size={64} color={COLORS.white} />
        </View>
        <Text style={styles.userName}>{user?.name || 'Usuario'}</Text>
        <Text style={styles.userEmail}>{user?.email || 'email@ug.uchile.cl'}</Text>
      </View>

      <View style={styles.content}>
        <View style={styles.infoCard}>
          <Text style={styles.cardTitle}>Información Académica</Text>
          <View style={styles.infoRow}>
            <Text style={styles.infoLabel}>RUT:</Text>
            <Text style={styles.infoValue}>{user?.studentId || '21.123.456-7'}</Text>
          </View>
          <View style={styles.infoRow}>
            <Text style={styles.infoLabel}>Carrera:</Text>
            <Text style={styles.infoValue}>{user?.career || 'Ingeniería Civil en Computación'}</Text>
          </View>
          <View style={styles.infoRow}>
            <Text style={styles.infoLabel}>Año:</Text>
            <Text style={styles.infoValue}>{user?.year || 4}º año</Text>
          </View>
          <View style={styles.infoRow}>
            <Text style={styles.infoLabel}>PPA:</Text>
            <Text style={styles.infoValue}>{user?.ppa || '5.5'}</Text>
          </View>
          <View style={styles.infoRow}>
            <Text style={styles.infoLabel}>Universidad:</Text>
            <Text style={styles.infoValue}>{user?.university || 'Universidad de Chile'}</Text>
          </View>
        </View>

        <TouchableOpacity style={styles.logoutButton} onPress={handleLogout}>
          <Ionicons name="log-out-outline" size={24} color={COLORS.white} />
          <Text style={styles.logoutText}>Cerrar Sesión</Text>
        </TouchableOpacity>
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  header: {
    backgroundColor: COLORS.primary,
    alignItems: 'center',
    padding: SPACING.xl,
    paddingTop: SPACING.xl * 2,
  },
  avatarContainer: {
    width: 120,
    height: 120,
    borderRadius: 60,
    backgroundColor: 'rgba(255,255,255,0.2)',
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: SPACING.md,
  },
  userName: {
    fontSize: FONT_SIZE.xl,
    color: COLORS.white,
    fontWeight: FONT_WEIGHT.bold,
    marginBottom: SPACING.xs,
  },
  userEmail: {
    fontSize: FONT_SIZE.md,
    color: COLORS.white,
    opacity: 0.9,
  },
  content: {
    padding: SPACING.md,
  },
  infoCard: {
    backgroundColor: COLORS.white,
    borderRadius: 12,
    padding: SPACING.md,
    marginBottom: SPACING.lg,
  },
  cardTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.md,
  },
  infoRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: SPACING.sm,
  },
  infoLabel: {
    fontSize: FONT_SIZE.md,
    color: COLORS.muted,
  },
  infoValue: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    fontWeight: FONT_WEIGHT.medium,
  },
  logoutButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: COLORS.error,
    borderRadius: 8,
    padding: SPACING.md,
  },
  logoutText: {
    color: COLORS.white,
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
    marginLeft: SPACING.sm,
  },
});

export default ProfileScreen;
