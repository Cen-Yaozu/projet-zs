package com.zs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.entity.AdmissionPolicy;
import com.zs.entity.MajorScore;
import com.zs.mapper.MajorScoreMapper;
import com.zs.service.AdmissionPolicyService;
import com.zs.service.MajorScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class MajorScoreServiceImpl extends ServiceImpl<MajorScoreMapper, MajorScore> implements MajorScoreService {

    @Autowired
    private AdmissionPolicyService admissionPolicyService;

    @Override
    public List<MajorScore> getBySchoolId(Long schoolId) {
        return list(new LambdaQueryWrapper<MajorScore>()
                .eq(MajorScore::getSchoolId, schoolId));
    }

    @Override
    public List<MajorScore> getByYearAndSchoolId(Integer year, Long schoolId) {
        return list(new LambdaQueryWrapper<MajorScore>()
                .eq(MajorScore::getYear, year)
                .eq(MajorScore::getSchoolId, schoolId));
    }

    @Override
    public List<MajorScore> getByProvince(String province) {
        return list(new LambdaQueryWrapper<MajorScore>()
                .eq(MajorScore::getProvince, province));
    }

    @Override
    public List<MajorScore> getByYearAndSchoolIdAndProvince(Integer year, Long schoolId, String province) {
        return list(new LambdaQueryWrapper<MajorScore>()
                .eq(MajorScore::getYear, year)
                .eq(MajorScore::getSchoolId, schoolId)
                .eq(MajorScore::getProvince, province));
    }

    @Override
    public List<MajorScore> getByMajorName(String majorName) {
        // 获取专业ID
        Long majorId = getMajorIdByName(majorName);
        return list(new LambdaQueryWrapper<MajorScore>()
                .eq(MajorScore::getMajorId, majorId));
    }

    @Override
    public List<MajorScore> getByYearAndSchoolIdAndMajorName(Integer year, Long schoolId, String majorName) {
        // 获取专业ID
        Long majorId = getMajorIdByName(majorName);
        return list(new LambdaQueryWrapper<MajorScore>()
                .eq(MajorScore::getYear, year)
                .eq(MajorScore::getSchoolId, schoolId)
                .eq(MajorScore::getMajorId, majorId));
    }

    @Override
    @Transactional
    public boolean fillHistoricalScoreData() {
        // 先清空专业分数表数据
        remove(null);
        
        // 获取所有招生政策数据
        List<AdmissionPolicy> policies = admissionPolicyService.list();
        
        // 按学校ID、年份分组
        Map<String, List<AdmissionPolicy>> groupedPolicies = policies.stream()
                .collect(Collectors.groupingBy(p -> p.getSchoolId() + "_" + p.getYear()));
        
        List<MajorScore> majorScores = new ArrayList<>();
        
        // 处理每个学校的每年数据
        for (Map.Entry<String, List<AdmissionPolicy>> entry : groupedPolicies.entrySet()) {
            List<AdmissionPolicy> schoolYearPolicies = entry.getValue();
            if (schoolYearPolicies.isEmpty()) continue;
            
            // 提取学校ID和年份
            Long schoolId = schoolYearPolicies.get(0).getSchoolId();
            Integer year = schoolYearPolicies.get(0).getYear();
            
            // 按省份再分组
            Map<String, List<AdmissionPolicy>> provinceGroups = schoolYearPolicies.stream()
                    .collect(Collectors.groupingBy(AdmissionPolicy::getProvince));
            
            // 处理每个省份数据
            for (Map.Entry<String, List<AdmissionPolicy>> provinceEntry : provinceGroups.entrySet()) {
                String province = provinceEntry.getKey();
                List<AdmissionPolicy> provincePolicies = provinceEntry.getValue();
                
                // 提取专业信息
                Map<String, List<AdmissionPolicy>> majorGroups = groupByMajor(provincePolicies);
                
                // 为每个专业创建分数记录
                for (Map.Entry<String, List<AdmissionPolicy>> majorEntry : majorGroups.entrySet()) {
                    String majorName = majorEntry.getKey();
                    List<AdmissionPolicy> majorPolicies = majorEntry.getValue();
                    
                    // 计算最高分和最低分
                    OptionalInt maxScoreOpt = majorPolicies.stream()
                            .mapToInt(AdmissionPolicy::getMinScore)
                            .max();
                    OptionalInt minScoreOpt = majorPolicies.stream()
                            .mapToInt(AdmissionPolicy::getMinScore)
                            .min();
                    
                    int maxScore = maxScoreOpt.orElse(0);
                    int minScore = minScoreOpt.orElse(0);
                    
                    // 计算录取人数（实际应该是计划招生人数的总和）
                    int planNumber = majorPolicies.stream()
                            .mapToInt(p -> p.getPlanNumber() != null ? p.getPlanNumber() : 0)
                            .sum();
                    
                    // 获取科目要求和省控线
                    String[] subjects = getSubjectRequirements(majorName);
                    int provinceControlLine = getProvinceControlLine(province);
                    
                    // 获取专业ID
                    Long majorId = getMajorIdByName(majorName);
                    
                    // 创建专业分数记录
                    MajorScore majorScore = new MajorScore();
                    majorScore.setSchoolId(schoolId);
                    majorScore.setMajorId(majorId);
                    majorScore.setYear(year);
                    majorScore.setProvince(province);
                    majorScore.setPlanNumber(planNumber > 0 ? planNumber : 50); // 默认50人
                    majorScore.setMinScore(minScore);
                    majorScore.setMaxScore(maxScore);
                    majorScore.setProvinceControlLine(provinceControlLine);
                    majorScore.setFirstSubject(subjects[0]);
                    majorScore.setSecondSubject(subjects.length > 1 ? subjects[1] : null);
                    majorScore.setCreateTime(LocalDateTime.now());
                    majorScore.setUpdateTime(LocalDateTime.now());
                    
                    majorScores.add(majorScore);
                }
            }
        }
        
        // 批量保存专业分数数据
        return saveBatch(majorScores);
    }
    
    /**
     * 根据政策内容提取专业名称，并按专业分组
     */
    private Map<String, List<AdmissionPolicy>> groupByMajor(List<AdmissionPolicy> policies) {
        Map<String, List<AdmissionPolicy>> majorGroups = new HashMap<>();
        
        // 预定义的专业列表（如果政策内容中无法提取，则使用这些默认值）
        List<String> defaultMajors = Arrays.asList("航海技术", "轮机工程", "船舶电子电气工程");
        
        for (AdmissionPolicy policy : policies) {
            // 尝试从政策内容中提取专业名称
            String majorName = extractMajorName(policy.getPolicyContent());
            
            // 如果无法提取，则根据其他信息进行猜测
            if (majorName == null) {
                // 如果有科类信息，可以与默认专业组合猜测
                if ("理科".equals(policy.getCategory()) && policy.getMinScore() > 535) {
                    majorName = "航海技术";
                } else if ("理科".equals(policy.getCategory()) && policy.getMinScore() > 530) {
                    majorName = "轮机工程";
                } else {
                    majorName = "船舶电子电气工程";
                }
            }
            
            // 添加到对应专业的列表中
            majorGroups.computeIfAbsent(majorName, k -> new ArrayList<>()).add(policy);
        }
        
        return majorGroups;
    }
    
    /**
     * 提取专业名称
     */
    private String extractMajorName(String content) {
        if (content == null) {
            return null;
        }
        
        // 尝试匹配常见专业名称
        Pattern pattern = Pattern.compile("(航海技术|轮机工程|船舶电子电气工程|航运管理|海事管理)");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        
        // 尝试提取第一行作为专业名称
        String[] lines = content.split("\\n");
        if (lines.length > 0) {
            String firstLine = lines[0].trim();
            // 移除前缀数字和标点
            return firstLine.replaceAll("^\\d+[.、\\s]+", "");
        }
        
        return null;
    }
    
    /**
     * 获取专业的科目要求
     */
    private String[] getSubjectRequirements(String majorName) {
        Map<String, String[]> subjectRequirements = new HashMap<>();
        subjectRequirements.put("航海技术", new String[]{"物理", "化学"});
        subjectRequirements.put("轮机工程", new String[]{"物理", "化学"});
        subjectRequirements.put("船舶电子电气工程", new String[]{"物理", "化学"});
        subjectRequirements.put("航运管理", new String[]{"物理", "地理"});
        subjectRequirements.put("海事管理", new String[]{"历史", "地理"});
        
        return subjectRequirements.getOrDefault(majorName, new String[]{"物理", "化学"});
    }
    
    /**
     * 获取省控线
     */
    private int getProvinceControlLine(String province) {
        Map<String, Integer> provinceControlLines = new HashMap<>();
        provinceControlLines.put("北京市", 480);
        provinceControlLines.put("天津市", 475);
        provinceControlLines.put("上海市", 400);
        provinceControlLines.put("重庆市", 450);
        provinceControlLines.put("河北省", 480);
        provinceControlLines.put("山西省", 470);
        provinceControlLines.put("辽宁省", 480);
        provinceControlLines.put("吉林省", 455);
        provinceControlLines.put("黑龙江省", 450);
        provinceControlLines.put("江苏省", 490);
        provinceControlLines.put("浙江省", 490);
        provinceControlLines.put("安徽省", 485);
        provinceControlLines.put("福建省", 465);
        provinceControlLines.put("江西省", 520);
        provinceControlLines.put("山东省", 505);
        provinceControlLines.put("河南省", 518);
        provinceControlLines.put("湖北省", 495);
        provinceControlLines.put("湖南省", 485);
        provinceControlLines.put("广东省", 465);
        provinceControlLines.put("广西壮族自治区", 455);
        provinceControlLines.put("海南省", 539);
        provinceControlLines.put("四川省", 475);
        provinceControlLines.put("贵州省", 470);
        provinceControlLines.put("云南省", 465);
        provinceControlLines.put("陕西省", 460);
        provinceControlLines.put("甘肃省", 435);
        provinceControlLines.put("青海省", 425);
        provinceControlLines.put("内蒙古自治区", 430);
        provinceControlLines.put("宁夏回族自治区", 430);
        provinceControlLines.put("新疆维吾尔自治区", 425);
        provinceControlLines.put("西藏自治区", 420);
        
        return provinceControlLines.getOrDefault(province, 450);
    }
    
    /**
     * 根据专业名称获取专业ID
     */
    private Long getMajorIdByName(String majorName) {
        // 专业名称对应ID的映射（模拟，实际应从数据库查询）
        Map<String, Long> majorNameToId = new HashMap<>();
        majorNameToId.put("航海技术", 1L);
        majorNameToId.put("轮机工程", 2L);
        majorNameToId.put("船舶电子电气工程", 3L);
        majorNameToId.put("航运管理", 4L);
        majorNameToId.put("海事管理", 5L);
        
        return majorNameToId.getOrDefault(majorName, 1L); // 默认返回1
    }
} 