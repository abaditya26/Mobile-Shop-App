package ml.adityabodhankar.mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OrderPlacedActivity extends AppCompatActivity {

    private TextView cNameView, totalView;
    private Button okBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);


        Intent intent = getIntent();
        if (intent.getStringExtra("id") == null) {
            Toast.makeText(this, "Invalid navigation to screen", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String id = intent.getStringExtra("id");
        String total = intent.getStringExtra("total");
        String name = intent.getStringExtra("c_name");
        System.out.println(id);

        cNameView = findViewById(R.id.customer_name);
        totalView = findViewById(R.id.order_total);
        okBtn = findViewById(R.id.ok_btn);

        cNameView.setText(name);
        totalView.setText(total);

        okBtn.setOnClickListener(view -> {
            Intent i = new Intent(this, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        });
    }
}