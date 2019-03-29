package ru.itis.task7;

import ru.itis.dao.ArticleDao;
import ru.itis.dao.TermDao;
import ru.itis.models.Article;

import java.util.Arrays;
import java.util.Map;

public class LSI {
    private String[] articleIds;
    private String[] terms;
    private Integer[][] matrix;

    public LSI() {
        ArticleDao articleDao = new ArticleDao();
        TermDao termDao = new TermDao();

        Map<String, Map<String, Integer>> termsWithArticleIds = termDao.getTermTextsWithArticleIds();

        articleIds = articleDao.getAllArticles().stream().map(Article::getId).toArray(String[]::new);
        terms = termsWithArticleIds.keySet().stream().toArray(String[]::new);

        matrix = new Integer[terms.length][articleIds.length];

        for (int i = 0; i < terms.length; i++) {
            Map<String, Integer> article = termsWithArticleIds.get(terms[i]);
            for (int j = 0; j < articleIds.length; j++) {
                matrix[i][j] = article.getOrDefault(articleIds[j], 0);
            }
        }
    }

    public static void main(String[] args) {
        LSI lsi = new LSI();

        System.out.println(Arrays.toString(lsi.getTerms()));
        System.out.println(Arrays.toString(lsi.getArticleIds()));
        System.out.println(Arrays.deepToString(lsi.getMatrix()));
    }

    public String[] getArticleIds() {
        return articleIds;
    }

    public String[] getTerms() {
        return terms;
    }

    public Integer[][] getMatrix() {
        return matrix;
    }
}
