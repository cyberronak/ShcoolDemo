package com.example.webservice;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.connection.AsyncInterface;
import com.example.utility.ConstantUtility;
import com.example.utility.StringConst;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class WebService extends AsyncTask<String, Void, String>
{

	private Context						mContext;
	public AsyncInterface				mListener	= null;
	private String						mFlag;
	private ArrayList<NameValuePair>	nameValuePairs;
	InputStream							is			= null;
	String								result		= null;
	String								line		= null;

	public WebService(Context contex, String flag, ArrayList<NameValuePair> valuePairs)
	{
		mContext = contex;
		mFlag = flag;
		nameValuePairs = valuePairs;
	}

	@Override
	protected String doInBackground(String... params)
	{
		// TODO Auto-generated method stub
		System.out.println(nameValuePairs.toString());
		try
		{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(ConstantUtility.getURL(mFlag));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			Log.e("pass 1", "connection success ");
		}
		catch (Exception e)
		{
			Log.e("Fail 1", e.toString());
			return StringConst.ERROR_ON_NETWORK;
		}

		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			Log.e("pass 2", "connection success ");
			return result;
		}
		catch (Exception e)
		{
			Log.e("Fail 2", e.toString());
			return StringConst.ERROR_ON_NETWORK;
		}
	}

	@Override
	protected void onPostExecute(String result)
	{
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		try
		{
			if (result != null)
			{
				Log.e("pass 2", "all data get");
				if(!result.contains(StringConst.ERROR_ON_NETWORK))
				{
					Log.e("result", result);
					Object obj = result;
					mListener.asyncResult(obj, mFlag);					
				}
				else
				{
					Object obj = false;
					mListener.asyncResult(obj, StringConst.CONNECTION);											
				}
					
			}
		}
		catch (Exception e)
		{
			Log.e("Fail 3", e.toString());
		}
	}
}
