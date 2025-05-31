import React, { useState, useContext, useEffect } from 'react';
import { View, StyleSheet, TouchableOpacity, SafeAreaView, ScrollView, TextInput } from 'react-native';
import { Text, Button, Snackbar } from 'react-native-paper';
import { Ionicons } from '@expo/vector-icons';
import { AuthContext } from '../../context/AuthContext';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT } from '../../config/theme';

const DEMO_MODE = true; // Activar modo demo

const LoginScreen = ({ navigation }) => {
  const [email, setEmail] = useState('juan.perez@alumno.edu');
  const [password, setPassword] = useState('demo123');
  const [loading, setLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [showSnackbar, setShowSnackbar] = useState(false);
  const [snackbarMsg, setSnackbarMsg] = useState('');
  
  const { login, error, clearError } = useContext(AuthContext);

  useEffect(() => {
    if (error) {
      setSnackbarMsg(error);
      setShowSnackbar(true);
      clearError(); // Limpiar error después de mostrarlo
    }
  }, [error, clearError]);

  const handleLogin = async () => {
    if (!email || !password) {
      setSnackbarMsg('Por favor ingresa tu usuario y contraseña');
      setShowSnackbar(true);
      return;
    }
    
    if (!email.includes('@')) {
      setSnackbarMsg('Por favor ingresa un email válido');
      setShowSnackbar(true);
      return;
    }
    
    setLoading(true);
    try {
      const result = await login(email, password);
      if (!result.success) {
        setSnackbarMsg(result.error || 'Error de autenticación');
        setShowSnackbar(true);
      }
      // Si es exitoso, el contexto manejará la navegación automáticamente
    } catch (error) {
      console.error('Error en handleLogin:', error);
      setSnackbarMsg('Error inesperado. Inténtalo más tarde.');
      setShowSnackbar(true);
    } finally {
      setLoading(false);
    }
  };

  const fillDemoCredentials = () => {
    setEmail('juan.perez@alumno.edu');
    setPassword('demo123');
    setSnackbarMsg('✅ Credenciales demo rellenadas. Presiona "Iniciar Sesión" para continuar.');
    setShowSnackbar(true);
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scrollContent}>
        {/* Header */}
        <View style={styles.header}>
          <Text style={styles.title}>EduTech</Text>
          <Text style={styles.subtitle}>Accede a tu cuenta</Text>
          
          {/* Mensaje de modo demo */}
          {DEMO_MODE && (
            <TouchableOpacity style={styles.demoNotice} onPress={fillDemoCredentials}>
              <Ionicons name="information-circle" size={20} color="#3b82f6" />
              <Text style={styles.demoText}>
                Modo Demo - Toca aquí para usar credenciales demo
              </Text>
            </TouchableOpacity>
          )}
        </View>

        {/* Formulario */}
        <View style={styles.form}>
          <View style={styles.inputGroup}>
            <Text style={styles.label}>Correo electrónico</Text>
            <View style={styles.inputContainer}>
              <Ionicons name="mail-outline" size={20} color={COLORS.muted} style={styles.inputIcon} />
              <TextInput
                style={styles.input}
                value={email}
                onChangeText={setEmail}
                placeholder="usuario@ejemplo.com"
                keyboardType="email-address"
                autoCapitalize="none"
                placeholderTextColor={COLORS.muted}
              />
            </View>
          </View>

          <View style={styles.inputGroup}>
            <Text style={styles.label}>Contraseña</Text>
            <View style={styles.inputContainer}>
              <Ionicons name="lock-closed-outline" size={20} color={COLORS.muted} style={styles.inputIcon} />
              <TextInput
                style={styles.input}
                value={password}
                onChangeText={setPassword}
                placeholder="Tu contraseña"
                secureTextEntry={!showPassword}
                placeholderTextColor={COLORS.muted}
              />
              <TouchableOpacity
                style={styles.eyeIcon}
                onPress={() => setShowPassword(!showPassword)}
              >
                <Ionicons 
                  name={showPassword ? "eye-off-outline" : "eye-outline"} 
                  size={20} 
                  color={COLORS.muted} 
                />
              </TouchableOpacity>
            </View>
          </View>

          <TouchableOpacity style={styles.forgotPassword}>
            <Text style={styles.forgotPasswordText}>¿Olvidaste tu contraseña?</Text>
          </TouchableOpacity>

          <Button
            mode="contained"
            onPress={handleLogin}
            loading={loading}
            disabled={loading}
            style={styles.loginButton}
          >
            {loading ? "Iniciando sesión..." : "Iniciar sesión"}
          </Button>
        </View>

        {/* Footer */}
        <View style={styles.footer}>
          <Text style={styles.footerText}>¿No tienes cuenta? </Text>
          <TouchableOpacity onPress={() => navigation.navigate('Register')}>
            <Text style={styles.signUpText}>Regístrate</Text>
          </TouchableOpacity>
        </View>
      </ScrollView>
      
      <Snackbar
        visible={showSnackbar}
        onDismiss={() => setShowSnackbar(false)}
        duration={4000}
        action={{
          label: 'Cerrar',
          onPress: () => setShowSnackbar(false),
        }}
      >
        {snackbarMsg}
      </Snackbar>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.white,
  },
  scrollContent: {
    flexGrow: 1,
    justifyContent: 'center',
    padding: SPACING.md,
  },
  header: {
    alignItems: 'center',
    marginBottom: 48,
  },
  title: {
    fontSize: 40,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.primary,
    marginBottom: 4,
  },
  subtitle: {
    fontSize: FONT_SIZE.md,
    color: COLORS.muted,
  },
  demoNotice: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#eff6ff',
    padding: 12,
    borderRadius: 8,
    marginTop: 16,
    borderWidth: 1,
    borderColor: '#bfdbfe',
  },
  demoText: {
    marginLeft: 8,
    fontSize: 14,
    color: '#3b82f6',
    fontWeight: '500',
  },
  form: {
    marginBottom: 48,
  },
  inputGroup: {
    marginBottom: SPACING.md,
  },
  label: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.text,
    marginBottom: 8,
  },
  inputContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    borderWidth: 1,
    borderColor: COLORS.border,
    borderRadius: 8,
    padding: 10,
    backgroundColor: COLORS.white,
  },
  inputIcon: {
    marginRight: 10,
  },
  input: {
    flex: 1,
    height: 40,
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
  },
  eyeIcon: {
    padding: 8,
  },
  forgotPassword: {
    alignItems: 'center',
    marginTop: 24,
  },
  forgotPasswordText: {
    color: COLORS.primary,
    fontSize: FONT_SIZE.sm,
  },
  loginButton: {
    padding: 8,
    backgroundColor: COLORS.primary,
    marginTop: 16,
  },
  footer: {
    flexDirection: 'row',
    justifyContent: 'center',
    marginTop: 32,
  },
  footerText: {
    color: COLORS.text,
  },
  signUpText: {
    color: COLORS.primary,
    fontWeight: FONT_WEIGHT.bold,
  },
});

export default LoginScreen;
