package ru.itis.task5;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.itis.config.DataConfig;
import ru.itis.models.Article;
import ru.itis.models.ArticleTerm;

import java.util.List;

public class Dao {
    private static final String GET_ARTICLE_IDS_BY_WORDS_BEGIN = "SELECT a1.id, a1.url FROM article_term a " +
            "INNER JOIN terms_list t ON t.term_id = a.term_id INNER JOIN articles a1 ON a.article_id = a1.id " +
            "WHERE t.term_text IN (";
    private static final String GET_ARTICLE_IDS_BY_WORDS_END = ") GROUP BY (a1.id, a1.url);";

    private static final String GET_ARTICLE_TERMS_BY_ARTICLE_ID = "SELECT a.article_id, a.tf_idf, a.idf, " +
            "t.term_text AS term FROM article_term a INNER JOIN terms_list t ON t.term_id = a.term_id WHERE " +
            "a.article_id = '%s';";

    private JdbcTemplate jdbcTemplate;

    public Dao() {
        DataConfig dataConfig = new DataConfig();
        jdbcTemplate = dataConfig.jdbcTemplate();
    }

    public List<Article> getArticleIdsByWords(List<String> words) {
        StringBuilder query = new StringBuilder(GET_ARTICLE_IDS_BY_WORDS_BEGIN);
        for (String word : words) {
            query.append("'");
            query.append(word);
            query.append("',");
        }
        query.deleteCharAt(query.length() - 1);
        query.append(GET_ARTICLE_IDS_BY_WORDS_END);
        return jdbcTemplate.query(query.toString(), new BeanPropertyRowMapper<>(Article.class));
    }

    public List<ArticleTerm> getArticleTermsByArticleId(String articleId) {
        return jdbcTemplate.query(String.format(GET_ARTICLE_TERMS_BY_ARTICLE_ID, articleId),
                new BeanPropertyRowMapper<>(ArticleTerm.class));
    }
}
