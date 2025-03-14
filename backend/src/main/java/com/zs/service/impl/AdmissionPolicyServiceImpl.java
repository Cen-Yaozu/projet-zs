package com.zs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.entity.AdmissionPolicy;
import com.zs.mapper.AdmissionPolicyMapper;
import com.zs.service.AdmissionPolicyService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AdmissionPolicyServiceImpl extends ServiceImpl<AdmissionPolicyMapper, AdmissionPolicy> implements AdmissionPolicyService {

    @Override
    public List<AdmissionPolicy> getBySchoolId(Long schoolId) {
        return list(new LambdaQueryWrapper<AdmissionPolicy>()
                .eq(AdmissionPolicy::getSchoolId, schoolId));
    }

    @Override
    public List<AdmissionPolicy> getByYearAndSchoolId(Integer year, Long schoolId) {
        return list(new LambdaQueryWrapper<AdmissionPolicy>()
                .eq(AdmissionPolicy::getYear, year)
                .eq(AdmissionPolicy::getSchoolId, schoolId));
    }

    @Override
    public List<AdmissionPolicy> getByProvince(String province) {
        return list(new LambdaQueryWrapper<AdmissionPolicy>()
                .eq(AdmissionPolicy::getProvince, province));
    }

    @Override
    public List<AdmissionPolicy> getByYearAndSchoolIdAndProvince(Integer year, Long schoolId, String province) {
        return list(new LambdaQueryWrapper<AdmissionPolicy>()
                .eq(AdmissionPolicy::getYear, year)
                .eq(AdmissionPolicy::getSchoolId, schoolId)
                .eq(AdmissionPolicy::getProvince, province));
    }

    @Override
    public List<AdmissionPolicy> getByScoreRange(Integer minScore, Integer maxScore) {
        return list(new LambdaQueryWrapper<AdmissionPolicy>()
                .ge(AdmissionPolicy::getMinScore, minScore)
                .le(AdmissionPolicy::getMinScore, maxScore));
    }
    
    @Override
    public List<Map<String, Object>> getMajorScores(Integer year, Long schoolId, String province) {
        // 构建查询条件
        LambdaQueryWrapper<AdmissionPolicy> queryWrapper = new LambdaQueryWrapper<AdmissionPolicy>()
                .eq(AdmissionPolicy::getYear, year)
                .eq(AdmissionPolicy::getSchoolId, schoolId);
        
        // 如果指定了省份，添加省份筛选条件
        if (StringUtils.hasText(province)) {
            queryWrapper.eq(AdmissionPolicy::getProvince, province);
        }
        
        // 查询数据
        List<AdmissionPolicy> policies = list(queryWrapper);
        
        // 按专业分组处理数据
        Map<String, List<AdmissionPolicy>> majorGroups = groupByMajor(policies);
        
        // 转换为前端所需的数据格式
        return convertToMajorScores(majorGroups, year);
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
     * 转换为专业分数线数据
     */
    private List<Map<String, Object>> convertToMajorScores(Map<String, List<AdmissionPolicy>> majorGroups, Integer year) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 预设的科目信息和省控线（实际项目中可从配置或数据库获取）
        Map<String, Integer> provinceControlLines = new HashMap<>();
        provinceControlLines.put("北京市", 480);
        provinceControlLines.put("天津市", 475);
        provinceControlLines.put("上海市", 400);
        provinceControlLines.put("重庆市", 450);
        
        // 科目要求
        Map<String, String[]> subjectRequirements = new HashMap<>();
        subjectRequirements.put("航海技术", new String[]{"物理", "化学"});
        subjectRequirements.put("轮机工程", new String[]{"物理", "化学"});
        subjectRequirements.put("船舶电子电气工程", new String[]{"物理", "化学"});
        
        for (Map.Entry<String, List<AdmissionPolicy>> entry : majorGroups.entrySet()) {
            String majorName = entry.getKey();
            List<AdmissionPolicy> majorPolicies = entry.getValue();
            
            // 按省份分组
            Map<String, List<AdmissionPolicy>> provinceGroups = majorPolicies.stream()
                    .collect(Collectors.groupingBy(AdmissionPolicy::getProvince));
            
            // 对每个省份的数据进行处理
            for (Map.Entry<String, List<AdmissionPolicy>> provinceEntry : provinceGroups.entrySet()) {
                String province = provinceEntry.getKey();
                List<AdmissionPolicy> provincePolicies = provinceEntry.getValue();
                
                // 计算最高分和最低分
                OptionalInt maxScoreOpt = provincePolicies.stream()
                        .mapToInt(AdmissionPolicy::getMinScore)
                        .max();
                OptionalInt minScoreOpt = provincePolicies.stream()
                        .mapToInt(AdmissionPolicy::getMinScore)
                        .min();
                
                int maxScore = maxScoreOpt.orElse(0);
                int minScore = minScoreOpt.orElse(0);
                
                // 计算录取人数（实际应该是计划招生人数的总和）
                int planNumber = provincePolicies.stream()
                        .mapToInt(p -> p.getPlanNumber() != null ? p.getPlanNumber() : 0)
                        .sum();
                
                // 获取科目要求
                String[] subjects = subjectRequirements.getOrDefault(majorName, new String[]{"物理", "化学"});
                
                // 获取省控线
                int provinceControlLine = provinceControlLines.getOrDefault(province, 450);
                
                // 创建结果对象
                Map<String, Object> majorScore = new HashMap<>();
                majorScore.put("majorName", majorName);
                majorScore.put("year", year);
                majorScore.put("province", province);
                majorScore.put("planNumber", planNumber > 0 ? planNumber : 1); // 如果没有人数数据，默认为1
                majorScore.put("maxScore", maxScore);
                majorScore.put("minScore", minScore);
                majorScore.put("provinceControlLine", provinceControlLine);
                majorScore.put("firstSubject", subjects[0]);
                majorScore.put("secondSubject", subjects.length > 1 ? subjects[1] : null);
                
                result.add(majorScore);
            }
        }
        
        return result;
    }
}