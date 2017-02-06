package gr.rambou.myicarus;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MarksSemesterFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static int REFRESH_TIME_IN_SECONDS = 6;
    ArrayList<Lesson> studentLessons;
    SwipeRefreshLayout swipeRefreshLayout;
    private Icarus myicarus;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public MarksSemesterFragment() {
        // Required empty public constructor
    }

    public static MarksSemesterFragment newInstance() {
        MarksSemesterFragment fragment = new MarksSemesterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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

        new MarksSemesterFragment.ReloadGrades().execute();
    }

    private void stopSwipeRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
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

        new MarksSemesterFragment.ReloadGrades().execute();

        return swipeRefreshLayout;
    }

    public class ReloadGrades extends AsyncTask<Void, Void, Void> {
        ArrayList<Lesson> arraylist;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            System.setProperty("jsse.enableSNIExtension", "false");

            try {
                myicarus.LoadMarks(null);
                arraylist = myicarus.getExams_Lessons();
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
            mAdapter = new LessonAdapter(arraylist.toArray());
            mRecyclerView.setAdapter(mAdapter);
        }
    }

}
