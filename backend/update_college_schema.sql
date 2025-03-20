-- 创建院系信息表
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

-- 添加college_id字段到major_info表
ALTER TABLE `major_info` ADD COLUMN `college_id` BIGINT COMMENT '所属院系ID' AFTER `school_id`;
ALTER TABLE `major_info` ADD KEY `idx_college_id` (`college_id`);
ALTER TABLE `major_info` ADD CONSTRAINT `fk_major_college` FOREIGN KEY (`college_id`) REFERENCES `college` (`id`);

-- 插入测试院系数据
INSERT INTO `college` (`name`, `degree`, `duration`, `description`, `school_id`) VALUES
('航运学院', '本科', '4', '航运学院致力于培养航运领域的高素质人才，拥有完善的教学设施和优秀的师资力量。', 1),
('海洋装备工程学院', '本科', '4', '海洋装备工程学院专注于海洋工程装备的研发与应用，培养海洋工程领域的专业人才。', 1),
('低空装备与智能控制学院', '本科', '4', '低空装备与智能控制学院是新兴交叉学科学院，致力于培养无人机等低空装备研发与应用人才。', 2),
('智能制造学院', '本科', '4', '智能制造学院聚焦工业4.0和智能制造领域，培养具有创新能力的高级工程技术人才。', 2),
('智能交通与工程学院', '本科', '4', '智能交通与工程学院立足未来交通发展，培养具备智能交通系统设计与管理能力的专业人才。', 3);

-- 创建临时表存储专业名称与学院ID的映射关系
CREATE TEMPORARY TABLE major_college_mapping (
    major_name VARCHAR(100),
    college_id BIGINT
);

-- 设置专业与学院的对应关系
INSERT INTO major_college_mapping (major_name, college_id) VALUES
('计算机科学与技术', 4),
('人工智能', 4),
('金融学', 3),
('医学', 3);

-- 更新专业表中的college_id
UPDATE major_info m
JOIN major_college_mapping mcm ON m.name = mcm.major_name
SET m.college_id = mcm.college_id;

-- 删除临时表
DROP TEMPORARY TABLE major_college_mapping; 