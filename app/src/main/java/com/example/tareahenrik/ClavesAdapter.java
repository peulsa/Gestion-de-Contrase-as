package com.example.tareahenrik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;
import java.util.List;

public class ClavesAdapter extends BaseAdapter {

    private Context context;
    private List<Clave> clavesList;

    public ClavesAdapter(Context context, List<Clave> clavesList) {
        this.context = context;
        this.clavesList = clavesList;
    }

    @Override
    public int getCount() {
        return clavesList.size();
    }

    @Override
    public Object getItem(int position) {
        return clavesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_clave, parent, false);
        }

        // Obtener el objeto Clave de la lista
        Clave clave = (Clave) getItem(position);

        // Enlazar los valores de la clave con el diseño
        TextView claveTextView = convertView.findViewById(R.id.claveTextView);
        TextView origenTextView = convertView.findViewById(R.id.origenTextView);

        // Asignar la clave y el userName a sus respectivos TextViews
        claveTextView.setText(clave.getClave());  // Aquí se muestra la clave
        origenTextView.setText(clave.getUserName());  // Aquí se muestra el userName

        return convertView;
    }
}
