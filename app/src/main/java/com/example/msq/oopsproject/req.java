package com.example.msq.oopsproject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import static android.text.TextUtils.isEmpty;

public class req extends AppCompatActivity implements View.OnClickListener{

    private EditText location;
    private EditText phNo;
    private String bloodGrp;
    private EditText quantity;
    private Button makeReq;



    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private boolean requestValid() {

        if (location.getText().toString().trim().isEmpty() || quantity.getText().toString().trim().isEmpty() || phNo.getText().toString().trim().isEmpty()) {

            Toast.makeText(req.this, "Enter all the details !", Toast.LENGTH_SHORT).show();
            return false;

        }
        else if (phNo.getText().toString().trim().length() != 10) {

            Toast.makeText(req.this, "Invalid mobile number !", Toast.LENGTH_SHORT).show();
            return false;

        }
        return true;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_a_request);


        location = findViewById(R.id.Requestlocation);
        phNo = findViewById(R.id.RequestmobileNo);
        quantity = findViewById(R.id.BloodQuantity);

        makeReq = findViewById(R.id.makeReq);


        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        Spinner bloodGrpSpinner = (Spinner) findViewById(R.id.bloodGrpReq);

        ArrayAdapter<CharSequence> bloodGrpAdapter = ArrayAdapter.createFromResource(this, R.array.bloodGrp_Array, android.R.layout.simple_spinner_item);

        bloodGrpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGrpSpinner.setAdapter(bloodGrpAdapter);


        bloodGrpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 0)
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);

                bloodGrp = (String) parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(req.this, "Choose your Blood Grp !", Toast.LENGTH_SHORT).show();
            }
        });


        makeReq.setOnClickListener(this);

    }

    public void onClick(View view){

        if(view.getId() ==  R.id.makeReq)
            if(requestValid()) {
                requestBlood();
            }

    }

    private void requestBlood(){


        // this method will save reques of current user to the database
        progressDialog.setMessage("Requesting, Please wait...");
        progressDialog.show();


        String location1 = location.getText().toString().trim();

        int quantity1= Integer.parseInt((quantity.getText().toString().trim()));
        long phNo1 = Long.parseLong((phNo.getText().toString().trim()));

        reqinfo reqinfo= new reqinfo(location1, quantity1,bloodGrp,phNo1 );

        progressDialog.setMessage("Requesting, Please wait...");
        progressDialog.show();

        FirebaseDatabase.getInstance().getReference("reqinfo")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .setValue(reqinfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(req.this, "Request Successfull!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    finish();
                    Intent intent = new Intent(req.this, homePage.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(req.this, "Failed to Request, Please try again !" , Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

        });

    }

}

