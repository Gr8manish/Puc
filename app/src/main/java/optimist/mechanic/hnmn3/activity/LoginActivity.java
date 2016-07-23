package optimist.mechanic.hnmn3.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import optimist.mechanic.hnmn3.puc.R;


public class LoginActivity extends Activity implements OnClickListener {

	Button btnlinktoregister, btnlogin,bSkip;
	EditText input_LN;
	EditText inputPassword;

	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		TextView main = (TextView) findViewById(R.id.textView111);
		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"myfont.ttf");
		main.setTypeface(custom_font);

		btnlinktoregister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
		btnlogin = (Button) findViewById(R.id.btnLogin);
		bSkip = (Button) findViewById(R.id.bSkip);
		bSkip.setOnClickListener(this);
		btnlinktoregister.setOnClickListener(this);
		btnlogin.setOnClickListener(this);
		input_LN = (EditText) findViewById(R.id.et_license);
		inputPassword = (EditText) findViewById(R.id.password);

		input_LN.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String result = s.toString().replaceAll(" ", "");
				if (!s.toString().equals(result)) {
					input_LN.setText(result);
					input_LN.setSelection(result.length());
					Toast.makeText(getBaseContext(), "space is not allowed..",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		inputPassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String result = s.toString().replaceAll(" ", "");
				if (!s.toString().equals(result)) {
					inputPassword.setText(result);
					inputPassword.setSelection(result.length());
					Toast.makeText(getBaseContext(), "space is not allowed..",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnLinkToRegisterScreen:
			startActivity(new Intent(this, RegisterActivity.class));
			overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
			break;
		case R.id.bSkip:
			Toast.makeText(LoginActivity.this, "Skip clicked", Toast.LENGTH_SHORT).show();
			Intent p = new Intent(this,MainActivity.class);
			startActivity(p);
		break;
		case R.id.btnLogin:
			String s1,
			s2;
			s1 = input_LN.getText().toString();
			s2 = inputPassword.getText().toString();
			if ((!s1.equals("")) && (!s2.equals(""))) {
				if (s2.length() > 5 ) {
					NetAsync(v);
				} else {
					if (s2.length() > 5) {
						Toast.makeText(getBaseContext(), "Invalid License number",
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getBaseContext(), "Short Password",
								Toast.LENGTH_LONG).show();
					}
				}
			} else if ((!s1.equals(""))) {
				Toast.makeText(getApplicationContext(), "Password field empty",
						Toast.LENGTH_SHORT).show();
			} else if ((!s2.equals(""))) {
				Toast.makeText(getApplicationContext(), "License number field empty",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"License number and Password field are empty",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	/**
	 * Async Task to check whether internet connection is working.
	 **/

	private class NetCheck extends AsyncTask<String, String, Boolean> {
		private ProgressDialog nDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			nDialog = new ProgressDialog(LoginActivity.this);
			nDialog.setTitle("Checking Network");
			nDialog.setMessage("Loading..");
			nDialog.setIndeterminate(false);
			nDialog.setCancelable(false);
			nDialog.show();
		}

		/**
		 * Gets current device state and checks for working internet connection
		 * by trying Google.
		 **/
		@Override
		protected Boolean doInBackground(String... args) {

			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
		protected void onPostExecute(Boolean th) {

			if (th == true) {
				nDialog.dismiss();
				new ProcessLogin().execute();
			} else {
				nDialog.dismiss();
				Toast.makeText(getBaseContext(), "Error in Network Connection",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * Async Task to get and send data to My Sql database through JSON respone.
	 **/
	private class ProcessLogin extends AsyncTask<String, String, JSONObject> {

		private ProgressDialog pDialog;

		String  password,license;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();


			license = input_LN.getText().toString();
			password = inputPassword.getText().toString();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setTitle("Contacting Servers");
			pDialog.setMessage("Logging in ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {

			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.loginUser(license, password);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					String red = json.getString(KEY_ERROR);

					if (Integer.parseInt(res) == 1) {
						pDialog.setMessage("Loading User Space");
						pDialog.setTitle("Getting Data");
						DatabaseHandler db = new DatabaseHandler(
								getApplicationContext());

						/**
						 * Clear all previous data in SQlite database.
						 **/
						UserFunctions logout = new UserFunctions();
						logout.logoutUser(getApplicationContext());
						db.addUser(license, password, "1");

						pDialog.dismiss();

						/**
						 * Hashmap to load data from the Sqlite database
						 **/

						HashMap<String, String> user = new HashMap<String, String>();
						user = db.getUserDetails();
						/*Toast.makeText(
								getBaseContext(),
								"Username :" + user.get("username")
										+ "\npassword : "
										+ user.get("password") + "\nISlogin : "
										+ user.get("ISlogin"),
								Toast.LENGTH_LONG).show();*/
						finish();
						Intent p = new Intent(LoginActivity.this,MainActivity.class);
						startActivity(p);

					} else if(Integer.parseInt(red) == 1) {

						pDialog.dismiss();
						Toast.makeText(getBaseContext(),
								"Incorrect License number/password",
								Toast.LENGTH_LONG).show();

					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void NetAsync(View view) {
		new NetCheck().execute();
	}
}
