package ml.adityabodhankar.mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private FirebaseAuth auth;
    private TextView loginBtn;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.blue_app));

        emailInput = findViewById(R.id.login_email);
        passwordInput = findViewById(R.id.login_password);

        loginBtn = findViewById(R.id.sign_in_btn);
        TextView registerNavigateBtn = findViewById(R.id.sign_up_redirect);
        TextView forgetPasswordBtn = findViewById(R.id.forget_password_btn);
        progress = findViewById(R.id.sign_in_progress);

        loginBtn.setOnClickListener(view -> loginBtn());
        registerNavigateBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistrationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        forgetPasswordBtn.setOnClickListener(view -> {
        });

//        firebase
        auth = FirebaseAuth.getInstance();


        progress.setVisibility(View.GONE);
        loginBtn.setVisibility(View.VISIBLE);
    }

    private void loginBtn() {
        if (!validateInput()) {
            Toast.makeText(this, "Please verify the details first.", Toast.LENGTH_SHORT).show();
            return;
        }
        progress.setVisibility(View.VISIBLE);
        loginBtn.setVisibility(View.GONE);
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    progress.setVisibility(View.GONE);
                    loginBtn.setVisibility(View.VISIBLE);
                }).addOnFailureListener(e -> {
                    progress.setVisibility(View.GONE);
                    loginBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Error to sign in :- " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private boolean validateInput() {
        if (emailInput.getText().toString().equalsIgnoreCase("")) {
            emailInput.setError("Required field");
            return false;
        }
        if (passwordInput.getText().toString().equalsIgnoreCase("")) {
            passwordInput.setError("Required field");
            return false;
        }
        if (passwordInput.getText().toString().length() < 6) {
            passwordInput.setError("Minimum password length is 6");
            return false;
        }
        return true;
    }
}