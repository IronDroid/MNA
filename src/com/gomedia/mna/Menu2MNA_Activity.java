package com.gomedia.mna;

import java.lang.reflect.Field;

import com.fk.coverflow.CoverFlow;
import com.fk.coverflow.ReflectingImageAdapter;
import com.fk.coverflow.ResourceImageAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import com.gomedia.mna.db.DBAdapter;
import com.gomedia.ws.ConsumeGps;

public class Menu2MNA_Activity extends Activity implements LocationListener {

    private TextView textView;
    DBAdapter dba;
    LocationManager locationManager;
    int[] stringMenu = {R.string.menu_txt_ingresar_codigo, R.string.menu_txt_leer_qr, R.string.menu_txt_ver_lista, R.string.menu_txt_acerca_mna,};
    private String android_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2_mna);

        dba = new DBAdapter(this);
        android_id = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);

        textView = (TextView) findViewById(R.id.textView1);
        final CoverFlow reflectingCoverFlow = (CoverFlow) findViewById(R.id.coverflow);
        setupCoverFlow(reflectingCoverFlow, true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000 * 60, 0, this);
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 10000 * 60, 0, this);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void setupCoverFlow(final CoverFlow mCoverFlow, final boolean reflect) {
        BaseAdapter coverImageAdapter = null;
        if (reflect) {
            coverImageAdapter = new ReflectingImageAdapter(new ResourceImageAdapter(this));
        } else {
            // coverImageAdapter = new ResourceImageAdapter(this);
        }
        mCoverFlow.setAdapter(coverImageAdapter);
        mCoverFlow.setSelection(0, true);
        setupListeners(mCoverFlow);
    }

    private void setupListeners(final CoverFlow mCoverFlow) {
        mCoverFlow.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                // textView.setText("Usted selecciono  : " +
                // stringMenu[position]);
                goFromMenuToActivity(position);
            }
        });
        mCoverFlow.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                textView.setText(getString(stringMenu[position]));
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
                // textView.setText("Nothing clicked!");
            }
        });
    }

    private void goFromMenuToActivity(int position) {

        switch (position) {
            case 1:
                startActivity(new Intent(this, TaskQRCode_Activity.class));
                break;
            case 2:
                startActivity(new Intent(this, ListMNA_Activity.class));
                break;
            case 3:
                startActivity(new Intent(this, About_Activity.class));
                break;
            default:
                startActivity(new Intent(this, CodeMNA_Activity.class));
                break;
        }

    }

    public static int getSmallestScreenWidthDp(Context context) {
        Resources resources = context.getResources();
        try {
            Field field = Configuration.class.getDeclaredField("smallestScreenWidthDp");
            return (Integer) field.get(resources.getConfiguration());
        } catch (Exception e) {
            // not perfect because reported screen size might not include status and
            // button bars
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            int smallestScreenWidthPixels = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
            return Math.round(smallestScreenWidthPixels / displayMetrics.density);
        }
    }

    public void onLocationChanged(Location location) {
        if (isOnline()) {
            new Asincrona(location.getLatitude() + "", location.getLongitude() + "").execute();
        } else {
            dba.open();
            dba.insertPos(android_id, location.getLatitude() + "", location.getLongitude() + "", false);
            dba.close();
        }
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onProviderDisabled(String provider) {
    }

    private class Asincrona extends AsyncTask<Void, Integer, Boolean> {

        ConsumeGps wsGps = new ConsumeGps();
        String respuesta = null;
        String lat;
        String lon;

        public Asincrona(String lat, String lon) {
            this.lat = lat;
            this.lon = lon;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean flag;
            try {
                respuesta = wsGps.gpsIn(android_id, lat, lon);
                flag = true;
            } catch (Exception ex) {
                flag = false;
            }
            return flag;
        }

        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Boolean result) {
        }
    }
}
