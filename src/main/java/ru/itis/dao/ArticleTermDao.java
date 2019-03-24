package ru.itis.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import ru.itis.config.DataConfig;
import ru.itis.models.ArticleTerm;
import ru.itis.models.ModifiedArticleTerm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleTermDao {
    private static final String UPDATE_ARTICLE_TERM = "UPDATE article_term SET tf_idf = :tfIdf, idf = :idf " +
            "WHERE article_id = :articleId::UUID AND term_id = :termId::UUID;";
    private static final String GET_WORDS_WITH_ARTICLE_IDS = "SELECT t.term_id, t.term_text, p.article_id " +
            "FROM words_porter p INNER JOIN terms_list t ON t.term_text = p.term;";
    private static final String GET_ALL_MODIFIED_ARTICLE_TERMS = "SELECT at.tf_idf, at.idf, " +
            "a.url AS article_url, t.term_text AS term FROM article_term at " +
            "INNER JOIN articles a ON at.article_id = a.id INNER JOIN terms_list t ON t.term_id = at.term_id;";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ArticleTermDao() {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DataConfig.getInstance().getDataSource());
    }

    public void updateArticleTerms(List<ArticleTerm> articleTerms) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(articleTerms.toArray());
        namedParameterJdbcTemplate.batchUpdate(UPDATE_ARTICLE_TERM, batch);
    }

    public Map<String, Map<String, Integer>> getWordsWithArticleIds(String termField) {
        return namedParameterJdbcTemplate.getJdbcTemplate().query(GET_WORDS_WITH_ARTICLE_IDS, rs -> {
            Map<String, Map<String, Integer>> result = new HashMap<>();
            while (rs.next()) {
                String term = rs.getString(termField);
                String articleId = rs.getString("article_id");
                if (result.containsKey(term)) {
                    Map<String, Integer> article = result.get(term);
                    if (article.containsKey(articleId)) {
                        article.put(articleId, article.get(articleId) + 1);
                    } else {
                        article.put(articleId, 1);
                    }
                } else {
                    result.put(term, new HashMap<String, Integer>() {{
                        put(articleId, 1);
                    }});
                }
            }
            return result;
        });
    }

    public List<ModifiedArticleTerm> getAllModifiedArticleTerms() {
        return namedParameterJdbcTemplate
                .query(GET_ALL_MODIFIED_ARTICLE_TERMS, new BeanPropertyRowMapper<>(ModifiedArticleTerm.class));
    }
}
