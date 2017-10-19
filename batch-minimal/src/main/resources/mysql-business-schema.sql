DROP TABLE IF EXISTS FILE_INFO;

CREATE TABLE FILE_INFO  (
    file_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    file_name VARCHAR(128),
    file_path VARCHAR(1024),
    file_created BIGINT,
    file_last_modified BIGINT,
	file_owner VARCHAR(128),
    file_size BIGINT
);

