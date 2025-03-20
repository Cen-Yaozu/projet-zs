<template>
  <view class="container">
    <!-- 页面标题 -->
    <view class="header">
      <text class="title">通知公告</text>
    </view>
    
    <!-- 加载中提示 -->
    <view class="loading-container" v-if="loading">
      <view class="loading"></view>
      <text class="loading-text">加载中...</text>
    </view>
    
    <!-- 通知公告列表 -->
    <view class="announcement-list" v-else>
      <view class="empty-tip" v-if="announcements.length === 0">
        <text>暂无通知公告</text>
      </view>
      
      <view 
        v-for="(item, index) in announcements" 
        :key="index" 
        class="announcement-item"
        :class="{ 'important': item.type === 'IMPORTANT' }"
        @click="viewDetail(item.id)"
      >
        <view class="announcement-tag" v-if="item.type === 'IMPORTANT'">
          <text>重要</text>
        </view>
        <view class="announcement-content">
          <text class="announcement-title">{{ item.title }}</text>
          <text class="announcement-time">{{ formatDate(item.publishTime) }}</text>
        </view>
        <view class="announcement-arrow">
          <text class="arrow-icon">›</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      loading: true,
      announcements: []
    }
  },
  onLoad() {
    this.getAnnouncements();
  },
  methods: {
    // 获取通知公告列表
    getAnnouncements() {
      this.loading = true;
      // 直接使用uni.request避开认证
      uni.request({
        url: 'http://localhost:8080/api/announcements/active',
        method: 'GET',
        success: (res) => {
          if (res.data && res.data.code === 200) {
            this.announcements = res.data.data || [];
          } else {
            uni.showToast({
              title: '获取通知公告失败',
              icon: 'none'
            });
          }
        },
        fail: (err) => {
          console.error('获取通知公告失败:', err);
          uni.showToast({
            title: '网络异常，请稍后重试',
            icon: 'none'
          });
        },
        complete: () => {
          this.loading = false;
        }
      });
    },
    
    // 查看公告详情
    viewDetail(id) {
      uni.navigateTo({
        url: `/pages/announcement/detail?id=${id}`
      });
    },
    
    // 格式化日期
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
    }
  }
}
</script>

<style lang="scss">
.container {
  min-height: 100vh;
  background-color: #f5f7fa;
  padding-bottom: 30rpx;
}

.header {
  background-color: #3399cc;
  padding: 30rpx;
  
  .title {
    color: #ffffff;
    font-size: 36rpx;
    font-weight: bold;
  }
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
  
  .loading {
    width: 60rpx;
    height: 60rpx;
    border: 6rpx solid #f3f3f3;
    border-top: 6rpx solid #3399cc;
    border-radius: 50%;
    animation: spin 1s linear infinite;
  }
  
  .loading-text {
    color: #999;
    font-size: 28rpx;
    margin-top: 20rpx;
  }
  
  @keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
  }
}

.announcement-list {
  padding: 0 30rpx;
  
  .empty-tip {
    text-align: center;
    padding: 100rpx 0;
    color: #999;
    font-size: 28rpx;
  }
  
  .announcement-item {
    display: flex;
    background-color: #fff;
    border-radius: 12rpx;
    margin-top: 20rpx;
    padding: 30rpx;
    position: relative;
    align-items: center;
    box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
    
    &.important {
      border-left: 8rpx solid #ff6b6b;
    }
    
    .announcement-tag {
      background-color: #ff6b6b;
      color: #fff;
      font-size: 20rpx;
      padding: 4rpx 10rpx;
      border-radius: 4rpx;
      position: absolute;
      top: 30rpx;
      right: 30rpx;
    }
    
    .announcement-content {
      flex: 1;
      
      .announcement-title {
        font-size: 30rpx;
        color: #333;
        font-weight: 500;
        line-height: 1.4;
        margin-bottom: 10rpx;
        padding-right: 60rpx;
      }
      
      .announcement-time {
        font-size: 24rpx;
        color: #999;
      }
    }
    
    .announcement-arrow {
      .arrow-icon {
        font-size: 36rpx;
        color: #ccc;
      }
    }
  }
}
</style> 