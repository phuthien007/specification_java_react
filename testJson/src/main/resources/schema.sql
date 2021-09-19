DROP TABLE IF EXISTS tbl_user;

CREATE TABLE tbl_user (
                          id INT AUTO_INCREMENT  PRIMARY KEY,
                          username VARCHAR(250) NOT NULL,
                          password VARCHAR(250) NOT NULL
);

DROP TABLE IF EXISTS tbl_test;

CREATE TABLE tbl_test (
                          id INT AUTO_INCREMENT  PRIMARY KEY,
                          bio VARCHAR(250) NOT NULL,
                          slider INT NOT NULL
);