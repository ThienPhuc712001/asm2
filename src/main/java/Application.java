import java.util.ArrayList;
import java.util.Scanner;

public class Application {
    private static ArticleRepository articleRepository = new MySqlArticleRepository();
    private static ArticleService vnexpressService = new VnexpressArticleService();
    private static ArticleService myArticleService = new MyArticleService();

    public static void main(String[] args) {
        generateMenu();
    }

    public static void generateMenu() {
        Scanner scanner = new Scanner(System.in);
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
                    crawlArticles(vnexpressService);
                    break;
                case 2:
                    crawlArticles(myArticleService);
                    break;
                case 3:
                    displayArticles();
                    break;
                case 4:
                    System.out.println("Thoát chương trình.");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private static void crawlArticles(ArticleService articleService) {
        ArrayList<String> links = articleService.getLinks("some_url");
        for (String link : links) {
            Article article = articleService.getArticle(link);
            articleRepository.save(article);
        }
        System.out.println("Crawl xong.");
    }

    private static void displayArticles() {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Article> articles = articleRepository.findAll();
        for (Article article : articles) {
            System.out.println(article);
        }
        while (true) {
            System.out.print("Nhập mã tin cần xem chi tiết hoặc 'exit' để thoát: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            } else {
                try {
                    int articleId = Integer.parseInt(input);
                    Article article = articleRepository.findByUrl(articles.get(articleId).getBaseUrl());
                    System.out.println(article);
                } catch (Exception e) {
                    System.out.println("Mã tin không hợp lệ.");
                }
            }
        }
    }
}
