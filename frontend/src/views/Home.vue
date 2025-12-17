<template>
  <div class="home">
    <el-button type="primary" @click="Test()" style="width:130px;">测试后端</el-button>
  </div>
</template>

<script>
export default {
  name: "HomeComponent",
  methods: {
    Test() {

      this.$axios.get("http://192.168.121.188:8081/apply/test").then((res) => {
        console.log(res.data);
      })
    },
  },
};
</script>
