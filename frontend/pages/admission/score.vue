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
      
      <view v-if="loading" class="loading">
        <uni-load-more status="loading" :content-text="loadingText"></uni-load-more>
      </view>
      
      <view v-else-if="years.length === 0" class="empty-data">
        <text>暂无数据</text>
      </view>
      
      <view v-else class="score-table">
        <view class="table-header">
          <text class="col">专业名称</text>
          <text class="col">最低分</text>
          <text class="col">科类</text>
          <text class="col">省份</text>
        </view>
        <view 
          class="table-row" 
          v-for="(item, index) in currentYearData" 
          :key="index"
        >
          <text class="col major">{{ item.major || '未知专业' }}</text>
          <text class="col">{{ item.minScore || '-' }}</text>
          <text class="col">{{ item.category || '-' }}</text>
          <text class="col">{{ item.province || '-' }}</text>
        </view>
      </view>
      
      <view class="note">
        <text class="note-title">说明：</text>
        <text class="note-text">1. 以上分数为录取最低分数线</text>
        <text class="note-text">2. 分数仅供参考，具体以当年实际录取分数为准</text>
      </view>
    </view>
  </view>
</template>

<script>
import admissionApi from '@/api/admission';

export default {
  data() {
    return {
      years: [],
      currentYear: null,
      scoreData: {},
      loading: true,
      schoolId: 1, // 默认学校ID，可从配置或全局状态获取
      loadingText: {
        contentdown: '加载更多',
        contentrefresh: '正在加载...',
        contentnomore: '没有更多数据了'
      }
    }
  },
  computed: {
    currentYearData() {
      return this.scoreData[this.currentYear] || [];
    }
  },
  created() {
    this.initData();
  },
  methods: {
    // 初始化数据
    async initData() {
      this.loading = true;
      try {
        await this.loadAvailableYears();
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
    
    // 加载可用年份
    async loadAvailableYears() {
      try {
        const res = await admissionApi.getAvailableYears(this.schoolId);
        
        // 从政策数据中提取年份并去重排序
        this.years = [...new Set(res.map(item => item.year))].sort((a, b) => b - a);
        
        if (this.years.length > 0) {
          this.currentYear = this.years[0]; // 默认选择最新年份
          await this.loadScoreData(this.currentYear);
        }
      } catch (err) {
        console.error('获取年份数据失败:', err);
        throw err;
      }
    },
    
    // 加载指定年份的分数数据
    async loadScoreData(year) {
      if (this.scoreData[year]) {
        return; // 如果数据已加载，直接返回
      }
      
      this.loading = true;
      try {
        const res = await admissionApi.getScoresByYear(year, this.schoolId);
        
        // 处理数据，按专业分组，从AdmissionPolicy数据转换为前端所需格式
        // 为简化处理，我们直接使用后端数据，前端做适应性展示
        this.scoreData[year] = this.processScoreData(res);
      } catch (err) {
        console.error('获取分数数据失败:', err);
        uni.showToast({
          title: '分数数据加载失败',
          icon: 'none'
        });
      } finally {
        this.loading = false;
      }
    },
    
    // 处理分数数据，后端返回的是政策数据，我们需要转换为分数展示数据
    processScoreData(policies) {
      // 这里可以根据实际需求调整数据处理逻辑
      return policies.map(policy => {
        return {
          major: policy.policyContent ? policy.policyContent.split('\n')[0] : '未知专业', // 从政策内容中提取专业名称
          minScore: policy.minScore,
          category: policy.category,
          province: policy.province
        };
      });
    },
    
    // 选择年份
    async selectYear(year) {
      if (this.currentYear === year) return;
      
      this.currentYear = year;
      if (!this.scoreData[year]) {
        await this.loadScoreData(year);
      }
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
    flex-wrap: wrap;
    
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
  
  .loading, .empty-data {
    height: 300rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #666;
    font-size: 28rpx;
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
        
        &.major {
          text-align: left;
        }
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