package com.example.inboxcubetestproject;

import java.util.ArrayList;

import com.google.gson.Gson;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	SharedPreferences sharedpreferences;
	public static final String MyPREFERENCES = "InboxCubeTestProject";
	private static final String TAG_USERINFO = "userInfo";	
	ArrayList<String> userInfoArrayList = new ArrayList<String>();
	Gson gson = new Gson();
	
	private ProgressDialog pDialog;	
	
	private EditText etUserName;
	private EditText etPassword;
	
	private String userName;
	private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
		        
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        
    }
    
    //login method
    public void onLoginClick(View view){
    	userName = etUserName.getText().toString();
    	password = etPassword.getText().toString();
    	if ((userName.equals(""))) {
			Toast.makeText(getApplicationContext(), "User name is empty",
					Toast.LENGTH_SHORT).show();
		} else if ((password.equals(""))) {
			Toast.makeText(getApplicationContext(), "Password is empty",
					Toast.LENGTH_SHORT).show();
		} else{
			new LoginTask().execute();
		}
    }
    
    //start register activity
    public void onRegisterClick(View view){
		Intent intent = new Intent(getApplicationContext(),
				RegisterActivity.class);		
		startActivity(intent);    	
    }
    
    //start find password activity
    public void onForgotPSWClick(View view){
    	Toast.makeText(getApplicationContext(), "Start forgot password activity",
				Toast.LENGTH_SHORT).show();
    }
    
    //async login task
    private class LoginTask extends AsyncTask<String, Void, String> {
    	int login = 0;
    	
    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setTitle("Contacting Servers");
			pDialog.setMessage("Logging in ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ArrayList<String> userInfoArray = new ArrayList<String>();
			userInfoArray.add(userName);
			userInfoArray.add(password);
			//convert user info array into json string
			String jsonUserInfo = gson.toJson(userInfoArray);
			
			String jsonUserInfoArrayList = sharedpreferences.getString(TAG_USERINFO, "");
			userInfoArrayList = gson.fromJson(jsonUserInfoArrayList, ArrayList.class);
			for (String s : userInfoArrayList){
				if(s.equals(jsonUserInfo)){
					login = 1;
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			pDialog.dismiss();
			if (login != 1) {
				Toast.makeText(getApplicationContext(), "User name or password is wrong",
						Toast.LENGTH_SHORT).show();
			} else{				
			//start search activity	
			Intent intent = new Intent(getApplicationContext(),
					SearchActivity.class);		
			startActivity(intent);
			}
		}
    	
    }
}
