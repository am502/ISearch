package ru.itis.task3;

import ru.itis.util.StemProcessor;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Dao dao = new Dao();

        String text = "Обзор хорошей игры";

        List<String> processedWords = new ArrayList<>();
        for (String word : text.split(" ")) {
            processedWords.add(StemProcessor.getInstance().processPorterStem(word.toLowerCase()));
        }

        List<String> sortedWords = dao.getSortedWords(processedWords);

        List<String> urls = dao.getUrls(sortedWords);

        System.out.println("### URLS ###");
        for (String url : urls) {
            System.out.println(url);
        }
    }
}
