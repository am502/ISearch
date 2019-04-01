package ru.itis.task7;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import ru.itis.dao.ArticleDao;
import ru.itis.dao.TermDao;
import ru.itis.models.Article;

import java.util.Arrays;
import java.util.Map;

public class LSI {
    private String[] articleIds;
    private String[] terms;
    private double[][] matrix;

    public LSI() {
        ArticleDao articleDao = new ArticleDao();
        TermDao termDao = new TermDao();

        Map<String, Map<String, Integer>> termsWithArticleIds = termDao.getTermTextsWithArticleIds();

        articleIds = articleDao.getAllArticles().stream().map(Article::getId).toArray(String[]::new);
        terms = termsWithArticleIds.keySet().stream().toArray(String[]::new);

        matrix = new double[terms.length][articleIds.length];

        for (int i = 0; i < terms.length; i++) {
            Map<String, Integer> article = termsWithArticleIds.get(terms[i]);
            for (int j = 0; j < articleIds.length; j++) {
                matrix[i][j] = article.getOrDefault(articleIds[j], 0);
            }
        }
    }

    public static void main(String[] args) {
        LSI lsi = new LSI();

        double[][] exampleQuery = {{0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1}};

        double[][] exampleArray = {
                {1, 1, 1},
                {0, 1, 1},
                {1, 0, 0},
                {0, 1, 0},
                {1, 0, 0},
                {1, 0, 1},
                {1, 1, 1},
                {1, 1, 1},
                {1, 0, 1},
                {0, 2, 0},
                {0, 1, 1}
        };

        int termsCount = exampleArray.length;
        int documentsCount = exampleArray[0].length;

        Matrix q = new Matrix(exampleQuery);
        Matrix matrix = new Matrix(exampleArray);

        SingularValueDecomposition svd = matrix.svd();

        Matrix u = svd.getU();
        Matrix s = svd.getS();
        Matrix vt = svd.getV().transpose();

        u.print(termsCount, documentsCount);
        s.print(termsCount, documentsCount);
        vt.print(termsCount, documentsCount);

        int k = 2;

        Matrix uk = u.getMatrix(0, termsCount - 1, 0, k - 1);
        Matrix sk = s.getMatrix(0, k - 1, 0, k - 1);
        Matrix vtk = vt.getMatrix(0, k - 1, 0, documentsCount - 1);

        uk.print(termsCount, documentsCount);
        sk.print(termsCount, documentsCount);
        vtk.print(termsCount, documentsCount);

        Matrix result = (q.times(uk)).times(sk);

        result.print(termsCount, documentsCount);
    }

    public String[] getArticleIds() {
        return articleIds;
    }

    public String[] getTerms() {
        return terms;
    }

    public double[][] getMatrix() {
        return matrix;
    }
}
