<template>
  <view
    class="zs-button"
    :class="[
      `zs-button--${type}`,
      `zs-button--${size}`,
      {
        'zs-button--block': block,
        'zs-button--round': round,
        'zs-button--circle': circle,
        'zs-button--loading': loading,
        'zs-button--disabled': disabled
      }
    ]"
    :style="buttonStyle"
    :hover-class="disabled || loading ? '' : 'zs-button--active'"
    @tap="handleTap"
  >
    <view class="zs-button__content">
      <view v-if="loading" class="zs-button__loading">
        <view class="zs-loading-circle"></view>
      </view>
      <text v-else-if="icon" class="zs-button__icon" :class="icon"></text>
      <text v-if="text" class="zs-button__text">{{ text }}</text>
      <slot v-else></slot>
    </view>
  </view>
</template>

<script>
export default {
  name: 'zs-button',
  props: {
    text: {
      type: String,
      default: ''
    },
    type: {
      type: String,
      default: 'primary',
      validator: value => {
        return ['primary', 'success', 'warning', 'error', 'info', 'default', 'text'].includes(value);
      }
    },
    size: {
      type: String,
      default: 'medium',
      validator: value => {
        return ['mini', 'small', 'medium', 'large'].includes(value);
      }
    },
    icon: {
      type: String,
      default: ''
    },
    color: {
      type: String,
      default: ''
    },
    block: {
      type: Boolean,
      default: false
    },
    round: {
      type: Boolean,
      default: false
    },
    circle: {
      type: Boolean,
      default: false
    },
    loading: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Boolean,
      default: false
    },
    plain: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    buttonStyle() {
      const style = {};
      if (this.color) {
        if (this.plain) {
          style.color = this.color;
          style.borderColor = this.color;
          style.backgroundColor = 'transparent';
        } else {
          style.color = '#ffffff';
          style.backgroundColor = this.color;
          style.borderColor = this.color;
        }
      }
      return style;
    }
  },
  methods: {
    handleTap(e) {
      if (this.disabled || this.loading) return;
      this.$emit('tap', e);
      this.$emit('click', e);
    }
  }
};
</script>

<style lang="scss">
@import '../styles/common.scss';

.zs-button {
  position: relative;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border: 1px solid transparent;
  transition: $transition-base;
  padding: 0 30rpx;
  overflow: hidden;
  
  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    opacity: 0;
    border-radius: inherit;
    background-color: $black;
    transition: opacity 0.2s;
  }
  
  &--active::after {
    opacity: 0.1;
  }
  
  &--disabled {
    cursor: not-allowed;
    opacity: 0.6;
  }
  
  &__content {
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1;
  }
  
  &__loading {
    margin-right: 8rpx;
  }
  
  &__icon {
    margin-right: 8rpx;
  }
  
  &__text {
    line-height: 1;
  }
  
  // 按钮类型
  &--primary {
    color: $white;
    background-color: $primary-color;
    border-color: $primary-color;
    box-shadow: $shadow-primary;
    
    &.zs-button--plain {
      color: $primary-color;
      background-color: transparent;
      border-color: $primary-color;
      box-shadow: none;
    }
  }
  
  &--success {
    color: $white;
    background-color: $success-color;
    border-color: $success-color;
    
    &.zs-button--plain {
      color: $success-color;
      background-color: transparent;
      border-color: $success-color;
    }
  }
  
  &--warning {
    color: $white;
    background-color: $warning-color;
    border-color: $warning-color;
    
    &.zs-button--plain {
      color: $warning-color;
      background-color: transparent;
      border-color: $warning-color;
    }
  }
  
  &--error {
    color: $white;
    background-color: $error-color;
    border-color: $error-color;
    
    &.zs-button--plain {
      color: $error-color;
      background-color: transparent;
      border-color: $error-color;
    }
  }
  
  &--info {
    color: $white;
    background-color: $info-color;
    border-color: $info-color;
    
    &.zs-button--plain {
      color: $info-color;
      background-color: transparent;
      border-color: $info-color;
    }
  }
  
  &--default {
    color: $gray-800;
    background-color: $white;
    border-color: $gray-300;
    
    &.zs-button--plain {
      color: $gray-800;
      background-color: transparent;
    }
  }
  
  &--text {
    color: $primary-color;
    background-color: transparent;
    border-color: transparent;
    padding: 0;
    box-shadow: none;
  }
  
  // 按钮尺寸
  &--mini {
    height: 48rpx;
    font-size: $font-size-mini;
    border-radius: $radius-small;
  }
  
  &--small {
    height: 64rpx;
    font-size: $font-size-small;
    border-radius: $radius-base;
  }
  
  &--medium {
    height: 80rpx;
    font-size: $font-size-base;
    border-radius: $radius-medium;
  }
  
  &--large {
    height: 96rpx;
    font-size: $font-size-medium;
    border-radius: $radius-large;
  }
  
  &--block {
    display: flex;
    width: 100%;
  }
  
  &--round {
    border-radius: 100rpx;
  }
  
  &--circle {
    width: 80rpx;
    padding: 0;
    border-radius: 50%;
    
    &.zs-button--mini {
      width: 48rpx;
    }
    
    &.zs-button--small {
      width: 64rpx;
    }
    
    &.zs-button--large {
      width: 96rpx;
    }
  }
}

// 加载动画
.zs-loading-circle {
  display: inline-block;
  width: 30rpx;
  height: 30rpx;
  border: 2rpx solid;
  border-color: currentColor currentColor currentColor transparent;
  border-radius: 50%;
  animation: zs-loading 0.8s linear infinite;
}

@keyframes zs-loading {
  0% {
    transform: rotate(0);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style> 