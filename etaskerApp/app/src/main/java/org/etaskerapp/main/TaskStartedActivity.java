package org.etaskerapp.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.etaskerapp.R;
import org.etaskerapp.constant.Constant;
import org.etaskerapp.dialog.StopDialog;
import org.etaskerapp.model.Material;
import org.etaskerapp.model.Task;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskStartedActivity extends AppCompatActivity {

    private Task task;
    static long timeTotal;
    static long timeStarted;
    static boolean stopped;
    private ListView listViewUsedMaterials;
    private LinearLayout linearLayoutUsedMaterials;

    private Chronometer chronometer;

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
        SpannableString s = new SpannableString(getResources().getString(R.string.app_name));
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green)), 2, 3, 1);
        getSupportActionBar().setTitle(s);

        Intent intent = getIntent();
        task = (Task)intent.getSerializableExtra(TaskListActivity.TASK);
        listViewUsedMaterials = (ListView)findViewById(R.id.listViewUsedMaterial);
        linearLayoutUsedMaterials = (LinearLayout)findViewById(R.id.linearLayoutUsedMaterials);
        linearLayoutUsedMaterials.setVisibility(LinearLayout.GONE);
        startChronometer();
        setButtonListeners();
        listUsedMaterials();
    }

    private void startChronometer() {
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime() - timeTotal);
        chronometer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startChronometer();
        listUsedMaterials();
    }

    private void showStopDialog() {
        LinearLayout l = new LinearLayout(getApplicationContext());
        l.setOrientation(LinearLayout.VERTICAL);
        TextView tv = new TextView(getApplicationContext());
        tv.setText("Select");
        tv.setTextSize(30);
        tv.setTextColor(Color.parseColor("#189c21"));
        tv.setPadding(75,55,15,15);

        l.addView(tv);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,
                R.style.AlertStyle);
        alertDialogBuilder.setCustomTitle(l).setItems(
                new String[]{"End of work day", "Task done"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                AndroidNetworking.put(Constant.URL_TASKS + "/" + task.getId())
                                        .setOkHttpClient(LoginActivity.OK_HTTP_CLIENT)
                                        .addBodyParameter("status", "5")
                                        .build()
                                        .getAsObject(Task.class, new ParsedRequestListener<Task>() {
                                            @Override
                                            public void onResponse(Task t) {
                                                timeTotal += System.currentTimeMillis() - timeStarted;
                                                stopped = true;
                                                task = t;
                                                Intent intent = new Intent(getApplicationContext(), TaskListActivity.class);
                                                intent.putExtra(TaskListActivity.TASK, t);
                                                startActivity(intent);
                                            }
                                            @Override
                                            public void onError(ANError anError) {
                                                makeToast(anError.getErrorBody());
                                            }
                                        });
                                break;
                            case 1:
                                AndroidNetworking.put(Constant.URL_TASKS + "/" + task.getId())
                                        .setOkHttpClient(LoginActivity.OK_HTTP_CLIENT)
                                        .addBodyParameter("status", "3")
                                        .addBodyParameter("end_time", new Date().toString())
                                        .build()
                                        .getAsObject(Task.class, new ParsedRequestListener<Task>() {
                                            @Override
                                            public void onResponse(Task t) {
                                                task = t;
                                                Intent intent = new Intent(getApplicationContext(),ImageActivity.class);
                                                intent.putExtra(TaskListActivity.TASK, t);
                                                startActivity(intent);
                                            }
                                            @Override
                                            public void onError(ANError anError) {
                                                makeToast(anError.getErrorBody());
                                            }
                                        });
                                break;
                        }
                    }
                }
        );


        AlertDialog  alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void setButtonListeners() {
        LinearLayout rightBottomButton = (LinearLayout)findViewById(R.id.taskSTartBottomRightButton);
        rightBottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStopDialog();
            }
        });

        LinearLayout leftBottomButton = (LinearLayout)findViewById(R.id.taskStartBottomLeftButton);
        leftBottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MaterialActivity.class);
                intent.putExtra(TaskListActivity.TASK, task);
                startActivity(intent);
            }
        });

    }

    void listUsedMaterials() {
        AndroidNetworking.get(Constant.URL_MATERIALS)
                .setOkHttpClient(LoginActivity.OK_HTTP_CLIENT)
                .build()
                .getAsObjectList(Material.class, new ParsedRequestListener<List<Material>>() {
                    @Override
                    public void onResponse(List<Material> materials) {
                        fillUsedMaterials(materials);
                    }
                    @Override
                    public void onError(ANError anError) {
                        makeToast(anError.getMessage());
                    }
                });
    }

    void fillUsedMaterials(List<Material> list) {
        List<Material> tempList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getQuantity() != null && list.get(i).getQuantity() > 0) {
                tempList.add(list.get(i));
            }
        }

        if (tempList.size() > 0) {
            linearLayoutUsedMaterials.setVisibility(LinearLayout.VISIBLE);
            UsedMaterialAdapter listAdapter = new UsedMaterialAdapter(getApplicationContext(), tempList, this);
            listViewUsedMaterials.setAdapter(listAdapter);
        } else {
            linearLayoutUsedMaterials.setVisibility(LinearLayout.GONE);
        }


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
