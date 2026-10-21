CREATE DATABASE IF NOT EXISTS inventory_db;
USE inventory_db;

CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(30) NOT NULL,
    price DOUBLE NOT NULL,
    quantity INT NOT NULL
);

-- sample data to test with
INSERT INTO products (name, category, price, quantity) VALUES
('Redmi Note 13', 'MOBILE', 15999, 12),
('HP Pavilion 15', 'LAPTOP', 55999, 4),
('boAt Rockerz 255', 'AUDIO', 1299, 30),
('Samsung 43 inch LED TV', 'TV', 27999, 3),
('USB Cable', 'ACCESSORIES', 199, 50);
