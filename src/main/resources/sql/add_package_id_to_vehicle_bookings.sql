-- Add PackageId column to VehicleBookings table
ALTER TABLE VehicleBookings 
ADD PackageId INT NULL;

-- Add comment to the column
EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'Optional reference to packages table', 
    @level0type = N'SCHEMA', @level0name = N'dbo', 
    @level1type = N'TABLE', @level1name = N'VehicleBookings', 
    @level2type = N'COLUMN', @level2name = N'PackageId';

-- Create index for better performance (optional)
CREATE INDEX IX_VehicleBookings_PackageId ON VehicleBookings(PackageId);
