DROP DATABASE meter;
CREATE DATABASE meter;
USE meter;

CREATE TABLE person (
	person_id		 INT PRIMARY KEY AUTO_INCREMENT,
	name       VARCHAR(30),
	city	    VARCHAR(30)
);

CREATE TABLE  payment (
    payment_id		 INT PRIMARY KEY AUTO_INCREMENT,
    amount                         INT,
    payment_date	               DATE,
    month_of_calculation           INT,
    person_id 		INT,
    foreign key (person_id) references person(person_id)
);

INSERT INTO person VALUES (1, 'Ivan Ivanovych Ivanov', 'Gomel');
INSERT INTO person VALUES (2, 'Dmitry Vitalyevich Gogol', 'Gomel');
INSERT INTO person VALUES (3, 'Eyhenyi Alexandrovich Alehin', 'Gomel');
