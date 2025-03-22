<template>
  <view class="container">
    <view class="header">
      <view class="back-button" @tap="navBack">
        <text class="iconfont icon-left"></text>
      </view>
      <text class="title">{{ collegeName ? collegeName + '专业管理' : '专业管理' }}</text>
    </view>
    
    <view class="filter-bar" v-if="!collegeId">
      <picker 
        @change="onCollegeFilterChange" 
        :value="selectedCollegeIndex" 
        :range="collegeNames"
      >
        <view class="picker">
          <text>{{ selectedCollegeIndex >= 0 ? collegeNames[selectedCollegeIndex] : '选择学院' }}</text>
          <text class="iconfont icon-down"></text>
        </view>
      </picker>
    </view>
    
    <view class="action-bar">
      <button class="add-btn" @tap="handleAddMajor" :disabled="!selectedCollegeId && !collegeId">新增专业</button>
      <view class="search-box">
        <input type="text" v-model="searchKeyword" placeholder="搜索专业名称" />
        <text class="iconfont icon-search"></text>
      </view>
    </view>
    
    <view class="major-list">
      <view class="loading" v-if="loading">加载中...</view>
      <view class="empty" v-else-if="!selectedCollegeId && !collegeId">请选择学院查看专业</view>
      <view class="empty" v-else-if="majors.length === 0">该学院暂无专业数据</view>
      <view class="major-item" v-for="(major, index) in filteredMajors" :key="major.id">
        <view class="major-info" @tap="navigateToDetail(major)">
          <image class="major-icon" :src="major.iconUrl || '/static/images/major-icon.png'" mode="aspectFit"></image>
          <view class="major-detail">
            <text class="major-name">{{ major.name }}</text>
            <text class="major-meta">{{ major.code || '无专业代码' }} · {{ major.degree || '未设置' }} · {{ major.duration || '未设置' }}年</text>
          </view>
        </view>
        <view class="major-actions">
          <text class="action-btn edit" @tap="navigateToDetail(major)">编辑</text>
          <text class="action-btn delete" @tap="handleDeleteMajor(major)">删除</text>
        </view>
      </view>
    </view>
    
    <!-- 分页 -->
    <view class="pagination" v-if="majors.length > 0">
      <text class="page-info">共 {{total}} 条</text>
      <view class="page-btns">
        <button 
          :disabled="currentPage === 1" 
          @tap="handlePageChange(currentPage - 1)"
        >上一页</button>
        <text class="page-num">第 {{currentPage}} 页</text>
        <button 
          :disabled="currentPage === totalPages" 
          @tap="handlePageChange(currentPage + 1)"
        >下一页</button>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      loading: false,
      colleges: [],
      majors: [],
      searchKeyword: '',
      collegeId: null, // 从URL参数获取，如果有，则不显示学院选择器
      collegeName: '', // 从URL参数获取，用于显示在标题
      selectedCollegeId: null, // 通过选择器选择的学院ID
      selectedCollegeIndex: -1,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      showDeleteConfirm: false,
      majorToDelete: null
    }
  },
  computed: {
    filteredMajors() {
      if (!this.searchKeyword) return this.majors;
      return this.majors.filter(major => 
        major.name.toLowerCase().includes(this.searchKeyword.toLowerCase()) ||
        (major.code && major.code.toLowerCase().includes(this.searchKeyword.toLowerCase()))
      );
    },
    collegeNames() {
      return this.colleges.map(college => college.name);
    },
    totalPages() {
      return Math.ceil(this.total / this.pageSize);
    }
  },
  onLoad(options) {
    this.checkAuth();
    
    // 如果有学院ID参数，则直接加载该学院的专业
    if (options.collegeId) {
      this.collegeId = options.collegeId;
      this.collegeName = options.collegeName ? decodeURIComponent(options.collegeName) : '';
      this.loadMajorsByCollege(this.collegeId);
    } else {
      this.loadColleges();
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
        const token = uni.getStorageSync('token');
        if (!token) {
          uni.showToast({
            title: '未登录或token已过期',
            icon: 'none'
          });
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
    
    onCollegeFilterChange(e) {
      this.selectedCollegeIndex = e.detail.value;
      this.selectedCollegeId = this.colleges[this.selectedCollegeIndex].id;
      this.collegeName = this.colleges[this.selectedCollegeIndex].name;
      this.loadMajorsByCollege(this.selectedCollegeId);
    },
    
    async loadMajorsByCollege(collegeId) {
      this.loading = true;
      try {
        const token = uni.getStorageSync('token');
        if (!token) {
          uni.showToast({
            title: '未登录或token已过期',
            icon: 'none'
          });
          return;
        }

        const [err, res] = await uni.request({
          url: `http://localhost:8080/api/major-info/college/${collegeId}`,
          method: 'GET',
          header: {
            'Authorization': token
          }
        });
        
        if (!err && res.statusCode === 200) {
          this.majors = res.data || [];
          this.total = this.majors.length;
          console.log('加载专业数据成功:', this.majors.length);
        } else {
          console.error('获取专业列表失败:', err, res);
          uni.showToast({
            title: '获取专业列表失败',
            icon: 'none'
          });
        }
      } catch (e) {
        console.error('获取专业列表异常:', e);
        uni.showToast({
          title: '获取专业列表异常',
          icon: 'none'
        });
      } finally {
        this.loading = false;
      }
    },
    
    handleAddMajor() {
      const collegeId = this.collegeId || this.selectedCollegeId;
      if (!collegeId) {
        uni.showToast({
          title: '请先选择学院',
          icon: 'none'
        });
        return;
      }
      
      uni.navigateTo({
        url: `/pages/admin/major/edit?collegeId=${collegeId}&type=add`,
        fail: (err) => {
          console.error('导航到专业编辑失败:', err);
          uni.showToast({
            title: '页面跳转失败',
            icon: 'none'
          });
        }
      });
    },
    
    navigateToDetail(major) {
      uni.navigateTo({
        url: `/pages/admin/major/edit?id=${major.id}&type=edit`,
        fail: (err) => {
          console.error('导航到专业编辑失败:', err);
          uni.showToast({
            title: '页面跳转失败',
            icon: 'none'
          });
        }
      });
    },
    
    handleDeleteMajor(major) {
      this.majorToDelete = major;
      uni.showModal({
        title: '确认删除',
        content: `确定要删除专业 ${major.name} 吗？此操作不可恢复！`,
        success: async (res) => {
          if (res.confirm) {
            this.confirmDelete();
          }
        }
      });
    },
    
    async confirmDelete() {
      if (!this.majorToDelete) return;
      
      try {
        const token = uni.getStorageSync('token');
        if (!token) {
          uni.showToast({
            title: '未登录或token已过期',
            icon: 'none'
          });
          return;
        }

        const [err, res] = await uni.request({
          url: `http://localhost:8080/api/major-info/${this.majorToDelete.id}`,
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
          // 重新加载专业列表
          if (this.collegeId) {
            this.loadMajorsByCollege(this.collegeId);
          } else if (this.selectedCollegeId) {
            this.loadMajorsByCollege(this.selectedCollegeId);
          }
        } else {
          console.error('删除专业失败:', err, res);
          uni.showToast({
            title: '删除失败',
            icon: 'none'
          });
        }
      } catch (e) {
        console.error('删除专业异常:', e);
        uni.showToast({
          title: '删除异常',
          icon: 'none'
        });
      } finally {
        this.majorToDelete = null;
      }
    },
    
    cancelDelete() {
      this.majorToDelete = null;
      this.showDeleteConfirm = false;
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

.filter-bar {
  margin: 20rpx 30rpx;
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.05);
  
  .picker {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 80rpx;
    padding: 0 20rpx;
    background-color: #f6f8fa;
    border-radius: 8rpx;
  }
}

.action-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 20rpx 30rpx;
  
  .add-btn {
    background: linear-gradient(135deg, #4aa3ff 0%, #3a93f0 100%);
    color: #fff;
    border-radius: 8rpx;
    padding: 12rpx 30rpx;
    font-size: 28rpx;
    box-shadow: 0 2rpx 8rpx rgba(74, 163, 255, 0.3);
    
    &:disabled {
      background: #ccc;
      box-shadow: none;
    }
  }
  
  .search-box {
    flex: 1;
    max-width: 400rpx;
    margin-left: 20rpx;
    position: relative;
    display: flex;
    align-items: center;
    background-color: #fff;
    border-radius: 8rpx;
    padding: 0 20rpx;
    box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.05);
    
    input {
      flex: 1;
      height: 70rpx;
      padding-right: 60rpx;
    }
    
    .icon-search {
      position: absolute;
      right: 20rpx;
      font-size: 32rpx;
      color: #999;
    }
  }
}

.major-list {
  flex: 1;
  padding: 0 30rpx;
  
  .loading, .empty {
    text-align: center;
    padding: 100rpx 0;
    color: #999;
    font-size: 28rpx;
  }
  
  .major-item {
    background-color: #fff;
    border-radius: 12rpx;
    padding: 24rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.05);
    transition: box-shadow 0.3s;
    
    &:hover {
      box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.08);
    }
    
    .major-info {
      display: flex;
      align-items: center;
      padding-bottom: 20rpx;
      
      .major-icon {
        width: 80rpx;
        height: 80rpx;
        margin-right: 20rpx;
        border-radius: 8rpx;
      }
      
      .major-detail {
        flex: 1;
        
        .major-name {
          font-size: 32rpx;
          font-weight: 500;
          margin-bottom: 8rpx;
          color: #333;
        }
        
        .major-meta {
          font-size: 24rpx;
          color: #666;
        }
      }
    }
    
    .major-actions {
      display: flex;
      justify-content: flex-end;
      border-top: 1px solid #eee;
      padding-top: 20rpx;
      
      .action-btn {
        margin-left: 20rpx;
        padding: 8rpx 20rpx;
        border-radius: 6rpx;
        font-size: 26rpx;
        
        &.edit {
          background-color: #f0f9ff;
          color: #4aa3ff;
        }
        
        &.delete {
          background-color: #fff0f0;
          color: #ff6b6b;
        }
      }
    }
  }
}

.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 30rpx;
  background-color: #fff;
  
  .page-info {
    font-size: 24rpx;
    color: #666;
  }
  
  .page-btns {
    display: flex;
    align-items: center;
    
    button {
      background-color: #f5f7fa;
      border: 1px solid #e0e0e0;
      color: #666;
      font-size: 24rpx;
      padding: 0 20rpx;
      margin: 0 10rpx;
      height: 60rpx;
      line-height: 60rpx;
      
      &:disabled {
        color: #ccc;
        background-color: #f8f8f8;
      }
    }
    
    .page-num {
      margin: 0 20rpx;
      font-size: 24rpx;
      color: #666;
    }
  }
}

.confirm-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
  
  .modal-content {
    width: 600rpx;
    background-color: #fff;
    border-radius: 12rpx;
    overflow: hidden;
    
    .modal-header {
      padding: 30rpx;
      text-align: center;
      border-bottom: 1px solid #eee;
      
      .modal-title {
        font-size: 32rpx;
        font-weight: 500;
      }
    }
    
    .modal-body {
      padding: 40rpx 30rpx;
      
      .modal-message {
        font-size: 28rpx;
        line-height: 1.5;
        color: #333;
      }
    }
    
    .modal-footer {
      display: flex;
      border-top: 1px solid #eee;
      
      .modal-btn {
        flex: 1;
        text-align: center;
        padding: 24rpx 0;
        font-size: 30rpx;
        
        &.cancel-btn {
          color: #666;
          background-color: #f5f7fa;
        }
        
        &.confirm-btn {
          color: #ff6b6b;
          background-color: #fff0f0;
        }
      }
    }
  }
}
</style> 