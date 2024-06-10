import java.util.ArrayList;
import java.util.Scanner;

public class Application {
    private static final ArticleService vnexpressService = new VnexpressArticleService();
    private static final ArticleService myArticleService = new MyArticleService();
    private static final ArticleRepository articleRepository = new MySqlArticleRepository();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        generateMenu();
    }

    private static void crawlFromSource(ArticleService articleService, String url) {
        ArrayList<String> links = articleService.getLinks(url);
        for (String link : links) {
            Article article = articleService.getArticle(link);
            if (article != null) {
                articleRepository.save(article);
            }
        }
        System.out.println("Crawl thông tin hoàn tất.");
    }

    private static void displayArticles() {
        ArrayList<Article> articles = articleRepository.findAll();
        for (Article article : articles) {
            System.out.println(article.getId() + ": " + article.getTitle());
        }
        System.out.println("Nhập mã tin cần xem chi tiết hoặc 'exit' để quay lại:");
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                return;
            }
            try {
                int articleId = Integer.parseInt(input);
                Article article = articles.stream()
                        .filter(a -> a.getId() == articleId)
                        .findFirst()
                        .orElse(null);
                if (article != null) {
                    System.out.println(article);
                } else {
                    System.out.println("Mã tin không hợp lệ.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Mã tin không hợp lệ.");
            }
        }
    }
    private static void generateMenu() {
        while (true) {
            System.out.println("1. Crawl thông tin từ Vnexpress");
            System.out.println("2. Crawl thông tin từ nguồn của tôi");
            System.out.println("3. Hiển thị danh sách tin hiện có");
            System.out.println("4. Thoát chương trình");
            System.out.print("Chọn: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    crawlFromSource(vnexpressService, "https://vnexpress.net");
                    break;
                case 2:
                    crawlFromSource(myArticleService, "https://thethao247.vn");
                    break;
                case 3:
                    displayArticles();
                    break;
                case 4:
                    System.out.println("Thoát chương trình.");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        }
    }

}

