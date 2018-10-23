package com.example.msq.oopsproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;




import org.w3c.dom.Text;

public class profilePage extends AppCompatActivity {

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_profile_page);
        Intent newIntent = getIntent();
        String name = newIntent.getStringExtra("name");
        String e_mail = newIntent.getStringExtra("email");
        String mob_num = newIntent.getStringExtra("mobile");
        TextView name1 = findViewById(R.id.Name);
        name1.setText(name);
        TextView e_mail1 = findViewById(R.id.Email_id);
        e_mail1.setText(e_mail);
        TextView mob_num1 = findViewById(R.id.mobile);
        mob_num1.setText(mob_num);

    }
}
