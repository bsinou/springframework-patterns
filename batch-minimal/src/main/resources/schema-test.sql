DROP TABLE FILE_INFO IF EXISTS;

CREATE TABLE FILE_INFO  (
    file_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    file_name VARCHAR(128),
    file_path VARCHAR(1024),
    file_created BIGINT,
    file_last_modified BIGINT,
	file_owner VARCHAR(128),
    file_size BIGINT
);