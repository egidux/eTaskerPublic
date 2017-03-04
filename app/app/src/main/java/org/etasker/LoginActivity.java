package org.etasker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.*;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.etasker.constants.Constant;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class LoginActivity extends AppCompatActivity {
    public static String ID;
    private static final String URL_LOGIN = Constant.URL + "/user/api/workers/login";
    public static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {
                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url.host(), cookies);
                }
                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            }).build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        AndroidNetworking.initialize(getApplicationContext());
    }

    public void login(View view) {
        final Button btnLogin = ((Button)findViewById(R.id.btn_login));
        btnLogin.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        progressDialog.show();

        String email = ((EditText)findViewById(R.id.email_login)).getText().toString();
        String password = ((EditText)findViewById(R.id.password_login)).getText().toString();

        AndroidNetworking.post(URL_LOGIN)
                .setOkHttpClient(OK_HTTP_CLIENT)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ID = response.getString("id");
                        } catch (JSONException e) {}

                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(ANError error) {
                        btnLogin.setEnabled(true);
                        progressDialog.dismiss();
                        try {
                            JSONObject errorJson = new JSONObject(error.getErrorBody());
                            Toast.makeText(getApplicationContext(), errorJson.getString("error"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {}
                     }
                });
    }
}