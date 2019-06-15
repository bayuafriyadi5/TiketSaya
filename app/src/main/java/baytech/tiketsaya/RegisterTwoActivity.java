package baytech.tiketsaya;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisterTwoActivity extends AppCompatActivity {

    Button btn_continue,btn_add_photo;
    LinearLayout btnback;
    ImageView pic_photo_regis_user;
    EditText xbio,xnama_lengkap;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    DatabaseReference reference;
    StorageReference storage;

    Uri photo_location;
    Integer photo_max = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        getUsernameLocal();

        btn_add_photo = findViewById(R.id.btn_add_photo);
        pic_photo_regis_user = findViewById(R.id.picture_photo_register_user);
        btn_continue = findViewById(R.id.btn_continue);
        btnback = findViewById(R.id.btn_back);
        xbio = findViewById(R.id.bio);
        xnama_lengkap = findViewById(R.id.nama_lengkap);

        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String nama_lengkap = xnama_lengkap.getText().toString();
                final String bio = xbio.getText().toString();

                if (nama_lengkap.isEmpty() ||  bio.isEmpty() ){
                    btn_continue.setEnabled(true);
                    btn_continue.setText("Continue");
                    Toast.makeText(RegisterTwoActivity.this, "Data lengkapkeun heula Dude!", Toast.LENGTH_SHORT).show();
                }else{
                    //loading
                    btn_continue.setEnabled(false);
                    btn_continue.setText("Loading...");

                    //menyimpan data ke firebase
                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                    storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(username_key_new);

                    //validasi file
                    if(photo_location != null){
                        final StorageReference storageReference1 = storage.child(System.currentTimeMillis() + "." + getFileExtension(photo_location));

                        storageReference1.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                            reference.getRef().child("url_photo_profile").setValue(uri.toString());
                                            reference.getRef().child("nama_lengkap").setValue(xnama_lengkap.getText().toString());
                                            reference.getRef().child("bio").setValue(xbio.getText().toString());
                                    }
                                });
                            }
                        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                Intent intent = new Intent(RegisterTwoActivity.this,SuccessRegisterActivity.class);
                                startActivity(intent);

                            }
                        });
                    }
                    else{
                        btn_continue.setEnabled(true);
                        btn_continue.setText("Continue");
                        Toast.makeText(RegisterTwoActivity.this, "Hese sih sia Di bejaan Goblok!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

    }

    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public  void findPhoto(){
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic,photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null){
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(pic_photo_regis_user);
        }
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
