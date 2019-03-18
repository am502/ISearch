package ru.itis.task3;

import ru.itis.util.Stemmer;

import java.util.List;

public class App {
    public static void main(String[] args) {
        Dao dao = new Dao();

        String text = "Обзор зла";

        List<String> processedWords = Stemmer.processPorterStem(text.split(" "));

        List<String> sortedWords = dao.getSortedWords(processedWords);

        List<String> urls = dao.getUrls(sortedWords);

        System.out.println("### URLS ###");
        for (String url : urls) {
            System.out.println(url);
        }
    }
}
