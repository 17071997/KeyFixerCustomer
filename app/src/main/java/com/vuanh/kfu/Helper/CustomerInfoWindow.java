package com.vuanh.kfu.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.vuanh.kfu.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomerInfoWindow implements GoogleMap.InfoWindowAdapter{
    private View myView;

    public CustomerInfoWindow(Context context){
        myView = LayoutInflater.from(context).inflate(R.layout.custom_customer_info_window,null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        TextView txtpickupTitle = ((TextView)myView.findViewById(R.id.txt_pickupinfo));
        txtpickupTitle.setText(marker.getTitle());
        TextView txtpickupSnippet = ((TextView)myView.findViewById(R.id.txt_pickupSnippet));
        txtpickupSnippet.setText(marker.getSnippet());

        return myView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
