<template>
  <view class="container">
    <view class="header">
      <view class="back-button" @tap="navBack">
        <text class="iconfont icon-left"></text>
      </view>
      <text class="title">学院管理</text>
    </view>
    
    <view class="content">
      <view class="filter-bar">
        <view class="search-box">
          <text class="iconfont icon-search"></text>
          <input type="text" v-model="searchKeyword" placeholder="搜索学院名称" class="input" />
          <text v-if="searchKeyword" class="clear-icon iconfont icon-close" @tap="searchKeyword = ''"></text>
        </view>
        <button class="add-button" @tap="handleAddCollege">
          <text class="add-icon iconfont icon-add"></text>
          <text>新增学院</text>
        </button>
      </view>
      
      <view class="college-list">
        <view class="empty-tip" v-if="!loading && colleges.length === 0">
          <text class="empty-icon iconfont icon-empty"></text>
          <view>暂无学院数据</view>
        </view>
        <view class="college-item" v-for="(college, index) in filteredColleges" :key="college.id">
          <view class="college-info" @tap="navigateToDetail(college)">
            <image class="college-logo" :src="college.logoUrl || '/static/images/college-logo.png'" mode="aspectFit"></image>
            <view class="info-content">
              <view class="college-name">{{ college.name }}</view>
              <view class="college-meta">
                <view class="meta-item">
                  <text class="meta-icon iconfont icon-education"></text>
                  <text>{{ college.degree || '未设置' }}</text>
                </view>
                <view class="meta-item">
                  <text class="meta-icon iconfont icon-time"></text>
                  <text>{{ college.duration || '未设置' }}年</text>
                </view>
              </view>
              <view class="college-intro" v-if="college.introduction">{{ college.introduction }}</view>
            </view>
          </view>
          <view class="action-row">
            <button class="action-button edit-btn" @tap="navigateToDetail(college)">编辑</button>
            <button class="action-button view-btn" @tap="navigateToMajors(college)">专业</button>
            <button class="action-button delete-btn" @tap="handleDeleteCollege(college)">删除</button>
          </view>
        </view>
      </view>

      <!-- 分页 -->
      <view class="pagination" v-if="colleges.length > 0">
        <button 
          class="page-btn prev-btn" :class="{ disabled: currentPage === 1 }"
          @tap="handlePageChange(currentPage - 1)"
        >上一页</button>
        <button 
          v-for="page in Math.min(5, totalPages)" 
          :key="page"
          class="page-btn" :class="{ active: page === currentPage }"
          @tap="handlePageChange(page)"
        >{{ page }}</button>
        <button 
          class="page-btn next-btn" :class="{ disabled: currentPage === totalPages }"
          @tap="handlePageChange(currentPage + 1)"
        >下一页</button>
        <text class="page-text">共 {{total}} 条</text>
      </view>
    </view>
    
    <!-- 加载中遮罩 -->
    <view class="loading-overlay" v-if="loading">
      <view class="loading-content">
        <text class="loading-text">加载中...</text>
      </view>
    </view>
    
    <!-- 确认删除弹窗 -->
    <view class="confirm-modal" v-if="showDeleteConfirm">
      <view class="modal-content">
        <view class="modal-header">
          <text class="modal-title">确认删除</text>
        </view>
        <view class="modal-body">
          <text class="modal-message">确定要删除 {{ collegeToDelete.name }} 及其所有专业信息吗？此操作不可恢复！</text>
        </view>
        <view class="modal-footer">
          <text class="modal-btn cancel-btn" @tap="cancelDelete">取消</text>
          <text class="modal-btn confirm-btn" @tap="confirmDelete">删除</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  components: {
    // 如果页面使用了uni-popup组件，需要确保正确导入
  },
  data() {
    return {
      loading: false,
      colleges: [],
      searchKeyword: '',
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  computed: {
    filteredColleges() {
      if (!this.searchKeyword) return this.colleges;
      return this.colleges.filter(college => 
        college.name.toLowerCase().includes(this.searchKeyword.toLowerCase())
      );
    },
    totalPages() {
      return Math.ceil(this.total / this.pageSize);
    }
  },
  onShow() {
    this.checkAuth();
    this.loadColleges();
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
      this.loading = true;
      try {
        // 获取token
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
          url: 'http://localhost:8080/api/college/list',
          method: 'GET',
          header: {
            'Authorization': token
          }
        });
        
        if (!err && res.statusCode === 200) {
          this.colleges = res.data || [];
          this.total = this.colleges.length;
          console.log('加载学院数据成功:', this.colleges.length);
        } else {
          console.error('获取学院列表失败:', err, res);
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
      } finally {
        this.loading = false;
      }
    },
    
    handleAddCollege() {
      uni.navigateTo({
        url: '/pages/admin/college/edit?type=add'
      });
    },
    
    navigateToDetail(college) {
      console.log('跳转到学院编辑页:', college.id, college.name);
      
      try {
        uni.navigateTo({
          url: `/pages/admin/college/edit?id=${college.id}&type=edit`,
          fail: (err) => {
            console.error('导航到学院编辑失败:', err);
            uni.showToast({
              title: '页面跳转失败',
              icon: 'none'
            });
          }
        });
      } catch (e) {
        console.error('导航到学院编辑异常:', e);
      }
    },
    
    navigateToMajors(college) {
      console.log('跳转到专业管理页:', college.id, college.name);
      
      try {
        uni.navigateTo({
          url: `/pages/admin/major-manage?collegeId=${college.id}&collegeName=${encodeURIComponent(college.name)}`,
          fail: (err) => {
            console.error('导航到专业管理失败:', err);
            uni.showToast({
              title: '页面跳转失败',
              icon: 'none'
            });
          }
        });
      } catch (e) {
        console.error('导航到专业管理异常:', e);
      }
    },
    
    handleDeleteCollege(college) {
      uni.showModal({
        title: '确认删除',
        content: `确定要删除 ${college.name} 及其所有专业信息吗？此操作不可恢复！`,
        success: async (res) => {
          if (res.confirm) {
            try {
              // 获取token
              const token = uni.getStorageSync('token');
              if (!token) {
                uni.showToast({
                  title: '未登录或token已过期',
                  icon: 'none'
                });
                return;
              }

              const [err, res] = await uni.request({
                url: `http://localhost:8080/api/college/${college.id}`,
                method: 'DELETE',
                header: {
                  'Authorization': token
                }
              });
              
              if (!err && res.statusCode === 200) {
                uni.showToast({
                  title: '删除成功',
                  icon: 'success'
                });
                this.loadColleges();
              } else {
                console.error('删除学院失败:', err, res);
                uni.showToast({
                  title: '删除失败',
                  icon: 'none'
                });
              }
            } catch (e) {
              console.error('删除学院异常:', e);
              uni.showToast({
                title: '删除异常',
                icon: 'none'
              });
            }
          }
        }
      });
    },
    
    handlePageChange(page) {
      this.currentPage = page;
      // 实现分页逻辑
      // 目前后端没有分页API，这里只是UI展示
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
  
  .filter-bar {
    display: flex;
    align-items: center;
    margin-bottom: 30rpx;
    background-color: #fff;
    padding: 20rpx;
    border-radius: 16rpx;
    box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.05);
    transition: box-shadow 0.3s;
    
    &:hover {
      box-shadow: 0 6rpx 20rpx rgba(0,0,0,0.08);
    }
    
    .search-box {
      flex: 1;
      position: relative;
      display: flex;
      align-items: center;
      background-color: #f6f8fa;
      border-radius: 12rpx;
      padding: 0 20rpx;
      height: 80rpx;
      margin-right: 20rpx;
      
      .search-icon {
        font-size: 32rpx;
        color: #999;
      }
      
      .input {
        flex: 1;
        height: 80rpx;
        padding: 0 20rpx;
        font-size: 28rpx;
        color: #333;
        border: none;
        background: transparent;
      }
      
      .clear-icon {
        font-size: 32rpx;
        color: #999;
        transition: transform 0.3s;
        
        &:active {
          transform: scale(1.2);
        }
      }
    }
    
    .add-button {
      height: 80rpx;
      line-height: 80rpx;
      background: linear-gradient(135deg, #4aa3ff 0%, #3a93f0 100%);
      color: #fff;
      font-size: 28rpx;
      border-radius: 40rpx;
      padding: 0 40rpx;
      display: flex;
      align-items: center;
      box-shadow: 0 4rpx 16rpx rgba(74, 163, 255, 0.3);
      transition: all 0.3s;
      flex-shrink: 0;
      
      &:active {
        transform: translateY(2rpx);
        box-shadow: 0 2rpx 8rpx rgba(74, 163, 255, 0.2);
      }
      
      .add-icon {
        font-size: 32rpx;
        margin-right: 10rpx;
      }
    }
  }
  
  .college-list {
    .college-item {
      background-color: #fff;
      border-radius: 16rpx;
      padding: 30rpx;
      margin-bottom: 30rpx;
      box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.05);
      transition: transform 0.3s, box-shadow 0.3s;
      
      &:hover {
        transform: translateY(-2rpx);
        box-shadow: 0 8rpx 24rpx rgba(0,0,0,0.08);
      }
      
      .college-info {
        display: flex;
        
        .college-logo {
          width: 120rpx;
          height: 120rpx;
          border-radius: 16rpx;
          background-color: #f6f8fa;
          margin-right: 20rpx;
          object-fit: cover;
          box-shadow: 0 2rpx 10rpx rgba(0,0,0,0.08);
        }
        
        .info-content {
          flex: 1;
          
          .college-name {
            font-size: 32rpx;
            font-weight: 600;
            color: #333;
            margin-bottom: 15rpx;
          }
          
          .college-meta {
            display: flex;
            align-items: center;
            flex-wrap: wrap;
            
            .meta-item {
              display: flex;
              align-items: center;
              margin-right: 30rpx;
              margin-bottom: 10rpx;
              font-size: 26rpx;
              color: #666;
              
              .meta-icon {
                font-size: 26rpx;
                color: #999;
                margin-right: 8rpx;
              }
            }
          }
          
          .college-intro {
            font-size: 26rpx;
            color: #666;
            margin-top: 15rpx;
            line-height: 1.6;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            text-overflow: ellipsis;
          }
        }
      }
      
      .action-row {
        display: flex;
        justify-content: flex-end;
        margin-top: 20rpx;
        padding-top: 20rpx;
        border-top: 1rpx solid #f0f0f0;
        
        .action-button {
          height: 60rpx;
          line-height: 60rpx;
          padding: 0 30rpx;
          font-size: 26rpx;
          border-radius: 30rpx;
          margin-left: 20rpx;
          transition: all 0.3s;
          
          &.view-btn {
            background-color: #e6f7ff;
            color: #4aa3ff;
            box-shadow: 0 2rpx 8rpx rgba(74, 163, 255, 0.15);
            
            &:active {
              transform: translateY(2rpx);
              box-shadow: 0 1rpx 3rpx rgba(74, 163, 255, 0.1);
            }
          }
          
          &.edit-btn {
            background-color: #e6f7ff;
            color: #4aa3ff;
            box-shadow: 0 2rpx 8rpx rgba(74, 163, 255, 0.15);
            
            &:active {
              transform: translateY(2rpx);
              box-shadow: 0 1rpx 3rpx rgba(74, 163, 255, 0.1);
            }
          }
          
          &.delete-btn {
            background-color: #fff0f0;
            color: #ff4d4f;
            box-shadow: 0 2rpx 8rpx rgba(255, 77, 79, 0.15);
            
            &:active {
              transform: translateY(2rpx);
              box-shadow: 0 1rpx 3rpx rgba(255, 77, 79, 0.1);
            }
          }
        }
      }
    }
  }
  
  .empty-tip {
    padding: 100rpx 0;
    text-align: center;
    font-size: 28rpx;
    color: #999;
    
    .empty-icon {
      font-size: 96rpx;
      color: #ddd;
      margin-bottom: 20rpx;
    }
  }
  
  .pagination {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 20rpx 0;
    
    .page-btn {
      width: 70rpx;
      height: 70rpx;
      display: flex;
      justify-content: center;
      align-items: center;
      font-size: 28rpx;
      color: #666;
      margin: 0 10rpx;
      border-radius: 8rpx;
      background-color: #fff;
      box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.05);
      transition: all 0.3s;
      
      &:active {
        transform: translateY(2rpx);
        box-shadow: 0 1rpx 3rpx rgba(0,0,0,0.03);
      }
      
      &.prev-btn, &.next-btn {
        width: 120rpx;
      }
      
      &.active {
        background: linear-gradient(135deg, #4aa3ff 0%, #3a93f0 100%);
        color: #fff;
        font-weight: 500;
        box-shadow: 0 4rpx 16rpx rgba(74, 163, 255, 0.2);
      }
      
      &.disabled {
        opacity: 0.5;
        pointer-events: none;
      }
    }
    
    .page-text {
      margin: 0 20rpx;
      font-size: 26rpx;
      color: #999;
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

.confirm-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  
  .modal-content {
    width: 600rpx;
    background-color: #fff;
    border-radius: 16rpx;
    overflow: hidden;
    box-shadow: 0 12rpx 36rpx rgba(0, 0, 0, 0.2);
    transform: scale(1);
    animation: modal-in 0.3s;
    
    @keyframes modal-in {
      0% {
        transform: scale(0.8);
        opacity: 0;
      }
      100% {
        transform: scale(1);
        opacity: 1;
      }
    }
    
    .modal-header {
      padding: 30rpx;
      
      .modal-title {
        font-size: 32rpx;
        font-weight: 600;
        color: #333;
      }
    }
    
    .modal-body {
      padding: 0 30rpx 30rpx;
      
      .modal-message {
        font-size: 28rpx;
        color: #666;
        line-height: 1.6;
      }
    }
    
    .modal-footer {
      display: flex;
      border-top: 1rpx solid #f0f0f0;
      
      .modal-btn {
        flex: 1;
        height: 100rpx;
        line-height: 100rpx;
        text-align: center;
        font-size: 30rpx;
        transition: background-color 0.3s;
        
        &.cancel-btn {
          color: #666;
          
          &:active {
            background-color: #f6f8fa;
          }
        }
        
        &.confirm-btn {
          color: #ff4d4f;
          font-weight: 500;
          
          &:active {
            background-color: #fff0f0;
          }
        }
      }
    }
  }
}
</style> 