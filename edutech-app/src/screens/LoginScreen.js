import React, { useState, useContext } from 'react';
import { View, StyleSheet, Alert } from 'react-native';
import { TextInput, Button, Text } from 'react-native-paper';
import { AuthContext } from '../context/AuthContext';

export default function LoginScreen({ navigation }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [secureTextEntry, setSecureTextEntry] = useState(true);
  
  const { login } = useContext(AuthContext);

  const handleLogin = async () => {
    if (!username || !password) {
      Alert.alert('Error', 'Por favor completa todos los campos');
      return;
    }

    setLoading(true);
    try {
      const result = await login(username, password);
      if (!result.success) {
        Alert.alert('Error', result.error);
      } else {
        // La navegación se manejará automáticamente por el contexto
        navigation.navigate('Home');
      }
    } catch (error) {
      Alert.alert('Error', 'Ocurrió un error al intentar iniciar sesión');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Iniciar Sesión</Text>
      
      <TextInput
        label="Usuario"
        value={username}
        onChangeText={setUsername}
        style={styles.input}
        autoCapitalize="none"
        mode="outlined"
      />
      
      <TextInput
        label="Contraseña"
        value={password}
        onChangeText={setPassword}
        secureTextEntry={secureTextEntry}
        style={styles.input}
        mode="outlined"
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
      
      <View style={styles.registerContainer}>
        <Text>¿No tienes una cuenta? </Text>
        <Button 
          mode="text" 
          onPress={() => navigation.navigate('Register')}
          style={styles.registerButton}
        >
          Regístrate
        </Button>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    justifyContent: 'center',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
    textAlign: 'center',
  },
  input: {
    marginBottom: 16,
  },
  button: {
    marginTop: 16,
    paddingVertical: 8,
  },
  registerContainer: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 20,
  },
  registerButton: {
    margin: 0,
    padding: 0,
  }
});
