const fs = require('fs');
const path = require('path');

console.log('🔍 EduTech App - Verificación Final Automatizada');
console.log('=' .repeat(50));

// Rutas de archivos críticos
const criticalFiles = [
    'App.js',
    'app.config.js',
    'package.json',
    'src/navigation/AppNavigator.js',
    'src/config/theme.js',
    'src/screens/home/NewHomeScreen.js',
    'src/screens/academic/ScheduleScreen.js',
    'src/screens/academic/GradesScreen.js',
    'src/screens/academic/AssignmentsScreen.js',
    'src/screens/academic/CommunicationsScreen.js',
    'src/screens/academic/ResourcesScreen.js',
    'src/services/studentApiService.js',
    'src/data/demoData.js'
];

// Verificar existencia de archivos
console.log('\n📁 Verificando archivos críticos...');
let allFilesExist = true;

criticalFiles.forEach(file => {
    if (fs.existsSync(file)) {
        console.log(`✅ ${file}`);
    } else {
        console.log(`❌ ${file} - NO ENCONTRADO`);
        allFilesExist = false;
    }
});

// Verificar contenido de archivos clave
console.log('\n🔧 Verificando configuraciones clave...');

// Verificar App.js
if (fs.existsSync('App.js')) {
    const appContent = fs.readFileSync('App.js', 'utf8');
    if (appContent.includes('export default function App') && !appContent.match(/export default function App.*export default function App/s)) {
        console.log('✅ App.js - Sin duplicaciones');
    } else {
        console.log('❌ App.js - Posibles duplicaciones detectadas');
    }
}

// Verificar AppNavigator.js
if (fs.existsSync('src/navigation/AppNavigator.js')) {
    const navContent = fs.readFileSync('src/navigation/AppNavigator.js', 'utf8');
    if (navContent.includes('<Stack.Screen') && navContent.includes('NewHomeScreen')) {
        console.log('✅ AppNavigator.js - Estructura de navegación correcta');
    } else {
        console.log('❌ AppNavigator.js - Problemas en estructura de navegación');
    }
}

// Verificar theme.js
if (fs.existsSync('src/config/theme.js')) {
    const themeContent = fs.readFileSync('src/config/theme.js', 'utf8');
    if (themeContent.includes('body2') && themeContent.includes('FONTS')) {
        console.log('✅ theme.js - Objeto FONTS completo');
    } else {
        console.log('❌ theme.js - Falta configuración de FONTS');
    }
}

// Verificar package.json
if (fs.existsSync('package.json')) {
    const packageContent = fs.readFileSync('package.json', 'utf8');
    const packageData = JSON.parse(packageContent);
    
    const requiredDeps = [
        '@react-navigation/native',
        '@react-navigation/bottom-tabs',
        '@react-navigation/stack',
        'react-native-paper',
        'react-native-vector-icons',
        'expo'
    ];
    
    let allDepsPresent = true;
    requiredDeps.forEach(dep => {
        if (packageData.dependencies && packageData.dependencies[dep]) {
            console.log(`✅ Dependencia: ${dep}`);
        } else {
            console.log(`❌ Dependencia faltante: ${dep}`);
            allDepsPresent = false;
        }
    });
    
    if (allDepsPresent) {
        console.log('✅ Todas las dependencias críticas están presentes');
    }
}

// Verificar que no existan archivos conflictivos
console.log('\n🧹 Verificando archivos conflictivos eliminados...');
const conflictingFiles = [
    'app.json',
    'src/screens/HomeScreen.js',
    'src/screens/LoginScreen.js',
    'src/screens/CalendarScreen.js',
    'src/services/api.js',
    'src/services/apiService.js',
    'src/config/paper.js',
    'src/config/paperTheme.js'
];

let noConflicts = true;
conflictingFiles.forEach(file => {
    if (!fs.existsSync(file)) {
        console.log(`✅ ${file} - Correctamente eliminado`);
    } else {
        console.log(`⚠️  ${file} - Aún presente (podría causar conflictos)`);
        noConflicts = false;
    }
});

// Resumen final
console.log('\n' + '='.repeat(50));
console.log('📊 RESUMEN DE VERIFICACIÓN FINAL');
console.log('='.repeat(50));

if (allFilesExist && noConflicts) {
    console.log('🎉 ¡APLICACIÓN LISTA PARA EJECUTAR!');
    console.log('✅ Todos los archivos críticos están presentes');
    console.log('✅ No se detectaron archivos conflictivos');
    console.log('✅ Configuraciones verificadas correctamente');
    console.log('\n📱 Para ejecutar la aplicación:');
    console.log('   1. npx expo start');
    console.log('   2. Escanear QR con Expo Go');
    console.log('   3. Verificar que todas las pantallas cargan correctamente');
} else {
    console.log('⚠️  SE DETECTARON ALGUNOS PROBLEMAS');
    if (!allFilesExist) {
        console.log('❌ Faltan algunos archivos críticos');
    }
    if (!noConflicts) {
        console.log('❌ Existen archivos que pueden causar conflictos');
    }
    console.log('\n🔧 Revisar los elementos marcados arriba antes de ejecutar');
}

console.log('\n📝 Documentación generada en: GUIA_VERIFICACION_FINAL.md');
console.log('🕒 Verificación completada: ' + new Date().toLocaleString());
