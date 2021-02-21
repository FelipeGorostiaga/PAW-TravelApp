CREATE TABLE IF NOT EXISTS places (
									  id IDENTITY PRIMARY KEY,
									  google_id varchar(150) UNIQUE NOT NULL,
									  name varchar(100) NOT NULL,
									  latitude double precision NOT NULL,
									  longitude double precision NOT NULL,
									  address varchar(500) NOT NULL
)

CREATE TABLE IF NOT EXISTS users (
									 id IDENTITY PRIMARY KEY,
									 firstname varchar(100) NOT NULL,
									 lastname varchar(100) NOT NULL,
									 email varchar(100) UNIQUE NOT NULL,
									 password varchar(100) NOT NULL,
									 sex varchar(100) NOT NULL,
									 nationality varchar(100) NOT NULL,
									 token varchar(64) NOT NULL,
									 birthday DATE
)

CREATE TABLE IF NOT EXISTS trips (
									 id IDENTITY PRIMARY KEY,
									 name varchar(100) NOT NULL,
									 description varchar(500),
									 start_date DATE,
									 end_date DATE,
									 startplaceid INTEGER NOT NULL
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

INSERT INTO places(id, google_id, name, latitude, longitude, address) values(3, 'google id', 'Bahamas', 100, 100, 'Bahamas address');



