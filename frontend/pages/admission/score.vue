<template>
  <view class="container">
    <!-- 顶部导航栏 -->
    <view class="header">
      <text class="title">历年分数</text>
    </view>
    
    <!-- 年份和模式选择器 -->
    <view class="filter-bar">
      <view class="year-selector" @tap="openYearPopup">
        <uni-icons type="calendar" size="20" color="#0f3c88"></uni-icons>
        <text class="year-text">{{ currentYear }}</text>
      </view>
      
      <view class="school-selector" @tap="openSchoolPopup">
        <uni-icons type="location" size="20" color="#0f3c88"></uni-icons>
        <text class="school-text">{{ selectedSchool.name || '全部学校' }}</text>
      </view>
      
      <view class="display-mode" @tap="toggleDisplayMode">
        <uni-icons :type="displayMode === 'card' ? 'list' : 'grid'" size="20" color="#f89d35"></uni-icons>
        <text class="mode-text">{{ displayMode === 'card' ? '表格式' : '卡片式' }}</text>
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
    
    <!-- 学校选择弹窗 -->
    <view class="custom-popup" v-if="showSchoolSelect" @click.stop="closeSchoolPopup">
      <view class="popup-mask"></view>
      <view class="popup-content" @click.stop>
        <view class="popup-title">
          <text>选择学校</text>
        </view>
        <view class="school-list">
          <view 
            v-for="(school, index) in schools" 
            :key="index"
            class="school-item"
            :class="{ active: selectedSchool.id === school.id }"
            @tap="selectSchool(school)"
          >
            <text>{{ school.name }}</text>
          </view>
        </view>
        <view class="popup-cancel-btn" @tap="closeSchoolPopup">
          <text>取消</text>
        </view>
      </view>
    </view>
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
      displayMode: 'card', // 显示模式：card-卡片式，table-表格式
      showYearSelect: false, // 控制年份选择弹窗显示
      showSchoolSelect: false, // 控制学校选择弹窗显示
      tableData: [],
      schools: [
        { id: 1, name: '广州航海学院' },
        { id: 2, name: '上海海事大学' },
        { id: 3, name: '大连海事大学' },
        { id: 4, name: '武汉理工大学' },
        { id: 5, name: '集美大学' }
      ],
      selectedSchool: { id: 1, name: '广州航海学院' }, // 默认选中的学校
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
        await this.loadScoreData(this.currentYear, this.selectedSchool.id);
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
    
    // 加载指定年份和学校的分数数据
    async loadScoreData(year, schoolId) {
      const cacheKey = `${year}_${schoolId}`;
      if (this.scoreData[cacheKey]) {
        this.processedData = this.scoreData[cacheKey];
        this.tableData = this.processedData.map(item => ({
          year: item.year,
          category: item.firstSubject === '物理' ? '理科' : '文科',
          province: item.province,
          majorName: item.majorName
        }));
        return; // 如果数据已加载，直接返回
      }
      
      this.loading = true;
      try {
        // 使用API获取专业分数线数据
        const res = await admissionApi.getMajorScores(year, schoolId);
        
        // 使用后端返回的数据
        this.scoreData[cacheKey] = res;
        this.processedData = res;
        this.tableData = res.map(item => ({
          year: item.year,
          category: item.firstSubject === '物理' ? '理科' : '文科',
          province: item.province,
          majorName: item.majorName
        }));
      } catch (err) {
        console.error('获取分数数据失败:', err);
        uni.showToast({
          title: '分数数据加载失败',
          icon: 'none'
        });
        // 设置默认数据用于展示
        this.processedData = this.getDefaultData();
        this.tableData = this.processedData.map(item => ({
          year: item.year,
          category: item.firstSubject === '物理' ? '理科' : '文科',
          province: item.province,
          majorName: item.majorName
        }));
      } finally {
        this.loading = false;
      }
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
      this.loadScoreData(year, this.selectedSchool.id);
      this.closeYearPopup();
    },
    
    // 打开学校选择弹窗
    openSchoolPopup() {
      this.showSchoolSelect = true;
    },
    
    // 关闭学校选择弹窗
    closeSchoolPopup() {
      this.showSchoolSelect = false;
    },
    
    // 选择学校
    selectSchool(school) {
      if (this.selectedSchool.id === school.id) {
        this.closeSchoolPopup();
        return;
      }
      
      this.selectedSchool = school;
      this.loadScoreData(this.currentYear, this.selectedSchool.id);
      this.closeSchoolPopup();
    },
    
    // 获取默认数据（用于演示或测试）
    getDefaultData() {
      return [
        {
          majorName: '航海技术',
          year: 2024,
          province: '天津市',
          planNumber: 50,
          minScore: 542,
          maxScore: 550,
          provinceControlLine: 475,
          firstSubject: '物理',
          secondSubject: '化学'
        },
        {
          majorName: '轮机工程',
          year: 2024,
          province: '天津市',
          planNumber: 50,
          minScore: 537,
          maxScore: 545,
          provinceControlLine: 475,
          firstSubject: '物理',
          secondSubject: '化学'
        },
        {
          majorName: '船舶电子电气工程',
          year: 2024,
          province: '天津市',
          planNumber: 50,
          minScore: 530,
          maxScore: 540,
          provinceControlLine: 475,
          firstSubject: '物理',
          secondSubject: '化学'
        },
        {
          majorName: '航海技术',
          year: 2024,
          province: '河北省',
          planNumber: 55,
          minScore: 545,
          maxScore: 553,
          provinceControlLine: 480,
          firstSubject: '物理',
          secondSubject: '化学'
        },
        {
          majorName: '轮机工程',
          year: 2024,
          province: '河北省',
          planNumber: 55,
          minScore: 540,
          maxScore: 548,
          provinceControlLine: 480,
          firstSubject: '物理',
          secondSubject: '化学'
        },
        {
          majorName: '船舶电子电气工程',
          year: 2024,
          province: '河北省',
          planNumber: 55,
          minScore: 535,
          maxScore: 545,
          provinceControlLine: 480,
          firstSubject: '物理',
          secondSubject: '化学'
        },
        {
          majorName: '航海技术',
          year: 2024,
          province: '山西省',
          planNumber: 45,
          minScore: 540,
          maxScore: 548,
          provinceControlLine: 470,
          firstSubject: '物理',
          secondSubject: '化学'
        },
        {
          majorName: '轮机工程',
          year: 2024,
          province: '山西省',
          planNumber: 45,
          minScore: 535,
          maxScore: 543,
          provinceControlLine: 470,
          firstSubject: '物理',
          secondSubject: '化学'
        },
        {
          majorName: '船舶电子电气工程',
          year: 2024,
          province: '山西省',
          planNumber: 45,
          minScore: 530,
          maxScore: 540,
          provinceControlLine: 470,
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
  
  .year-selector, .school-selector, .display-mode {
    display: flex;
    align-items: center;
    gap: 10rpx;
  }
  
  .year-text, .school-text, .mode-text {
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
    
    .year-list, .school-list {
      display: flex;
      flex-wrap: wrap;
      padding: 20rpx;
      
      .year-item, .school-item {
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
    
    .school-list {
      .school-item {
        width: 100%;
        text-align: left;
        padding: 25rpx 30rpx;
        border-bottom: 1rpx solid #f0f0f0;
        
        &:last-child {
          border-bottom: none;
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