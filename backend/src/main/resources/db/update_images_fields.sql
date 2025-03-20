-- 学院表添加图片字段
ALTER TABLE college 
ADD COLUMN logo_url VARCHAR(255) COMMENT '学院Logo图片URL' AFTER description,
ADD COLUMN banner_url VARCHAR(255) COMMENT '学院横幅图片URL' AFTER logo_url;

-- 专业表添加图片字段和其他新字段
ALTER TABLE major_info 
ADD COLUMN training_objectives TEXT COMMENT '专业培养目标' AFTER description,
ADD COLUMN main_courses TEXT COMMENT '主要课程' AFTER training_objectives,
ADD COLUMN icon_url VARCHAR(255) COMMENT '专业图标URL' AFTER career_prospects,
ADD COLUMN images JSON COMMENT '专业相关图片URL列表' AFTER icon_url;

-- 更新已有数据，设置默认图片路径
UPDATE college SET 
logo_url = CONCAT('/static/images/college/', LOWER(REPLACE(name, ' ', '-')), '-logo.png'),
banner_url = CONCAT('/static/images/college/', LOWER(REPLACE(name, ' ', '-')), '.jpg');

-- 为专业设置默认图标
UPDATE major_info SET 
icon_url = CONCAT('/static/images/major/', LOWER(REPLACE(name, ' ', '-')), '-icon.png'),
images = JSON_ARRAY(
  CONCAT('/static/images/major/', LOWER(REPLACE(name, ' ', '-')), '-1.jpg'),
  CONCAT('/static/images/major/', LOWER(REPLACE(name, ' ', '-')), '-2.jpg'),
  CONCAT('/static/images/major/', LOWER(REPLACE(name, ' ', '-')), '-3.jpg')
); 