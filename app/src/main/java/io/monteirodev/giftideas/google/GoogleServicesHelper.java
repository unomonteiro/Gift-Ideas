package io.monteirodev.giftideas.google;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import io.monteirodev.giftideas.BuildConfig;

public class GoogleServicesHelper
        implements GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener {


    /* the are the only thing UI needs to know */
    public interface GoogleServicesListener {
        void onConnected();
        void onDisconnected();
    }

    private static final int REQUEST_CODE_RESOLUTION = -100;
    private static final int REQUEST_CODE_AVAILABILITY = -101;


    private Activity activity;
    private GoogleServicesListener listener;
    private GoogleApiClient apiClient;

    /* Constructor (activity, listener ) */
    public GoogleServicesHelper(Activity activity, GoogleServicesListener listener ) {
        this.activity = activity;
        this.listener = listener;
        /* (this) = GoogleServicesHelper
         * the one that actually implements this interface
         * we also need the Google ServicesHelper to
         * implement GoogleApiClient.ConnectionCallbacks,
         * and GoogleApiClient.OnConnectionFailedListener */
        this.apiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this) //
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API,
                        Plus.PlusOptions.builder()
                                .setServerClientId(BuildConfig.GOOGLE_PLUS_API)
                                .build())
                .build();
    }

    public void connect() {
        if(isGooglePlayServicesAvailable()){
            apiClient.connect();
        } else {
            listener.onDisconnected();
        }
    }

    public void disconnect() {
        if(isGooglePlayServicesAvailable()){
            apiClient.disconnect();
        } else {
            listener.onDisconnected();
        }
    }

    // http://stackoverflow.com/questions/31016722/googleplayservicesutil-vs-googleapiavailability
    private boolean isGooglePlayServicesAvailable(){
        // Deprecated
        // GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(activity);
        if(result != ConnectionResult.SUCCESS){
            if(googleAPI.isUserResolvableError(result)){
                googleAPI.getErrorDialog(
                        activity,
                        result,
                        REQUEST_CODE_AVAILABILITY)
                        .show();

            }
            return false;
        }
        return true;
    }

    // implements ConnectionCallbacks
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        listener.onConnected();

    }

    // implements ConnectionCallbacks
    @Override
    public void onConnectionSuspended(int i) {
        listener.onDisconnected();
    }

    // implements OnConnectionFailedListener
    /* this method result has */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if(connectionResult.hasResolution()){
            try {
                connectionResult.startResolutionForResult(activity,
                        REQUEST_CODE_RESOLUTION);
            } catch (IntentSender.SendIntentException e){
                connect(); // try again
            }
        } else {
            listener.onDisconnected();
        }
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_RESOLUTION || requestCode == REQUEST_CODE_AVAILABILITY){
            if(resultCode == Activity.RESULT_OK){
                connect();
            } else {
                listener.onDisconnected();
            }
        }
    }
}
