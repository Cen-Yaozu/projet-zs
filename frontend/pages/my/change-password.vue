<template>
  <view class="container">
    <view class="form-group">
      <view class="form-item">
        <text class="label">当前密码</text>
        <input 
          class="input" 
          type="password" 
          v-model="passwordForm.oldPassword" 
          placeholder="请输入当前密码"
        />
      </view>
      
      <view class="form-item">
        <text class="label">新密码</text>
        <input 
          class="input" 
          type="password" 
          v-model="passwordForm.newPassword" 
          placeholder="请输入新密码"
        />
      </view>
      
      <view class="form-item">
        <text class="label">确认新密码</text>
        <input 
          class="input" 
          type="password" 
          v-model="passwordForm.confirmPassword" 
          placeholder="请再次输入新密码"
        />
      </view>
    </view>

    <view class="tips">
      <text class="tip-text">密码要求：</text>
      <text class="tip-item">• 长度至少8位</text>
      <text class="tip-item">• 包含字母和数字</text>
    </view>

    <button class="submit-btn" @tap="handleSubmit">确认修改</button>
  </view>
</template>

<script>
import userAPI from '@/api/user.js'

export default {
  data() {
    return {
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
    }
  },
  methods: {
    validatePassword(password) {
      // 密码至少8位，包含字母和数字
      const reg = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/
      return reg.test(password)
    },
    
    async handleSubmit() {
      // 表单验证
      if (!this.passwordForm.oldPassword) {
        uni.showToast({
          title: '请输入当前密码',
          icon: 'none'
        })
        return
      }
      
      if (!this.validatePassword(this.passwordForm.newPassword)) {
        uni.showToast({
          title: '新密码不符合要求',
          icon: 'none'
        })
        return
      }
      
      if (this.passwordForm.newPassword !== this.passwordForm.confirmPassword) {
        uni.showToast({
          title: '两次输入的密码不一致',
          icon: 'none'
        })
        return
      }
      
      try {
        uni.showLoading({
          title: '修改中...'
        })
        
        const res = await userAPI.changePassword({
          oldPassword: this.passwordForm.oldPassword,
          newPassword: this.passwordForm.newPassword
        })
        
        uni.hideLoading()
        
        if (res.code === 200) {
          uni.showToast({
            title: '密码修改成功',
            icon: 'success'
          })
          
          // 清除登录信息，返回登录页
          setTimeout(() => {
            uni.removeStorageSync('userInfo')
            uni.removeStorageSync('token')
            uni.reLaunch({
              url: '/pages/my/login'
            })
          }, 1500)
        } else {
          uni.showToast({
            title: res.message || '修改失败',
            icon: 'none'
          })
        }
      } catch (error) {
        uni.hideLoading()
        uni.showToast({
          title: '网络错误，请稍后重试',
          icon: 'none'
        })
        console.error('修改密码错误:', error)
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

.form-group {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 30rpx;
}

.form-item {
  margin-bottom: 30rpx;
  
  &:last-child {
    margin-bottom: 0;
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
}

.tips {
  padding: 20rpx;
  margin-bottom: 40rpx;
  
  .tip-text {
    font-size: 28rpx;
    color: #666;
    margin-bottom: 10rpx;
    display: block;
  }
  
  .tip-item {
    font-size: 26rpx;
    color: #999;
    margin-bottom: 6rpx;
    display: block;
  }
}

.submit-btn {
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
  
  &:active {
    opacity: 0.9;
  }
}
</style> 