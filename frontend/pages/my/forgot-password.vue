<template>
  <view class="container">
    <view class="title">忘记密码</view>
    
    <view class="form-group">
      <view class="form-item">
        <text class="label">用户名</text>
        <input 
          class="input" 
          type="text" 
          v-model="resetForm.username" 
          placeholder="请输入用户名"
        />
      </view>
      
      <view class="form-item">
        <text class="label">旧密码</text>
        <input 
          class="input" 
          type="password" 
          v-model="resetForm.oldPassword" 
          placeholder="请输入旧密码"
        />
      </view>
      
      <view class="form-item">
        <text class="label">新密码</text>
        <input 
          class="input" 
          type="password" 
          v-model="resetForm.newPassword" 
          placeholder="请输入新密码"
        />
      </view>
      
      <view class="form-item">
        <text class="label">确认新密码</text>
        <input 
          class="input" 
          type="password" 
          v-model="confirmPassword" 
          placeholder="请再次输入新密码"
        />
      </view>
    </view>

    <button class="reset-btn" @tap="handleResetPassword">重置密码</button>
    
    <view class="back-link" @tap="goBack">
      <text>返回登录</text>
    </view>
  </view>
</template>

<script>
import userAPI from '@/api/user.js';

export default {
  data() {
    return {
      resetForm: {
        username: '',
        oldPassword: '',
        newPassword: ''
      },
      confirmPassword: ''
    }
  },
  methods: {
    validateForm() {
      if (!this.resetForm.username) {
        uni.showToast({
          title: '请输入用户名',
          icon: 'none'
        })
        return false
      }
      
      if (!this.resetForm.oldPassword) {
        uni.showToast({
          title: '请输入旧密码',
          icon: 'none'
        })
        return false
      }
      
      if (!this.resetForm.newPassword) {
        uni.showToast({
          title: '请输入新密码',
          icon: 'none'
        })
        return false
      }
      
      if (this.resetForm.newPassword.length < 6) {
        uni.showToast({
          title: '新密码不能少于6位',
          icon: 'none'
        })
        return false
      }
      
      if (this.resetForm.newPassword !== this.confirmPassword) {
        uni.showToast({
          title: '两次输入的新密码不一致',
          icon: 'none'
        })
        return false
      }
      
      return true
    },
    
    async handleResetPassword() {
      if (!this.validateForm()) return
      
      try {
        // 显示加载中
        uni.showLoading({
          title: '正在重置密码...'
        })
        
        console.log('发送密码重置请求:', {
          username: this.resetForm.username,
          oldPassword: this.resetForm.oldPassword,
          newPassword: this.resetForm.newPassword
        })
        
        const res = await userAPI.updatePassword({
          username: this.resetForm.username,
          oldPassword: this.resetForm.oldPassword,
          newPassword: this.resetForm.newPassword
        })
        
        // 隐藏加载
        uni.hideLoading()
        
        console.log('密码重置响应:', res)
        
        if (res.code === 200) {
          uni.showToast({
            title: '密码重置成功',
            icon: 'success'
          })
          
          // 延迟返回登录页
          setTimeout(() => {
            uni.navigateBack()
          }, 1500)
        } else {
          uni.showToast({
            title: res.message || '密码重置失败',
            icon: 'none'
          })
        }
      } catch (error) {
        uni.hideLoading()
        console.error('密码重置错误详情:', error)
        uni.showToast({
          title: '网络错误，请稍后重试',
          icon: 'none'
        })
      }
    },
    
    goBack() {
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

.reset-btn {
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

.back-link {
  text-align: center;
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.8);
}
</style> 