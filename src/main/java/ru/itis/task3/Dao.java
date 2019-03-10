package ru.itis.task3;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.itis.config.DataConfig;

import java.util.List;

public class Dao {
    private static final String GET_SORTED_WORDS_BEGIN = "SELECT term FROM words_porter WHERE term IN (";
    private static final String GET_SORTED_WORDS_END = ") GROUP BY (term) ORDER BY (COUNT(term));";

    private static final String GET_URLS_PART = "SELECT a.url FROM articles a WHERE id IN " +
            "(SELECT d.article_id FROM article_term d " +
            "INNER JOIN terms_list t ON d.term_id = t.term_id WHERE t.term_text = '";

    private JdbcTemplate jdbcTemplate;

    public Dao() {
        DataConfig dataConfig = new DataConfig();
        jdbcTemplate = dataConfig.jdbcTemplate();
    }

    public List<String> getSortedWords(List<String> words) {
        StringBuilder query = new StringBuilder(GET_SORTED_WORDS_BEGIN);
        for (String word : words) {
            query.append("'");
            query.append(word);
            query.append("',");
        }
        query.deleteCharAt(query.length() - 1);
        query.append(GET_SORTED_WORDS_END);
        return jdbcTemplate.queryForList(query.toString(), String.class);
    }

    public List<String> getUrls(List<String> words) {
        StringBuilder query = new StringBuilder();
        for (int i = 0; i < words.size() - 1; i++) {
            query.append(GET_URLS_PART);
            query.append(words.get(i));
            query.append("') INTERSECT ");
        }
        query.append(GET_URLS_PART);
        query.append(words.get(words.size() - 1));
        query.append("');");
        return jdbcTemplate.queryForList(query.toString(), String.class);
    }
}
