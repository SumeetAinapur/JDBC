CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

CREATE TABLE books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    author VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    available_quantity INT NOT NULL
);

CREATE TABLE members (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    member_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15)
);

CREATE TABLE book_issues (
    issue_id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT NOT NULL,
    member_id INT NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    FOREIGN KEY (book_id) REFERENCES books(book_id),
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);
