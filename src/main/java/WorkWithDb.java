import java.io.IOException;
import java.sql.*;

public class WorkWithDb {
    public static void main(String[] args) throws SQLException {

    }
    static void insertInDB() throws SQLException {
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/Quizlet?characterEncoding=utf8", "root", "zxcvasdf");
        final Statement stmt = connection.createStatement();
        for(int i = 0; i < Quizlet.getKeys().size(); i++){
            String word = Quizlet.getKeys().get(i);
            String translate = Quizlet.getWords().get(word);

            stmt.executeUpdate("INSERT INTO words (Word, Translate ) values ('"+word+"','"+translate+"')");
        }
        connection.close();
    }

    static void InsertWordsOfDbInMap() throws SQLException, IOException {
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/Quizlet?characterEncoding=utf8", "root", "zxcvasdf");
        final Statement stmt = connection.createStatement();
        final String query = "SELECT * FROM words";
        final ResultSet rs = stmt.executeQuery(query);
        while(rs.next()){
            String wordAndTranslate = rs.getString(1) + " - " + rs.getString(2);
            Quizlet.createWordsList(wordAndTranslate);
        }
    }
    static void deleteWord(String word) throws SQLException {
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/Quizlet?characterEncoding=utf8", "root", "zxcvasdf");
        final Statement stmt = connection.createStatement();
        String[] delete = word.split(" ");
        stmt.executeUpdate("DELETE FROM WORDS WHERE Word = '" + delete[1] + "'");
    }
}




