<template>
  <div>
    <button @click="startScreenShare">开始屏幕共享</button>
    <video v-if="screenStream" :srcObject="screenStream" autoplay></video>
    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script>
import request from "@/utils/request";
export default {
  data() {
    return {
      screenStream: null,
      error: null
    };
  },
  methods: {
    async startScreenShare() {
      try {
        this.screenStream = await navigator.mediaDevices.getDisplayMedia({ video: true });
        console.log(this.screenStream)
        await this.sendDataChunk();
      } catch (err) {
        this.error = '无法获取屏幕共享，请检查浏览器设置或尝试不同的浏览器。';
      }
    },
    async  sendDataChunk() {
      await request.post('/upload', this.screenStream)
    },


  },
  beforeDestroy() {
    if (this.screenStream) {
      this.screenStream.getTracks().forEach(track => track.stop());
    }
  },
};
</script>

<style scoped>
.error {
  color: red;
}
</style>