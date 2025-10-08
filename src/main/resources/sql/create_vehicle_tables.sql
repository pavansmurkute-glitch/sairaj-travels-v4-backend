-- Create VehiclePricing table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='VehiclePricing' AND xtype='U')
CREATE TABLE VehiclePricing (
    PricingId INT IDENTITY(1,1) PRIMARY KEY,
    VehicleId INT NOT NULL,
    RateType NVARCHAR(20) NOT NULL,
    RatePerKm DECIMAL(10,2),
    MinKmPerDay INT,
    PackageHours INT,
    PackageKm INT,
    PackageRate DECIMAL(10,2),
    ExtraKmRate DECIMAL(10,2),
    ExtraHourRate DECIMAL(10,2),
    FOREIGN KEY (VehicleId) REFERENCES Vehicles(VehicleId)
);

-- Create VehicleCharges table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='VehicleCharges' AND xtype='U')
CREATE TABLE VehicleCharges (
    ChargeId INT IDENTITY(1,1) PRIMARY KEY,
    VehicleId INT NOT NULL,
    DriverAllowance DECIMAL(10,2),
    NightCharge DECIMAL(10,2),
    FuelIncluded BIT DEFAULT 0,
    TollIncluded BIT DEFAULT 0,
    FOREIGN KEY (VehicleId) REFERENCES Vehicles(VehicleId)
);

-- Create VehicleTerms table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='VehicleTerms' AND xtype='U')
CREATE TABLE VehicleTerms (
    TermId INT IDENTITY(1,1) PRIMARY KEY,
    VehicleId INT NOT NULL,
    TermText NTEXT,
    FOREIGN KEY (VehicleId) REFERENCES Vehicles(VehicleId)
);

-- Create VehicleImages table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='VehicleImages' AND xtype='U')
CREATE TABLE VehicleImages (
    ImageId INT IDENTITY(1,1) PRIMARY KEY,
    VehicleId INT NOT NULL,
    ImagePath NVARCHAR(500),
    Caption NVARCHAR(200),
    FOREIGN KEY (VehicleId) REFERENCES Vehicles(VehicleId)
);
