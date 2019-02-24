package ru.itis.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.itis.config.DataConfig;
import ru.itis.models.Article;

import java.util.List;

public class ArticleDao {
    private static final String INSERT_SQL = "INSERT INTO articles (title, keywords, content, url, student_id) " +
            "VALUES (?, ?, ?, ?, ?);";
    private static final String GET_ALL_SQL = "SELECT * FROM articles;";

    private static final int STUDENT_ID = 110;

    private JdbcTemplate jdbcTemplate;

    public ArticleDao() {
        DataConfig dataConfig = new DataConfig();
        jdbcTemplate = dataConfig.jdbcTemplate();
    }

    public void insert(Article article) {
        jdbcTemplate.update(INSERT_SQL,
                article.getTitle(),
                article.getKeywords(),
                article.getContent(),
                article.getUrl(),
                STUDENT_ID);
    }

    public List<Article> getAllArticles() {
        return jdbcTemplate.query(GET_ALL_SQL, new BeanPropertyRowMapper<>(Article.class));
    }
}
