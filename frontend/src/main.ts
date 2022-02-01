import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap"
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import "@/assets/common.css"
import ko from 'element-plus/es/locale/lang/ko'


createApp(App).use(store).use(router).use(ElementPlus, {locale: ko}).mount('#app')
