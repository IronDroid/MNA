package com.gomedia.mna.tools;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.VideoView;

public class VideoSeekBarAsyncTask extends AsyncTask<String, Void, Void> {

	public static boolean isViewOn = true;
	Context context;
	String fileUrl;
	public SeekBar seekBar;
	public VideoView videoView;

	@Override
	protected void onPreExecute() {

		super.onPreExecute();
	}

	public VideoSeekBarAsyncTask(Context context, SeekBar seekBar, VideoView videoView) {
		super();
		this.context = context;
		this.seekBar = seekBar;
		this.videoView = videoView;
	}

	@Override
	protected Void doInBackground(String... files) {

		fileUrl = files[0];
		videoView.setVideoURI(FileIO.readFileFromSD(fileUrl));
		videoView.start();

		while (videoView.isPlaying() && isViewOn == true) {
			try {
				// Realiza un retardo en el Seekbar
				Thread.sleep(700);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			publishProgress();
			// onProgressUpdate();
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
		seekBar.setMax(videoView.getDuration());
		seekBar.setProgress(videoView.getCurrentPosition());
		// Log.e("Media ", "ok " + );
	}

	@Override
	protected void onPostExecute(Void result) {
		Log.d("#Seek Bar Handler#", "#Destroyed#");
		super.onPostExecute(result);
	}

}
