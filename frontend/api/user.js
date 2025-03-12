// 用户相关API
import request from './request.js';

export default {
  /**
   * 用户登录
   * @param {Object} data - 登录信息 {username, password, role}
   * @returns {Promise} - 返回登录结果
   */
  login(data) {
    return request.post('/api/user/login', data);
  },

  /**
   * 用户注册
   * @param {Object} data - 注册信息
   * @returns {Promise} - 返回注册结果
   */
  register(data) {
    return request.post('/api/user/register', data);
  },

  /**
   * 获取用户信息
   * @param {Number} id - 用户ID
   * @returns {Promise} - 返回用户信息
   */
  getUserInfo(id) {
    return request.get(`/api/user/${id}`);
  },
  
  /**
   * 更新用户密码
   * @param {Object} data - 包含用户名、旧密码和新密码的数据对象
   * @returns {Promise} - 返回更新结果
   */
  updatePassword(data) {
    return request.post('/api/user/update-password', data);
  }
}; 