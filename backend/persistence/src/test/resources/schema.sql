CREATE TABLE IF NOT EXISTS users
(
    id IDENTITY PRIMARY KEY,
    firstname        VARCHAR(100),
    lastname         VARCHAR(100),
    email            VARCHAR(100),
    password         VARCHAR(100),
    sex              VARCHAR(100),
    nationality      VARCHAR(100),
    verificationCode VARCHAR(64),
    biography        VARCHAR(500),
    verified         BOOLEAN
);

CREATE TABLE IF NOT EXISTS places
(
    id IDENTITY PRIMARY KEY,
    google_id VARCHAR(150)     NOT NULL,
    name      VARCHAR(100)     NOT NULL,
    latitude  DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    address   VARCHAR(500)     NOT NULL
);

CREATE TABLE IF NOT EXISTS trips
(
    id IDENTITY PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    start_date  DATE,
    end_date    DATE,
    place_id    INTEGER,
    isPrivate   BOOLEAN,
    status      VARCHAR(100),
    FOREIGN KEY (place_id) REFERENCES places ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS activities
(
    id IDENTITY PRIMARY KEY,
    name        VARCHAR(40)  NOT NULL,
    category    VARCHAR(40)  NOT NULL,
    description VARCHAR(100) NOT NULL,
    start_date  DATE,
    end_date    DATE,
    place_id    INTEGER      NOT NULL,
    trip_id     INTEGER      NOT NULL,
    FOREIGN KEY (place_id) REFERENCES places ON DELETE CASCADE,
    FOREIGN KEY (trip_id) REFERENCES trips ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS trip_members
(
    id IDENTITY PRIMARY KEY,
    role    VARCHAR(255) NOT NULL,
    trip_id INTEGER,
    user_id INTEGER      NOT NULL,
    FOREIGN KEY (trip_id) REFERENCES trips,
    FOREIGN KEY (user_id) REFERENCES users
);

CREATE TABLE IF NOT EXISTS trip_comments
(
    id IDENTITY PRIMARY KEY,
    comment    VARCHAR(160) NOT NULL,
    created_on TIMESTAMP,
    trip_id    INTEGER      NOT NULL,
    user_id    INTEGER      NOT NULL,
    FOREIGN KEY (trip_id) REFERENCES trips ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_pictures
(
    id IDENTITY PRIMARY KEY,
    user_id INTEGER NOT NULL,
    image   BLOB,
    FOREIGN KEY (user_id) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS trip_pictures
(
    id IDENTITY PRIMARY KEY,
    trip_id INTEGER,
    image   BLOB,
    FOREIGN KEY (trip_id) REFERENCES trips ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS trip_invitations
(
    id IDENTITY PRIMARY KEY,
    trip_id    INTEGER,
    inviter_id INTEGER,
    invitee_id INTEGER,
    token      VARCHAR(64),
    responded  BOOLEAN,
    FOREIGN KEY (trip_id) REFERENCES trips ON DELETE CASCADE,
    FOREIGN KEY (inviter_id) REFERENCES users ON DELETE CASCADE,
    FOREIGN KEY (invitee_id) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS trip_pending_confirmations
(
    id IDENTITY PRIMARY KEY,
    trip_id            INTEGER,
    requestinguser_id INTEGER,
    token              VARCHAR(64),
    accepted           BOOLEAN,
    edited             BOOLEAN,
    FOREIGN KEY (trip_id) REFERENCES trips ON DELETE CASCADE,
    FOREIGN KEY (requestinguser_id) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_rates
(
    id IDENTITY PRIMARY KEY,
    rated_user_id    INTEGER,
    rated_by_user_id INTEGER,
    trip_id          INTEGER,
    rate             INTEGER,
    pending          BOOLEAN,
    comment          VARCHAR(140),
    FOREIGN KEY (trip_id) REFERENCES trips ON DELETE CASCADE,
    FOREIGN KEY (rated_user_id) REFERENCES users ON DELETE CASCADE,
    FOREIGN KEY (rated_by_user_id) REFERENCES users ON DELETE CASCADE
);

INSERT INTO places(id, google_id, name, latitude, longitude, address)
values (3, 'google id', 'Bahamas', 100, 100, 'Bahamas address');

INSERT INTO users(id, firstname, lastname, email, password, sex, nationality, verificationCode, biography, verified)
VALUES (1, 'Felipe', 'Gorostiaga', 'fgorostiaga@itba.edu.ar', '123321', 'M', 'Argentina', 'secret',
        'this is my biography', TRUE);

INSERT INTO users(id, firstname, lastname, email, password, sex, nationality, verificationCode, biography, verified)
VALUES (2, 'Test', 'User', 'feligoros@gmail.com', '123321', 'M', 'Argentina', 'secret',
        'this is my biography', TRUE);

INSERT INTO trips(id, name, description, start_date, end_date, place_id, isPrivate, status)
VALUES (1, 'Test Trip', 'This is a test description', NULL, NULL, 3, FALSE, 'COMPLETED');

INSERT INTO activities(id, name, category, description, start_date, end_date, place_id, trip_id)
VALUES (1, 'Trip Activity', 'Adventure', 'Kill the gold hoarding dragon underneath the rock', NULL, NULL, 3, 1);

INSERT INTO trip_members(id, role, trip_id, user_id)
VALUES (1, 'CREATOR', 1, 1);

INSERT INTO trip_comments(id, comment, created_on, trip_id, user_id)
VALUES (1, 'This is a comment', NULL, 1, 1);

INSERT INTO user_pictures(id, user_id, image)
VALUES (1, 1, NULL);

INSERT INTO trip_pictures(id, trip_id, image)
VALUES (1, 1, NULL);

INSERT INTO trip_invitations(id, trip_id, inviter_id, invitee_id, token, responded)
VALUES (1, 1, 1, 2, 'secret', FALSE);

INSERT INTO trip_pending_confirmations(id, trip_id, requestinguser_id, token, accepted, edited)
VALUES(1, 1, 1, 'token', FALSE, FALSE);

INSERT INTO user_rates(id, rated_user_id, rated_by_user_id, trip_id, rate, pending, comment)
VALUES(1, 1, 2, 1, 5, FALSE, 'Excelente persona para compartir un viaje!');


