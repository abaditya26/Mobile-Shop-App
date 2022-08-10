package ml.adityabodhankar.mobileapp.Models;

public class OrderModel {
    private String orderId, uid, name, phone, addressHouseNo, addressStreet, addressLandmark, addressCity, addressPinCode;
    private double orderTotal;
    private String paymentId;
    private boolean isPaid = false;

    public OrderModel(String orderId, String uid, String name, String phone, String addressHouseNo,
                      String addressStreet, String addressLandmark, String addressCity,
                      String addressPinCode, double orderTotal) {
        this.orderId = orderId;
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.addressHouseNo = addressHouseNo;
        this.addressStreet = addressStreet;
        this.addressLandmark = addressLandmark;
        this.addressCity = addressCity;
        this.addressPinCode = addressPinCode;
        this.orderTotal = orderTotal;
        this.isPaid = false;
        paymentId = "";
    }

    public OrderModel() {
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressHouseNo() {
        return addressHouseNo;
    }

    public void setAddressHouseNo(String addressHouseNo) {
        this.addressHouseNo = addressHouseNo;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getAddressLandmark() {
        return addressLandmark;
    }

    public void setAddressLandmark(String addressLandmark) {
        this.addressLandmark = addressLandmark;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressPinCode() {
        return addressPinCode;
    }

    public void setAddressPinCode(String addressPinCode) {
        this.addressPinCode = addressPinCode;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
