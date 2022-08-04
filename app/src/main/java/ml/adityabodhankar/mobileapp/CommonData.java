package ml.adityabodhankar.mobileapp;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import ml.adityabodhankar.mobileapp.Models.ProductModel;

public class CommonData {

    public static ArrayList<ProductModel> products;

    public static void signOut(Context context) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
