USE `hiperquiz`;

DROP TABLE IF EXISTS `quizes_results`;
DROP TABLE IF EXISTS `answers`;
DROP TABLE IF EXISTS `questions`;
DROP TABLE IF EXISTS `quizes`;
DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
`id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
`username` VARCHAR(15) NOT NULL,
`email` VARCHAR(45) NOT NULL,
`password` VARCHAR(15) NOT NULL,
`gender` TINYINT NOT NULL,
`role` TINYINT NOT NULL DEFAULT 0,
`picture_url` VARCHAR(250) DEFAULT 'default picture',
`description` VARCHAR(250),
`metadata` VARCHAR(512),
`status` TINYINT NOT NULL DEFAULT 1,
`created` DATETIME DEFAULT NOW(),
`modified` DATETIME DEFAULT NOW()
);

CREATE TABLE `quizes` (
`id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
`author_id` INT UNSIGNED NOT NULL,
`description` VARCHAR(250) NOT NULL,
`expected_duration` INT UNSIGNED NOT NULL,
`picture_url` VARCHAR(250) NOT NULL DEFAULT 'default picture',
`tags` VARCHAR(250),
INDEX `fk_quizes_users_idx` (`author_id`),
CONSTRAINT `fk_quizes_users`
FOREIGN KEY (`author_id`)
REFERENCES `users`(`id`),
`created` DATETIME DEFAULT NOW(),
`modified` DATETIME DEFAULT NOW()
);

CREATE TABLE `questions` (
`id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
`quiz_id` INT UNSIGNED NOT NULL,
`text` VARCHAR(300) NOT NULL,
`picture_url` VARCHAR(250),
INDEX `fk_questions_quizes_idx` (`quiz_id`),
CONSTRAINT `fk_questions_quizes`
FOREIGN KEY (`quiz_id`)
REFERENCES `quizes`(`id`),
`created` DATETIME DEFAULT NOW(),
`modified` DATETIME DEFAULT NOW()
);

CREATE TABLE `answers` (
`id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
`question_id` INT UNSIGNED NOT NULL,
`text` VARCHAR(150) NOT NULL,
`picture_url` VARCHAR(250),
`score` INT NOT NULL,
INDEX `fk_answers_questions_idx` (`question_id`),
CONSTRAINT `fk_answers_questions`
FOREIGN KEY (`question_id`)
REFERENCES `questions`(`id`),
`created` DATETIME DEFAULT NOW(),
`modified` DATETIME DEFAULT NOW()
);

CREATE TABLE `quizes_results` (
`id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
`quiz_id` INT UNSIGNED NOT NULL,
`user_id` INT UNSIGNED NOT NULL,
`score` INT UNSIGNED NOT NULL,
INDEX `fk_quizes_results_quizes_idx` (`quiz_id`),
CONSTRAINT `fk_quizes_results_quizes`
FOREIGN KEY (`quiz_id`)
REFERENCES `quizes`(`id`),
INDEX `fk_quizes_results_users_idx` (`user_id`),
CONSTRAINT `fk_quizes_results_users`
FOREIGN KEY (`user_id`)
REFERENCES `users`(`id`),
`created` DATETIME DEFAULT NOW(),
`modified` DATETIME DEFAULT NOW()
);
