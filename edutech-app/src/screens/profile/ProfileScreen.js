import React, { useState, useEffect, useContext } from 'react';
import { View, Text, StyleSheet, ScrollView, Image, TouchableOpacity, SafeAreaView, Alert, TextInput } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT, SHADOWS } from '../../config/theme';
import { Button, Card } from '../../components/common/UIComponents';
import { LoadingState, ErrorState } from '../../components/common/StateComponents';
import { AuthContext } from '../../context/AuthContext';
import { User } from '../../services/apiService';

const ProfileScreen = ({ navigation }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);
  const [userData, setUserData] = useState(null);
  const [editedData, setEditedData] = useState({});
  
  const { token, logout } = useContext(AuthContext);
  
  // Cargar datos del perfil
  useEffect(() => {
    const loadUserProfile = async () => {
      setLoading(true);
      setError(null);
      
      try {
        const data = await User.getProfile(token);
        setUserData(data);
        setEditedData({
          name: data.name,
          email: data.email,
          phone: data.phone,
          bio: data.bio
        });
      } catch (error) {
        console.error('Error cargando perfil:', error);
        setError(error.message || 'No se pudo cargar la información del perfil');
      } finally {
        setLoading(false);
      }
    };
    
    loadUserProfile();
  }, [token]);
  
  const handleEdit = () => {
    setIsEditing(true);
  };
  
  const handleCancel = () => {
    setIsEditing(false);
    // Restaurar los datos originales
    setEditedData({
      name: userData.name,
      email: userData.email,
      phone: userData.phone,
      bio: userData.bio
    });
  };
  
  const handleSave = async () => {
    // Validación básica
    if (!editedData.name || !editedData.email) {
      Alert.alert("Error", "El nombre y el correo electrónico son obligatorios");
      return;
    }
    
    setSaving(true);
    
    try {
      // Enviar datos actualizados al servidor
      const updatedProfile = await User.updateProfile(token, editedData);
      
      // Actualizar los datos del usuario
      setUserData({
        ...userData,
        ...updatedProfile
      });
      
      setIsEditing(false);
      Alert.alert("Éxito", "Perfil actualizado correctamente");
    } catch (error) {
      Alert.alert("Error", error.message || "No se pudo actualizar el perfil");
    } finally {
      setSaving(false);
    }
  };
  
  const handleLogout = () => {
    Alert.alert(
      "Cerrar sesión",
      "¿Estás seguro de que quieres cerrar sesión?",
      [
        { text: "Cancelar", style: "cancel" },
        { text: "Cerrar sesión", onPress: () => logout() }
      ]
    );
  };
  
  const handleInputChange = (field, value) => {
    setEditedData(prev => ({
      ...prev,
      [field]: value
    }));
  };
  
  const renderAchievements = () => {
    if (!userData.achievements || userData.achievements.length === 0) {
      return (
        <Card style={styles.sectionCard}>
          <View style={styles.sectionHeader}>
            <Text style={styles.sectionTitle}>Logros</Text>
          </View>
          <View style={styles.emptySection}>
            <Ionicons name="trophy-outline" size={48} color={COLORS.muted} />
            <Text style={styles.emptyText}>Aún no tienes logros</Text>
            <Text style={styles.emptySubtext}>Continúa con tus cursos para desbloquear logros</Text>
          </View>
        </Card>
      );
    }
    
    return (
      <Card style={styles.sectionCard}>
        <View style={styles.sectionHeader}>
          <Text style={styles.sectionTitle}>Logros</Text>
        </View>
        
        {userData.achievements.map(achievement => (
          <View key={achievement.id} style={styles.achievementItem}>
            <View style={styles.achievementIcon}>
              <Ionicons name={achievement.icon} size={24} color={COLORS.primary} />
            </View>
            
            <View style={styles.achievementInfo}>
              <Text style={styles.achievementTitle}>{achievement.title}</Text>
              <Text style={styles.achievementDate}>{achievement.date}</Text>
            </View>
          </View>
        ))}
      </Card>
    );
  };
  
  const renderCertificates = () => {
    if (!userData.certificates || userData.certificates.length === 0) {
      return (
        <Card style={styles.sectionCard}>
          <View style={styles.sectionHeader}>
            <Text style={styles.sectionTitle}>Certificados</Text>
          </View>
          <View style={styles.emptySection}>
            <Ionicons name="document-outline" size={48} color={COLORS.muted} />
            <Text style={styles.emptyText}>Aún no tienes certificados</Text>
            <Text style={styles.emptySubtext}>Completa tus cursos para obtener certificados</Text>
          </View>
        </Card>
      );
    }
    
    return (
      <Card style={styles.sectionCard}>
        <View style={styles.sectionHeader}>
          <Text style={styles.sectionTitle}>Certificados</Text>
        </View>
        
        {userData.certificates.map(certificate => (
          <TouchableOpacity 
            key={certificate.id} 
            style={styles.certificateItem}
            onPress={() => {
              // Lógica para ver/descargar el certificado
              Alert.alert(
                "Certificado",
                `Ver certificado de ${certificate.title}`,
                [{ text: "OK" }]
              );
            }}
          >
            <Ionicons name="document-text-outline" size={24} color={COLORS.primary} />
            <View style={styles.certificateInfo}>
              <Text style={styles.certificateTitle}>{certificate.title}</Text>
              <Text style={styles.certificateDate}>{certificate.date}</Text>
            </View>
            <TouchableOpacity style={styles.downloadButton}>
              <Ionicons name="download-outline" size={22} color={COLORS.primary} />
            </TouchableOpacity>
          </TouchableOpacity>
        ))}
      </Card>
    );
  };
  
  const renderProfileInfo = () => {
    if (isEditing) {
      return renderEditForm();
    }
    
    return (
      <Card style={styles.profileCard}>
        <View style={styles.headerSection}>
          <View style={styles.avatarContainer}>
            {userData.avatarUrl ? (
              <Image 
                source={{ uri: userData.avatarUrl }} 
                style={styles.avatar} 
              />
            ) : (
              <View style={styles.avatarPlaceholder}>
                <Text style={styles.avatarLetter}>
                  {userData.name ? userData.name.charAt(0).toUpperCase() : 'U'}
                </Text>
              </View>
            )}
          </View>
          
          <View style={styles.nameSection}>
            <Text style={styles.userName}>{userData.name}</Text>
            <Text style={styles.userRole}>{userData.role}</Text>
          </View>
          
          <TouchableOpacity 
            style={styles.editButton}
            onPress={handleEdit}
          >
            <Ionicons name="create-outline" size={22} color={COLORS.primary} />
          </TouchableOpacity>
        </View>
        
        <View style={styles.infoSection}>
          <View style={styles.infoItem}>
            <Ionicons name="mail-outline" size={20} color={COLORS.primary} />
            <Text style={styles.infoText}>{userData.email}</Text>
          </View>
          
          {userData.phone && (
            <View style={styles.infoItem}>
              <Ionicons name="call-outline" size={20} color={COLORS.primary} />
              <Text style={styles.infoText}>{userData.phone}</Text>
            </View>
          )}
          
          <View style={styles.infoItem}>
            <Ionicons name="calendar-outline" size={20} color={COLORS.primary} />
            <Text style={styles.infoText}>Miembro desde {userData.joinDate}</Text>
          </View>
        </View>
        
        {userData.bio && (
          <View style={styles.bioSection}>
            <Text style={styles.bioText}>{userData.bio}</Text>
          </View>
        )}
        
        <View style={styles.statsContainer}>
          <View style={styles.statItem}>
            <Text style={styles.statNumber}>{userData.totalCourses}</Text>
            <Text style={styles.statLabel}>Cursos</Text>
          </View>
          
          <View style={styles.statDivider} />
          
          <View style={styles.statItem}>
            <Text style={styles.statNumber}>{userData.completedCourses}</Text>
            <Text style={styles.statLabel}>Completados</Text>
          </View>
          
          <View style={styles.statDivider} />
          
          <View style={styles.statItem}>
            <Text style={styles.statNumber}>
              {userData.certificates ? userData.certificates.length : 0}
            </Text>
            <Text style={styles.statLabel}>Certificados</Text>
          </View>
        </View>
      </Card>
    );
  };
  
  const renderEditForm = () => {
    return (
      <Card style={styles.profileCard}>
        <View style={styles.formHeader}>
          <Text style={styles.formTitle}>Editar perfil</Text>
        </View>
        
        <View style={styles.formGroup}>
          <Text style={styles.formLabel}>Nombre</Text>
          <TextInput
            style={styles.formInput}
            value={editedData.name}
            onChangeText={(text) => handleInputChange('name', text)}
            placeholder="Tu nombre completo"
          />
        </View>
        
        <View style={styles.formGroup}>
          <Text style={styles.formLabel}>Correo electrónico</Text>
          <TextInput
            style={styles.formInput}
            value={editedData.email}
            onChangeText={(text) => handleInputChange('email', text)}
            placeholder="correo@ejemplo.com"
            keyboardType="email-address"
          />
        </View>
        
        <View style={styles.formGroup}>
          <Text style={styles.formLabel}>Teléfono</Text>
          <TextInput
            style={styles.formInput}
            value={editedData.phone}
            onChangeText={(text) => handleInputChange('phone', text)}
            placeholder="Tu número de teléfono"
            keyboardType="phone-pad"
          />
        </View>
        
        <View style={styles.formGroup}>
          <Text style={styles.formLabel}>Bio</Text>
          <TextInput
            style={[styles.formInput, styles.bioInput]}
            value={editedData.bio}
            onChangeText={(text) => handleInputChange('bio', text)}
            placeholder="Cuéntanos sobre ti"
            multiline
            numberOfLines={4}
          />
        </View>
        
        <View style={styles.formActions}>
          <Button 
            title="Cancelar" 
            onPress={handleCancel}
            variant="outlined"
            style={styles.cancelButton}
          />
          <Button 
            title={saving ? "Guardando..." : "Guardar cambios"} 
            onPress={handleSave}
            disabled={saving}
            style={styles.saveButton}
          />
        </View>
      </Card>
    );
  };
  
  const renderPreferences = () => {
    return (
      <Card style={styles.sectionCard}>
        <View style={styles.sectionHeader}>
          <Text style={styles.sectionTitle}>Preferencias</Text>
        </View>
        
        <TouchableOpacity style={styles.preferenceItem}>
          <View style={styles.preferenceInfo}>
            <Ionicons name="notifications-outline" size={22} color={COLORS.text} />
            <Text style={styles.preferenceText}>Notificaciones</Text>
          </View>
          <View style={[
            styles.preferenceToggle,
            userData.preferences?.notifications && styles.preferenceToggleActive
          ]}>
            <View style={[
              styles.toggleCircle,
              userData.preferences?.notifications && styles.toggleCircleActive
            ]} />
          </View>
        </TouchableOpacity>
        
        <TouchableOpacity style={styles.preferenceItem}>
          <View style={styles.preferenceInfo}>
            <Ionicons name="moon-outline" size={22} color={COLORS.text} />
            <Text style={styles.preferenceText}>Modo oscuro</Text>
          </View>
          <View style={[
            styles.preferenceToggle,
            userData.preferences?.darkMode && styles.preferenceToggleActive
          ]}>
            <View style={[
              styles.toggleCircle,
              userData.preferences?.darkMode && styles.toggleCircleActive
            ]} />
          </View>
        </TouchableOpacity>
        
        <TouchableOpacity style={styles.preferenceItem}>
          <View style={styles.preferenceInfo}>
            <Ionicons name="language-outline" size={22} color={COLORS.text} />
            <Text style={styles.preferenceText}>Idioma</Text>
          </View>
          <View style={styles.preferenceValue}>
            <Text style={styles.valueText}>{userData.preferences?.language || 'Español'}</Text>
            <Ionicons name="chevron-forward" size={18} color={COLORS.muted} />
          </View>
        </TouchableOpacity>
        
        <TouchableOpacity 
          style={[styles.preferenceItem, styles.logoutItem]}
          onPress={handleLogout}
        >
          <View style={styles.preferenceInfo}>
            <Ionicons name="log-out-outline" size={22} color={COLORS.error} />
            <Text style={styles.logoutText}>Cerrar sesión</Text>
          </View>
        </TouchableOpacity>
      </Card>
    );
  };
  
  // Si está cargando, mostrar pantalla de carga
  if (loading) {
    return <LoadingState message="Cargando perfil..." />;
  }
  
  // Si hay un error, mostrar pantalla de error
  if (error) {
    return <ErrorState message={error} onRetry={() => navigation.goBack()} />;
  }
  
  // Si no hay datos del usuario, mostrar mensaje
  if (!userData) {
    return <ErrorState message="No se encontró información del perfil" onRetry={() => navigation.goBack()} />;
  }
  
  return (
    <SafeAreaView style={styles.container}>
      <ScrollView showsVerticalScrollIndicator={false}>
        <View style={styles.header}>
          <Text style={styles.headerTitle}>Perfil</Text>
        </View>
        
        {renderProfileInfo()}
        {renderAchievements()}
        {renderCertificates()}
        {renderPreferences()}
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  header: {
    padding: SPACING.md,
    backgroundColor: COLORS.white,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  headerTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    textAlign: 'center',
  },
  profileCard: {
    margin: SPACING.md,
    padding: 0,
    overflow: 'hidden',
  },
  headerSection: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  avatarContainer: {
    marginRight: SPACING.md,
  },
  avatar: {
    width: 80,
    height: 80,
    borderRadius: 40,
  },
  avatarPlaceholder: {
    width: 80,
    height: 80,
    borderRadius: 40,
    backgroundColor: COLORS.primary,
    justifyContent: 'center',
    alignItems: 'center',
  },
  avatarLetter: {
    fontSize: FONT_SIZE.xxl,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.white,
  },
  nameSection: {
    flex: 1,
  },
  userName: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: 2,
  },
  userRole: {
    fontSize: FONT_SIZE.md,
    color: COLORS.muted,
  },
  editButton: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: COLORS.lightGray,
    justifyContent: 'center',
    alignItems: 'center',
  },
  infoSection: {
    padding: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  infoItem: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: SPACING.sm,
  },
  infoText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    marginLeft: SPACING.sm,
  },
  bioSection: {
    padding: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  bioText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    lineHeight: 22,
  },
  statsContainer: {
    flexDirection: 'row',
    padding: SPACING.md,
  },
  statItem: {
    flex: 1,
    alignItems: 'center',
  },
  statNumber: {
    fontSize: FONT_SIZE.xl,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.primary,
    marginBottom: 4,
  },
  statLabel: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
  },
  statDivider: {
    width: 1,
    height: '80%',
    backgroundColor: COLORS.lightGray,
    alignSelf: 'center',
  },
  sectionCard: {
    margin: SPACING.md,
    marginTop: 0,
  },
  sectionHeader: {
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
    padding: SPACING.md,
  },
  sectionTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
  },
  achievementItem: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  achievementIcon: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: COLORS.primaryLight,
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: SPACING.md,
  },
  achievementInfo: {
    flex: 1,
  },
  achievementTitle: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.text,
    marginBottom: 2,
  },
  achievementDate: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
  },
  certificateItem: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  certificateInfo: {
    flex: 1,
    marginLeft: SPACING.md,
  },
  certificateTitle: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.text,
    marginBottom: 2,
  },
  certificateDate: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
  },
  downloadButton: {
    padding: SPACING.xs,
  },
  preferenceItem: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  preferenceInfo: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  preferenceText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    marginLeft: SPACING.md,
  },
  preferenceToggle: {
    width: 50,
    height: 28,
    borderRadius: 14,
    backgroundColor: COLORS.lightGray,
    justifyContent: 'center',
    paddingHorizontal: 2,
  },
  preferenceToggleActive: {
    backgroundColor: COLORS.primary,
  },
  toggleCircle: {
    width: 24,
    height: 24,
    borderRadius: 12,
    backgroundColor: COLORS.white,
    shadowColor: COLORS.black,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    elevation: 2,
  },
  toggleCircleActive: {
    alignSelf: 'flex-end',
  },
  preferenceValue: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  valueText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    marginRight: SPACING.xs,
  },
  logoutItem: {
    borderBottomWidth: 0,
  },
  logoutText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.error,
    marginLeft: SPACING.md,
  },
  formHeader: {
    padding: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  formTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
  },
  formGroup: {
    padding: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.lightGray,
  },
  formLabel: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  formInput: {
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
    padding: SPACING.sm,
    borderWidth: 1,
    borderColor: COLORS.lightGray,
    borderRadius: 8,
  },
  bioInput: {
    minHeight: 100,
    textAlignVertical: 'top',
  },
  formActions: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    padding: SPACING.md,
  },
  cancelButton: {
    flex: 1,
    marginRight: SPACING.sm,
  },
  saveButton: {
    flex: 1,
    marginLeft: SPACING.sm,
  },
  emptySection: {
    alignItems: 'center',
    padding: SPACING.lg,
  },
  emptyText: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.muted,
    marginTop: SPACING.md,
    marginBottom: SPACING.xs,
  },
  emptySubtext: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.muted,
    textAlign: 'center',
  },
});

export default ProfileScreen;
