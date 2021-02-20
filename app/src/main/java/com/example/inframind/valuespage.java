package com.example.inframind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class valuespage extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText heart,pluse,sugar,glocose,stress,cholesterol;


    Button btnre_start;
    FirebaseFirestore fStore;
    String userID;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valuespage);

        heart = findViewById(R.id.heart);
        pluse = findViewById(R.id.pluse);
        sugar = findViewById(R.id.sugar);
        glocose = findViewById(R.id.glucose);
        stress = findViewById(R.id.stress);
        cholesterol = findViewById(R.id.cholesterol);






        btnre_start=(Button) findViewById(R.id.btn_result);

        btnre_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in=new Intent(valuespage.this,MainActivity.class);

                String heartValue = heart.getText().toString().trim();
                String pluseValue = pluse.getText().toString().trim();
                String sugarValue = sugar.getText().toString().trim();
                String glucoseValue = glocose.getText().toString().trim();
                String stressValue = stress.getText().toString().trim();
                String cholesterolValue = cholesterol.getText().toString().trim();

                Log.d(TAG, "onCreate: "+heartValue);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                String format = simpleDateFormat.format(new Date());



                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                fStore = FirebaseFirestore.getInstance();
                userID = fAuth.getCurrentUser().getEmail();
                email = fAuth.getCurrentUser().getEmail();
                DocumentReference documentReference = fStore.collection("users").document(userID);
                DocumentReference subdocumentReference = fStore.collection("users").document(userID).collection("values").document(format);
                DocumentReference documentReference1 = fStore.collection("medical").document();
                if (userID==null){
                    Intent in1=new Intent(valuespage.this,Login.class);


                    startActivity(in1);
                }


                Map<String,Object> user = new HashMap<>();
                user.put("heart",(Object) heartValue);
                user.put("Blood pressure",(Object) pluseValue);
                user.put("Oxygen Saturation",(Object) stressValue);
                user.put("glucose",(Object) glucoseValue);
                user.put("Respiration",(Object) sugarValue);
                user.put("cholesterol",(Object)cholesterolValue);
                user.put("Email",userID);
                user.put("Time",format);
                subdocumentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {


                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });
                Map<String,Object> user1 = new HashMap<>();
                user1.put("heart",(Object) heartValue);
                user1.put("Blood pressure",(Object) pluseValue);
                user1.put("Oxygen Saturation",(Object) stressValue);
                user1.put("glucose",(Object) glucoseValue);
                user1.put("Respiration",(Object) sugarValue);
                user1.put("cholesterol",(Object)cholesterolValue);
                user1.put("Email",userID);
                user1.put("Time",format);

                documentReference1.set(user1).addOnSuccessListener(new OnSuccessListener<Void>() {


                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });

                startActivity(in);
            }
        });
    }
}


