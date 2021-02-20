package com.example.inframind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class description extends AppCompatActivity {

    TextView bptxt,chdtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        Intent intent = getIntent();
        Boolean BP = intent.getBooleanExtra("BP",false);
        Boolean HR = intent.getBooleanExtra("HR",false);
        Boolean CHLO = intent.getBooleanExtra("CHLO",false);



        bptxt = findViewById(R.id.bp);
        chdtxt = findViewById(R.id.chd);

        if (BP && HR && CHLO){
            // CHD
            chdtxt.setText("Alert");
        }
        if(BP){
            // HYPERTENSION
            bptxt.setText("sorry");
        }
    }
}