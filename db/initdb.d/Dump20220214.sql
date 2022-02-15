CREATE DATABASE  IF NOT EXISTS `inview` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `inview`;
-- MySQL dump 10.13  Distrib 8.0.26, for Win64 (x86_64)
--
-- Host: localhost    Database: inview
-- ------------------------------------------------------
-- Server version	5.7.35-log

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `archive`
--

LOCK TABLES `archive` WRITE;
/*!40000 ALTER TABLE `archive` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meeting`
--

LOCK TABLES `meeting` WRITE;
/*!40000 ALTER TABLE `meeting` DISABLE KEYS */;
INSERT INTO `meeting` VALUES (1,'방 제목1','2022-02-01 11:15:06','2022-02-07 13:15:06','1111',5,1,4,'b3452cbd-0f01-47b7-85cd-a871784e882e',NULL,0),(2,'방 제목2','2022-02-02 11:15:06','2022-02-07 13:15:06',NULL,5,2,4,'328ff78a-9983-4ed5-abae-35e200a0e1fa',NULL,0),(3,'방 제목3','2022-02-03 11:15:06','2022-02-07 13:15:06',NULL,5,3,1,'0ef04702-bd78-4f4f-a857-df64785739e0',NULL,0),(4,'방 제목4','2022-02-04 11:15:06','2022-02-07 13:15:06',NULL,5,4,2,'cdbabffb-b70d-4ec7-853a-6902945ec618',NULL,0),(5,'방 제목5','2022-02-05 11:15:06','2022-02-07 13:15:06',NULL,5,4,7,'2f3481c6-c087-4b28-add3-f7fc60fda028',NULL,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meetingcompany`
--

LOCK TABLES `meetingcompany` WRITE;
/*!40000 ALTER TABLE `meetingcompany` DISABLE KEYS */;
INSERT INTO `meetingcompany` VALUES (1,1,2),(2,1,1),(3,2,3),(4,3,6),(5,3,7),(6,4,9),(7,4,10),(8,5,16),(9,5,14);
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participant`
--

LOCK TABLES `participant` WRITE;
/*!40000 ALTER TABLE `participant` DISABLE KEYS */;
INSERT INTO `participant` VALUES (1,1,3,0),(2,1,4,0),(3,2,1,0),(4,2,4,0),(5,3,2,0),(6,3,4,0),(7,4,2,0),(8,4,3,0),(9,5,3,0),(10,5,4,0);
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
INSERT INTO `user` VALUES (1,'ssafy1@ssafy.com','$2a$10$XzUkiaTWK16pOrp0K/k6ieDlXW4kTqWrZ0dJ/ybtvJwcyXGWAVuUG','ssafy1'),(2,'ssafy2@ssafy.com','$2a$10$ad50G.mr69ZnfOnBSBQFwOmYPK.p951tz1N52VfPSnDEvpYZSQ2ZC','ssafy2'),(3,'ssafy3@ssafy.com','$2a$10$KGUDHJncpSk3P9ZVrMvbhezkRoxiD8QA3/ksRav2Zzjo/up9Q8IX6','ssafy3'),(4,'ssafy4@ssafy.com','$2a$10$L60Ijb81MWuvrvPUHSMVEufz3D1EIwHNzIINioXtJAX70fCwUVSzu','ssafy4'),(5,'ssafy5@ssafy.com','$2a$10$cpJnwvm/CRsww0s0/1vCCerXd6dHNJ90OirpJnmiEFXjNRSyhlg/G','ssafy5');
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

-- Dump completed on 2022-02-14 13:07:04
