package optimist.mechanic.hnmn3.activity;

import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserFunctions {

    private JSONParser jsonParser;
    private static String registerURL = "http://smartlogicks.16mb.com/Puc/";


    private static String register_tag = "register";
    private static String login_tag = "login";

    // constructor
    public UserFunctions() {
        jsonParser = new JSONParser();
    }

    public JSONObject registerUser(String name, String license_no, String email,
                                   String gender, String password, String mobile) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("license_no", license_no));
        params.add(new BasicNameValuePair("mobile", mobile));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("gender", gender));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        return json;
    }


    public boolean logoutUser(Context context) {
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

    public JSONObject loginUser(String license, String password) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("license", license));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        return json;
    }
}
