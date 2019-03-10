package ru.itis.Task4;

import ru.itis.models.ArticleTerm;
import ru.itis.util.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        Dao dao = new Dao();

        Map<String, Integer> articleIdsWithWordCount = dao.getArticleIdsWithWordCount();

        Map<String, List<String>> wordIdsWithArticleIds = dao.getWordIdsWithArticleIds();

        List<ArticleTerm> articleTerms = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : articleIdsWithWordCount.entrySet()) {
            for (Map.Entry<String, List<String>> wordEntry : wordIdsWithArticleIds.entrySet()) {
                int count = 0;
                for (String s : wordEntry.getValue()) {
                    if (s.equals(entry.getKey())) {
                        count++;
                    }
                }
                double tf = (double) count / entry.getValue();
                double idf = Math.log((double) Constants.ARTICLES_QUANTITY /
                        new HashSet<>(wordEntry.getValue()).size());
                articleTerms.add(new ArticleTerm(entry.getKey(), wordEntry.getKey(), tf * idf));
            }
        }

        dao.updateArticleTerm(articleTerms);
    }
}
