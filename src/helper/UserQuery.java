package helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserQuery {

    // SELECT STATEMENT
    public static List<User> select() {
        String sql = "SELECT * FROM USERS";
        List<User> users = new ArrayList<>();

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int userID = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                String createDate = rs.getString("Create_Date");
                String createdBy = rs.getString("Created_By");
                String lastUpdate = rs.getString("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");

                users.add(new User(userID, userName, password, createDate, createdBy, lastUpdate, lastUpdatedBy));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    // INSERT STATEMENT
    public static int insert(String userName, String password, String createdBy, String lastUpdatedBy) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        String sql = "INSERT INTO USERS (User_Name, Password, Create_Date, Created_By, Last_Update, Last_Updated_By) VALUES(?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setString(1, userName);
            ps.setString(2, password);
            ps.setTimestamp(3, currentTimestamp);
            ps.setString(4, createdBy);
            ps.setTimestamp(5, currentTimestamp);
            ps.setString(6, lastUpdatedBy);

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // UPDATE STATEMENT
    public static int update(int userID, String userName, String password, String lastUpdatedBy) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        String sql = "UPDATE USERS SET User_Name = ?, Password = ?, Last_Update = ?, Last_Updated_By = ? WHERE User_ID = ?";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setString(1, userName);
            ps.setString(2, password);
            ps.setTimestamp(3, currentTimestamp);
            ps.setString(4, lastUpdatedBy);
            ps.setInt(5, userID);

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // DELETE STATEMENT
    public static int delete(int userID) {
        String sql = "DELETE FROM USERS WHERE User_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, userID);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}

