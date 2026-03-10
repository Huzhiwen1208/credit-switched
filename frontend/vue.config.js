const { defineConfig } = require('@vue/cli-service')


module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8080,
    proxy: {
      '/api': {
        /** TODO3: 修改这个为配置参数， ./run.sh <ip地址> 就可以直接替换这里，比如: ./run.sh 192.168.121.188 */
        target: 'http://192.168.121.188:8081', 
        changeOrigin: true,
        pathRewrite: {
          '^/api': '' 
        }
      }
    }
  }
})
