<template>
  <view class="home-container">
    <!-- 顶部Banner -->
    <swiper class="home-banner" circular autoplay interval="5000" duration="500" indicator-dots 
      indicator-active-color="#4aa3ff" indicator-color="rgba(255,255,255,0.5)">
      <swiper-item>
        <image 
          class="home-banner__image" 
          src="../../static/images/banner1.jpg" 
          mode="aspectFill"
          @error="handleImageError('banner')" 
        />
        <view class="home-banner__overlay">
          <view class="home-banner__content">
            <text class="home-banner__title">遇见理想的大学</text>
            <text class="home-banner__subtitle">成就你的未来梦想</text>
          </view>
        </view>
      </swiper-item>
    </swiper>

    <!-- 功能导航 -->
    <view class="home-nav">
      <view class="home-nav__card">
        <view class="home-nav__grid">
          <view class="home-nav__item" v-for="(item, index) in navItems" :key="index" @tap="navigateTo(item.url)">
            <view class="home-nav__icon-wrapper" :style="{backgroundColor: item.bgColor || '#ebf5ff'}">
              <image class="home-nav__icon" :src="item.icon" mode="aspectFit" />
            </view>
            <text class="home-nav__text">{{ item.text }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 招生信息 -->
    <view class="home-section">
      <view class="home-section__header">
        <text class="home-section__title">
          <text class="home-section__title-icon"></text>
          招生信息
        </text>
        <text class="home-section__more" @tap="navigateTo('/pages/announcement/index')">查看全部</text>
      </view>
      
      <view class="home-tabs">
        <view class="home-tabs__header">
          <view 
            v-for="(tab, index) in tabs" 
            :key="index"
            class="home-tabs__item" :class="{ 'home-tabs__item--active': currentTab === index }"
            @tap="switchTab(index)"
          >
            <text class="home-tabs__text">{{ tab }}</text>
          </view>
        </view>
        
        <swiper class="home-tabs__content" :current="currentTab" @change="handleSwiperChange">
          <swiper-item v-for="(list, tabIndex) in newsLists" :key="tabIndex">
            <scroll-view scroll-y class="home-tabs__scroll">
              <view class="home-news">
                <view v-if="list.length === 0" class="home-news__empty">
                  <image class="home-news__empty-icon" src="/static/images/empty.png" mode="aspectFit"></image>
                  <text class="home-news__empty-text">暂无数据</text>
                </view>
                <view 
                  class="home-news__item" 
                  v-for="(item, index) in list" 
                  :key="index" 
                  @tap="navigateTo(item.url)"
                  :style="{animationDelay: index * 0.1 + 's'}"
                >
                  <view class="home-news__content">
                    <text class="home-news__title">{{ item.title }}</text>
                    <view class="home-news__meta">
                      <text class="home-news__date">{{ item.date }}</text>
                      <view class="home-news__tag">{{ tabs[tabIndex] }}</view>
                    </view>
                  </view>
                  <image v-if="item.image" class="home-news__image" :src="item.image" mode="aspectFill" />
                </view>
              </view>
            </scroll-view>
          </swiper-item>
        </swiper>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      tabs: ['招生公告', '招生章程', '招生政策', '往年参考'],
      currentTab: 0,
      navItems: [
        { 
          icon: '/static/images/logo1.jpg', 
          text: '学校简介', 
          url: '/pages/school/intro',
          bgColor: '#e6f7ff'
        },
        { 
          icon: '/static/images/logo2.jpg', 
          text: '院系专业', 
          url: '/pages/school/major',
          bgColor: '#ebf5ff'
        },
        { 
          icon: '/static/images/logo3.jpg', 
          text: '招生动态', 
          url: '/pages/admission/news',
          bgColor: '#e6fffa'
        },
        { 
          icon: '/static/images/logo4.jpg', 
          text: '历年分数', 
          url: '/pages/admission/score',
          bgColor: '#f9f0ff'
        }
      ],
      newsLists: [
        [], // 招生公告列表
        [], // 招生章程列表
        [], // 招生政策列表
        []  // 往年参考列表
      ]
    }
  },
  onLoad() {
    this.getAnnouncementData();
  },
  methods: {
    // 获取公告数据
    getAnnouncementData() {
      uni.showLoading({
        title: '加载中...'
      });
      
      // 获取所有通知公告
      uni.request({
        url: this.$baseUrl + '/api/announcements/active',
        method: 'GET',
        success: (res) => {
          if (res.data && res.data.code === 200) {
            const announcements = res.data.data || [];
            
            // 初始化列表
            this.newsLists = [[], [], [], []];
            
            // 根据类型分类公告
            announcements.forEach(item => {
              let tabIndex = 0; // 默认为招生公告
              
              // 根据类别分类
              switch(item.category) {
                case 'GENERAL_NOTICE':
                  tabIndex = 0; // 招生公告
                  break;
                case 'ADMISSION_RULES':
                  tabIndex = 1; // 招生章程
                  break;
                case 'ADMISSION_POLICY':
                  tabIndex = 2; // 招生政策
                  break;
                case 'HISTORICAL_REFERENCE':
                  tabIndex = 3; // 往年参考
                  break;
              }
              
              // 添加到对应分类
              this.newsLists[tabIndex].push({
                title: item.title,
                date: this.formatDate(item.publishTime),
                image: item.imageUrl || '/static/images/default-news.jpg',
                url: `/pages/announcement/detail?id=${item.id}`
              });
            });
          } else {
            // 如果后端返回错误，显示空数据
            this.newsLists = [[], [], [], []];
            uni.showToast({
              title: '获取公告失败',
              icon: 'none'
            });
          }
        },
        fail: () => {
          // API请求失败，显示空数据
          this.newsLists = [[], [], [], []];
          uni.showToast({
            title: '获取公告失败',
            icon: 'none'
          });
        },
        complete: () => {
          uni.hideLoading();
        }
      });
    },
    
    // 格式化日期
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
    },
    
    // 页面导航
    navigateTo(url) {
      uni.navigateTo({ url });
    },
    
    // 切换标签页
    switchTab(index) {
      this.currentTab = index;
    },
    
    // 轮播图切换
    handleSwiperChange(e) {
      this.currentTab = e.detail.current;
    },
    
    // 图片加载错误处理
    handleImageError(type) {
      console.error(`图片加载失败: ${type}`);
      // 将当前图片添加error类
      event.target.classList.add('error');
    }
  }
}
</script>

<style lang="scss">
@import '../../styles/common.scss';

.home-container {
  min-height: 100vh;
  background-color: $gray-100;
}

// Banner 样式
.home-banner {
  position: relative;
  width: 100%;
  height: 400rpx;
  
  &__image {
    width: 100%;
    height: 100%;
  }
  
  &__overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(to bottom, rgba(0,0,0,0.1), rgba(0,0,0,0.4));
    display: flex;
    align-items: center;
    padding: 40rpx;
  }
  
  &__content {
    animation: fadeInUp 0.8s;
  }
  
  &__title {
    font-size: $font-size-xlarge;
    color: $white;
    font-weight: bold;
    text-shadow: 0 2rpx 4rpx rgba(0,0,0,0.3);
    display: block;
    margin-bottom: 12rpx;
  }
  
  &__subtitle {
    font-size: $font-size-medium;
    color: $white;
    text-shadow: 0 2rpx 4rpx rgba(0,0,0,0.3);
    opacity: 0.9;
  }
}

// 导航菜单样式
.home-nav {
  margin: -40rpx 20rpx 20rpx;
  position: relative;
  z-index: 10;
  
  &__card {
    background-color: $white;
    border-radius: $radius-large;
    padding: 30rpx;
    box-shadow: $shadow-base;
  }
  
  &__grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20rpx;
  }
  
  &__item {
    display: flex;
    flex-direction: column;
    align-items: center;
    transition: $transition-base;
    
    &:active {
      transform: scale(0.95);
    }
  }
  
  &__icon-wrapper {
    width: 100rpx;
    height: 100rpx;
    margin-bottom: 12rpx;
    border-radius: $radius-medium;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: $transition-base;
    
    .home-nav__item:active & {
      transform: translateY(-5rpx);
    }
  }
  
  &__icon {
    width: 60rpx;
    height: 60rpx;
  }
  
  &__text {
    font-size: $font-size-small;
    color: $gray-700;
    line-height: 1.4;
    text-align: center;
  }
}

// 主体内容区块样式
.home-section {
  margin: 30rpx 20rpx;
  background-color: $white;
  border-radius: $radius-large;
  overflow: hidden;
  box-shadow: $shadow-light;
  
  &__header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 24rpx;
    border-bottom: 1rpx solid $gray-200;
  }
  
  &__title {
    font-size: $font-size-medium;
    font-weight: 500;
    color: $gray-900;
    display: flex;
    align-items: center;
    
    &-icon {
      display: inline-block;
      width: 6rpx;
      height: 30rpx;
      background-color: $primary-color;
      margin-right: 16rpx;
      border-radius: $radius-small;
    }
  }
  
  &__more {
    font-size: $font-size-small;
    color: $gray-600;
    
    &:active {
      color: $primary-color;
    }
  }
}

// 标签页样式
.home-tabs {
  &__header {
    display: flex;
    border-bottom: 1rpx solid $gray-200;
    background-color: $gray-100;
  }
  
  &__item {
    flex: 1;
    text-align: center;
    padding: 20rpx 0;
    position: relative;
    transition: $transition-base;
    
    &--active {
      color: $primary-color;
      font-weight: 500;
      
      &::after {
        content: '';
        position: absolute;
        left: 50%;
        bottom: 0;
        transform: translateX(-50%);
        width: 40rpx;
        height: 4rpx;
        background: $primary-color;
        border-radius: $radius-small;
      }
      
      .home-tabs__text {
        color: $primary-color;
      }
    }
  }
  
  &__text {
    font-size: $font-size-small;
    color: $gray-600;
    transition: $transition-base;
  }
  
  &__content {
    height: 800rpx;
  }
  
  &__scroll {
    height: 100%;
  }
}

// 新闻列表样式
.home-news {
  padding: 10rpx 20rpx;
  
  &__empty {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 80rpx 0;
    
    &-icon {
      width: 200rpx;
      height: 200rpx;
      margin-bottom: 20rpx;
      opacity: 0.5;
    }
    
    &-text {
      font-size: $font-size-small;
      color: $gray-500;
    }
  }
  
  &__item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 24rpx;
    border-bottom: 1rpx solid $gray-200;
    transition: $transition-base;
    animation: fadeInRight 0.5s both;
    
    &:active {
      background-color: $gray-100;
      transform: translateX(5rpx);
    }
  }
  
  &__content {
    flex: 1;
    margin-right: 20rpx;
  }
  
  &__title {
    font-size: $font-size-base;
    color: $gray-800;
    line-height: 1.5;
    margin-bottom: 16rpx;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }
  
  &__meta {
    display: flex;
    align-items: center;
  }
  
  &__date {
    font-size: $font-size-mini;
    color: $gray-500;
    margin-right: 16rpx;
  }
  
  &__tag {
    display: inline-block;
    font-size: $font-size-mini;
    color: $primary-color;
    background-color: $primary-light-color;
    padding: 4rpx 12rpx;
    border-radius: $radius-small;
  }
  
  &__image {
    width: 180rpx;
    height: 120rpx;
    border-radius: $radius-medium;
    object-fit: cover;
  }
}

// 动画
@keyframes fadeInRight {
  from {
    opacity: 0;
    transform: translateX(20rpx);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}
</style> 