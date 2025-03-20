<template>
  <view class="container">
    <view class="header">
      <view class="back-button" @tap="navBack">
        <text class="iconfont icon-left"></text>
      </view>
      <text class="title">系统设置</text>
    </view>
    
    <view class="content">
      <view class="setting-groups">
        <view class="setting-card">
          <view class="card-header">
            <text class="card-title">
              <text class="title-icon iconfont icon-basic"></text>
              基础设置
            </text>
          </view>
          <view class="setting-list">
            <view class="setting-item">
              <view class="setting-label">系统名称</view>
              <view class="setting-editor">
                <input 
                  type="text" 
                  v-model="systemSettings.systemName" 
                  placeholder="请输入系统名称"
                  class="input"
                />
              </view>
            </view>
            <view class="setting-item">
              <view class="setting-label">系统Logo</view>
              <view class="setting-editor">
                <view class="logo-preview" v-if="systemSettings.logoUrl">
                  <image :src="systemSettings.logoUrl" mode="aspectFit" class="logo-image"></image>
                  <text class="logo-remove iconfont icon-delete" @tap="removeLogo"></text>
                </view>
                <button class="upload-button" @tap="uploadLogo" v-else>
                  <text class="upload-icon iconfont icon-upload"></text>
                  <text>上传Logo</text>
                </button>
              </view>
            </view>
            <view class="setting-item">
              <view class="setting-label">系统描述</view>
              <view class="setting-editor">
                <textarea 
                  v-model="systemSettings.systemDesc" 
                  placeholder="请输入系统描述"
                  class="textarea"
                />
              </view>
            </view>
          </view>
          <view class="card-actions">
            <button class="save-button" @tap="saveSetting('basic')">
              <text class="button-icon iconfont icon-save"></text>
              保存更改
            </button>
          </view>
        </view>
        
        <view class="setting-card">
          <view class="card-header">
            <text class="card-title">
              <text class="title-icon iconfont icon-notice"></text>
              通知设置
            </text>
          </view>
          <view class="setting-list">
            <view class="setting-item">
              <view class="setting-label">邮件通知</view>
              <view class="setting-editor">
                <switch 
                  :checked="systemSettings.emailNotificationEnabled" 
                  @change="handleSwitchChange('emailNotificationEnabled', $event)" 
                  color="#4aa3ff"
                  class="switch"
                />
              </view>
            </view>
            <view class="setting-item" v-if="systemSettings.emailNotificationEnabled">
              <view class="setting-label">SMTP服务器</view>
              <view class="setting-editor">
                <input 
                  type="text" 
                  v-model="systemSettings.smtpServer" 
                  placeholder="请输入SMTP服务器地址"
                  class="input"
                />
              </view>
            </view>
            <view class="setting-item" v-if="systemSettings.emailNotificationEnabled">
              <view class="setting-label">SMTP端口</view>
              <view class="setting-editor">
                <input 
                  type="number" 
                  v-model="systemSettings.smtpPort" 
                  placeholder="请输入SMTP端口"
                  class="input"
                />
              </view>
            </view>
            <view class="setting-item" v-if="systemSettings.emailNotificationEnabled">
              <view class="setting-label">邮箱账号</view>
              <view class="setting-editor">
                <input 
                  type="text" 
                  v-model="systemSettings.emailAccount" 
                  placeholder="请输入邮箱账号"
                  class="input"
                />
              </view>
            </view>
            <view class="setting-item" v-if="systemSettings.emailNotificationEnabled">
              <view class="setting-label">邮箱密码</view>
              <view class="setting-editor">
                <input 
                  type="password" 
                  v-model="systemSettings.emailPassword" 
                  placeholder="请输入邮箱密码"
                  class="input"
                />
              </view>
            </view>
          </view>
          <view class="card-actions">
            <button class="test-button" @tap="testEmail" v-if="systemSettings.emailNotificationEnabled">
              <text class="button-icon iconfont icon-test"></text>
              测试发送
            </button>
            <button class="save-button" @tap="saveSetting('email')">
              <text class="button-icon iconfont icon-save"></text>
              保存更改
            </button>
          </view>
        </view>
        
        <view class="setting-card">
          <view class="card-header">
            <text class="card-title">
              <text class="title-icon iconfont icon-security"></text>
              安全设置
            </text>
          </view>
          <view class="setting-list">
            <view class="setting-item">
              <view class="setting-label">登录限制</view>
              <view class="setting-editor">
                <switch 
                  :checked="systemSettings.loginLimitEnabled" 
                  @change="handleSwitchChange('loginLimitEnabled', $event)" 
                  color="#4aa3ff"
                  class="switch"
                />
              </view>
            </view>
            <view class="setting-item" v-if="systemSettings.loginLimitEnabled">
              <view class="setting-label">最大尝试次数</view>
              <view class="setting-editor">
                <input 
                  type="number" 
                  v-model="systemSettings.maxLoginAttempts" 
                  placeholder="请输入最大尝试次数"
                  class="input"
                />
              </view>
            </view>
            <view class="setting-item" v-if="systemSettings.loginLimitEnabled">
              <view class="setting-label">锁定时间(分钟)</view>
              <view class="setting-editor">
                <input 
                  type="number" 
                  v-model="systemSettings.lockDurationMinutes" 
                  placeholder="请输入锁定时间"
                  class="input"
                />
              </view>
            </view>
            <view class="setting-item">
              <view class="setting-label">密码策略</view>
              <view class="setting-editor">
                <switch 
                  :checked="systemSettings.passwordPolicyEnabled" 
                  @change="handleSwitchChange('passwordPolicyEnabled', $event)" 
                  color="#4aa3ff"
                  class="switch"
                />
              </view>
            </view>
            <view class="setting-item" v-if="systemSettings.passwordPolicyEnabled">
              <view class="setting-label">最小密码长度</view>
              <view class="setting-editor">
                <input 
                  type="number" 
                  v-model="systemSettings.minPasswordLength" 
                  placeholder="请输入最小密码长度"
                  class="input"
                />
              </view>
            </view>
            <view class="setting-item" v-if="systemSettings.passwordPolicyEnabled">
              <view class="setting-label">密码复杂度要求</view>
              <view class="setting-editor">
                <picker 
                  :range="passwordComplexityOptions" 
                  :value="passwordComplexityIndex"
                  @change="handleComplexityChange"
                >
                  <view class="picker-value">
                    {{passwordComplexityOptions[passwordComplexityIndex]}}
                    <text class="iconfont icon-down"></text>
                  </view>
                </picker>
              </view>
            </view>
          </view>
          <view class="card-actions">
            <button class="save-button" @tap="saveSetting('security')">
              <text class="button-icon iconfont icon-save"></text>
              保存更改
            </button>
          </view>
        </view>
      </view>
    </view>
    
    <!-- 加载中遮罩 -->
    <view class="loading-overlay" v-if="loading">
      <view class="loading-content">
        <text class="loading-text">加载中...</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      loading: false,
      systemSettings: {
        // 基础设置
        systemName: '招生管理系统',
        logoUrl: '',
        systemDesc: '',
        
        // 邮件设置
        emailNotificationEnabled: false,
        smtpServer: '',
        smtpPort: 465,
        emailAccount: '',
        emailPassword: '',
        
        // 安全设置
        loginLimitEnabled: false,
        maxLoginAttempts: 5,
        lockDurationMinutes: 30,
        passwordPolicyEnabled: false,
        minPasswordLength: 8,
        passwordComplexity: 'MEDIUM'
      },
      passwordComplexityOptions: ['低', '中', '高'],
      passwordComplexityIndex: 1,
      passwordComplexityValues: ['LOW', 'MEDIUM', 'HIGH']
    }
  },
  onShow() {
    this.loadSettings()
  },
  methods: {
    navBack() {
      uni.navigateBack();
    },
    
    async loadSettings() {
      this.loading = true;
      try {
        const [err, res] = await uni.request({
          url: 'http://localhost:8080/api/system/settings',
          method: 'GET',
          header: {
            'Authorization': uni.getStorageSync('token')
          }
        })
        
        if (!err && res.statusCode === 200) {
          this.systemSettings = res.data.data || this.systemSettings
          
          // 设置密码复杂度下拉索引
          if (this.systemSettings.passwordComplexity) {
            const complexityIndex = this.passwordComplexityValues.indexOf(this.systemSettings.passwordComplexity)
            if (complexityIndex !== -1) {
              this.passwordComplexityIndex = complexityIndex
            }
          }
        }
      } catch (error) {
        console.error('获取系统设置失败:', error)
        uni.showToast({
          title: '获取系统设置失败',
          icon: 'none'
        })
      } finally {
        this.loading = false;
      }
    },
    
    handleSwitchChange(key, event) {
      this.systemSettings[key] = event.detail.value
    },
    
    handleComplexityChange(e) {
      this.passwordComplexityIndex = e.detail.value
      this.systemSettings.passwordComplexity = this.passwordComplexityValues[this.passwordComplexityIndex]
    },
    
    uploadLogo() {
      uni.chooseImage({
        count: 1,
        success: (res) => {
          const tempFilePath = res.tempFilePaths[0]
          this.loading = true
          
          uni.uploadFile({
            url: 'http://localhost:8080/api/system/upload-logo',
            filePath: tempFilePath,
            name: 'file',
            header: {
              'Authorization': uni.getStorageSync('token')
            },
            success: (uploadRes) => {
              const result = JSON.parse(uploadRes.data)
              if (uploadRes.statusCode === 200 && result.code === 200) {
                this.systemSettings.logoUrl = result.data.url
                uni.showToast({
                  title: '上传成功',
                  icon: 'success'
                })
              } else {
                uni.showToast({
                  title: '上传失败',
                  icon: 'none'
                })
              }
            },
            fail: () => {
              uni.showToast({
                title: '上传失败',
                icon: 'none'
              })
            },
            complete: () => {
              this.loading = false
            }
          })
        }
      })
    },
    
    removeLogo() {
      uni.showModal({
        title: '确认删除',
        content: '确定要删除当前Logo吗？',
        success: (res) => {
          if (res.confirm) {
            this.systemSettings.logoUrl = ''
            uni.showToast({
              title: '已删除',
              icon: 'success'
            })
          }
        }
      })
    },
    
    async testEmail() {
      if (!this.systemSettings.emailAccount) {
        uni.showToast({
          title: '请输入邮箱账号',
          icon: 'none'
        })
        return
      }
      
      this.loading = true
      try {
        const [err, res] = await uni.request({
          url: 'http://localhost:8080/api/system/test-email',
          method: 'POST',
          header: {
            'Authorization': uni.getStorageSync('token'),
            'Content-Type': 'application/json'
          },
          data: {
            smtpServer: this.systemSettings.smtpServer,
            smtpPort: this.systemSettings.smtpPort,
            emailAccount: this.systemSettings.emailAccount,
            emailPassword: this.systemSettings.emailPassword
          }
        })
        
        if (!err && res.statusCode === 200) {
          uni.showToast({
            title: '测试邮件发送成功',
            icon: 'success'
          })
        } else {
          uni.showToast({
            title: res.data.message || '测试邮件发送失败',
            icon: 'none'
          })
        }
      } catch (error) {
        uni.showToast({
          title: '测试邮件发送失败',
          icon: 'none'
        })
      } finally {
        this.loading = false
      }
    },
    
    async saveSetting(type) {
      let validatePass = true
      let settingsToSave = {}
      
      // 根据类型校验不同的设置
      if (type === 'basic') {
        if (!this.systemSettings.systemName) {
          uni.showToast({
            title: '请输入系统名称',
            icon: 'none'
          })
          validatePass = false
        }
        
        settingsToSave = {
          systemName: this.systemSettings.systemName,
          logoUrl: this.systemSettings.logoUrl,
          systemDesc: this.systemSettings.systemDesc
        }
      } else if (type === 'email') {
        if (this.systemSettings.emailNotificationEnabled) {
          if (!this.systemSettings.smtpServer || !this.systemSettings.emailAccount) {
            uni.showToast({
              title: '请填写完整的邮件设置',
              icon: 'none'
            })
            validatePass = false
          }
        }
        
        settingsToSave = {
          emailNotificationEnabled: this.systemSettings.emailNotificationEnabled,
          smtpServer: this.systemSettings.smtpServer,
          smtpPort: this.systemSettings.smtpPort,
          emailAccount: this.systemSettings.emailAccount,
          emailPassword: this.systemSettings.emailPassword
        }
      } else if (type === 'security') {
        if (this.systemSettings.loginLimitEnabled) {
          if (!this.systemSettings.maxLoginAttempts || !this.systemSettings.lockDurationMinutes) {
            uni.showToast({
              title: '请填写完整的登录限制设置',
              icon: 'none'
            })
            validatePass = false
          }
        }
        
        if (this.systemSettings.passwordPolicyEnabled) {
          if (!this.systemSettings.minPasswordLength) {
            uni.showToast({
              title: '请填写密码最小长度',
              icon: 'none'
            })
            validatePass = false
          }
        }
        
        settingsToSave = {
          loginLimitEnabled: this.systemSettings.loginLimitEnabled,
          maxLoginAttempts: this.systemSettings.maxLoginAttempts,
          lockDurationMinutes: this.systemSettings.lockDurationMinutes,
          passwordPolicyEnabled: this.systemSettings.passwordPolicyEnabled,
          minPasswordLength: this.systemSettings.minPasswordLength,
          passwordComplexity: this.systemSettings.passwordComplexity
        }
      }
      
      if (!validatePass) return
      
      this.loading = true
      try {
        const [err, res] = await uni.request({
          url: `http://localhost:8080/api/system/settings/${type}`,
          method: 'PUT',
          header: {
            'Authorization': uni.getStorageSync('token'),
            'Content-Type': 'application/json'
          },
          data: settingsToSave
        })
        
        if (!err && res.statusCode === 200) {
          uni.showToast({
            title: '保存成功',
            icon: 'success'
          })
        } else {
          uni.showToast({
            title: res.data.message || '保存失败',
            icon: 'none'
          })
        }
      } catch (error) {
        uni.showToast({
          title: '保存失败',
          icon: 'none'
        })
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style lang="scss">
.container {
  min-height: 100vh;
  background-color: #f6f8fa;
  display: flex;
  flex-direction: column;
}

.header {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #4aa3ff 0%, #3a93f0 100%);
  color: #fff;
  padding: 20rpx 30rpx;
  height: 90rpx;
  box-shadow: 0 4rpx 16rpx rgba(74, 163, 255, 0.2);
  position: relative;
  z-index: 10;
  
  .back-button {
    font-size: 40rpx;
    width: 60rpx;
    display: flex;
    align-items: center;
    transition: opacity 0.2s;
    
    &:active {
      opacity: 0.7;
    }
  }
  
  .title {
    font-size: 36rpx;
    font-weight: 500;
    text-align: center;
    flex: 1;
  }
}

.content {
  flex: 1;
  padding: 30rpx;
  
  .setting-groups {
    .setting-card {
      background-color: #fff;
      border-radius: 16rpx;
      box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.05);
      margin-bottom: 30rpx;
      overflow: hidden;
      transition: transform 0.3s, box-shadow 0.3s;
      
      &:hover {
        transform: translateY(-2rpx);
        box-shadow: 0 8rpx 24rpx rgba(0,0,0,0.08);
      }
      
      .card-header {
        padding: 30rpx;
        border-bottom: 1rpx solid #f0f0f0;
        
        .card-title {
          font-size: 32rpx;
          font-weight: 600;
          color: #333;
          display: flex;
          align-items: center;
          
          .title-icon {
            font-size: 36rpx;
            color: #4aa3ff;
            margin-right: 16rpx;
          }
        }
      }
      
      .setting-list {
        .setting-item {
          display: flex;
          align-items: center;
          padding: 24rpx 30rpx;
          border-bottom: 1rpx solid #f8f8f8;
          
          &:last-child {
            border-bottom: none;
          }
          
          .setting-label {
            width: 240rpx;
            font-size: 28rpx;
            font-weight: 500;
            color: #333;
            padding-right: 20rpx;
          }
          
          .setting-editor {
            flex: 1;
            
            .input {
              width: 100%;
              height: 80rpx;
              background-color: #f6f8fa;
              border-radius: 12rpx;
              padding: 0 24rpx;
              font-size: 28rpx;
              color: #333;
              border: 2rpx solid transparent;
              transition: all 0.3s;
              box-shadow: inset 0 2rpx 6rpx rgba(0,0,0,0.03);
              
              &:focus {
                border-color: #4aa3ff;
                box-shadow: 0 0 0 3rpx rgba(74, 163, 255, 0.1);
              }
            }
            
            .textarea {
              width: 100%;
              height: 160rpx;
              background-color: #f6f8fa;
              border-radius: 12rpx;
              padding: 24rpx;
              font-size: 28rpx;
              color: #333;
              border: 2rpx solid transparent;
              transition: all 0.3s;
              box-shadow: inset 0 2rpx 6rpx rgba(0,0,0,0.03);
              
              &:focus {
                border-color: #4aa3ff;
                box-shadow: 0 0 0 3rpx rgba(74, 163, 255, 0.1);
              }
            }
            
            .picker-value {
              width: 100%;
              height: 80rpx;
              line-height: 80rpx;
              background-color: #f6f8fa;
              border-radius: 12rpx;
              padding: 0 24rpx;
              font-size: 28rpx;
              color: #333;
              display: flex;
              justify-content: space-between;
              align-items: center;
              border: 2rpx solid transparent;
              transition: all 0.3s;
              box-shadow: inset 0 2rpx 6rpx rgba(0,0,0,0.03);
              
              .iconfont {
                font-size: 24rpx;
                color: #999;
                transition: transform 0.3s;
              }
              
              &:active .iconfont {
                transform: rotate(180deg);
              }
            }
            
            .switch {
              transform: scale(0.9);
              margin-left: -10rpx;
            }
            
            .logo-preview {
              position: relative;
              width: 200rpx;
              height: 120rpx;
              display: flex;
              justify-content: center;
              align-items: center;
              background-color: #f6f8fa;
              border-radius: 12rpx;
              box-shadow: inset 0 2rpx 6rpx rgba(0,0,0,0.03);
              
              .logo-image {
                max-width: 180rpx;
                max-height: 100rpx;
                border-radius: 4rpx;
              }
              
              .logo-remove {
                position: absolute;
                top: -16rpx;
                right: -16rpx;
                width: 44rpx;
                height: 44rpx;
                background: #ff4d4f;
                color: #fff;
                border-radius: 50%;
                display: flex;
                justify-content: center;
                align-items: center;
                font-size: 24rpx;
                box-shadow: 0 4rpx 12rpx rgba(255, 77, 79, 0.25);
                z-index: 2;
                transition: all 0.3s;
                
                &:active {
                  transform: scale(0.9);
                }
              }
            }
            
            .upload-button {
              width: 200rpx;
              height: 80rpx;
              line-height: 80rpx;
              background: linear-gradient(135deg, #4aa3ff 0%, #3a93f0 100%);
              color: #fff;
              font-size: 28rpx;
              border-radius: 40rpx;
              padding: 0 40rpx;
              display: flex;
              align-items: center;
              justify-content: center;
              box-shadow: 0 4rpx 16rpx rgba(74, 163, 255, 0.3);
              transition: all 0.3s;
              
              &:active {
                transform: translateY(2rpx);
                box-shadow: 0 2rpx 8rpx rgba(74, 163, 255, 0.2);
              }
              
              .upload-icon {
                font-size: 28rpx;
                margin-right: 8rpx;
              }
            }
          }
        }
      }
      
      .card-actions {
        display: flex;
        justify-content: flex-end;
        padding: 24rpx 30rpx;
        border-top: 1rpx solid #f0f0f0;
        
        button {
          height: 80rpx;
          line-height: 80rpx;
          border-radius: 40rpx;
          padding: 0 40rpx;
          font-size: 28rpx;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-left: 20rpx;
          transition: all 0.3s;
          
          .button-icon {
            font-size: 28rpx;
            margin-right: 8rpx;
          }
          
          &.test-button {
            background-color: #f9f0ff;
            color: #722ed1;
            box-shadow: 0 4rpx 16rpx rgba(114, 46, 209, 0.15);
            
            &:active {
              transform: translateY(2rpx);
              box-shadow: 0 2rpx 8rpx rgba(114, 46, 209, 0.1);
            }
          }
          
          &.save-button {
            background: linear-gradient(135deg, #4aa3ff 0%, #3a93f0 100%);
            color: #fff;
            box-shadow: 0 4rpx 16rpx rgba(74, 163, 255, 0.3);
            
            &:active {
              transform: translateY(2rpx);
              box-shadow: 0 2rpx 8rpx rgba(74, 163, 255, 0.2);
            }
          }
        }
      }
    }
  }
}

.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  backdrop-filter: blur(5rpx);
  -webkit-backdrop-filter: blur(5rpx);
  
  .loading-content {
    background-color: rgba(0, 0, 0, 0.7);
    padding: 30rpx 60rpx;
    border-radius: 16rpx;
    box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.2);
    transform: scale(0.9);
    animation: pulse 1.5s infinite alternate;
    
    @keyframes pulse {
      0% {
        transform: scale(0.95);
      }
      100% {
        transform: scale(1);
      }
    }
    
    .loading-text {
      color: #fff;
      font-size: 28rpx;
    }
  }
}
</style> 