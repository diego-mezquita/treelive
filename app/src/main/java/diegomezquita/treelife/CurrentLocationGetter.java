package diegomezquita.treelife;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by diegomezquita on 23/05/16.
 */
public class CurrentLocationGetter  extends AsyncTask<String, Void, String> {

    private RecycleInMenuActivity activity;

    public CurrentLocationGetter(RecycleInMenuActivity activity) {
        super();
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(activity, Locale.getDefault());

        String address = new String();
        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(params[0]), Double.parseDouble(params[1]), 1);
            //addresses = geocoder.getFromLocation(43.5378057, -5.6718853, 1);

            //get current 'Street name, number'
            address = addresses.get(0).getAddressLine(0);

            //get current city
            address = address + ", " + addresses.get(0).getLocality();

            //get current subAdminArea (region)
            address = address + ", " + addresses.get(0).getSubAdminArea();

            String s = "";
            //get country
            //address = address + ", " + addresses.get(0).getCountryName();


            //get postal code
            //String postalCode = addresses.get(0).getPostalCode();

            //get place Name
            //String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            return address;
            //return "hola";

        } catch (IOException ex) {
            ex.printStackTrace();
            return "IOE EXCEPTION";

        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return "IllegalArgument Exception";
        }
    }

    /**
     * When the task finishes, onPostExecute() call back data to Activity UI and displays the address.
     * @param address
     */
    @Override
    protected void onPostExecute(String address) {
        // Call back Data and Display the current address in the UI
        activity.locationAddress(address);
    }
}
