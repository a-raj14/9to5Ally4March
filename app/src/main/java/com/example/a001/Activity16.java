package com.example.a001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Base64;
import java.util.Calendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Activity16 extends AppCompatActivity {

    EditText namee;
    CheckBox isanaymous;
    EditText complainsub;
    EditText complain;
    ImageView writingimage;
    //ImageView stopimage;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Button submitbtn;
    String anaymous = "No";
    String temp = "Yes";
    Calendar calendar = Calendar.getInstance();
    int currentMonth = calendar.get(Calendar.MONTH);
    long count = 0;
    boolean check11 = true;
    boolean check12 = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Activity16 oncreate start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_16);
        namee = (EditText) findViewById(R.id.complainname);
        complainsub = (EditText) findViewById(R.id.subtext);
        complain = (EditText) findViewById(R.id.comlaintext);
        isanaymous = (CheckBox) findViewById(R.id.beanonyms);
        writingimage = (ImageView) findViewById(R.id.writeanim);
        submitbtn = (Button) findViewById(R.id.btnsubmitcomplain);
        //stopimage = (ImageView)findViewById(R.id.stopanim);
        if(check11)
        {
            System.out.println("Activity16 callinf fetch value");
            fetchValue();
        }
        namee.addTextChangedListener(new TextWatcher()
        {
            @Override
            // when there is no text added
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().trim().length() == 0) {
                    //stopimage.setVisibility(View.VISIBLE);
                    // set text to Not typing
                } else {
                    // set text to typing
                    writingimage.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                writingimage.setVisibility(View.VISIBLE);
            }
            // after we input some text
            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.toString().trim().length() == 0)
                {
                    writingimage.setVisibility(View.GONE);
                    // set text to Stopped typing
                    //stopimage.setVisibility(View.VISIBLE);
                }
            }
        });
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(count<2)
                {
                    System.out.println("Activity16 callinf count < 2");
                    performAuth();
                    count++;
                    //saveNewCountData(count);
                }
                else if (count >=2 && !isanaymous.isChecked())
                {
                    System.out.println("Activity16 callelseif count >=2");
                    performAuth();
                }
                else
                {
                    System.out.println("Activity16 callinf fetch value");
                    Toast.makeText(Activity16.this,"You have already used Anonymous service twice a month, so please uncheck the box to proceed...",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void fetchValue() {
        System.out.println("Activity16 fetch value< 2");
        check11 = false;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String email="";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            email = user.getEmail();
        }
        String key = android.util.Base64.encodeToString(email.getBytes(), android.util.Base64.NO_WRAP); //encoder
        //String name = username.getText().toString();
        if(key.isEmpty())
        {
            Toast.makeText(Activity16.this,"Failed to Process...",Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(check12)
            {
                check12 = false;
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Anonymous");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        System.out.println("Activity16 ondatachnage");
                        Object replyValue = snapshot.child(key).child(String.valueOf(currentMonth)).getValue();
                        if (replyValue != null)
                        {
                            // Do something with the non-null value

                            count = Long.parseLong(replyValue.toString());

                        }
                        else
                        {
                            Toast.makeText(Activity16.this,"Data fetch fails",Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Activity16.this,"No Reply Found",Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
    }

    public void Check(View view)
    {
        if(isanaymous.isChecked())
        {
            anaymous = "Yes";
        }
        Toast.makeText(this,"Your Identity Hidden", Toast.LENGTH_LONG).show();
    }
    public void performAuth()
    {
        String username = namee.getText().toString();
        String usersub = complainsub.getText().toString();
        String usercomplain = complain.getText().toString();
        String email = "anaymous@gmail.com";
        if(isanaymous.isChecked())
        {
            username = "Anonymous";
        }

        FirebaseDatabase firebaseDatabase  = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            email = user.getEmail();
        }

        //String encodedEmail = "example@email.com"; // retrieved from the database
        //String key = new String(android.util.Base64.decode(email, android.util.Base64.DEFAULT));
        String key = android.util.Base64.encodeToString(email.getBytes(), android.util.Base64.NO_WRAP); //encoder
        String key1 = new String(android.util.Base64.decode(key, android.util.Base64.DEFAULT)); //decoder
        System.out.println("paggu don " + key);
        System.out.println("paggu don " + key1);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Activity16.this,"Fail to read value",Toast.LENGTH_SHORT).show();
            }
        });
        databaseReference.child("Users").child(key).child("Name").setValue(username);
        databaseReference.child("Users").child(key).child("Status").setValue("Your Complain is Pending !!");
        databaseReference.child("Users").child(key).child("RealName").setValue(namee.getText().toString());
        databaseReference.child("Users").child(key).child("Email").setValue(email);
        databaseReference.child("Users").child(key).child("subject").setValue(complainsub.getText().toString());
        databaseReference.child("Users").child(key).child("Description").setValue(complain.getText().toString());
        databaseReference.child("Admin").child(key).child("Name").setValue(namee.getText().toString());
        databaseReference.child("Admin").child(key).child("Reply").setValue("Your Complain is Pending !!");
        databaseReference.child("Anonymous").child(key).child(String.valueOf(currentMonth)).setValue(count);
        Toast.makeText(Activity16.this,"Your Complain Registered",Toast.LENGTH_SHORT).show();
        sendUserToNextActivity();
    }
    private void sendUserToNextActivity() {
        Intent intent = new Intent(Activity16.this,Activity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}