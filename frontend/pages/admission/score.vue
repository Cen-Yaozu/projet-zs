<template>
  <view class="container">
    <view class="header">
      <text class="title">历年分数</text>
    </view>
    
    <view class="score-section">
      <view class="year-selector">
        <text 
          v-for="year in years" 
          :key="year"
          class="year-item"
          :class="{ active: currentYear === year }"
          @tap="selectYear(year)"
        >{{ year }}年</text>
      </view>
      
      <view class="score-table">
        <view class="table-header">
          <text class="col">专业名称</text>
          <text class="col">最高分</text>
          <text class="col">最低分</text>
          <text class="col">平均分</text>
        </view>
        <view 
          class="table-row" 
          v-for="(item, index) in scoreData[currentYear]" 
          :key="index"
        >
          <text class="col">{{ item.major }}</text>
          <text class="col">{{ item.highest }}</text>
          <text class="col">{{ item.lowest }}</text>
          <text class="col">{{ item.average }}</text>
        </view>
      </view>
      
      <view class="note">
        <text class="note-title">说明：</text>
        <text class="note-text">1. 以上分数为广东省文理科录取分数</text>
        <text class="note-text">2. 分数仅供参考，具体以当年实际录取分数为准</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      years: [2023, 2022, 2021],
      currentYear: 2023,
      scoreData: {
        2023: [
          { major: '航海技术', highest: 568, lowest: 535, average: 549 },
          { major: '轮机工程', highest: 558, lowest: 525, average: 539 },
          { major: '船舶与海洋工程', highest: 572, lowest: 538, average: 552 },
          { major: '物流管理', highest: 565, lowest: 532, average: 545 }
        ],
        2022: [
          { major: '航海技术', highest: 565, lowest: 532, average: 546 },
          { major: '轮机工程', highest: 555, lowest: 522, average: 536 },
          { major: '船舶与海洋工程', highest: 570, lowest: 535, average: 550 },
          { major: '物流管理', highest: 562, lowest: 530, average: 543 }
        ],
        2021: [
          { major: '航海技术', highest: 562, lowest: 530, average: 543 },
          { major: '轮机工程', highest: 552, lowest: 520, average: 534 },
          { major: '船舶与海洋工程', highest: 568, lowest: 533, average: 548 },
          { major: '物流管理', highest: 560, lowest: 528, average: 541 }
        ]
      }
    }
  },
  methods: {
    selectYear(year) {
      this.currentYear = year
    }
  }
}
</script>

<style lang="scss">
.container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 40rpx;
}

.header {
  background: #fff;
  padding: 40rpx 30rpx;
  margin-bottom: 20rpx;
  
  .title {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
  }
}

.score-section {
  background: #fff;
  padding: 30rpx;
  
  .year-selector {
    display: flex;
    gap: 20rpx;
    margin-bottom: 30rpx;
    
    .year-item {
      padding: 12rpx 30rpx;
      font-size: 28rpx;
      color: #666;
      background: #f5f5f5;
      border-radius: 8rpx;
      
      &.active {
        background: #4aa3ff;
        color: #fff;
      }
    }
  }
  
  .score-table {
    border: 1rpx solid #f0f0f0;
    border-radius: 8rpx;
    
    .table-header {
      display: flex;
      background: #f8f8f8;
      
      .col {
        flex: 1;
        padding: 20rpx;
        text-align: center;
        font-size: 28rpx;
        color: #333;
        font-weight: bold;
      }
    }
    
    .table-row {
      display: flex;
      border-top: 1rpx solid #f0f0f0;
      
      .col {
        flex: 1;
        padding: 20rpx;
        text-align: center;
        font-size: 28rpx;
        color: #666;
      }
    }
  }
  
  .note {
    margin-top: 30rpx;
    
    .note-title {
      font-size: 28rpx;
      color: #333;
      font-weight: bold;
      margin-bottom: 10rpx;
      display: block;
    }
    
    .note-text {
      font-size: 26rpx;
      color: #666;
      line-height: 1.5;
      display: block;
    }
  }
}
</style> 