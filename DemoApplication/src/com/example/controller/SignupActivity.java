package com.example.controller;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.Handler.UserDataHandler;
import com.example.connection.AsyncInterface;
import com.example.utility.ConstantUtility;
import com.example.utility.StringConst;
import com.example.webservice.WebService;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity implements AsyncInterface
{
	private WebService			service;
	private SharedPreferences	shpref;
	private ProgressDialog		pDialog;

	private EditText			_firstnameText;
	private EditText			_lastnameText;
	private EditText			_emailText;
	private EditText			_usernameText;
	private EditText			_passwordText;
	private Button				_signupButton;
	private TextView			_loginLink;
	private String				fname, lname, email, uname, password;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_signup);

		//opening transition animations
	    overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_transition);

		_firstnameText = (EditText) findViewById(R.id.input_firstname);
		_lastnameText = (EditText) findViewById(R.id.input_lastname);
		_emailText = (EditText) findViewById(R.id.input_email);
		_usernameText = (EditText) findViewById(R.id.input_username);
		_passwordText = (EditText) findViewById(R.id.input_password);
		_signupButton = (Button) findViewById(R.id.btn_signup);
		_loginLink = (TextView) findViewById(R.id.link_login);

		_signupButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				// signup();
				if (!validate())
				{
					onSignupFailed();
					return;
				}

				pDialog = new ProgressDialog(v.getContext());
				pDialog.setMessage(StringConst.LOADING_MSG);
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();

				UserDataHandler handler = new UserDataHandler();
				ArrayList<NameValuePair> valuePairs = handler.registerUser(fname, lname, email, uname, password);

				_signupButton.setEnabled(false);

				service = new WebService(getApplicationContext(), StringConst.SIGN_UP, valuePairs);
				service.mListener = SignupActivity.this;
				service.execute();
			}
		});

		_loginLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				// Finish the registration screen and return to the Login
				// activity
				Intent intent = new Intent(getApplicationContext(),
						LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				//closing transition animations
			    overridePendingTransition(R.anim.activity_open_transition,R.anim.activity_close_translate);
			    finishAfterTransition();
			}
		});

	}

	public void onSignupSuccess()
	{
		_signupButton.setEnabled(true);
		pDialog.cancel();
		Toast.makeText(this, StringConst.SUCCESS_SIGN_UP, Toast.LENGTH_SHORT).show();
		// Start the Signup activity
		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	public void onSignupFailed()
	{
		Toast.makeText(getBaseContext(), StringConst.ERROR_SIGN_UP, Toast.LENGTH_LONG).show();
		_signupButton.setEnabled(true);
	}

	public boolean validate()
	{
		boolean valid = true;

		fname = _firstnameText.getText().toString();
		lname = _lastnameText.getText().toString();
		email = _emailText.getText().toString();
		uname = _usernameText.getText().toString();
		password = _passwordText.getText().toString();

		if (fname.isEmpty() || fname.length() < 3)
		{
			_firstnameText.setError("at least 3 characters");
			valid = false;
		}
		else
		{
			_firstnameText.setError(null);
		}

		if (lname.isEmpty() || lname.length() < 3)
		{
			_lastnameText.setError("at least 3 characters");
			valid = false;
		}
		else
		{
			_lastnameText.setError(null);
		}

		if (email.isEmpty() || !ConstantUtility.emailValidator(email))
		{
			_emailText.setError("enter a valid email address");
			valid = false;
		}
		else
		{
			_emailText.setError(null);
		}

		if (uname.isEmpty() || uname.length() < 3)
		{
			_usernameText.setError("at least 3 characters");
			valid = false;
		}
		else
		{
			_usernameText.setError(null);
		}

		if (password.isEmpty() || password.length() < 4 || password.length() > 10)
		{
			_passwordText.setError("between 4 and 10 alphanumeric characters");
			valid = false;
		}
		else
		{
			_passwordText.setError(null);
		}

		return valid;
	}

	@Override
	public void asyncResult(Object result, String flag)
	{
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if (flag.equals(StringConst.CONNECTION))
		{
			if ((Boolean) result == true)
			{

			}
			else
			{
				Toast.makeText(this, StringConst.ERROR_ON_NETWORK, Toast.LENGTH_LONG).show();
				_signupButton.setEnabled(true);
				pDialog.cancel();
			}
		}
		else if (flag.equals(StringConst.SIGN_UP))
		{
			String finalResult = (String) result;
			JSONObject json_data;
			try
			{
				json_data = new JSONObject(finalResult);
				String success = (json_data.getString(StringConst.RESPONSE_SUCCESS));
				String error = (json_data.getString(StringConst.RESPONSE_ERROR));
				if (Integer.parseInt(success) == 1)
				{
					JSONObject json_user = json_data.getJSONObject(StringConst.USER_DATA);
					shpref = getSharedPreferences(StringConst.My_PREFERENCES, Context.MODE_PRIVATE);

					SharedPreferences.Editor editor = shpref.edit();

					editor.putString(StringConst.FIRSTNAME, json_user.getString(StringConst.FIRSTNAME));
					editor.putString(StringConst.LASTNAME, json_user.getString(StringConst.LASTNAME));
					editor.putString(StringConst.EMAIL, json_user.getString(StringConst.EMAIL));
					editor.putString(StringConst.USERNAME, json_user.getString(StringConst.USERNAME));
					editor.putString(StringConst.CREATED_AT, json_user.getString(StringConst.CREATED_AT));
					editor.commit();

					// On register success or failed
					// depending on success
					onSignupSuccess();
				}
				else if (Integer.parseInt(error) == 2)
				{
					_signupButton.setEnabled(true);
					pDialog.cancel();
					Toast.makeText(this, StringConst.USER_EXIST, Toast.LENGTH_SHORT).show();
				}
				else if (Integer.parseInt(error) == 3)
				{
					_signupButton.setEnabled(true);
					pDialog.cancel();
					Toast.makeText(this, StringConst.Valid_EMAIL, Toast.LENGTH_SHORT).show();
				}
				else
				{
					_signupButton.setEnabled(true);
					pDialog.cancel();
					Toast.makeText(this, StringConst.ERROR_SIGN_UP, Toast.LENGTH_SHORT).show();
				}
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				pDialog.cancel();
				e.printStackTrace();
			}
		}
	}
}
