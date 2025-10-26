--for docker
INSERT INTO users (email, password, first_name, last_name, phone_number, user_role, user_account_status)
SELECT 'admin@gmail.com', '$2a$10$6mMR41Be5L4ofFPvHmaNq.fFt9taTYT.T0UQda3R2bWhVmKCuRA.a', 'Admin', 'Admin', '1234567890','ROLE_ADMIN','ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@gmail.com');