package model;

/**
 * The Monthly report class is for the reports page.
 */
public class MonthlyReport {

    private String month;
    private String appointmentType;
    private int totalAmount;

    public MonthlyReport(String month, String appointmentType, int totalAmount) {
        this.month = month;
        this.appointmentType = appointmentType;
        this.totalAmount = totalAmount;
    }

    public String getMonth() {
        return month;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public int getTotalAmount() {
        return totalAmount;
    }
}