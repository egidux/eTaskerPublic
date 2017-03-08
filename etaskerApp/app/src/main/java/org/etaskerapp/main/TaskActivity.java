package org.etaskerapp.main;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.etaskerapp.R;
import org.etaskerapp.constant.Constant;
import org.etaskerapp.model.Client;
import org.etaskerapp.model.Object;
import org.etaskerapp.model.Task;
import org.json.JSONException;
import org.json.JSONObject;

public class TaskActivity extends AppCompatActivity {

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity);

        ImageView lineColorCode = (ImageView)findViewById(R.id.taskBottomLeftButtonImage);
        int color = Color.parseColor("#8e8e8e");
        lineColorCode.setColorFilter(color);

        Intent intent = getIntent();
        task = (Task)intent.getSerializableExtra(AdminActivity.TASK);

        TextView startButtonText = (TextView)findViewById(R.id.taskBottomRightButtonText);
        if (task.getStatus() == 0 || task.getStatus() == 1) {
            startButtonText.setText("Start ");
        } else if (task.getStatus() == 3 || task.getStatus() == 4){
            findViewById(R.id.taskFooter).setVisibility(View.GONE);
        } else if (task.getStatus() == 5){
            startButtonText.setText("Resume ");
        } else {
            startButtonText.setText("Next");
            ((ImageView)findViewById(R.id.taskBottomRightButtonImage)).setImageResource(R.drawable.ic_navigate_next_white_24dp);
        }

        Toolbar topBar = (Toolbar) findViewById(R.id.taskActivityTopBar);
        topBar.setTitleTextAppearance(this, R.style.ToolBar);
        setSupportActionBar(topBar);
        SpannableString s = new SpannableString(getResources().getString(R.string.app_name));
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green)), 2, 3, 1);
        getSupportActionBar().setTitle(s);

        fillTaskDetails();
    }

    private void setClickListeners(final Object object) {
        LinearLayout leftBottomButton = (LinearLayout)findViewById(R.id.taskBottomLeftButton);
        leftBottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+object.getLat()+","+object.getLng()+"");

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        LinearLayout rightBottomButton = (LinearLayout)findViewById(R.id.taskBottomRightButton);
        rightBottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidNetworking.put(Constant.URL_TASKS + "/" + task.getId())
                        .setOkHttpClient(LoginActivity.OK_HTTP_CLIENT)
                        .addBodyParameter("status", "2")
                        .build()
                        .getAsObject(Task.class, new ParsedRequestListener<Task>() {
                            @Override
                            public void onResponse(Task t) {
                                task = t;
                                Intent intent = new Intent(getApplicationContext(), TaskStartActivity.class);
                                intent.putExtra(AdminActivity.TASK, t);
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

    private void fillTaskDetails() {
        AndroidNetworking.get(Constant.URL_CLIENTS + "/name/" + task.getClient())
                .setOkHttpClient(LoginActivity.OK_HTTP_CLIENT)
                .build()
                .getAsObject(Client.class, new ParsedRequestListener<Client>() {
                    @Override
                    public void onResponse(Client client) {
                        ((TextView)findViewById(R.id.TaskClientName)).setText(client.getName());
                        ((TextView)findViewById(R.id.TaskClientAddress)).setText(client.getAddress());
                        AndroidNetworking.get(Constant.URL_OBJECTS + "/name/" + task.getObject())
                                .setOkHttpClient(LoginActivity.OK_HTTP_CLIENT)
                                .build()
                                .getAsObject(Object.class, new ParsedRequestListener<Object>() {
                                    @Override
                                    public void onResponse(Object object) {
                                        ((TextView)findViewById(R.id.TaskObjectName)).setText(object.getName());
                                        ((TextView)findViewById(R.id.TaskObjectAddress)).setText(object.getAddress());
                                        setClickListeners(object);
                                    }
                                    @Override
                                    public void onError(ANError anError) {
                                        makeToast(anError.getErrorBody());
                                    }
                                });
                    }
                    @Override
                    public void onError(ANError anError) {
                        makeToast(anError.getErrorBody());
                    }
                });
        ((TextView)findViewById(R.id.TaskTitle)).setText(task.getTitle());
        ((TextView)findViewById(R.id.TaskDescription)).setText(task.getDescription());
        ((TextView)findViewById(R.id.TaskWorker)).setText(task.getWorker());
        ((TextView)findViewById(R.id.TaskPlanedStart)).setText(task.getPlanned_time());
        ((TextView)findViewById(R.id.TaskPlanedEnd)).setText(task.getPlanned_end_time());
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
