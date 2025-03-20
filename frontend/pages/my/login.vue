<template>
  <view class="login-container">
    <view class="login-background">
      <view class="login-shape login-shape--1"></view>
      <view class="login-shape login-shape--2"></view>
      <view class="login-shape login-shape--3"></view>
    </view>
    
    <view class="login-box">
      <view class="login-header">
        <image class="login-logo" src="/static/images/logo5.png" mode="aspectFit" />
        <text class="login-title">用户登录</text>
        <text class="login-subtitle">欢迎回来，请登录您的账号</text>
      </view>
      
      <view class="login-form">
        <view class="login-input-group">
          <view class="login-input-icon">
            <text class="iconfont icon-user"></text>
          </view>
          <input 
            class="login-input" 
            type="text" 
            v-model="form.username" 
            placeholder="请输入用户名"
            maxlength="30"
            :focus="true"
          />
        </view>
        
        <view class="login-input-group">
          <view class="login-input-icon">
            <text class="iconfont icon-lock"></text>
          </view>
          <input 
            class="login-input" 
            :type="showPassword ? 'text' : 'password'" 
            v-model="form.password" 
            placeholder="请输入密码"
            maxlength="30"
          />
          <view class="login-input-action" @tap="togglePasswordVisibility">
            <text class="iconfont" :class="showPassword ? 'icon-eye' : 'icon-eye-off'"></text>
          </view>
        </view>
        
        <view class="login-remember">
          <view class="login-remember-checkbox" @tap="toggleRemember">
            <view :class="{'login-remember-checkbox--active': remember}"></view>
          </view>
          <text>记住我</text>
        </view>
        
        <view class="login-button-group">
          <button class="login-button login-button--primary" @tap="handleLogin">
            <text>登 录</text>
          </button>
          
          <button class="login-button login-button--default" @tap="navigateTo('/pages/my/register')">
            <text>注 册</text>
          </button>
        </view>
        
        <view class="login-actions">
          <text @tap="navigateTo('/pages/my/forgot-password')">忘记密码?</text>
          <text @tap="navigateTo('/pages/index/index')">返回首页</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      form: {
        username: '',
        password: ''
      },
      showPassword: false,
      remember: false,
      isLoading: false
    }
  },
  onLoad() {
    // 检查是否有保存的登录信息
    const savedUsername = uni.getStorageSync('savedUsername');
    if (savedUsername) {
      this.form.username = savedUsername;
      this.remember = true;
    }
  },
  methods: {
    navigateTo(url) {
      uni.navigateTo({ url })
    },
    togglePasswordVisibility() {
      this.showPassword = !this.showPassword;
    },
    toggleRemember() {
      this.remember = !this.remember;
    },
    async handleLogin() {
      if (!this.form.username || !this.form.password) {
        uni.showToast({
          title: '请输入用户名和密码',
          icon: 'none'
        })
        return
      }
      
      if (this.isLoading) return;
      this.isLoading = true;
      
      try {
        const response = await uni.request({
          url: 'http://localhost:8080/api/users/login',
          method: 'POST',
          header: {
            'Content-Type': 'application/json'
          },
          data: this.form,
          timeout: 10000
        })
        
        console.log('登录响应:', response)
        
        const [err, res] = response
        if (!err && res.statusCode === 200 && res.data.success) {
          const { token, user } = res.data.data
          
          // 保存token和用户信息
          uni.setStorageSync('token', token)
          uni.setStorageSync('userInfo', user)
          
          // 如果勾选了记住我，则保存用户名
          if (this.remember) {
            uni.setStorageSync('savedUsername', this.form.username)
          } else {
            uni.removeStorageSync('savedUsername')
          }
          
          uni.showToast({
            title: '登录成功',
            icon: 'success'
          })
          
          // 统一跳转到个人中心页面
          setTimeout(() => {
            uni.reLaunch({
              url: '/pages/my/index'
            })
          }, 1000)
        } else {
          uni.showToast({
            title: (res && res.data && res.data.message) || '登录失败',
            icon: 'none'
          })
        }
      } catch (error) {
        console.error('登录错误:', error)
        uni.showToast({
          title: '登录失败，请稍后重试',
          icon: 'none'
        })
      } finally {
        this.isLoading = false;
      }
    }
  }
}
</script>

<style lang="scss">
@import '../../styles/common.scss';

.login-container {
  position: relative;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: $white;
  padding: 40rpx;
  overflow: hidden;
}

.login-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 0;
  overflow: hidden;
}

.login-shape {
  position: absolute;
  border-radius: 50%;
  
  &--1 {
    width: 500rpx;
    height: 500rpx;
    background: linear-gradient(135deg, rgba($primary-color, 0.1), rgba($primary-color, 0.05));
    top: -200rpx;
    right: -100rpx;
  }
  
  &--2 {
    width: 600rpx;
    height: 600rpx;
    background: linear-gradient(135deg, rgba($primary-darker-color, 0.12), rgba($primary-color, 0.06));
    bottom: -300rpx;
    left: -200rpx;
  }
  
  &--3 {
    width: 300rpx;
    height: 300rpx;
    background: linear-gradient(135deg, rgba($primary-color, 0.07), rgba($primary-darker-color, 0.03));
    top: 30%;
    left: -150rpx;
  }
}

.login-box {
  position: relative;
  z-index: 1;
  width: 90%;
  max-width: 600rpx;
  background-color: $white;
  border-radius: $radius-large;
  box-shadow: $shadow-large;
  padding: 50rpx 40rpx;
  animation: fadeInUp 0.5s ease-out;
}

.login-header {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 60rpx;
    
  .login-logo {
    width: 140rpx;
    height: 140rpx;
      margin-bottom: 20rpx;
    animation: pulse 2s infinite;
  }
  
  .login-title {
    font-size: $font-size-xlarge;
    font-weight: bold;
    color: $gray-900;
    margin-bottom: 16rpx;
  }
  
  .login-subtitle {
    font-size: $font-size-small;
    color: $gray-600;
    text-align: center;
  }
}

.login-form {
  .login-input-group {
    position: relative;
    margin-bottom: 30rpx;
    display: flex;
    align-items: center;
    background-color: $gray-100;
    border-radius: $radius-large;
    border: 1px solid $gray-200;
    transition: all 0.3s;
    
    &:focus-within {
      border-color: $primary-color;
      background-color: $white;
      box-shadow: $shadow-primary;
    }
    
    .login-input-icon {
      width: 80rpx;
      display: flex;
      justify-content: center;
      align-items: center;
      color: $gray-600;
      font-size: $font-size-medium;
    }
    
    .login-input {
      flex: 1;
      height: 90rpx;
      font-size: $font-size-base;
      color: $gray-900;
      background-color: transparent;
      padding: 0 10rpx;
    }
    
    .login-input-action {
      width: 80rpx;
      display: flex;
      justify-content: center;
      align-items: center;
      color: $gray-500;
    }
  }
  
  .login-remember {
    display: flex;
    align-items: center;
      margin-bottom: 30rpx;
    font-size: $font-size-small;
    color: $gray-600;
    
    .login-remember-checkbox {
      position: relative;
      width: 36rpx;
      height: 36rpx;
      border-radius: $radius-small;
      border: 1px solid $gray-400;
      margin-right: 12rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: $transition-base;
      
      &--active {
        background-color: $primary-color;
        border-color: $primary-color;
        
        &:after {
          content: "";
          position: absolute;
          width: 16rpx;
          height: 8rpx;
          border-left: 2px solid $white;
          border-bottom: 2px solid $white;
          transform: rotate(-45deg);
        }
      }
    }
  }
  
  .login-button-group {
    margin-bottom: 30rpx;
    
    .login-button {
      position: relative;
      width: 100%;
      height: 90rpx;
      border-radius: $radius-large;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: $font-size-medium;
      font-weight: 500;
      border: none;
      transition: $transition-base;
      margin-bottom: 20rpx;
      overflow: hidden;
      
      &--primary {
        background: linear-gradient(135deg, $primary-color, $primary-darker-color);
        color: $white;
        box-shadow: $shadow-primary;
        
        &:active {
          transform: translateY(2rpx);
          box-shadow: 0 2rpx 8rpx rgba($primary-color, 0.2);
        }
      }
      
      &--default {
        background-color: $white;
        color: $primary-color;
        border: 1px solid $primary-color;
        
        &:active {
          background-color: $primary-light-color;
          transform: translateY(2rpx);
        }
      }
    }
  }
  
  .login-actions {
      display: flex;
      justify-content: space-between;
    font-size: $font-size-small;
      
      text {
      color: $primary-color;
      padding: 10rpx;
      
      &:active {
        opacity: 0.8;
      }
    }
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
  }
}
</style> 