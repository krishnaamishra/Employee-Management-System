-- ======================================================
--  EMPLOYEE MANAGEMENT SYSTEM DATABASE SETUP (v1.0)
--  Compatible with DatabaseUtil.java structure
-- ======================================================

-- 1️⃣ Create or use existing database
CREATE DATABASE IF NOT EXISTS employee;
USE employee;

-- ======================================================
-- 2️⃣ Drop old tables if they exist (clean start)
-- ======================================================
DROP TABLE IF EXISTS attendance;
DROP TABLE IF EXISTS salary_record;
DROP TABLE IF EXISTS leave_requests;
DROP TABLE IF EXISTS task;
DROP TABLE IF EXISTS empdata;
DROP TABLE IF EXISTS emp;

-- ======================================================
-- 3️⃣ Admin login table
-- ======================================================
CREATE TABLE emp (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE,
    password VARCHAR(100)
);

-- Insert default admin login
INSERT INTO emp (username, password)
VALUES ('asad@admin', 'Asad@123');

-- ======================================================
-- 4️⃣ Employee Data Table (empdata)
-- ======================================================
CREATE TABLE empdata (
    eid INT AUTO_INCREMENT PRIMARY KEY,
    fname VARCHAR(100),
    lname VARCHAR(100),
    gender VARCHAR(10),
    contact VARCHAR(20),
    mail VARCHAR(100) UNIQUE,
    designation VARCHAR(100),
    doj DATE,
    salary DECIMAL(10,2),
    image LONGBLOB,
    empusername VARCHAR(100) UNIQUE,
    emppass VARCHAR(100)
);

-- Add one sample employee for testing login
INSERT INTO empdata (fname, lname, gender, contact, mail, designation, doj, salary, empusername, emppass)
VALUES ('John', 'Doe', 'Male', '9876543210', 'john@company.com', 'Developer', '2024-01-15', 65000.00, 'john@emp', 'John@123');

-- ======================================================
-- 5️⃣ Attendance Table
-- ======================================================
CREATE TABLE attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    eid INT,
    date DATE,
    status ENUM('Present', 'Absent', 'Leave'),
    FOREIGN KEY (eid) REFERENCES empdata(eid) ON DELETE CASCADE,
    CONSTRAINT unique_attendance_per_day UNIQUE (eid, date)
);

-- ======================================================
-- 6️⃣ Salary Record Table
-- ======================================================
CREATE TABLE salary_record (
    gross_pay DECIMAL(10,2),
    net_pay DECIMAL(10,2),
    health_insurance DECIMAL(10,2),
    travel_allowance DECIMAL(10,2),
    tax DECIMAL(10,2),
    eid INT,
    FOREIGN KEY (eid) REFERENCES empdata(eid) ON DELETE CASCADE
);

-- ======================================================
-- 7️⃣ Task Assignment Table
-- ======================================================
CREATE TABLE task (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    task_name VARCHAR(100),
    description TEXT,
    post DATE DEFAULT (CURRENT_DATE),
    status ENUM('Assigned', 'In Progress', 'Completed') DEFAULT 'Assigned',
    eid INT,
    FOREIGN KEY (eid) REFERENCES empdata(eid) ON DELETE CASCADE
);

-- Sample task for testing
INSERT INTO task (task_name, description, eid)
VALUES ('Website Update', 'Improve UI for Employee Panel', 1);

-- ======================================================
-- 8️⃣ Leave Requests Table
-- ======================================================
CREATE TABLE leave_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    eid INT,
    leave_type VARCHAR(50),
    start_date DATE,
    end_date DATE,
    leave_reason TEXT,
    status ENUM('Pending', 'Approved', 'Rejected') DEFAULT 'Pending',
    FOREIGN KEY (eid) REFERENCES empdata(eid) ON DELETE CASCADE
);

-- ======================================================
-- ✅ DATABASE READY
-- ======================================================
SHOW TABLES;

DESCRIBE empdata;

