module.exports = {
  name: "edutech-app",
  slug: "edutech-app",
  version: "1.0.0",
  orientation: "portrait",
  userInterfaceStyle: "light",
  // Configuración más explícita de los patrones de activos
  assetBundlePatterns: [
    "**/*",
    "assets/images/*",
    "assets/fonts/*",
    "node_modules/react-native/Libraries/LogBox/UI/LogBoxImages/*.png"
  ],
  ios: {
    supportsTablet: true
  },
  android: {
    adaptiveIcon: {
      backgroundColor: "#ffffff"
    }
  },
  web: {},
  experiments: {
    tsconfigPaths: true
  }
};
