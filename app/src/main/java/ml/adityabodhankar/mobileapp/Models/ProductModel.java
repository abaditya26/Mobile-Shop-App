package ml.adityabodhankar.mobileapp.Models;

import java.util.Map;

public class ProductModel {
    private String id, name, image, price, description, category;
    private boolean pickup;

    public ProductModel() {
    }

    public ProductModel(String id, String name, String image, String price, String description, String category) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.description = description;
        this.category = category;
        this.pickup = false;
    }

    public ProductModel(String id, String name, String image, String price, String description, String category, boolean pickup) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.description = description;
        this.category = category;
        this.pickup = pickup;
    }

    public ProductModel(Map<String, Object> data) {
        this.id = (String) data.get("id");
        this.name = (String) data.get("name");
        this.image = (String) data.get("image");
        this.price = (String) data.get("price");
        this.description = (String) data.get("description");
        this.category = (String) data.get("category");
        if (data.containsKey("pickup")) {
            try {
                this.pickup = (boolean) data.get("pickup");
            } catch (Exception ignored) {
                this.pickup = false;
            }
        } else {
            this.pickup = false;
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPickup() {
        return pickup;
    }

    public void setPickup(boolean pickup) {
        this.pickup = pickup;
    }
}
