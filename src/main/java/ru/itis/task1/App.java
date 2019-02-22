package ru.itis.task1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.itis.dao.ArticleDao;
import ru.itis.models.Article;
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;
import java.util.List;

public class App {
    private static final int ARTICLES_QUANTITY = 30;

    public static void main(String[] args) {
        ArticleDao articleDao = new ArticleDao();

        Document mainDoc = null;
        try {
            mainDoc = Jsoup.connect("https://www.playground.ru/articles/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> hrefs = Xsoup.compile("//a[@class='item story-container']/@href")
                .evaluate(mainDoc).list();

        for (int i = 0; i < ARTICLES_QUANTITY; i++) {
            String url = hrefs.get(i);
            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String title = Xsoup.compile("//h1//text()").evaluate(doc).get();
            String keywords = Xsoup.compile("//div[@class='article-metadata-content']//a[contains(@href, " +
                    "'articles')]/text()").evaluate(doc).getElements().text().replaceAll(" ", ";");

            String content = doc.select("div.article-content > *:not(footer)").text();

            Article article = Article.builder()
                    .title(title)
                    .keywords(keywords)
                    .content(content)
                    .url(url)
                    .build();

            articleDao.insert(article);
        }
    }
}
