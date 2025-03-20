<template>
  <button
    class="uni-button"
    :class="[
      'uni-button-' + type,
      'uni-button-' + size,
      { 'uni-button-disabled': disabled },
      customClass
    ]"
    :disabled="disabled"
    :form-type="formType"
    :open-type="openType"
    @tap="onClick"
  >
    <view class="uni-button-wrapper">
      <text v-if="loading" class="uni-button-loading"></text>
      <slot></slot>
    </view>
  </button>
</template>

<script>
export default {
  name: 'UniButton',
  emits: ['click'],
  props: {
    // 按钮类型：primary, secondary, outline, text
    type: {
      type: String,
      default: 'primary'
    },
    // 按钮尺寸：normal, small, mini
    size: {
      type: String,
      default: 'normal'
    },
    // 是否禁用
    disabled: {
      type: Boolean,
      default: false
    },
    // 是否显示加载图标
    loading: {
      type: Boolean,
      default: false
    },
    // 用于表单提交的类型
    formType: {
      type: String,
      default: ''
    },
    // 开放能力
    openType: {
      type: String,
      default: ''
    },
    // 自定义类名
    customClass: {
      type: String,
      default: ''
    }
  },
  methods: {
    onClick(e) {
      if (!this.disabled && !this.loading) {
        this.$emit('click', e);
      }
    }
  }
}
</script>

<style lang="scss">
$primary-color: #4aa3ff;
$primary-hover-color: #3a93f0;
$primary-active-color: #3183e0;
$secondary-color: #f5f7fa;
$secondary-hover-color: #e8eaed;
$secondary-active-color: #dadddf;
$text-color: #ffffff;
$text-dark-color: #333333;
$text-disabled-color: #c0c0c0;
$disabled-bg-color: #e0e0e0;

@keyframes rotate {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.uni-button {
  position: relative;
  margin: 4rpx;
  border: none;
  padding: 0;
  font-weight: 500;
  text-align: center;
  border-radius: 8rpx;
  overflow: hidden;
  transition: all 0.25s ease;
  
  // 按钮内容包装器
  &-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
  }
  
  // 加载图标
  &-loading {
    display: inline-block;
    width: 28rpx;
    height: 28rpx;
    margin-right: 10rpx;
    border: 3rpx solid rgba(255, 255, 255, 0.6);
    border-left: 3rpx solid transparent;
    border-radius: 50%;
    animation: rotate 0.8s linear infinite;
  }
  
  // 类型样式
  &-primary {
    background: linear-gradient(135deg, $primary-color 0%, $primary-hover-color 100%);
    color: $text-color;
    box-shadow: 0 4rpx 12rpx rgba($primary-color, 0.3);
    
    &:active {
      background: linear-gradient(135deg, $primary-hover-color 0%, $primary-active-color 100%);
      transform: translateY(2rpx);
      box-shadow: 0 2rpx 6rpx rgba($primary-color, 0.2);
    }
    
    .uni-button-loading {
      border-color: rgba(255, 255, 255, 0.6);
      border-left-color: transparent;
    }
  }
  
  &-secondary {
    background-color: $secondary-color;
    color: $text-dark-color;
    
    &:active {
      background-color: $secondary-active-color;
      transform: translateY(2rpx);
    }
    
    .uni-button-loading {
      border-color: rgba($text-dark-color, 0.3);
      border-left-color: transparent;
    }
  }
  
  &-outline {
    background-color: transparent;
    color: $primary-color;
    border: 1rpx solid $primary-color;
    
    &:active {
      background-color: rgba($primary-color, 0.1);
      transform: translateY(2rpx);
    }
    
    .uni-button-loading {
      border-color: rgba($primary-color, 0.6);
      border-left-color: transparent;
    }
  }
  
  &-text {
    background-color: transparent;
    color: $primary-color;
    
    &:active {
      background-color: rgba($primary-color, 0.1);
      transform: translateY(2rpx);
    }
    
    .uni-button-loading {
      border-color: rgba($primary-color, 0.6);
      border-left-color: transparent;
    }
  }
  
  // 尺寸样式
  &-normal {
    height: 88rpx;
    line-height: 88rpx;
    font-size: 32rpx;
    padding: 0 30rpx;
  }
  
  &-small {
    height: 72rpx;
    line-height: 72rpx;
    font-size: 28rpx;
    padding: 0 24rpx;
  }
  
  &-mini {
    height: 60rpx;
    line-height: 60rpx;
    font-size: 24rpx;
    padding: 0 16rpx;
  }
  
  // 禁用状态
  &-disabled {
    opacity: 0.7;
    cursor: not-allowed;
    
    &.uni-button-primary {
      background: $disabled-bg-color;
      color: $text-disabled-color;
      box-shadow: none;
    }
    
    &.uni-button-secondary {
      background: $disabled-bg-color;
      color: $text-disabled-color;
    }
    
    &.uni-button-outline,
    &.uni-button-text {
      color: $text-disabled-color;
      border-color: $text-disabled-color;
    }
    
    &:active {
      transform: none;
    }
  }
}

/* 暗黑模式适配 */
@media (prefers-color-scheme: dark) {
  .uni-button {
    &-secondary {
      background-color: #2d2d2d;
      color: #f0f0f0;
      
      &:active {
        background-color: #3a3a3a;
      }
      
      .uni-button-loading {
        border-color: rgba(240, 240, 240, 0.3);
        border-left-color: transparent;
      }
    }
    
    &-disabled {
      &.uni-button-primary {
        background: #3a3a3a;
        color: #7a7a7a;
      }
      
      &.uni-button-secondary {
        background: #3a3a3a;
        color: #7a7a7a;
      }
      
      &.uni-button-outline,
      &.uni-button-text {
        color: #7a7a7a;
        border-color: #7a7a7a;
      }
    }
  }
}
</style> 