<template>
  <div class="register">
    <el-form ref="form" :model="form" label-width="0px" class="register-form">
      <el-form-item>
        <el-input v-model="form.email" placeholder="邮箱"></el-input>
      </el-form-item>
      <el-form-item class="captcha-row">
        <el-input v-model="form.captcha" placeholder="人机验证码"></el-input>
        <img :src="captchaImage" @click="loadCaptcha" alt="captcha" class="captcha-img"/>
      </el-form-item>
      <el-form-item>
        <el-button :disabled="sendDisabled" @click="sendOtp">发送邮箱验证码 {{countdownText}}</el-button>
      </el-form-item>
      <el-form-item>
        <el-input v-model="form.otp" placeholder="邮箱验证码"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="form.password" placeholder="输入密码" type="password"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="confirmRegister">注册</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'RegisterPage',
  data() {
    return {
      form: {
        email: '',
        captcha: '',
        captchaUuid: '',
        otp: '',
        password: ''
      },
      captchaImage: '',
      sendDisabled: false,
      countdown: 0
    }
  },
  computed: {
    countdownText() {
      return this.countdown > 0 ? '(' + this.countdown + 's)' : '';
    }
  },
  created() {
    this.loadCaptcha();
  },
  methods: {
    loadCaptcha() {
      this.$axios.get('/apply/captcha').then(res => {
        if (res.data && res.data.code === 200) {
          const data = res.data.data;
          console.log('[DEBUG] Captcha generated:', data.uuid);
          this.form.captchaUuid = data.uuid;
          this.captchaImage = data.imageBase64;
        } else {
          this.$message.error('获取验证码失败');
        }
      }).catch(err => {
        console.error(err);
        this.$message.error('获取验证码失败');
      })
    },
    sendOtp() {
      if (!this.form.email) { this.$message.error('邮箱不能为空'); return; }
      if (!this.form.captcha) { this.$message.error('请填写人机验证码'); return; }
      console.log('[DEBUG] sendOtp payload:', { email: this.form.email, captcha: (this.form.captcha || '').trim(), captchaUuid: this.form.captchaUuid });
      this.$axios.post('/apply/register/request', {
        email: this.form.email,
        // trim input to avoid accidental spaces
        captcha: (this.form.captcha || '').trim(),
        captchaUuid: this.form.captchaUuid
      }).then(res => {
        if (res.data && res.data.code === 200) {
          this.$message.success('验证码已发送');
          this.sendDisabled = true;
          this.countdown = 60;
          const timer = setInterval(() => {
            this.countdown--;
            if (this.countdown <= 0) {
              clearInterval(timer);
              this.sendDisabled = false;
            }
          }, 1000);
        } else {
          this.$message.error(res.data.msg || '发送失败');
          this.loadCaptcha();
        }
      }).catch(err => {
        console.error(err);
        this.$message.error('发送失败');
        this.loadCaptcha();
      })
    },
    confirmRegister() {
      if (!this.form.email || !this.form.otp || !this.form.password) {
        this.$message.error('请填写所有字段');
        return;
      }
      this.$axios.post('/apply/register/confirm', {
        email: this.form.email,
        otp: this.form.otp,
        password: this.form.password
      }).then(res => {
        if (res.data && res.data.code === 200) {
          this.$message.success('注册成功');
          this.$router.push('/');
        } else {
          this.$message.error(res.data.msg || '注册失败');
        }
      }).catch(err => {
        console.error(err);
        this.$message.error('注册失败');
      })
    }
  }
}
</script>

<style scoped>
.register {
  width: 420px;
  margin: 60px auto;
}
.captcha-row {
  display: flex;
  align-items: center;
}
.captcha-img {
  height: 40px;
  margin-left: 10px;
  cursor: pointer;
}
</style>
