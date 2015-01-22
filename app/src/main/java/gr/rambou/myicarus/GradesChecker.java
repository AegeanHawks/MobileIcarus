package gr.rambou.myicarus;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

public class GradesChecker extends Service {

    public GradesChecker() {

    }

    private void SendNotification(String message){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icarus)
                        .setContentTitle("Επ, βγήκε μαθηματάκι...")
                        .setContentText(message);

        //Set vibration when new curriculum arrives
        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = { 0, 100, 600, 100, 700};
        vibrator.vibrate(pattern, -1);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }

    private class Check extends AsyncTask<String, Void, Boolean> {
        Icarus myicarus;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {
            System.setProperty("jsse.enableSNIExtension", "false");

            myicarus = new Icarus(params[0], params[1]);
            myicarus.login();

            for(Lesson l : myicarus.getExams_Lessons()){
                if(l.Get_Status() != Lesson.LessonStatus.NOT_GIVEN){
                    SendNotification(l.Get_Title() + " με βαθμό " + l.Get_Mark());
                }
            }

            return myicarus.login();
        }

        @Override
        protected void onPostExecute(Boolean result) {

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SessionManager session = new SessionManager(getApplicationContext());

        new Check().execute(session.getUserDetails().get("username"), session.getUserDetails().get("password"));

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
