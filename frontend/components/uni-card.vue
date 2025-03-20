<template>
  <view 
    class="uni-card" 
    :class="[shadow ? 'uni-card-shadow' : '', customClass]" 
    @tap="onClick"
  >
    <view v-if="title || extra || $slots.title || $slots.extra" class="uni-card-header">
      <view class="uni-card-header-box">
        <slot name="title">
          <text class="uni-card-title">{{ title }}</text>
        </slot>
        <slot name="extra">
          <text class="uni-card-extra" v-if="extra">{{ extra }}</text>
        </slot>
      </view>
    </view>
    <view class="uni-card-content" :class="{'uni-card-content-padding': padding}">
      <slot></slot>
    </view>
    <view class="uni-card-footer" v-if="note || $slots.footer">
      <slot name="footer">
        <text class="uni-card-note">{{ note }}</text>
      </slot>
    </view>
  </view>
</template>

<script>
export default {
  name: 'UniCard',
  emits: ['click'],
  props: {
    title: {
      type: String,
      default: ''
    },
    extra: {
      type: String,
      default: ''
    },
    note: {
      type: String,
      default: ''
    },
    thumbnail: {
      type: String,
      default: ''
    },
    padding: {
      type: Boolean,
      default: true
    },
    shadow: {
      type: Boolean,
      default: true
    },
    customClass: {
      type: String,
      default: ''
    }
  },
  methods: {
    onClick(e) {
      this.$emit('click', e)
    }
  }
}
</script>

<style lang="scss">
$card-bg-color: #ffffff;
$card-border-color: #e5e5e5;
$card-shadow-color: rgba(74, 163, 255, 0.15);
$card-hover-shadow-color: rgba(74, 163, 255, 0.25);
$card-title-color: #333333;
$card-header-bg-color: rgba(74, 163, 255, 0.1);
$card-content-color: #666666;
$card-note-color: #999999;

.uni-card {
  margin: 20rpx;
  position: relative;
  display: flex;
  flex-direction: column;
  background-color: $card-bg-color;
  border-radius: 16rpx;
  border: 1rpx solid $card-border-color;
  overflow: hidden;
  transition: all 0.3s ease;
  
  &:active {
    transform: translateY(2rpx);
  }
  
  &-shadow {
    box-shadow: 0 4rpx 12rpx $card-shadow-color;
    
    &:hover {
      box-shadow: 0 6rpx 16rpx $card-hover-shadow-color;
    }
  }
  
  &-header {
    display: flex;
    position: relative;
    flex-direction: row;
    align-items: center;
    padding: 24rpx;
    background-color: $card-header-bg-color;
    border-bottom: 1rpx solid $card-border-color;
    
    &-box {
      display: flex;
      flex: 1;
      flex-direction: row;
      align-items: center;
      justify-content: space-between;
    }
  }
  
  &-title {
    font-size: 32rpx;
    font-weight: 600;
    color: $card-title-color;
    line-height: 1.2;
  }
  
  &-extra {
    font-size: 26rpx;
    color: $card-note-color;
  }
  
  &-content {
    color: $card-content-color;
    
    &-padding {
      padding: 24rpx;
    }
  }
  
  &-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20rpx 24rpx;
    border-top: 1rpx solid $card-border-color;
  }
  
  &-note {
    font-size: 26rpx;
    color: $card-note-color;
  }
}

/* 暗黑模式适配 */
@media (prefers-color-scheme: dark) {
  .uni-card {
    background-color: #1f1f1f;
    border-color: #333333;
    
    &-header {
      background-color: rgba(74, 163, 255, 0.05);
      border-bottom-color: #333333;
    }
    
    &-shadow {
      box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.4);
      
      &:hover {
        box-shadow: 0 6rpx 16rpx rgba(74, 163, 255, 0.15);
      }
    }
    
    &-title {
      color: #f0f0f0;
    }
    
    &-content {
      color: #d0d0d0;
    }
    
    &-footer {
      border-top-color: #333333;
    }
    
    &-note {
      color: #b0b0b0;
    }
  }
}
</style> 