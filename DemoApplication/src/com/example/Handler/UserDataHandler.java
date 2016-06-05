package com.example.Handler;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.example.utility.StringConst;

public class UserDataHandler
{
	public ArrayList<NameValuePair>	nameValuePairs;
	
	public UserDataHandler()
	{
		// TODO Auto-generated constructor stub
		nameValuePairs = new ArrayList<NameValuePair>();
	}

	// Login Authentication
	public ArrayList<NameValuePair> loginUser(String username, String password)
	{
		nameValuePairs.add(new BasicNameValuePair(StringConst.Flag, StringConst.SIGN_IN));
		nameValuePairs.add(new BasicNameValuePair(StringConst.EMAIL, username));
		nameValuePairs.add(new BasicNameValuePair(StringConst.PASSWORD, password));
		return nameValuePairs;
	}
	
	// Register user detailes
	public ArrayList<NameValuePair> registerUser(String firstname,String lastname,String email,String username, String password)
	{
		nameValuePairs.add(new BasicNameValuePair(StringConst.Flag, StringConst.SIGN_UP));
		nameValuePairs.add(new BasicNameValuePair(StringConst.FIRSTNAME, firstname));
		nameValuePairs.add(new BasicNameValuePair(StringConst.LASTNAME, lastname));
		nameValuePairs.add(new BasicNameValuePair(StringConst.EMAIL, email));
		nameValuePairs.add(new BasicNameValuePair(StringConst.USERNAME, username));
		nameValuePairs.add(new BasicNameValuePair(StringConst.PASSWORD, password));
		return nameValuePairs;
	}
}
