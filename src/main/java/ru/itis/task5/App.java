package ru.itis.task5;

import ru.itis.models.Article;
import ru.itis.models.ArticleTerm;
import ru.itis.util.StemProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class App {
    public static void main(String[] args) {
        Dao dao = new Dao();

        String text = "Обзор хорошей игры";

        List<String> processedWords = new ArrayList<>();
        for (String word : text.split(" ")) {
            processedWords.add(StemProcessor.getInstance().processPorterStem(word.toLowerCase()));
        }

        List<Article> articles = dao.getArticleIdsByWords(processedWords);

        TreeMap<Double, String> scores = new TreeMap<>();

        for (Article article : articles) {
            List<ArticleTerm> articleTerms = dao.getArticleTermsByArticleId(article.getId());
            double a2 = 0;
            double ab = 0;
            double b2 = 0;
            for (ArticleTerm articleTerm : articleTerms) {
                a2 += (articleTerm.getTfIdf() * articleTerm.getTfIdf());
            }
            a2 = Math.sqrt(a2);
            for (String word : processedWords) {
                for (ArticleTerm articleTerm : articleTerms) {
                    if (articleTerm.getTerm().equals(word)) {
                        ab += (articleTerm.getIdf() * articleTerm.getTfIdf());
                        b2 += (articleTerm.getIdf() * articleTerm.getIdf());
                    }
                }
            }
            b2 = Math.sqrt(b2);
            scores.put(ab / (a2 * b2), article.getUrl());
        }

        System.out.println("### RESULT ###");

        List<Double> keysList = new ArrayList<>(scores.descendingKeySet());
        keysList.stream().limit(10).forEach(e -> {
            System.out.println(e + " " + scores.get(e));
        });
    }
}
