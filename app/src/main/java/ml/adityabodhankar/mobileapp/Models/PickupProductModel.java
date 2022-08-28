package ml.adityabodhankar.mobileapp.Models;

import java.util.Map;

public class PickupProductModel {
    private String productId, productName, productImage, productPrice, productModel, productDescription, productRepair, productSuggestion;

    public PickupProductModel(String productId, String productName, String productImage, String productPrice, String productModel, String productDescription, String productRepair, String productSuggestion) {
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.productModel = productModel;
        this.productDescription = productDescription;
        this.productRepair = productRepair;
        this.productSuggestion = productSuggestion;
    }

    public PickupProductModel(Map<String, Object> data){
        this.productId = (String) data.get("productId");
        this.productName = (String) data.get("productName");
        this.productImage = (String) data.get("productImage");
        this.productPrice = (String) data.get("productPrice");
        this.productModel = (String) data.get("productModel");
        this.productDescription = (String) data.get("productDescription");
        this.productRepair = (String) data.get("productRepair");
        this.productSuggestion = (String) data.get("productSuggestion");
    }

    public PickupProductModel() {
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

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductRepair() {
        return productRepair;
    }

    public void setProductRepair(String productRepair) {
        this.productRepair = productRepair;
    }

    public String getProductSuggestion() {
        return productSuggestion;
    }

    public void setProductSuggestion(String productSuggestion) {
        this.productSuggestion = productSuggestion;
    }
}
