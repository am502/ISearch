package ru.itis.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.itis.config.DataConfig;

import java.util.List;
import java.util.Map;

public class StemDao {
    private static final String INSERT_MYSTEM_SQL = "INSERT INTO words_mystem (term, article_id) VALUES";
    private static final String INSERT_PORTERSTEM_SQL = "INSERT INTO words_porter (term, article_id) VALUES";

    private JdbcTemplate jdbcTemplate;

    public StemDao() {
        DataConfig dataConfig = new DataConfig();
        jdbcTemplate = dataConfig.jdbcTemplate();
    }

    public void insertPorterStem(Map<String, List<String>> words) {
        insertStem(INSERT_PORTERSTEM_SQL, words);
    }

    public void insertMyStem(Map<String, List<String>> words) {
        insertStem(INSERT_MYSTEM_SQL, words);
    }

    private void insertStem(String sql, Map<String, List<String>> words) {
        StringBuilder query = new StringBuilder(sql);
        for (Map.Entry<String, List<String>> entry : words.entrySet()) {
            String key = entry.getKey();
            for (String s : entry.getValue()) {
                query
                        .append(" ('")
                        .append(key)
                        .append("', '")
                        .append(s)
                        .append("'),");
            }
        }
        query.deleteCharAt(query.length() - 1);
        query.append(';');
        jdbcTemplate.update(query.toString());
    }
}
