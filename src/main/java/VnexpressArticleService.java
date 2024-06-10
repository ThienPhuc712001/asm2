import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

public class VnexpressArticleService implements ArticleService {
    @Override
    public ArrayList<String> getLinks(String url) {
        HashSet<String> links = new HashSet<>(); // Conllections chua nhung phan` tu? voi gia tri unique
        try {
            Document document = Jsoup.connect(url).get();
            Elements elements = document.getElementsByTag("a");
            for (Element element : elements) {
                String href = element.attr("href");
                if (href.contains("https://vnexpress.net") && href.contains(".html")) {
                    links.add(href);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>(links);
    }

    @Override
    public Article getArticle(String url) {
        Article article = new Article();
        try {
            Document doc = Jsoup.connect(url).get();
            article.setBaseUrl(url);
            article.setTitle(doc.select("h1.title-detail").text());
            article.setDescription(doc.select("p.description").text());
            article.setContent(doc.select(".fck_detail").text());
            article.setThumbnail(doc.select(".fig-picture img").attr("src"));
            article.setCreatedAt(LocalDate.parse(java.time.LocalDateTime.now().toString()));
            article.setUpdatedAt(LocalDate.parse(java.time.LocalDateTime.now().toString()));
            article.setDeletedAt(LocalDate.parse(java.time.LocalDateTime.now().toString()));
            article.setStatus(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return article;
    }
}
