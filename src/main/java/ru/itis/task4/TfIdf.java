package ru.itis.task4;

import ru.itis.dao.ArticleDao;
import ru.itis.dao.ArticleTermDao;
import ru.itis.models.ArticleTerm;
import ru.itis.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TfIdf {
    public static void main(String[] args) {
        ArticleDao articleDao = new ArticleDao();

        Map<String, Integer> articleIdsWithWordCount = articleDao.getArticleIdsWithWordCount();

        ArticleTermDao articleTermDao = new ArticleTermDao();

        List<ArticleTerm> articleTerms = new ArrayList<>();
        for (Map.Entry<String, Map<String, Integer>> wordEntry : articleTermDao
                .getWordsWithArticleIds("term_id").entrySet()) {
            double idf = Math.log((double) Constants.ARTICLES_QUANTITY / wordEntry.getValue().size());
            for (Map.Entry<String, Integer> articleEntry : wordEntry.getValue().entrySet()) {
                double tf = (double) articleEntry.getValue() / articleIdsWithWordCount.get(articleEntry.getKey());
                ArticleTerm articleTerm = ArticleTerm.builder()
                        .termId(wordEntry.getKey())
                        .articleId(articleEntry.getKey())
                        .tfIdf(tf * idf)
                        .idf(idf)
                        .build();
                articleTerms.add(articleTerm);
            }
        }
        articleTermDao.updateArticleTerms(articleTerms);
    }
}
