/*
 Navicat Premium Data Transfer

 Source Server         : Localhost
 Source Server Type    : MySQL
 Source Server Version : 100432
 Source Host           : localhost:3306
 Source Schema         : handicrafts_group33

 Target Server Type    : MySQL
 Target Server Version : 100432
 File Encoding         : 65001

 Date: 02/04/2025 14:37:51
*/

-- SET NAMES utf8mb4;
-- SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blogs
-- ----------------------------
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/* Cơ sở dữ liệu : 'handicraft'

Cấu trúc bảng 'handicraft'*/
DROP TABLE IF EXISTS `handicraft`;

CREATE TABLE `handicraft` (
                              `id` int(11) NOT NULL,
                              `name` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
                              `year_made` int(11) DEFAULT NULL,
                              `material` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
                              `categoryID` int(11) NOT NULL,
                              `category_typeID` int(11) NOT NULL,
                              `brand` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
                              `description` text COLLATE utf8_unicode_ci NOT NULL,
                              `price` int(11) NOT NULL,
                              `quantity_sold` int(11) NOT NULL,
                              `created_at` date DEFAULT NULL,
                              `updated_at` date DEFAULT NULL,
                              `active` bit(1) NOT NULL,
                              `popular` bit(1) NOT NULL,
                              `new_arrival` bit(1) NOT NULL,
                              `discount_percent` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Cấu trúc bảng cho bảng `category_type`
--
DROP TABLE IF EXISTS `category_type`;

CREATE TABLE `category_type` (
                                 `category_typeID` int(11) NOT NULL,
                                 `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
                                 `category_type_code` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
                                 `created_at` date DEFAULT NULL,
                                 `updated_at` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Cấu trúc bảng cho bảng `handicraftimage`
--
DROP TABLE IF EXISTS `handicraftimage`;

CREATE TABLE `handicraftimage` (
                                   `imageID` int(11) NOT NULL,
                                   `path` text COLLATE utf8_unicode_ci NOT NULL,
                                   `handicraftID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
--
-- Cấu trúc bảng cho bảng `cart`
--
DROP TABLE IF EXISTS `cart`;

CREATE TABLE `cart` (
                        `cartID` int(11) NOT NULL,
                        `quantity` int(11) DEFAULT NULL,
                        `userID` int(11) DEFAULT NULL,
                        `handicraftID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
--
-- Cấu trúc bảng cho bảng `category`
--
DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
                            `categoryID` int(11) NOT NULL,
                            `code` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
                            `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
                            `created_at` date DEFAULT NULL,
                            `updated_at` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
--
-- Cấu trúc bảng cho bảng `orderline`
--
DROP TABLE IF EXISTS `orderline`;

CREATE TABLE `orderline` (
                             `id` int(11) NOT NULL,
                             `quantity` int(11) DEFAULT NULL,
                             `total_price` int(11) DEFAULT NULL,
                             `handicraftID` int(11) DEFAULT NULL,
                             `orderID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
--
-- Cấu trúc bảng cho bảng `orders`
--
DROP TABLE IF EXISTS `orders`;

CREATE TABLE `orders` (
                          `orderID` int(11) NOT NULL,
                          `address` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                          `created_at` date DEFAULT NULL,
                          `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                          `note` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                          `phone` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                          `status` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                          `total_price` int(11) DEFAULT NULL,
                          `updated_at` date DEFAULT NULL,
                          `userID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Cấu trúc bảng cho bảng `role`
--
DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
                        `roleID` int(11) NOT NULL,
                        `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Cấu trúc bảng cho bảng `roleuser`
--
DROP TABLE IF EXISTS `roleuser`;

CREATE TABLE `roleuser` (
                            `userID` int(11) NOT NULL,
                            `roleID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Cấu trúc bảng cho bảng `users`
--
DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
                         `userID` int(11) NOT NULL,
                         `email` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
                         `fullname` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
                         `username` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
                         `birthdate` date DEFAULT NULL,
                         `gender` bit(1) DEFAULT NULL,
                         `phone` varchar(12) COLLATE utf8_unicode_ci DEFAULT NULL,
                         `password` varchar(70) COLLATE utf8_unicode_ci DEFAULT NULL,
                         `status` bit(1) DEFAULT NULL,
                         `confirm_token` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
                         `is_enable` bit(1) DEFAULT NULL,
                         `provider` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL,
                         `created_at` date DEFAULT NULL,
                         `updated_at` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `author`
--
ALTER TABLE `author`
    ADD PRIMARY KEY (`authorID`);

--
-- Chỉ mục cho bảng `handicraft`
--
ALTER TABLE `handicraft`
    ADD PRIMARY KEY (`id`),
  ADD KEY `fk_handicraft_cat` (`categoryID`),
  ADD KEY `FKhcj0e0ky3ftaweqnllqfwbn99` (`category_typeID`);

--
-- Chỉ mục cho bảng `handicraftimage`
--
ALTER TABLE `handicraftimage`
    ADD PRIMARY KEY (`imageID`),
  ADD KEY `handicraftID` (`handicraftID`);

--
-- Chỉ mục cho bảng `cart`
--
ALTER TABLE `cart`
    ADD PRIMARY KEY (`cartID`),
  ADD KEY `fk_cart_user` (`userID`),
  ADD KEY `fk_cart_handicraft` (`handicraftID`);

--
-- Chỉ mục cho bảng `category`
--
ALTER TABLE `category`
    ADD PRIMARY KEY (`categoryID`);

--
-- Chỉ mục cho bảng `orderline`
--
ALTER TABLE `orderline`
    ADD PRIMARY KEY (`id`),
  ADD KEY `FKcyl1xgylw2sa0k3oqighqgd56` (`handicraftID`),
  ADD KEY `fk_order` (`orderID`);

--
-- Chỉ mục cho bảng `orders`
--
ALTER TABLE `orders`
    ADD PRIMARY KEY (`orderID`),
  ADD KEY `FKpnm1eeupqm4tykds7k3okqegv` (`userID`);

--
-- Chỉ mục cho bảng `role`
--
ALTER TABLE `role`
    ADD PRIMARY KEY (`roleID`);

--
-- Chỉ mục cho bảng `roleuser`
--
ALTER TABLE `roleuser`
    ADD KEY `fk_user` (`userID`),
  ADD KEY `fk_role` (`roleID`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
    ADD PRIMARY KEY (`userID`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `author`
--
ALTER TABLE `category_type`
    MODIFY `category_typeID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT cho bảng `handicraft`
--
ALTER TABLE `handicraft`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=55;

--
-- AUTO_INCREMENT cho bảng `handicraftimage`
--
ALTER TABLE `handicraftimage`
    MODIFY `imageID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;

--
-- AUTO_INCREMENT cho bảng `cart`
--
ALTER TABLE `cart`
    MODIFY `cartID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- AUTO_INCREMENT cho bảng `category`
--
ALTER TABLE `category`
    MODIFY `categoryID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT cho bảng `orderline`
--
ALTER TABLE `orderline`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT cho bảng `orders`
--
ALTER TABLE `orders`
    MODIFY `orderID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT cho bảng `role`
--
ALTER TABLE `role`
    MODIFY `roleID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
    MODIFY `userID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `handicraft`
--
ALTER TABLE `handicraft`
    ADD CONSTRAINT `FKhcj0e0ky3ftaweqnllqfwbn99` FOREIGN KEY (`category_typeID`) REFERENCES `author` (`category_typeID`),
  ADD CONSTRAINT `fk_handicraft_cat` FOREIGN KEY (`categoryID`) REFERENCES `category` (`categoryID`);

--
-- Các ràng buộc cho bảng `handicraftimage`
--
ALTER TABLE `handicraftimage`
    ADD CONSTRAINT `handicraftimage_ibfk_1` FOREIGN KEY (`handicraftID`) REFERENCES `handicraft` (`id`);

--
-- Các ràng buộc cho bảng `cart`
--
ALTER TABLE `cart`
    ADD CONSTRAINT `fk_carhandicraft` FOREIGN KEY (`categoryID`) REFERENCES `handicraft` (`id`),
  ADD CONSTRAINT `fk_cart_user` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`);

--
-- Các ràng buộc cho bảng `orderline`
--
ALTER TABLE `orderline`
    ADD CONSTRAINT `FKcyl1xgylw2sa0k3oqighqgd56` FOREIGN KEY (`handicraftid`) REFERENCES `handicraft` (`id`),
  ADD CONSTRAINT `FKdouy9nmjxe36by2xqq0t5pqxf` FOREIGN KEY (`orderid`) REFERENCES `orders` (`orderid`),
  ADD CONSTRAINT `fk_order` FOREIGN KEY (`orderID`) REFERENCES `orders` (`orderid`);

--
-- Các ràng buộc cho bảng `orders`
--
ALTER TABLE `orders`
    ADD CONSTRAINT `FKpnm1eeupqm4tykds7k3okqegv` FOREIGN KEY (`userid`) REFERENCES `users` (`userID`);

--
-- Các ràng buộc cho bảng `roleuser`
--
ALTER TABLE `roleuser`
    ADD CONSTRAINT `fk_role` FOREIGN KEY (`roleID`) REFERENCES `role` (`roleID`),
  ADD CONSTRAINT `fk_user` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

SET FOREIGN_KEY_CHECKS = 1;
