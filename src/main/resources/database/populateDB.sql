DELETE FROM user_role;
DELETE FROM room;
DELETE FROM event;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (email, name, password)
VALUES ('user1@gmail.com', 'User1', 'user1pass'),
       ('user2@gmail.com', 'User2', 'user2pass'),
       ('user3@gmail.com', 'User3', 'user3pass'),
       ('manager@gmail.com', 'manager', 'manager');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('USER', 100001),
       ('USER', 100002),
       ('USER', 100003),
       ('MANAGER', 100003);

INSERT INTO room (description, capacity, is_has_projector, is_has_white_board)
VALUES ('Conference hall', 30, TRUE, TRUE),
       ('Meeting room 1', 10, FALSE, TRUE),
       ('Meeting room 2', 20, TRUE, FALSE);

INSERT INTO event(description, room_id, user_id, is_accepted, duration)
VALUES ('r1u1d15', 100004, 100000, TRUE, '[2021-10-15 10:00, 2021-10-15 11:00)'),
       ('r1u2d15', 100004, 100002, FALSE, '[2021-10-15 10:30, 2021-10-15 11:00)'),
       ('r1u2d15', 100004, 100002, TRUE, '[2021-10-15 11:00, 2021-10-15 11:30)'),
       ('r2u1d15', 100005, 100000, TRUE, '[2021-10-15 11:00, 2021-10-15 11:30)'),
       ('r2u2d15', 100005, 100001, FALSE, '[2021-10-15 11:00, 2021-10-15 11:30)'),
       ('r2u1d15', 100005, 100000, TRUE, '[2021-10-15 12:00, 2021-10-15 13:30)'),
       ('r2u1d15', 100005, 100000, TRUE, '[2021-10-15 15:00, 2021-10-15 16:30)'),
       ('r3u1d10', 100006, 100000, TRUE, '[2021-10-10 11:00, 2021-10-10 11:30)'),
       ('r3u1d15', 100006, 100000, FALSE, '[2021-10-15 11:00, 2021-10-15 11:30)');
