module.exports = {
  expo: {
    name: "EduTech App",
    slug: "edutech-app",
    version: "1.0.0",
    orientation: "portrait",
    userInterfaceStyle: "light",
    platforms: ["ios", "android", "web"],
    
    // Configuración de activos
    assetBundlePatterns: [
      "**/*"
    ],
    
    // Configuración iOS
    ios: {
      supportsTablet: true,
      bundleIdentifier: "com.edutech.app"
    },
    
    // Configuración Android
    android: {
      adaptiveIcon: {
        backgroundColor: "#4f46e5"
      },
      package: "com.edutech.app"
    },
    
    // Configuración Web
    web: {
      bundler: "metro"
    },
    
    // Configuración de plugins
    plugins: [],
    
    // Configuración de esquema de URL
    scheme: "edutech",
    
    // Configuración de splash screen básica
    splash: {
      backgroundColor: "#4f46e5",
      resizeMode: "contain"
    },
    
    // Configuración básica
    primaryColor: "#4f46e5",
    
    // Configuración de updates
    updates: {
      fallbackToCacheTimeout: 0
    },
    
    // Configuración de experimentos
    experiments: {
      tsconfigPaths: true
    }
  }
};
