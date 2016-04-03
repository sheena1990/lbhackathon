package gcm.play.android.samples.com.gcmquickstart;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;

/**
 * Created by Owner on 03-04-2016.
 */
public class GPSIntentService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private Location mLastLocation;

    public GPSIntentService() {
        super("GPSIntentService");
    }

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("Starting: ", "gpsIntentService");
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Integer msgId = sharedPreferences.getInt(QuickstartPreferences.MESSAGE_ID, 0);
        String token = sharedPreferences
                .getString(QuickstartPreferences.TOKEN, "");

        // Create an instance of GoogleAPIClient.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        while (true) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
//                return;
            }
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
            }
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
            try {
                Bundle data = new Bundle();
                data.putString("action", "coordinates");
                data.putString("latitude", ""+latitude);
                data.putString("longitude", ""+longitude);
                data.putString("token", token);
                msgId = sharedPreferences.getInt(QuickstartPreferences.MESSAGE_ID, 0);
                String id = Integer.toString(msgId);
                sharedPreferences.edit().putInt(QuickstartPreferences.MESSAGE_ID, msgId + 1).apply();
                gcm.send("1034234491032" + "@gcm.googleapis.com", id, data);
                Log.e("coordinates sent", "coords: " + latitude + longitude);
                Thread.sleep(10000, 0);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    static double latitude;
    static double longitude;

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
