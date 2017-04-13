package org.etaskerapp.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.etaskerapp.R;
import org.etaskerapp.constant.Constant;
import org.etaskerapp.model.Material;
import org.etaskerapp.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MaterialActivity extends AppCompatActivity {

    private SwipeRefreshLayout mySwipeRefreshLayout;
    private ListView listViewMaterials;
    private ProgressDialog progressDialog;
    static Map<Long, Material> materials = new HashMap<>();
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_activity);

        Intent intent = getIntent();
        task = (Task)intent.getSerializableExtra(TaskListActivity.TASK);

        listViewMaterials= (ListView)findViewById(R.id.materialListView);

        progressDialog = new ProgressDialog(MaterialActivity.this, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        progressDialog.show();

        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshMaterial);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        listMaterials();
                    }
                }
        );

        listMaterials();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listMaterials();
    }

    private void makeToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    private void listMaterials() {
        AndroidNetworking.get(Constant.URL_MATERIALS)
                .setOkHttpClient(LoginActivity.OK_HTTP_CLIENT)
                .build()
                .getAsObjectList(Material.class, new ParsedRequestListener<List<Material>>() {
                    @Override
                    public void onResponse(List<Material> materials) {
                        fillMaterialListView(materials);
                    }
                    @Override
                    public void onError(ANError anError) {
                        makeToast(anError.getMessage());
                    }
                });
    }

    private void fillMaterialListView(List<Material> list) {
        MaterialAdapter listAdapter = new MaterialAdapter(getApplicationContext(), list);
        listViewMaterials.setAdapter(listAdapter);
        progressDialog.dismiss();
        mySwipeRefreshLayout.setRefreshing(false);

        findViewById(R.id.buttonSaveMaterial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                final Set<Long> keys = materials.keySet();
                for (Long key: keys) {
                    Material material = materials.get(key);
                    if (material.getQuantity() != null && material.getQuantity() > 0) {
                        AndroidNetworking.post(Constant.URL_MATERIALS + "/used/" + task.getId())
                                .setOkHttpClient(LoginActivity.OK_HTTP_CLIENT)
                                .addBodyParameter("quantity", material.getQuantity() + "")
                                .addBodyParameter("name", material.getName())
                                .addBodyParameter("unit", material.getUnit())
                                .addBodyParameter("price", material.getPrice().toString())
                                .addBodyParameter("location", task.getObject())
                                .build()
                                .getAsObject(Material.class, new ParsedRequestListener() {
                                    @Override
                                    public void onResponse(Object response) {

                                        finish();
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        makeToast(anError.getMessage());
                                    }
                                });
                    }
                }
            }
        });
    }
}
