-- Update all users to have USER role
UPDATE users SET role = 'USER' WHERE role = 'ADMIN' OR role = 'SELLER'; 