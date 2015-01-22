package gr.rambou.myicarus;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Icarus myicarus;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    public Icarus getIcarus(){
        return myicarus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        myicarus = (Icarus) getIntent().getExtras().getSerializable("icarus");

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    private void ChangeFragment(Fragment newFragment, Bundle data){
        if( data!=null ){
            newFragment.setArguments(data);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    public void onSectionAttached(int number) {
        //String[] navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        Bundle bundle = new Bundle();
        switch (number) {
            case 1:
                mTitle = getString(R.string.Grades);
                ArrayList<Lesson> arraylist = myicarus.getAll_Lessons();
                bundle.putSerializable("arraylist", arraylist);
                bundle.putSerializable("myicarus", myicarus);
                ChangeFragment(new Grades(), bundle);
                break;
            case 2:
                mTitle = getString(R.string.Request);
                bundle.putSerializable("myicarus", myicarus);
                ChangeFragment(new request(), bundle);
                break;
            case 3:
                mTitle = getString(R.string.Course_Register);
                bundle.putSerializable("myicarus", myicarus);
                //ChangeFragment(new request(),bundle);
                break;
            case 4:
                mTitle = getString(R.string.About);
                ChangeFragment(new About(),null);
                break;
            case 5:
                mTitle = getString(R.string.Logout);
                SessionManager session = new SessionManager(getApplicationContext());
                session.logoutUser();
                this.finish();
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void RequestSend_Clicked(View view){
        Bundle args = new Bundle();
        args.putString("fathername",((TextView) findViewById(R.id.fathername)).getText().toString());
        try {
            Integer semester = Integer.valueOf(((TextView) findViewById(R.id.semester)).getText().toString());
            args.putInt("Semester", semester.intValue());
        }catch(Exception e){
            Toast.makeText(getBaseContext(),getString(R.string.NotificationMSG),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        args.putString("address",((TextView) findViewById(R.id.home_address)).getText().toString());
        args.putString("phone",((TextView) findViewById(R.id.phone_number)).getText().toString());
        args.putString("send_address",((TextView) findViewById(R.id.fax_or_address)).getText().toString());

        if(((RadioButton) findViewById(R.id.radioButton)).isChecked()){
            args.putSerializable("SendType",Icarus.SendType.OFFICE);
        }else if(((RadioButton) findViewById(R.id.radioButton1)).isChecked()){
            args.putSerializable("SendType",Icarus.SendType.COURIER);
        }else {
            args.putSerializable("SendType",Icarus.SendType.FAX);
        }

        String[] papers = new String[11];
        papers[0] = ((TextView) findViewById(R.id.textView_0)).getText().toString();
        papers[1] = ((TextView) findViewById(R.id.textView_1)).getText().toString();
        papers[2] = ((TextView) findViewById(R.id.textView_2)).getText().toString();
        papers[3] = ((TextView) findViewById(R.id.textView_3)).getText().toString();
        papers[4] = ((TextView) findViewById(R.id.textView_4)).getText().toString();
        papers[5] = ((TextView) findViewById(R.id.textView_5)).getText().toString();
        papers[6] = ((TextView) findViewById(R.id.textView_6)).getText().toString();
        papers[7] = ((TextView) findViewById(R.id.textView_7)).getText().toString();
        papers[8] = ((TextView) findViewById(R.id.textView_8)).getText().toString();
        papers[9] = ((TextView) findViewById(R.id.textView_9)).getText().toString();
        papers[10] = ((TextView) findViewById(R.id.another_paper)).getText().toString();

        args.putStringArray("papers",papers);

        new RequestSend().execute(args);
    }

    private void SendNotification(String message){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icarus)
                        .setContentTitle(getString(R.string.Notification))
                        .setContentText(message);
        // Creates an explicit intent for an Activity in your app
        /*Intent resultIntent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("icarus", myicarus);
        resultIntent.putExtras(bundle);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        0
                );
        mBuilder.setContentIntent(resultPendingIntent);*/

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        Notification nf = mBuilder.build();
        nf.flags |= Notification.FLAG_AUTO_CANCEL;

        mNotificationManager.notify(0,nf );
    }

    private class RequestSend extends AsyncTask<Bundle,Void , Void> {
        Boolean success;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Bundle... params) {
            Bundle b = params[0];
            System.setProperty("jsse.enableSNIExtension", "false");
            success = myicarus.SendRequest(b.getString("fathername"),b.getInt("Semester"),b.getString("address"),b.getString("phone"),b.getString("send_address"), (Icarus.SendType) b.getSerializable("SendType"), b.getStringArray("papers") );

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(success)
                Toast.makeText(getBaseContext(),getString(R.string.RequestSuccess),
                    Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getBaseContext(),getString(R.string.RequestFailed),
                        Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ChangeFragment(new Settings(), null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public void onSwitchClicked(View view) {
        // Is the toggle on?
        boolean on = ((Switch) view).isChecked();
        SessionManager session = new SessionManager(getApplicationContext());

        Intent intent = new Intent(this, GradesChecker.class);
        PendingIntent sender = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (on) {
            //start service
            if(!isMyServiceRunning(GradesChecker.class)) {
                // Start service using AlarmManager

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, 10);
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 12*60*60*1000, sender);

                startService(new Intent(getBaseContext(), GradesChecker.class));
                session.setServiceState(true);
            }
        } else {
            //stop service
            if(isMyServiceRunning(GradesChecker.class)) {
                alarm.cancel(sender);
                stopService(new Intent(getBaseContext(), GradesChecker.class));
                session.setServiceState(false);
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
