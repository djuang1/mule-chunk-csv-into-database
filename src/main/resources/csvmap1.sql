-- phpMyAdmin SQL Dump
-- version 4.4.3
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jun 01, 2016 at 06:42 PM
-- Server version: 5.6.21
-- PHP Version: 5.6.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `hris`
--

-- --------------------------------------------------------

--
-- Table structure for table `csvmap1`
--

CREATE TABLE IF NOT EXISTS `csvmap1` (
  `pid` varchar(100) DEFAULT NULL,
  `last_name` varchar(100) DEFAULT NULL,
  `first_name` varchar(100) DEFAULT NULL,
  `dob` varchar(100) DEFAULT NULL,
  `gender` varchar(100) DEFAULT NULL,
  `race` varchar(100) DEFAULT NULL,
  `patient_id` varchar(100) DEFAULT NULL,
  `date_admitted` varchar(100) DEFAULT NULL,
  `date_discharged` varchar(100) DEFAULT NULL,
  `reason_for_visit` varchar(255) DEFAULT NULL,
  `hospitalization_type_id` varchar(100) DEFAULT NULL,
  `patient_id2` varchar(100) DEFAULT NULL,
  `icd9` varchar(100) DEFAULT NULL,
  `diagnosis_type_id` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
