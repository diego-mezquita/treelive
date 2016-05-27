package diegomezquita.treelife;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;

public class RecycleInMenuActivity extends Activity implements LocationListener {

    public final static String EXTRA_LOCATION = "com.diegomezquita.treelife.RECYCLE_IN_MENU_SEARCH_LOCATION";
    protected final static String EXTRA_CLOTHES_CONTAINERS_JSON = "com.diegomezquita.treelife.CLOTHES_CONTAINERS_JSON";
    protected final static String EXTRA_SEARCH_LOCATION = "com.diegomezquita.treelife.RECYCLE_IN_MENU_SEARCH_LOCATION";
    protected String currentLocation = new String();

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Double locationLatitude;
    protected Double locationLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_in_menu);

        ActivityCompat.requestPermissions(RecycleInMenuActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                124);
    }

    public void displaySearchByAddress(View view) {
        /*Intent intent = new Intent(this, RecycleInMenuActivity.class);
        startActivity(intent);*/
    }

    public void displayHabitualContainers(View view) {
        Intent intent = new Intent(this, RecycleInMenuHabitualContainersActivity.class);
        startActivity(intent);
        this.overridePendingTransition(0, 0);
    }

    public void displayCreateContainers(View view) {
        Intent intent = new Intent(this, RecycleInMenuCreateContainerActivity.class);
        startActivity(intent);
        this.overridePendingTransition(0, 0);
    }

    public void executeSearch(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        String urlClothes = "http://opendata.gijon.es/descargar.php?id=7&tipo=JSON";
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mainActivityLinearLayout);

        // Getting info from the search form
        // Location specified
        EditText editText = (EditText) this.findViewById(R.id.edit_text_recycle_in_menu_where_location_feedback);
        String location = "calle aviles, gijon";//editText.getText().toString();
        // Container types selected ordered alphabetically
     //   boolean checkBoxBatteries = ((CheckBox) this.findViewById(R.id.feedback_check_box_recycle_in_menu__what__batteries)).isChecked();
     //   boolean checkBoxClothes = ((CheckBox) this.findViewById(R.id.feedback_check_box_recycle_in_menu__what__clothes)).isChecked();
     //   boolean checkBoxGlass = ((CheckBox) this.findViewById(R.id.feedback_check_box_recycle_in_menu__what__glass)).isChecked();

        // ArrayList that stores the materials to be or not requested to Gijón Open Data
        ArrayList<Boolean> materials = new ArrayList<>();
        materials.add(((CheckBox) this.findViewById(R.id.feedback_check_box_recycle_in_menu__what__batteries)).isChecked());
        materials.add(((CheckBox) this.findViewById(R.id.feedback_check_box_recycle_in_menu__what__clothes)).isChecked());
        materials.add(((CheckBox) this.findViewById(R.id.feedback_check_box_recycle_in_menu__what__oil)).isChecked());

        boolean checkBoxGlass = ((CheckBox) this.findViewById(R.id.feedback_check_box_recycle_in_menu__what__glass)).isChecked();
        boolean checkBoxPaper = ((CheckBox) this.findViewById(R.id.feedback_check_box_recycle_in_menu__what__paper)).isChecked();
        boolean checkBoxPlastic = ((CheckBox) this.findViewById(R.id.feedback_check_box_recycle_in_menu__what__plastic)).isChecked();

        /*DataGetter dataGetter = new DataGetter(checkBoxClothes);

        if(checkBoxClothes) {
            Containers clothesContainers = dataGetter.getClothesContainers();
        }*/

        DataGetter data_getter = new DataGetter(this, location, materials);
        data_getter.execute(urlClothes);

        //data_getter.getClothesContainers();
//
//        String hola = "HOla";
//
//        Containers containersRequested = data_getter.getContainersRequested();
//
//        intent.putExtra(EXTRA_CLOTHES_CONTAINERS_JSON, containersRequested);
//        intent.putExtra(EXTRA_SEARCH_LOCATION, location);
//        this.startActivity(intent);
    }

    public void getLocationListener() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent displayGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(displayGPSSettingIntent, 0);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String s = "s";
            return;
        }
        String ss = "";

        /*try {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() -2 * 60 * 1000) {
                // lastKnownLocation valid -> use it
                String s = "";
            }
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        } catch (SecurityException e) {
            e = e;
        }*/

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() -2 * 60 * 1000) {
            // lastKnownLocation valid -> use it
            String s = "";
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }
// trying code from http://stackoverflow.com/questions/15997079/getlastknownlocation-always-return-null-after-i-re-install-the-apk-file-via-ecli
    // instead of getLocationListener method
    public Location getLocation() {
        Location location = new Location("Service provider");
        try {
            this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                //this.canGetLocation = true;
                if (isNetworkEnabled) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                    Log.d("Network", "Network Enabled");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            this.locationLatitude = location.getLatitude();
                            this.locationLongitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                        Log.d("GPS", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                this.locationLatitude = location.getLatitude();
                                this.locationLongitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    public void getCurrentLocation(View view) {
        //this.getLocationListener();
        Location location = this.getLocation();
        CurrentLocationGetter currentLocation = new CurrentLocationGetter(this);
        currentLocation.execute(String.valueOf(this.locationLatitude), String.valueOf(locationLongitude));
    }

    public void locationAddress(String address) {
        this.currentLocation = address;
        EditText editTextWhere = (EditText) findViewById(R.id.edit_text_recycle_in_menu_where_location_feedback);
        editTextWhere.setText(address);
        //this.findViewById(R.id.edit_text_recycle_in_menu_where_location_feedback);
    }

    // Required methods due to implement LocationListener
    public void onLocationChanged(Location location) {
        if (location != null) {
            this.locationLatitude = location.getLatitude();
            this.locationLongitude = location.getLongitude();

            if ((checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    && (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                return;
            }

            locationManager.removeUpdates(this);
        }
    }

    public void onProviderEnabled(String arg0) {}

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}

    public void onProviderDisabled(String arg0) {}

}
