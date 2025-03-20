import App from './App'
import Vue from 'vue'
import store from './store'

Vue.config.productionTip = false

// 配置API基础URL
Vue.prototype.$baseUrl = 'http://localhost:8080'

// 解决页面跳转问题
Vue.prototype.$navigateTo = function(url, params = {}) {
  console.log('准备导航到:', url, params);
  uni.navigateTo({
    url: url,
    fail: (err) => {
      console.error('导航失败:', err, url);
      uni.showToast({
        title: '页面跳转失败',
        icon: 'none'
      });
    }
  });
}

App.mpType = 'app'

const app = new Vue({
    ...App,
    store
})
app.$mount() 