<template>
  <view class="container">
    <!-- 政策列表 -->
    <view class="policy-list">
      <view class="policy-item" v-for="(item, index) in policyList" :key="index" @tap="viewPolicy(item)">
        <view class="policy-content">
          <text class="policy-title">{{ item.title }}</text>
          <text class="policy-desc">{{ item.description }}</text>
          <view class="policy-meta">
            <text class="policy-date">{{ item.date }}</text>
            <text class="policy-type" :style="{ backgroundColor: item.typeColor }">{{ item.type }}</text>
          </view>
        </view>
        <text class="arrow-icon">→</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      policyList: [
        {
          title: '2024年广州航海学院招生章程',
          description: '2024年我校继续面向全国招收普通类本科生，具体招生计划和要求详见本章程...',
          date: '2024-03-11',
          type: '重要',
          typeColor: '#ff6b6b'
        },
        {
          title: '关于做好2024年艺术类专业招生工作的通知',
          description: '为做好2024年艺术类专业招生工作，根据教育部相关规定，现将有关事项通知如下...',
          date: '2024-03-10',
          type: '通知',
          typeColor: '#4aa3ff'
        },
        {
          title: '2024年高水平运动队招生简章',
          description: '根据教育部、广东省教育厅有关规定，结合我校实际情况，制定本年度高水平运动队招生办法...',
          date: '2024-03-09',
          type: '简章',
          typeColor: '#4ecdc4'
        },
        {
          title: '2024年"三位一体"综合评价招生简章',
          description: '为深入推进招生制度改革，促进学生全面发展，提高人才选拔水平，特制定本简章...',
          date: '2024-03-08',
          type: '简章',
          typeColor: '#4ecdc4'
        }
      ]
    }
  },
  methods: {
    viewPolicy(item) {
      // 查看政策详情
      uni.navigateTo({
        url: `/pages/policy/detail?title=${encodeURIComponent(item.title)}`
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
  padding: 20rpx;
}

.policy-list {
  .policy-item {
    background-color: #fff;
    border-radius: 12rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;
    display: flex;
    align-items: center;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.04);
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .policy-content {
    flex: 1;
    margin-right: 20rpx;
  }
  
  .policy-title {
    font-size: 32rpx;
    color: #333;
    font-weight: bold;
    margin-bottom: 10rpx;
  }
  
  .policy-desc {
    font-size: 28rpx;
    color: #666;
    margin-bottom: 20rpx;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    overflow: hidden;
  }
  
  .policy-meta {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  
  .policy-date {
    font-size: 24rpx;
    color: #999;
  }
  
  .policy-type {
    font-size: 24rpx;
    color: #fff;
    padding: 4rpx 16rpx;
    border-radius: 20rpx;
  }
}

.arrow-icon {
  font-size: 32rpx;
  color: #999;
}
</style> 