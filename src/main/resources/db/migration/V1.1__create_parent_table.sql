CREATE TABLE IF NOT EXISTS parent_task (id BIGINT AUTO_INCREMENT PRIMARY KEY, ticket_id VARCHAR(255),
 title VARCHAR(255), qa_owner VARCHAR(255), status VARCHAR(255), automation_ticket VARCHAR(255), no_of_blocker VARCHAR(255), no_of_rejection VARCHAR(255),
 no_of_enhancement VARCHAR(255), automation_sanity_suite_used VARCHAR(255), qa_suggestion VARCHAR(255), from_date DATE, to_date DATE, created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
last_updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP );