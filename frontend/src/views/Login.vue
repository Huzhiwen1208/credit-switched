<template>
    <div class="login">
        <el-input v-model="account" placeholder="账号" style="width: 300px; margin-bottom: 20px;" />
        <br />
        <el-input v-model="passwd" placeholder="密码" type="password" style="width: 300px; margin-bottom: 20px;" />
        <br />
        <el-button type="primary" @click="Login()" style="width: 130px;">登录</el-button>
    </div>
</template>

<script>
export default {
    name: "LoginComponent",
    methods: {
        Login() {
            console.log("Login Button Clicked");
            console.log("Account:", this.account);
            console.log("Password:", this.passwd);

            try {
                this.$axios.post("http://192.168.121.188:8081/apply/login", {
                    account: this.account,
                    passwd: this.passwd,
                }).then((res) => {
                    if (res.data.data == "Success") {
                        this.$router.push("/home");
                    } else {
                        alert("登录失败，请检查账号和密码");
                    }
                });

            } catch (error) {
                console.error("Error during login:", error);
            }
        },
    },
    data() {
        return {
            account: "",
            passwd: "",
        };
    },
};
</script>
