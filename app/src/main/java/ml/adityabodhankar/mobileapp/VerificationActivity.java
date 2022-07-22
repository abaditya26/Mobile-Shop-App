package ml.adityabodhankar.mobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class VerificationActivity extends AppCompatActivity {

    private TextView sendLinkBtn, loginBtn;
    private ProgressBar progress;
    private LinearLayout verificationPending, verificationSent;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        sendLinkBtn = findViewById(R.id.send_link_btn);
        loginBtn = findViewById(R.id.login_btn);
        progress = findViewById(R.id.sign_in_progress);
        verificationPending = findViewById(R.id.verification_pending);
        verificationSent = findViewById(R.id.verification_sent);

        verificationPending.setVisibility(View.VISIBLE);
        verificationSent.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        progress.setVisibility(View.GONE);
        sendLinkBtn.setVisibility(View.VISIBLE);
        loginBtn.setVisibility(View.VISIBLE);

        sendLinkBtn.setOnClickListener(view -> sendLink());
        loginBtn.setOnClickListener(view -> {
            auth.signOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

    }


    private void sendLink(){
        verificationPending.setVisibility(View.VISIBLE);
        verificationSent.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        sendLinkBtn.setVisibility(View.GONE);
        Objects.requireNonNull(auth.getCurrentUser()).sendEmailVerification()
                .addOnSuccessListener(unused -> {
                    progress.setVisibility(View.GONE);
                    sendLinkBtn.setVisibility(View.VISIBLE);
                    verificationPending.setVisibility(View.GONE);
                    verificationSent.setVisibility(View.VISIBLE);
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Error to send verification mail.", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                    sendLinkBtn.setVisibility(View.VISIBLE);
                    verificationPending.setVisibility(View.VISIBLE);
                    verificationSent.setVisibility(View.GONE);
                });
    }
}