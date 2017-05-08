package com.example.mcardenas.authpoc.authlogic;

import android.content.Context;

import java.util.Map;

/**
 * Created by mcardenas on 05/05/2017.
 */

public abstract class CustomMethod implements AuthMethod {

    protected String url, username, password;
    protected Context context;
    protected String token;

    @Override
    public void login() throws CredentialsException {

    }

    @Override
    public void login(String url, Map params, AuthListener listener) throws CredentialsException {

    }

    @Override
    public void storeToken() {

    }

    @Override
    public void clearToken() {

    }
}
