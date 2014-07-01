
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

/*!40000 DROP DATABASE IF EXISTS `location_service`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `location_service` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_swedish_ci */;

USE `location_service`;
DROP TABLE IF EXISTS `AREA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AREA` (
  `area_id` int(11) NOT NULL,
  `x1` int(11) DEFAULT NULL,
  `y1` int(11) DEFAULT NULL,
  `x2` int(11) DEFAULT NULL,
  `y2` int(11) DEFAULT NULL,
  `location_id` int(11) DEFAULT NULL,
  `angle` int(11) DEFAULT NULL,
  PRIMARY KEY (`area_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `AREA` WRITE;
/*!40000 ALTER TABLE `AREA` DISABLE KEYS */;
/*!40000 ALTER TABLE `AREA` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `COLLECTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `COLLECTION` (
  `location_id` int(11) NOT NULL,
  `location_code` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `map_id` int(11) DEFAULT NULL,
  `image_id` int(11) DEFAULT NULL,
  `owner_id` int(11) NOT NULL,
  `name` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `floor` varchar(20) COLLATE utf8_swedish_ci DEFAULT NULL,
  `library_id` int(11) DEFAULT NULL,
  `shelf_number` varchar(40) COLLATE utf8_swedish_ci DEFAULT NULL,
  `staff_note_1` text COLLATE utf8_swedish_ci,
  `staff_note_2` text COLLATE utf8_swedish_ci,
  `created` datetime DEFAULT NULL,
  `creator` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `updater` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `is_substring` bit(1) DEFAULT NULL,
  `collection_code` varchar(50) COLLATE utf8_swedish_ci DEFAULT NULL,
  `latitude` varchar(20) COLLATE utf8_swedish_ci DEFAULT NULL,
  `longitude` varchar(20) COLLATE utf8_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`location_id`),
  KEY `FK752A03D562A15F58b51f9a9e` (`map_id`),
  KEY `FK752A03D51B01B78b51f9a9e` (`image_id`),
  KEY `FKB51F9A9E21D96FF8` (`library_id`),
  KEY `FK752A03D598677C78b51f9a9e` (`owner_id`),
  KEY `FK752A03D54CBB46C5b51f9a9e` (`map_id`),
  KEY `FK752A03D5CCEDDBA5b51f9a9e` (`image_id`),
  KEY `FKB51F9A9E14A7D8E5` (`library_id`),
  KEY `FK752A03D563A53CA5b51f9a9e` (`owner_id`),
  KEY `COLLECTIONidx_location_code` (`location_code`),
  KEY `COLLECTIONlocation_code_idx` (`location_code`),
  KEY `COLLECTION_location_code_idx` (`location_code`),
  KEY `collection_code_idx` (`collection_code`),
  CONSTRAINT `FK752A03D51B01B78b51f9a9e` FOREIGN KEY (`image_id`) REFERENCES `IMAGE` (`id`),
  CONSTRAINT `FK752A03D54CBB46C5b51f9a9e` FOREIGN KEY (`map_id`) REFERENCES `MAP` (`id`),
  CONSTRAINT `FK752A03D562A15F58b51f9a9e` FOREIGN KEY (`map_id`) REFERENCES `MAP` (`id`),
  CONSTRAINT `FK752A03D563A53CA5b51f9a9e` FOREIGN KEY (`owner_id`) REFERENCES `OWNER` (`id`),
  CONSTRAINT `FK752A03D598677C78b51f9a9e` FOREIGN KEY (`owner_id`) REFERENCES `OWNER` (`id`),
  CONSTRAINT `FK752A03D5CCEDDBA5b51f9a9e` FOREIGN KEY (`image_id`) REFERENCES `IMAGE` (`id`),
  CONSTRAINT `FKB51F9A9E14A7D8E5` FOREIGN KEY (`library_id`) REFERENCES `LIBRARY` (`location_id`),
  CONSTRAINT `FKB51F9A9E21D96FF8` FOREIGN KEY (`library_id`) REFERENCES `LIBRARY` (`location_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `COLLECTION` WRITE;
/*!40000 ALTER TABLE `COLLECTION` DISABLE KEYS */;
/*!40000 ALTER TABLE `COLLECTION` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `DESCRIPTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DESCRIPTION` (
  `description_id` int(11) NOT NULL,
  `description` varchar(200) COLLATE utf8_swedish_ci DEFAULT NULL,
  `language_id` int(11) DEFAULT NULL,
  `location_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`description_id`),
  KEY `FK198917DC64469A5C` (`language_id`),
  KEY `FK198917DCCB454F0F` (`language_id`),
  CONSTRAINT `FK198917DC64469A5C` FOREIGN KEY (`language_id`) REFERENCES `LANGUAGE` (`id`),
  CONSTRAINT `FK198917DCCB454F0F` FOREIGN KEY (`language_id`) REFERENCES `LANGUAGE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `DESCRIPTION` WRITE;
/*!40000 ALTER TABLE `DESCRIPTION` DISABLE KEYS */;
/*!40000 ALTER TABLE `DESCRIPTION` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `IMAGE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMAGE` (
  `id` int(11) NOT NULL,
  `path` text COLLATE utf8_swedish_ci,
  `description` text COLLATE utf8_swedish_ci,
  `isExternal` char(1) COLLATE utf8_swedish_ci DEFAULT NULL,
  `owner_id` int(11) NOT NULL,
  `x` int(11) DEFAULT NULL,
  `y` int(11) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `creator` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `updater` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3EF5B97098677C78428b13b` (`owner_id`),
  CONSTRAINT `FK3EF5B97098677C78428b13b` FOREIGN KEY (`owner_id`) REFERENCES `OWNER` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `IMAGE` WRITE;
/*!40000 ALTER TABLE `IMAGE` DISABLE KEYS */;
/*!40000 ALTER TABLE `IMAGE` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `LANGUAGE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LANGUAGE` (
  `id` int(11) NOT NULL,
  `code` varchar(20) COLLATE utf8_swedish_ci DEFAULT NULL,
  `name` varchar(50) COLLATE utf8_swedish_ci DEFAULT NULL,
  `owner_id` int(11) NOT NULL,
  `created` datetime DEFAULT NULL,
  `creator` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `updater` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKCE78835898677C78` (`owner_id`),
  KEY `FKCE78835863A53CA5` (`owner_id`),
  KEY `idx_code` (`code`),
  KEY `code_idx` (`code`),
  CONSTRAINT `FKCE78835863A53CA5` FOREIGN KEY (`owner_id`) REFERENCES `OWNER` (`id`),
  CONSTRAINT `FKCE78835898677C78` FOREIGN KEY (`owner_id`) REFERENCES `OWNER` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `LANGUAGE` WRITE;
/*!40000 ALTER TABLE `LANGUAGE` DISABLE KEYS */;
/*!40000 ALTER TABLE `LANGUAGE` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `LIBRARY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LIBRARY` (
  `location_id` int(11) NOT NULL,
  `location_code` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `map_id` int(11) DEFAULT NULL,
  `image_id` int(11) DEFAULT NULL,
  `owner_id` int(11) NOT NULL,
  `name` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `floor` varchar(20) COLLATE utf8_swedish_ci DEFAULT NULL,
  `staff_note_1` text COLLATE utf8_swedish_ci,
  `staff_note_2` text COLLATE utf8_swedish_ci,
  `created` datetime DEFAULT NULL,
  `creator` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `updater` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `latitude` varchar(20) COLLATE utf8_swedish_ci DEFAULT NULL,
  `longitude` varchar(20) COLLATE utf8_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`location_id`),
  KEY `FK752A03D562A15F5834b3b09b` (`map_id`),
  KEY `FK752A03D51B01B7834b3b09b` (`image_id`),
  KEY `FK752A03D598677C7834b3b09b` (`owner_id`),
  KEY `FK752A03D54CBB46C534b3b09b` (`map_id`),
  KEY `FK752A03D5CCEDDBA534b3b09b` (`image_id`),
  KEY `FK752A03D563A53CA534b3b09b` (`owner_id`),
  KEY `LIBRARYidx_location_code` (`location_code`),
  KEY `LIBRARYlocation_code_idx` (`location_code`),
  KEY `LIBRARY_location_code_idx` (`location_code`),
  CONSTRAINT `FK752A03D51B01B7834b3b09b` FOREIGN KEY (`image_id`) REFERENCES `IMAGE` (`id`),
  CONSTRAINT `FK752A03D54CBB46C534b3b09b` FOREIGN KEY (`map_id`) REFERENCES `MAP` (`id`),
  CONSTRAINT `FK752A03D562A15F5834b3b09b` FOREIGN KEY (`map_id`) REFERENCES `MAP` (`id`),
  CONSTRAINT `FK752A03D563A53CA534b3b09b` FOREIGN KEY (`owner_id`) REFERENCES `OWNER` (`id`),
  CONSTRAINT `FK752A03D598677C7834b3b09b` FOREIGN KEY (`owner_id`) REFERENCES `OWNER` (`id`),
  CONSTRAINT `FK752A03D5CCEDDBA534b3b09b` FOREIGN KEY (`image_id`) REFERENCES `IMAGE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `LIBRARY` WRITE;
/*!40000 ALTER TABLE `LIBRARY` DISABLE KEYS */;
/*!40000 ALTER TABLE `LIBRARY` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `MAP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MAP` (
  `id` int(11) NOT NULL,
  `path` text COLLATE utf8_swedish_ci,
  `description` text COLLATE utf8_swedish_ci,
  `isExternal` char(1) COLLATE utf8_swedish_ci DEFAULT NULL,
  `owner_id` int(11) NOT NULL,
  `color` varchar(6) COLLATE utf8_swedish_ci DEFAULT NULL,
  `opacity` varchar(3) COLLATE utf8_swedish_ci DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `creator` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `updater` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3EF5B97098677C781293c` (`owner_id`),
  CONSTRAINT `FK3EF5B97098677C781293c` FOREIGN KEY (`owner_id`) REFERENCES `OWNER` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `MAP` WRITE;
/*!40000 ALTER TABLE `MAP` DISABLE KEYS */;
/*!40000 ALTER TABLE `MAP` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `MY_USER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MY_USER` (
  `login` varchar(15) COLLATE utf8_swedish_ci NOT NULL,
  `first_name` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `last_name` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `pass` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `organization` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `owner_id` int(11) NOT NULL,
  `created` datetime DEFAULT NULL,
  `creator` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `updater` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`login`),
  KEY `FK8681BE7E98677C78` (`owner_id`),
  CONSTRAINT `FK8681BE7E98677C78` FOREIGN KEY (`owner_id`) REFERENCES `OWNER` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `MY_USER` WRITE;
/*!40000 ALTER TABLE `MY_USER` DISABLE KEYS */;
INSERT INTO `MY_USER` VALUES ('admin','Admin','Admin','13d8c9009647abffee45341993ab3952f57c361d','admin@example.com','Organization',1,'2014-06-22 17:19:00','SYSTEM',NULL,NULL);
/*!40000 ALTER TABLE `MY_USER` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `NOTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `NOTE` (
  `note_id` int(11) NOT NULL,
  `note` text COLLATE utf8_swedish_ci,
  `language_id` int(11) DEFAULT NULL,
  `location_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`note_id`),
  KEY `FK24A7F264469A5C` (`language_id`),
  KEY `FK24A7F2CB454F0F` (`language_id`),
  CONSTRAINT `FK24A7F264469A5C` FOREIGN KEY (`language_id`) REFERENCES `LANGUAGE` (`id`),
  CONSTRAINT `FK24A7F2CB454F0F` FOREIGN KEY (`language_id`) REFERENCES `LANGUAGE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `NOTE` WRITE;
/*!40000 ALTER TABLE `NOTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `NOTE` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `NOT_FOUND_REDIRECT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `NOT_FOUND_REDIRECT` (
  `id` int(11) NOT NULL,
  `mod_condition` varchar(200) COLLATE utf8_swedish_ci DEFAULT NULL,
  `mod_operation` varchar(200) COLLATE utf8_swedish_ci DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `owner_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6DBF53FB63A53CA557304b25` (`owner_id`),
  KEY `FK57304B2598677C78` (`owner_id`),
  KEY `FK6DBF53FB7A5EB45157304b25` (`owner_id`),
  KEY `FK57304B25306CC361` (`owner_id`),
  KEY `FK57304B25248802FD` (`owner_id`),
  KEY `FK6DBF53FBC6EE2D6A57304b25` (`owner_id`),
  KEY `FK6DBF53FB248802FD57304b25` (`owner_id`),
  KEY `FK6DBF53FB5836D97157304b25` (`owner_id`),
  KEY `FK6DBF53FBC292488B57304b25` (`owner_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `NOT_FOUND_REDIRECT` WRITE;
/*!40000 ALTER TABLE `NOT_FOUND_REDIRECT` DISABLE KEYS */;
/*!40000 ALTER TABLE `NOT_FOUND_REDIRECT` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `OWNER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OWNER` (
  `id` int(11) NOT NULL,
  `code` varchar(10) COLLATE utf8_swedish_ci DEFAULT NULL,
  `name` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `color` varchar(6) COLLATE utf8_swedish_ci DEFAULT NULL,
  `opacity` varchar(3) COLLATE utf8_swedish_ci DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `creator` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `updater` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `exporter_visible` bit(1) DEFAULT NULL,
  `allowed_ips` longtext COLLATE utf8_swedish_ci,
  `locating_strategy` varchar(255) COLLATE utf8_swedish_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_code` (`code`),
  KEY `code_idx` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `OWNER` WRITE;
/*!40000 ALTER TABLE `OWNER` DISABLE KEYS */;
INSERT INTO `OWNER` VALUES (1,'ADMIN','Admin','dd0000','150','2014-06-22 17:18:00','SYSTEM',NULL,NULL,'\0','','INDEX_EXTERNAL');
/*!40000 ALTER TABLE `OWNER` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `PREPROCESSING_REDIRECT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PREPROCESSING_REDIRECT` (
  `id` int(11) NOT NULL,
  `mod_condition` varchar(200) COLLATE utf8_swedish_ci DEFAULT NULL,
  `mod_operation` varchar(200) COLLATE utf8_swedish_ci DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `owner_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6DBF53FB63A53CA5c7fd8025` (`owner_id`),
  KEY `FKC7FD802598677C78` (`owner_id`),
  KEY `FK6DBF53FB7A5EB451c7fd8025` (`owner_id`),
  KEY `FKC7FD8025306CC361` (`owner_id`),
  KEY `FKC7FD8025248802FD` (`owner_id`),
  KEY `FK6DBF53FBC6EE2D6Ac7fd8025` (`owner_id`),
  KEY `FK6DBF53FB248802FDc7fd8025` (`owner_id`),
  KEY `FK6DBF53FB5836D971c7fd8025` (`owner_id`),
  KEY `FK6DBF53FBC292488Bc7fd8025` (`owner_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `PREPROCESSING_REDIRECT` WRITE;
/*!40000 ALTER TABLE `PREPROCESSING_REDIRECT` DISABLE KEYS */;
/*!40000 ALTER TABLE `PREPROCESSING_REDIRECT` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `SEARCH_EVENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SEARCH_EVENT` (
  `id` int(11) NOT NULL,
  `call_number` longtext COLLATE utf8_swedish_ci,
  `collection_code` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `language_code` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `status` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `search_type` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `position` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `is_authorized` bit(1) DEFAULT NULL,
  `ip_address` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `event_type` varchar(255) COLLATE utf8_swedish_ci NOT NULL,
  `processing_time` bigint(20) DEFAULT NULL,
  `owner_code` varchar(10) COLLATE utf8_swedish_ci DEFAULT NULL,
  `event_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `search_event_owner_code_idx` (`owner_code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `SEARCH_EVENT` WRITE;
/*!40000 ALTER TABLE `SEARCH_EVENT` DISABLE KEYS */;
/*!40000 ALTER TABLE `SEARCH_EVENT` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `SEARCH_INDEX`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SEARCH_INDEX` (
  `id` int(11) NOT NULL,
  `location_id` int(11) NOT NULL,
  `location_type` varchar(255) COLLATE utf8_swedish_ci NOT NULL,
  `call_number` longtext COLLATE utf8_swedish_ci NOT NULL,
  `location_code` varchar(100) COLLATE utf8_swedish_ci NOT NULL,
  `collection_code` varchar(50) COLLATE utf8_swedish_ci DEFAULT NULL,
  `owner_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKDBC30F5B98677C78` (`owner_id`),
  KEY `collection_code_idx` (`collection_code`),
  KEY `location_code_idx` (`location_code`),
  KEY `_location_id_idx` (`location_id`),
  KEY `_location_type_idx` (`location_type`),
  CONSTRAINT `FKDBC30F5B98677C78` FOREIGN KEY (`owner_id`) REFERENCES `OWNER` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `SEARCH_INDEX` WRITE;
/*!40000 ALTER TABLE `SEARCH_INDEX` DISABLE KEYS */;
/*!40000 ALTER TABLE `SEARCH_INDEX` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `SHELF`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SHELF` (
  `location_id` int(11) NOT NULL,
  `location_code` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `map_id` int(11) DEFAULT NULL,
  `image_id` int(11) DEFAULT NULL,
  `owner_id` int(11) NOT NULL,
  `name` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `floor` varchar(20) COLLATE utf8_swedish_ci DEFAULT NULL,
  `collection_id` int(11) DEFAULT NULL,
  `shelf_number` varchar(40) COLLATE utf8_swedish_ci DEFAULT NULL,
  `staff_note_1` text COLLATE utf8_swedish_ci,
  `staff_note_2` text COLLATE utf8_swedish_ci,
  `created` datetime DEFAULT NULL,
  `creator` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `updater` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `latitude` varchar(20) COLLATE utf8_swedish_ci DEFAULT NULL,
  `longitude` varchar(20) COLLATE utf8_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`location_id`),
  KEY `FK752A03D562A15F584b3660a` (`map_id`),
  KEY `FK752A03D51B01B784b3660a` (`image_id`),
  KEY `FK4B3660A5B7586B3` (`collection_id`),
  KEY `FK752A03D598677C784b3660a` (`owner_id`),
  KEY `FK752A03D54CBB46C54b3660a` (`map_id`),
  KEY `FK752A03D5CCEDDBA54b3660a` (`image_id`),
  KEY `FK4B3660A651B6B60` (`collection_id`),
  KEY `FK752A03D563A53CA54b3660a` (`owner_id`),
  KEY `SHELFidx_location_code` (`location_code`),
  KEY `SHELFlocation_code_idx` (`location_code`),
  KEY `SHELF_location_code_idx` (`location_code`),
  CONSTRAINT `FK4B3660A5B7586B3` FOREIGN KEY (`collection_id`) REFERENCES `COLLECTION` (`location_id`),
  CONSTRAINT `FK4B3660A651B6B60` FOREIGN KEY (`collection_id`) REFERENCES `COLLECTION` (`location_id`),
  CONSTRAINT `FK752A03D51B01B784b3660a` FOREIGN KEY (`image_id`) REFERENCES `IMAGE` (`id`),
  CONSTRAINT `FK752A03D54CBB46C54b3660a` FOREIGN KEY (`map_id`) REFERENCES `MAP` (`id`),
  CONSTRAINT `FK752A03D562A15F584b3660a` FOREIGN KEY (`map_id`) REFERENCES `MAP` (`id`),
  CONSTRAINT `FK752A03D563A53CA54b3660a` FOREIGN KEY (`owner_id`) REFERENCES `OWNER` (`id`),
  CONSTRAINT `FK752A03D598677C784b3660a` FOREIGN KEY (`owner_id`) REFERENCES `OWNER` (`id`),
  CONSTRAINT `FK752A03D5CCEDDBA54b3660a` FOREIGN KEY (`image_id`) REFERENCES `IMAGE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `SHELF` WRITE;
/*!40000 ALTER TABLE `SHELF` DISABLE KEYS */;
/*!40000 ALTER TABLE `SHELF` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `SUBJECT_MATTER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SUBJECT_MATTER` (
  `subject_matter_id` int(11) NOT NULL,
  `index_term` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `language_id` int(11) DEFAULT NULL,
  `owner_id` int(11) NOT NULL,
  `created` datetime DEFAULT NULL,
  `creator` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `updater` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`subject_matter_id`),
  KEY `FK5679A95498677C78` (`owner_id`),
  KEY `FK5679A95464469A5C` (`language_id`),
  KEY `FK5679A954CB454F0F` (`language_id`),
  KEY `idx_index_term` (`index_term`),
  KEY `index_term_idx` (`index_term`),
  CONSTRAINT `FK5679A95464469A5C` FOREIGN KEY (`language_id`) REFERENCES `LANGUAGE` (`id`),
  CONSTRAINT `FK5679A95498677C78` FOREIGN KEY (`owner_id`) REFERENCES `OWNER` (`id`),
  CONSTRAINT `FK5679A954CB454F0F` FOREIGN KEY (`language_id`) REFERENCES `LANGUAGE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `SUBJECT_MATTER` WRITE;
/*!40000 ALTER TABLE `SUBJECT_MATTER` DISABLE KEYS */;
/*!40000 ALTER TABLE `SUBJECT_MATTER` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `SUBJECT_MATTERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SUBJECT_MATTERS` (
  `location_id` int(11) NOT NULL,
  `subject_matter_id` int(11) NOT NULL,
  KEY `FK78BB817FB10154B1` (`subject_matter_id`),
  KEY `FK78BB817F528697DE` (`subject_matter_id`),
  CONSTRAINT `FK78BB817F528697DE` FOREIGN KEY (`subject_matter_id`) REFERENCES `SUBJECT_MATTER` (`subject_matter_id`),
  CONSTRAINT `FK78BB817FB10154B1` FOREIGN KEY (`subject_matter_id`) REFERENCES `SUBJECT_MATTER` (`subject_matter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `SUBJECT_MATTERS` WRITE;
/*!40000 ALTER TABLE `SUBJECT_MATTERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `SUBJECT_MATTERS` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `USER_INFO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_INFO` (
  `user_info_id` int(11) NOT NULL,
  `login` varchar(15) COLLATE utf8_swedish_ci NOT NULL,
  `group_name` varchar(15) COLLATE utf8_swedish_ci NOT NULL,
  PRIMARY KEY (`user_info_id`),
  UNIQUE KEY `login` (`login`),
  KEY `FKBC12D8A2C79C05` (`login`),
  KEY `group_name_idx` (`group_name`),
  CONSTRAINT `FKBC12D8A2C79C05` FOREIGN KEY (`login`) REFERENCES `MY_USER` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `USER_INFO` WRITE;
/*!40000 ALTER TABLE `USER_INFO` DISABLE KEYS */;
INSERT INTO `USER_INFO` VALUES (1,'admin','ADMIN');
/*!40000 ALTER TABLE `USER_INFO` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

