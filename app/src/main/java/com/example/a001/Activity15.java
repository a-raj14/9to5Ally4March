package com.example.a001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity15 extends AppCompatActivity {

    private Button btnfilecomplain;
    private Button btncheckcomplain;
    private ValueEventListener mValueEventListener;
    ProgressBar progressBar_cyclic;
    boolean check = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_15);
        btnfilecomplain = findViewById(R.id.btnfilecomplain);
        btncheckcomplain = findViewById(R.id.btncomplainstatus);
        progressBar_cyclic = findViewById(R.id.progressBar_cyclic);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin");
        btnfilecomplain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //progressBar_cyclic.setVisibility(View.VISIBLE);

                startProgressBar();
                String email="";
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {

                    email = user.getEmail();
                }
                String key = android.util.Base64.encodeToString(email.getBytes(), android.util.Base64.NO_WRAP); //encoder
                //String name = username.getText().toString();
                if(key.isEmpty())
                {
                    Toast.makeText(Activity15.this,"Failed to fetch complain ! PLease try after sometime..",Toast.LENGTH_SHORT).show();
                }
                else
                {
                           mValueEventListener = (new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(check) {
                                    Object replyValue = snapshot.child(key).child("Reply").getValue();
                                    if (replyValue != null) {
                                        // Do something with the non-null value

                                        String response = replyValue.toString();
                                        if (response.equals("Your Complain is Pending !!") && check) {
                                            progressBar_cyclic.setVisibility(View.GONE);
                                            Toast.makeText(Activity15.this, "Your previous complain is pending , please wait till it got resolved)\n", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            check = false;
                                            progressBar_cyclic.setVisibility(View.GONE);
                                            System.out.println("Activity16 from activity 15 count < 2");
                                            Intent intent = new Intent(Activity15.this, Activity16.class);
                                            startActivity(intent);

                                        }
                                    } else {
                                        check = false;
                                        progressBar_cyclic.setVisibility(View.GONE);
                                        System.out.println("Activity16 from activity 15 count < 2");
                                        Intent intent = new Intent(Activity15.this, Activity16.class);
                                        startActivity(intent);

                                    }
                                }
                                databaseReference.removeEventListener(mValueEventListener);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(Activity15.this,"No Reply Found",Toast.LENGTH_SHORT).show();
                            }
                        });
                       databaseReference.addValueEventListener(mValueEventListener);
                }
            }
        });

        btncheckcomplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity15.this,Activity17.class);
                startActivity(intent);
            }
        });

    }
    private void startProgressBar() {
        progressBar_cyclic.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar_cyclic.setVisibility(View.GONE);
            }
        }, 2500); // 2.5 seconds
    }
}