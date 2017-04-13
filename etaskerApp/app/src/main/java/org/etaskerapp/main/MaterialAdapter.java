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

import org.etaskerapp.R;
import org.etaskerapp.model.Material;

import java.util.List;

public class MaterialAdapter extends ArrayAdapter<Material>  {
    private Context c;
    MaterialAdapter(Context c, List<Material> list) {
        super(c, R.layout.material_row, list);
        this.c = c;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.material_row, parent, false);

        final Material material = getItem(position);

        TextView materialName = (TextView)v.findViewById(R.id.materialName);
        materialName.setText(material.getName());
        TextView materialUnit = (TextView)v.findViewById(R.id.materialUnit);
        materialUnit.setText(material.getUnit());

        final TextView quantity = (TextView)v.findViewById(R.id.materialQuantity);

        ImageView remove = (ImageView)v.findViewById(R.id.materialRemoveImage);
        remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int q =  Integer.parseInt(quantity.getText().toString());
                    if (q > 0) {
                        quantity.setText(--q + "");
                        material.setQuantity((long)q);
                    }
                }
            }
        );
        int color = Color.parseColor("#189c21");
        remove.setColorFilter(color);
        ImageView add = (ImageView)v.findViewById(R.id.materialAddImage);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int q =  Integer.parseInt(quantity.getText().toString());
                quantity.setText(++q + "");
                material.setQuantity((long)q);
            }
        });
        add.setColorFilter(color);
        MaterialActivity.materials.put(material.getId(), material);
        return v;
    }
}
