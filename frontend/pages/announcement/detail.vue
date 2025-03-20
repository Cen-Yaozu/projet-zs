<template>
  <view class="container">
    <!-- 加载中提示 -->
    <view class="loading-container" v-if="loading">
      <view class="loading"></view>
      <text class="loading-text">加载中...</text>
    </view>
    
    <!-- 通知公告详情 -->
    <view class="announcement-detail" v-else-if="announcement">
      <!-- 标题 -->
      <view class="detail-header">
        <text class="detail-title">{{ announcement.title }}</text>
        <view class="tag-row">
          <view class="detail-tag" v-if="announcement.type === 'IMPORTANT'">重要</view>
          <text class="detail-time">发布时间：{{ formatDate(announcement.publishTime) }}</text>
        </view>
      </view>
      
      <!-- 内容 -->
      <view class="detail-content">
        <rich-text :nodes="formatContent(announcement.content)"></rich-text>
      </view>
    </view>
    
    <!-- 错误提示 -->
    <view class="error-container" v-else>
      <text class="error-text">该通知公告不存在或已被删除</text>
      <button class="back-btn" @click="goBack">返回列表</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      id: null,
      loading: true,
      announcement: null
    }
  },
  onLoad(options) {
    if (options.id) {
      this.id = options.id;
      this.getAnnouncementDetail();
    } else {
      this.loading = false;
    }
  },
  methods: {
    // 获取通知公告详情
    getAnnouncementDetail() {
      this.loading = true;
      // 直接使用uni.request避开认证
      uni.request({
        url: 'http://localhost:8080/api/announcements/' + this.id,
        method: 'GET',
        success: (res) => {
          if (res.data && res.data.code === 200) {
            this.announcement = res.data.data;
          } else {
            uni.showToast({
              title: '获取公告详情失败',
              icon: 'none'
            });
          }
        },
        fail: (err) => {
          console.error('获取公告详情失败:', err);
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
    
    // 格式化日期
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
    },
    
    // 格式化内容
    formatContent(content) {
      if (!content) return '';
      // 将换行符转换为<br>标签
      return content.replace(/\n/g, '<br>');
    },
    
    // 返回列表
    goBack() {
      uni.navigateBack();
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

.announcement-detail {
  margin: 30rpx;
  background-color: #fff;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
  overflow: hidden;
  
  .detail-header {
    padding: 40rpx;
    border-bottom: 1rpx solid #f0f0f0;
    
    .detail-title {
      font-size: 36rpx;
      color: #333;
      font-weight: bold;
      line-height: 1.4;
    }
    
    .tag-row {
      display: flex;
      align-items: center;
      margin-top: 20rpx;
    }
    
    .detail-tag {
      background-color: #ff6b6b;
      color: #fff;
      font-size: 22rpx;
      padding: 6rpx 16rpx;
      border-radius: 4rpx;
      margin-right: 20rpx;
    }
    
    .detail-time {
      font-size: 24rpx;
      color: #999;
    }
  }
  
  .detail-content {
    padding: 40rpx;
    line-height: 1.8;
    color: #333;
    font-size: 28rpx;
  }
}

.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
  
  .error-text {
    color: #999;
    font-size: 28rpx;
    margin-bottom: 30rpx;
  }
  
  .back-btn {
    background-color: #3399cc;
    color: #fff;
    font-size: 28rpx;
    padding: 16rpx 40rpx;
    border-radius: 50rpx;
    border: none;
  }
}
</style> 