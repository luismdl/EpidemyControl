package es.luisma.epidemycontroll.View;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.luisma.epidemycontroll.Controller.ProfileController;
import es.luisma.epidemycontroll.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeleteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeleteFragment extends Fragment {
    private EditText user;
    private Button borra;
    private ProfileController controller;

    private OnFragmentInteractionListener mListener;

    public DeleteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeleteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeleteFragment newInstance(String param1, String param2) {
        DeleteFragment fragment = new DeleteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete, container, false);

        controller = new ProfileController();
        user = (EditText) view.findViewById(R.id.delUser);
        borra = (Button) view.findViewById(R.id.delBtn);

        borra.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String u = user.getText().toString();
                if(!u.equals("")){
                    int i = controller.deleteUser(u);

                    if(i==-1){
                        Toast toast1 = Toast.makeText(getActivity().getApplicationContext(),
                                "Usuario no existentes", Toast.LENGTH_SHORT);
                        toast1.setGravity(Gravity.TOP|Gravity.RIGHT,0,0);
                        toast1.show();
                    }else if (i==-2){
                        Toast toast1 = Toast.makeText(getActivity().getApplicationContext(),
                                "Error en la transmision", Toast.LENGTH_SHORT);
                        toast1.setGravity(Gravity.TOP|Gravity.RIGHT,0,0);
                        toast1.show();
                    }else if(i>=0){
                        Toast toast1 = Toast.makeText(getActivity().getApplicationContext(),
                                "Usuario eliminado", Toast.LENGTH_SHORT);
                        toast1.setGravity(Gravity.TOP|Gravity.RIGHT,0,0);
                        toast1.show();
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
