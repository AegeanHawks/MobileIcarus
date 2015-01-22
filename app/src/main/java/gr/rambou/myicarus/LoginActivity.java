package gr.rambou.myicarus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginActivity extends Activity {
    private SessionManager session;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        String username = session.getUserDetails().get(SessionManager.KEY_NAME);
        String password = session.getUserDetails().get(SessionManager.KEY_PASSWORD);
        if(username!=null) {
            if (!username.toString().isEmpty()) {
                new Login().execute(username.toString(), password.toString());
            }
        }
        ((TextView) findViewById(R.id.Username)).setText(username);
    }

    public void Login_Clicked(View view) {
        hideKeyboard();

        //Take components from xml
        TextView username = (TextView) findViewById(R.id.Username);
        TextView password = (TextView) findViewById(R.id.Password);

        new Login().execute(username.getText().toString(),password.getText().toString());

        //this.finish();
        //endregion
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    // Title AsyncTask
    private class Login extends AsyncTask<String, Void, Boolean> {
        Icarus myicarus;
        String username, password;

        @Override
        protected void onPreExecute() {
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            System.setProperty("jsse.enableSNIExtension", "false");
            username = params[0];
            password = params[1];
            myicarus = new Icarus(username, password);

            return myicarus.login();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            spinner.setVisibility(View.GONE);
            if(!result){
                TextView error = (TextView) findViewById(R.id.ErrorMsg);
                error.setText(getString(R.string.Error_Login));
                error.setVisibility(View.VISIBLE);
            }else{
                session.createLoginSession(username, password);
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("icarus", myicarus);
                i.putExtras(bundle);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i); //Στην onCreate της MainActivity κάνω retrieve το object Icarus - φτιάξε τα υπόλοιπα, δες τι έκανε και ο Χάρης
                LoginActivity.this.finish();
            }
        }
    }
}
