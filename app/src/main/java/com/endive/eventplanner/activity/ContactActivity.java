package com.endive.eventplanner.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.model.ApiClient;
import com.endive.eventplanner.model.ApiInterface;
import com.endive.eventplanner.pojo.ContactPojo;
import com.endive.eventplanner.util.ConnectionDetector;
import com.endive.eventplanner.util.EventDialogs;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by upasna.mishra on 11/6/2017.
 */

public class ContactActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_email, tv_phone_no, tv_address, tv_fax_no;
    private MapView mapView;
    private GoogleMap map;
    private Double lat;
    private Double lng;
    private ConnectionDetector cd;
    private EventDialogs dialog;
    private LinearLayout lin_phone_no, lin_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);
        setHeader(getResources().getString(R.string.contact_us));
        cd = new ConnectionDetector(this);
        dialog = new EventDialogs(this);
        getContact();
        mapView = (MapView) findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);

        mapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initialize();
        setListener();
    }

    private void initialize() {
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_phone_no = (TextView) findViewById(R.id.tv_phone_no);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_fax_no = (TextView) findViewById(R.id.tv_fax_no);
        lin_email = (LinearLayout) findViewById(R.id.lin_email);
        lin_phone_no = (LinearLayout) findViewById(R.id.lin_phone_no);

    }

    private void setListener() {
        lin_email.setOnClickListener(this);
        lin_phone_no.setOnClickListener(this);
    }

    private void getContact() {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog d = EventDialogs.showLoading(this);
            d.setCanceledOnTouchOutside(false);

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<ContactPojo> call = apiService.getContactDetails("Contact");
            call.enqueue(new Callback<ContactPojo>() {
                @Override
                public void onResponse(Call<ContactPojo> call, Response<ContactPojo> response) {
                    if (response.body() != null && response.body().getStatus_code().equals("1")) {
                        tv_email.setText(response.body().getData().getSupport_email());
                        tv_phone_no.setText(response.body().getData().getPhone_no());
                        tv_address.setText(response.body().getData().getAddress());
                        tv_fax_no.setText(response.body().getData().getFax_no());
                        lat = Double.parseDouble(response.body().getData().getAddress_latitude());
                        lng = Double.parseDouble(response.body().getData().getAddress_longitude());

                        mapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap mMap) {
                                map = mMap;

                                // For showing a move to my location button
                                if (ActivityCompat.checkSelfPermission(ContactActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ContactActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                map.setMyLocationEnabled(false);

                                // For dropping a marker at a point on the Map
                                LatLng latLng = new LatLng(lat, lng);
                                map.addMarker(new MarkerOptions().position(latLng));
                                //  map.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                                // For zooming automatically to the location of the marker
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
                                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }
                        });
                    } else {
                        dialog.displayCommonDialog(response.body().getError_message());
                    }
                    d.dismiss();
                }

                @Override
                public void onFailure(Call<ContactPojo> call, Throwable t) {
                    // Log error here since request failed
                    d.dismiss();
                }
            });
        } else {
            dialog.displayCommonDialog(getResources().getString(R.string.no_internet_connection));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_email:
                String email[] = {tv_email.getText().toString()};
                shareToGMail(email);
                break;
            case R.id.lin_phone_no:
                String number = tv_phone_no.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
                break;
        }
    }

    public void shareToGMail(String[] email) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.setType("text/plain");
        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        startActivity(emailIntent);
    }
}
