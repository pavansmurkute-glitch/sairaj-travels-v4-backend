-- Create packages table
CREATE TABLE packages (
    package_id INT PRIMARY KEY IDENTITY(1,1),
    package_name NVARCHAR(200) NOT NULL,
    package_category NVARCHAR(50) NOT NULL,
    package_category_id NVARCHAR(50) NOT NULL,
    package_description NTEXT,
    package_duration NVARCHAR(100),
    package_price DECIMAL(10,2) NOT NULL,
    original_price DECIMAL(10,2),
    discount_percentage DECIMAL(5,2),
    package_image_url NVARCHAR(500),
    package_features NTEXT, -- JSON string of features array
    package_highlights NTEXT, -- JSON string of highlights array
    rating DECIMAL(3,2),
    reviews_count INT,
    is_active BIT NOT NULL DEFAULT 1,
    is_featured BIT NOT NULL DEFAULT 0,
    sort_order INT,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE()
);

-- Create indexes for better performance
CREATE INDEX IX_packages_category ON packages(package_category_id);
CREATE INDEX IX_packages_active ON packages(is_active);
CREATE INDEX IX_packages_featured ON packages(is_featured);
CREATE INDEX IX_packages_sort ON packages(sort_order);
CREATE INDEX IX_packages_price ON packages(package_price);

-- Insert sample packages data
INSERT INTO packages (
    package_name, package_category, package_category_id, package_description, 
    package_duration, package_price, original_price, discount_percentage, 
    package_image_url, package_features, package_highlights, rating, reviews_count, 
    is_active, is_featured, sort_order
) VALUES 
(
    'Pune to Mumbai Airport Transfer',
    'Airport Transfer',
    'airport',
    'Comfortable and reliable airport transfer service with experienced drivers and modern vehicles.',
    '3-4 Hours',
    2500.00,
    3000.00,
    16.67,
    'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80',
    '["Professional Driver", "AC Vehicle", "On-Time Service", "Luggage Assistance", "24/7 Support"]',
    '["Popular", "Best Value"]',
    4.9,
    150,
    1,
    1,
    1
),
(
    'Goa Beach Vacation Package',
    'Family Package',
    'family',
    'Complete beach vacation package with accommodation, meals, and transportation included.',
    '3 Days / 2 Nights',
    8500.00,
    10000.00,
    15.00,
    'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?auto=format&fit=crop&w=800&q=80',
    '["Transportation", "Hotel Accommodation", "Sightseeing", "Meals Included", "Beach Activities"]',
    '["Family Friendly", "All Inclusive"]',
    4.8,
    89,
    1,
    1,
    2
),
(
    'Shirdi Darshan Package',
    'Pilgrimage Package',
    'pilgrimage',
    'Peaceful spiritual journey with comfortable accommodation and temple darshan arrangements.',
    '2 Days / 1 Night',
    3200.00,
    3800.00,
    15.79,
    'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?auto=format&fit=crop&w=800&q=80',
    '["Temple Darshan", "Accommodation", "Meals", "Transportation", "Guide Service"]',
    '["Spiritual", "Comfortable"]',
    4.9,
    120,
    1,
    1,
    3
),
(
    'Corporate Team Outing',
    'Corporate Package',
    'corporate',
    'Perfect for corporate team outings with professional service and team building activities.',
    '1 Day',
    4500.00,
    5000.00,
    10.00,
    'https://images.unsplash.com/photo-1521737604893-d14cc237f11d?auto=format&fit=crop&w=800&q=80',
    '["Professional Service", "Team Building Activities", "Refreshments", "Photography", "Flexible Schedule"]',
    '["Corporate", "Team Building"]',
    4.7,
    65,
    1,
    0,
    4
),
(
    'Lonavala Hill Station Package',
    'Family Package',
    'family',
    'Escape to the beautiful hill station with scenic views and comfortable accommodation.',
    '2 Days / 1 Night',
    5800.00,
    6500.00,
    10.77,
    'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?auto=format&fit=crop&w=800&q=80',
    '["Hill Station Visit", "Scenic Views", "Local Cuisine", "Photography Spots", "Comfortable Stay"]',
    '["Scenic", "Relaxing"]',
    4.6,
    95,
    1,
    0,
    5
),
(
    'Mumbai City Tour Package',
    'Family Package',
    'family',
    'Explore the vibrant city of Mumbai with our comprehensive city tour package.',
    '1 Day',
    3500.00,
    4000.00,
    12.50,
    'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80',
    '["City Sightseeing", "Local Guide", "Photography", "Local Food", "Shopping"]',
    '["City Tour", "Cultural"]',
    4.8,
    110,
    1,
    0,
    6
);
