<template>
  <div class="auth-container">
    <div class="auth-box">
      
      <div class="tabs">
        <span :class="{ active: isLoginMode }" @click="switchMode(true)">用户登录</span>
        <span :class="{ active: !isLoginMode }" @click="switchMode(false)">新用户注册</span>
      </div>

      <form v-if="isLoginMode" @submit.prevent="handleLogin">
        <div class="form-item">
          <label>邮箱账号</label>
          <input type="email" v-model="loginForm.email" placeholder="请输入注册邮箱" required />
        </div>
        
        <div class="form-item">
          <label>登录密码</label>
          <input type="password" v-model="loginForm.password" placeholder="请输入密码" required />
        </div>

        <div class="form-item">
          <label>人机验证</label>
          <div class="captcha-row">
            <input type="text" v-model="loginForm.captcha" placeholder="输入右侧字符" required />
            <img 
              :src="captchaUrl" 
              @click="refreshCaptcha" 
              alt="验证码" 
              title="看不清？点击更换" 
            />
          </div>
        </div>

        <button type="submit" :disabled="loading" class="submit-btn">
          {{ loading ? '登录校验中...' : '立即登录' }}
        </button>
      </form>

      <form v-else @submit.prevent="handleRegister">
        
        <div class="form-item">
          <label>注册邮箱</label>
          <input type="email" v-model="registerForm.email" placeholder="请输入您的邮箱" required />
        </div>

        <div class="form-item">
          <label>人机验证 (发送邮件前必填)</label>
          <div class="captcha-row">
            <input type="text" v-model="registerForm.captcha" placeholder="输入字符以发送邮件" required />
            <img 
              :src="captchaUrl" 
              @click="refreshCaptcha" 
              alt="验证码" 
              title="看不清？点击更换" 
            />
          </div>
        </div>

        <div class="form-item">
          <button 
            type="button" 
            class="send-code-btn" 
            :disabled="isCounting || loading" 
            @click.prevent="sendEmailCode"
          >
            {{ isCounting ? `${countdown}秒后重新获取` : '点击获取邮件验证码' }}
          </button>
        </div>

        <div class="form-item">
          <label>邮件验证码</label>
          <input type="text" v-model="registerForm.emailCode" placeholder="查看邮箱并输入验证码" required />
        </div>

        <div class="form-item">
          <label>设置密码</label>
          <input type="password" v-model="registerForm.password" placeholder="设置您的登录密码" required />
        </div>

        <button type="submit" :disabled="loading" class="submit-btn">
          {{ loading ? '注册中...' : '完成注册' }}
        </button>
      </form>

    </div>
  </div>
</template>

<script>
// 1. 必须放在最顶部的引入！
import axios from 'axios'

export default {
  name: 'LoginRegister',
  
  data() {
    return {
      isLoginMode: true, // true=登录, false=注册
      loading: false,    // 按钮加载状态
      captchaUrl: '',    // 验证码图片地址
      
      // 倒计时相关
      isCounting: false,
      countdown: 60,
      timer: null,

      // 登录数据模型
      loginForm: {
        email: '',
        password: '',
        captcha: '' // 登录也需要验证码
      },

      // 注册数据模型
      registerForm: {
        email: '',
        captcha: '',    // 用于换取邮件的图形码
        emailCode: '',  // 邮箱里收到的验证码
        password: ''
      }
    }
  },

  // 页面加载完成后，立刻请求第一张验证码
  mounted() {
    this.refreshCaptcha()
  },

  // 页面销毁前清理定时器，防止内存泄漏
  beforeDestroy() {
    if (this.timer) clearInterval(this.timer)
  },

  methods: {
    // ---------------------------------------
    // 1. 刷新/获取图形验证码
    // ---------------------------------------
    refreshCaptcha() {
      // 这里的路径需要和后端 Controller 对应
      // 加个时间戳 t=... 是为了防止浏览器缓存旧图片
      this.captchaUrl = `/api/apply/image?t=${Date.now()}`
      
      // 刷新图片时，把输入框清空，体验更好
      this.loginForm.captcha = ''
      this.registerForm.captcha = ''
    },

    // ---------------------------------------
    // 2. 切换 登录/注册 模式
    // ---------------------------------------
    switchMode(mode) {
      this.isLoginMode = mode
      this.refreshCaptcha() // 切换模式时自动刷新验证码
      // 重置倒计时状态（可选）
      // this.isCounting = false=
      // if(this.timer) clearInterval(this.timer)
    },

    // ---------------------------------------
    // 3. 发送邮件验证码 (注册流程核心)
    // ---------------------------------------
    // 核心功能：请求发送邮箱验证码
    async sendEmailCode() {
      // 1. 前端基础校验
      if (!this.registerForm.email) return alert("请先填写邮箱");
      if (!this.registerForm.captcha) return alert("请填写图片验证码");

      try {
        // 2. 发送请求给后端
        // 注意：这里使用了代理路径 /api/...
        // 后端会先校验 imageCaptcha，如果错了会直接报错抛出异常
        await this.$axios.post('/api/apply/send-email-code', {
            email: this.registerForm.email,
            captcha: this.registerForm.captcha
        });
        
        // 3. 成功逻辑 (后端没报错)
        alert("邮件发送成功，请查收！");

      } catch (error) {
        // 4. 失败逻辑 (图片验证码错误或网络问题)
        console.error(error);
        // 如果后端返回了错误信息，这里显示出来
        const msg = error.response && error.response.data ? error.response.data.message : "发送失败，请检查验证码";
        alert(msg);
        
        // 失败后，通常图片验证码就失效了，自动刷新一张新的给用户
        this.refreshCaptcha(); 
      }
    },

    // ---------------------------------------
    // 4. 提交注册
    // ---------------------------------------
    async handleRegister() {
      // 简单校验
      if (!this.registerForm.emailCode) return alert('请输入邮件验证码')
      
      try {
        this.loading = true
        // 提交注册：后端校验邮件验证码是否正确
        const res=await axios.post('/api/apply/register', {
          email: this.registerForm.email,
          password: this.registerForm.password,
          code: this.registerForm.emailCode // 注意字段名要和后端DTO一致
        })

        alert(res.data)
        this.switchMode(true) // 自动跳到登录页

      } catch (error) {
           console.error(error); // 在控制台打印详细错误
           // 尝试获取后端返回的具体错误文字
           const msg = error.response && error.response.data ? error.response.data.message : "注册失败(未知错误)";
           // 弹窗显示真正的错误原因
           alert(msg);
}
    },

    // ---------------------------------------
    // 5. 提交登录
    // ---------------------------------------
    async handleLogin() {
      try {
        this.loading = true
        // 登录请求
        const res = await axios.post('/api/apply/login', {
          email: this.loginForm.email,
          password: this.loginForm.password,
          captcha: this.loginForm.captcha // 登录也需要传图形码
        })

        // 登录成功，保存 token
        localStorage.setItem('token', res.data.token)
        alert('登录成功！')
        
        // TODO: 这里写跳转逻辑，比如 this.$router.push('/home')
        this.$router.push({ name: 'Home' })
        
      } catch (error) {
        const msg = error.response && error.response.data.message 
                    ? error.response.data.message 
                    : '登录失败'
        alert(msg)
        this.refreshCaptcha() // 密码错或验证码错，都刷新图片
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
/* ===========================================
   样式区域：这里修复了你之前遇到的“太宽”的问题
   =========================================== */

/* 1. 外层容器：负责水平居中，并给顶部留出空间 */
.auth-container {
  display: flex;
  justify-content: center;
  align-items: flex-start; /* 靠上一点 */
  padding-top: 50px;       /* 距离顶部 50px */
  min-height: 100vh;       /* 确保占满整个屏幕高度 */
  background-color: #f5f7fa; /* 给背景加个淡淡的灰色，好看 */
}

/* 2. 核心盒子：强制固定宽度 */
.auth-box {
  width: 400px;            /* 【关键】固定宽度，不再无限拉伸 */
  background-color: #fff;
  padding: 30px;
  border-radius: 8px;      /* 圆角 */
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1); /* 阴影 */
}

/* 顶部 Tab 切换 */
.tabs {
  display: flex;
  justify-content: space-around;
  margin-bottom: 25px;
  border-bottom: 2px solid #f0f0f0;
}
.tabs span {
  padding-bottom: 10px;
  cursor: pointer;
  font-size: 16px;
  color: #666;
}
.tabs span.active {
  color: #409EFF; /* 选中变蓝 */
  border-bottom: 2px solid #409EFF;
  margin-bottom: -2px; /* 盖住底下的灰线 */
}

/* 表单项间距 */
.form-item {
  margin-bottom: 20px;
}
.form-item label {
  display: block;
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}
.form-item input {
  width: 100%;
  height: 40px;
  line-height: 40px;
  padding: 0 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  box-sizing: border-box; /* 防止 padding 撑大宽度 */
  outline: none;
}
.form-item input:focus {
  border-color: #409EFF;
}

/* 验证码那一行：输入框 + 图片 */
.captcha-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.captcha-row input {
  width: 60%; /* 输入框占左边 */
}
.captcha-row img {
  width: 35%; /* 图片占右边 */
  height: 40px;
  cursor: pointer;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

/* 按钮通用样式 */
.submit-btn {
  width: 100%;
  height: 40px;
  background-color: #409EFF;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}
.submit-btn:hover {
  background-color: #66b1ff;
}
.submit-btn:disabled {
  background-color: #a0cfff;
  cursor: not-allowed;
}

/* 发送验证码的小按钮 */
.send-code-btn {
  width: 100%;
  height: 36px;
  background-color: #67C23A; /* 绿色 */
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.send-code-btn:disabled {
  background-color: #b3e19d;
}
</style>