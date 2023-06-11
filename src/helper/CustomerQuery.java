package helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

public abstract class CustomerQuery {

    public static void select() {
        String sql = "SELECT * FROM CUSTOMERS";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            // rs.next() returns true while there is data, it goes line by line
            while (rs.next()) {
                int ID = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                String createDate = String.valueOf(rs.getDate("Create_Date"));  // Wrapping these in String Value Of
                String createdBy = rs.getString("Created_By");
                String lastUpdate = String.valueOf(rs.getTimestamp("Last_Update")); // Wrapping these in String Value Of
                String lastUpdateBy = rs.getString("Last_Updated_By");
                int division = rs.getInt("Division_ID");
                System.out.println(ID + " | " + name + " | " + address + " | " + postalCode + " | " + phone + " | " + createDate + " | " + createdBy + " | " + lastUpdate + " | " + lastUpdateBy + " | " + division);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    // INSERT STATEMENT
    public static int insert(String customerName, String address, String postalCode, String phone, String createdBy, String updatedBy, int divisionID) {
        Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());

        String sql = "INSERT INTO CUSTOMERS (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)"; // Fixed the column names

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            // Set the values for each column you want to update
            ps.setString(1, customerName);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setTimestamp(5, currentTimestamp);
            ps.setString(6, createdBy);
            ps.setTimestamp(7, currentTimestamp);
            ps.setString(8, updatedBy);
            ps.setInt(9, divisionID);

            return ps.executeUpdate();
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace();
            return 0;
        }
    }

    //  UPDATE STATEMENT
    public static int update(String customerName, String address, String postalCode, String phone, String createdBy, String updatedBy, int divisionID, int customerID) {
        Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
        String sql = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            // Set the values for each column you want to update
            ps.setString(1, customerName);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setTimestamp(5, currentTimestamp);
            ps.setString(6, createdBy);
            ps.setTimestamp(7, currentTimestamp);
            ps.setString(8, updatedBy);
            ps.setInt(9, divisionID);
            ps.setInt(10, customerID);
            // Execute the update and return the number of affected rows
            return ps.executeUpdate();
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace();
            return 0;
        }
    }

    //  DELETE STATEMENT
    public static int delete(int customerID) {
        String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
