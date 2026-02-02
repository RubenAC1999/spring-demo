-- ADMIN USER SEED CREATION --
INSERT INTO employees(name, email, password, role, uuid) VALUES(
'admin',
'admin@local.com',
'$2a$10$eLibSSclXZBJ5eBV9orcXuBNuH2OecXk4ulDSxmeegfJ3AGjeJjNe',
'ROLE_ADMIN',
gen_random_uuid()
);