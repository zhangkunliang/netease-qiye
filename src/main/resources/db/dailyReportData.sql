DROP TABLE IF EXISTS MESSAGE;

-- auto-generated definition
CREATE TABLE MESSAGE (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         from_who VARCHAR(255) NOT NULL,
                         subject VARCHAR(255) NOT NULL,
                         to_who VARCHAR(255) NOT NULL,
                         content_type VARCHAR(255),
                         content TEXT,
                         mail_size_byte VARCHAR(255),
                         sent_date VARCHAR(255),
                         charset VARCHAR(255),
                         attachment VARCHAR(255)
);


