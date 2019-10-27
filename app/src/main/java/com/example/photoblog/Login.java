package com.example.photoblog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button mLoginBtn;
    private Button mRegisterBtn;

    private FirebaseAuth mAuth;
    private ProgressBar mLoginProgress;

    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.reg_email);
        mPassword = findViewById(R.id.reg_confirm_pass);
        mLoginBtn = findViewById(R.id.login_btn);
        mRegisterBtn = findViewById(R.id.login_reg_btn);
        mLoginProgress = findViewById(R.id.login_progress);

        mAuth = FirebaseAuth.getInstance();

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(Login.this, Register.class);
                startActivity(regIntent);
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    mLoginProgress.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sentToMainActivity();
                            } else {
                                String mErrorMessage = task.getException().getMessage();
                                Toast.makeText(Login.this, "Error: " + mErrorMessage, Toast.LENGTH_LONG).show();
                            }
                            mLoginProgress.setVisibility(View.INVISIBLE);
                        }
                    });
                } else {
                    Toast.makeText(Login.this, "Please fill the empty boxes..!", Toast.LENGTH_LONG).show();
                    textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            textToSpeech.speak("Please Fill the empty boxes", TextToSpeech.SUCCESS, null);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentuser != null) {
            sentToMainActivity();
        }
    }

    private void sentToMainActivity() {
        Intent mainIntent = new Intent(Login.this, MainActivity.class);
        startActivity(mainIntent);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.speak("Great..! You have been logged in..! ", TextToSpeech.SUCCESS, null);
            }
        });
        finish();
    }
}
