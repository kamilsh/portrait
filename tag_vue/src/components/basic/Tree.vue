<template>
  <div style="height: 100%">
    <div><search-bar @onSearch="getData" ref="searchBar"></search-bar></div>
    <div id="tree" :style="{width: '100%', height: '100%'}" :data="treeData"></div>
  </div>
</template>

<script>
import SearchBar from '../common/SearchBar'
export default {
  name: 'Tree',
  components: {SearchBar},
  data () {
    return {
      treeData: {
        'name': 'animate',
        'children': [
          {'name': 'Easing'},
          {'name': 'FunctionSequence'},
          {
            'name': 'interpolate',
            'children': [
              {'name': 'ArrayInterpolator'},
              {'name': 'ColorInterpolator'},
              {'name': 'DateInterpolator'},
              {'name': 'Interpolator'},
              {'name': 'MatrixInterpolator'},
              {'name': 'NumberInterpolator'},
              {'name': 'ObjectInterpolator'},
              {'name': 'PointInterpolator'},
              {'name': 'RectangleInterpolator'}
            ]
          },
          {'name': 'ISchedulable'},
          {'name': 'Parallel'},
          {'name': 'Pause'},
          {'name': 'Scheduler'},
          {'name': 'Sequence'},
          {'name': 'Transition'},
          {'name': 'Transitioner'},
          {'name': 'TransitionEvent'},
          {'name': 'Tween'}
        ]
      }
    }
  },
  methods: {
    initChart () {
      this.chart = this.$echarts.init(document.getElementById('tree'))
      const option = {
        tooltip: {
          trigger: 'item',
          triggerOn: 'mousemove'
        },
        series: [
          {
            type: 'tree',

            data: [this.treeData],

            // top: '1%',
            // left: '7%',
            // bottom: '1%',
            // right: '20%',

            symbolSize: 20,

            label: {
              position: 'left',
              verticalAlign: 'middle',
              align: 'right',
              fontSize: 20
            },

            leaves: {
              label: {
                position: 'right',
                verticalAlign: 'middle',
                align: 'left'
              }
            },

            emphasis: {
              focus: 'descendant'
            },

            expandAndCollapse: true,
            animationDuration: 550,
            animationDurationUpdate: 750
          }
        ]
      }
      this.chart.setOption(option)
      window.onresize = this.chart.resize
    },
    getData () {
      this.$axios.get('/tree/' + this.$refs.searchBar.keywords + '/').then(resp => {
        if (resp.data.code === 200) {
          this.treeData = resp.data.data[0]
          // console.log(resp)
          // console.log(this.treeData)
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
  }
}
</script>

<style scoped>

</style>
