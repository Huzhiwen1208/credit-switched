import Vue from 'vue'
import App from './App.vue'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'
import router from './router'

Vue.config.productionTip = false

Vue.use(ElementUI)
// 设置默认的 API 路径为后端服务的网络地址（可替换为您的后端IP或使用环境变量）
axios.defaults.baseURL = process.env.VUE_APP_API_URL || 'http://localhost:8081'
Vue.prototype.$axios = axios

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
