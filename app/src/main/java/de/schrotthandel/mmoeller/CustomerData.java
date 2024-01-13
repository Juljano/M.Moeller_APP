package de.schrotthandel.mmoeller;

public class CustomerData {
    private String name;
    private String address;
    private String cityAndPLZ;
    private String date;
    private String taxNumber;
    private Boolean isPrivate;
    private Boolean isBusiness;
    private Boolean isCashPayment;
    private Boolean isBankTransfer;



    private String receiverName;

    private String iBan;

    // Private Konstruktor, um die direkte Instanziierung zu verhindern
    CustomerData() {
    }

    public static CustomerData getInstance() {
        return CustomerDataSingleton.instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityAndPLZ() {
        return cityAndPLZ;
    }

    public void setCityAndPLZ(String cityAndPLZ) {
        this.cityAndPLZ = cityAndPLZ;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Boolean getBusiness() {
        return isBusiness;
    }

    public void setBusiness(Boolean business) {
        isBusiness = business;
    }

    public Boolean getCashPayment() {
        return isCashPayment;
    }

    public Boolean getBankTransfer() {
        return isBankTransfer;
    }

    public void setBankTransfer(Boolean bankTransfer) {
        isBankTransfer = bankTransfer;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public Boolean isPrivate(boolean isPrivate) {
        return this.isPrivate;
    }


    public Boolean isBusiness(boolean isBusiness) {
        return this.isBusiness;
    }


    public Boolean isCashPayment(boolean isCashPayment) {
        return this.isCashPayment;
    }

    public void setCashPayment(Boolean cashPayment) {
        isCashPayment = cashPayment;
    }

    public Boolean isBankTransfer(boolean isBankTransfer) {
        return this.isBankTransfer;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getiBan() {
        return iBan;
    }

    public void setiBan(String iBan) {
        this.iBan = iBan;
    }
}

class CustomerDataSingleton {
    static CustomerData instance = new CustomerData();
}