<template>
  <view class="container">
    <view class="header">
      <text class="title">院系专业</text>
    </view>
    
    <view class="department-list">
      <view class="department-item" v-for="(dept, index) in departments" :key="index">
        <view class="dept-header">
          <text class="dept-name">{{ dept.name }}</text>
        </view>
        <view class="major-list">
          <view class="major-item" v-for="(major, mIndex) in dept.majors" :key="mIndex">
            <text class="major-name">{{ major.name }}</text>
            <text class="major-desc">{{ major.description }}</text>
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
      departments: [
        {
          name: '航海学院',
          majors: [
            {
              name: '航海技术',
              description: '培养具备船舶驾驶与管理能力的高级航海人才'
            },
            {
              name: '轮机工程',
              description: '培养船舶动力装置操作与管理的专业人才'
            }
          ]
        },
        {
          name: '船舶与海洋工程学院',
          majors: [
            {
              name: '船舶与海洋工程',
              description: '培养船舶设计与建造的专业技术人才'
            },
            {
              name: '海洋工程与技术',
              description: '培养海洋工程设施建设与管理人才'
            }
          ]
        },
        {
          name: '物流工程学院',
          majors: [
            {
              name: '物流管理',
              description: '培养现代物流系统规划与运营管理人才'
            },
            {
              name: '港口物流工程',
              description: '培养港口运营与管理的专业技术人才'
            }
          ]
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

.department-list {
  .department-item {
    background: #fff;
    margin-bottom: 20rpx;
    
    .dept-header {
      padding: 30rpx;
      border-bottom: 1rpx solid #f0f0f0;
      
      .dept-name {
        font-size: 32rpx;
        font-weight: bold;
        color: #333;
      }
    }
    
    .major-list {
      padding: 20rpx 30rpx;
      
      .major-item {
        padding: 20rpx 0;
        border-bottom: 1rpx solid #f5f5f5;
        
        &:last-child {
          border-bottom: none;
        }
        
        .major-name {
          font-size: 30rpx;
          color: #333;
          margin-bottom: 10rpx;
          display: block;
        }
        
        .major-desc {
          font-size: 26rpx;
          color: #666;
          line-height: 1.5;
        }
      }
    }
  }
}
</style> 