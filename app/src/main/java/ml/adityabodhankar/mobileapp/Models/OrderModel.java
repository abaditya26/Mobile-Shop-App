package ml.adityabodhankar.mobileapp.Models;

import java.util.Map;

public class OrderModel {
    private String orderId, uid, orderTitle, orderImage, orderStatus, name, phone, addressHouseNo, addressStreet, addressLandmark, addressCity, addressPinCode;
    private double orderTotal;
    private String paymentId;
    private boolean isPaid = false;

    public OrderModel(Map<String, Object> data){
        this.orderId = (String) data.get("orderId");
        this.uid = (String) data.get("uid");
        this.orderTitle = (String) data.get("orderTitle");
        this.orderImage = (String) data.get("orderImage");
        this.orderStatus = (String) data.get("orderStatus");
        this.name = (String) data.get("name");
        this.phone = (String) data.get("phone");
        this.addressHouseNo = (String) data.get("addressHouseNo");
        this.addressStreet = (String) data.get("addressStreet");
        this.addressLandmark = (String) data.get("addressLandmark");
        this.addressCity = (String) data.get("addressCity");
        this.addressPinCode = (String) data.get("addressPinCode");
        this.orderTotal = (double) data.get("orderTotal");
        this.isPaid = (boolean) data.get("paid");
        this.paymentId = (String) data.get("paymentId");
    }

    public OrderModel(String orderId, String uid, String orderTitle, String orderImage, String orderStatus, String name, String phone, String addressHouseNo,
                      String addressStreet, String addressLandmark, String addressCity,
                      String addressPinCode, double orderTotal) {
        this.orderId = orderId;
        this.uid = uid;
        this.orderTitle = orderTitle;
        this.orderImage = orderImage;
        this.orderStatus = orderStatus;
        this.name = name;
        this.phone = phone;
        this.addressHouseNo = addressHouseNo;
        this.addressStreet = addressStreet;
        this.addressLandmark = addressLandmark;
        this.addressCity = addressCity;
        this.addressPinCode = addressPinCode;
        this.orderTotal = orderTotal;
        this.isPaid = false;
        this.paymentId = "";
    }

    public OrderModel() {
    }

    public String getOrderImage() {
        return orderImage;
    }

    public void setOrderImage(String orderImage) {
        this.orderImage = orderImage;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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
