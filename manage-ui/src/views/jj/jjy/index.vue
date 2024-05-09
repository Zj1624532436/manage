<script setup>
import request from "@/utils/request";
import {onMounted, reactive, ref} from 'vue'
import 'videojs-contrib-hls';
import videoplay from "@/utils/video.vue";

const videoSrc = reactive(
    [
      {
        src: '',
        type: 'video/mp4'
      }
    ]
)

const videoOptions= ref({
  autoplay: true,
  controls: true,
  posterImage: true,
  language: 'zh-CN',
  playbackRates: [0.5, 1, 1.5, 2] ,
  sources: videoSrc
})

const url = ref(true)
const initUserList = (url1)=>{
  if(url1){
    videoOptions.value.sources[0].src = request.getServerUrl()+'video/'+url1
  }
  url.value = false
  setTimeout(function() {
    url.value = true
  }, 10)
}

const links= ref(null)

const getList=async () => {
  let result = await request.get("sys/user/list/address");
  if (result.data.code === 200) {
    links.value = result.data.address
  }
}
onMounted(async ()=>{
  await getList();
})

const openProgram= async()=> {
  await request.get("sys/user/start");
}

</script>

<template>
  <el-row>
    <el-col :span="3">
      <el-card>
        <el-collapse model-value="1">
          <el-collapse-item title="ADDRESS"  name="1" >
            <ul ><li v-for="item in links"><a  :href="item.address" target="_blank" class="link">{{item.text}}</a></li></ul>
          </el-collapse-item></el-collapse>
        <ul style="padding-top: 20px">
          <li><el-button @click="openProgram" >启动跨网程序</el-button></li>
        </ul>
      </el-card>
    </el-col>
  </el-row>
</template>

<style scoped lang="scss">

.image{
  width: 100%;
  height: 100%;
}
.link {
  margin-top: 0;
  display: block;
  margin-bottom: 0; /* 调整间隔 */
  font-size: 16px; /* 调整字体大小 */
  font-weight: bold; /* 加粗字体 */
  padding: 5px;
  color: black;
}


</style>