create database db_tp4mastercamp;

use db_tp4mastercamp;

CREATE TABLE `topic` (
  `TopicID` int NOT NULL AUTO_INCREMENT,
  `Topic` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`TopicID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `category` (
  `CategoryID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`CategoryID`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `document` (
  `DocumentID` int NOT NULL AUTO_INCREMENT,
  `DocumentName` varchar(50) DEFAULT NULL,
  `DocumentDate` date DEFAULT NULL,
  `StorageAdresse` text,
  `TopicID` int NOT NULL,
  `CategoryID` int NOT NULL,
  PRIMARY KEY (`DocumentID`),
  KEY `document_ibfk_1` (`TopicID`),
  KEY `document_ibfk_2` (`CategoryID`),
  CONSTRAINT `document_ibfk_1` FOREIGN KEY (`TopicID`) REFERENCES `topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `document_ibfk_2` FOREIGN KEY (`CategoryID`) REFERENCES `category` (`CategoryID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tag` (
  `TagID` int NOT NULL AUTO_INCREMENT,
  `Tag` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`TagID`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `avoir` (
  `DocumentID` int NOT NULL,
  `TagID` int NOT NULL,
  KEY `avoir_ibfk_1_idx` (`DocumentID`),
  KEY `avoir_ibfk_2_idx` (`TagID`),
  CONSTRAINT `avoir_ibfk_1` FOREIGN KEY (`DocumentID`) REFERENCES `document` (`DocumentID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `avoir_ibfk_2` FOREIGN KEY (`TagID`) REFERENCES `tag` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;