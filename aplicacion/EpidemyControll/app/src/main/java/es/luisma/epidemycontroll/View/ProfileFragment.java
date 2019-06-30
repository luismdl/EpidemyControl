package es.luisma.epidemycontroll.View;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import java.text.SimpleDateFormat;

import es.luisma.epidemycontroll.Controller.ProfileController;
import es.luisma.epidemycontroll.Model.Dominio.Usuario;
import es.luisma.epidemycontroll.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "user";
    private ProfileController controller;
    private String username;
    private OnFragmentInteractionListener mListener;
    private TextView usertext;
    private TextView email;
    private TextView birth;
    private Button actualiza;



    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view =inflater.inflate(R.layout.fragment_profile, container, false);
        controller = new ProfileController();
        usertext = (TextView) view.findViewById(R.id.profUser);
        email = (TextView) view.findViewById(R.id.profMail);
        birth = (TextView) view.findViewById(R.id.profBirth);
        actualiza = (Button) view.findViewById(R.id.profActua);

        Usuario a = controller.getData(username);
        usertext.setText(a.getUserName());
        email.setText(a.getEmail());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        birth.setText(format.format(a.getBirthDate()));
        actualiza.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("user", username);
                Fragment fragment = new UpdateDataFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
        );

        // Inflate the layout for this fragment
        return view;
    }

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
