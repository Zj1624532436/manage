<script setup>
import {ref, watch} from "vue";
import store from "@/store";
import { ArrowDown } from '@element-plus/icons-vue'
import requestUtil,{getServerUrl} from "@/utils/request";
const userinfo = ref(store.getters.GET_USERINFO);
const squareUrl = ref(getServerUrl()+'video/img/'+userinfo.value.avatar)
let username = userinfo.value.username
const logout=async ()=>{
  let result = await requestUtil.get("/logout")
  if (result.data.code === 200){
    store.state.token=null
    sessionStorage.clear()
    store.commit('SET_ROUTES_STATE',false)
    store.commit('RESET_TABS')
    await store.dispatch('logout');
  }
}

</script>

<template>
  <el-dropdown>
    <span class="el-dropdown-link">
      <el-avatar :size="50" :src="squareUrl"/>
      &nbsp;&nbsp;{{username}}
      <el-icon class="el-icon--right">
        <arrow-down />
      </el-icon>
    </span>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item><router-link :to="{name:'个人中心'}">个人中心</router-link></el-dropdown-item>
        <el-dropdown-item divided @click="logout">安全退出</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<style scoped lang="scss">
.el-dropdown-link {
  cursor: pointer;
  color: black;
  display: flex;
  align-items: center;
  font-size: 20px;
}
</style>