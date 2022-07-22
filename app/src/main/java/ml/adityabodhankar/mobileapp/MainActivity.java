package ml.adityabodhankar.mobileapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //check if user is logged in or not
        if(auth.getCurrentUser() != null){
            //has current user
            //open dashboard
            db.collection("users").document(auth.getCurrentUser().getUid())
                    .addSnapshotListener((documentSnapshot, e) -> {
                if(documentSnapshot == null){
                    Toast.makeText(MainActivity.this, "User Not Exists", Toast.LENGTH_SHORT).show();
                }else{
                    if (documentSnapshot.exists()){
                        Toast.makeText(MainActivity.this, "User Exists", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "User Not Exists", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            //no user data
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}