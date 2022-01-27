module.exports = {
    devServer: {
      // https: true,
      port: 3000,
      open: true,
      // proxy: {
      //   '/api/v1' : {
      //     target: 'https://localhost:8443/'
      //   },
      //   '/webjars': {
      //     target: 'https://localhost:8443/'
      //   },
      //   '/groupcall': {
      //     target: 'https://localhost:8443/'
      //   },
      //   '/upload': {
      //     target: 'https://localhost:8443/'
      //   }
      // },
      historyApiFallback: true,
      hot: true
    },
    // css: {
    //   requireModuleExtension: false
    // },
    transpileDependencies: [
      'element-plus'
    ],
    lintOnSave: false,
    
    outputDir: "../backend/src/main/resources/dist"
    
  }
  