package com.example.a001;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

        //import org.tensorflow.lite.Interpreter;

        import java.io.File;

public class FrontPage extends AppCompatActivity implements SelectListener {


    RecyclerView recyclerView;
    DatabaseReference database;
    DatabaseReference statusshow;
    myadaptar myAdapter;
    ArrayList<item> list;
    ProgressDialog progressDialog;
    String DesName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);
        //setContentView(R.layout.item);
        recyclerView = findViewById(R.id.userList);
        database = FirebaseDatabase.getInstance().getReference("Users");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);
        list = new ArrayList<>();
        myAdapter = new myadaptar(this,list,this);
        recyclerView.setAdapter(myAdapter);
        progressDialog.setMessage("Please wait while Fetching Complaint.......");
        progressDialog.setTitle("Complaints");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    item user = dataSnapshot.getValue(item.class);
                    list.add(user);


                }
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClicked(item Item) {

        Intent intent = new Intent(FrontPage.this,Activity18.class);

        String Des = Item.getDescription();
        System.out.println("Realname finding");
        String datafind = Item.getEmail();
        System.out.println("Realname "+datafind);
        intent.putExtra("message_des", Des);
        intent.putExtra("message_name", datafind);
        startActivity(intent);
        Toast.makeText(this,"Opening Details",Toast.LENGTH_SHORT).show();
    }
}


