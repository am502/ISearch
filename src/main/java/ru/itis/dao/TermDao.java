package ru.itis.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.itis.config.DataConfig;
import ru.itis.models.Term;

import java.util.List;

public class TermDao {
    private static final String GET_ALL_TERMS = "SELECT * FROM terms_list;";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TermDao() {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DataConfig.getInstance().getDataSource());
    }

    public List<Term> getAllTerms() {
        return namedParameterJdbcTemplate.query(GET_ALL_TERMS, new BeanPropertyRowMapper<>(Term.class));
    }
}
