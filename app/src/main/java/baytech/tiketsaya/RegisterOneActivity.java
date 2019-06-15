package baytech.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterOneActivity extends AppCompatActivity {

    Button btn_continue;
    LinearLayout btnback;
    EditText xusername,xpassword,xemail_address;
    DatabaseReference reference,userref;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);

        xusername = findViewById(R.id.username);
        xpassword = findViewById(R.id.password);
        btn_continue = findViewById(R.id.btn_continue);

        xemail_address = findViewById(R.id.email_address);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = xusername.getText().toString();
                final String password = xpassword.getText().toString();
                final String email_address = xemail_address.getText().toString();

                userref = FirebaseDatabase.getInstance().getReference().child("Users").child(xusername.getText().toString());
                userref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (username.isEmpty() && password.isEmpty() && email_address.isEmpty()){
                            Toast.makeText(RegisterOneActivity.this, "Isi Heula dude!", Toast.LENGTH_SHORT).show();
                        }else{
                            if (dataSnapshot.exists()){
                                Toast.makeText(RegisterOneActivity.this, "User Already Exists!", Toast.LENGTH_SHORT).show();
                            }else{
                                //menyimpan data pada local
                                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(username_key,xusername.getText().toString());
                                editor.apply();

                                //simpan data ke database
                                reference = FirebaseDatabase.getInstance().getReference().child("Users").child(xusername.getText().toString());
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        dataSnapshot.getRef().child("username").setValue(xusername.getText().toString());
                                        dataSnapshot.getRef().child("password").setValue(xpassword.getText().toString());
                                        dataSnapshot.getRef().child("email_address").setValue(xemail_address.getText().toString());
                                        dataSnapshot.getRef().child("user_balance").setValue(50);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(RegisterOneActivity.this, "Format Incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                Intent intent = new Intent(RegisterOneActivity.this,RegisterTwoActivity.class);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btnback = findViewById(R.id.btn_back);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}
