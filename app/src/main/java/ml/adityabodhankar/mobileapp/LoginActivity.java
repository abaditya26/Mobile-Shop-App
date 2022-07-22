package ml.adityabodhankar.mobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.login_email);
        passwordInput = findViewById(R.id.login_password);

        TextView loginBtn = findViewById(R.id.sign_in_btn);
        TextView registerNavigateBtn = findViewById(R.id.sign_up_redirect);
        TextView forgetPasswordBtn = findViewById(R.id.forget_password_btn);

        loginBtn.setOnClickListener(view -> loginBtn());
        registerNavigateBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistrationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        forgetPasswordBtn.setOnClickListener(view -> {});

        auth = FirebaseAuth.getInstance();

    }

    private void loginBtn() {
        if(!validateInput()){
            Toast.makeText(this, "Please verify the details first.", Toast.LENGTH_SHORT).show();
            return;
        }
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Error to sign in :- "+e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private boolean validateInput() {
        if(emailInput.getText().toString().equalsIgnoreCase("")){
            emailInput.setError("Required field");
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
        return true;
    }
}