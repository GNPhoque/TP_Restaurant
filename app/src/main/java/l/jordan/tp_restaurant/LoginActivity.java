package l.jordan.tp_restaurant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements WebService.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE);
        if (sharedPref.contains("isAuth") && sharedPref.getBoolean("isAuth", false)){
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LoginActivity.this, CoreActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        setContentView(R.layout.activity_login);
    }

    public void onClickConnexion(View v){
        final String login = ((EditText)findViewById(R.id.login)).getText().toString();
        final String pass = ((EditText)findViewById(R.id.pass)).getText().toString();
        if (Pattern.matches(".*@.*[.].*", login) && (Pattern.matches(".*", pass))){
            Map<String, String> map = new HashMap<String, String>();
            map.put("login", login);
            map.put("pass", pass);
            WebService.sendRequest("/connexion", map,this);
        }
        else ((TextView)findViewById(R.id.err)).setText("Email or password is incorrect");
    }

    public void onClickSignup(View v){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void success(String response) {
        Pair<Boolean, String> pair;
        try{
            JSONObject obj = new JSONObject(response);
            if (obj.has("id")) {
                pair = new Pair<>(true, "success");
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoginActivity.this, CoreActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
            else if (obj.has("error")) pair=new Pair<>(false, obj.getString("error"));
            else pair=new Pair<>(false, "An error occured");
        }
        catch (JSONException e) {
            e.printStackTrace();
            pair=new Pair<>(false, e.getMessage());
        }
        displayMessage(pair);
    }

    @Override
    public void failure(String response) {
        ((TextView)findViewById(R.id.err)).setText("Email or password is incorrect");
    }

    private void displayMessage(Pair<Boolean, String> pair) {
        if (pair.first){
            Toast.makeText(this, pair.second, Toast.LENGTH_LONG).show();
        }
    }
}