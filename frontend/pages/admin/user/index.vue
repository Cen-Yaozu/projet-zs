<template>
  <view class="container">
    <view class="header">
      <view class="back-button" @tap="navBack">
        <text class="iconfont icon-left"></text>
      </view>
      <text class="title">用户管理</text>
    </view>
    
    <view class="content">
      <view class="filter-bar" v-if="hasPermission">
        <view class="search-box">
          <text class="iconfont icon-search"></text>
          <input 
            type="text" 
            v-model="searchKeyword"
            placeholder="搜索用户名/姓名/学号"
            class="input"
          />
          <text v-if="searchKeyword" class="clear-icon iconfont icon-close" @tap="searchKeyword = ''"></text>
        </view>
        <button class="add-button" @tap="showAddModal">
          <text class="add-icon iconfont icon-add"></text>
          <text>新增用户</text>
        </button>
      </view>
      
      <view class="permission-denied" v-if="!hasPermission">
        <view class="permission-icon">
          <text class="iconfont icon-lock"></text>
        </view>
        <view class="permission-text">
          <view class="permission-title">暂无访问权限</view>
          <view class="permission-desc">您当前账号无权访问用户管理功能</view>
        </view>
        <button class="back-home-btn" @tap="navToHome">返回首页</button>
      </view>
      
      <view class="user-list" v-if="hasPermission">
        <view class="empty-tip" v-if="!loading && !loadError && users.length === 0">
          <text class="empty-icon iconfont icon-empty"></text>
          <view>暂无用户数据</view>
        </view>
        
        <view class="empty-tip" v-if="!loading && loadError && errorCode !== 403">
          <text class="empty-icon iconfont icon-warning"></text>
          <view>加载失败，请重试</view>
        </view>
        
        <view class="user-item" v-for="item in users" :key="item.id">
          <view class="user-info">
            <view class="avatar-box">
              <image class="avatar" :src="item.avatar || '/static/images/default-avatar.png'" mode="aspectFill"></image>
              <view class="role-tag" :class="roleClassMap[item.role] || ''">{{getRoleText(item.role)}}</view>
            </view>
            <view class="user-detail">
              <view class="user-name-row">
                <text class="user-name">{{item.name || item.username}}</text>
                <view class="status-tag" :class="item.status ? 'active' : 'inactive'">
                  {{item.status ? '已激活' : '未激活'}}
                </view>
              </view>
              <view class="user-meta">
                <view class="meta-item" v-if="item.username">
                  <text class="meta-icon iconfont icon-user"></text>
                  <text>{{item.username}}</text>
                </view>
                <view class="meta-item" v-if="item.studentId">
                  <text class="meta-icon iconfont icon-id"></text>
                  <text>{{item.studentId}}</text>
                </view>
                <view class="meta-item" v-if="item.email">
                  <text class="meta-icon iconfont icon-email"></text>
                  <text>{{item.email}}</text>
                </view>
                <view class="meta-item" v-if="item.department">
                  <text class="meta-icon iconfont icon-department"></text>
                  <text>{{item.department}}</text>
                </view>
              </view>
            </view>
          </view>
          
          <view class="action-row">
            <button class="action-button edit-btn" @tap="handleEdit(item)">编辑</button>
            <button 
              class="action-button reset-btn" 
              @tap="handleResetPassword(item)"
            >重置密码</button>
            <button 
              class="action-button toggle-btn" :class="item.status ? 'disable-btn' : 'enable-btn'"
              @tap="handleToggleStatus(item)"
            >{{item.status ? '禁用' : '启用'}}</button>
            <button class="action-button delete-btn" @tap="handleDelete(item)">删除</button>
          </view>
        </view>
      </view>
      
      <view class="pagination" v-if="hasPermission && users.length > 0">
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
    
    <view class="loading-overlay" v-if="loading">
      <view class="loading-content">
        <text class="loading-text">加载中...</text>
      </view>
    </view>
    
    <uni-popup ref="popup" type="center" v-if="hasPermission">
      <view class="popup-content">
        <view class="popup-header">
          <text class="popup-title">{{isEdit ? '编辑用户' : '新增用户'}}</text>
        </view>
        <view class="popup-body">
          <view class="form-item">
            <text class="label">用户名<text class="required">*</text></text>
            <input 
              type="text" 
              v-model="form.username" 
              placeholder="请输入用户名"
              class="input"
              :disabled="isEdit"
            />
          </view>
          <view class="form-item" v-if="!isEdit">
            <text class="label">密码<text class="required">*</text></text>
            <input 
              type="password" 
              v-model="form.password" 
              placeholder="请输入密码"
              class="input"
            />
          </view>
          <view class="form-item">
            <text class="label">姓名</text>
            <input 
              type="text" 
              v-model="form.name" 
              placeholder="请输入姓名"
              class="input"
            />
          </view>
          <view class="form-item">
            <text class="label">学号</text>
            <input 
              type="text" 
              v-model="form.studentId" 
              placeholder="请输入学号"
              class="input"
            />
          </view>
          <view class="form-item">
            <text class="label">邮箱</text>
            <input 
              type="text" 
              v-model="form.email" 
              placeholder="请输入邮箱"
              class="input"
            />
          </view>
          <view class="form-item">
            <text class="label">角色<text class="required">*</text></text>
            <picker 
              :range="roleOptions" 
              :value="roleIndex"
              @change="handleRoleChange"
            >
              <view class="picker-value">
                {{roleOptions[roleIndex]}}
                <text class="iconfont icon-down"></text>
              </view>
            </picker>
          </view>
          <view class="form-item">
            <text class="label">院系</text>
            <input 
              type="text" 
              v-model="form.department" 
              placeholder="请输入院系"
              class="input"
            />
          </view>
        </view>
        <view class="popup-footer">
          <button class="cancel-btn" @tap="hidePopup">取消</button>
          <button class="confirm-btn" @tap="handleSubmit">确定</button>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script>
import userAPI from '@/api/user.js';
import uniPopup from '@/uni_modules/uni-popup/components/uni-popup/uni-popup.vue';

export default {
  components: {
    uniPopup
  },
  data() {
    return {
      loading: false,
      loadError: false,
      errorCode: null,
      hasPermission: false,
      searchKeyword: '',
      users: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      form: {
        username: '',
        password: '',
        name: '',
        studentId: '',
        email: '',
        role: 'STUDENT',
        department: '',
        status: true
      },
      isEdit: false,
      editingUser: null,
      roleOptions: ['学生', '教师', '管理员'],
      roleIndex: 0,
      roleValues: ['STUDENT', 'TEACHER', 'ADMIN'],
      roleClassMap: {
        'STUDENT': 'student',
        'TEACHER': 'teacher',
        'ADMIN': 'admin'
      }
    }
  },
  computed: {
    totalPages() {
      return Math.ceil(this.total / this.pageSize)
    }
  },
  onShow() {
    this.checkPermission();
  },
  methods: {
    navBack() {
      uni.navigateBack();
    },
    
    navToHome() {
      uni.switchTab({
        url: '/pages/index/index'
      });
    },
    
    checkPermission() {
      this.loading = true;
      
      userAPI.getAllUsers()
        .then(res => {
          this.hasPermission = true;
          this.users = res || [];
          this.total = this.users.length;
        })
        .catch(error => {
          console.error('获取用户列表失败:', error);
          if (error && error.statusCode === 403) {
            this.hasPermission = false;
          } else {
            this.hasPermission = true;
            this.loadError = true;
            if (error && error.statusCode) {
              this.errorCode = error.statusCode;
            }
            
            uni.showToast({
              title: '获取用户列表失败',
              icon: 'none'
            });
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    
    getRoleText(role) {
      const roleMap = {
        'STUDENT': '学生',
        'TEACHER': '教师',
        'ADMIN': '管理员'
      }
      return roleMap[role] || role
    },
    
    getRoleClass(role) {
      const classMap = {
        'STUDENT': 'student',
        'TEACHER': 'teacher',
        'ADMIN': 'admin'
      }
      return classMap[role] || ''
    },
    
    handleSearch() {
      this.loadUsers()
    },
    
    showAddModal() {
      this.isEdit = false
      this.form = {
        username: '',
        password: '',
        name: '',
        studentId: '',
        email: '',
        role: 'STUDENT',
        department: '',
        status: true
      }
      this.roleIndex = 0
      this.$refs.popup.open()
    },
    
    hidePopup() {
      this.$refs.popup.close()
    },
    
    handleEdit(item) {
      this.isEdit = true
      this.editingUser = item
      this.form = { 
        ...item,
        password: ''
      }
      this.roleIndex = this.roleValues.indexOf(item.role)
      this.$refs.popup.open()
    },
    
    handleRoleChange(e) {
      this.roleIndex = e.detail.value
      this.form.role = this.roleValues[this.roleIndex]
    },
    
    async handleToggleStatus(item) {
      try {
        this.loading = true;
        await userAPI.updateUser(item.id, { ...item, status: !item.status });
        
        uni.showToast({
          title: !item.status ? '启用成功' : '禁用成功',
          icon: 'success'
        });
        
        this.loadUsers();
      } catch (error) {
        console.error('操作失败:', error);
        uni.showToast({
          title: !item.status ? '启用失败' : '禁用失败',
          icon: 'none'
        });
      } finally {
        this.loading = false;
      }
    },
    
    async handleResetPassword(item) {
      uni.showModal({
        title: '重置密码',
        content: `确定要重置 ${item.username} 的密码吗？`,
        success: async (res) => {
          if (res.confirm) {
            try {
              this.loading = true;
              await userAPI.resetPassword(item.id);
              
              uni.showToast({
                title: '密码重置成功',
                icon: 'success'
              });
            } catch (error) {
              console.error('重置密码失败:', error);
              uni.showToast({
                title: '重置密码失败',
                icon: 'none'
              });
            } finally {
              this.loading = false;
            }
          }
        }
      });
    },
    
    async handleDelete(item) {
      uni.showModal({
        title: '删除用户',
        content: `确定要删除用户 ${item.username} 吗？`,
        success: async (res) => {
          if (res.confirm) {
            try {
              this.loading = true;
              await userAPI.deleteUser(item.id);
              
              uni.showToast({
                title: '删除成功',
                icon: 'success'
              });
              
              this.loadUsers();
            } catch (error) {
              console.error('删除失败:', error);
              uni.showToast({
                title: '删除失败',
                icon: 'none'
              });
            } finally {
              this.loading = false;
            }
          }
        }
      });
    },
    
    async handleSubmit() {
      if (!this.form.username) {
        uni.showToast({
          title: '请输入用户名',
          icon: 'none'
        });
        return;
      }
      
      if (!this.isEdit && !this.form.password) {
        uni.showToast({
          title: '请输入密码',
          icon: 'none'
        });
        return;
      }
      
      try {
        this.loading = true;
        
        const formData = {...this.form};
        
        if (this.isEdit) {
          delete formData.password;
          await userAPI.updateUser(this.editingUser.id, formData);
        } else {
          await userAPI.register(formData);
        }
        
        uni.showToast({
          title: this.isEdit ? '更新成功' : '添加成功',
          icon: 'success'
        });
        
        this.hidePopup();
        this.loadUsers();
      } catch (error) {
        console.error(this.isEdit ? '更新用户失败:' : '添加用户失败:', error);
        uni.showToast({
          title: this.isEdit ? '更新失败' : '添加失败',
          icon: 'none'
        });
      } finally {
        this.loading = false;
      }
    },
    
    handlePageChange(page) {
      if (page < 1 || page > this.totalPages) return
      this.currentPage = page
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
  
  .user-list {
    .user-item {
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
      
      .user-info {
        display: flex;
        
        .avatar-box {
          position: relative;
          width: 120rpx;
          margin-right: 20rpx;
          
          .avatar {
            width: 100rpx;
            height: 100rpx;
            border-radius: 50%;
            background-color: #f0f0f0;
            border: 2rpx solid #fff;
            box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.1);
          }
          
          .role-tag {
            position: absolute;
            bottom: 0;
            right: 10rpx;
            font-size: 20rpx;
            padding: 2rpx 12rpx;
            border-radius: 20rpx;
            font-weight: 500;
            box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
            
            &.student {
              background-color: #e6f7ff;
              color: #1890ff;
              border: 1rpx solid #91d5ff;
            }
            
            &.teacher {
              background-color: #f6ffed;
              color: #52c41a;
              border: 1rpx solid #b7eb8f;
            }
            
            &.admin {
              background-color: #fff0f6;
              color: #eb2f96;
              border: 1rpx solid #ffadd2;
            }
          }
        }
        
        .user-detail {
          flex: 1;
          
          .user-name-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15rpx;
            
            .user-name {
              font-size: 32rpx;
              font-weight: 600;
              color: #333;
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
                color: #ff4d4f;
                background-color: #fff1f0;
                border: 1rpx solid #ffa39e;
              }
            }
          }
          
          .user-meta {
            display: flex;
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
          
          &.reset-btn {
            background-color: #f9f0ff;
            color: #722ed1;
            box-shadow: 0 2rpx 8rpx rgba(114, 46, 209, 0.15);
            
            &:active {
              transform: translateY(2rpx);
              box-shadow: 0 1rpx 3rpx rgba(114, 46, 209, 0.1);
            }
          }
          
          &.enable-btn {
            background-color: #f6ffed;
            color: #52c41a;
            box-shadow: 0 2rpx 8rpx rgba(82, 196, 26, 0.15);
            
            &:active {
              transform: translateY(2rpx);
              box-shadow: 0 1rpx 3rpx rgba(82, 196, 26, 0.1);
            }
          }
          
          &.disable-btn {
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
    
    .icon-warning {
      color: #faad14;
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

.popup-content {
  width: 650rpx;
  background-color: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  box-shadow: 0 12rpx 36rpx rgba(0, 0, 0, 0.2);
  
  .popup-header {
    padding: 30rpx;
    border-bottom: 1rpx solid #f0f0f0;
    
    .popup-title {
      font-size: 32rpx;
      font-weight: 600;
      color: #333;
    }
  }
  
  .popup-body {
    padding: 30rpx;
    max-height: 600rpx;
    overflow-y: auto;
    
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
    }
  }
  
  .popup-footer {
    display: flex;
    border-top: 1rpx solid #f0f0f0;
    
    button {
      flex: 1;
      height: 90rpx;
      line-height: 90rpx;
      font-size: 32rpx;
      text-align: center;
      transition: all 0.3s;
      border: none;
      
      &.cancel-btn {
        background-color: #f6f8fa;
        color: #666;
        
        &:active {
          background-color: #eee;
        }
      }
      
      &.confirm-btn {
        background: linear-gradient(135deg, #4aa3ff 0%, #3a93f0 100%);
        color: #fff;
        
        &:active {
          opacity: 0.9;
        }
      }
    }
  }
}

.permission-denied {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 40rpx;
  background-color: #fff;
  border-radius: 16rpx;
  box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.05);
  margin-top: 60rpx;
  
  .permission-icon {
    font-size: 120rpx;
    color: #4aa3ff;
    margin-bottom: 30rpx;
    opacity: 0.8;
    text-align: center;
    
    .iconfont {
      display: inline-block;
      animation: pulse 2s infinite;
    }
    
    @keyframes pulse {
      0% {
        transform: scale(0.95);
        opacity: 0.7;
      }
      50% {
        transform: scale(1);
        opacity: 1;
      }
      100% {
        transform: scale(0.95);
        opacity: 0.7;
      }
    }
  }
  
  .permission-text {
    text-align: center;
    margin-bottom: 40rpx;
    
    .permission-title {
      font-size: 36rpx;
      font-weight: 600;
      color: #333;
      margin-bottom: 20rpx;
    }
    
    .permission-desc {
      font-size: 28rpx;
      color: #666;
      line-height: 1.5;
    }
  }
  
  .back-home-btn {
    background: linear-gradient(135deg, #4aa3ff 0%, #3a93f0 100%);
    color: #fff;
    font-size: 30rpx;
    height: 80rpx;
    line-height: 80rpx;
    padding: 0 60rpx;
    border-radius: 40rpx;
    box-shadow: 0 6rpx 20rpx rgba(74, 163, 255, 0.3);
    margin-top: 20rpx;
    transition: all 0.3s;
    
    &:active {
      transform: translateY(2rpx);
      box-shadow: 0 2rpx 10rpx rgba(74, 163, 255, 0.2);
    }
  }
}
</style> 