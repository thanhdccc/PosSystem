-- MySQL dump 10.13  Distrib 8.0.26, for Win64 (x86_64)
--
-- Host: localhost    Database: possystem
-- ------------------------------------------------------
-- Server version	8.0.26

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `order_detail`
--

DROP TABLE IF EXISTS `order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `amount` int NOT NULL,
  `price` int NOT NULL,
  `quantity` int NOT NULL,
  `thumbnail` varchar(255) DEFAULT NULL,
  `unit` int NOT NULL,
  `order_id` int NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ORDER_DETAIL_ORD_FK` (`order_id`),
  KEY `ORDER_DETAIL_PROD_FK` (`product_id`)
) ENGINE=MyISAM AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_detail`
--

LOCK TABLES `order_detail` WRITE;
/*!40000 ALTER TABLE `order_detail` DISABLE KEYS */;
INSERT INTO `order_detail` VALUES (16,'admin','2021-09-27 13:59:10','admin','2021-09-27 13:59:10',4000,2000,2,'jhkeqh_-1647738193_1_3.jpg',0,8,8),(15,'admin','2021-09-27 13:57:17','admin','2021-09-27 13:57:17',16000,8000,2,'sbedhc_325349_3_1.jpg',1,7,9),(8,'admin','2021-09-24 10:11:21','admin','2021-09-24 10:11:21',54000,6000,9,'cpcark_-1252748241_3_2.jpg',1,5,1),(9,'user123456','2021-09-24 13:48:35','user123456','2021-09-24 13:48:35',24000,6000,4,'cpcark_-1252748241_3_2.jpg',1,6,1),(10,'user123456','2021-09-24 13:48:35','user123456','2021-09-24 13:48:35',2000,2000,1,'jhkeqh_-1647738193_1_3.jpg',0,6,8),(11,'admin','2021-09-24 13:48:35','admin','2021-09-24 13:48:35',18000,6000,3,'cpcark_-1252748241_3_2.jpg',1,7,1),(12,'admin','2021-09-24 13:48:35','admin','2021-09-24 13:48:35',8000,2000,4,'jhkeqh_-1647738193_1_3.jpg',0,7,8),(13,'admin','2021-09-27 11:01:32','admin','2021-09-27 12:34:54',24000,6000,4,'cpcark_-1252748241_3_2.jpg',1,4,1),(14,'admin','2021-09-27 13:15:46','admin','2021-09-27 13:15:46',24000,8000,3,'sbedhc_325349_3_1.jpg',1,4,9),(17,'admin','2021-09-27 13:59:10','admin','2021-09-27 13:59:10',40000,8000,5,'sbedhc_325349_3_1.jpg',1,8,9),(18,'admin','2021-09-29 14:20:40','admin','2021-09-29 14:20:40',18000,6000,3,'cpcark_-1252748241_3_2.jpg',1,9,1),(19,'admin','2021-09-29 14:20:40','admin','2021-09-29 14:20:40',4000,2000,2,'jhkeqh_-1647738193_1_3.jpg',0,9,8),(20,'admin','2021-09-29 14:20:40','admin','2021-09-29 14:20:40',16000,8000,2,'sbedhc_325349_3_1.jpg',1,9,9),(21,'admin','2021-09-29 15:16:57','admin','2021-09-29 15:16:57',60000,20000,3,'dhlnts_3103248_3_3.png',1,5,11),(22,'admin','2021-09-29 15:19:04','admin','2021-09-29 15:19:04',10000,5000,2,'nppcir_2846877_4_3.jpg',0,8,10),(23,'admin','2021-09-29 15:20:23','admin','2021-09-29 15:20:37',5000,5000,1,'sqgdlq_309045_5_3.jpg',1,8,13),(24,'admin','2021-09-29 15:39:52','admin','2021-09-29 15:39:52',25000,5000,5,'bfoixx_88579844_4_3.jpg',1,10,12),(25,'admin','2021-09-29 15:39:52','admin','2021-09-29 15:39:52',30000,5000,6,'yrxbyh_311413_4_3.jpg',0,10,14),(26,'admin','2021-09-30 08:47:49','admin','2021-09-30 08:47:49',6000,6000,1,'cpcark_-1252748241_3_2.jpg',1,11,1),(27,'admin','2021-09-30 08:47:49','admin','2021-09-30 08:47:49',6000,2000,3,'jhkeqh_-1647738193_1_3.jpg',0,11,8),(28,'admin','2021-09-30 08:47:49','admin','2021-09-30 08:47:49',15000,5000,3,'sqgdlq_309045_5_3.jpg',1,11,13),(29,'admin','2021-09-30 08:49:08','admin','2021-09-30 08:49:08',6000,6000,1,'cpcark_-1252748241_3_2.jpg',1,12,1),(30,'admin','2021-09-30 08:49:08','admin','2021-09-30 08:50:27',8000,2000,4,'jhkeqh_-1647738193_1_3.jpg',0,12,8),(31,'admin','2021-09-30 08:49:08','admin','2021-09-30 08:49:08',5000,5000,1,'nppcir_2846877_4_3.jpg',0,12,10),(32,'admin','2021-09-30 08:49:08','admin','2021-09-30 08:49:08',20000,20000,1,'dhlnts_3103248_3_3.png',1,12,11),(33,'admin','2021-09-30 13:32:05','admin','2021-09-30 13:32:05',20000,20000,1,'dhlnts_3103248_3_3.png',1,13,11),(34,'admin','2021-09-30 13:32:05','admin','2021-09-30 13:32:05',5000,5000,1,'nppcir_2846877_4_3.jpg',0,13,10),(35,'admin','2021-09-30 13:32:05','admin','2021-09-30 13:32:05',8000,8000,1,'sbedhc_325349_3_1.jpg',1,13,9),(36,'admin','2021-09-30 13:32:58','admin','2021-09-30 13:32:58',5000,5000,1,'bfoixx_88579844_4_3.jpg',1,14,12),(37,'admin','2021-09-30 13:32:58','admin','2021-09-30 13:32:58',20000,20000,1,'dhlnts_3103248_3_3.png',1,14,11),(38,'admin','2021-09-30 13:32:58','admin','2021-09-30 13:32:58',5000,5000,1,'nppcir_2846877_4_3.jpg',0,14,10),(39,'admin','2021-09-30 13:32:58','admin','2021-09-30 13:32:58',8000,8000,1,'sbedhc_325349_3_1.jpg',1,14,9),(40,'admin','2021-09-30 13:32:58','admin','2021-09-30 13:32:58',6000,6000,1,'cpcark_-1252748241_3_2.jpg',1,14,1),(41,'admin','2021-09-30 15:42:42','admin','2021-09-30 15:42:42',6000,6000,1,'cpcark_-1252748241_3_2.jpg',1,15,1),(42,'admin','2021-09-30 15:42:42','admin','2021-09-30 15:42:42',2000,2000,1,'jhkeqh_-1647738193_1_3.jpg',0,15,8),(43,'admin','2021-09-30 15:42:42','admin','2021-09-30 15:42:42',8000,8000,1,'sbedhc_325349_3_1.jpg',1,15,9),(44,'admin','2021-09-30 15:42:42','admin','2021-09-30 15:42:42',5000,5000,1,'nppcir_2846877_4_3.jpg',0,15,10),(45,'admin','2021-10-01 12:10:51','admin','2021-10-01 12:10:51',10000,5000,2,'nppcir_2846877_4_3.jpg',0,16,10),(46,'admin','2021-10-01 12:10:51','admin','2021-10-01 12:10:51',10000,5000,2,'bfoixx_88579844_4_3.jpg',1,16,12);
/*!40000 ALTER TABLE `order_detail` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-10-05 17:27:36
