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
      total: 0
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
    
    async loadMajorsByCollege(collegeId) {
      this.loading = true;
      try {
        const [err, res] = await uni.request({
          url: `http://localhost:8080/api/major-info/college/${collegeId}`,
          method: 'GET',
          header: {
            'Authorization': uni.getStorageSync('token')
          }
        });
        
        if (!err && res.statusCode === 200) {
          this.majors = res.data || [];
          this.total = this.majors.length;
        } else {
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
    
    onCollegeFilterChange(e) {
      this.selectedCollegeIndex = parseInt(e.detail.value);
      const selectedCollege = this.colleges[this.selectedCollegeIndex];
      this.selectedCollegeId = selectedCollege ? selectedCollege.id : null;
      
      if (this.selectedCollegeId) {
        this.loadMajorsByCollege(this.selectedCollegeId);
      } else {
        this.majors = [];
      }
    },
    
    handleAddMajor() {
      uni.navigateTo({
        url: '/pages/admin/major/edit?type=add' + (this.selectedCollegeId ? `&collegeId=${this.selectedCollegeId}` : '')
      })
    },
    
    handleEdit(major) {
      uni.navigateTo({
        url: `/pages/admin/major/edit?id=${major.id}&type=edit`
      })
    },
    
    navigateToDetail(major) {
      console.log('跳转到专业编辑页:', major.id, major.name);
      
      try {
        uni.navigateTo({
          url: `/pages/admin/major/edit?id=${major.id}&type=edit`,
          success: () => {
            console.log('导航到专业编辑成功');
          },
          fail: (err) => {
            console.error('导航到专业编辑失败:', err);
            // 使用全局定义的导航方法
            this.$navigateTo(`/pages/admin/major/edit?id=${major.id}&type=edit`);
          }
        });
      } catch (e) {
        console.error('导航到专业编辑异常:', e);
        uni.showToast({
          title: '页面跳转异常',
          icon: 'none'
        });
      }
    },
    
    handleDeleteMajor(major) {
      uni.showModal({
        title: '确认删除',
        content: `确定要删除 ${major.name} 专业信息吗？此操作不可恢复！`,
        success: async (res) => {
          if (res.confirm) {
            try {
              const [err, res] = await uni.request({
                url: `http://localhost:8080/api/major-info/${major.id}`,
                method: 'DELETE',
                header: {
                  'Authorization': uni.getStorageSync('token')
                }
              });
              
              if (!err && res.statusCode === 200) {
                uni.showToast({
                  title: '删除成功',
                  icon: 'success'
                });
                
                // 重新加载专业列表
                const targetCollegeId = this.collegeId || this.selectedCollegeId;
                this.loadMajorsByCollege(targetCollegeId);
              } else {
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
  padding-bottom: 30rpx;
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
  padding: 30rpx;
  background: #fff;
  box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.05);
  margin-bottom: 2rpx;
  
  .picker {
    background-color: #f6f8fa;
    border-radius: 12rpx;
    height: 76rpx;
    padding: 0 24rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 28rpx;
    color: #333;
    border: 2rpx solid transparent;
    transition: all 0.3s;
    
    &:active {
      background-color: #eef6ff;
      border-color: #4aa3ff;
    }
    
    .iconfont {
      font-size: 24rpx;
      color: #999;
      transition: transform 0.3s;
    }
    
    &:active .iconfont {
      transform: rotate(180deg);
    }
  }
}

.action-bar {
  display: flex;
  align-items: center;
  padding: 30rpx;
  background: #fff;
  box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.05);
  margin-bottom: 20rpx;
  
  .add-btn {
    background: linear-gradient(135deg, #4aa3ff 0%, #3a93f0 100%);
    color: #fff;
    font-size: 28rpx;
    border-radius: 35rpx;
    height: 70rpx;
    line-height: 70rpx;
    padding: 0 40rpx;
    border: none;
    margin-right: 20rpx;
    flex-shrink: 0;
    transition: all 0.3s;
    box-shadow: 0 4rpx 12rpx rgba(74, 163, 255, 0.2);
    
    &:active {
      transform: translateY(2rpx);
      box-shadow: 0 2rpx 6rpx rgba(74, 163, 255, 0.15);
    }
    
    &:disabled {
      background: #cccccc;
      box-shadow: none;
    }
  }
  
  .search-box {
    flex: 1;
    display: flex;
    align-items: center;
    background-color: #f6f8fa;
    border-radius: 35rpx;
    padding: 0 30rpx;
    height: 70rpx;
    position: relative;
    transition: all 0.3s;
    box-shadow: inset 0 2rpx 6rpx rgba(0,0,0,0.03);
    border: 2rpx solid transparent;
    
    &:focus-within {
      background-color: #fff;
      border-color: #4aa3ff;
      box-shadow: 0 0 0 3rpx rgba(74, 163, 255, 0.1);
    }
    
    input {
      flex: 1;
      height: 100%;
      font-size: 28rpx;
      padding-right: 60rpx;
    }
    
    .iconfont {
      position: absolute;
      right: 30rpx;
      font-size: 32rpx;
      color: #999;
      transition: color 0.3s;
    }
    
    &:focus-within .iconfont {
      color: #4aa3ff;
    }
  }
}

.major-list {
  flex: 1;
  padding: 0 30rpx;
}

.loading, .empty {
  text-align: center;
  color: #999;
  padding: 100rpx 0;
  font-size: 28rpx;
}

.major-item {
  background-color: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  padding: 30rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.05);
  transition: transform 0.3s, box-shadow 0.3s;
  
  &:active {
    transform: translateY(-2rpx);
    box-shadow: 0 8rpx 24rpx rgba(0,0,0,0.08);
  }
  
  .major-info {
    display: flex;
    align-items: center;
    flex: 1;
    cursor: pointer;
    
    .major-icon {
      width: 80rpx;
      height: 80rpx;
      border-radius: 16rpx;
      margin-right: 24rpx;
      background-color: #f6f8fa;
      box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.08);
      object-fit: cover;
    }
    
    .major-detail {
      .major-name {
        font-size: 32rpx;
        font-weight: 600;
        color: #333;
        margin-bottom: 10rpx;
        display: block;
      }
      
      .major-meta {
        font-size: 26rpx;
        color: #999;
      }
    }
  }
  
  .major-actions {
    display: flex;
    
    .action-btn {
      font-size: 24rpx;
      padding: 10rpx 20rpx;
      border-radius: 30rpx;
      margin-left: 16rpx;
      cursor: pointer;
      transition: all 0.3s;
      
      &.edit {
        background-color: #e6f7ff;
        color: #4aa3ff;
        
        &:active {
          background-color: #d0edff;
          transform: translateY(2rpx);
        }
      }
      
      &.delete {
        background-color: #fff2f0;
        color: #ff4d4f;
        
        &:active {
          background-color: #ffded9;
          transform: translateY(2rpx);
        }
      }
    }
  }
}

.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  background: #fff;
  margin: 20rpx 30rpx 0;
  border-radius: 16rpx;
  box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.05);
  
  .page-info {
    font-size: 26rpx;
    color: #666;
  }
  
  .page-btns {
    display: flex;
    align-items: center;
    gap: 20rpx;
    
    button {
      font-size: 26rpx;
      padding: 10rpx 30rpx;
      border-radius: 30rpx;
      background: linear-gradient(135deg, #4aa3ff 0%, #3a93f0 100%);
      color: #fff;
      transition: all 0.3s;
      box-shadow: 0 4rpx 12rpx rgba(74, 163, 255, 0.2);
      
      &:active {
        transform: translateY(2rpx);
        box-shadow: 0 2rpx 6rpx rgba(74, 163, 255, 0.15);
      }
      
      &:disabled {
        background: #ccc;
        box-shadow: none;
      }
    }
    
    .page-num {
      font-size: 26rpx;
      color: #666;
      min-width: 100rpx;
      text-align: center;
    }
  }
}
</style> 