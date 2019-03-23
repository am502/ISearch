package ru.itis.dao;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import ru.itis.config.DataConfig;
import ru.itis.models.ArticleTerm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleTermDao {
    private static final String UPDATE_ARTICLE_TERM = "UPDATE article_term SET tf_idf = :tfIdf, idf = :idf " +
            "WHERE article_id = :articleId::UUID AND term_id = :termId::UUID;";
    private static final String GET_WORD_IDS_WITH_ARTICLE_IDS = "SELECT t.term_id, p.article_id " +
            "FROM words_porter p INNER JOIN terms_list t ON t.term_text = p.term;";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ArticleTermDao() {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DataConfig.getInstance().getDataSource());
    }

    public void updateArticleTerms(List<ArticleTerm> articleTerms) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(articleTerms.toArray());
        namedParameterJdbcTemplate.batchUpdate(UPDATE_ARTICLE_TERM, batch);
    }

    public Map<String, Map<String, Integer>> getWordIdsWithArticleIds() {
        return namedParameterJdbcTemplate.getJdbcTemplate().query(GET_WORD_IDS_WITH_ARTICLE_IDS, rs -> {
            Map<String, Map<String, Integer>> result = new HashMap<>();
            while (rs.next()) {
                String termId = rs.getString("term_id");
                String articleId = rs.getString("article_id");
                if (result.containsKey(termId)) {
                    Map<String, Integer> article = result.get(termId);
                    if (article.containsKey(articleId)) {
                        article.put(articleId, article.get(articleId) + 1);
                    } else {
                        article.put(articleId, 1);
                    }
                } else {
                    result.put(termId, new HashMap<String, Integer>() {{
                        put(articleId, 1);
                    }});
                }
            }
            return result;
        });
    }
}
