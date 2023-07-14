CREATE SCHEMA IF NOT EXISTS `esm` DEFAULT CHARACTER SET utf8;
USE `esm`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `Tag`;
CREATE TABLE `Tag` (
                       `id` bigint(20) NOT NULL AUTO_INCREMENT,
                       `name` varchar(255) NOT NULL,
                       PRIMARY KEY (`id`),
                       UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB CHARSET=utf8;

