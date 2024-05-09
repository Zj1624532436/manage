<template>
  <el-tabs
      v-model="editableTabsValue"
      type="border-card"
      class="demo-tabs"
      closable
      @tab-remove="removeTab"
      @tab-click = "clickTab"
  >
    <el-tab-pane
        v-for="item in editableTabs"
        :key="item.name"
        :label="item.title"
        :name="item.name"
    >
      {{ item.content }}
    </el-tab-pane>
  </el-tabs>
</template>

<script setup>
import { ref,watch } from 'vue'
import store from '@/store'
import router from "@/router";

const editableTabsValue = ref(store.state.editableTabsValue)
const editableTabs = ref(store.state.editableTabs)

const removeTab = (targetName) => {
  const tabs = editableTabs.value
  let activeName = editableTabsValue.value
  if(targetName==='/index'){
    return
  }
  if (activeName === targetName) {
    tabs.forEach((tab, index) => {
      if (tab.name === targetName) {
        const nextTab = tabs[index + 1] || tabs[index - 1]
        if (nextTab) {
          activeName = nextTab.name
        }
      }
    })
  }
  editableTabsValue.value = activeName
  editableTabs.value = tabs.filter((tab) => tab.name !== targetName)
  store.state.editableTabsValue = editableTabsValue.value;
  store.state.editableTabs = editableTabs.value;
  router.push({path:activeName})
}
const refreshTabs=()=>{
  editableTabsValue.value=store.state.editableTabsValue;
  editableTabs.value=store.state.editableTabs
}
const clickTab=(target)=>{
  router.push({name:target.props.label})
}
watch(store.state,()=>{
  refreshTabs()
},{deep:true,immediate:true})
</script>
<style lang="scss">
.demo-tabs  {
  color: black;
  font-size: 32px;
}
.el-tabs__item{
  width: 180px;
}
.el-tabs__content{
    display: none;
  }
</style>
