-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `phone` VARCHAR(20) COMMENT '手机号',
    `email` VARCHAR(100) COMMENT '邮箱',
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

-- 专业信息表
CREATE TABLE IF NOT EXISTS `major_info` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `school_id` BIGINT NOT NULL COMMENT '所属学校ID',
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
    CONSTRAINT `fk_major_school` FOREIGN KEY (`school_id`) REFERENCES `school_info` (`id`)
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
INSERT INTO `user` (`username`, `password`, `nickname`, `phone`, `email`, `status`) VALUES
('admin', '$2a$10$X/hxGJ8wZH.jB5sFHxzqPeYwLQv4ZyQvG7HB.1WqWzYFPVJQzQUxu', '管理员', '13800138000', 'admin@example.com', 1),
('teacher1', '$2a$10$X/hxGJ8wZH.jB5sFHxzqPeYwLQv4ZyQvG7HB.1WqWzYFPVJQzQUxu', '招生老师1', '13800138001', 'teacher1@example.com', 1),
('student1', '$2a$10$X/hxGJ8wZH.jB5sFHxzqPeYwLQv4ZyQvG7HB.1WqWzYFPVJQzQUxu', '学生1', '13800138002', 'student1@example.com', 1);

-- 插入测试学校数据
INSERT INTO `school_info` (`name`, `code`, `type`, `level`, `province`, `city`, `address`, `description`, `website`) VALUES
('清华大学', '10003', '本科', '985,211', '北京市', '北京市', '北京市海淀区清华大学', '清华大学（Tsinghua University）是中国著名高等学府，是国家重点建设的综合性、研究型大学。', 'https://www.tsinghua.edu.cn'),
('北京大学', '10001', '本科', '985,211', '北京市', '北京市', '北京市海淀区颐和园路5号', '北京大学（Peking University）创办于1898年，是中国第一所国立综合性大学。', 'https://www.pku.edu.cn'),
('浙江大学', '10335', '本科', '985,211', '浙江省', '杭州市', '浙江省杭州市西湖区余杭塘路866号', '浙江大学（Zhejiang University）是中国著名高等学府，是国家重点建设的综合性、研究型大学。', 'https://www.zju.edu.cn');

-- 插入测试专业数据
INSERT INTO `major_info` (`school_id`, `name`, `code`, `degree`, `duration`, `description`, `career_prospects`) VALUES
(1, '计算机科学与技术', '080901', '工学学士', '4年', '计算机科学与技术专业培养具备计算机硬件、软件与网络系统等方面的知识和能力的高级人才。', '可在IT企业、科研机构、金融机构等从事软件开发、系统架构设计等工作。'),
(1, '人工智能', '080910', '工学学士', '4年', '人工智能专业培养具备机器学习、深度学习、计算机视觉等方面知识和能力的高级人才。', '可在AI企业、研究所、互联网公司等从事算法研发、AI应用开发等工作。'),
(2, '金融学', '020301', '经济学学士', '4年', '金融学专业培养具备金融理论、金融市场、投资管理等方面知识和能力的高级人才。', '可在银行、证券公司、基金公司等金融机构从事投资、理财、风控等工作。'),
(3, '医学', '100201', '医学学士', '5年', '医学专业培养具备基础医学、临床医学等方面知识和能力的医疗人才。', '可在医院、医疗机构、医药企业等从事医疗、研究、管理等工作。');

-- 角色表
CREATE TABLE IF NOT EXISTS `role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `code` VARCHAR(50) NOT NULL COMMENT '角色编码',
    `description` VARCHAR(200) COMMENT '角色描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `user_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    CONSTRAINT `fk_ur_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk_ur_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 权限表
CREATE TABLE IF NOT EXISTS `permission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '权限名称',
    `code` VARCHAR(100) NOT NULL COMMENT '权限编码',
    `type` TINYINT NOT NULL COMMENT '权限类型：1-菜单，2-按钮，3-接口',
    `parent_id` BIGINT COMMENT '父权限ID',
    `path` VARCHAR(200) COMMENT '路径',
    `icon` VARCHAR(100) COMMENT '图标',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS `role_permission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT NOT NULL COMMENT '权限ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
    CONSTRAINT `fk_rp_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
    CONSTRAINT `fk_rp_permission` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 插入基础角色数据
INSERT INTO `role` (`name`, `code`, `description`, `status`) VALUES
('超级管理员', 'SUPER_ADMIN', '系统超级管理员，拥有所有权限', 1),
('招生老师', 'TEACHER', '负责招生咨询和答疑的教师', 1),
('学生', 'STUDENT', '普通学生用户', 1);

-- 插入基础权限数据
INSERT INTO `permission` (`name`, `code`, `type`, `parent_id`, `path`, `icon`, `sort`, `status`) VALUES
-- 系统管理
(1, '系统管理', 'system', 1, NULL, '/system', 'setting', 1, 1),
(2, '用户管理', 'system:user', 1, 1, '/system/user', 'user', 1, 1),
(3, '角色管理', 'system:role', 1, 1, '/system/role', 'team', 2, 1),
(4, '权限管理', 'system:permission', 1, 1, '/system/permission', 'safety', 3, 1),
-- 招生管理
(5, '招生管理', 'admission', 1, NULL, '/admission', 'solution', 2, 1),
(6, '学校管理', 'admission:school', 1, 5, '/admission/school', 'bank', 1, 1),
(7, '专业管理', 'admission:major', 1, 5, '/admission/major', 'profile', 2, 1),
(8, '政策管理', 'admission:policy', 1, 5, '/admission/policy', 'file-text', 3, 1),
-- 咨询管理
(9, '咨询管理', 'consultation', 1, NULL, '/consultation', 'message', 3, 1),
(10, '咨询列表', 'consultation:list', 1, 9, '/consultation/list', 'ordered-list', 1, 1),
(11, '咨询回复', 'consultation:reply', 1, 9, '/consultation/reply', 'comment', 2, 1);

-- 插入角色权限关联数据
-- 超级管理员拥有所有权限
INSERT INTO `role_permission` (`role_id`, `permission_id`)
SELECT 1, id FROM `permission`;

-- 招生老师权限
INSERT INTO `role_permission` (`role_id`, `permission_id`)
SELECT 2, id FROM `permission` WHERE `code` LIKE 'admission%' OR `code` LIKE 'consultation%';

-- 学生权限
INSERT INTO `role_permission` (`role_id`, `permission_id`)
SELECT 3, id FROM `permission` WHERE `code` IN ('consultation:list');

-- 插入用户角色关联数据
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES
(1, 1),  -- admin -> 超级管理员
(2, 2),  -- teacher1 -> 招生老师
(3, 3);  -- student1 -> 学生

-- 插入测试招生政策数据
INSERT INTO `admission_policy` (`school_id`, `year`, `province`, `plan_number`, `min_score`, `category`, `policy_content`) VALUES
(1, 2023, '北京市', 300, 680, '理科', '1. 综合评价录取：高考成绩占比90%，高中学业水平考试成绩占比10%\n2. 特殊类型招生：强基计划、自主招生等'),
(1, 2023, '浙江省', 200, 670, '综合改革', '1. 采用"3+3"模式录取\n2. 选考科目要求：物理、化学、生物至少选考一门'),
(2, 2023, '北京市', 280, 670, '理科', '1. 常规批次录取\n2. 特殊类型招生：博雅人才培养计划等'),
(2, 2023, '江苏省', 180, 665, '理科', '1. 采用院系专业组投档模式\n2. 选考科目要求：物理必选，化学、生物选考一门'),
(3, 2023, '浙江省', 350, 650, '综合改革', '1. 普通类招生专业组\n2. 特殊类型招生：三位一体综合评价等');