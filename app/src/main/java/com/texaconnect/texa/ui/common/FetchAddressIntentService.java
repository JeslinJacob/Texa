package com.texaconnect.texa.ui.common;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.texaconnect.texa.R;
import com.texaconnect.texa.util.Constantz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.texaconnect.texa.util.Constantz.LOCATION_DATA_EXTRA;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FetchAddressIntentService extends IntentService {
    public static final String TAG = "FetchAddressService";
    private static final String ACTION_FETCH_ADDRESS = "com.texaconnect.texa.ui.common.action.FETCH_ADDRESS";

    private static final String EXTRA_PARAM_LOCATION = "com.texaconnect.texa.ui.common.extra.LOCATION";
    private static final String EXTRA_PARAM_LATITUDE = "com.texaconnect.texa.ui.common.extra.LATITUDE";
    private static final String EXTRA_PARAM_LONGITUDE = "com.texaconnect.texa.ui.common.extra.LONGITUDE";

    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    /**
     * Starts this service to perform action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startFetchAddressService(Context context, ResultReceiver receiver, Location location) {
        Intent intent = new Intent(context, FetchAddressIntentService.class);
        intent.putExtra(Constantz.RESULT_RECEIVER, receiver);
        intent.setAction(ACTION_FETCH_ADDRESS);
        intent.putExtra(LOCATION_DATA_EXTRA, location);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {

            final String action = intent.getAction();
            if (ACTION_FETCH_ADDRESS.equals(action)) {

                handleAction(intent);
            }
        }
    }

    /**
     * Handle action in the provided background thread with the provided
     * parameters.
     */
    private void handleAction(Intent intent) {
        ResultReceiver resultReceiver = intent.getParcelableExtra(Constantz.RESULT_RECEIVER);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        if (intent == null) {
            return;
        }
        String errorMessage = "";

        // Get the location passed to this service through an extra.
        Location location = intent.getParcelableExtra(
                LOCATION_DATA_EXTRA);

        // ...

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(resultReceiver, Constantz.FAILURE_RESULT, null, errorMessage);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG, getString(R.string.address_found));
            Log.d(TAG, "address "+address.toString());
            deliverResultToReceiver(resultReceiver, Constantz.SUCCESS_RESULT,
                    address, "");
        }
    }

    private void deliverResultToReceiver(ResultReceiver resultReceiver, int successResult, Address address, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constantz.RESULT_DATA_KEY_MESSAGE, message);
        com.texaconnect.texa.model.Address address1 = new com.texaconnect.texa.model.Address();
        address1.copyToAddress(address);
        bundle.putParcelable(Constantz.RESULT_DATA_KEY, address1);
        resultReceiver.send(successResult, bundle);
    }
}