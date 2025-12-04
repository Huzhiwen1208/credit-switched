import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/home',
    name: 'Home',
    component: ()=> import("../views/Home")
  },
  {
    path: '/',
    name: 'login',
    component: ()=> import("../views/Login")
  },
  {
    path: '/register',
    name: 'register',
    component: ()=> import('../views/Register')
  },
  {
    path: '*',
    redirect: '/'
  }
]


const router = new VueRouter({
  routes,
  mode: 'history',             /* 这两行用来消去URL中的 # */
  base: process.env.BASE_URL,
})

export default router
