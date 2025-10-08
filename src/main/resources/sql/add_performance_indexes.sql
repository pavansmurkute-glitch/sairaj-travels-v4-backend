-- Performance Optimization: Add Database Indexes
-- This script adds indexes to improve query performance

-- AdminUser table indexes
CREATE INDEX IF NOT EXISTS idx_adminuser_username ON admin_users(username);
CREATE INDEX IF NOT EXISTS idx_adminuser_email ON admin_users(email);
CREATE INDEX IF NOT EXISTS idx_adminuser_isactive ON admin_users(is_active);
CREATE INDEX IF NOT EXISTS idx_adminuser_role_id ON admin_users(role_id);
CREATE INDEX IF NOT EXISTS idx_adminuser_must_change_password ON admin_users(must_change_password);

-- Booking table indexes
CREATE INDEX IF NOT EXISTS idx_booking_user_id ON bookings(user_id);
CREATE INDEX IF NOT EXISTS idx_booking_status ON bookings(booking_status);
CREATE INDEX IF NOT EXISTS idx_booking_journey_date ON bookings(journey_date);
CREATE INDEX IF NOT EXISTS idx_booking_bus_id ON bookings(bus_id);
CREATE INDEX IF NOT EXISTS idx_booking_created_at ON bookings(created_at);

-- Travel Package table indexes
CREATE INDEX IF NOT EXISTS idx_travel_package_isactive ON travel_packages(is_active);
CREATE INDEX IF NOT EXISTS idx_travel_package_sort_order ON travel_packages(sort_order);
CREATE INDEX IF NOT EXISTS idx_travel_package_category_id ON travel_packages(package_category_id);
CREATE INDEX IF NOT EXISTS idx_travel_package_is_featured ON travel_packages(is_featured);
CREATE INDEX IF NOT EXISTS idx_travel_package_price ON travel_packages(package_price);

-- Gallery table indexes
CREATE INDEX IF NOT EXISTS idx_gallery_category ON gallery(category);
CREATE INDEX IF NOT EXISTS idx_gallery_isactive ON gallery(is_active);
CREATE INDEX IF NOT EXISTS idx_gallery_sort_order ON gallery(sort_order);
CREATE INDEX IF NOT EXISTS idx_gallery_is_featured ON gallery(is_featured);
CREATE INDEX IF NOT EXISTS idx_gallery_created_at ON gallery(created_at);

-- Vehicle table indexes
CREATE INDEX IF NOT EXISTS idx_vehicle_type ON vehicles(type);
CREATE INDEX IF NOT EXISTS idx_vehicle_isactive ON vehicles(is_active);
CREATE INDEX IF NOT EXISTS idx_vehicle_capacity ON vehicles(capacity);

-- Enquiry table indexes
CREATE INDEX IF NOT EXISTS idx_enquiry_status ON enquiries(status);
CREATE INDEX IF NOT EXISTS idx_enquiry_service ON enquiries(service);
CREATE INDEX IF NOT EXISTS idx_enquiry_email ON enquiries(email);
CREATE INDEX IF NOT EXISTS idx_enquiry_phone ON enquiries(phone);
CREATE INDEX IF NOT EXISTS idx_enquiry_created_at ON enquiries(created_at);

-- Contact Message table indexes
CREATE INDEX IF NOT EXISTS idx_contact_message_email ON contact_messages(email);
CREATE INDEX IF NOT EXISTS idx_contact_message_created_at ON contact_messages(created_at);

-- Testimonial table indexes
CREATE INDEX IF NOT EXISTS idx_testimonial_isactive ON testimonials(is_active);
CREATE INDEX IF NOT EXISTS idx_testimonial_is_featured ON testimonials(is_featured);
CREATE INDEX IF NOT EXISTS idx_testimonial_created_at ON testimonials(created_at);

-- Vehicle Booking table indexes
CREATE INDEX IF NOT EXISTS idx_vehicle_booking_customer_phone ON vehicle_bookings(customer_phone);
CREATE INDEX IF NOT EXISTS idx_vehicle_booking_status ON vehicle_bookings(status);
CREATE INDEX IF NOT EXISTS idx_vehicle_booking_trip_date ON vehicle_bookings(trip_date);
CREATE INDEX IF NOT EXISTS idx_vehicle_booking_vehicle_id ON vehicle_bookings(vehicle_id);
CREATE INDEX IF NOT EXISTS idx_vehicle_booking_created_at ON vehicle_bookings(created_at);

-- Composite indexes for common query patterns
CREATE INDEX IF NOT EXISTS idx_adminuser_active_role ON admin_users(is_active, role_id);
CREATE INDEX IF NOT EXISTS idx_travel_package_active_featured ON travel_packages(is_active, is_featured, sort_order);
CREATE INDEX IF NOT EXISTS idx_gallery_active_category ON gallery(is_active, category, sort_order);
CREATE INDEX IF NOT EXISTS idx_booking_status_date ON bookings(booking_status, journey_date);
CREATE INDEX IF NOT EXISTS idx_vehicle_booking_status_date ON vehicle_bookings(status, trip_date);

-- Performance monitoring query
-- This query can be used to check index usage
-- SELECT * FROM sys.dm_db_index_usage_stats WHERE database_id = DB_ID();
