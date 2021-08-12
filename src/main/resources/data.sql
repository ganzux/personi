CREATE TABLE employee (
       user_id VARCHAR(15) PRIMARY KEY,
       manager VARCHAR(15) DEFAULT NULL,
       CONSTRAINT `employee_to_employee` FOREIGN KEY (`manager`) REFERENCES `employee` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

