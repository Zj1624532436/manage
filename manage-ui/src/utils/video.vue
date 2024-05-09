<template>
  <div ref="videoContainer">
    <video ref="videoPlayer" class="video-js"></video>
  </div>
</template>

<script>
import videojs from 'video.js';
import "videojs-contrib-hls";

export default {
  name: 'VideoPlayer',
  props: {
    options: {
      type: Object,
      default() {
        return {};
      }
    }
  },
  data() {
    return {
      player: null

    }
  },
  mounted() {
    this.player = videojs(this.$refs.videoPlayer, this.options, () => {
      this.player.log('onPlayerReady', this);
    });


  },
  beforeDestroy() {
    if (this.player) {
      this.player.dispose();
    }
  }
}
</script>

<style scoped>
.video-js{
  width: 100%;
  height: 1000px;
}
</style>