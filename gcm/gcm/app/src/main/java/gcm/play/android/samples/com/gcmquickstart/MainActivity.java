/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gcm.play.android.samples.com.gcmquickstart;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressBar mRegistrationProgressBar;
    private TextView mInformationTextView;
    private boolean isReceiverRegistered;
    NotificationManager nMN;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private int msgId;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nMN = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification n = new Notification.Builder(this)
                .setContentTitle("Whip And Weep")
                .setContentText("Whip is On!")
                .setSmallIcon(R.drawable.emg)
                .build();
        nMN.notify(1, n);
        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                boolean isReg = sharedPreferences.getBoolean(QuickstartPreferences.IS_REGISTERED, false);
                if (sentToken) {
                    if (!isReg) {
                        startRegistration();
                        sharedPreferences.edit().putBoolean(QuickstartPreferences.IS_REGISTERED, true).apply();
                    }
                    mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
                    mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };
        mInformationTextView = (TextView) findViewById(R.id.informationTextView);

        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
// If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // permission was granted, yay! Do the
            // contacts-related task you need to do.
            Intent gpsIntent = new Intent(this, GPSIntentService.class);
            startService(gpsIntent);
        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    String token;
    void startRegistration() {
        Intent regIntent = new Intent(MainActivity.this, RegistrationActivity.class);
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
         token = sharedPreferences
                .getString(QuickstartPreferences.TOKEN, "");
        regIntent.putExtra("token", token);
        startActivity(regIntent);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        }
    }

    public void callEmergency(View view) {
        Bundle data = new Bundle();
        data.putString("action", "help");
        data.putString("latitude", ""+GPSIntentService.latitude);
        data.putString("longitude", ""+GPSIntentService.longitude);
        data.putString("token", token);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        msgId = sharedPreferences.getInt(QuickstartPreferences.MESSAGE_ID, 0);
        String id = Integer.toString(msgId);
        sharedPreferences.edit().putInt(QuickstartPreferences.MESSAGE_ID, msgId + 1).apply();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
        try {
            gcm.send("1034234491032" + "@gcm.googleapis.com", id, data);
            Log.e("Help request", "sent");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Help request", "error");
        }

//        Intent gpsIntent = new Intent(this, GPSIntentService.class);
//        startService(gpsIntent);
//        try {
//            Thread.sleep(20);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Log.e("Map","geo:"+GPSIntentService.longitude+","+GPSIntentService.latitude);
        Uri gmmIntentUri = Uri.parse("geo:"+GPSIntentService.longitude+","+GPSIntentService.latitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }

//        Uri gmmIntentUri = Uri.parse("google.streetview:cbll="+GPSIntentService.longitude+","+GPSIntentService.latitude);
//
//// Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
//        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//// Make the Intent explicit by setting the Google Maps package
//        mapIntent.setPackage("com.google.android.apps.maps");
//
//// Attempt to start an activity that can handle the Intent
//        startActivity(mapIntent);

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://gcm.play.android.samples.com.gcmquickstart/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://gcm.play.android.samples.com.gcmquickstart/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
