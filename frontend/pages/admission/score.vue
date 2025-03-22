<template>
  <view class="container">
    <!-- 顶部导航栏 -->
    <view class="header">
      <text class="title">广州航海学院历年分数</text>
    </view>
    
    <!-- 年份和模式选择器 -->
    <view class="filter-bar">
      <view class="year-selector" @tap="openYearPopup">
        <uni-icons type="calendar" size="20" color="#0f3c88"></uni-icons>
        <text class="year-text">{{ currentYear || '选择年份' }}</text>
      </view>
      
      <view class="display-mode" @tap="toggleDisplayMode">
        <uni-icons :type="displayMode === 'card' ? 'list' : 'grid'" size="20" color="#f89d35"></uni-icons>
        <text class="mode-text">{{ displayMode === 'card' ? '表格式' : '卡片式' }}</text>
      </view>
    </view>
    
    <!-- 指引提示 -->
    <view v-if="!currentYear" class="guide-tips">
      <text>请选择年份查看分数数据</text>
    </view>
    
    <!-- 加载中提示 -->
    <view v-else-if="loading" class="loading">
      <uni-load-more status="loading" :content-text="loadingText"></uni-load-more>
    </view>
    
    <!-- 无数据提示 -->
    <view v-else-if="processedData.length === 0" class="empty-data">
      <text>暂无数据</text>
    </view>
    
    <!-- 卡片式视图 -->
    <view v-else-if="displayMode === 'card'" class="score-list">
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
    
    <!-- 表格式视图 -->
    <view v-else class="table-view">
      <!-- 顶部筛选与模式 -->
      <view class="table-top-bar">
        <view class="year-filter">
          <uni-icons type="calendar" size="20" color="#333"></uni-icons>
          <text class="year-text">{{ currentYear }}</text>
        </view>
        <view class="mode-switch" @tap="toggleDisplayMode">
          <uni-icons type="grid" size="20" color="#f89d35"></uni-icons>
          <text class="mode-text">卡片式</text>
        </view>
      </view>
      
      <!-- 表格头部 -->
      <view class="table-header">
        <view class="table-cell">年份</view>
        <view class="table-cell">科类</view>
        <view class="table-cell">省份</view>
        <view class="table-cell">专业名称</view>
      </view>
      
      <!-- 表格数据行 -->
      <view v-for="(item, index) in tableData" :key="index" class="table-row">
        <view class="table-cell">{{ item.year }}</view>
        <view class="table-cell">{{ item.category || '理科' }}</view>
        <view class="table-cell">{{ item.province }}</view>
        <view class="table-cell">{{ item.majorName }}</view>
      </view>
    </view>
    
    <!-- 年份选择弹窗 - 自定义实现，不依赖uni-popup -->
    <view class="custom-popup" v-if="showYearSelect" @click.stop="closeYearPopup">
      <view class="popup-mask"></view>
      <view class="popup-content" @click.stop>
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
        <view class="popup-cancel-btn" @tap="closeYearPopup">
          <text>取消</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import admissionApi from '@/api/admission';
import request from '@/api/request';

export default {
  data() {
    return {
      years: [], // 可用年份列表
      currentYear: null, // 当前选中年份
      scoreData: {}, // 缓存的分数数据
      processedData: [], // 处理后的展示数据
      loading: true, // 加载状态
      displayMode: 'card', // 显示模式：card-卡片式，table-表格式
      showYearSelect: false, // 控制年份选择弹窗显示
      tableData: [], // 表格数据
      majorMap: {}, // 专业ID到名称的映射
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
        // 1. 先获取专业列表和映射
        await this.fetchMajorMap();
        
        // 2. 获取年份列表
        await this.fetchYears();
        
        // 3. 如果有年份数据，加载第一个年份的数据
        if (this.years.length > 0 && this.currentYear) {
          await this.loadScoreData(this.currentYear);
        }
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
    
    // 获取专业映射
    async fetchMajorMap() {
      try {
        // 获取所有专业信息
        const res = await request.get('/api/major-info/list');
        if (res && Array.isArray(res)) {
          // 创建ID到名称的映射
          const map = {};
          res.forEach(major => {
            map[major.id] = major.name;
          });
          this.majorMap = map;
          console.log('专业映射数据:', map);
        } else {
          console.error('获取专业数据格式错误', res);
        }
      } catch (err) {
        console.error('获取专业映射失败:', err);
        uni.showToast({
          title: '专业数据加载失败',
          icon: 'none'
        });
      }
    },
    
    // 确保数据源已初始化
    async ensureDataSource() {
      // 先检查专业分数表是否有数据
      try {
        const scores = await admissionApi.getAllMajorScores();
        if (!scores || scores.length === 0) {
          // 如果没有数据，尝试填充历史数据
          console.log('正在初始化专业分数数据源...');
          await admissionApi.fillHistoricalScoreData();
          console.log('专业分数数据源初始化完成');
        }
      } catch (err) {
        console.error('检查数据源失败:', err);
      }
    },
    
    // 获取年份列表
    async fetchYears() {
      try {
        // 使用API获取招生政策数据，不再需要传入学校ID
        const res = await admissionApi.getAvailableYears();
        if (res && res.length > 0) {
          // 提取所有年份并去重
          const availableYears = [...new Set(res.map(item => item.year))].sort((a, b) => b - a);
          if (availableYears.length > 0) {
            this.years = availableYears;
            this.currentYear = availableYears[0]; // 默认选中最新的年份
            return;
          }
        }
        // 如果没有获取到数据，显示提示
        this.years = [];
        this.currentYear = null;
        uni.showToast({
          title: '暂无年份数据',
          icon: 'none'
        });
      } catch (err) {
        console.error('获取年份数据失败:', err);
        this.years = [];
        this.currentYear = null;
        uni.showToast({
          title: '年份数据加载失败',
          icon: 'none'
        });
      }
    },
    
    // 加载指定年份的分数数据
    async loadScoreData(year) {
      if (!year) {
        uni.showToast({
          title: '请选择年份',
          icon: 'none'
        });
        return;
      }
      
      const cacheKey = `${year}`;
      if (this.scoreData[cacheKey]) {
        this.processedData = this.scoreData[cacheKey];
        this.updateTableData();
        return; // 如果数据已加载，直接返回
      }
      
      this.loading = true;
      try {
        // 使用API获取专业分数线数据，不再需要传入学校ID
        const res = await admissionApi.getMajorScores(year);
        console.log('API返回的原始数据:', JSON.stringify(res));
        
        // 检查返回的数据格式并进行处理
        if (res && Array.isArray(res)) {
          // 添加专业名称信息
          const processedData = res.map(item => {
            // 添加专业名称
            return {
              ...item,
              majorName: this.getMajorNameById(item.majorId)
            };
          });
          
          console.log('处理后的前端显示数据:', JSON.stringify(processedData));
          this.scoreData[cacheKey] = processedData;
          this.processedData = processedData;
          
          // 更新表格数据
          this.updateTableData();
        } else {
          console.error('API返回的数据格式不是数组', res);
          this.processedData = [];
          this.tableData = [];
          uni.showToast({
            title: '暂无分数数据',
            icon: 'none'
          });
        }
      } catch (err) {
        console.error('获取分数数据失败:', err);
        this.processedData = [];
        this.tableData = [];
        uni.showToast({
          title: '分数数据加载失败',
          icon: 'none'
        });
      } finally {
        this.loading = false;
      }
    },
    
    // 更新表格数据
    updateTableData() {
      this.tableData = this.processedData.map(item => ({
        year: item.year,
        category: item.firstSubject === '物理' ? '理科' : '文科',
        province: item.province,
        majorName: item.majorName
      }));
    },
    
    // 切换显示模式
    toggleDisplayMode() {
      this.displayMode = this.displayMode === 'card' ? 'table' : 'card';
    },
    
    // 打开年份选择弹窗
    openYearPopup() {
      this.showYearSelect = true;
    },
    
    // 关闭年份选择弹窗
    closeYearPopup() {
      console.log('关闭年份选择弹窗');
      this.showYearSelect = false;
    },
    
    // 选择年份
    selectYear(year) {
      if (this.currentYear === year) {
        this.closeYearPopup();
        return;
      }
      
      this.currentYear = year;
      this.loadScoreData(year);
      this.closeYearPopup();
    },
    
    // 根据ID获取专业名称
    getMajorNameById(majorId) {
      // 使用从数据库获取的映射
      return this.majorMap[majorId] || `专业ID:${majorId}`;
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

// 添加指引提示样式
.guide-tips {
  text-align: center;
  padding: 80rpx 30rpx;
  color: #999;
  font-size: 28rpx;
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

/* 表格式视图样式 */
.table-view {
  background: #fff;
  border-radius: 0;
  overflow: hidden;
  margin-bottom: 20rpx;
  
  .table-top-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20rpx 30rpx;
    background: #fff;
    border-bottom: 2rpx solid #f1f1f1;
    
    .year-filter {
      display: flex;
      align-items: center;
      gap: 10rpx;
    }
    
    .year-text {
      font-size: 30rpx;
      font-weight: bold;
    }
    
    .mode-switch {
      display: flex;
      align-items: center;
      gap: 8rpx;
      color: #f89d35;
    }
    
    .mode-text {
      font-size: 28rpx;
    }
  }
  
  .table-header {
    display: flex;
    background: #f8f8f8;
    font-size: 28rpx;
    color: #666;
    padding: 25rpx 0;
    font-weight: normal;
    border-bottom: 2rpx solid #eee;
  }
  
  .table-row {
    display: flex;
    font-size: 28rpx;
    color: #333;
    border-bottom: 2rpx solid #f5f5f5;
    
    &:last-child {
      border-bottom: none;
    }
  }
  
  .table-cell {
    flex: 1;
    padding: 25rpx 10rpx;
    text-align: center;
    display: flex;
    align-items: center;
    justify-content: center;
    word-break: break-all;
    line-height: 1.4;
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

/* 自定义弹窗样式 */
.custom-popup {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 999;
  
  .popup-mask {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
  }
  
  .popup-content {
    position: absolute;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: #fff;
    border-radius: 20rpx 20rpx 0 0;
    transform: translateY(0);
    transition: transform 0.3s ease;
    
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
    
    .popup-cancel-btn {
      padding: 30rpx;
      text-align: center;
      font-size: 32rpx;
      color: #333;
      border-top: 1rpx solid #f0f0f0;
      background-color: #fff;
      font-weight: 500;
      
      &:active {
        background-color: #f5f5f5;
      }
    }
  }
}
</style> 