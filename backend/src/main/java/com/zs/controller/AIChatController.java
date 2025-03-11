package com.zs.controller;

import com.zs.common.response.ApiResponse;
import com.zs.service.AIChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai-chat")
@Tag(name = "AI智能问答接口", description = "AI智能问答相关接口")
public class AIChatController {

    @Autowired
    private AIChatService aiChatService;

    @PostMapping("/question")
    @Operation(summary = "AI问答", description = "向AI提问并获取回答")
    public ResponseEntity<?> askQuestion(
            @Parameter(description = "问题内容", required = true) 
            @RequestBody String question) {
        try {
            String answer = aiChatService.generateAnswer(question);
            return ResponseEntity.ok(ApiResponse.success(answer));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("AI回答生成失败: " + e.getMessage()));
        }
    }
} 