const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
  ,devServer: {
    proxy: {
      '^/apply': {
        target: process.env.VUE_APP_API_URL || 'http://192.168.199.188:8081',
        changeOrigin: true,
        secure: false
      }
    }
  }
})
