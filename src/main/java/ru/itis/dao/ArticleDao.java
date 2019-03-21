package ru.itis.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import ru.itis.config.DataConfig;
import ru.itis.models.Article;

import java.util.List;

public class ArticleDao {
    private static final String INSERT_ARTICLE = "INSERT INTO articles (title, keywords, content, url, student_id) " +
            "VALUES (:title, :keywords, :content, :url, :studentId);";
    private static final String GET_ALL_ARTICLES = "SELECT * FROM articles;";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ArticleDao() {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DataConfig.getInstance().getDataSource());
    }

    public void insertArticle(Article article) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(article);
        namedParameterJdbcTemplate.update(INSERT_ARTICLE, parameters);
    }

    public List<Article> getAllArticles() {
        return namedParameterJdbcTemplate.query(GET_ALL_ARTICLES, new BeanPropertyRowMapper<>(Article.class));
    }
}
