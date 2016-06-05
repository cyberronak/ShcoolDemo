package com.example.controller;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.Handler.UserDataHandler;
import com.example.connection.AsyncInterface;
import com.example.connection.NetCheck;
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

public class LoginActivity extends AppCompatActivity implements AsyncInterface {
	private SharedPreferences shpref;
	private ProgressDialog pDialog;

	private EditText _emailText;
	private EditText _passwordText;
	private Button _loginButton;
	private TextView _signupLink;
	private String email, password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		_emailText = (EditText) findViewById(R.id.input_email);
		_passwordText = (EditText) findViewById(R.id.input_password);
		_loginButton = (Button) findViewById(R.id.btn_login);
		_signupLink = (TextView) findViewById(R.id.link_signup);

		shpref = getSharedPreferences(StringConst.My_PREFERENCES,
				Context.MODE_PRIVATE);
		String userFn = shpref.getString(StringConst.FIRSTNAME, "");
		String userLn = shpref.getString(StringConst.LASTNAME, "");
		String userEmail = shpref.getString(StringConst.EMAIL, "");
		String userUserName = shpref.getString(StringConst.USERNAME, "");
		String userDate = shpref.getString(StringConst.CREATED_AT, "");

		_loginButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// login();
				if (!validate()) {
					onLoginFailed();
					return;
				}

				pDialog = new ProgressDialog(v.getContext());
				pDialog.setMessage(StringConst.LOADING_MSG);
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();

				UserDataHandler handler = new UserDataHandler();
				ArrayList<NameValuePair> valuePairs = handler.loginUser(email,
						password);

				_loginButton.setEnabled(false);

				WebService service = new WebService(getApplicationContext(),
						StringConst.SIGN_IN, valuePairs);
				service.mListener = LoginActivity.this;
				service.execute();
			}
		});

		_signupLink.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Start the Signup activity
				Intent intent = new Intent(getApplicationContext(),
						SignupActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});

		if (ConstantUtility.notEmpty(userFn)
				&& ConstantUtility.notEmpty(userLn)
				&& ConstantUtility.notEmpty(userEmail)
				&& ConstantUtility.notEmpty(userUserName)
				&& ConstantUtility.notEmpty(userDate)) {
			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	public void onLoginSuccess() {
		_loginButton.setEnabled(true);
		pDialog.cancel();
		finish();
	}

	public void onLoginFailed() {
		Toast.makeText(getBaseContext(), StringConst.ERROR_SIGN_IN,
				Toast.LENGTH_LONG).show();
		_loginButton.setEnabled(true);
	}

	public boolean validate() {
		boolean valid = true;
		email = _emailText.getText().toString();
		password = _passwordText.getText().toString();

		if (email.isEmpty()
				|| !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
						.matches()) {
			_emailText.setError(StringConst.Valid_EMAIL);
			valid = false;
		} else {
			_emailText.setError(null);
		}

		if (password.isEmpty() || password.length() < 4
				|| password.length() > 10) {
			_passwordText.setError(StringConst.Valid_PASSWORD);
			valid = false;
		} else {
			_passwordText.setError(null);
		}

		return valid;
	}

	@Override
	public void asyncResult(Object result, String flag) {
		// TODO Auto-generated method stub
		if (flag.equals(StringConst.CONNECTION)) {
			if ((Boolean) result == true) {

			} else {
				Toast.makeText(this, StringConst.ERROR_ON_NETWORK,
						Toast.LENGTH_LONG).show();
				_loginButton.setEnabled(true);
				pDialog.cancel();
			}
		} else if (flag.equals(StringConst.SIGN_IN)) {
			String finalResult = (String) result;
			JSONObject json_data;
			try {
				json_data = new JSONObject(finalResult);
				String success = (json_data
						.getString(StringConst.RESPONSE_SUCCESS));
				if (Integer.parseInt(success) == 1) {
					Toast.makeText(this, StringConst.SUCCESS_SIGN_IN,
							Toast.LENGTH_SHORT).show();

					JSONObject json_user = json_data
							.getJSONObject(StringConst.USER_DATA);
					shpref = getSharedPreferences(StringConst.My_PREFERENCES,
							Context.MODE_PRIVATE);

					SharedPreferences.Editor editor = shpref.edit();

					editor.putString(StringConst.FIRSTNAME,
							json_user.getString(StringConst.FIRSTNAME));
					editor.putString(StringConst.LASTNAME,
							json_user.getString(StringConst.LASTNAME));
					editor.putString(StringConst.EMAIL,
							json_user.getString(StringConst.EMAIL));
					editor.putString(StringConst.USERNAME,
							json_user.getString(StringConst.USERNAME));
					editor.putString(StringConst.CREATED_AT,
							json_user.getString(StringConst.CREATED_AT));
					editor.commit();

					Intent upanel = new Intent(getApplicationContext(),
							MainActivity.class);
					upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(upanel);
					onLoginSuccess();
				} else {
					Toast.makeText(this, StringConst.NO_RESOURCE_FOUND,
							Toast.LENGTH_SHORT).show();
					_loginButton.setEnabled(true);
					pDialog.cancel();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
