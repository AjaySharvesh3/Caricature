package com.example.photoblog;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.photoblog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportUserFragment extends Fragment {

    private EditText userName;
    private EditText userReportDetails;
    private Button reportBtn;
    private ProgressBar progressBar;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;

    public ReportUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_user, container, false);
        userName = view.findViewById(R.id.userName);
        userReportDetails = view.findViewById(R.id.reportBug);
        reportBtn = view.findViewById(R.id.reportBugBtn);
        progressBar = view.findViewById(R.id.report_progress);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertReports(v);
            }
        });
        return view;
    }

    private void insertReports(final View view) {
        final String name = userName.getText().toString();
        final String report = userReportDetails.getText().toString();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(report)) {
            progressBar.setVisibility(View.VISIBLE);
            Random random = new Random();
            ReportUser reportUser = new ReportUser(name, report);
            firebaseFirestore.collection("Report Users").document(random.toString()).set(reportUser)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Reported " + userName.getText().toString() + "..!", Toast.LENGTH_SHORT).show();
                                userName.setText("");
                                userReportDetails.setText("");
                                progressBar.setVisibility(View.INVISIBLE);
                            } else {
                                Toast.makeText(getActivity(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
        }
    }

}
