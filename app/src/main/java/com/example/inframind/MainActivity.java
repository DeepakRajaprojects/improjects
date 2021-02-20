package com.example.inframind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView fullName,bp_value,oxy_value,resp_value,glucose_value,heart_value,time_value,cholesterol_value;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId,username;
    Toolbar toolbar;
    Boolean bpdisease,heartdisease,chlodisease;
    RelativeLayout resplayout,glucoselayout,bplayout,heartlayout,chlolayout,oxylayout;

    ArrayList<Integer> stress_ArrayList = new ArrayList<>();
    ArrayList<Integer> pluse_ArrayList = new ArrayList<>();
    ArrayList<Integer> heart_ArrayList = new ArrayList<>();
    ArrayList<Integer> glucose_ArrayList = new ArrayList<>();
    ArrayList<Integer> sugar_ArrayList = new ArrayList<>();
    ArrayList<Integer> time_ArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final GraphView graphView =(GraphView)findViewById(R.id.graphview);
        LineGraphSeries<DataPoint> series=new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(1,8),
                new DataPoint(2,5),
                });
        graphView.addSeries(series);



        String Tpattern = "HH.mm";
        final SimpleDateFormat SDformat = new SimpleDateFormat(Tpattern);
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");






        bp_value= findViewById(R.id.bptxt);
        oxy_value=findViewById(R.id.oxytxt);
        glucose_value=findViewById(R.id.glucosetxt);
        resp_value=findViewById(R.id.resptxt);
        heart_value=findViewById(R.id.hearttxt);
        time_value=findViewById(R.id.timetxt);
        cholesterol_value = findViewById(R.id.cholesteroltxt);

        resplayout = (RelativeLayout) findViewById(R.id.respirationlayout);
        glucoselayout = (RelativeLayout) findViewById(R.id.glucoselayout);
        bplayout = (RelativeLayout) findViewById(R.id.bplayout);
        heartlayout = (RelativeLayout) findViewById(R.id.heartlayout);
        chlolayout = (RelativeLayout) findViewById(R.id.cholesterollayout);
        oxylayout = (RelativeLayout) findViewById(R.id.oxylayout);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getEmail();



        final DocumentReference documentReference = fStore.collection("users").document(userId);
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        Query query = rootRef.collection("users").document(userId).collection("values")
                .orderBy("Time", Query.Direction.DESCENDING)
                .limit(1);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot snapshot: snapshotList){

                    /*if(Integer.parseInt(hv)>=18){
                        resplayout.setBackgroundColor(Color.parseColor("#fa8072"));
                    }
                    else if(Integer.parseInt(hv)>=12 && Integer.parseInt(hv)<=16){
                        resplayout.setBackgroundColor(Color.parseColor("#228b22"));
                    }*/




                    String bv = snapshot.get("Blood pressure").toString();
                    bp_value.setText(bv);

                    if(Integer.parseInt(bv)>=121){
                        bplayout.setBackgroundColor(Color.parseColor("#fa8072"));
                        bpdisease = true;
                    }
                    else if(Integer.parseInt(bv)<=120){
                        bplayout.setBackgroundColor(Color.parseColor("#228b22"));
                    }



                    String hv = snapshot.get("heart").toString();
                    heart_value.setText(hv);


                    if(Integer.parseInt(hv)>=101){
                        heartlayout.setBackgroundColor(Color.parseColor("#fa8072"));
                        heartdisease = true;
                    }
                    else if(Integer.parseInt(hv)>=60 && Integer.parseInt(hv)<=100){
                        heartlayout.setBackgroundColor(Color.parseColor("#228b22"));
                    }

                    String cv = snapshot.get("cholesterol").toString();
                    cholesterol_value.setText(cv);


                    if(Integer.parseInt(cv)>=201){
                        chlolayout.setBackgroundColor(Color.parseColor("#fa8072"));
                        chlodisease = true;
                    }
                    else if(Integer.parseInt(cv)<=200){
                        chlolayout.setBackgroundColor(Color.parseColor("#228b22"));
                    }

                    String gv = snapshot.get("glucose").toString();
                    glucose_value.setText(gv);
                    glucoselayout.setBackgroundColor(Color.parseColor("#228b22"));

                    String rv = snapshot.get("Respiration").toString();
                    resp_value.setText(rv);
                    resplayout.setBackgroundColor(Color.parseColor("#fa8072"));

                    String ov = snapshot.get("Oxygen Saturation").toString();
                    oxy_value.setText(rv);
                    oxylayout.setBackgroundColor(Color.parseColor("#228b22"));









                    time_value.setText("Last Update :"+snapshot.get("Time").toString());

                    /*if(!sugar_value.getText().toString().equals("")){

                        heartlayout.setBackgroundColor(Color.parseColor("#7fff00"));
                    }*/
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("fail","failed");
                    }
                });
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot.exists()) {

                    username = documentSnapshot.getString("Name");

                    toolbar.setTitle(toolbar.getTitle()+" "+username);

                } else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });


        CollectionReference retriveDocument = fStore.collection("users").document(userId).collection("values");
        retriveDocument.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String,Object> obj = new HashMap<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        obj = document.getData();
                        Log.d("TAG", document.getId() + " => " + document.getData());



                        for (Map.Entry<String,Object> entry :obj.entrySet()){
                            Date t;
                            if (entry.getKey().toString().equals("Time")){
                                try {
                                    t = formatter.parse(entry.getValue().toString());
                                    String x = SDformat.format(t);
                                    time_ArrayList.add(Integer.parseInt(x));
                                }
                                catch (Exception e){

                                }

                            }




                            if (entry.getKey().toString().equals("Oxygen Saturation")){
                                stress_ArrayList.add(Integer.parseInt(entry.getValue().toString()));
                            }
                            else if (entry.getKey().toString().equals("Blood pressure")){
                                pluse_ArrayList.add(Integer.parseInt(entry.getValue().toString()));
                            }
                            else if (entry.getKey().toString().equals("heart")){
                                heart_ArrayList.add(Integer.parseInt(entry.getValue().toString()));
                            }
                            else if (entry.getKey().toString().equals("glucose")){
                                glucose_ArrayList.add(Integer.parseInt(entry.getValue().toString()));
                            }
                            else if (entry.getKey().toString().equals("Respiration")){
                                sugar_ArrayList.add(Integer.parseInt(entry.getValue().toString()));
                            }
                        }
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }



            }
        });


        glucoselayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(),description.class);
                startActivity(i);
            }
        });



        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.add){
            Intent in=new Intent(MainActivity.this,valuespage.class);

            startActivity(in);

        }
        else if(id==R.id.logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }

        else if(id==R.id.alert_id){
                Intent in=new Intent(MainActivity.this,description.class);
                in.putExtra("BP",bpdisease);
                in.putExtra("HR",heartdisease);
                in.putExtra("CHLO",chlodisease);
                startActivity(in);
            }

        return true;
    }
    public static int sumAvg(ArrayList<Integer> listobject){
        int l = listobject.size();
        int result = 0;
        for (int i : listobject){
            result += i;
        }
        int avg = result/l;
        return avg;
    }

    static DataPoint[] createdata(ArrayList<Integer> Al,ArrayList<Integer> tl){
        int size = Al.size();
        int po = 0;
        DataPoint[] Dp = new DataPoint[size];
        for (int i=0;i<size-1;i++){
            Dp[po] = new DataPoint(tl.get(i),Al.get(i));
            po++;
            Log.d("for",""+i);
        }
        return  Dp;
    }
}
