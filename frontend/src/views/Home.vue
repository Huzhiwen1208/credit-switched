<template>
  <div class="home">
    <h2 v-if="username">欢迎, {{ username }}</h2>
    <h2 v-else>欢迎来到首页</h2>
    <el-button type="primary" @click="Test()" style="width:130px;">测试后端</el-button>
  </div>
</template>

<script>
export default {
  name: "HomeComponent",
  data() {
    return {
      username: localStorage.getItem('username') || ''
    }
  },
  methods: {
    Test() {
      this.$axios.get("/apply/test").then((res) => {
        console.log(res.data);
      })
    },
  },
};
</script>
