package com.gomedia.mna.tools;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class MPlaySeekBarAsyncTask extends AsyncTask<String, Void, Void> {

    public static boolean isViewOn = true;
    Context context;
    String fileUrl;
    public ProgressBar progressBar;
    public SeekBar seekBar;
    public TextView textView;
    public MediaPlayer mediaPlayer;

    public MPlaySeekBarAsyncTask(Context context, ProgressBar progressBar, SeekBar seekBar, TextView textView, MediaPlayer mediaPlayer) {
        super();
        this.context = context;
        this.progressBar = progressBar;
        this.seekBar = seekBar;
        this.textView = textView;
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... files) {
        fileUrl = files[0];
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, FileIO.readFileFromSD(fileUrl));
            mediaPlayer.start();

            while (mediaPlayer.isPlaying() && isViewOn == true) {
                try {
                    // Realiza un retardo en el Seekbar
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
                publishProgress();
                // onProgressUpdate();
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {

        super.onProgressUpdate(values);

        progressBar.setMax(mediaPlayer.getDuration());
        progressBar.setProgress(mediaPlayer.getCurrentPosition());

        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        // Log.e("Media ", "ok " + );

        String s = "";
        s = String.format("%.2f", (((float) (mediaPlayer.getDuration() / 1000) / 60)));
        try {
            s = s.split(",")[0] + ":" + s.split(",")[1];
        } catch (Exception e) {
            // TODO: handle exception
        }

        String s1 = "";
        s1 = String.format("%.2f", (((float) (mediaPlayer.getCurrentPosition() / 1000) / 60)));
        try {
            s1 = s1.split(",")[0] + ":" + s1.split(",")[1];
        } catch (Exception e) {
            // TODO: handle exception
        }

        textView.setText(s1 + "/" + s);

    }

    @Override
    protected void onPostExecute(Void result) {

        Log.d("#Seek Bar Handler#", "#Destroyed#");
        super.onPostExecute(result);
    }
}