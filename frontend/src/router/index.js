import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: ()=> import("../views/Home")
  },
]


const router = new VueRouter({
  routes,
  mode: 'history',             /* 这两行用来消去URL中的 # */
  base: process.env.BASE_URL,
})

export default router
