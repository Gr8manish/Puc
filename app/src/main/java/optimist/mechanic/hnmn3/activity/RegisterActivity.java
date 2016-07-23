package optimist.mechanic.hnmn3.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import optimist.mechanic.hnmn3.puc.R;


public class RegisterActivity extends FragmentActivity implements
		OnClickListener {

	private Button btnLinkToLoginScreen;
	private Button register;
	private EditText email, password, etfname, etlname,license,mobile_no;
	private RadioGroup radioSexGroup;
	private RadioButton radioSexButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("yoyo", "RegisterActivityonCreate");
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);

		TextView main = (TextView) findViewById(R.id.textView1111);
		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"myfont.ttf");
		main.setTypeface(custom_font);

		btnLinkToLoginScreen = (Button) findViewById(R.id.btnLinkToLoginScreen);
		register = (Button) findViewById(R.id.btnregister);
		email = (EditText) findViewById(R.id.etemail);
		password = (EditText) findViewById(R.id.etpassword);
		etfname = (EditText) findViewById(R.id.etfname);
		etlname = (EditText) findViewById(R.id.etlname);
		license = (EditText) findViewById(R.id.etDL);
		mobile_no = (EditText) findViewById(R.id.etMobile);
		radioSexGroup = (RadioGroup) findViewById(R.id.radioGroup1);

		// Button click listeners
		btnLinkToLoginScreen.setOnClickListener(this);
		register.setOnClickListener(this);

		password.addTextChangedListener(new TextWatcher() {

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
					password.setText(result);
					password.setSelection(result.length());
					Toast.makeText(getBaseContext(), "space is not allowed..",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		etfname.addTextChangedListener(new TextWatcher() {

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
					etfname.setText(result);
					etfname.setSelection(result.length());
					Toast.makeText(getBaseContext(), "space is not allowed..",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		etlname.addTextChangedListener(new TextWatcher() {

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
					etlname.setText(result);
					etlname.setSelection(result.length());
					Toast.makeText(getBaseContext(), "space is not allowed..",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	// validate first name
	public static boolean validateFirstName(String firstName) {
		return firstName.matches("[a-zA-Z]*");
	} // end method validateFirstName

	// validate last name
	public static boolean validateLastName(String lastName) {
		return lastName.matches("[a-zA-z]+([ '-][a-zA-Z]+)*");
	}

	public static boolean isValidEmailAddress(String email) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}

	public static boolean passwordvalidation(String pass) {
		String ePattern = "(?=\\S+$).{5,15}";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(pass);
		return m.matches();
	}

	/**
	 * Button on click listener
	 * */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLinkToLoginScreen:
			Intent ii = new Intent(this, LoginActivity.class);
			startActivity(ii);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.btnregister:
			String fnames = etfname.getText().toString();
			String lnames = etlname.getText().toString();
			String names = fnames + " " + lnames;
			String emails = email.getText().toString();
			String pass = password.getText().toString();
			String license_no = license.getText().toString();
			String mobile = mobile_no.getText().toString();

			// get selected radio button from radioGroup
			int selectedId = radioSexGroup.getCheckedRadioButtonId();
			// find the radiobutton by returned id
			radioSexButton = (RadioButton) findViewById(selectedId);

			String gender = radioSexButton.getText().toString();

			boolean checkpass = passwordvalidation(pass);
			boolean checkname = validateFirstName(fnames)
					&& validateLastName(lnames);
			if (!fnames.equals("") && !lnames.equals("") && !emails.equals("")
					&& !pass.equals("") && !license_no.equals("") && !mobile.equals("")) {
				if (isValidEmailAddress(emails) && checkname && checkpass) {
					Intent in = new Intent(this,SendingData.class);
					in.putExtra("personName", names);
					in.putExtra("email", emails);
					in.putExtra("gender", gender);
					in.putExtra("password", pass);
					in.putExtra("license_no", license_no);
					in.putExtra("mobile", mobile);
					startActivity(in);
					finish();
				} else if (!checkname) {
					Toast.makeText(this, "Invalid Name..(Don't use space)",
							Toast.LENGTH_SHORT).show();
				} else if (!isValidEmailAddress(emails)) {
					Toast.makeText(this, "Invalid Email Id..",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "short password/contain Space..",
							Toast.LENGTH_SHORT).show();
				}
			} else if (fnames.equals("")) {
				Toast.makeText(this, "Please Enter Your First Name..",
						Toast.LENGTH_SHORT).show();
			} else if (lnames.equals("")) {
				Toast.makeText(this, "Please Enter Your last Name..",
						Toast.LENGTH_SHORT).show();
			}else if (license_no.equals("")) {
				Toast.makeText(this, "Please Enter License No..",
						Toast.LENGTH_SHORT).show();
			} else if (emails.equals("")) {
				Toast.makeText(this, "Please Enter email Id..",
						Toast.LENGTH_SHORT).show();
			}else if (mobile.equals("")) {
				Toast.makeText(this, "Please Enter Mobile number..",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Please Enter Your Password..",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
}
