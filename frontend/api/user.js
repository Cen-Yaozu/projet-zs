// 用户相关API
import request from './request.js';

export default {
  /**
   * 用户登录
   * @param {Object} data - 登录信息 {username, password, role}
   * @returns {Promise} - 返回登录结果
   */
  login(data) {
    return request.post('/api/users/login', data);
  },

  /**
   * 用户注册
   * @param {Object} data - 注册信息
   * @returns {Promise} - 返回注册结果
   */
  register(data) {
    return request.post('/api/users/register', data);
  },

  /**
   * 获取用户信息
   * @param {Number} id - 用户ID
   * @returns {Promise} - 返回用户信息
   */
  getUserInfo(id) {
    return request.get(`/api/users/${id}`);
  },
  
  /**
   * 更新用户密码
   * @param {Object} data - 包含用户名、旧密码和新密码的数据对象
   * @returns {Promise} - 返回更新结果
   */
  updatePassword(data) {
    return request.post('/api/users/password/update', data);
  },
  
  /**
   * 搜索用户
   * @param {String} username - 用户名
   * @returns {Promise} - 返回用户信息
   */
  searchUser(username) {
    return request.get(`/api/users/search?username=${username}`);
  },
  
  /**
   * 更新用户信息
   * @param {Number} id - 用户ID
   * @param {Object} data - 用户信息
   * @returns {Promise} - 返回更新结果
   */
  updateUser(id, data) {
    return request.put(`/api/users/${id}`, data);
  },
  
  /**
   * 重置用户密码
   * @param {Number} id - 用户ID
   * @returns {Promise} - 返回重置结果
   */
  resetPassword(id) {
    return request.post(`/api/users/${id}/password/reset`);
  },
  
  /**
   * 上传用户头像
   * @param {String} username - 用户名
   * @param {Object} file - 头像文件
   * @returns {Promise} - 返回上传结果
   */
  uploadAvatar(username, file) {
    const formData = new FormData();
    formData.append('file', file);
    return request.post(`/api/users/${username}/avatar`, formData);
  },

  /**
   * 更新当前用户密码
   * @param {Object} data - 包含旧密码和新密码的数据对象
   * @returns {Promise} - 返回更新结果
   */
  changePassword(data) {
    const userInfo = uni.getStorageSync('userInfo');
    if (!userInfo || !userInfo.username) {
      return Promise.reject(new Error('未登录'));
    }
    
    return request.post('/api/users/password/update', {
      username: userInfo.username,
      oldPassword: data.oldPassword,
      newPassword: data.newPassword
    });
  },

  /**
   * 获取所有用户
   * @returns {Promise} - 返回所有用户列表
   */
  getAllUsers() {
    return request.get('/api/users');
  },

  /**
   * 删除用户
   * @param {Number} id - 用户ID
   * @returns {Promise} - 返回删除结果
   */
  deleteUser(id) {
    return request.delete(`/api/users/${id}`);
  }
}; 