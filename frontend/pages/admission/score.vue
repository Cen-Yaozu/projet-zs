<template>
  <view class="container">
    <!-- 顶部导航栏 -->
    <view class="header">
      <text class="title">历年分数</text>
    </view>
    
    <!-- 年份和模式选择器 -->
    <view class="filter-bar">
      <view class="year-selector">
        <uni-icons type="calendar" size="20" color="#0f3c88"></uni-icons>
        <text class="year-text">{{ currentYear }}</text>
      </view>
      
      <view class="display-mode">
        <uni-icons type="list" size="20" color="#f89d35"></uni-icons>
        <text class="mode-text">表格式</text>
      </view>
    </view>
    
    <!-- 加载中提示 -->
    <view v-if="loading" class="loading">
      <uni-load-more status="loading" :content-text="loadingText"></uni-load-more>
    </view>
    
    <!-- 无数据提示 -->
    <view v-else-if="processedData.length === 0" class="empty-data">
      <text>暂无数据</text>
    </view>
    
    <!-- 分数数据展示 -->
    <view v-else class="score-list">
      <!-- 按专业分组展示 -->
      <view 
        class="score-item" 
        v-for="(item, index) in processedData" 
        :key="index"
      >
        <!-- 专业名称 -->
        <view class="major-name">
          <view class="major-indicator"></view>
          <text>{{ item.majorName }}</text>
        </view>
        
        <!-- 分数详情 -->
        <view class="score-details">
          <!-- 左侧信息 -->
          <view class="detail-col">
            <view class="detail-row">
              <text class="label">年份：</text>
              <text class="value blue">{{ item.year }}</text>
            </view>
            <view class="detail-row">
              <text class="label">录取人数：</text>
              <text class="value blue">{{ item.planNumber || '0' }}</text>
            </view>
            <view class="detail-row">
              <text class="label">最低分：</text>
              <text class="value blue">{{ item.minScore || '-' }}</text>
            </view>
            <view class="detail-row">
              <text class="label">首选科目：</text>
              <text class="value blue">{{ item.firstSubject || '物理' }}</text>
            </view>
          </view>
          
          <!-- 右侧信息 -->
          <view class="detail-col">
            <view class="detail-row">
              <text class="label">省份：</text>
              <text class="value blue">{{ item.province || '-' }}</text>
            </view>
            <view class="detail-row">
              <text class="label">最高分：</text>
              <text class="value blue">{{ item.maxScore || item.minScore || '-' }}</text>
            </view>
            <view class="detail-row">
              <text class="label">省控线：</text>
              <text class="value blue">{{ item.provinceControlLine || '475' }}</text>
            </view>
            <view class="detail-row">
              <text class="label">再选科目：</text>
              <text class="value blue">{{ item.secondSubject || '化学' }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>
    
    <!-- 年份选择弹窗 -->
    <uni-popup ref="yearPopup" type="bottom">
      <view class="popup-content">
        <view class="popup-title">
          <text>选择年份</text>
        </view>
        <view class="year-list">
          <view 
            v-for="year in years" 
            :key="year"
            class="year-item"
            :class="{ active: currentYear === year }"
            @tap="selectYear(year)"
          >
            <text>{{ year }}年</text>
          </view>
        </view>
        <view class="popup-btn" @tap="closeYearPopup">
          <text>取消</text>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script>
import admissionApi from '@/api/admission';

export default {
  data() {
    return {
      years: [2024, 2023, 2022, 2021, 2020], // 默认可用年份
      currentYear: 2024, // 默认当前年份
      scoreData: {},
      processedData: [], // 处理后的展示数据
      loading: true,
      schoolId: 1, // 默认学校ID，可从配置或全局状态获取
      loadingText: {
        contentdown: '加载更多',
        contentrefresh: '正在加载...',
        contentnomore: '没有更多数据了'
      }
    }
  },
  onLoad() {
    this.initData();
  },
  methods: {
    // 初始化数据
    async initData() {
      this.loading = true;
      try {
        await this.loadScoreData(this.currentYear);
      } catch (err) {
        console.error('初始化数据失败:', err);
        uni.showToast({
          title: '数据加载失败',
          icon: 'none'
        });
      } finally {
        this.loading = false;
      }
    },
    
    // 加载指定年份的分数数据
    async loadScoreData(year) {
      if (this.scoreData[year]) {
        this.processedData = this.scoreData[year];
        return; // 如果数据已加载，直接返回
      }
      
      this.loading = true;
      try {
        // 使用新的API获取专业分数线数据
        const res = await admissionApi.getMajorScores(year, this.schoolId);
        
        // 直接使用后端返回的数据，无需额外处理
        this.scoreData[year] = res;
        this.processedData = res;
      } catch (err) {
        console.error('获取分数数据失败:', err);
        uni.showToast({
          title: '分数数据加载失败',
          icon: 'none'
        });
        // 设置默认数据用于展示
        this.processedData = this.getDefaultData();
      } finally {
        this.loading = false;
      }
    },
    
    // 选择年份
    selectYear(year) {
      if (this.currentYear === year) return;
      
      this.currentYear = year;
      this.loadScoreData(year);
      this.closeYearPopup();
    },
    
    // 打开年份选择弹窗
    openYearPopup() {
      this.$refs.yearPopup.open();
    },
    
    // 关闭年份选择弹窗
    closeYearPopup() {
      this.$refs.yearPopup.close();
    },
    
    // 获取默认数据（用于演示或测试）
    getDefaultData() {
      return [
        {
          majorName: '航海技术',
          year: 2024,
          province: '天津市',
          planNumber: 1,
          minScore: 542,
          maxScore: 542,
          provinceControlLine: 475,
          firstSubject: '物理',
          secondSubject: '化学'
        },
        {
          majorName: '轮机工程',
          year: 2024,
          province: '天津市',
          planNumber: 1,
          minScore: 537,
          maxScore: 537,
          provinceControlLine: 475,
          firstSubject: '物理',
          secondSubject: '化学'
        },
        {
          majorName: '船舶电子电气工程',
          year: 2024,
          province: '天津市',
          planNumber: 1,
          minScore: 530,
          maxScore: 530,
          provinceControlLine: 475,
          firstSubject: '物理',
          secondSubject: '化学'
        }
      ];
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
  background: #3399cc;
  padding: 30rpx;
  color: white;
  text-align: center;
  
  .title {
    font-size: 36rpx;
    font-weight: bold;
  }
}

.filter-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  background: #fff;
  margin-bottom: 20rpx;
  
  .year-selector, .display-mode {
    display: flex;
    align-items: center;
    gap: 10rpx;
  }
  
  .year-text, .mode-text {
    font-size: 30rpx;
  }
}

.score-list {
  padding: 0 20rpx;
}

.score-item {
  background: #fff;
  border-radius: 12rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
  
  .major-name {
    display: flex;
    align-items: center;
    padding: 20rpx;
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    
    .major-indicator {
      width: 8rpx;
      height: 36rpx;
      background: #4a7bff;
      margin-right: 20rpx;
      border-radius: 4rpx;
    }
  }
  
  .score-details {
    display: flex;
    padding: 0 20rpx 20rpx;
    
    .detail-col {
      flex: 1;
    }
    
    .detail-row {
      margin: 16rpx 0;
      
      .label {
        font-size: 28rpx;
        color: #666;
      }
      
      .value {
        font-size: 28rpx;
        
        &.blue {
          color: #4a7bff;
        }
      }
    }
  }
}

.loading, .empty-data {
  height: 300rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
  font-size: 28rpx;
}

.popup-content {
  background: #fff;
  border-radius: 20rpx 20rpx 0 0;
  padding-bottom: env(safe-area-inset-bottom);
  
  .popup-title {
    padding: 30rpx;
    text-align: center;
    font-size: 32rpx;
    font-weight: bold;
    border-bottom: 1rpx solid #f0f0f0;
  }
  
  .year-list {
    display: flex;
    flex-wrap: wrap;
    padding: 20rpx;
    
    .year-item {
      width: 25%;
      padding: 20rpx 0;
      text-align: center;
      font-size: 28rpx;
      
      &.active {
        color: #4a7bff;
        font-weight: bold;
      }
    }
  }
  
  .popup-btn {
    padding: 30rpx;
    text-align: center;
    font-size: 32rpx;
    color: #999;
    border-top: 1rpx solid #f0f0f0;
  }
}
</style> 