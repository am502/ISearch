package ru.itis.task3;

import opennlp.tools.stemmer.snowball.SnowballStemmer;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Dao dao = new Dao();

        String text = "Карта Деньги Перифирия";

        List<String> processedWords = processPorterStem(text.split(" "));

        List<String> sortedWords = dao.getSortedWords(processedWords);

        List<String> urls = dao.getUrls(sortedWords);

        System.out.println("### URLS ###");
        for (String url : urls) {
            System.out.println(url);
        }
    }

    private static List<String> processPorterStem(String[] words) {
        List<String> processedWords = new ArrayList<>();
        SnowballStemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.RUSSIAN);
        for (String word : words) {
            processedWords.add(stemmer.stem(word.toLowerCase()).toString());
        }
        return processedWords;
    }
}
