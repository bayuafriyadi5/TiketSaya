package baytech.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    Animation app_splash;
    Animation subtitle;
    TextView app_subtitle;
    ImageView app_logo;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        app_logo = findViewById(R.id.app_logo);

        app_subtitle = findViewById(R.id.app_subtitle);

        app_splash = AnimationUtils.loadAnimation(this,R.anim.app_splash);

        subtitle = AnimationUtils.loadAnimation(this,R.anim.subtitle);

        app_logo.startAnimation(app_splash);

        app_subtitle.startAnimation(subtitle);

        getUsernameLocal();


    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
        if(username_key_new.isEmpty()){
            Handler handler= new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this,GetStartedActivity.class);
                    startActivity(intent);
                    finish();
                }
            },7000);
        }else{
            Handler handler= new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            },7000);
        }
    }
}
