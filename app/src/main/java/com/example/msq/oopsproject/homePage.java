package com.example.msq.oopsproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class homePage extends AppCompatActivity implements View.OnClickListener {

    private NavigationView navigationView;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference,postRef;
    private String userID;

    private ImageView header_profilePic;
    private TextView header_name, header_email;
    private Button makeRequest;

    private RecyclerView postList;
    postAdapter adapter;
    List<homePagePost> pList;
    //postList is variable of RecylerView Tyoe

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        makeRequest = (Button)findViewById(R.id.makeRequest);
        postRef = FirebaseDatabase.getInstance().getReference("reqinfo");
        makeRequest.setOnClickListener(this);
        postList = (RecyclerView) findViewById(R.id.homepageRecycler);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.Open, R.string.Close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.navigation_view);

        header_profilePic = navigationView.getHeaderView(0).findViewById(R.id.header_profile_pic);
        header_name = navigationView.getHeaderView(0).findViewById(R.id.header_username);
        header_email = navigationView.getHeaderView(0).findViewById(R.id.header_emailid);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseUser = firebaseAuth.getCurrentUser();
        userID = firebaseUser.getUid();

        authStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
            }
        };

        pList = new ArrayList<>();
        postRef = FirebaseDatabase.getInstance().getReference("reqinfo");

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot )
            {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    {
                        homePagePost p = postSnapshot.getValue(homePagePost.class);
                        pList.add(p);
                    }

                    adapter = new postAdapter( homePage.this, pList);
                    postList.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        navigationView = findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_logout){
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(homePage.this, loginPage.class));
                }
                if(item.getItemId() == R.id.nav_account){
                    finish();
                    startActivity(new Intent(homePage.this, registerPage.class));
                }
                return false;
            }
        });



    }


    private void showData(DataSnapshot dataSnapshot) {

        for(DataSnapshot ds : dataSnapshot.getChildren()){

            userData uData = new userData();

            uData.setE_mail(ds.child(userID).getValue(userData.class).getE_mail());
            uData.setfName(ds.child(userID).getValue(userData.class).getfName());
            uData.setlName(ds.child(userID).getValue(userData.class).getlName());

            //Toast.makeText(this, uData.getfName() + " " + uData.getlName(), Toast.LENGTH_SHORT).show(); :- works

            String fullName = uData.getfName() + " " + uData.getlName();

            header_name.setText(fullName);
            header_email.setText(uData.getE_mail());

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view)
    {
        switch(view.getId())
        {
            case(R.id.makeRequest): {
                finish();
                startActivity(new Intent(homePage.this, req.class));
            }
        }


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}