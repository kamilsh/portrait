import Vue from 'vue'
import Router from 'vue-router'

import Login from '../components/Login'
import Home from '../components/Home'
import Personal from '../components/basic/Personal'
import Business from '../components/basic/Business'
import Action from '../components/basic/Action'
import Value from '../components/basic/Value'

Vue.use(Router)

const basic = {
  template: `<router-view></router-view>`
}

export default new Router({
  routes: [
    {
      path: '/',
      name: 'whitePage',
      redirect: '/home'
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/home',
      name: 'Home',
      component: Home,
      children: [
        {
          path: '/home/basic',
          name: 'basic',
          component: basic,
          children: [
            {
              path: '/home/basic/personal',
              name: 'personal',
              component: Personal
            },
            {
              path: '/home/basic/business',
              name: 'business',
              component: Business
            },
            {
              path: '/home/basic/action',
              name: 'action',
              component: Action
            },
            {
              path: '/home/basic/value',
              name: 'value',
              component: Value
            }
          ]
        }
      ]
    }
  ]
})
