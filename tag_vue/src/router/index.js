import Vue from 'vue'
import Router from 'vue-router'

import Login from '../components/common/Login'
import Home from '../components/common/Home'
import Personal from '../components/basic/Personal'
import Business from '../components/basic/Business'
import Action from '../components/basic/Action'
import Value from '../components/basic/Value'
import Portrait from '../components/portrait/Portrait'
import Tree from '../components/basic/Tree'
import Gender from '../components/basic/charts/Gender'
import AgeGroup from '../components/basic/charts/AgeGroup'

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
              component: Personal,
              children: [
                {
                  path: '/home/basic/personal/gender',
                  name: 'gender',
                  component: Gender
                },
                {
                  path: '/home/basic/personal/ageGroup',
                  name: 'ageGroup',
                  component: AgeGroup
                }
              ]
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
        },
        {
          path: '/home/portrait',
          name: 'portrait',
          component: Portrait
        },
        {
          path: '/home/tree',
          name: 'tree',
          component: Tree
        }
      ]
    },
    {
      path: '/test',
      name: 'test',
      component: AgeGroup
    }
  ]
})
