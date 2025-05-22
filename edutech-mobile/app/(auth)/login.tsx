import React, { useState } from 'react';
import { TouchableOpacity, View, Image, KeyboardAvoidingView, Platform } from 'react-native';
import { Stack, Link } from 'expo-router';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { Ionicons } from '@expo/vector-icons';

import { ThemedText } from '@/components/ThemedText';
import { ThemedView } from '@/components/ThemedView';
import { Button } from '@/components/ui/Button';
import { Input } from '@/components/ui/Input';
import { IconSymbol } from '@/components/ui/IconSymbol';
import { useAuth } from '../../contexts/AuthContext';
import { Colors } from '@/constants/Colors';
import { useColorScheme } from '@/hooks/useColorScheme';
import { styles } from './authStyles';

export default function LoginScreen() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [usernameError, setUsernameError] = useState('');
  const [passwordError, setPasswordError] = useState('');
  const { signIn, isLoading, error } = useAuth();
  const insets = useSafeAreaInsets();
  
  const colorScheme = useColorScheme();
  const colors = Colors[colorScheme ?? 'light'];
  
  const validateForm = () => {
    let isValid = true;
    
    if (!username.trim()) {
      setUsernameError('El nombre de usuario es obligatorio');
      isValid = false;
    } else {
      setUsernameError('');
    }
    
    if (!password.trim()) {
      setPasswordError('La contraseña es obligatoria');
      isValid = false;
    } else if (password.length < 6) {
      setPasswordError('La contraseña debe tener al menos 6 caracteres');
      isValid = false;
    } else {
      setPasswordError('');
    }
    
    return isValid;
  };
  
  const handleLogin = async () => {
    if (validateForm()) {
      await signIn(username, password);
    }
  };

  return (
    <ThemedView style={[styles.container, { paddingBottom: insets.bottom, paddingTop: insets.top }]}>
      <Stack.Screen options={{ headerShown: false }} />
      
      <KeyboardAvoidingView
        style={{ flex: 1 }}
        behavior={Platform.OS === 'ios' ? 'padding' : undefined}
        keyboardVerticalOffset={Platform.OS === 'ios' ? 64 : 0}
      >
        <View style={styles.header}>
          <Image
            source={require('@/assets/images/icon.png')}
            style={styles.logo}
          />
          
          <ThemedText type="heading1" style={styles.title}>
            Bienvenido a EduTech
          </ThemedText>
          
          <ThemedText type="body" secondary={true} style={styles.subtitle}>
            Inicia sesión para continuar aprendiendo
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
            label="Usuario o Email"
            placeholder="Ingresa tu usuario o email"
            value={username}
            onChangeText={(text) => {
              setUsername(text);
              if (text.trim()) setUsernameError('');
            }}
            error={usernameError}
            leftIcon={<IconSymbol name="person.fill" size={18} color={colors.icon} />}
            autoCapitalize="none"
            variant="outline"
            containerStyle={{ marginBottom: 16 }}
          />
          
          <Input
            label="Contraseña"
            placeholder="Ingresa tu contraseña"
            value={password}
            onChangeText={(text) => {
              setPassword(text);
              if (text.trim()) setPasswordError('');
            }}
            error={passwordError}
            leftIcon={<IconSymbol name="lock.fill" size={18} color={colors.icon} />}
            secureTextEntry
            secureToggle={true}
            variant="outline"
            containerStyle={{ marginBottom: 8 }}
          />          <Link href="/register" asChild>
            <TouchableOpacity 
              style={styles.forgotPassword}
              activeOpacity={0.7}
            >
              <ThemedText style={{ color: colors.tint }}>
                ¿Olvidaste tu contraseña?
              </ThemedText>
            </TouchableOpacity>
          </Link>
          
          <Button
            title="Iniciar sesión"
            onPress={handleLogin}
            loading={isLoading}
            variant="primary"
            fullWidth={true}
            size="large"
            style={styles.button}
          />
        </View>
        
        <View style={styles.orDivider}>
          <View style={[styles.dividerLine, { backgroundColor: colors.divider }]} />
          <ThemedText type="caption" secondary={true} style={styles.dividerText}>
            O continúa con
          </ThemedText>
          <View style={[styles.dividerLine, { backgroundColor: colors.divider }]} />
        </View>
        
        <View style={styles.socialButtonsContainer}>
          <TouchableOpacity 
            style={[styles.socialButton, { backgroundColor: colors.backgroundSecondary }]}
            activeOpacity={0.7}
          >
            <Ionicons name="logo-google" size={22} color={colors.text} />
          </TouchableOpacity>
          
          <TouchableOpacity 
            style={[styles.socialButton, { backgroundColor: colors.backgroundSecondary }]}
            activeOpacity={0.7}
          >
            <Ionicons name="logo-apple" size={22} color={colors.text} />
          </TouchableOpacity>
          
          <TouchableOpacity 
            style={[styles.socialButton, { backgroundColor: colors.backgroundSecondary }]}
            activeOpacity={0.7}
          >
            <Ionicons name="logo-facebook" size={22} color={colors.text} />
          </TouchableOpacity>
        </View>
        
        <View style={styles.signUpContainer}>
          <ThemedText secondary={true}>
            ¿No tienes una cuenta?
          </ThemedText>
          <Link href="/register" asChild>
            <TouchableOpacity activeOpacity={0.7}>
              <ThemedText style={[styles.signUpText, { color: colors.tint }]}>
                Regístrate
              </ThemedText>
            </TouchableOpacity>
          </Link>        </View>
      </KeyboardAvoidingView>
    </ThemedView>
  );
}
