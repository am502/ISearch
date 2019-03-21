package ru.itis.dao;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import ru.itis.config.DataConfig;
import ru.itis.models.ArticleTerm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleTermDao {
    private static final String UPDATE_ARTICLE_TERM = "UPDATE article_term SET tf_idf = :tfIdf, idf = :idf " +
            "WHERE article_id = :articleId::UUID AND term_id = :termId::UUID;";
    private static final String GET_WORD_IDS_WITH_ARTICLE_IDS = "SELECT term_id, article_id FROM article_term;";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ArticleTermDao() {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DataConfig.getInstance().getDataSource());
    }

    public void updateArticleTerms(List<ArticleTerm> articleTerms) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(articleTerms.toArray());
        namedParameterJdbcTemplate.batchUpdate(UPDATE_ARTICLE_TERM, batch);
    }

    public Map<String, List<String>> getWordIdsWithArticleIds() {
        return namedParameterJdbcTemplate.getJdbcTemplate().query(GET_WORD_IDS_WITH_ARTICLE_IDS, rs -> {
            Map<String, List<String>> result = new HashMap<>();
            while (rs.next()) {
                String termId = rs.getString("term_id");
                String articleId = rs.getString("article_id");
                if (result.containsKey(termId)) {
                    result.get(termId).add(articleId);
                } else {
                    result.put(termId, new ArrayList<String>() {{
                        add(articleId);
                    }});
                }
            }
            return result;
        });
    }
}
