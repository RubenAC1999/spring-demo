-- ADMIN USER SEED CREATION --
INSERT INTO employees(name, email, password, position,  role, uuid) VALUES(
'admin',
'admin@local.com',
'$2a$10$mgXAfgRFZV0qsJhz9rfQHOJ7XkpnFqu2YdZIz5AMQXGgMJ4vG5WSW',
'DEVELOPER',
'ROLE_ADMIN',
gen_random_uuid()
);