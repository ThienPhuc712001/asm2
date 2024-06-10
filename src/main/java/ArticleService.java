import java.util.ArrayList;

public interface ArticleService {
    ArrayList<String> getLinks(String url);

    Article getArticle(String url);
}
