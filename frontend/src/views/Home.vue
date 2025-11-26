<template>
  <div class="home">
    <el-button type="primary" @click="Test()" style="width:130px;">Home</el-button>
  </div>
</template>

<script>
export default {
  name: "HomeComponent",
  methods: {
    Test() {
      console.log("Home Button Clicked");
    },
  },
};
</script>
