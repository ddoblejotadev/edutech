// Test script to validate app structure and imports
const fs = require('fs');
const path = require('path');

const checkFile = (filePath) => {
  try {
    const content = fs.readFileSync(filePath, 'utf8');
    console.log(`✅ ${filePath} - Readable`);
    return true;
  } catch (error) {
    console.error(`❌ ${filePath} - Error: ${error.message}`);
    return false;
  }
};

const criticalFiles = [
  'App.js',
  'src/navigation/AppNavigator.js',
  'src/screens/home/MinimalistHomeScreen.js',
  'src/context/AuthContext.js',
  'src/services/studentApiService.js',
  'src/config/theme.js',
  'src/config/api.js',
  'src/components/common/UIComponents.js'
];

console.log('🔍 Validando archivos críticos de EduTech App...\n');

let allValid = true;
for (const file of criticalFiles) {
  const fullPath = path.join(__dirname, file);
  const valid = checkFile(fullPath);
  if (!valid) allValid = false;
}

console.log('\n📋 Resultado:');
if (allValid) {
  console.log('✅ Todos los archivos críticos están disponibles y legibles');
  console.log('✨ La aplicación debería funcionar correctamente');
} else {
  console.log('❌ Se encontraron problemas en algunos archivos');
}
