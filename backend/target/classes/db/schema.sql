-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `phone` VARCHAR(20) COMMENT '手机号',
    `email` VARCHAR(100) COMMENT '邮箱',
    `role` VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER' COMMENT '用户角色：ROLE_ADMIN-管理员，ROLE_USER-普通用户',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 学校信息表
CREATE TABLE IF NOT EXISTS `school_info` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '学校名称',
    `code` VARCHAR(20) NOT NULL COMMENT '学校代码',
    `type` VARCHAR(50) COMMENT '学校类型（本科、专科等）',
    `level` VARCHAR(50) COMMENT '学校层次（985、211等）',
    `province` VARCHAR(50) NOT NULL COMMENT '所在省份',
    `city` VARCHAR(50) NOT NULL COMMENT '所在城市',
    `address` VARCHAR(200) COMMENT '详细地址',
    `description` TEXT COMMENT '学校简介',
    `website` VARCHAR(200) COMMENT '官网地址',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`),
    KEY `idx_province_city` (`province`, `city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学校信息表';

-- 院系信息表
CREATE TABLE IF NOT EXISTS `college` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '院系名称',
    `degree` VARCHAR(50) COMMENT '学历层次（本科、专科等）',
    `duration` VARCHAR(20) COMMENT '学制（年）',
    `description` TEXT COMMENT '院系介绍',
    `school_id` BIGINT NOT NULL COMMENT '所属学校ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_school_id` (`school_id`),
    CONSTRAINT `fk_college_school` FOREIGN KEY (`school_id`) REFERENCES `school_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='院系信息表';

-- 专业信息表
CREATE TABLE IF NOT EXISTS `major_info` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `school_id` BIGINT NOT NULL COMMENT '所属学校ID',
    `college_id` BIGINT COMMENT '所属院系ID',
    `name` VARCHAR(100) NOT NULL COMMENT '专业名称',
    `code` VARCHAR(20) NOT NULL COMMENT '专业代码',
    `degree` VARCHAR(50) COMMENT '学位类型',
    `duration` VARCHAR(20) COMMENT '学制',
    `description` TEXT COMMENT '专业介绍',
    `career_prospects` TEXT COMMENT '就业前景',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_school_id` (`school_id`),
    KEY `idx_college_id` (`college_id`),
    CONSTRAINT `fk_major_school` FOREIGN KEY (`school_id`) REFERENCES `school_info` (`id`),
    CONSTRAINT `fk_major_college` FOREIGN KEY (`college_id`) REFERENCES `college` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专业信息表';

-- 招生政策表
CREATE TABLE IF NOT EXISTS `admission_policy` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `school_id` BIGINT NOT NULL COMMENT '学校ID',
    `year` INT NOT NULL COMMENT '年份',
    `province` VARCHAR(50) NOT NULL COMMENT '招生省份',
    `plan_number` INT COMMENT '计划招生人数',
    `min_score` INT COMMENT '最低录取分数',
    `category` VARCHAR(50) COMMENT '科类（理科、文科等）',
    `policy_content` TEXT COMMENT '政策详细内容',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_school_year_province` (`school_id`, `year`, `province`),
    CONSTRAINT `fk_policy_school` FOREIGN KEY (`school_id`) REFERENCES `school_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='招生政策表';

-- 插入测试用户数据
INSERT INTO `user` (`username`, `password`, `nickname`, `phone`, `email`, `role`, `status`) VALUES
('admin', '$2a$10$X/hxGJ8wZH.jB5sFHxzqPeYwLQv4ZyQvG7HB.1WqWzYFPVJQzQUxu', '管理员', '13800138000', 'admin@example.com', 'ROLE_ADMIN', 1),
('user1', '$2a$10$X/hxGJ8wZH.jB5sFHxzqPeYwLQv4ZyQvG7HB.1WqWzYFPVJQzQUxu', '用户1', '13800138001', 'user1@example.com', 'ROLE_USER', 1);

-- 插入测试学校数据
INSERT INTO `school_info` (`name`, `code`, `type`, `level`, `province`, `city`, `address`, `description`, `website`) VALUES
('清华大学', '10003', '本科', '985,211', '北京市', '北京市', '北京市海淀区清华大学', '清华大学（Tsinghua University）是中国著名高等学府，是国家重点建设的综合性、研究型大学。', 'https://www.tsinghua.edu.cn'),
('北京大学', '10001', '本科', '985,211', '北京市', '北京市', '北京市海淀区颐和园路5号', '北京大学（Peking University）创办于1898年，是中国第一所国立综合性大学。', 'https://www.pku.edu.cn'),
('浙江大学', '10335', '本科', '985,211', '浙江省', '杭州市', '浙江省杭州市西湖区余杭塘路866号', '浙江大学（Zhejiang University）是中国著名高等学府，是国家重点建设的综合性、研究型大学。', 'https://www.zju.edu.cn');

-- 插入测试院系数据
INSERT INTO `college` (`name`, `degree`, `duration`, `description`, `school_id`) VALUES
('航运学院', '本科', '4', '航运学院致力于培养航运领域的高素质人才，拥有完善的教学设施和优秀的师资力量。', 1),
('海洋装备工程学院', '本科', '4', '海洋装备工程学院专注于海洋工程装备的研发与应用，培养海洋工程领域的专业人才。', 1),
('低空装备与智能控制学院', '本科', '4', '低空装备与智能控制学院是新兴交叉学科学院，致力于培养无人机等低空装备研发与应用人才。', 2),
('智能制造学院', '本科', '4', '智能制造学院聚焦工业4.0和智能制造领域，培养具有创新能力的高级工程技术人才。', 2),
('智能交通与工程学院', '本科', '4', '智能交通与工程学院立足未来交通发展，培养具备智能交通系统设计与管理能力的专业人才。', 3);

-- 插入测试专业数据
INSERT INTO `major_info` (`school_id`, `college_id`, `name`, `code`, `degree`, `duration`, `description`, `career_prospects`) VALUES
(1, 1, '航海技术', '081901', '工学学士', '4年', '培养具备船舶驾驶、海上运输组织与管理等能力的高级航海人才', '毕业生可在航运公司、港口、海事管理等单位从事船舶驾驶、运输管理等工作'),
(1, 1, '轮机工程', '081902', '工学学士', '4年', '培养具备船舶动力装置操作、维护与管理等能力的专业人才', '毕业生可在航运公司、船厂、海事管理等单位从事船舶轮机管理等工作'),
(1, 2, '船舶与海洋工程', '082401', '工学学士', '4年', '培养具备船舶与海洋工程结构物设计、建造与管理能力的专业人才', '毕业生可在船厂、海洋工程公司、科研院所等单位工作'),
(1, 2, '海洋资源开发技术', '082402', '工学学士', '4年', '培养掌握海洋资源勘探、开发和利用技术的专业人才', '毕业生可在海洋资源开发企业、海洋环境保护等领域工作'),
(2, 3, '无人机应用技术', '083101', '工学学士', '4年', '培养掌握无人机设计、制造、操控及应用的专业人才', '毕业生可在无人机制造、航拍测绘、物流配送等行业工作'),
(2, 3, '智能控制技术', '083102', '工学学士', '4年', '培养具备自动控制、智能系统设计与应用能力的专业人才', '毕业生可在智能制造、自动化系统集成等领域工作'),
(2, 4, '机械设计制造及自动化', '083201', '工学学士', '4年', '培养掌握机械设计、制造及自动化技术的专业人才', '毕业生可在制造业、自动化设备企业等单位工作'),
(2, 4, '工业机器人技术', '083202', '工学学士', '4年', '培养具备工业机器人应用、维护与管理能力的专业人才', '毕业生可在机器人企业、自动化生产线等领域工作'),
(3, 5, '智能交通技术', '083301', '工学学士', '4年', '培养掌握交通管理系统设计与应用的专业人才', '毕业生可在智能交通系统开发、交通管理等领域工作'),
(3, 5, '新能源汽车技术', '083302', '工学学士', '4年', '培养具备新能源汽车设计、制造、维修能力的专业人才', '毕业生可在新能源汽车企业、汽车研发等领域工作');

-- 插入测试招生政策数据
INSERT INTO `admission_policy` (`school_id`, `year`, `province`, `plan_number`, `min_score`, `category`, `policy_content`) VALUES
(1, 2023, '北京市', 300, 680, '理科', '1. 综合评价录取：高考成绩占比90%，高中学业水平考试成绩占比10%\n2. 特殊类型招生：强基计划、自主招生等'),
(1, 2023, '浙江省', 200, 670, '综合改革', '1. 采用"3+3"模式录取\n2. 选考科目要求：物理、化学、生物至少选考一门'),
(2, 2023, '北京市', 280, 670, '理科', '1. 常规批次录取\n2. 特殊类型招生：博雅人才培养计划等'),
(2, 2023, '江苏省', 180, 665, '理科', '1. 采用院系专业组投档模式\n2. 选考科目要求：物理必选，化学、生物选考一门'),
(3, 2023, '浙江省', 350, 650, '综合改革', '1. 普通类招生专业组\n2. 特殊类型招生：三位一体综合评价等');