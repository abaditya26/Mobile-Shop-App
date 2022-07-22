package ml.adityabodhankar.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class RegistrationActivity extends AppCompatActivity {


    private TextView registerBtn, signInRedirect;
    private EditText nameInput, emailInput, passwordInput, cPasswordInput, phoneInput;
    private String gender = "";
    private RadioButton maleBtn, femaleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registerBtn = findViewById(R.id.create_user_btn);
        signInRedirect = findViewById(R.id.sign_in_redirect);

        nameInput = findViewById(R.id.register_name);
        emailInput = findViewById(R.id.register_email);
        passwordInput = findViewById(R.id.register_password);
        cPasswordInput = findViewById(R.id.register_c_password);
        phoneInput =findViewById(R.id.register_phone);
        maleBtn = findViewById(R.id.register_male_btn);
        femaleBtn = findViewById(R.id.register_female_btn);

        maleBtn.setOnClickListener(view -> gender = "male");
        femaleBtn.setOnClickListener(view -> gender = "female");
    }
}