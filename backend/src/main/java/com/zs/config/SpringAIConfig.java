package com.zs.config;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring AI 配置类
 * 用于显式配置ChatClient Bean
 */
@Configuration
public class SpringAIConfig {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.base-url:https://api.openai.com}")
    private String baseUrl;

    @Value("${spring.ai.openai.model:gpt-3.5-turbo}")
    private String model;

    @Value("${spring.ai.openai.temperature:0.7}")
    private Float temperature;

    @Value("${spring.ai.openai.max-tokens:800}")
    private Integer maxTokens;

    /**
     * 显式配置ChatClient Bean
     * 确保ChatClient能够被正确初始化和注入
     */
    @Bean("OpenAiChatClient")
    public OpenAiChatClient OpenAiChatClient(){
        OpenAiApi openAiApi = new OpenAiApi(baseUrl, apiKey);
        return new OpenAiChatClient(openAiApi);
    }
} 