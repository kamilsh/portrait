<template>
  <div id="maxOrderAmount" :data="data" style="width: 65vw;height: 65vh;"></div>
</template>

<script>
export default {
  name: 'MaxOrderAmount',
  data () {
    return {
      data: []
    }
  },
  methods: {
    initChart () {
      this.chart = this.$echarts.init(document.getElementById('maxOrderAmount'))
      const option = {
        xAxis: {},
        yAxis: {},
        series: [{
          symbolSize: 20,
          data: this.data,
          type: 'scatter'
        }]
      }
      this.chart.setOption(option)
      window.onresize = this.chart.resize
    },
    loadData () {
      this.$axios.get('/business/maxOrderAmount').then(resp => {
        if (resp.data.code === 200) {
          this.data = resp.data.data
          // console.log(this.ageData)
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
