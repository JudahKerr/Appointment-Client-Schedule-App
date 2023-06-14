package model;

public class Customer {
    private final int id;
    private final String name;
    private final String address;
    private final String postalCode;
    private final String phone;
    private final String createDate;
    private final String createdBy;
    private final String lastUpdate;
    private final String lastUpdatedBy;
    private final int divisionId;
    private final String stateName;

    public Customer(int id, String name, String address, String postalCode, String phone, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy, int divisionId, String stateName) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;
        this.stateName = stateName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public String getStateName() {
        return stateName;
    }
}



