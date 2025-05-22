// Muestra la IP local en la red actual
const os = require('os');

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

console.log('\n=====================================================');
console.log(`Tu IP local es: ${ipAddress}`);
console.log('=====================================================');
console.log('Para probar en dispositivo físico con Expo Go:');
console.log(`1. Actualiza la IP en api/apiClient.ts línea 16 a: ${ipAddress}`);
console.log('2. Ambos dispositivos deben estar en la misma red Wi-Fi');
console.log('3. Asegúrate que el puerto 8080 no esté bloqueado por el firewall');
console.log('=====================================================\n');
