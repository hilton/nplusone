# --- !Ups

create table `user` (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id VARCHAR(254) NOT NULL,
  provider_id VARCHAR(254) NOT NULL,
  full_name VARCHAR(254) NOT NULL
);

create table chip (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  patient VARCHAR(32) NOT NULL,
  fracture BOOLEAN NOT NULL DEFAULT 0,
  contusion BOOLEAN NOT NULL DEFAULT 0,
  vomit BOOLEAN NOT NULL DEFAULT 0,
  loc BOOLEAN NOT NULL DEFAULT 0,
  seizure BOOLEAN NOT NULL DEFAULT 0,
  age TINYINT NOT NULL,
  act BOOLEAN NOT NULL DEFAULT 0,
  mechanism VARCHAR(7),
  gcs0 TINYINT,
  gcs1 TINYINT,
  pta TINYINT,
  memory BOOLEAN NOT NULL DEFAULT 0,
  deficit BOOLEAN NOT NULL DEFAULT 0,
  created TIMESTAMP NOT NULL,
  created_by VARCHAR(254) NOT NULL,
  uuid VARCHAR(36) NOT NULL);

# --- !Downs

drop table chip;
drop table `user`;
