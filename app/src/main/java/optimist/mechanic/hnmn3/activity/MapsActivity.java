package optimist.mechanic.hnmn3.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import optimist.mechanic.hnmn3.puc.R;


public class MapsActivity extends Fragment implements LocationListener {

    GoogleMap googleMap=null;
    double lastlatitude, lastlongitude, latitude, longitude;
    Marker mark;
    Location temp, mLastLocation;
    double totalDistance = 0;
    boolean startorstop=false;
    Button btnStartLocationUpdates;
    private FragmentActivity myContext;
    TextView locationTv,rupee,msg,counter;
    FragmentManager myFragmentManager;
    SupportMapFragment mySupportMapFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            myContext.finish();
        }

        /*SupportMapFragment supportMapFragment =
                (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.googleMap);*/

        /*googleMap.setMyLocationEnabled(true);*/

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(myContext.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location;
        if (ContextCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(myContext,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 0, 1, this);
        Log.d("yoyo", "Called Onclick lsitener");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_maps, container, false);
        Log.d("yoyo", "Button Initilized");
        btnStartLocationUpdates = (Button) view.findViewById(R.id.btnLocationUpdates);
        locationTv = (TextView) view.findViewById(R.id.latlongLocation);
        rupee = (TextView) view.findViewById(R.id.rupee);
        msg = (TextView) view.findViewById(R.id.msgOnMap);
        counter = (TextView) view.findViewById(R.id.Counterview);
        btnStartLocationUpdates.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                togglePeriodicLocationUpdates();
            }
        });
        try {
            // Loading map
            initilizeMap();
            googleMap.setMyLocationEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void initilizeMap() {

        try
        {
            if (googleMap == null) {
                SupportMapFragment supportMapFragment =
                        (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.googleMap);
                googleMap = supportMapFragment.getMap();

                if (googleMap == null) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void togglePeriodicLocationUpdates() {
        if (!startorstop) {
            btnStartLocationUpdates
                    .setText("Stop");
            msg.setText("No vehical available..");
            counter.setVisibility(View.GONE);
            startorstop = true;
            totalDistance=0;

        } else {
            // Changing the button text
            btnStartLocationUpdates
                    .setText("Start");
            double temp = totalDistance/1000;
            msg.setText("1 vehical available..");
            String total = String.format("%.3f",temp*1.25);
            counter.setText("Please pay " + total + " Rupees at counter");
            counter.setVisibility(View.VISIBLE);
            startorstop = false;
        }
    }


    int flag = 0;

    @Override
    public void onLocationChanged(Location location) {




        try
        {
            if (googleMap == null) {
                SupportMapFragment supportMapFragment =
                        (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.googleMap);
                googleMap = supportMapFragment.getMap();

                if (googleMap == null) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                            .show();
                }
            }else{
                lastlatitude = latitude;
                lastlongitude = longitude;
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                googleMap.clear();
                mark = googleMap.addMarker(new MarkerOptions().position(latLng));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(19));

                mLastLocation = location;
                if (flag == 3) {
                    totalDistance += location.distanceTo(temp);
                    temp = location;
                   // Toast.makeText(getActivity().getApplicationContext(), "Distance Changed  "+totalDistance,Toast.LENGTH_LONG).show();
                }else{
                    flag++;
                }

                if (startorstop) {
                    if(totalDistance>1000){
                        totalDistance/=1000;
                        String formato = String.format("%.3f",totalDistance);
                        locationTv.setText("Distance = " + formato+" km");
                        formato = String.format("%.3f",totalDistance*1.25);
                        rupee.setText("Rupees ="+formato);
                    }else{
                        String formato = String.format("%.3f",(totalDistance/1000)*1.25);
                        locationTv.setText("Distance = " + (int)totalDistance+ " meter");
                        rupee.setText("Rupees ="+formato);
                    }

                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        temp = location;

        /*mark.remove();*/

        Log.d("Yeah","Updating");
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            return false;
        }
    }
}