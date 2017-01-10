package gr.rambou.myicarus;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;


public class Grades extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private OnFragmentInteractionListener mListener;
    private static int REFRESH_TIME_IN_SECONDS = 6;
    private Icarus myicarus;
    ArrayList<Lesson> studentLessons;
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


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
        //thread που εκτελείται και σταματά σε κάποιον χρόνο σε περίπτωση που το refresh αποτύχει
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopSwipeRefresh();
            }
        }, REFRESH_TIME_IN_SECONDS * 1000);

        new ReloadGrades().execute();
    }

    public class ReloadGrades extends AsyncTask<Void, Void, Void> {
        Object[] arraylist;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            System.setProperty("jsse.enableSNIExtension", "false");

            try {
                myicarus.LoadMarks(null);
                arraylist = myicarus.getAll_Lessons_array();
                //ListView lessonsList = (ListView) swipeRefreshLayout.findViewById(R.id.grades_listview);
                Log.v("REFRESH", "success");
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("REFRESH", e.getLocalizedMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            stopSwipeRefresh();

            // specify an adapter (see also next example)
            mAdapter = new LessonAdapter(arraylist);
            mRecyclerView.setAdapter(mAdapter);
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

        mRecyclerView = (RecyclerView) swipeRefreshLayout.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(container.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        new ReloadGrades().execute();

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
