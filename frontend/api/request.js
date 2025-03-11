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
      uni.request({
        url: baseURL + url,
        method: 'GET',
        data: params,
        header: {
          'Content-Type': 'application/json',
          ...header
        },
        success: (res) => {
          if (res.statusCode === 200) {
            resolve(res.data);
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
      uni.request({
        url: baseURL + url,
        method: 'POST',
        data: data,
        header: {
          'Content-Type': 'application/json',
          ...header
        },
        success: (res) => {
          if (res.statusCode === 200) {
            resolve(res.data);
          } else {
            reject(res);
          }
        },
        fail: (err) => {
          reject(err);
        }
      });
    });
  }
};

export default request; 