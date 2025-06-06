import React, { useState, useContext, useEffect } from 'react';
import { View, StyleSheet, ScrollView, SafeAreaView } from 'react-native';
import { TextInput, Button, Text, Snackbar } from 'react-native-paper';
import { Ionicons } from '@expo/vector-icons';
import { AuthContext } from '../../context/AuthContext';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT } from '../../config/theme';

const RegisterScreen = ({ navigation }) => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [secureTextEntry, setSecureTextEntry] = useState(true);
  const [confirmSecureTextEntry, setConfirmSecureTextEntry] = useState(true);
  const [showSnackbar, setShowSnackbar] = useState(false);
  const [snackbarMsg, setSnackbarMsg] = useState('');
  
  const { register, loading, error } = useContext(AuthContext);
  
  // Mostrar error en Snackbar si existe
  useEffect(() => {
    if (error) {
      setSnackbarMsg(error);
      setShowSnackbar(true);
    }
  }, [error]);
    const validateEmail = (email) => {
    const re = /\S+@\S+\.\S+/;
    return re.test(email);
  };
  
  const handleRegister = async () => {
    // Validaciones
    if (!name || !email || !username || !password || !confirmPassword) {
      setSnackbarMsg('Por favor completa todos los campos');
      setShowSnackbar(true);
      return;
    }
    
    if (!validateEmail(email)) {
      setSnackbarMsg('Por favor ingresa un email válido');
      setShowSnackbar(true);
      return;
    }
    
    if (password !== confirmPassword) {
      setSnackbarMsg('Las contraseñas no coinciden');
      setShowSnackbar(true);
      return;
    }
    
    if (password.length < 6) {
      setSnackbarMsg('La contraseña debe tener al menos 6 caracteres');
      setShowSnackbar(true);
      return;
    }
    
    try {
      const result = await register(username, email, password, name);
      if (!result.success) {
        setSnackbarMsg(result.error || 'Ocurrió un error durante el registro');
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
      <ScrollView contentContainerStyle={styles.scrollContainer}>
        <View style={styles.headerContainer}>
          <Ionicons name="person-add-outline" size={40} color={COLORS.primary} />
          <Text style={styles.title}>Crear Cuenta</Text>
          <Text style={styles.subtitle}>Únete a nuestra plataforma de aprendizaje</Text>
        </View>
        
        <View style={styles.formContainer}>
          <TextInput
            label="Nombre completo"
            value={name}
            onChangeText={setName}
            style={styles.input}
            mode="outlined"
            left={<TextInput.Icon icon="account" />}
            disabled={loading}
          />
          
          <TextInput
            label="Email"
            value={email}
            onChangeText={setEmail}
            style={styles.input}
            mode="outlined"
            keyboardType="email-address"
            autoCapitalize="none"
            left={<TextInput.Icon icon="email" />}
            disabled={loading}
          />
          
          <TextInput
            label="Usuario"
            value={username}
            onChangeText={setUsername}
            style={styles.input}
            mode="outlined"
            autoCapitalize="none"
            left={<TextInput.Icon icon="account-outline" />}
            disabled={loading}
          />
          
          <TextInput
            label="Contraseña"
            value={password}
            onChangeText={setPassword}
            secureTextEntry={secureTextEntry}
            style={styles.input}
            mode="outlined"
            left={<TextInput.Icon icon="lock-outline" />}
            disabled={loading}
            right={
              <TextInput.Icon 
                icon={secureTextEntry ? "eye" : "eye-off"} 
                onPress={() => setSecureTextEntry(!secureTextEntry)}
              />
            }
          />
          
          <TextInput
            label="Confirmar contraseña"
            value={confirmPassword}
            onChangeText={setConfirmPassword}
            secureTextEntry={confirmSecureTextEntry}
            style={styles.input}
            mode="outlined"
            left={<TextInput.Icon icon="lock-check-outline" />}
            disabled={loading}
            right={
              <TextInput.Icon 
                icon={confirmSecureTextEntry ? "eye" : "eye-off"} 
                onPress={() => setConfirmSecureTextEntry(!confirmSecureTextEntry)}
              />
            }
          />
          
          <Button 
            mode="contained" 
            onPress={handleRegister}
            style={styles.button}
            loading={loading}
            disabled={loading}
          >
            Registrarse
          </Button>
          
          <Button 
            mode="text" 
            onPress={() => navigation.navigate('Login')}
            style={styles.loginButton}
            disabled={loading}
            icon="login"
          >
            ¿Ya tienes una cuenta? Inicia sesión
          </Button>
        </View>
      </ScrollView>
      
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
  scrollContainer: {
    flexGrow: 1,
    padding: SPACING.md,
  },
  headerContainer: {
    alignItems: 'center',
    marginVertical: SPACING.xl,
  },
  title: {
    fontSize: FONT_SIZE.xxl,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.primary,
    marginTop: SPACING.md,
    marginBottom: SPACING.xs,
  },
  subtitle: {
    fontSize: FONT_SIZE.md,
    color: COLORS.muted,
  },
  formContainer: {
    marginBottom: SPACING.xl,
  },
  input: {
    marginBottom: SPACING.md,
    backgroundColor: COLORS.white,
  },
  button: {
    padding: SPACING.sm,
    marginTop: SPACING.md,
    backgroundColor: COLORS.primary,
  },
  loginButton: {
    marginTop: SPACING.xl,
  },
});

export default RegisterScreen;
