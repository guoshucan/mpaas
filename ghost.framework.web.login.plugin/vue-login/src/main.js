// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
Vue.use(ElementUI)
import VueI18n from 'vue-i18n'
Vue.use(VueI18n)
// import axios from './common/AxiosApi.js'
// Vue.prototype.axios = axios
Vue.config.productionTip = false
// 使用语言包
const i18n = new VueI18n({
  locale: localStorage.getItem("Accept-Language") == null ? 'zh-CN' : localStorage.getItem("Accept-Language"),
  // locale: VueCookie.get('language') || 'zh', // 使用vueCookie动态切换语言环境，默认中文
  messages: {
    'zh-CN': require('./locales/zh-CN.json'),
    'zh-TW': require('./locales/zh-TW.json'),
    'en': require('./locales/en.json')
  }
})
new Vue({
  el: '#app',
  i18n,
  router,
  render: h => h(App)
})