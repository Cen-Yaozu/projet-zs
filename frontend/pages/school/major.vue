<template>
  <view class="container">
    <view class="nav-header">
      <view class="back-button" @click="navBack">
        <text class="iconfont icon-left"></text>
      </view>
      <text class="nav-title">院系专业</text>
      <view class="right-icons">
        <text class="iconfont icon-more"></text>
        <text class="iconfont icon-minus"></text>
        <text class="iconfont icon-round"></text>
      </view>
    </view>
    
    <view class="page-header">
      <text class="page-title">学院专业</text>
      <text class="icon-arrow-right"></text>
    </view>
    
    <view class="section-title">
      <text>专业介绍</text>
    </view>
    
    <!-- 加载中提示 -->
    <view class="loading-container" v-if="loading">
      <view class="loading"></view>
      <text class="loading-text">加载中...</text>
    </view>
    
    <view class="college-list" v-else>
      <view class="college-item" v-for="(college, index) in colleges" :key="index">
        <view class="college-card">
          <view class="college-left">
            <view class="college-name">
              <view class="orange-bar"></view>
              <text>{{ college.name }}</text>
            </view>
            <view class="college-info">
              <view class="info-item">
                <text class="label">学历层次：</text>
                <text class="value">{{ college.degree || '本科' }}</text>
              </view>
              <view class="info-item">
                <text class="label">学制类型：</text>
                <text class="value">{{ college.duration || '4' }}</text>
              </view>
            </view>
          </view>
          <view class="college-right" @click="viewDetail(college)">
            <text>查看详情</text>
            <text class="iconfont icon-right"></text>
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
      loading: true,
      colleges: []
    }
  },
  onLoad() {
    this.loadMajorInfo();
  },
  methods: {
    navBack() {
      uni.navigateBack();
    },
    
    viewDetail(college) {
      console.log('点击查看详情:', college);
      // 跳转到学院详情页面
      uni.navigateTo({
        url: `/pages/school/college-detail?id=${college.id}&name=${encodeURIComponent(college.name)}`,
        success: function() {
          console.log('导航成功');
        },
        fail: function(err) {
          console.error('导航失败:', err);
        }
      });
    },
    
    loadMajorInfo() {
      this.loading = true;
      
      // 获取院系信息
      uni.request({
        url: 'http://localhost:8080/api/college/list',
        method: 'GET',
        success: (res) => {
          if (res.data && res.data.length > 0) {
            // 处理院系数据
            this.colleges = res.data.map(college => ({
              id: college.id,
              name: college.name,
              degree: college.degree || '本科',
              duration: college.duration || '4'
            }));
          } else {
            // 如果获取失败，使用默认数据
            this.colleges = this.getDefaultCollegeData();
            uni.showToast({
              title: '获取院系信息失败',
              icon: 'none'
            });
          }
        },
        fail: () => {
          // 如果接口请求失败，使用默认数据
          this.colleges = this.getDefaultCollegeData();
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
    
    // 处理专业数据，按学院分组
    processCollegeData(majors) {
      // 这里应根据后端返回的数据结构进行处理
      // 由于后端实体没有department字段，我们尝试从专业名称或其他字段推断
      const collegeMap = {};
      
      // 预设几个学院
      const defaultColleges = [
        { id: 1, name: '航运学院', degree: '本科', duration: '4' },
        { id: 2, name: '海洋装备工程学院', degree: '本科', duration: '4' },
        { id: 3, name: '低空装备与智能控制学院', degree: '本科', duration: '4' },
        { id: 4, name: '智能制造学院', degree: '本科', duration: '4' },
        { id: 5, name: '智能交通与工程学院', degree: '本科', duration: '4' }
      ];
      
      // 可以根据专业名称或代码推断所属学院，或者直接使用默认学院
      return defaultColleges;
    },
    
    // 默认学院数据
    getDefaultCollegeData() {
      return [
        { id: 1, name: '航运学院', degree: '本科', duration: '4' },
        { id: 2, name: '海洋装备工程学院', degree: '本科', duration: '4' },
        { id: 3, name: '低空装备与智能控制学院', degree: '本科', duration: '4' },
        { id: 4, name: '智能制造学院', degree: '本科', duration: '4' },
        { id: 5, name: '智能交通与工程学院(未来交通学院)', degree: '本科', duration: '4' }
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

.nav-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #3a86ff;
  color: #fff;
  padding: 20rpx 30rpx;
  position: relative;
  height: 80rpx;
  
  .back-button {
    font-size: 40rpx;
    width: 60rpx;
  }
  
  .nav-title {
    font-size: 34rpx;
    font-weight: 500;
    text-align: center;
    flex: 1;
  }
  
  .right-icons {
    display: flex;
    justify-content: flex-end;
    width: 160rpx;
    
    .iconfont {
      margin-left: 30rpx;
      font-size: 36rpx;
    }
  }
}

.page-header {
  padding: 30rpx;
  background: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  .page-title {
    font-size: 34rpx;
    font-weight: bold;
    color: #333;
  }
  
  .icon-arrow-right {
    color: #999;
    font-size: 30rpx;
  }
}

.section-title {
  padding: 20rpx 30rpx;
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
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

.college-list {
  padding: 0 20rpx;
  
  .college-item {
    margin-bottom: 20rpx;
    
    .college-card {
      background: #fff;
      border-radius: 8rpx;
      padding: 30rpx;
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      .college-left {
        flex: 1;
        
        .college-name {
          display: flex;
          align-items: center;
          margin-bottom: 20rpx;
          
          .orange-bar {
            width: 6rpx;
            height: 30rpx;
            background: #ff9900;
            margin-right: 15rpx;
          }
          
          text {
            font-size: 30rpx;
            font-weight: 500;
            color: #333;
          }
        }
        
        .college-info {
          display: flex;
          
          .info-item {
            margin-right: 40rpx;
            display: flex;
            align-items: center;
            
            .label {
              font-size: 26rpx;
              color: #666;
            }
            
            .value {
              font-size: 26rpx;
              color: #ff9900;
              font-weight: 500;
            }
          }
        }
      }
      
      .college-right {
        display: flex;
        align-items: center;
        color: #999;
        font-size: 26rpx;
        
        .iconfont {
          margin-left: 10rpx;
        }
      }
    }
  }
}
</style> 