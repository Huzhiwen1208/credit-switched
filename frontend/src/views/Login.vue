<template>
  <div id="particles-js" class="main-form-box">
    
    <div v-if="dialogVisible" class="custom-mask">
      <div class="custom-dialog">
        <h3 style="margin-top: 0;">{{ dialogTitle }}</h3>
        <p>{{ dialogMessage }}</p>
        <button class="btn btn-info" @click="handleDialogConfirm">确定</button>
      </div>
    </div>

    <div class="md-form">
      <div class="container">
        <div class="row">
          <div class="col-md-6 offset-md-3">
            <div class="panel panel-login">
              
              <div class="logo-top">
                <a href="#"><img src="@/assets/images/logo.png" alt="Logo" /></a>
              </div>
              
              <div class="panel-heading">
                <div class="row">
                  <div class="col-lg-6 col-sm-6 col-xl-6">
                    <a href="#" :class="{ active: isLoginMode }" @click.prevent="switchMode(true)">Login</a>
                  </div>
                  <div class="col-lg-6 col-sm-6 col-xl-6">
                    <a href="#" :class="{ active: !isLoginMode }" @click.prevent="switchMode(false)">Register</a>
                  </div>
                  <div class="or">OR</div>
                </div>
              </div>

              <div class="panel-body">
                <div class="row">
                  <div class="col-lg-12">
                    
                    <form id="login-form" v-show="isLoginMode">
                      <div class="form-group">
                        <label class="icon-lp"><i class="fas fa-envelope"></i></label>
                        <input type="email" class="form-control" placeholder="Email" v-model="loginForm.email">
                      </div>
                      <div class="form-group">
                        <label class="icon-lp"><i class="fas fa-key"></i></label>
                        <input type="password" class="form-control" placeholder="Password" v-model="loginForm.password">
                      </div>
                      
                      <div class="form-group" style="display: flex; justify-content: space-between; align-items: center;">
                        <div style="position: relative; width: 65%;">
                          <label class="icon-lp"><i class="fas fa-shield-alt"></i></label>
                          <input type="text" class="form-control" placeholder="Captcha" v-model="loginForm.captcha">
                        </div>
                        <img :src="captchaUrl" @click="refreshCaptcha" class="captcha-img" title="点击刷新" alt="验证码">
                      </div>

                      <div class="che-box">
                        <label class="checkbox-in">
                          <input type="checkbox"> <span></span> Remember Me
                        </label>
                      </div>
                      
                      <ul>
                        <li><button class="fb" @click.prevent="mockSocialLogin"><i class="fab fa-facebook-f"></i> Connect with Facebook</button></li>
                        <li><button class="tw" @click.prevent="mockSocialLogin"><i class="fab fa-twitter"></i> Connect with Twitter</button></li>
                      </ul>
                      
                      <div class="form-group">
                        <div class="row">
                          <div class="col-sm-6 offset-sm-3">
                            <button class="form-control btn btn-login" @click.prevent="handleLogin" :disabled="loading">
                              {{ loading ? 'Logging in...' : 'Log In' }}
                            </button>
                          </div>
                        </div>
                      </div>
                    </form>

                    <form id="register-form" v-show="!isLoginMode">
                      <div class="form-group">
                        <label class="icon-lp"><i class="fas fa-envelope"></i></label>
                        <input type="email" class="form-control" placeholder="Email Address" v-model="registerForm.email">
                      </div>

                      <div class="form-group" style="display: flex; justify-content: space-between; align-items: center;">
                        <div style="position: relative; width: 65%;">
                          <label class="icon-lp"><i class="fas fa-shield-alt"></i></label>
                          <input type="text" class="form-control" placeholder="Captcha" v-model="registerForm.captcha">
                        </div>
                        <img :src="captchaUrl" @click="refreshCaptcha" class="captcha-img" title="点击刷新" alt="验证码">
                      </div>

                      <div class="form-group" style="display: flex; justify-content: space-between; align-items: center;">
                        <div style="position: relative; width: 60%;">
                          <label class="icon-lp"><i class="fas fa-paper-plane"></i></label>
                          <input type="text" class="form-control" placeholder="Email Code" v-model="registerForm.emailCode">
                        </div>
                        <button class="send-btn" @click.prevent="sendEmailCode" :disabled="isCounting || loading">
                          {{ isCounting ? countdown + 's' : 'Send Code' }}
                        </button>
                      </div>

                      <div class="form-group">
                        <label class="icon-lp"><i class="fas fa-key"></i></label>
                        <input type="password" class="form-control" placeholder="Password" v-model="registerForm.password">
                      </div>
                      
                      <ul>
                        <li><button class="fb" @click.prevent="mockSocialLogin"><i class="fab fa-facebook-f"></i> Connect with Facebook</button></li>
                        <li><button class="tw" @click.prevent="mockSocialLogin"><i class="fab fa-twitter"></i> Connect with Twitter</button></li>
                      </ul>
                      
                      <div class="che-box">
                        <label class="checkbox-in"> 
                          <input type="checkbox"> <span></span>I agree to the <a href="#"> Terms and Conditions </a> and <a href="#">Privacy Policy </a>
                        </label>
                      </div>
                      
                      <div class="form-group">
                        <div class="row">
                          <div class="col-sm-6 offset-sm-3">
                            <button class="form-control btn btn-register" @click.prevent="handleRegister" :disabled="loading">
                              {{ loading ? 'Registering...' : 'Register Now' }}
                            </button>
                          </div>
                        </div>
                      </div>
                    </form>

                  </div>
                </div>
              </div>
            </div>
            <p class="footer-company-name" style="color: white; text-align: center;">All Rights Reserved. &copy; 2026</p>
          </div>
        </div>
      </div>  
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import '@/assets/css/bootstrap.min.css'
import '@/assets/css/all.min.css'
import '@/assets/css/style.css'
import '@/assets/css/responsive.css'

export default {
  name: 'LoginRegister',

  data() {
    return {
      isLoginMode: true,
      loading: false,
      captchaUrl: '',

      /* 倒计时相关 */
      isCounting: false,
      countdown: 60,
      timer: null,

      /* 登录数据模型 */
      loginForm: {
        email: '',
        password: '',
        captcha: ''
      },

      /* 注册数据模型 */
      registerForm: {
        email: '',
        captcha: '',    
        emailCode: '',
        password: ''
      },

      /* 弹窗控制模型 */
      dialogVisible: false,
      dialogTitle: '提示',
      dialogMessage: '',
      dialogType: ''
    }
  },

  mounted() {
    this.refreshCaptcha();
  },

  beforeDestroy() {
    if (this.timer) clearInterval(this.timer)
  },

  methods: {
    /* 1. 弹窗公共方法 */
    showDialog(msg, title = '提示', type = 'info') {
      this.dialogMessage = msg;
      this.dialogTitle = title;
      this.dialogType = type;
      this.dialogVisible = true;
    },

    /* 弹窗点击确定后的分发逻辑 */
    handleDialogConfirm() {
      this.dialogVisible = false;
      if (this.dialogType === 'register_success') {
        this.switchMode(true); // 注册成功去登录
      }
      if (this.dialogType === 'login_success') {
        this.$router.push({ name: 'Home' }); // 登录成功去主页
      }
    },

    /* 2. 刷新验证码 */
    refreshCaptcha() {
      this.captchaUrl = `/api/apply/image?t=${Date.now()}`
      this.loginForm.captcha = ''
      this.registerForm.captcha = ''
    },

    /* 3. 切换模式 */
    switchMode(mode) {
      this.isLoginMode = mode
      this.refreshCaptcha()
    },

    /* 4. 模拟第三方登录提示 */
    mockSocialLogin() {
      this.showDialog("该功能暂未开放", "提示");
    },

    /* 5. 发送邮件验证码 */
    async sendEmailCode() {
      if (!this.registerForm.email) return this.showDialog("请先填写邮箱", "警告");
      if (!this.registerForm.captcha) return this.showDialog("请填写图片验证码", "警告");

      try {
        this.loading = true;
        const res = await axios.post('/api/apply/send-email-code', {
          email: this.registerForm.email,
          captcha: this.registerForm.captcha
        });
        
        // 适配 Axios 数据结构，通常后端返回的数据包在 res.data 里
        const data = res.data || res; 
        if (data.code === 200) {
          this.showDialog("邮件已发送，请查收", "成功");
          this.startCountdown();
        } else {
          this.showDialog(data.msg || "邮件发送失败", "错误");
          this.refreshCaptcha();
        }
      } catch (error) {
        console.error(error);
        const msg = error.response?.data?.message || error.response?.data?.msg || "发送失败，请检查网络或验证码";
        this.showDialog(msg, "错误");
        this.refreshCaptcha();
      } finally {
        this.loading = false;
      }
    },

    /* 启动验证码倒计时 */
    startCountdown() {
      this.isCounting = true;
      this.countdown = 60;
      this.timer = setInterval(() => {
        this.countdown--;
        if (this.countdown <= 0) {
          clearInterval(this.timer);
          this.isCounting = false;
        }
      }, 1000);
    },

    /* 6. 提交注册 */
    async handleRegister() {
      if (!this.registerForm.email) return this.showDialog("请填写邮箱", "警告");
      if (!this.registerForm.emailCode) return this.showDialog("请填写邮件验证码", "警告");
      if (!this.registerForm.password) return this.showDialog("请填写密码", "警告");

      try {
        this.loading = true;
        const res = await axios.post('/api/apply/register', {
          email: this.registerForm.email,
          password: this.registerForm.password,
          code: this.registerForm.emailCode
        });
        
        const data = res.data || res;
        if (data.code === 200) {
          this.showDialog("注册成功，点击确定前往登录", "恭喜", "register_success");
        } else {
          this.showDialog(data.msg || "注册失败", "错误");
          this.refreshCaptcha();
        }
      } catch (error) {
        console.error(error);
        const msg = error.response?.data?.message || error.response?.data?.msg || "注册异常(未知错误)";
        this.showDialog(msg, "注册失败");
        this.refreshCaptcha();
      } finally {
        this.loading = false;
      }
    },

    /* 7. 提交登录 */
    async handleLogin() {
      if (!this.loginForm.email) return this.showDialog("请填写邮箱", "警告");
      if (!this.loginForm.password) return this.showDialog("请填写密码", "警告");
      if (!this.loginForm.captcha) return this.showDialog("请填写验证码", "警告");

      try {
        this.loading = true;
        const res = await axios.post('/api/apply/login', {
          email: this.loginForm.email,
          password: this.loginForm.password,
          captcha: this.loginForm.captcha
        });
        
        const data = res.data || res;
        if (data.code === 200) {
          this.showDialog('登录成功！', '欢迎', 'login_success');
        } else {
          this.showDialog(data.msg || "登录失败", "错误");
          this.refreshCaptcha();
        }
      } catch (error) {
        const msg = error.response?.data?.message || error.response?.data?.msg || '登录异常，请检查网络';
        this.showDialog(msg, "登录失败");
        this.refreshCaptcha();
      } finally {
        this.loading = false;
      }
    }
  }
}
</script>

<style scoped>


.main-form-box {
  min-height: 100vh !important;
  width: 100%;
  margin: 0;
  padding: 0;
  display: flex;
  align-items: center; 
  justify-content: center; 
}

::v-deep .particles-js-canvas-el {
  position: absolute !important;
  top: 0;
  left: 0;
  z-index: 0 !important;
  pointer-events: none !important;
}


.md-form {
  position: relative;
  z-index: 10 !important;
  pointer-events: auto !important;
  width: 100%; 
}


.custom-mask {
  position: fixed;
  top: 0; left: 0; width: 100%; height: 100%;
  background: rgba(0,0,0,0.6);
  z-index: 9999;
  display: flex; justify-content: center; align-items: center;
}

.custom-dialog {
  background: white; padding: 30px; border-radius: 8px;
  text-align: center; min-width: 320px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.2);
}

.custom-dialog h3 { color: #333; margin-bottom: 15px; }
.custom-dialog p { color: #666; font-size: 16px; margin-bottom: 25px; }
.custom-dialog button { padding: 8px 30px; font-size: 16px; }


.captcha-img { cursor: pointer; height: 45px; border-radius: 4px; object-fit: cover;}
.send-btn { background-color: #5bc0de; color: white; height: 45px; border: none; border-radius: 4px; width: 35%; cursor: pointer; transition: 0.3s;}
.send-btn:disabled { background-color: #ccc; cursor: not-allowed; }
</style>