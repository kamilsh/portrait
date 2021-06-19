<template>
  <div id="paymentCode" :data="data" style="width: 65vw;height: 65vh;"></div>
</template>

<script>
export default {
  name: 'PaymentCode',
  data () {
    return {
      data: []
    }
  },
  methods: {
    initChart () {
      this.chart = this.$echarts.init(document.getElementById('paymentCode'))
      const option = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '5%',
          left: 'center'
        },
        series: [
          {
            name: '支付方式',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '40',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: this.data
          }
        ]
      }
      this.chart.setOption(option)
      window.onresize = this.chart.resize
    },
    loadData () {
      this.$axios.get('/business/paymentCode').then(resp => {
        if (resp.data.code === 200) {
          this.data = resp.data.data
          this.initChart()
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
    this.loadData()
  }
}
</script>

<style scoped>

</style>
