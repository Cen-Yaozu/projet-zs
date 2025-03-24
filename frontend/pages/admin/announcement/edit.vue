<template>
  <view class="container">
    <view class="header">
      <view class="back-button" @tap="navBack">
        <text class="iconfont icon-left"></text>
      </view>
      <text class="title">{{isEdit ? '编辑公告' : '新增公告'}}</text>
      <view class="preview-button" @tap="previewAnnounce" v-if="form.title && form.content">
        <text class="iconfont icon-eye"></text>
      </view>
    </view>
    
    <view class="content">
      <view class="form-container">
        <view class="form-section">
          <view class="form-item">
            <text class="label">标题<text class="required">*</text></text>
            <input 
              type="text" 
              v-model="form.title" 
              placeholder="请输入公告标题"
              class="input"
            />
          </view>
          
          <view class="form-item">
            <text class="label">封面图<text class="tip">(建议上传16:9宽图)</text></text>
            <view class="upload-area" @tap="handleUploadImage" v-if="!form.coverImage">
              <text class="iconfont icon-image"></text>
              <text class="upload-text">点击上传封面图片</text>
            </view>
            <view class="image-preview" v-else>
              <image :src="form.coverImage" mode="aspectFill" class="preview-image"></image>
              <view class="image-actions">
                <view class="action-icon" @tap="handleUploadImage">
                  <text class="iconfont icon-edit"></text>
                </view>
                <view class="action-icon" @tap="removeCoverImage">
                  <text class="iconfont icon-delete"></text>
                </view>
              </view>
            </view>
          </view>
          
          <view class="form-item">
            <text class="label">内容<text class="required">*</text></text>
            <textarea 
              v-model="form.content" 
              placeholder="请输入公告内容"
              class="textarea"
              maxlength="2000"
            />
            <text class="word-count">{{form.content.length}}/2000</text>
          </view>
          
          <view class="form-item">
            <text class="label">类型<text class="required">*</text></text>
            <picker 
              :range="typeOptions" 
              :value="typeIndex"
              @change="handleTypeChange"
            >
              <view class="picker-value">
                {{typeOptions[typeIndex]}}
                <text class="iconfont icon-down"></text>
              </view>
            </picker>
          </view>
          
          <view class="form-item">
            <text class="label">类别</text>
            <view class="category-selector">
              <view 
                class="category-option" :class="{ active: form.category === 'GENERAL_NOTICE' }"
                @tap="form.category = 'GENERAL_NOTICE'"
              >招生公告</view>
              <view 
                class="category-option" :class="{ active: form.category === 'ADMISSION_RULES' }"
                @tap="form.category = 'ADMISSION_RULES'"
              >招生章程</view>
              <view 
                class="category-option" :class="{ active: form.category === 'ADMISSION_POLICY' }"
                @tap="form.category = 'ADMISSION_POLICY'"
              >招生政策</view>
              <view 
                class="category-option" :class="{ active: form.category === 'HISTORICAL_REFERENCE' }"
                @tap="form.category = 'HISTORICAL_REFERENCE'"
              >往年参考</view>
            </view>
          </view>
          
          <view class="form-item" v-if="isEdit">
            <text class="label">状态</text>
            <view class="status-selector">
              <view 
                class="status-option" :class="{ active: form.status === false }"
                @tap="form.status = false"
              >草稿</view>
              <view 
                class="status-option" :class="{ active: form.status === true }"
                @tap="form.status = true"
              >已发布</view>
            </view>
          </view>
        </view>
      </view>
      
      <view class="action-buttons">
        <button class="cancel-btn" @tap="navBack">取消</button>
        <button class="submit-btn" @tap="handleSubmit">保存</button>
      </view>
    </view>
    
    <!-- 预览弹窗 -->
    <uni-popup ref="previewPopup" type="center">
      <view class="preview-popup">
        <view class="preview-header">
          <text class="preview-title">预览公告</text>
          <text class="preview-close" @tap="$refs.previewPopup.close()">×</text>
        </view>
        <scroll-view class="preview-content" scroll-y>
          <view class="preview-article">
            <text class="article-title">{{form.title}}</text>
            <view class="article-meta">
              <text class="article-type">{{typeOptions[typeIndex]}}</text>
              <text class="article-date">{{currentDate}}</text>
            </view>
            <image 
              v-if="form.coverImage" 
              :src="form.coverImage" 
              mode="widthFix" 
              class="article-cover">
            </image>
            <view class="article-body">{{form.content}}</view>
          </view>
        </scroll-view>
      </view>
    </uni-popup>
    
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
      isEdit: false,
      announceId: null,
      loading: false,
      form: {
        title: '',
        content: '',
        type: 'NOTICE',
        status: false,
        coverImage: '',
        category: 'GENERAL_NOTICE'
      },
      typeOptions: ['通知公告', '招生信息', '新闻动态'],
      typeIndex: 0,
      typeValues: ['NOTICE', 'ADMISSION', 'NEWS']
    }
  },
  computed: {
    currentDate() {
      const now = new Date()
      return `${now.getFullYear()}-${(now.getMonth() + 1).toString().padStart(2, '0')}-${now.getDate().toString().padStart(2, '0')}`
    }
  },
  onLoad(options) {
    // 根据路由参数确定是新增还是编辑
    if (options.type === 'edit' && options.id) {
      this.isEdit = true
      this.announceId = options.id
      this.loadAnnounceDetail()
    }
  },
  methods: {
    navBack() {
      uni.navigateBack()
    },
    
    async loadAnnounceDetail() {
      if (!this.announceId) return
      
      this.loading = true
      try {
        const [err, res] = await uni.request({
          url: `http://localhost:8080/api/announcements/${this.announceId}`,
          method: 'GET',
          header: {
            'Authorization': uni.getStorageSync('token')
          }
        })
        
        if (!err && res.statusCode === 200 && res.data.data) {
          const announcement = res.data.data
          this.form = { 
            title: announcement.title || '',
            content: announcement.content || '',
            type: announcement.type || 'NOTICE',
            status: announcement.status !== undefined ? announcement.status : false,
            coverImage: announcement.coverImage || '',
            category: announcement.category || 'GENERAL_NOTICE'
          }
          this.typeIndex = this.typeValues.indexOf(this.form.type)
          if (this.typeIndex === -1) this.typeIndex = 0
        } else {
          uni.showToast({
            title: '获取公告详情失败',
            icon: 'none'
          })
        }
      } catch (error) {
        console.error('获取公告详情失败:', error)
        uni.showToast({
          title: '获取公告详情失败',
          icon: 'none'
        })
      } finally {
        this.loading = false
      }
    },
    
    handleTypeChange(e) {
      this.typeIndex = e.detail.value
      this.form.type = this.typeValues[this.typeIndex]
    },
    
    async handleSubmit() {
      // 表单验证
      if (!this.form.title.trim()) {
        uni.showToast({
          title: '请输入公告标题',
          icon: 'none'
        })
        return
      }
      
      if (!this.form.content.trim()) {
        uni.showToast({
          title: '请输入公告内容',
          icon: 'none'
        })
        return
      }
      
      this.loading = true
      try {
        const url = this.isEdit 
          ? `http://localhost:8080/api/announcements/${this.announceId}`
          : 'http://localhost:8080/api/announcements'
        
        const [err, res] = await uni.request({
          url,
          method: this.isEdit ? 'PUT' : 'POST',
          header: {
            'Authorization': uni.getStorageSync('token'),
            'Content-Type': 'application/json'
          },
          data: this.form
        })
        
        if (!err && (res.statusCode === 200 || res.statusCode === 201)) {
          uni.showToast({
            title: this.isEdit ? '更新成功' : '添加成功',
            icon: 'success'
          })
          
          // 延迟返回上一页，让用户看到成功提示
          setTimeout(() => {
            uni.navigateBack()
          }, 1500)
        } else {
          uni.showToast({
            title: res.data?.message || (this.isEdit ? '更新失败' : '添加失败'),
            icon: 'none'
          })
        }
      } catch (error) {
        console.error(this.isEdit ? '更新公告失败:' : '添加公告失败:', error)
        uni.showToast({
          title: this.isEdit ? '更新失败' : '添加失败',
          icon: 'none'
        })
      } finally {
        this.loading = false
      }
    },
    
    handleUploadImage() {
      uni.chooseImage({
        count: 1,
        sizeType: ['original', 'compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          const tempFilePaths = res.tempFilePaths
          this.uploadImage(tempFilePaths[0])
        }
      })
    },
    
    async uploadImage(filePath) {
      this.loading = true
      try {
        // 此处应该替换为实际的文件上传接口
        // 模拟上传成功并返回URL
        setTimeout(() => {
          this.form.coverImage = filePath
          this.loading = false
        }, 1000)
        
        // 实际上传代码类似如下:
        /*
        const [err, res] = await uni.uploadFile({
          url: 'http://localhost:8080/api/upload',
          filePath: filePath,
          name: 'file',
          header: {
            'Authorization': uni.getStorageSync('token')
          }
        })
        
        if (!err && res.statusCode === 200) {
          const data = JSON.parse(res.data)
          this.form.coverImage = data.url
        } else {
          uni.showToast({
            title: '上传图片失败',
            icon: 'none'
          })
        }
        */
      } catch (error) {
        console.error('上传图片失败:', error)
        uni.showToast({
          title: '上传图片失败',
          icon: 'none'
        })
      } finally {
        this.loading = false
      }
    },
    
    removeCoverImage() {
      this.form.coverImage = ''
    },
    
    previewAnnounce() {
      this.$refs.previewPopup.open()
    },
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
  
  .back-button, .preview-button {
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
  display: flex;
  flex-direction: column;
}

.form-container {
  flex: 1;
  overflow: auto;
}

.form-section {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.05);
}

.form-item {
  margin-bottom: 30rpx;
  
  &:last-child {
    margin-bottom: 0;
  }
  
  .label {
    font-size: 28rpx;
    color: #333;
    margin-bottom: 15rpx;
    display: block;
    font-weight: 500;
    
    .required {
      color: #ff4d4f;
      font-weight: bold;
    }
    
    .tip {
      font-size: 24rpx;
      color: #999;
      font-weight: normal;
      margin-left: 10rpx;
    }
  }
  
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
    height: 360rpx;
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
  
  .word-count {
    text-align: right;
    font-size: 24rpx;
    color: #999;
    margin-top: 10rpx;
    display: block;
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
  
  .status-selector {
    display: flex;
    border-radius: 12rpx;
    overflow: hidden;
    background-color: #f6f8fa;
    box-shadow: inset 0 2rpx 6rpx rgba(0,0,0,0.03);
    
    .status-option {
      flex: 1;
      height: 80rpx;
      line-height: 80rpx;
      text-align: center;
      font-size: 28rpx;
      color: #666;
      transition: all 0.3s;
      
      &.active {
        background-color: #4aa3ff;
        color: #fff;
        font-weight: 500;
      }
      
      &:active {
        opacity: 0.8;
      }
    }
  }
  
  .upload-area {
    width: 100%;
    height: 200rpx;
    background-color: #f6f8fa;
    border-radius: 12rpx;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    border: 2rpx dashed #ddd;
    transition: all 0.3s;
    
    .iconfont {
      font-size: 56rpx;
      color: #bbb;
      margin-bottom: 20rpx;
    }
    
    .upload-text {
      font-size: 28rpx;
      color: #999;
    }
    
    &:active {
      background-color: #eef1f5;
    }
  }
  
  .image-preview {
    position: relative;
    width: 100%;
    height: 300rpx;
    border-radius: 12rpx;
    overflow: hidden;
    box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.1);
    
    .preview-image {
      width: 100%;
      height: 100%;
    }
    
    .image-actions {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      display: flex;
      justify-content: flex-end;
      padding: 16rpx;
      background: linear-gradient(to top, rgba(0,0,0,0.5), transparent);
      
      .action-icon {
        width: 60rpx;
        height: 60rpx;
        background-color: rgba(255,255,255,0.8);
        border-radius: 30rpx;
        display: flex;
        justify-content: center;
        align-items: center;
        margin-left: 20rpx;
        box-shadow: 0 4rpx 8rpx rgba(0,0,0,0.2);
        transition: all 0.2s;
        
        .iconfont {
          font-size: 32rpx;
          color: #666;
        }
        
        &:active {
          transform: scale(0.95);
        }
      }
    }
  }
}

.action-buttons {
  display: flex;
  justify-content: space-between;
  padding: 20rpx 0;
  
  button {
    width: 45%;
    height: 92rpx;
    line-height: 92rpx;
    font-size: 32rpx;
    border-radius: 46rpx;
    box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.1);
    transition: all 0.3s;
    
    &.cancel-btn {
      background-color: #f6f8fa;
      color: #666;
      
      &:active {
        transform: translateY(2rpx);
        box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.05);
      }
    }
    
    &.submit-btn {
      background: linear-gradient(135deg, #4aa3ff 0%, #3a93f0 100%);
      color: #fff;
      font-weight: 500;
      
      &:active {
        transform: translateY(2rpx);
        box-shadow: 0 2rpx 8rpx rgba(74, 163, 255, 0.2);
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

.preview-popup {
  width: 650rpx;
  height: 800rpx;
  background-color: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  box-shadow: 0 20rpx 40rpx rgba(0, 0, 0, 0.2);
  
  .preview-header {
    height: 90rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 30rpx;
    border-bottom: 1rpx solid #f0f0f0;
    
    .preview-title {
      font-size: 32rpx;
      font-weight: 600;
      color: #333;
    }
    
    .preview-close {
      font-size: 48rpx;
      color: #999;
      line-height: 1;
      padding: 10rpx;
      transition: all 0.2s;
      
      &:active {
        transform: scale(0.9);
      }
    }
  }
  
  .preview-content {
    flex: 1;
    padding: 30rpx;
  }
  
  .preview-article {
    .article-title {
      font-size: 36rpx;
      font-weight: 600;
      color: #333;
      line-height: 1.4;
      margin-bottom: 20rpx;
    }
    
    .article-meta {
      display: flex;
      align-items: center;
      margin-bottom: 30rpx;
      
      .article-type {
        font-size: 24rpx;
        color: #4aa3ff;
        background-color: rgba(74, 163, 255, 0.1);
        padding: 6rpx 16rpx;
        border-radius: 20rpx;
        margin-right: 20rpx;
      }
      
      .article-date {
        font-size: 24rpx;
        color: #999;
      }
    }
    
    .article-cover {
      width: 100%;
      border-radius: 12rpx;
      margin-bottom: 30rpx;
    }
    
    .article-body {
      font-size: 28rpx;
      color: #333;
      line-height: 1.8;
      text-align: justify;
      white-space: pre-wrap;
    }
  }
}

.category-selector {
  display: flex;
  border-radius: 12rpx;
  overflow: hidden;
  background-color: #f6f8fa;
  box-shadow: inset 0 2rpx 6rpx rgba(0,0,0,0.03);
  
  .category-option {
    flex: 1;
    height: 80rpx;
    line-height: 80rpx;
    text-align: center;
    font-size: 28rpx;
    color: #666;
    transition: all 0.3s;
    
    &.active {
      background-color: #4aa3ff;
      color: #fff;
      font-weight: 500;
    }
    
    &:active {
      opacity: 0.8;
    }
  }
}
</style> 