package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The Appointment Query class is where the Select, Update, Insert, and Delete methods take place with Appointments and the Database.
 */
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

    public static List<Appointment> appointmentsByContactID(String contactID) {
        String sql = "SELECT * FROM APPOINTMENTS WHERE Contact_ID = ?";
        List<Appointment> appointments = new ArrayList<>();

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            // Set the parameter before executing the query
            ps.setString(1, contactID);

            // Now execute the query
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

    public static List<Appointment> appointmentsByUserID(String userID) {
        String sql = "SELECT * FROM APPOINTMENTS WHERE User_ID = ?";
        List<Appointment> appointments = new ArrayList<>();

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            // Set the parameter before executing the query
            ps.setString(1, userID);

            // Now execute the query
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


    public static List<Appointment> selectAppointmentsForCurrentWeek() {
        String sql = "SELECT * FROM APPOINTMENTS WHERE EXTRACT(WEEK FROM start) = EXTRACT(WEEK FROM CURRENT_DATE) AND EXTRACT(YEAR FROM start) = EXTRACT(YEAR FROM CURRENT_DATE)";
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

    public static List<Appointment> selectAppointmentsForCurrentMonth() {
        String sql = "SELECT * FROM APPOINTMENTS WHERE EXTRACT(MONTH FROM start) = EXTRACT(MONTH FROM CURRENT_DATE) AND EXTRACT(YEAR FROM start) = EXTRACT(YEAR FROM CURRENT_DATE)";
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
                "WHERE Appointment_id = ?";

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

    public static List<Appointment> getAppointmentsForCustomer(int customerId) {
        String sql = "SELECT * FROM APPOINTMENTS WHERE customer_id = ?";
        List<Appointment> appointments = new ArrayList<>();

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, customerId);
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

    public static int delete(int id) {
        String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_id = ?";

        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void deleteAppointmentsByCustomerID(int customerID) {
        String sql = "DELETE FROM appointments WHERE customer_id = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

