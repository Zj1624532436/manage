<script setup>
import {HomeFilled} from '@element-plus/icons-vue'
import {useRoute} from "vue-router";
import {ref,watch} from "vue";
const parentName  = ref('');
const breadcrumbList =ref([]);
const route = useRoute()
const initBreadcrumbList=()=>{
  breadcrumbList.value=route.matched;
  parentName.value=route.meta.parentName;
}
watch(route,()=>{
  initBreadcrumbList();
},{deep:true,immediate:true})
</script>

<template>
  <el-icon><HomeFilled/></el-icon>&nbsp;&nbsp;
  <el-breadcrumb separator="/">
    <el-breadcrumb-item v-for="(item,index) in breadcrumbList">
      <span v-if="parentName && index>0" class="root">{{parentName}}&nbsp;&nbsp;/&nbsp;&nbsp;</span>
      <span class="root">{{ item.name }}</span>
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<style scoped lang="scss">

.root{
  color: black;
  font-weight: 600;
}
</style>