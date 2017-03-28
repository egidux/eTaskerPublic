package org.etaskerapp.main;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.etaskerapp.R;
import org.etaskerapp.constant.Constant;
import org.etaskerapp.model.Material;

import java.util.List;

public class UsedMaterialAdapter extends ArrayAdapter<Material>  {
    private Context c;
    private TaskStartedActivity activity;
    UsedMaterialAdapter(Context c, List<Material> list, TaskStartedActivity activity) {
        super(c, R.layout.material_row, list);
        this.c = c;
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.used_material_row, parent, false);

        final Material material = getItem(position);

            TextView materialName = (TextView)v.findViewById(R.id.usedMaterialName);
            materialName.setText(material.getName());
            TextView materialQuantity = (TextView)v.findViewById(R.id.usedMaterialQuantity);
            materialQuantity.setText(material.getQuantity() + "");


            ImageView remove = (ImageView)v.findViewById(R.id.usedMaterialRemoveImage);
            remove.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              AndroidNetworking.put(Constant.URL_MATERIALS + "/" + material.getId())
                                                      .setOkHttpClient(LoginActivity.OK_HTTP_CLIENT)
                                                      .addBodyParameter("quantity", "0")
                                                      .build()
                                                      .getAsObject(Material.class, new ParsedRequestListener() {
                                                          @Override
                                                          public void onResponse(Object response) {
                                                              activity.listUsedMaterials();
                                                          }

                                                          @Override
                                                          public void onError(ANError anError) {
                                                          }
                                                      });
                                          }
                                      }
            );


        return v;
    }
}
