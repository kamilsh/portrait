<template>
  <div>
    <div class="block">
      <!--      <span class="demonstration">组合标签</span>-->
      <el-cascader
        ref="cascade"
        placeholder="搜索或选择标签"
        :options="options"
        :props="{ multiple: true }"
        @change="handleChange"
        filterable
        clearable></el-cascader>
      <el-button @click="search">查询</el-button>
    </div>
    <json-viewer :value="jsonData" copyable expand-depth=2></json-viewer>
  </div>
</template>

<script>
export default {
  name: 'Combination',
  data () {
    return {
      valueList: [],
      // options: [
      //   {
      //     'children': [
      //       {
      //         'children': [
      //           {
      //             'label': '男',
      //             'value': 50
      //           },
      //           {
      //             'label': '女',
      //             'value': 51
      //           }
      //         ],
      //         'label': '性别',
      //         'value': 8
      //       },
      //       {
      //         'children': [
      //           {
      //             'label': '50后',
      //             'value': 52
      //           },
      //           {
      //             'label': '60后',
      //             'value': 53
      //           },
      //           {
      //             'label': '70后',
      //             'value': 54
      //           },
      //           {
      //             'label': '80后',
      //             'value': 55
      //           },
      //           {
      //             'label': '90后',
      //             'value': 56
      //           },
      //           {
      //             'label': '00后',
      //             'value': 57
      //           },
      //           {
      //             'label': '10后',
      //             'value': 58
      //           },
      //           {
      //             'label': '20后',
      //             'value': 59
      //           }
      //         ],
      //         'label': '年龄段',
      //         'value': 9
      //       },
      //       {
      //         'children': [
      //           {
      //             'label': '群众',
      //             'value': 80
      //           },
      //           {
      //             'label': '党员',
      //             'value': 81
      //           },
      //           {
      //             'label': '无党派人士',
      //             'value': 82
      //           }
      //         ],
      //         'label': '政治面貌',
      //         'value': 13
      //       },
      //       {
      //         'children': [
      //           {
      //             'label': '学生',
      //             'value': 83
      //           },
      //           {
      //             'label': '公务员',
      //             'value': 84
      //           },
      //           {
      //             'label': '军人',
      //             'value': 85
      //           },
      //           {
      //             'label': '警察',
      //             'value': 86
      //           },
      //           {
      //             'label': '教师',
      //             'value': 87
      //           },
      //           {
      //             'label': '工人',
      //             'value': 88
      //           }
      //         ],
      //         'label': '职业',
      //         'value': 14
      //       },
      //       {
      //         'children': [
      //           {
      //             'label': '未婚',
      //             'value': 89
      //           },
      //           {
      //             'label': '已婚',
      //             'value': 90
      //           },
      //           {
      //             'label': '离异',
      //             'value': 91
      //           }
      //         ],
      //         'label': '婚姻状况',
      //         'value': 15
      //       },
      //       {
      //         'children': [
      //           {
      //             'label': '白羊座',
      //             'value': 103
      //           },
      //           {
      //             'label': '金牛座',
      //             'value': 104
      //           },
      //           {
      //             'label': '双子座',
      //             'value': 105
      //           },
      //           {
      //             'label': '巨蟹座',
      //             'value': 106
      //           },
      //           {
      //             'label': '狮子座',
      //             'value': 107
      //           },
      //           {
      //             'label': '处女座',
      //             'value': 108
      //           },
      //           {
      //             'label': '天秤座',
      //             'value': 109
      //           },
      //           {
      //             'label': '天蝎座',
      //             'value': 110
      //           },
      //           {
      //             'label': '射手座',
      //             'value': 111
      //           },
      //           {
      //             'label': '摩羯座',
      //             'value': 112
      //           },
      //           {
      //             'label': '水瓶座',
      //             'value': 113
      //           },
      //           {
      //             'label': '双鱼座',
      //             'value': 114
      //           }
      //         ],
      //         'label': '星座',
      //         'value': 18
      //       },
      //       {
      //         'children': [
      //           {
      //             'label': '中国大陆',
      //             'value': 122
      //           },
      //           {
      //             'label': '中国香港',
      //             'value': 123
      //           },
      //           {
      //             'label': '中国澳门',
      //             'value': 124
      //           },
      //           {
      //             'label': '中国台湾',
      //             'value': 125
      //           },
      //           {
      //             'label': '其他',
      //             'value': 126
      //           }
      //         ],
      //         'label': '国籍',
      //         'value': 20
      //       }
      //     ],
      //     'label': '人口属性',
      //     'value': 3
      //   },
      //   {
      //     'children': [
      //       {
      //         'children': [
      //           {
      //             'label': '近7天',
      //             'value': 127
      //           },
      //           {
      //             'label': '近14天',
      //             'value': 128
      //           },
      //           {
      //             'label': '近30天',
      //             'value': 129
      //           },
      //           {
      //             'label': '近60天',
      //             'value': 130
      //           },
      //           {
      //             'label': '近90天',
      //             'value': 131
      //           }
      //         ],
      //         'label': '消费周期',
      //         'value': 21
      //       },
      //       {
      //         'children': [
      //           {
      //             'label': '1-999',
      //             'value': 139
      //           },
      //           {
      //             'label': '1000-2999',
      //             'value': 140
      //           },
      //           {
      //             'label': '3000-4999',
      //             'value': 141
      //           },
      //           {
      //             'label': '5000-9999',
      //             'value': 142
      //           }
      //         ],
      //         'label': '客单价',
      //         'value': 23
      //       },
      //       {
      //         'children': [
      //           {
      //             'label': '支付宝',
      //             'value': 143
      //           },
      //           {
      //             'label': '微信',
      //             'value': 144
      //           },
      //           {
      //             'label': '储蓄卡',
      //             'value': 145
      //           },
      //           {
      //             'label': '信用卡',
      //             'value': 146
      //           }
      //         ],
      //         'label': '支付方式',
      //         'value': 24
      //       },
      //       {
      //         'children': [
      //           {
      //             'label': '1-999',
      //             'value': 147
      //           },
      //           {
      //             'label': '1000-2999',
      //             'value': 148
      //           },
      //           {
      //             'label': '3000-4999',
      //             'value': 149
      //           },
      //           {
      //             'label': '5000-9999',
      //             'value': 150
      //           }
      //         ],
      //         'label': '单笔最高',
      //         'value': 25
      //       }
      //     ],
      //     'label': '商业属性',
      //     'value': 4
      //   },
      //   {
      //     'children': [
      //       {
      //         'children': [
      //           {
      //             'label': '1天内',
      //             'value': 169
      //           },
      //           {
      //             'label': '7天内',
      //             'value': 170
      //           },
      //           {
      //             'label': '14天内',
      //             'value': 171
      //           },
      //           {
      //             'label': '30天内',
      //             'value': 172
      //           }
      //         ],
      //         'label': '最近登录',
      //         'value': 33
      //       },
      //       {
      //         'children': [
      //           {
      //             'label': '登录页',
      //             'value': 173
      //           },
      //           {
      //             'label': '首页',
      //             'value': 174
      //           },
      //           {
      //             'label': '分类页',
      //             'value': 175
      //           },
      //           {
      //             'label': '商品页',
      //             'value': 176
      //           },
      //           {
      //             'label': '我的订单页',
      //             'value': 177
      //           },
      //           {
      //             'label': '订单物流页',
      //             'value': 178
      //           }
      //         ],
      //         'label': '浏览页面',
      //         'value': 34
      //       },
      //       {
      //         'children': [
      //           {
      //             'label': '经常',
      //             'value': 182
      //           },
      //           {
      //             'label': '偶尔',
      //             'value': 183
      //           },
      //           {
      //             'label': '很少',
      //             'value': 184
      //           },
      //           {
      //             'label': '从不',
      //             'value': 185
      //           }
      //         ],
      //         'label': '访问频率',
      //         'value': 37
      //       },
      //       {
      //         'children': [
      //           {
      //             'label': 'Windows',
      //             'value': 186
      //           },
      //           {
      //             'label': 'Mac',
      //             'value': 187
      //           },
      //           {
      //             'label': 'Linux',
      //             'value': 188
      //           },
      //           {
      //             'label': 'Android',
      //             'value': 189
      //           },
      //           {
      //             'label': 'IOS',
      //             'value': 190
      //           }
      //         ],
      //         'label': '设备类型',
      //         'value': 38
      //       },
      //       {
      //         'children': [
      //           {
      //             'label': '1点-7点',
      //             'value': 191
      //           },
      //           {
      //             'label': '8点-12点',
      //             'value': 192
      //           },
      //           {
      //             'label': '13点-17点',
      //             'value': 193
      //           },
      //           {
      //             'label': '18点-21点',
      //             'value': 194
      //           },
      //           {
      //             'label': '22点-24点',
      //             'value': 195
      //           }
      //         ],
      //         'label': '浏览时段',
      //         'value': 39
      //       },
      //       {
      //         'children': [
      //           {
      //             'label': '无',
      //             'value': 196
      //           },
      //           {
      //             'label': '较少',
      //             'value': 197
      //           },
      //           {
      //             'label': '一般',
      //             'value': 198
      //           },
      //           {
      //             'label': '经常',
      //             'value': 199
      //           }
      //         ],
      //         'label': '近7日登录频率',
      //         'value': 40
      //       },
      //       {
      //         'children': [],
      //         'label': '浏览商品',
      //         'value': 41
      //       },
      //       {
      //         'children': [],
      //         'label': '购买商品',
      //         'value': 42
      //       }
      //     ],
      //     'label': '行为属性',
      //     'value': 5
      //   }
      // ]
      options: [],
      // jsonData: [
      //   {
      //     'rowkey': '459',
      //     'info': [
      //       {
      //         'AgeGroup': '80后'
      //       },
      //       {
      //         'browse_products': '海尔电热水器EC5002-R'
      //       },
      //       {
      //         'browse_products': '海尔冰箱BCD-225SFM'
      //       },
      //       {
      //         'browse_products': '海尔电热水器ES60H-Q1(ZE)'
      //       },
      //       {
      //         'browse_products': '海尔电热水器ES80H-M5(NT)'
      //       },
      //       {
      //         'browse_products': '海尔波轮洗衣机XQS60-Z9288 至爱'
      //       },
      //       {
      //         'browse_products': '卡萨帝冰吧 LC-162E'
      //       },
      //       {
      //         'browse_products': '海尔波轮洗衣机XQB55-M1268 关爱'
      //       },
      //       {
      //         'browse_products': '海尔电饭煲XQG60-812 家家爱'
      //       },
      //       {
      //         'browse_products': '海尔电热水器EC5002-D'
      //       },
      //       {
      //         'browse_products': '海尔电热水器EC6003-I'
      //       },
      //       {
      //         'browse_products': '海尔微波炉 MZC-2070M1'
      //       },
      //       {
      //         'browse_products': '海尔冰箱BCD-118TMPA'
      //       },
      //       {
      //         'browse_products': '海尔冰箱BC-93TMPF'
      //       },
      //       {
      //         'browse_products': '海尔滚筒洗衣机XQG70-1000J'
      //       },
      //       {
      //         'browse_products': '统帅投影仪iSee mini 1S'
      //       },
      //       {
      //         'browse_products': '海尔冰箱BCD-216SDN'
      //       },
      //       {
      //         'browse_products': '海尔燃气热水器JSQ24-UT(12T)'
      //       },
      //       {
      //         'browse_products': '海尔电热水器EC6002-D'
      //       },
      //       {
      //         'browse_products': '海尔吸油烟机CXW-200-C150'
      //       },
      //       {
      //         'browse_products': '海尔冰箱BCD-206STPA'
      //       },
      //       {
      //         'browse_products': '海尔冰箱BCD-649WADV'
      //       },
      //       {
      //         'browse_products': '海尔电热水器EC6002-R'
      //       },
      //       {
      //         'browse_products': '海尔波轮洗衣机XQB70-M1268 关爱'
      //       },
      //       {
      //         'browse_products': '海尔电热水器EC5002-Q6'
      //       },
      //       {
      //         'constellation': '白羊座'
      //       },
      //       {
      //         'device_type': 'iOS:39'
      //       },
      //       {
      //         'device_type': 'Windows:204'
      //       },
      //       {
      //         'device_type': 'Android:138'
      //       },
      //       {
      //         'device_type': 'Linux:5'
      //       },
      //       {
      //         'device_type': 'other:1'
      //       },
      //       {
      //         'email': 't17lmmo@0355.net'
      //       },
      //       {
      //         'gender': '男'
      //       },
      //       {
      //         'job': '学生'
      //       },
      //       {
      //         'login_frequency': '一般'
      //       },
      //       {
      //         'marital_status': '已婚'
      //       },
      //       {
      //         'nationality': '中国大陆'
      //       },
      //       {
      //         'political_status': '群众'
      //       },
      //       {
      //         'recently_login_time': '7天内'
      //       },
      //       {
      //         'view_frequency': '经常'
      //       },
      //       {
      //         'view_interval': '1:00-7:00'
      //       },
      //       {
      //         'view_page': '其它:227'
      //       },
      //       {
      //         'view_page': '主页:55'
      //       },
      //       {
      //         'view_page': '订单页:25'
      //       },
      //       {
      //         'view_page': '登录页:6'
      //       },
      //       {
      //         'view_page': '商品页:61'
      //       },
      //       {
      //         'view_page': '分类页:13'
      //       }
      //     ]
      //   },
      //   {
      //     'rowkey': '73',
      //     'info': [
      //       {
      //         'AgeGroup': '00后'
      //       },
      //       {
      //         'avg_order_amount': '2097.4250'
      //       },
      //       {
      //         'browse_products': '海尔冰箱BCD-206SM'
      //       },
      //       {
      //         'browse_products': '海尔电热水器ES50H-Z4(ZE)'
      //       },
      //       {
      //         'browse_products': '海尔电热水器EC4002-Q6'
      //       },
      //       {
      //         'browse_products': 'MOOKA彩电48A5'
      //       },
      //       {
      //         'browse_products': '海尔电热水器EC6002-Q6'
      //       },
      //       {
      //         'browse_products': '海尔微波炉 MZC-2070M1'
      //       },
      //       {
      //         'browse_products': '统帅投影仪iSee mini 1S'
      //       },
      //       {
      //         'browse_products': '海尔净水机HRO5015-5(WD)'
      //       },
      //       {
      //         'browse_products': '海尔冰箱BCD-216SDN'
      //       },
      //       {
      //         'browse_products': '统帅冰箱BCD-206LST'
      //       },
      //       {
      //         'browse_products': '海尔燃气热水器JSQ24-UT(12T)'
      //       },
      //       {
      //         'browse_products': '海尔吸油烟机CXW-200-C150'
      //       },
      //       {
      //         'browse_products': 'MOOKA彩电55A5'
      //       },
      //       {
      //         'browse_products': '海尔冰箱BCD-539WT(惠民)'
      //       },
      //       {
      //         'browse_products': '海尔电热水器EC6002-R'
      //       },
      //       {
      //         'browse_products': '海尔波轮洗衣机XQB70-M1268 关爱'
      //       },
      //       {
      //         'browse_products': '海尔空调SKFR-50LW/02WAA22A(香槟金)套机-1'
      //       },
      //       {
      //         'browse_products': '海尔消毒柜ZQD100F-E65S2'
      //       },
      //       {
      //         'constellation': '白羊座'
      //       },
      //       {
      //         'consume_cycle': '7日'
      //       },
      //       {
      //         'device_type': 'Android:134'
      //       },
      //       {
      //         'device_type': 'iOS:39'
      //       },
      //       {
      //         'device_type': 'other:4'
      //       },
      //       {
      //         'device_type': 'Windows:190'
      //       },
      //       {
      //         'device_type': 'Mac:1'
      //       },
      //       {
      //         'device_type': 'Linux:7'
      //       },
      //       {
      //         'email': '2d7s23@126.com'
      //       },
      //       {
      //         'gender': '男'
      //       },
      //       {
      //         'job': '学生'
      //       },
      //       {
      //         'login_frequency': '一般'
      //       },
      //       {
      //         'marital_status': '未婚'
      //       },
      //       {
      //         'max_order_amount': '23370'
      //       },
      //       {
      //         'nationality': '中国大陆'
      //       },
      //       {
      //         'paymentCode': 'alipay'
      //       },
      //       {
      //         'political_status': '无党派人士'
      //       },
      //       {
      //         'purchase_goods': 'BCD-216SDN'
      //       },
      //       {
      //         'purchase_goods': 'EC6002-R'
      //       },
      //       {
      //         'purchase_goods': 'XPB70-227HS 关爱'
      //       },
      //       {
      //         'purchase_goods': 'XQB55-Z1269'
      //       },
      //       {
      //         'purchase_goods': '统帅投影仪众筹isee mini 1S'
      //       },
      //       {
      //         'purchase_goods': 'XQB55-M1268 关爱'
      //       },
      //       {
      //         'purchase_goods': 'XQB50-M1268 关爱'
      //       },
      //       {
      //         'purchase_goods': '统帅彩电 D58LW7110'
      //       },
      //       {
      //         'purchase_goods': '海尔波轮洗衣机XQB50-M1268 关爱'
      //       },
      //       {
      //         'purchase_goods': 'MOOKA彩电U55H7'
      //       },
      //       {
      //         'purchase_goods': '海尔波轮洗衣机XQS60-828F 至爱'
      //       },
      //       {
      //         'purchase_goods': '海尔波轮洗衣机XQB50-728E'
      //       },
      //       {
      //         'purchase_goods': 'CXW-200-E750C2'
      //       },
      //       {
      //         'purchase_goods': '统帅电视盒子 iSee box'
      //       },
      //       {
      //         'purchase_goods': 'XQG70-B12866'
      //       },
      //       {
      //         'purchase_goods': '海尔吸尘器ZW1201C'
      //       },
      //       {
      //         'purchase_goods': 'XQB50-M1258关爱'
      //       },
      //       {
      //         'purchase_goods': '海尔空调KFR-35GW/05GDC23A套机'
      //       },
      //       {
      //         'purchase_goods': 'XPM30-2008'
      //       },
      //       {
      //         'purchase_goods': 'XQS60-Z9288 至爱'
      //       },
      //       {
      //         'purchase_goods': 'CXW-180-JS721'
      //       },
      //       {
      //         'purchase_goods': 'BCD-539WT(惠民)'
      //       },
      //       {
      //         'purchase_goods': '海尔波轮洗衣机XQB55-M1268 关爱'
      //       },
      //       {
      //         'purchase_goods': 'EC5002-Q6'
      //       },
      //       {
      //         'purchase_goods': 'MOOKA彩电U50H7'
      //       },
      //       {
      //         'purchase_goods': '海尔电暖器HN2003E'
      //       },
      //       {
      //         'purchase_goods': '海尔波轮洗衣机XQB60-M1038'
      //       },
      //       {
      //         'purchase_goods': '48A5'
      //       },
      //       {
      //         'purchase_goods': '统帅冰箱BCD-305WLV'
      //       },
      //       {
      //         'purchase_goods': 'BCD-196TMPI'
      //       },
      //       {
      //         'purchase_goods': '海尔电热水器EC5002-R'
      //       },
      //       {
      //         'purchase_goods': 'D48MF7000'
      //       },
      //       {
      //         'purchase_goods': '海尔电视D48MF7000'
      //       },
      //       {
      //         'purchase_goods': '海尔波轮洗衣机 XQB60-728E[海尔套餐 XQB60-728E+EC6002-Q6]'
      //       },
      //       {
      //         'purchase_goods': 'EC5002-R'
      //       },
      //       {
      //         'purchase_goods': '海尔滚筒洗衣机XQG70-B12866'
      //       },
      //       {
      //         'purchase_goods': '海尔数码相框DPF-801D（雪白色）'
      //       },
      //       {
      //         'purchase_goods': '海尔除螨仪ZB403F'
      //       },
      //       {
      //         'purchase_goods': 'iSee box'
      //       },
      //       {
      //         'purchase_goods': '海尔冰箱BCD-118TMPA'
      //       },
      //       {
      //         'purchase_goods': 'BCD-206LST'
      //       },
      //       {
      //         'purchase_goods': 'XQS70-Z9288 至爱'
      //       },
      //       {
      //         'purchase_goods': '32EU3000'
      //       },
      //       {
      //         'purchase_goods': 'iwash-1w'
      //       },
      //       {
      //         'purchase_goods': '海尔吸尘器ZW1401B'
      //       },
      //       {
      //         'purchase_goods': '海尔电风扇FSJ4018E'
      //       },
      //       {
      //         'purchase_goods': '统帅彩电众筹D50MF5000'
      //       },
      //       {
      //         'purchase_goods': '海尔电热水器EC6002-R'
      //       },
      //       {
      //         'purchase_goods': '统帅冰箱BCD-649WLD'
      //       },
      //       {
      //         'purchase_goods': 'MOOKA彩电U42H7030'
      //       },
      //       {
      //         'purchase_goods': 'XQG70-1012 家家爱'
      //       },
      //       {
      //         'purchase_goods': 'LES40H-C(E)'
      //       },
      //       {
      //         'purchase_goods': 'KFR-26GW/09QDA22A(白)套机'
      //       },
      //       {
      //         'purchase_goods': 'XQG70-1000J'
      //       },
      //       {
      //         'purchase_goods': '海尔电熨斗YD1301 虚网'
      //       },
      //       {
      //         'purchase_goods': '海尔彩电众筹D50MF5000'
      //       },
      //       {
      //         'purchase_goods': 'BCD-133ES'
      //       },
      //       {
      //         'purchase_goods': 'XQB50-728E'
      //       },
      //       {
      //         'purchase_goods': 'XQB60-M1038'
      //       },
      //       {
      //         'purchase_goods': 'BCD-225SEVF-ES'
      //       },
      //       {
      //         'purchase_goods': 'XQB60-728E'
      //       },
      //       {
      //         'purchase_goods': '海尔空调KFR-26GW/07ZFT23A-DS套机'
      //       },
      //       {
      //         'purchase_goods': '海尔燃气热水器JSQ20-UT(12T)'
      //       },
      //       {
      //         'purchase_goods': '海尔电热水器EC6002-D'
      //       },
      //       {
      //         'purchase_goods': '海尔冰箱BCD-225SLDA'
      //       },
      //       {
      //         'purchase_goods': 'EC5002-D'
      //       },
      //       {
      //         'purchase_goods': '海尔冰箱BCD-206STPA'
      //       },
      //       {
      //         'purchase_goods': 'BCD-206STPA'
      //       },
      //       {
      //         'purchase_goods': '海尔吸油烟机CXW-200-C150'
      //       },
      //       {
      //         'purchase_goods': '海尔电热水器ES60H-M5(NT)'
      //       },
      //       {
      //         'purchase_goods': '海尔原汁机众筹HYZ-101A'
      //       },
      //       {
      //         'purchase_goods': 'XPB65-1186BS AM'
      //       },
      //       {
      //         'purchase_goods': '海尔波轮洗衣机XQB60-728E'
      //       },
      //       {
      //         'recently_login_time': '7天内'
      //       },
      //       {
      //         'view_frequency': '经常'
      //       },
      //       {
      //         'view_interval': '1:00-7:00'
      //       },
      //       {
      //         'view_page': '登录页:4'
      //       },
      //       {
      //         'view_page': '订单页:25'
      //       },
      //       {
      //         'view_page': '分类页:24'
      //       },
      //       {
      //         'view_page': '商品页:46'
      //       },
      //       {
      //         'view_page': '其它:224'
      //       },
      //       {
      //         'view_page': '主页:52'
      //       }
      //     ]
      //   }
      // ]
      jsonData: []
    }
  },
  methods: {
    handleChange (item) {
      this.valueList = item
    },
    search () {
      console.log(this.valueList)
      const _this = this
      this.$axios
        .post('/search/combination', {
          valueList: _this.valueList
        })
        .then(resp => {
          if (resp.data.code === 200) {
            _this.jsonData = resp.data.data
          } else {
            this.$alert(resp.data.message, '提示', {
              confirmButtonText: '确定'
            })
          }
        })
        .catch(failResponse => {
          this.$message('服务器异常')
        })
    },
    loadTag () {
      this.$axios.get('/combination').then(resp => {
        if (resp.data.code === 200) {
          this.options = resp.data.data
        } else {
          this.$alert(resp.data.message, '提示', {
            confirmButtonText: '确定'
          })
        }
      }).catch(failResponse => {
        this.$message('加载失败')
      })
    }
  },
  mounted () {
    this.loadTag()
  }
}
</script>

<style scoped>
</style>
