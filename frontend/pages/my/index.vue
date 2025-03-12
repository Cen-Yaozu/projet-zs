<template>
  <view class="container">
    <!-- 用户信息卡片 -->
    <view class="user-card">
      <view class="user-info">
        <view class="avatar-wrapper">
          <text class="avatar-text">{{ userInfo ? userInfo.username.charAt(0).toUpperCase() : '👤' }}</text>
        </view>
        <view class="user-detail">
          <text class="nickname">{{ userInfo ? userInfo.username : '未登录' }}</text>
          <text class="login-tip" @tap="handleLogin">{{ userInfo ? '' : '点击登录/注册' }}</text>
        </view>
      </view>
    </view>

    <!-- 菜单列表 -->
    <view class="menu-card">
      <view class="menu-group">
        <view class="menu-item" v-for="(item, index) in menuList1" :key="index" @tap="navigateTo(item.path)">
          <text class="menu-icon">{{ item.icon }}</text>
          <text class="menu-text">{{ item.text }}</text>
          <text class="arrow">›</text>
        </view>
      </view>
    </view>

    <!-- 退出登录按钮 -->
    <view class="logout-btn" v-if="userInfo" @tap="handleLogout">
      退出登录
    </view>

    <!-- 版本信息 -->
    <view class="version-info">
      <text class="version-text">当前版本 5.1.15.2</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userInfo: null,
      menuList1: [
        { text: '意见反馈', icon: '💭', path: '/pages/my/feedback' },
        { text: '关于我们', icon: '🏢', path: '/pages/my/about' }
      ]
    }
  },
  onLoad() {
    // 页面加载时检查登录状态
    this.checkLoginStatus()
  },
  onShow() {
    // 每次显示页面时检查登录状态
    this.checkLoginStatus()
  },
  methods: {
    checkLoginStatus() {
      try {
        const userInfo = uni.getStorageSync('userInfo')
        const token = uni.getStorageSync('token')
        if (userInfo && token) {
          this.userInfo = userInfo
        } else {
          this.userInfo = null
        }
      } catch (e) {
        console.error('获取登录状态失败:', e)
        this.userInfo = null
      }
    },
    
    handleLogin() {
      if (!this.userInfo) {
        uni.navigateTo({
          url: '/pages/my/login'
        })
      }
    },
    
    handleLogout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            // 清除本地存储的用户信息和token
            uni.removeStorageSync('userInfo')
            uni.removeStorageSync('token')
            
            // 清除Vuex中的用户状态
            this.$store.commit('logout')
            
            // 更新页面状态
            this.userInfo = null
            
            uni.showToast({
              title: '已退出登录',
              icon: 'success'
            })
          }
        }
      })
    },
    
    navigateTo(path) {
      // 如果需要登录权限的页面，先检查登录状态
      if (path.includes('feedback') && !this.userInfo) {
        uni.showModal({
          title: '提示',
          content: '请先登录后再操作',
          confirmText: '去登录',
          success: (res) => {
            if (res.confirm) {
              uni.navigateTo({
                url: '/pages/my/login'
              })
            }
          }
        })
        return
      }
      
      uni.navigateTo({
        url: path
      })
    }
  }
}
</script>

<style lang="scss">
.container {
  min-height: 100vh;
  background-color: #f7f7f7;
  padding-bottom: 40rpx;
}

.user-card {
  background-color: #4aa3ff;
  padding: 40rpx 30rpx;
  margin-bottom: 20rpx;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar-wrapper {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background-color: #fff;
  margin-right: 30rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-text {
  font-size: 60rpx;
}

.user-detail {
  flex: 1;
  color: #fff;
}

.nickname {
  font-size: 36rpx;
  font-weight: 500;
  margin-bottom: 10rpx;
  display: block;
}

.login-tip {
  font-size: 28rpx;
  opacity: 0.9;
}

.menu-card {
  background-color: #fff;
}

.menu-group {
  margin-bottom: 20rpx;

  &:last-child {
    margin-bottom: 0;
  }
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  background-color: #fff;
  border-bottom: 1rpx solid #eee;

  &:last-child {
    border-bottom: none;
  }
}

.menu-icon {
  font-size: 40rpx;
  margin-right: 20rpx;
}

.menu-text {
  flex: 1;
  font-size: 30rpx;
  color: #333;
}

.arrow {
  font-size: 36rpx;
  color: #ccc;
}

.logout-btn {
  margin: 40rpx 30rpx;
  height: 88rpx;
  background-color: #ff4d4f;
  color: #fff;
  font-size: 32rpx;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.version-info {
  text-align: center;
  padding: 40rpx 0;
}

.version-text {
  font-size: 24rpx;
  color: #999;
}
</style> 