package org.etaskerapp.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.etaskerapp.R;
import org.etaskerapp.constant.Constant;
import org.etaskerapp.model.Task;
import org.etaskerapp.tool.Time;
import org.json.JSONException;
import org.json.JSONObject;

public class TaskStartActivity extends AppCompatActivity {

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_start_activity);

        ImageView lineColorCode = (ImageView)findViewById(R.id.chronometerImage);
        int color = Color.parseColor("#189c21");
        lineColorCode.setColorFilter(color);

        lineColorCode = (ImageView)findViewById(R.id.taskStarttBottomLeftButtonImage);
        color = Color.parseColor("#5e5e5e");
        lineColorCode.setColorFilter(color);

        Toolbar topBar = (Toolbar) findViewById(R.id.taskStartActivityTopBar);
        topBar.setTitleTextAppearance(this, R.style.ToolBar);
        setSupportActionBar(topBar);

        Intent intent = getIntent();
        task = (Task)intent.getSerializableExtra(AdminActivity.TASK);

        Chronometer simpleChronometer = (Chronometer) findViewById(R.id.chronometer);
        simpleChronometer.start();

        setButtonListeners();
    }

    private void setButtonListeners() {
        LinearLayout leftBottomButton = (LinearLayout)findViewById(R.id.taskSTartBottomRightButton);
        leftBottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AndroidNetworking.put(Constant.URL_TASKS + "/" + task.getId())
                        .setOkHttpClient(LoginActivity.OK_HTTP_CLIENT)
                        .addBodyParameter("status", "5")
                        .build()
                        .getAsObject(Task.class, new ParsedRequestListener<Task>() {
                            @Override
                            public void onResponse(Task t) {
                                task = t;
                                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                                intent.putExtra(AdminActivity.TASK, t);
                                Time.time = SystemClock.elapsedRealtime();
                                startActivity(intent);
                            }
                            @Override
                            public void onError(ANError anError) {
                                makeToast(anError.getErrorBody());
                            }
                        });
            }
        });

    }

    private void makeToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu_top, menu);
        return true;
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
}
