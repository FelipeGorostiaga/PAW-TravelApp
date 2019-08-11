CREATE TABLE IF NOT EXISTS places (
									  id IDENTITY PRIMARY KEY,
									  google_id varchar(150) UNIQUE NOT NULL,
									  name varchar(100) NOT NULL,
									  latitude double precision NOT NULL,
									  longitude double precision NOT NULL,
									  address varchar(500) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
									 id IDENTITY PRIMARY KEY,
									 firstname varchar(100) NOT NULL,
									 lastname varchar(100) NOT NULL,
									 email varchar(100) UNIQUE NOT NULL,
									 password varchar(100) NOT NULL,
									 nationality varchar(100) NOT NULL,
									 birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS trips (
									 id IDENTITY PRIMARY KEY,
									 adminid INTEGER NOT NULL,
									 name varchar(100) NOT NULL,
									 description varchar(500),
									 start_date DATE,
									 end_date DATE,
									 startplaceid INTEGER NOT NULL,
									 FOREIGN KEY (startplaceid) REFERENCES places ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS activities (
										  id IDENTITY PRIMARY KEY,
										  name varchar(40) NOT NULL,
										  category varchar(40) NOT NULL,
										  start_date DATE NOT NULL,
										  end_date DATE NOT NULL,
										  place_id INTEGER NOT NULL,
										  trip_id INTEGER NOT NULL,
										  FOREIGN KEY (place_id) REFERENCES places ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS trip_comments (
    									   id IDENTITY PRIMARY KEY ,
    									   comment varchar(160) NOT NULL,
    									   created_on TIMESTAMP NOT NULL,
    									   trip_id INTEGER NOT NULL,
										   user_id INTEGER NOT NULL,
										   FOREIGN KEY (trip_id) REFERENCES trips ON DELETE CASCADE,
										   FOREIGN KEY (user_id) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS trips_users (
										  trips_id INTEGER NOT NULL,
										  users_id INTEGER NOT NULL,
										  FOREIGN KEY (trips_id) REFERENCES trips ON DELETE CASCADE,
										  FOREIGN KEY (users_id) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_pictures (
											 id IDENTITY PRIMARY KEY,
											 user_id INTEGER NOT NULL,
											 image BLOB,
											 FOREIGN KEY (user_id) REFERENCES users ON DELETE CASCADE

);

CREATE TABLE IF NOT EXISTS trip_pictures (
											 id IDENTITY PRIMARY KEY,
											 trip_id INTEGER NOT NULL,
											 image BLOB,
											 FOREIGN KEY (trip_id) REFERENCES trips ON DELETE CASCADE

);

/*
user id = 1
trip_id = 2
place_id = 3
activity_id = 4
trip_place_id = 1
trip_users_id = 1
*/

INSERT INTO places(id, google_id, name, latitude, longitude, address)
values(3, 'google id', 'Bahamas', 100, 100, 'Bahamas address');

INSERT INTO users (id, firstname, lastname, email, password, nationality, birthday)
values (1, 'felipe', 'gorostiaga', 'fgorostiaga@itba.edu.ar', 'password', 'ARG', DATE '1997-06-16');

INSERT INTO trips(id, name, description, start_date, end_date, startplaceid, adminid)
values(2,'test name', 'test description', DATE '2019-08-23', DATE '2020-02-01', 3, 1);

INSERT INTO activities(id, name, category, place_id, start_date, end_date, trip_id)
values(4,'Scuba diving','Sports',3, DATE '2019-09-10', DATE '2019-09-11', 2);

INSERT INTO trips_users(trips_id, users_id)
values(2, 1);

INSERT INTO trip_pictures(id, trip_id, image)
values(1, 2, NULL);

INSERT INTO user_pictures(id, user_id, image)
values(1, 1, NULL);

INSERT INTO trip_comments(id, comment, created_on, trip_id, user_id)
VALUES (1, 'test comment', CURRENT_TIMESTAMP, 2, 1);


