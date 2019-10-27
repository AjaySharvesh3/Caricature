package com.example.photoblog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private FloatingActionButton addPostBtn;
    private BottomNavigationView bottomNavigationView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private String currentUserID;

    private TextToSpeech textToSpeech;

    private HomeFragment homeFragment;
    private NotificationFragment notificationFragment;
    private ReportUserFragment reportUserFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Caricature");

        addPostBtn = findViewById(R.id.add_post_btn);
        bottomNavigationView = findViewById(R.id.mainBottomNav);

        homeFragment = new HomeFragment();
        notificationFragment = new NotificationFragment();
        reportUserFragment = new ReportUserFragment();

        replaceFragment(homeFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.bottom_action_home : replaceFragment(homeFragment); return true;
                    /*case R.id.bottom_action_notification: replaceFragment(notificationFragment); return true;*/
                    case R.id.bottom_action_report_user: replaceFragment(reportUserFragment);
                    default: return true;
                }
            }
        });

        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newPostIntent = new Intent(MainActivity.this, NewPostActivity.class);
                startActivity(newPostIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser ==  null) {
            sentToLogin();
        } else {
            currentUserID = mAuth.getCurrentUser().getUid();
            firebaseFirestore.collection("Users").document(currentUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (!task.getResult().exists()) {
                            Intent setUpIntent = new Intent(MainActivity.this, SetUpActivity.class);
                            startActivity(setUpIntent);
                            finish();
                        }
                    } else {
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu item) {
        getMenuInflater().inflate(R.menu.main_menu, item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout_button: 
                logout();
                return true;
            case R.id.action_settings_button:
                Intent settingsIntent = new Intent(MainActivity.this, SetUpActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_report_btn:
                Intent reportBugsIntent = new Intent(MainActivity.this, ReportBug.class);
                startActivity(reportBugsIntent);
                return true;
            case R.id.action_about_button:
                Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
                return true;
             default:
                 return true;
        }
    }

    private void logout() {
        mAuth.signOut();
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.speak("We can't wait to see you again..!", TextToSpeech.SUCCESS, null);
            }
        });
        sentToLogin();
    }

    private void sentToLogin() {
        Intent loginIntent  = new Intent(MainActivity.this, Login.class);
        startActivity(loginIntent);
        finish();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }

}
