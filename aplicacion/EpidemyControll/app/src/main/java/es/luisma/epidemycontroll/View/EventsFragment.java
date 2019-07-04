package es.luisma.epidemycontroll.View;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;

import es.luisma.epidemycontroll.Controller.AlertsController;
import es.luisma.epidemycontroll.Controller.ProfileController;
import es.luisma.epidemycontroll.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "user";
    private String username;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AlertsController controller;
    private ListView listView;

    private OnFragmentInteractionListener mListener;

    public EventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment EventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsFragment newInstance(String param1) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
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
        // Inflate the layout for this fragment
        controller = new AlertsController();

        EventListAdapter whatever;
        try {

            JSONArray json = controller.getAlerts(username);
            String[] nameArray =new String[json.length()];
            String[] infoArray=new String[json.length()];
            final String[] ids=new String[json.length()];
            for(int i =0;i<json.length();i++){
                JSONObject obj = json.getJSONObject(i);
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                String currentTime = sdf.format(sdf2.parse(obj.getString("timestamp")));
                String estado = "Enfermo";
                if(obj.getInt("state")==0){
                    estado = "Sano";
                }
                nameArray[i] =estado;
                infoArray[i] =currentTime;
                ids[i] = obj.getString("_id");

            }
            whatever = new EventListAdapter(getActivity(), nameArray, infoArray,ids);
            View view = inflater.inflate(R.layout.fragment_events, container, false);

            listView = view.findViewById(R.id.eventList);
            listView.setAdapter(whatever);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", ids[position]);
                    Fragment fragment = new EventDetailsFragment();
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }
            });
            return view;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inflater.inflate(R.layout.fragment_events, container, false);

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
