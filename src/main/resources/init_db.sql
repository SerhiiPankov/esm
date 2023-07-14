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

DROP TABLE IF EXISTS `GiftCertificate`;
CREATE TABLE `GiftCertificate` (
                       `id` bigint(20) NOT NULL AUTO_INCREMENT,
                       `name` varchar(64) NOT NULL,
                       `description` varchar(255) NOT NULL,
                       `price` decimal(10,2) NOT NULL,
                       `duration` int(11) NOT NULL,
                       `createDate` datetime NOT NULL,
                       `lastUpdateDate` datetime NOT NULL,
                       PRIMARY KEY (`id`),
                       UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB CHARSET=utf8;


CREATE TABLE `Tag_GiftCertificate` (
                                       `gift_certificate_id` bigint(20) NOT NULL,
                                       `tag_id` bigint(20) NOT NULL,
                                       UNIQUE KEY `gift_certificate_id` (`gift_certificate_id`,`tag_id`),
                                       KEY `fk_tag_id` (`tag_id`),
                                       CONSTRAINT `fk_gift_certificate_id` FOREIGN KEY (`gift_certificate_id`) REFERENCES `GiftCertificate` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
                                       CONSTRAINT `fk_tag_id` FOREIGN KEY (`tag_id`) REFERENCES `Tag` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8
