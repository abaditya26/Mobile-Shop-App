package ml.adityabodhankar.mobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {


    private EditText nameInput, emailInput, passwordInput, cPasswordInput, phoneInput;
    private String gender = "";
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private ProgressBar progress;
    private TextView registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.blue_app));

        registerBtn = findViewById(R.id.create_user_btn);
        TextView signInRedirect = findViewById(R.id.sign_in_redirect);

        nameInput = findViewById(R.id.register_name);
        emailInput = findViewById(R.id.register_email);
        passwordInput = findViewById(R.id.register_password);
        cPasswordInput = findViewById(R.id.register_c_password);
        phoneInput =findViewById(R.id.register_phone);
        RadioButton maleBtn = findViewById(R.id.register_male_btn);
        RadioButton femaleBtn = findViewById(R.id.register_female_btn);
        progress = findViewById(R.id.create_user_progress);

        maleBtn.setOnClickListener(view -> gender = "male");
        femaleBtn.setOnClickListener(view -> gender = "female");

        registerBtn.setOnClickListener(view -> registerUser());
        signInRedirect.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

//        firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        progress.setVisibility(View.GONE);
        registerBtn.setVisibility(View.VISIBLE);
    }

    private void registerUser() {
        if(!validateData()){
            return;
        }
        progress.setVisibility(View.VISIBLE);
        registerBtn.setVisibility(View.GONE);
        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String gender = this.gender;
        String password = passwordInput.getText().toString();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                    Map<String, Object> data = new HashMap<>();
                    data.put("uid", uid);
                    data.put("name", name);
                    data.put("email", email);
                    data.put("phone", phone);
                    data.put("gender", gender);
                    data.put("profile", "default");
                    db.collection("users").document(uid).set(data)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                progress.setVisibility(View.GONE);
                                registerBtn.setVisibility(View.VISIBLE);
                            }).addOnFailureListener(e -> {
                                progress.setVisibility(View.GONE);
                                registerBtn.setVisibility(View.VISIBLE);
                                Toast.makeText(this, "Unable to create user :- "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }).addOnFailureListener(e -> {
                    progress.setVisibility(View.GONE);
                    registerBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Error! Unable to create user. :- "+e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private boolean validateData() {
        if(emailInput.getText().toString().equalsIgnoreCase("")){
            emailInput.setError("Required field");
            return false;
        }
        if (nameInput.getText().toString().equalsIgnoreCase("")){
            nameInput.setError("Required Field");
            return false;
        }
        if (phoneInput.getText().toString().equalsIgnoreCase("")){
            phoneInput.setError("Required Field");
            return false;
        }
        if (gender.equalsIgnoreCase("")){
            Toast.makeText(this, "Please Select gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cPasswordInput.getText().toString().equalsIgnoreCase("")){
            cPasswordInput.setError("Required Field");
            return false;
        }
        if (passwordInput.getText().toString().equalsIgnoreCase("")){
            passwordInput.setError("Required field");
            return false;
        }
        if (passwordInput.getText().toString().length() < 6){
            passwordInput.setError("Minimum password length is 6");
            return false;
        }
        if (cPasswordInput.getText().toString().length() < 6){
            cPasswordInput.setError("Minimum password length is 6");
            return false;
        }
        if(!passwordInput.getText().toString().equals(cPasswordInput.getText().toString())){
            passwordInput.setError("Both passwords not match");
            cPasswordInput.setError("Both passwords not match");
            return false;
        }
        return true;
    }

}