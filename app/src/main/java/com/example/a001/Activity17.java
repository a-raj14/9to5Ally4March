package com.example.a001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Activity17 extends AppCompatActivity {

    TextView status;
    EditText desc;
    Button Refresh;
    Button back;
    String description;
    String response;
    String check;
    ImageView animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_17);
        status = findViewById(R.id.textView);
        desc = findViewById(R.id.desc);
        Refresh = findViewById(R.id.button3);
        back = findViewById(R.id.button2);
        animation = findViewById(R.id.imageView9);
        Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = "";
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {

                    email = user.getEmail();
                }
                String key = android.util.Base64.encodeToString(email.getBytes(), android.util.Base64.NO_WRAP); //encoder
                //String name = username.getText().toString();
                if(key.isEmpty())
                {
                    Toast.makeText(Activity17.this,"Failed to fetch complain ! PLease try after sometime..",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin");
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Users");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            response = snapshot.child(key).child("Reply").getValue().toString();
                            if(response.equals("Your Complain is Pending !!"))
                            {
                                animation.setImageResource(R.drawable.pendingforproject);
                            }
                            else
                            {
                                response = "Reply from Admin :\n"+response;
                                animation.setImageResource(R.drawable.checkforproject);
                            }
                            status.setText(response);
                            Toast.makeText(Activity17.this,"Your Reply Received",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Activity17.this,"No Reply Found",Toast.LENGTH_SHORT).show();


                        }
                    });
                    databaseReference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            description = snapshot.child(key).child("Description").getValue().toString();
                            desc.setText(description);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity17.this,Activity2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}