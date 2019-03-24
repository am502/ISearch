package ru.itis.task6;

import ru.itis.dao.ArticleDao;
import ru.itis.dao.ArticleTermDao;
import ru.itis.util.Constants;
import ru.itis.util.StemProcessor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OkapiBM25 {
    private static final double K1 = 1.2;
    private static final double B = 0.75;
    private static final double IDF_SMOOTH = 0.5;
    private static final double EPS = 0.01;

    public static void main(String[] args) {
        String text = "Обзор хорошей игры";

        List<String> processedWords = Arrays.stream(text.split(" ")).map(e ->
                StemProcessor.getInstance().processPorterStem(e.toLowerCase())).collect(Collectors.toList());

        ArticleDao articleDao = new ArticleDao();

        ArticleTermDao articleTermDao = new ArticleTermDao();

        Map<String, Integer> articleIdsWithWordCount = articleDao.getArticleIdsWithWordCount();
        double avg = 0;
        for (Integer count : articleIdsWithWordCount.values()) {
            avg += count;
        }
        avg /= Constants.ARTICLES_QUANTITY;

        Map<String, Map<String, Integer>> wordsWithArticleIds = articleTermDao
                .getWordsWithArticleIds("term_text");

        Map<String, Double> scores = new HashMap<>();
        for (Map.Entry<String, Integer> articleEntry : articleIdsWithWordCount.entrySet()) {
            double score = 0;
            for (String processedWord : processedWords.stream().filter(wordsWithArticleIds::containsKey)
                    .collect(Collectors.toList())) {
                Map<String, Integer> articleIdsByProcessedWord = wordsWithArticleIds.get(processedWord);

                int n = articleIdsByProcessedWord.size();
                double idf = Math.log((Constants.ARTICLES_QUANTITY - n + IDF_SMOOTH) / (n + IDF_SMOOTH));
                if (idf < 0) {
                    idf = EPS;
                }

                double tf = (double) articleIdsByProcessedWord.getOrDefault(articleEntry.getKey(), 0)
                        / articleEntry.getValue();

                double dividend = tf * (K1 + 1);

                double divisor = tf + K1 * (1 - B + B * articleEntry.getValue() / avg);
                score += (idf * dividend / divisor);
            }
            scores.put(articleEntry.getKey(), score);
        }

        scores.entrySet().forEach(e -> System.out.println(e.getKey() + " " + e.getValue()));
    }
}
