const baseURL = 'http://localhost:8080';

const request = {
  /**
   * 发送GET请求
   * @param {String} url - 请求地址
   * @param {Object} params - 请求参数
   * @param {Object} header - 请求头
   * @returns {Promise} - 返回Promise对象
   */
  get(url, params = {}, header = {}) {
    return new Promise((resolve, reject) => {
      // 获取token
      const token = uni.getStorageSync('token');
      
      // 合并请求头
      const headers = {
        'Content-Type': 'application/json',
        ...header
      };
      
      // 如果token存在，添加到请求头
      if (token) {
        headers['Authorization'] = `Bearer ${token}`;
      }
      
      uni.request({
        url: baseURL + url,
        method: 'GET',
        data: params,
        header: headers,
        success: (res) => {
          if (res.statusCode === 200) {
            resolve(res.data);
          } else if (res.statusCode === 401) {
            // 未授权，清除登录状态
            uni.removeStorageSync('token');
            uni.removeStorageSync('userInfo');
            // 跳转到登录页
            uni.showToast({
              title: '登录已过期，请重新登录',
              icon: 'none'
            });
            setTimeout(() => {
              uni.navigateTo({
                url: '/pages/my/login'
              });
            }, 1500);
            reject(res);
          } else {
            reject(res);
          }
        },
        fail: (err) => {
          reject(err);
        }
      });
    });
  },

  /**
   * 发送POST请求
   * @param {String} url - 请求地址
   * @param {Object|String} data - 请求数据
   * @param {Object} header - 请求头
   * @returns {Promise} - 返回Promise对象
   */
  post(url, data = {}, header = {}) {
    return new Promise((resolve, reject) => {
      // 获取token
      const token = uni.getStorageSync('token');
      
      // 合并请求头
      const headers = {
        'Content-Type': 'application/json',
        ...header
      };
      
      // 如果token存在，添加到请求头
      if (token) {
        headers['Authorization'] = `Bearer ${token}`;
      }
      
      console.log(`发送POST请求到 ${url}`, data, headers);
      
      uni.request({
        url: baseURL + url,
        method: 'POST',
        data: data,
        header: headers,
        success: (res) => {
          console.log(`收到响应:`, res);
          if (res.statusCode === 200) {
            resolve(res.data);
          } else if (res.statusCode === 401) {
            // 未授权，清除登录状态
            uni.removeStorageSync('token');
            uni.removeStorageSync('userInfo');
            // 跳转到登录页
            uni.showToast({
              title: '登录已过期，请重新登录',
              icon: 'none'
            });
            setTimeout(() => {
              uni.navigateTo({
                url: '/pages/my/login'
              });
            }, 1500);
            reject(res);
          } else {
            console.error('请求失败:', res.statusCode, res.data);
            reject(res.data || res);
          }
        },
        fail: (err) => {
          console.error('请求错误:', err);
          reject(err);
        }
      });
    });
  }
};

export default request; 