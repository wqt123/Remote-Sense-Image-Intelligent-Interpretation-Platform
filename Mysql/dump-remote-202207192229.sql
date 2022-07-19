-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: 112.124.12.171    Database: remote
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `history`
--

DROP TABLE IF EXISTS `history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `history` (
  `type` varchar(20) NOT NULL,
  `status` varchar(20) NOT NULL,
  `title` varchar(100) NOT NULL,
  `size` varchar(20) DEFAULT NULL,
  `his_id` varchar(20) NOT NULL,
  `create_time` datetime(6) DEFAULT NULL,
  `is_remove` bit(1) NOT NULL,
  `origin_name1` varchar(100) DEFAULT NULL,
  `origin_name2` varchar(100) DEFAULT NULL,
  `result_name` varchar(100) DEFAULT NULL,
  `user_id` varchar(20) NOT NULL,
  PRIMARY KEY (`his_id`),
  KEY `history__index` (`is_remove`),
  KEY `history_user__fk` (`user_id`),
  KEY `history_isRemove_IDX` (`is_remove`),
  CONSTRAINT `history_user__fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `history`
--

LOCK TABLES `history` WRITE;
/*!40000 ALTER TABLE `history` DISABLE KEYS */;
INSERT INTO `history` VALUES ('地物分类','uploadSuccess','test','4M','H000001','2022-07-06 16:31:54.373358',_binary '','test','',NULL,'U000001'),('目标检测','runEnd','test','4M','H000002','2022-07-06 16:44:40.627889',_binary '','target_detection.zip','','target_detection_result.zip','U000001'),('变化检测','runEnd','test','4M','H000003','2022-07-06 16:45:21.270689',_binary '','change_detector1.zip','change_detector2.zip','change_detector1_result.zip','U000001'),('目标检测','uploadSuccess','test','1.66M','H000004','2022-07-08 13:43:21.670557',_binary '','1657258998419.zip','',NULL,'U000001'),('目标检测','runEnd','test','1.66M','H000006','2022-07-08 13:58:17.037881',_binary '','1657259893947.zip','','1657259893947_result.zip','U000001'),('目标检测','runEnd','test','1.66M','H000007','2022-07-08 13:58:42.014113',_binary '','1657259919103.zip','','1657259919103_result.zip','U000001'),('目标检测','uploadSuccess','test','1.66M','H000008','2022-07-08 14:17:04.983664',_binary '','1657261022569.zip','',NULL,'U000001'),('目标检测','runEnd','test','1.66M','H000009','2022-07-08 14:18:28.816585',_binary '','1657261105960.zip','','1657261105960_result.zip','U000001'),('目标检测','uploadSuccess','test','1.66M','H000010','2022-07-08 14:25:01.336765',_binary '','1657261498518.zip','',NULL,'U000001'),('目标检测','runEnd','test','0.51M','H000011','2022-07-08 11:03:31.526117',_binary '','1657249410635.zip','','1657249410635_result.zip','U000001'),('目标检测','runEnd','new Test','1.66M','H000012','2022-07-19 11:29:12.332181',_binary '\0','1658201349807.zip','','1658201349807_result.zip','U000001'),('目标检测','runEnd','my Test','1.66M','H000013','2022-07-19 11:29:27.678679',_binary '\0','1658201365343.zip','','1658201365343_result.zip','U000001'),('目标检测','runEnd','target_detection_test','1.66M','H000014','2022-07-19 20:20:13.551493',_binary '\0','1658233210262.zip','','1658233210262_result.zip','U000001'),('目标检测','runEnd','target_detection_test','1.66M','H000015','2022-07-19 20:21:48.164121',_binary '\0','1658233305260.zip','','1658233305260_result.zip','U000001'),('目标检测','runEnd','target_detection_test','1.66M','H000016','2022-07-19 20:25:20.797000',_binary '\0','1658233508478.zip','','1658233508478_result.zip','U000001'),('目标检测','runEnd','test','1.66M','H000017','2022-07-19 20:40:54.728578',_binary '\0','1658234421638.zip','','1658234421638_result.zip','U000001'),('目标检测','runEnd','test','2.3M','H000018','2022-07-19 20:59:31.558718',_binary '\0','1658235551393.zip','','1658235551393_result.zip','U000001'),('目标提取','runEnd','test','3.91M','H000019','2022-07-19 21:06:02.431422',_binary '\0','1658235895147.zip','','1658235895147_result.zip','U000001'),('目标提取','runEnd','test','3.91M','H000020','2022-07-19 21:07:23.311068',_binary '\0','1658236038879.zip','','1658236038879_result.zip','U000001'),('目标提取','runEnd','test','3.91M','H000021','2022-07-19 21:10:29.209850',_binary '\0','1658236224482.zip','','1658236224482_result.zip','U000001'),('目标提取','runEnd','test','3.91M','H000022','2022-07-19 21:10:42.262830',_binary '\0','1658236236160.zip','','1658236236160_result.zip','U000001'),('变化检测','runEnd','test','4.49M','H000023','2022-07-19 21:25:31.701791',_binary '\0','1658237128335.zip','1658237128335.zip','1658237128335_result.zip','U000001'),('变化检测','runEnd','test','4.49M','H000024','2022-07-19 21:27:04.405370',_binary '\0','1658237222622.zip','1658237222622.zip','1658237222622_result.zip','U000001'),('变化检测','runEnd','test','4.49M','H000025','2022-07-19 21:27:04.799142',_binary '\0','1658237222623.zip','1658237222623.zip','1658237222623_result.zip','U000001'),('变化检测','runEnd','test','4.49M','H000026','2022-07-19 21:27:51.007021',_binary '\0','1658237269218.zip','1658237269218.zip','1658237269218_result.zip','U000001'),('变化检测','runEnd','test','4.49M','H000027','2022-07-19 21:27:51.405240',_binary '\0','1658237269219.zip','1658237269219.zip','1658237269219_result.zip','U000001'),('变化检测','runEnd','test','4.49M','H000028','2022-07-19 21:29:42.996312',_binary '\0','1658237381463.zip','1658237381463.zip','1658237381463_result.zip','U000001'),('变化检测','runEnd','test','4.48M','H000029','2022-07-19 21:57:37.794366',_binary '\0','1658239023734.zip','1658239023734.zip','1658239023734_result.zip','U000001'),('变化检测','isRunning','test','4.48M','H000030','2022-07-19 22:02:23.424767',_binary '\0','1658239336740.zip','1658239336740.zip',NULL,'U000001'),('变化检测','uploadSuccess','test','4.48M','H000031','2022-07-19 22:04:05.280858',_binary '\0','1658239439788.zip','1658239439788.zip',NULL,'U000001'),('变化检测','runEnd','test','4.48M','H000032','2022-07-19 22:04:43.069351',_binary '\0','1658239477105.zip','1658239477105.zip','1658239477105_result.zip','U000001'),('地物分类','runEnd','test','0.02M','H000033','2022-07-19 22:08:03.960755',_binary '\0','1658239681765.zip','','1658239681765_result.zip','U000001'),('变化检测','uploadSuccess','test','4.48M','H000034','2022-07-19 22:12:03.191279',_binary '\0','1658239914590.zip','1658239914591.zip',NULL,'U000001'),('变化检测','uploadSuccess','test','4.48M','H000035','2022-07-19 22:13:04.450539',_binary '\0','1658239972272.zip','1658239972273.zip',NULL,'U000001'),('变化检测','runEnd','test','4.48M','H000036','2022-07-19 22:16:32.746468',_binary '\0','1658240186621.zip','1658240186622.zip','1658240186621_result.zip','U000001');
/*!40000 ALTER TABLE `history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `name` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `salt` varchar(50) NOT NULL,
  `user_id` varchar(20) NOT NULL,
  UNIQUE KEY `user_pk` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('CourageJ','ab536e1ccb2f68063661bb8e37d92e18','1729145790@qq.com','xcs0+FJs8sVzYBJWXHSxQw==','U000001');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'remote'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-07-19 22:29:37
