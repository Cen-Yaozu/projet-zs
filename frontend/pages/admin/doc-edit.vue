<template>
  <view class="doc-edit-container">
    <view class="doc-edit-header">
      <view class="doc-edit-header__title">
        <text class="doc-edit-header__icon"></text>
        <text>编辑公告</text>
      </view>
      
      <view class="doc-edit-actions">
        <button class="zs-button zs-button--primary zs-button--small" @tap="saveAnnouncement">
          <text class="iconfont icon-save"></text>
          <text class="btn-text">保存</text>
        </button>
        <button class="zs-button zs-button--default zs-button--small" @tap="goBack">
          <text class="iconfont icon-back"></text>
          <text class="btn-text">返回</text>
        </button>
      </view>
    </view>
    
    <view class="doc-edit-form">
      <view class="form-group">
        <view class="form-label">
          <text class="form-label__required">*</text>
          <text>公告标题</text>
        </view>
        <input 
          class="form-input" 
          v-model="form.title" 
          placeholder="请输入公告标题"
          maxlength="100"
        />
      </view>
      
      <view class="form-group">
        <view class="form-label">公告状态</view>
        <view class="form-radio-group">
          <view 
            class="form-radio-item"
            :class="{'form-radio-item--active': form.status === 1}"
            @tap="form.status = 1"
          >
            <view class="form-radio"></view>
            <text>已发布</text>
          </view>
          <view 
            class="form-radio-item"
            :class="{'form-radio-item--active': form.status === 0}"
            @tap="form.status = 0"
          >
            <view class="form-radio"></view>
            <text>草稿</text>
          </view>
        </view>
      </view>
      
      <view class="form-group">
        <view class="form-label">置顶公告</view>
        <view class="form-switch" @tap="toggleTopStatus">
          <view class="form-switch__track" :class="{'form-switch__track--active': form.isTop}">
            <view class="form-switch__thumb" :class="{'form-switch__thumb--active': form.isTop}"></view>
          </view>
        </view>
      </view>
      
      <view class="form-group">
        <view class="form-label">公告类型</view>
        <view class="form-select" @tap="showTypeSelector = true">
          <text>{{ getTypeName(form.type) }}</text>
          <text class="iconfont icon-down"></text>
        </view>
      </view>
      
      <view class="form-group">
        <view class="form-label">封面图片</view>
        <view class="form-upload" @tap="uploadImage">
          <image 
            v-if="form.imageUrl" 
            class="form-upload__preview" 
            :src="form.imageUrl" 
            mode="aspectFill"
          />
          <view v-else class="form-upload__add">
            <text class="iconfont icon-plus"></text>
            <text>上传图片</text>
          </view>
        </view>
      </view>
      
      <view class="form-group form-group--full">
        <view class="form-label">
          <text class="form-label__required">*</text>
          <text>公告内容</text>
        </view>
        <textarea 
          class="form-textarea" 
          v-model="form.content" 
          placeholder="请输入公告内容"
          maxlength="5000"
        />
      </view>
    </view>
    
    <!-- 类型选择器弹窗 -->
    <view class="type-selector" v-if="showTypeSelector">
      <view class="type-selector__mask" @tap="showTypeSelector = false"></view>
      <view class="type-selector__content">
        <view class="type-selector__header">
          <text>选择公告类型</text>
          <text class="type-selector__close" @tap="showTypeSelector = false">×</text>
        </view>
        <view class="type-selector__body">
          <view 
            class="type-selector__item"
            v-for="(type, index) in typeOptions" 
            :key="index"
            :class="{'type-selector__item--active': form.type === type.value}"
            @tap="selectType(type.value)"
          >
            <text>{{ type.label }}</text>
            <text v-if="form.type === type.value" class="iconfont icon-check"></text>
          </view>
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
        id: null,
        title: '',
        content: '',
        status: 1, // 1-已发布, 0-草稿
        type: 'ANNOUNCEMENT', // 默认为通知公告
        isTop: false,
        imageUrl: ''
      },
      showTypeSelector: false,
      typeOptions: [
        { label: '招生公告', value: 'ANNOUNCEMENT' },
        { label: '招生章程', value: 'REGULATION' },
        { label: '招生政策', value: 'POLICY' },
        { label: '往年参考', value: 'REFERENCE' }
      ],
      isLoading: false
    }
  },
  onLoad(options) {
    if (options.id) {
      this.loadAnnouncementDetail(options.id);
    }
  },
  methods: {
    // 加载公告详情
    loadAnnouncementDetail(id) {
      uni.showLoading({ title: '加载中...' });
      
      uni.request({
        url: this.$baseUrl + '/api/announcements/' + id,
        method: 'GET',
        header: {
          'Authorization': uni.getStorageSync('token')
        },
        success: (res) => {
          if (res.data && res.data.code === 200) {
            const data = res.data.data;
            this.form = {
              id: data.id,
              title: data.title,
              content: data.content,
              status: data.status, // 1-已发布, 0-草稿
              type: data.type,
              isTop: data.isTop === 1,
              imageUrl: data.imageUrl || ''
            };
            } else {
            uni.showToast({
              title: '获取公告详情失败',
              icon: 'none'
            });
          }
        },
        fail: () => {
          uni.showToast({
            title: '获取公告详情失败',
            icon: 'none'
          });
        },
        complete: () => {
          uni.hideLoading();
        }
      });
    },
    
    // 保存公告
    async saveAnnouncement() {
      if (!this.form.title.trim()) {
        uni.showToast({
          title: '请输入公告标题',
          icon: 'none'
        });
        return;
      }
      
      if (!this.form.content.trim()) {
        uni.showToast({
          title: '请输入公告内容',
          icon: 'none'
        });
        return;
      }
      
      if (this.isLoading) return;
      this.isLoading = true;
      
      uni.showLoading({ title: '保存中...' });
      
      const url = this.form.id 
        ? `${this.$baseUrl}/api/announcements/${this.form.id}` 
        : `${this.$baseUrl}/api/announcements`;
      
      const method = this.form.id ? 'PUT' : 'POST';
      
      const requestData = {
        ...this.form,
        isTop: this.form.isTop ? 1 : 0
      };
      
      try {
        const [err, res] = await uni.request({
          url,
          method,
          header: {
            'Authorization': uni.getStorageSync('token'),
            'Content-Type': 'application/json'
          },
          data: requestData
        });
        
        if (!err && res.data && res.data.code === 200) {
          uni.showToast({
            title: '保存成功',
            icon: 'success'
          });
          
          setTimeout(() => {
            uni.navigateBack();
          }, 1500);
        } else {
          uni.showToast({
            title: res.data?.message || '保存失败',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error('保存公告错误:', error);
        uni.showToast({
          title: '保存失败',
          icon: 'none'
        });
      } finally {
        uni.hideLoading();
        this.isLoading = false;
      }
    },
    
    // 上传图片
    uploadImage() {
      uni.chooseImage({
        count: 1,
        success: (res) => {
          const tempFilePath = res.tempFilePaths[0];
          
          uni.showLoading({ title: '上传中...' });
          
          uni.uploadFile({
            url: this.$baseUrl + '/api/upload',
            filePath: tempFilePath,
            name: 'file',
            header: {
              'Authorization': uni.getStorageSync('token')
            },
            success: (uploadRes) => {
              const data = JSON.parse(uploadRes.data);
              if (data.code === 200) {
                this.form.imageUrl = data.data.url;
              } else {
                uni.showToast({
                  title: '上传失败',
                  icon: 'none'
                });
              }
            },
            fail: () => {
              uni.showToast({
                title: '上传失败',
                icon: 'none'
              });
            },
            complete: () => {
              uni.hideLoading();
            }
          });
        }
      });
    },
    
    // 切换置顶状态
    toggleTopStatus() {
      this.form.isTop = !this.form.isTop;
    },
    
    // 选择类型
    selectType(type) {
      this.form.type = type;
      this.showTypeSelector = false;
    },
    
    // 获取类型名称
    getTypeName(value) {
      const type = this.typeOptions.find(item => item.value === value);
      return type ? type.label : '招生公告';
    },
    
    // 返回
    goBack() {
      uni.navigateBack();
    }
  }
}
</script>

<style lang="scss">
@import '../../styles/common.scss';

.doc-edit-container {
  min-height: 100vh;
  background-color: $gray-100;
  padding: 20rpx;
}

.doc-edit-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
  
  &__title {
    font-size: $font-size-large;
    font-weight: 500;
    color: $gray-900;
    display: flex;
    align-items: center;
  }
  
  &__icon {
    display: inline-block;
    width: 8rpx;
    height: 36rpx;
    background-color: $primary-color;
    border-radius: $radius-small;
    margin-right: 16rpx;
  }
}

.doc-edit-actions {
  display: flex;
  gap: 20rpx;
  
  .btn-text {
    margin-left: 8rpx;
  }
}

.doc-edit-form {
  background-color: $white;
  border-radius: $radius-large;
  padding: 30rpx;
  box-shadow: $shadow-light;
}

.form-group {
  margin-bottom: 30rpx;
  
  &--full {
    margin-bottom: 0;
  }
}

.form-label {
  font-size: $font-size-base;
  color: $gray-700;
  margin-bottom: 12rpx;
  display: flex;
  align-items: center;
  
  &__required {
    color: $error-color;
    margin-right: 4rpx;
  }
}

.form-input {
  width: 100%;
  height: 80rpx;
  background-color: $gray-100;
  border-radius: $radius-medium;
  padding: 0 24rpx;
  font-size: $font-size-base;
  color: $gray-800;
  border: 1px solid $gray-200;
  transition: $transition-base;
  
  &:focus {
    background-color: $white;
    border-color: $primary-color;
    box-shadow: $shadow-primary;
  }
}

.form-textarea {
  width: 100%;
  height: 400rpx;
  background-color: $gray-100;
  border-radius: $radius-medium;
  padding: 24rpx;
  font-size: $font-size-base;
  color: $gray-800;
  border: 1px solid $gray-200;
  transition: $transition-base;
  
  &:focus {
    background-color: $white;
    border-color: $primary-color;
    box-shadow: $shadow-primary;
  }
}

.form-radio-group {
  display: flex;
  gap: 40rpx;
}

.form-radio-item {
  display: flex;
  align-items: center;
  gap: 12rpx;
  color: $gray-600;
  font-size: $font-size-base;
  transition: $transition-base;
  
  &--active {
    color: $primary-color;
  }
}

.form-radio {
  width: 36rpx;
  height: 36rpx;
  border-radius: $radius-circle;
  border: 2rpx solid $gray-400;
  position: relative;
  transition: $transition-base;
  
  .form-radio-item--active & {
    border-color: $primary-color;
    
    &::after {
      content: '';
      position: absolute;
      width: 20rpx;
      height: 20rpx;
      border-radius: $radius-circle;
      background-color: $primary-color;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
    }
  }
}

.form-switch {
  display: inline-block;
  
  &__track {
    width: 80rpx;
    height: 40rpx;
    border-radius: 100rpx;
    background-color: $gray-300;
    position: relative;
    transition: $transition-base;
    
    &--active {
      background-color: $primary-color;
    }
  }
  
  &__thumb {
    position: absolute;
    width: 32rpx;
    height: 32rpx;
    border-radius: $radius-circle;
    background-color: $white;
    top: 4rpx;
    left: 4rpx;
    transition: $transition-base;
    box-shadow: 0 2rpx 4rpx rgba(0,0,0,0.1);
    
    &--active {
      left: calc(100% - 36rpx);
    }
  }
}

.form-select {
  height: 80rpx;
  background-color: $gray-100;
  border-radius: $radius-medium;
  padding: 0 24rpx;
  font-size: $font-size-base;
  color: $gray-800;
  border: 1px solid $gray-200;
  display: flex;
  align-items: center;
  justify-content: space-between;
  transition: $transition-base;
  
  &:active {
    background-color: $white;
    border-color: $primary-color;
  }
}

.form-upload {
  &__add {
    width: 200rpx;
    height: 200rpx;
    background-color: $gray-100;
    border: 1px dashed $gray-300;
    border-radius: $radius-medium;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 12rpx;
    color: $gray-500;
    font-size: $font-size-small;
    transition: $transition-base;
    
    &:active {
      background-color: $gray-200;
    }
  }
  
  &__preview {
    width: 300rpx;
    height: 200rpx;
    border-radius: $radius-medium;
    object-fit: cover;
    border: 1px solid $gray-200;
  }
}

// 类型选择器弹窗
.type-selector {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 100;
  
  &__mask {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0,0,0,0.5);
    animation: fadeIn 0.3s;
  }
  
  &__content {
    position: absolute;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: $white;
    border-radius: $radius-large $radius-large 0 0;
    animation: slideUp 0.3s;
  }
  
  &__header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30rpx;
    border-bottom: 1rpx solid $gray-200;
    font-size: $font-size-medium;
    font-weight: 500;
    color: $gray-900;
  }
  
  &__close {
    font-size: $font-size-xlarge;
    color: $gray-600;
    padding: 0 20rpx;
  }
  
  &__body {
    padding: 0 30rpx;
    max-height: 600rpx;
    overflow-y: auto;
  }
  
  &__item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30rpx 0;
    border-bottom: 1rpx solid $gray-200;
    font-size: $font-size-base;
    color: $gray-700;
    transition: $transition-base;
    
    &--active {
      color: $primary-color;
    }
    
    &:active {
      background-color: $gray-100;
    }
  }
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideUp {
  from { transform: translateY(100%); }
  to { transform: translateY(0); }
}
</style> 