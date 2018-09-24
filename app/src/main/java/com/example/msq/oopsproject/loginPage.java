package com.example.msq.oopsproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.text.TextUtils.isEmpty;

public class loginPage extends AppCompatActivity {

    private EditText emailId;
    private EditText password;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    public boolean isValidEmail(CharSequence target) {
        return (!isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void userLogin(){
        String email = emailId.getText().toString().trim();
        String pass = password.getText().toString().trim();

        progressDialog.setMessage("Signing Up, Please wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            finish();
                            Intent intent = new Intent(loginPage.this, homePage.class);
                            loginPage.this.startActivity(intent);
                        }
                        else{
                            Toast.makeText(loginPage.this, "Login Failed, Check credentials and try again !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        TextView signUpPage = findViewById(R.id.signUp);
        Button logIn = findViewById(R.id.log_in);

        emailId = findViewById(R.id.emailId);
        password = findViewById(R.id.password);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            finish();
            Intent intent = new Intent(loginPage.this, homePage.class);
            loginPage.this.startActivity(intent);
        }

        signUpPage.setOnClickListener(
                new TextView.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(loginPage.this, signUpPage.class);
                        loginPage.this.startActivity(intent);
                    }
                }
        );

        logIn.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        if(isValidEmail(emailId.getText().toString().trim()) && !password.getText().toString().trim().isEmpty()) {
                            userLogin();
                        }
                        else if(emailId.getText().toString().trim().isEmpty() && password.getText().toString().trim().isEmpty()){
                            Toast.makeText(loginPage.this, "Enter Your Credentials !", Toast.LENGTH_SHORT).show();
                        }
                        else if(emailId.getText().toString().trim().isEmpty()){
                            Toast.makeText(loginPage.this, "Enter Your email address !", Toast.LENGTH_SHORT).show();
                        }
                        else if(!isValidEmail(emailId.getText().toString().trim())){
                            Toast.makeText(loginPage.this, "Enter a valid email address !", Toast.LENGTH_SHORT).show();
                        }
                        else if(password.getText().toString().trim().isEmpty()){
                            Toast.makeText(loginPage.this, "Enter Your Password !", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

    }
}
