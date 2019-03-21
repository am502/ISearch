package ru.itis.Task4;

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

        ArticleTermDao articleTermDao = new ArticleTermDao();

        List<ArticleTerm> articleTerms = new ArrayList<>();

        int c1 = 0;
        for (Map.Entry<String, Integer> articleEntry : articleDao.getArticleIdsWithWordCount().entrySet()) {
            int c = 0;

            for (Map.Entry<String, List<String>> wordEntry : articleTermDao
                    .getWordIdsWithArticleIds().entrySet()) {
                int count = 0;
                for (String s : wordEntry.getValue()) {
                    if (s.equals(articleEntry.getKey())) {
                        count++;
                    }
                }
                c+=wordEntry.getValue().size();
                double tf = (double) count / articleEntry.getValue();
                double idf = Math.log((double) Constants.ARTICLES_QUANTITY / wordEntry.getValue().size());
                ArticleTerm articleTerm = ArticleTerm.builder()
                        .termId(wordEntry.getKey())
                        .articleId(articleEntry.getKey())
                        .tfIdf(tf * idf)
                        .idf(idf)
                        .build();
                articleTerms.add(articleTerm);
            }
            c1+=articleEntry.getValue();
            System.out.println(c);
        }
        System.out.println(c1);

//        articleTermDao.updateArticleTerms(articleTerms);
    }
}
