package com.gomedia.mna;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.gomedia.mna.db.DBAdapter;
import com.gomedia.ws.ConsumeGps;
import com.gomedia.ws.ConsumeObraMensaje;
import com.gomedia.ws.ConsumeObraVista;

public class About_Activity extends Activity {

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mna_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_down:
                onCreateDialog(1).show();
                return true;
            case R.id.action_upload:
                try {
                    new Asincrona().execute();
                    new AsincronaMensaje().execute();
                    new AsincronaTipo().execute();
                } catch (Exception e) {
                    Toast.makeText(this, "action " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected Dialog onCreateDialog(int id) {

        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_dialog_code);
        dialog.setTitle(getResources().getString(R.string.action_down));
        final TextView textView = (TextView) dialog.findViewById(R.id.textView1);
        final EditText editText = (EditText) dialog.findViewById(R.id.editText1);

        Button dialogButton = (Button) dialog.findViewById(R.id.buttonCancel);
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button buttonok = (Button) dialog.findViewById(R.id.buttonOK);
        buttonok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().length() == 0) {
                    textView.setText(getResources().getString(R.string.txt_in_code));
                } else if (editText.getText().toString().equals("777")) {
                    downData();
                    dialog.dismiss();
                } else {
                    textView.setText(getResources().getString(R.string.dialog_mensage_erro));
                }
            }
        });
        return dialog;
    }

    public void downData() {

        // declare the dialog as a member field of your activity

        // instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Descargando Contenido...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);

        // URL A DESCARGAR
        // AGREGAR WS

        //http://192.168.2.115/museo/ARCHIVO/65.jpg

        String server = "http://192.168.2.115/";
        String service1 = "museo/";
        String service2 = "museo/ARCHIVO/";
        String[] ur = {
            server + service1 + "SS501_Play.mp3",
            server + service2 + "40.jpg 	",
            server + service2 + "40.jpg 	",
            server + service2 + "40_CH.txt",
            server + service2 + "40_en.mp3",
            server + service2 + "40_en.txt",
            server + service2 + "en.txt",
            server + service2 + "40_es.mp3",
            server + service2 + "40_es.txt",
            server + service2 + "es.txt",
            server + service2 + "40_es.mp4",
            server + service2 + "40_it.txt ",
            server + service2 + "40_nn.txt ",
            server + service2 + "65.jpg 	",
            //            server + service2 + "65_en.mp3",
            //            server + service2 + "65_en.txt",

            server + service2 + "65_es.mp3",
            server + service2 + "65_es.txt",
            server + service2 + "65_es.mp4",
            server + service2 + "65_it.txt ",
            server + service2 + "65_nn.txt ",
            server + service2 + "60.jpg ",
            server + service2 + "80.jpg ",
            server + service2 + "idiomas.txt   ",
            server + service2 + "idiomasAgregar.txt",};

        // execute this when the downloader must be fired
        final DownloadDataTask downloadTask = new DownloadDataTask(this);
        // downloadTask.execute("http://192.168.0.100/ccsys.resources/SS501_BecauseI'mStupid.mp4");
        downloadTask.execute(ur);

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });

    }

    public class DownloadDataTask extends AsyncTask<String, Integer, String> {

        private Context context;

        public DownloadDataTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... urls) {

            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            wl.acquire();

            try {
                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;
                try {

                    for (String urlS : urls) {

                        URL url = new URL(urlS);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.connect();

                        Log.e("fer", url.getFile());

                        // expect HTTP 200 OK, so we don't mistakenly save error report
                        // instead of the file
                        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                            return "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
                        }

                        // this will be useful to display download percentage
                        // might be -1: server did not report the length
                        int fileLength = connection.getContentLength();

                        // download the file
                        String[] cleanURL = url.getFile().split("/");

                        // crear carpeta
                        File carpeta = new File("/sdcard/.mna/");
                        if (!carpeta.exists()) {
                            carpeta.mkdirs();
                        }

                        Log.e("fer", carpeta.getPath() + File.separator);

                        input = connection.getInputStream();
                        output = new FileOutputStream(carpeta.getPath() + File.separator + cleanURL[cleanURL.length - 1]);

                        byte data[] = new byte[4096];
                        long total = 0;
                        int count;
                        while ((count = input.read(data)) != -1) {
                            // allow canceling with back button
                            if (isCancelled()) {
                                return null;
                            }
                            total += count;
                            // publishing the progress....
                            if (fileLength > 0) // only if total length is known
                            {
                                publishProgress((int) (total * 100 / fileLength));
                            }
                            output.write(data, 0, count);
                        }
                    }

                } catch (Exception e) {
                    return e.toString();
                } finally {
                    try {
                        if (output != null) {
                            output.close();
                        }
                        if (input != null) {
                            input.close();
                        }
                    } catch (IOException ignored) {
                    }

                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            } finally {
                wl.release();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mProgressDialog.dismiss();
//            startActivity(new Intent(Menu2MNA_Activity.this, Language_Activity.class));
//            Menu2MNA_Activity.this.finish();

            if (result != null) {
                Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Archivos Descargados Correctamente", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class Asincrona extends AsyncTask<Void, Integer, Boolean> {

        ConsumeGps wsGps = new ConsumeGps();
        DBAdapter dba;

        public Asincrona() {
            dialogo("Localización");
            dba = new DBAdapter(getApplicationContext());
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean flag = false;
            try {
                dba.open();
                Cursor obraPos = dba.getPos();
                Toast.makeText(getApplicationContext(), obraPos.getCount(), Toast.LENGTH_SHORT).show();
                while (obraPos.moveToNext()) {
                    int id = obraPos.getInt(0);
                    String imei = obraPos.getString(1);
                    String lat = obraPos.getString(2);
                    String lon = obraPos.getString(3);
                    wsGps.gpsIn(imei, lat, lon);
                    dba.updatePos(id);
                }
                dba.close();
                flag = true;
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "gps" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                flag = false;
            }
            return flag;
        }

        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
            pDialog.setProgress(progreso);
        }

        @Override
        protected void onPreExecute() {
            pDialog.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Asincrona.this.cancel(true);
                }
            });
            pDialog.setProgress(0);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pDialog.dismiss();
            if (!result) {
                Toast.makeText(getApplicationContext(), "No se puedo sincronizar.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class AsincronaMensaje extends AsyncTask<Void, Integer, Boolean> {

        ConsumeObraMensaje wsGps = new ConsumeObraMensaje();
        DBAdapter dba;

        public AsincronaMensaje() {
            dialogo("Mensajes");
            dba = new DBAdapter(getApplicationContext());
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean flag = false;
            try {
                dba.open();
                Cursor obraMensaje = dba.getObraMensaje();
                Toast.makeText(getApplicationContext(), obraMensaje.getCount(), Toast.LENGTH_SHORT).show();
                while (obraMensaje.moveToNext()) {
                    int id = obraMensaje.getInt(0);
                    String imei = obraMensaje.getString(1);
                    String codObra = obraMensaje.getString(2);
                    String mensaje = obraMensaje.getString(3);
                    wsGps.mensajeObraIn(imei, codObra, mensaje);
                    dba.updateObraMensaje(id);
                }
                dba.close();
                flag = true;
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "msj " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                flag = false;
            }
            return flag;
        }

        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
            pDialog.setProgress(progreso);
        }

        @Override
        protected void onPreExecute() {
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    AsincronaMensaje.this.cancel(true);
                }
            });

            pDialog.setProgress(0);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pDialog.dismiss();
        }
    }

    private class AsincronaTipo extends AsyncTask<Void, Integer, Boolean> {

        ConsumeObraVista wsTip = new ConsumeObraVista();
        DBAdapter dba;

        public AsincronaTipo() {
            dialogo("Tipos");
            dba = new DBAdapter(getApplicationContext());
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean flag = false;
            try {
                dba.open();
                Cursor obraVista = dba.getObraVista();
                Toast.makeText(getApplicationContext(), obraVista.getCount(), Toast.LENGTH_SHORT).show();
                while (obraVista.moveToNext()) {
                    int id = obraVista.getInt(0);
                    String imei = obraVista.getString(1);
                    String tipo = obraVista.getString(2);
                    String codObra = obraVista.getString(3);
                    wsTip.obraVisualizadaIn(imei, codObra, tipo);
                    dba.updateObraMensaje(id);
                }
                dba.close();
                flag = true;

            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "view " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                flag = false;
            }
            return flag;
        }

        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
            pDialog.setProgress(progreso);
        }

        @Override
        protected void onPreExecute() {
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    AsincronaTipo.this.cancel(true);
                }
            });

            pDialog.setProgress(0);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pDialog.dismiss();
        }
    }
    private ProgressDialog pDialog;

    private void dialogo(String msn) {
        pDialog = new ProgressDialog(getApplicationContext());
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Sincronizando " + msn + "....");
        pDialog.setCancelable(false);
    }
}
