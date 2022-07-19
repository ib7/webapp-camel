CREATE TABLE Departments
(
    id       UUID PRIMARY KEY,
    name     VARCHAR(50) NOT NULL,
    location VARCHAR(50) NOT NULL
);

COMMENT ON TABLE departments IS 'Departments table that shows details of departments where employees work';
COMMENT ON COLUMN departments.id IS 'Primary key column of departments table.';
COMMENT ON COLUMN departments.name IS 'A not null column that shows name of a department. Administration,
Marketing, Purchasing, Human Resources, Shipping, IT, Executive, Public
Relations, Sales, Finance, and Accounting. ';
COMMENT ON COLUMN departments.location IS 'Location id where a department is located';