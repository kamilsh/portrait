<template>
  <div id="age_group" :data="ageData" style="width: 65vw;height: 65vh;"></div>
</template>

<script>
export default {
  name: 'AgeGroup',
  data () {
    return {
      ageData: [
        {
          'name': '50后',
          'value': 0
        },
        {
          'name': '60后',
          'value': 0
        },
        {
          'name': '70后',
          'value': 257
        },
        {
          'name': '80后',
          'value': 240
        },
        {
          'name': '90后',
          'value': 296
        },
        {
          'name': '00后',
          'value': 157
        },
        {
          'name': '10后',
          'value': 0
        },
        {
          'name': '20后',
          'value': 0
        }
      ]
    }
  },
  methods: {
    initChart () {
      this.chart = this.$echarts.init(document.getElementById('age_group'))
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
            name: '年龄群分布',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
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
            data: this.ageData
          }
        ]
      }
      this.chart.setOption(option)
      window.onresize = this.chart.resize
    },
    loadData () {
      this.$axios.get('/personal/ageGroup').then(resp => {
        if (resp.data.code === 200) {
          this.ageData = resp.data.data
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
