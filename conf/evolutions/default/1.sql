# --- !Ups

create table `USER` (
  ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  USER_ID VARCHAR(254) NOT NULL,
  PROVIDER_ID VARCHAR(254) NOT NULL,
  FULL_NAME VARCHAR(254) NOT NULL
);

# --- !Downs

drop table `USER`;
