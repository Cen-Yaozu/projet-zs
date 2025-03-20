<script>
export default {
  onLaunch: function() {
    console.log('App Launch');
    
    // 初始化用户状态
    this.$store.dispatch('initUser');
    
    // 初始化系统信息
    this.$store.dispatch('getSystemInfo');
  },
  onShow: function() {
    console.log('App Show');
  },
  onHide: function() {
    console.log('App Hide');
  }
};
</script>

<style lang="scss">
@import '@/common/main.scss';

/* 引入默认样式 */
@import './static/styles/common.scss';

/* 全局样式 */
page {
  font-family: -apple-system, BlinkMacSystemFont, 'Helvetica Neue', Helvetica,
    Segoe UI, Arial, Roboto, 'PingFang SC', 'miui', 'Hiragino Sans GB', 'Microsoft Yahei',
    sans-serif;
  background-color: #f7f7f7;
}

/* 去除按钮默认边框 */
button {
  margin: 0;
  padding: 0;
  border: none;
  outline: none;
  background: transparent;
}

/* 去除按钮默认边框 */
button::after {
  border: none;
}

/* 在这里添加其他全局样式 */
.container {
  padding-bottom: 120rpx;
}

/* 处理图片加载错误 */
image {
  will-change: transform;
}

image[mode="aspectFill"].error,
image[mode="aspectFit"].error,
image[mode="scaleToFill"].error {
  background-color: #f8f8f8;
  position: relative;
}

image[mode="aspectFill"].error::before,
image[mode="aspectFit"].error::before,
image[mode="scaleToFill"].error::before {
  content: "\e656";
  font-family: "iconfont";
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  font-size: 40rpx;
  color: #ccc;
}
</style> 