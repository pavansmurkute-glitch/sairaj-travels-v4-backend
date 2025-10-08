-- User Management System Tables
-- These tables add role-based access control without affecting existing functionality

-- Admin Roles Table
CREATE TABLE IF NOT EXISTS admin_roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) UNIQUE NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    description TEXT,
    permissions JSON,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Admin Users Table (Enhanced user management)
CREATE TABLE IF NOT EXISTS admin_users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role_id BIGINT NOT NULL,
    is_active BOOLEAN DEFAULT true,
    must_change_password BOOLEAN DEFAULT false,
    last_login TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES admin_roles(id),
    FOREIGN KEY (created_by) REFERENCES admin_users(id)
);

-- Password Reset Tokens Table
CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    used_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES admin_users(id) ON DELETE CASCADE
);

-- Insert Default Roles
INSERT IGNORE INTO admin_roles (role_name, display_name, description, permissions) VALUES
('SUPER_ADMIN', 'Super Administrator', 'Full system access with all permissions', 
 JSON_OBJECT(
    'users', true, 'roles', true, 'bookings', true, 'enquiries', true, 
    'vehicles', true, 'drivers', true, 'packages', true, 'gallery', true,
    'testimonials', true, 'contact', true, 'reports', true, 'file_manager', true
 )),
('MANAGER', 'Manager', 'Business operations management access',
 JSON_OBJECT(
    'users', false, 'roles', false, 'bookings', true, 'enquiries', true,
    'vehicles', true, 'drivers', true, 'packages', true, 'gallery', true,
    'testimonials', true, 'contact', true, 'reports', true, 'file_manager', false
 )),
('OPERATOR', 'Operator', 'Day-to-day operations access',
 JSON_OBJECT(
    'users', false, 'roles', false, 'bookings', true, 'enquiries', true,
    'vehicles', false, 'drivers', false, 'packages', false, 'gallery', false,
    'testimonials', false, 'contact', true, 'reports', false, 'file_manager', false
 )),
('VIEWER', 'Viewer', 'Read-only access to reports and data',
 JSON_OBJECT(
    'users', false, 'roles', false, 'bookings', false, 'enquiries', false,
    'vehicles', false, 'drivers', false, 'packages', false, 'gallery', false,
    'testimonials', false, 'contact', false, 'reports', true, 'file_manager', false
 ));

-- Create default super admin user (you can change password later)
-- Password: 'admin123' (will be hashed by the application)
INSERT IGNORE INTO admin_users (username, email, password, full_name, role_id, must_change_password) 
SELECT 'admin', 'admin@sairajtravels.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'System Administrator', r.id, true
FROM admin_roles r WHERE r.role_name = 'SUPER_ADMIN';
