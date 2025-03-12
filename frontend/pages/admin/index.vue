<template>
  <view class="admin-container">
    <view class="header">
      <text class="title">管理后台</text>
      <text class="welcome">欢迎回来，{{ userInfo.username }}</text>
    </view>
    
    <view class="menu-list">
      <view class="menu-item" @click="navigateTo('/pages/admin/users')">
        <text class="menu-title">用户管理</text>
        <text class="menu-desc">管理系统用户</text>
      </view>
      
      <view class="menu-item" @click="navigateTo('/pages/admin/settings')">
        <text class="menu-title">系统设置</text>
        <text class="menu-desc">配置系统参数</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userInfo: {
        username: ''
      }
    }
  },
  onLoad() {
    // 从本地存储获取用户信息
    const userInfo = uni.getStorageSync('userInfo')
    if (userInfo) {
      this.userInfo = JSON.parse(userInfo)
    }
    
    // 检查是否是管理员
    if (this.userInfo.role !== 'ROLE_ADMIN') {
      uni.showToast({
        title: '无权访问',
        icon: 'none'
      })
      uni.redirectTo({
        url: '/pages/my/index'
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
  padding: 20px;
  
  .header {
    margin-bottom: 30px;
    
    .title {
      font-size: 24px;
      font-weight: bold;
      color: #333;
      margin-bottom: 10px;
      display: block;
    }
    
    .welcome {
      font-size: 16px;
      color: #666;
    }
  }
  
  .menu-list {
    .menu-item {
      background-color: #fff;
      border-radius: 10px;
      padding: 20px;
      margin-bottom: 15px;
      box-shadow: 0 2px 6px rgba(0,0,0,0.1);
      
      .menu-title {
        font-size: 18px;
        font-weight: bold;
        color: #333;
        margin-bottom: 5px;
        display: block;
      }
      
      .menu-desc {
        font-size: 14px;
        color: #666;
      }
      
      &:active {
        opacity: 0.8;
      }
    }
  }
}
</style> 