package com.zs.service;

/**
 * AI聊天服务接口
 * 负责处理AI问答功能
 */
public interface AIChatService {
    
    /**
     * 根据用户问题生成AI回答
     * 
     * @param question 用户问题
     * @return AI生成的回答
     */
    String generateAnswer(String question);
} 