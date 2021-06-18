<template>
  <div style="margin-bottom: 30px;display: flex;justify-content: center;align-items: center">
    <el-input
      @keyup.enter.native="searchClick"
      placeholder="输入rowkey查询..."
      prefix-icon="el-icon-search"
      style="width: 400px;margin-right: 10px;margin-top: 20px"
      v-model="keywords">
    </el-input>
    <el-button style="margin-top: 20px" type="primary" plain icon="el-icon-search" @click="searchClick" round>查询</el-button>
  </div>
</template>

<script>
export default {
  name: 'SearchBar',
  data () {
    return {
      keywords: ''
    }
  },
  methods: {
    searchClick () {
      this.$emit('onSearch')
    }
  }
}
</script>

<style scoped>

</style>
