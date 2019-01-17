package l.jordan.tp_restaurant;

import android.util.Log;
import android.util.Pair;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Response;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebService {

    public interface Listener {
        public void success(String data);

        public void failure(String response);
    }

    public static void sendRequest(final Listener listener){

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type","application/json");

        Fuel.post("http://51.15.254.4:9001/ws/resto/addCompte")
        .jsonBody("{\"login\":\"aze@aze.aze\",\"pass\":\"azeaze\",\"nom\":\"aze\",\"prenom\":\"aze\"}", Charset.forName("UTF-8"))
        .header(header)
        .responseString(new com.github.kittinunf.fuel.core.Handler<String>() {
            @Override
            public void failure(com.github.kittinunf.fuel.core.Request request, Response response, FuelError error) {
                Log.d("error", "failure: "+error);
                listener.failure(error.toString());
            }
            @Override
            public void success(com.github.kittinunf.fuel.core.Request request, Response response, String data) {
                try {
                    JSONObject obj = new JSONObject(response.getResponseMessage());
                    listener.success(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

        public static void sendRequest(String target, Map<String, String> params, final Listener listener){
        if(params == null){
            params = new HashMap<>();
        }
        String jsonString = new Gson().toJson(params);

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type","application/json");

        Fuel.post("http://51.15.254.4:9001/ws/resto" + target)
            .jsonBody(jsonString,Charset.forName("UTF-8"))
//            .jsonBody("{\"login\":\"aze@aze.aze\",\"pass\":\"azeaze\",\"nom\":\"aze\",\"prenom\":\"aze\"}",Charset.forName("UTF-8"))
            .header(header)
            .responseString(new Handler<String>() {
                @Override
                public void failure(com.github.kittinunf.fuel.core.Request request, Response response, FuelError error) {
                    Log.d("error", "failure: "+error);
                    listener.failure(error.toString());
                }
                @Override
                public void success(com.github.kittinunf.fuel.core.Request request, Response response, String data) {
                        listener.success(data);
                }
            });
    }
}