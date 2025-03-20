<template>
  <view class="container">
    <view class="header">
      <text class="title">招生动态</text>
    </view>
    
    <!-- 加载中提示 -->
    <view class="loading-container" v-if="loading">
      <view class="loading"></view>
      <text class="loading-text">加载中...</text>
    </view>
    
    <view class="news-list" v-else>
      <view class="empty-tip" v-if="newsList.length === 0">
        <text>暂无招生动态</text>
      </view>
      
      <view class="news-item" v-for="(item, index) in newsList" :key="index" @click="viewDetail(item.id)">
        <view class="news-content">
          <view class="news-info">
            <text class="news-title">{{ item.title }}</text>
            <text class="news-date">{{ formatDate(item.createTime || item.date) }}</text>
          </view>
          <image v-if="item.imageUrl || item.image" class="news-image" :src="item.imageUrl || item.image" mode="aspectFill" />
        </view>
        <text class="news-summary">{{ item.summary || (item.content && item.content.substring(0, 80) + '...') }}</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      loading: true,
      newsList: []
    }
  },
  onLoad() {
    this.loadNewsData();
  },
  methods: {
    loadNewsData() {
      this.loading = true;
      
      // 获取招生动态信息
      uni.request({
        url: 'http://localhost:8080/api/admission-policy/list',
        method: 'GET',
        success: (res) => {
          if (res.data && res.data.length > 0) {
            // 处理返回的数据
            this.newsList = res.data.map(item => ({
              id: item.id,
              title: item.policyContent ? item.policyContent.split('\n')[0] : '招生政策通知',
              content: item.policyContent,
              imageUrl: item.imageUrl,
              createTime: item.createTime,
              summary: item.policyContent ? 
                      (item.policyContent.length > 80 ? 
                       item.policyContent.substring(0, 80) + '...' : 
                       item.policyContent) : 
                      '暂无详细内容'
            }));
          } else {
            // 如果没有数据，使用默认数据
            this.newsList = this.getDefaultNewsData();
          }
        },
        fail: () => {
          // 如果接口请求失败，使用默认数据
          this.newsList = this.getDefaultNewsData();
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
    
    viewDetail(id) {
      // 跳转到详情页面
      uni.navigateTo({
        url: `/pages/admission/policy-detail?id=${id}`
      });
    },
    
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
    },
    
    // 默认新闻数据
    getDefaultNewsData() {
      return [
        {
          id: 1,
          title: '2024年广州航海学院招生计划发布',
          date: '2024-03-01',
          image: '../../static/images/news1.jpg',
          summary: '2024年我校计划招收本科生3000名，涵盖航海类、工程类、管理类等多个专业方向...'
        },
        {
          id: 2,
          title: '2024年广东省优质生源基地建设推进会顺利召开',
          date: '2024-02-28',
          image: '../../static/images/news2.jpg',
          summary: '我校与20余所重点中学签署优质生源基地协议，进一步加强校地合作...'
        },
        {
          id: 3,
          title: '关于举办2024年高考招生政策宣讲会的通知',
          date: '2024-02-25',
          image: '../../static/images/news3.jpg',
          summary: '为帮助考生和家长更好地了解我校招生政策，定于3月15日举办线上宣讲会...'
        }
      ];
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

.empty-tip {
  text-align: center;
  padding: 100rpx 0;
  color: #999;
  font-size: 28rpx;
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