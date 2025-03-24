/*
 Navicat Premium Dump SQL

 Source Server         : 本地mysql
 Source Server Type    : MySQL
 Source Server Version : 90200 (9.2.0)
 Source Host           : localhost:3306
 Source Schema         : project_zs

 Target Server Type    : MySQL
 Target Server Version : 90200 (9.2.0)
 File Encoding         : 65001

 Date: 24/03/2025 23:54:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admission_policy
-- ----------------------------
DROP TABLE IF EXISTS `admission_policy`;
CREATE TABLE `admission_policy` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `school_id` bigint NOT NULL COMMENT '学校ID',
  `year` int NOT NULL COMMENT '年份',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '招生省份',
  `plan_number` int DEFAULT NULL COMMENT '计划招生人数',
  `min_score` int DEFAULT NULL COMMENT '最低录取分数',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '科类（理科、文科等）',
  `policy_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '政策详细内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_school_year_province` (`school_id`,`year`,`province`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='招生政策表';

-- ----------------------------
-- Records of admission_policy
-- ----------------------------
BEGIN;
INSERT INTO `admission_policy` (`id`, `school_id`, `year`, `province`, `plan_number`, `min_score`, `category`, `policy_content`, `create_time`, `update_time`) VALUES (1, 1, 2023, '北京市', 300, 680, '理科', '1. 综合评价录取：高考成绩占比90%，高中学业水平考试成绩占比10%\n2. 特殊类型招生：强基计划、自主招生等', '2025-03-11 23:14:06', '2025-03-11 23:14:06');
INSERT INTO `admission_policy` (`id`, `school_id`, `year`, `province`, `plan_number`, `min_score`, `category`, `policy_content`, `create_time`, `update_time`) VALUES (2, 1, 2023, '浙江省', 200, 670, '综合改革', '1. 采用\"3+3\"模式录取\n2. 选考科目要求：物理、化学、生物至少选考一门', '2025-03-11 23:14:06', '2025-03-11 23:14:06');
INSERT INTO `admission_policy` (`id`, `school_id`, `year`, `province`, `plan_number`, `min_score`, `category`, `policy_content`, `create_time`, `update_time`) VALUES (3, 1, 2023, '北京市', 280, 670, '理科', '1. 常规批次录取\n2. 特殊类型招生：博雅人才培养计划等', '2025-03-11 23:14:06', '2025-03-22 16:18:34');
INSERT INTO `admission_policy` (`id`, `school_id`, `year`, `province`, `plan_number`, `min_score`, `category`, `policy_content`, `create_time`, `update_time`) VALUES (4, 1, 2023, '江苏省', 180, 665, '理科', '1. 采用院系专业组投档模式\n2. 选考科目要求：物理必选，化学、生物选考一门', '2025-03-11 23:14:06', '2025-03-22 16:18:34');
INSERT INTO `admission_policy` (`id`, `school_id`, `year`, `province`, `plan_number`, `min_score`, `category`, `policy_content`, `create_time`, `update_time`) VALUES (5, 1, 2023, '浙江省', 350, 650, '综合改革', '1. 普通类招生专业组\n2. 特殊类型招生：三位一体综合评价等', '2025-03-11 23:14:06', '2025-03-22 16:18:34');
INSERT INTO `admission_policy` (`id`, `school_id`, `year`, `province`, `plan_number`, `min_score`, `category`, `policy_content`, `create_time`, `update_time`) VALUES (6, 1, 2024, '北京市', 320, 685, '理科', '1. 综合评价录取：高考成绩占比90%，高中学业水平考试成绩占比10%\n2. 特殊类型招生：强基计划、拔尖创新人才计划等\n3. 选考科目要求：物理必选，化学或生物至少选考一门', '2025-03-15 00:54:10', '2025-03-15 00:54:10');
INSERT INTO `admission_policy` (`id`, `school_id`, `year`, `province`, `plan_number`, `min_score`, `category`, `policy_content`, `create_time`, `update_time`) VALUES (7, 1, 2024, '浙江省', 220, 678, '综合改革', '1. 采用\"3+3\"模式录取\n2. 选考科目要求：物理、化学、生物至少选考一门\n3. 新增AI与未来科技试验班招生计划', '2025-03-15 00:54:10', '2025-03-15 00:54:10');
INSERT INTO `admission_policy` (`id`, `school_id`, `year`, `province`, `plan_number`, `min_score`, `category`, `policy_content`, `create_time`, `update_time`) VALUES (8, 1, 2024, '北京市', 290, 680, '理科', '1. 常规批次录取\n2. 特殊类型招生：博雅人才培养计划、未来领袖计划等\n3. 校际交流项目增加欧美名校交换生名额', '2025-03-15 00:54:10', '2025-03-22 16:18:34');
INSERT INTO `admission_policy` (`id`, `school_id`, `year`, `province`, `plan_number`, `min_score`, `category`, `policy_content`, `create_time`, `update_time`) VALUES (9, 1, 2024, '江苏省', 200, 675, '理科', '1. 采用院系专业组投档模式\n2. 选考科目要求：物理必选，化学、生物选考一门\n3. 新增人工智能与数据科学专业招生', '2025-03-15 00:54:10', '2025-03-22 16:18:34');
INSERT INTO `admission_policy` (`id`, `school_id`, `year`, `province`, `plan_number`, `min_score`, `category`, `policy_content`, `create_time`, `update_time`) VALUES (10, 1, 2024, '浙江省', 380, 660, '综合改革', '1. 普通类招生专业组\n2. 特殊类型招生：三位一体综合评价、校长实名推荐制等\n3. 新增跨学科交叉创新人才培养项目', '2025-03-15 00:54:10', '2025-03-22 16:18:34');
INSERT INTO `admission_policy` (`id`, `school_id`, `year`, `province`, `plan_number`, `min_score`, `category`, `policy_content`, `create_time`, `update_time`) VALUES (11, 1, 2024, '广东省', 180, 670, '物理类', '1. 综合评价录取方式\n2. 选考科目要求：物理必选，化学或生物选考一门\n3. 增加粤港澳大湾区创新人才培养专项', '2025-03-15 00:54:17', '2025-03-15 00:54:17');
INSERT INTO `admission_policy` (`id`, `school_id`, `year`, `province`, `plan_number`, `min_score`, `category`, `policy_content`, `create_time`, `update_time`) VALUES (12, 1, 2024, '广东省', 160, 665, '物理类', '1. 按专业组招生\n2. 选考科目要求：物理必选，化学优先\n3. 开展\"大湾区未来科学家\"培养计划', '2025-03-15 00:54:17', '2025-03-22 16:18:34');
INSERT INTO `admission_policy` (`id`, `school_id`, `year`, `province`, `plan_number`, `min_score`, `category`, `policy_content`, `create_time`, `update_time`) VALUES (13, 1, 2024, '广东省', 200, 650, '物理类', '1. 常规批次录取\n2. 粤港澳联合培养项目\n3. 新增人工智能与生物医学工程交叉学科试点班', '2025-03-15 00:54:17', '2025-03-22 16:18:34');
COMMIT;

-- ----------------------------
-- Table structure for announcement
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告内容',
  `publish_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'GENERAL' COMMENT '公告类型：GENERAL-普通公告，IMPORTANT-重要公告',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '公告图片URL',
  `category` varchar(50) NOT NULL DEFAULT 'GENERAL_NOTICE' COMMENT '公告类别: GENERAL_NOTICE(招生公告), ADMISSION_RULES(招生章程), ADMISSION_POLICY(招生政策), HISTORICAL_REFERENCE(往年参考)',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_publish_time` (`publish_time`) USING BTREE,
  KEY `idx_type` (`type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='通知公告表';

-- ----------------------------
-- Records of announcement
-- ----------------------------
BEGIN;
INSERT INTO `announcement` (`id`, `title`, `content`, `publish_time`, `end_time`, `status`, `type`, `create_user_id`, `create_time`, `update_time`, `image_url`, `category`) VALUES (1, '2025年招生简章发布', '我校2025年招生简章已正式发布，详情请访问学校官网或联系招生办公室。', '2025-03-18 23:50:37', '2025-04-17 23:50:37', 1, 'IMPORTANT', 1, '2025-03-18 23:50:37', '2025-03-19 00:24:47', '/static/images/news1.jpg', 'GENERAL_NOTICE');
INSERT INTO `announcement` (`id`, `title`, `content`, `publish_time`, `end_time`, `status`, `type`, `create_user_id`, `create_time`, `update_time`, `image_url`, `category`) VALUES (2, '高考志愿填报指导讲座', '为帮助考生科学填报志愿，我校将于2025年6月15日举办高考志愿填报指导讲座，欢迎广大考生和家长参加。', '2025-03-18 23:50:37', '2025-04-02 23:50:37', 1, 'GENERAL', 1, '2025-03-18 23:50:37', '2025-03-19 00:24:53', '/static/images/news2.jpg', 'GENERAL_NOTICE');
INSERT INTO `announcement` (`id`, `title`, `content`, `publish_time`, `end_time`, `status`, `type`, `create_user_id`, `create_time`, `update_time`, `image_url`, `category`) VALUES (3, '校园开放日活动通知', '我校将于2025年5月20日至5月22日举办校园开放日活动，欢迎考生和家长前来参观。', '2025-03-18 23:50:37', '2025-04-07 23:50:37', 1, 'GENERAL', 1, '2025-03-18 23:50:37', '2025-03-19 00:25:00', '/static/images/news3.jpg', 'GENERAL_NOTICE');
COMMIT;

-- ----------------------------
-- Table structure for college
-- ----------------------------
DROP TABLE IF EXISTS `college`;
CREATE TABLE `college` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '院系名称',
  `degree` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '学历层次（本科、专科等）',
  `duration` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '学制（年）',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '院系介绍',
  `school_id` bigint NOT NULL COMMENT '所属学校ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `logo_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Logo URL',
  `banner_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Banner URL',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_school_id` (`school_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='院系信息表';

-- ----------------------------
-- Records of college
-- ----------------------------
BEGIN;
INSERT INTO `college` (`id`, `name`, `degree`, `duration`, `description`, `school_id`, `create_time`, `update_time`, `logo_url`, `banner_url`) VALUES (1, '航运学院', '本科', '4', '航运学院致力于培养航运领域的高素质人才，拥有完善的教学设施和优秀的师资力量。', 1, '2025-03-20 02:09:49', '2025-03-20 02:09:49', 'blob:http://localhost:8082/9bf16681-e5b2-4b3c-bb14-1d92988a4fb0', NULL);
INSERT INTO `college` (`id`, `name`, `degree`, `duration`, `description`, `school_id`, `create_time`, `update_time`, `logo_url`, `banner_url`) VALUES (2, '海洋装备工程学院', '本科', '4', '海洋装备工程学院专注于海洋工程装备的研发与应用，培养海洋工程领域的专业人才。', 1, '2025-03-20 02:09:49', '2025-03-20 02:09:49', NULL, NULL);
INSERT INTO `college` (`id`, `name`, `degree`, `duration`, `description`, `school_id`, `create_time`, `update_time`, `logo_url`, `banner_url`) VALUES (3, '低空装备与智能控制学院', '本科', '4', '低空装备与智能控制学院是新兴交叉学科学院，致力于培养无人机等低空装备研发与应用人才。', 1, '2025-03-20 02:09:49', '2025-03-22 15:53:01', NULL, NULL);
INSERT INTO `college` (`id`, `name`, `degree`, `duration`, `description`, `school_id`, `create_time`, `update_time`, `logo_url`, `banner_url`) VALUES (4, '智能制造学院', '本科', '4', '智能制造学院聚焦工业4.0和智能制造领域，培养具有创新能力的高级工程技术人才。', 1, '2025-03-20 02:09:49', '2025-03-22 15:53:01', NULL, NULL);
INSERT INTO `college` (`id`, `name`, `degree`, `duration`, `description`, `school_id`, `create_time`, `update_time`, `logo_url`, `banner_url`) VALUES (5, '智能交通与工程学院', '本科', '4', '智能交通与工程学院立足未来交通发展，培养具备智能交通系统设计与管理能力的专业人才。', 1, '2025-03-20 02:09:49', '2025-03-22 15:53:01', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for major_info
-- ----------------------------
DROP TABLE IF EXISTS `major_info`;
CREATE TABLE `major_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `school_id` bigint NOT NULL COMMENT '所属学校ID',
  `college_id` bigint DEFAULT NULL COMMENT '所属院系ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '专业名称',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '专业代码',
  `degree` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '学位类型',
  `duration` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '学制',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '专业介绍',
  `career_prospects` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '就业前景',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `icon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Icon URL',
  `images` json DEFAULT NULL COMMENT 'Images JSON array',
  `training_objectives` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT 'Training objectives',
  `main_courses` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT 'Main courses',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_school_id` (`school_id`) USING BTREE,
  KEY `idx_college_id` (`college_id`) USING BTREE,
  CONSTRAINT `fk_major_college` FOREIGN KEY (`college_id`) REFERENCES `college` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='专业信息表';

-- ----------------------------
-- Records of major_info
-- ----------------------------
BEGIN;
INSERT INTO `major_info` (`id`, `school_id`, `college_id`, `name`, `code`, `degree`, `duration`, `description`, `career_prospects`, `create_time`, `update_time`, `icon_url`, `images`, `training_objectives`, `main_courses`) VALUES (1, 1, 4, '计算机科学与技术', '080901', '工学学士', '4年', '计算机科学与技术专业培养具备计算机硬件、软件与网络系统等方面的知识和能力的高级人才。', '可在IT企业、科研机构、金融机构等从事软件开发、系统架构设计等工作。', '2025-03-11 23:14:06', '2025-03-20 02:11:01', NULL, NULL, NULL, NULL);
INSERT INTO `major_info` (`id`, `school_id`, `college_id`, `name`, `code`, `degree`, `duration`, `description`, `career_prospects`, `create_time`, `update_time`, `icon_url`, `images`, `training_objectives`, `main_courses`) VALUES (2, 1, 4, '人工智能', '080910', '工学学士', '4年', '人工智能专业培养具备机器学习、深度学习、计算机视觉等方面知识和能力的高级人才。', '可在AI企业、研究所、互联网公司等从事算法研发、AI应用开发等工作。', '2025-03-11 23:14:06', '2025-03-20 02:11:01', NULL, NULL, NULL, NULL);
INSERT INTO `major_info` (`id`, `school_id`, `college_id`, `name`, `code`, `degree`, `duration`, `description`, `career_prospects`, `create_time`, `update_time`, `icon_url`, `images`, `training_objectives`, `main_courses`) VALUES (3, 1, 3, '金融学', '020301', '经济学学士', '4年', '金融学专业培养具备金融理论、金融市场、投资管理等方面知识和能力的高级人才。', '可在银行、证券公司、基金公司等金融机构从事投资、理财、风控等工作。', '2025-03-11 23:14:06', '2025-03-22 15:54:16', NULL, NULL, NULL, NULL);
INSERT INTO `major_info` (`id`, `school_id`, `college_id`, `name`, `code`, `degree`, `duration`, `description`, `career_prospects`, `create_time`, `update_time`, `icon_url`, `images`, `training_objectives`, `main_courses`) VALUES (4, 1, 3, '医学', '100201', '医学学士', '5年', '医学专业培养具备基础医学、临床医学等方面知识和能力的医疗人才。', '可在医院、医疗机构、医药企业等从事医疗、研究、管理等工作。', '2025-03-11 23:14:06', '2025-03-22 15:54:16', NULL, NULL, NULL, NULL);
INSERT INTO `major_info` (`id`, `school_id`, `college_id`, `name`, `code`, `degree`, `duration`, `description`, `career_prospects`, `create_time`, `update_time`, `icon_url`, `images`, `training_objectives`, `main_courses`) VALUES (5, 1, 1, '航海技术', '082303', '工学学士', '4年', '航海技术专业培养具有船舶驾驶技术及船舶管理能力的专业人才。', '可在航运企业、港口、船舶公司等从事船舶驾驶、管理等工作。', '2025-03-22 16:11:52', '2025-03-22 16:11:52', NULL, NULL, NULL, NULL);
INSERT INTO `major_info` (`id`, `school_id`, `college_id`, `name`, `code`, `degree`, `duration`, `description`, `career_prospects`, `create_time`, `update_time`, `icon_url`, `images`, `training_objectives`, `main_courses`) VALUES (6, 1, 1, '轮机工程', '082304', '工学学士', '4年', '轮机工程专业培养具备轮机设备操作维护和管理能力的高级专业人才。', '可在航运公司、船厂、船检机构等从事轮机管理、设备维护等工作。', '2025-03-22 16:11:52', '2025-03-22 16:11:52', NULL, NULL, NULL, NULL);
INSERT INTO `major_info` (`id`, `school_id`, `college_id`, `name`, `code`, `degree`, `duration`, `description`, `career_prospects`, `create_time`, `update_time`, `icon_url`, `images`, `training_objectives`, `main_courses`) VALUES (7, 1, 2, '船舶与海洋工程', '082401', '工学学士', '4年', '船舶与海洋工程专业培养具备船舶与海洋工程结构物设计与建造能力的专业人才。', '可在船舶设计院、船厂、海洋工程公司等从事设计、建造与管理工作。', '2025-03-22 16:11:52', '2025-03-22 16:11:52', NULL, NULL, NULL, NULL);
INSERT INTO `major_info` (`id`, `school_id`, `college_id`, `name`, `code`, `degree`, `duration`, `description`, `career_prospects`, `create_time`, `update_time`, `icon_url`, `images`, `training_objectives`, `main_courses`) VALUES (8, 1, 2, '海洋资源开发技术', '082502', '工学学士', '4年', '海洋资源开发技术专业培养具备海洋资源勘探与开发能力的高级专业人才。', '可在海洋资源勘探公司、海洋工程企业等从事勘探、开发与管理工作。', '2025-03-22 16:11:52', '2025-03-22 16:11:52', NULL, NULL, NULL, NULL);
INSERT INTO `major_info` (`id`, `school_id`, `college_id`, `name`, `code`, `degree`, `duration`, `description`, `career_prospects`, `create_time`, `update_time`, `icon_url`, `images`, `training_objectives`, `main_courses`) VALUES (9, 1, 3, '无人机应用工程', '082004', '工学学士', '4年', '无人机应用工程专业培养具备无人机系统设计、操控与应用能力的专业人才。', '可在无人机研发企业、航空公司等从事设计、操控与管理工作。', '2025-03-22 16:11:52', '2025-03-22 16:11:52', NULL, NULL, NULL, NULL);
INSERT INTO `major_info` (`id`, `school_id`, `college_id`, `name`, `code`, `degree`, `duration`, `description`, `career_prospects`, `create_time`, `update_time`, `icon_url`, `images`, `training_objectives`, `main_courses`) VALUES (10, 1, 3, '导航工程', '082301', '工学学士', '4年', '导航工程专业培养具备现代导航技术应用与开发能力的高级专业人才。', '可在导航设备研发企业、航运公司等从事研发、应用与管理工作。', '2025-03-22 16:11:52', '2025-03-22 16:11:52', NULL, NULL, NULL, NULL);
INSERT INTO `major_info` (`id`, `school_id`, `college_id`, `name`, `code`, `degree`, `duration`, `description`, `career_prospects`, `create_time`, `update_time`, `icon_url`, `images`, `training_objectives`, `main_courses`) VALUES (11, 1, 4, '智能制造工程', '080213', '工学学士', '4年', '智能制造工程专业培养具备智能制造系统设计与管理能力的专业人才。', '可在制造企业、自动化公司等从事系统集成、管理等工作。', '2025-03-22 16:11:52', '2025-03-22 16:11:52', NULL, NULL, NULL, NULL);
INSERT INTO `major_info` (`id`, `school_id`, `college_id`, `name`, `code`, `degree`, `duration`, `description`, `career_prospects`, `create_time`, `update_time`, `icon_url`, `images`, `training_objectives`, `main_courses`) VALUES (12, 1, 5, '交通工程', '082303', '工学学士', '4年', '交通工程专业培养具备交通规划、设计与管理能力的高级专业人才。', '可在交通设计院、交通管理部门等从事规划、设计与管理工作。', '2025-03-22 16:11:52', '2025-03-22 16:11:52', NULL, NULL, NULL, NULL);
INSERT INTO `major_info` (`id`, `school_id`, `college_id`, `name`, `code`, `degree`, `duration`, `description`, `career_prospects`, `create_time`, `update_time`, `icon_url`, `images`, `training_objectives`, `main_courses`) VALUES (13, 1, 5, '智能交通工程', '082306', '工学学士', '4年', '智能交通工程专业培养具备智能交通系统设计与管理能力的专业人才。', '可在交通系统集成企业、交通管理部门等从事系统设计、管理等工作。', '2025-03-22 16:11:52', '2025-03-22 16:11:52', NULL, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for major_score
-- ----------------------------
DROP TABLE IF EXISTS `major_score`;
CREATE TABLE `major_score` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `school_id` bigint NOT NULL,
  `major_id` bigint NOT NULL,
  `year` int NOT NULL,
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `min_score` int DEFAULT NULL,
  `max_score` int DEFAULT NULL,
  `plan_number` int DEFAULT NULL,
  `province_control_line` int DEFAULT NULL,
  `first_subject` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `second_subject` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_school_major` (`school_id`,`major_id`) USING BTREE,
  KEY `idx_year_province` (`year`,`province`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of major_score
-- ----------------------------
BEGIN;
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (1, 1, 1, 2024, '北京市', 690, 700, 50, 500, '物理', '数学', '2025-03-15 00:54:42', '2025-03-15 00:54:42');
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (2, 1, 1, 2024, '广东省', 680, 695, 30, 480, '物理', '数学', '2025-03-15 00:54:42', '2025-03-15 00:54:42');
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (3, 1, 1, 2024, '浙江省', 685, 698, 40, 490, '物理', '数学', '2025-03-15 00:54:42', '2025-03-15 00:54:42');
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (4, 1, 2, 2024, '北京市', 688, 698, 40, 500, '物理', '数学', '2025-03-15 00:54:42', '2025-03-15 00:54:42');
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (5, 1, 2, 2024, '广东省', 675, 690, 25, 480, '物理', '数学', '2025-03-15 00:54:42', '2025-03-15 00:54:42');
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (6, 1, 2, 2024, '浙江省', 680, 695, 35, 490, '物理', '数学', '2025-03-15 00:54:42', '2025-03-15 00:54:42');
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (7, 1, 3, 2024, '北京市', 685, 695, 45, 500, '物理', '数学', '2025-03-15 00:54:42', '2025-03-22 16:17:50');
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (8, 1, 3, 2024, '广东省', 670, 685, 30, 480, '物理', '数学', '2025-03-15 00:54:42', '2025-03-22 16:17:50');
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (9, 1, 3, 2024, '江苏省', 675, 690, 35, 485, '物理', '数学', '2025-03-15 00:54:42', '2025-03-22 16:17:50');
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (10, 1, 4, 2024, '浙江省', 665, 680, 60, 490, '物理', '生物', '2025-03-15 00:54:42', '2025-03-22 16:17:50');
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (11, 1, 4, 2024, '广东省', 655, 670, 50, 480, '物理', '生物', '2025-03-15 00:54:42', '2025-03-22 16:17:50');
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (12, 1, 1, 2023, '北京市', 685, 695, 45, 495, '物理', '数学', '2025-03-15 00:54:51', '2025-03-15 00:54:51');
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (13, 1, 1, 2023, '广东省', 675, 690, 25, 475, '物理', '数学', '2025-03-15 00:54:51', '2025-03-15 00:54:51');
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (14, 1, 1, 2023, '浙江省', 680, 693, 35, 485, '物理', '数学', '2025-03-15 00:54:51', '2025-03-15 00:54:51');
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (15, 1, 2, 2023, '北京市', 683, 693, 35, 495, '物理', '数学', '2025-03-15 00:54:51', '2025-03-15 00:54:51');
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (16, 1, 2, 2023, '广东省', 670, 685, 20, 475, '物理', '数学', '2025-03-15 00:54:51', '2025-03-15 00:54:51');
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (17, 1, 2, 2023, '浙江省', 675, 690, 30, 485, '物理', '数学', '2025-03-15 00:54:51', '2025-03-15 00:54:51');
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (18, 1, 3, 2023, '北京市', 680, 690, 40, 495, '物理', '数学', '2025-03-15 00:54:51', '2025-03-22 16:17:50');
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (19, 1, 3, 2023, '广东省', 665, 680, 25, 475, '物理', '数学', '2025-03-15 00:54:51', '2025-03-22 16:17:50');
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (20, 1, 3, 2023, '江苏省', 670, 685, 30, 480, '物理', '数学', '2025-03-15 00:54:51', '2025-03-22 16:17:50');
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (21, 1, 4, 2023, '浙江省', 660, 675, 55, 485, '物理', '生物', '2025-03-15 00:54:51', '2025-03-22 16:17:50');
INSERT INTO `major_score` (`id`, `school_id`, `major_id`, `year`, `province`, `min_score`, `max_score`, `plan_number`, `province_control_line`, `first_subject`, `second_subject`, `create_time`, `update_time`) VALUES (22, 1, 4, 2023, '广东省', 650, 665, 45, 475, '物理', '生物', '2025-03-15 00:54:51', '2025-03-22 16:17:50');
COMMIT;

-- ----------------------------
-- Table structure for school_info
-- ----------------------------
DROP TABLE IF EXISTS `school_info`;
CREATE TABLE `school_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学校名称',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学校代码',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '学校类型（本科、专科等）',
  `level` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '学校层次（985、211等）',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所在省份',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所在城市',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '详细地址',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '学校简介',
  `website` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '官网地址',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_code` (`code`) USING BTREE,
  KEY `idx_province_city` (`province`,`city`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='学校信息表';

-- ----------------------------
-- Records of school_info
-- ----------------------------
BEGIN;
INSERT INTO `school_info` (`id`, `name`, `code`, `type`, `level`, `province`, `city`, `address`, `description`, `website`, `create_time`, `update_time`) VALUES (1, '广州航海学院', '12573', '本科', '普通高等学校', '广东省', '广州市', '广东省广州市黄埔区红山大道8号', '广州航海学院（Guangzhou Maritime University）是广东省人民政府举办的全日制普通本科高等学校。', 'https://www.gzhmu.edu.cn', '2025-03-11 23:14:06', '2025-03-22 15:13:15');
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '昵称',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ROLE_USER' COMMENT '用户角色：ROLE_ADMIN-管理员，ROLE_USER-普通用户',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '/static/images/logo5.png' COMMENT '用户头像URL',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_username` (`username`) USING BTREE,
  KEY `idx_phone` (`phone`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` (`id`, `username`, `password`, `nickname`, `phone`, `email`, `role`, `status`, `create_time`, `update_time`, `avatar`) VALUES (1, 'admin', '$2a$10$zUptj5F/pCHM56kb01Bfe.MEm6iZ.U5vlFwFJANl1LeAQSvGQMWkC', '管理员', '13800138000', 'admin@example.com', 'ROLE_ADMIN', 1, '2025-03-11 23:14:06', '2025-03-12 19:39:30', '/static/images/logo5.png');
INSERT INTO `user` (`id`, `username`, `password`, `nickname`, `phone`, `email`, `role`, `status`, `create_time`, `update_time`, `avatar`) VALUES (2, 'user1', '$2a$10$X/hxGJ8wZH.jB5sFHxzqPeYwLQv4ZyQvG7HB.1WqWzYFPVJQzQUxu', '用户1', '13800138001', 'user1@example.com', 'ROLE_USER', 1, '2025-03-11 23:14:06', '2025-03-11 23:14:06', '/static/images/logo5.png');
INSERT INTO `user` (`id`, `username`, `password`, `nickname`, `phone`, `email`, `role`, `status`, `create_time`, `update_time`, `avatar`) VALUES (3, 'azu', '$2a$10$ZIP9IOtjUQL.pNfUCWQ7teqwlbuFK8ItwW5Yo8KmxB2aSwtaXWZv6', NULL, '15217664810', 'cenyaozu@126.com', 'ROLE_USER', 1, '2025-03-12 20:11:53', '2025-03-12 20:11:53', '/static/images/logo5.png');
INSERT INTO `user` (`id`, `username`, `password`, `nickname`, `phone`, `email`, `role`, `status`, `create_time`, `update_time`, `avatar`) VALUES (4, 'azua', '$2a$10$aH0RcT0RINrL2v8HQRJuX.5wGSSOJD3vOcFmJaNI9J9zMWkwNwlr6', NULL, '15217664810', 'cenyaozu@126.com', 'ROLE_USER', 1, '2025-03-12 20:17:02', '2025-03-12 20:17:02', '/static/images/logo5.png');
INSERT INTO `user` (`id`, `username`, `password`, `nickname`, `phone`, `email`, `role`, `status`, `create_time`, `update_time`, `avatar`) VALUES (5, 'azu321', '$2a$10$XTP4J8eqGDjxW8cWHh/FJOs.lbqFxcM8eNwz0i3rxckOnSCjkG33C', NULL, '15217664810', 'cenyaozu@126.com', 'ROLE_USER', 1, '2025-03-12 20:17:15', '2025-03-12 20:17:15', '/static/images/logo5.png');
INSERT INTO `user` (`id`, `username`, `password`, `nickname`, `phone`, `email`, `role`, `status`, `create_time`, `update_time`, `avatar`) VALUES (6, 'aua2313', '$2a$10$griUORlUbs5fetKO3mGuT.CUzW80hZAf1qvjD7zLFjMsu0dRABmqS', NULL, '15217664810', 'cenyaozu@126.com', 'ROLE_USER', 1, '2025-03-12 20:24:34', '2025-03-12 20:24:34', '/static/images/logo5.png');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
