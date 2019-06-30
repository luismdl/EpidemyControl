package es.luisma.epidemycontroll.View;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import es.luisma.epidemycontroll.Controller.ProfileController;
import es.luisma.epidemycontroll.Model.Dominio.Usuario;
import es.luisma.epidemycontroll.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateDataFragment extends Fragment {
    private static final String ARG_PARAM1 = "user";
    private ProfileController controller;
    private String username;
    private OnFragmentInteractionListener mListener;
    private EditText pass1;
    private EditText pass2;
    private Button guarda;



    public UpdateDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateDataFragment newInstance(String param1, String param2) {
        UpdateDataFragment fragment = new UpdateDataFragment();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_data, container, false);

        controller = new ProfileController();
        pass1 = (EditText) view.findViewById(R.id.loginPass1);
        pass2 = (EditText) view.findViewById(R.id.loginPass2);
        guarda = (Button) view.findViewById(R.id.profSave);

        guarda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String p1 = pass1.getText().toString();
                String p2 = pass2.getText().toString();
                if(p1.equals(p2)){
                    int i = controller.savePass(username,p1);

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
            }
        });

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
