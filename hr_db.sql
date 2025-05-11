CREATE DATABASE hr_db DEFAULT CHARACTER SET utf8;
USE hr_db;
/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : hr_db

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2025-05-06 10:46:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `dept_inf`
-- ----------------------------
DROP TABLE IF EXISTS `dept_inf`;
CREATE TABLE `dept_inf` (
  `dept_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `dept_name` varchar(50) NOT NULL COMMENT '部门名称，唯一,非空',
  `dept_remark` varchar(300) NOT NULL COMMENT '部门介绍',
  PRIMARY KEY (`dept_id`),
  UNIQUE KEY `dept_name` (`dept_name`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='部门信息表';

-- ----------------------------
-- Records of dept_inf
-- ----------------------------
INSERT INTO `dept_inf` VALUES ('1', '技术部', '技术部');
INSERT INTO `dept_inf` VALUES ('2', '运营部', '运营部');
INSERT INTO `dept_inf` VALUES ('3', '财务部', '财务部');
INSERT INTO `dept_inf` VALUES ('4', '总公办', '总公办');
INSERT INTO `dept_inf` VALUES ('5', '市场部', '市场部');
INSERT INTO `dept_inf` VALUES ('6', '学生部', '学生部');
INSERT INTO `dept_inf` VALUES ('7', '教学部', '教学部');
INSERT INTO `dept_inf` VALUES ('10', 'hello', 'world');
INSERT INTO `dept_inf` VALUES ('11', '钢琴社', '爱好弹吉他的同学的社团');

-- ----------------------------
-- Table structure for `document_inf`
-- ----------------------------
DROP TABLE IF EXISTS `document_inf`;
CREATE TABLE `document_inf` (
  `document_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `document_title` varchar(50) NOT NULL COMMENT '文档标题信息',
  `document_file_name` varchar(300) NOT NULL COMMENT '文档文件信息',
  `document_remark` varchar(300) DEFAULT NULL COMMENT '文档文件备注',
  `document_create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `document_user_id` int(11) DEFAULT NULL COMMENT '发布用户Id',
  PRIMARY KEY (`document_id`),
  KEY `document_user_id` (`document_user_id`),
  CONSTRAINT `document_inf_ibfk_1` FOREIGN KEY (`document_user_id`) REFERENCES `user_inf` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='文档信息表';

-- ----------------------------
-- Records of document_inf
-- ----------------------------
INSERT INTO `document_inf` VALUES ('2', '毕设材料', 'bb046e27-81d9-482f-b473-051cdbf3cbd3.txt', '数据库材料提交-孙翔宇', '2023-06-19 14:36:08', '2');
INSERT INTO `document_inf` VALUES ('3', '数据库文件', '6e2daf47-04bf-4cd5-8b64-45a1221fba8e.sql', '数据库文件', '2023-06-19 14:57:21', '2');
INSERT INTO `document_inf` VALUES ('4', 'aaa', '06055617-4374-4da6-b303-351c82324d55.txt', 'aaa', '2023-06-19 15:02:27', '2');
INSERT INTO `document_inf` VALUES ('5', '数据库作业', '173b9b8e-97ba-4e33-96eb-cbf7bbaeb27c_hanjia2023.sql', '数据库作业', '2023-06-20 08:55:59', '1');
INSERT INTO `document_inf` VALUES ('6', '大作业', '3617684f-6f82-47c8-a66d-1682caf6241a_Linux应用开发-大作业模板.doc', '大作业', '2023-06-20 09:04:35', '1');
INSERT INTO `document_inf` VALUES ('7', '凄凄切切', '3583dbca-d714-4c3a-8004-792d9c380014_hbase.txt', '阿斯蒂芬', '2023-06-26 14:58:42', '1');

-- ----------------------------
-- Table structure for `employee_inf`
-- ----------------------------
DROP TABLE IF EXISTS `employee_inf`;
CREATE TABLE `employee_inf` (
  `emp_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `emp_dept_id` int(11) DEFAULT NULL COMMENT '部门编号',
  `emp_job_id` int(11) DEFAULT NULL COMMENT '职位编号',
  `emp_name` varchar(20) NOT NULL COMMENT '姓名 ',
  `emp_card_id` char(10) NOT NULL COMMENT '身份证号',
  `emp_address` varchar(50) NOT NULL COMMENT '地址',
  `emp_post_code` char(6) DEFAULT NULL COMMENT '邮编',
  `emp_tel` varchar(16) DEFAULT NULL COMMENT '固话',
  `emp_phone` char(11) DEFAULT NULL COMMENT '手机',
  `emp_qq` varchar(16) DEFAULT NULL COMMENT 'qq号',
  `emp_email` varchar(16) DEFAULT NULL COMMENT '邮箱',
  `emp_sex` int(11) DEFAULT '1' COMMENT '性别 1-男，2-女',
  `emp_party` varchar(16) DEFAULT NULL COMMENT '政治面貌',
  `emp_birth` datetime DEFAULT NULL COMMENT '生日',
  `emp_race` varchar(16) DEFAULT '汉族' COMMENT '民族',
  `emp_edu` varchar(16) DEFAULT NULL COMMENT '学历',
  `emp_speciality` varchar(16) DEFAULT NULL COMMENT '专业',
  `emp_hobby` varchar(100) DEFAULT NULL COMMENT '爱好',
  `emp_remark` varchar(500) DEFAULT NULL COMMENT '记录',
  `emp_create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`emp_id`),
  KEY `emp_dept_id` (`emp_dept_id`),
  KEY `emp_job_id` (`emp_job_id`),
  CONSTRAINT `employee_inf_ibfk_1` FOREIGN KEY (`emp_dept_id`) REFERENCES `dept_inf` (`dept_id`),
  CONSTRAINT `employee_inf_ibfk_2` FOREIGN KEY (`emp_job_id`) REFERENCES `job_inf` (`job_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='员工信息表';

-- ----------------------------
-- Records of employee_inf
-- ----------------------------
INSERT INTO `employee_inf` VALUES ('1', '1', '8', '爱丽丝', '4328011988', '广州天河', '510000', '020-77777777', '13712345678', '36760066', '36760066@qq.com', '2', '党员', '1980-01-01 00:00:00', '满族', '本科', '美声', '唱歌', '四大天王', '2016-01-01 00:00:00');
INSERT INTO `employee_inf` VALUES ('2', '2', '1', '杰克', '22623', '江苏南京', '210000', '025-77777777', '13712345671', '36760061', '36760061@qq.com', '1', '党员', '1980-02-01 00:00:00', '汉族', '本科', null, null, '无', '2016-07-15 00:00:00');
INSERT INTO `employee_inf` VALUES ('3', '1', '2', '宝宝', '4328011983', '江苏徐州', '221000', '0516-87777777', '13712345675', '36760065', '36760065@qq.com', '1', '群众', '1980-06-01 00:00:00', '满族', '本科', '计算机', '唱歌', '麦霸', '2017-01-01 00:00:00');

-- ----------------------------
-- Table structure for `job_inf`
-- ----------------------------
DROP TABLE IF EXISTS `job_inf`;
CREATE TABLE `job_inf` (
  `job_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `job_name` varchar(50) NOT NULL COMMENT '岗位名称，唯一,非空',
  `job_remark` varchar(300) NOT NULL COMMENT '岗位介绍',
  PRIMARY KEY (`job_id`),
  UNIQUE KEY `job_name` (`job_name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='岗位信息表';

-- ----------------------------
-- Records of job_inf
-- ----------------------------
INSERT INTO `job_inf` VALUES ('1', '职员', '职员');
INSERT INTO `job_inf` VALUES ('2', 'Java开发工程师', 'Java开发工程师');
INSERT INTO `job_inf` VALUES ('3', 'Java中级开发工程师', 'Java中级开发工程师');
INSERT INTO `job_inf` VALUES ('4', 'Java高级开发工程师', 'Java高级开发工程师');
INSERT INTO `job_inf` VALUES ('5', '系统管理员', '系统管理员');
INSERT INTO `job_inf` VALUES ('6', '架构师', '架构师');
INSERT INTO `job_inf` VALUES ('7', '主管', '主管');
INSERT INTO `job_inf` VALUES ('8', '经理', '经理');
INSERT INTO `job_inf` VALUES ('9', '总经理', '总经理');

-- ----------------------------
-- Table structure for `notice_inf`
-- ----------------------------
DROP TABLE IF EXISTS `notice_inf`;
CREATE TABLE `notice_inf` (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `notice_title` varchar(50) NOT NULL COMMENT '公告标题信息',
  `notice_content` text NOT NULL COMMENT '公告内容',
  `notice_create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `notice_user_id` int(11) DEFAULT NULL COMMENT '发布用户Id',
  PRIMARY KEY (`notice_id`),
  KEY `notice_user_id` (`notice_user_id`),
  CONSTRAINT `notice_inf_ibfk_1` FOREIGN KEY (`notice_user_id`) REFERENCES `user_inf` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='公告信息表';

-- ----------------------------
-- Records of notice_inf
-- ----------------------------
INSERT INTO `notice_inf` VALUES ('1', '2023-6-19 毕业典礼', '2023-06-19 下午，6点在操场举行2020届学生毕业典礼', '2023-06-18 00:00:00', '2');
INSERT INTO `notice_inf` VALUES ('3', 'aaabbb', 'bbbdddd', '2023-06-18 00:00:00', null);

-- ----------------------------
-- Table structure for `user_inf`
-- ----------------------------
DROP TABLE IF EXISTS `user_inf`;
CREATE TABLE `user_inf` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `user_login_name` varchar(20) NOT NULL COMMENT '登录名，唯一,非空',
  `user_password` varchar(64) NOT NULL COMMENT '密码',
  `user_rand` varchar(20) DEFAULT NULL COMMENT '随机字符串，盐',
  `user_status` int(11) DEFAULT '1' COMMENT '状态 1-正常，2-禁用，0-删除',
  `user_error` int(11) DEFAULT '0' COMMENT '错误次数，5次以上禁用该账户',
  `user_create_date` date NOT NULL COMMENT '创建时间，默认当前日期',
  `user_name` varchar(20) DEFAULT NULL COMMENT '用户姓名',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_login_name` (`user_login_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='用户登录表';

-- ----------------------------
-- Records of user_inf
-- ----------------------------
INSERT INTO `user_inf` VALUES ('1', 'admin', '08FD68386957EFDC6F1D94D8F588CBE8', 'kewen', '1', '0', '2023-06-15', '超级管理员');
INSERT INTO `user_inf` VALUES ('2', 'zhangsan', '229F8838D4BD10512950C8061A7A6F95', 'pZnTiQsq', '1', '0', '2023-06-16', '张三');
INSERT INTO `user_inf` VALUES ('3', 'lisi', '497AB0FFDC19034666A4D1D29EDE8DD3', 'PwjywWgG', '1', '0', '2023-06-16', '李四');
INSERT INTO `user_inf` VALUES ('5', 'zhangsi', '544C0A49D0FDAA330319F9E0D82E79CE', 'fSnIqYwUf6', '1', '0', '2023-06-18', 'hello');
