-- Users 테이블 (Java @Table("Users"))
CREATE TABLE IF NOT EXISTS Users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       user_id VARCHAR(50)     NOT NULL UNIQUE,
                       password VARCHAR(255)   NOT NULL,
                       nickname VARCHAR(100),
                       email VARCHAR(255)      NOT NULL UNIQUE,
                       phone VARCHAR(20),
                       status INT              NOT NULL DEFAULT 1,
                       created_date DATETIME   NOT NULL,
                       modified_date DATETIME  NOT NULL
);

-- comment 테이블
CREATE TABLE IF NOT EXISTS comment (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         board_id INT            NOT NULL,
                         nickname VARCHAR(100)   NOT NULL,
                         content TEXT            NOT NULL,
                         status INT              NOT NULL DEFAULT 1,
                         created_date DATETIME   NOT NULL,
                         modified_date DATETIME  NOT NULL
);

-- reply 테이블
CREATE TABLE IF NOT EXISTS reply (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       comment_id BIGINT       NOT NULL,
                       nickname VARCHAR(100)   NOT NULL,
                       content TEXT            NOT NULL,
                       status INT              NOT NULL DEFAULT 1,
                       created_date DATETIME   NOT NULL,
                       modified_date DATETIME  NOT NULL,
                       FOREIGN KEY (comment_id) REFERENCES comment(id)
);
