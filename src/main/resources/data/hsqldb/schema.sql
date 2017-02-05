DROP TABLE User IF EXISTS;
DROP TABLE Worker IF EXISTS;
DROP TABLE Client IF EXISTS;
DROP TABLE Task IF EXISTS;
DROP TABLE Task_type IF EXISTS;
DROP TABLE Object IF EXISTS;
DROP TABLE Report IF EXISTS;
DROP TABLE Material IF EXISTS;
DROP TABLE Responsible_person IF EXISTS;
DROP TABLE Image IF EXISTS;

CREATE TABLE User (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL,
  email VARCHAR(50) NOT NULL,
  name VARCHAR(50),
  password VARCHAR(50) NOT NULL,
  companyName VARCHAR(50),
  isver BOOLEAN,
  PRIMARY KEY(id)
);

CREATE TABLE Worker (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL,
  email VARCHAR(50) NOT NULL,
  name VARCHAR(50) NOT NULL,
  password VARCHAR(50) NOT NULL,
  companyName VARCHAR(50) NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE Client (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL,
  email VARCHAR(50) NOT NULL,
  name VARCHAR(50) NOT NULL,
  companyName VARCHAR(50) NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE Task (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL,
  title VARCHAR(50) NOT NULL,
  description VARCHAR(50),
  rating INTEGER,
  agreement boolean,
  signature_type INTEGER,
  abort_message VARCHAR(50),
  client_note VARCHAR(50),
  client_note_reply VARCHAR(50),
  distance INTEGER,
  work_price INTEGER,
  material_price INTEGER,
  final_price INTEGER,
  status INTEGER,
  planned_time VARCHAR(50) NOT NULL,
  planned_end_time VARCHAR(50),
  fetched BOOLEAN,
  start_time VARCHAR(50),
  end_time VARCHAR(50),
  start_on_time BOOLEAN,
  duration BIGINT,
  bill_status BOOLEAN,
  bill_date VARCHAR(50),
  file_exists BOOLEAN,
  worker BIGINT,
  task_type BIGINT NOT NULL,
  object BIGINT NOT NULL,
  client BIGINT NOT NULL,
  created VARCHAR(50),
  updated VARCHAR(50),
  PRIMARY KEY(id)
);

CREATE TABLE Task_type (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL,
  title VARCHAR(50) NOT NULL,
  mail_list VARCHAR(500),
  signature BOOLEAN,
  created VARCHAR(50),
  updated VARCHAR(50),
  PRIMARY KEY(id)
);

CREATE TABLE Object (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL,
  address VARCHAR(50) NOT NULL,
  client BIGINT NOT NULL,
  responsiblePerson VARCHAR(50) NOT NULL,
  created VARCHAR(50),
  updated VARCHAR(50),
  PRIMARY KEY(id)
);

CREATE TABLE Report (
  id int,
  company_name VARCHAR(50),
  company_code VARCHAR(50),
  company_address VARCHAR(50),
  company_phone VARCHAR(50),
  report_text VARCHAR(500),
  show_price BOOLEAN,
  show_description BOOLEAN,
  show_start BOOLEAN,
  show_finish BOOLEAN,
  show_duration BOOLEAN 
);

CREATE TABLE Material (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL,
  title VARCHAR(50) NOT NULL,
  serial_number VARCHAR(50) NOT NULL,
  created VARCHAR(50),
  updated VARCHAR(50),
  PRIMARY KEY(id)
);

CREATE TABLE Responsible_person (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL,
  first_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  phone VARCHAR(50) NOT NULL,
  email VARCHAR(50) NOT NULL,
  client BIGINT NOT NULL,
  created VARCHAR(50),
  updated VARCHAR(50),
  PRIMARY KEY(id)
);

CREATE TABLE Image (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL,
  name VARCHAR(50),
  path VARCHAR(50),
  task BIGINT,
  created VARCHAR(50),
  PRIMARY KEY(id)
);