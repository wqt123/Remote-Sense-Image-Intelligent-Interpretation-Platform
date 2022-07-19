<template>
  <div class="login-container">
    <el-form
      ref="loginForm"
      :model="loginForm"
      :rules="loginRules"
      class="login-form"
      auto-complete="on"
      label-position="left"
    >
      <div class="title-container">
        <h3 class="title">Login</h3>
      </div>

      <el-form-item prop="id">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="username"
          v-model="loginForm.id"
          placeholder="用户名或邮箱"
          name="username"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="loginForm.password"
          :type="passwordType"
          placeholder="Password"
          name="password"
          tabindex="2"
          auto-complete="on"
          @keyup.enter.native="handleLogin"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon
            :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'"
          />
        </span>
      </el-form-item>

      <el-form-item prop="code">
        <el-input
          v-model="loginForm.code"
          placeholder="验证码"
          name="password"
          tabindex="3"
          class="code"
        />
        <SIdentify
          :identifyCode="identifyCode"
          @click.native="refreshCode()"
          style="position: absolute; width: 30%; right: -5px; top: 0px"
        ></SIdentify>
      </el-form-item>
      <el-button
        :loading="loading"
        type="primary"
        style="width: 100%; margin-bottom: 30px"
        @click="handleLogin"
        >Login</el-button
      >
      <el-link
        type="success"
        @click="
          () => {
            this.$router.push('/register');
          }
        "
        >点击注册</el-link
      >
    </el-form>
  </div>
</template>

<script>
import { validateNumber, isPassword } from "@/utils/validate";
import SIdentify from "@/components/Identity/identify";
import Base64 from "@/utils/encryption/Base64";

export default {
  name: "Login",
  components: {
    SIdentify,
  },
  data() {
    const validateCode = (rule, value, callback) => {
      if (this.identifyCode !== value && this.loginForm.input_code !== "") {
        this.loginForm.input_code = "";
        this.refreshCode();
        //callback(new Error("请输入正确的验证码"));
        this.$message({
          message: "请输入正确的验证码!",
          type: "error",
        });
      } else {
        callback();
      }
    };

    return {
      identifyCodes: "1234567890",
      identifyCode: "", //图形验证码
      loginForm: {
        id: "",
        password: "",
        code: "",
      },
      loginRules: {
        id: [
          {
            required: true,
            trigger: "blur",
            min: 1,
            message: "用户名不能为空",
          },
          {
            required: true,
            trigger: "blur",
            min: 1,
            message: "用户名不能为空",
          },
          {
            required: true,
            trigger: "change",
            max: 10,
            message: "用户名最大长度不超过10",
          },
          {
            required: true,
            trigger: "bur",
            max: 10,
            message: "用户名最大长度不超过10",
          },
        ],
        password: [
          { required: true, trigger: "blur", min: 1 },
          { required: true, trigger: "blur", validator: isPassword },
          { required: true, trigger: "change", validator: isPassword },
        ],
        code: [
          { required: true, trigger: "blur", validator: validateCode },
          {
            required: true,
            trigger: "blur",
            min: 1,
            message: "验证码不能为空",
          },
        ],
      },
      loading: false,
      passwordType: "password",
      redirect: undefined,
    };
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true,
    },
  },
  methods: {
    showPwd() {
      if (this.passwordType === "password") {
        this.passwordType = "";
      } else {
        this.passwordType = "password";
      }
      this.$nextTick(() => {
        this.$refs.password.focus();
      });
    },
    handleLogin() {
      this.loading = true;
      // if (!this.validate()) {
      //   this.loading = false;
      //   console.log(1);
      //   this.$message({
      //     message: "请输入完整信息!",
      //     type: "error",
      //   });
      //   return;
      // }
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = false;
        } else {
          this.$message({
            message: "请输入完整信息!",
            type: "error",
          });
          this.loading = false;
          return false;
        }
      });
      this.$store
        .dispatch("user/login", this.loginForm)
        .then(() => {
          // console.log(response)
          this.$router.push({ path: this.redirect || "/" });
          this.$message({
            message: "登录成功",
            type: "success",
            duration: 5 * 1000,
          });
          this.loading = false;
        })
        .catch(() => {
          this.loading = false;
          // this.loading = false
        });
    },

    //验证码相关
    randomNum(min, max) {
      return Math.floor(Math.random() * (max - min) + min);
    },
    refreshCode() {
      this.identifyCode = "";
      this.makeCode(this.identifyCodes, 4);
    },
    makeCode(o, l) {
      for (let i = 0; i < l; i++) {
        this.identifyCode +=
          this.identifyCodes[this.randomNum(0, this.identifyCodes.length)];
      }
    },
  },
  mounted() {
    const self = this;
    self.dphone = localStorage.user;
    self.dpass = localStorage.password;
    self.identifyCode = "";
    self.makeCode(this.identifyCodes, 4);
    console.log(this.identifyCode);
  },
  created() {
    this.refreshCode();
  },
};
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg: #283443;
$light_gray: #fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
.code {
  width: 100px;
}
</style>
