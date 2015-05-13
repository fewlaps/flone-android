package com.fewlaps.flone.communication;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

public class Utilities {
		public static void playNotification(Activity myActivity) {
			Uri defaultRingtoneUri = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			try {
				MediaPlayer mediaPlayer = new MediaPlayer();
				mediaPlayer.setDataSource(myActivity.getBaseContext(),
						defaultRingtoneUri);
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
				mediaPlayer.prepare();
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

					public void onCompletion(MediaPlayer mp) {
						mp.release();
					}

				});
				mediaPlayer.start();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public static void showToast(final String toast,
				final Activity myNewActivity) {
			myNewActivity.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(myNewActivity.getBaseContext(), toast,
							Toast.LENGTH_SHORT).show();
				}
			});
		}
		
		public static void showToast(final String toast,
				final Activity myNewActivity, final int length) {
			myNewActivity.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(myNewActivity.getBaseContext(), toast,
							length).show();
				}
			});
		}
		
		public static double mapCons(double x, double in_min, double in_max, double out_min, double out_max)
		{
		  return constrain(map(x, in_min, in_max, out_min, out_max), out_min, out_max);
		}
		
		public static float mapCons(float x, float in_min, float in_max, float out_min, float out_max)
		{
		  return constrain(map(x, in_min, in_max, out_min, out_max), out_min, out_max);
		}
		
		public static double map(double x, double in_min, double in_max, double out_min, double out_max)
		{
		  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
		}
		
		public static float map(float x, float in_min, float in_max, float out_min, float out_max)
		{
		  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
		}
		
		public static double constrain(double x, double min, double max)
		{
			if(x < min) 
				return min;
			else if(x > max) 
				return max;
			else
				return x;
		}
		
		public static float constrain(float x, float min, float max)
		{
			if(x < min) 
				return min;
			else if(x > max) 
				return max;
			else
				return x;
		}
		
		public static boolean IsBoolean(Object newValue) {
			try {
				Boolean.parseBoolean(newValue.toString());
				return true;
			}
			catch(Exception e) {
				return false;
			}
		}
		
		public static boolean IsInteger(Object newValue) {
			try {
				Integer.parseInt(newValue.toString());
				return true;
			}
			catch(Exception e) {
				return false;
			}
		}
		
		public static boolean IsFloat(Object newValue) {
			try {
				Float.parseFloat(newValue.toString());
				return true;
			}
			catch(Exception e) {
				return false;
			}
		}
}
