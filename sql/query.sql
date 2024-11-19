DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS bookmark;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS users;


CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       gmail VARCHAR(255) NOT NULL
);

CREATE TABLE tag (
                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                     tagname VARCHAR(255) NOT NULL
);


CREATE TABLE bookmark (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          tagname VARCHAR(255),
                          url VARCHAR(255) NOT NULL,
                          description TEXT,
                          image_url VARCHAR(255)
);

