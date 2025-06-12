-- Update role enum to only have USER role
ALTER TABLE users MODIFY COLUMN role ENUM('USER') NOT NULL DEFAULT 'USER';

-- Update any existing users to USER role
UPDATE users SET role = 'USER' WHERE role != 'USER'; 