<template>

  <div class="login">
    <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title" >后台管理系统</h3>
      <el-form-item prop="username">
        <el-input type="text" size="large" auto-complete="off" placeholder="账号" clearable  v-model="loginForm.username">
        <template #prefix><svg-icon icon="user"/></template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" size="large" auto-complete="off" placeholder="密码" @keyup.enter="handleLogin" show-password v-model="loginForm.password">
          <template #prefix><svg-icon icon="password"/></template>
        </el-input>
      </el-form-item>
      <el-form-item label="验证码" prop="code" >
        <div class="input-box">
          <el-input v-model="code" maxlength="5" style="width: 150px; float: left" @keyup.enter="handleLogin"></el-input>
          <el-image :src="captchaData.captchaImg" class="captchaImg" @click="getCaptcha" style="padding-left: 5px;padding-bottom: 8px"></el-image>
        </div>
      </el-form-item>
      <el-checkbox v-model="loginForm.rememberMe" style="margin-bottom: 10px;">记住密码</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button size="large" type="primary" style="width:100%;" @click.prevent="handleLogin"><span>登录</span></el-button>
      </el-form-item>
    </el-form>
    <div class="el-login-footer">
      <span>Copyright 2013-2022 <a href="http://www.baidu.com" target="_blank">ZJ．</a>版权所有</span>
    </div><backgroundImage/>
  </div>


</template>

<script setup>
import {ref} from "vue";
import requestUtil from "@/utils/request";
import store from "@/store"
import qs from "qs"
import {ElMessage} from "element-plus";
import router from "@/router";
import BackgroundImage from '@/views/Background.vue';
import Cookies from "js-cookie";
import {encrypt,decrypt} from "@/utils/jsencrypt";

const captchaData = ref({
  uuid:'',
  captchaImg:''
})
const code = ref('')
const backgroundImage = BackgroundImage
const loginRef=ref(null)
const loginForm = ref({ username: '', password: '' , rememberMe : false});
const loginRules = {
  username:[{required:true,trigger:"blur",message:"请输入账号"}],
  password:[{required:true,trigger:"blur",message:"请输入密码"}],
}
const handleLogin = () => {
  loginRef.value.validate(async (valid)=>{
    if(loginForm.value.rememberMe){
      Cookies.set("username",loginForm.value.username,{expires:30});
      Cookies.set("password",loginForm.value.password,{expires:30});
      Cookies.set("rememberMe",loginForm.value.rememberMe,{expires:30});
    }else {
      Cookies.remove("username");
      Cookies.remove("password");
      Cookies.remove("rememberMe");
    }
    if(valid){
      let result=await requestUtil.post(`api/auth/login?code=${code.value}&uuid=${captchaData.value.uuid}`,qs.stringify(loginForm.value))
      if(result.data.code===200){
        const token=result.data.authorization
        const menuList=result.data.menuList
        const currentUser=result.data.currentUser;
        store.commit('SET_TOKEN',token)
        store.commit('SET_MENULIST',menuList)
        store.commit('SET_USERINFO',currentUser)
        ElMessage.success("欢迎访问")
        await router.replace("/home")
        location.reload();
        console.log(menuList)
      }else {
        ElMessage.error(result.data.msg)
      }
    }else {

    }
  })
};
function getCookie(){
  const username = Cookies.get("username");
  const password = Cookies.get("password");
  const rememberMe = Cookies.get("rememberMe");
  loginForm.value = {
    username:username === undefined ? loginForm.value.username:username,
    password:password === undefined ? loginForm.value.password:password,
    rememberMe:rememberMe === undefined ? false:Boolean(rememberMe)
  };
}
const getCaptcha = async () => {
  const captchaRes = await requestUtil.get(`/captcha`);
  if (captchaRes.data.code === 200) {
    captchaData.value.captchaImg = captchaRes.data.captchaImg
    captchaData.value.uuid = captchaRes.data.uuid
  }
}
getCaptcha()
getCookie()
</script>

<style lang="scss" scoped>
a{
  color:white;
}
.login{
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-color: #111;
  background-size: contain;
  canvas {
    width: 100%;
    height: 100%;
    position: absolute;
  }
}
.title{
  margin: 0 auto 30px auto;
  text-align: center;
  color: #707070;
  font-size: 17px;
}
.login-form {
  position: absolute;
  z-index: 1;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  margin: auto;
  padding: 16px 20px 0;
  width: 360px;
  height: 350px;
  font-size: 14px;
  background: #fff;
  border: 1px solid #ccc;
  border-radius: 12px;
  box-shadow: 2px 2px 32px 1px rgba(0, 0, 0, .45);
  opacity: .75;
  h3 {
    margin-top: 0;
    margin-bottom: 0;
    padding: 12px 0;
    font-weight: normal;
    font-size: 22px;
    text-align: center;
  }
  .field {
    display: block;
    margin: 0 auto;
    padding: 6px 0;
  }
  .submit {
    padding: 12px 0;
  }
}
.el-input{
  height: 40px;
  input{
    display: inline-block;
    height: 40px;
  }
}
.input-icon{
  height: 39px;
  width: 14px;
  margin-left: 0;
}
.login-tip{
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code{
  width: 33%;
  height: 40px;
  float: right;
  img{
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer{
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.login-code-img{
  height: 40px;
  padding-left: 12px;
}
</style>
