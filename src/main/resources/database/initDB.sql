DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS room CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS event;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE users
(
    id               INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name             VARCHAR                           NOT NULL,
    email            VARCHAR                           NOT NULL,
    password         VARCHAR                           NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_role
(
    user_id INTEGER NOT NULL,
    role    VARCHAR,
    CONSTRAINT user_role_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE room
(
    id                  INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    description         VARCHAR                             NOT NULL,
    capacity            INTEGER                             NOT NULL,
    is_has_projector    BOOLEAN             DEFAULT FALSE   NOT NULL,
    is_has_white_board  BOOLEAN             DEFAULT FALSE   NOT NULL
);
CREATE UNIQUE INDEX room_unique_description_idx ON room (description);

CREATE TABLE event
(
    id                  INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    description         VARCHAR                             NOT NULL,
    is_accepted         BOOLEAN             DEFAULT FALSE   NOT NULL,
    room_id             INTEGER                             NOT NULL,
    user_id             INTEGER                             NOT NULL,
    duration            tsrange                             NOT NULL,
    EXCLUDE USING GIST (room_id WITH =, duration WITH &&) WHERE (is_accepted),
    FOREIGN KEY (room_id) REFERENCES room (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

