<template>
  <view class="container">
    <view class="login-box">
      <view class="header">
        <image class="logo" src="/static/images/logo5.png" mode="aspectFit" />
        <text class="title">用户登录</text>
      </view>
      
      <view class="form">
        <view class="input-group">
          <input 
            class="input" 
            type="text" 
            v-model="form.username" 
            placeholder="请输入用户名"
          />
        </view>
        <view class="input-group">
          <input 
            class="input" 
            type="password" 
            v-model="form.password" 
            placeholder="请输入密码"
          />
        </view>
        
        <button class="submit-btn" @tap="handleLogin">登 录</button>
        
        <view class="actions">
          <text @tap="navigateTo('/pages/my/register')">注册账号</text>
          <text @tap="navigateTo('/pages/my/forgot-password')">忘记密码</text>
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
        password: '',
        role: 'ROLE_USER' // 默认为普通用户角色
      }
    }
  },
  methods: {
    navigateTo(url) {
      uni.navigateTo({ url })
    },
    async handleLogin() {
      if (!this.form.username || !this.form.password) {
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
          uni.setStorageSync('userInfo', {
            ...user,
            avatar: user.avatar || '/static/images/logo5.png' // 设置默认头像
          })
          
          uni.showToast({
            title: '登录成功',
            icon: 'success'
          })
          
          // 根据角色跳转
          if (user.role === 'ROLE_ADMIN') {
            uni.reLaunch({
              url: '/pages/admin/index'
            })
          } else {
            uni.reLaunch({
              url: '/pages/my/index'
            })
          }
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
      }
    }
  }
}
</script>

<style lang="scss">
.container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 40rpx;
}

.login-box {
  background: #fff;
  border-radius: 16rpx;
  padding: 40rpx;
  
  .header {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 60rpx;
    
    .logo {
      width: 160rpx;
      height: 160rpx;
      margin-bottom: 20rpx;
    }
    
    .title {
      font-size: 36rpx;
      font-weight: bold;
      color: #333;
    }
  }
  
  .form {
    .input-group {
      margin-bottom: 30rpx;
      
      .input {
        width: 100%;
        height: 88rpx;
        background: #f8f8f8;
        border-radius: 8rpx;
        padding: 0 30rpx;
        font-size: 28rpx;
        color: #333;
      }
    }
    
    .submit-btn {
      width: 100%;
      height: 88rpx;
      background: #4aa3ff;
      color: #fff;
      font-size: 32rpx;
      border-radius: 8rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 60rpx 0 40rpx;
      border: none;
    }
    
    .actions {
      display: flex;
      justify-content: space-between;
      font-size: 28rpx;
      color: #666;
      
      text {
        color: #4aa3ff;
      }
    }
  }
}
</style> 