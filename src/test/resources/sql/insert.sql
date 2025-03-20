insert into users (id, user_name, password, email)
values (1, 'Ivan', '$2a$10$EcwGMpm1dqk4Vylb00UzOu1UZftdQPCdoKO6pQQFncCnwrTjYIZG6', 'ivan@mail.ru');
insert into users (id, user_name, password, email)
values (2, 'Alexandr', '$2a$10$TkOSxEMuMiVrzMqTN0fWgeir5gc3ZEsox2XQkT8OMNHQXWN87Dud.', 'alex@mail.ru');
insert into users (id, user_name, password, email)
values (3, 'Anna', '$2a$10$TkOSxEMuMiVrzMqTN0fWgeir5gc3ZEsox2XQkT8OMNHQXWN87Dud.', 'anna@mail.ru');

SELECT setval ('users_id_seq', (SELECT MAX(id) FROM users));

insert into user_roles (user_id, roles)
values (1,  'ROLE_USER');
insert into user_roles (user_id, roles)
values (2, 'ROLE_ADMIN');
insert into user_roles (user_id, roles)
values (3, 'ROLE_USER');

insert into tasks (id, title, description, status, priority, author_id, performer_id)
values (1, 'Test task 1', 'To do test task 1', 'PENDING', 'HIGH', 2, 1);
insert into tasks (id, title, description, status, priority, author_id, performer_id)
values (2, 'Test task 2', 'To do test task 2', 'PROGRESS', 'MEDIUM', 2, 1);
insert into tasks (id, title, description, status, priority, author_id, performer_id)
values (3, 'Test task 3', 'To do test task 3', 'COMPLETED', 'LOW', 2, 2);

SELECT setval ('tasks_id_seq', (SELECT MAX(id) FROM tasks));

insert into comments (id, text, time, author_id, task_id)
values (1, 'Comment 1 text', '2024-12-31 14:30:00', 1, 1);
insert into comments (id, text, time, author_id, task_id)
values (2, 'Comment 2 text', '2024-11-11 11:11:00', 1, 2);
insert into comments (id, text, time, author_id, task_id)
values (3, 'Comment 3 text', '2024-10-30 10:00:00', 2, 1);

SELECT setval ('comments_id_seq', (SELECT MAX(id) FROM comments));