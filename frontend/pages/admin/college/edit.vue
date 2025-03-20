<template>
  <view class="container">
    <view class="header">
      <view class="back-button" @tap="navBack">
        <text class="iconfont icon-left"></text>
      </view>
      <text class="title">{{ isEdit ? '编辑学院' : '添加学院' }}</text>
    </view>
    
    <view class="form-container">
      <view class="form-section">
        <view class="section-title">基本信息</view>
        
        <view class="form-item">
          <text class="label">学院名称 <text class="required">*</text></text>
          <input 
            type="text" 
            v-model="collegeForm.name" 
            placeholder="请输入学院名称"
            class="input"
          />
        </view>
        
        <view class="form-item">
          <text class="label">学历层次</text>
          <picker
            @change="handleDegreeChange"
            :value="degreeIndex"
            :range="degreeOptions"
          >
            <view class="picker-value">
              {{ degreeOptions[degreeIndex] || '请选择学历层次' }}
              <text class="iconfont icon-down"></text>
            </view>
          </picker>
        </view>
        
        <view class="form-item">
          <text class="label">学制(年)</text>
          <picker
            @change="handleDurationChange"
            :value="durationIndex"
            :range="durationOptions"
          >
            <view class="picker-value">
              {{ durationOptions[durationIndex] || '请选择学制' }}
              <text class="iconfont icon-down"></text>
            </view>
          </picker>
        </view>
        
        <view class="form-item">
          <text class="label">学院介绍</text>
          <textarea 
            v-model="collegeForm.description" 
            placeholder="请输入学院介绍"
            maxlength="1000"
            class="textarea"
          ></textarea>
          <text class="word-count">{{ collegeForm.description.length }}/1000</text>
        </view>
      </view>
      
      <view class="form-section">
        <view class="section-title">媒体资源</view>
        
        <view class="form-item">
          <text class="label">学院Logo</text>
          <view class="upload-area">
            <image 
              v-if="collegeForm.logoUrl" 
              :src="collegeForm.logoUrl" 
              class="preview-image"
              mode="aspectFit"
              @tap="previewImage(collegeForm.logoUrl)"
            ></image>
            <view v-else class="upload-placeholder" @tap="uploadLogo">
              <text class="iconfont icon-upload"></text>
              <text class="upload-text">上传Logo</text>
            </view>
            <button class="upload-btn" @tap="uploadLogo">{{ collegeForm.logoUrl ? '更换' : '上传' }}</button>
          </view>
          <text class="hint">推荐尺寸：200x200px，格式：JPG/PNG</text>
        </view>
        
        <view class="form-item">
          <text class="label">学院Banner</text>
          <view class="upload-area">
            <image 
              v-if="collegeForm.bannerUrl" 
              :src="collegeForm.bannerUrl" 
              class="preview-image banner-preview"
              mode="aspectFill"
              @tap="previewImage(collegeForm.bannerUrl)"
            ></image>
            <view v-else class="upload-placeholder banner-placeholder" @tap="uploadBanner">
              <text class="iconfont icon-upload"></text>
              <text class="upload-text">上传Banner</text>
            </view>
            <button class="upload-btn" @tap="uploadBanner">{{ collegeForm.bannerUrl ? '更换' : '上传' }}</button>
          </view>
          <text class="hint">推荐尺寸：1080x300px，格式：JPG/PNG</text>
        </view>
      </view>
    </view>
    
    <view class="footer">
      <button class="cancel-btn" @tap="navBack">取消</button>
      <button class="submit-btn" @tap="submitForm">保存</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      isEdit: false,
      collegeId: null,
      collegeForm: {
        id: null,
        name: '',
        degree: '本科',
        duration: '4',
        description: '',
        logoUrl: '',
        bannerUrl: ''
      },
      degreeOptions: ['专科', '本科', '专科和本科'],
      degreeIndex: 1,
      durationOptions: ['3', '4', '5'],
      durationIndex: 1,
      loading: false
    }
  },
  onLoad(options) {
    this.checkAuth();
    
    if (options.type === 'edit' && options.id) {
      this.isEdit = true;
      this.collegeId = options.id;
      this.loadCollegeDetail(options.id);
    }
  },
  methods: {
    checkAuth() {
      try {
        const userInfoStr = uni.getStorageSync('userInfo');
        if (userInfoStr) {
          // 检查是否是对象类型
          const userInfo = typeof userInfoStr === 'object' ? userInfoStr : JSON.parse(userInfoStr);
          if (userInfo.role !== 'ROLE_ADMIN') {
            uni.showToast({
              title: '无权访问',
              icon: 'none'
            });
            setTimeout(() => {
              uni.navigateBack();
            }, 1500);
          }
        } else {
          uni.showToast({
            title: '请先登录',
            icon: 'none'
          });
          setTimeout(() => {
            uni.navigateBack();
          }, 1500);
        }
      } catch (e) {
        console.error('获取用户信息失败:', e);
        // 处理错误，让用户能够继续操作
        uni.showToast({
          title: '获取用户信息失败，请重新登录',
          icon: 'none'
        });
        setTimeout(() => {
          uni.navigateTo({
            url: '/pages/my/login'
          });
        }, 1500);
      }
    },
    
    navBack() {
      uni.navigateBack();
    },
    
    async loadCollegeDetail(id) {
      this.loading = true;
      try {
        const [err, res] = await uni.request({
          url: `http://localhost:8080/api/college/${id}`,
          method: 'GET',
          header: {
            'Authorization': uni.getStorageSync('token')
          }
        });
        
        if (!err && res.statusCode === 200 && res.data) {
          this.collegeForm = res.data;
          
          // 设置学历层次选择器索引
          const degreeIndex = this.degreeOptions.findIndex(d => d === res.data.degree);
          this.degreeIndex = degreeIndex >= 0 ? degreeIndex : 1;
          
          // 设置学制选择器索引
          const durationIndex = this.durationOptions.findIndex(d => d === res.data.duration);
          this.durationIndex = durationIndex >= 0 ? durationIndex : 1;
        } else {
          uni.showToast({
            title: '获取学院信息失败',
            icon: 'none'
          });
          setTimeout(() => {
            uni.navigateBack();
          }, 1500);
        }
      } catch (e) {
        console.error('获取学院信息异常:', e);
        uni.showToast({
          title: '获取学院信息异常',
          icon: 'none'
        });
      } finally {
        this.loading = false;
      }
    },
    
    handleDegreeChange(e) {
      this.degreeIndex = e.detail.value;
      this.collegeForm.degree = this.degreeOptions[this.degreeIndex];
    },
    
    handleDurationChange(e) {
      this.durationIndex = e.detail.value;
      this.collegeForm.duration = this.durationOptions[this.durationIndex];
    },
    
    uploadLogo() {
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          this.uploadFile(res.tempFilePaths[0], 'logo');
        }
      });
    },
    
    uploadBanner() {
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          this.uploadFile(res.tempFilePaths[0], 'banner');
        }
      });
    },
    
    async uploadFile(filePath, type) {
      uni.showLoading({
        title: '上传中...'
      });
      
      try {
        // 在实际应用中，需要调用后端的文件上传接口
        // 这里模拟上传成功后的逻辑
        setTimeout(() => {
          // 模拟上传成功，后端返回图片URL
          // 实际应用中应当使用返回的真实URL
          const randomFileName = Date.now() + Math.floor(Math.random() * 1000);
          const fileUrl = filePath; // 实际使用时应换成后端返回的URL
          
          if (type === 'logo') {
            this.collegeForm.logoUrl = fileUrl;
          } else if (type === 'banner') {
            this.collegeForm.bannerUrl = fileUrl;
          }
          
          uni.hideLoading();
          uni.showToast({
            title: '上传成功',
            icon: 'success'
          });
        }, 1500);
        
        // 实际的上传代码如下（需要后端支持）：
        /*
        const [err, res] = await uni.uploadFile({
          url: 'http://localhost:8080/api/upload',
          filePath: filePath,
          name: 'file',
          header: {
            'Authorization': uni.getStorageSync('token')
          },
          formData: {
            'type': type
          }
        });
        
        if (!err && res.statusCode === 200) {
          const data = JSON.parse(res.data);
          if (type === 'logo') {
            this.collegeForm.logoUrl = data.url;
          } else if (type === 'banner') {
            this.collegeForm.bannerUrl = data.url;
          }
          
          uni.hideLoading();
          uni.showToast({
            title: '上传成功',
            icon: 'success'
          });
        } else {
          throw new Error('上传失败');
        }
        */
      } catch (e) {
        console.error('上传文件异常:', e);
        uni.hideLoading();
        uni.showToast({
          title: '上传失败',
          icon: 'none'
        });
      }
    },
    
    previewImage(url) {
      uni.previewImage({
        urls: [url],
        current: url
      });
    },
    
    async submitForm() {
      if (!this.collegeForm.name) {
        uni.showToast({
          title: '请输入学院名称',
          icon: 'none'
        });
        return;
      }
      
      this.loading = true;
      try {
        const url = this.isEdit 
          ? 'http://localhost:8080/api/college/update'
          : 'http://localhost:8080/api/college/create';
        
        const method = this.isEdit ? 'PUT' : 'POST';
        
        const [err, res] = await uni.request({
          url,
          method,
          data: this.collegeForm,
          header: {
            'Authorization': uni.getStorageSync('token'),
            'Content-Type': 'application/json'
          }
        });
        
        if (!err && (res.statusCode === 200 || res.statusCode === 201)) {
          uni.showToast({
            title: this.isEdit ? '更新成功' : '添加成功',
            icon: 'success'
          });
          
          setTimeout(() => {
            uni.navigateBack();
          }, 1500);
        } else {
          uni.showToast({
            title: this.isEdit ? '更新失败' : '添加失败',
            icon: 'none'
          });
        }
      } catch (e) {
        console.error(this.isEdit ? '更新学院异常:' : '添加学院异常:', e);
        uni.showToast({
          title: this.isEdit ? '更新异常' : '添加异常',
          icon: 'none'
        });
      } finally {
        this.loading = false;
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

.form-container {
  flex: 1;
  padding-bottom: 150rpx;
  
  .scroll-view {
    height: calc(100vh - 240rpx);
    padding: 30rpx;
  }
}

.form-section {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 4rpx 20rpx rgba(0,0,0,0.05);
  transition: transform 0.3s, box-shadow 0.3s;
  
  &:hover {
    transform: translateY(-2rpx);
    box-shadow: 0 8rpx 24rpx rgba(0,0,0,0.08);
  }
  
  .section-title {
    font-size: 32rpx;
    font-weight: 600;
    color: #333;
    margin-bottom: 30rpx;
    position: relative;
    padding-left: 20rpx;
    
    &:before {
      content: '';
      position: absolute;
      left: 0;
      top: 6rpx;
      width: 6rpx;
      height: 28rpx;
      background: linear-gradient(to bottom, #4aa3ff, #3a93f0);
      border-radius: 3rpx;
    }
  }
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
  }
  
  .input, .picker-value, .textarea {
    width: 100%;
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
    display: flex;
    justify-content: space-between;
    align-items: center;
    color: #333;
    
    .iconfont {
      font-size: 28rpx;
      color: #999;
      transition: transform 0.3s;
    }
    
    &:active .iconfont {
      transform: rotate(180deg);
    }
  }
  
  .textarea {
    height: 200rpx;
    width: 100%;
    box-sizing: border-box;
  }
  
  .word-count {
    text-align: right;
    font-size: 24rpx;
    color: #999;
    margin-top: 10rpx;
    display: block;
  }
  
  .hint {
    font-size: 24rpx;
    color: #999;
    margin-top: 10rpx;
    display: block;
  }
  
  .upload-area {
    display: flex;
    margin-bottom: 10rpx;
    align-items: center;
    
    .preview-image {
      width: 200rpx;
      height: 200rpx;
      background-color: #f6f8fa;
      border-radius: 12rpx;
      margin-right: 20rpx;
      object-fit: cover;
      box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.08);
      transition: transform 0.3s;
      
      &:active {
        transform: scale(0.98);
      }
    }
    
    .upload-placeholder {
      width: 200rpx;
      height: 200rpx;
      background-color: #f6f8fa;
      border-radius: 12rpx;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      margin-right: 20rpx;
      border: 2rpx dashed #ddd;
      transition: all 0.3s;
      
      &:active {
        background-color: #eef6ff;
        border-color: #4aa3ff;
      }
      
      .iconfont {
        font-size: 40rpx;
        color: #999;
        margin-bottom: 10rpx;
      }
      
      .upload-text {
        font-size: 26rpx;
        color: #999;
      }
    }
    
    .upload-btn {
      height: 70rpx;
      line-height: 70rpx;
      background-color: #e6f7ff;
      color: #4aa3ff;
      font-size: 26rpx;
      border-radius: 35rpx;
      padding: 0 30rpx;
      border: none;
      transition: all 0.3s;
      box-shadow: 0 2rpx 8rpx rgba(74, 163, 255, 0.15);
      
      &:active {
        transform: translateY(2rpx);
        box-shadow: 0 1rpx 3rpx rgba(74, 163, 255, 0.1);
      }
    }
  }
  
  .images-list {
    display: flex;
    flex-wrap: wrap;
    margin-bottom: 10rpx;
    
    .image-item {
      width: 200rpx;
      height: 200rpx;
      margin-right: 20rpx;
      margin-bottom: 20rpx;
      position: relative;
      
      .preview-image {
        width: 100%;
        height: 100%;
        border-radius: 12rpx;
        background-color: #f6f8fa;
        object-fit: cover;
        box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.08);
        transition: transform 0.3s;
        
        &:active {
          transform: scale(0.98);
        }
      }
      
      .delete-btn {
        position: absolute;
        top: -10rpx;
        right: -10rpx;
        width: 44rpx;
        height: 44rpx;
        line-height: 40rpx;
        text-align: center;
        background-color: #ff4d4f;
        color: #fff;
        font-size: 28rpx;
        border-radius: 50%;
        z-index: 10;
        box-shadow: 0 2rpx 8rpx rgba(255, 77, 79, 0.3);
        transition: all 0.3s;
        
        &:active {
          transform: scale(1.1);
          box-shadow: 0 3rpx 12rpx rgba(255, 77, 79, 0.4);
        }
      }
      
      &.add-image {
        background-color: #f6f8fa;
        border-radius: 12rpx;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        border: 2rpx dashed #ddd;
        transition: all 0.3s;
        
        &:active {
          background-color: #eef6ff;
          border-color: #4aa3ff;
        }
        
        .iconfont {
          font-size: 40rpx;
          color: #999;
          margin-bottom: 10rpx;
        }
        
        .add-text {
          font-size: 26rpx;
          color: #999;
        }
      }
    }
  }
}

.footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  padding: 20rpx 30rpx;
  background-color: #fff;
  box-shadow: 0 -4rpx 16rpx rgba(0,0,0,0.08);
  z-index: 100;
  
  button {
    flex: 1;
    height: 90rpx;
    line-height: 90rpx;
    font-size: 32rpx;
    border-radius: 45rpx;
    border: none;
    transition: all 0.3s;
    
    &.cancel-btn {
      background-color: #f6f8fa;
      color: #666;
      margin-right: 20rpx;
      
      &:active {
        background-color: #eee;
        transform: translateY(2rpx);
      }
    }
    
    &.submit-btn {
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