package es.luisma.epidemycontroll.View;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import es.luisma.epidemycontroll.Controller.AlertsController;
import es.luisma.epidemycontroll.R;

import static java.text.DateFormat.getTimeInstance;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private AlertsController controller;

    // TODO: Rename and change types of parameters
    private String id;

    private OnFragmentInteractionListener mListener;
    private TextView state;
    private TextView time;
    private TextView details;
    private LineChart pulsaciones;

    public EventDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment EventDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventDetailsFragment newInstance(String param1) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        controller = new AlertsController();
        state = view.findViewById(R.id.evntDetState);
        time = view.findViewById(R.id.evntDetTime);
        details = view.findViewById(R.id.evntDetDet);
        pulsaciones = view.findViewById(R.id.chart1);
        pulsaciones.getDescription().setEnabled(false);
        pulsaciones.setTouchEnabled(true);
        pulsaciones.setDragEnabled(true);
        pulsaciones.setScaleEnabled(true);
        XAxis xAxis = pulsaciones.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float value) {

                long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });

        YAxis leftAxis = pulsaciones.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(170f);
        YAxis rightAxis = pulsaciones.getAxisRight();
        rightAxis.setEnabled(false);
        JSONObject json = controller.getStateId(id);

        try {
            String estado = "Enfermo";
            if(json.getInt("state")==0){
                estado = "Sano";
            }
            state.setText(estado);

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            String currentTime = sdf.format(sdf2.parse(json.getString("timestamp")));
            time.setText(currentTime);
            details.setText(json.getString("details"));

            JSONArray arr = json.getJSONArray("heartbeat");
            ArrayList<Entry> values = new ArrayList<>();
            DateFormat dateFormat = getTimeInstance();
            for(int i =0;i<arr.length();i++){
                JSONObject obj = arr.getJSONObject(i);
                long now = TimeUnit.MILLISECONDS.toMinutes(dateFormat.parse(obj.getString("timestamp")).getTime());
                long value = obj.getLong("value");
                values.add(new Entry(now,value));
            }

            LineDataSet set1 = new LineDataSet(values, "Pulsaciones(bpm)");
            set1.setColor(ColorTemplate.getHoloBlue());
            set1.setLineWidth(1.5f);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.2f);

            LineData data = new LineData(set1);


            pulsaciones.setData(data);


        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
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
