USE `hiperquiz`;

INSERT INTO `users`(`username`,`email`,`password`,`gender`) VALUES
('emilia','emilia@gmail.com','Pa$$w0rd',1),
('valentin','valentin@gmail.com','Asd123*',0),
('daniela','daniela@gmail.com','Pa55w0rd',1),
('plamen','plamen@gmail.com','123qwE',0);

INSERT INTO `quizes`(`author_id`,`description`,`expected_duration`) VALUES
(1,'English Quiz',10),
(2,'Java Quiz',10);

INSERT INTO `questions`(`quiz_id`,`text`) VALUES
(1,'There ______ a red car parked in our driveway.'),
(1,'There ______ six eggs in the fridge.'),
(1,'There ______ many options to pick from.'),
(2,'What is a correct syntax to output "Hello World" in Java?'),
(2,'Which data type is used to create a variable that should store text?'),
(2,'How do you create a variable with the numeric value 5?');

INSERT INTO `answers`(`question_id`,`text`,`score`) VALUES
(1,'is',5),
(1,'are',0),
(2,'is',0),
(2,'are',10),
(3,'is',0),
(3,'are',10),
(4,'Console.WriteLine("Hello World")',0),
(4,'System.out.println("Hello World")',5),
(5,'String',10),
(5,'char',0),
(6,'int x = 5;',10),
(6,'x = 5;',0);

INSERT INTO `quizes_results`(`quiz_id`,`user_id`,`score`) VALUES
(2,1,10),
(1,3,25);