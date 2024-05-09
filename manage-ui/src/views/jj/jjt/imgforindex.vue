<script setup>
import {defineEmits, defineProps, onMounted, ref, watch} from "vue";
import request from "@/utils/request";

const props= defineProps(
    {
      modelValue:{
        type:String,
        default:'',
        required:true
      },
      lists:{
        type:Array,
        default:[],
        required:false
      }
    }
)

const url = ref(request.getServerUrl()+request.getResource()+props.modelValue)
let list = ref([])
for(let file of props.lists){
  if(file.includes('.')){
    list.value.push(request.getServerUrl()+request.getResource()+file)
  }
}
const isLargeNumber = (element) => element === url.value;
let num = list.value.findIndex(isLargeNumber)
defineEmits(['update:modelValue'])
watch(
    ()=>props.modelValue,
    ()=>{
      url.value = request.getServerUrl()+request.getResource()+props.modelValue
      num = list.value.findIndex(isLargeNumber)
    }
)

</script>

<template>
  <div>
    <el-image
        :src="url"
        :zoom-rate="1.1"
        :max-scale="7"
        :min-scale="0.2"
        :preview-src-list="list"
        fit="fill"
        :initial-index = "num"
        lazy
        infinite
    />
  </div>
</template>

<style scoped lang="scss">
</style>