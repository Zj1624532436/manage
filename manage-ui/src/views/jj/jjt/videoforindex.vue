<script setup>
import {defineProps, reactive, ref, watch} from "vue";
import 'videojs-contrib-hls';
import videoplay from "@/utils/video.vue";
import request from "@/utils/request";

const props= defineProps(
    {
      modelValue:{
        type:String,
        default:'',
        required:true
      }
    }
)

const url = ref(request.getServerUrl()+request.getResource()+props.modelValue)
const videoSrc = reactive(
    [
      {
        src: url,
        type: 'video/mp4'
      }
    ]
)
const boolean = ref(true)
const videoOptions= ref({
  autoplay: true,
  controls: true,
  posterImage: true,
  loop:true,
  language: 'zh-CN',
  playbackRates: [0.5, 1, 1.5, 2] ,
  sources: videoSrc
})
watch(
    ()=>props.modelValue,
    ()=>{
      boolean.value = false
      url.value = request.getServerUrl()+request.getResource()+props.modelValue
      setTimeout(function() {
        boolean.value = true
      }, 10)
    }
)

</script>

<template>
  <videoplay :options="videoOptions" v-if="boolean"/>
</template>

<style scoped lang="scss">

</style>