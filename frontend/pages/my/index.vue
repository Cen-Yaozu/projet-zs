<template>
  <view class="container">
    <!-- 用户信息卡片 -->
    <view class="user-card">
      <view class="avatar">
        <image class="avatar-img" src="/static/avatar.png" mode="aspectFill" />
      </view>
      <view class="user-info">
        <text class="username">{{ userInfo.username || '未登录' }}</text>
        <text class="role" v-if="isLoggedIn">{{ roleText }}</text>
      </view>
    </view>

    <!-- 功能菜单 -->
    <view class="menu-list" v-if="isLoggedIn">
      <!-- 个人信息 -->
      <view class="menu-section">
        <view class="section-title">个人信息</view>
        <view class="menu-item" @tap="navigateTo('/pages/my/profile')">
          <text class="item-text">修改资料</text>
          <text class="arrow">></text>
        </view>
        <view class="menu-item" @tap="navigateTo('/pages/my/change-password')">
          <text class="item-text">修改密码</text>
          <text class="arrow">></text>
        </view>
      </view>

      <!-- 系统相关 -->
      <view class="menu-section">
        <view class="section-title">系统</view>
        <view class="menu-item" @tap="navigateTo('/pages/my/about')">
          <text class="item-text">关于我们</text>
          <text class="arrow">></text>
        </view>
        <view class="menu-item" @tap="handleLogout">
          <text class="item-text logout">退出登录</text>
          <text class="arrow">></text>
        </view>
      </view>
    </view>

    <!-- 未登录时显示的登录按钮 -->
    <view v-if="!isLoggedIn" class="login-btn-wrapper">
      <button class="login-btn" @tap="goToLogin">立即登录</button>
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
  computed: {
    roleText() {
      if (!this.userInfo.role) return '';
      return this.userInfo.role === 'ROLE_ADMIN' ? '管理员' : '普通用户';
    }
  },
  onShow() {
    this.loadUserInfo()
  },
  onLoad() {
    this.loadUserInfo()
  },
  methods: {
    loadUserInfo() {
      try {
        const token = uni.getStorageSync('token')
        const userInfoStr = uni.getStorageSync('userInfo')
        
        if (token && userInfoStr) {
          const userInfo = JSON.parse(userInfoStr)
          this.userInfo = userInfo
          this.isLoggedIn = true
          
          // 打印调试信息
          console.log('当前登录状态:', {
            token: token,
            userInfo: this.userInfo,
            isLoggedIn: this.isLoggedIn
          })
        } else {
          this.userInfo = {}
          this.isLoggedIn = false
          console.log('未检测到登录信息')
        }
      } catch (error) {
        console.error('加载用户信息错误:', error)
        this.userInfo = {}
        this.isLoggedIn = false
      }
    },
    navigateTo(url) {
      // 检查登录状态
      if (!this.isLoggedIn) {
        uni.showToast({
          title: '请先登录',
          icon: 'none'
        })
        setTimeout(() => {
          uni.navigateTo({
            url: '/pages/my/login'
          })
        }, 1500)
        return
      }
      
      // 已登录则正常跳转
      uni.navigateTo({ url })
    },
    goToLogin() {
      uni.navigateTo({
        url: '/pages/my/login'
      })
    },
    handleLogout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            // 清除用户信息和token
            uni.removeStorageSync('userInfo')
            uni.removeStorageSync('token')
            
            // 更新状态
            this.userInfo = {}
            this.isLoggedIn = false
            
            // 提示
            uni.showToast({
              title: '已退出登录',
              icon: 'success'
            })
            
            // 如果当前用户是管理员，退出后返回首页
            if (this.userInfo.role === 'ROLE_ADMIN') {
              uni.reLaunch({
                url: '/pages/index/index'
              })
            }
          }
        }
      })
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

.user-card {
  background-color: #4aa3ff;
  padding: 40rpx;
  display: flex;
  align-items: center;
  
  .avatar {
    width: 120rpx;
    height: 120rpx;
    border-radius: 60rpx;
    overflow: hidden;
    background-color: #fff;
    margin-right: 30rpx;
    
    .avatar-img {
      width: 100%;
      height: 100%;
    }
  }
  
  .user-info {
    flex: 1;
    
    .username {
      font-size: 36rpx;
      color: #fff;
      font-weight: bold;
      margin-bottom: 10rpx;
      display: block;
    }
    
    .role {
      font-size: 24rpx;
      color: rgba(255, 255, 255, 0.8);
      background-color: rgba(255, 255, 255, 0.2);
      padding: 4rpx 16rpx;
      border-radius: 20rpx;
    }
  }
}

.menu-list {
  margin-top: 20rpx;
}

.menu-section {
  background-color: #fff;
  margin-bottom: 20rpx;
  
  .section-title {
    font-size: 28rpx;
    color: #999;
    padding: 20rpx 30rpx;
  }
  
  .menu-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 30rpx;
    border-top: 1rpx solid #f5f5f5;
    
    .item-text {
      font-size: 30rpx;
      color: #333;
      
      &.logout {
        color: #ff4d4f;
      }
    }
    
    .arrow {
      font-size: 30rpx;
      color: #999;
    }
  }
}

.login-btn-wrapper {
  padding: 40rpx;
  
  .login-btn {
    width: 100%;
    height: 88rpx;
    background-color: #4aa3ff;
    color: #fff;
    font-size: 32rpx;
    font-weight: bold;
    border-radius: 12rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    border: none;
  }
}
</style> 