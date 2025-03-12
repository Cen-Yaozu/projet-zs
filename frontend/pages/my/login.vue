<template>
  <view class="container">
    <view class="title">用户登录</view>
    
    <view class="form-group">
      <view class="form-item">
        <text class="label">用户名</text>
        <input 
          class="input" 
          type="text" 
          v-model="loginForm.username" 
          placeholder="请输入用户名"
        />
      </view>
      
      <view class="form-item">
        <text class="label">密码</text>
        <input 
          class="input" 
          type="password" 
          v-model="loginForm.password" 
          placeholder="请输入密码"
        />
      </view>

      <view class="form-item">
        <text class="label">角色</text>
        <view class="role-selector">
          <view 
            class="role-item" 
            :class="{ active: loginForm.role === 'ROLE_USER' }"
            @tap="loginForm.role = 'ROLE_USER'"
          >
            <text class="role-text">普通用户</text>
          </view>
          <view 
            class="role-item" 
            :class="{ active: loginForm.role === 'ROLE_ADMIN' }"
            @tap="loginForm.role = 'ROLE_ADMIN'"
          >
            <text class="role-text">管理员</text>
          </view>
        </view>
      </view>
    </view>

    <button class="login-btn" @tap="handleLogin">登录</button>
    
    <view class="action-links">
      <view class="register-link" @tap="goToRegister">
        <text>还没有账号？</text>
        <text class="link">立即注册</text>
      </view>
      
      <view class="forgot-password-link" @tap="goToForgotPassword">
        <text class="link">忘记密码</text>
      </view>
    </view>
  </view>
</template>

<script>
import userAPI from '@/api/user.js';

export default {
  data() {
    return {
      loginForm: {
        username: '',
        password: '',
        role: 'ROLE_USER' // 默认为普通用户角色
      }
    }
  },
  methods: {
    async handleLogin() {
      if (!this.loginForm.username || !this.loginForm.password) {
        uni.showToast({
          title: '请输入用户名和密码',
          icon: 'none'
        })
        return
      }

      try {
        // 显示加载中
        uni.showLoading({
          title: '登录中...'
        })
        
        const res = await userAPI.login(this.loginForm)
        
        // 隐藏加载
        uni.hideLoading()
        
        // 打印调试信息
        console.log('登录响应:', res)
        
        if (res.code === 200) {
          // 保存token和用户信息
          uni.setStorageSync('token', res.data.token)
          uni.setStorageSync('userInfo', JSON.stringify({
            ...res.data.user,
            role: this.loginForm.role // 确保保存角色信息
          }))
          
          // 打印保存后的信息
          console.log('保存的登录信息:', {
            token: uni.getStorageSync('token'),
            userInfo: uni.getStorageSync('userInfo')
          })
          
          uni.showToast({
            title: '登录成功',
            icon: 'success'
          })
          
          // 根据角色跳转到不同页面
          setTimeout(() => {
            if (this.loginForm.role === 'ROLE_ADMIN') {
              uni.reLaunch({
                url: '/pages/admin/index'
              })
            } else {
              uni.reLaunch({
                url: '/pages/my/index'
              })
            }
          }, 1500)
        } else {
          uni.showToast({
            title: res.message || '登录失败',
            icon: 'none'
          })
        }
      } catch (error) {
        uni.hideLoading()
        console.error('登录错误:', error)
        uni.showToast({
          title: '网络错误，请稍后重试',
          icon: 'none'
        })
      }
    },
    
    goToRegister() {
      uni.navigateTo({
        url: '/pages/my/register'
      })
    },
    
    goToForgotPassword() {
      uni.navigateTo({
        url: '/pages/my/forgot-password'
      })
    }
  }
}
</script>

<style lang="scss">
.container {
  min-height: 100vh;
  background-color: #4aa3ff;
  padding: 60rpx 40rpx;
}

.title {
  font-size: 48rpx;
  font-weight: bold;
  color: #fff;
  text-align: center;
  margin-bottom: 80rpx;
  margin-top: 60rpx;
}

.form-group {
  background: #fff;
  border-radius: 20rpx;
  padding: 40rpx 30rpx;
  margin-bottom: 40rpx;
}

.form-item {
  margin-bottom: 30rpx;

  &:last-child {
    margin-bottom: 0;
  }
}

.label {
  font-size: 28rpx;
  color: #666;
  margin-bottom: 16rpx;
  display: block;
}

.input {
  width: 100%;
  height: 88rpx;
  background-color: #f8f8f8;
  border-radius: 12rpx;
  padding: 0 30rpx;
  font-size: 30rpx;
  color: #333;
  box-sizing: border-box;
}

.role-selector {
  display: flex;
  gap: 20rpx;
}

.role-item {
  flex: 1;
  height: 88rpx;
  background-color: #f8f8f8;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;

  &.active {
    background-color: #4aa3ff;
    .role-text {
      color: #fff;
    }
  }
}

.role-text {
  font-size: 28rpx;
  color: #666;
}

.login-btn {
  width: 100%;
  height: 88rpx;
  background-color: #fff;
  color: #4aa3ff;
  font-size: 32rpx;
  font-weight: bold;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 30rpx;
  border: none;
}

.action-links {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.register-link, .forgot-password-link {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.8);
}

.link {
  color: #fff;
  margin-left: 10rpx;
  font-weight: bold;
}
</style> 