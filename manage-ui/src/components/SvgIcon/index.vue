<template>
  <svg class="svg-icon" aria-hidden="true">
    <use :xlink:href="iconName"></use>
  </svg>
</template>

<script setup>
import { defineProps,computed } from 'vue'

const props = defineProps({
  icon: {
    type: String,
    required: true
  }
})

const iconName = computed(() => { return `#icon-${props.icon}`})
</script>

<style lang="scss" scoped>
.svg-icon {
  width: 1em;
  height: 1em;
  fill: currentColor;
  vertical-align: -0.15em;
  overflow: hidden;
}
</style>