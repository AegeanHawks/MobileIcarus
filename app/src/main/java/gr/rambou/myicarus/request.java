package gr.rambou.myicarus;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class request extends Fragment {
    private static final String ARG_PARAM1 = "myicarus";

    private Icarus mParam1;
    private OnFragmentInteractionListener mListener;

    public static request newInstance(Icarus param1) {
        request fragment = new request();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (Icarus) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_request, container, false);
        EditText surname = (EditText) v.findViewById(R.id.surname);
        EditText firstname = (EditText) v.findViewById(R.id.firstname);
        EditText studentid = (EditText) v.findViewById(R.id.student_id);

        studentid.setText(mParam1.getID());
        surname.setText(mParam1.getSurname());
        firstname.setText(mParam1.getStudentName());

        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
