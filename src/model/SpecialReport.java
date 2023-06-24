package model;

public class SpecialReport {
    private String state;
    private int total;

    public SpecialReport(String state, int total) {
        this.state = state;
        this.total = total;

    }

    public String getName() {
        return state;
    }

    public int getTotal() {
        return total;
    }
}
