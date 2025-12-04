<template>
  <div class="login">
    <el-input placeholder="邮箱" v-model="email" class="custom-input"></el-input>
    <el-input placeholder="密码" v-model="password" type="password" class="custom-input"></el-input>
    <div style="margin-top:10px; display:flex; align-items:center; gap:10px;">
      <el-input placeholder="人机验证码" v-model="captcha" style="width:180px"></el-input>
      <div style="cursor:pointer;" @click="refreshCaptcha">
        <img :src="captchaImage" alt="captcha" v-if="captchaImage" style="height:38px;border-radius:4px;"/>
        <div v-else style="height:38px;width:100px;background:#f5f5f5;border-radius:4px;display:flex;align-items:center;justify-content:center;color:#888">点击获取</div>
      </div>
    </div>
    <el-button type="primary" @click="login" class="login-btn">登录</el-button>
    <el-button type="text" @click="$router.push({ name: 'register' })" style="margin-top:10px;">去注册</el-button>
  </div>
</template>

<script>
export default {
  name: "loginComponent",
  data() {
    return {
      email: '',
      password: '',
      captcha: '',
      captchaUuid: null,
      captchaImage: null
    }
  },
  methods: {
    Test() {
      // 使用 axios baseURL（已在 main.js 中设置），改为相对路径
      this.$axios.get("/apply/login").then((res) => {
        console.log(res.data);
      })
    },
    refreshCaptcha() {
      this.$axios.get('/apply/captcha').then((res) => {
        if (res.data.code === 200 && res.data.data) {
          this.captchaUuid = res.data.data.uuid;
          this.captchaImage = res.data.data.imageBase64;
        } else {
          console.warn('captcha fetch failed', res.data);
        }
      }).catch(e => console.error('captcha fetch error', e));
    },
    login() {
      if (!this.email || !this.password || !this.captcha || !this.captchaUuid) {
        alert('请输入邮箱、密码和人机验证码');
        return;
      }
      this.$axios.post('/apply/login', {
        email: this.email,
        password: this.password,
        captcha: this.captcha,
        captchaUuid: this.captchaUuid
      }).then((res) => {
        if (res.data.code === 200) {
          try { localStorage.setItem('username', this.email); } catch(e){ console.warn('localStorage not available', e); }
          this.$router.push({ name: 'Home' });
        } else {
          alert(res.data.msg || '登录失败');
          this.refreshCaptcha();
        }
      }).catch(err => {
        console.error(err);
        alert('请求失败，请检查网络或后端服务');
        this.refreshCaptcha();
      });
    }
  },
  mounted() {
    this.refreshCaptcha();
  },
};
</script>

<style scoped>
.login {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 60px;
}

.custom-input {
  width: 300px; /* 限制输入框长度 */
  margin-top: 15px;
}

/* 使用深度选择器修改 Element UI 内部 input 的样式 */
.custom-input >>> .el-input__inner {
  border-radius: 25px; /* 设置圆角 */
}

.login-btn {
  width: 120px;
  margin-top: 25px;
  border-radius: 25px; /* 按钮也设置圆角以保持风格一致 */
}
</style>
