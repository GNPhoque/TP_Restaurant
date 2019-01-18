package l.jordan.tp_restaurant;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity implements WebService.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        EditText edit_txt = findViewById(R.id.pass);

        edit_txt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    findViewById(R.id.btnSignup).performClick();
                    return true;
                }
                return false;
            }
        });
    }

    public void onClickSignup(View v){
        final String login = ((EditText)findViewById(R.id.login)).getText().toString();
        final String pass = ((EditText)findViewById(R.id.pass)).getText().toString();
        final String nom = ((EditText)findViewById(R.id.nom)).getText().toString();
        final String prenom = ((EditText)findViewById(R.id.prenom)).getText().toString();
        if (Pattern.matches(".*@.*[.].*", login) && (Pattern.matches(".*", pass))
                && (Pattern.matches(".*", nom)) && (Pattern.matches(".*", prenom))){
            Map<String, String> map = new HashMap<String, String>();
            map.put("login", login);
            map.put("pass", pass);
            map.put("nom", nom);
            map.put("prenom", prenom);
            WebService.sendRequest("/addCompte", map,this);
        }
        else failure("Tout les champs sont requis");
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
                        Intent intent = new Intent(SignupActivity.this, RecipeActivity.class);
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
