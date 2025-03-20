<template>
  <view class="container">
    <view class="header">
      <view class="back-button" @tap="navBack">
        <text class="iconfont icon-left"></text>
      </view>
      <text class="title">{{ isEdit ? '编辑专业' : '添加专业' }}</text>
    </view>
    
    <view class="loading-overlay" v-if="loading">
      <view class="loading-content">
        <text class="loading-text">加载中...</text>
      </view>
    </view>
    
    <view class="form-container">
      <scroll-view scroll-y class="scroll-view">
        <view class="form-section">
          <view class="section-title">基本信息</view>
          
          <view class="form-item">
            <text class="label">专业名称 <text class="required">*</text></text>
            <input 
              type="text" 
              v-model="majorForm.name" 
              placeholder="请输入专业名称"
              class="input"
            />
          </view>
          
          <view class="form-item">
            <text class="label">专业代码</text>
            <input 
              type="text" 
              v-model="majorForm.code" 
              placeholder="请输入专业代码"
              class="input"
            />
          </view>
          
          <view class="form-item">
            <text class="label">所属学院 <text class="required">*</text></text>
            <picker
              @change="handleCollegeChange"
              :value="collegeIndex"
              :range="collegeNames"
              :disabled="collegeIdFromParams"
            >
              <view class="picker-value">
                {{ collegeNames[collegeIndex] || '请选择所属学院' }}
                <text v-if="!collegeIdFromParams" class="iconfont icon-down"></text>
              </view>
            </picker>
          </view>
          
          <view class="form-item">
            <text class="label">学位类型</text>
            <picker
              @change="handleDegreeChange"
              :value="degreeIndex"
              :range="degreeOptions"
            >
              <view class="picker-value">
                {{ degreeOptions[degreeIndex] || '请选择学位类型' }}
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
        </view>
        
        <view class="form-section">
          <view class="section-title">专业介绍</view>
          
          <view class="form-item">
            <text class="label">专业简介</text>
            <textarea 
              v-model="majorForm.description" 
              placeholder="请输入专业简介"
              maxlength="1000"
              class="textarea"
            ></textarea>
            <text class="word-count">{{ majorForm.description.length }}/1000</text>
          </view>
          
          <view class="form-item">
            <text class="label">培养目标</text>
            <textarea 
              v-model="majorForm.trainingObjectives" 
              placeholder="请输入培养目标"
              maxlength="1000"
              class="textarea"
            ></textarea>
            <text class="word-count">{{ majorForm.trainingObjectives.length }}/1000</text>
          </view>
          
          <view class="form-item">
            <text class="label">主要课程</text>
            <textarea 
              v-model="majorForm.mainCourses" 
              placeholder="请输入主要课程"
              maxlength="1000"
              class="textarea"
            ></textarea>
            <text class="word-count">{{ majorForm.mainCourses.length }}/1000</text>
          </view>
          
          <view class="form-item">
            <text class="label">就业前景</text>
            <textarea 
              v-model="majorForm.careerProspects" 
              placeholder="请输入就业前景"
              maxlength="1000"
              class="textarea"
            ></textarea>
            <text class="word-count">{{ majorForm.careerProspects.length }}/1000</text>
          </view>
        </view>
        
        <view class="form-section">
          <view class="section-title">媒体资源</view>
          
          <view class="form-item">
            <text class="label">专业图标</text>
            <view class="upload-area">
              <image 
                v-if="majorForm.iconUrl" 
                :src="majorForm.iconUrl" 
                class="preview-image"
                mode="aspectFit"
                @tap="previewImage(majorForm.iconUrl)"
              ></image>
              <view v-else class="upload-placeholder" @tap="uploadIcon">
                <text class="iconfont icon-upload"></text>
                <text class="upload-text">上传图标</text>
              </view>
              <button class="upload-btn" @tap="uploadIcon">{{ majorForm.iconUrl ? '更换' : '上传' }}</button>
            </view>
            <text class="hint">推荐尺寸：200x200px，格式：JPG/PNG</text>
          </view>
          
          <view class="form-item">
            <text class="label">专业图片集</text>
            <view class="images-list">
              <view 
                class="image-item" 
                v-for="(image, index) in majorForm.images" 
                :key="index"
              >
                <image 
                  :src="image" 
                  class="preview-image"
                  mode="aspectFill"
                  @tap="previewImage(image)"
                ></image>
                <text class="delete-btn" @tap="deleteImage(index)">×</text>
              </view>
              
              <view 
                class="image-item add-image" 
                @tap="uploadImage"
                v-if="majorForm.images.length < 5"
              >
                <text class="iconfont icon-add"></text>
                <text class="add-text">添加图片</text>
              </view>
            </view>
            <text class="hint">最多添加5张图片，推荐尺寸：1080x720px，格式：JPG/PNG</text>
          </view>
        </view>
      </scroll-view>
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
      majorId: null,
      collegeIdFromParams: false, // 标记是否从URL参数获取学院ID
      majorForm: {
        id: null,
        collegeId: null,
        name: '',
        code: '',
        degree: '学士',
        duration: '4',
        description: '',
        trainingObjectives: '',
        mainCourses: '',
        careerProspects: '',
        iconUrl: '',
        images: []
      },
      colleges: [],
      collegeIndex: -1,
      degreeOptions: ['专科', '学士', '硕士', '博士'],
      degreeIndex: 1,
      durationOptions: ['2', '3', '4', '5'],
      durationIndex: 2,
      loading: false
    }
  },
  computed: {
    collegeNames() {
      return this.colleges.map(college => college.name);
    }
  },
  onLoad(options) {
    this.checkAuth();
    this.loadColleges();
    
    if (options.type === 'edit' && options.id) {
      this.isEdit = true;
      this.majorId = options.id;
      this.loadMajorDetail(options.id);
    } else if (options.type === 'add' && options.collegeId) {
      this.majorForm.collegeId = options.collegeId;
      this.collegeIdFromParams = true;
      // 加载完学院列表后设置选中的学院
      this.$nextTick(() => {
        this.setCollegeByIdAfterLoad(options.collegeId);
      });
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
    
    async loadColleges() {
      try {
        const [err, res] = await uni.request({
          url: 'http://localhost:8080/api/college/list',
          method: 'GET',
          header: {
            'Authorization': uni.getStorageSync('token')
          }
        });
        
        if (!err && res.statusCode === 200) {
          this.colleges = res.data || [];
          
          // 如果是从URL参数获取学院ID，需要设置对应的索引
          if (this.collegeIdFromParams && this.majorForm.collegeId) {
            this.setCollegeByIdAfterLoad(this.majorForm.collegeId);
          }
        } else {
          uni.showToast({
            title: '获取学院列表失败',
            icon: 'none'
          });
        }
      } catch (e) {
        console.error('获取学院列表异常:', e);
        uni.showToast({
          title: '获取学院列表异常',
          icon: 'none'
        });
      }
    },
    
    setCollegeByIdAfterLoad(collegeId) {
      const index = this.colleges.findIndex(college => college.id == collegeId);
      if (index >= 0) {
        this.collegeIndex = index;
      }
    },
    
    async loadMajorDetail(id) {
      this.loading = true;
      try {
        console.log('开始加载专业详情，ID:', id);
        const token = uni.getStorageSync('token');
        if (!token) {
          uni.showToast({
            title: '未登录或token已过期',
            icon: 'none'
          });
          setTimeout(() => {
            uni.navigateTo({
              url: '/pages/my/login'
            });
          }, 1500);
          return;
        }

        const [err, res] = await uni.request({
          url: `http://localhost:8080/api/major-info/${id}`,
          method: 'GET',
          header: {
            'Authorization': token
          }
        });
        
        console.log('专业详情API响应:', err, res);
        
        if (!err && res.statusCode === 200 && res.data) {
          // 处理images字段，确保是数组
          let majorData = res.data;
          
          // 初始化空字段，防止null导致的异常
          majorData.description = majorData.description || '';
          majorData.trainingObjectives = majorData.trainingObjectives || '';
          majorData.mainCourses = majorData.mainCourses || '';
          majorData.careerProspects = majorData.careerProspects || '';
          
          if (typeof majorData.images === 'string') {
            try {
              majorData.images = JSON.parse(majorData.images);
            } catch (e) {
              majorData.images = [];
            }
          }
          
          if (!Array.isArray(majorData.images)) {
            majorData.images = [];
          }
          
          this.majorForm = majorData;
          
          // 设置学院选择器索引
          const collegeIndex = this.colleges.findIndex(c => c.id == majorData.collegeId);
          this.collegeIndex = collegeIndex >= 0 ? collegeIndex : -1;
          
          // 设置学位类型选择器索引
          const degreeIndex = this.degreeOptions.findIndex(d => d === majorData.degree);
          this.degreeIndex = degreeIndex >= 0 ? degreeIndex : 1;
          
          // 设置学制选择器索引
          const durationIndex = this.durationOptions.findIndex(d => d === majorData.duration);
          this.durationIndex = durationIndex >= 0 ? durationIndex : 2;
          
          console.log('专业详情加载完成:', this.majorForm);
        } else {
          console.error('获取专业信息失败:', err, res);
          uni.showToast({
            title: '获取专业信息失败',
            icon: 'none'
          });
          setTimeout(() => {
            uni.navigateBack();
          }, 1500);
        }
      } catch (e) {
        console.error('获取专业信息异常:', e);
        uni.showToast({
          title: '获取专业信息异常',
          icon: 'none'
        });
      } finally {
        this.loading = false;
      }
    },
    
    handleCollegeChange(e) {
      this.collegeIndex = e.detail.value;
      this.majorForm.collegeId = this.colleges[this.collegeIndex].id;
    },
    
    handleDegreeChange(e) {
      this.degreeIndex = e.detail.value;
      this.majorForm.degree = this.degreeOptions[this.degreeIndex];
    },
    
    handleDurationChange(e) {
      this.durationIndex = e.detail.value;
      this.majorForm.duration = this.durationOptions[this.durationIndex];
    },
    
    uploadIcon() {
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          this.uploadFile(res.tempFilePaths[0], 'icon');
        }
      });
    },
    
    uploadImage() {
      if (this.majorForm.images.length >= 5) {
        uni.showToast({
          title: '最多上传5张图片',
          icon: 'none'
        });
        return;
      }
      
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          this.uploadFile(res.tempFilePaths[0], 'image');
        }
      });
    },
    
    deleteImage(index) {
      uni.showModal({
        title: '确认删除',
        content: '确定要删除此图片吗？',
        success: (res) => {
          if (res.confirm) {
            this.majorForm.images.splice(index, 1);
          }
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
          
          if (type === 'icon') {
            this.majorForm.iconUrl = fileUrl;
          } else if (type === 'image') {
            this.majorForm.images.push(fileUrl);
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
          if (type === 'icon') {
            this.majorForm.iconUrl = data.url;
          } else if (type === 'image') {
            this.majorForm.images.push(data.url);
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
      if (!this.majorForm.name) {
        uni.showToast({
          title: '请输入专业名称',
          icon: 'none'
        });
        return;
      }
      
      if (!this.majorForm.collegeId) {
        uni.showToast({
          title: '请选择所属学院',
          icon: 'none'
        });
        return;
      }
      
      this.loading = true;
      try {
        // 处理images字段，转为JSON字符串
        const formData = { ...this.majorForm };
        formData.images = JSON.stringify(formData.images);
        
        const url = this.isEdit 
          ? 'http://localhost:8080/api/major-info/update'
          : 'http://localhost:8080/api/major-info/create';
        
        const method = this.isEdit ? 'PUT' : 'POST';
        
        const [err, res] = await uni.request({
          url,
          method,
          data: formData,
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
        console.error(this.isEdit ? '更新专业异常:' : '添加专业异常:', e);
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