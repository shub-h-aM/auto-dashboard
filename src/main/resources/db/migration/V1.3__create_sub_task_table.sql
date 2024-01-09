CREATE TABLE IF NOT EXISTS sub_task (id BIGINT AUTO_INCREMENT PRIMARY KEY, ticket VARCHAR(255),
 title VARCHAR(255), parent_ticket VARCHAR(255), status VARCHAR(255), qa_owner VARCHAR(255), from_date DATE,
 to_date DATE, created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
 last_updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP );