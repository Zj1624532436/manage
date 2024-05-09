<template>
  <div class="app-wrapper">
    <el-container >
      <el-aside width="200px" class="common-aside"><Menu/></el-aside>
      <el-container class="bak">
        <el-header class="common-header"><Header/></el-header>
        <el-main class="common-main">
          <Tabs/>
          <router-view/>
        </el-main>
        <el-footer><Footer/></el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import Footer from "./footer/Index.vue"
import Header from "./header/Index.vue"
import Menu from "./menu/Index.vue"
import Tabs from "./tabs/Index.vue"
import router from "@/router";
import {onMounted} from "vue";

onMounted(function(){
  router.push({path:'/index'});
})
</script>

<style lang="scss" scoped>
.app-wrapper{
  position: relative;
  width: 100%;
  height: 100%;
}

.sidebar-container{
  background-color: #2d3a4b;
  height: 100%;
}

.el-container {
  height: 100%;
}
.el-header{
  padding-left: 0;
  padding-right: 0;
}
:deep(ul.el-menu){
  border-right-width: 0;
}

.common-layout el-container {
  height: 100%;
}

.common-aside {
  background: #2d3a4b;
  border-right: 0;
  height: 100%;
}

.common-header {
  border-bottom: 1px solid #ccc;
  padding: 0;
}

.common-main {
  transition: box-shadow 0.3s ease-in-out;
  padding: 0 0 20px;
}

//.common-main:hover {
//  box-shadow: 0 0 20px rgba(0, 0, 0, 2);
//}
</style>
