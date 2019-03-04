DROP TABLE IF EXISTS terms_list;
DROP TABLE IF EXISTS doc_term;

CREATE TABLE terms_list (
  term_id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
  term_text VARCHAR (64)
);

CREATE TABLE doc_term (
  article_id uuid,
  term_id uuid
);

INSERT INTO terms_list (term_id, term_text)
  SELECT DISTINCT ON (w.term) w.id, w.term FROM words_porter w ORDER BY (w.term) ASC;

INSERT INTO doc_term (article_id, term_id)
  SELECT w.article_id, t.term_id FROM words_porter w INNER JOIN terms_list t ON t.term_text = w.term;