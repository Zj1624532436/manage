import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/home',
    name: 'Home',
    component: () => import( '../views/Home.vue'),
    children:[
      {
        path: '/index',
        name: '首页',
        component: () => import( '../views/Index.vue')
      },
      {
        path: '/center',
        name: '个人中心',
        component:()=>import('@/views/center/index.vue')
      }
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: () => import( '../views/Login.vue')
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
