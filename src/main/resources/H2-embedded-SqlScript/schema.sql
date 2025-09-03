



-- auto-generated definition
create table `emp`
(
    id         int auto_increment
        primary key,
    username   varchar(50)  null,
    password   varchar(100) null,
    real_name  varchar(20)  null ,
    phone      varchar(11)  null,
    role       varchar(255) null ,
    created_at datetime     not null,
    updated_at datetime     not null,
    constraint username
        unique (username)
)
  ;

-- auto-generated definition
create table `emp_role`
(
    emp_id  int not null,
    role_id int not null,
    primary key (emp_id, role_id)
)
   ;

-- auto-generated definition
create table `house`
(
    id                int auto_increment
        primary key,
    image             varchar(255)   null,
    title             varchar(100)   null,
    region_id         int            null,
    region_name       varchar(10)    not null ,
    address_detail    varchar(255)   not null ,
    price_per_month   decimal(10, 2) not null ,
    area              int            not null,
    layout            int            not null ,
    status            int            not null ,
    created_by_emp_id int            not null ,
    created_at        datetime       not null,
    updated_at        datetime       not null
)
   ;

-- auto-generated definition
create table `house_tag`
(
    house_id int not null ,
    tag_id   int not null ,
    primary key (house_id, tag_id)
)
   ;

-- auto-generated definition
create table `notification`
(
    id              int auto_increment
        primary key,
    user_id         int                      null ,
    emp_id          int                      null ,
    title           varchar(100)             not null ,
    content         varchar(255)             not null ,
    created_at      datetime default (now()) null ,
    delivery_status varchar(20)              null ,
    user_is_read    tinyint  default 0       null,
    emp_is_read     tinyint  default 0       null
);

-- auto-generated definition
create table `permission`
(
    id             int auto_increment
        primary key,
    name           varchar(20) not null ,
    permission_key varchar(50) not null ,
    created_at     datetime    not null,
    updated_at     datetime    not null
)
   ;

-- auto-generated definition
create table `region`
(
    id        int auto_increment
        primary key,
    name      varchar(10) not null,
    parent_id int         null ,
    constraint uk_region_name
        unique (name)
)
   ;

-- auto-generated definition
create table `rental_contract`
(
    id                 int auto_increment
        primary key,
    contract_no        varchar(50)    null ,
    house_id           int            null ,
    user_id            int            null,
    emp_id             int            null ,
    final_price        decimal(10, 2) null ,
    start_date         date           null ,
    end_date           date           null ,
    status             tinyint        null ,
    created_at         datetime       null ,
    is_expiry_notified tinyint        not null ,
    constraint rental_contract_pk
        unique (contract_no)
)
   ;

-- auto-generated definition
create table `role`
(
    id         int auto_increment
        primary key,
    name       varchar(20) not null ,
    role_key   varchar(50) not null ,
    created_at datetime    not null,
    updated_at datetime    not null
)
   ;

-- auto-generated definition
create table `role_permission`
(
    role_id       int not null,
    permission_id int not null,
    primary key (permission_id, role_id)
)
   ;

-- auto-generated definition
create table `sys_menu`
(
    id             int auto_increment
        primary key,
    parent_id      int          null ,
    permission_key int          not null ,
    name           varchar(20)  not null ,
    path           varchar(255) null ,
    component      varchar(255) null ,
    icon           int          null ,
    order_num      int          not null
)
  ;

-- auto-generated definition
create table `tag`
(
    id   int auto_increment
        primary key,
    name varchar(15) not null ,
    constraint uk_tag_name
        unique (name)
)
  ;

-- auto-generated definition
create table `user`
(
    id         int auto_increment
        primary key,
    phone      char(11)     not null ,
    password   varchar(100) null ,
    created_at datetime     null,
    updated_at datetime     null,
    status     int          not null ,
    constraint uk_user_phone
        unique (phone)
)
   ;

-- auto-generated definition
create table `user_detail`
(
    user_id          int        not null
        primary key,
    identity_card_id char(18)   not null,
    real_name        varchar(6) not null ,
    constraint user_detail_pk
        unique (identity_card_id)
);


-- auto-generated definition
create table `user_favorite`
(
    user_id    int      not null ,
    house_id   int      not null ,
    created_at datetime not null,
    primary key (user_id, house_id)
);


-- auto-generated definition
create table `user_role`
(
    user_id int not null,
    role_id int not null,
    primary key (user_id, role_id)
);


INSERT INTO emp (id, username, password, real_name, phone, role, created_at, updated_at) VALUES (1, 'Bai', 'abcd1234', '小白', '13926012332', '暂无', '2025-06-26 15:40:02', '2025-06-26 15:40:03');
INSERT INTO emp (id, username, password, real_name, phone, role, created_at, updated_at) VALUES (2, 'chenmin', 'abcd1234', '陈敏', '15900088702', '暂无', '2025-06-26 15:40:05', '2025-06-26 15:40:06');
INSERT INTO emp (id, username, password, real_name, phone, role, created_at, updated_at) VALUES (3, 'yangyongqiang', 'abcd1234', '杨永强', '13535875421', '暂无', '2025-06-26 15:40:07', '2025-06-26 15:40:07');
INSERT INTO emp (id, username, password, real_name, phone, role, created_at, updated_at) VALUES (22, 'bai2', '$2a$10$vCbt32lpNpi1o/O4wjgOZ.iskRGsGamrFzLmQzauFQpLobjTjhisq', '铭千店长', '15900086700', 'BranchManager', '2025-07-10 21:56:09', '2025-07-10 21:56:09');
INSERT INTO emp (id, username, password, real_name, phone, role, created_at, updated_at) VALUES (23, 'bai5', '$2a$10$4bOrFPRNBhVjoDnqCHKDIOXE4dYr9lm8wju36DF2.thOTyRIwUGAa', '铭铭组长', '15900086701', 'TeamLeader', '2025-07-13 20:17:17', '2025-07-13 20:17:17');

INSERT INTO emp_role (emp_id, role_id) VALUES (22, 3);
INSERT INTO emp_role (emp_id, role_id) VALUES (23, 2);

INSERT INTO house (id, image, title, region_id, region_name, address_detail, price_per_month, area, layout, status, created_by_emp_id, created_at, updated_at) VALUES (1, '测试图片链接1', '整租·富力天寓 1室1厅 南', 1, '天河', '天河-广氮-富力天寓', 2300.00, 46, 1, 3, 1, '2025-06-26 14:46:07', '2025-06-26 14:46:14');
INSERT INTO house (id, image, title, region_id, region_name, address_detail, price_per_month, area, layout, status, created_by_emp_id, created_at, updated_at) VALUES (2, '测试图片链接2', '合租·兰亭盛荟 3居室 北卧', 1, '天河', '天河-东圃-兰亭盛荟', 2150.00, 10, 1, 1, 1, '2025-06-26 15:01:25', '2025-06-26 15:01:27');
INSERT INTO house (id, image, title, region_id, region_name, address_detail, price_per_month, area, layout, status, created_by_emp_id, created_at, updated_at) VALUES (3, '测试图片链接3', '整租·水荫一横路 2室2厅 南', 1, '天河', '天河-水荫东-水荫一横路 ', 3500.00, 70, 2, 1, 1, '2025-06-26 15:03:16', '2025-06-26 15:03:18');
INSERT INTO house (id, image, title, region_id, region_name, address_detail, price_per_month, area, layout, status, created_by_emp_id, created_at, updated_at) VALUES (4, '
https://rent-hub.oss-cn-beijing.aliyuncs.com/listings/2025/08/27/7e218295a91b4b58816169b098eff0e4.jpg', '合租·兰亭盛荟 4居室 南卧', 1, '天河', '天河-东圃-兰亭盛荟', 2680.00, 11, 4, 1, 1, '2025-06-26 15:05:01', '2025-06-26 15:05:03');
INSERT INTO house (id, image, title, region_id, region_name, address_detail, price_per_month, area, layout, status, created_by_emp_id, created_at, updated_at) VALUES (25, null, '整租·骏景花园 2室1厅 北', 1, '天河', '天河-棠下-骏景花园 ', 3300.00, 74, 2, 1, 1, '2025-06-26 15:09:57', '2025-06-26 15:09:59');
INSERT INTO house (id, image, title, region_id, region_name, address_detail, price_per_month, area, layout, status, created_by_emp_id, created_at, updated_at) VALUES (26, null, '整租·先烈中路 3室1厅 西南', 2, '越秀', '越秀-黄花岗-先烈中路 ', 3500.00, 60, 3, 1, 2, '2025-06-26 15:11:24', '2025-06-26 15:11:25');
INSERT INTO house (id, image, title, region_id, region_name, address_detail, price_per_month, area, layout, status, created_by_emp_id, created_at, updated_at) VALUES (27, null, '合租·水荫路34号大院 4居室 北卧', 2, '越秀', '水荫路34号大院', 2000.00, 18, 4, 1, 2, '2025-06-26 15:12:38', '2025-06-26 15:12:40');
INSERT INTO house (id, image, title, region_id, region_name, address_detail, price_per_month, area, layout, status, created_by_emp_id, created_at, updated_at) VALUES (28, null, '整租·淘金路 1室0厅 南/北', 2, '越秀', '越秀-淘金-淘金路 ', 2800.00, 25, 1, 1, 2, '2025-06-26 15:14:53', '2025-06-26 15:14:54');
INSERT INTO house (id, image, title, region_id, region_name, address_detail, price_per_month, area, layout, status, created_by_emp_id, created_at, updated_at) VALUES (29, null, '合租·先烈南路青菜东街 3居室 东南卧', 2, '越秀', '越秀-建设路-先烈南路青菜东街 ', 2100.00, 11, 3, 1, 2, '2025-06-26 15:16:08', '2025-06-26 15:16:09');
INSERT INTO house (id, image, title, region_id, region_name, address_detail, price_per_month, area, layout, status, created_by_emp_id, created_at, updated_at) VALUES (30, null, '整租·海珠中路 3室1厅 南
', 2, '越秀', '越秀-五羊新城-璟泰大厦', 3900.00, 40, 3, 0, 2, '2025-06-26 15:18:53', '2025-06-26 15:18:54');
INSERT INTO house (id, image, title, region_id, region_name, address_detail, price_per_month, area, layout, status, created_by_emp_id, created_at, updated_at) VALUES (32, null, '整租·广东省输变电工程公司上村小区 2室2厅 东南', 3, '荔湾', '荔湾-南岸路-广东省输变电工程公司上村小区 ', 5000.00, 76, 2, 0, 3, '2025-06-26 15:21:15', '2025-06-26 15:21:17');
INSERT INTO house (id, image, title, region_id, region_name, address_detail, price_per_month, area, layout, status, created_by_emp_id, created_at, updated_at) VALUES (33, null, '合租·富力广场D区 5居室 西卧', 3, '荔湾', '荔湾-中山八-富力广场D区 ', 1250.00, 5, 5, 0, 3, '2025-06-26 15:22:24', '2025-06-26 15:22:25');
INSERT INTO house (id, image, title, region_id, region_name, address_detail, price_per_month, area, layout, status, created_by_emp_id, created_at, updated_at) VALUES (34, null, '合租·壹诚大厦 3居室 西卧', 3, '荔湾', '荔湾-东塱-壹诚大厦', 1300.00, 10, 3, 0, 3, '2025-06-26 15:24:02', '2025-06-26 15:24:03');
INSERT INTO house (id, image, title, region_id, region_name, address_detail, price_per_month, area, layout, status, created_by_emp_id, created_at, updated_at) VALUES (35, null, '合租·东浚荔景苑 4居室 北卧', 3, '荔湾', '荔湾-东浚荔景苑', 1630.00, 12, 4, 0, 3, '2025-06-26 15:26:46', '2025-06-26 15:26:47');
INSERT INTO house (id, image, title, region_id, region_name, address_detail, price_per_month, area, layout, status, created_by_emp_id, created_at, updated_at) VALUES (36, null, '整租·河沙花园 2室1厅 南', 3, '荔湾', '荔湾-大坦沙-河沙花园', 4000.00, 73, 2, 0, 3, '2025-06-26 15:30:02', '2025-06-26 15:30:05');
INSERT INTO house (id, image, title, region_id, region_name, address_detail, price_per_month, area, layout, status, created_by_emp_id, created_at, updated_at) VALUES (40, null, '新增房源', 1, '天河', '测试详细地址', 20000.00, 500, 3, 0, 1, '2025-07-10 18:20:10', '2025-07-10 18:20:10');
INSERT INTO house (id, image, title, region_id, region_name, address_detail, price_per_month, area, layout, status, created_by_emp_id, created_at, updated_at) VALUES (41, null, '测试新增房源', 1, '天河', '测试详细地址2', 20000.00, 2, 4, 0, 1, '2025-07-10 18:26:46', '2025-07-10 18:26:46');
INSERT INTO house (id, image, title, region_id, region_name, address_detail, price_per_month, area, layout, status, created_by_emp_id, created_at, updated_at) VALUES (42, null, '测试新增房源3', 1, '天河', '测试详细地址3', 20000.00, 2, 4, 0, 1, '2025-07-10 18:28:36', '2025-07-10 18:28:36');

INSERT INTO house_tag (house_id, tag_id) VALUES (1, 4);
INSERT INTO house_tag (house_id, tag_id) VALUES (1, 6);
INSERT INTO house_tag (house_id, tag_id) VALUES (1, 9);
INSERT INTO house_tag (house_id, tag_id) VALUES (1, 12);
INSERT INTO house_tag (house_id, tag_id) VALUES (2, 1);
INSERT INTO house_tag (house_id, tag_id) VALUES (2, 3);
INSERT INTO house_tag (house_id, tag_id) VALUES (2, 7);
INSERT INTO house_tag (house_id, tag_id) VALUES (2, 8);
INSERT INTO house_tag (house_id, tag_id) VALUES (2, 13);
INSERT INTO house_tag (house_id, tag_id) VALUES (3, 2);
INSERT INTO house_tag (house_id, tag_id) VALUES (3, 12);
INSERT INTO house_tag (house_id, tag_id) VALUES (4, 13);
INSERT INTO house_tag (house_id, tag_id) VALUES (25, 12);
INSERT INTO house_tag (house_id, tag_id) VALUES (26, 12);
INSERT INTO house_tag (house_id, tag_id) VALUES (27, 13);
INSERT INTO house_tag (house_id, tag_id) VALUES (28, 12);
INSERT INTO house_tag (house_id, tag_id) VALUES (29, 13);
INSERT INTO house_tag (house_id, tag_id) VALUES (30, 12);
INSERT INTO house_tag (house_id, tag_id) VALUES (32, 12);
INSERT INTO house_tag (house_id, tag_id) VALUES (33, 13);
INSERT INTO house_tag (house_id, tag_id) VALUES (34, 13);
INSERT INTO house_tag (house_id, tag_id) VALUES (35, 13);
INSERT INTO house_tag (house_id, tag_id) VALUES (36, 12);
INSERT INTO house_tag (house_id, tag_id) VALUES (40, 1);
INSERT INTO house_tag (house_id, tag_id) VALUES (40, 2);
INSERT INTO house_tag (house_id, tag_id) VALUES (41, 1);
INSERT INTO house_tag (house_id, tag_id) VALUES (41, 2);
INSERT INTO house_tag (house_id, tag_id) VALUES (42, 1);
INSERT INTO house_tag (house_id, tag_id) VALUES (42, 2);

INSERT INTO notification (id, user_id, emp_id, title, content, created_at, delivery_status, user_is_read, emp_is_read) VALUES (1, 9, 22, '测试单条通知title', '测试content', '2025-08-30 09:37:14', 'PENDING', 1, 1);
INSERT INTO notification (id, user_id, emp_id, title, content, created_at, delivery_status, user_is_read, emp_is_read) VALUES (2, 9, null, '您的房屋合同即将到期', '您好，您的合同将于 2025-08-29 到期，请留意。', null, null, null, null);
INSERT INTO notification (id, user_id, emp_id, title, content, created_at, delivery_status, user_is_read, emp_is_read) VALUES (3, null, 22, '客户合同即将到期提醒', '客户（用户ID: 9）的合同将于 2025-08-29 到期，请及时跟进。', null, null, null, null);
INSERT INTO notification (id, user_id, emp_id, title, content, created_at, delivery_status, user_is_read, emp_is_read) VALUES (4, 9, null, '您的房屋合同即将到期', '您好，您的合同将于 2025-08-29 到期，请留意。', '2025-08-30 04:57:04', null, 0, 0);
INSERT INTO notification (id, user_id, emp_id, title, content, created_at, delivery_status, user_is_read, emp_is_read) VALUES (5, null, 22, '客户合同即将到期提醒', '客户（用户ID: 9）的合同将于 2025-08-29 到期，请及时跟进。', '2025-08-30 04:57:04', null, 0, 0);
INSERT INTO notification (id, user_id, emp_id, title, content, created_at, delivery_status, user_is_read, emp_is_read) VALUES (6, 9, null, '您的房屋合同即将到期', '您好，您的合同将于 2025-08-29 到期，请留意。', '2025-08-30 06:46:41', null, 0, 0);
INSERT INTO notification (id, user_id, emp_id, title, content, created_at, delivery_status, user_is_read, emp_is_read) VALUES (7, null, 22, '客户合同即将到期提醒', '客户（: 柯鸣谦 ）的合同将于 2025-08-29 到期，请及时跟进。', '2025-08-30 06:46:41', null, 0, 0);
INSERT INTO notification (id, user_id, emp_id, title, content, created_at, delivery_status, user_is_read, emp_is_read) VALUES (8, 9, null, '您的房屋合同即将到期', '您好，您的合同将于 2025-08-29 到期，请留意。', '2025-08-30 06:49:08', null, 0, 0);
INSERT INTO notification (id, user_id, emp_id, title, content, created_at, delivery_status, user_is_read, emp_is_read) VALUES (9, null, 22, '客户合同即将到期提醒', '客户（ 柯鸣谦 18520080162）的合同将于 2025-08-29 到期，请及时跟进。', '2025-08-30 06:49:08', null, 0, 0);
INSERT INTO notification (id, user_id, emp_id, title, content, created_at, delivery_status, user_is_read, emp_is_read) VALUES (10, 9, null, '您的房屋合同即将到期', '您好，您的合同将于 2025-08-29 到期，请留意。', '2025-08-30 10:12:36', null, 0, 0);
INSERT INTO notification (id, user_id, emp_id, title, content, created_at, delivery_status, user_is_read, emp_is_read) VALUES (11, null, 22, '客户合同即将到期提醒', '客户（ 柯鸣谦 18520080162）的合同将于 2025-08-29 到期，请及时跟进。', '2025-08-30 10:12:36', null, 0, 0);
INSERT INTO notification (id, user_id, emp_id, title, content, created_at, delivery_status, user_is_read, emp_is_read) VALUES (12, 9, null, '您的房屋合同即将到期', '您好，您的合同将于 2025-08-29 到期，请留意。', '2025-08-30 13:01:07', null, 0, 0);
INSERT INTO notification (id, user_id, emp_id, title, content, created_at, delivery_status, user_is_read, emp_is_read) VALUES (13, null, 22, '客户合同即将到期提醒', '客户（ 柯鸣谦 18520080162）的合同将于 2025-08-29 到期，请及时跟进。', '2025-08-30 13:01:07', null, 0, 0);

INSERT INTO permission (id, name, permission_key, created_at, updated_at) VALUES (1, '创建房源', 'house:create', '2025-07-10 22:20:06', '2025-07-10 22:20:07');
INSERT INTO permission (id, name, permission_key, created_at, updated_at) VALUES (2, '查看房源', 'house:read', '2025-07-10 22:24:36', '2025-07-10 22:24:37');
INSERT INTO permission (id, name, permission_key, created_at, updated_at) VALUES (3, '修改房源', 'house:update', '2025-07-10 22:25:03', '2025-07-10 22:25:04');
INSERT INTO permission (id, name, permission_key, created_at, updated_at) VALUES (4, '删除房源', 'house:delete', '2025-07-10 22:25:28', '2025-07-10 22:25:29');
INSERT INTO permission (id, name, permission_key, created_at, updated_at) VALUES (5, '全部权限', '*', '2025-07-10 22:29:19', '2025-07-10 22:29:22');

INSERT INTO region (id, name, parent_id) VALUES (1, '天河', 1);
INSERT INTO region (id, name, parent_id) VALUES (2, '越秀', 1);
INSERT INTO region (id, name, parent_id) VALUES (3, '荔湾', 1);
INSERT INTO region (id, name, parent_id) VALUES (4, '海珠', 1);
INSERT INTO region (id, name, parent_id) VALUES (5, '番禺', 1);
INSERT INTO region (id, name, parent_id) VALUES (6, '白云', 1);
INSERT INTO region (id, name, parent_id) VALUES (7, '黄埔', 1);
INSERT INTO region (id, name, parent_id) VALUES (8, '从化', 1);
INSERT INTO region (id, name, parent_id) VALUES (9, '增城', 1);
INSERT INTO region (id, name, parent_id) VALUES (10, '花都', 1);
INSERT INTO region (id, name, parent_id) VALUES (11, '南沙', 1);

INSERT INTO rental_contract (id, contract_no, house_id, user_id, emp_id, final_price, start_date, end_date, status, created_at, is_expiry_notified) VALUES (2, 'HC-20250827-879330', 1, 9, 22, 9999.00, '2026-09-09', '2027-01-01', 2, '2025-08-27 19:57:59', 0);
INSERT INTO rental_contract (id, contract_no, house_id, user_id, emp_id, final_price, start_date, end_date, status, created_at, is_expiry_notified) VALUES (3, 'HC-10086', 1, 9, 22, 100086.00, '2025-05-29', '2025-08-29', 2, '2025-08-27 12:23:53', 1);

INSERT INTO role (id, name, role_key, created_at, updated_at) VALUES (1, '中介', 'Agent', '2025-07-10 22:15:46', '2025-07-10 22:15:47');
INSERT INTO role (id, name, role_key, created_at, updated_at) VALUES (2, '组长 ', 'TeamLeader', '2025-07-10 22:16:04', '2025-07-10 22:16:06');
INSERT INTO role (id, name, role_key, created_at, updated_at) VALUES (3, '分店经理', 'BranchManager', '2025-07-10 22:16:57', '2025-07-10 22:16:59');
INSERT INTO role (id, name, role_key, created_at, updated_at) VALUES (4, '用户', 'User', '2025-07-10 22:26:18', '2025-07-10 22:26:19');

INSERT INTO role_permission (role_id, permission_id) VALUES (1, 1);
INSERT INTO role_permission (role_id, permission_id) VALUES (2, 1);
INSERT INTO role_permission (role_id, permission_id) VALUES (1, 2);
INSERT INTO role_permission (role_id, permission_id) VALUES (2, 2);
INSERT INTO role_permission (role_id, permission_id) VALUES (2, 3);
INSERT INTO role_permission (role_id, permission_id) VALUES (3, 5);

INSERT INTO tag (id, name) VALUES (6, 'VR房源');
INSERT INTO tag (id, name) VALUES (15, '一居');
INSERT INTO tag (id, name) VALUES (17, '三居');
INSERT INTO tag (id, name) VALUES (20, '业主自荐');
INSERT INTO tag (id, name) VALUES (10, '中楼层');
INSERT INTO tag (id, name) VALUES (16, '二居');
INSERT INTO tag (id, name) VALUES (9, '低楼层');
INSERT INTO tag (id, name) VALUES (13, '合租');
INSERT INTO tag (id, name) VALUES (18, '四居+');
INSERT INTO tag (id, name) VALUES (4, '押一付一');
INSERT INTO tag (id, name) VALUES (2, '拎包入住');
INSERT INTO tag (id, name) VALUES (12, '整租');
INSERT INTO tag (id, name) VALUES (7, '无电梯');
INSERT INTO tag (id, name) VALUES (8, '有电梯');
INSERT INTO tag (id, name) VALUES (3, '精装修');
INSERT INTO tag (id, name) VALUES (19, '认证公寓');
INSERT INTO tag (id, name) VALUES (1, '近地铁');
INSERT INTO tag (id, name) VALUES (5, '随时看房');
INSERT INTO tag (id, name) VALUES (11, '高楼层');

INSERT INTO `user` (id, phone, password, created_at, updated_at, status) VALUES (1, '13926012332', 'abcd1234', '2025-06-26 15:42:36', '2025-06-26 15:42:37', 1);
INSERT INTO `user`(id, phone, password, created_at, updated_at, status) VALUES (9, '18520080162', '$2a$10$f4c8ALZKo/newMkQHurdiOCR9K.LL6hK/2JmjuldIwSChNt8VTQJS', '2025-07-05 17:11:43', '2025-07-07 22:15:32', 1);
INSERT INTO `user` (id, phone, password, created_at, updated_at, status) VALUES (10, '18520080111', '$2a$10$ASS1ZQRRgRSFX89S.YrWI.kis35lExlmcGMhzMWmcHIApXaMbdcTe', '2025-07-08 09:57:55', '2025-07-08 09:57:55', 1);
INSERT INTO `user` (id, phone, password, created_at, updated_at, status) VALUES (16, '18520080777', '$2a$10$kt9/5ZxnOzSaj7X5DBdBMOkXUk4OVbwkR9uoy3HeNLgNR8VfVSfdC', '2025-07-08 20:15:09', '2025-07-08 20:15:09', 1);

INSERT INTO user_detail (user_id, identity_card_id, real_name) VALUES (1, '532101198906010015', '陈鹏涛');
INSERT INTO user_detail (user_id, identity_card_id, real_name) VALUES (9, '440105200409161416', '柯鸣谦');
INSERT INTO user_detail (user_id, identity_card_id, real_name) VALUES (10, '440105200409161417', '陈陈');

INSERT INTO user_favorite (user_id, house_id, created_at) VALUES (1, 1, '2025-06-26 15:52:22');
INSERT INTO user_favorite (user_id, house_id, created_at) VALUES (1, 2, '2025-07-02 14:34:44');
INSERT INTO user_favorite (user_id, house_id, created_at) VALUES (1, 3, '2025-07-02 16:01:19');
INSERT INTO user_favorite (user_id, house_id, created_at) VALUES (1, 4, '2025-07-02 16:01:35');
INSERT INTO user_favorite (user_id, house_id, created_at) VALUES (1, 5, '2025-08-23 21:31:07');
INSERT INTO user_favorite (user_id, house_id, created_at) VALUES (9, 26, '2025-07-06 21:38:54');

