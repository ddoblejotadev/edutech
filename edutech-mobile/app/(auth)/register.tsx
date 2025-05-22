import React, { useState } from 'react';
import { 
  View, 
  TouchableOpacity, 
  ScrollView,
  // Comentar importaciones no utilizadas
  // StyleSheet,
  // Image,
  // KeyboardAvoidingView,
  // Platform
} from 'react-native';

// Eliminar importaciones no utilizadas
import { Stack, Link, router } from 'expo-router';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { Ionicons } from '@expo/vector-icons';

import { ThemedText } from '@/components/ThemedText';
import { ThemedView } from '@/components/ThemedView';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { useAuth } from '../../contexts/AuthContext';
import { Colors } from '@/constants/Colors';
import { useColorScheme } from '@/hooks/useColorScheme';
import { styles } from './authStyles';

interface UsuarioRegistro {
  nombres: string;
  apellidos: string;
  rut: string;
  email: string;
  password: string;
  rol: string;
}

export default function RegisterScreen() {
  const [nombres, setNombres] = useState('');
  const [apellidos, setApellidos] = useState('');
  const [rut, setRut] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [formErrors, setFormErrors] = useState<Record<string, string>>({});
  
  const { register, isLoading, error } = useAuth();
  const insets = useSafeAreaInsets();
  
  const colorScheme = useColorScheme();
  const colors = Colors[colorScheme ?? 'light'];
  
  const validateForm = () => {
    const errors: Record<string, string> = {};
    
    if (!nombres.trim()) errors.nombres = 'El nombre es requerido';
    if (!apellidos.trim()) errors.apellidos = 'Los apellidos son requeridos';
    if (!rut.trim()) errors.rut = 'El RUT es requerido';
    if (!email.trim()) errors.email = 'El email es requerido';
    else if (!/\S+@\S+\.\S+/.test(email)) errors.email = 'Email inválido';
    
    if (!password) errors.password = 'La contraseña es requerida';
    else if (password.length < 6) errors.password = 'La contraseña debe tener al menos 6 caracteres';
    
    if (password !== confirmPassword) errors.confirmPassword = 'Las contraseñas no coinciden';
    
    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };
  
  const handleRegister = async () => {
    if (validateForm()) {
      const userData: UsuarioRegistro = {
        nombres,
        apellidos,
        rut,
        email,
        password,
        rol: 'ESTUDIANTE' // Rol por defecto
      };
      
      await register(userData);
    }
  };

  return (
    <ThemedView style={[styles.container, { paddingBottom: insets.bottom, paddingTop: insets.top }]}>
      <Stack.Screen options={{ headerShown: false }} />
      
      <ScrollView 
        showsVerticalScrollIndicator={false}
        contentContainerStyle={{ flexGrow: 1 }}
      >
        <TouchableOpacity 
          style={{ marginBottom: 20 }}
          onPress={() => router.back()}
          activeOpacity={0.7}
        >
          <Ionicons name="chevron-back" size={24} color={colors.text} />
        </TouchableOpacity>
        
        <View style={[styles.header, { marginTop: 0 }]}>
          <ThemedText type="heading1" style={styles.title}>
            Crear cuenta
          </ThemedText>
          
          <ThemedText type="body" secondary={true} style={styles.subtitle}>
            Completa tus datos para registrarte en EduTech
          </ThemedText>
        </View>
        
        {error && (
          <ThemedView 
            style={[styles.errorContainer, { backgroundColor: colors.error + '15' }]}
            variant="primary"
          >
            <ThemedText style={[styles.errorText, { color: colors.error }]}>
              {error}
            </ThemedText>
          </ThemedView>
        )}
        
        <View style={styles.form}>
          <Input
            label="Nombres"
            placeholder="Ingresa tus nombres"
            value={nombres}
            onChangeText={(text) => {
              setNombres(text);
              setFormErrors(prev => ({ ...prev, nombres: '' }));
            }}
            error={formErrors.nombres}
            leftIcon={<Ionicons name="person" size={18} color={colors.icon} />}
            variant="outline"
            containerStyle={{ marginBottom: 16 }}
          />
          
          <Input
            label="Apellidos"
            placeholder="Ingresa tus apellidos"
            value={apellidos}
            onChangeText={(text) => {
              setApellidos(text);
              setFormErrors(prev => ({ ...prev, apellidos: '' }));
            }}
            error={formErrors.apellidos}
            leftIcon={<Ionicons name="person" size={18} color={colors.icon} />}
            variant="outline"
            containerStyle={{ marginBottom: 16 }}
          />
          
          <Input
            label="RUT"
            placeholder="Ej: 12345678-9"
            value={rut}
            onChangeText={(text) => {
              setRut(text);
              setFormErrors(prev => ({ ...prev, rut: '' }));
            }}
            error={formErrors.rut}
            leftIcon={<Ionicons name="card" size={18} color={colors.icon} />}
            variant="outline"
            containerStyle={{ marginBottom: 16 }}
          />
          
          <Input
            label="Email"
            placeholder="Ingresa tu email"
            value={email}
            onChangeText={(text) => {
              setEmail(text);
              setFormErrors(prev => ({ ...prev, email: '' }));
            }}
            error={formErrors.email}
            leftIcon={<Ionicons name="mail" size={18} color={colors.icon} />}
            keyboardType="email-address"
            autoCapitalize="none"
            variant="outline"
            containerStyle={{ marginBottom: 16 }}
          />
          
          <Input
            label="Contraseña"
            placeholder="Mínimo 6 caracteres"
            value={password}
            onChangeText={(text) => {
              setPassword(text);
              setFormErrors(prev => ({ ...prev, password: '' }));
            }}
            error={formErrors.password}
            leftIcon={<Ionicons name="lock-closed" size={18} color={colors.icon} />}
            secureTextEntry
            secureToggle={true}
            variant="outline"
            containerStyle={{ marginBottom: 16 }}
          />
          
          <Input
            label="Confirmar Contraseña"
            placeholder="Repite tu contraseña"
            value={confirmPassword}
            onChangeText={(text) => {
              setConfirmPassword(text);
              setFormErrors(prev => ({ ...prev, confirmPassword: '' }));
            }}
            error={formErrors.confirmPassword}
            leftIcon={<Ionicons name="lock-closed" size={18} color={colors.icon} />}
            secureTextEntry
            secureToggle={true}
            variant="outline"
            containerStyle={{ marginBottom: 24 }}
          />
          
          <Button
            title="Registrarse"
            onPress={handleRegister}
            loading={isLoading}
            variant="primary"
            fullWidth={true}
            size="large"
            style={styles.button}
          />
        </View>
        
        <View style={styles.signUpContainer}>
          <ThemedText secondary={true}>
            ¿Ya tienes una cuenta?
          </ThemedText>
          <Link href="/login" asChild>
            <TouchableOpacity activeOpacity={0.7}>
              <ThemedText style={[styles.signUpText, { color: colors.tint }]}>
                Iniciar sesión
              </ThemedText>
            </TouchableOpacity>
          </Link>
        </View>
      </ScrollView>
    </ThemedView>
  );
}
