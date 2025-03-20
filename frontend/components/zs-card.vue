<template>
  <view 
    class="zs-card" 
    :class="[
      `zs-card--${shadow}`,
      {
        'zs-card--full': full,
        'zs-card--border': border,
        'zs-card--shadow': shadow !== 'none'
      }
    ]"
    :style="cardStyle"
    @tap="handleTap"
  >
    <view v-if="$slots.cover || cover" class="zs-card__cover">
      <slot name="cover">
        <image v-if="cover" class="zs-card__cover-image" :src="cover" mode="aspectFill"></image>
      </slot>
    </view>
    
    <view v-if="$slots.header || title || extra" class="zs-card__header">
      <slot name="header">
        <view class="zs-card__header-wrapper">
          <text v-if="title" class="zs-card__title">{{ title }}</text>
          <text v-if="extra" class="zs-card__extra">{{ extra }}</text>
        </view>
      </slot>
    </view>
    
    <view class="zs-card__body" :style="bodyStyle">
      <slot></slot>
    </view>
    
    <view v-if="$slots.footer" class="zs-card__footer">
      <slot name="footer"></slot>
    </view>
  </view>
</template>

<script>
export default {
  name: 'zs-card',
  props: {
    title: {
      type: String,
      default: ''
    },
    extra: {
      type: String,
      default: ''
    },
    cover: {
      type: String,
      default: ''
    },
    shadow: {
      type: String,
      default: 'always',
      validator: value => {
        return ['always', 'hover', 'none'].includes(value);
      }
    },
    border: {
      type: Boolean,
      default: true
    },
    full: {
      type: Boolean,
      default: false
    },
    padding: {
      type: String,
      default: '24rpx'
    },
    borderRadius: {
      type: String,
      default: '12rpx'
    },
    background: {
      type: String,
      default: ''
    }
  },
  computed: {
    cardStyle() {
      const style = {};
      if (this.borderRadius) {
        style.borderRadius = this.borderRadius;
      }
      if (this.background) {
        style.backgroundColor = this.background;
      }
      return style;
    },
    bodyStyle() {
      return {
        padding: this.padding
      };
    }
  },
  methods: {
    handleTap(e) {
      this.$emit('tap', e);
      this.$emit('click', e);
    }
  }
};
</script>

<style lang="scss">
@import '../styles/common.scss';

.zs-card {
  position: relative;
  box-sizing: border-box;
  overflow: hidden;
  background-color: $white;
  margin-bottom: $spacing-base;
  transition: $transition-base;
  border-radius: $radius-medium;
  
  &--border {
    border: 1rpx solid $gray-200;
  }
  
  &--full {
    margin-left: -$spacing-base;
    margin-right: -$spacing-base;
    border-radius: 0;
  }
  
  &--always {
    box-shadow: $shadow-base;
  }
  
  &--hover {
    box-shadow: none;
    
    &:active {
      box-shadow: $shadow-base;
    }
  }
  
  &__cover {
    position: relative;
    width: 100%;
    height: 400rpx;
    
    &-image {
      width: 100%;
      height: 100%;
    }
  }
  
  &__header {
    display: flex;
    align-items: center;
    padding: $spacing-base $spacing-base 0 $spacing-base;
    
    &-wrapper {
      display: flex;
      align-items: center;
      justify-content: space-between;
      width: 100%;
    }
  }
  
  &__title {
    font-size: $font-size-medium;
    font-weight: 500;
    color: $gray-900;
  }
  
  &__extra {
    font-size: $font-size-small;
    color: $gray-500;
    margin-left: auto;
  }
  
  &__body {
    width: 100%;
    box-sizing: border-box;
  }
  
  &__footer {
    border-top: 1rpx solid $gray-200;
    padding: $spacing-base;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  
  &:active {
    transform: translateY(2rpx);
  }
}
</style> 