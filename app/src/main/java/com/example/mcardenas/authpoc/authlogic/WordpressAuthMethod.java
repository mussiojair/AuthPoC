package com.example.mcardenas.authpoc.authlogic;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mcardenas on 05/05/2017.
 */

public class WordpressAuthMethod extends CustomMethod {

    private static final String TOKEN_KEY = "wp_token";
    private static final String TOKEN_FILE = "com.example.mcardenas.authpoc.wp_token";
    private String user_email, user_nice_name, user_display_name;

    private final static String LOG_TAG = "WordpressAuthMethod";
    public WordpressAuthMethod(Context c){
        super();
        context = c;
    }

    @Override
    public void login() throws CredentialsException {
        throw new CredentialsException("");
    }

    @Override
    public void login(String url, Map params, final AuthListener authListener)  {

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams auth_params = new RequestParams();
        auth_params.add("username", params.get("username").toString());
        auth_params.add("password", params.get("password").toString());

        client.post(url, auth_params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(context, "Credenciales válidas", Toast.LENGTH_SHORT ).show();

                String content = new String(responseBody);
                try {

                    JSONObject jsoncontent = new JSONObject(content);

                    String t = jsoncontent.getString("token");
                    String user_email = jsoncontent.getString("user_email");
                    String user_nicename = jsoncontent.getString("user_nicename");
                    String user_display_name = jsoncontent.getString("user_display_name");

                    token = t;
                    WordpressAuthMethod.this.user_email = user_email;
                    WordpressAuthMethod.this.user_nice_name = user_nicename;
                    WordpressAuthMethod.this.user_display_name = user_display_name;

                    authListener.onSuccess();


                    Log.v(LOG_TAG, new String(responseBody));


                }catch( JSONException jsone){
                    jsone.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, "Usuario no registrado", Toast.LENGTH_SHORT ).show();
                // Log.v(LOG_TAG, new String(responseBody));
                authListener.onFailure();
            }
        });
    }

    @Override
    public void storeToken() {

        SharedPreferences sharedPreferences = context.getSharedPreferences(TOKEN_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY, token).commit();

    }

    @Override
    public void clearToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TOKEN_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TOKEN_KEY);
        editor.commit();
    }

    @Override
    public String getToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TOKEN_FILE, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(TOKEN_KEY, null);
        return token;
    }
}
