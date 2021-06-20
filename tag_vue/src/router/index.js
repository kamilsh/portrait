import Vue from 'vue'
import Router from 'vue-router'

import Login from '../components/common/Login'
import Home from '../components/common/Home'
import Personal from '../components/basic/Personal'
import Business from '../components/basic/Business'
import Action from '../components/basic/Action'
import Portrait from '../components/portrait/Portrait'
import Tree from '../components/basic/Tree'
import Gender from '../components/basic/charts/Gender'
import AgeGroup from '../components/basic/charts/AgeGroup'
import PoliticalStatus from '../components/basic/charts/PoliticalStatus'
import Job from '../components/basic/charts/Job'
import MaritalStatus from '../components/basic/charts/MaritalStatus'
import Constellation from '../components/basic/charts/Constellation'
import Nationality from '../components/basic/charts/Nationality'
import Combination from '../components/combination/Combination'
import ConsumeCycle from '../components/basic/charts/ConsumeCycle'
import AvgOrderAmount from '../components/basic/charts/AvgOrderAmount'
import PaymentCode from '../components/basic/charts/PaymentCode'
import MaxOrderAmount from '../components/basic/charts/MaxOrderAmount'
import ViewPage from '../components/basic/charts/ViewPage'
import viewFrequency from '../components/basic/charts/viewFrequency'
import deviceType from '../components/basic/charts/deviceType'
import viewInterval from '../components/basic/charts/viewInterval'
import loginFrequency from '../components/basic/charts/loginFrequency'
import RecentlyLoginTime from '../components/basic/charts/RecentlyLoginTime'

Vue.use(Router)

const basic = {
  template: `<router-view></router-view>`
}

export default new Router({
  routes: [
    {
      path: '/',
      name: 'whitePage',
      redirect: '/login'
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
                },
                {
                  path: '/home/basic/personal/politicalStatus',
                  name: 'politicalStatus',
                  component: PoliticalStatus
                },
                {
                  path: '/home/basic/personal/job',
                  name: 'Job',
                  component: Job
                },
                {
                  path: '/home/basic/personal/maritalStatus',
                  name: 'maritalStatus',
                  component: MaritalStatus
                },
                {
                  path: '/home/basic/personal/constellation',
                  name: 'Constellation',
                  component: Constellation
                },
                {
                  path: '/home/basic/personal/nationality',
                  name: 'nationality',
                  component: Nationality
                }
              ]
            },
            {
              path: '/home/basic/business',
              name: 'business',
              component: Business,
              children: [
                {
                  path: '/home/basic/business/consumeCycle',
                  name: 'consumeCycle',
                  component: ConsumeCycle
                },
                {
                  path: '/home/basic/business/avgOrderAmount',
                  name: 'avgOrderAmount',
                  component: AvgOrderAmount
                },
                {
                  path: '/home/basic/business/paymentCode',
                  name: 'paymentCode',
                  component: PaymentCode
                },
                {
                  path: '/home/basic/business/maxOrderAmount',
                  name: 'maxOrderAmount',
                  component: MaxOrderAmount
                }
              ]
            },
            {
              path: '/home/basic/action',
              name: 'action',
              component: Action,
              children: [
                {
                  path: '/home/basic/action/viewPage',
                  name: 'viewPage',
                  component: ViewPage
                },
                {
                  path: '/home/basic/action/viewFrequency',
                  name: 'viewFrequency',
                  component: viewFrequency
                },
                {
                  path: '/home/basic/action/deviceType',
                  name: 'deviceType',
                  component: deviceType
                },
                {
                  path: '/home/basic/action/viewInterval',
                  name: 'viewInterval',
                  component: viewInterval
                },
                {
                  path: '/home/basic/action/loginFrequency',
                  name: 'loginFrequency',
                  component: loginFrequency
                },
                {
                  path: '/home/basic/action/recentlyLoginTime',
                  name: 'recentlyLoginTime',
                  component: RecentlyLoginTime
                }
              ]
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
        },
        {
          path: '/home/combination',
          name: 'combination',
          component: Combination
        }
      ]
    }
  ]
})
