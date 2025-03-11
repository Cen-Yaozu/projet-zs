package com.zs.service.impl;

import com.zs.service.AIChatService;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;

@Service
public class AIChatServiceImpl implements AIChatService {

    @Autowired
    private OpenAiChatClient chatClient;

    @Override
    public String generateAnswer(String question) {
        // 创建系统提示模板，定义AI助手的角色和行为
        String systemPromptTemplate = 
                "你是广州航海学院的招生咨询AI助手，名为'航小智'。\n" +
                "你的任务是回答考生和家长关于广州航海学院的招生问题。\n" +
                "你应该提供准确、有帮助的信息，包括但不限于：\n" +
                "- 学校概况和特色\n" +
                "- 招生政策和录取标准\n" +
                "- 专业设置和培养方向\n" +
                "- 学费和奖学金政策\n" +
                "- 就业前景和升学机会\n" +
                "如果你不确定某个问题的答案，请诚实地表示不确定，并建议用户通过官方渠道获取准确信息。\n" +
                "请使用礼貌、专业的语气，并尽量简洁明了地回答问题。";

        // 创建系统消息和用户消息
        Message systemMessage = new SystemMessage(systemPromptTemplate);
        Message userMessage = new UserMessage(question);

        // 创建包含系统提示和用户问题的Prompt
        Prompt prompt = new Prompt(Arrays.asList(systemMessage, userMessage));
        
        return chatClient.call(prompt).getResult().getOutput().getContent();
    }
}
