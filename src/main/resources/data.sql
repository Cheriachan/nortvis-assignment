CREATE TABLE users
(
    id UUID NOT NULL,
    first_name VARCHAR(63) NOT NULL,
    last_name VARCHAR(63) NOT NULL,
    username VARCHAR(63) NOT NULL,
    password VARCHAR(63) NOT NULL,
    phone LONG NOT NULL,
    email VARCHAR(127) NOT NULL,
    created_at LONG,
    PRIMARY KEY (id)
);

CREATE TABLE images
(
    id UUID NOT NULL,
    imgur_id VARCHAR(63) NOT NULL,
    url TEXT NOT NULL,
    delete_hash VARCHAR(63) NOT NULL,
    user_id UUID NOT NULL,
    created_at LONG,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);