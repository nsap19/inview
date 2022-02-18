CREATE DATABASE  IF NOT EXISTS `inview` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `inview`;
-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: 3.35.231.64    Database: inview
-- ------------------------------------------------------
-- Server version	5.7.37

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
-- Table structure for table `archive`
--

DROP TABLE IF EXISTS `archive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `archive` (
  `archiveId` int(11) NOT NULL AUTO_INCREMENT,
  `archiveName` varchar(200) NOT NULL,
  `archiveType` int(11) NOT NULL,
  `path` varchar(350) NOT NULL,
  `meetingId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  PRIMARY KEY (`archiveId`),
  KEY `archive_userid_fk_idx` (`userId`),
  KEY `archive_meetingid_fk_idx` (`meetingId`),
  CONSTRAINT `archive_meetingid_fk` FOREIGN KEY (`meetingId`) REFERENCES `meeting` (`meetingId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `archive_userid_fk` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `archive`
--

LOCK TABLES `archive` WRITE;
/*!40000 ALTER TABLE `archive` DISABLE KEYS */;
INSERT INTO `archive` VALUES (1,'ssafy1_모두.txt',4,'/files:6/chat:ssafy1_모두.txt',6,1),(2,'ssafy1_모두.txt',4,'/files:7/chat:ssafy1_모두.txt',7,1),(3,'ssafy2_모두.txt',4,'/files:6/chat:ssafy2_모두.txt',6,2),(4,'ssafy3_모두.txt',4,'/files:8/chat:ssafy3_모두.txt',8,3),(5,'ssafy3_모두.txt',4,'/files:9/chat:ssafy3_모두.txt',9,3),(6,'ssafy4_모두.txt',4,'/files:8/chat:ssafy4_모두.txt',8,4),(9,'ssafy4_모두.txt',4,'/files:9/chat:ssafy4_모두.txt',9,4);
/*!40000 ALTER TABLE `archive` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company` (
  `companyId` int(11) NOT NULL AUTO_INCREMENT,
  `companyName` varchar(45) NOT NULL,
  `count` int(11) DEFAULT '0',
  PRIMARY KEY (`companyId`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company`
--

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
INSERT INTO `company` VALUES (1,'네이버',0),(2,'카카오',0),(3,'라인',0),(4,'쿠팡',0),(5,'배달의민족',0),(6,'삼성',0),(7,'현대자동차',0),(8,'SK',0),(9,'LG',0),(10,'롯데',0),(11,'포스코',0),(12,'한화',0),(13,'GS',0),(14,'농협',0),(15,'신세계',0),(16,'KT',0),(17,'한진',0),(18,'CJ',0);
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evaluation`
--

DROP TABLE IF EXISTS `evaluation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluation` (
  `evaluationId` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(200) NOT NULL,
  PRIMARY KEY (`evaluationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluation`
--

LOCK TABLES `evaluation` WRITE;
/*!40000 ALTER TABLE `evaluation` DISABLE KEYS */;
/*!40000 ALTER TABLE `evaluation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `industry`
--

DROP TABLE IF EXISTS `industry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `industry` (
  `industryId` int(11) NOT NULL AUTO_INCREMENT,
  `industryName` varchar(100) NOT NULL,
  `count` int(11) DEFAULT '0',
  PRIMARY KEY (`industryId`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `industry`
--

LOCK TABLES `industry` WRITE;
/*!40000 ALTER TABLE `industry` DISABLE KEYS */;
INSERT INTO `industry` VALUES (1,'경영',0),(2,'마켓팅',0),(3,'영업',0),(4,'IT',0),(5,'생산',0),(6,'연구개발',0),(7,'디자인',0),(8,'서비스',0);
/*!40000 ALTER TABLE `industry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meeting`
--

DROP TABLE IF EXISTS `meeting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meeting` (
  `meetingId` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `startTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `endTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `password` varchar(100) DEFAULT NULL,
  `userLimit` int(11) DEFAULT '6',
  `hostId` int(11) NOT NULL,
  `industryId` int(11) NOT NULL,
  `url` varchar(200) DEFAULT NULL,
  `closeTime` datetime DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`meetingId`),
  KEY `meeting_hostid_fk_idx` (`hostId`),
  KEY `meeting_industryid_fk_idx` (`industryId`),
  CONSTRAINT `meeting_hostid_fk` FOREIGN KEY (`hostId`) REFERENCES `user` (`userId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `meeting_industryid_fk` FOREIGN KEY (`industryId`) REFERENCES `industry` (`industryId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meeting`
--

LOCK TABLES `meeting` WRITE;
/*!40000 ALTER TABLE `meeting` DISABLE KEYS */;
INSERT INTO `meeting` VALUES (1,'방 제목1','2022-02-01 11:15:06','2022-02-07 13:15:06','1111',5,1,4,'b3452cbd-0f01-47b7-85cd-a871784e882e',NULL,0),(4,'방 제목4','2022-02-04 11:15:06','2022-02-07 13:15:06',NULL,5,4,2,'cdbabffb-b70d-4ec7-853a-6902945ec618',NULL,0),(5,'방 제목5','2022-02-05 11:15:06','2022-02-07 13:15:06',NULL,5,4,7,'2f3481c6-c087-4b28-add3-f7fc60fda028',NULL,0),(6,'SSAFY 면접 준비방1','2022-02-23 00:00:00','2022-02-26 00:00:00',NULL,3,1,4,'0212d13b-beeb-4340-8db9-f1aca34f41a5',NULL,0),(7,'신세계 마켓팅 신입 면접준비','2022-02-22 00:00:00','2022-02-25 00:00:00',NULL,6,1,2,'ff5eb0ea-3a49-43e0-9bdc-5172e1146c55',NULL,0),(8,'삼성전자 상반기 면접','2022-02-23 00:00:00','2022-02-26 00:00:00',NULL,6,3,4,'d7943e73-2619-4f73-bba2-d02b098934d1',NULL,0),(9,'롯데 경영 경력직 면접 준비','2022-02-24 00:00:00','2022-02-26 00:00:00',NULL,6,3,1,'1cba3311-cabd-4f8e-8447-4aaf5bbb392c',NULL,0);
/*!40000 ALTER TABLE `meeting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meetingcompany`
--

DROP TABLE IF EXISTS `meetingcompany`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meetingcompany` (
  `meetingCompanyId` int(11) NOT NULL AUTO_INCREMENT,
  `meetingId` int(11) NOT NULL,
  `companyId` int(11) NOT NULL,
  PRIMARY KEY (`meetingCompanyId`),
  KEY `mc_meetingid_fk_idx` (`meetingId`),
  KEY `mc_companyid_fk_idx` (`companyId`),
  CONSTRAINT `mc_companyid_fk` FOREIGN KEY (`companyId`) REFERENCES `company` (`companyId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `mc_meetingid_fk` FOREIGN KEY (`meetingId`) REFERENCES `meeting` (`meetingId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meetingcompany`
--

LOCK TABLES `meetingcompany` WRITE;
/*!40000 ALTER TABLE `meetingcompany` DISABLE KEYS */;
INSERT INTO `meetingcompany` VALUES (10,6,6),(11,7,15),(12,8,6);
/*!40000 ALTER TABLE `meetingcompany` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participant`
--

DROP TABLE IF EXISTS `participant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `participant` (
  `participantId` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `meetingId` int(11) NOT NULL,
  `forcedExit` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`participantId`),
  KEY `participant_userid_fk_idx` (`userId`),
  KEY `participant_meetingid_fk_idx` (`meetingId`),
  CONSTRAINT `participant_meetingid_fk` FOREIGN KEY (`meetingId`) REFERENCES `meeting` (`meetingId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `participant_userid_fk` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=387 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participant`
--

LOCK TABLES `participant` WRITE;
/*!40000 ALTER TABLE `participant` DISABLE KEYS */;
INSERT INTO `participant` VALUES (2,1,4,0),(3,2,1,0),(4,2,4,0),(6,3,4,0),(10,5,4,0),(18,7,9,0),(19,7,10,0),(20,1,9,0),(21,1,10,0),(22,1,11,0),(23,6,12,0),(24,7,13,0),(25,7,14,0),(26,7,15,0),(29,1,15,0),(30,1,14,0),(31,1,13,0),(32,1,12,0),(33,7,16,0),(34,1,16,0),(35,7,17,0),(37,1,17,0),(38,2,17,0),(39,8,18,0),(40,9,18,0),(41,4,17,0),(43,4,18,0),(47,10,20,0),(49,10,21,0),(50,2,16,0),(51,4,16,0),(56,4,21,0),(57,2,21,0),(58,2,20,0),(60,4,20,0),(62,2,15,0),(92,2,23,0),(96,3,27,0),(97,2,27,0),(98,8,27,0),(125,8,43,0),(126,12,43,0),(127,12,44,0),(128,12,45,0),(129,12,46,0),(130,8,47,0),(131,12,47,0),(132,1,47,0),(133,1,48,0),(134,2,45,0),(135,3,45,0),(136,1,45,0),(137,1,44,0),(138,3,44,0),(139,10,49,0),(140,10,50,0),(141,8,50,0),(142,8,51,0),(143,8,52,0),(144,12,52,0),(151,12,56,0),(152,8,56,1),(155,8,58,0),(156,12,58,0),(157,12,59,0),(158,8,59,0),(160,8,61,0),(161,8,62,0),(162,12,62,0),(163,8,63,0),(164,12,63,0),(165,12,64,0),(166,8,64,0),(167,8,65,0),(168,12,65,0),(169,12,67,0),(171,8,67,0),(172,8,68,0),(173,12,68,0),(174,8,69,0),(175,12,69,0),(176,8,70,0),(177,1,50,0),(178,13,71,0),(179,4,70,0),(180,5,70,0),(181,5,56,0),(182,3,70,0),(183,2,70,0),(184,13,56,0),(185,13,72,0),(186,13,73,0),(187,3,56,0),(188,2,56,0),(189,13,74,0),(190,8,75,0),(191,13,52,0),(192,10,52,0),(193,12,76,0),(194,8,76,0),(197,16,52,0),(198,16,51,0),(199,16,46,0),(200,8,77,0),(201,8,46,0),(203,15,79,0),(204,3,80,0),(205,15,77,0),(206,2,80,0),(207,15,81,0),(209,5,83,0),(210,2,83,0),(211,14,81,0),(212,3,84,0),(213,2,84,0),(214,18,46,0),(215,8,85,0),(216,8,86,0),(217,8,87,0),(218,8,88,0),(219,13,89,0),(220,13,90,0),(221,13,91,0),(223,13,93,0),(224,13,94,0),(227,13,95,0),(228,13,96,0),(229,8,97,0),(230,4,97,0),(231,2,97,0),(232,3,95,0),(233,2,95,0),(234,2,94,0),(235,3,94,0),(236,8,98,0),(237,4,98,0),(238,8,99,0),(239,4,99,0),(240,3,77,0),(241,2,44,0),(242,12,100,0),(244,8,100,0),(245,2,100,0),(246,4,100,0),(247,2,43,0),(248,5,43,0),(250,19,102,0),(251,16,93,0),(252,16,103,0),(253,19,104,0),(254,19,105,0),(255,16,91,0),(256,3,21,0),(257,19,106,0),(258,22,107,0),(259,19,108,0),(260,22,109,0),(261,16,109,0),(262,4,25,0),(263,2,25,0),(264,1,18,0),(265,22,110,0),(266,16,110,1),(267,8,111,0),(268,8,112,0),(269,12,112,0),(270,22,111,0),(271,10,113,0),(273,13,111,0),(274,12,114,0),(275,8,114,0),(276,22,115,0),(277,8,116,0),(278,12,116,0),(279,8,117,0),(280,12,117,0),(281,8,118,0),(282,12,118,0),(283,13,115,0),(284,4,115,0),(285,4,15,0),(286,8,119,0),(287,3,23,0),(288,22,119,0),(289,22,120,0),(290,3,120,0),(291,2,120,0),(292,3,119,0),(293,2,119,0),(294,20,13,0),(295,8,121,0),(296,8,122,0),(297,18,122,0),(298,18,123,0),(299,18,124,0),(300,8,125,0),(301,3,14,0),(302,3,124,0),(303,2,124,0),(304,2,14,0),(305,8,126,0),(306,8,127,0),(307,3,12,0),(308,2,12,0),(309,3,10,0),(310,2,10,0),(311,4,9,0),(312,2,9,0),(313,23,13,0),(314,10,128,0),(315,10,129,0),(318,8,130,0),(319,1,130,0),(320,18,130,0),(321,8,131,0),(323,24,132,0),(324,2,133,0),(325,4,133,0),(326,24,131,0),(327,8,134,0),(328,25,131,1),(329,10,134,0),(330,4,135,0),(331,2,135,0),(332,13,136,0),(333,1,136,0),(334,4,137,0),(335,2,137,0),(336,4,138,0),(337,2,138,0),(338,8,139,0),(339,12,139,0),(340,8,140,0),(341,13,141,0),(342,8,142,0),(343,23,142,0),(344,8,143,0),(345,8,144,0),(346,8,145,0),(347,19,146,0),(348,2,146,0),(349,8,146,0),(350,8,147,0),(351,19,148,0),(352,2,148,0),(353,8,149,0),(354,26,147,0),(355,8,150,0),(356,26,150,0),(357,20,150,0),(358,19,150,0),(359,4,151,0),(360,19,151,0),(361,4,152,0),(362,19,152,0),(363,19,153,0),(364,27,154,0),(365,19,154,0),(366,19,155,0),(367,27,155,0),(368,19,156,0),(369,27,156,1),(371,19,158,0),(372,2,158,0),(375,19,161,0),(376,2,161,0),(379,19,164,0),(380,29,153,1),(381,1,6,0),(382,1,7,0),(383,2,6,0),(384,3,8,0),(385,3,9,0),(386,4,8,0);
/*!40000 ALTER TABLE `participant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `questionId` int(11) NOT NULL AUTO_INCREMENT,
  `industryId` int(11) NOT NULL,
  `content` varchar(200) NOT NULL,
  PRIMARY KEY (`questionId`),
  KEY `question_industryid_fk_idx` (`industryId`),
  CONSTRAINT `question_industryid_fk` FOREIGN KEY (`industryId`) REFERENCES `industry` (`industryId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `nickname` varchar(45) NOT NULL,
  PRIMARY KEY (`userId`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `nickname_UNIQUE` (`nickname`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'ssafy1@ssafy.com','$2a$10$3VWvCQlYsYqBd6K8ubvJaORY/sS7YMMu2d1sfYvIBVZsaH2CvohB6','ssafy1'),(2,'ssafy2@ssafy.com','$2a$10$VtHnPCuKttVICIcxA.OGJuRnwPtYT65Mhjqm7.mu.6qip/FNccWdO','ssafy2'),(3,'ssafy3@ssafy.com','$2a$10$85YU/pzEByqGQsgzErDcr.tfxj5vG5zKgYYKd/5RCWQaNKpbTLueG','ssafy3'),(4,'ssafy4@ssafy.com','$2a$10$S/Z7PQRIcXxPN8hioXl2e.9jFnE2pOCh1sN2x8Z4I5yuNcK7lU49y','ssafy4');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-02-18 13:45:22
