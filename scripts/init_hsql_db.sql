-- Patient Table Creation
CREATE TABLE IF NOT EXISTS patient (
    pid INT IDENTITY PRIMARY KEY,
    firstname VARCHAR(60),
    surname VARCHAR(60),
    date_of_birth DATE,
    carelevel SMALLINT,
    roomNumber SMALLINT,
    lockDate VARCHAR(25),
    deletionDate VARCHAR(25),
    locked BIT DEFAULT 0
);

-- Treatment Table Creation
CREATE TABLE IF NOT EXISTS treatment (
    tid INT IDENTITY PRIMARY KEY,
    pid INT,
    treatment_date DATE,
    "begin" VARCHAR(10),
    "end" VARCHAR(10),
    description VARCHAR(200),
    remarks VARCHAR(200),
    cid INT UNIQUE,
    locked BIT DEFAULT 0
);

-- User Table Creation
CREATE TABLE IF NOT EXISTS users (
    uid INT IDENTITY PRIMARY KEY,
    firstname VARCHAR(60),
    surname VARCHAR(60),
    username VARCHAR(30) UNIQUE,
    password VARCHAR(255),
    userGroup VARCHAR(30)
);

-- Caregiver Table Creation
CREATE TABLE IF NOT EXISTS caregiver (
      cid INT IDENTITY PRIMARY KEY,
      firstname VARCHAR(60),
      surname VARCHAR(60),
      telephone VARCHAR(60)
);
