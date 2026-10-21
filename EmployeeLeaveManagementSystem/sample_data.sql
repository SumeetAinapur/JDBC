USE leave_db;

-- Run this AFTER schema.sql, on empty tables (IDs start from 1)

-- Employees: name, department, leave balance (12 days a year)
INSERT INTO employees (name, department, leave_balance) VALUES
('Ramesh Gowda', 'IT', 9),
('Priya Nair', 'HR', 10),
('Arjun Reddy', 'Finance', 12),
('Sneha Patil', 'Sales', 12),
('Mohammed Irfan', 'IT', 12),
('Kavya Shetty', 'Admin', 12),
('Rahul Sharma', 'Sales', 12),
('Ananya Iyer', 'HR', 7);

-- Leave requests (dates are relative to today)
-- 1 to 3 are APPROVED (balances above already have these days deducted)
-- 4 is REJECTED
-- 5 to 7 are PENDING, ready for you to approve or reject
INSERT INTO leave_requests (emp_id, leave_type, from_date, to_date, days, reason, status, applied_date) VALUES
(1, 'CASUAL', DATE_SUB(CURDATE(), INTERVAL 20 DAY), DATE_SUB(CURDATE(), INTERVAL 18 DAY), 3, 'Family function in Mysuru', 'APPROVED', DATE_SUB(CURDATE(), INTERVAL 25 DAY)),
(2, 'SICK',   DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_SUB(CURDATE(), INTERVAL 9 DAY),  2, 'Fever',                    'APPROVED', DATE_SUB(CURDATE(), INTERVAL 10 DAY)),
(8, 'EARNED', DATE_SUB(CURDATE(), INTERVAL 40 DAY), DATE_SUB(CURDATE(), INTERVAL 36 DAY), 5, 'Trip to Udupi',            'APPROVED', DATE_SUB(CURDATE(), INTERVAL 50 DAY)),
(4, 'CASUAL', DATE_SUB(CURDATE(), INTERVAL 15 DAY), DATE_SUB(CURDATE(), INTERVAL 15 DAY), 1, 'Personal work',            'REJECTED', DATE_SUB(CURDATE(), INTERVAL 16 DAY)),
(5, 'SICK',   DATE_ADD(CURDATE(), INTERVAL 3 DAY),  DATE_ADD(CURDATE(), INTERVAL 4 DAY),  2, 'Dental treatment',         'PENDING',  CURDATE()),
(6, 'EARNED', DATE_ADD(CURDATE(), INTERVAL 10 DAY), DATE_ADD(CURDATE(), INTERVAL 14 DAY), 5, 'Sister''s wedding',        'PENDING',  CURDATE()),
(3, 'CASUAL', DATE_ADD(CURDATE(), INTERVAL 7 DAY),  DATE_ADD(CURDATE(), INTERVAL 7 DAY),  1, 'Bank work',                'PENDING',  CURDATE());
