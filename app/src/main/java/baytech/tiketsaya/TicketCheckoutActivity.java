package baytech.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class TicketCheckoutActivity extends AppCompatActivity {

    Button btn_pay_ticket,btn_plus,btn_min;
    TextView textjumlahtiket,texttotalharga,textmybalance,nama_wisata,lokasi,ketentuan;
    Integer valuejumlahtiket = 1;
    Integer mybalance = 0;
    Integer valuetotalharga = 0;
    Integer valuehargatiket = 0;
    ImageView alert_uang;
    DatabaseReference reference,userref,simpanref,updatebalanceref;
    LinearLayout btn_back;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    Integer nomor_transaksi = new Random().nextInt();

    String date_wisata = "";
    String time_wisata = "";
    Integer sisa_balance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);

        getUsernameLocal();

        //mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");

        btn_plus = findViewById(R.id.btn_plus);
        btn_back = findViewById(R.id.btn_back);
        btn_min = findViewById(R.id.btn_min);
        btn_pay_ticket = findViewById(R.id.btn_pay_ticket);
        textjumlahtiket = findViewById(R.id.textjumlahtiket);
        texttotalharga = findViewById(R.id.text_total_harga);
        textmybalance = findViewById(R.id.text_my_balance);
        alert_uang = findViewById(R.id.alert_uang);
        nama_wisata = findViewById(R.id.nama_wisata);
        ketentuan = findViewById(R.id.ketentuan);
        lokasi = findViewById(R.id.lokasi);

        alert_uang.animate().alpha(0).setDuration(200).start();
        alert_uang.setEnabled(false);

        textjumlahtiket.setText(valuejumlahtiket.toString());

        btn_min.animate().alpha(0).setDuration(300).start();
        btn_min.setEnabled(false);

        //nyokot data ti users firebase
        userref = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mybalance = Integer.valueOf(dataSnapshot.child("user_balance").getValue().toString());
                textmybalance.setText("US$ " + mybalance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //nyokot data ti firebase sesuai intent
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                ketentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());

                date_wisata = dataSnapshot.child("date_wisata").getValue().toString();
                time_wisata = dataSnapshot.child("time_wisata").getValue().toString();

                valuehargatiket = Integer.valueOf(dataSnapshot.child("harga_tiket").getValue().toString());

                valuetotalharga = valuehargatiket * valuejumlahtiket;
                texttotalharga.setText("US$ " + valuetotalharga);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valuejumlahtiket += 1;
                textjumlahtiket.setText(valuejumlahtiket.toString());
                if (valuejumlahtiket >1 ){
                    btn_min.animate().alpha(1).setDuration(300).start();
                    btn_min.setEnabled(true);
                }
                valuetotalharga = valuehargatiket * valuejumlahtiket;
                texttotalharga.setText("US$" +valuetotalharga+"");
                if (valuetotalharga > mybalance){
                    btn_pay_ticket.animate().translationY(300).alpha(0).setDuration(350).start();
                    btn_pay_ticket.setEnabled(false);
                    textmybalance.setTextColor(Color.RED);
                    alert_uang.animate().alpha(1).setDuration(200).start();
                    alert_uang.setEnabled(true);
                }
            }
        });

        btn_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valuejumlahtiket -= 1;
                textjumlahtiket.setText(valuejumlahtiket.toString());
                if (valuejumlahtiket <2 ){
                    btn_min.animate().alpha(0).setDuration(300).start();
                    btn_min.setEnabled(false);
                }
                valuetotalharga = valuehargatiket * valuejumlahtiket;
                texttotalharga.setText("US$" +valuetotalharga+"");
                if (valuetotalharga < mybalance){
                    btn_pay_ticket.animate().translationY(0).alpha(1).setDuration(350).start();
                    btn_pay_ticket.setEnabled(true);
                    textmybalance.setTextColor(Color.BLUE);
                    alert_uang.animate().alpha(0).setDuration(200).start();
                    alert_uang.setEnabled(false);
                }
            }
        });

        btn_pay_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //nyimpen data users firebase jeung nyien tabel baru MyTickets
                simpanref = FirebaseDatabase.getInstance()
                        .getReference().child("MyTickets")
                        .child(username_key_new).child(nama_wisata.getText().toString() + nomor_transaksi);
                simpanref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        simpanref.getRef().child("id_ticket").setValue(nama_wisata.getText().toString() + nomor_transaksi);
                        simpanref.getRef().child("nama_wisata").setValue(nama_wisata.getText().toString());
                        simpanref.getRef().child("lokasi").setValue(lokasi.getText().toString());
                        simpanref.getRef().child("ketentuan").setValue(ketentuan.getText().toString());
                        simpanref.getRef().child("jumlah_tiket").setValue(valuejumlahtiket.toString());
                        simpanref.getRef().child("date_wisata").setValue(date_wisata);
                        simpanref.getRef().child("time_wisata").setValue(time_wisata);

                        Intent gotosuccessticket = new Intent(TicketCheckoutActivity.this,SuccestBuyTicketActivity.class);
                        startActivity(gotosuccessticket);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                updatebalanceref = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                updatebalanceref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        sisa_balance = mybalance - valuetotalharga;
                        updatebalanceref.getRef().child("user_balance").setValue(sisa_balance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
