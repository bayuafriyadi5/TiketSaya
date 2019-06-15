package baytech.tiketsaya;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    Button btn_register;
    Button btn_signin;
    EditText xusername,xpassword;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    RelativeLayout progressBar;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        progressBar = findViewById(R.id.loadingPanel);

        progressBar.setEnabled(false);
        progressBar.setVisibility(View.GONE);

        btn_signin = findViewById(R.id.btn_sign_in);
        btn_register = findViewById(R.id.btn_register);
        xusername = findViewById(R.id.xusername);
        xpassword = findViewById(R.id.xpassword);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this,RegisterOneActivity.class);
                startActivity(intent);
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setEnabled(true);
                progressBar.setVisibility(View.VISIBLE);

                final String username = xusername.getText().toString();
                final String password = xpassword.getText().toString();

                if(username.isEmpty()){
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    progressBar.setEnabled(false);
                    Toast.makeText(SignInActivity.this, "username isi heula atuh boy!", Toast.LENGTH_SHORT).show();
                }else{
                    if(password.isEmpty()){
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        progressBar.setEnabled(false);
                        Toast.makeText(SignInActivity.this, "password isi heula atuh boy!", Toast.LENGTH_SHORT).show();
                    }else {
                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username);

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){

                                    //ambil password dari firebase
                                    String passwordFromFirebase = Objects.requireNonNull(dataSnapshot.child("password").getValue()).toString();

                                    //validasi password input dengan password firebase
                                    if (password.equals(passwordFromFirebase)){
                                        //menyimpan data pada local
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key,xusername.getText().toString());
                                        editor.apply();

                                        Intent intent =  new Intent(SignInActivity.this,HomeActivity.class);
                                        startActivity(intent);

                                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                        progressBar.setEnabled(false);
                                    }
                                    else{
                                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                        progressBar.setEnabled(false);
                                        Toast.makeText(SignInActivity.this, "Passwordna salah eui!", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                    progressBar.setEnabled(false);
                                    Toast.makeText(SignInActivity.this, "Eweh userna Boy!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
        });
    }
}
