/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50729
Source Host           : 127.0.0.1:3306
Source Database       : GHOST-FRAMEWORK

Target Server Type    : MYSQL
Target Server Version : 50729
File Encoding         : 65001

Date: 2020-03-12 21:06:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `admin_id` char(36) NOT NULL COMMENT '管理员id',
  `admin_name` varchar(50) NOT NULL COMMENT '管理员名称',
  `admin_user` varchar(25) NOT NULL COMMENT '管理员账号',
  `admin_password` varchar(128) NOT NULL COMMENT '管理员密码',
  `create_time` datetime NOT NULL COMMENT '管理员创建时间',
  `status` tinyint(255) NOT NULL DEFAULT '0' COMMENT '管理员状态：0为禁用，1为启用',
  `group_id` char(36) NOT NULL COMMENT '管理组id',
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `uk` (`admin_id`),
  KEY `pk` (`admin_id`,`admin_name`,`admin_user`,`admin_password`,`create_time`,`status`,`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for group
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `group_id` char(36) NOT NULL COMMENT '管理组id',
  `create_time` datetime NOT NULL COMMENT '管理员创建时间',
  `status` tinyint(255) NOT NULL DEFAULT '0' COMMENT '管理组状态：0为禁用，1为启用',
  `group_name` varchar(50) NOT NULL COMMENT '管理组名称',
  PRIMARY KEY (`group_id`),
  UNIQUE KEY `uk` (`group_id`),
  KEY `pk` (`group_id`,`create_time`,`status`,`group_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
