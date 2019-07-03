package es.luisma.epidemycontroll.View;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;

import es.luisma.epidemycontroll.Controller.AlertsController;
import es.luisma.epidemycontroll.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlertsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlertsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int MY_LOCATION_REQUEST_CODE = 23;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TableLayout mTableAlerts;
    private AlertsController controller;
    private FusedLocationProviderClient fusedLocationClient;
    private OnFragmentInteractionListener mListener;

    public AlertsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlertsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlertsFragment newInstance(String param1, String param2) {
        AlertsFragment fragment = new AlertsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_alerts, container, false);

        controller = new AlertsController();
        mTableAlerts = view.findViewById(R.id.alertsList);

        checkLocationPermission();
        return view;
    }


    private void getAlerts(Location mLastLocation){

        try {
            JSONArray json =controller.getAlerts();
            for(int i =0;i<json.length();i++){
                JSONObject obj = json.getJSONObject(i);
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                String currentTime = sdf.format(sdf2.parse(obj.getString("timestamp")));
                DecimalFormat df = new DecimalFormat("#.##");



                LayoutInflater inflater = getLayoutInflater();
                TableRow tr = (TableRow)inflater.inflate(R.layout.table_row2, mTableAlerts, false);

                // Add First Column
                TextView tvTitle = (TextView)tr.findViewById(R.id.tvFecha2);
                tvTitle.setText(currentTime);

                TextView tvTit = (TextView)tr.findViewById(R.id.tvLat2);
                Double Lat=Double.parseDouble(obj.getJSONObject("location").getJSONArray("coordinates").get(1).toString());
                tvTit.setText(df.format(Lat));

                // Add the 3rd Column
                TextView tvValue = (TextView)tr.findViewById(R.id.tvLon2);
                Double Lon = Double.parseDouble(obj.getJSONObject("location").getJSONArray("coordinates").get(0).toString());
                tvValue.setText(df.format(Lon));

                TextView tvDist = (TextView)tr.findViewById(R.id.tvDist2);
                Double dist = distance(mLastLocation.getLatitude(),Lat,mLastLocation.getLongitude(),Lon);
                tvDist.setText(df.format(dist)+"m");

                mTableAlerts.addView(tr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    getAlerts(location);
                                }
                            }
                        });

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Localización")
                        .setMessage("Esta aplicación necesita la localización para hacer uso de su funcionaliadad principal")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION_REQUEST_CODE);
            }

            return false;
        } else {
            fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        getAlerts(location);
                    }
                }
            });

            return true;
        }

    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * @returns Distance in Meters
     */
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
