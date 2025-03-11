<template>
  <view class="tab-bar">
    <view 
      class="tab-item" 
      v-for="(item, index) in tabList" 
      :key="index"
      @tap="switchTab(item.pagePath)"
      :class="{ active: currentPath === item.pagePath }"
    >
      <uni-icons 
        :type="currentPath === item.pagePath ? item.selectedIcon : item.icon" 
        size="24" 
        :color="currentPath === item.pagePath ? '#007AFF' : '#999999'"
      ></uni-icons>
      <text class="tab-text" :class="{ active: currentPath === item.pagePath }">{{ item.text }}</text>
    </view>
  </view>
</template>

<script>
export default {
  name: 'CustomTabBar',
  data() {
    return {
      currentPath: '/pages/index/index',
      tabList: [
        {
          pagePath: '/pages/index/index',
          text: '首页',
          icon: 'home',
          selectedIcon: 'home-filled'
        },
        {
          pagePath: '/pages/ai/index',
          text: 'AI咨询',
          icon: 'chat',
          selectedIcon: 'chat-filled'
        },
        {
          pagePath: '/pages/my/index',
          text: '我的',
          icon: 'person',
          selectedIcon: 'person-filled'
        }
      ]
    }
  },
  methods: {
    switchTab(path) {
      if (this.currentPath === path) return
      this.currentPath = path
      uni.switchTab({
        url: path
      })
    }
  },
  onShow() {
    const pages = getCurrentPages()
    if (pages.length) {
      this.currentPath = '/' + pages[pages.length - 1].route
    }
  }
}
</script>

<style lang="scss">
.tab-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100rpx;
  background: #FFFFFF;
  display: flex;
  padding-bottom: env(safe-area-inset-bottom);
  border-top: 1rpx solid #eee;
}

.tab-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 10rpx 0;
}

.tab-text {
  font-size: 24rpx;
  color: #999999;
  margin-top: 4rpx;

  &.active {
    color: #007AFF;
  }
}
</style> 