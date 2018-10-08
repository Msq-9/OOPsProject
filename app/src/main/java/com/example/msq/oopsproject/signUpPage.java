package com.example.msq.oopsproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.text.TextUtils.isEmpty;

public class signUpPage extends AppCompatActivity {

    private TextView email, password, confirmPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Button signUp;
    private boolean TandC = false;

    private CheckBox TermsAndCondCheckbox;

    private boolean isValidEmail(CharSequence target) {
        return (!isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean credentialsValid() {

        if (email.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty() || confirmPassword.getText().toString().trim().isEmpty()) {

            Toast.makeText(signUpPage.this, "Enter all the details !", Toast.LENGTH_SHORT).show();
            return false;

        }
        else if (!isValidEmail(email.getText().toString().trim())) {

            Toast.makeText(signUpPage.this, "Invalid EmailId !", Toast.LENGTH_SHORT).show();
            return false;

        }
        else if (!password.getText().toString().trim().equals(confirmPassword.getText().toString().trim())){

            Toast.makeText(signUpPage.this, "Password didn't match !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!TandC){
            Toast.makeText(signUpPage.this, "Agree to the Terms & Conditions !", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        email = findViewById(R.id.email_id);
        password = findViewById(R.id.signup_pass);
        confirmPassword = findViewById(R.id.signup_Cpass);
        TermsAndCondCheckbox = findViewById(R.id.TermsAndConditions);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        signUp = findViewById(R.id.signUpBtn);

        TermsAndCondCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TandC = TermsAndCondCheckbox.isChecked();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(credentialsValid()) {

                    String emailID = email.getText().toString().trim();
                    String pass = password.getText().toString().trim();

                    progressDialog.setMessage("Signing Up, Please wait...");
                    progressDialog.show();

                    firebaseAuth.createUserWithEmailAndPassword(emailID, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(signUpPage.this, "Sign Up Successful, Enter your details !", Toast.LENGTH_SHORT).show();

                                        finish();
                                        Intent intent = new Intent(signUpPage.this, registerPage.class);
                                        signUpPage.this.startActivity(intent);

                                    } else {
                                        Toast.makeText(signUpPage.this, "Sign Up failed, try Again !", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });

    }
}
