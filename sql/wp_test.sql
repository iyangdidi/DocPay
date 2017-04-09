/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50616
Source Host           : localhost:3306
Source Database       : bi

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2017-02-13 19:00:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for wp_test
-- ----------------------------
DROP TABLE IF EXISTS `wp_test`;
CREATE TABLE `wp_test` (
  `name` varchar(255) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wp_test
-- ----------------------------
INSERT INTO `wp_test` VALUES ('站牌', '1');
INSERT INTO `wp_test` VALUES ('测试', '2');
INSERT INTO `wp_test` VALUES ('无敌', '3');
