<template>
  <view class="container">
    <view class="title">用户注册</view>
    
    <view class="form-group">
      <view class="form-item">
        <text class="label">用户名</text>
        <input 
          class="input" 
          type="text" 
          v-model="registerForm.username" 
          placeholder="请输入用户名"
        />
      </view>
      
      <view class="form-item">
        <text class="label">密码</text>
        <input 
          class="input" 
          type="password" 
          v-model="registerForm.password" 
          placeholder="请输入密码"
        />
      </view>

      <view class="form-item">
        <text class="label">确认密码</text>
        <input 
          class="input" 
          type="password" 
          v-model="confirmPassword" 
          placeholder="请再次输入密码"
        />
      </view>
      
      <view class="form-item">
        <text class="label">手机号</text>
        <input 
          class="input" 
          type="number" 
          v-model="registerForm.phone" 
          placeholder="请输入手机号"
          maxlength="11"
        />
      </view>

      <view class="form-item">
        <text class="label">邮箱</text>
        <input 
          class="input" 
          type="text" 
          v-model="registerForm.email" 
          placeholder="请输入邮箱"
        />
      </view>
    </view>

    <button class="register-btn" @tap="handleRegister">注册</button>
    
    <view class="login-link" @tap="goToLogin">
      <text>已有账号？</text>
      <text class="link">立即登录</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      registerForm: {
        username: '',
        password: '',
        phone: '',
        email: '',
        role: 'ROLE_USER', // 修改默认角色
        status: 1 // 默认状态：正常
      },
      confirmPassword: ''
    }
  },
  methods: {
    validateForm() {
      if (!this.registerForm.username) {
        uni.showToast({
          title: '请输入用户名',
          icon: 'none'
        })
        return false
      }
      if (!this.registerForm.password) {
        uni.showToast({
          title: '请输入密码',
          icon: 'none'
        })
        return false
      }
      if (this.registerForm.password !== this.confirmPassword) {
        uni.showToast({
          title: '两次输入的密码不一致',
          icon: 'none'
        })
        return false
      }
      if (this.registerForm.phone && !/^1[3-9]\d{9}$/.test(this.registerForm.phone)) {
        uni.showToast({
          title: '手机号格式不正确',
          icon: 'none'
        })
        return false
      }
      if (this.registerForm.email && !/^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/.test(this.registerForm.email)) {
        uni.showToast({
          title: '邮箱格式不正确',
          icon: 'none'
        })
        return false
      }
      return true
    },
    
    async handleRegister() {
      if (!this.validateForm()) return

      try {
        const response = await uni.request({
          url: 'http://localhost:8080/api/user/register',
          method: 'POST',
          data: this.registerForm,
          header: {
            'Content-Type': 'application/json'
          }
        })

        if (response.statusCode === 200) {
          uni.showToast({
            title: '注册成功',
            icon: 'success'
          })
          
          // 延迟跳转到登录页
          setTimeout(() => {
            uni.navigateBack()
          }, 1500)
        } else {
          uni.showToast({
            title: response.data.message || '注册失败',
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
    
    goToLogin() {
      uni.navigateBack()
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

.register-btn {
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

.login-link {
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