package baytech.tiketsaya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GetStartedActivity extends AppCompatActivity {

    Button btnsignin;
    Button btnregister;
    Animation getstarted1,getstarted2;
    ImageView emblem_app;
    TextView intro_app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        getstarted1 = AnimationUtils.loadAnimation(this,R.anim.getstarted1);
        getstarted2 = AnimationUtils.loadAnimation(this,R.anim.getstarted2);

        btnsignin = findViewById(R.id.btn_sign_in);
        btnregister = findViewById(R.id.btn_register_get_started);

        emblem_app = findViewById(R.id.emblem_app);

        intro_app = findViewById(R.id.intro_app);

        emblem_app.startAnimation(getstarted1);

        intro_app.startAnimation(getstarted1);

        btnsignin.startAnimation(getstarted2);

        btnregister.startAnimation(getstarted2);

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetStartedActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetStartedActivity.this,RegisterOneActivity.class);
                startActivity(intent);
            }
        });
    }
}
