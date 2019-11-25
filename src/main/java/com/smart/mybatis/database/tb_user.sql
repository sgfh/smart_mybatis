/*
 Navicat MySQL Data Transfer

 Source Server         : ys_net
 Source Server Version : 50724
 Source Host           : 47.97.206.112
 Source Database       : house_master

 Target Server Version : 50724
 File Encoding         : utf-8

 Date: 06/17/2019 08:29:53 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `tb_user`
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL,
  `modify_date` datetime NOT NULL,
  `is_show` tinyint(1) DEFAULT '1',
  `name` varchar(20) COLLATE utf8_bin NOT NULL,
  `mobile` varchar(11) COLLATE utf8_bin NOT NULL,
  `type` varchar(15) COLLATE utf8_bin NOT NULL,
  `avatar` varchar(150) COLLATE utf8_bin NOT NULL,
  `sex` int(1) NOT NULL,
  `student_id` varchar(20) COLLATE utf8_bin NOT NULL,
  `job_number` varchar(20) COLLATE utf8_bin NOT NULL,
  `faculty_id` bigint(5) NOT NULL,
  `major_id` bigint(5) NOT NULL,
  `campus_id` bigint(5) NOT NULL,
  `group_id` bigint(5) NOT NULL,
  `building_id` bigint(5) NOT NULL,
  `dormitory_id` bigint(5) NOT NULL,
  `grade_id` bigint(5) NOT NULL,
  `education_id` bigint(5) NOT NULL,
  `instructor_id` bigint(5) NOT NULL,
  `subject_id` bigint(20) NOT NULL,
  `reason` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `remark` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `contacts` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `termination_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `is_show` (`is_show`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

SET FOREIGN_KEY_CHECKS = 1;
