CREATE TABLE Employees
(
    id            UUID            PRIMARY KEY,
    first_name    VARCHAR(50)        NOT NULL,
    last_name     VARCHAR(50)        NOT NULL,
    department_id UUID            REFERENCES Departments (id),
    email         VARCHAR(50) UNIQUE NOT NULL,
    phone_number  VARCHAR(50) UNIQUE NOT NULL,
    salary        BIGINT CHECK (salary >= 1)
);

CREATE INDEX emp_name_idx ON Employees (last_name, first_name);

COMMENT ON COLUMN employees.first_name IS 'First name of the employee. A not null column.';
COMMENT ON COLUMN employees.last_name IS 'Last name of the employee. A not null column.';
COMMENT ON COLUMN employees.email IS 'Email id of the employee';
COMMENT ON COLUMN employees.phone_number IS 'Phone number of the employee; includes country code and area code';
COMMENT ON COLUMN employees.salary IS 'Monthly salary of the employee. Must be greater
then one or equal to one (enforced by constraint emp_salary_min)';
COMMENT ON COLUMN employees.department_id IS 'Department id where employee works; foreign key to department_id
column of the departments table';
