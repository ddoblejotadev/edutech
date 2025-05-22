// Muestra la IP local en la red actual y actualiza automáticamente apiClient.ts
const os = require('os');
const fs = require('fs');
const path = require('path');

// Obtener el directorio actual
const __dirname = path.resolve();

function getIPAddress() {
  const interfaces = os.networkInterfaces();
  
  for (const devName in interfaces) {
    const iface = interfaces[devName];
    
    for (let i = 0; i < iface.length; i++) {
      const alias = iface[i];
      if (alias.family === 'IPv4' && !alias.internal) {
        return alias.address;
      }
    }
  }
  
  return '127.0.0.1';
}

const ipAddress = getIPAddress();

// Actualizar el archivo apiClient.ts
const apiClientPath = path.join(__dirname, '..', 'api', 'apiClient.ts');

try {
  let apiClientContent = fs.readFileSync(apiClientPath, 'utf8');
    // Buscar y reemplazar la IP en la línea correspondiente
  const regex = /(process\.env\.API_URL \|\| ['"]http:\/\/)([\d\.]+)(:\d+['"])/;
  const updatedContent = apiClientContent.replace(regex, `$1${ipAddress}$3`);
  
  fs.writeFileSync(apiClientPath, updatedContent, 'utf8');
  console.log('\n=====================================================');
  console.log(`Tu IP local es: ${ipAddress}`);
  console.log(`✅ IP actualizada automáticamente en apiClient.ts`);
  console.log('=====================================================');
  console.log('Para probar en dispositivo físico con Expo Go:');
  console.log('1. IP configurada automáticamente en apiClient.ts');
  console.log('2. Ambos dispositivos deben estar en la misma red Wi-Fi');
  console.log('3. Asegúrate que el puerto 8080 no esté bloqueado por el firewall');
  console.log('=====================================================\n');
} catch (error) {
  console.error('Error al actualizar apiClient.ts:', error.message);
  console.log('\n=====================================================');
  console.log(`Tu IP local es: ${ipAddress}`);
  console.log('❌ No se pudo actualizar automáticamente la IP en apiClient.ts');
  console.log('Por favor, actualiza manualmente la IP en api/apiClient.ts');
  console.log('=====================================================\n');
}
