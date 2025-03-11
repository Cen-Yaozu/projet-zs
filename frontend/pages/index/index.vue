<template>
  <view class="container">
    <!-- 顶部学校形象图 -->
    <view class="header-image">
      <image src="/static/images/school-header.jpg" mode="widthFix" class="header-bg"></image>
    </view>
    
    <!-- 学校基本信息 -->
    <view class="school-info">
      <view class="school-logo">
        <view class="logo-circle">
          <text>航</text>
        </view>
      </view>
      <view class="school-name-container">
        <text class="name">广州航海学院</text>
        <text class="en-name">GUANGZHOU MARITIME UNIVERSITY</text>
      </view>
      
      <view class="info-list">
        <view class="info-item">
          <text class="label">建校时间：</text>
          <text class="value">1964年</text>
        </view>
        <view class="info-item">
          <text class="label">办学层次：</text>
          <text class="value">本科类</text>
        </view>
        <view class="info-item">
          <text class="label">学校性质：</text>
          <text class="value">广东省属</text>
        </view>
        <view class="info-item">
          <text class="label">学校类别：</text>
          <text class="value">公办院校</text>
        </view>
      </view>
      
      <view class="tags">
        <view class="tag">广东省绿色学校</view>
        <view class="tag">广东省首届"文明校园"单位</view>
        <view class="tag">广东省硕士学位授予立项建设单位</view>
      </view>
    </view>
    
    <!-- 功能按钮区 -->
    <view class="function-grid">
      <view class="function-item" v-for="(item, index) in functionList" :key="index" @tap="navigateTo(item.url)">
        <view class="function-icon-box">
          <uni-icons :type="item.icon" size="30" color="#3c7fef"></uni-icons>
        </view>
        <text class="function-text">{{item.text}}</text>
      </view>
    </view>
    
    <!-- 宣传横幅 -->
    <view class="promotion-banner">
      <image src="/static/images/banner.jpg" mode="widthFix" class="banner-img"></image>
    </view>
    
    <!-- 功能卡片区域 -->
    <view class="feature-cards">
      <view class="feature-row">
        <view class="feature-card admission-card" @tap="navigateTo('/pages/admission/query')">
          <text class="card-title">录取查询</text>
          <text class="card-action">点击进入 →</text>
        </view>
        <view class="feature-card vr-card" @tap="navigateTo('/pages/vr/campus')">
          <text class="card-title">VR校园</text>
          <text class="card-action">点击进入 →</text>
        </view>
      </view>
      <view class="feature-row">
        <view class="feature-card video-card" @tap="navigateTo('/pages/videos/index')">
          <text class="card-title">广航视频</text>
          <text class="card-action">点击进入 →</text>
        </view>
        <view class="feature-card style-card" @tap="navigateTo('/pages/style/index')">
          <text class="card-title">广航风采</text>
          <text class="card-action">点击进入 →</text>
        </view>
      </view>
    </view>
    
    <!-- 新闻公告标签栏 -->
    <view class="news-tabs">
      <view 
        v-for="(tab, index) in newsTabs" 
        :key="index" 
        class="tab-item" 
        :class="{ active: currentTab === index }"
        @tap="switchTab(index)"
      >
        <text>{{tab}}</text>
      </view>
    </view>
    
    <!-- 新闻公告列表 -->
    <view class="news-list">
      <view class="news-item" v-for="(item, index) in newsList" :key="index" @tap="viewNews(item)">
        <text class="news-title">{{item.title}}</text>
        <text class="news-date">{{item.date}}</text>
      </view>
      <view class="load-more" @tap="loadMore">查看更多</view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      functionList: [
        { text: '学校简介', icon: 'home', url: '/pages/school/intro' },
        { text: '院系专业', icon: 'folder', url: '/pages/school/departments' },
        { text: '招生动态', icon: 'notification', url: '/pages/admission/news' },
        { text: '历年分数', icon: 'chart', url: '/pages/admission/scores' },
        { text: '招生计划', icon: 'calendar', url: '/pages/admission/plan' },
        { text: '校园新闻', icon: 'sound', url: '/pages/news/index' },
        { text: '学子风采', icon: 'star', url: '/pages/student/style' },
        { text: '入学须知', icon: 'help', url: '/pages/admission/notice' }
      ],
      newsTabs: ['招生公告', '招生章程', '招生政策', '往年参考'],
      currentTab: 0,
      newsList: [
        { id: 1, title: '[带你看广航] 招盟回响——清远', date: '2023-03-03' },
        { id: 2, title: '[带你看广航] 招盟回响——汕尾、揭阳', date: '2023-03-01' },
        { id: 3, title: '[带你看广航] 广州航海学院DeepSeek版"智能招生问答系统"来啦!', date: '2023-02-28' },
        { id: 4, title: '[带你看广航] 招盟回响——云浮', date: '2023-02-27' },
        { id: 5, title: '[带你看广航] 招盟回响——茂名', date: '2023-02-26' }
      ]
    }
  },
  onLoad() {
    // 页面加载时的逻辑
    uni.setNavigationBarTitle({
      title: '首页'
    });
  },
  methods: {
    navigateTo(url) {
      uni.navigateTo({
        url: url
      });
    },
    switchTab(index) {
      this.currentTab = index;
      // 根据选中的标签加载不同类型的新闻数据
      this.loadNewsByType(index);
    },
    loadNewsByType(type) {
      // 这里应该有一个异步请求获取对应类型的新闻数据
      // 这里用静态数据模拟
      console.log('加载类型为', this.newsTabs[type], '的新闻');
    },
    viewNews(item) {
      uni.navigateTo({
        url: `/pages/news/detail?id=${item.id}`
      });
    },
    loadMore() {
      // 加载更多新闻
      uni.showToast({
        title: '加载更多新闻',
        icon: 'none'
      });
    }
  }
}
</script>

<style>
.container {
  padding-bottom: 30rpx;
  background-color: #f8f8f8;
}

.header-image {
  width: 100%;
  height: auto;
  overflow: hidden;
}

.header-bg {
  width: 100%;
  display: block;
}

.school-info {
  padding: 30rpx;
  background-color: #fff;
  border-radius: 10rpx;
  margin: -40rpx 20rpx 20rpx;
  position: relative;
  z-index: 10;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.school-logo {
  text-align: center;
  margin-bottom: 20rpx;
}

.logo-circle {
  width: 120rpx;
  height: 120rpx;
  margin: 0 auto;
  background-color: #3c7fef;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #fff;
  font-size: 48rpx;
  font-weight: bold;
}

.school-name-container {
  text-align: center;
  margin-bottom: 30rpx;
}

.name {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
  display: block;
}

.en-name {
  font-size: 24rpx;
  color: #666;
  margin-top: 10rpx;
  display: block;
}

.info-list {
  margin-top: 20rpx;
}

.info-item {
  display: flex;
  margin-bottom: 15rpx;
  font-size: 28rpx;
}

.label {
  color: #666;
  width: 160rpx;
}

.value {
  color: #333;
  flex: 1;
}

.tags {
  display: flex;
  flex-direction: column;
  margin-top: 20rpx;
}

.tag {
  display: inline-block;
  background-color: #e9f5ff;
  color: #3c7fef;
  font-size: 26rpx;
  padding: 12rpx 20rpx;
  border-radius: 10rpx;
  margin-bottom: 15rpx;
  max-width: 100%;
}

.function-grid {
  display: flex;
  flex-wrap: wrap;
  padding: 20rpx;
  background-color: #fff;
  margin: 20rpx;
  border-radius: 10rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.function-item {
  width: 25%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 0;
}

.function-icon-box {
  width: 100rpx;
  height: 100rpx;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 15rpx;
  background-color: #f5f8ff;
  border-radius: 15rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
}

.function-text {
  font-size: 26rpx;
  color: #333;
}

.promotion-banner {
  width: 100%;
  padding: 0 20rpx;
  box-sizing: border-box;
  margin-bottom: 20rpx;
}

.banner-img {
  width: 100%;
  border-radius: 10rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.1);
}

.feature-cards {
  padding: 0 20rpx;
}

.feature-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.feature-card {
  width: 48%;
  color: #fff;
  border-radius: 10rpx;
  padding: 30rpx 20rpx;
  box-sizing: border-box;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.1);
}

.admission-card {
  background-color: #4aa3ff;
}

.vr-card {
  background-color: #5ca2e5;
}

.video-card {
  background-color: #3c7fef;
}

.style-card {
  background-color: #509be2;
}

.card-title {
  font-size: 30rpx;
  font-weight: bold;
  display: block;
  margin-bottom: 20rpx;
}

.card-action {
  font-size: 24rpx;
  opacity: 0.9;
}

.news-tabs {
  display: flex;
  background-color: #fff;
  margin: 30rpx 20rpx 0;
  border-top-left-radius: 10rpx;
  border-top-right-radius: 10rpx;
  overflow: hidden;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 25rpx 0;
  font-size: 28rpx;
  color: #666;
  position: relative;
}

.tab-item.active {
  color: #3c7fef;
  font-weight: bold;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 60rpx;
  height: 6rpx;
  background-color: #3c7fef;
  border-radius: 3rpx;
}

.news-list {
  background-color: #fff;
  padding: 0 30rpx;
  margin: 0 20rpx 20rpx;
  border-bottom-left-radius: 10rpx;
  border-bottom-right-radius: 10rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.news-item {
  padding: 30rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.news-title {
  font-size: 28rpx;
  color: #333;
  line-height: 1.6;
}

.news-date {
  font-size: 24rpx;
  color: #999;
  margin-top: 10rpx;
  display: block;
}

.load-more {
  text-align: center;
  padding: 30rpx 0;
  font-size: 28rpx;
  color: #3c7fef;
}
</style> 