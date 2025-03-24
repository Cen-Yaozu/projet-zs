<template>
  <view class="container">
    <view class="header">
      <view class="back-button" @tap="navBack">
        <text class="iconfont icon-left"></text>
      </view>
      <text class="title">公告管理</text>
    </view>
    
    <view class="content">
      <view class="filter-bar">
        <view class="search-box">
          <text class="iconfont icon-search"></text>
          <input 
            type="text" 
            v-model="searchKeyword"
            placeholder="搜索公告标题"
            class="input"
          />
          <text v-if="searchKeyword" class="clear-icon iconfont icon-close" @tap="searchKeyword = ''"></text>
        </view>
        <button class="add-button" @tap="showAddModal">
          <text class="add-icon iconfont icon-add"></text>
          <text>新增公告</text>
        </button>
      </view>

      <view class="announcement-list">
        <view class="empty-tip" v-if="!loading && announcements.length === 0">
          <text class="empty-icon iconfont icon-empty"></text>
          <view>暂无公告数据</view>
        </view>
        <view class="announcement-item" v-for="item in announcements" :key="item.id">
          <view class="announce-info">
            <view class="announce-header">
              <view class="title">{{item.title}}</view>
              <view class="status-tag" :class="item.status ? 'active' : 'inactive'">
                {{item.status ? '已发布' : '草稿'}}
              </view>
            </view>
            <view class="announce-meta">
              <view class="meta-item">
                <text class="meta-icon iconfont icon-time"></text>
                <text>{{formatDate(item.createTime)}}</text>
              </view>
              <view class="meta-item">
                <text class="meta-icon iconfont icon-type"></text>
                <text>{{getTypeText(item.type)}}</text>
              </view>
            </view>
            <view class="announce-content" v-if="item.content">{{item.content}}</view>
          </view>
          <view class="action-row">
            <button class="action-button edit-btn" @tap="handleEdit(item)">编辑</button>
            <button 
              class="action-button publish-btn" 
              v-if="!item.status"
              @tap="handlePublish(item)"
            >发布</button>
            <button 
              class="action-button unpublish-btn" 
              v-else
              @tap="handleUnpublish(item)"
            >撤回</button>
            <button class="action-button delete-btn" @tap="handleDelete(item)">删除</button>
          </view>
        </view>
      </view>

      <!-- 分页 -->
      <view class="pagination" v-if="announcements.length > 0">
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
  </view>
</template>

<script>
export default {
  data() {
    return {
      loading: false,
      searchKeyword: '',
      announcements: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      typeOptions: ['通知公告', '招生信息', '新闻动态'],
      typeValues: ['NOTICE', 'ADMISSION', 'NEWS']
    }
  },
  computed: {
    totalPages() {
      return Math.ceil(this.total / this.pageSize)
    }
  },
  onShow() {
    this.loadAnnouncements()
  },
  methods: {
    navBack() {
      uni.navigateBack();
    },
    
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
    },
    
    getTypeText(type) {
      const typeMap = {
        'NOTICE': '通知公告',
        'ADMISSION': '招生信息',
        'NEWS': '新闻动态'
      }
      return typeMap[type] || type
    },
    
    async loadAnnouncements() {
      this.loading = true;
      try {
        const [err, res] = await uni.request({
          url: 'http://localhost:8080/api/announcements',
          method: 'GET',
          header: {
            'Authorization': uni.getStorageSync('token')
          }
        })
        
        if (!err && res.statusCode === 200) {
          this.announcements = res.data.data
          this.total = res.data.data.length
        }
      } catch (error) {
        console.error('获取公告列表失败:', error)
        uni.showToast({
          title: '获取公告列表失败',
          icon: 'none'
        })
      } finally {
        this.loading = false;
      }
    },
    
    handleSearch() {
      // 实现搜索功能
      this.loadAnnouncements()
    },
    
    showAddModal() {
      // 跳转到新增页面
      uni.navigateTo({
        url: '/pages/admin/announcement/edit?type=add'
      })
    },
    
    handleEdit(item) {
      // 跳转到编辑页面
      uni.navigateTo({
        url: `/pages/admin/announcement/edit?id=${item.id}&type=edit`
      })
    },
    
    async handlePublish(item) {
      try {
        this.loading = true;
        const [err, res] = await uni.request({
          url: `http://localhost:8080/api/announcements/${item.id}/publish`,
          method: 'PUT',
          header: {
            'Authorization': uni.getStorageSync('token')
          }
        })
        
        if (!err && res.statusCode === 200) {
          uni.showToast({
            title: '发布成功',
            icon: 'success'
          })
          this.loadAnnouncements()
        }
      } catch (error) {
        uni.showToast({
          title: '发布失败',
          icon: 'none'
        })
      } finally {
        this.loading = false;
      }
    },
    
    async handleUnpublish(item) {
      try {
        this.loading = true;
        const [err, res] = await uni.request({
          url: `http://localhost:8080/api/announcements/${item.id}/unpublish`,
          method: 'PUT',
          header: {
            'Authorization': uni.getStorageSync('token')
          }
        })
        
        if (!err && res.statusCode === 200) {
          uni.showToast({
            title: '撤回成功',
            icon: 'success'
          })
          this.loadAnnouncements()
        }
      } catch (error) {
        uni.showToast({
          title: '撤回失败',
          icon: 'none'
        })
      } finally {
        this.loading = false;
      }
    },
    
    async handleDelete(item) {
      uni.showModal({
        title: '确认删除',
        content: `确定要删除公告"${item.title}"吗？`,
        success: async (res) => {
          if (res.confirm) {
            try {
              this.loading = true;
              const [err, res] = await uni.request({
                url: `http://localhost:8080/api/announcements/${item.id}`,
                method: 'DELETE',
                header: {
                  'Authorization': uni.getStorageSync('token')
                }
              })
              
              if (!err && res.statusCode === 200) {
                uni.showToast({
                  title: '删除成功',
                  icon: 'success'
                })
                this.loadAnnouncements()
              }
            } catch (error) {
              uni.showToast({
                title: '删除失败',
                icon: 'none'
              })
            } finally {
              this.loading = false;
            }
          }
        }
      })
    },
    
    handlePageChange(page) {
      if (page < 1 || page > this.totalPages) return
      this.currentPage = page
      // 实现分页逻辑
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
  
  .announcement-list {
    .announcement-item {
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
      
      .announce-info {
        .announce-header {
          display: flex;
          align-items: center;
          justify-content: space-between;
          margin-bottom: 15rpx;
          
          .title {
            font-size: 32rpx;
            font-weight: 600;
            color: #333;
            flex: 1;
          }
          
          .status-tag {
            display: inline-block;
            font-size: 24rpx;
            padding: 4rpx 16rpx;
            border-radius: 24rpx;
            
            &.active {
              color: #52c41a;
              background-color: #f6ffed;
              border: 1rpx solid #b7eb8f;
            }
            
            &.inactive {
              color: #faad14;
              background-color: #fffbe6;
              border: 1rpx solid #ffe58f;
            }
          }
        }
        
        .announce-meta {
          display: flex;
          align-items: center;
          flex-wrap: wrap;
          margin-bottom: 15rpx;
          
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
        
        .announce-content {
          font-size: 28rpx;
          color: #666;
          line-height: 1.6;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
          text-overflow: ellipsis;
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
          
          &.edit-btn {
            background-color: #e6f7ff;
            color: #4aa3ff;
            box-shadow: 0 2rpx 8rpx rgba(74, 163, 255, 0.15);
            
            &:active {
              transform: translateY(2rpx);
              box-shadow: 0 1rpx 3rpx rgba(74, 163, 255, 0.1);
            }
          }
          
          &.publish-btn {
            background-color: #f6ffed;
            color: #52c41a;
            box-shadow: 0 2rpx 8rpx rgba(82, 196, 26, 0.15);
            
            &:active {
              transform: translateY(2rpx);
              box-shadow: 0 1rpx 3rpx rgba(82, 196, 26, 0.1);
            }
          }
          
          &.unpublish-btn {
            background-color: #fffbe6;
            color: #faad14;
            box-shadow: 0 2rpx 8rpx rgba(250, 173, 20, 0.15);
            
            &:active {
              transform: translateY(2rpx);
              box-shadow: 0 1rpx 3rpx rgba(250, 173, 20, 0.1);
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
</style> 