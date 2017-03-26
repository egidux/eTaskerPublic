package org.etaskerapp.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
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

public class RateActivity extends AppCompatActivity {

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_activity);

        Toolbar topBar = (Toolbar) findViewById(R.id.taskActivityTopBar);
        topBar.setTitleTextAppearance(this, R.style.ToolBar);
        setSupportActionBar(topBar);
        SpannableString s = new SpannableString(getResources().getString(R.string.app_name));
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green)), 2, 3, 1);
        getSupportActionBar().setTitle(s);

        Intent intent = getIntent();
        task = (Task)intent.getSerializableExtra(TaskListActivity.TASK);

        setListeners();
    }

    private void makeToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    private void setListeners() {

        findViewById(R.id.buttonRateSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText)findViewById(R.id.rateName)).getText().toString();
                double rate = ((RatingBar)findViewById(R.id.ratingBar)).getRating();
                boolean agreed = ((RadioButton) findViewById(R.id.radioButtonAgree)).isChecked();
                AndroidNetworking.put(Constant.URL_TASKS + "/" + task.getId())
                        .setOkHttpClient(LoginActivity.OK_HTTP_CLIENT)
                        .addBodyParameter("signed_by", name)
                        .addBodyParameter("rating", ((int)rate) + "")
                        .addBodyParameter("agreed", agreed + "")
                        .addBodyParameter("status", task.getStatus() + "")
                        .build()
                        .getAsObject(Task.class, new ParsedRequestListener<Task>() {
                            @Override
                            public void onResponse(Task t) {
                                Intent intent = new Intent(getApplicationContext(), TaskListActivity.class);
                                startActivity(intent);
                            }
                            @Override
                            public void onError(ANError anError) {
                                makeToast(anError.getErrorBody());
                            }
                        });
            }
        });


        RadioButton rbagree = (RadioButton) findViewById(R.id.radioButtonAgree);
        rbagree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent(getApplicationContext(), SignatureActivity.class);
                    intent.putExtra(TaskListActivity.TASK, task);
                    startActivity(intent);
                }
            }
        });

        RadioButton rbagreen = (RadioButton) findViewById(R.id.radioButtonDontAgree);
        rbagreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent(getApplicationContext(), SignatureActivity.class);
                    intent.putExtra(TaskListActivity.TASK, task);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), TaskListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                AndroidNetworking.post(Constant.URL_LOGOUT)
                        .setOkHttpClient(LoginActivity.OK_HTTP_CLIENT)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                            @Override
                            public void onError(ANError error) {
                                try {
                                    JSONObject errorJson = new JSONObject(error.getErrorBody());
                                    Toast.makeText(getApplicationContext(), errorJson.getString("error"), Toast.LENGTH_LONG).show();

                                } catch (JSONException e) {}
                            }
                        });
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu_top, menu);
        return true;
    }
}
