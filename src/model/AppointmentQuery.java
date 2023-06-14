package model;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AppointmentQuery {

    public static List<Appointment> select() {
        String sql = "SELECT * FROM APPOINTMENTS";
        List<Appointment> appointments = new ArrayList<>();

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("appointment_id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String location = rs.getString("location");
                String type = rs.getString("type");
                LocalDateTime start = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("end").toLocalDateTime();
                LocalDateTime createDate = rs.getTimestamp("create_date").toLocalDateTime();
                String createdBy = rs.getString("created_by");
                LocalDateTime lastUpdate = rs.getTimestamp("last_update").toLocalDateTime();
                String lastUpdatedBy = rs.getString("last_updated_by");
                int customerId = rs.getInt("customer_id");
                int userId = rs.getInt("user_id");
                int contactId = rs.getInt("contact_id");

                appointments.add(new Appointment(id, title, description, location, type, start, end,
                        createDate, createdBy, lastUpdate, lastUpdatedBy, customerId, userId, contactId));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    public static int insert(String title, String description, String location, String type, LocalDateTime start, LocalDateTime end,
                             String createdBy, String lastUpdatedBy, int customerId, int userId, int contactId) {

        String sql = "INSERT INTO APPOINTMENTS (title, description, location, type, start, end, create_date, created_by, last_update, last_updated_by, customer_id, user_id, contact_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, Timestamp.valueOf(start));
            ps.setTimestamp(6, Timestamp.valueOf(end));
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(8, createdBy);
            ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(10, lastUpdatedBy);
            ps.setInt(11, customerId);
            ps.setInt(12, userId);
            ps.setInt(13, contactId);

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int update(int id, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end,
                             String lastUpdatedBy, int customerId, int userId, int contactId) {

        String sql = "UPDATE APPOINTMENTS SET title = ?, description = ?, location = ?, type = ?, start = ?, end = ?, last_update = ?, last_updated_by = ?, customer_id = ?, user_id = ?, contact_id = ? " +
                "WHERE id = ?";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, Timestamp.valueOf(start));
            ps.setTimestamp(6, Timestamp.valueOf(end));
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(8, lastUpdatedBy);
            ps.setInt(9, customerId);
            ps.setInt(10, userId);
            ps.setInt(11, contactId);
            ps.setInt(12, id);

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int delete(int id) {
        String sql = "DELETE FROM APPOINTMENTS WHERE id = ?";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}

