<template>
  <view class="container">
    <view class="header">
      <text class="title">个人中心</text>
    </view>
    
    <view class="user-card" @tap="handleUserCardClick">
      <image 
        class="avatar" 
        :src="userInfo.avatar || '/static/images/logo5.png'" 
        mode="aspectFill"
      />
      <view class="user-info">
        <text class="username">{{ userInfo.username || '未登录' }}</text>
        <text class="role">{{ userInfo.role === 'ROLE_ADMIN' ? '管理员' : '普通用户' }}</text>
      </view>
    </view>
    
    <!-- 管理员功能区 -->
    <view class="admin-panel" v-if="userInfo.role === 'ROLE_ADMIN'">
      <text class="section-title">管理功能</text>
      <view class="admin-menu">
        <view class="admin-menu-item" @tap="navigateTo('/pages/admin/college/index')">
          <view class="admin-menu-icon">
            <text class="iconfont icon-college"></text>
          </view>
          <text class="admin-menu-text">院系管理</text>
        </view>
        
        <view class="admin-menu-item" @tap="navigateTo('/pages/admin/announcement/index')">
          <view class="admin-menu-icon">
            <text class="iconfont icon-announcement"></text>
          </view>
          <text class="admin-menu-text">公告管理</text>
        </view>
        
        <view class="admin-menu-item" @tap="navigateTo('/pages/admin/user/index')">
          <view class="admin-menu-icon">
            <text class="iconfont icon-user"></text>
          </view>
          <text class="admin-menu-text">用户管理</text>
        </view>
        
        <view class="admin-menu-item" @tap="navigateTo('/pages/admin/system/index')">
          <view class="admin-menu-icon">
            <text class="iconfont icon-setting"></text>
          </view>
          <text class="admin-menu-text">系统设置</text>
        </view>
      </view>
    </view>
    
    <view class="menu-list">
      <view class="menu-item" @tap="navigateTo('/pages/my/profile')">
        <text class="menu-text">个人资料</text>
        <text class="arrow">></text>
      </view>
      
      <view class="menu-item" @tap="navigateTo('/pages/my/change-password')">
        <text class="menu-text">修改密码</text>
        <text class="arrow">></text>
      </view>
      
      <view class="menu-item" @tap="navigateTo('/pages/my/about')">
        <text class="menu-text">关于我们</text>
        <text class="arrow">></text>
      </view>
    </view>
    
    <view class="logout-btn" @tap="handleLogout" v-if="isLoggedIn">
      退出登录
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userInfo: {},
      isLoggedIn: false
    }
  },
  
  onShow() {
    this.loadUserInfo()
  },
  
  methods: {
    loadUserInfo() {
      const userInfo = uni.getStorageSync('userInfo')
      const token = uni.getStorageSync('token')
      
      this.isLoggedIn = !!token
      if (userInfo) {
        this.userInfo = typeof userInfo === 'string' ? JSON.parse(userInfo) : userInfo
      }
    },
    
    navigateTo(url) {
      if (!this.isLoggedIn && url !== '/pages/my/about') {
        uni.navigateTo({
          url: '/pages/my/login'
        })
        return
      }
      uni.navigateTo({ url })
    },
    
    handleLogout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.removeStorageSync('token')
            uni.removeStorageSync('userInfo')
            this.userInfo = {}
            this.isLoggedIn = false
            
            uni.showToast({
              title: '已退出登录',
              icon: 'success'
            })
          }
        }
      })
    },
    
    handleUserCardClick() {
      if (!this.isLoggedIn) {
        uni.navigateTo({
          url: '/pages/my/login'
        })
      }
    }
  }
}
</script>

<style lang="scss">
.container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 20rpx;
}

.header {
  padding: 20rpx;
  text-align: center;
  
  .title {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
  }
}

.user-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
  
  .avatar {
    width: 120rpx;
    height: 120rpx;
    border-radius: 60rpx;
    margin-right: 30rpx;
  }
  
  .user-info {
    flex: 1;
    
    .username {
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
      margin-bottom: 10rpx;
      display: block;
    }
    
    .role {
      font-size: 24rpx;
      color: #666;
    }
  }
}

/* 管理员功能区样式 */
.admin-panel {
  background: #fff;
  border-radius: 16rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
  
  .section-title {
    font-size: 30rpx;
    font-weight: 500;
    color: #333;
    padding: 10rpx 10rpx 20rpx 10rpx;
    display: block;
  }
  
  .admin-menu {
    display: flex;
    flex-wrap: wrap;
    
    .admin-menu-item {
      width: 25%;
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 20rpx 0;
      
      .admin-menu-icon {
        width: 80rpx;
        height: 80rpx;
        background-color: #eaf4ff;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 12rpx;
        
        .iconfont {
          font-size: 40rpx;
          color: #3a86ff;
        }
      }
      
      .admin-menu-text {
        font-size: 24rpx;
        color: #666;
        text-align: center;
      }
    }
  }
}

.menu-list {
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  
  .menu-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 30rpx;
    border-bottom: 1rpx solid #f5f5f5;
    
    &:last-child {
      border-bottom: none;
    }
    
    .menu-text {
      font-size: 28rpx;
      color: #333;
    }
    
    .arrow {
      font-size: 28rpx;
      color: #999;
    }
  }
}

.logout-btn {
  margin-top: 40rpx;
  background: #ff4d4f;
  color: #fff;
  font-size: 32rpx;
  height: 88rpx;
  border-radius: 8rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style> 