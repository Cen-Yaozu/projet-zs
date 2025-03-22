import request from './request';

/**
 * 招生相关API
 */
export default {
  /**
   * 获取历年分数数据
   * @param {Number} year - 年份
   * @returns {Promise} - 返回Promise对象
   */
  getScoresByYear(year) {
    return request.get('/api/admission-policy/search/year', {
      year: year
    });
  },
  
  /**
   * 获取所有可用年份列表
   * @returns {Promise} - 返回Promise对象
   */
  getAvailableYears() {
    // 使用新的专用年份接口
    return request.get('/api/admission-policy/years');
  },

  /**
   * 根据省份获取招生政策
   * @param {String} province - 省份名称
   * @returns {Promise} - 返回Promise对象
   */
  getPoliciesByProvince(province) {
    return request.get('/api/admission-policy/search/province/' + province);
  },

  /**
   * 获取所有招生政策
   * @returns {Promise} - 返回Promise对象
   */
  getAllPolicies() {
    return request.get('/api/admission-policy/list');
  },
  
  /**
   * 获取专业分数线数据
   * @param {Number} year - 年份
   * @param {String} province - 省份(可选)
   * @returns {Promise} - 返回Promise对象
   */
  getMajorScores(year, province) {
    const params = {
      year: year
    };
    
    if (province) {
      params.province = province;
    }
    
    return request.get('/api/admission-policy/search/major-scores', params);
  },
  
  /**
   * 获取所有专业分数线数据
   * @returns {Promise} - 返回Promise对象
   */
  getAllMajorScores() {
    return request.get('/api/major-score/list');
  },
  
  /**
   * 根据专业名称获取分数线数据
   * @param {String} majorName - 专业名称
   * @returns {Promise} - 返回Promise对象
   */
  getMajorScoresByName(majorName) {
    return request.get('/api/major-score/search/major/' + encodeURIComponent(majorName));
  },
  
  /**
   * 填充历史分数数据（将招生政策数据转换为专业分数数据）
   * @returns {Promise} - 返回Promise对象
   */
  fillHistoricalScoreData() {
    return request.post('/api/major-score/fill-data');
  }
} 