<template>
  <div>
    <el-row>
      <el-col :span="3">
        <ul>
          <li><el-button class="custom-button" @click="getFileList(null)">首页</el-button></li>
          <li style="padding-bottom: 20px"><el-button class="custom-button" @click="split(path)">上一级</el-button></li>
        </ul>
        <ul class="scrollable-list">
          <li v-for="item in lists" :key="item"><el-button class="custom-button"  @click="getFileList(item) ">{{item}}</el-button></li>
        </ul>
      </el-col>
      <el-col :span="21" >
        <component :is="fileComponentType" v-model="filename" v-if="fileComponentType" :lists="lists" style="max-height: 1100px"></component>
        <iframe :src="pdfUrl" v-if="pdfUrl" style="width: 100%;height: 1200px;border: none"/>
      </el-col>
    </el-row>
  </div>

</template>

<script setup>
import request from "@/utils/request";
import {onMounted, ref, shallowRef} from "vue";
import imgforindex from "./imgforindex.vue"
import videoforindex from "./videoforindex.vue"
import audioforindex from "./audioforindex.vue"

const lists = ref([])
let fileComponentType = shallowRef(null)
const filename = ref('')
const path = ref('')

let result1 = ref([]);
let pdfUrl=ref('');

// 遍历文件名称数组

const split = async (url)=>{
  const arr = url.split('\\')
  const len = arr.length
  arr.value = arr.filter((_, i) => i !== len-1)
  await getFileList(arr.value.join('\\'))
}
const getFileList = async(url)=>{
  let result
  if(url){
    result = await request.get("sys/user/getFileList",{'url': url})
  }
  else {
    result = await request.get("sys/user/getFileList")
  }
  if(result.data.code ===200){
    pdfUrl.value = ''
    lists.value = result.data.list
    path.value = url
    fileComponentType.value = null
    result1.value=result.data.fileName
  }
  if(result.data.code ===500){
    if(result.data.msg.startsWith("img")||result.data.msg.startsWith("gif")||result.data.msg.startsWith("png")||result.data.msg.startsWith("jpg")){
      fileComponentType.value= imgforindex
    }
    else if(result.data.msg.startsWith("mp4")||result.data.msg.startsWith("avi")){
      fileComponentType.value= videoforindex
    }
    else if(result.data.msg.startsWith("pdf")){
      pdfUrl.value =  request.getServerUrl()+request.getResource()+url
    }
    else if(result.data.msg.startsWith("skel")||result.data.msg.startsWith("atlas")||result.data.msg.startsWith("html")){
      const size = url.split('.')[0].split('\\').length
      const rep = url.split('.')[0].split('\\')[size-1]
      const before = url.split('.')[0].replace(rep,'')
      console.log(url.split('.')[0])
      window.open(request.getServerUrl()+request.getResource()+before+'index.html?u='+rep+','+result1.value)
    }
    else if(result.data.msg.startsWith("mp3")){
      fileComponentType.value = audioforindex
    }
    filename.value = url
  }
}
onMounted(async ()=>{
  await getFileList();
})
</script>

<style scoped>
.custom-button {
  /* 其他样式，如背景色、文字颜色等 */
  background-color: white; /* 透明背景 */
  min-width: 100%;
  /* 更多的样式设置... */
  color:black;
}
.scrollable-list {
  min-width: 200px; /* 设置您希望的最大高度 */
  max-height: 1000px;
  overflow-y: auto; /* 当内容超出时显示滚动条 */
  padding: 0; /* 移除默认的padding */
  margin: 0; /* 移除默认的margin */
}

</style>