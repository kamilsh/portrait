<template>
  <div id="deviceType" :data="data" style="width: 65vw;height: 65vh;"></div>
</template>

<script>
export default {
  name: 'deviceType',
  data () {
    return {
      data: []
    }
  },
  methods: {
    initChart () {
      this.chart = this.$echarts.init(document.getElementById('deviceType'))
      const option = {
        title: {
          text: '设备类型',
          subtext: '用户使用设备类型统计',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          left: 'center',
          top: 'bottom'
        },
        toolbox: {
          show: true,
          feature: {
            mark: {show: true},
            dataView: {show: true, readOnly: false},
            restore: {show: true},
            saveAsImage: {show: true}
          }
        },
        series: [
          {
            type: 'pie',
            radius: [20, 140],
            roseType: 'area',
            itemStyle: {
              borderRadius: 5
            },
            data: this.data
          }
        ]
      }
      this.chart.setOption(option)
      window.onresize = this.chart.resize
    },
    loadData () {
      this.$axios.get('/action/deviceType').then(resp => {
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
