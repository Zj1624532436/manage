import { createApp }  from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import '@/assets/styles/border.css'
import '@/assets/styles/reset.css'
import SvgIcon from '@/icons'
import '@/router/permission'
import 'video.js/dist/video-js.css'
import zhCn from "element-plus/es/locale/lang/zh-cn";
import '@/assets/styles/global.css';

const app = createApp(App)
SvgIcon(app)
app.use(store).use(router).use(ElementPlus,{locale:zhCn}).mount('#app')


export const baseUrl="http://192.168.18.168:80/";
export const resource="C/";