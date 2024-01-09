-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 08, 2024 at 02:30 PM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gestion-taxi`
--

-- --------------------------------------------------------

--
-- Table structure for table `chauffeur`
--

CREATE TABLE `chauffeur` (
  `id` int(11) NOT NULL,
  `nom` varchar(45) NOT NULL,
  `prenom` varchar(45) NOT NULL,
  `email` varchar(50) NOT NULL,
  `tel` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `chauffeur`
--

INSERT INTO `chauffeur` (`id`, `nom`, `prenom`, `email`, `tel`) VALUES
(1, 'Benhaddou', 'Yasmine', 'yasmine.benhaddou@example.com', '0661122334'),
(2, 'El Amrani', 'Karim', 'karim.elamrani@example.com', '0655443322'),
(3, 'Chakir', 'Fatima', 'fatima.chakir@example.com', '0677890123'),
(4, 'Ouazzani', 'Mehdi', 'mehdi.ouazzani@example.com', '0601234567'),
(5, 'Tazi', 'Leila', 'leila.tazi@example.com', '0668765432'),
(8, 'Lmanjra', 'Hassan', 'hassan@example.com', '0661324356'),
(9, 'Maabodi', 'Amine', 'amine@example.com', '0678657867');

-- --------------------------------------------------------

--
-- Table structure for table `client`
--

CREATE TABLE `client` (
  `id_client` int(11) NOT NULL,
  `nom` varchar(30) NOT NULL,
  `prenom` varchar(30) NOT NULL,
  `num_tel` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `client`
--

INSERT INTO `client` (`id_client`, `nom`, `prenom`, `num_tel`) VALUES
(4, 'Bouhaddi', 'Nadia', 655443322),
(3, 'Ait Addi', 'Hassan', 661122334),
(5, 'El Fassi', 'Mohammed', 677890123),
(6, 'Ouahbi', 'Fatima', 601234567),
(7, 'Zerouali', 'Karima', 668765432),
(10, 'errabih', 'abdo', 12234);

-- --------------------------------------------------------

--
-- Table structure for table `login`
--

CREATE TABLE `login` (
  `user_id` int(11) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `login`
--

INSERT INTO `login` (`user_id`, `username`, `password`) VALUES
(1, 'issam@gmail.com', '1234');

-- --------------------------------------------------------

--
-- Table structure for table `reservation`
--

CREATE TABLE `reservation` (
  `id_reservation` int(11) NOT NULL,
  `client` varchar(100) NOT NULL,
  `matriculetaxi` varchar(10) NOT NULL,
  `prix` int(11) NOT NULL,
  `dateHeure` varchar(30) NOT NULL,
  `status` varchar(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reservation`
--

INSERT INTO `reservation` (`id_reservation`, `client`, `matriculetaxi`, `prix`, `dateHeure`, `status`) VALUES
(5, 'hossam chalabi', '212223-B-2', 42, '2024-01-24 20:30', 'complete'),
(4, 'hossam chalabi', '212223-B-2', 26, '2023-12-15 10:56', 'incomplete'),
(10, 'Hassan Ait Addi', 'IJ-789-KL', 99, '2024-01-09 03:12', 'complete');

-- --------------------------------------------------------

--
-- Table structure for table `taxi`
--

CREATE TABLE `taxi` (
  `id` int(11) NOT NULL,
  `marque` varchar(60) NOT NULL,
  `matricule` varchar(10) NOT NULL,
  `nbrplace` int(11) NOT NULL,
  `etat` varchar(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `taxi`
--

INSERT INTO `taxi` (`id`, `marque`, `matricule`, `nbrplace`, `etat`) VALUES
(10, 'Dacia', 'MN-012-OP', 4, 'Occupé'),
(9, 'Dacia', 'IJ-789-KL', 4, 'Disponible'),
(8, 'Dacia', 'EF-456-GH', 4, 'Occupé'),
(7, 'Dacia', 'AB-123-CD', 4, 'Disponible'),
(14, 'Dacia', 'A1343', 4, '0');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `chauffeur`
--
ALTER TABLE `chauffeur`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`id_client`);

--
-- Indexes for table `login`
--
ALTER TABLE `login`
  ADD PRIMARY KEY (`user_id`);

--
-- Indexes for table `reservation`
--
ALTER TABLE `reservation`
  ADD PRIMARY KEY (`id_reservation`);

--
-- Indexes for table `taxi`
--
ALTER TABLE `taxi`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `chauffeur`
--
ALTER TABLE `chauffeur`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `client`
--
ALTER TABLE `client`
  MODIFY `id_client` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `login`
--
ALTER TABLE `login`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `reservation`
--
ALTER TABLE `reservation`
  MODIFY `id_reservation` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `taxi`
--
ALTER TABLE `taxi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
