package ml.adityabodhankar.mobileapp.Models;

import java.util.HashMap;
import java.util.Map;

public class UserModel {
    private String uid, name, email, phone, profile, gender;
    private boolean admin;

    public UserModel() {
    }

    public UserModel(Map<String, Object> data) {
        this.uid = (String) data.get("uid");
        this.name = (String) data.get("name");
        this.email = (String) data.get("email");
        this.phone = (String) data.get("phone");
        this.profile = (String) data.get("profile");
        this.gender = (String) data.get("gender");
        this.admin = data.get("admin") != "false";
    }

    public UserModel(String uid, String name, String email, String phone, String profile, String gender) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.profile = profile;
        this.gender = gender;
        this.admin = false;
    }

    public UserModel(String uid, String name, String email, String phone, String profile, String gender, boolean admin) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.profile = profile;
        this.gender = gender;
        this.admin = admin;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("uid", uid);
        data.put("name", name);
        data.put("email", email);
        data.put("phone", phone);
        data.put("gender", gender);
        data.put("profile", profile);
        data.put("admin", admin);
        return data;
    }
}
