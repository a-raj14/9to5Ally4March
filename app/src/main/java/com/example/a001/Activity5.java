package com.example.a001;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.a001.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Activity5 extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    TextView one;
    TextView two;
    TextView three;
    Button btn2;
    public long employee = 0;
    public long empAverage = 0;
    public long users = 0;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Ayush Output check 2");
        setContentView(R.layout.activity_5);
        System.out.println("Ayush Output check 3");
        one = (TextView) findViewById(R.id.textView14);
        two = (TextView) findViewById(R.id.textView13);
        three = (TextView) findViewById(R.id.textView12);
        btn2 = (Button) findViewById(R.id.next);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("UserAnswer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                users = (long) snapshot.child("user").child("total_user").getValue();
                one.setText(String.valueOf(users));
                employee = (long) snapshot.child("user").child("Emp_Participate").getValue();
                two.setText(String.valueOf(employee));
                empAverage = (long) snapshot.child("user").child("Avg_Mental_health").getValue();
                three.setText(String.valueOf(empAverage)+"%");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Activity5.this, "We are Sorry, Server Down", Toast.LENGTH_SHORT).show();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity5.this, hr_front_page.class);
                startActivity(intent);
            }
        });
    }
}

