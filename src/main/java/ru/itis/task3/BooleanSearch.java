package ru.itis.task3;

import ru.itis.dao.ArticleDao;
import ru.itis.util.StemProcessor;

import java.util.ArrayList;
import java.util.List;

public class BooleanSearch {
    public static void main(String[] args) {
        String text = "Обзор хорошей игры";

        List<String> processedWords = new ArrayList<>();
        for (String word : text.split(" ")) {
            processedWords.add(StemProcessor.getInstance().processPorterStem(word.toLowerCase()));
        }

        ArticleDao articleDao = new ArticleDao();

        articleDao.getUrlsByWords(processedWords).forEach(System.out::println);
    }
}
