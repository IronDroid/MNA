package com.gomedia.mna;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.gomedia.mna.db.DBAdapter;

import com.gomedia.mna.tools.FileIO;
import com.gomedia.mna.tools.MPlaySeekBarAsyncTask;
import com.gomedia.ws.ConsumeObraMensaje;
import com.gomedia.ws.ConsumeObraVista;
import java.io.File;

public class PropertiesMNA_Activity extends Activity {

    GlobalDataApplication global;
    ProgressBar progressBar;
    SeekBar seekBar;
    TextView textView_media_time;
    MPlaySeekBarAsyncTask mPlayAsync;
    int tipo;
    private TextView videoTime;
    private ImageButton videoPlay;
    private ImageButton videoStop;
    private VideoView videoView;
    private SeekBar seekVideoBar;
    private boolean swVideo;
    private DBAdapter dba;
    private String android_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properties_mna);

        dba = new DBAdapter(getApplicationContext());
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        global = (GlobalDataApplication) getApplication();
        tipo = getIntent().getIntExtra("tipo", 0);

        ImageView imageView = (ImageView) findViewById(R.id.imageViewObra);
        imageView.setImageBitmap(FileIO.readImageFromSD("/.mna/" + global.codeObra + ".jpg"));

        TextView textViewContents = (TextView) findViewById(R.id.textViewContents);
        textViewContents.setText(FileIO.readTextFromSD("/.mna/" + global.codeObra + "_" + global.idLang + ".txt"));

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        seekBar.setOnSeekBarChangeListener(new SeekBarHandler());

        textView_media_time = (TextView) findViewById(R.id.textView_media_time);

        mPlayAsync = new MPlaySeekBarAsyncTask(this, progressBar, seekBar, textView_media_time, mediaPlayer);

        swVideo = true;
        videoTime = (TextView) findViewById(R.id.textView_media_time_video);
        videoPlay = (ImageButton) findViewById(R.id.buttonPlayVideo);
        videoPlay.setImageResource(R.drawable.media_play);
        videoStop = (ImageButton) findViewById(R.id.buttonStopVideo);
        videoStop.setImageResource(R.drawable.media_stop);
        seekVideoBar = (SeekBar) findViewById(R.id.seekBarVideo);
        seekVideoBar.setOnSeekBarChangeListener(new SeekVideoBarHandler());
        videoView = (VideoView) findViewById(R.id.videoView1);
        videoView.setVideoURI(readFile("/.mna/" + global.codeObra + "_" + global.idLang + ".mp4"));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {

                seekVideoBar.setMax(mp.getDuration());
                seekVideoBar.postDelayed(onEverySecond, 7000);
                videoTime.setText(convertTimeFormat(videoView.getCurrentPosition())
                        + "/" + convertTimeFormat(videoView.getDuration()));
            }
        });

        if (isOnline()) {
            new AsincronaTipo().execute();
        } else {
            dba.open();
            dba.insertObraVista(android_id, tipo, global.codeObra, false);
            Toast.makeText(getApplicationContext(), "insert DB obra vista ", Toast.LENGTH_SHORT).show();
            dba.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.properties_mna, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.comentario:
                onCreateDialog().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    protected Dialog onCreateDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_dialog_sumit);
        dialog.setTitle(R.string.sumit_title);
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
                if (editText.getText().toString().length() != 0) {
                    dialog.dismiss();
                    if (isOnline()) {
                        new AsincronaMensaje(editText.getText().toString()).execute();
                    } else {
                        dba.open();
                        dba.insertObraMensaje(android_id, Integer.parseInt(global.codeObra), editText.getText().toString(), false);
                        Toast.makeText(getApplicationContext(), "insert DB obra vista ", Toast.LENGTH_SHORT).show();
                        dba.close();
                    }
                }
            }
        });
        return dialog;
    }

    public Uri readFile(String fileDir) {
        Uri media = Uri.parse(Environment.getExternalStorageDirectory().getPath() + fileDir);
        return media;
    }

    public String convertTimeFormat(int time) {
        String s = String.format("%.2f", time * (1f / 1000f) * (1f / 60f));
        s = s.replace(",", ":");
        return s;
    }
    MediaPlayer mediaPlayer;
    ImageButton buttonPlay;
    ImageButton buttonStop;
    boolean sw = true;

    public void buttonMediaPlay(View view) {
        File extStore = Environment.getExternalStorageDirectory();
        File myFile = new File(extStore.getAbsolutePath() + "/.mna/" + global.codeObra + "_" + global.idLang + ".mp3");
        if (!myFile.exists()) {
            return;
        }
        try {
            buttonPlay = (ImageButton) findViewById(R.id.buttonPlay);
            buttonStop = (ImageButton) findViewById(R.id.buttonStop);
            if (sw) {
                buttonPlay.setImageResource(R.drawable.media_pause);
                buttonStop.setImageResource(R.drawable.media_stop);
                MPlaySeekBarAsyncTask.isViewOn = true;
                sw = false;
                mPlayAsync = new MPlaySeekBarAsyncTask(this, progressBar, seekBar, textView_media_time, mPlayAsync.mediaPlayer);
                // mPlayAsync.execute("/SS501_UR_Man.mp3");
                mPlayAsync.execute("/.mna/" + global.codeObra + "_" + global.idLang + ".mp3");
            } else {
                buttonPlay.setImageResource(R.drawable.media_play);
                MPlaySeekBarAsyncTask.isViewOn = false;
                sw = true;
                mPlayAsync.mediaPlayer.pause();
            }
        } catch (IllegalStateException ise) {
            Toast.makeText(this, "ISE: " + ise.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "E: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void buttonMediaStop(View view) {
        try {
            mPlayAsync.mediaPlayer.stop();
            mPlayAsync.cancel(true);
            mPlayAsync = new MPlaySeekBarAsyncTask(this, progressBar, seekBar, textView_media_time, mediaPlayer);
            sw = true;
            buttonPlay.setImageResource(R.drawable.media_play);
            buttonStop = (ImageButton) findViewById(R.id.buttonStop);
            buttonStop.setImageResource(R.drawable.media_stop_pressed);
        } catch (Exception e) {
        }
    }

    private class SeekBarHandler implements OnSeekBarChangeListener {

        public SeekBarHandler() {
            super();
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                if (mPlayAsync.mediaPlayer != null) {
                    mPlayAsync.mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    private class SeekVideoBarHandler implements OnSeekBarChangeListener {

        public SeekVideoBarHandler() {
            super();
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                videoView.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }
    private Runnable onEverySecond = new Runnable() {
        @Override
        public void run() {
            if (seekVideoBar != null) {
                seekVideoBar.setProgress(videoView.getCurrentPosition());
            } else if (videoView.isPlaying()) {
                seekVideoBar.postDelayed(onEverySecond, 1000);
            } else {
                videoTime.setText("0:00/0:00");
            }
            videoTime.setText(convertTimeFormat(videoView.getCurrentPosition()) + "/" + convertTimeFormat(videoView.getDuration()));
        }
    };

    public void buttonMediaPlayVideo(View view) {
        videoStop.setImageResource(R.drawable.media_stop);
        if (swVideo) {
            videoPlay.setImageResource(R.drawable.media_pause);
            videoView.start();
            swVideo = false;
        } else {
            videoView.pause();
            videoPlay.setImageResource(R.drawable.media_play);
            swVideo = true;
        }
    }

    public void buttonMediaStopVideo(View view) {
        videoPlay.setImageResource(R.drawable.media_play);
        videoStop.setImageResource(R.drawable.media_stop_pressed);
        videoView.stopPlayback();
        videoView.setVideoURI(readFile("/.mna/" + global.codeObra + "_" + global.idLang + ".mp4"));
        swVideo = true;
    }

    @Override
    protected void onPause() {
        MPlaySeekBarAsyncTask.isViewOn = false;
        try {
            mPlayAsync.mediaPlayer.pause();
        } catch (Exception e) {
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        MPlaySeekBarAsyncTask.isViewOn = true;
        // new SeekBarHandler(this).execute();
        // mediaPlayer.start();
        super.onResume();
    }

    private class AsincronaTipo extends AsyncTask<Void, Integer, Boolean> {

        ConsumeObraVista wsTip = new ConsumeObraVista();
        String respuesta = null;

        public AsincronaTipo() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean flag = false;
            try {
                respuesta = wsTip.obraVisualizadaIn(android_id, global.codeObra, tipo + "");
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

    private class AsincronaMensaje extends AsyncTask<Void, Integer, Boolean> {

        private ConsumeObraMensaje wsGps = new ConsumeObraMensaje();
        private String msj;

        public AsincronaMensaje(String msj) {
            this.msj = msj;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean flag = false;
            try {
                wsGps.mensajeObraIn(android_id, global.codeObra, msj);
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

    public void fullScreen(View view) {
        videoView.layout(0, 500, 0, 300);
    }
}