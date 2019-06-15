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

public class SuccestBuyTicketActivity extends AppCompatActivity {

    Animation success_register,getstarted2,getstarted1;
    Button btn_to_dashboard;
    ImageView icon_success;
    TextView app_title,app_subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succest_buy_ticket);

        btn_to_dashboard = findViewById(R.id.btn_to_dashboard);
        icon_success = findViewById(R.id.icon_success);
        app_title = findViewById(R.id.app_title);
        app_subtitle = findViewById(R.id.app_subtitle);

        getstarted2 = AnimationUtils.loadAnimation(this,R.anim.getstarted2);
        getstarted1 = AnimationUtils.loadAnimation(this,R.anim.getstarted1);
        success_register = AnimationUtils.loadAnimation(this,R.anim.success_register);

        icon_success.setAnimation(success_register);
        app_title.setAnimation(getstarted1);
        app_subtitle.setAnimation(getstarted1);
        btn_to_dashboard.setAnimation(getstarted2);

        btn_to_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotodashboard = new Intent(SuccestBuyTicketActivity.this,HomeActivity.class);
                startActivity(gotodashboard);
            }
        });
    }
}
