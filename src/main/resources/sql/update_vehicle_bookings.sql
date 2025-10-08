-- Add new columns to VehicleBookings table
-- Run this script to update the existing table structure

-- Add new columns if they don't exist
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'VehicleBookings' AND COLUMN_NAME = 'CustomerEmail')
    ALTER TABLE VehicleBookings ADD CustomerEmail NVARCHAR(100);

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'VehicleBookings' AND COLUMN_NAME = 'PickupLocation')
    ALTER TABLE VehicleBookings ADD PickupLocation NVARCHAR(200);

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'VehicleBookings' AND COLUMN_NAME = 'DropLocation')
    ALTER TABLE VehicleBookings ADD DropLocation NVARCHAR(200);

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'VehicleBookings' AND COLUMN_NAME = 'ReturnDate')
    ALTER TABLE VehicleBookings ADD ReturnDate DATE;

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'VehicleBookings' AND COLUMN_NAME = 'TripTime')
    ALTER TABLE VehicleBookings ADD TripTime NVARCHAR(20);

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'VehicleBookings' AND COLUMN_NAME = 'Passengers')
    ALTER TABLE VehicleBookings ADD Passengers INT;

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'VehicleBookings' AND COLUMN_NAME = 'Luggage')
    ALTER TABLE VehicleBookings ADD Luggage NVARCHAR(100);

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'VehicleBookings' AND COLUMN_NAME = 'SpecialRequests')
    ALTER TABLE VehicleBookings ADD SpecialRequests NVARCHAR(500);

-- Update existing records to have default values
UPDATE VehicleBookings 
SET Passengers = 1 
WHERE Passengers IS NULL;

UPDATE VehicleBookings 
SET Status = 'PENDING' 
WHERE Status IS NULL OR Status = '';

PRINT 'VehicleBookings table updated successfully with new columns.';
