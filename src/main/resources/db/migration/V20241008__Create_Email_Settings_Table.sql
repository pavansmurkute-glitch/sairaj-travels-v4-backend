-- Create email_settings table
CREATE TABLE email_settings (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    email_enabled BIT NOT NULL DEFAULT 1,
    smtp_host NVARCHAR(255),
    smtp_port INT,
    smtp_username NVARCHAR(255),
    smtp_password NVARCHAR(500),
    from_email NVARCHAR(255),
    admin_email NVARCHAR(255),
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2,
    updated_by NVARCHAR(255)
);

-- Insert default email settings
INSERT INTO email_settings (
    email_enabled, 
    smtp_host, 
    smtp_port, 
    smtp_username, 
    from_email, 
    admin_email, 
    updated_by
) VALUES (
    1, -- email_enabled = true
    'smtp.sendgrid.net', -- smtp_host
    2525, -- smtp_port
    'apikey', -- smtp_username
    'PavansMurkute@gmail.com', -- from_email
    'admin@sairajtravels.com', -- admin_email
    'System' -- updated_by
);

-- Create index on email_enabled for faster queries
CREATE INDEX IX_email_settings_enabled ON email_settings(email_enabled);

-- Create index on updated_at for ordering
CREATE INDEX IX_email_settings_updated_at ON email_settings(updated_at DESC);
