DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE oauth_access_token
(
  token_id          VARCHAR(256) NULL,
  token             BLOB         NULL,
  authentication_id VARCHAR(256) NULL,
  user_name         VARCHAR(256) NULL,
  client_id         VARCHAR(256) NULL,
  authentication    BLOB         NULL,
  refresh_token     VARCHAR(256) NULL
);
DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE oauth_refresh_token
(
  token_id       VARCHAR(256) NULL,
  token          BLOB         NULL,
  authentication BLOB         NULL
);
DROP TABLE IF EXISTS `sessions`;
CREATE TABLE sessions
(
  session_id VARCHAR(255)     NOT NULL PRIMARY KEY,
  expires    INT(11) UNSIGNED NOT NULL,
  data       TEXT             NULL
);
INSERT INTO engine_user (fullname, username, email, password, activated, phone, `type`)
VALUES ('ADMIN', 'admin', 'admin@mail.me','b8f57d6d6ec0a60dfe2e20182d4615b12e321cad9e2979e0b9f81e0d6eda78ad9b6dcfe53e4e22d1', true, '+375259177315','ROLE_ADMIN');
