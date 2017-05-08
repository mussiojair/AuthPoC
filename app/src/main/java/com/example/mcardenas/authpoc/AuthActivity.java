package com.example.mcardenas.authpoc;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.mcardenas.authpoc.authlogic.AuthListener;
import com.example.mcardenas.authpoc.authlogic.WordpressAuthMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class AuthActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AuthActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;

    private String username = "mussio", password = "N0m4m3s_";
    private Button mCustomAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        setup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_auth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop(){
        super.onStop();

        if( authListener != null){
            mAuth.removeAuthStateListener(authListener);
        }
    }


    private void setup(){

        mAuth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    Log.d(LOG_TAG, "user signed_in");
                }else{
                    Log.d(LOG_TAG, "user signed_out");
                }
            }
        };


        mCustomAuth = (Button) findViewById(R.id.auth_custom);
        mCustomAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map map = new HashMap<String, String>();
                map.put("username", username);
                map.put("password", password);

                final WordpressAuthMethod wpauth = new WordpressAuthMethod(AuthActivity.this);
                wpauth.login("http://mussiocardenas.com/wp-json/jwt-auth/v1/token", map, new AuthListener() {
                    @Override
                    public void onSuccess() {

                        // guarda el token recibido en el dispositivo
                        wpauth.storeToken();
                        String token = wpauth.getToken();

                        Log.v(LOG_TAG, "stored token: " + token);

                        // Intercambiar Token con Firebase
                        mAuth.signInWithCustomToken(token).addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(LOG_TAG, "signInWithCustomToken:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w(LOG_TAG, "signInWithCustomToken", task.getException());
                                    Toast.makeText(AuthActivity.this, "Authentication con Firebase falló.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(AuthActivity.this, "Authentication con Wordpress falló.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
