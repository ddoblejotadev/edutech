import React, { useState, useContext, useEffect } from 'react';
import { View, StyleSheet, Image, TouchableOpacity, Alert, SafeAreaView, ActivityIndicator } from 'react-native';
import { TextInput, Button, Text, Snackbar } from 'react-native-paper';
import { Ionicons } from '@expo/vector-icons';
import { AuthContext } from '../../context/AuthContext';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT } from '../../config/theme';

const LoginScreen = ({ navigation }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [secureTextEntry, setSecureTextEntry] = useState(true);
  const [showSnackbar, setShowSnackbar] = useState(false);
  const [snackbarMsg, setSnackbarMsg] = useState('');
  
  const { login, loading, error } = useContext(AuthContext);
  
  // Mostrar error en Snackbar si existe
  useEffect(() => {
    if (error) {
      setSnackbarMsg(error);
      setShowSnackbar(true);
    }
  }, [error]);
  
  const handleLogin = async () => {
    if (!username || !password) {
      setSnackbarMsg('Por favor ingresa tu usuario y contraseña');
      setShowSnackbar(true);
      return;
    }
    
    try {
      const result = await login(username, password);
      if (!result.success) {
        setSnackbarMsg(result.error || 'Credenciales incorrectas. Inténtalo de nuevo.');
        setShowSnackbar(true);
      }
    } catch (error) {
      setSnackbarMsg('Error de conexión. Inténtalo más tarde.');
      setShowSnackbar(true);
      console.error(error);
    }
  };
    return (
    <SafeAreaView style={styles.container}>
      <View style={styles.content}>
        <View style={styles.logoContainer}>
          <View style={styles.logoWrapper}>
            <Ionicons name="school" size={60} color={COLORS.primary} />
          </View>
          <Text style={styles.logoText}>Edu-Tech</Text>
          <Text style={styles.subtitle}>Plataforma de aprendizaje en línea</Text>
        </View>
        
        <View style={styles.formContainer}>
          <TextInput
            label="Usuario"
            value={username}
            onChangeText={setUsername}
            style={styles.input}
            mode="outlined"
            autoCapitalize="none"
            left={<TextInput.Icon icon="account" />}
            disabled={loading}
          />
          
          <TextInput
            label="Contraseña"
            value={password}
            onChangeText={setPassword}
            secureTextEntry={secureTextEntry}
            style={styles.input}
            mode="outlined"
            disabled={loading}
            left={<TextInput.Icon icon="lock" />}
            right={
              <TextInput.Icon 
                icon={secureTextEntry ? "eye" : "eye-off"} 
                onPress={() => setSecureTextEntry(!secureTextEntry)}
              />
            }
          />
          
          <Button 
            mode="contained" 
            onPress={handleLogin}
            style={styles.button}
            loading={loading}
            disabled={loading}
          >
            Iniciar Sesión
          </Button>
          
          <TouchableOpacity 
            style={styles.forgotPassword}
            onPress={() => {
              // Implementar recuperación de contraseña
              setSnackbarMsg('Función de recuperación de contraseña disponible próximamente');
              setShowSnackbar(true);
            }}
          >
            <Text style={styles.forgotPasswordText}>¿Olvidaste tu contraseña?</Text>
          </TouchableOpacity>
          
          <View style={styles.registerContainer}>
            <Text style={styles.registerQuestion}>¿No tienes una cuenta? </Text>
            <TouchableOpacity onPress={() => navigation.navigate('Register')}>
              <Text style={styles.registerText}>Regístrate</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
      
      <Snackbar
        visible={showSnackbar}
        onDismiss={() => setShowSnackbar(false)}
        duration={3000}
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
  content: {
    flex: 1,
    padding: SPACING.md,
    justifyContent: 'center',
  },
  logoContainer: {
    alignItems: 'center',
    marginBottom: SPACING.xxl,
  },
  logoWrapper: {
    width: 100,
    height: 100,
    borderRadius: 50,
    backgroundColor: COLORS.primary + '15',
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: SPACING.md,
  },
  logoText: {
    fontSize: FONT_SIZE.xxxl,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.primary,
    marginBottom: SPACING.xs,
  },
  subtitle: {
    fontSize: FONT_SIZE.md,
    color: COLORS.muted,
  },
  formContainer: {
    marginBottom: SPACING.xxl,
  },
  input: {
    marginBottom: SPACING.md,
    backgroundColor: COLORS.white,
  },
  button: {
    padding: SPACING.sm,
    backgroundColor: COLORS.primary,
    marginTop: SPACING.md,
  },
  forgotPassword: {
    alignItems: 'center',
    marginTop: SPACING.lg,
  },
  forgotPasswordText: {
    color: COLORS.primary,
    fontSize: FONT_SIZE.sm,
  },
  registerContainer: {
    flexDirection: 'row',
    justifyContent: 'center',
    marginTop: SPACING.xl,
  },
  registerQuestion: {
    color: COLORS.text,
  },
  registerText: {
    color: COLORS.primary,
    fontWeight: FONT_WEIGHT.bold,
  },
});

export default LoginScreen;
