package ru.itis.task2;

import ru.itis.dao.ArticleDao;
import ru.itis.dao.StemDao;
import ru.itis.models.Article;
import ru.itis.util.Stemmer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) {
        ArticleDao articleDao = new ArticleDao();

        List<Article> articles = articleDao.getAllArticles();

        List<String> stopWords = new ArrayList<>();

        Map<String, List<String>> words = new HashMap<>();

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("src/main/resources/task2/stopwords-ru.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNext()) {
            stopWords.add(scanner.nextLine());
        }

        Pattern pattern = Pattern.compile("[А-Яа-яЁё]+(-[А-Яа-яЁё]+)*");

        for (Article article : articles) {
            Matcher matcher = pattern.matcher(article.getContent());
            String articleId = article.getId();
            while (matcher.find()) {
                String word = matcher.group().toLowerCase();
                if (!stopWords.contains(word)) {
                    if (words.containsKey(word)) {
                        words.get(word).add(articleId);
                    } else {
                        List<String> ids = new ArrayList<>();
                        ids.add(articleId);
                        words.put(word, ids);
                    }
                }
            }
        }

        StemDao stemDao = new StemDao();

        stemDao.insertPorterStem(Stemmer.processPorterStem(words));

        stemDao.insertMyStem(Stemmer.processMyStem(words));
    }
}
