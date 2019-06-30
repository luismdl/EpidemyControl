package es.luisma.epidemycontroll.View;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import es.luisma.epidemycontroll.Controller.HomeController;
import es.luisma.epidemycontroll.R;

import static android.content.Context.LOCATION_SERVICE;
import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;

public class HomeFragment extends Fragment implements LocationListener {

    private static final int MY_LOCATION_REQUEST_CODE = 23;
    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 12;

    private static final String TAG = "test";
    private static final String ARG_PARAM1 = "user";
    private String username;
    private Location mLastLocation;

    private TextView mEstadoText;
    private Button changeEstadoBtn;
    private TextView mLatitudeText;
    private TextView mLongitudeText;
    private TableLayout mTableAlerts;
    String provider;
    LocationManager locationManager;

    private HomeController controller;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            username = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        controller= new HomeController();
        int state =controller.getState(username);

        mTableAlerts = (TableLayout) view.findViewById(R.id.homeAlerts);
        mLatitudeText = (TextView) view.findViewById(R.id.homeLat);
        mLongitudeText = (TextView) view.findViewById(R.id.homeLon);
        mEstadoText = (TextView) view.findViewById(R.id.homeEstado);
        if(state==0){
            mEstadoText.setText("Sano");
        } else if (state == 1) {
            mEstadoText.setText("Enfermo");
        }
        changeEstadoBtn = (Button) view.findViewById(R.id.homeChange);

        changeEstadoBtn.setOnClickListener(new View.OnClickListener() {

                                               @Override
                                               public void onClick(View v) {
                                                   Bundle bundle = new Bundle();
                                                   bundle.putDouble("Lat", mLastLocation.getLatitude());
                                                   bundle.putDouble("Lon", mLastLocation.getLongitude());
                                                   bundle.putString("user", username);
                                                   Fragment fragment = new ChangeStateFragment();
                                                   fragment.setArguments(bundle);
                                                   FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                   transaction.replace(R.id.content, fragment);
                                                   transaction.addToBackStack(null);
                                                   transaction.commit();
                                               }
                                           }
        );


        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        checkLocationPermission();

        return view;
    }

    private void getAlerts(){
        try {
            JSONArray json =controller.getAlerts(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            for(int i =0;i<json.length();i++){
                JSONObject obj = json.getJSONObject(i);

                //TableRow t = new TableRow(getContext());
                //TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                //t.setLayoutParams(lp);
                //TextView tv = new TextView(getContext());
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                String currentTime = sdf.format(sdf2.parse(obj.getString("timestamp")));
                //tv.setLayoutParams(new TableRow.LayoutParams(0));
                //tv.setWidth(100);
                //tv.setGravity(Gravity.CENTER);
                //tv.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
                //tv.setText(currentTime);
                DecimalFormat df = new DecimalFormat("#.##");
                /*TextView tv2 = new TextView(getContext());
                //tv2.setWidth(30);
                //tv2.setLayoutParams(new TableRow.LayoutParams(1));
                //tv2.setGravity(Gravity.CENTER);
                //tv2.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
                tv2.setText(df.format(Double.parseDouble(obj.getJSONObject("location").getJSONArray("coordinates").get(1).toString())));
                TextView tv3 = new TextView(getContext());
                //tv3.setLayoutParams(new TableRow.LayoutParams(2));
                //tv3.setWidth(30);
                //tv3.setGravity(Gravity.CENTER);
                //tv3.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
                tv3.setText(df.format(Double.parseDouble(obj.getJSONObject("location").getJSONArray("coordinates").get(0).toString())));
                t.addView(tv);
                t.addView(tv2);
                t.addView(tv3);
                mTableAlerts.addView(t);*/



                LayoutInflater inflater = getLayoutInflater();
                TableRow tr = (TableRow)inflater.inflate(R.layout.table_row, mTableAlerts, false);

                // Add First Column
                TextView tvTitle = (TextView)tr.findViewById(R.id.tvFecha);
                tvTitle.setText(currentTime);

                TextView tvTit = (TextView)tr.findViewById(R.id.tvLat);
                tvTit.setText(df.format(Double.parseDouble(obj.getJSONObject("location").getJSONArray("coordinates").get(1).toString())));

                // Add the 3rd Column
                TextView tvValue = (TextView)tr.findViewById(R.id.tvLon);
                tvValue.setText(df.format(Double.parseDouble(obj.getJSONObject("location").getJSONArray("coordinates").get(0).toString())));

                mTableAlerts.addView(tr);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);

            return true;
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        mLatitudeText.setText(lat.toString());
        mLongitudeText.setText(lng.toString());
        getAlerts();
        location.getAccuracy();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}

