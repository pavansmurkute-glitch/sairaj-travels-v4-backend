-- User Management System Tables for SQL Server
-- These tables add role-based access control without affecting existing functionality

-- Admin Roles Table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='admin_roles' AND xtype='U')
CREATE TABLE admin_roles (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    role_name NVARCHAR(50) UNIQUE NOT NULL,
    display_name NVARCHAR(100) NOT NULL,
    description NVARCHAR(MAX),
    permissions NVARCHAR(MAX), -- JSON stored as NVARCHAR
    is_active BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE()
);

-- Admin Users Table (Enhanced user management)
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='admin_users' AND xtype='U')
CREATE TABLE admin_users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) UNIQUE NOT NULL,
    email NVARCHAR(100) UNIQUE NOT NULL,
    password NVARCHAR(255) NOT NULL,
    full_name NVARCHAR(100) NOT NULL,
    role_id BIGINT NOT NULL,
    is_active BIT DEFAULT 1,
    must_change_password BIT DEFAULT 0,
    last_login DATETIME2 NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    created_by BIGINT NULL,
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (role_id) REFERENCES admin_roles(id),
    FOREIGN KEY (created_by) REFERENCES admin_users(id)
);

-- Password Reset Tokens Table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='password_reset_tokens' AND xtype='U')
CREATE TABLE password_reset_tokens (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token NVARCHAR(255) NOT NULL UNIQUE,
    expires_at DATETIME2 NOT NULL,
    used BIT DEFAULT 0,
    created_at DATETIME2 DEFAULT GETDATE(),
    used_at DATETIME2 NULL,
    FOREIGN KEY (user_id) REFERENCES admin_users(id) ON DELETE CASCADE
);

-- Insert Default Roles (only if not exists)
IF NOT EXISTS (SELECT * FROM admin_roles WHERE role_name = 'SUPER_ADMIN')
INSERT INTO admin_roles (role_name, display_name, description, permissions) VALUES
('SUPER_ADMIN', 'Super Administrator', 'Full system access with all permissions', 
 '{"users":true,"roles":true,"bookings":true,"enquiries":true,"vehicles":true,"drivers":true,"packages":true,"gallery":true,"testimonials":true,"contact":true,"reports":true,"file_manager":true}');

IF NOT EXISTS (SELECT * FROM admin_roles WHERE role_name = 'MANAGER')
INSERT INTO admin_roles (role_name, display_name, description, permissions) VALUES
('MANAGER', 'Manager', 'Business operations management access',
 '{"users":false,"roles":false,"bookings":true,"enquiries":true,"vehicles":true,"drivers":true,"packages":true,"gallery":true,"testimonials":true,"contact":true,"reports":true,"file_manager":false}');

IF NOT EXISTS (SELECT * FROM admin_roles WHERE role_name = 'OPERATOR')
INSERT INTO admin_roles (role_name, display_name, description, permissions) VALUES
('OPERATOR', 'Operator', 'Day-to-day operations access',
 '{"users":false,"roles":false,"bookings":true,"enquiries":true,"vehicles":false,"drivers":false,"packages":false,"gallery":false,"testimonials":false,"contact":true,"reports":false,"file_manager":false}');

IF NOT EXISTS (SELECT * FROM admin_roles WHERE role_name = 'VIEWER')
INSERT INTO admin_roles (role_name, display_name, description, permissions) VALUES
('VIEWER', 'Viewer', 'Read-only access to reports and data',
 '{"users":false,"roles":false,"bookings":false,"enquiries":false,"vehicles":false,"drivers":false,"packages":false,"gallery":false,"testimonials":false,"contact":false,"reports":true,"file_manager":false}');

-- Create default super admin user (you can change password later)
-- Password: 'admin123' (will be hashed by the application)
IF NOT EXISTS (SELECT * FROM admin_users WHERE username = 'admin')
INSERT INTO admin_users (username, email, password, full_name, role_id, must_change_password) 
SELECT 'admin', 'admin@sairajtravels.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'System Administrator', r.id, 1
FROM admin_roles r WHERE r.role_name = 'SUPER_ADMIN';

PRINT 'User Management tables created successfully for SQL Server!';
