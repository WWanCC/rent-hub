-- =================================================================
--                 数据库表结构 (DDL)
-- =================================================================

DROP TABLE IF EXISTS `emp`;
CREATE TABLE `emp`
(
    `id`         int      NOT NULL AUTO_INCREMENT,
    `username`   varchar(50)  DEFAULT NULL,
    `password`   varchar(100) DEFAULT NULL,
    `real_name`  varchar(20)  DEFAULT NULL COMMENT '员工真名',
    `phone`      varchar(11)  DEFAULT NULL,
    `role`       varchar(255) DEFAULT NULL COMMENT '分配角色',
    `created_at` datetime NOT NULL,
    `updated_at` datetime NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`)
) ENGINE = InnoDB COMMENT ='后台员工';

DROP TABLE IF EXISTS `emp_role`;
CREATE TABLE `emp_role`
(
    `emp_id`  int NOT NULL,
    `role_id` int NOT NULL,
    PRIMARY KEY (`emp_id`, `role_id`)
) ENGINE = InnoDB COMMENT ='员工-角色中间表';

DROP TABLE IF EXISTS `house`;
CREATE TABLE `house`
(
    `id`                int            NOT NULL AUTO_INCREMENT,
    `image`             varchar(255) DEFAULT NULL,
    `title`             varchar(100) DEFAULT NULL,
    `region_id`         int          DEFAULT NULL,
    `region_name`       varchar(10)    NOT NULL COMMENT '转单表查询',
    `address_detail`    varchar(255)   NOT NULL COMMENT '详细地址',
    `price_per_month`   decimal(10, 2) NOT NULL COMMENT '每月租金',
    `area`              int            NOT NULL COMMENT '房源面积',
    `layout`            int            NOT NULL COMMENT '房间数量',
    `status`            int            NOT NULL COMMENT '0下架，1待租，2签约中， 3已签约',
    `created_by_emp_id` int            NOT NULL COMMENT '创建房源的员工id',
    `created_at`        datetime       NOT NULL,
    `updated_at`        datetime       NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='房源';

DROP TABLE IF EXISTS `house_tag`;
CREATE TABLE `house_tag`
(
    `house_id` int NOT NULL COMMENT '房源表 house.id',
    `tag_id`   int NOT NULL COMMENT '标签表 tag.id',
    PRIMARY KEY (`house_id`, `tag_id`)
) ENGINE = InnoDB COMMENT ='房源_标签中间表';

DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification`
(
    `id`              int          NOT NULL AUTO_INCREMENT,
    `user_id`         int         DEFAULT NULL COMMENT '引用user表逻辑外键',
    `emp_id`          int         DEFAULT NULL COMMENT '引用emp表逻辑外键',
    `title`           varchar(100) NOT NULL COMMENT '通知标题',
    `content`         varchar(255) NOT NULL COMMENT '通知内容   ',
    `created_at`      datetime    DEFAULT (now()) COMMENT '  创建时间 ',
    `delivery_status` varchar(20) DEFAULT NULL COMMENT '消息投递状态(引入MQ队列后)默认PENDING,把信息插入通知表后,立即发送给CB端,之后修改为sent状态',
    `user_is_read`    tinyint     DEFAULT '0',
    `emp_is_read`     tinyint     DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;

DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`
(
    `id`             int         NOT NULL AUTO_INCREMENT,
    `name`           varchar(20) NOT NULL COMMENT '权限中文名',
    `permission_key` varchar(50) NOT NULL COMMENT '权限标识',
    `created_at`     datetime    NOT NULL,
    `updated_at`     datetime    NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='权限表';

DROP TABLE IF EXISTS `region`;
CREATE TABLE `region`
(
    `id`        int         NOT NULL AUTO_INCREMENT,
    `name`      varchar(10) NOT NULL COMMENT '地区名称',
    `parent_id` int DEFAULT NULL COMMENT '上级地区id （现在以 parent_id:广州）暂无其他城市',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_region_name` (`name`)
) ENGINE = InnoDB COMMENT ='地区表 ';

DROP TABLE IF EXISTS `rental_contract`;
CREATE TABLE `rental_contract`
(
    `id`                 int     NOT NULL AUTO_INCREMENT,
    `contract_no`        varchar(50)    DEFAULT NULL COMMENT '合同编号, 唯一,带特殊格式的字符串',
    `house_id`           int            DEFAULT NULL COMMENT '外键, 关联 house_.id',
    `user_id`            int            DEFAULT NULL COMMENT '外键, 关联 user.id',
    `emp_id`             int            DEFAULT NULL COMMENT '外键, 关联 emp.id',
    `final_price`        decimal(10, 2) DEFAULT NULL COMMENT '实际月租金',
    `start_date`         date           DEFAULT NULL COMMENT '合同开始日期',
    `end_date`           date           DEFAULT NULL COMMENT '合同结束日期',
    `status`             tinyint        DEFAULT NULL COMMENT '合同状态 (1:进行中, 2:签约完成)',
    `created_at`         datetime       DEFAULT NULL COMMENT '签约时间',
    `is_expiry_notified` tinyint NOT NULL COMMENT '是否已发送到期提醒 (0:未发送, 1:已发送)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `rental_contract_pk` (`contract_no`)
) ENGINE = InnoDB COMMENT ='租赁合同表';

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id`         int         NOT NULL AUTO_INCREMENT,
    `name`       varchar(20) NOT NULL COMMENT '角色中文名',
    `role_key`   varchar(50) NOT NULL COMMENT '角色标识',
    `created_at` datetime    NOT NULL,
    `updated_at` datetime    NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='角色表';

DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`
(
    `role_id`       int NOT NULL,
    `permission_id` int NOT NULL,
    PRIMARY KEY (`permission_id`, `role_id`)
) ENGINE = InnoDB COMMENT ='角色-权限中间表';

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`
(
    `id`             int         NOT NULL AUTO_INCREMENT,
    `parent_id`      int          DEFAULT NULL COMMENT '父菜单ID,用于构建树结构',
    `permission_key` int         NOT NULL COMMENT '权限标识',
    `name`           varchar(20) NOT NULL COMMENT '菜单项名',
    `path`           varchar(255) DEFAULT NULL COMMENT '前端路由路径',
    `component`      varchar(255) DEFAULT NULL COMMENT '前端组件路径',
    `icon`           int          DEFAULT NULL COMMENT '菜单图标的类名或路径',
    `order_num`      int         NOT NULL COMMENT '控制显示排序',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='管理系统菜单';

DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`
(
    `id`   int         NOT NULL AUTO_INCREMENT,
    `name` varchar(15) NOT NULL COMMENT '标签名',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tag_name` (`name`)
) ENGINE = InnoDB COMMENT ='房源标签表';

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`         int      NOT NULL AUTO_INCREMENT,
    `phone`      char(11) NOT NULL COMMENT '唯一手机号-作为登录用户名',
    `password`   varchar(100) DEFAULT NULL COMMENT '登录密码',
    `created_at` datetime     DEFAULT NULL,
    `updated_at` datetime     DEFAULT NULL,
    `status`     int      NOT NULL COMMENT '0已注销，1正常',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_phone` (`phone`)
) ENGINE = InnoDB COMMENT ='用户表';

DROP TABLE IF EXISTS `user_detail`;
CREATE TABLE `user_detail`
(
    `user_id`          int        NOT NULL,
    `identity_card_id` char(18)   NOT NULL COMMENT '用户身份证号',
    `real_name`        varchar(6) NOT NULL COMMENT '用户真实姓名',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `user_detail_pk` (`identity_card_id`)
) ENGINE = InnoDB COMMENT ='用户详细信息';

DROP TABLE IF EXISTS `user_favorite`;
CREATE TABLE `user_favorite`
(
    `user_id`    int      NOT NULL COMMENT '用户表 user.id',
    `house_id`   int      NOT NULL COMMENT '房源表 house.id',
    `created_at` datetime NOT NULL,
    PRIMARY KEY (`user_id`, `house_id`) COMMENT '联合主键,确保一个用户不能收藏多次同一个房源'
) ENGINE = InnoDB COMMENT ='用户收藏表';

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`
(
    `user_id` int NOT NULL,
    `role_id` int NOT NULL,
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE = InnoDB COMMENT ='用户-角色中间表';


-- =================================================================
--                 数据库初始数据 (DML)
-- =================================================================

INSERT INTO `emp`
VALUES (1, 'Bai', 'abcd1234', '小白', '13926012332', '暂无', '2025-06-26 15:40:02', '2025-06-26 15:40:03'),
       (2, 'chenmin', 'abcd1234', '陈敏', '15900088702', '暂无', '2025-06-26 15:40:05', '2025-06-26 15:40:06'),
       (3, 'yangyongqiang', 'abcd1234', '杨永强', '13535875421', '暂无', '2025-06-26 15:40:07', '2025-06-26 15:40:07'),
       (22, 'bai2', '$2a$10$vCbt32lpNpi1o/O4wjgOZ.iskRGsGamrFzLmQzauFQpLobjTjhisq', '铭千店长', '15900086700',
        'BranchManager', '2025-07-10 21:56:09', '2025-07-10 21:56:09'),
       (23, 'bai5', '$2a$10$4bOrFPRNBhVjoDnqCHKDIOXE4dYr9lm8wju36DF2.thOTyRIwUGAa', '铭铭组长', '15900086701',
        'TeamLeader', '2025-07-13 20:17:17', '2025-07-13 20:17:17');
INSERT INTO `emp_role`
VALUES (22, 3),
       (23, 2);
INSERT INTO `house`
VALUES (1, '测试图片链接1', '整租·富力天寓 1室1厅 南', 1, '天河', '天河-广氮-富力天寓', 2300.00, 46, 1, 3, 1,
        '2025-06-26 14:46:07', '2025-06-26 14:46:14'),
       (2, '测试图片链接2', '合租·兰亭盛荟 3居室 北卧', 1, '天河', '天河-东圃-兰亭盛荟', 2150.00, 10, 1, 1, 1,
        '2025-06-26 15:01:25', '2025-06-26 15:01:27'),
       (3, '测试图片链接3', '整租·水荫一横路 2室2厅 南', 1, '天河', '天河-水荫东-水荫一横路 ', 3500.00, 70, 2, 1, 1,
        '2025-06-26 15:03:16', '2025-06-26 15:03:18'),
       (4, '\nhttps://rent-hub.oss-cn-beijing.aliyuncs.com/listings/2025/08/27/7e218295a91b4b58816169b098eff0e4.jpg',
        '合租·兰亭盛荟 4居室 南卧', 1, '天河', '天河-东圃-兰亭盛荟', 2680.00, 11, 4, 1, 1, '2025-06-26 15:05:01',
        '2025-06-26 15:05:03'),
       (25, NULL, '整租·骏景花园 2室1厅 北', 1, '天河', '天河-棠下-骏景花园 ', 3300.00, 74, 2, 1, 1,
        '2025-06-26 15:09:57', '2025-06-26 15:09:59'),
       (26, NULL, '整租·先烈中路 3室1厅 西南', 2, '越秀', '越秀-黄花岗-先烈中路 ', 3500.00, 60, 3, 1, 2,
        '2025-06-26 15:11:24', '2025-06-26 15:11:25'),
       (27, NULL, '合租·水荫路34号大院 4居室 北卧', 2, '越秀', '水荫路34号大院', 2000.00, 18, 4, 1, 2,
        '2025-06-26 15:12:38', '2025-06-26 15:12:40'),
       (28, NULL, '整租·淘金路 1室0厅 南/北', 2, '越秀', '越秀-淘金-淘金路 ', 2800.00, 25, 1, 1, 2,
        '2025-06-26 15:14:53', '2025-06-26 15:14:54'),
       (29, NULL, '合租·先烈南路青菜东街 3居室 东南卧', 2, '越秀', '越秀-建设路-先烈南路青菜东街 ', 2100.00, 11, 3, 1,
        2, '2025-06-26 15:16:08', '2025-06-26 15:16:09'),
       (30, NULL, '整租·海珠中路 3室1厅 南\n', 2, '越秀', '越秀-五羊新城-璟泰大厦', 3900.00, 40, 3, 1, 2,
        '2025-06-26 15:18:53', '2025-06-26 15:18:54'),
       (32, NULL, '整租·广东省输变电工程公司上村小区 2室2厅 东南', 3, '荔湾',
        '荔湾-南岸路-广东省输变电工程公司上村小区 ', 5000.00, 76, 2, 1, 3, '2025-06-26 15:21:15',
        '2025-06-26 15:21:17'),
       (33, NULL, '合租·富力广场D区 5居室 西卧', 3, '荔湾', '荔湾-中山八-富力广场D区 ', 1250.00, 5, 5, 1, 3,
        '2025-06-26 15:22:24', '2025-06-26 15:22:25'),
       (34, NULL, '合租·壹诚大厦 3居室 西卧', 3, '荔湾', '荔湾-东塱-壹诚大厦', 1300.00, 10, 3, 1, 3,
        '2025-06-26 15:24:02', '2025-06-26 15:24:03'),
       (35, NULL, '合租·东浚荔景苑 4居室 北卧', 3, '荔湾', '荔湾-东浚荔景苑', 1630.00, 12, 4, 1, 3,
        '2025-06-26 15:26:46', '2025-06-26 15:26:47'),
       (36, NULL, '整租·河沙花园 2室1厅 南', 3, '荔湾', '荔湾-大坦沙-河沙花园', 4000.00, 73, 2, 1, 3,
        '2025-06-26 15:30:02', '2025-06-26 15:30:05'),
       (40, NULL, '新增房源', 1, '天河', '测试详细地址', 20000.00, 500, 3, 0, 1, '2025-07-10 18:20:10',
        '2025-07-10 18:20:10'),
       (41, NULL, '测试新增房源', 1, '天河', '测试详细地址2', 20000.00, 2, 4, 0, 1, '2025-07-10 18:26:46',
        '2025-07-10 18:26:46'),
       (42, NULL, '测试新增房源', 1, '天河', '测试详细地址3', 20000.00, 2, 4, 1, 1, '2025-07-10 18:28:36',
        '2025-07-10 18:28:36');
INSERT INTO `house_tag`
VALUES (1, 4),
       (1, 6),
       (1, 9),
       (1, 12),
       (2, 1),
       (2, 3),
       (2, 7),
       (2, 8),
       (2, 13),
       (3, 2),
       (3, 12),
       (4, 13),
       (25, 12),
       (26, 12),
       (27, 13),
       (28, 12),
       (29, 13),
       (30, 12),
       (32, 12),
       (33, 13),
       (34, 13),
       (35, 13),
       (36, 12),
       (40, 1),
       (40, 2),
       (41, 1),
       (41, 2),
       (42, 1),
       (42, 2);
INSERT INTO `notification`
VALUES (1, 9, 22, '测试单条通知title', '测试content', '2025-08-30 09:37:14', 'PENDING', 1, 1),
       (2, 9, NULL, '您的房屋合同即将到期', '您好，您的合同将于 2025-08-29 到期，请留意。', NULL, NULL, NULL, NULL),
       (3, NULL, 22, '客户合同即将到期提醒', '客户（用户ID: 9）的合同将于 2025-08-29 到期，请及时跟进。', NULL, NULL, NULL,
        NULL),
       (4, 9, NULL, '您的房屋合同即将到期', '您好，您的合同将于 2025-08-29 到期，请留意。', '2025-08-30 04:57:04', NULL, 0,
        0),
       (5, NULL, 22, '客户合同即将到期提醒', '客户（用户ID: 9）的合同将于 2025-08-29 到期，请及时跟进。',
        '2025-08-30 04:57:04', NULL, 0, 0),
       (6, 9, NULL, '您的房屋合同即将到期', '您好，您的合同将于 2025-08-29 到期，请留意。', '2025-08-30 06:46:41', NULL, 0,
        0),
       (7, NULL, 22, '客户合同即将到期提醒', '客户（: 柯鸣谦 ）的合同将于 2025-08-29 到期，请及时跟进。',
        '2025-08-30 06:46:41', NULL, 0, 0),
       (8, 9, NULL, '您的房屋合同即将到期', '您好，您的合同将于 2025-08-29 到期，请留意。', '2025-08-30 06:49:08', NULL, 0,
        0),
       (9, NULL, 22, '客户合同即将到期提醒', '客户（ 柯鸣谦 18520080162）的合同将于 2025-08-29 到期，请及时跟进。',
        '2025-08-30 06:49:08', NULL, 0, 0);
INSERT INTO `permission`
VALUES (1, '创建房源', 'house:create', '2025-07-10 22:20:06', '2025-07-10 22:20:07'),
       (2, '查看房源', 'house:read', '2025-07-10 22:24:36', '2025-07-10 22:24:37'),
       (3, '修改房源', 'house:update', '2025-07-10 22:25:03', '2025-07-10 22:25:04'),
       (4, '删除房源', 'house:delete', '2025-07-10 22:25:28', '2025-07-10 22:25:29'),
       (5, '全部权限', '*', '2025-07-10 22:29:19', '2025-07-10 22:29:22');
INSERT INTO `region`
VALUES (1, '天河', 1),
       (2, '越秀', 1),
       (3, '荔湾', 1),
       (4, '海珠', 1),
       (5, '番禺', 1),
       (6, '白云', 1),
       (7, '黄埔', 1),
       (8, '从化', 1),
       (9, '增城', 1),
       (10, '花都', 1),
       (11, '南沙', 1);
INSERT INTO `rental_contract`
VALUES (2, 'HC-20250827-879330', 1, 9, 22, 9999.00, '2026-09-09', '2027-01-01', 2, '2025-08-27 19:57:59', 0),
       (3, 'HC-10086', 1, 9, 22, 100086.00, '2025-05-29', '2025-08-29', 2, '2025-08-27 12:23:53', 0);
INSERT INTO `role`
VALUES (1, '中介', 'Agent', '2025-07-10 22:15:46', '2025-07-10 22:15:47'),
       (2, '组长 ', 'TeamLeader', '2025-07-10 22:16:04', '2025-07-10 22:16:06'),
       (3, '分店经理', 'BranchManager', '2025-07-10 22:16:57', '2025-07-10 22:16:59'),
       (4, '用户', 'User', '2025-07-10 22:26:18', '2025-07-10 22:26:19');
INSERT INTO `role_permission`
VALUES (1, 1),
       (2, 1),
       (1, 2),
       (2, 2),
       (2, 3),
       (3, 5);
INSERT INTO `tag`
VALUES (6, 'VR房源'),
       (15, '一居'),
       (17, '三居'),
       (20, '业主自荐'),
       (10, '中楼层'),
       (16, '二居'),
       (9, '低楼层'),
       (13, '合租'),
       (18, '四居+'),
       (4, '押一付一'),
       (2, '拎包入住'),
       (12, '整租'),
       (7, '无电梯'),
       (8, '有电梯'),
       (3, '精装修'),
       (19, '认证公寓'),
       (1, '近地铁'),
       (5, '随时看房'),
       (11, '高楼层');
INSERT INTO `user`
VALUES (1, '13926012332', 'abcd1234', '2025-06-26 15:42:36', '2025-06-26 15:42:37', 1),
       (9, '18520080162', '$2a$10$f4c8ALZKo/newMkQHurdiOCR9K.LL6hK/2JmjuldIwSChNt8VTQJS', '2025-07-05 17:11:43',
        '2025-07-07 22:15:32', 1),
       (10, '18520080111', '$2a$10$ASS1ZQRRgRSFX89S.YrWI.kis35lExlmcGMhzMWmcHIApXaMbdcTe', '2025-07-08 09:57:55',
        '2025-07-08 09:57:55', 1),
       (16, '18520080777', '$2a$10$kt9/5ZxnOzSaj7X5DBdBMOkXUk4OVbwkR9uoy3HeNLgNR8VfVSfdC', '2025-07-08 20:15:09',
        '2025-07-08 20:15:09', 1);
INSERT INTO `user_detail`
VALUES (1, '532101198906010015', '陈鹏涛'),
       (9, '440105200409161416', '柯鸣谦'),
       (10, '440105200409161417', '陈陈');
INSERT INTO `user_favorite`
VALUES (1, 1, '2025-06-26 15:52:22'),
       (1, 2, '2025-07-02 14:34:44'),
       (1, 3, '2025-07-02 16:01:19'),
       (1, 4, '2025-07-02 16:01:35'),
       (1, 5, '2025-08-23 21:31:07'),
       (9, 26, '2025-07-06 21:38:54');