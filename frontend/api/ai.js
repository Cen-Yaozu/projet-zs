// AI问答API
import request from './request.js';

export default {
  /**
   * 向AI提问
   * @param {String} question - 问题内容
   * @returns {Promise} - 返回AI回答
   */
  askQuestion(question) {
    return request.post('/api/ai-chat/question', question, {
      header: {
        'Content-Type': 'text/plain'
      }
    });
  }
};