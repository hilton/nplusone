# --- !Ups

create table `USER` (
  ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  USER_ID VARCHAR(254) NOT NULL,
  PROVIDER_ID VARCHAR(254) NOT NULL,
  FULL_NAME VARCHAR(254) NOT NULL,
  ACCESS_TOKEN VARCHAR(254),
  TOKEN_TYPE VARCHAR(254),
  EXPIRES_IN INTEGER,
  REFRESH_TOKEN VARCHAR(254)
);

# --- !Downs

drop table `USER`;
