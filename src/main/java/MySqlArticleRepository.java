import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MySqlArticleRepository implements ArticleRepository{
    DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // Tên hằng số
    private final String MYSQL_CONNECTION_STRING = "jdbc:mysql://localhost:3306/asm2";
    private final String MYSQL_USERNAME = "root";
    private final String MYSQL_PASSWORD = "";
    @Override
    public ArrayList<Article> findAll() {
        ArrayList<Article> articles = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(MYSQL_CONNECTION_STRING,
                    MYSQL_USERNAME,
                    MYSQL_PASSWORD);
            String sql = "select * from articles where status = 1";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String baseUrl = resultSet.getString("baseUrl");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String content = resultSet.getString("content");
                String thumbnail = resultSet.getString("thumbnail");
                String createdAt = resultSet.getString("createdAt");
                String updatedAt = resultSet.getString("updatedAt");
                String deletedAt = resultSet.getString("deletedAt");
                int status = resultSet.getInt("status");
                Article article = new Article();
                article.setId(id);
                article.setBaseUrl(baseUrl);
                article.setTitle(title);
                article.setDescription(description);
                article.setContent(content);
                article.setThumbnail(thumbnail);
                article.setCreatedAt(LocalDate.parse(createdAt, formatter));
                article.setUpdatedAt(LocalDate.parse(updatedAt, formatter));
                article.setDeletedAt(LocalDate.parse(deletedAt, formatter));
                article.setStatus(status);
                articles.add(article);
            }
            connection.close();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return articles;
    }

    @Override
    public Article findByUrl(String url) {
        Article article = null;
        try {
            Connection connection = DriverManager.getConnection(MYSQL_CONNECTION_STRING, MYSQL_USERNAME, MYSQL_PASSWORD);
            String sql = "select * from articles where baseurl = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, url);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String content = resultSet.getString("content");
                String thumbnal = resultSet.getString("thumbnail");
                String createdAt = resultSet.getString("createdAt");
                String updatedAt = resultSet.getString("updatedAt");
                String delatedAt = resultSet.getString("delatedAt");
                int status = resultSet.getInt("status");
                article = new Article();
                article.setId(id);
                article.setTitle(title);
                article.setDescription(description);
                article.setContent(content);
                article.setThumbnail(thumbnal);
                article.setCreatedAt(LocalDate.parse(createdAt, formatter));
                article.setUpdatedAt(LocalDate.parse(updatedAt, formatter));
                article.setDeletedAt(LocalDate.parse(delatedAt, formatter));
                article.setStatus(status);
            }
            // 4. Đóng kết nối.
            connection.close();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return article;
    }

    @Override
    public Article save(Article article) {
        try {
            // 1. Mở kết nối đến database.
            Connection connection =
                    DriverManager.getConnection(MYSQL_CONNECTION_STRING,
                            MYSQL_USERNAME,
                            MYSQL_PASSWORD);
            // 2. Tạo câu lệnh prepareStatement
            String prepareSql =
                    "insert into articles "
                            + "(baseUrl,title, description, content, thumbnail, createdAt,updatedAt,deletedAt, status) "
                            + "values "
                            + "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(prepareSql);
            // 3. Thực thi câu lệnh
            preparedStatement.setString(1, article.getBaseUrl());
            preparedStatement.setString(2, article.getTitle());
            preparedStatement.setString(3, article.getDescription());
            preparedStatement.setString(4, article.getContent());
            preparedStatement.setString(5, article.getThumbnail());
            preparedStatement.setString(6, article.getCreatedAt().toString());
            preparedStatement.setString(7, article.getUpdatedAt().toString());
            preparedStatement.setString(8, article.getDeletedAt().toString());
            preparedStatement.setInt(9, article.getStatus());
            preparedStatement.execute();
            System.out.println("Save success!");
            // 4. Đóng kết nối.
            connection.close();
        } catch (SQLException e) {
            System.out.println("Có lỗi xảy ra, vui lòng thử lại sau.");
            e.printStackTrace();
        }
        return article;
    }

    @Override
    public Article update(Article article) {
        try {
            // 1. Mở kết nối đến database.
            Connection connection =
                    DriverManager.getConnection(MYSQL_CONNECTION_STRING,
                            MYSQL_USERNAME,
                            MYSQL_PASSWORD);
            // 2. Tạo câu lệnh prepareStatement
            String prepareSql =
                    "update articles set baseUrl = ?, title = ?, description = ?, content = ?, " +
                            "thumbnail = ?, createdAt = ?,updatedAt = ?,deletedAt = ?, status = ? where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(prepareSql);
            // 3. Thực thi câu lệnh
            preparedStatement.setString(1, article.getBaseUrl());
            preparedStatement.setString(2, article.getTitle());
            preparedStatement.setString(3, article.getDescription());
            preparedStatement.setString(4, article.getContent());
            preparedStatement.setString(5, article.getThumbnail());
            preparedStatement.setString(6, article.getCreatedAt().toString());
            preparedStatement.setString(7, article.getUpdatedAt().toString());
            preparedStatement.setString(8, article.getDeletedAt().toString());
            preparedStatement.setInt(9, article.getStatus());
            preparedStatement.setLong(10, article.getId());
            preparedStatement.execute();
            System.out.println("Updated success!");
            // 4. Đóng kết nối.
            connection.close();
        } catch (SQLException e) {
            System.out.println("Có lỗi xảy ra, vui lòng thử lại sau.");
            e.printStackTrace();
        }
        return article;
    }

    @Override
    public void deleteByUrl(String url) {
        try {
            // 1. Mở kết nối đến database.
            Connection connection =
                    DriverManager.getConnection(MYSQL_CONNECTION_STRING,
                            MYSQL_USERNAME,
                            MYSQL_PASSWORD);
            // 2. Tạo câu lệnh prepareStatement
            String prepareSql =
                    "update articles set status = -1 where baseurl = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(prepareSql);
            // 3. Thực thi câu lệnh
            preparedStatement.setString(1, url);
            preparedStatement.executeUpdate();
            System.out.println("Deleted success!");
            // 4. Đóng kết nối.
            connection.close();
        } catch (SQLException e) {
            System.out.println("Có lỗi xảy ra, vui lòng thử lại sau.");
            e.printStackTrace();
        }

    }
}
