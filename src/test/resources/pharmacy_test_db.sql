DROP TABLE IF EXISTS `pharmacy_test_db`.`prescriptions`;
DROP TABLE IF EXISTS `pharmacy_test_db`.`basket`;
DROP TABLE IF EXISTS `pharmacy_test_db`.`orders`;
DROP TABLE IF EXISTS `pharmacy_test_db`.`users`;
DROP TABLE IF EXISTS `pharmacy_test_db`.`roles`;
DROP TABLE IF EXISTS `pharmacy_test_db`.`products_local`;
DROP TABLE IF EXISTS `pharmacy_test_db`.`products`;


-- -----------------------------------------------------
-- Table `pharmacy_test_db`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pharmacy_test_db`.`products` (
  `id_product` INT(11) NOT NULL AUTO_INCREMENT,
  `is_need_prescription` BIT NOT NULL DEFAULT 0,
  `pr_quantity` INT(11) NOT NULL DEFAULT 0,
  `price` DECIMAL(20,2) NULL,
  PRIMARY KEY (`id_product`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pharmacy_test_db`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pharmacy_test_db`.`roles` (
  `id_role` TINYINT NOT NULL AUTO_INCREMENT,
  `role` VARCHAR(15) NULL,
  PRIMARY KEY (`id_role`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pharmacy_test_db`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pharmacy_test_db`.`users` (
  `id_user` INT(11) NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(60) NOT NULL,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `name` VARCHAR(45) NULL,
  `middlename` VARCHAR(45) NULL,
  `surname` VARCHAR(45) NULL,
  `address` VARCHAR(60) NULL,
  `passport` VARCHAR(45) NULL,
  `telephone` VARCHAR(20) NULL,
  `id_role` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_user`),
  INDEX `id_role_idx` (`id_role` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC),
  CONSTRAINT `id_role`
    FOREIGN KEY (`id_role`)
    REFERENCES `pharmacy_test_db`.`roles` (`id_role`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pharmacy_test_db`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pharmacy_test_db`.`orders` (
  `id_order` INT NOT NULL AUTO_INCREMENT,
  `id_user` INT NOT NULL,
  `id_product` INT NOT NULL,
  `order_quantity` DECIMAL(20,2) UNSIGNED NULL,
  `order_price` DOUBLE UNSIGNED NULL,
  `order_date` TIMESTAMP NULL,
  PRIMARY KEY (`id_order`),
  INDEX `id_user_idx` (`id_user` ASC),
  INDEX `id_product_idx` (`id_product` ASC),
  CONSTRAINT `id_user_order`
    FOREIGN KEY (`id_user`)
    REFERENCES `pharmacy_test_db`.`users` (`id_user`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `id_product_order`
    FOREIGN KEY (`id_product`)
    REFERENCES `pharmacy_test_db`.`products` (`id_product`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pharmacy_test_db`.`basket`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pharmacy_test_db`.`basket` (
  `id_user` INT NULL,
  `id_product` INT NULL,
  `basket_quantity` INT NULL,
  INDEX `id_user_idx` (`id_user` ASC),
  INDEX `id_product_idx` (`id_product` ASC),
  UNIQUE INDEX `basket` (`id_user` ASC, `id_product` ASC),
  CONSTRAINT `id_user_basket`
    FOREIGN KEY (`id_user`)
    REFERENCES `pharmacy_test_db`.`users` (`id_user`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `id_product_basket`
    FOREIGN KEY (`id_product`)
    REFERENCES `pharmacy_test_db`.`products` (`id_product`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pharmacy_test_db`.`prescriptions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pharmacy_test_db`.`prescriptions` (
  `id_user` INT NOT NULL,
  `id_product` INT NOT NULL,
  `date_start` DATE NULL,
  `date_end` DATE NULL,
  `getting_request_status` ENUM('WAITING', 'SATISFY') NOT NULL DEFAULT 'WAITING',
  `extending_request_status` ENUM('WAITING', 'SATISFY') NULL,
  `request_date` TIMESTAMP NOT NULL,
  INDEX `id_user_idx` (`id_user` ASC),
  INDEX `id_product_idx` (`id_product` ASC),
  UNIQUE INDEX `prescription` (`id_user` ASC, `id_product` ASC),
  CONSTRAINT `id_user`
    FOREIGN KEY (`id_user`)
    REFERENCES `pharmacy_test_db`.`users` (`id_user`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `id_product`
    FOREIGN KEY (`id_product`)
    REFERENCES `pharmacy_test_db`.`products` (`id_product`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pharmacy_test_db`.`products_local`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pharmacy_test_db`.`products_local` (
  `id_product` INT NOT NULL,
  `locale` ENUM('EN_US', 'RU_BY') NOT NULL,
  `name` VARCHAR(80) NOT NULL,
  `non_patent_name` VARCHAR(60) NULL,
  `producer` VARCHAR(60) NULL,
  `form` VARCHAR(60) NULL,
  `instruction` VARCHAR(80) NULL,
  PRIMARY KEY (`id_product`, `locale`),
  CONSTRAINT `fk_products`
    FOREIGN KEY (`id_product`)
    REFERENCES `pharmacy_test_db`.`products` (`id_product`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Data for table `pharmacy_test_db`.`products`
-- -----------------------------------------------------
START TRANSACTION;
USE `pharmacy_test_db`;
INSERT INTO `pharmacy_test_db`.`products` (`id_product`, `is_need_prescription`, `pr_quantity`, `price`) VALUES (1, 0, 30, 21.68);
INSERT INTO `pharmacy_test_db`.`products` (`id_product`, `is_need_prescription`, `pr_quantity`, `price`) VALUES (2, 0, 30, 13.06);
INSERT INTO `pharmacy_test_db`.`products` (`id_product`, `is_need_prescription`, `pr_quantity`, `price`) VALUES (3, 1, 40, 47.60);
INSERT INTO `pharmacy_test_db`.`products` (`id_product`, `is_need_prescription`, `pr_quantity`, `price`) VALUES (4, 1, 40, 100.00);
INSERT INTO `pharmacy_test_db`.`products` (`id_product`, `is_need_prescription`, `pr_quantity`, `price`) VALUES (5, 1, 40, 89.92);
INSERT INTO `pharmacy_test_db`.`products` (`id_product`, `is_need_prescription`, `pr_quantity`, `price`) VALUES (6, 1, 40, 89.70);
INSERT INTO `pharmacy_test_db`.`products` (`id_product`, `is_need_prescription`, `pr_quantity`, `price`) VALUES (7, 1, 60, 0.50);
INSERT INTO `pharmacy_test_db`.`products` (`id_product`, `is_need_prescription`, `pr_quantity`, `price`) VALUES (8, 1, 60, 2.04);
INSERT INTO `pharmacy_test_db`.`products` (`id_product`, `is_need_prescription`, `pr_quantity`, `price`) VALUES (9, 1, 60, 3.85);
INSERT INTO `pharmacy_test_db`.`products` (`id_product`, `is_need_prescription`, `pr_quantity`, `price`) VALUES (10, 0, 30, 10.73);
INSERT INTO `pharmacy_test_db`.`products` (`id_product`, `is_need_prescription`, `pr_quantity`, `price`) VALUES (11, 0, 30, 22.46);
INSERT INTO `pharmacy_test_db`.`products` (`id_product`, `is_need_prescription`, `pr_quantity`, `price`) VALUES (12, 0, 30, 5.93);
INSERT INTO `pharmacy_test_db`.`products` (`id_product`, `is_need_prescription`, `pr_quantity`, `price`) VALUES (13, 0, 30, 1.80);
INSERT INTO `pharmacy_test_db`.`products` (`id_product`, `is_need_prescription`, `pr_quantity`, `price`) VALUES (14, 0, 15, 2.40);

COMMIT;


-- -----------------------------------------------------
-- Data for table `pharmacy_test_db`.`roles`
-- -----------------------------------------------------
START TRANSACTION;
USE `pharmacy_test_db`;
INSERT INTO `pharmacy_test_db`.`roles` (`id_role`, `role`) VALUES (1, 'CUSTOMER');
INSERT INTO `pharmacy_test_db`.`roles` (`id_role`, `role`) VALUES (2, 'PHARMACIST');
INSERT INTO `pharmacy_test_db`.`roles` (`id_role`, `role`) VALUES (3, 'DOCTOR');

COMMIT;


-- -----------------------------------------------------
-- Data for table `pharmacy_test_db`.`users`
-- -----------------------------------------------------
START TRANSACTION;
USE `pharmacy_test_db`;
INSERT INTO `pharmacy_test_db`.`users` (`id_user`, `email`, `login`, `password`, `name`, `middlename`, `surname`, `address`, `passport`, `telephone`, `id_role`) VALUES (1, 'user@mail.ru', 'user', '04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `pharmacy_test_db`.`users` (`id_user`, `email`, `login`, `password`, `name`, `middlename`, `surname`, `address`, `passport`, `telephone`, `id_role`) VALUES (2, 'pharmacist@mail.ru', 'pharmacist', '2e9926caf5a34b3625f9f73567680a47947dc4e7beb4fc454273535a87d68c4e', NULL, NULL, NULL, NULL, NULL, NULL, 2);
INSERT INTO `pharmacy_test_db`.`users` (`id_user`, `email`, `login`, `password`, `name`, `middlename`, `surname`, `address`, `passport`, `telephone`, `id_role`) VALUES (3, 'doctor@mail.ru', 'doctor', '72f4be89d6ebab1496e21e38bcd7c8ca0a68928af3081ad7dff87e772eb350c2', NULL, NULL, NULL, NULL, NULL, NULL, 3);
INSERT INTO `pharmacy_test_db`.`users` (`id_user`, `email`, `login`, `password`, `name`, `middlename`, `surname`, `address`, `passport`, `telephone`, `id_role`) VALUES (4, 'petrov@mail.ru', 'petrov', 'cf13fe2cff28343e661d1ada6a36a7bc04a734ffa5647c8ea767c632032e930a', 'Petr', 'Petrovich', 'Petrov', 'Minsk', 'MP123409', '775-34-56', 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `pharmacy_test_db`.`orders`
-- -----------------------------------------------------
START TRANSACTION;
USE `pharmacy_test_db`;
INSERT INTO `pharmacy_test_db`.`orders` (`id_order`, `id_user`, `id_product`, `order_quantity`, `order_price`, `order_date`) VALUES (1, 2, 3, 5, 4.13, '2018-01-16 12:00:00');
INSERT INTO `pharmacy_test_db`.`orders` (`id_order`, `id_user`, `id_product`, `order_quantity`, `order_price`, `order_date`) VALUES (2, 2, 7, 8, 5.60, '2018-01-16 12:00:00');

COMMIT;


-- -----------------------------------------------------
-- Data for table `pharmacy_test_db`.`basket`
-- -----------------------------------------------------
START TRANSACTION;
USE `pharmacy_test_db`;
INSERT INTO `pharmacy_test_db`.`basket` (`id_user`, `id_product`, `basket_quantity`) VALUES (1, 7, 20);
INSERT INTO `pharmacy_test_db`.`basket` (`id_user`, `id_product`, `basket_quantity`) VALUES (1, 6, 2);
INSERT INTO `pharmacy_test_db`.`basket` (`id_user`, `id_product`, `basket_quantity`) VALUES (1, 2, 1);
INSERT INTO `pharmacy_test_db`.`basket` (`id_user`, `id_product`, `basket_quantity`) VALUES (4, 7, 50);

COMMIT;


-- -----------------------------------------------------
-- Data for table `pharmacy_test_db`.`prescriptions`
-- -----------------------------------------------------
START TRANSACTION;
USE `pharmacy_test_db`;
INSERT INTO `pharmacy_test_db`.`prescriptions` (`id_user`, `id_product`, `date_start`, `date_end`, `getting_request_status`, `extending_request_status`, `request_date`) VALUES (1, 7, '2018-01-16', '2018-04-16', 'SATISFY', 'WAITING', '2018-01-16 12:00:00');
INSERT INTO `pharmacy_test_db`.`prescriptions` (`id_user`, `id_product`, `date_start`, `date_end`, `getting_request_status`, `extending_request_status`, `request_date`) VALUES (1, 6, '2018-01-16', '2018-04-16', 'SATISFY', 'WAITING', '2018-01-16 12:10:00');
INSERT INTO `pharmacy_test_db`.`prescriptions` (`id_user`, `id_product`, `date_start`, `date_end`, `getting_request_status`, `extending_request_status`, `request_date`) VALUES (2, 1, '2018-02-16', '2018-05-16', 'WAITING', NULL, '2018-01-16 12:00:00');
INSERT INTO `pharmacy_test_db`.`prescriptions` (`id_user`, `id_product`, `date_start`, `date_end`, `getting_request_status`, `extending_request_status`, `request_date`) VALUES (3, 1, '2018-02-16', '2018-05-16', 'WAITING', NULL, '2018-01-16 11:00:00');
INSERT INTO `pharmacy_test_db`.`prescriptions` (`id_user`, `id_product`, `date_start`, `date_end`, `getting_request_status`, `extending_request_status`, `request_date`) VALUES (3, 2, '2018-02-16', '2018-05-16', 'WAITING', NULL, '2018-01-16 10:50:00');

COMMIT;


-- -----------------------------------------------------
-- Data for table `pharmacy_test_db`.`products_local`
-- -----------------------------------------------------
START TRANSACTION;
USE `pharmacy_test_db`;
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (1, 'RU_BY', 'ГРОПРИНОСИН', 'Инозин пранобекс', 'Гедеон Рихтер / Польша', 'таб 500мг N50', 'groprinosin_ru.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (1, 'EN_US', 'GROPRINOSINE', 'Inosin pranobeks', 'Gedeon Richter / Poland', 'tab 500mg N50', 'groprinosin_en.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (2, 'RU_BY', 'ГРОПРИНОСИН-РИХТЕР', 'Инозин пранобекс', 'Гедеон Рихтер / Польша', 'сироп 250мг/5мл 150мл N1', 'groprinosin_ru.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (2, 'EN_US', 'GROPRINOSINE-RICHTER', 'Inosin pranobeks', 'Gedeon Richter / Poland', 'sirup 250mg/5ml 150ml  N1', 'groprinosin_en.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (3, 'RU_BY', 'КСАРЕЛТО', 'Ривароксабан', 'Байер Фарма / Германия', 'таб 10мг N10', 'ksalerto_ru.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (3, 'EN_US', 'XARELTO', 'Rivaroxaban', 'Bayer Pharma/Germany', 'tab 10mg N10', 'ksalerto_en.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (4, 'RU_BY', 'КСАРЕЛТО', 'Ривароксабан', 'Байер Фарма / Германия', 'таб 10мг N30', 'ksalerto_ru.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (4, 'EN_US', 'XARELTO', 'Rivaroxaban', 'Bayer Pharma/Germany', 'tab 10mg N30', 'ksalerto_en.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (5, 'RU_BY', 'КСАРЕЛТО', 'Ривароксабан', 'Байер Фарма / Германия', 'таб 20мг N28', 'ksalerto_ru.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (5, 'EN_US', 'XARELTO', 'Rivaroxaban', 'Bayer Pharma/Germany', 'tab 20mg N28', 'ksalerto_en.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (6, 'RU_BY', 'КСАРЕЛТО', 'Ривароксабан', 'Байер Фарма / Германия', 'таб 15мг N28', 'ksalerto_ru.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (6, 'EN_US', 'XARELTO', 'Rivaroxaban', 'Bayer Pharma/Germany', 'tab 15mg N28', 'ksalerto_en.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (7, 'RU_BY', 'АЗИТРОМИЦИН', 'Азитромицин', 'Белмедпрепараты / Беларусь', 'капс 250мг N6', 'azithromycin_ru.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (7, 'EN_US', 'AZITHROMYCIN', 'Azithromycin', 'Belmedpreparates/Belarus', 'caps 250mg N6', 'azithromycin_en.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (8, 'RU_BY', 'АЗИТРОМИЦИН', 'Азитромицин', 'Борисовский ЗМП / Беларусь', 'капс 250мг N6', 'azithromycin_ru.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (8, 'EN_US', 'AZITHROMYCIN', 'Azithromycin', 'Borisov PMP/Belarus', 'caps 250mg N6', 'azithromycin_en.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (9, 'RU_BY', 'БЕНТИЗИН', 'Азитромицин', 'Фармлэнд / Беларусь', 'таб 250мг N6', 'azithromycin_ru.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (9, 'EN_US', 'BENTIZIN', 'Azithromycin', 'PharmLand / Belarus', 'tab 250mg N6', 'azithromycin_en.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (10, 'RU_BY', 'МИЛДРОНАТ', 'Мельдоний', 'Гриндекс / Латвия', 'капс 250мг N20', 'mildronat_ru.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (10, 'EN_US', 'MILDRONATE', 'Meldonium', 'Grindex / Latvia', 'caps 250 mg 20', 'mildronat_en.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (11, 'RU_BY', 'МИЛДРОНАТ', 'Мельдоний', 'Гриндекс / Латвия', 'капс 250мг N40', 'mildronat_ru.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (11, 'EN_US', 'MILDRONATE', 'Meldonium', 'Grindex / Latvia', 'caps 250 mg  N40', 'mildronat_en.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (12, 'RU_BY', 'МИЛДРОКАРД', 'Мельдоний', 'Белмедпрепараты / Беларусь', 'капс 250мг N40', 'mildrokard_ru.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (12, 'EN_US', 'MILDROKARD', 'Meldonium', 'Belmedpreparates/Belarus', 'caps 250 mg  N40', 'mildrokard_en.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (13, 'RU_BY', 'МИЛДРОКАРД', 'Мельдоний', 'Белмедпрепараты / Беларусь', 'капс 250мг N20', 'mildrokard_ru.pdf');
INSERT INTO `pharmacy_test_db`.`products_local` (`id_product`, `locale`, `name`, `non_patent_name`, `producer`, `form`, `instruction`) VALUES (13, 'EN_US', 'MILDROKARD', 'Meldonium', 'Belmedpreparates/Belarus', 'caps 250 mg  N40', 'mildrokard_en.pdf');

COMMIT;

