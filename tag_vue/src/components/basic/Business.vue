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
  name: 'Business',
  data () {
    return {
      data: []
      // data: [
      //   {
      //     'id': 21,
      //     'name': '消费周期',
      //     'industry': '标签',
      //     'rule': null,
      //     'business': '用户的消费周期：7日、2周、1月、2月、3月、4月、5月、6月、1年',
      //     'level': 4,
      //     'pid': 4,
      //     'state': 4,
      //     'remark': 'consume_cycle'
      //   },
      //   {
      //     'id': 23,
      //     'name': '客单价',
      //     'industry': '标签',
      //     'rule': null,
      //     'business': '用户订单数据的客单价：1~999、1000~2999、3000~4999、5000~9999',
      //     'level': 4,
      //     'pid': 4,
      //     'state': 4,
      //     'remark': 'avg_order_amount'
      //   },
      //   {
      //     'id': 24,
      //     'name': '支付方式',
      //     'industry': '标签',
      //     'rule': null,
      //     'business': '用户订单的支付方式：支付宝、微信、储蓄卡、信用卡',
      //     'level': 4,
      //     'pid': 4,
      //     'state': 4,
      //     'remark': 'paymentCode'
      //   },
      //   {
      //     'id': 25,
      //     'name': '单笔最高',
      //     'industry': '标签',
      //     'rule': null,
      //     'business': '用户订单数据中的金额最高的订单：1~999、1000~2999、3000~4999、5000~9999',
      //     'level': 4,
      //     'pid': 4,
      //     'state': 4,
      //     'remark': 'max_order_amount'
      //   }
      // ]
    }
  },
  methods: {
    loadData () {
      this.$axios.get('/basic/business').then(resp => {
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
