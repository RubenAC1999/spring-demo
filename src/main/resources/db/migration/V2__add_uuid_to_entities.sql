-- EMPLOYEES --
ALTER TABLE employees
ADD COLUMN UUID uuid;

UPDATE employees
SET uuid = gen_random_uuid()
WHERE uuid IS NULL;

ALTER TABLE employees
ALTER COLUMN uuid SET NOT NULL;

CREATE UNIQUE INDEX idx_employees_uuid ON employees(uuid);

-- TASKS --
ALTER TABLE tasks
ADD COLUMN UUID uuid;

UPDATE tasks
SET uuid = gen_random_uuid()
WHERE uuid IS NULL;

ALTER TABLE tasks
ALTER COLUMN uuid SET NOT NULL;

CREATE UNIQUE INDEX idx_tasks_uuid ON tasks(uuid);

-- PROJECTS --
ALTER TABLE projects
ADD COLUMN UUID uuid;

UPDATE projects
SET uuid = gen_random_uuid()
WHERE uuid IS NULL;

ALTER TABLE projects
ALTER COLUMN uuid SET NOT NULL;

CREATE UNIQUE INDEX idx_projects_uuid ON projects(uuid);

