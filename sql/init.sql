-- Alice User
CREATE SEQUENCE seq_alice_user;

CREATE TABLE alice_user (
  id           BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('seq_alice_user'),
  version      BIGINT                      NOT NULL             DEFAULT 0,
  date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL             DEFAULT now(),
  last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL             DEFAULT now(),
  yandex_id    CHARACTER VARYING(64)       NOT NULL UNIQUE
);

-- Alice Memory
CREATE SEQUENCE seq_memory;

CREATE TABLE memory (
  id           BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('seq_memory'),
  version      BIGINT                      NOT NULL             DEFAULT 0,
  date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL             DEFAULT now(),
  last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL             DEFAULT now(),
  user_id      BIGINT                      NOT NULL REFERENCES alice_user (id),
  text         CHARACTER VARYING(1024)     NOT NULL
);

CREATE INDEX idx_memory_user
  ON memory (user_id);