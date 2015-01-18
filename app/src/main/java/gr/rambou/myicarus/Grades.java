package gr.rambou.myicarus;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class Grades extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private OnFragmentInteractionListener mListener;
    private static int REFRESH_TIME_IN_SECONDS = 6;
    private Icarus myicarus;
    ArrayList<Lesson> studentLessons;
    SwipeRefreshLayout swipeRefreshLayout;

    public static Grades newInstance() {
        Grades fragment = new Grades();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public Grades() {
        // Required empty public constructor
    }

    @Override
    public void onRefresh() {
        Log.e(getClass().getSimpleName(), "refreshing...");

        //thread που εκτελείται και σταματά σε κάποιον χρόνο σε περίπτωση που το refresh αποτύχει
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopSwipeRefresh();
            }
        }, REFRESH_TIME_IN_SECONDS * 1000);


        try{
            ArrayList<Lesson> arraylist = new ReloadGrades().execute().get();
            ListView lessonsList = (ListView) swipeRefreshLayout.findViewById(R.id.grades_listview);
            lessonsList.setAdapter(new AdapterGrades(
                    getActivity().getApplicationContext(),
                    R.layout.grade_layout,
                    arraylist
            ));
        }catch (Exception e){
            stopSwipeRefresh();
        }

    }

    public class ReloadGrades extends AsyncTask<Void, Void, ArrayList<Lesson>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Lesson> doInBackground(Void... params) {
            System.setProperty("jsse.enableSNIExtension", "false");
            myicarus.LoadMarks(null);
            ArrayList<Lesson> arraylist = myicarus.getAll_Lessons();

            return arraylist;
        }

        @Override
        protected void onPostExecute(ArrayList<Lesson> result) {
            super.onPostExecute(result);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void stopSwipeRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //φορτώνουμε τα μαθήματα
        ArrayList<Lesson> lessons = (ArrayList<Lesson>) getArguments().getSerializable("arraylist");
        myicarus = (Icarus) getArguments().getSerializable("myicarus");

        swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_grades, container, false);
        swipeRefreshLayout.setOnRefreshListener(this);

        //region Initialize ListView
        ListView lessonsList = (ListView) swipeRefreshLayout.findViewById(R.id.grades_listview);

        //region Set ListView Adapter
        lessonsList.setAdapter(new AdapterGrades(
                getActivity().getApplicationContext(),
                R.layout.grade_layout,
                lessons
        ));
        //endregion

        return swipeRefreshLayout;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
