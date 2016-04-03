package gcm.play.android.samples.com.gcmquickstart;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class RegistrationActivity extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    String phoneNo;
    String imeiNo;
    String token;
    TelephonyManager tMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        firstName = (EditText) findViewById(R.id.editText);
        lastName = (EditText) findViewById(R.id.editText2);

        tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        getPermissions();

        Log.e("Reterived", "data");
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
//        sendData(firstName.getText().toString(),lastName.getText().toString(),phoneNo,imeiNo,token);

    }

    final int REQUEST_READ_PHONE_STATE = 1;

    void getPermissions() {

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_READ_PHONE_STATE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    phoneNo = tMgr.getLine1Number();
                    imeiNo = tMgr.getDeviceId();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    public void sendData(View view) {
        String[] params = new String[5];
        params[0] = firstName.getText().toString();
        params[1] = lastName.getText().toString();
        params[2] = imeiNo;
        params[3] = phoneNo;
        params[4] = token;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String[] params) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Integer msgId = sharedPreferences.getInt(QuickstartPreferences.MESSAGE_ID, 0);
                String msg = "";
                GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                try {
                    Bundle data = new Bundle();
                    data.putString("action","register");
                    data.putString("first_name", params[0]);
                    data.putString("last_name", params[1]);
                    data.putString("imeiNo", params[2]);
                    data.putString("phoneNo", params[3]);
                    data.putString("token", params[4]);
                    String id = Integer.toString(msgId);
                    sharedPreferences.edit().putInt(QuickstartPreferences.MESSAGE_ID, msgId + 1).apply();
                    gcm.send("1034234491032" + "@gcm.googleapis.com", id, data);
                    msg = "Sent message";
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.e("Message", msg);
                finish();
            }
        }.execute(params);
    }
}
