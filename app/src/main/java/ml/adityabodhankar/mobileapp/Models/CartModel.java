package ml.adityabodhankar.mobileapp.Models;

import java.util.Map;

public class CartModel {
    private String productId, productName, productImage, productPrice;
    private int quantity;

    public CartModel() {
    }

    public CartModel(Map<String, Object> data){
        this.productId = (String) data.get("productId");
        this.productName = (String) data.get("productName");
        this.productImage = (String) data.get("productImage");
        this.productPrice = (String) data.get("productPrice");
        this.quantity = (int) ((long)data.get("quantity"));
    }

    public CartModel(String productId, String productName, String productImage, String productPrice, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
