/*
 Navicat Premium Data Transfer

 Source Server         : Localhost
 Source Server Type    : MySQL
 Source Server Version : 100432
 Source Host           : localhost:3306
 Source Schema         : ltw_group33

 Target Server Type    : MySQL
 Target Server Version : 100432
 File Encoding         : 65001

 Date: 02/04/2025 14:37:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blogs
-- ----------------------------
DROP TABLE IF EXISTS `blogs`;
CREATE TABLE `blogs`  (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `title` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                          `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                          `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                          `profilePic` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                          `categoryId` int NOT NULL,
                          `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                          `status` int NOT NULL,
                          `createdDate` timestamp NULL DEFAULT NULL,
                          `createdBy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                          `modifiedDate` timestamp NULL DEFAULT NULL,
                          `modifiedBy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blogs
-- ----------------------------
INSERT INTO `blogs` VALUES (1, 'Thủ công mỹ nghệ và sản phẩm thủ công mỹ nghệ', 'Admin', 'Thủ công mỹ nghệ là ngành nghề chuyên làm thủ công (làm bằng tay) các mặt hàng có tính thẩm mỹ và nghệ thuật.', NULL, 1, 'Các sản phẩm thủ công mỹ nghệ là sản phẩm được làm hoàn toàn bằng tay dưới sự hỗ trợ của công cụ đơn giản. Nói cách khác, sản phẩm Thủ công mỹ nghệ (Handicraft) là kết quả từ bàn tay của nghệ nhân thủ công. Chúng thể hiện vẻ đẹp của sự khéo léo cùng kĩ thuật truyền thống; chúng không được tạo ra từ quá trình sản xuất máy móc hàng loạt. Do đó, sản phẩm thủ công mỹ nghệ thường sẽ là sản phẩm của các ngành nghề truyền thống, lâu đời, không chỉ có giá trị nghệ thuật mà còn có giá trị văn hóa và giá trị dân tộc.\r\nHiện nay, các sản phẩm thủ công mỹ nghệ với những ưu điểm nổi bật do đặc tính sáng tạo, tính nghệ thuật và xu hướng tiêu dùng xanh của xã hội mà ngành hàng này càng được ưa chuộng và phát triển rộng lớn.', 1, '2024-04-02 20:56:53', 'impmd2305@gmail.com', '2024-04-02 20:56:53', 'impmd2305@gmail.com');
INSERT INTO `blogs` VALUES (2, 'Test đan lát blog', 'Admin 2', 'Thử test đan lát blog và cái kết', NULL, 1, 'Đây là bài blog test với danh mục đan lát', 1, NULL, NULL, NULL, NULL);
INSERT INTO `blogs` VALUES (3, 'Test gốm sứ blog', 'Admin 1', 'Thử test gốm sứ blog và cái kết', NULL, 1, 'Đây là bài blog test với danh mục gốm sứ', 1, NULL, NULL, NULL, NULL);
INSERT INTO `blogs` VALUES (4, 'Test gốm sứ blog 2', 'Admin 2', 'Thử test gốm sứ blog 2 và cái kết', NULL, 1, 'Đây là bài blog test với danh mục gốm sứ 2', 1, NULL, NULL, NULL, NULL);
INSERT INTO `blogs` VALUES (5, 'Test vô hiệu hóa blog', 'Admin 2', 'Thử test vô hiệu hóa blog và cái kết', NULL, 1, 'Đây là bài blog test vô hiệu hóa', 0, NULL, NULL, NULL, NULL);
INSERT INTO `blogs` VALUES (6, 'Sample Title', 'Author Name', 'This is a sample description.', 'profilePicUrl.jpg', 1, 'This is the content of the blog.', 0, '2024-07-07 11:18:06', 'Duyen', '2024-07-07 11:18:06', 'Duyen');

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`  (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                               `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
                               `profilePic` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                               `createdDate` timestamp NULL DEFAULT NULL,
                               `createdBy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                               `modifiedDate` timestamp NULL DEFAULT NULL,
                               `modifiedBy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of categories
-- ----------------------------
INSERT INTO `categories` VALUES (1, 'Gỗ', NULL, NULL, '2024-04-02 20:56:52', NULL, '2024-04-02 20:56:52', NULL);
INSERT INTO `categories` VALUES (2, 'Đan lát', NULL, NULL, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `categories` VALUES (3, 'Gốm sứ', NULL, NULL, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);

-- ----------------------------
-- Table structure for category_types
-- ----------------------------
DROP TABLE IF EXISTS `category_types`;
CREATE TABLE `category_types`  (
                                   `id` int NOT NULL AUTO_INCREMENT,
                                   `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                   `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
                                   `categoryId` int NOT NULL,
                                   `idOnBrowser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                   `status` int NOT NULL,
                                   `createdDate` timestamp NULL DEFAULT NULL,
                                   `createdBy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                   `modifiedDate` timestamp NULL DEFAULT NULL,
                                   `modifiedBy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                   PRIMARY KEY (`id`) USING BTREE,
                                   INDEX `categoryType_category`(`categoryId` ASC) USING BTREE,
                                   CONSTRAINT `categoryType_category` FOREIGN KEY (`categoryId`) REFERENCES `categories` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category_types
-- ----------------------------
INSERT INTO `category_types` VALUES (1, 'Lục bình gỗ', NULL, 1, 'luc-binh-go', 1, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `category_types` VALUES (2, 'Đĩa gỗ', NULL, 1, 'dia-trang-tri-go', 1, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `category_types` VALUES (3, 'Tượng gỗ', NULL, 1, 'tuong-go', 1, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `category_types` VALUES (4, 'Đồng hồ gỗ', NULL, 1, 'dong-ho-go', 1, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `category_types` VALUES (5, 'Nội thất gỗ', NULL, 1, 'noi-that-go', 2, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `category_types` VALUES (6, 'Túi xách', NULL, 2, 'tui-xach', 1, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `category_types` VALUES (7, 'Ví cầm tay', NULL, 2, 'vi-cam-tay', 1, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `category_types` VALUES (8, 'Hộp đựng', NULL, 2, 'hop-dung', 1, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `category_types` VALUES (9, 'Nón', NULL, 2, 'non', 1, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `category_types` VALUES (10, 'Nhà thú cưng', NULL, 2, 'nha-thu-cung', 1, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `category_types` VALUES (11, 'Thùng rác', NULL, 2, 'thung-rac', 1, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `category_types` VALUES (12, 'Khác', NULL, 2, 'khac', 1, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `category_types` VALUES (13, 'Bộ ấm trà', NULL, 3, 'bo-am-tra', 1, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `category_types` VALUES (14, 'Lọ', NULL, 3, 'lo', 1, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `category_types` VALUES (15, 'Bình phong thủy', NULL, 3, 'binh-phong-thuy', 1, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `category_types` VALUES (16, 'Tranh', NULL, 3, 'tranh', 1, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `category_types` VALUES (17, 'Thác nước & Tượng', NULL, 3, 'thac-nuoc-va-tuong', 1, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `category_types` VALUES (18, 'Ly, cốc, phin cà phê', NULL, 3, 'ly-coc-phin', 1, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `category_types` VALUES (19, 'Khay mứt', NULL, 3, 'khay-mut', 1, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `category_types` VALUES (20, 'Bộ bát đĩa', NULL, 3, 'bo-bat-dia', 1, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);

-- ----------------------------
-- Table structure for contacts
-- ----------------------------
DROP TABLE IF EXISTS `contacts`;
CREATE TABLE `contacts`  (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                             `firstName` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                             `lastName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                             `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                             `status` int NOT NULL,
                             `createdDate` timestamp NULL DEFAULT NULL,
                             `createdBy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `modifiedDate` timestamp NULL DEFAULT NULL,
                             `modifiedBy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of contacts
-- ----------------------------

-- ----------------------------
-- Table structure for customize_pages
-- ----------------------------
DROP TABLE IF EXISTS `customize_pages`;
CREATE TABLE `customize_pages`  (
                                    `id` int NOT NULL AUTO_INCREMENT,
                                    `welcomeTitle` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `welcomeDes` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `productTitle` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `productDes` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `prTitle1` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `prDes1` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `prIcon1` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `prContentTitle1` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `prContentDes1` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `prLink1` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `prLink1InStorage` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `prTitle2` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `prDes2` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `prContent2` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `prLink2` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `prLink2InStorage` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `background` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `backgroundInStorage` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `footerLeft` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `footerMiddle` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `facebookLink` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                    `twitterLink` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                    `instagramLink` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                    `linkedinLink` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customize_pages
-- ----------------------------
INSERT INTO `customize_pages` VALUES (1, 'Nghệ thuật từ mỹ nghệ', 'Mỗi một sản phẩm mỹ nghệ là một kiệt tác. Thưởng thức tinh túy của nghệ thuật thông qua các sản phẩm mỹ nghệ tuyệt vời của chúng tôi.', 'Sản phẩm mỹ nghệ với chất lượng tuyệt vời.', 'Khám phá, lựa chọn và trải nghiệm sản phẩm mỹ nghệ với chất lượng hàng đầu. Thưởng thức theo cách của bạn.', 'Vì sao chọn chúng tôi', 'Mang trong mình suy nghĩ \"mỗi một sản phẩm là một tác phẩm nghệ thuật\", chúng tôi luôn đảm bảo được sự nâng niu, chỉn chu trong từng sản phẩm cũng như sự tiện lợi, dễ dàng trong quá trình mua hàng của bạn.', '<i class=\"fa-thin fa-truck-fast fa-2xl\" style=\"color: #e3bd74;\"></i>,<i class=\"fa-thin fa-bag-shopping fa-2xl\" style=\"color: #e3bd74;\"></i>,<i class=\"fa-thin fa-question fa-2xl\" style=\"color: #e3bd74;\"></i>,<i class=\"fa-thin fa-arrows-repeat fa-2xl\" style=\"color: #e3bd74;\"></i>', 'Giao hàng nhanh chóng~Dễ dàng mua sắm~Hỗ trợ 24/7~Đổi trả miễn phí', 'Đảm bảo giao hàng tận tay bạn một cách nhanh chóng. Miễn phí giao hàng cho các đơn hàng giá trị cao.~Dễ dàng chọn lựa thông qua trang web cũng như tại cửa hàng trực tiếp. Sản phẩm được cập nhật thường xuyên.~Chúng tôi luôn sẵn sàng tư vấn và giải đáp cho bạn về việc mua hàng trực tuyến và các sản phẩm mỹ nghệ.~Đối với các đơn hàng bị lỗi do nhà sản xuất, chúng tôi sẵn sàng đổi trả sản phẩm mới cho bạn.', '', '', 'Dễ dàng mua sắm. Hỗ trợ tận tâm. Nghệ thuật trong tay bạn.', 'Dễ dàng mua sắm thông qua website của chúng tôi. Truy cập vào danh mục hàng mà bạn quan tâm, thêm vào giỏ hàng những sản phẩm mà bạn muốn mua, vào giỏ hàng và thanh toán. Dễ dàng sở hữu sản phẩm mỹ nghệ mà bạn yêu thích.', 'Truy cập vào mục sản phẩm~Chọn loại hàng bạn quan tâm~Thêm sản phẩm vào giỏ hàng~Vào giỏ hàng và thanh toán', '', '', '', '', 'Với chúng tôi, mỗi một sản phẩm mỹ nghệ đều là một kiệt tác, là một tác phẩm nghệ thuật. Cảm ơn bạn đã ghé thăm DDD. - Nghệ thuật mỹ nghệ. Mua sắm với chúng tôi trong mục sản phẩm, hoặc bấm vào nút Khám phá trên trang chủ.', 'Đừng quên theo dõi chúng tôi qua các kênh mạng xã hội sau để không bỏ lỡ nhưng thông tin mới nhất của DDD. - Nghệ thuật mỹ nghệ', '', '', '', '');
INSERT INTO `customize_pages` VALUES (2, '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for images
-- ----------------------------
DROP TABLE IF EXISTS `images`;
CREATE TABLE `images`  (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                           `link` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                           `productId` int NOT NULL,
                           `nameInStorage` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                           `createdDate` timestamp NULL DEFAULT NULL,
                           `createdBy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                           `modifiedDate` timestamp NULL DEFAULT NULL,
                           `modifiedBy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                           PRIMARY KEY (`id`) USING BTREE,
                           INDEX `image_product`(`productId` ASC) USING BTREE,
                           CONSTRAINT `image_product` FOREIGN KEY (`productId`) REFERENCES `products` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of images
-- ----------------------------

-- ----------------------------
-- Table structure for logs
-- ----------------------------
DROP TABLE IF EXISTS `logs`;
CREATE TABLE `logs`  (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                         `level` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                         `address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                         `previousValue` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
                         `currentValue` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
                         `national` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                         `createdDate` timestamp NOT NULL DEFAULT current_timestamp ON UPDATE CURRENT_TIMESTAMP,
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of logs
-- ----------------------------
INSERT INTO `logs` VALUES (1, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login\",\"userId\":-1,\"content\":\"login-success-active\"}', NULL, NULL, '', '2024-07-04 10:17:19');
INSERT INTO `logs` VALUES (2, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login\",\"userId\":-1,\"content\":\"login-success-active\"}', NULL, NULL, '', '2024-07-04 10:17:56');
INSERT INTO `logs` VALUES (3, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login\",\"userId\":-1,\"content\":\"login-success-active\"}', NULL, NULL, '', '2024-07-04 10:51:27');
INSERT INTO `logs` VALUES (4, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login\",\"userId\":-1,\"content\":\"login-success-active\"}', NULL, NULL, '', '2024-07-04 10:56:51');
INSERT INTO `logs` VALUES (5, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login\",\"userId\":-1,\"content\":\"login-success-active\"}', NULL, NULL, '', '2024-07-04 11:05:35');
INSERT INTO `logs` VALUES (6, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login\",\"userId\":-1,\"content\":\"login-success-active\"}', NULL, NULL, '', '2024-07-04 11:31:29');
INSERT INTO `logs` VALUES (7, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login\",\"userId\":-1,\"content\":\"login-success-active\"}', NULL, NULL, '', '2024-07-04 11:32:37');
INSERT INTO `logs` VALUES (8, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login\",\"userId\":-1,\"content\":\"login-success-active\"}', NULL, NULL, '', '2024-07-04 12:16:06');
INSERT INTO `logs` VALUES (9, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login\",\"userId\":-1,\"content\":\"login-success-active\"}', NULL, NULL, '', '2024-07-04 12:52:38');
INSERT INTO `logs` VALUES (10, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login\",\"userId\":-1,\"content\":\"login-success-active\"}', NULL, NULL, '', '2024-07-04 19:04:15');
INSERT INTO `logs` VALUES (11, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login\",\"userId\":-1,\"content\":\"login-success-active\"}', NULL, NULL, '', '2024-07-05 10:33:24');
INSERT INTO `logs` VALUES (12, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login\",\"userId\":-1,\"content\":\"login-success-active\"}', NULL, NULL, '', '2024-07-05 10:34:02');
INSERT INTO `logs` VALUES (13, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login\",\"userId\":-1,\"content\":\"login-success-active\"}', NULL, NULL, '', '2024-07-05 10:35:05');
INSERT INTO `logs` VALUES (14, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-05 18:42:19');
INSERT INTO `logs` VALUES (15, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-05 19:18:39');
INSERT INTO `logs` VALUES (16, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-06 20:06:29');
INSERT INTO `logs` VALUES (17, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-06 20:34:20');
INSERT INTO `logs` VALUES (18, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-07 09:08:31');
INSERT INTO `logs` VALUES (19, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-07 10:21:57');
INSERT INTO `logs` VALUES (20, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-07 10:22:54');
INSERT INTO `logs` VALUES (21, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-07 10:25:21');
INSERT INTO `logs` VALUES (22, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-07 10:26:32');
INSERT INTO `logs` VALUES (23, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-07 10:31:19');
INSERT INTO `logs` VALUES (24, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-07 11:05:02');
INSERT INTO `logs` VALUES (25, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-07 11:19:16');
INSERT INTO `logs` VALUES (26, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-07 11:31:43');
INSERT INTO `logs` VALUES (27, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-07 11:39:42');
INSERT INTO `logs` VALUES (28, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-07 11:49:33');
INSERT INTO `logs` VALUES (29, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-07 11:58:31');
INSERT INTO `logs` VALUES (30, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-07 12:03:00');
INSERT INTO `logs` VALUES (31, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-07 12:08:49');
INSERT INTO `logs` VALUES (32, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-07 12:11:10');
INSERT INTO `logs` VALUES (33, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-11 11:54:00');
INSERT INTO `logs` VALUES (34, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-11 11:58:16');
INSERT INTO `logs` VALUES (35, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":2,\"email\":\"nguyenthimyduyen8683@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":1,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":2,\"email\":\"nguyenthimyduyen8683@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":1,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-11 12:29:52');
INSERT INTO `logs` VALUES (36, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-11 12:48:48');
INSERT INTO `logs` VALUES (37, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-11 13:34:31');
INSERT INTO `logs` VALUES (38, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-13 12:35:43');
INSERT INTO `logs` VALUES (39, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-13 13:33:12');
INSERT INTO `logs` VALUES (40, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-13 13:41:10');
INSERT INTO `logs` VALUES (41, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-13 13:42:27');
INSERT INTO `logs` VALUES (42, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-13 13:47:51');
INSERT INTO `logs` VALUES (43, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-13 13:50:26');
INSERT INTO `logs` VALUES (44, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-13 14:18:26');
INSERT INTO `logs` VALUES (45, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":2,\"email\":\"nguyenthimyduyen8683@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":1,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":2,\"email\":\"nguyenthimyduyen8683@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":1,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-13 14:33:26');
INSERT INTO `logs` VALUES (46, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-13 14:43:28');
INSERT INTO `logs` VALUES (47, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":2,\"email\":\"nguyenthimyduyen8683@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":1,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":2,\"email\":\"nguyenthimyduyen8683@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":1,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-13 14:59:01');
INSERT INTO `logs` VALUES (48, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":2,\"email\":\"nguyenthimyduyen8683@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":1,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":2,\"email\":\"nguyenthimyduyen8683@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":1,\"status\":1,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-13 15:05:19');
INSERT INTO `logs` VALUES (49, '0:0:0:0:0:0:0:1', '2', '{\"function\":\"register\",\"userId\":-1,\"content\":\"Đăng ký tài khoản mới thành công\"}', NULL, '{\"id\":5,\"email\":\"lamnguyennhat102@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":1,\"status\":2,\"viaOAuth\":0,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-17 16:07:54');
INSERT INTO `logs` VALUES (50, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":2,\"email\":\"nguyenthimyduyen8683@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":1,\"status\":1,\"viaOAuth\":0,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":2,\"email\":\"nguyenthimyduyen8683@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":1,\"status\":1,\"viaOAuth\":0,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-17 17:50:25');
INSERT INTO `logs` VALUES (51, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":2,\"email\":\"nguyenthimyduyen8683@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":1,\"status\":1,\"viaOAuth\":0,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":2,\"email\":\"nguyenthimyduyen8683@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":1,\"status\":1,\"viaOAuth\":0,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-18 11:24:22');
INSERT INTO `logs` VALUES (52, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":2,\"email\":\"nguyenthimyduyen8683@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":1,\"status\":1,\"viaOAuth\":0,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":2,\"email\":\"nguyenthimyduyen8683@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":1,\"status\":1,\"viaOAuth\":0,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-18 12:52:16');
INSERT INTO `logs` VALUES (53, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":2,\"email\":\"nguyenthimyduyen8683@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":1,\"status\":1,\"viaOAuth\":0,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":2,\"email\":\"nguyenthimyduyen8683@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":1,\"status\":1,\"viaOAuth\":0,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-18 15:09:36');
INSERT INTO `logs` VALUES (54, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"viaOAuth\":0,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"viaOAuth\":0,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-18 15:53:54');
INSERT INTO `logs` VALUES (55, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"viaOAuth\":0,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"viaOAuth\":0,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-18 16:39:54');
INSERT INTO `logs` VALUES (56, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"viaOAuth\":0,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"viaOAuth\":0,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-18 18:50:23');
INSERT INTO `logs` VALUES (57, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":2,\"email\":\"nguyenthimyduyen8683@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":1,\"status\":1,\"viaOAuth\":0,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":2,\"email\":\"nguyenthimyduyen8683@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":1,\"status\":1,\"viaOAuth\":0,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-18 18:52:39');
INSERT INTO `logs` VALUES (58, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":2,\"email\":\"nguyenthimyduyen8683@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":1,\"status\":1,\"viaOAuth\":0,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":2,\"email\":\"nguyenthimyduyen8683@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":1,\"status\":1,\"viaOAuth\":0,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-19 09:25:22');
INSERT INTO `logs` VALUES (59, '0:0:0:0:0:0:0:1', '1', '{\"function\":\"login-active\",\"userId\":-1,\"content\":\"Đăng nhập thành công (Đã active)\"}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"viaOAuth\":0,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '{\"id\":1,\"email\":\"Duyendethx2k3@gmail.com\",\"password\":null,\"firstName\":null,\"lastName\":null,\"roleId\":2,\"status\":1,\"viaOAuth\":0,\"verifiedCode\":null,\"key\":null,\"addressLine\":null,\"addressWard\":null,\"addressDistrict\":null,\"addressProvince\":null,\"createdDate\":null,\"createdBy\":null,\"modifiedDate\":null,\"modifiedBy\":null}', '', '2024-07-19 09:25:42');

-- ----------------------------
-- Table structure for order_details
-- ----------------------------
DROP TABLE IF EXISTS `order_details`;
CREATE TABLE `order_details` (
                                 `id` INT NOT NULL AUTO_INCREMENT,
                                 `orderId` INT NOT NULL,
                                 `productId` INT NOT NULL,
                                 `quantity` INT NOT NULL,
                                 `reviewed` INT NOT NULL,
                                 PRIMARY KEY (`id`), -- ✅ id là khóa chính
                                 UNIQUE KEY unique_order_product (`orderId`, `productId`), -- ✅ đảm bảo không trùng sản phẩm trong cùng 1 đơn
                                 INDEX `detail_product`(`productId` ASC),
                                 CONSTRAINT `detail_product` FOREIGN KEY (`productId`) REFERENCES `products` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of order_details
-- ----------------------------

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `userId` int NOT NULL,
                           `createdDate` timestamp NULL DEFAULT NULL,
                           `shipToDate` timestamp NULL DEFAULT NULL,
                           `total` double NOT NULL,
                           `paymentMethod` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                           `status` int NOT NULL,
                           `createdBy` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                           `modifiedDate` timestamp NULL DEFAULT NULL,
                           `modifiedBy` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                           PRIMARY KEY (`id`) USING BTREE,
                           INDEX `order_user`(`userId` ASC) USING BTREE,
                           CONSTRAINT `order_user` FOREIGN KEY (`userId`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (0, 2, '2024-07-11 13:35:20', '2024-07-22 13:01:49', 330, 'Thanh toán khi nhận hàng', 2, 'Duyen', NULL, NULL);
INSERT INTO `orders` VALUES (3, 1, '2024-07-11 13:41:19', '2024-07-18 13:41:26', 530, 'Thanh toán khi nhận hàng', 3, 'Duyen', NULL, NULL);

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`  (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                             `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                             `categoryTypeId` int NOT NULL,
                             `originalPrice` double NOT NULL,
                             `discountPrice` double NOT NULL,
                             `discountPercent` double NOT NULL,
                             `quantity` int NOT NULL,
                             `size` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                             `otherSpec` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `status` int NOT NULL,
                             `keyword` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                             `createdDate` timestamp NULL DEFAULT NULL,
                             `createdBy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `modifiedDate` timestamp NULL DEFAULT NULL,
                             `modifiedBy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             PRIMARY KEY (`id`) USING BTREE,
                             INDEX `product_categoryType`(`categoryTypeId` ASC) USING BTREE,
                             CONSTRAINT `product_categoryType` FOREIGN KEY (`categoryTypeId`) REFERENCES `category_types` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of products
-- ----------------------------
INSERT INTO `products` VALUES (1, 'Cặp lục bình gỗ hương đá', 'Lục bình bằng gỗ có ý nghĩa đặc biệt trong phong thủy. Người ta nói rằng những bông hoa này sẽ mang lại cho bạn may mắn và tài lộc.', 1, 5490000, 4950000, 10, 21, '100 x 27 x 27 (cm)', 'Gỗ hương đá', 1, 'lục bình gỗ, hương đá, phong thủy, lớn', '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `products` VALUES (2, 'Bình phú quý gỗ cẩm', 'Bình Phú Quý Gỗ Cẩm Cao 30cm nghệ thuật để trưng bày tủ kính, kệ tivi phòng khách.', 1, 2490000, 2249000, 10, 14, '33 x 17 x 17 (cm)', 'Gỗ cẩm', 1, 'bình gỗ, cẩm, nghệ thuật, trưng bày, nhỏ', '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                          `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                          `status` int NOT NULL,
                          `createdDate` timestamp NULL DEFAULT NULL,
                          `createdBy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                          `modifiedDate` timestamp NULL DEFAULT NULL,
                          `modifiedBy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES (1, 'Client', NULL, 1, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);
INSERT INTO `roles` VALUES (2, 'Admin', NULL, 1, '2024-04-02 20:56:53', NULL, '2024-04-02 20:56:53', NULL);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                          `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                          `firstName` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                          `lastName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                          `roleId` int NOT NULL,
                          `status` int NOT NULL,
                          `verifiedCode` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                          `changePwHash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                          `expiredTime` timestamp NULL DEFAULT NULL,
                          `addressLine` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                          `addressWard` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                          `addressDistrict` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                          `addressProvince` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                          `createdDate` timestamp NULL DEFAULT NULL,
                          `modifiedDate` timestamp NULL DEFAULT NULL,
                          `viaOAuth` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                          PRIMARY KEY (`id`) USING BTREE,
                          INDEX `users_role`(`roleId` ASC) USING BTREE,
                          CONSTRAINT `users_role` FOREIGN KEY (`roleId`) REFERENCES `roles` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'Duyendethx2k3@gmail.com', '$2a$10$4nTraEi2PBeStIGSZuHPXua0dzqtjoZR4W85WcNVga6my3XzBxeCy', NULL, NULL, 2, 1, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `users` VALUES (2, 'nguyenthimyduyen8683@gmail.com', '$2a$10$ZkFfbYtsP3o/iKMR7UOuqeCOJmrTmKibbaux6SHPtf0JPnRjybcTO', NULL, NULL, 1, 1, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `users` VALUES (4, 'testxoa', '', NULL, NULL, 1, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `users` VALUES (5, 'lamnguyennhat102@gmail.com', '$2a$10$2tVZ9O/gbn5n554x0ONd.uO5N3OM79i0CU.R81x265e5p7SgKzsC.', NULL, NULL, 1, 2, 'N04HCZUL', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
