-- Create testimonials table for customer testimonials
CREATE TABLE IF NOT EXISTS testimonials (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(100) NOT NULL,
    customer_type VARCHAR(50) NOT NULL,
    testimonial_text TEXT NOT NULL,
    rating INT DEFAULT 5,
    avatar_letter VARCHAR(1) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert sample testimonials data
INSERT INTO testimonials (customer_name, customer_type, testimonial_text, rating, avatar_letter, is_active, sort_order) VALUES
('Rajesh Kumar', 'Family Trip Customer', 'Excellent service! The driver was professional and the vehicle was spotless. Highly recommended for family trips.', 5, 'R', TRUE, 1),
('Priya Sharma', 'Corporate Client', 'Perfect for corporate events. Punctual, reliable, and accommodating. Our team was very satisfied.', 5, 'P', TRUE, 2),
('Suresh Patel', 'Pilgrimage Customer', 'Used their services for pilgrimage tours. Comfortable and safe journey. Will definitely use again.', 5, 'S', TRUE, 3);
