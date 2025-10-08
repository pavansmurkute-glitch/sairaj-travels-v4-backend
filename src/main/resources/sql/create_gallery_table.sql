-- Create gallery table for images and videos
CREATE TABLE gallery (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(200) NOT NULL,
    description NTEXT,
    image_path NVARCHAR(500),
    video_url NVARCHAR(500),
    category NVARCHAR(50) NOT NULL,
    is_featured BIT DEFAULT 0,
    is_active BIT DEFAULT 1,
    sort_order INT DEFAULT 0,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE()
);

-- Insert sample gallery data
INSERT INTO gallery (title, description, image_path, video_url, category, is_featured, is_active, sort_order) VALUES
('Mercedes-Benz Luxury Bus', 'Premium luxury bus with modern amenities', '/images/gallery/mercedes-luxury-bus.jpg', NULL, 'Fleet', 1, 1, 1),
('Mercedes-Benz Citaro Electric Bus', 'Eco-friendly electric bus for sustainable travel', '/images/gallery/citaro-electric-bus.jpg', NULL, 'Fleet', 1, 1, 2),
('Safety First', 'Our commitment to passenger safety', '/images/gallery/safety-first.jpg', NULL, 'Fleet', 1, 1, 3),
('Maruti Suzuki Ertiga', 'Comfortable family vehicle for short trips', '/images/gallery/ertiga.jpg', NULL, 'Fleet', 0, 1, 4),
('Luxury Interior', 'Premium interior with comfortable seating', '/images/gallery/luxury-interior.jpg', NULL, 'Luxury', 1, 1, 5),
('Mountain Adventure Ride', 'Adventure awaits with our reliable mountain transportation', NULL, 'https://youtube.com/watch?v=mountain-adventure', 'Destinations', 1, 1, 6),
('Corporate Travel Solutions', 'Professional transportation for your business needs', NULL, 'https://youtube.com/watch?v=corporate-travel', 'Destinations', 1, 1, 7),
('Happy Customer - Rajesh', 'Customer testimonial from satisfied traveler', '/images/gallery/happy-customer-rajesh.jpg', NULL, 'Happy Customers', 0, 1, 8),
('Driver Training Session', 'Our professional drivers in training', '/images/gallery/driver-training.jpg', NULL, 'Drivers', 0, 1, 9),
('Pune to Mumbai Route', 'Scenic route from Pune to Mumbai', '/images/gallery/pune-mumbai-route.jpg', NULL, 'Destinations', 0, 1, 10);
