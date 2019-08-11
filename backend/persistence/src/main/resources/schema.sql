/*CREATE TABLE IF NOT EXISTS places (
                id SERIAL PRIMARY KEY,
                google_id varchar(150) UNIQUE NOT NULL,
                name varchar(100) NOT NULL,
                latitude double precision NOT NULL,
                longitude double precision NOT NULL,
                address varchar(500) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
				id SERIAL PRIMARY KEY,
				firstname varchar(100) NOT NULL,
				lastname varchar(100) NOT NULL,
				email varchar(100) UNIQUE NOT NULL,
				password varchar(100) NOT NULL,
				nationality varchar(100) NOT NULL,
				birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS trips (
                id SERIAL PRIMARY KEY,
                name varchar(100) NOT NULL,
                description varchar(500),
                start_date DATE,
                end_date DATE,
                startplace_id INTEGER NOT NULL,
                FOREIGN KEY (startplace_id) REFERENCES places ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS activities (
                id SERIAL PRIMARY KEY,
                name varchar(40) NOT NULL,
                category varchar(40) NOT NULL,
                place_id integer NOT NULL,
                FOREIGN KEY (place_id) REFERENCES places ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS trip_places (
                id SERIAL PRIMARY KEY,
                trip_id INTEGER NOT NULL,
                place_id INTEGER NOT NULL,
                FOREIGN KEY (trip_id) REFERENCES trips ON DELETE CASCADE,
                FOREIGN KEY (place_id) REFERENCES places ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS trip_activities (
                id SERIAL PRIMARY KEY,
                trip_id INTEGER NOT NULL,
                activity_id INTEGER NOT NULL,
                FOREIGN KEY (trip_id) REFERENCES trips ON DELETE CASCADE,
                FOREIGN KEY (activity_id) REFERENCES activities ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS trip_users (
                id SERIAL PRIMARY KEY,
                trip_id INTEGER NOT NULL,
                user_id INTEGER NOT NULL,
                user_role varchar(10) NOT NULL,
                FOREIGN KEY (trip_id) REFERENCES trips ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_pictures (
                id SERIAL PRIMARY KEY,
                user_id INTEGER NOT NULL,
                image BYTEA NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users ON DELETE CASCADE

);

CREATE TABLE IF NOT EXISTS trip_pictures (
               id SERIAL PRIMARY KEY,
               trip_id INTEGER NOT NULL,
               image BYTEA NOT NULL,
               FOREIGN KEY (trip_id) REFERENCES trips ON DELETE CASCADE

);*/
