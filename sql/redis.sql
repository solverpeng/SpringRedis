/*
SQLyog Ultimate v11.25 (64 bit)
MySQL - 5.6.14-log : Database - redis
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`redis` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `redis`;

/*Table structure for table `t_cache_config` */

DROP TABLE IF EXISTS `t_cache_config`;

CREATE TABLE `t_cache_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cache_key` varchar(100) DEFAULT NULL,
  `cache_name` varchar(100) DEFAULT NULL,
  `store_type` varchar(10) DEFAULT NULL,
  `sql_text` varchar(4000) DEFAULT NULL,
  `remark` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `t_cache_config` */

insert  into `t_cache_config`(`id`,`cache_key`,`cache_name`,`store_type`,`sql_text`,`remark`) values (1,'T_USER','用户信息','Object','select id, user_name from t_user where id = ?','用户缓存sql');

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `t_user` */

insert  into `t_user`(`id`,`user_name`) values (1,'tom'),(2,'jerry'),(3,'lily'),(4,'lucy');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
