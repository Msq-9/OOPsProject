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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import static android.text.TextUtils.isEmpty;

public class signUpPage extends AppCompatActivity implements View.OnClickListener{

    private EditText fName;
    private EditText lName;
    private EditText newEmailId;
    private EditText mobile;
    private EditText pass;
    private EditText newPass;
    private String gender;
    private String bloodGrp;

    private boolean knowCPR = false;
    private boolean TandC = false;

    private CheckBox CPRCheckbox;
    private CheckBox TermsAndCondCheckbox;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private boolean isValidEmail(CharSequence target) {
        return (!isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean credentialsValid() {

        if (fName.getText().toString().trim().isEmpty() || lName.getText().toString().trim().isEmpty() || newEmailId.getText().toString().trim().isEmpty() ||
                mobile.getText().toString().trim().isEmpty() || pass.getText().toString().trim().isEmpty() || newPass.getText().toString().trim().isEmpty()) {

            Toast.makeText(signUpPage.this, "Enter all the details !", Toast.LENGTH_SHORT).show();
            return false;

        }
        else if (mobile.getText().toString().trim().length() != 10) {

            Toast.makeText(signUpPage.this, "Invalid mobile number !", Toast.LENGTH_SHORT).show();
            return false;

        }
        else if (!isValidEmail(newEmailId.getText().toString().trim())) {

            Toast.makeText(signUpPage.this, "Invalid EmailId !", Toast.LENGTH_SHORT).show();
            return false;

        }
        else if (!pass.getText().toString().trim().equals(newPass.getText().toString().trim())){

            Toast.makeText(signUpPage.this, "Password didn't match !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!TandC){
            Toast.makeText(signUpPage.this, "Agree to the Terms & Conditions !", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

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
                Toast.makeText(signUpPage.this, "Choose your Gender !", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(signUpPage.this, "Choose your Blood Grp !", Toast.LENGTH_SHORT).show();
            }
        });

        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        newEmailId = findViewById(R.id.newEmailId);
        mobile = findViewById(R.id.mobileNo);
        pass = findViewById(R.id.newPass);
        newPass = findViewById(R.id.newPassConfirm);
        gender = genderSpinner.getSelectedItem().toString();
        bloodGrp = bloodGrpSpinner.getSelectedItem().toString();
        CPRCheckbox = findViewById(R.id.CPR_check);
        TermsAndCondCheckbox = findViewById(R.id.TermsAndConditions);
        Button signUp = findViewById(R.id.signUpButton);


        CPRCheckbox.setOnClickListener(this);
        TermsAndCondCheckbox.setOnClickListener(this);
        signUp.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(firebaseAuth.getCurrentUser() != null){
            finish();
            Intent intent = new Intent(signUpPage.this, loginPage.class);
            signUpPage.this.startActivity(intent);
        }
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.CPR_check:
                knowCPR = CPRCheckbox.isChecked();
                break;
            case R.id.TermsAndConditions:
                TandC = TermsAndCondCheckbox.isChecked();
                break;
            case R.id.signUpButton:
                if(credentialsValid()) {
                    registerUser();
                }
                break;
        }
    }

    private void registerUser(){

        String password = pass.getText().toString().trim();
        String email = newEmailId.getText().toString().trim();

        progressDialog.setMessage("Registering User, Please wait...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();

                            String firstName = fName.getText().toString().trim();
                            String lastName = lName.getText().toString().trim();
                            long phNo = Integer.parseInt(mobile.getText().toString().trim());

                            userData userData = new userData(firstName, lastName, phNo, gender, bloodGrp, knowCPR);

                            FirebaseDatabase.getInstance().getReference("userData")
                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                    .setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                        Toast.makeText(signUpPage.this, "Registration Successful !", Toast.LENGTH_SHORT).show();
                                    else{
                                        progressDialog.dismiss();
                                        Toast.makeText(signUpPage.this, "Registration Failed, Please try again !" , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            finish();
                            Intent intent = new Intent(signUpPage.this, homePage.class);
                            signUpPage.this.startActivity(intent);
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(signUpPage.this, "Registration Failed, Please try again !" , Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
