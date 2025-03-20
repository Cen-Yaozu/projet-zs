<template>
  <view class="container">
    <view class="nav-header">
      <view class="back-button" @click="navBack">
        <text class="iconfont icon-left"></text>
      </view>
      <text class="nav-title">院系专业</text>
      <view class="right-icons">
        <text class="iconfont icon-more"></text>
        <text class="iconfont icon-minus"></text>
        <text class="iconfont icon-round"></text>
      </view>
    </view>
    
    <!-- 学院头部信息 -->
    <view class="college-header">
      <view class="college-logo">
        <image :src="collegeInfo.logoUrl || '/static/images/college-logo.png'" mode="aspectFit"></image>
        <!-- Logo上传按钮（仅管理员可见） -->
        <view class="upload-btn" v-if="isAdmin" @click="uploadLogo">
          <text class="iconfont icon-camera"></text>
        </view>
      </view>
      <view class="college-basic-info">
        <view class="college-name">{{ collegeInfo.name }}</view>
        <view class="college-meta">
          <text class="meta-item">{{ collegeInfo.degree || '本科' }}</text>
          <text class="meta-divider">·</text>
          <text class="meta-item">{{ collegeInfo.duration || '4' }}</text>
        </view>
      </view>
    </view>
    
    <!-- 加载中提示 -->
    <view class="loading-container" v-if="loading">
      <view class="loading"></view>
      <text class="loading-text">加载中...</text>
    </view>
    
    <!-- 学院详情内容 -->
    <scroll-view scroll-y class="college-detail-content" v-else>
      <!-- 学院宣传图和介绍 -->
      <view class="college-brochure">
        <view class="banner-container">
          <image class="college-banner" :src="collegeInfo.bannerUrl || '/static/images/college-banner.jpg'" mode="widthFix"></image>
          <!-- 横幅上传按钮（仅管理员可见） -->
          <view class="upload-banner-btn" v-if="isAdmin" @click="uploadBanner">
            <text class="iconfont icon-camera"></text>
            <text class="upload-text">更换横幅</text>
          </view>
        </view>
        
        <!-- 专业介绍 -->
        <view class="major-section" v-for="(major, index) in majors" :key="index">
          <view class="major-header">
            <view class="major-title-box">
              <image class="college-icon" :src="major.iconUrl || '/static/images/college-icon.png'" mode="aspectFit"></image>
              <text class="major-title">{{ major.name }}专业</text>
            </view>
            <!-- 专业图标上传按钮（仅管理员可见） -->
            <view class="upload-icon-btn" v-if="isAdmin" @click="uploadMajorIcon(major)">
              <text class="iconfont icon-camera"></text>
            </view>
          </view>
          
          <view class="major-content">
            <!-- 专业介绍项目 -->
            <view class="intro-item">
              <view class="intro-title">• 专业介绍</view>
              <view class="intro-text">{{ major.description }}</view>
            </view>
            
            <!-- 培养目标 -->
            <view class="intro-item">
              <view class="intro-title">• 培养目标</view>
              <view class="intro-text">{{ major.trainingObjectives || '培养具有扎实的专业基础知识、实践能力和创新精神的高素质专业人才。' }}</view>
            </view>
            
            <!-- 主要课程 -->
            <view class="intro-item">
              <view class="intro-title">• 主要课程</view>
              <view class="intro-text">{{ major.mainCourses || '专业基础课程、专业核心课程、专业选修课程等。' }}</view>
            </view>
            
            <!-- 就业方向 -->
            <view class="intro-item">
              <view class="intro-title">• 就业方向</view>
              <view class="intro-text">{{ major.careerProspects }}</view>
            </view>
            
            <!-- 专业图片展示 -->
            <view class="major-images-container">
              <view class="major-images" v-if="major.images && major.images.length > 0">
                <image class="major-image" v-for="(img, imgIndex) in major.images" 
                      :key="imgIndex" 
                      :src="img" 
                      mode="aspectFill"></image>
              </view>
              <view class="major-images" v-else>
                <image class="major-image" src="/static/images/major-image1.jpg" mode="aspectFill"></image>
                <image class="major-image" src="/static/images/major-image2.jpg" mode="aspectFill"></image>
                <image class="major-image" src="/static/images/major-image3.jpg" mode="aspectFill"></image>
              </view>
              
              <!-- 专业图片上传按钮（仅管理员可见） -->
              <view class="upload-major-img-btn" v-if="isAdmin" @click="uploadMajorImages(major)">
                <text class="iconfont icon-plus"></text>
                <text class="upload-text">添加图片</text>
              </view>
            </view>
          </view>
        </view>
      </view>
      
      <!-- 其他学院列表 -->
      <view class="other-colleges">
        <view class="other-title">其他学院</view>
        <view class="other-list">
          <view class="other-item" v-for="(college, index) in otherColleges" :key="index" @click="navigateToCollege(college)">
            <text>{{ college.name }}</text>
          </view>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      collegeId: null,
      collegeName: '',
      loading: true,
      collegeInfo: {},
      majors: [],
      otherColleges: [],
      isAdmin: false, // 是否为管理员
      token: '', // 用户令牌
    }
  },
  onLoad(options) {
    console.log('收到参数:', options);
    if (options.id) {
      this.collegeId = options.id;
      this.collegeName = decodeURIComponent(options.name || '学院详情');
      console.log('设置collegeId:', this.collegeId, '类型:', typeof this.collegeId);
      this.loadCollegeDetail();
      this.loadOtherColleges();
      this.checkUserRole();
    } else {
      uni.showToast({
        title: '参数错误',
        icon: 'none'
      });
      setTimeout(() => {
        uni.navigateBack();
      }, 1500);
    }
  },
  methods: {
    navBack() {
      uni.navigateBack();
    },
    
    // 检查用户角色
    checkUserRole() {
      try {
        const userInfo = uni.getStorageSync('userInfo');
        if (userInfo) {
          const user = JSON.parse(userInfo);
          this.isAdmin = user.role === 'ROLE_ADMIN';
          this.token = user.token;
        }
      } catch (e) {
        console.error('获取用户信息失败:', e);
      }
    },
    
    loadCollegeDetail() {
      this.loading = true;
      console.log('正在请求学院信息, ID:', this.collegeId);
      
      // 获取学院信息
      uni.request({
        url: `http://localhost:8080/api/college/${this.collegeId}`,
        method: 'GET',
        success: (res) => {
          console.log('学院信息API响应:', res.data);
          if (res.data) {
            this.collegeInfo = res.data;
          } else {
            // 如果获取失败，使用默认信息
            this.collegeInfo = this.getDefaultCollegeInfo();
            uni.showToast({
              title: '获取学院信息失败',
              icon: 'none'
            });
          }
          
          // 获取该学院下的专业列表
          this.loadMajorsByCollegeId();
        },
        fail: (err) => {
          console.error('获取学院信息失败:', err);
          // 如果接口请求失败，使用默认信息
          this.collegeInfo = this.getDefaultCollegeInfo();
          
          // 使用默认专业数据
          this.loadDefaultMajors();
          
          this.loading = false;
          
          uni.showToast({
            title: '网络异常，请稍后重试',
            icon: 'none'
          });
        }
      });
    },
    
    // 获取其他学院列表
    loadOtherColleges() {
      uni.request({
        url: 'http://localhost:8080/api/college/list',
        method: 'GET',
        success: (res) => {
          if (res.data && res.data.length > 0) {
            // 过滤掉当前学院
            this.otherColleges = res.data.filter(college => college.id != this.collegeId);
          } else {
            this.otherColleges = this.getDefaultOtherColleges();
          }
        },
        fail: () => {
          this.otherColleges = this.getDefaultOtherColleges();
        }
      });
    },
    
    // 导航到其他学院详情
    navigateToCollege(college) {
      uni.navigateTo({
        url: `/pages/school/college-detail?id=${college.id}&name=${encodeURIComponent(college.name)}`
      });
    },
    
    // 获取该学院下的专业列表
    loadMajorsByCollegeId() {
      console.log('正在请求专业列表, 学院ID:', this.collegeId);
      uni.request({
        url: `http://localhost:8080/api/major-info/college/${this.collegeId}`,
        method: 'GET',
        success: (res) => {
          console.log('专业列表API响应:', res.data);
          if (res.data && res.data.length > 0) {
            this.majors = res.data;
          } else {
            this.loadDefaultMajors();
          }
        },
        fail: (err) => {
          console.error('获取专业列表失败:', err);
          this.loadDefaultMajors();
        },
        complete: () => {
          this.loading = false;
        }
      });
    },
    
    // 上传学院Logo
    uploadLogo() {
      if (!this.isAdmin) {
        uni.showToast({
          title: '需要管理员权限',
          icon: 'none'
        });
        return;
      }
      
      uni.chooseImage({
        count: 1, // 只能选择一张图片
        sizeType: ['compressed'], // 压缩图片
        sourceType: ['album', 'camera'], // 可以从相册选择或拍照
        success: (res) => {
          const tempFilePaths = res.tempFilePaths;
          
          uni.showLoading({
            title: '正在上传...'
          });
          
          uni.uploadFile({
            url: `http://localhost:8080/api/upload`,
            filePath: tempFilePaths[0],
            name: 'file',
            formData: {
              directory: `college/logo`
            },
            header: {
              'Authorization': 'Bearer ' + this.token
            },
            success: (uploadRes) => {
              try {
                const resultData = JSON.parse(uploadRes.data);
                if (resultData.code === 200) {
                  this.collegeInfo.logoUrl = resultData.data.url;
                  uni.showToast({
                    title: 'Logo上传成功',
                    icon: 'success'
                  });
                } else {
                  uni.showToast({
                    title: resultData.message || 'Logo上传失败',
                    icon: 'none'
                  });
                }
              } catch (e) {
                uni.showToast({
                  title: 'Logo上传失败',
                  icon: 'none'
                });
              }
            },
            fail: () => {
              uni.showToast({
                title: '网络错误，请稍后重试',
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
    
    // 上传学院横幅
    uploadBanner() {
      if (!this.isAdmin) {
        uni.showToast({
          title: '需要管理员权限',
          icon: 'none'
        });
        return;
      }
      
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          const tempFilePaths = res.tempFilePaths;
          
          uni.showLoading({
            title: '正在上传...'
          });
          
          uni.uploadFile({
            url: `http://localhost:8080/api/upload`,
            filePath: tempFilePaths[0],
            name: 'file',
            formData: {
              directory: `college/banner`
            },
            header: {
              'Authorization': 'Bearer ' + this.token
            },
            success: (uploadRes) => {
              try {
                const resultData = JSON.parse(uploadRes.data);
                if (resultData.code === 200) {
                  this.collegeInfo.bannerUrl = resultData.data.url;
                  uni.showToast({
                    title: '横幅上传成功',
                    icon: 'success'
                  });
                } else {
                  uni.showToast({
                    title: resultData.message || '横幅上传失败',
                    icon: 'none'
                  });
                }
              } catch (e) {
                uni.showToast({
                  title: '横幅上传失败',
                  icon: 'none'
                });
              }
            },
            fail: () => {
              uni.showToast({
                title: '网络错误，请稍后重试',
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
    
    // 上传专业图标
    uploadMajorIcon(major) {
      if (!this.isAdmin) {
        uni.showToast({
          title: '需要管理员权限',
          icon: 'none'
        });
        return;
      }
      
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          const tempFilePaths = res.tempFilePaths;
          
          uni.showLoading({
            title: '正在上传...'
          });
          
          uni.uploadFile({
            url: `http://localhost:8080/api/upload`,
            filePath: tempFilePaths[0],
            name: 'file',
            formData: {
              directory: `major/icon`
            },
            header: {
              'Authorization': 'Bearer ' + this.token
            },
            success: (uploadRes) => {
              try {
                const resultData = JSON.parse(uploadRes.data);
                if (resultData.code === 200) {
                  major.iconUrl = resultData.data.url;
                  uni.showToast({
                    title: '图标上传成功',
                    icon: 'success'
                  });
                } else {
                  uni.showToast({
                    title: resultData.message || '图标上传失败',
                    icon: 'none'
                  });
                }
              } catch (e) {
                uni.showToast({
                  title: '图标上传失败',
                  icon: 'none'
                });
              }
            },
            fail: () => {
              uni.showToast({
                title: '网络错误，请稍后重试',
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
    
    // 上传专业图片
    uploadMajorImages(major) {
      if (!this.isAdmin) {
        uni.showToast({
          title: '需要管理员权限',
          icon: 'none'
        });
        return;
      }
      
      uni.chooseImage({
        count: 3, // 最多可以选择3张图片
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          const tempFilePaths = res.tempFilePaths;
          
          uni.showLoading({
            title: '正在上传...'
          });
          
          // 创建表单数据
          const formData = {};
          
          // 创建上传任务
          let uploadCount = 0;
          let successCount = 0;
          
          tempFilePaths.forEach((path, index) => {
            uni.uploadFile({
              url: `http://localhost:8080/api/upload`,
              filePath: path,
              name: 'file',
              formData: {
                directory: `major/images`
              },
              header: {
                'Authorization': 'Bearer ' + this.token
              },
              success: (uploadRes) => {
                try {
                  const resultData = JSON.parse(uploadRes.data);
                  if (resultData.code === 200) {
                    successCount++;
                    if (!major.images) {
                      major.images = [];
                    }
                    // 更新专业图片列表，添加新上传的图片URL
                    major.images.push(resultData.data.url);
                  }
                } catch (e) {
                  console.error('解析上传结果失败:', e);
                }
              },
              complete: () => {
                uploadCount++;
                if (uploadCount === tempFilePaths.length) {
                  uni.hideLoading();
                  uni.showToast({
                    title: `成功上传${successCount}张图片`,
                    icon: 'success'
                  });
                }
              }
            });
          });
        }
      });
    },
    
    // 获取默认学院信息
    getDefaultCollegeInfo() {
      let collegeInfo = {};
      
      // 根据collegeId获取对应的默认学院信息
      switch (this.collegeId) {
        case '1':
          collegeInfo = {
            id: 1,
            name: '航运学院',
            degree: '本科',
            duration: '4',
            description: '航运学院致力于培养航运领域的高素质人才，拥有完善的教学设施和优秀的师资力量。',
            logoUrl: '/static/images/college/navigation-logo.png',
            bannerUrl: '/static/images/college/navigation.jpg'
          };
          break;
        case '2':
          collegeInfo = {
            id: 2,
            name: '海洋装备工程学院',
            degree: '本科',
            duration: '4',
            description: '海洋装备工程学院专注于海洋工程装备的研发与应用，培养海洋工程领域的专业人才。',
            logoUrl: '/static/images/college/marine-logo.png',
            bannerUrl: '/static/images/college/marine.jpg'
          };
          break;
        // 其他学院默认信息...
        default:
          collegeInfo = {
            id: this.collegeId,
            name: this.collegeName,
            degree: '本科',
            duration: '4',
            description: '本学院致力于培养高素质、应用型专业人才，注重理论与实践相结合。',
            logoUrl: '/static/images/college/default-logo.png',
            bannerUrl: '/static/images/college/default.jpg'
          };
      }
      
      return collegeInfo;
    },
    
    // 获取默认其他学院列表
    getDefaultOtherColleges() {
      return [
        { id: 2, name: '海洋装备工程学院' },
        { id: 3, name: '低空装备与智能控制学院' },
        { id: 4, name: '智能制造学院' },
        { id: 5, name: '智能交通与工程学院(未来交通学院)' },
        { id: 6, name: '海事法律与交通管理学院' },
        { id: 7, name: '数字经济与贸易学院' },
        { id: 8, name: '计算机学院（人工智能学院）' },
        { id: 9, name: '数字媒体与交互设计学院' },
        { id: 10, name: '外国语学院' },
        { id: 11, name: '国际交流学院（中外合作项目）' }
      ].filter(item => item.id != this.collegeId);
    },
    
    // 加载默认专业数据
    loadDefaultMajors() {
      // 根据不同学院获取默认专业数据
      switch (this.collegeId) {
        case '1': // 航运学院
          this.majors = [
            {
              name: '航海技术',
              description: '航海技术专业是培养具备船舶驾驶、海上运输组织与管理等能力的高级航海人才的专业。学生将学习航海理论、船舶结构、航海气象、海洋环境等相关知识，掌握现代船舶驾驶技术和船舶管理技能。',
              trainingObjectives: '培养适应我国航运事业发展需要的，具有良好的职业道德和敬业精神，掌握航海技术的基本理论和船舶驾驶技能，能够从事船舶驾驶、海上运输组织与管理工作的高级应用型专门人才。',
              mainCourses: '船舶结构与设备、航海学、航海气象学、船舶操纵与避碰、海洋环境保护、航行仪器、航运经济与管理、GMDSS通信、轮机基础等。',
              careerProspects: '毕业生可在航运公司、港口、海事管理等单位从事船舶驾驶、运输管理等工作。也可在航运企事业单位、港航管理部门、海事局等就业。',
              iconUrl: '/static/images/major/navtech-icon.png',
              images: [
                '/static/images/major/navtech-1.jpg',
                '/static/images/major/navtech-2.jpg',
                '/static/images/major/navtech-3.jpg'
              ]
            },
            {
              name: '轮机工程',
              description: '轮机工程专业培养具备船舶动力装置操作、维护与管理等能力的专业人才。学生将学习船舶动力系统、电气自动化、热工系统等相关知识，掌握船舶机械设备的操作与维护技能。',
              trainingObjectives: '培养具有轮机工程领域的基础理论知识和专业技能，能够从事船舶机械设备操作、维护与管理等工作的高级工程技术人才。',
              mainCourses: '船舶柴油机、船舶辅机、船舶电气设备、船舶自动化系统、制冷与空调、船舶动力装置、船舶机械设备管理等。',
              careerProspects: '毕业生可在航运公司、船厂、海事管理等单位从事船舶轮机管理等工作，也可在船舶检验、设计、制造单位和海事管理部门就业。',
              iconUrl: '/static/images/major/marine-eng-icon.png',
              images: [
                '/static/images/major/marine-eng-1.jpg',
                '/static/images/major/marine-eng-2.jpg',
                '/static/images/major/marine-eng-3.jpg'
              ]
            }
          ];
          break;
        // 其他专业默认数据...
        default:
          this.majors = [];
      }
    }
  }
}
</script>

<style lang="scss">
.container {
  min-height: 100vh;
  background-color: #f5f5f5;
  display: flex;
  flex-direction: column;
}

.nav-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #3a86ff;
  color: #fff;
  padding: 20rpx 30rpx;
  position: relative;
  height: 80rpx;
  
  .back-button {
    font-size: 40rpx;
    width: 60rpx;
  }
  
  .nav-title {
    font-size: 34rpx;
    font-weight: 500;
    text-align: center;
    flex: 1;
  }
  
  .right-icons {
    display: flex;
    justify-content: flex-end;
    width: 160rpx;
    
    .iconfont {
      margin-left: 30rpx;
      font-size: 36rpx;
    }
  }
}

.college-header {
  background-color: #eef7ff;
  padding: 30rpx;
  display: flex;
  align-items: center;
  
  .college-logo {
    width: 120rpx;
    height: 120rpx;
    margin-right: 30rpx;
    position: relative;
    
    image {
      width: 100%;
      height: 100%;
    }
    
    .upload-btn {
      position: absolute;
      right: -10rpx;
      bottom: -10rpx;
      width: 50rpx;
      height: 50rpx;
      background-color: rgba(58, 134, 255, 0.8);
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      color: white;
      font-size: 24rpx;
    }
  }
  
  .college-basic-info {
    flex: 1;
    
    .college-name {
      font-size: 36rpx;
      font-weight: bold;
      color: #333;
      margin-bottom: 10rpx;
    }
    
    .college-meta {
      display: flex;
      align-items: center;
      
      .meta-item {
        font-size: 28rpx;
        color: #3a86ff;
        font-weight: 500;
      }
      
      .meta-divider {
        margin: 0 10rpx;
        color: #3a86ff;
      }
    }
  }
}

.loading-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
  
  .loading {
    width: 60rpx;
    height: 60rpx;
    border: 6rpx solid #f3f3f3;
    border-top: 6rpx solid #3399cc;
    border-radius: 50%;
    animation: spin 1s linear infinite;
  }
  
  .loading-text {
    color: #999;
    font-size: 28rpx;
    margin-top: 20rpx;
  }
  
  @keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
  }
}

.college-detail-content {
  flex: 1;
  
  .college-brochure {
    background-color: #fff;
    
    .banner-container {
      position: relative;
      
      .college-banner {
        width: 100%;
      }
      
      .upload-banner-btn {
        position: absolute;
        right: 20rpx;
        bottom: 20rpx;
        background-color: rgba(0, 0, 0, 0.6);
        color: white;
        padding: 10rpx 20rpx;
        border-radius: 30rpx;
        display: flex;
        align-items: center;
        font-size: 24rpx;
        
        .iconfont {
          margin-right: 10rpx;
        }
      }
    }
    
    .major-section {
      padding-bottom: 20rpx;
      
      .major-header {
        padding: 20rpx 30rpx;
        background-color: #3a86ff;
        display: flex;
        justify-content: space-between;
        align-items: center;
        
        .major-title-box {
          display: flex;
          align-items: center;
          
          .college-icon {
            width: 40rpx;
            height: 40rpx;
            margin-right: 15rpx;
          }
          
          .major-title {
            color: #fff;
            font-size: 32rpx;
            font-weight: 500;
          }
        }
        
        .upload-icon-btn {
          width: 40rpx;
          height: 40rpx;
          background-color: rgba(255, 255, 255, 0.3);
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          color: white;
          font-size: 22rpx;
        }
      }
      
      .major-content {
        padding: 20rpx 30rpx;
        
        .intro-item {
          margin-bottom: 20rpx;
          
          .intro-title {
            font-size: 28rpx;
            color: #333;
            font-weight: bold;
            margin-bottom: 10rpx;
          }
          
          .intro-text {
            font-size: 26rpx;
            color: #666;
            line-height: 1.6;
            text-align: justify;
          }
        }
        
        .major-images-container {
          position: relative;
          
          .major-images {
            display: flex;
            flex-wrap: wrap;
            margin-top: 15rpx;
            justify-content: space-between;
            
            .major-image {
              width: 31%;
              height: 180rpx;
              margin-bottom: 10rpx;
              border-radius: 6rpx;
            }
          }
          
          .upload-major-img-btn {
            margin-top: 10rpx;
            padding: 15rpx;
            background-color: #f5f5f5;
            border-radius: 6rpx;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #3a86ff;
            font-size: 26rpx;
            
            .iconfont {
              margin-right: 10rpx;
            }
          }
        }
      }
    }
  }
  
  .other-colleges {
    margin-top: 20rpx;
    background-color: #fff;
    padding: 20rpx 30rpx;
    
    .other-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
      margin-bottom: 20rpx;
    }
    
    .other-list {
      .other-item {
        padding: 16rpx 0;
        border-bottom: 1rpx solid #f0f0f0;
        font-size: 28rpx;
        color: #333;
        
        &:last-child {
          border-bottom: none;
        }
      }
    }
  }
}
</style> 