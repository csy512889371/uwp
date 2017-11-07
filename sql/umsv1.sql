/*
Navicat MySQL Data Transfer

Source Server         : 192.168.210.207
Source Server Version : 50614
Source Host           : 192.168.210.207:3306
Source Database       : umsv1

Target Server Type    : MYSQL
Target Server Version : 50614
File Encoding         : 65001

Date: 2017-11-07 11:28:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for CFG_BASE_LOG_COMPUTER_REC
-- ----------------------------
DROP TABLE IF EXISTS `CFG_BASE_LOG_COMPUTER_REC`;
CREATE TABLE `CFG_BASE_LOG_COMPUTER_REC` (
  `ID` varchar(255) NOT NULL,
  `DATA_ID` varchar(255) DEFAULT NULL,
  `IP_ADR` varchar(255) NOT NULL,
  `NEW_VAL_MAP` varchar(255) DEFAULT NULL,
  `OLD_VAL_MAP` varchar(255) DEFAULT NULL,
  `OPERATE_DATE` datetime DEFAULT NULL,
  `OPERATE_OBJ` varchar(255) DEFAULT NULL,
  `OPERATE_SET` varchar(255) DEFAULT NULL,
  `OPERATE_TYPE` int(11) DEFAULT NULL,
  `OPERATOR_ID` int(11) DEFAULT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of CFG_BASE_LOG_COMPUTER_REC
-- ----------------------------

-- ----------------------------
-- Table structure for CFG_BASE_MSG_REMIND
-- ----------------------------
DROP TABLE IF EXISTS `CFG_BASE_MSG_REMIND`;
CREATE TABLE `CFG_BASE_MSG_REMIND` (
  `MSG_REMIND_ID` varchar(255) NOT NULL,
  `CONTENTS` longtext,
  `MSG_STATUS` int(11) DEFAULT NULL,
  `MSG_TYPE` int(11) DEFAULT NULL,
  `PARAM` varchar(50) DEFAULT NULL,
  `PUBLICTIME` datetime DEFAULT NULL,
  `SKIP_URL` varchar(200) DEFAULT NULL,
  `TITLE` varchar(50) DEFAULT NULL,
  `USER_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`MSG_REMIND_ID`),
  KEY `FK_k2wqg08a78cdq81nn6pp7qfxl` (`USER_ID`),
  CONSTRAINT `FK_k2wqg08a78cdq81nn6pp7qfxl` FOREIGN KEY (`USER_ID`) REFERENCES `CFG_UMS_USER` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of CFG_BASE_MSG_REMIND
-- ----------------------------

-- ----------------------------
-- Table structure for CFG_BASE_VERSION_PUBLIC
-- ----------------------------
DROP TABLE IF EXISTS `CFG_BASE_VERSION_PUBLIC`;
CREATE TABLE `CFG_BASE_VERSION_PUBLIC` (
  `public_id` int(11) NOT NULL AUTO_INCREMENT,
  `memo` longtext,
  `pub_date` datetime DEFAULT NULL,
  `pub_desc` longtext,
  `pub_type` int(11) DEFAULT NULL,
  `SCRIPT_TYPE` int(11) DEFAULT NULL,
  `VERSION_NO` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`public_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of CFG_BASE_VERSION_PUBLIC
-- ----------------------------
INSERT INTO `CFG_BASE_VERSION_PUBLIC` VALUES ('1', '备注', '2017-11-07 11:19:14', '发布说明', '0', '0', 'v0.0.1');

-- ----------------------------
-- Table structure for CFG_BASE_VERSION_PUBLIC_DET
-- ----------------------------
DROP TABLE IF EXISTS `CFG_BASE_VERSION_PUBLIC_DET`;
CREATE TABLE `CFG_BASE_VERSION_PUBLIC_DET` (
  `version_public_det` int(11) NOT NULL AUTO_INCREMENT,
  `SEQNO` int(11) DEFAULT NULL,
  `public_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`version_public_det`),
  KEY `FK_1nn9fsin19y1l14oplge61tpj` (`SEQNO`),
  KEY `FK_qld4b2iud4f37beeblj7mf1g1` (`public_id`),
  CONSTRAINT `FK_qld4b2iud4f37beeblj7mf1g1` FOREIGN KEY (`public_id`) REFERENCES `CFG_BASE_VERSION_PUBLIC` (`public_id`),
  CONSTRAINT `FK_1nn9fsin19y1l14oplge61tpj` FOREIGN KEY (`SEQNO`) REFERENCES `CFG_UMS_SCRIPT_VERSION` (`SEQNO`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of CFG_BASE_VERSION_PUBLIC_DET
-- ----------------------------
INSERT INTO `CFG_BASE_VERSION_PUBLIC_DET` VALUES ('1', '1', '1');

-- ----------------------------
-- Table structure for CFG_TOPMENU_USER
-- ----------------------------
DROP TABLE IF EXISTS `CFG_TOPMENU_USER`;
CREATE TABLE `CFG_TOPMENU_USER` (
  `CFG_TOPMENU_USER_ID` varchar(36) NOT NULL,
  `MENU_ID` int(11) DEFAULT NULL,
  `USER_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`CFG_TOPMENU_USER_ID`),
  KEY `FK_lmchvda7clfhvvp6vuqjdrukh` (`MENU_ID`),
  KEY `FK_ovirnomfcwjwf6rydsrxey809` (`USER_ID`),
  CONSTRAINT `FK_ovirnomfcwjwf6rydsrxey809` FOREIGN KEY (`USER_ID`) REFERENCES `CFG_UMS_USER` (`ID`),
  CONSTRAINT `FK_lmchvda7clfhvvp6vuqjdrukh` FOREIGN KEY (`MENU_ID`) REFERENCES `CFG_UMS_MENU` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of CFG_TOPMENU_USER
-- ----------------------------

-- ----------------------------
-- Table structure for CFG_UMS_CODE
-- ----------------------------
DROP TABLE IF EXISTS `CFG_UMS_CODE`;
CREATE TABLE `CFG_UMS_CODE` (
  `ID` varchar(36) NOT NULL,
  `CODE` varchar(20) DEFAULT NULL COMMENT '代码',
  `REMARK` varchar(50) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CFG_UMS代码';

-- ----------------------------
-- Records of CFG_UMS_CODE
-- ----------------------------
INSERT INTO `CFG_UMS_CODE` VALUES ('1', 'standardCode', '指标配置');
INSERT INTO `CFG_UMS_CODE` VALUES ('3', 'cadreListCode', '信息录入配置集');
INSERT INTO `CFG_UMS_CODE` VALUES ('4', 'dataPrivilegeCode', '数据权限');

-- ----------------------------
-- Table structure for CFG_UMS_CODE_ATTRIBUTE
-- ----------------------------
DROP TABLE IF EXISTS `CFG_UMS_CODE_ATTRIBUTE`;
CREATE TABLE `CFG_UMS_CODE_ATTRIBUTE` (
  `ID` varchar(36) NOT NULL,
  `CODE_ID` varchar(36) DEFAULT NULL COMMENT '代码ID',
  `ATTRCODE` varchar(50) DEFAULT NULL COMMENT '属性代码',
  `ATTRNAME` varchar(50) DEFAULT NULL COMMENT '属性名称',
  `PARENTID` varchar(36) DEFAULT NULL COMMENT '父类ID',
  `STATUS` char(1) DEFAULT NULL COMMENT '状态',
  `seq` int(11) DEFAULT NULL,
  `URL` text,
  `isBatch` tinyint(4) DEFAULT NULL,
  `isBasic` tinyint(4) DEFAULT NULL,
  `recordType` varchar(255) DEFAULT NULL,
  `infoSetType` varchar(255) DEFAULT NULL,
  `iscommon` tinyint(4) DEFAULT NULL COMMENT '0::非常用信息集1:常用信息集',
  `script` text COMMENT '脚本',
  PRIMARY KEY (`ID`),
  KEY `CODE_ID` (`CODE_ID`) USING BTREE,
  CONSTRAINT `CFG_UMS_CODE_ATTRIBUTE_ibfk_1` FOREIGN KEY (`CODE_ID`) REFERENCES `CFG_UMS_CODE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of CFG_UMS_CODE_ATTRIBUTE
-- ----------------------------
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('1009', '1', 'UNIT_INDEX', '组织机构', '0', '1', '10', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('122ada8e-1c89-4ffa-94b7-85a9d0b2f034', '1', 'ZD213', '管理权限', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('1827fb17-ced5-442f-871e-031d4ca202ce', '1', 'ZD113', '国别', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('28159003-851b-4228-aa26-a6ddcb6674d0', '1', 'ZD212', '社会责任类别', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('35bb2343-d853-43ed-b436-f7aab22c17e7', '1', 'ZD218', '教育类别', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('35dc872f-1499-48de-a10a-d697d27bb01b', '1', 'ZD216', '政治面貌', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('428dd0ab-0352-4601-944f-2c60f87f6a07', '1', 'ZD005', '审核状态', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('43', '4', null, '权限设置', '0', '1', '21', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('44', '4', '/sys/role/roleDeptList.do', '数据权限', '43', '1', '22', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('45', '4', 'commonTree/getMultyMenuTree.do', '功能权限', '43', '1', '23', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('45fdf5cb-1237-4c45-a75c-7f5162389356', '1', 'ZD109', '企业类型', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('46', '4', '/sys/role/infoCadreSetTemplate.do', '信息集权限', '43', '1', '24', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('47', '4', '/sys/role/infoCadreTemplate.do', '信息项权限', '43', '1', '25', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('486117c2-0437-47de-953c-9a057aac7797', '1', 'ZD201', '是否党代表', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('51', '1', 'ZD001', '单位性质类别', '0', '1', '29', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('52', '1', 'ZD002', '单位隶属关系', '0', '1', '30', '', null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('5591a55f-1ce6-4ad9-b89f-50e9181bb414', '1', 'ZD211', '高新企业类型', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('655511e6-36e1-4957-9d6c-80e7df0f042d', '1', 'ZD221', '政策发布来源', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('6691a8a2-311f-4261-9883-3c31501af012', '1', 'ZD003', '是否', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('73fe1d3a-c616-470d-a86d-aa47e4b9e3cf', '1', 'ZD102', '所有制类型', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('7a24952d-b52f-4548-95a6-780ef2a368d9', '1', 'ZD204', '党建情况', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('7d993dd1-edb5-49c3-91f5-57f19a5f71e1', '1', 'ZD105', '区域分类', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('7eea3f7f-c637-4b40-9360-db6061df621b', '1', 'ZD209', '证书分类', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('7f6c2c03-b793-4be8-bdf3-58dc37b6f2c8', '1', 'ZD210', '证书级别', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('84651086-057a-46dc-93a9-fe0f7bcb6bc6', '1', 'ZD114', '币种', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('84eee913-31f7-4259-a538-61ac2a8d9e18', '1', 'ZD205', '企业安全风险分级', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('862d6240-7ded-4594-a5b8-cbf94e0bcf4d', '1', 'ZD103', '投资方分类', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('8f726a97-ecaa-4917-9747-0952995d7cec', '1', 'ZD206', '时间类别', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('910c1a05-085c-46e8-8a48-455b72833a9e', '1', 'ZD217', '学历', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('94379e2b-7d8d-40bc-942c-032f8d392499', '1', 'ZD101', '类型', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('a1fd2f10-f1b8-44aa-8848-0682ef164ae3', '1', 'ZD004', '性别', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('a3c40dcb-8704-4ec5-bcf3-92f707ee408a', '1', 'ZD106', '管理权限', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('a57b077b-fb4c-48f3-8c4c-f9a5b97718ea', '1', 'ZD202', '联系人类别', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('a6816a4f-2caf-4b1a-9832-0353fdfb3297', '1', 'ZD207', '季度', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('bd52dd76-7df0-4fe0-856c-8212b6c612d9', '1', 'ZD215', '特种设备类型', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('bf5df6b8-1bd4-4cdb-ba7e-e96b22b207fe', '1', 'ZD115', '管理状态', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('c7292b05-23bc-4b31-825d-a587dddb590c', '1', 'ZD111', '状态', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('d9367398-cc2e-4135-a9f1-f2c921f4e2fb', '1', 'ZD203', '关联方式', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('dfe9b34a-9cb1-44ac-8370-bc2bdff3a26d', '1', 'ZD104', '地块分类', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('e011e403-722b-404d-9ced-9dc578ec381f', '1', 'ZD214', '标准化达标等级', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('e0c0daaf-07df-47e3-965d-ebe304d68876', '1', 'ZD108', '安全监管等级分类', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('e56d413b-08a2-4940-af1b-0c170c97de5e', '1', 'ZD219', '政策类型', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('eba34a09-bef3-4363-9c3f-23aa54f57b62', '1', 'ZD112', '所属行业分类', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('ecdec191-db43-4caf-b891-46b34f349c14', '1', 'ZD208', '市区级', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('edf9afe2-2673-46be-a0a2-96844fd88eac', '1', 'ZD107', '行业分类', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('f24ae38f-3764-4fc3-89e1-580c02ecd550', '1', 'ZD220', '咨询分类', '0', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_CODE_ATTRIBUTE` VALUES ('f781bd94-b98b-4fc2-9ce7-123f9f531d16', '1', 'ZD110', '业主类型', '0', '1', '1', null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for CFG_UMS_DATADICT
-- ----------------------------
DROP TABLE IF EXISTS `CFG_UMS_DATADICT`;
CREATE TABLE `CFG_UMS_DATADICT` (
  `CODE` varchar(255) NOT NULL,
  `attribute` int(11) DEFAULT NULL,
  `CODE_ANAME` varchar(255) DEFAULT NULL,
  `CODE_ABR1` varchar(60) DEFAULT NULL,
  `CODE_ABR2` varchar(60) DEFAULT NULL,
  `CODE_ASSIST` varchar(255) DEFAULT NULL,
  `CODE_LEAF` varchar(255) DEFAULT NULL,
  `CODE_LEVEL` double DEFAULT NULL,
  `CODE_NAME` varchar(80) DEFAULT NULL,
  `CODE_SPELLING` varchar(100) DEFAULT NULL,
  `dmGrp` varchar(255) DEFAULT NULL,
  `dmLevCod` varchar(255) DEFAULT NULL,
  `ININO` int(11) DEFAULT NULL,
  `INVALID` varchar(255) DEFAULT NULL,
  `IS_COMMON` smallint(6) DEFAULT NULL,
  `IS_STAND` smallint(6) DEFAULT NULL,
  `START_DATE` varchar(255) DEFAULT NULL,
  `STOP_DATE` varchar(255) DEFAULT NULL,
  `SUP_CODE` varchar(8) DEFAULT NULL,
  `yesPrv` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of CFG_UMS_DATADICT
-- ----------------------------

-- ----------------------------
-- Table structure for CFG_UMS_EVENT
-- ----------------------------
DROP TABLE IF EXISTS `CFG_UMS_EVENT`;
CREATE TABLE `CFG_UMS_EVENT` (
  `ID` varchar(255) NOT NULL,
  `count` int(11) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `topic` varchar(255) DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of CFG_UMS_EVENT
-- ----------------------------

-- ----------------------------
-- Table structure for CFG_UMS_GROUP
-- ----------------------------
DROP TABLE IF EXISTS `CFG_UMS_GROUP`;
CREATE TABLE `CFG_UMS_GROUP` (
  `CFG_UMS_GROUP_ID` varchar(36) NOT NULL COMMENT '用户分组标识',
  `PARENT_GROUP_ID` varchar(36) DEFAULT NULL COMMENT '上级分组标识',
  `GROUP_NAME` varchar(100) DEFAULT NULL COMMENT '分组名称',
  `GROUP_DESC` varchar(200) DEFAULT NULL COMMENT '分组描述',
  `SEQNO` int(11) DEFAULT NULL COMMENT '排序号',
  PRIMARY KEY (`CFG_UMS_GROUP_ID`),
  KEY `PARENT_GROUP_ID` (`PARENT_GROUP_ID`) USING BTREE,
  CONSTRAINT `CFG_UMS_GROUP_ibfk_1` FOREIGN KEY (`PARENT_GROUP_ID`) REFERENCES `CFG_UMS_GROUP` (`CFG_UMS_GROUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户分组表';

-- ----------------------------
-- Records of CFG_UMS_GROUP
-- ----------------------------
INSERT INTO `CFG_UMS_GROUP` VALUES ('402880f6599023dc01599685976e2034', null, '分组一', null, '1');
INSERT INTO `CFG_UMS_GROUP` VALUES ('402880f6599023dc015996863d222036', null, '分组三', null, '3');
INSERT INTO `CFG_UMS_GROUP` VALUES ('402880f6599023dc015996872f012037', null, '分组二', null, '2');
INSERT INTO `CFG_UMS_GROUP` VALUES ('402880f65a8de334015a8e21ed210005', null, '测试', null, '4');

-- ----------------------------
-- Table structure for CFG_UMS_LOG
-- ----------------------------
DROP TABLE IF EXISTS `CFG_UMS_LOG`;
CREATE TABLE `CFG_UMS_LOG` (
  `ID` varchar(255) NOT NULL,
  `CONTENTS` varchar(255) DEFAULT NULL,
  `DATA_ID` varchar(255) DEFAULT NULL,
  `IP_ADR` varchar(255) NOT NULL,
  `NEW_VALS` varchar(255) DEFAULT NULL,
  `OLD_VALS` varchar(255) DEFAULT NULL,
  `OPERATE_DATE` datetime DEFAULT NULL,
  `OPERATE_OBJ` varchar(255) DEFAULT NULL,
  `OPERATE_SET` varchar(255) DEFAULT NULL,
  `OPERATE_TYPE` int(11) DEFAULT NULL,
  `operator` varchar(255) DEFAULT NULL,
  `OPERATOR_ID` int(11) DEFAULT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of CFG_UMS_LOG
-- ----------------------------

-- ----------------------------
-- Table structure for CFG_UMS_MENU
-- ----------------------------
DROP TABLE IF EXISTS `CFG_UMS_MENU`;
CREATE TABLE `CFG_UMS_MENU` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `TITLE` varchar(30) DEFAULT NULL COMMENT '菜单标题',
  `MENUTYPE` smallint(6) DEFAULT NULL COMMENT '菜单类型',
  `URL` varchar(100) DEFAULT NULL COMMENT 'URL',
  `PARENTID` int(11) DEFAULT NULL COMMENT '父菜单ID',
  `CODE` varchar(100) DEFAULT NULL COMMENT '菜单编码',
  `STATE` tinyint(4) DEFAULT NULL COMMENT '状态',
  `SORT` smallint(6) DEFAULT NULL COMMENT '排序',
  `REMARK` text COMMENT '备注',
  `BIG_ICON` varchar(20) DEFAULT NULL COMMENT '大图标',
  `SMALL_ICON` varchar(20) DEFAULT NULL COMMENT '小图标',
  `TYPE` varchar(11) DEFAULT NULL COMMENT '类型',
  PRIMARY KEY (`ID`),
  KEY `PARENTID` (`PARENTID`) USING BTREE,
  CONSTRAINT `CFG_UMS_MENU_ibfk_1` FOREIGN KEY (`PARENTID`) REFERENCES `CFG_UMS_MENU` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=24229 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of CFG_UMS_MENU
-- ----------------------------
INSERT INTO `CFG_UMS_MENU` VALUES ('1006', '后台管理系统', '1', null, null, '14', '1', '5', null, 'ico2_03_03.png', 'ico2_m_03_03.png', '1');
INSERT INTO `CFG_UMS_MENU` VALUES ('1007', '用户管理', '3', 'sys/user/searchUser.do', '1011', '140101', '1', '1', '用户管理', 'ico2_01_01.png', 'ico2_m_01_01.png', '1');
INSERT INTO `CFG_UMS_MENU` VALUES ('1008', '角色管理', '3', 'sys/role/searchRole.do', '1011', '140102', '1', '2', null, 'ico2_01_02.png', 'ico2_m_01_02.png', '1');
INSERT INTO `CFG_UMS_MENU` VALUES ('1009', '菜单管理', '3', 'sys/menu/searchMenu.do', '1011', '140103', '1', '3', null, 'ico2_01_03.png', 'ico2_m_01_03.png', '1');
INSERT INTO `CFG_UMS_MENU` VALUES ('1011', '权限配置', '2', null, '1006', '1401', '1', '1', null, 'nav_ico41.png', 'nav_ico41.png', '1');
INSERT INTO `CFG_UMS_MENU` VALUES ('2026', '参数管理', '2', null, '1006', '1402', '1', '2', null, 'nav_ico43.png', 'nav_ico43.png', '1');
INSERT INTO `CFG_UMS_MENU` VALUES ('2027', '日志管理', '3', 'sys/log/logList.do', '2026', '140201', '1', '1', '阿萨德', 'ico2_03_01.png', 'ico2_m_03_01.png', '1');
INSERT INTO `CFG_UMS_MENU` VALUES ('3034', '管理工具', '2', null, '1006', '1403', '1', '3', null, 'nav_ico44.png', 'nav_ico44.png', '1');
INSERT INTO `CFG_UMS_MENU` VALUES ('3035', '标准代码缓存', '3', 'sys/datadictcache/codeTableInfoList.do', '3034', '140301', '1', '1', null, 'ico2_04_01.png', 'ico2_m_04_01.png', '1');
INSERT INTO `CFG_UMS_MENU` VALUES ('4034', '系统缓存', '3', 'sys/syscache/clear.do', '3034', '140302', '1', '2', null, 'ico2_04_02.png', 'ico2_m_04_02.png', '1');
INSERT INTO `CFG_UMS_MENU` VALUES ('4035', '系统下拉参数管理', '3', 'sys/coder/coderList.do', '2026', '140202', '1', '2', null, 'ico2_03_02.png', 'ico2_m_03_02.png', '1');
INSERT INTO `CFG_UMS_MENU` VALUES ('5035', '系统参数管理', '3', 'sys/setting/manage.do', '3034', '140203', '1', '3', null, 'ico2_04_03.png', 'ico2_m_04_03.png', '1');
INSERT INTO `CFG_UMS_MENU` VALUES ('5036', '字段与指标关系', '3', 'sys/fieldinfo/fieldInfoList.do', '2026', '140204', '1', '4', null, 'ico2_03_03.png', 'ico2_m_03_03.png', '1');
INSERT INTO `CFG_UMS_MENU` VALUES ('20139', '系统构建', '2', null, '1006', '2', '1', '1', null, 'nav_ico42.png', 'nav_ico42.png', '1');
INSERT INTO `CFG_UMS_MENU` VALUES ('20140', '消息管理', '3', 'sys/msgRemind/loadMsgRemindIndex.do', '20139', 'msgRemindIndex', '1', '7', null, 'ico2_02_01.png', 'ico2_m_02_01.png', '1');
INSERT INTO `CFG_UMS_MENU` VALUES ('24174', '版本控制', '3', 'sys/version/versionIndex.do', '3034', '12', '1', '4', null, 'ico2_04_04.png', 'ico2_m_04_04.png', '1');
INSERT INTO `CFG_UMS_MENU` VALUES ('24192', '分组管理', '3', 'sys/user/searchGroupIndex.do', '1011', 'userGroup', '1', '4', null, 'ico_02_01.png', 'ico_m_02_01.png', '1');
INSERT INTO `CFG_UMS_MENU` VALUES ('24200', 'excel导出模板配置', '3', 'report/engine/reportTemplateIndex.do', '20139', 'reportTemplateIndex', '1', '6', null, 'ico_05_02.png', 'ico_m_05_02.png', '1');
INSERT INTO `CFG_UMS_MENU` VALUES ('24209', '指标代码维护', '3', 'sys/code/codeIndex.do', '20139', 'codeIndexManage', '1', '4', null, 'ico_04_01.png', 'ico_m_04_01.png', '1');
INSERT INTO `CFG_UMS_MENU` VALUES ('24210', '信息集信息项维护', '3', 'sys/structure/etpInfoBankStruct.do', '20139', 'etpInfoBankStructManage', '1', '2', null, 'ico_04_02.png', 'ico_m_04_02.png', '1');
INSERT INTO `CFG_UMS_MENU` VALUES ('24211', '信息集信息管理', '3', 'sys/infoSetManage/getInfoSetMangePage.do', '20139', 'infoSetMange', '1', '1', null, 'ico_04_04.png', 'ico_m_04_04.png', '1');
INSERT INTO `CFG_UMS_MENU` VALUES ('24225', '系统公告', '2', 'admin/sysAnnounce/announceInfo.do', '1006', '2', '1', '2', null, 'nav_ico75.png', 'nav_ico75.png', '0');

-- ----------------------------
-- Table structure for CFG_UMS_MENU_OPER
-- ----------------------------
DROP TABLE IF EXISTS `CFG_UMS_MENU_OPER`;
CREATE TABLE `CFG_UMS_MENU_OPER` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `MENUID` int(11) DEFAULT NULL COMMENT '菜单ID',
  `OPERNAME` varchar(20) DEFAULT NULL COMMENT '操作名称',
  `PERMISSION` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8480 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of CFG_UMS_MENU_OPER
-- ----------------------------
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2067', '2027', '删除', 'log:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2068', '2027', '查询', 'log:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2072', '4035', '新增常量字段', 'coder:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2129', '2022', '查询', 'enterpriseView:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2132', '2022', '表单输出', 'enterpriseView:formOut');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2143', '7061', '查询', 'analysisIndex:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2149', '15091', '新增单位', 'unit:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2150', '15091', '新增虚单位', 'unit:vulAdd');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2151', '15091', '编辑', 'unit:edit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2152', '15091', '删除', 'unit:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2153', '15091', '递归删除', 'unit:recDelete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2158', '18094', '保存排序', 'unitSort:sortSave');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2159', '6049', '上移', 'fixSearch:up');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2160', '6049', '下移', 'fixSearch:down');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2161', '6049', '删除', 'fixSearch:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2162', '6049', '全删', 'fixSearch:deleteAll');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2163', '6049', '执行查询', 'fixSearch:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2164', '6049', '存为模板', 'fixSearch:saveToTemp');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2165', '6049', '模板保存', 'fixSearch:saveTemp');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2166', '6049', '人员统计分析', 'fixSearch:countAnalysis');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2167', '6049', '表单输出', 'fixSearch:formOut');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2168', '6052', '人员统计分析', 'conSer:countAnalysis');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2169', '6052', '表单输出', 'conSer:formOut');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2170', '6053', '保存模板', 'combSer:saveTemp');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2171', '6053', '表单输出', 'combSer:formOut');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2196', '1007', '新增用户', 'user:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2197', '1007', '编辑', 'user:edit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2198', '1007', '删除', 'user:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2199', '1007', '配置角色', 'user:setRole');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2200', '1007', '重置密码', 'user:reset');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2201', '1007', '查询', 'user:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2202', '1008', '新增角色', 'role:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2203', '1008', '编辑', 'role:edit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2204', '1008', '删除', 'role:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2205', '1008', '单位机构授权', 'role:unitGrant');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2206', '1008', '分配用户', 'role:grantToUser');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2207', '1008', '权限设置', 'role:setGrant');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2208', '1008', '查询', 'role:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2209', '1009', '新增菜单', 'menu:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2210', '1009', '编辑', 'menu:edit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2211', '1009', '操作权限', 'menu:setGrant');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2212', '1009', '删除', 'menu:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2213', '1009', '增加下级', 'menu:addSup');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2215', '1009', '查询', 'menu:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2216', '4035', '修改常量字段', 'coder:edit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2217', '4035', '删除常量字段', 'coder:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2218', '4035', '查询', 'coder:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2219', '5036', '新增对应关系', 'field:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2220', '5036', '修改对应关系', 'field:edit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2221', '5036', '删除对应关系', 'field:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2222', '5036', '查询', 'field:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2223', '3035', '更新缓存', 'codeCache:update');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2224', '3035', '全部更新', 'codeCache:updateAll');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2225', '4034', '清除缓存', 'sysCache:clear');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2226', '5035', '编辑系统设置', 'sysParCache:edit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2227', '6056', '保存排序', 'dept:save');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2228', '6056', '新增', 'dept:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2229', '6056', '编辑', 'dept:edit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2230', '6056', '删除', 'dept:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2231', '6057', '保存排序', 'deptType:save');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2232', '6057', '新增', 'deptType:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2233', '6057', '编辑', 'deptType:edit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2234', '6057', '删除', 'deptType:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2235', '6058', '保存排序', 'unitGroup:save');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2236', '6058', '新增', 'unitGroup:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2237', '6058', '编辑', 'unitGroup:edit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2238', '6058', '删除', 'unitGroup:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2315', '2027', '查看详细', 'log:detail');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2319', '6053', '执行查询', 'combSer:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2320', '6053', '删除', 'combSer:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2321', '6049', '删除模板', 'fixSearch:deleteTemplate');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2322', '6049', '执行查询（模板页面）', 'fixSearch:searchByTemplate');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2324', '6053', '人员统计分析', 'combSer:countAnalysis');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('2325', '1007', '所属部门', 'user:setBranch');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('5326', '6053', '保存结果', 'combSer:saveResult');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('5327', '6053', '二次查询', 'combSer:twiceSearch');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('5328', '6053', '编辑模版', 'combSer:editTemp');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('5332', '20140', '顶部未读消息提醒', 'message:topTip');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('5333', '4034', '更新综合数据', 'sysCache:updateIntegrate');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('5334', '6049', '编辑模板', 'fixSearch:editTemp');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('7339', '2022', '照片上传', 'enterpriseView:imageUpload');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8341', '1008', '操作人员权限', 'role:cadreOptGrant');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8343', '20137', '查询', 'adjustPlan:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8344', '20137', '新增', 'adjustPlan:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8345', '20137', '编辑', 'adjustPlan:edit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8346', '20137', '删除', 'adjustPlan:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8347', '20137', '下载', 'adjustPlan:down');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8348', '20137', '状态设置', 'adjustPlan:setSta');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8349', '20138', '查询', 'candMan:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8350', '20138', '新增', 'candMan:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8351', '20138', '编辑', 'candMan:edit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8352', '20138', '删除', 'candMan:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8353', '20138', '人员排序', 'candMan:sort');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8354', '20138', '提交干监室', 'candMan:subDry');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8355', '20138', '档案审核', 'candMan:arcChk');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8356', '21142', '查询', 'assessPlan:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8357', '21142', '新增', 'assessPlan:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8358', '21142', '编辑', 'assessPlan:edit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8359', '21142', '删除', 'assessPlan:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8360', '21142', '下载方案', 'assessPlan:down');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8361', '21143', '查询', 'assessTeam:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8362', '21143', '新增', 'assessTeam:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8363', '21143', '编辑', 'assessTeam:edit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8364', '21143', '删除', 'assessTeam:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8365', '20142', '查询', 'recomCand:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8366', '20142', '导入', 'recomCand:imp');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8367', '20142', '新增', 'recomCand:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8368', '20142', '编辑', 'recomCand:edit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8369', '20142', '删除', 'recomCand:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8370', '20142', '录入推荐结果', 'recomCand:inpRes');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8371', '20142', '导入推荐结果', 'recomCand:impRes');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8372', '21145', '查询', 'assessObj:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8373', '21145', '导入', 'assessObj:imp');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8374', '21145', '新增', 'assessObj:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8375', '21145', '编辑', 'assessObj:edit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8376', '21145', '删除', 'assessObj:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8377', '21145', '人员排序', 'assessObj:sort');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8378', '21146', '查询', 'assessCond:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8379', '21146', '成考察预告文本', 'assessCond:assPre');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8380', '21146', '录入考察结果', 'assessCond:inpRes');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8381', '21146', '录入上级分管意见', 'assessCond:inpAsk');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8382', '21146', '档案审核', 'assessCond:arcChk');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8383', '21146', '提交干监室', 'assessCond:subDry');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8384', '21149', '查询', 'groupRos1:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8385', '21149', '导入', 'groupRos1:imp');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8386', '21149', '新增', 'groupRos1:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8387', '21149', '删除', 'groupRos1:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8388', '21149', '拟任免职务', 'groupRos1:disCad');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8389', '21149', '任免表补录', 'groupRos1:cadMake');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8390', '21149', '提交一处', 'groupRos1:cadSub');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8391', '21149', '排序', 'groupRos1:sort');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8392', '21150', '查询', 'groupStu1:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8393', '21150', '编辑报件内容', 'groupStu1:editCon');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8394', '21150', '五人小组报件', 'groupStu1:report');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8395', '21150', '任免表输出', 'groupStu1:out');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8396', '21151', '查询', 'groupRos2:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8397', '21151', '创建批次', 'groupRos2:addBat');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8398', '21151', '编辑批次', 'groupRos2:editBat');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8399', '21151', '新增', 'groupRos2:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8400', '21151', '删除', 'groupRos2:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8401', '21151', '拟任免职务', 'groupRos2:disCad');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8402', '21151', '任免表补录', 'groupRos2:cadMake');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8403', '21151', '待提请名单', 'groupRos2:repCad');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8404', '21151', '按单位合并', 'groupRos2:merge');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8405', '21151', '排序', 'groupRos2:sort');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8406', '21152', '查询', 'groupStu2:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8407', '21152', '编辑报件内容', 'groupStu2:editCon');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8408', '21152', '五人小组报件', 'groupStu2:report1');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8409', '21152', '常委会报件', 'groupStu2:report2');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8410', '21152', '任免表输出', 'groupStu2:out');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8411', '21153', '查询', 'disResult:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8412', '21153', '五人小组结果', 'disResult:result1');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8413', '21153', '常委会结果', 'disResult:result2');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8414', '21153', '全委会结果', 'disResult:result3');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8415', '21153', '征求意见结果', 'disResult:result4');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8416', '23172', '查询', 'dutyRos:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8417', '23172', '导入', 'dutyRos:imp');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8418', '23172', '删除', 'dutyRos:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8419', '23172', '任职', 'dutyRos:offer');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8420', '23172', '介绍信', 'dutyRos:intro');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8421', '21155', '查询', 'pubPlan:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8422', '21155', '公示分组', 'pubPlan:group');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8423', '21155', '编辑公示时间', 'pubPlan:edit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8424', '21155', '公示方案', 'pubPlan:plan');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8425', '21155', '任前公示谈话名单', 'pubPlan:cadList');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8426', '21156', '查询', 'pubNotice:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8427', '21157', '查询', 'pubResult:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8428', '21157', '公示结果', 'pubResult:result');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8429', '21158', '查询', 'disSet:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8430', '21158', '编辑', 'disSet:edit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8431', '21158', '合并发文', 'disSet:merge');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8432', '21158', '取消合并', 'disSet:unMerge');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8459', '24203', '新增', 'pubGroup:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8460', '24203', '编辑', 'pubGroup:edit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8461', '24203', '删除', 'pubGroup:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8462', '24203', '排序', 'pubGroup:sort');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8463', '2022', '修改地理位置', 'enterpriseView:locationEdit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8465', '2022', '删除', 'enterpriseView:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8466', '24209', '编辑指标', 'codeUpt:edit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8467', '24209', '新增指标', 'codeUpt:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8468', '24209', '查询', 'codeUpt:search');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8469', '24209', '新增指标项', 'codeUpt:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8470', '24209', '新增下级指标项', 'codeUpt:addSup');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8471', '24209', '编辑指标项', 'codeUpt:editSup');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8472', '24209', '设置常用指标项', 'codeUpt:comSup');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8473', '24209', '删除指标项', 'codeUpt:delete');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8474', '24210', '新增属性', 'codesUpt:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8475', '24210', '保存', 'codesUpt:save');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8476', '24222', '保存排序', 'dept:save');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8477', '24222', '新增', 'dept:add');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8478', '24222', '编辑', 'dept:edit');
INSERT INTO `CFG_UMS_MENU_OPER` VALUES ('8479', '24222', '删除', 'dept:delete');

-- ----------------------------
-- Table structure for CFG_UMS_PLUGIN_CONFIG
-- ----------------------------
DROP TABLE IF EXISTS `CFG_UMS_PLUGIN_CONFIG`;
CREATE TABLE `CFG_UMS_PLUGIN_CONFIG` (
  `ID` varchar(255) NOT NULL,
  `seq` int(11) DEFAULT NULL,
  `isEnabled` varchar(2) NOT NULL,
  `pluginId` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of CFG_UMS_PLUGIN_CONFIG
-- ----------------------------
INSERT INTO `CFG_UMS_PLUGIN_CONFIG` VALUES ('1', '1', '1', 'filePlugin');

-- ----------------------------
-- Table structure for CFG_UMS_PLUGIN_CONFIG_ATTRIBUTE
-- ----------------------------
DROP TABLE IF EXISTS `CFG_UMS_PLUGIN_CONFIG_ATTRIBUTE`;
CREATE TABLE `CFG_UMS_PLUGIN_CONFIG_ATTRIBUTE` (
  `PluginConfig_ID` varchar(255) NOT NULL,
  `attributes` varchar(255) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `CfgUmsPluginConfig_ID` varchar(255) NOT NULL,
  PRIMARY KEY (`PluginConfig_ID`,`name`),
  KEY `CfgUmsPluginConfig_ID` (`CfgUmsPluginConfig_ID`) USING BTREE,
  CONSTRAINT `CFG_UMS_PLUGIN_CONFIG_ATTRIBUTE_ibfk_1` FOREIGN KEY (`CfgUmsPluginConfig_ID`) REFERENCES `CFG_UMS_PLUGIN_CONFIG` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of CFG_UMS_PLUGIN_CONFIG_ATTRIBUTE
-- ----------------------------

-- ----------------------------
-- Table structure for CFG_UMS_ROLE
-- ----------------------------
DROP TABLE IF EXISTS `CFG_UMS_ROLE`;
CREATE TABLE `CFG_UMS_ROLE` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `ROLENAME` varchar(60) DEFAULT NULL COMMENT '角色名',
  `STATE` tinyint(4) DEFAULT NULL COMMENT '状态',
  `SORT` smallint(6) DEFAULT NULL COMMENT '排序',
  `REMARK` text COMMENT '备注',
  `CODE` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7048 DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of CFG_UMS_ROLE
-- ----------------------------
INSERT INTO `CFG_UMS_ROLE` VALUES ('1', '管理员', '1', '1', '系统管理员，拥有最高权限', null);
INSERT INTO `CFG_UMS_ROLE` VALUES ('7047', '测试', '1', '2', '测试', null);

-- ----------------------------
-- Table structure for CFG_UMS_ROLE_MENU_OPER
-- ----------------------------
DROP TABLE IF EXISTS `CFG_UMS_ROLE_MENU_OPER`;
CREATE TABLE `CFG_UMS_ROLE_MENU_OPER` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `MENUID` int(11) DEFAULT NULL COMMENT '菜单ID',
  `ROLE_ID` int(11) DEFAULT NULL,
  `OPERATION_IDS` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `ROLE_ID` (`ROLE_ID`) USING BTREE,
  CONSTRAINT `CFG_UMS_ROLE_MENU_OPER_ibfk_1` FOREIGN KEY (`ROLE_ID`) REFERENCES `CFG_UMS_ROLE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=32057 DEFAULT CHARSET=utf8 COMMENT='角色菜单表';

-- ----------------------------
-- Records of CFG_UMS_ROLE_MENU_OPER
-- ----------------------------
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('7260', '1006', '1', null);
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('7261', '1011', '1', null);
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('7262', '1007', '1', '2200,2201,2325,2196,2197,2198,2199,');
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('7263', '1008', '1', '2202,2203,2204,2206,2207,2208,8341,');
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('7264', '1009', '1', '2211,2210,2209,2215,2213,2212,');
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('7265', '2026', '1', null);
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('7268', '5035', '1', '2226,');
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('7269', '5036', '1', '2219,2222,2221,2220,');
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('7270', '3034', '1', null);
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('7271', '3035', '1', '2223,2224,');
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('7272', '4034', '1', '5333,2225,');
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('8358', '2027', '1', '2067,2068,2315,');
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('8359', '4035', '1', '2218,2217,2216,2072,');
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('27674', '20139', '1', '');
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('27675', '20140', '1', '5332,');
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('29730', '24174', '1', '');
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('29747', '24192', '1', '');
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('31051', '24200', '1', '');
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('32004', '24209', '1', '8466,8467,8468,8469,8470,8471,8472,8473,');
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('32005', '24210', '1', '8474,8475,');
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('32006', '24211', '1', '');
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('32053', '3034', '7047', '');
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('32054', '1006', '7047', '');
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('32055', '24174', '7047', '');
INSERT INTO `CFG_UMS_ROLE_MENU_OPER` VALUES ('32056', '5035', '7047', '');

-- ----------------------------
-- Table structure for CFG_UMS_ROLE_UNIT_OPER
-- ----------------------------
DROP TABLE IF EXISTS `CFG_UMS_ROLE_UNIT_OPER`;
CREATE TABLE `CFG_UMS_ROLE_UNIT_OPER` (
  `ID` int(11) NOT NULL COMMENT 'ID',
  `ROLE_ID` int(11) DEFAULT NULL COMMENT '角色ID',
  `UNIT_ID` varchar(36) DEFAULT NULL COMMENT '单位代码',
  `PER_LV` char(1) DEFAULT NULL COMMENT '权限等级0:无权限 1：只读 2：可写',
  PRIMARY KEY (`ID`),
  KEY `UNIT_ID` (`UNIT_ID`) USING BTREE,
  KEY `ROLE_ID` (`ROLE_ID`) USING BTREE,
  CONSTRAINT `CFG_UMS_ROLE_UNIT_OPER_ibfk_1` FOREIGN KEY (`ROLE_ID`) REFERENCES `CFG_UMS_ROLE` (`ID`),
  CONSTRAINT `CFG_UMS_ROLE_UNIT_OPER_ibfk_2` FOREIGN KEY (`UNIT_ID`) REFERENCES `UNIT_INFO` (`B00`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of CFG_UMS_ROLE_UNIT_OPER
-- ----------------------------

-- ----------------------------
-- Table structure for CFG_UMS_ROLE_USER
-- ----------------------------
DROP TABLE IF EXISTS `CFG_UMS_ROLE_USER`;
CREATE TABLE `CFG_UMS_ROLE_USER` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `ROLEID` int(11) DEFAULT NULL COMMENT '角色ID',
  `USERID` int(11) DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='角色用户表';

-- ----------------------------
-- Records of CFG_UMS_ROLE_USER
-- ----------------------------
INSERT INTO `CFG_UMS_ROLE_USER` VALUES ('6', '704', '8031');
INSERT INTO `CFG_UMS_ROLE_USER` VALUES ('16', '1', '8031');
INSERT INTO `CFG_UMS_ROLE_USER` VALUES ('18', '1', '1');
INSERT INTO `CFG_UMS_ROLE_USER` VALUES ('19', '7047', '8035');

-- ----------------------------
-- Table structure for CFG_UMS_SCRIPT_VERSION
-- ----------------------------
DROP TABLE IF EXISTS `CFG_UMS_SCRIPT_VERSION`;
CREATE TABLE `CFG_UMS_SCRIPT_VERSION` (
  `SEQNO` int(11) NOT NULL AUTO_INCREMENT COMMENT '序列号',
  `CHN_SCRIPT` text COMMENT '变更脚本',
  `CHN_DESC` varchar(200) DEFAULT NULL COMMENT '变更说明',
  `CHN_TIME` datetime DEFAULT NULL COMMENT '变更时间',
  `CHN_TYPE` tinyint(4) DEFAULT NULL COMMENT '变更类型0：结构 1：数据',
  `SVN_VERNO` varchar(50) DEFAULT NULL COMMENT 'SVN版本号',
  `DEVDB_UP` tinyint(4) DEFAULT NULL COMMENT '开发库库变更状态0:未更新1：已更新',
  `DEMODB_UP` tinyint(4) DEFAULT NULL COMMENT '183库变更状态0:未更新1：已更新',
  `FZDB_UP` tinyint(4) DEFAULT NULL COMMENT '市组库变更状态0:未更新1：已更新',
  `FJDB_UP` tinyint(4) DEFAULT NULL COMMENT '省组库变更状态0:未更新1：已更新',
  `TESTDB_UP` tinyint(4) DEFAULT NULL COMMENT '测试库变更状态0:未更新1：已更新',
  `PDM_UP` tinyint(4) DEFAULT NULL COMMENT 'PDM结构维护0：未维护 1：已维护',
  `CHN_SCRIPT_ACCESS` varchar(255) DEFAULT NULL,
  `dep_version` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`SEQNO`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='脚本版本控制';

-- ----------------------------
-- Records of CFG_UMS_SCRIPT_VERSION
-- ----------------------------
INSERT INTO `CFG_UMS_SCRIPT_VERSION` VALUES ('1', 'select * from SHJT_ENT_INF;', '测试', '2017-11-02 00:00:00', '0', null, null, null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_SCRIPT_VERSION` VALUES ('2', 'select * from cfg_ums_code;', 'test', '2017-11-03 00:00:00', '0', null, null, null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_SCRIPT_VERSION` VALUES ('3', 'select * from cfg_ums_code;', 'test', '2017-11-03 00:00:00', '1', null, null, null, null, null, null, null, null, null);
INSERT INTO `CFG_UMS_SCRIPT_VERSION` VALUES ('4', 'update SHJT_SEARCH_TRAN set value_type=\'7\' where ID=\'11003\' or ID=\'11401\';', '修改自定义查询字段类型', '2017-11-06 00:00:00', '1', null, null, null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for CFG_UMS_USER
-- ----------------------------
DROP TABLE IF EXISTS `CFG_UMS_USER`;
CREATE TABLE `CFG_UMS_USER` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `USERNAME` varchar(50) DEFAULT NULL COMMENT '用户名',
  `PASSWORD` varchar(60) DEFAULT NULL COMMENT '密码',
  `STATE` tinyint(4) DEFAULT NULL COMMENT '状态',
  `SORT` smallint(6) DEFAULT NULL COMMENT '排序',
  `REMARK` text COMMENT '备注',
  `CFG_UMS_GROUP_ID` varchar(36) DEFAULT NULL COMMENT '用户分组标识',
  PRIMARY KEY (`ID`),
  KEY `CFG_UMS_GROUP_ID` (`CFG_UMS_GROUP_ID`) USING BTREE,
  KEY `idx_CFG_UMS_USER_USERNAME_PASSWORD` (`USERNAME`,`PASSWORD`) USING BTREE,
  CONSTRAINT `CFG_UMS_USER_ibfk_1` FOREIGN KEY (`CFG_UMS_GROUP_ID`) REFERENCES `CFG_UMS_GROUP` (`CFG_UMS_GROUP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8036 DEFAULT CHARSET=utf8 COMMENT='用户信息表';

-- ----------------------------
-- Records of CFG_UMS_USER
-- ----------------------------
INSERT INTO `CFG_UMS_USER` VALUES ('1', 'admin', '25d55ad283aa400af464c76d713c07ad', '1', '1', '系统管理员', '402880f6599023dc01599685976e2034');
INSERT INTO `CFG_UMS_USER` VALUES ('8035', 'test', '81dc9bdb52d04dc20036dbd8313ed055', '1', '1', null, '402880f65a8de334015a8e21ed210005');

-- ----------------------------
-- Table structure for CMIS_DEPARTMENT
-- ----------------------------
DROP TABLE IF EXISTS `CMIS_DEPARTMENT`;
CREATE TABLE `CMIS_DEPARTMENT` (
  `CODE` varchar(36) NOT NULL,
  `CODE_ANAME` varchar(50) DEFAULT NULL,
  `CODE_ABR1` varchar(50) DEFAULT NULL,
  `CODE_ABR2` varchar(50) DEFAULT NULL,
  `CODE_SPELLING` varchar(100) DEFAULT NULL,
  `CODE_NAME` varchar(120) DEFAULT NULL,
  `DESCRIPTION` varchar(200) DEFAULT NULL,
  `ININO` int(11) DEFAULT NULL,
  PRIMARY KEY (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of CMIS_DEPARTMENT
-- ----------------------------

-- ----------------------------
-- Table structure for REPORT_TEMPLATE
-- ----------------------------
DROP TABLE IF EXISTS `REPORT_TEMPLATE`;
CREATE TABLE `REPORT_TEMPLATE` (
  `REPORT_TEMPLATE_ID` varchar(36) NOT NULL,
  `FILEPATH` varchar(255) DEFAULT NULL,
  `ININO` int(11) DEFAULT NULL,
  `ISVALID` bit(1) DEFAULT NULL,
  `RULES` varchar(4000) DEFAULT NULL,
  `TEMPLATENAME` varchar(255) DEFAULT NULL,
  `TEMPLATETYPE` int(11) DEFAULT NULL,
  PRIMARY KEY (`REPORT_TEMPLATE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of REPORT_TEMPLATE
-- ----------------------------
INSERT INTO `REPORT_TEMPLATE` VALUES ('0980f72a-1a7c-430e-9056-d0bc2785baf7', 'E:/jtiism_data/resource/reportTemplate/f3b0a378-d1f1-4f0a-b1a1-7ef9e40432e5/本年度企业税收统计.xls', '3', '', '.输入定义\r\n$no>cds.setCell(1)\r\n$year>cds.getCurDate().dateFormate(\"yyyy\")\r\n$title>cds.setCell(\"年度税收企业名单\")\r\n>>$title = $year + $title\r\nA1>cds.setCell($title)\r\n\r\n.单元格定义\r\n>>cds.copyARowWithStartTempRow(3)\r\n\r\n#企业数据\r\n$enterprise>cds.data()\r\n#企业id\r\n$enterpriseId>cds.getParam(\"$enterprise\").get(\"ENTINF000\")\r\n\r\n#序号\r\nA3>cds.setCell($no)\r\n>>$no= $no + 1\r\n\r\n#企业名称\r\nB3>cds.getParam(\"$enterprise\").get(\"ENTINF001\")\r\n\r\n#税收\r\n$entYosList>cds.sql(\"select sum(ENTYOS006) ENTYOS006 from SHJT_ENT_YOS where ENTINF000 = \'${$enterpriseId}\' and ENTYOS002 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#税收合计\r\nC3>cds.getParam(\"$entYosList\").index(0).get(\"ENTYOS006\")\r\n\r\n#企业性质\r\nD3>cds.getParam(\"$enterprise\").get(\"ENTINF019\").codeName(\"ENTINF019\")\r\n\r\n#企业类型\r\nE3>cds.getParam(\"$enterprise\").get(\"ENTINF026\").codeName(\"ENTINF026\")\r\n\r\n#经营地址\r\nF3>cds.getParam(\"$enterprise\").get(\"ENTINF006\")\r\n\r\n#企业法人列表\r\n$entPerList>cds.sql(\"select ENTCTI001,ENTCTI002,ENTCTI009,ENTCTI011,ENTCTI012 from SHJT_ENT_CTI where ENTINF000 = \'${$enterpriseId}\' and ENTCTI003 = \'1\'\")\r\n#法人\r\n$entPerFir>cds.getParam(\"$entPerList\").index(0)\r\n#法人姓名\r\nG3>cds.getParam(\"$entPerFir\").get(\"ENTCTI002\")\r\n#法人固定电话\r\nH3>cds.getParam(\"$entPerFir\").get(\"ENTCTI010\")\r\n#法人手机号\r\nI3>cds.getParam(\"$entPerFir\").get(\"ENTCTI009\")\r\n#联系电话\r\nJ3>cds.getParam(\"$enterprise\").get(\"ENTINF051\")\r\n#企业年度经营情况列表\r\n$entAosList>cds.sql(\"select ENTAOS017,ENTAOS018,ENTAOS019 from SHJT_ENT_AOS where ENTINF000 = \'${$enterpriseId}\'\")\r\n#企业年度经营情况\r\n$entAos>cds.getParam(\"$entAosList\").index(0)\r\n#增值税（万元）\r\nK3>cds.getParam(\"$entAos\").get(\"ENTAOS017\")\r\n#企业所得税（万元）\r\nL3>cds.getParam(\"$entAos\").get(\"ENTAOS018\")\r\n#个人所得税（万元）\r\nM3>cds.getParam(\"$entAos\").get(\"ENTAOS019\")', '本年度企业税收', '0');
INSERT INTO `REPORT_TEMPLATE` VALUES ('11039e0a-8df2-4445-9139-357b971cd28f', 'E:/jtiism_data/resource/reportTemplate/925b21a0-75ec-4d3e-a539-957c32e9e833/上年度企业税收统计.xls', '5', '', '.输入定义\r\n$no>cds.setCell(1)\r\n$year>cds.getCurDate().dateFormate(\"yyyy\")\r\n$title>cds.setCell(\"年度税收统计\")\r\n>>$title = $year + $title\r\nA1>cds.setCell($title)\r\n\r\n.单元格定义\r\n>>cds.copyARowWithStartTempRow(4)\r\n\r\n#企业数据\r\n$enterprise>cds.data()\r\n#企业id\r\n$enterpriseId>cds.getParam(\"$enterprise\").get(\"ENTINF000\")\r\n\r\n#序号\r\nA4>cds.setCell($no)\r\n>>$no= $no + 1\r\n\r\n#企业名称\r\nB4>cds.getParam(\"$enterprise\").get(\"ENTINF001\")\r\n\r\n#社会统一信用代码\r\nC4>cds.getParam(\"$enterprise\").get(\"ENTINF015\")\r\n\r\n#税收第一季度统计数据 REPYOS001 = 1\r\n$entYosFirst>cds.sql(\"select ENTINF000, REPYOS001, REPYOS011, REPYOS002, REPYOS003  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'1\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#税收第二季度统计数据 REPYOS001 = 2\r\n$entYosSec>cds.sql(\"select ENTINF000, REPYOS001, REPYOS011, REPYOS002, REPYOS003  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'2\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#税收第三季度统计数据 REPYOS001 = 3\r\n$entYosThir>cds.sql(\"select ENTINF000, REPYOS001, REPYOS011, REPYOS002, REPYOS003  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'3\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#税收第四季度统计数据 REPYOS001 = 4\r\n$entYosFour>cds.sql(\"select ENTINF000, REPYOS001, REPYOS011, REPYOS002, REPYOS003  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'4\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#第一季度税收\r\nD4>cds.getParam(\"$entYosFirst\").index(0).get(\"REPYOS011\")\r\n\r\n#第一季度同比\r\nE4>cds.getParam(\"$entYosFirst\").index(0).get(\"REPYOS002\")\r\n\r\n#第一季度环比\r\nF4>cds.getParam(\"$entYosFirst\").index(0).get(\"REPYOS003\")\r\n\r\n#第二季度税收\r\nG4>cds.getParam(\"$entYosSec\").index(0).get(\"REPYOS011\")\r\n\r\n#第二季度同比\r\nH4>cds.getParam(\"$entYosSec\").index(0).get(\"REPYOS002\")\r\n\r\n#第二季度环比\r\nI4>cds.getParam(\"$entYosSec\").index(0).get(\"REPYOS003\")\r\n\r\n#第三季度税收\r\nJ4>cds.getParam(\"$entYosThir\").index(0).get(\"REPYOS011\")\r\n\r\n#第三季度同比\r\nK4>cds.getParam(\"$entYosThir\").index(0).get(\"REPYOS002\")\r\n\r\n#第三季度环比\r\nL4>cds.getParam(\"$entYosThir\").index(0).get(\"REPYOS003\")\r\n\r\n\r\n#第四季度税收\r\nM4>cds.getParam(\"$entYosFour\").index(0).get(\"REPYOS011\")\r\n\r\n#第四季度同比\r\nN4>cds.getParam(\"$entYosFour\").index(0).get(\"REPYOS002\")\r\n\r\n#第四季度环比\r\nO4>cds.getParam(\"$entYosFour\").index(0).get(\"REPYOS003\")\r\n', '本年度企业税收统计', '0');
INSERT INTO `REPORT_TEMPLATE` VALUES ('32706a0d-4844-4b84-857d-05298ae0986c', 'E:/jtiism_data/resource/reportTemplate/bd7b3534-c6f4-4428-b2e6-6105700ae949/九亭镇安监所企业排摸表.xls', '1', '', '.输入定义\r\n$no>cds.setCell(1)\r\n$year>cds.getCurDate().dateFormate(\"yyyy\")\r\n$title>cds.setCell(\"年九亭镇安全生产监管企业汇总表\")\r\n>>$title = $year + $title\r\nA1>cds.setCell($title)\r\n\r\n.单元格定义\r\n>>cds.copyARowWithStartTempRow(3)\r\n\r\n#企业数据\r\n$enterprise>cds.data()\r\n#企业id\r\n$enterpriseId>cds.getParam(\"$enterprise\").get(\"ENTINF000\")\r\n\r\n#序号\r\nA3>cds.setCell($no)\r\n>>$no= $no + 1\r\n\r\n#企业名称\r\nB3>cds.getParam(\"$enterprise\").get(\"ENTINF001\")\r\n#企业性质\r\nC3>cds.getParam(\"$enterprise\").get(\"ENTINF003\").codeName(\"ENTINF003\")\r\n#企业地址\r\nD3>cds.getParam(\"$enterprise\").get(\"ENTINF006\")\r\n\r\n#法人代表\r\nE3>cds.sql(\"select ENTCTI002,ENTCTI009 from SHJT_ENT_CTI where ENTCTI003 = \'1\' and ENTINF000 = \'${$enterpriseId}\'\").index(0).get(\"ENTCTI002\")\r\n\r\n#联系人\r\n$entCti>cds.sql(\"select ENTCTI002,ENTCTI009 from SHJT_ENT_CTI where ENTCTI003 = \'3\' and ENTINF000 = \'${$enterpriseId}\'\").index(0)\r\n#安全干部\r\nF3>cds.getParam(\"$entCti\").get(\"ENTCTI002\")\r\n#联系方式\r\nG3>cds.getParam(\"$entCti\").get(\"ENTCTI009\")\r\n\r\n#企业人员\r\n$entPer>cds.sql(\"select ENTPER001 from SHJT_ENT_PER  where ENTINF000 = \'${$enterpriseId}\'\").index(0)\r\n\r\n#员工人数\r\nH3>cds.getParam(\"$entPer\").get(\"ENTPER001\")\r\n\r\n#行业类别\r\nI3>cds.getParam(\"$enterprise\").get(\"ENTINF012\").codeName(\"ENTINF012\")\r\n\r\n#安监\r\n$entSsm>cds.sql(\"select ENTSSM001,ENTSSM003,ENTSSM014,ENTSSM005,ENTSSM007,ENTSSM009,ENTSSM002,ENTSSM004,ENTSSM006,ENTSSM008,ENTSSM013 from SHJT_ENT_SSM  where ENTINF000 = \'${$enterpriseId}\' order by ENTSSM015 DESC \").index(0)\r\n\r\n#进驻时间\r\nJ3>cds.getParam(\"$entSsm\").get(\"ENTSSM001\").dateFormate(\"yyyy-MM-dd\")\r\n\r\n#标准化达标等级\r\n$entssm003>cds.getParam(\"$entSsm\").get(\"ENTSSM003\").codeName(\"ENTSSM003\")\r\n#标准化达标时间 ENTSSM014\r\n$entssm014>cds.getParam(\"$entSsm\").get(\"ENTSSM014\").dateFormate(\"yyyy-MM-dd\")\r\n>> $entssm003 = iff($entssm003 == null, \"\", $entssm003);\r\n$entssm003 = $entssm003 + \"/\" + $entssm014;\r\n$entssm003 = iff(\"/\".equals($entssm003), \"\", $entssm003);\r\n\r\nK3>cds.setCell($entssm003)\r\n\r\n#应急预案备案日期\r\nL3>cds.getParam(\"$entSsm\").get(\"ENTSSM005\").dateFormate(\"yyyy-MM-dd\")\r\n\r\n#是否完成环评审批\r\nM3>cds.getParam(\"$entSsm\").get(\"ENTSSM007\").yesOrno()\r\n\r\n#是否完成环评验收\r\nN3>cds.getParam(\"$entSsm\").get(\"ENTSSM009\").yesOrno()\r\n\r\n#企业安全风险分级\r\nO3>cds.getParam(\"$entSsm\").get(\"ENTSSM002\").codeName(\"ENTSSM002\")\r\n\r\n#危险化学品存量\r\nP3>cds.getParam(\"$entSsm\").get(\"ENTSSM004\")\r\n\r\n#区/镇消防重点单位\r\nQ3>cds.getParam(\"$entSsm\").get(\"ENTSSM006\").yesOrno()\r\n\r\n#重点监管单位\r\nR3>cds.getParam(\"$entSsm\").get(\"ENTSSM008\").yesOrno()\r\n\r\n#备注\r\nS3>cds.getParam(\"$entSsm\").get(\"ENTSSM013\")\r\n\r\n#辖区\r\nT3>cds.getParam(\"$enterprise\").get(\"ENTINF023\").codeName(\"ENTINF023\")\r\n\r\n\r\n\r\n', '安监所企业排摸表', '0');
INSERT INTO `REPORT_TEMPLATE` VALUES ('359ba5e7-780d-4168-bbbb-53c54cc97f7b', 'E:/jtiism_data/resource/reportTemplate/d3d99af4-67ea-4dfb-b845-0b23a98b5c72/本年度企业销售统计.xls', '7', '', '.输入定义\r\n$no>cds.setCell(1)\r\n$year>cds.getCurDate().dateFormate(\"yyyy\")\r\n$title>cds.setCell(\"年度销售统计\")\r\n>>$title = $year + $title\r\nA1>cds.setCell($title)\r\n\r\n.单元格定义\r\n>>cds.copyARowWithStartTempRow(4)\r\n\r\n#企业数据\r\n$enterprise>cds.data()\r\n#企业id\r\n$enterpriseId>cds.getParam(\"$enterprise\").get(\"ENTINF000\")\r\n\r\n#序号\r\nA4>cds.setCell($no)\r\n>>$no= $no + 1\r\n\r\n#企业名称\r\nB4>cds.getParam(\"$enterprise\").get(\"ENTINF001\")\r\n\r\n#社会统一信用代码\r\nC4>cds.getParam(\"$enterprise\").get(\"ENTINF015\")\r\n\r\n#税收第一季度统计数据 REPYOS001 = 1\r\n$entYosFirst>cds.sql(\"select ENTINF000, REPYOS001, REPYOS009, REPYOS004, REPYOS005  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'1\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#税收第二季度统计数据 REPYOS001 = 2\r\n$entYosSec>cds.sql(\"select ENTINF000, REPYOS001, REPYOS009, REPYOS004, REPYOS005  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'2\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#税收第三季度统计数据 REPYOS001 = 3\r\n$entYosThir>cds.sql(\"select ENTINF000, REPYOS001, REPYOS009, REPYOS004, REPYOS005  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'3\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#税收第四季度统计数据 REPYOS001 = 4\r\n$entYosFour>cds.sql(\"select ENTINF000, REPYOS001, REPYOS009, REPYOS004, REPYOS005  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'4\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#第一季度销售\r\nD4>cds.getParam(\"$entYosFirst\").index(0).get(\"REPYOS009\")\r\n\r\n#第一季度同比\r\nE4>cds.getParam(\"$entYosFirst\").index(0).get(\"REPYOS004\")\r\n\r\n#第一季度环比\r\nF4>cds.getParam(\"$entYosFirst\").index(0).get(\"REPYOS005\")\r\n\r\n#第二季度销售\r\nG4>cds.getParam(\"$entYosSec\").index(0).get(\"REPYOS009\")\r\n\r\n#第二季度同比\r\nH4>cds.getParam(\"$entYosSec\").index(0).get(\"REPYOS004\")\r\n\r\n#第二季度环比\r\nI4>cds.getParam(\"$entYosSec\").index(0).get(\"REPYOS005\")\r\n\r\n#第三季度销售\r\nJ4>cds.getParam(\"$entYosThir\").index(0).get(\"REPYOS009\")\r\n\r\n#第三季度同比\r\nK4>cds.getParam(\"$entYosThir\").index(0).get(\"REPYOS004\")\r\n\r\n#第三季度环比\r\nL4>cds.getParam(\"$entYosThir\").index(0).get(\"REPYOS005\")\r\n\r\n\r\n#第四季度销售\r\nM4>cds.getParam(\"$entYosFour\").index(0).get(\"REPYOS009\")\r\n\r\n#第四季度同比\r\nN4>cds.getParam(\"$entYosFour\").index(0).get(\"REPYOS004\")\r\n\r\n#第四季度环比\r\nO4>cds.getParam(\"$entYosFour\").index(0).get(\"REPYOS005\")', '本年度企业销售统计', '0');
INSERT INTO `REPORT_TEMPLATE` VALUES ('888c5565-c252-476e-a031-d2647a9dfecd', 'E:/jtiism_data/resource/reportTemplate/59ca1be9-cc68-4bc4-94aa-b6459fa3bd5b/上年度企业税收.xls', '4', '', '.输入定义\r\n$no>cds.setCell(1)\r\n$year>cds.getDateBeforeYear(1).dateFormate(\"yyyy\")\r\n$title>cds.setCell(\"年度税收企业名单\")\r\n>>$title = $year + $title\r\nA1>cds.setCell($title)\r\n\r\n.单元格定义\r\n>>cds.copyARowWithStartTempRow(3)\r\n\r\n#企业数据\r\n$enterprise>cds.data()\r\n#企业id\r\n$enterpriseId>cds.getParam(\"$enterprise\").get(\"ENTINF000\")\r\n\r\n#序号\r\nA3>cds.setCell($no)\r\n>>$no= $no + 1\r\n\r\n#企业名称\r\nB3>cds.getParam(\"$enterprise\").get(\"ENTINF001\")\r\n\r\n#税收\r\n$entYosList>cds.sql(\"select sum(ENTYOS006) ENTYOS006 from SHJT_ENT_YOS where ENTINF000 = \'${$enterpriseId}\' and ENTYOS002 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#税收合计\r\nC3>cds.getParam(\"$entYosList\").index(0).get(\"ENTYOS006\")\r\n\r\n#企业性质\r\nD3>cds.getParam(\"$enterprise\").get(\"ENTINF019\").codeName(\"ENTINF019\")\r\n\r\n#企业类型\r\nE3>cds.getParam(\"$enterprise\").get(\"ENTINF026\").codeName(\"ENTINF026\")\r\n\r\n#经营地址\r\nF3>cds.getParam(\"$enterprise\").get(\"ENTINF006\")\r\n\r\n#企业法人列表\r\n$entPerList>cds.sql(\"select ENTCTI001,ENTCTI002,ENTCTI009,ENTCTI011,ENTCTI012 from SHJT_ENT_CTI where ENTINF000 = \'${$enterpriseId}\' and ENTCTI003 = \'1\'\")\r\n#法人\r\n$entPerFir>cds.getParam(\"$entPerList\").index(0)\r\n#法人姓名\r\nG3>cds.getParam(\"$entPerFir\").get(\"ENTCTI002\")\r\n#法人固定电话\r\nH3>cds.getParam(\"$entPerFir\").get(\"ENTCTI010\")\r\n#法人手机号\r\nI3>cds.getParam(\"$entPerFir\").get(\"ENTCTI009\")', '上年度企业税收', '0');
INSERT INTO `REPORT_TEMPLATE` VALUES ('88cf3508-8bab-4ab7-be1b-aac255b51e24', 'E:/jtiism_data/resource/reportTemplate/fc380bcc-7e9d-4763-bb31-870fddb9c375/本年度企业利润统计.xls', '10', '', '.输入定义\r\n$no>cds.setCell(1)\r\n$year>cds.getDateBeforeYear(1).dateFormate(\"yyyy\")\r\n$title>cds.setCell(\"年度利润统计\")\r\n>>$title = $year + $title\r\nA1>cds.setCell($title)\r\n\r\n.单元格定义\r\n>>cds.copyARowWithStartTempRow(4)\r\n\r\n#企业数据\r\n$enterprise>cds.data()\r\n#企业id\r\n$enterpriseId>cds.getParam(\"$enterprise\").get(\"ENTINF000\")\r\n\r\n#序号\r\nA4>cds.setCell($no)\r\n>>$no= $no + 1\r\n\r\n#企业名称\r\nB4>cds.getParam(\"$enterprise\").get(\"ENTINF001\")\r\n\r\n#社会统一信用代码\r\nC4>cds.getParam(\"$enterprise\").get(\"ENTINF015\")\r\n\r\n#利润第一季度统计数据 REPYOS001 = 1\r\n$entYosFirst>cds.sql(\"select ENTINF000, REPYOS001, REPYOS010, REPYOS006, REPYOS007  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'1\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#利润第二季度统计数据 REPYOS001 = 2\r\n$entYosSec>cds.sql(\"select ENTINF000, REPYOS001, REPYOS010, REPYOS006, REPYOS007  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'2\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#利润第三季度统计数据 REPYOS001 = 3\r\n$entYosThir>cds.sql(\"select ENTINF000, REPYOS001, REPYOS010, REPYOS006, REPYOS007  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'3\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#利润第四季度统计数据 REPYOS001 = 4\r\n$entYosFour>cds.sql(\"select ENTINF000, REPYOS001, REPYOS010, REPYOS006, REPYOS007  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'4\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#第一季度利润\r\nD4>cds.getParam(\"$entYosFirst\").index(0).get(\"REPYOS010\")\r\n\r\n#第一季度同比\r\nE4>cds.getParam(\"$entYosFirst\").index(0).get(\"REPYOS006\")\r\n\r\n#第一季度环比\r\nF4>cds.getParam(\"$entYosFirst\").index(0).get(\"REPYOS007\")\r\n\r\n#第二季度利润\r\nG4>cds.getParam(\"$entYosSec\").index(0).get(\"REPYOS010\")\r\n\r\n#第二季度同比\r\nH4>cds.getParam(\"$entYosSec\").index(0).get(\"REPYOS006\")\r\n\r\n#第二季度环比\r\nI4>cds.getParam(\"$entYosSec\").index(0).get(\"REPYOS007\")\r\n\r\n#第三季度利润\r\nJ4>cds.getParam(\"$entYosThir\").index(0).get(\"REPYOS010\")\r\n\r\n#第三季度同比\r\nK4>cds.getParam(\"$entYosThir\").index(0).get(\"REPYOS006\")\r\n\r\n#第三季度环比\r\nL4>cds.getParam(\"$entYosThir\").index(0).get(\"REPYOS007\")\r\n\r\n\r\n#第四季度利润\r\nM4>cds.getParam(\"$entYosFour\").index(0).get(\"REPYOS010\")\r\n\r\n#第四季度同比\r\nN4>cds.getParam(\"$entYosFour\").index(0).get(\"REPYOS006\")\r\n\r\n#第四季度环比\r\nO4>cds.getParam(\"$entYosFour\").index(0).get(\"REPYOS007\")', '上年度企业利润统计', '0');
INSERT INTO `REPORT_TEMPLATE` VALUES ('966ff87e-f871-4a52-86e0-e43114b394b1', 'E:/jtiism_data/resource/reportTemplate/c87aa1af-3998-4efc-b5fb-9d69b375c0a0/企业呈报表.xls', '2', '', '.输入定义\r\n>> if (!context.isMulFile())cds.setFileName(\"企业呈报表\")\r\n\r\n.单元格定义\r\n\r\n\r\n#企业数据\r\n$enterprise>cds.data()\r\n#企业id\r\n$enterpriseId>cds.getParam(\"$enterprise\").get(\"ENTINF000\")\r\n\r\n\r\n#企业名称\r\n$enterpriseName>cds.getParam(\"$enterprise\").get(\"ENTINF001\")\r\n>> if(context.isMulFile())cds.setFileName($enterpriseName)\r\n>> cds.setSheetName($enterpriseName)\r\n\r\n#企业名称\r\nH3>cds.getParam(\"$enterprise\").get(\"ENTINF001\")\r\n#企业图片\r\nT3,AK10>cds.getParam(\"$enterprise\").picture(\"ENTINF000\").setPicture()\r\n\r\n#社会统一代码\r\nH5>cds.getParam(\"$enterprise\").get(\"ENTINF015\")\r\n#类型\r\nH7>cds.getParam(\"$enterprise\").get(\"ENTINF003\").codeName(\"ENTINF003\")\r\n#企业类型\r\nH9>cds.getParam(\"$enterprise\").get(\"ENTINF026\").codeName(\"ENTINF026\")\r\n#投资方分类\r\nH11>cds.getParam(\"$enterprise\").get(\"ENTINF020\").codeName(\"ENTINF020\")\r\n#所有制类型 \r\nZ11>cds.getParam(\"$enterprise\").get(\"ENTINF019\").codeName(\"ENTINF019\")\r\n#币种H13\r\nH13>cds.getParam(\"$enterprise\").get(\"ENTINF043\").codeName(\"ENTINF043\")\r\n#注册资本Z13\r\nZ13>cds.getParam(\"$enterprise\").get(\"ENTINF009\")\r\n#合同外资（万）\r\nH15>cds.getParam(\"$enterprise\").get(\"ENTINF031\")\r\n#投资总额（万）\r\nZ15>cds.getParam(\"$enterprise\").get(\"ENTINF042\")\r\n#到资额（万）\r\nH17>cds.getParam(\"$enterprise\").get(\"ENTINF032\")\r\n#地块分类\r\nZ17>cds.getParam(\"$enterprise\").get(\"ENTINF021\").codeName(\"ENTINF021\")\r\n#区域分类\r\nH19>cds.getParam(\"$enterprise\").get(\"ENTINF022\").codeName(\"ENTINF022\")\r\n#管理权限\r\nZ19>cds.getParam(\"$enterprise\").get(\"ENTINF023\").codeName(\"ENTINF023\")\r\n#成立时间 \r\nH21>cds.getParam(\"$enterprise\").get(\"ENTINF010\").dateFormate(\"yyyy-MM-dd\")\r\n#国    别\r\nZ21>cds.getParam(\"$enterprise\").get(\"ENTINF041\").codeName(\"ENTINF041\")\r\n#主营业务\r\nH23>cds.getParam(\"$enterprise\").get(\"ENTINF034\")\r\n#登记机关 \r\nH25>cds.getParam(\"$enterprise\").get(\"ENTINF014\")\r\n#企业邮箱\r\nH27>cds.getParam(\"$enterprise\").get(\"ENTINF017\")\r\n#企业网址\r\nH29>cds.getParam(\"$enterprise\").get(\"ENTINF018\")\r\n#固定资产投资计划说明\r\nE31>cds.getParam(\"$enterprise\").get(\"ENTINF040\")\r\n\r\n#企业人员列表\r\n$entPerList>cds.sql(\"select ENTCTI001,ENTCTI002,ENTCTI009,ENTCTI011,ENTCTI012 from SHJT_ENT_CTI where ENTINF000 = \'${$enterpriseId}\'\")\r\n\r\n#企业人员\r\n$entPerFir>cds.getParam(\"$entPerList\").index(0)\r\n$entPerSec>cds.getParam(\"$entPerList\").index(1)\r\n$entPerThird>cds.getParam(\"$entPerList\").index(2)\r\n\r\n#第一个人员\r\n#职务\r\nE40>cds.getParam(\"$entPerFir\").get(\"ENTCTI001\")\r\n#姓名\r\nI40>cds.getParam(\"$entPerFir\").get(\"ENTCTI002\")\r\n#手机号\r\nQ40>cds.getParam(\"$entPerFir\").get(\"ENTCTI009\")\r\n#QQ\r\nX40>cds.getParam(\"$entPerFir\").get(\"ENTCTI011\")\r\n#微信\r\nAD40>cds.getParam(\"$entPerFir\").get(\"ENTCTI012\")\r\n\r\n#第二个人员\r\n#职务\r\nE42>cds.getParam(\"$entPerSec\").get(\"ENTCTI001\")\r\n#姓名\r\nI42>cds.getParam(\"$entPerSec\").get(\"ENTCTI002\")\r\n#手机号\r\nQ42>cds.getParam(\"$entPerSec\").get(\"ENTCTI009\")\r\n#QQ\r\nX42>cds.getParam(\"$entPerSec\").get(\"ENTCTI011\")\r\n#微信\r\nAD42>cds.getParam(\"$entPerSec\").get(\"ENTCTI012\")\r\n\r\n#第三个人员\r\n#职务\r\nE44>cds.getParam(\"$entPerThird\").get(\"ENTCTI001\")\r\n#姓名\r\nI44>cds.getParam(\"$entPerThird\").get(\"ENTCTI002\")\r\n#手机号\r\nQ44>cds.getParam(\"$entPerThird\").get(\"ENTCTI009\")\r\n#QQ\r\nX44>cds.getParam(\"$entPerThird\").get(\"ENTCTI011\")\r\n#微信\r\nAD44>cds.getParam(\"$entPerThird\").get(\"ENTCTI012\")\r\n\r\n#关联公司\r\n$entRcsList>cds.sql(\"select ENTRCS001,ENTRCS004,ENTRCS007,ENTRCS008 from SHJT_ENT_RCS where ENTINF000 = \'${$enterpriseId}\'\")\r\n\r\n$entRcsFir>cds.getParam(\"$entRcsList\").index(0)\r\n$entRcsSec>cds.getParam(\"$entRcsList\").index(1)\r\n$entRcsThird>cds.getParam(\"$entRcsList\").index(2)\r\n\r\n#第一个关联公司\r\n#企业名称\r\nE48>cds.getParam(\"$entRcsFir\").get(\"ENTRCS001\")\r\n#关联方式\r\nN48>cds.getParam(\"$entRcsFir\").get(\"ENTRCS004\").codeName(\"ENTRCS004\")\r\n#联系方式\r\nV48>cds.getParam(\"$entRcsFir\").get(\"ENTRCS008\")\r\n\r\n\r\n#第二个关联公司\r\n#企业名称\r\nE50>cds.getParam(\"$entRcsSec\").get(\"ENTRCS001\")\r\n#关联方式\r\nN50>cds.getParam(\"$entRcsSec\").get(\"ENTRCS004\").codeName(\"ENTRCS004\")\r\n#联系方式\r\nV50>cds.getParam(\"$entRcsSec\").get(\"ENTRCS008\")\r\n\r\n#第三个关联公司\r\n#企业名称\r\nE52>cds.getParam(\"$entRcsThird\").get(\"ENTRCS001\")\r\n#关联方式\r\nN52>cds.getParam(\"$entRcsThird\").get(\"ENTRCS004\").codeName(\"ENTRCS004\")\r\n#联系方式\r\nV52>cds.getParam(\"$entRcsThird\").get(\"ENTRCS008\")\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n', '企业呈报表', '1');
INSERT INTO `REPORT_TEMPLATE` VALUES ('b0d63fb4-9e79-41ef-b1f1-c2f3900848ff', 'E:/jtiism_data/resource/reportTemplate/66c9b4c3-0dd9-4bf3-aa91-6ac5ae6d0963/本年度企业销售统计.xls', '8', '', '.输入定义\r\n$no>cds.setCell(1)\r\n$year>cds.getDateBeforeYear(1).dateFormate(\"yyyy\")\r\n$title>cds.setCell(\"年度销售统计\")\r\n>>$title = $year + $title\r\nA1>cds.setCell($title)\r\n\r\n.单元格定义\r\n>>cds.copyARowWithStartTempRow(4)\r\n\r\n#企业数据\r\n$enterprise>cds.data()\r\n#企业id\r\n$enterpriseId>cds.getParam(\"$enterprise\").get(\"ENTINF000\")\r\n\r\n#序号\r\nA4>cds.setCell($no)\r\n>>$no= $no + 1\r\n\r\n#企业名称\r\nB4>cds.getParam(\"$enterprise\").get(\"ENTINF001\")\r\n\r\n#社会统一信用代码\r\nC4>cds.getParam(\"$enterprise\").get(\"ENTINF015\")\r\n\r\n#销售第一季度统计数据 REPYOS001 = 1\r\n$entYosFirst>cds.sql(\"select ENTINF000, REPYOS001, REPYOS009, REPYOS004, REPYOS005  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'1\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#销售第二季度统计数据 REPYOS001 = 2\r\n$entYosSec>cds.sql(\"select ENTINF000, REPYOS001, REPYOS009, REPYOS004, REPYOS005  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'2\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#销售第三季度统计数据 REPYOS001 = 3\r\n$entYosThir>cds.sql(\"select ENTINF000, REPYOS001, REPYOS009, REPYOS004, REPYOS005  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'3\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#销售第四季度统计数据 REPYOS001 = 4\r\n$entYosFour>cds.sql(\"select ENTINF000, REPYOS001, REPYOS009, REPYOS004, REPYOS005  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'4\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#第一季度销售\r\nD4>cds.getParam(\"$entYosFirst\").index(0).get(\"REPYOS009\")\r\n\r\n#第一季度同比\r\nE4>cds.getParam(\"$entYosFirst\").index(0).get(\"REPYOS004\")\r\n\r\n#第一季度环比\r\nF4>cds.getParam(\"$entYosFirst\").index(0).get(\"REPYOS005\")\r\n\r\n#第二季度销售\r\nG4>cds.getParam(\"$entYosSec\").index(0).get(\"REPYOS009\")\r\n\r\n#第二季度同比\r\nH4>cds.getParam(\"$entYosSec\").index(0).get(\"REPYOS004\")\r\n\r\n#第二季度环比\r\nI4>cds.getParam(\"$entYosSec\").index(0).get(\"REPYOS005\")\r\n\r\n#第三季度销售\r\nJ4>cds.getParam(\"$entYosThir\").index(0).get(\"REPYOS009\")\r\n\r\n#第三季度同比\r\nK4>cds.getParam(\"$entYosThir\").index(0).get(\"REPYOS004\")\r\n\r\n#第三季度环比\r\nL4>cds.getParam(\"$entYosThir\").index(0).get(\"REPYOS005\")\r\n\r\n\r\n#第四季度销售\r\nM4>cds.getParam(\"$entYosFour\").index(0).get(\"REPYOS009\")\r\n\r\n#第四季度同比\r\nN4>cds.getParam(\"$entYosFour\").index(0).get(\"REPYOS004\")\r\n\r\n#第四季度环比\r\nO4>cds.getParam(\"$entYosFour\").index(0).get(\"REPYOS005\")', '上年度企业销售统计', '0');
INSERT INTO `REPORT_TEMPLATE` VALUES ('def0ed73-a593-4eff-a15a-88c6859fe423', 'E:/jtiism_data/resource/reportTemplate/74fa8b65-21fa-4bf8-8d97-7ad4ccb03ed3/本年度企业利润统计.xls', '9', '', '.输入定义\r\n$no>cds.setCell(1)\r\n$year>cds.getCurDate().dateFormate(\"yyyy\")\r\n$title>cds.setCell(\"年度利润统计\")\r\n>>$title = $year + $title\r\nA1>cds.setCell($title)\r\n\r\n.单元格定义\r\n>>cds.copyARowWithStartTempRow(4)\r\n\r\n#企业数据\r\n$enterprise>cds.data()\r\n#企业id\r\n$enterpriseId>cds.getParam(\"$enterprise\").get(\"ENTINF000\")\r\n\r\n#序号\r\nA4>cds.setCell($no)\r\n>>$no= $no + 1\r\n\r\n#企业名称\r\nB4>cds.getParam(\"$enterprise\").get(\"ENTINF001\")\r\n\r\n#社会统一信用代码\r\nC4>cds.getParam(\"$enterprise\").get(\"ENTINF015\")\r\n\r\n#利润第一季度统计数据 REPYOS001 = 1\r\n$entYosFirst>cds.sql(\"select ENTINF000, REPYOS001, REPYOS010, REPYOS006, REPYOS007  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'1\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#利润第二季度统计数据 REPYOS001 = 2\r\n$entYosSec>cds.sql(\"select ENTINF000, REPYOS001, REPYOS010, REPYOS006, REPYOS007  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'2\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#利润第三季度统计数据 REPYOS001 = 3\r\n$entYosThir>cds.sql(\"select ENTINF000, REPYOS001, REPYOS010, REPYOS006, REPYOS007  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'3\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#利润第四季度统计数据 REPYOS001 = 4\r\n$entYosFour>cds.sql(\"select ENTINF000, REPYOS001, REPYOS010, REPYOS006, REPYOS007  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'4\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#第一季度利润\r\nD4>cds.getParam(\"$entYosFirst\").index(0).get(\"REPYOS010\")\r\n\r\n#第一季度同比\r\nE4>cds.getParam(\"$entYosFirst\").index(0).get(\"REPYOS006\")\r\n\r\n#第一季度环比\r\nF4>cds.getParam(\"$entYosFirst\").index(0).get(\"REPYOS007\")\r\n\r\n#第二季度利润\r\nG4>cds.getParam(\"$entYosSec\").index(0).get(\"REPYOS010\")\r\n\r\n#第二季度同比\r\nH4>cds.getParam(\"$entYosSec\").index(0).get(\"REPYOS006\")\r\n\r\n#第二季度环比\r\nI4>cds.getParam(\"$entYosSec\").index(0).get(\"REPYOS007\")\r\n\r\n#第三季度利润\r\nJ4>cds.getParam(\"$entYosThir\").index(0).get(\"REPYOS010\")\r\n\r\n#第三季度同比\r\nK4>cds.getParam(\"$entYosThir\").index(0).get(\"REPYOS006\")\r\n\r\n#第三季度环比\r\nL4>cds.getParam(\"$entYosThir\").index(0).get(\"REPYOS007\")\r\n\r\n\r\n#第四季度利润\r\nM4>cds.getParam(\"$entYosFour\").index(0).get(\"REPYOS010\")\r\n\r\n#第四季度同比\r\nN4>cds.getParam(\"$entYosFour\").index(0).get(\"REPYOS006\")\r\n\r\n#第四季度环比\r\nO4>cds.getParam(\"$entYosFour\").index(0).get(\"REPYOS007\")', '本年度企业利润统计', '0');
INSERT INTO `REPORT_TEMPLATE` VALUES ('f68cb36a-a2b6-4dc8-a2a3-b2137474974e', 'E:/jtiism_data/resource/reportTemplate/fdec8cee-7c6e-495d-9808-57d9360e9b91/本年度企业税收统计.xls', '6', '', '.输入定义\r\n$no>cds.setCell(1)\r\n$year>cds.getDateBeforeYear(1).dateFormate(\"yyyy\")\r\n$title>cds.setCell(\"年度税收统计\")\r\n>>$title = $year + $title\r\nA1>cds.setCell($title)\r\n\r\n.单元格定义\r\n>>cds.copyARowWithStartTempRow(4)\r\n\r\n#企业数据\r\n$enterprise>cds.data()\r\n#企业id\r\n$enterpriseId>cds.getParam(\"$enterprise\").get(\"ENTINF000\")\r\n\r\n#序号\r\nA4>cds.setCell($no)\r\n>>$no= $no + 1\r\n\r\n#企业名称\r\nB4>cds.getParam(\"$enterprise\").get(\"ENTINF001\")\r\n\r\n#社会统一信用代码\r\nC4>cds.getParam(\"$enterprise\").get(\"ENTINF015\")\r\n\r\n#税收第一季度统计数据 REPYOS001 = 1\r\n$entYosFirst>cds.sql(\"select ENTINF000, REPYOS001, REPYOS011, REPYOS002, REPYOS003  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'1\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#税收第二季度统计数据 REPYOS001 = 2\r\n$entYosSec>cds.sql(\"select ENTINF000, REPYOS001, REPYOS011, REPYOS002, REPYOS003  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'2\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#税收第三季度统计数据 REPYOS001 = 3\r\n$entYosThir>cds.sql(\"select ENTINF000, REPYOS001, REPYOS011, REPYOS002, REPYOS003  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'3\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#税收第四季度统计数据 REPYOS001 = 4\r\n$entYosFour>cds.sql(\"select ENTINF000, REPYOS001, REPYOS011, REPYOS002, REPYOS003  from SHJT_REP_YOS where ENTINF000 = \'${$enterpriseId}\' and REPYOS001 = \'4\' and REPYOS008 BETWEEN \'${$year}-01-01\' AND DATE_ADD(\'${$year}-01-01\', INTERVAL 1 YEAR)\")\r\n\r\n#第一季度税收\r\nD4>cds.getParam(\"$entYosFirst\").index(0).get(\"REPYOS011\")\r\n\r\n#第一季度同比\r\nE4>cds.getParam(\"$entYosFirst\").index(0).get(\"REPYOS002\")\r\n\r\n#第一季度环比\r\nF4>cds.getParam(\"$entYosFirst\").index(0).get(\"REPYOS003\")\r\n\r\n#第二季度税收\r\nG4>cds.getParam(\"$entYosSec\").index(0).get(\"REPYOS011\")\r\n\r\n#第二季度同比\r\nH4>cds.getParam(\"$entYosSec\").index(0).get(\"REPYOS002\")\r\n\r\n#第二季度环比\r\nI4>cds.getParam(\"$entYosSec\").index(0).get(\"REPYOS003\")\r\n\r\n#第三季度税收\r\nJ4>cds.getParam(\"$entYosThir\").index(0).get(\"REPYOS011\")\r\n\r\n#第三季度同比\r\nK4>cds.getParam(\"$entYosThir\").index(0).get(\"REPYOS002\")\r\n\r\n#第三季度环比\r\nL4>cds.getParam(\"$entYosThir\").index(0).get(\"REPYOS003\")\r\n\r\n\r\n#第四季度税收\r\nM4>cds.getParam(\"$entYosFour\").index(0).get(\"REPYOS011\")\r\n\r\n#第四季度同比\r\nN4>cds.getParam(\"$entYosFour\").index(0).get(\"REPYOS002\")\r\n\r\n#第四季度环比\r\nO4>cds.getParam(\"$entYosFour\").index(0).get(\"REPYOS003\")\r\n', '上年度企业税收统计', '0');

-- ----------------------------
-- Table structure for ROLE_DEPT_RELA
-- ----------------------------
DROP TABLE IF EXISTS `ROLE_DEPT_RELA`;
CREATE TABLE `ROLE_DEPT_RELA` (
  `ROLE_DEPT_RELA_ID` varchar(36) NOT NULL COMMENT '角色与处室关系标识',
  `ROLE_ID` int(11) DEFAULT NULL COMMENT '角色标识',
  `DEPT_ID` varchar(36) DEFAULT NULL COMMENT '处室标识',
  PRIMARY KEY (`ROLE_DEPT_RELA_ID`),
  KEY `ROLE_ID` (`ROLE_ID`) USING BTREE,
  KEY `DEPT_ID` (`DEPT_ID`) USING BTREE,
  CONSTRAINT `ROLE_DEPT_RELA_ibfk_1` FOREIGN KEY (`ROLE_ID`) REFERENCES `CFG_UMS_ROLE` (`ID`),
  CONSTRAINT `ROLE_DEPT_RELA_ibfk_2` FOREIGN KEY (`DEPT_ID`) REFERENCES `CMIS_DEPARTMENT` (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与处室关系表';

-- ----------------------------
-- Records of ROLE_DEPT_RELA
-- ----------------------------

-- ----------------------------
-- Table structure for ROLE_INFOSET_PRI
-- ----------------------------
DROP TABLE IF EXISTS `ROLE_INFOSET_PRI`;
CREATE TABLE `ROLE_INFOSET_PRI` (
  `ROLE_INFOSET_PRI_ID` varchar(36) NOT NULL COMMENT '角色信息集信息项权限标识',
  `ROLE_ID` int(11) DEFAULT NULL COMMENT '角色ID',
  `INFO_TYPE` int(11) DEFAULT NULL COMMENT '0：信息集 1：信息项',
  `PRIV_CODE` varchar(50) DEFAULT NULL COMMENT '权限编码',
  `PRIV` int(11) DEFAULT NULL COMMENT '权限 0：只读 1：可编辑',
  PRIMARY KEY (`ROLE_INFOSET_PRI_ID`),
  KEY `ROLE_ID` (`ROLE_ID`) USING BTREE,
  CONSTRAINT `ROLE_INFOSET_PRI_ibfk_1` FOREIGN KEY (`ROLE_ID`) REFERENCES `CFG_UMS_ROLE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色信息集信息项权限';

-- ----------------------------
-- Records of ROLE_INFOSET_PRI
-- ----------------------------
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d1124c10001', null, '0', 'SHJT_ENT_INF', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d1124c10002', null, '0', 'SHJT_ENT_CTI', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d1124c10003', null, '0', 'SHJT_ENT_PLI', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d1124c10004', null, '0', 'SHJT_ENT_RCS', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d1124c10005', null, '0', 'SHJT_ENT_EQU', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d1124c10006', null, '0', 'SHJT_ENT_PER', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d1124c10007', null, '0', 'SHJT_ENT_HTT', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d1124c10008', null, '0', 'SHJT_ENT_YOS', '0');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d1124c10009', null, '0', 'SHJT_ENT_AOS', '0');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d1124c1000a', null, '0', 'SHJT_ENT_SSM', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d1124c1000b', null, '0', 'SHJT_ENT_SEM', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef5000c', null, '1', 'SHJT_ENT_INF_entInf046', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef5000d', null, '1', 'SHJT_ENT_INF_entInf047', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef5000e', null, '1', 'SHJT_ENT_INF_entInf048', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef5000f', null, '1', 'SHJT_ENT_INF_entInf033', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef50010', null, '1', 'SHJT_ENT_INF_entInf045', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef50011', null, '1', 'SHJT_ENT_INF_entInf044', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef50012', null, '1', 'SHJT_ENT_INF_entInf001', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef50013', null, '1', 'SHJT_ENT_INF_entInf002', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef50014', null, '1', 'SHJT_ENT_INF_entInf003', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef60015', null, '1', 'SHJT_ENT_INF_entInf004', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef60016', null, '1', 'SHJT_ENT_INF_entInf005', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef60017', null, '1', 'SHJT_ENT_INF_entInf006', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef60018', null, '1', 'SHJT_ENT_INF_entInf007', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef60019', null, '1', 'SHJT_ENT_INF_entInf008', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef6001a', null, '1', 'SHJT_ENT_INF_entInf009', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef6001b', null, '1', 'SHJT_ENT_INF_entInf010', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef6001c', null, '1', 'SHJT_ENT_INF_entInf011', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef6001d', null, '1', 'SHJT_ENT_INF_entInf012', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef6001e', null, '1', 'SHJT_ENT_INF_entInf013', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef6001f', null, '1', 'SHJT_ENT_INF_entInf014', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef60020', null, '1', 'SHJT_ENT_INF_entInf015', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef60021', null, '1', 'SHJT_ENT_INF_entInf016', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef60022', null, '1', 'SHJT_ENT_INF_entInf017', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef60023', null, '1', 'SHJT_ENT_INF_entInf018', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef60024', null, '1', 'SHJT_ENT_INF_entInf019', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef70025', null, '1', 'SHJT_ENT_INF_entInf020', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef70026', null, '1', 'SHJT_ENT_INF_entInf021', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef80027', null, '1', 'SHJT_ENT_INF_entInf022', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef80028', null, '1', 'SHJT_ENT_INF_entInf023', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef80029', null, '1', 'SHJT_ENT_INF_entInf024', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef8002a', null, '1', 'SHJT_ENT_INF_entInf025', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef8002b', null, '1', 'SHJT_ENT_INF_entInf026', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef8002c', null, '1', 'SHJT_ENT_INF_entInf027', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef8002d', null, '1', 'SHJT_ENT_INF_entInf028', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef8002e', null, '1', 'SHJT_ENT_INF_entInf029', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef8002f', null, '1', 'SHJT_ENT_INF_entInf030', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef90030', null, '1', 'SHJT_ENT_INF_entInf031', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef90031', null, '1', 'SHJT_ENT_INF_entInf032', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef90032', null, '1', 'SHJT_ENT_INF_entInf034', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef90033', null, '1', 'SHJT_ENT_INF_entInf035', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef90034', null, '1', 'SHJT_ENT_INF_entInf036', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef90035', null, '1', 'SHJT_ENT_INF_entInf037', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef90036', null, '1', 'SHJT_ENT_INF_entInf038', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef90037', null, '1', 'SHJT_ENT_INF_entInf039', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef90038', null, '1', 'SHJT_ENT_INF_entInf040', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef90039', null, '1', 'SHJT_ENT_INF_entInf041', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef9003a', null, '1', 'SHJT_ENT_INF_entInf042', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef9003b', null, '1', 'SHJT_ENT_INF_entInf049', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef9003c', null, '1', 'SHJT_ENT_INF_ENTINF050', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef9003d', null, '1', 'SHJT_ENT_INF_entInf043', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef9003e', null, '1', 'SHJT_ENT_INF_entInf051', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef9003f', null, '1', 'SHJT_ENT_CTI_ENTCTI001', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117ef90040', null, '1', 'SHJT_ENT_CTI_ENTCTI002', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efa0041', null, '1', 'SHJT_ENT_CTI_ENTCTI003', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efa0042', null, '1', 'SHJT_ENT_CTI_ENTCTI004', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efa0043', null, '1', 'SHJT_ENT_CTI_ENTCTI005', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efa0044', null, '1', 'SHJT_ENT_CTI_ENTCTI006', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efa0045', null, '1', 'SHJT_ENT_CTI_ENTCTI007', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efa0046', null, '1', 'SHJT_ENT_CTI_ENTCTI008', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efa0047', null, '1', 'SHJT_ENT_CTI_ENTCTI009', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efa0048', null, '1', 'SHJT_ENT_CTI_ENTCTI010', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efa0049', null, '1', 'SHJT_ENT_CTI_ENTCTI011', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efa004a', null, '1', 'SHJT_ENT_CTI_ENTCTI012', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efa004b', null, '1', 'SHJT_ENT_CTI_ENTCTI013', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efa004c', null, '1', 'SHJT_ENT_CTI_ENTCTI014', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efa004d', null, '1', 'SHJT_ENT_CTI_ENTCTI015', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efa004e', null, '1', 'SHJT_ENT_CTI_ENTCTI016', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efa004f', null, '1', 'SHJT_ENT_CTI_ENTCTI017', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efa0050', null, '1', 'SHJT_ENT_CTI_ENTCTI018', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efa0051', null, '1', 'SHJT_ENT_CTI_ENTCTI019', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efa0052', null, '1', 'SHJT_ENT_PLI_ENTPLI014', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efa0053', null, '1', 'SHJT_ENT_PLI_ENTPLI002', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efb0054', null, '1', 'SHJT_ENT_PLI_ENTPLI003', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efb0055', null, '1', 'SHJT_ENT_PLI_ENTPLI004', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efb0056', null, '1', 'SHJT_ENT_PLI_ENTPLI005', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efb0057', null, '1', 'SHJT_ENT_PLI_ENTPLI006', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efb0058', null, '1', 'SHJT_ENT_PLI_ENTPLI007', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efb0059', null, '1', 'SHJT_ENT_PLI_ENTPLI008', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efb005a', null, '1', 'SHJT_ENT_PLI_ENTPLI009', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efb005b', null, '1', 'SHJT_ENT_PLI_ENTPLI010', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efb005c', null, '1', 'SHJT_ENT_PLI_ENTPLI011', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efb005d', null, '1', 'SHJT_ENT_PLI_ENTPLI012', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efb005e', null, '1', 'SHJT_ENT_PLI_ENTPLI013', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efb005f', null, '1', 'SHJT_ENT_RCS_ENTRCS001', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efb0060', null, '1', 'SHJT_ENT_RCS_ENTRCS002', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efb0061', null, '1', 'SHJT_ENT_RCS_ENTRCS003', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efb0062', null, '1', 'SHJT_ENT_RCS_ENTRCS004', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efb0063', null, '1', 'SHJT_ENT_RCS_ENTRCS005', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efb0064', null, '1', 'SHJT_ENT_RCS_ENTRCS006', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efc0065', null, '1', 'SHJT_ENT_RCS_ENTRCS007', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efc0066', null, '1', 'SHJT_ENT_RCS_ENTRCS008', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efc0067', null, '1', 'SHJT_ENT_EQU_EQU001', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efc0068', null, '1', 'SHJT_ENT_EQU_EQU002', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efc0069', null, '1', 'SHJT_ENT_EQU_EQU003', '1');
INSERT INTO `ROLE_INFOSET_PRI` VALUES ('40283d815f4cd869015f4d117efc006a', null, '1', 'SHJT_ENT_EQU_EQU004', '1');

-- ----------------------------
-- Table structure for SHJT_SAN_INFO
-- ----------------------------
DROP TABLE IF EXISTS `SHJT_SAN_INFO`;
CREATE TABLE `SHJT_SAN_INFO` (
  `SANINF000` varchar(255) NOT NULL,
  `SANINF001` varchar(255) DEFAULT NULL,
  `SANINF002` varchar(255) DEFAULT NULL,
  `SANINF003` datetime DEFAULT NULL,
  `SANINF004` datetime DEFAULT NULL,
  `SANINF005` varchar(255) DEFAULT NULL,
  `SANINF006` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`SANINF000`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of SHJT_SAN_INFO
-- ----------------------------
INSERT INTO `SHJT_SAN_INFO` VALUES ('9c7823bc-65a5-4e40-87c4-64ce0a501917', '消息通知：系统正在持续开发中', '1', '2017-10-25 09:59:11', '2017-10-25 16:28:45', '1', 'admin');

-- ----------------------------
-- Table structure for SHJT_SYS_CODER
-- ----------------------------
DROP TABLE IF EXISTS `SHJT_SYS_CODER`;
CREATE TABLE `SHJT_SYS_CODER` (
  `ID` varchar(36) NOT NULL COMMENT '静态配置标识',
  `TAB_NAME` varchar(30) DEFAULT NULL COMMENT '表名',
  `COL_NAME` varchar(20) DEFAULT NULL COMMENT '字段名',
  `COL_DESC` varchar(60) DEFAULT NULL COMMENT '字段描述',
  `VAL` varchar(20) DEFAULT NULL COMMENT '值',
  `VAL_DESC` varchar(30) DEFAULT NULL COMMENT '值简拼',
  `CODE_SPELLING` varchar(30) DEFAULT NULL COMMENT '值描述',
  `STATE` int(11) DEFAULT NULL COMMENT '状态',
  `STATE_CHNTIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of SHJT_SYS_CODER
-- ----------------------------
INSERT INTO `SHJT_SYS_CODER` VALUES ('168', 'CFG_UMS_ROLE_UNIT_OPER', 'PER_LV', '权限等级', '0', '无权限', 'wqx', '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('169', 'CFG_UMS_ROLE_UNIT_OPER', 'PER_LV', '权限等级', '1', '只读', 'ZD', '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('170', 'CFG_UMS_ROLE_UNIT_OPER', 'PER_LV', '权限等级', '2', '可写', 'KX', '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('171', 'CMIS_SEARCH_TRAN', 'VALUE_TYPE', '值类型', '0', '自输入字符串', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('172', 'CMIS_SEARCH_TRAN', 'VALUE_TYPE', '值类型', '1', '自输入数字', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('173', 'CMIS_SEARCH_TRAN', 'VALUE_TYPE', '值类型', '2', '时间表', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('174', 'CMIS_SEARCH_TRAN', 'VALUE_TYPE', '值类型', '3', '代码集', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('175', 'CMIS_SEARCH_CONDITION', 'ROP', '关联符', 'and', '且', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('176', 'CMIS_SEARCH_CONDITION', 'ROP', '关联符', 'or', '或', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('177', 'CMIS_SEARCH_CONDITION', 'COP', '等于', 'eq', '等于', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('178', 'CMIS_SEARCH_CONDITION', 'COP', '标识符', 'ne', '不等于', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('179', 'CMIS_SEARCH_CONDITION', 'COP', '标识符', 'gt', '大于', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('180', 'CMIS_SEARCH_CONDITION', 'COP', '标识符', 'ge', '大于等于', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('181', 'CMIS_SEARCH_CONDITION', 'COP', '标识符', 'lt', '小于', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('182', 'CMIS_SEARCH_CONDITION', 'COP', '标识符', 'le', '小于等于', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('183', 'CMIS_SEARCH_CONDITION', 'COP', '标识符', 'between', '在..之间', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('184', 'CMIS_SEARCH_CONDITION', 'COP', '标识符', 'like', '包含字符', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('185', 'CMIS_SEARCH_CONDITION', 'COP', '标识符', 'notLike', '不包含字符', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('18501', 'CMIS_SEARCH_CONDITION', 'COP', '标识符', 'in', '包含', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('18502', 'CMIS_SEARCH_CONDITION', 'COP', '标识符', 'notIn', '不包含', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('1851', 'CMIS_SEARCH_CONDITION', 'COP', '标识符', 'isNull', '为Null', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('1852', 'CMIS_SEARCH_CONDITION', 'COP', '标识符', 'isNotNull', '不为Null', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('186', 'CMIS_SEARCH_QUERY', 'OWNER_TYPE', '所属类别', '0', '个人', 'GR', '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('187', 'CMIS_SEARCH_QUERY', 'OWNER_TYPE', '所属类别', '1', '公用', 'GY', '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('188', 'CMIS_SEARCH_CONDITION', 'value_type', '值类型', '0', 'VARCHAR', '', '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('189', 'CMIS_SEARCH_CONDITION', 'value_type', '值类型', '1', 'INT', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('190', 'CMIS_SEARCH_CONDITION', 'LBREAKET', '左括号', 'NONE', ' ', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('191', 'CMIS_SEARCH_CONDITION', 'LBREAKET', '左括号', 'SINGLE', '(', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('192', 'CMIS_SEARCH_CONDITION', 'LBREAKET', '左括号', 'DOUBLE', '((', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('193', 'CMIS_SEARCH_CONDITION', 'LBREAKET', '左括号', 'THREE', '(((', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('194', 'CMIS_SEARCH_CONDITION', 'LBREAKET', '左括号', 'FOUR', '((((', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('195', 'CMIS_SEARCH_CONDITION', 'LBREAKET', '左括号', 'FIVE', '(((((', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('196', 'CMIS_SEARCH_CONDITION', 'RBREAKET', '右括号', 'NONE', ' ', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('197', 'CMIS_SEARCH_CONDITION', 'RBREAKET', '右括号', 'SINGLE', ')', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('198', 'CMIS_SEARCH_CONDITION', 'RBREAKET', '右括号', 'DOUBLE', '))', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('199', 'CMIS_SEARCH_CONDITION', 'RBREAKET', '右括号', 'THREE', ')))', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('200', 'CMIS_SEARCH_CONDITION', 'RBREAKET', '右括号', 'FOUR', '))))', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('2002', 'SYS_COLUMN_SHOW', 'type', '类型', 'text', '文本', 'WB', '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('2003', 'SYS_COLUMN_SHOW', 'type', '类型', 'datetemp', '时间控件', 'SJKJ', '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('2004', 'SYS_COLUMN_SHOW', 'type', '类型', 'dataList', '数据集控件', 'DMJKJ', '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('2005', 'SYS_COLUMN_SHOW', 'type', '类型', 'select', 'select单选', 'DX', '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('2006', 'SYS_COLUMN_SHOW', 'type', '类型', 'checkbox', 'checkbox', 'DX', '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('2007', 'SYS_COLUMN_SHOW', 'type', '类型', 'readonly', '只读', 'ZD', '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('2008', 'SYS_COLUMN_SHOW', 'type', '类型', 'textarea', '多行文本框', 'DHWBK', '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('2009', 'SYS_COLUMN_SHOW', 'type', '类型', 'singleimage', '单图片上传', 'SINGLEIMAGE', '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('201', 'CMIS_SEARCH_CONDITION', 'RBREAKET', '右括号', 'FIVE', ')))))', null, '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('2010', 'SYS_COLUMN_SHOW', 'type', '类型', 'radio', '单选框', 'RADIO', '1', null);
INSERT INTO `SHJT_SYS_CODER` VALUES ('2011', 'SYS_COLUMN_SHOW', 'type', '类型', 'multiFile', '多文件上传', 'MULTIFILE', '1', null);

-- ----------------------------
-- Table structure for SHJT_SYS_CONFIG
-- ----------------------------
DROP TABLE IF EXISTS `SHJT_SYS_CONFIG`;
CREATE TABLE `SHJT_SYS_CONFIG` (
  `name` varchar(255) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of SHJT_SYS_CONFIG
-- ----------------------------
INSERT INTO `SHJT_SYS_CONFIG` VALUES ('userGroupRootName', '用户组', '05');
INSERT INTO `SHJT_SYS_CONFIG` VALUES ('accountLockCount', '3', '1');
INSERT INTO `SHJT_SYS_CONFIG` VALUES ('accountLockTime', '5', '10');
INSERT INTO `SHJT_SYS_CONFIG` VALUES ('appAdminUrl', 'http://baidu.com', '11');
INSERT INTO `SHJT_SYS_CONFIG` VALUES ('dynaReportTempDoc', '/resources/temp/dynaReportDoc/', '2');
INSERT INTO `SHJT_SYS_CONFIG` VALUES ('siteUrl', 'http://localhost:8080', '4028c1e85e5b34dc015e5b356e4b0000');
INSERT INTO `SHJT_SYS_CONFIG` VALUES ('resourcePath', 'E:/jtiism_data/', '4028c1e85e5b34dc015e5b356e5d0001');
INSERT INTO `SHJT_SYS_CONFIG` VALUES ('licensePicPath', '/entLicense/', '4028c1e85e5b4bdc015e5b4dcdcb0001');
INSERT INTO `SHJT_SYS_CONFIG` VALUES ('licenseThumbPath', '/thumb/entLicense/', '4028c1e85e5b4bdc015e5b4dcdcb0002');
INSERT INTO `SHJT_SYS_CONFIG` VALUES ('publicPicPath', '/entPublic/', '4028c1e85e5b4bdc015e5b4dcdcb0003');
INSERT INTO `SHJT_SYS_CONFIG` VALUES ('publicThumbPath', '/thumb/entPublic/', '4028c1e85e5b4bdc015e5b4dcdcb0004');
INSERT INTO `SHJT_SYS_CONFIG` VALUES ('dynaImgPath', '/dynaInfoImg/', '4028c1e85e5b4bdc015e5b4dcdcb0005');
INSERT INTO `SHJT_SYS_CONFIG` VALUES ('dynaThumbPath', '/thumb/dynaInfoImg/', '4028c1e85e5b4bdc015e5b4dcdcb0006');
INSERT INTO `SHJT_SYS_CONFIG` VALUES ('originPassword', '123456', '8');

-- ----------------------------
-- Table structure for SYS_COLUMN_SHOW
-- ----------------------------
DROP TABLE IF EXISTS `SYS_COLUMN_SHOW`;
CREATE TABLE `SYS_COLUMN_SHOW` (
  `ID` varchar(255) NOT NULL,
  `isEnabled` tinyint(4) DEFAULT NULL,
  `isRequired` tinyint(4) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `propertyName` varchar(200) DEFAULT NULL,
  `infoSet` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `seq` int(11) DEFAULT NULL,
  `propertyCode` varchar(200) DEFAULT NULL,
  `validateRule` varchar(50) DEFAULT NULL,
  `lengthLimit` varchar(10) DEFAULT NULL,
  `allowBatch` tinyint(4) DEFAULT NULL,
  `propertyType` int(11) DEFAULT NULL,
  `ISGRIDITEM` tinyint(4) DEFAULT NULL,
  `rowHeight` varchar(255) DEFAULT NULL,
  `fileRelPath` varchar(100) DEFAULT NULL,
  `dataFormat` varchar(20) DEFAULT NULL,
  `comment` text,
  `htmlType` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of SYS_COLUMN_SHOW
-- ----------------------------

-- ----------------------------
-- Table structure for SYS_FIELDINFO
-- ----------------------------
DROP TABLE IF EXISTS `SYS_FIELDINFO`;
CREATE TABLE `SYS_FIELDINFO` (
  `ID` varchar(36) NOT NULL,
  `SRCTABLE` varchar(100) DEFAULT NULL,
  `SRCFIELD` varchar(100) NOT NULL,
  `DESFIELD` varchar(100) NOT NULL,
  `CODETABLE` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`,`SRCFIELD`,`DESFIELD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='编码字段定义';

-- ----------------------------
-- Records of SYS_FIELDINFO
-- ----------------------------
INSERT INTO `SYS_FIELDINFO` VALUES ('25894659-7ce6-11e7-93ae-00505690398c', 'SHJT_ENT_INF', 'entInf020', 'entInf020', 'ZD103');
INSERT INTO `SYS_FIELDINFO` VALUES ('258c0ad9-7ce6-11e7-93ae-00505690398c', 'SHJT_ENT_INF', 'entInf021', 'entInf021', 'ZD105');
INSERT INTO `SYS_FIELDINFO` VALUES ('258e8a2f-7ce6-11e7-93ae-00505690398c', 'SHJT_ENT_INF', 'entInf022', 'entInf022', 'ZD104');
INSERT INTO `SYS_FIELDINFO` VALUES ('25911468-7ce6-11e7-93ae-00505690398c', 'SHJT_ENT_INF', 'entInf023', 'entInf023', 'UNIT_INDEX');
INSERT INTO `SYS_FIELDINFO` VALUES ('2593ac71-7ce6-11e7-93ae-00505690398c', 'SHJT_ENT_INF', 'entInf024', 'entInf024', 'ZD107');
INSERT INTO `SYS_FIELDINFO` VALUES ('259631d0-7ce6-11e7-93ae-00505690398c', 'SHJT_ENT_INF', 'entInf025', 'entInf025', 'ZD108');
INSERT INTO `SYS_FIELDINFO` VALUES ('2598be85-7ce6-11e7-93ae-00505690398c', 'SHJT_ENT_INF', 'entInf026', 'entInf026', 'ZD109');
INSERT INTO `SYS_FIELDINFO` VALUES ('259b386f-7ce6-11e7-93ae-00505690398c', 'SHJT_ENT_INF', 'entInf028', 'entInf028', 'ZD110');
INSERT INTO `SYS_FIELDINFO` VALUES ('259dce65-7ce6-11e7-93ae-00505690398c', 'SHJT_ENT_INF', 'entInf029', 'entInf029', 'ZD111');
INSERT INTO `SYS_FIELDINFO` VALUES ('402881e75f044304015f0539891c0000', 'SHJT_CST_INF', 'CSTINF002', 'CODE', 'ZD220');
INSERT INTO `SYS_FIELDINFO` VALUES ('402881e85dedbb16015dedddcb8a0000', 'UNIT_INFO', 'B0120', 'B0120', 'CMIS_DEPARTMENT');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1e85dbb5897015dbc01d1b40000', 'SHJT_ENT_INF', 'ENTINF003', 'entInf003', 'ZD101');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1e85dc106b7015dc113ef940000', 'SHJT_ENT_INF', 'entInf012', 'entInf012', 'ZD112');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1e85dde65fd015dde7b00380000', 'SHJT_ENT_INF', 'entInf041', 'entInf041', 'ZD113');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1e85e09ae2a015e09c614c20000', 'SHJT_ENT_INF', 'entInf043', 'entInf043', 'ZD114');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1e85e3c5ec1015e3c6981770000', 'SHJT_ENT_INF', 'entInf049', 'entInf049', 'ZD115');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1e85e6faf44015e6fca05130006', 'SHJT_POLICY', 'plcy001', 'plcy001', 'ZD219');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1e85f524b56015f524dc9d50001', 'SHJT_POLICY', 'PLCY008', 'PLCY008', 'ZD221');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1ea5de8c494015de8da56c80134', 'SHJT_ENT_CTI', 'ENTCTI004', 'ENTCTI004', 'ZD004');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1ea5de9bf20015de9ca7a4b0000', 'SHJT_ENT_SRI', 'ENTSRI001', 'ENTSRI001', 'ZD212');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1ea5e13a564015e140f850a0007', 'SHJT_ENT_SSM', 'ENTSSM003', 'ENTSSM003', 'ZD214');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1ea5e2dec5b015e2df6264600b1', 'SHJT_ENT_SEM', 'ENTSEM002', 'ENTSEM002', 'ZD215');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1ea5e30d67d015e30df792a0000', 'SHJT_ENT_CTI', 'ENTCTI005', 'ENTCTI005', 'ZD216');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1ea5e5c2762015e5c6837960015', 'SHJT_ENT_HTT', 'ENTHTT005', 'ENTHTT005', 'ZD217');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1ea5e7f5579015e7f61ff5b0000', 'SHJT_ENT_CTI', 'ENTCTI008', 'ENTCTI008', 'ZD217');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1ea5ea3cdda015ea45468450006', 'SHJT_ENT_CTI', 'ENTCTI014', 'ENTCTI014', 'ZD003');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1ea5ea3cdda015ea45647d90007', 'SHJT_ENT_SSM', 'ENTSSM006', 'ENTSSM006', 'ZD003');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1ea5ea3cdda015ea456b2460008', 'SHJT_ENT_SSM', 'ENTSSM007', 'ENTSSM007', 'ZD003');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1ea5ea3cdda015ea456faaa0009', 'SHJT_ENT_SSM', 'ENTSSM009', 'ENTSSM009', 'ZD003');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1ea5eb6b836015eb7f517f90034', 'SHJT_ENT_UIM', 'ENTUIM003', 'ENTUIM003', 'ZD004');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1ea5ebbbc79015ebc2a816800f1', 'SHJT_ENT_SSM', 'ENTSSM016', 'ENTSSM016', 'ZD005');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1ea5f10466c015f106732e70001', 'SHJT_ENT_HTT', 'ENTHTT006', 'ENTHTT006', 'ZD218');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1ea5f3322c4015f335c989c0007', 'SHJT_ENT_PLI', 'ENTPLI004', 'ENTPLI004', 'ZD105');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1ea5f3322c4015f335d05ae0008', 'SHJT_ENT_PLI', 'ENTPLI005', 'ENTPLI005', 'ZD104');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1ee5dc5a557015dc5aa4bb80000', 'UNIT_INFO', 'b0124', 'b0124', 'ZD002');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dc0d171015dc0e22bcb0000', 'SHJT_ENT_INF', 'entInf019', 'entInf019', 'ZD102');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dc5d0fd015dc5db36610000', 'UNIT_INFO', 'b0131', 'b0131', 'ZD001');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd06e4c170000', 'SHJT_ENT_CTI', 'ENTCTI003', 'ENTCTI003', 'ZD202');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd06f86f90001', 'SHJT_ENT_CTI', 'ENTCTI017', 'ENTCTI017', 'ZD201');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd06ffb790002', 'SHJT_ENT_CTI', 'ENTCTI018', 'ENTCTI018', 'ZD201');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd0704cd70003', 'SHJT_ENT_CTI', 'ENTCTI019', 'ENTCTI019', 'ZD201');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd07273b10004', 'SHJT_ENT_RCS', 'ENTRCS004', 'ENTRCS004', 'ZD203');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd0737b030005', 'SHJT_ENT_PBS', 'ENTPBS001', 'ENTPBS001', 'ZD003');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd073e2990006', 'SHJT_ENT_PBS', 'ENTPBS002', 'ENTPBS002', 'ZD003');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd0743b390007', 'SHJT_ENT_PBS', 'ENTPBS003', 'ENTPBS003', 'ZD003');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd0749a320008', 'SHJT_ENT_PBS', 'ENTPBS004', 'ENTPBS004', 'ZD003');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd075c9460009', 'SHJT_ENT_PBS', 'ENTPBS005', 'ENTPBS005', 'ZD204');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd077d8a3000a', 'SHJT_ENT_PBS', 'ENTPBS011', 'ENTPBS011', 'ZD004');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd07918ef000b', 'SHJT_ENT_PER', 'ENTPER007', 'ENTPER007', 'ZD003');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd07c725b000c', 'SHJT_ENT_SSM', 'ENTSSM002', 'ENTSSM002', 'ZD205');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd07dd3ce000e', 'SHJT_ENT_SSM', 'ENTSSM008', 'ENTSSM008', 'ZD003');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd08dfeef0011', 'SHJT_ENT_YOS', 'ENTYOS001', 'ENTYOS001', 'ZD206');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd08ed9520012', 'SHJT_ENT_YOS', 'ENTYOS003', 'ENTYOS003', 'ZD207');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd09018ee0013', 'SHJT_ENT_YOS', 'ENTYOS010', 'ENTYOS010', 'ZD003');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd09769a10014', 'SHJT_ENT_EBS', 'ENTEBS001', 'ENTEBS001', 'ZD211');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd098bc740015', 'SHJT_ENT_EBS', 'ENTEBS004', 'ENTEBS004', 'ZD208');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd098f6620016', 'SHJT_ENT_EBS', 'ENTEBS005', 'ENTEBS005', 'ZD208');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd099e9dc0017', 'SHJT_ENT_EBS', 'ENTEBS007', 'ENTEBS007', 'ZD003');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd09a26750018', 'SHJT_ENT_EBS', 'ENTEBS008', 'ENTEBS008', 'ZD003');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd09ab5ad0019', 'SHJT_ENT_EBS', 'ENTEBS009', 'ENTEBS009', 'ZD208');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd09db0fb001a', 'SHJT_ENT_TPB', 'ENTTPB001', 'ENTTPB001', 'ZD209');
INSERT INTO `SYS_FIELDINFO` VALUES ('4028c1fc5dd00928015dd09e2166001b', 'SHJT_ENT_TPB', 'ENTTPB003', 'ENTTPB003', 'ZD210');

-- ----------------------------
-- Table structure for SYS_OUTPUT_CONFIG
-- ----------------------------
DROP TABLE IF EXISTS `SYS_OUTPUT_CONFIG`;
CREATE TABLE `SYS_OUTPUT_CONFIG` (
  `ID` varchar(36) NOT NULL,
  `COL_FLAG` varchar(20) DEFAULT NULL,
  `COL_NAME` varchar(20) DEFAULT NULL,
  `COL_TITLE` varchar(20) DEFAULT NULL,
  `COL_WIDTH` smallint(6) DEFAULT NULL,
  `FONT_SIZE` smallint(6) DEFAULT NULL,
  `ISUSE` smallint(6) DEFAULT NULL,
  `ROW_HEIGHT` smallint(6) DEFAULT NULL,
  `SORTNO` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of SYS_OUTPUT_CONFIG
-- ----------------------------

-- ----------------------------
-- Table structure for UNIT_HIBER_RELA
-- ----------------------------
DROP TABLE IF EXISTS `UNIT_HIBER_RELA`;
CREATE TABLE `UNIT_HIBER_RELA` (
  `B01_UNIT_LIBRARY_RELA_ID` varchar(36) NOT NULL,
  `ININO` int(11) DEFAULT NULL,
  `isHidden` bit(1) DEFAULT NULL,
  `PUNIT_LEV` int(11) DEFAULT NULL,
  `P_B01_UNIT_LIBRARY_RELA_ID` varchar(36) DEFAULT NULL,
  `PUNIT_ID` varchar(36) DEFAULT NULL,
  `B00` varchar(36) NOT NULL,
  PRIMARY KEY (`B01_UNIT_LIBRARY_RELA_ID`),
  KEY `FK_3cp368crc4mfyhp62ri13svtp` (`P_B01_UNIT_LIBRARY_RELA_ID`),
  KEY `FK_4lo7a576cp3l568ap9o94iovl` (`PUNIT_ID`),
  KEY `FK_oqbyu9vo3g9vwafpn5is0x30f` (`B00`),
  CONSTRAINT `FK_oqbyu9vo3g9vwafpn5is0x30f` FOREIGN KEY (`B00`) REFERENCES `UNIT_INFO` (`B00`),
  CONSTRAINT `FK_3cp368crc4mfyhp62ri13svtp` FOREIGN KEY (`P_B01_UNIT_LIBRARY_RELA_ID`) REFERENCES `UNIT_HIBER_RELA` (`B01_UNIT_LIBRARY_RELA_ID`),
  CONSTRAINT `FK_4lo7a576cp3l568ap9o94iovl` FOREIGN KEY (`PUNIT_ID`) REFERENCES `UNIT_INFO` (`B00`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of UNIT_HIBER_RELA
-- ----------------------------

-- ----------------------------
-- Table structure for UNIT_INDEX
-- ----------------------------
DROP TABLE IF EXISTS `UNIT_INDEX`;
CREATE TABLE `UNIT_INDEX` (
  `CODE` varchar(36) NOT NULL,
  `Attribute` int(11) DEFAULT NULL,
  `CODE_ANAME` varchar(255) DEFAULT NULL,
  `CODE_ABR1` varchar(60) DEFAULT NULL,
  `CODE_ABR2` varchar(60) DEFAULT NULL,
  `CODE_ASSIST` varchar(255) DEFAULT NULL,
  `CODE_LEAF` varchar(255) DEFAULT NULL,
  `CODE_LEVEL` double DEFAULT NULL,
  `CODE_NAME` varchar(80) DEFAULT NULL,
  `CODE_SPELLING` varchar(100) DEFAULT NULL,
  `DmGrp` varchar(255) DEFAULT NULL,
  `DmLevCod` varchar(255) DEFAULT NULL,
  `ININO` int(11) DEFAULT NULL,
  `INVALID` varchar(255) DEFAULT NULL,
  `START_DATE` varchar(255) DEFAULT NULL,
  `STOP_DATE` varchar(255) DEFAULT NULL,
  `SUP_CODE` varchar(8) DEFAULT NULL,
  `UNIT_CODE` varchar(255) DEFAULT NULL,
  `YesPrv` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of UNIT_INDEX
-- ----------------------------

-- ----------------------------
-- Table structure for UNIT_INFO
-- ----------------------------
DROP TABLE IF EXISTS `UNIT_INFO`;
CREATE TABLE `UNIT_INFO` (
  `B00` varchar(36) NOT NULL,
  `B0101` varchar(120) DEFAULT NULL,
  `B0104` varchar(20) DEFAULT NULL,
  `B0111` varchar(150) DEFAULT NULL,
  `B0120` varchar(255) DEFAULT NULL,
  `B0120N` varchar(255) DEFAULT NULL,
  `B0124` varchar(2) DEFAULT NULL,
  `B0124N` varchar(100) DEFAULT NULL,
  `B0127` varchar(4) DEFAULT NULL,
  `B0127N` varchar(100) DEFAULT NULL,
  `B0131` varchar(3) DEFAULT NULL,
  `B0131N` varchar(100) DEFAULT NULL,
  `UNITTYPE` int(11) DEFAULT NULL,
  PRIMARY KEY (`B00`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of UNIT_INFO
-- ----------------------------

-- ----------------------------
-- Table structure for USER_DEPT_RELA
-- ----------------------------
DROP TABLE IF EXISTS `USER_DEPT_RELA`;
CREATE TABLE `USER_DEPT_RELA` (
  `USER_DEPT_RELA_ID` varchar(255) NOT NULL,
  `USER_ID` int(11) DEFAULT NULL,
  `DEPT_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`USER_DEPT_RELA_ID`),
  KEY `FK_25n7l84x84sune5pkwm0nhnhm` (`USER_ID`),
  KEY `FK_8d7fcyig515cumk9ruj4kfbp2` (`DEPT_ID`),
  CONSTRAINT `FK_8d7fcyig515cumk9ruj4kfbp2` FOREIGN KEY (`DEPT_ID`) REFERENCES `CMIS_DEPARTMENT` (`CODE`),
  CONSTRAINT `FK_25n7l84x84sune5pkwm0nhnhm` FOREIGN KEY (`USER_ID`) REFERENCES `CFG_UMS_USER` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of USER_DEPT_RELA
-- ----------------------------
