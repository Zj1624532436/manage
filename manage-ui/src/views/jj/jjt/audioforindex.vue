<script setup>
import {defineEmits, defineProps, ref, watch} from "vue";
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

defineEmits(['update:modelValue'])
watch(
    ()=>props.modelValue,
    ()=>{
      url.value = request.getServerUrl()+request.getResource()+props.modelValue
    }
)
</script>

<template>
  <div >
    <audio :src='url' controls autoplay style="margin-top: 20%;width: 100%"></audio>
  </div>
</template>

<style scoped lang="scss">

</style>