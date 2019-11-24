/*
Navicat MySQL Data Transfer

Source Server         : 3306
Source Server Version : 50723
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50723
File Encoding         : 65001

Date: 2019-09-26 10:26:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(30) DEFAULT NULL COMMENT '姓名',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `gender` int(1) DEFAULT NULL COMMENT '性别 1：男，0：女',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `version` int(1) DEFAULT NULL COMMENT '乐观锁版本号，更新数据前对比数据版本号，如果相同更新数据，不相同则不进行操作，每次更新成功版本号自增1',
  `deleted` int(1) DEFAULT NULL COMMENT '逻辑删除标识，1：存在；0：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'tom', '22', '0', '1234@163.com', '2010-06-03', '1', '1');
INSERT INTO `user` VALUES ('2', 'jerry', '23', '1', '1234@126.com', '1998-02-05', '1', '1');
INSERT INTO `user` VALUES ('3', 'john', '27', '0', '1234@sina.com', '1995-04-05', '1', '1');
INSERT INTO `user` VALUES ('4', 'ash', '53', '1', '1234@qq.com', '1986-01-05', '1', '1');
INSERT INTO `user` VALUES ('5', 'ben', '26', '0', '1234@qq.com', '1997-06-09', '1', '0');
INSERT INTO `user` VALUES ('6', 'fasdfd', '22', '1', '1234@qq.com', '2009-09-26', '5', '1');
