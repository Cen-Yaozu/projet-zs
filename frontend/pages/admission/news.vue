<template>
  <view class="container">
    <view class="header">
      <text class="title">招生动态</text>
    </view>
    
    <view class="news-list">
      <view class="news-item" v-for="(item, index) in newsList" :key="index">
        <view class="news-content">
          <view class="news-info">
            <text class="news-title">{{ item.title }}</text>
            <text class="news-date">{{ item.date }}</text>
          </view>
          <image v-if="item.image" class="news-image" :src="item.image" mode="aspectFill" />
        </view>
        <text class="news-summary">{{ item.summary }}</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      newsList: [
        {
          title: '2024年广州航海学院招生计划发布',
          date: '2024-03-01',
          image: '../../static/images/news1.jpg',
          summary: '2024年我校计划招收本科生3000名，涵盖航海类、工程类、管理类等多个专业方向...'
        },
        {
          title: '2024年广东省优质生源基地建设推进会顺利召开',
          date: '2024-02-28',
          image: '../../static/images/news2.jpg',
          summary: '我校与20余所重点中学签署优质生源基地协议，进一步加强校地合作...'
        },
        {
          title: '关于举办2024年高考招生政策宣讲会的通知',
          date: '2024-02-25',
          image: '../../static/images/news3.jpg',
          summary: '为帮助考生和家长更好地了解我校招生政策，定于3月15日举办线上宣讲会...'
        }
      ]
    }
  }
}
</script>

<style lang="scss">
.container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 40rpx;
}

.header {
  background: #fff;
  padding: 40rpx 30rpx;
  margin-bottom: 20rpx;
  
  .title {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
  }
}

.news-list {
  .news-item {
    background: #fff;
    padding: 30rpx;
    margin-bottom: 20rpx;
    
    .news-content {
      display: flex;
      gap: 20rpx;
      margin-bottom: 16rpx;
      
      .news-info {
        flex: 1;
        
        .news-title {
          font-size: 32rpx;
          color: #333;
          line-height: 1.4;
          margin-bottom: 12rpx;
          display: block;
        }
        
        .news-date {
          font-size: 24rpx;
          color: #999;
        }
      }
      
      .news-image {
        width: 200rpx;
        height: 150rpx;
        border-radius: 8rpx;
      }
    }
    
    .news-summary {
      font-size: 28rpx;
      color: #666;
      line-height: 1.5;
    }
  }
}
</style> 