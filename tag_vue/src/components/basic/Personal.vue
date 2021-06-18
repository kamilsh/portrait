<template>
  <el-table
    :data="tableData"
    border
    style="width: 100%">
    <el-table-column
      prop="name"
      label="标签"
      width="150">
    </el-table-column>
    <el-table-column
      prop="business"
      label="值"
      width="500">
    </el-table-column>
    <el-table-column
      fixed="right"
      label="操作"
      width="100">
      <template slot-scope="scope">
        <el-button @click="handleClick(scope.row)" type="text">分析</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
export default {
  name: 'Personal',
  data () {
    return {
      tableData: ''
    }
  },
  methods: {
    handleClick (row) {
      console.log(row)
    }
  }
}
</script>

<style scoped>

</style>
