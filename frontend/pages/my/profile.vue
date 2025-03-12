<template>
  <view class="container">
    <view class="form-group">
      <view class="form-item">
        <text class="label">用户名</text>
        <input 
          class="input" 
          type="text" 
          v-model="userForm.username" 
          placeholder="请输入用户名"
          disabled
        />
      </view>
      
      <view class="form-item">
        <text class="label">昵称</text>
        <input 
          class="input" 
          type="text" 
          v-model="userForm.nickname" 
          placeholder="请输入昵称"
        />
      </view>
      
      <view class="form-item">
        <text class="label">邮箱</text>
        <input 
          class="input" 
          type="text" 
          v-model="userForm.email" 
          placeholder="请输入邮箱"
        />
      </view>
      
      <view class="form-item">
        <text class="label">手机号</text>
        <input 
          class="input" 
          type="number" 
          v-model="userForm.phone" 
          placeholder="请输入手机号"
        />
      </view>
    </view>

    <button class="submit-btn" @tap="handleSubmit">保存修改</button>
  </view>
</template>

<script>
import userAPI from '@/api/user.js'

export default {
  data() {
    return {
      userForm: {
        username: '',
        nickname: '',
        email: '',
        phone: ''
      }
    }
  },
  onLoad() {
    this.loadUserInfo()
  },
  methods: {
    loadUserInfo() {
      const userInfoStr = uni.getStorageSync('userInfo')
      if (userInfoStr) {
        const userInfo = JSON.parse(userInfoStr)
        this.userForm = {
          username: userInfo.username || '',
          nickname: userInfo.nickname || '',
          email: userInfo.email || '',
          phone: userInfo.phone || ''
        }
      }
    },
    
    async handleSubmit() {
      // 表单验证
      if (!this.userForm.nickname) {
        uni.showToast({
          title: '请输入昵称',
          icon: 'none'
        })
        return
      }
      
      if (this.userForm.email && !this.validateEmail(this.userForm.email)) {
        uni.showToast({
          title: '邮箱格式不正确',
          icon: 'none'
        })
        return
      }
      
      if (this.userForm.phone && !this.validatePhone(this.userForm.phone)) {
        uni.showToast({
          title: '手机号格式不正确',
          icon: 'none'
        })
        return
      }
      
      try {
        uni.showLoading({
          title: '保存中...'
        })
        
        const res = await userAPI.updateProfile(this.userForm)
        
        uni.hideLoading()
        
        if (res.code === 200) {
          // 更新本地存储的用户信息
          const userInfo = JSON.parse(uni.getStorageSync('userInfo'))
          const newUserInfo = {
            ...userInfo,
            ...this.userForm
          }
          uni.setStorageSync('userInfo', JSON.stringify(newUserInfo))
          
          uni.showToast({
            title: '保存成功',
            icon: 'success'
          })
          
          // 返回上一页
          setTimeout(() => {
            uni.navigateBack()
          }, 1500)
        } else {
          uni.showToast({
            title: res.message || '保存失败',
            icon: 'none'
          })
        }
      } catch (error) {
        uni.hideLoading()
        uni.showToast({
          title: '网络错误，请稍后重试',
          icon: 'none'
        })
        console.error('更新资料错误:', error)
      }
    },
    
    validateEmail(email) {
      const reg = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      return reg.test(email)
    },
    
    validatePhone(phone) {
      const reg = /^1[3-9]\d{9}$/
      return reg.test(phone)
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
  margin-bottom: 40rpx;
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
    
    &[disabled] {
      background-color: #f5f5f5;
      color: #999;
    }
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