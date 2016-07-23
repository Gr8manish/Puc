package optimist.mechanic.hnmn3.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import optimist.mechanic.hnmn3.puc.R;


public class SendingData extends Activity {

	private static String KEY_SUCCESS = "success";

	private static String KEY_ERROR = "error";

	String email, password, name, gender,mobile,license_no;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("yoyo", "onCreate");
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);

		TextView main = (TextView) findViewById(R.id.textView1111);
		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"myfont.ttf");
		main.setTypeface(custom_font);

		Intent intent = getIntent();
		name = intent.getStringExtra("personName");
		email = intent.getStringExtra("email");
		gender = intent.getStringExtra("gender");
		password = intent.getStringExtra("password");
		license_no = intent.getStringExtra("license_no");
		mobile = intent.getStringExtra("mobile");

		new ProcessRegister().execute();
	}

	private class ProcessRegister extends AsyncTask<String, String, JSONObject> {

		/**
		 * Defining Process dialog
		 **/
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			Log.d("yoyo", "ProcessRegisteronPostExecute");

			super.onPreExecute();
			pDialog = new ProgressDialog(SendingData.this);
			pDialog.setTitle("Contacting Servers");
			pDialog.setMessage("Registering ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			Log.d("yoyo", "ProcessRegisteronPostExecute");

			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.registerUser(name, license_no, email,gender, password,mobile);
			return json;

		}

		@Override
		protected void onPostExecute(JSONObject json) {
			Log.d("yoyo", "ProcessRegisteronPostExecute");
			/**
			 * Checks for success message.
			 **/
			try {
				if (json.getString(KEY_SUCCESS) != null) {

					String res = json.getString(KEY_SUCCESS);

					String red = json.getString(KEY_ERROR);

					if (Integer.parseInt(res) == 1) {
						pDialog.setTitle("Getting Data");
						pDialog.setMessage("Loading Info");

						Toast.makeText(SendingData.this,
								"Successfully Registered", Toast.LENGTH_SHORT)
								.show();

						DatabaseHandler db = new DatabaseHandler(
								getApplicationContext());

						/**
						 * Removes all the previous data in the SQlite database
						 **/
						UserFunctions logout = new UserFunctions();
						logout.logoutUser(getApplicationContext());
						db.addUser(license_no, password, "1");
						pDialog.dismiss();
						Intent myIntent = new Intent(SendingData.this,
								MainActivity.class);
						startActivity(myIntent);
						finish();

					}

					else if (Integer.parseInt(red) == 2) {
						pDialog.dismiss();

						Toast.makeText(SendingData.this, "User already exists",
								Toast.LENGTH_SHORT).show();
						Intent myIntent = new Intent(SendingData.this,
								RegisterActivity.class);
						startActivity(myIntent);
						finish();
					} else if (Integer.parseInt(red) == 3) {
						pDialog.dismiss();
						Toast.makeText(SendingData.this, "Invalid Email id",
								Toast.LENGTH_SHORT).show();
						Intent myIntent = new Intent(SendingData.this,
								RegisterActivity.class);
						startActivity(myIntent);
						finish();
					}else if(Integer.parseInt(red) == 5){
						pDialog.dismiss();
						Toast.makeText(SendingData.this, "Phone number already Exist",
								Toast.LENGTH_SHORT).show();
						Intent myIntent = new Intent(SendingData.this,
								RegisterActivity.class);
						startActivity(myIntent);
						finish();
					}

				}

				else {
					pDialog.dismiss();

					Toast.makeText(SendingData.this,
							"Error occured in registration", Toast.LENGTH_SHORT)
							.show();
				}

			} catch (JSONException e) {
				e.printStackTrace();

			}
		}
	}
}
