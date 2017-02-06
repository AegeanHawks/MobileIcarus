package gr.rambou.myicarus;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager mFragmentManager;
    private TextView nameView;
    private TextView emailView;
    private FragmentTransaction mFragmentTransaction;
    private Icarus myicarus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myicarus = (Icarus) getIntent().getExtras().getSerializable("icarus");

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        nameView = (TextView) hView.findViewById(R.id.nameView);
        emailView = (TextView) hView.findViewById(R.id.emailView);
        nameView.setText(myicarus.getStudentFullName());
        emailView.setText(myicarus.getID());

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();


        Bundle requestBundle = new Bundle();
        requestBundle.putSerializable("myicarus", myicarus);
        Fragment fragment = new TabFragment();
        fragment.setArguments(requestBundle);
        mFragmentTransaction.replace(R.id.content_navigation, fragment).commit();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        Fragment fragment = null;


        Bundle requestBundle = new Bundle();
        requestBundle.putSerializable("myicarus", myicarus);

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_marks) {
            fragment = new TabFragment();
            fragment.setArguments(requestBundle);
        } else if (id == R.id.nav_applications) {
            fragment = new request();
            fragment.setArguments(requestBundle);
        } else if (id == R.id.nav_semester_statement) {
            fragment = new CourseRegister();
            fragment.setArguments(requestBundle);
        } else if (id == R.id.nav_performance) {
            fragment = new Statistics();
            fragment.setArguments(requestBundle);
        } else if (id == R.id.nav_about) {
            fragment = new About();
        } else if (id == R.id.nav_settings) {
            fragment = new Settings();
        } else if (id == R.id.nav_logout) {
            SessionManager session = new SessionManager(getApplicationContext());
            session.logoutUser();
            this.finish();
            return true;
        }

        // commit the transaction if any
        if (fragment != null)
            mFragmentTransaction.replace(R.id.content_navigation, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.content_navigation, new Settings());
            //transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Icarus getIcarus() {
        return myicarus;
    }

    public void RequestSend_Clicked(View view) {
        Bundle args = new Bundle();
        args.putString("fathername", ((TextView) findViewById(R.id.fathername)).getText().toString());
        try {
            Integer semester = Integer.valueOf(((TextView) findViewById(R.id.semester)).getText().toString());
            args.putInt("Semester", semester.intValue());
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), getString(R.string.NotificationMSG),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        args.putString("address", ((TextView) findViewById(R.id.home_address)).getText().toString());
        args.putString("phone", ((TextView) findViewById(R.id.phone_number)).getText().toString());
        args.putString("send_address", ((TextView) findViewById(R.id.fax_or_address)).getText().toString());

        if (((RadioButton) findViewById(R.id.radioButton)).isChecked()) {
            args.putSerializable("SendType", Icarus.SendType.OFFICE);
        } else if (((RadioButton) findViewById(R.id.radioButton1)).isChecked()) {
            args.putSerializable("SendType", Icarus.SendType.COURIER);
        } else {
            args.putSerializable("SendType", Icarus.SendType.FAX);
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

        args.putStringArray("papers", papers);

        new RequestSend().execute(args);
    }

    private void SendNotification(String message) {
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

        mNotificationManager.notify(0, nf);
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
            if (!isMyServiceRunning(GradesChecker.class)) {
                // Start service using AlarmManager

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, 10);
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 12 * 60 * 60 * 1000, sender);

                startService(new Intent(getBaseContext(), GradesChecker.class));
                session.setServiceState(true);
            }
        } else {
            //stop service
            if (isMyServiceRunning(GradesChecker.class)) {
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

    private class RequestSend extends AsyncTask<Bundle, Void, Void> {
        Boolean success;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Bundle... params) {
            Bundle b = params[0];
            System.setProperty("jsse.enableSNIExtension", "false");
            success = myicarus.SendRequest(b.getString("fathername"), b.getInt("Semester"), b.getString("address"), b.getString("phone"), b.getString("send_address"), (Icarus.SendType) b.getSerializable("SendType"), b.getStringArray("papers"));

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (success)
                Toast.makeText(getBaseContext(), getString(R.string.RequestSuccess),
                        Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getBaseContext(), getString(R.string.RequestFailed),
                        Toast.LENGTH_SHORT).show();
        }
    }

}
