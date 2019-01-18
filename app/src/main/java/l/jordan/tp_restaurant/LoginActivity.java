package l.jordan.tp_restaurant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE);
        if (sharedPref.contains("isAuth") && sharedPref.getBoolean("isAuth", false)){
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LoginActivity.this, RecipeActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        setContentView(R.layout.activity_login);
        EditText edit_txt = findViewById(R.id.pass);

        edit_txt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    findViewById(R.id.btnLogin).performClick();
                    return true;
                }
                return false;
            }
        });
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
        else failure("Votre login ou mot de passe incorrect");
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
                        Intent intent = new Intent(LoginActivity.this, RecipeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
            else if (obj.has("error")) pair=new Pair<>(false, obj.getString("error"));
            else pair=new Pair<>(false, "Une erreur s'est produite");
        }
        catch (Exception e) {
            e.printStackTrace();
            pair=new Pair<>(false, e.getMessage());
        }
        if (!pair.first)
            failure(pair.second);
    }

    @Override
    public void failure(String response) {
        ((TextView)findViewById(R.id.err)).setText(response);
    }
}