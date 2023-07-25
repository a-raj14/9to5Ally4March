package com.example.a001;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions;
import com.google.firebase.ml.modeldownloader.DownloadType;
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Activity14 extends AppCompatActivity {

    Button goback;
    private NLClassifier textClassifier;
    private ExecutorService executorService;
    private static final String TAG = "TextClassificationDemo";
    private TextView resultTextView;
    //private EditText inputEditText;
    //private ScrollView scrollView;
    private Button back;
    private Button viewReport;
    public String response = "";
    public float negative = 0;
    public float positive = 0;
    public float score = 0;
    public float avg_Score = 0;
    ProgressBar progressBar_cyclic;
    String[] strArray = new String[5];
    String textToShow;
    public int paggu = 0;
    public long employee = 0;
    public long empAverage = 0;
    ImageView animation;
    TextView showscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_14);
        executorService = Executors.newSingleThreadExecutor();
        resultTextView = findViewById(R.id.txt123);
        progressBar_cyclic = findViewById(R.id.progressBar_cyclic);
        viewReport = findViewById(R.id.button7);
        back = findViewById(R.id.button5);
        animation = findViewById(R.id.imageView10);
        showscore = findViewById(R.id.textView15);
        ///////////////loader////////////////////////
        startProgressBar();
        ///////////////download model/////////////////////////////
        downloadModel("sentiment_analysis");
        ///////////////compulsory data download///////////////////
        //predictButton.setVisibility(View.GONE);
        //goback.setVisibility(View.VISIBLE);
        /////Database script start///////////////////
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("UserAnswer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            boolean check = false;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(check!=true)
                {
                    employee = (long) snapshot.child("user").child("Emp_Participate").getValue();
                    employee++;
                    empAverage = (long) snapshot.child("user").child("Avg_Mental_health").getValue();
                    strArray[0] = snapshot.child("user").child("1").getValue().toString();
                    strArray[1] = snapshot.child("user").child("2").getValue().toString();
                    strArray[2] = snapshot.child("user").child("3").getValue().toString();
                    strArray[3] = snapshot.child("user").child("4").getValue().toString();
                    strArray[4] = snapshot.child("user").child("5").getValue().toString();
                    check = true;
                    for (int i = 0; i < strArray.length; i++) {
                        System.out.println("test string array" + strArray[i]);
                        classify(strArray[i]);
                        Log.d("test", String.valueOf(score));
                    }
                    while (paggu <= 6) {
                        //pause
                        if (paggu == 5) {
                            Toast.makeText(
                                            Activity14.this,
                                            "Your Report Ready",
                                            Toast.LENGTH_LONG)
                                    .show();
                            break;
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Activity14.this, "We are Sorry, Server Down", Toast.LENGTH_SHORT).show();
            }
        });

        viewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewReport.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
                back.setEnabled(true);
                avg_Score = avg_Score / 5;
                System.out.println("test average" + avg_Score);
                empAverage = (long)((empAverage*(employee-1)) + avg_Score)/employee;
                animation.setImageResource(R.drawable.pendingforproject);
                showimage(avg_Score);
                showResult(ouputbasedresult(avg_Score, textToShow));
                performAuth();
                stopAllThreads(v);
                executorService.shutdownNow();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("main menu click");
                Intent intent = new Intent(Activity14.this, Activity2.class);
                startActivity(intent);
            }
        });
    }

    private void showimage(float avg_score) {
        animation.setVisibility(View.VISIBLE);
        if(avg_score>=0 && avg_score<=20)
        {
            animation.setImageResource(R.drawable.red);
        }
        else if(avg_score>20 && avg_score<=40)
        {
            animation.setImageResource(R.drawable.orange);
        }
        else if(avg_score>=41 && avg_score<=60)
        {
            animation.setImageResource(R.drawable.yellow);
        }
        else if(avg_score>=61 && avg_score<=80)
        {
            animation.setImageResource(R.drawable.greenlow);
        }
        else if(avg_score>=81 && avg_score<=100)
        {
            animation.setImageResource(R.drawable.green);
        }
      showscore.setText(String.valueOf((int)avg_score) +"%");
    }

    private void classify(final String text) {
        System.out.println("test text check" + text);
        executorService.execute(
                () -> {
                    if (textClassifier != null) {
                        List<Category> results = textClassifier.classify(text);
                        //textToShow = "Input: " + text + "\nOutput:\n";
                        Category result = results.get(1);
                        positive = result.getScore();
                        System.out.println("test result get score" + result.getScore());
                        String str = String.format("%.2f", positive);
                        positive = (Float.parseFloat(str)) * 100;
                        System.out.println("test positive" + positive);
                        score = positive;
                        avg_Score += score;
                        paggu++;
                        if (paggu == 5) {
                            System.out.println("test report ready");
                        }
                    } else {
                        //textClassifier = NLClassifier.createFromFile("default_model.tflite");
                        System.out.println("test report ready is crashing");
                    }


                });
    }

    private String ouputbasedresult(float val, String textToShow) {
        textToShow = "";
        if (val <= 10.0)
            textToShow += "It seems like things are not working out and youre feeling overwhelmed with the current situations. Please contact your in office counsellor to work through your problems, and let your teammates know how they can support you better with the work environment. It is necessary for us to pay attention to our well being while working too!";
        else if (val > 10.0 && val <= 20.0)
            textToShow += "You should acknowledge that things seem hard right now. It is okay to feel this way, and also important to give yourself the required care. Indulge yourself in building positive habits, and rely on your team to work together through this phase!";
        else if (val > 20.0 && val <= 30.0)
            textToShow += "Things seem tough but do not be helpless now. Relay your worries to your seniors, speak up to your teammates about how you can work together better and go with the flow! You will reach a good point with satisfaction at your work place.";
        else if (val > 30.0 && val <= 40.0)
            textToShow += "It sounds like you are somewhat satisfied with your workplace, but there may still be some areas that could be improved. Consider having an open and honest conversation with your supervisor or HR representative to address any concerns you may have.";
        else if (val > 40.0 && val <= 50.0)
            textToShow += "For you, the positives seem well balanced with the negatives at your work place! While this may not be a bad thing, consider how you can grow, or how your workplace can support you better and relay your suggestions, we are always open to hearing new ideas!";
        else if (val > 50.0 && val <= 60.0)
            textToShow += "Your outlook towards the workplace is quite neutral! It might be time to spice things up by  learning some new skills and growing your career, or you can take some time too draft suggestions that will support you better at the work place and let us know!";
        else if (val > 60.0 && val <= 70.0)
            textToShow += "we are glad the positives seem to outweigh the negatives at your work place! To help us improve, let us know what we can do better, and try to focus on thing you like about your work, shape your skillset in the direction of your career goals and remember to take a breather!";
        else if (val > 70.0 && val <= 80.0)
            textToShow += "It's great to hear that you are mostly satisfied with your workplace! However, there is always room for improvement. Consider talking with your supervisor or HR representative about ways to continue growing and developing within your current role.";
        else if (val > 80.0 && val <= 90.0)
            textToShow += "We are happy to see you so satisfied with your environment! You can keep looking for opportunities to get involved in projects or initiatives that align with your interests and career goals. Keep on maintaining a positive attitude and building relationships with your colleagues to foster a supportive and enjoyable work environment.";
        else
            textToShow += "We are glad to see you doing so well at the work place! Let us know what we are doing right/what we could do better by dropping an email to the HR time any time!";
        System.out.println("test text to show ->" + textToShow);
        return textToShow;
    }

    private void showResult(final String textToShow) {
        // Run on UI thread as we'll updating our app UI
        resultTextView.setText(textToShow);
//        runOnUiThread(
//                () -> {
//                    // Append the result to the UI.
//                    resultTextView.append(textToShow);
//                    response = "";
//
//                });
    }

    /**
     * Download model from Firebase ML.
     */
    private synchronized void downloadModel(String modelName) {
        CustomModelDownloadConditions conditions = new CustomModelDownloadConditions.Builder()
                //.requireWifi() //// Allow downloading using any network type
                .build();
        FirebaseModelDownloader.getInstance()
                .getModel("sentiment_analysis", DownloadType.LOCAL_MODEL, conditions)
                .addOnSuccessListener(model -> {
                    try {
                        // TODO 6: Initialize a TextClassifier with the downloaded model
                        textClassifier = NLClassifier.createFromFile(model.getFile());
                        //predictButton.setEnabled(true);
                        Toast.makeText(
                                        Activity14.this,
                                        "Processing Start .........",
                                        Toast.LENGTH_LONG)
                                .show();
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to initialize the model. ", e);
                        Toast.makeText(
                                        Activity14.this,
                                        "Model initialization failed.",
                                        Toast.LENGTH_LONG)
                                .show();
                        //predictButton.setEnabled(false);
                    }
                })
                .addOnFailureListener(e -> {
                            Log.e(TAG, "Failed to download the model. ", e);
                            Toast.makeText(
                                            Activity14.this,
                                            "Model download failed, please check your connection.",
                                            Toast.LENGTH_LONG)
                                    .show();

                        }
                );

    }

    private void startProgressBar() {
        progressBar_cyclic.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar_cyclic.setVisibility(View.GONE);
            }
        }, 7000); // 7 seconds
    }
    public void performAuth()
    {
        FirebaseDatabase firebaseDatabase  = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        databaseReference.child("UserAnswer").child("user").child("Emp_Participate").setValue(employee);
        databaseReference.child("UserAnswer").child("user").child("Avg_Mental_health").setValue(empAverage);

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Activity2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void stopAllThreads(View view) {
        // Get all currently active threads
        Map<Thread, StackTraceElement[]> allThreads = Thread.getAllStackTraces();

        // Interrupt each thread to request that it stop running
        for (Thread thread : allThreads.keySet()) {
            thread.interrupt();
        }
    }
}
