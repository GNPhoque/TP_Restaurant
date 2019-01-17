package l.jordan.tp_restaurant;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void onClickSignup(View v){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SignupActivity.this, CoreActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
