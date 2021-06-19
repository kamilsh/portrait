<template>
    <div id="gender_pie" :data="genderData" style="width: 65vw;height: 65vh;"></div>
</template>

<script>
export default {
  name: 'Gender',
  data () {
    return {
      // genderData: [
      //   {
      //     'name': '男',
      //     'value': 481
      //   },
      //   {
      //     'name': '女',
      //     'value': 469
      //   }
      // ]
      genderData: []
    }
  },
  methods: {
    initChart () {
      this.chart = this.$echarts.init(document.getElementById('gender_pie'))
      const option = {
        // backgroundColor: '#FFEBCD',

        title: {
          text: '性别分布',
          left: 'center',
          top: 20,
          textStyle: {
            color: '#1E90FF'
          }
        },

        tooltip: {
          trigger: 'item'
        },

        visualMap: {
          show: false,
          min: 80,
          max: 600,
          inRange: {
            colorLightness: [0, 1]
          }
        },
        series: [
          {
            name: '性别分布',
            type: 'pie',
            radius: '55%',
            center: ['50%', '50%'],
            data: this.genderData.sort(function (a, b) { return a.value - b.value }),
            roseType: 'radius',
            label: {
              color: 'rgba(175, 238, 238, 0.3)'
            },
            labelLine: {
              lineStyle: {
                color: 'rgba(255, 255, 255, 0.3)'
              },
              smooth: 0.2,
              length: 10,
              length2: 20
            },
            itemStyle: {
              color: '#c23531',
              shadowBlur: 200,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            },

            animationType: 'scale',
            animationEasing: 'elasticOut',
            animationDelay: function (idx) {
              return Math.random() * 200
            }
          }
        ]
      }
      this.chart.setOption(option)
      window.onresize = this.chart.resize
    },
    loadData () {
      this.$axios.get('/personal/gender').then(resp => {
        if (resp.data.code === 200) {
          this.genderData = resp.data.data
          // console.log(this.genderData)
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
