package org.etaskerapp.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.*;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.etaskerapp.R;
import org.etaskerapp.constant.Constant;
import org.etaskerapp.model.Task;
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
    public static String name;
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

    private void makeToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        SpannableString s = new SpannableString(getResources().getString(R.string.app_name));
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green)), 2, 3, 1);
        TextView tv= (TextView) findViewById(R.id.loginLogo);
        tv.setText(s);
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

        final String email = ((EditText)findViewById(R.id.email_login)).getText().toString();
        final String password = ((EditText)findViewById(R.id.password_login)).getText().toString();

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
                            name = response.getString("name");
                        } catch (JSONException e) {}

                        SharedPreferences sharedPreference = getApplicationContext().getSharedPreferences(
                                getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
                        String token = sharedPreference.getString(getString(R.string.FCM_TOKEN), "");
                        AndroidNetworking.post(Constant.URL_TOKEN)
                                .setOkHttpClient(LoginActivity.OK_HTTP_CLIENT)
                                .addBodyParameter("worker", email)
                                .addBodyParameter("token", token)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                    }

                                    @Override
                                    public void onError(ANError anError) {

                                    }
                                });
                        Intent intent = new Intent(getApplicationContext(), TaskListActivity.class);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}