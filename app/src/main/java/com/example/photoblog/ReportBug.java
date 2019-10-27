package com.example.photoblog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class ReportBug extends AppCompatActivity {

    public EditText reportDetails;
    private Button reportBtn;
    private ProgressBar progressBar;
    private ImageView backBtn;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_bug);

        reportDetails = findViewById(R.id.reportBug);
        reportBtn = findViewById(R.id.reportBugBtn);
        backBtn = findViewById(R.id.backBtn);
        progressBar = findViewById(R.id.report_bug_progress);

        firebaseFirestore = FirebaseFirestore.getInstance();
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reportData = reportDetails.getText().toString();
                Random random = new Random();
                if(!TextUtils.isEmpty(reportData)) {
                    progressBar.setVisibility(View.VISIBLE);
                    ReportBugDetails reportBugDetails = new ReportBugDetails(reportData);
                    firebaseFirestore.collection("Report Bug").document(random.toString()).set(reportBugDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(ReportBug.this, "Reported Your Bug Details...!", Toast.LENGTH_LONG).show();
                                reportDetails.setText("");

                            } else {
                                Toast.makeText(ReportBug.this, "Error; " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ReportBug.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });
    }

}
