<template>
  <div id="viewPage" :data="data" style="width: 65vw;height: 65vh;"></div>
</template>

<script>
export default {
  name: 'ViewPage',
  data () {
    return {
      data: []
    }
  },
  methods: {
    initChart () {
      this.chart = this.$echarts.init(document.getElementById('viewPage'))
      const option = {
        title: {
          text: '最近浏览页面',
          subtext: '所有用户浏览页面统计',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          left: 'center',
          top: 'bottom'
          // data: ['订单页', '分类页', '商品页', '登录页', '主页', '其他']
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
            name: '最近浏览页面',
            type: 'pie',
            radius: [20, 140],
            // center: ['75%', '50%'],
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
      this.$axios.get('/action/viewPage').then(resp => {
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
