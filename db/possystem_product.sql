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
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `description` text,
  `name` varchar(255) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `price` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `thumbnail` varchar(255) DEFAULT NULL,
  `unit` int DEFAULT NULL,
  `version` bigint DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  `supplier_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1mtsbur82frn64de7balymq9s` (`category_id`),
  KEY `FK2kxvbr72tmtscjvyp9yqb12by` (`supplier_id`)
) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'admin','2021-09-22 11:13:38','admin','2021-09-30 15:42:42',NULL,'cà phê',NULL,6000,94,'cpcark_-1252748241_3_2.jpg',1,19,3,2),(8,'admin','2021-09-22 16:17:38','admin','2021-09-30 15:42:42',NULL,'Rau cải',NULL,2000,83,'jhkeqh_-1647738193_1_3.jpg',0,14,1,3),(9,'admin','2021-09-27 11:41:19','admin','2021-09-30 15:42:42',NULL,'Sữa',NULL,8000,85,'sbedhc_325349_3_1.jpg',1,7,3,1),(10,'admin','2021-09-28 16:42:58','admin','2021-10-01 12:10:51',NULL,'Thịt',NULL,5000,92,'nppcir_2846877_4_3.jpg',0,6,4,3),(11,'admin','2021-09-28 16:43:23','admin','2021-09-30 13:32:58',NULL,'Rượu',NULL,20000,94,'dhlnts_3103248_3_3.png',1,4,3,3),(12,'admin','2021-09-28 16:44:00','admin','2021-10-01 12:10:51',NULL,'Trứng',NULL,5000,92,'bfoixx_88579844_4_3.jpg',1,3,4,3),(13,'admin','2021-09-28 16:44:23','admin','2021-09-30 08:47:49',NULL,'Cốc',NULL,5000,96,'sqgdlq_309045_5_3.jpg',1,3,5,3),(14,'admin','2021-09-28 16:44:44','admin','2021-09-29 15:39:52',NULL,'Gạo',NULL,5000,94,'yrxbyh_311413_4_3.jpg',0,1,4,3);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
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
