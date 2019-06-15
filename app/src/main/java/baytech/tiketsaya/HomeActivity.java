package baytech.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.shapeofview.shapes.CircleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    private long backpressedTime;
    private Toast backToast;

    LinearLayout btn_pisa,btn_tori,btn_pagoda,btn_candi,btn_spinx,btn_monas;
    CircleView btn_to_profile;
    ImageView photo_home_user;
    TextView nama_lengkap,bio,user_balance;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    RelativeLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getUsernameLocal();

        progressBar = findViewById(R.id.loadingPanel);
        progressBar.setEnabled(true);
        progressBar.setVisibility(View.VISIBLE);

        btn_pisa = findViewById(R.id.btn_ticket_pisa);
        btn_tori = findViewById(R.id.btn_ticket_tori);
        btn_pagoda = findViewById(R.id.btn_ticket_pagoda);
        btn_candi = findViewById(R.id.btn_ticket_candi);
        btn_spinx = findViewById(R.id.btn_ticket_spinx);
        btn_monas = findViewById(R.id.btn_ticket_monas);

        btn_to_profile = findViewById(R.id.btn_to_profile);

        photo_home_user = findViewById(R.id.photo_home_user);

        nama_lengkap = findViewById(R.id.nama_lengkap);
        bio = findViewById(R.id.bio);
        user_balance = findViewById(R.id.user_balance);

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_lengkap.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                bio.setText(dataSnapshot.child("bio").getValue().toString());
                user_balance.setText("US$ "+dataSnapshot.child("user_balance").getValue().toString());
                Picasso.with(HomeActivity.this)
                        .load(dataSnapshot.child("url_photo_profile")
                                .getValue().toString()).centerCrop().fit()
                        .into(photo_home_user);

                if (dataSnapshot.exists()){
                    progressBar.setEnabled(false);
                    progressBar.setVisibility(View.GONE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_to_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoprofile = new Intent(HomeActivity.this,MyProfileActivity.class);
                startActivity(gotoprofile);
            }
        });

        btn_pisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gototicket = new Intent(HomeActivity.this,TicketDetailActivity.class);
                gototicket.putExtra("jenis_tiket","Pisa");
                startActivity(gototicket);
            }
        });
        btn_tori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gototicket = new Intent(HomeActivity.this,TicketDetailActivity.class);
                gototicket.putExtra("jenis_tiket","Tori");
                startActivity(gototicket);
            }
        });
        btn_pagoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gototicket = new Intent(HomeActivity.this,TicketDetailActivity.class);
                gototicket.putExtra("jenis_tiket","Pagoda");
                startActivity(gototicket);
            }
        });
        btn_candi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gototicket = new Intent(HomeActivity.this,TicketDetailActivity.class);
                gototicket.putExtra("jenis_tiket","Candi");
                startActivity(gototicket);
            }
        });
        btn_spinx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gototicket = new Intent(HomeActivity.this,TicketDetailActivity.class);
                gototicket.putExtra("jenis_tiket","Spinx");
                startActivity(gototicket);
            }
        });
        btn_monas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gototicket = new Intent(HomeActivity.this,TicketDetailActivity.class);
                gototicket.putExtra("jenis_tiket","Monas");
                startActivity(gototicket);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (backpressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        }else {
           backToast = Toast.makeText(this, "Press again to close app", Toast.LENGTH_SHORT);
           backToast.show();
        }
        backpressedTime = System.currentTimeMillis();
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
