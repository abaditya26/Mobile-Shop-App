package ml.adityabodhankar.mobileapp.Models;

public class TrackingModel {
    private String orderId, status, description, dateTime;

    public TrackingModel() {
    }

    public TrackingModel(String orderId, String status, String description, String dateTime) {
        this.orderId = orderId;
        this.status = status;
        this.description = description;
        this.dateTime = dateTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
