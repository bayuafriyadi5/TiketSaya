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

public class SuccessRegisterActivity extends AppCompatActivity {

    Animation success_register,getstarted2,getstarted1;
    Button btn_explore;
    ImageView icon_success;
    TextView app_title,app_subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_register);

        getstarted2 = AnimationUtils.loadAnimation(this,R.anim.getstarted2);
        getstarted1 = AnimationUtils.loadAnimation(this,R.anim.getstarted1);
        success_register = AnimationUtils.loadAnimation(this,R.anim.success_register);

        icon_success = findViewById(R.id.icon_success);

        app_title = findViewById(R.id.app_title);

        app_subtitle = findViewById(R.id.app_subtitle);

        btn_explore = findViewById(R.id.btn_explore);

        icon_success.startAnimation(success_register);
        app_title.startAnimation(getstarted1);
        app_subtitle.startAnimation(getstarted1);
        btn_explore.startAnimation(getstarted2);

        btn_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessRegisterActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
