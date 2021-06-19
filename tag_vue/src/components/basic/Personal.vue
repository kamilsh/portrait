<template>
  <div>
  <el-table
    :data="tableData"
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
  name: 'Personal',
  data () {
    return {
      // tableData: [
      //   {
      //     'id': 8,
      //     'name': '性别',
      //     'industry': '标签',
      //     'rule': null,
      //     'business': '注册会员的性别',
      //     'level': 4,
      //     'pid': 3,
      //     'state': 4,
      //     'remark': 'gender'
      //   },
      //   {
      //     'id': 9,
      //     'name': '年龄段',
      //     'industry': '标签',
      //     'rule': null,
      //     'business': '注册用户的生日所属年龄段',
      //     'level': 4,
      //     'pid': 3,
      //     'state': 4,
      //     'remark': 'AgeGroup'
      //   },
      //   {
      //     'id': 11,
      //     'name': '民族',
      //     'industry': '标签',
      //     'rule': null,
      //     'business': '注册用户的民族',
      //     'level': 4,
      //     'pid': 3,
      //     'state': 4,
      //     'remark': 'nation'
      //   },
      //   {
      //     'id': 13,
      //     'name': '政治面貌',
      //     'industry': '标签',
      //     'rule': null,
      //     'business': '注册用户的政治面貌',
      //     'level': 4,
      //     'pid': 3,
      //     'state': 4,
      //     'remark': 'political_status'
      //   },
      //   {
      //     'id': 14,
      //     'name': '职业',
      //     'industry': '标签',
      //     'rule': null,
      //     'business': '注册用户的职业',
      //     'level': 4,
      //     'pid': 3,
      //     'state': 4,
      //     'remark': 'job'
      //   },
      //   {
      //     'id': 15,
      //     'name': '婚姻状况',
      //     'industry': '标签',
      //     'rule': null,
      //     'business': '注册用户的婚姻状况',
      //     'level': 4,
      //     'pid': 3,
      //     'state': 4,
      //     'remark': 'marriage'
      //   },
      //   {
      //     'id': 18,
      //     'name': '星座',
      //     'industry': '标签',
      //     'rule': null,
      //     'business': '注册用户的星座',
      //     'level': 4,
      //     'pid': 3,
      //     'state': 4,
      //     'remark': null
      //   },
      //   {
      //     'id': 20,
      //     'name': '国籍',
      //     'industry': '标签',
      //     'rule': null,
      //     'business': '注册用户所属的国籍：中国大陆、中国香港、中国澳门、中国台湾、其他',
      //     'level': 4,
      //     'pid': 3,
      //     'state': 4,
      //     'remark': 'nationality'
      //   }
      // ]
      tableData: []
    }
  },
  methods: {
    handleClick (row) {
      // console.log(row)
      switch (row.name) {
        case '性别':
          console.log('route->gender')
          this.$router.push('/home/basic/personal/gender')
          break
        case '年龄段':
          console.log('route->ageGroup')
          this.$router.push('/home/basic/personal/ageGroup')
          break
        case '政治面貌':
          console.log('route->politicalStatus')
          this.$router.push('/home/basic/personal/politicalStatus')
          break
        case '职业':
          console.log('route->job')
          this.$router.push('/home/basic/personal/job')
          break
        case '婚姻状况':
          console.log('route->maritalStatus')
          this.$router.push('/home/basic/personal/maritalStatus')
          break
        case '星座':
          this.$router.push('/home/basic/personal/constellation')
          break
        case '国籍':
          this.$router.push('/home/basic/personal/nationality')
          break
      }
    },
    loadData () {
      this.$axios.get('/basic/personal').then(resp => {
        if (resp.data.code === 200) {
          this.tableData = resp.data.data
          // console.log(this.tableData)
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
