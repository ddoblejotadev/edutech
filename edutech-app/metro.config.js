// Learn more https://docs.expo.io/guides/customizing-metro
const { getDefaultConfig } = require('expo/metro-config');
const path = require('path');

const config = getDefaultConfig(__dirname);

// Añadir resolución de activos
config.resolver.assetExts = [...config.resolver.assetExts, 'db', 'mp3', 'ttf', 'obj', 'png', 'jpg', 'svg'];
config.transformer.assetPlugins = ['expo-asset/tools/hashAssetFiles'];
config.resolver.sourceExts = ['js', 'jsx', 'ts', 'tsx', 'cjs', 'json'];

// Asegurarse de que los activos de React Native se manejen correctamente
config.resolver.extraNodeModules = {
  'missing-asset-registry-path': path.resolve(__dirname, 'node_modules/react-native/Libraries/Image/AssetRegistry')
};

module.exports = config;
