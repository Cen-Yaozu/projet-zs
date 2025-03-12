import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    // 用户信息
    userInfo: null,
    // 是否已登录
    hasLogin: false,
    // token
    token: '',
    // 系统信息
    systemInfo: null
  },
  mutations: {
    // 登录成功
    login(state, userInfo) {
      state.userInfo = userInfo
      state.hasLogin = true
      state.token = userInfo.token
      // 保存登录状态到本地
      uni.setStorageSync('userInfo', userInfo)
      uni.setStorageSync('token', userInfo.token)
    },
    // 退出登录
    logout(state) {
      state.userInfo = null
      state.hasLogin = false
      state.token = ''
      // 移除本地登录状态
      uni.removeStorageSync('userInfo')
      uni.removeStorageSync('token')
    },
    // 设置系统信息
    setSystemInfo(state, systemInfo) {
      state.systemInfo = systemInfo
    }
  },
  actions: {
    // 初始化用户信息
    initUser({ commit }) {
      try {
        // 尝试从本地获取用户信息
        const userInfo = uni.getStorageSync('userInfo')
        const token = uni.getStorageSync('token')
        if (userInfo && token) {
          // 添加token到userInfo对象
          if (!userInfo.token) {
            userInfo.token = token
          }
          commit('login', userInfo)
          return true
        }
        return false
      } catch (e) {
        console.error('初始化用户信息失败:', e)
        return false
      }
    },
    // 获取系统信息
    getSystemInfo({ commit }) {
      return new Promise((resolve) => {
        uni.getSystemInfo({
          success: function(res) {
            commit('setSystemInfo', res)
            resolve(res)
          }
        })
      })
    }
  }
})

export default store 