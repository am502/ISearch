package ru.itis.Task4;

import ru.itis.util.Constants;

import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        Dao dao = new Dao();

        Map<String, Integer> articleIdsWithWordCount = dao.getArticleIdsWithWordCount();

        Map<String, List<String>> wordIdsWithArticleIds = dao.getWordIdsWithArticleIds();

        for (Map.Entry<String, Integer> entry : articleIdsWithWordCount.entrySet()) {
            for (Map.Entry<String, List<String>> wordEntry : wordIdsWithArticleIds.entrySet()) {
                int count = 0;
                for (String s : wordEntry.getValue()) {
                    if (s.equals(entry.getKey())) {
                        count++;
                    }
                }
                double tf = (double) count / entry.getValue();
                double idf = Math.log((double) Constants.ARTICLES_QUANTITY / wordEntry.getValue().size());
                dao.updateArticleTerm(tf * idf, wordEntry.getKey(), entry.getKey());
            }
        }
    }
}
