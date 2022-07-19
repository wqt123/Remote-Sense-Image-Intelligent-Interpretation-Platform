<template>
  <div class="login-container">
    <!--注册表单-->
    <el-form
      ref="registerForm"
      :model="registerForm"
      :rules="registerRules"
      class="login-form"
      auto-complete="on"
      label-position="left"
    >
      <div class="title-container">
        <h3 class="title">Register</h3>
      </div>

      <el-form-item prop="id">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="username"
          v-model="registerForm.id"
          placeholder="UserID"
          name="username"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="email">
        <span class="svg-container">
          <svg-icon icon-class="email" />
        </span>
        <el-input
          ref="email"
          v-model="registerForm.email"
          placeholder="email"
          name="email"
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
          v-model="registerForm.password"
          :type="passwordType"
          placeholder="Password"
          name="password"
          tabindex="3"
          auto-complete="on"
          @keyup.enter.native="handleRegister"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon
            :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'"
          />
        </span>
      </el-form-item>
      <el-form-item prop="password2">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType2"
          ref="password2s"
          v-model="registerForm.password2"
          :type="passwordType2"
          placeholder="ConfirmPassword"
          name="password2"
          tabindex="3"
          auto-complete="on"
          @keyup.enter.native="handleRegister"
        />
        <span class="show-pwd" @click="showPwd2">
          <svg-icon
            :icon-class="passwordType2 === 'password' ? 'eye' : 'eye-open'"
          />
        </span>
      </el-form-item>

      <el-form-item prop="code">
        <el-input
          v-model="registerForm.code"
          placeholder="验证码"
          name="password"
          tabindex="4"
          class="code"
        />
        <el-button
          type="primary"
          :disabled="hasSend"
          size="small"
          style="position: absolute; right: 2%; top: 15%"
          @click="sendCode"
          >{{ btnText }}</el-button
        >
      </el-form-item>
      <el-button
        type="primary"
        style="width: 100%; margin-bottom: 30px"
        @click="handleRegister"
        >Register</el-button
      >
      <el-link
        type="success"
        @click="
          () => {
            this.$router.push('/login');
          }
        "
        >点击登录</el-link
      >
    </el-form>
  </div>
</template>

<script>
import { validateNumber, isPassword, validateEMail } from "@/utils/validate";
import { register, getCode } from "@/api/api";

export default {
  name: "register",
  data() {
    const checkPsaaword = (rule, value, callback) => {
      if (value !== this.registerForm.password) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };

    return {
      hasSend: false,
      time: 0,
      timer: null,
      btnText: "获取验证码",
      registerForm: {
        id: "",
        email: "",
        password: "",
        password2: "",
        code: "",
      },
      registerRules: {
        id: [
          {
            required: true,
            trigger: "change",
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
        email: [
          { required: true, trigger: "blur", validator: validateEMail },
          { required: true, trigger: "blur", min: 1 },
        ],
        password: [
          { required: true, trigger: "blur", min: 1 },
          { required: true, trigger: "blur", validator: isPassword },
          { required: true, trigger: "change", validator: isPassword },
        ],
        password2: [
          {
            required: true,
            message: "请重复输入密码",
            trigger: "change",
            min: 1,
          },
          {
            required: true,
            message: "请重复输入密码",
            trigger: "blur",
            min: 1,
          },
          { validator: checkPsaaword, trigger: "change" },
        ],
        code: [
          {
            required: true,
            trigger: "blur",
            min: 1,
            message: "验证码不能为空",
          },
        ],
      },
      passwordType: "password",
      passwordType2: "password",
    };
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
    showPwd2() {
      if (this.passwordType2 === "password") {
        this.passwordType2 = "";
      } else {
        this.passwordType2 = "password";
      }
      this.$nextTick(() => {
        this.$refs.password2.focus();
      });
    },
    sendCode() {
      let reg = /^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/;
      let valid = reg.test(this.registerForm.email);
      if (valid) {
        getCode(this.registerForm.email)
          .then((res) => {
            this.$message({
              type: "success",
              message: "发送成功",
            });
            this.hasSend = true; //禁用按钮，防止多次触发
            this.time = 60; //60秒后能继续按按钮
            this.timer1();
          })
          .catch((error) => {
            this.$message({
              type: "error",
              message: "发送失败",
            });
            console.log(error);
          });
      } else {
        this.$message({
          type: "error",
          message: "邮箱格式错误",
        });
      }
    },
    timer1() {
      //验证码60s内不能继续点击的函数
      if (this.time > 0) {
        this.time--;
        this.btnText = this.time + "s后重新获取";
        this.timer = setTimeout(this.timer1, 1000);
      } else {
        this.time = 0;
        this.btnText = "获取验证码";
        this.canGet = false;
        clearTimeout(this.timer);
      }
    },
    handleRegister() {
      this.$refs.registerForm.validate((valid) => {
        if (valid) {
          let data = {
            code: this.registerForm.code,
            email: this.registerForm.email,
            name: this.registerForm.id,
            pwd: this.registerForm.password,
          };
          register(data)
            .then((response) => {
              console.log(response);
              this.$message({
                type: "success",
                message: "注册成功,您的用户ID是   " + response.data,
              });
              this.$router.go(-1);
            })
            .catch((error) => {
              console.log(error);
            });
        } else {
          this.$message({
            message: "请输入完整信息!",
            type: "error",
          });
          this.loading = false;
          return false;
        }
      });
    },
  },
  mounted() {},
  created() {},
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
