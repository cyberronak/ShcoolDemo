package com.example.connection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.example.utility.StringConst;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

public class NetCheck extends AsyncTask<String, Void, Boolean> {

	private Context mContext;
	private AsyncInterface mListener;
	
	public NetCheck(Context contex) {
		mContext = contex;
	}

	private ProgressDialog nDialog;

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		nDialog = new ProgressDialog(mContext);
		nDialog.setTitle("Checking Network");
		nDialog.setMessage("Loading..");
		nDialog.setIndeterminate(false);
		nDialog.setCancelable(true);
		nDialog.show();
	}

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			try {
				URL url = new URL("http://www.google.com");
				HttpURLConnection urlc = (HttpURLConnection) url
						.openConnection();
				urlc.setConnectTimeout(3000);
				urlc.connect();
				if (urlc.getResponseCode() == 200) {
					return true;
				}
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

			nDialog.dismiss();
			mListener.asyncResult(true,StringConst.CONNECTION);
	}
}
