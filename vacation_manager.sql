-- SQL script to create an enhanced database for vacation requests management

-- Create the database
CREATE DATABASE vacation_manager;

-- Use the database
USE vacation_manager;

-- Create the departments table
CREATE TABLE departments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Create the employees table
CREATE TABLE employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    department_id INT,
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

-- Create the vacation_types table
CREATE TABLE vacation_types (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255) NOT NULL
);

-- Create the vacation_requests table with foreign keys
CREATE TABLE vacation_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    vacation_type_id INT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employees(id),
    FOREIGN KEY (vacation_type_id) REFERENCES vacation_types(id)
);

-- Insert some test data into departments
INSERT INTO departments (name) VALUES
('Human Resources'),
('Engineering'),
('Sales');

-- Insert some test data into employees
INSERT INTO employees (name, department_id) VALUES
('John Doe', 1),
('Jane Smith', 2),
('Alice Johnson', 3);

-- Insert some test data into vacation_types
INSERT INTO vacation_types (type) VALUES
('Annual Leave'),
('Sick Leave'),
('Personal Leave');

-- Insert some test data into vacation_requests
INSERT INTO vacation_requests (employee_id, vacation_type_id, start_date, end_date) VALUES
(1, 1, '2024-01-10', '2024-01-15'),
(2, 2, '2024-02-05', '2024-02-10'),
(3, 1, '2024-03-20', '2024-03-25');
