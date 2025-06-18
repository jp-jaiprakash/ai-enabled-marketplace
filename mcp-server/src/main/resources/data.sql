-- USERS
INSERT INTO users (name, email, password_hash, address) VALUES
                                                            ('Alice Smith', 'alice@example.com', 'hashedpass1', '123 Apple Street'),
                                                            ('Bob Johnson', 'bob@example.com', 'hashedpass2', '456 Banana Blvd'),
                                                            ('Charlie Brown', 'charlie@example.com', 'hashedpass3', '789 Cherry Lane'),
                                                            ('Diana Prince', 'diana@example.com', 'hashedpass4', '1010 Date Drive'),
                                                            ('Ethan Hunt', 'ethan@example.com', 'hashedpass5', '1111 Elderberry Rd');

-- PRODUCTS
INSERT INTO products (name, description, minimum_selling_price, target_selling_price, stock, estimate_delivery_days) VALUES
                                                                                                                         ('Laptop', 'High-performance laptop', 800.00, 999.99, 10, 3),
                                                                                                                         ('Headphones', 'Noise-cancelling headphones', 50.00, 79.99, 25, 2),
                                                                                                                         ('Smartphone', 'Flagship smartphone model', 600.00, 699.99, 15, 5),
                                                                                                                         ('Backpack', 'Waterproof backpack', 20.00, 35.00, 50, 4),
                                                                                                                         ('Monitor', '27 inch 4K monitor', 200.00, 249.99, 8, 3);

-- ORDERS
INSERT INTO orders (user_id, order_date, status, total_amount) VALUES
                                                                   (1, CURRENT_TIMESTAMP, 'PENDING', 1079.98),
                                                                   (2, CURRENT_TIMESTAMP, 'SHIPPED', 699.99),
                                                                   (3, CURRENT_TIMESTAMP, 'DELIVERED', 114.99),
                                                                   (4, CURRENT_TIMESTAMP, 'CANCELLED', 249.99),
                                                                   (5, CURRENT_TIMESTAMP, 'PENDING', 35.00);

-- ORDER_ITEMS
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
                                                                    (1, 1, 1, 999.99),    -- Laptop
                                                                    (1, 2, 1, 79.99),     -- Headphones
                                                                    (2, 3, 1, 699.99),    -- Smartphone
                                                                    (3, 2, 1, 79.99),     -- Headphones
                                                                    (3, 4, 1, 35.00),     -- Backpack
                                                                    (4, 5, 1, 249.99),    -- Monitor
                                                                    (5, 4, 1, 35.00);     -- Backpack
