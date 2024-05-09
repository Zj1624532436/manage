<template>
  <router-view/>
</template>

<script setup>
import {watch} from "vue";
import {useRoute, useRouter} from "vue-router";
import store from "@/store";


const router = useRouter()
const route=useRoute();
const whitePath=['/login','/home','/']

//解决屏幕自适应问题
const debounce = (fn, delay) => {
  let timer = null;
  return function () {
    let context = this;
    let args = arguments;
    clearTimeout(timer);
    timer = setTimeout(function () {
      fn.apply(context, args);
    }, delay);
  }
}

const _ResizeObserver = window.ResizeObserver;
window.ResizeObserver = class ResizeObserver extends _ResizeObserver {
  constructor(callback) {
    callback = debounce(callback, 16);
    super(callback);
  }
}

//解决路由改变但router不变的问题
watch(route,(to,from)=>{
  if (whitePath.indexOf(to.path)===-1) {
    let obj = {
      name: to.name,
      path: to.path
    }
    store.commit('ADD_TABS',obj)
  }

},{deep:true,immediate:true})
</script>

<style>
html, body ,#app {
  height: 100%;
}
.app-container{
  padding: 20px;
}
</style>
