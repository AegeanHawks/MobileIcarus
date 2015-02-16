package gr.rambou.myicarus;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Statistics extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private Icarus mParam1;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment Statistics.
     */
    // TODO: Rename and change types and number of parameters
    public static Statistics newInstance(String param1) {
        Statistics fragment = new Statistics();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public Statistics() {
        // Required empty public constructor
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
        View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);

        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        TextView mo = (TextView) rootView.findViewById(R.id.mo);

        mParam1 = (Icarus) getArguments().getSerializable("myicarus");
        DataPoint[] data = new DataPoint[0];
        ArrayList<Lesson> arraylist;
        GetGrades f = new GetGrades();
        try {
            arraylist = f.execute().get();
            data = new DataPoint[arraylist.size()];

            int i=0;
            int m = 0;
            for(Lesson l : arraylist){
                data[i] = new DataPoint(i,l.Get_Mark());
                m +=l.Get_Mark();
                i++;
            }
            Double k = (double)m/arraylist.size();

            mo.setText("Ο μέσος όρος είναι: " + String.format("%.2f", k) + " και περασμένα μαθήματα "+ arraylist.size() + " από τα 56.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(data);
        graph.addSeries(series);

        // Inflate the layout for this fragment
        return rootView;
    }

    private class GetGrades extends AsyncTask<String, Void, ArrayList<Lesson>> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected ArrayList<Lesson> doInBackground(String... params) {
            System.setProperty("jsse.enableSNIExtension", "false");

            mParam1.LoadMarks(null);
            ArrayList<Lesson> arraylist = mParam1.getSucceed_Lessons();

            return arraylist;
        }

        @Override
        protected void onPostExecute(ArrayList<Lesson> result) {

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
