# 用户表

CREATE TABLE `sys_user` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户',
	`account` VARCHAR(32) NOT NULL COMMENT '用户账号',
	`avatar_url` VARCHAR(128) DEFAULT NULL COMMENT '头像链接',
	`nick_name` VARCHAR(32) DEFAULT NULL COMMENT '用户昵称',
	`mobile` VARCHAR(11) DEFAULT NULL COMMENT '用户手机',
	`email` VARCHAR(128) DEFAULT NULL COMMENT '用户邮箱',
	`password` VARCHAR(128) NOT NULL COMMENT '用户密码',
	`super_admin` INT(1) NOT NULL DEFAULT 0 COMMENT '是否为超级管理员（1：是，0：否）',
	`create_user_id` BIGINT(20) NOT NULL COMMENT '创建人ID',
	`create_time` datetime NOT NULL COMMENT '创建时间',
	`modify_user_id` BIGINT(20) DEFAULT NULL COMMENT '修改人ID',
	`modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
	`state` INT(1) NOT NULL DEFAULT 1 COMMENT '状态（1：使用中，2：暂停使用，0：无效）',
	PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户表';