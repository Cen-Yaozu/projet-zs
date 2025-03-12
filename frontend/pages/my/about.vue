<template>
  <view class="container">
    <view class="logo-section">
      <image class="logo" src="/static/logo.png" mode="aspectFit" />
      <text class="app-name">广州航海学院</text>
      <text class="version">Version 1.0.0</text>
    </view>

    <view class="content-section">
      <view class="section-title">关于我们</view>
      <view class="section-content">
        <text class="content-text">广州航海学院是一所以航海教育为特色的高等学府，致力于培养高素质的航海人才。本系统旨在为学生提供便捷的信息服务和智能咨询支持。</text>
      </view>
      
      <view class="section-title">联系方式</view>
      <view class="contact-list">
        <view class="contact-item">
          <text class="label">电话：</text>
          <text class="value">020-12345678</text>
        </view>
        <view class="contact-item">
          <text class="label">邮箱：</text>
          <text class="value">contact@example.com</text>
        </view>
        <view class="contact-item">
          <text class="label">地址：</text>
          <text class="value">广州市XX区XX路XX号</text>
        </view>
      </view>
    </view>

    <view class="footer">
      <text class="copyright">Copyright © 2024 广州航海学院</text>
      <text class="rights">All Rights Reserved</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {}
  }
}
</script>

<style lang="scss">
.container {
  min-height: 100vh;
  background-color: #fff;
  padding: 40rpx;
  display: flex;
  flex-direction: column;
}

.logo-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 60rpx;
  
  .logo {
    width: 160rpx;
    height: 160rpx;
    margin-bottom: 20rpx;
  }
  
  .app-name {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 10rpx;
  }
  
  .version {
    font-size: 24rpx;
    color: #999;
  }
}

.content-section {
  flex: 1;
  
  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
    padding-left: 20rpx;
    border-left: 8rpx solid #4aa3ff;
  }
  
  .section-content {
    margin-bottom: 40rpx;
    
    .content-text {
      font-size: 28rpx;
      color: #666;
      line-height: 1.6;
    }
  }
  
  .contact-list {
    margin-bottom: 40rpx;
    
    .contact-item {
      display: flex;
      margin-bottom: 16rpx;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      .label {
        font-size: 28rpx;
        color: #666;
        width: 100rpx;
      }
      
      .value {
        font-size: 28rpx;
        color: #333;
        flex: 1;
      }
    }
  }
}

.footer {
  text-align: center;
  padding: 40rpx 0;
  
  .copyright {
    font-size: 24rpx;
    color: #999;
    display: block;
    margin-bottom: 6rpx;
  }
  
  .rights {
    font-size: 24rpx;
    color: #999;
  }
}
</style> 