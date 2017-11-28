DELETE FROM groups;
DELETE FROM groupmembers;
DELETE FROM users;


INSERT INTO users (U_NAME, U_EMAIL, U_PASSWORD, U_DESCRIPTION)
VALUES ('Egor', 'ohapegor@list.ru', 'egor', 'Simple user'),
  ('User', 'user@gmail.ru', 'user', 'Simple user'),
  ('Admin', 'admin@yandex.ru', 'admin', 'Admin'),
  ('BannedUser', 'bannedUser@mail.ru', 'banedUser', 'Banned user');


INSERT INTO groups (G_NAME, G_DESCRIPTION)
VALUES ('BannedUsers','BannedUsers from DB'),
       ('NewUsers','registered by form'),
       ('SuperAdmins','superadmins in DB');

INSERT INTO groupmembers (G_NAME, G_MEMBER) VALUES
  ('BannedUsers', 'BannedUser'),
  ('NewUsers', 'BannedUser'),
  ('NewUsers', 'User'),
  ('NewUsers', 'Egor'),
  ('SuperAdmins', 'Admin');
