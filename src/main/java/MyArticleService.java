import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class MyArticleService implements ArticleService {
    @Override
    public ArrayList<String> getLinks(String url) {
        HashSet<String> links = new HashSet<>(); // Conllections chua nhung phan` tu? voi gia tri unique
        try {
            Document document = Jsoup.connect(url).get();
            Elements elements = document.getElementsByTag("a");
            for (Element element : elements) {
                String href = element.attr("href");
                if (href.contains("https://thethao247.vn") && href.contains(".html")) {
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
            String title = doc.select("h1.title").text();
            String description = doc.select("h2.description").text();
            String content = doc.select("div.content").text();
            String thumbnail = doc.select("img.thumbnail").attr("src");
            Date now = new Date();
            return new Article(0, "https://thethao247.vn", title, description, content, thumbnail, now, now, null, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return article;
    }
}
