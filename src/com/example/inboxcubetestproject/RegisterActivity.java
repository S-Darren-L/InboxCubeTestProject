package com.example.inboxcubetestproject;

import java.util.ArrayList;
import java.util.Set;

import com.google.gson.Gson;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	
	SharedPreferences sharedpreferences;
	public static final String MyPREFERENCES = "InboxCubeTestProject";
	private static final String TAG_USERINFO = "userInfo";
	ArrayList<String> userInfoArray = new ArrayList<String>();
	ArrayList<String> userInfoArrayList = new ArrayList<String>();
	Gson gson = new Gson();

	private ProgressDialog pDialog;
	
	private EditText etUserName;
	private EditText etPassword;
	private EditText etConfirmPassword;	

	private String userName;
	private String password;
	private String confirmPassword;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
		String jsonUserInfoArrayList = sharedpreferences.getString(TAG_USERINFO, "");
		userInfoArrayList = gson.fromJson(jsonUserInfoArrayList, ArrayList.class);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);

        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        
	}
	
	//register method
	public void onRegisterClick(View view){
    	userName = etUserName.getText().toString();
    	password = etPassword.getText().toString();
    	confirmPassword = etConfirmPassword.getText().toString();
		if ((userName.equals(""))) {
			Toast.makeText(getApplicationContext(), "User name is empty",
					Toast.LENGTH_SHORT).show();
		} else if ((password.equals(""))) {
			Toast.makeText(getApplicationContext(), "Password is empty",
					Toast.LENGTH_SHORT).show();
		} else if ((confirmPassword.equals(""))) {
			Toast.makeText(getApplicationContext(), "Confirm password is empty",
					Toast.LENGTH_SHORT).show();
		} else{
			if(password.equals(confirmPassword)){
				new RegisterTask().execute();				
			} else{
				Toast.makeText(getApplicationContext(), "Password is different",
						Toast.LENGTH_SHORT).show();
			}
		}		
	}
	
	//async register task
    private class RegisterTask extends AsyncTask<String, Void, String> {
    	
    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RegisterActivity.this);
			pDialog.setTitle("Contacting Servers");
			pDialog.setMessage("Logging in ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			//add created user info into array
			userInfoArray.add(userName);
			userInfoArray.add(password);
			Editor editor = sharedpreferences.edit();
			
			//convert user info array into json string
			String jsonUserInfo = gson.toJson(userInfoArray);
			//add json string user info into user info array list
			userInfoArrayList.add(jsonUserInfo);
			//convert user info array list into json string
			String jsonUserInfoArray = gson.toJson(userInfoArrayList);
			//put user info into shared preference
			editor.putString(TAG_USERINFO, jsonUserInfoArray);
			editor.commit();
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			pDialog.dismiss();
			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);		
			startActivity(intent);
		}
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
