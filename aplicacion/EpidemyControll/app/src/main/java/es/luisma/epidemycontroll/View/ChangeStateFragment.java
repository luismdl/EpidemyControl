package es.luisma.epidemycontroll.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import es.luisma.epidemycontroll.Controller.StateController;
import es.luisma.epidemycontroll.R;

import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangeStateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeStateFragment extends Fragment {

    private static final String TAG = "test";
    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 12;

    private static final String ARG_PARAM1 = "Lat";
    private static final String ARG_PARAM2 = "Lon";
    private static final String ARG_PARAM3 = "user";
    private String username;

    private OnFragmentInteractionListener mListener;
    private Spinner dropdown;
    private TextView details;
    private Button chngBtn;
    private StateController controller;

    private Double latitude;
    private Double longitude;

    public ChangeStateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChangeBlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangeStateFragment newInstance(String param1, String param2,String param3) {
        ChangeStateFragment fragment = new ChangeStateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            latitude = getArguments().getDouble(ARG_PARAM1);
            longitude = getArguments().getDouble(ARG_PARAM2);
            username = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_state, container, false);
        controller = new StateController();
        dropdown = view.findViewById(R.id.chngStateSpinner);
        details = view.findViewById(R.id.chngStateText);
        chngBtn = view.findViewById(R.id.chngStateBtn);
        String[] items = new String[]{"Bien", "Mal"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        chngBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FitnessOptions fitnessOptions = FitnessOptions.builder()
                        .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
                        .build();
                if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(getContext()), fitnessOptions)) {
                    GoogleSignIn.requestPermissions(getActivity(),GOOGLE_FIT_PERMISSIONS_REQUEST_CODE, GoogleSignIn.getLastSignedInAccount(getContext()), fitnessOptions);
                } else {
                    accessGoogleFit();
                }
            }
        });



        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
                accessGoogleFit();
            }
        }
    }

    private void accessGoogleFit() {
        // Setting a start and end date using a range of 1 week before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.DATE, -1);
        long startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = getDateInstance();
        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest =
                new DataReadRequest.Builder().enableServerQueries()
                        .read(DataType.TYPE_HEART_RATE_BPM)
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                        .build();
        Task<DataReadResponse> response = Fitness.getHistoryClient(getActivity(), GoogleSignIn.getLastSignedInAccount(getContext())).readData(readRequest);
        response.addOnCompleteListener(new OnCompleteListener<DataReadResponse>() {
            @Override
            public void onComplete(@NonNull Task<DataReadResponse> response) {
                Log.d(TAG, "onComplete()");
                List<DataSet> dataSets = response.getResult().getDataSets();
                int i = controller.updateData(username,dataSets.get(0), latitude,longitude, dropdown.getSelectedItem().toString(), details.getText().toString());
                makeToast(i);
            }
        });
    }


    public void makeToast(int i ){
        if(i==-1){
            Toast toast1 = Toast.makeText(getActivity().getApplicationContext(),
                    "Datos no existentes", Toast.LENGTH_SHORT);
            toast1.setGravity(Gravity.TOP|Gravity.RIGHT,0,0);
            toast1.show();
        }else if (i==-2){
            Toast toast1 = Toast.makeText(getActivity().getApplicationContext(),
                    "Error en la transmision", Toast.LENGTH_SHORT);
            toast1.setGravity(Gravity.TOP|Gravity.RIGHT,0,0);
            toast1.show();
        }else if(i>=0){
            Toast toast1 = Toast.makeText(getActivity().getApplicationContext(),
                    "Datos actualizados", Toast.LENGTH_SHORT);
            toast1.setGravity(Gravity.TOP|Gravity.RIGHT,0,0);
            toast1.show();
            getFragmentManager().popBackStack();
        }
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
