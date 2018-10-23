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

public class registerPage extends AppCompatActivity implements View.OnClickListener{

    private EditText fName;
    private EditText lName;
    private EditText newEmailId;
    private EditText mobile;
    private String gender;
    private String bloodGrp;
    private Button submit;
    private boolean knowCPR = false;

    private CheckBox CPRCheckbox;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;



    private boolean isValidEmail(CharSequence target) {
        return (!isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean credentialsValid() {

        if (fName.getText().toString().trim().isEmpty() || lName.getText().toString().trim().isEmpty() || newEmailId.getText().toString().trim().isEmpty() ||
                mobile.getText().toString().trim().isEmpty()) {

            Toast.makeText(registerPage.this, "Enter all the details !", Toast.LENGTH_SHORT).show();
            return false;

        }
        else if (mobile.getText().toString().trim().length() != 10) {

            Toast.makeText(registerPage.this, "Invalid mobile number !", Toast.LENGTH_SHORT).show();
            return false;

        }
        else if (!isValidEmail(newEmailId.getText().toString().trim())) {

            Toast.makeText(registerPage.this, "Invalid EmailId !", Toast.LENGTH_SHORT).show();
            return false;

        }
        return true;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        final Spinner genderSpinner = findViewById(R.id.gender);
        final Spinner bloodGrpSpinner = findViewById(R.id.bloodGrp);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender_Array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> bloodGrpAdapter = ArrayAdapter.createFromResource(this, R.array.bloodGrp_Array, android.R.layout.simple_spinner_item);

        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);
        bloodGrpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGrpSpinner.setAdapter(bloodGrpAdapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 0)
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(registerPage.this, "Choose your Gender !", Toast.LENGTH_SHORT).show();
            }
        });

        bloodGrpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 0)
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(registerPage.this, "Choose your Blood Grp !", Toast.LENGTH_SHORT).show();
            }
        });

        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        newEmailId = findViewById(R.id.newEmailId);
        mobile = findViewById(R.id.mobileNo);
        gender = genderSpinner.getSelectedItem().toString();
        bloodGrp = bloodGrpSpinner.getSelectedItem().toString();
        CPRCheckbox = findViewById(R.id.CPR_check);
        submit = findViewById(R.id.submitProfile);


        CPRCheckbox.setOnClickListener(this);
        submit.setOnClickListener(this);

    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.CPR_check:
                knowCPR = CPRCheckbox.isChecked();
                break;
            case R.id.submitProfile:
                if(credentialsValid()) {
                    safeProfile();
                }
                break;
        }
    }

    private void safeProfile(){

        final String email = newEmailId.getText().toString().trim();

        progressDialog.setMessage("Registering User, Please wait...");
        progressDialog.show();


        String firstName = fName.getText().toString().trim();
        String lastName = lName.getText().toString().trim();

        long phNo = Long.parseLong((mobile.getText().toString().trim()));

        userData userData = new userData(firstName, lastName, phNo, gender, email, bloodGrp, knowCPR);
        progressDialog.setMessage("Registering User, Please wait...");
        progressDialog.show();

        FirebaseDatabase.getInstance().getReference("userData")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        Toast.makeText(registerPage.this, "Profile Saved Successfully !", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        finish();
                        Intent intent = new Intent(registerPage.this, homePage.class);
                        registerPage.this.startActivity(intent);
                    }
                    else{
                            Toast.makeText(registerPage.this, "Failed to Update Profile, Please try again !" , Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }

            });

    }



}