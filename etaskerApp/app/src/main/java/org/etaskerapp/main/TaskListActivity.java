package org.etaskerapp.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.reflect.TypeToken;

import org.etaskerapp.R;
import org.etaskerapp.constant.Constant;
import org.etaskerapp.model.Object;
import org.etaskerapp.model.Task;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TaskListActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int PERMISSION_LOCATION_REQUEST_CODE = 1;
    public static final String TASK = "task";

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private ListView listViewTasks;
    private ProgressDialog progressDialog;
    private static int filterStatus = 0;
    private TextView currentFilter;
    private boolean isOpenSwipeLayout;
    public static boolean isTaskStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list_activity);

        listViewTasks = (ListView)findViewById(R.id.task_list_view);

        progressDialog = new ProgressDialog(TaskListActivity.this, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        progressDialog.show();


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        Toolbar topBar = (Toolbar) findViewById(R.id.adminActivityTopBar);
        topBar.setTitleTextAppearance(this, R.style.ToolBar);
        setSupportActionBar(topBar);
        SpannableString s = new SpannableString(getResources().getString(R.string.app_name));
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green)), 2, 3, 1);
        getSupportActionBar().setTitle(s);

        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        listTasks();
                    }
                }
        );

        listTasks();
        initFilter();
    }

    private void initFilter() {
       final SwipeLayout swipeLayout =  (SwipeLayout)findViewById(R.id.sample1);

//set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

//add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, findViewById(R.id.bottom_wrapper));

        new Thread(new Runnable() {
            @Override
            public void run() {
                swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                    @Override
                    public void onStartOpen(SwipeLayout layout) {
                        isOpenSwipeLayout = true;
                    }

                    @Override
                    public void onStartClose(SwipeLayout layout) {
                        isOpenSwipeLayout = false;
                    }
                });

                swipeLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (isOpenSwipeLayout) {
                                    // Opens the layout without animation
                                    swipeLayout.open(false);
                                }
                            }
                        });

            }
        }).start();


        TextView[] filters = new TextView[] {(TextView) findViewById(R.id.filterAll),
                (TextView) findViewById(R.id.filterAssigned), (TextView) findViewById(R.id.filterInProggress),
                (TextView) findViewById(R.id.filterDone), (TextView) findViewById(R.id.filterAborted),
                (TextView) findViewById(R.id.filterStopped)};

        currentFilter = (TextView) findViewById(R.id.filterAll);
        for (TextView tv: filters) {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentFilter.setBackgroundColor(0x00000000);
                    currentFilter.setTextColor(Color.parseColor("#808080"));
                    v.setBackgroundColor(getResources().getColor(R.color.green));
                    ((TextView) v).setTextColor(getResources().getColor(R.color.white));
                    currentFilter = (TextView) v;
                    switch (((TextView)v).getText().toString()) {
                        case "All": filterStatus = 0; break;
                        case "Assigned": filterStatus = 1; break;
                        case "In proggress": filterStatus = 2; break;
                        case "Done": filterStatus = 3; break;
                        case "Aborted": filterStatus = 4; break;
                        case "Stopped": filterStatus = 5; break;
                    }
                    listTasks();
                    isOpenSwipeLayout = false;
                }
            });
        }
    }

    private void makeToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    private void fillTaskListView(List<Task> list) {
        ListAdapter listAdapter = new TaskAdapter(getApplicationContext(), list);
        listViewTasks.setAdapter(listAdapter);

        mySwipeRefreshLayout.setRefreshing(false);
        listViewTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task)parent.getItemAtPosition(position);
                if (task.getStatus() == 1 || task.getStatus() == 2 ||
                        task.getStatus() == 5) {
                    Intent intent = new Intent(getApplicationContext(), TaskActivity.class);
                    intent.putExtra(TASK, task);
                    startActivity(intent);
                } else if(task.getStatus() == 3) {
                    if (task.getFile_exists() == null || !task.getFile_exists()) {
                        Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
                        intent.putExtra(TASK, task);
                        startActivity(intent);
                    } else if (task.getSignature_exists() == null || !task.getSignature_exists()) {
                        Intent intent = new Intent(getApplicationContext(), RateActivity.class);
                        intent.putExtra(TASK, task);
                        startActivity(intent);
                    }
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        filterStatus = 0;
        listTasks();
    }

    private void listTasks() {
        isTaskStarted = false;
        AndroidNetworking.get(Constant.URL_TASKS)
                .setOkHttpClient(LoginActivity.OK_HTTP_CLIENT)
                .build()
                .getAsObjectList(Task.class, new ParsedRequestListener<List<Task>>() {
                    @Override
                    public void onResponse(List<Task> list) {
                        progressDialog.dismiss();
                        listViewTasks.setVisibility(View.VISIBLE);
                        AsyncTask<List<Task>, Void, List<Task>> listVoidListAsyncTask = new AsyncTask<List<Task>, Void, List<Task>>() {
                            @Override
                            protected List<Task> doInBackground(List<Task>... params) {
                                List<Task> l = params[0];
                                List<Task> taskListAssigned = new ArrayList<Task>();
                                for (int i = 0; i < l.size(); i++) {
                                    Task t = l.get(i);
                                    if (t.getWorker().equals(LoginActivity.name)) {
                                        if (filterStatus != 0) {
                                            if (t.getStatus().equals(filterStatus)) {
                                                taskListAssigned.add(t);
                                            }
                                        } else {
                                            taskListAssigned.add(t);
                                        }
                                        if (t.getStatus().equals(2)) {
                                            isTaskStarted = true;
                                        }
                                    }
                                    ANRequest request = AndroidNetworking.get(Constant.URL_OBJECTS + "/name/" + t.getObject())
                                            .setOkHttpClient(LoginActivity.OK_HTTP_CLIENT).build();
                                    ANResponse<Object> response = request.executeForParsed(new TypeToken<Object>() {
                                    });
                                    if (response.isSuccess()) {
                                        Object o = response.getResult();
                                        t.setObjectAddress(o.getAddress());
                                    } else {
                                    }
                                }
                                return taskListAssigned;
                            }

                            @Override
                            protected void onPostExecute(List<Task> tasks) {
                                fillTaskListView(tasks);
                            }
                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);


                    }
                    @Override
                    public void onError(ANError error) {
                        try {
                            JSONObject errorJson = new JSONObject(error.getErrorBody());
                            Toast.makeText(getApplicationContext(), errorJson.getString("error"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {}
                    }
                });
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

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_LOCATION_REQUEST_CODE);
        } else {
            getLocation();
        }
    }

    private void updateLocationServer() {
        String lat = String.valueOf(mLastLocation.getLatitude());
        String lng = String.valueOf(mLastLocation.getLongitude());
        AndroidNetworking.put(Constant.URL_WORKERS + "/" + LoginActivity.ID)
                .setOkHttpClient(LoginActivity.OK_HTTP_CLIENT)
                .addQueryParameter("lat", lat)
                .addQueryParameter("lng", lng)
                .addQueryParameter("isactive", Boolean.TRUE.toString())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }

                    @Override
                    public void onError(ANError error) {
                        try {
                            JSONObject errorJson = new JSONObject(error.getErrorBody());
                            Toast.makeText(getApplicationContext(), errorJson.getString("error"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {}
                    }
                });
    }

    private void getLocation() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                updateLocationServer();
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        getLocation();
                } else {

                }
                return;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Toast.makeText(this, "Connection failed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
    }
}