import request from './request';

/**
 * 招生相关API
 */
export default {
  /**
   * 获取历年分数数据
   * @param {Number} year - 年份
   * @param {Number} schoolId - 学校ID
   * @returns {Promise} - 返回Promise对象
   */
  getScoresByYear(year, schoolId) {
    return request.get('/api/admission-policy/search/year-school', {
      year: year,
      schoolId: schoolId
    });
  },
  
  /**
   * 获取所有可用年份列表
   * @param {Number} schoolId - 学校ID
   * @returns {Promise} - 返回Promise对象
   */
  getAvailableYears(schoolId) {
    return request.get('/api/admission-policy/school/' + schoolId);
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
  }
} 