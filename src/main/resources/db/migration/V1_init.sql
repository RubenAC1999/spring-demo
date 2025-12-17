-- EMPLOYEES --

CREATE TABLE employees(
id BIGSERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL,
email VARCHAR(150) NOT NULL UNIQUE,
password VARCHAR(255) NOT NULL,
role VARCHAR(50) NOT NULL
);

-- PROJECTS --

CREATE TABLE projects(
id BIGSERIAL PRIMARY KEY,
name VARCHAR(255) NOT NULL
);

-- TASKS --

CREATE TABLE tasks(
id BIGSERIAL PRIMARY KEY,
description VARCHAR(255) NOT NULL,
status VARCHAR(50) NOT NULL,
created_at TIMESTAMP NOT NULL DEFAULT now(),
updated_at TIMESTAMP NOT NULL DEFAULT now(),
project_id BIGINT NOT NULL,
employee_id BIGINT

CONSTRAINT fk_task_project
FOREIGN KEY (project_id)
REFERENCES projects(id)
ON DELETE CASCADE,

CONSTRAINT fk_task_employee
FOREIGN KEY (employee_id)
REFERENCES employees(id)
ON DELETE SET NULL
);


-- EMPLOYEES MANY TO MANY PROJECTS --

CREATE TABLE employee_projects(
employee_id BIGINT NOT NULL,
project_id BIGINT NOT NULL,

PRIMARY KEY (employee_id, project_id),

CONSTRAINT fk_ep_employee
FOREIGN KEY (employee_id)
REFERENCES employees(id)
ON DELETE CASCADE,

CONSTRAINT fk_ep_project
FOREIGN KEY (project_id)
REFERENCES projects(id)
ON DELETE CASCADE
);