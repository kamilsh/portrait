<template>
  <body id="poster">
  <el-form ref="loginForm" :model="loginForm" :rules="rules" class="login-container" label-position="left"
           label-width="0px">
    <h3 class="login_title">登录</h3>
    <el-form-item prop="name">
      <el-input type="text" v-model="loginForm.name"
                auto-complete="off" placeholder="账号"></el-input>
    </el-form-item>
    <el-form-item prop="password">
      <el-input type="password" v-model="loginForm.password"
                auto-complete="off" placeholder="密码" show-password></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" class="el-icon-user-solid" style="width: 25%;background: #505458;border: none" @click="validate_user_login('loginForm')">登录</el-button>
    </el-form-item>
  </el-form>
  </body>
</template>

<script>
export default {
  name: 'Login',
  data () {
    return {
      rules: {
        name: [{required: true, message: '账号不能为空', trigger: 'blur'}],
        password: [{required: true, message: '密码不能为空', trigger: 'blur'}]
      },
      loginForm: {
        name: '',
        password: ''
      },
      loading: false
    }
  },
  methods: {
    user_login () {
      const _this = this
      this.$axios
        .post('/login', {
          username: _this.loginForm.name,
          password: _this.loginForm.password
        })
        .then(resp => {
          if (resp.data.code === 200) {
            console.log(_this.loginForm.name)
            console.log(_this.loginForm.password)
            const path = _this.$route.query.redirect
            _this.$router.replace({path: path === '/' || path === undefined ? '/home' : path})
          } else {
            this.$alert(resp.data.message, '提示', {
              confirmButtonText: '确定'
            })
          }
        })
        .catch(failResponse => {
          this.$message('服务器异常')
        })
    },
    validate_user_login (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.user_login()
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>

<style scoped>
  .login-container {
    border-radius: 15px;
    background-clip: padding-box;
    margin: 90px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }
  .login_title {
    margin: 0 auto 40px auto;
    text-align: center;
    color: #505458;
  }
  #poster {
    /*background: url("../assets/loginBG.jpeg") no-repeat center;*/
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
  }
  body{
    margin: 0;
  }
</style>
