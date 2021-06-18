<template>
  <el-container>
    <el-header>
      <span style="position: absolute;padding-top: 20px;font-size: 20px;font-weight: bold">电商平台(综合商城)</span>
    </el-header>
    <el-container style="height: 95vh">
      <el-aside style="width: 200px">
        <el-menu default-active="1" background-color="#EEE5DE" @select="select">
          <el-submenu index="1">
            <template slot="title"><i class="el-icon-s-help"></i>基础标签</template>
            <el-menu-item-group>
              <el-menu-item index="1-1"><i class="el-icon-user-solid"></i>人口属性</el-menu-item>
              <el-menu-item index="1-2"><i class="el-icon-s-goods"></i>商业属性</el-menu-item>
              <el-menu-item index="1-3"><i class="el-icon-video-camera-solid"></i>行为属性</el-menu-item>
              <el-menu-item index="1-4"><i class="el-icon-s-finance"></i>用户价值</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="2">
            <template slot="title"><i class="el-icon-s-data"></i>用户画像</template>
            <el-menu-item-group>
              <el-menu-item index="2-1"><i class="el-icon-s-grid"></i>词云</el-menu-item>
              <el-menu-item index="2-2"><i class="el-icon-s-operation"></i>标签结构分析</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-main>
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'Home',
  data () {
    return {
    }
  },
  methods: {
    select (key) {
      switch (key) {
        case '1-1':
          this.$router.push('/home/basic/personal')
          break
        case '1-2':
          this.$router.push('/home/basic/business')
          break
        case '1-3':
          this.$router.push('/home/basic/action')
          break
        case '1-4':
          this.$router.push('/home/basic/value')
          break
        case '2-1':
          this.$router.push('/home/portrait')
          break
        case '2-2':
          this.$router.push('/home/tree')
          break
      }
    }
  }
}
</script>

<style scoped>
</style>
