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
    
    <view class="register-link" @tap="goToRegister">
      <text>还没有账号？</text>
      <text class="link">立即注册</text>
    </view>
  </view>
</template>

<script>
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
        const response = await uni.request({
          url: 'http://localhost:8080/api/user/login',
          method: 'POST',
          data: this.loginForm,
          header: {
            'Content-Type': 'application/json'
          }
        })

        if (response.statusCode === 200 && response.data.code === 200) {
          // 保存token和用户信息
          uni.setStorageSync('token', response.data.data.token)
          uni.setStorageSync('userInfo', {
            ...response.data.data.userInfo,
            role: this.loginForm.role // 确保保存角色信息
          })
          
          uni.showToast({
            title: '登录成功',
            icon: 'success'
          })
          
          // 返回上一页
          setTimeout(() => {
            uni.navigateBack()
          }, 1500)
        } else {
          uni.showToast({
            title: response.data.message || '登录失败',
            icon: 'none'
          })
        }
      } catch (error) {
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

.register-link {
  text-align: center;
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.8);

  .link {
    color: #fff;
    margin-left: 10rpx;
    font-weight: bold;
  }
}
</style> 