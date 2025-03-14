<template>
  <view class="container">
    <view class="form">
      <view class="avatar-section">
        <image 
          class="avatar" 
          :src="form.avatar || '/static/images/logo5.png'" 
          mode="aspectFill"
          @tap="handleAvatarTap"
        />
        <text class="upload-text">点击更换头像</text>
      </view>

      <view class="form-item">
        <text class="label">用户名</text>
        <input 
          class="input" 
          type="text" 
          v-model="form.username" 
          placeholder="请输入用户名"
          disabled
        />
      </view>
      
      <view class="form-item">
        <text class="label">昵称</text>
        <input 
          class="input" 
          type="text" 
          v-model="form.nickname" 
          placeholder="请输入昵称"
        />
      </view>
      
      <view class="form-item">
        <text class="label">邮箱</text>
        <input 
          class="input" 
          type="text" 
          v-model="form.email" 
          placeholder="请输入邮箱"
        />
      </view>
      
      <view class="form-item">
        <text class="label">手机号</text>
        <input 
          class="input" 
          type="text" 
          v-model="form.phone" 
          placeholder="请输入手机号"
        />
      </view>
      
      <button class="submit-btn" @tap="handleSubmit">保存修改</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      form: {
        username: '',
        nickname: '',
        email: '',
        phone: '',
        avatar: ''
      }
    }
  },
  
  onShow() {
    this.loadUserInfo()
  },
  
  methods: {
    loadUserInfo() {
      const userInfo = uni.getStorageSync('userInfo')
      if (userInfo) {
        this.form = typeof userInfo === 'string' ? JSON.parse(userInfo) : userInfo
      }
      
      this.fetchUserInfo()
    },
    
    async fetchUserInfo() {
      try {
        const token = uni.getStorageSync('token')
        if (!token) {
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

        const [err, res] = await uni.request({
          url: 'http://localhost:8080/api/user/search/username/' + this.form.username,
          method: 'GET',
          header: {
            'Authorization': token.startsWith('Bearer ') ? token : 'Bearer ' + token
          }
        })
        
        if (!err && res.statusCode === 200) {
          const user = res.data
          this.form = {
            username: user.username || '',
            nickname: user.nickname || '',
            email: user.email || '',
            phone: user.phone || '',
            avatar: user.avatar || '/static/images/logo5.png'
          }
          
          uni.setStorageSync('userInfo', this.form)
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
        uni.showToast({
          title: '获取用户信息失败',
          icon: 'none'
        })
      }
    },
    
    async handleSubmit() {
      try {
        const token = uni.getStorageSync('token')
        const [err, res] = await uni.request({
          url: 'http://localhost:8080/api/user',
          method: 'PUT',
          header: {
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json'
          },
          data: this.form
        })
        
        if (!err && res.statusCode === 200) {
          uni.showToast({
            title: '保存成功',
            icon: 'success'
          })
          
          uni.setStorageSync('userInfo', this.form)
        } else {
          uni.showToast({
            title: '保存失败',
            icon: 'none'
          })
        }
      } catch (error) {
        console.error('保存用户信息失败:', error)
        uni.showToast({
          title: '保存失败，请重试',
          icon: 'none'
        })
      }
    },

    handleAvatarTap() {
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          const tempFilePath = res.tempFilePaths[0]
          
          uni.uploadFile({
            url: 'http://localhost:8080/api/user/avatar',
            filePath: tempFilePath,
            name: 'file',
            formData: {
              username: this.form.username
            },
            header: {
              'Authorization': 'Bearer ' + uni.getStorageSync('token')
            },
            success: (uploadRes) => {
              const result = JSON.parse(uploadRes.data)
              
              if (result.success) {
                this.form.avatar = result.data.avatar
                uni.setStorageSync('userInfo', this.form)
                
                uni.showToast({
                  title: '头像更新成功',
                  icon: 'success'
                })
              } else {
                uni.showToast({
                  title: result.message || '上传失败',
                  icon: 'none'
                })
              }
            },
            fail: () => {
              uni.showToast({
                title: '上传失败，请重试',
                icon: 'none'
              })
            }
          })
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
  padding: 20rpx;
}

.form {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  
  .avatar-section {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 40rpx;
    
    .avatar {
      width: 160rpx;
      height: 160rpx;
      border-radius: 80rpx;
      margin-bottom: 16rpx;
    }
    
    .upload-text {
      font-size: 24rpx;
      color: #666;
    }
  }
  
  .form-item {
    margin-bottom: 30rpx;
    
    .label {
      font-size: 28rpx;
      color: #333;
      margin-bottom: 16rpx;
      display: block;
    }
    
    .input {
      width: 100%;
      height: 88rpx;
      background: #f8f8f8;
      border-radius: 8rpx;
      padding: 0 30rpx;
      font-size: 28rpx;
      color: #333;
      
      &:disabled {
        background: #f0f0f0;
        color: #999;
      }
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
    margin-top: 60rpx;
    border: none;
  }
}
</style> 