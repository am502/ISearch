package ru.itis.util;

import opennlp.tools.stemmer.snowball.SnowballStemmer;
import ru.stachek66.nlp.mystem.holding.Factory;
import ru.stachek66.nlp.mystem.holding.MyStem;
import ru.stachek66.nlp.mystem.holding.MyStemApplicationException;
import ru.stachek66.nlp.mystem.holding.Request;
import ru.stachek66.nlp.mystem.model.Info;
import scala.Option;
import scala.collection.JavaConversions;

import java.util.*;

public class Stemmer {

    public static List<String> processPorterStem(String[] words) {
        List<String> processedWords = new ArrayList<>();
        SnowballStemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.RUSSIAN);
        for (String word : words) {
            processedWords.add(stemmer.stem(word.toLowerCase()).toString());
        }
        return processedWords;
    }

    public static Map<String, List<String>> processPorterStem(Map<String, List<String>> words) {
        SnowballStemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.RUSSIAN);
        Map<String, List<String>> result = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : words.entrySet()) {
            String word = stemmer.stem(entry.getKey()).toString();
            result.put(word, entry.getValue());
        }
        return result;
    }

    public static Map<String, List<String>> processMyStem(Map<String, List<String>> words) {
        MyStem stemmer = new Factory("-igd --eng-gr --format json --weight")
                .newMyStem("3.0", Option.empty()).get();
        Map<String, List<String>> result = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : words.entrySet()) {
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
}
