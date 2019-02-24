package ru.itis.task2;

import opennlp.tools.stemmer.snowball.SnowballStemmer;
import ru.itis.dao.ArticleDao;
import ru.itis.dao.StemDao;
import ru.itis.models.Article;
import ru.stachek66.nlp.mystem.holding.Factory;
import ru.stachek66.nlp.mystem.holding.MyStem;
import ru.stachek66.nlp.mystem.holding.MyStemApplicationException;
import ru.stachek66.nlp.mystem.holding.Request;
import ru.stachek66.nlp.mystem.model.Info;
import scala.Option;
import scala.collection.JavaConversions;

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

        Map<String, Set<String>> words = new HashMap<>();

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("src/main/resources/task2/stopwords-ru.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNext()) {
            stopWords.add(scanner.nextLine());
        }

        Pattern pattern = Pattern.compile("[А-Яа-яЁё-]+");

        for (Article article : articles) {
            Matcher matcher = pattern.matcher(article.getContent());
            String articleId = article.getId();
            while (matcher.find()) {
                String word = matcher.group().toLowerCase();
                if (!stopWords.contains(word)) {
                    if (words.containsKey(word)) {
                        words.get(word).add(articleId);
                    } else {
                        Set<String> ids = new HashSet<>();
                        ids.add(articleId);
                        words.put(word, ids);
                    }
                }
            }
        }

        StemDao stemDao = new StemDao();

        stemDao.insertPorterStem(processPorterStem(words));

        stemDao.insertMyStem(processMyStem(words));
    }

    private static Map<String, Set<String>> processPorterStem(Map<String, Set<String>> words) {
        SnowballStemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.RUSSIAN);
        Map<String, Set<String>> result = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : words.entrySet()) {
            String word = stemmer.stem(entry.getKey()).toString();
            result.put(word, entry.getValue());
        }
        return result;
    }

    private static Map<String, Set<String>> processMyStem(Map<String, Set<String>> words) {
        MyStem stemmer = new Factory("-igd --eng-gr --format json --weight")
                .newMyStem("3.0", Option.empty()).get();
        Map<String, Set<String>> result = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : words.entrySet()) {
            try {
                Iterable<Info> infos = JavaConversions.asJavaIterable(stemmer
                        .analyze(Request.apply(entry.getKey()))
                        .info()
                        .toIterable());
                for (Info info : infos) {
                    Option<String> lex = info.lex();
                    if (Objects.nonNull(lex) && lex.isDefined()) {
                        result.put(lex.get(), entry.getValue());
                    }
                }
            } catch (MyStemApplicationException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static void show(Map<String, Set<String>> words) {
        for (Map.Entry<String, Set<String>> entry : words.entrySet()) {
            System.out.print(entry.getKey() + ": ");
            for (String s : entry.getValue()) {
                System.out.print(s + " ");
            }
            System.out.println();
        }
    }
}
