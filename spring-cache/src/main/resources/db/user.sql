CREATE TABLE user (
  `id`          BIGINT(20) UNSIGNED  PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  `name`        CHAR(20) NOT NULL DEFAULT '' COMMENT '用户名',
  `account`     CHAR(20) NOT NULL DEFAULT '' COMMENT '账号',
  `password`    CHAR(20) NOT NULL DEFAULT '' COMMENT '密码',
  `mobile`      CHAR(11) NOT NULL DEFAULT '' COMMENT '手机号',
  `status`      TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '状态',
  `create_time` BIGINT(20) UNSIGNED NOT NULL COMMENT '创建时间',
  `update_time` BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '更新时间',
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户主表';