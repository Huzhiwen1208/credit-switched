import Vue from 'vue'
import App from './App.vue'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'
import router from './router'
// eslint-disable-next-line no-unused-vars
import { v4 as uuidv4 } from 'uuid'
// eslint-disable-next-line no-unused-vars
import { ref, reactive, onMounted } from 'vue'
Vue.config.productionTip = false

Vue.use(ElementUI)
Vue.prototype.$axios = axios

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
