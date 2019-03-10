package ru.itis.Task4;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.itis.config.DataConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dao {
    private static final String DROP_COLUMN = "ALTER TABLE article_term DROP COLUMN IF EXISTS tf_idf;";
    private static final String ADD_COLUMN = "ALTER TABLE article_term ADD COLUMN IF NOT EXISTS tf_idf FLOAT;";

    private static final String GET_ARTICLE_IDS_WITH_WORD_COUNT = "SELECT article_id, COUNT(term) " +
            "AS cnt FROM words_porter GROUP BY (article_id);";
    private static final String GET_WORD_IDS_WITH_ARTICLE_IDS = "SELECT term_id, article_id FROM article_term;";

    private static final String UPDATE_ARTICLE_TERM = "UPDATE article_term SET tf_idf = ? WHERE term_id = ?::UUID " +
            "AND article_id = ?::UUID;";

    private JdbcTemplate jdbcTemplate;

    public Dao() {
        DataConfig dataConfig = new DataConfig();
        jdbcTemplate = dataConfig.jdbcTemplate();
        jdbcTemplate.update(DROP_COLUMN);
        jdbcTemplate.update(ADD_COLUMN);
    }

    public Map<String, Integer> getArticleIdsWithWordCount() {
        return jdbcTemplate.query(GET_ARTICLE_IDS_WITH_WORD_COUNT, (ResultSet rs) -> {
            Map<String, Integer> result = new HashMap<>();
            while (rs.next()) {
                result.put(rs.getString("article_id"), rs.getInt("cnt"));
            }
            return result;
        });
    }

    public Map<String, List<String>> getWordIdsWithArticleIds() {
        return jdbcTemplate.query(GET_WORD_IDS_WITH_ARTICLE_IDS, (ResultSet rs) -> {
            Map<String, List<String>> result = new HashMap<>();
            while (rs.next()) {
                String wordId = rs.getString("term_id");
                String articleId = rs.getString("article_id");
                if (result.containsKey(wordId)) {
                    result.get(wordId).add(articleId);
                } else {
                    List<String> articleIds = new ArrayList<>();
                    articleIds.add(articleId);
                    result.put(wordId, articleIds);
                }
            }
            return result;
        });
    }

    public void updateArticleTerm(double tfIdf, String termId, String articleId) {
        jdbcTemplate.update(UPDATE_ARTICLE_TERM, tfIdf, termId, articleId);
    }
}
