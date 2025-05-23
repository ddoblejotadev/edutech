module.exports = function(api) {
  api.cache(true);
  return {
    presets: ['babel-preset-expo'],
    plugins: [
      // Asegurarse de que los activos se procesen correctamente
      ['module-resolver', {
        alias: {
          'missing-asset-registry-path': './node_modules/react-native/Libraries/Image/AssetRegistry'
        }
      }]
    ]
  };
};
