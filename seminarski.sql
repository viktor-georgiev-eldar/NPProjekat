-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: seminarski
-- ------------------------------------------------------
-- Server version	8.0.34

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
-- Table structure for table `artikal`
--

DROP TABLE IF EXISTS `artikal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `artikal` (
  `artikalId` int NOT NULL AUTO_INCREMENT,
  `naziv` varchar(50) DEFAULT NULL,
  `opis` varchar(100) DEFAULT NULL,
  `cena` double DEFAULT NULL,
  PRIMARY KEY (`artikalId`),
  UNIQUE KEY `naziv` (`naziv`),
  CONSTRAINT `Cena` CHECK ((`cena` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `artikal`
--

LOCK TABLES `artikal` WRITE;
/*!40000 ALTER TABLE `artikal` DISABLE KEYS */;
INSERT INTO `artikal` VALUES (1,'Kafa','Domaca (turska) kafa',75.8),(2,'Espresso','kratki',150),(5,'Gazirana voda','Knjaz Milos 0.25',120),(6,'Sok','Jabuka',120.99),(7,'Sok od jabuke','Next 0.2',160.99),(23,'topla cokolada','mlecna',189.99);
/*!40000 ALTER TABLE `artikal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `korisnik`
--

DROP TABLE IF EXISTS `korisnik`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `korisnik` (
  `korisnikId` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `ime` varchar(50) DEFAULT NULL,
  `prezime` varchar(50) DEFAULT NULL,
  `telefon` varchar(20) DEFAULT NULL,
  `tipKorisnikaId` int DEFAULT NULL,
  `ulogovan` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`korisnikId`),
  UNIQUE KEY `username` (`username`),
  KEY `korisnik_ibfk_1` (`tipKorisnikaId`),
  CONSTRAINT `korisnik_ibfk_1` FOREIGN KEY (`tipKorisnikaId`) REFERENCES `tip_korisnika` (`tipKorisnikaId`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `korisnik`
--

LOCK TABLES `korisnik` WRITE;
/*!40000 ALTER TABLE `korisnik` DISABLE KEYS */;
INSERT INTO `korisnik` VALUES (1,'admin','admin','Administrator','Administrator','0601234567',1,0),(3,'jovan','jovan','Jovan','Jovanovic','0645412845',2,0),(4,'ivan','ivan','Ivan','Ivic','0611548354',2,0),(10,'ana','ana','Ana','Anic','0635264399',2,0),(11,'petar','petar','Petar','Petrovic','0639827639',2,0),(12,'milos','milos','Milos','Milic','06345678',2,0),(13,'petar1','jasampetar','Petar','Petrovic','061555777',2,0);
/*!40000 ALTER TABLE `korisnik` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `racun`
--

DROP TABLE IF EXISTS `racun`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `racun` (
  `racunid` int NOT NULL AUTO_INCREMENT,
  `datum` datetime DEFAULT NULL,
  `korisnikid` int DEFAULT NULL,
  `ukupno` double DEFAULT NULL,
  PRIMARY KEY (`racunid`),
  KEY `racun_ibfk_1` (`korisnikid`),
  CONSTRAINT `racun_ibfk_1` FOREIGN KEY (`korisnikid`) REFERENCES `korisnik` (`korisnikId`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=181 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `racun`
--

LOCK TABLES `racun` WRITE;
/*!40000 ALTER TABLE `racun` DISABLE KEYS */;
INSERT INTO `racun` VALUES (155,'2022-07-12 22:23:34',4,75.8),(156,'2022-07-12 22:38:13',4,300),(158,'2022-07-13 20:28:30',4,241.98),(159,'2022-07-13 20:34:09',4,195.8),(160,'2022-07-13 20:37:05',10,2009.52),(161,'2022-07-13 20:37:07',10,195.8),(162,'2022-07-13 20:37:09',10,450),(163,'2022-07-13 20:37:11',4,482.97),(164,'2022-07-18 21:36:54',4,75.8),(165,'2022-07-23 22:31:55',4,151.6),(166,'2022-07-23 22:56:01',4,160.99),(167,'2022-08-20 18:48:15',4,75.8),(170,'2023-11-23 21:31:08',4,379.98);
/*!40000 ALTER TABLE `racun` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stavka_racuna`
--

DROP TABLE IF EXISTS `stavka_racuna`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stavka_racuna` (
  `racunid` int NOT NULL,
  `artikalid` int NOT NULL,
  `kolicina` int DEFAULT NULL,
  PRIMARY KEY (`racunid`,`artikalid`),
  KEY `artikalid` (`artikalid`),
  CONSTRAINT `stavka_racuna_ibfk_1` FOREIGN KEY (`artikalid`) REFERENCES `artikal` (`artikalId`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `stavka_racuna_ibfk_2` FOREIGN KEY (`racunid`) REFERENCES `racun` (`racunid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stavka_racuna`
--

LOCK TABLES `stavka_racuna` WRITE;
/*!40000 ALTER TABLE `stavka_racuna` DISABLE KEYS */;
INSERT INTO `stavka_racuna` VALUES (155,1,1),(156,2,2),(158,6,2),(159,1,1),(159,5,1),(160,1,2),(160,2,3),(160,5,2),(160,6,3),(160,7,5),(161,1,1),(161,5,1),(162,2,3),(163,7,3),(164,1,1),(165,1,2),(166,7,1),(167,1,1),(170,23,2);
/*!40000 ALTER TABLE `stavka_racuna` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tip_korisnika`
--

DROP TABLE IF EXISTS `tip_korisnika`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tip_korisnika` (
  `tipKorisnikaId` int NOT NULL,
  `nazivTipaKorisnika` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`tipKorisnikaId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tip_korisnika`
--

LOCK TABLES `tip_korisnika` WRITE;
/*!40000 ALTER TABLE `tip_korisnika` DISABLE KEYS */;
INSERT INTO `tip_korisnika` VALUES (1,'administrator'),(2,'korisnik');
/*!40000 ALTER TABLE `tip_korisnika` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'seminarski'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-30 22:33:55
