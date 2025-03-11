<template>
  <view class="container">
    <view class="chat-header">
      <!-- 使用背景色代替图片 -->
      <view class="ai-banner-placeholder"></view>
      <view class="title">智能招生咨询</view>
      <view class="subtitle">由AI驱动，为您解答招生相关问题</view>
    </view>
    
    <scroll-view class="chat-container" scroll-y="true" :scroll-top="scrollTop" @scrolltoupper="loadMore">
      <view class="chat-list">
        <view class="time-divider">{{ currentDate }}</view>
        
        <!-- 系统消息 -->
        <view class="chat-item system">
          <text class="message">您好，我是广州航海学院招生智能咨询助手，请问有什么可以帮助您的吗？您可以询问招生政策、专业信息、录取标准等问题。</text>
        </view>
        
        <!-- 聊天记录 -->
        <block v-for="(item, index) in chatMessages" :key="index">
          <!-- 用户消息 -->
          <view class="chat-item user" v-if="item.sender === 'user'">
            <view class="message">{{ item.content }}</view>
            <view class="avatar">
              <view class="avatar-placeholder user-avatar-placeholder"></view>
            </view>
          </view>
          
          <!-- AI 回复 -->
          <view class="chat-item ai" v-else>
            <view class="avatar">
              <view class="avatar-placeholder ai-avatar-placeholder">
                <uni-icons type="chatboxes-filled" size="24" color="#FFFFFF"></uni-icons>
              </view>
            </view>
            <view class="message">{{ item.content }}</view>
          </view>
        </block>
        
        <!-- AI 正在输入提示 -->
        <view class="chat-item ai" v-if="aiTyping">
          <view class="avatar">
            <view class="avatar-placeholder ai-avatar-placeholder">
              <uni-icons type="chatboxes-filled" size="24" color="#FFFFFF"></uni-icons>
            </view>
          </view>
          <view class="message typing">
            <view class="typing-animation">
              <view class="dot"></view>
              <view class="dot"></view>
              <view class="dot"></view>
            </view>
          </view>
        </view>
      </view>
    </scroll-view>
    
    <view class="input-container">
      <view class="hot-questions" v-if="showHotQuestions">
        <view class="hot-title">常见问题</view>
        <view class="question-list">
          <view class="question-item" v-for="(question, index) in hotQuestions" :key="index" @tap="selectHotQuestion(question)">
            {{ question }}
          </view>
        </view>
      </view>
      
      <view class="input-wrapper">
        <input 
          class="chat-input" 
          v-model="userInput" 
          placeholder="请输入您的问题" 
          confirm-type="send"
          @confirm="sendMessage"
          @focus="onInputFocus"
          @blur="onInputBlur"
        />
        <view class="send-btn" :class="{ active: userInput.trim().length > 0 }" @tap="sendMessage">发送</view>
      </view>
    </view>
  </view>
</template>

<script>
import aiAPI from '@/api/ai.js';

export default {
  data() {
    return {
      userInput: '',
      scrollTop: 0,
      aiTyping: false,
      showHotQuestions: true,
      currentDate: '',
      chatMessages: [],
      hotQuestions: [
        '广州航海学院有哪些王牌专业？',
        '今年招生计划和录取分数线是多少？',
        '学校有哪些奖学金政策？',
        '航海类专业就业前景怎么样？',
        '可以调剂到其他专业吗？'
      ]
    }
  },
  onLoad() {
    this.setCurrentDate();
    uni.setNavigationBarTitle({
      title: '智能咨询'
    });
  },
  methods: {
    setCurrentDate() {
      const date = new Date();
      const year = date.getFullYear();
      const month = date.getMonth() + 1;
      const day = date.getDate();
      this.currentDate = `${year}年${month}月${day}日`;
    },
    sendMessage() {
      if (this.userInput.trim() === '') return;
      
      // 添加用户消息
      this.chatMessages.push({
        sender: 'user',
        content: this.userInput,
        time: new Date().getTime()
      });
      
      // 清空输入框
      const userQuestion = this.userInput;
      this.userInput = '';
      
      // 自动滚动到底部
      this.$nextTick(() => {
        this.scrollToBottom();
      });
      
      // 显示AI正在输入状态
      this.aiTyping = true;
      
      // 调用后端AI接口获取回答
      aiAPI.askQuestion(userQuestion)
        .then(res => {
          this.aiTyping = false;
          
          // 添加AI回复
          this.chatMessages.push({
            sender: 'ai',
            content: res.data,
            time: new Date().getTime()
          });
          
          // 滚动到底部
          this.$nextTick(() => {
            this.scrollToBottom();
          });
        })
        .catch(err => {
          this.aiTyping = false;
          
          // 添加错误提示
          this.chatMessages.push({
            sender: 'ai',
            content: '抱歉，AI回答生成失败，请稍后再试。',
            time: new Date().getTime()
          });
          
          // 滚动到底部
          this.$nextTick(() => {
            this.scrollToBottom();
          });
          
          console.error('AI回答生成失败:', err);
        });
    },
    scrollToBottom() {
      // 获取节点信息，计算滚动位置
      const query = uni.createSelectorQuery().in(this);
      query.select('.chat-list').boundingClientRect(data => {
        if (data) {
          this.scrollTop = data.height * 2; // 滚动到底部
        }
      }).exec();
    },
    loadMore() {
      // 加载更多历史消息
      console.log('加载更多历史消息');
    },
    selectHotQuestion(question) {
      this.userInput = question;
      this.sendMessage();
    },
    onInputFocus() {
      this.showHotQuestions = false;
    },
    onInputBlur() {
      // 如果没有聊天记录，则显示热门问题
      if (this.chatMessages.length === 0) {
        this.showHotQuestions = true;
      }
    }
  }
}
</script>

<style>
.container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f7fa;
}

.chat-header {
  background-color: #fff;
  padding: 30rpx;
  position: relative;
}

.ai-banner-placeholder {
  width: 100%;
  height: 200rpx;
  background: linear-gradient(to right, #3cc51f, #4aa3ff);
  border-radius: 10rpx;
}

.title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
  margin-top: 20rpx;
}

.subtitle {
  font-size: 24rpx;
  color: #666;
  margin-top: 10rpx;
}

.chat-container {
  flex: 1;
  padding: 20rpx 30rpx;
}

.time-divider {
  text-align: center;
  font-size: 24rpx;
  color: #999;
  margin: 20rpx 0;
}

.chat-item {
  display: flex;
  margin-bottom: 30rpx;
}

.chat-item.user {
  justify-content: flex-end;
}

.chat-item.ai, .chat-item.system {
  justify-content: flex-start;
}

.avatar {
  width: 80rpx;
  height: 80rpx;
  flex-shrink: 0;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.user-avatar-placeholder {
  background-color: #007AFF;
}

.ai-avatar-placeholder {
  background-color: #3cc51f;
}

.message {
  max-width: 70%;
  padding: 20rpx;
  border-radius: 10rpx;
  font-size: 28rpx;
  word-break: break-all;
  position: relative;
  margin: 0 20rpx;
}

.user .message {
  background-color: #007AFF;
  color: #fff;
}

.ai .message, .system .message {
  background-color: #fff;
  color: #333;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.typing {
  padding: 20rpx 30rpx;
}

.typing-animation {
  display: flex;
  align-items: center;
}

.dot {
  width: 10rpx;
  height: 10rpx;
  background-color: #999;
  border-radius: 50%;
  margin: 0 5rpx;
  animation: typing 1.5s infinite ease-in-out;
}

.dot:nth-child(1) {
  animation-delay: 0s;
}

.dot:nth-child(2) {
  animation-delay: 0.2s;
}

.dot:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
  }
  30% {
    transform: translateY(-10rpx);
  }
}

.input-container {
  background-color: #fff;
  padding: 20rpx 30rpx;
  border-top: 1rpx solid #eee;
}

.hot-questions {
  margin-bottom: 20rpx;
}

.hot-title {
  font-size: 26rpx;
  color: #999;
  margin-bottom: 15rpx;
}

.question-list {
  display: flex;
  flex-wrap: wrap;
}

.question-item {
  background-color: #f5f7fa;
  color: #666;
  font-size: 24rpx;
  padding: 10rpx 20rpx;
  border-radius: 30rpx;
  margin-right: 15rpx;
  margin-bottom: 15rpx;
}

.input-wrapper {
  display: flex;
  align-items: center;
}

.chat-input {
  flex: 1;
  height: 80rpx;
  background-color: #f5f7fa;
  border-radius: 40rpx;
  padding: 0 30rpx;
  font-size: 28rpx;
}

.send-btn {
  width: 120rpx;
  height: 80rpx;
  line-height: 80rpx;
  text-align: center;
  color: #999;
  font-size: 28rpx;
}

.send-btn.active {
  color: #007AFF;
}
</style>