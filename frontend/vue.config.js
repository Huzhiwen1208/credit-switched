const { defineConfig } = require('@vue/cli-service')


module.exports = defineConfig({
  transpileDependencies: true,

  //把 devServer 配置直接加在这里，和 transpileDependencies 并列
  devServer: {
    port: 8080, // 前端跑在 8080
    proxy: {
      '/api': {
        // 1. 这里一定要填你【虚拟机】的 IP，不能填 localhost
        target: 'http://192.168.136.188:8081', 
        changeOrigin: true,
        pathRewrite: {
          // 2. 这里的坑请注意：
          // 如果你的后端接口就是 /api/login，那就用下面这行（不用改）：
          '^/api': '' 
          
          // 如果你的后端接口是 /login (没有api前缀)，请注释掉上面那行，改用下面这就行：
          // '^/api': '' 
        }
      }
    }
  }
})
