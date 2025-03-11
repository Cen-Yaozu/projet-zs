<template>
  <view class="container">
    <!-- 学校简介 -->
    <view class="school-intro">
      <image src="/static/images/school-banner.jpg" mode="aspectFill" class="school-banner"></image>
      <view class="intro-content">
        <view class="school-name">广州航海学院</view>
        <view class="school-en">Guangzhou Maritime University</view>
        <view class="intro-text">广州航海学院创建于1964年，是一所以航海、轮机、物流为特色，以工为主，工、管、经、文等多学科协调发展的广东省属普通本科院校。</view>
      </view>
    </view>

    <!-- 数据概览 -->
    <view class="data-overview">
      <view class="data-item">
        <text class="data-num">60</text>
        <text class="data-label">年办学历史</text>
      </view>
      <view class="data-item">
        <text class="data-num">40+</text>
        <text class="data-label">本科专业</text>
      </view>
      <view class="data-item">
        <text class="data-num">20000+</text>
        <text class="data-label">在校生</text>
      </view>
      <view class="data-item">
        <text class="data-num">95%</text>
        <text class="data-label">就业率</text>
      </view>
    </view>

    <!-- 功能入口 -->
    <view class="feature-grid">
      <view class="feature-item" v-for="(item, index) in featureList" :key="index" @tap="navigateTo(item.path)">
        <view class="feature-icon" :style="{ backgroundColor: item.bgColor }">
          <text class="feature-icon-text">{{ item.icon }}</text>
        </view>
        <text class="feature-text">{{ item.text }}</text>
      </view>
    </view>

    <!-- 学校荣誉 -->
    <view class="honor-section">
      <view class="section-title">学校荣誉</view>
      <view class="honor-list">
        <view class="honor-item" v-for="(item, index) in honorList" :key="index">
          <text class="honor-text">{{ item }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      featureList: [
        { text: '专业介绍', icon: '📚', path: '/pages/major/list', bgColor: '#4aa3ff' },
        { text: '招生计划', icon: '📅', path: '/pages/plan/index', bgColor: '#ff6b6b' },
        { text: '历年分数', icon: '📊', path: '/pages/score/index', bgColor: '#4ecdc4' },
        { text: '报考指南', icon: '📖', path: '/pages/guide/index', bgColor: '#45b787' },
        { text: '校园风光', icon: '🏫', path: '/pages/campus/index', bgColor: '#a587ff' },
        { text: '联系我们', icon: '📞', path: '/pages/contact/index', bgColor: '#ff9f43' }
      ],
      honorList: [
        '广东省首批硕士学位授予立项建设单位',
        '广东省首届"文明校园"单位',
        '广东省绿色学校',
        '广东省大学生创新创业示范学校',
        '广东省首批创新创业教育示范学校'
      ]
    }
  },
  methods: {
    navigateTo(path) {
      uni.navigateTo({
        url: path
      });
    }
  }
}
</script>

<style lang="scss">
@import '@/static/styles/common.scss';

.container {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.school-intro {
  background-color: #fff;
  margin-bottom: 20rpx;
  
  .school-banner {
    width: 100%;
    height: 400rpx;
    background-color: #4aa3ff;
  }
  
  .intro-content {
    padding: 30rpx;
  }
  
  .school-name {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
  }
  
  .school-en {
    font-size: 24rpx;
    color: #666;
    margin-top: 10rpx;
  }
  
  .intro-text {
    font-size: 28rpx;
    color: #666;
    line-height: 1.6;
    margin-top: 20rpx;
  }
}

.data-overview {
  display: flex;
  background-color: #fff;
  padding: 30rpx 0;
  margin-bottom: 20rpx;
  
  .data-item {
    flex: 1;
    text-align: center;
    position: relative;
    
    &:not(:last-child)::after {
      content: '';
      position: absolute;
      right: 0;
      top: 50%;
      transform: translateY(-50%);
      width: 1rpx;
      height: 60%;
      background-color: #eee;
    }
  }
  
  .data-num {
    font-size: 36rpx;
    color: $primary-color;
    font-weight: bold;
    display: block;
  }
  
  .data-label {
    font-size: 24rpx;
    color: #666;
    margin-top: 10rpx;
    display: block;
  }
}

.feature-grid {
  display: flex;
  flex-wrap: wrap;
  background-color: #fff;
  padding: 20rpx;
  margin-bottom: 20rpx;
  
  .feature-item {
    width: 33.33%;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20rpx 0;
  }
  
  .feature-icon {
    width: 80rpx;
    height: 80rpx;
    border-radius: 20rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 10rpx;
  }

  .feature-icon-text {
    font-size: 32rpx;
    color: #FFFFFF;
  }
  
  .feature-text {
    font-size: 24rpx;
    color: #333;
  }
}

.honor-section {
  background-color: #fff;
  padding: 30rpx;
  
  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
  }
  
  .honor-list {
    .honor-item {
      background-color: #f5f7fa;
      padding: 20rpx;
      border-radius: 8rpx;
      margin-bottom: 15rpx;
      
      &:last-child {
        margin-bottom: 0;
      }
    }
    
    .honor-text {
      font-size: 28rpx;
      color: #666;
      line-height: 1.4;
    }
  }
}
</style> 