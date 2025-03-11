<template>
  <view class="container">
    <!-- 顶部通知栏 -->
    <view class="notice-bar">
      <text class="notice-text">欢迎来到广州航海学院小程序</text>
    </view>

    <!-- 功能导航 -->
    <view class="nav-grid">
      <view class="nav-item" v-for="(item, index) in navList" :key="index" @tap="navigateTo(item.path)">
        <text class="nav-icon">{{ item.icon }}</text>
        <text class="nav-text">{{ item.text }}</text>
      </view>
    </view>

    <!-- 校园资讯 -->
    <view class="news-section">
      <view class="section-title">校园资讯</view>
      <view class="news-list">
        <view class="news-item" v-for="(item, index) in newsList" :key="index" @tap="navigateTo(item.path)">
          <view class="news-content">
            <text class="news-title">{{ item.title }}</text>
            <text class="news-date">{{ item.date }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      navList: [
        { text: '招生政策', icon: '📋', path: '/pages/policy/index' },
        { text: '专业介绍', icon: '📚', path: '/pages/major/index' },
        { text: '智能咨询', icon: '💬', path: '/pages/ai/index' },
        { text: '校园风光', icon: '🏫', path: '/pages/campus/index' }
      ],
      newsList: [
        {
          title: '2024年广州航海学院招生计划发布',
          date: '2024-03-15',
          path: '/pages/news/detail?id=1'
        },
        {
          title: '我校与多家航运企业签署合作协议',
          date: '2024-03-14',
          path: '/pages/news/detail?id=2'
        },
        {
          title: '2023届毕业生就业质量报告发布',
          date: '2024-03-13',
          path: '/pages/news/detail?id=3'
        }
      ]
    }
  },
  methods: {
    navigateTo(path) {
      uni.navigateTo({
        url: path
      })
    }
  }
}
</script>

<style lang="scss">
.container {
  min-height: 100vh;
  background-color: #f7f7f7;
  padding-bottom: 120rpx;
}

.notice-bar {
  background-color: #fff7e6;
  padding: 20rpx 30rpx;
}

.notice-text {
  font-size: 28rpx;
  color: #fa8c16;
}

.nav-grid {
  display: flex;
  flex-wrap: wrap;
  background-color: #fff;
  padding: 30rpx;
  margin: 20rpx;
  border-radius: 12rpx;
}

.nav-item {
  width: 25%;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20rpx;
}

.nav-icon {
  font-size: 40rpx;
  margin-bottom: 10rpx;
}

.nav-text {
  font-size: 24rpx;
  color: #333;
}

.news-section {
  margin: 20rpx;
  padding: 30rpx;
  background-color: #fff;
  border-radius: 12rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
  padding-left: 20rpx;
  border-left: 8rpx solid #4aa3ff;
}

.news-item {
  padding: 20rpx 0;
  border-bottom: 1rpx solid #eee;

  &:last-child {
    border-bottom: none;
  }
}

.news-content {
  display: flex;
  flex-direction: column;
}

.news-title {
  font-size: 28rpx;
  color: #333;
  line-height: 1.4;
}

.news-date {
  font-size: 24rpx;
  color: #999;
  margin-top: 10rpx;
}
</style> 