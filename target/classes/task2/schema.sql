DROP TABLE IF EXISTS words_porter;
DROP TABLE IF EXISTS words_mystem;

CREATE TABLE words_porter (
  id BIGSERIAL,
  term VARCHAR (64),
  article_id uuid
);

CREATE TABLE words_mystem (
  id BIGSERIAL,
  term VARCHAR (64),
  article_id uuid
);