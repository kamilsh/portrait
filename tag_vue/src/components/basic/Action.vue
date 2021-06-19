<template>
  <div>
    <el-table
      :data="data"
      border
      style="width: 100%">
      <el-table-column
        prop="name"
        label="标签"
        width="200">
      </el-table-column>
      <el-table-column
        prop="remark"
        label="tag"
        width="200">
      </el-table-column>
      <el-table-column
        prop="business"
        label="描述">
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="200">
        <template slot-scope="scope">
          <el-button @click="handleClick(scope.row)" type="text">分析</el-button>
        </template>
      </el-table-column>
    </el-table>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'Action',
  data () {
    return {
      data: []
      // data: [
      //   {
      //     'id': 33,
      //     'name': '最近登录',
      //     'industry': '标签',
      //     'rule': null,
      //     'business': '用户行为日志中的最近登录：1日内、7日内、14日内、1个月内',
      //     'level': 4,
      //     'pid': 5,
      //     'state': 4,
      //     'remark': 'recently_login_time'
      //   },
      //   {
      //     'id': 34,
      //     'name': '浏览页面',
      //     'industry': '标签',
      //     'rule': null,
      //     'business': '用户行为日志中的浏览页面：该商城所有页面',
      //     'level': 4,
      //     'pid': 5,
      //     'state': 4,
      //     'remark': 'view_page'
      //   },
      //   {
      //     'id': 37,
      //     'name': '访问频率',
      //     'industry': '标签',
      //     'rule': null,
      //     'business': '用户行为数据中的访问频率：经常、从不、偶尔、很少',
      //     'level': 4,
      //     'pid': 5,
      //     'state': 4,
      //     'remark': 'view_frequency'
      //   },
      //   {
      //     'id': 38,
      //     'name': '设备类型',
      //     'industry': '标签',
      //     'rule': null,
      //     'business': '用户行为数据中的设备类型：Window、Mac、Linux、Iphone、小米、华为',
      //     'level': 4,
      //     'pid': 5,
      //     'state': 4,
      //     'remark': 'device_type'
      //   },
      //   {
      //     'id': 39,
      //     'name': '浏览时段',
      //     'industry': '标签',
      //     'rule': null,
      //     'business': '用户行为数据中的浏览时段：1点~7点、8点~12点、13点~17点、18点~21点、22点~24点',
      //     'level': 4,
      //     'pid': 5,
      //     'state': 4,
      //     'remark': 'view_interval'
      //   },
      //   {
      //     'id': 40,
      //     'name': '近7日登录频率',
      //     'industry': '标签',
      //     'rule': null,
      //     'business': '用户行为数据中的登录频率:无、较少、一般、经常',
      //     'level': 4,
      //     'pid': 5,
      //     'state': 4,
      //     'remark': 'login_frequency'
      //   },
      //   {
      //     'id': 41,
      //     'name': '浏览商品',
      //     'industry': '标签',
      //     'rule': null,
      //     'business': '用户行为数据中的浏览商品：所有商品',
      //     'level': 4,
      //     'pid': 5,
      //     'state': 4,
      //     'remark': 'browse_products'
      //   },
      //   {
      //     'id': 42,
      //     'name': '购买商品',
      //     'industry': '标签',
      //     'rule': null,
      //     'business': '用户行为数据中的购买商品：所有商品',
      //     'level': 4,
      //     'pid': 5,
      //     'state': 4,
      //     'remark': 'purchase_goods'
      //   }
      // ]
    }
  },
  methods: {
    loadData () {
      this.$axios.get('/basic/action').then(resp => {
        if (resp.data.code === 200) {
          this.data = resp.data.data
          // console.log(this.tableData)
        } else {
          this.$alert(resp.data.message, '提示', {
            confirmButtonText: '确定'
          })
        }
      }).catch(failResponse => {
        this.$message('加载失败')
      })
    },
    handleClick (row) {
      // console.log(row)
      switch (row.name) {
        case '消费周期':
          this.$router.push('/home/basic/business/consumeCycle')
          break
        case '客单价':
          this.$router.push('/home/basic/business/avgOrderAmount')
          break
        case '支付方式':
          this.$router.push('/home/basic/business/paymentCode')
          break
        case '单笔最高':
          this.$router.push('/home/basic/business/maxOrderAmount')
          break
      }
    }
  },
  mounted () {
    this.loadData()
  }
}
</script>

<style scoped>

</style>
