<template>
  <div class="navbar">
    <breadcrumb/>
    <div class="navbar-right">
      <time class="text" >{{ formatDate(currentDate) }}</time>
      <avatar/>
    </div>
  </div>
</template>
<script setup>
import breadcrumb from "./component/breadcrumb.vue";
import avatar from "./component/avatar.vue";
import {formatDate} from "@/utils/formatDate";
import {onMounted, ref} from "vue";

const currentDate = ref(new Date())
onMounted(()=>{
  setInterval(() => {
    currentDate.value = new Date()
  },1000);
})
</script>
<style lang="scss" scoped>
.navbar {
  width: 100%;
  height: 60px;
  overflow: hidden;
  background-color: #ffffff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  padding: 0 16px;
  display: flex;
  align-items: center;
  box-sizing: border-box;
  position: relative;
}
.navbar-right {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}
:deep(.navbar-item) {
  display: inline-block;
  margin-left: 18px;
  font-size: 22px;
  color: #5a5e66;
  box-sizing: border-box;
  cursor: pointer;
}
.text{
  padding-right: 50px;
  background-image: -webkit-linear-gradient(left,blue,#66ffff 10%,#cc00ff 20%,#CC00CC 30%, #CCCCFF 40%, #00FFFF 50%,#CCCCFF 60%,#CC00CC 70%,#CC00FF 80%,#66FFFF 90%,blue 100%);
  -webkit-text-fill-color: transparent;
  -webkit-background-clip: text;
  -webkit-background-size: 200% 100%;
  -webkit-animation: masked-animation 4s linear infinite;
  font-size: 20px;
}
</style>