<template>
  <view class="container">
    <!-- 顶部Banner -->
    <view class="banner">
      <image 
        class="banner-bg" 
        src="/static/images/banner1.jpg" 
        mode="aspectFill"
        @error="handleImageError('banner')" 
      />
      <view class="school-info">
        <view class="logo-title">
          <image 
            class="school-logo" 
            src="/static/images/logo1.jpg" 
            mode="aspectFit"
            @error="handleImageError('logo')" 
          />
          <view class="title-group">
            <text class="school-name">广州航海学院</text>
            <text class="school-name-en">GUANGZHOU MARITIME UNIVERSITY</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 功能导航 -->
    <view class="nav-grid">
      <view class="nav-item" v-for="(item, index) in navItems" :key="index" @tap="navigateTo(item.url)">
        <view class="nav-icon-wrapper">
          <image class="nav-icon" :src="item.icon" mode="aspectFit" />
        </view>
        <text class="nav-text">{{ item.text }}</text>
      </view>
    </view>

    <!-- 招生信息 -->
    <view class="admission-section">
      <view class="tabs">
        <text 
          v-for="(tab, index) in tabs" 
          :key="index"
          class="tab"
          :class="{ active: currentTab === index }"
          @tap="switchTab(index)"
        >{{ tab }}</text>
      </view>
      <swiper class="news-swiper" :current="currentTab" @change="handleSwiperChange">
        <swiper-item v-for="(list, tabIndex) in newsLists" :key="tabIndex">
          <scroll-view scroll-y class="news-scroll">
            <view class="news-list">
              <view class="news-item" v-for="(item, index) in list" :key="index" @tap="navigateTo(item.url)">
                <view class="news-content">
                  <text class="news-title">{{ item.title }}</text>
                  <image v-if="item.image" class="news-image" :src="item.image" mode="aspectFill" />
                </view>
                <text class="news-date">{{ item.date }}</text>
              </view>
              <view class="more-btn" @tap="navigateTo('/pages/news/list?type=' + currentTab)">查看更多</view>
            </view>
          </scroll-view>
        </swiper-item>
      </swiper>
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
        { icon: '/static/images/logo1.jpg', text: '学校简介', url: '/pages/school/intro' },
        { icon: '/static/images/logo2.jpg', text: '院系专业', url: '/pages/school/major' },
        { icon: '/static/images/logo3.jpg', text: '招生动态', url: '/pages/admission/news' },
        { icon: '/static/images/logo4.jpg', text: '历年分数', url: '/pages/admission/score' }
      ],
      newsLists: [
        [
          { 
            title: '广航召开2024年招生工作总结暨2025年招生工作动员会议',
            date: '2024-10-23',
            image: '/static/images/news1.jpg',
            url: '/pages/news/detail?id=1'
          },
          { 
            title: '梦想启航！广州航海学院2024年录取通知书已全部寄出！',
            date: '2024-08-01',
            image: '/static/images/news2.jpg',
            url: '/pages/news/detail?id=2'
          },
          { 
            title: '广州航海学院2024年夏季高考录取结果最新发布',
            date: '2024-07-08',
            image: '/static/images/news3.jpg',
            url: '/pages/news/detail?id=3'
          }
        ],
        [], // 招生章程列表
        [], // 招生政策列表
        []  // 往年参考列表
      ]
    }
  },
  methods: {
    navigateTo(url) {
      uni.navigateTo({ url })
    },
    switchTab(index) {
      this.currentTab = index
    },
    handleSwiperChange(e) {
      this.currentTab = e.detail.current
    },
    handleImageError(type) {
      console.error(`图片加载失败: ${type}`)
      uni.showToast({
        title: '图片加载失败',
        icon: 'none'
      })
    }
  }
}
</script>

<style lang="scss">
.container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.banner {
  position: relative;
  width: 100%;
  overflow: hidden;
  min-height: 400rpx;
  
  .banner-bg {
    width: 100%;
    height: 400rpx;
    display: block;
    object-fit: cover;
  }
  
  .school-info {
    position: absolute;
    left: 40rpx;
    bottom: 40rpx;
    right: 40rpx;
    z-index: 1;
    
    .logo-title {
      display: flex;
      align-items: center;
      gap: 20rpx;
      
      .school-logo {
        width: 120rpx;
        height: 120rpx;
        border-radius: 60rpx;
        background: #fff;
      }
      
      .title-group {
        .school-name {
          font-size: 48rpx;
          color: #fff;
          font-weight: bold;
          margin-bottom: 8rpx;
          text-shadow: 0 2px 4px rgba(0,0,0,0.2);
          display: block;
        }
        
        .school-name-en {
          font-size: 24rpx;
          color: rgba(255,255,255,0.9);
          display: block;
        }
      }
    }
  }
}

.nav-grid {
  margin: 20rpx;
  padding: 30rpx;
  background: #fff;
  border-radius: 16rpx;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 30rpx;
  
  .nav-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    
    .nav-icon-wrapper {
      width: 100rpx;
      height: 100rpx;
      margin-bottom: 12rpx;
      background: #f8f8f8;
      border-radius: 16rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      
      .nav-icon {
        width: 60rpx;
        height: 60rpx;
      }
    }
    
    .nav-text {
      font-size: 28rpx;
      color: #333;
    }
  }
}

.admission-section {
  margin: 20rpx;
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  
  .tabs {
    display: flex;
    border-bottom: 1rpx solid #f0f0f0;
    
    .tab {
      flex: 1;
      text-align: center;
      font-size: 28rpx;
      color: #666;
      padding: 24rpx 0;
      position: relative;
      transition: all 0.3s;
      
      &.active {
        color: #333;
        font-weight: bold;
        
        &::after {
          content: '';
          position: absolute;
          left: 50%;
          bottom: 0;
          transform: translateX(-50%);
          width: 40rpx;
          height: 4rpx;
          background: #4aa3ff;
          border-radius: 2rpx;
        }
      }
    }
  }
  
  .news-swiper {
    height: 800rpx;
  }
  
  .news-scroll {
    height: 100%;
  }
  
  .news-list {
    padding: 20rpx;
    
    .news-item {
      padding: 20rpx 0;
      border-bottom: 1rpx solid #f5f5f5;
      
      .news-content {
        display: flex;
        align-items: center;
        gap: 20rpx;
        margin-bottom: 12rpx;
        
        .news-title {
          flex: 1;
          font-size: 28rpx;
          color: #333;
          line-height: 1.5;
        }
        
        .news-image {
          width: 160rpx;
          height: 120rpx;
          border-radius: 8rpx;
        }
      }
      
      .news-date {
        font-size: 24rpx;
        color: #999;
      }
    }
  }
  
  .more-btn {
    text-align: center;
    font-size: 28rpx;
    color: #666;
    padding: 20rpx 0;
  }
}
</style> 