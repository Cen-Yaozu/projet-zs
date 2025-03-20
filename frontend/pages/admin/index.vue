<template>
  <view class="admin-container">
    <view class="header">
      <text class="title">管理后台</text>
      <text class="welcome">欢迎回来，{{ userInfo.username }}</text>
    </view>
    
    <view class="menu-list">
      <view class="menu-item" @tap="navigateTo('/pages/admin/user/index')">
        <view class="menu-icon">
          <uni-icons type="person" size="24" color="#4aa3ff"></uni-icons>
        </view>
        <view class="menu-content">
          <text class="menu-title">用户管理</text>
          <text class="menu-desc">管理系统用户账号</text>
        </view>
      </view>
      
      <view class="menu-item" @tap="navigateTo('/pages/admin/college/index')">
        <view class="menu-icon">
          <uni-icons type="home" size="24" color="#4aa3ff"></uni-icons>
        </view>
        <view class="menu-content">
          <text class="menu-title">院系管理</text>
          <text class="menu-desc">管理学院信息</text>
        </view>
      </view>
      
      <view class="menu-item" @tap="navigateTo('/pages/admin/major/index')">
        <view class="menu-icon">
          <uni-icons type="paperplane" size="24" color="#4aa3ff"></uni-icons>
        </view>
        <view class="menu-content">
          <text class="menu-title">专业管理</text>
          <text class="menu-desc">管理专业信息</text>
        </view>
      </view>
      
      <view class="menu-item" @tap="navigateTo('/pages/admin/announcement/index')">
        <view class="menu-icon">
          <uni-icons type="notification" size="24" color="#4aa3ff"></uni-icons>
        </view>
        <view class="menu-content">
          <text class="menu-title">公告管理</text>
          <text class="menu-desc">管理系统公告</text>
        </view>
      </view>
      
      <view class="menu-item" @tap="navigateTo('/pages/admin/system/index')">
        <view class="menu-icon">
          <uni-icons type="gear" size="24" color="#4aa3ff"></uni-icons>
        </view>
        <view class="menu-content">
          <text class="menu-title">系统设置</text>
          <text class="menu-desc">配置系统参数</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userInfo: {
        username: '',
        role: ''
      }
    }
  },
  onShow() {
    // 从本地存储获取用户信息
    const userInfo = uni.getStorageSync('userInfo')
    if (userInfo) {
      this.userInfo = userInfo
      
      // 检查是否是管理员
      if (this.userInfo.role !== 'ROLE_ADMIN') {
        uni.showToast({
          title: '无权访问',
          icon: 'none'
        })
        uni.reLaunch({
          url: '/pages/my/index'
        })
      }
    } else {
      // 如果没有用户信息，跳转到登录页
      uni.reLaunch({
        url: '/pages/my/login'
      })
    }
  },
  methods: {
    navigateTo(url) {
      uni.navigateTo({
        url
      })
    }
  }
}
</script>

<style lang="scss">
.admin-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 30rpx;
  
  .header {
    background: linear-gradient(135deg, #4aa3ff, #3a8fe0);
    padding: 40rpx 30rpx;
    border-radius: 16rpx;
    margin-bottom: 30rpx;
    box-shadow: 0 4rpx 20rpx rgba(74, 163, 255, 0.2);
    
    .title {
      font-size: 38rpx;
      font-weight: bold;
      color: #fff;
      margin-bottom: 16rpx;
      display: block;
    }
    
    .welcome {
      font-size: 28rpx;
      color: rgba(255, 255, 255, 0.9);
    }
  }
  
  .menu-list {
    .menu-item {
      background-color: #fff;
      border-radius: 16rpx;
      padding: 30rpx;
      margin-bottom: 20rpx;
      box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.05);
      display: flex;
      align-items: center;
      transition: transform 0.2s;
      
      .menu-icon {
        width: 80rpx;
        height: 80rpx;
        background: rgba(74, 163, 255, 0.1);
        border-radius: 50%;
        display: flex;
        justify-content: center;
        align-items: center;
        margin-right: 20rpx;
      }
      
      .menu-content {
        flex: 1;
      }
      
      .menu-title {
        font-size: 32rpx;
        font-weight: bold;
        color: #333;
        margin-bottom: 10rpx;
        display: block;
      }
      
      .menu-desc {
        font-size: 26rpx;
        color: #666;
      }
      
      &:active {
        transform: scale(0.98);
        opacity: 0.9;
      }
    }
  }
}
</style> 