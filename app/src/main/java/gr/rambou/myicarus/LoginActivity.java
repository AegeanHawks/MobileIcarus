package gr.rambou.myicarus;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
    }

    public void Login_Clicked(View view) {
        //Take components from xml
        TextView username = (TextView) findViewById(R.id.Username);
        TextView password = (TextView) findViewById(R.id.Password);

        new Login().execute(username.getText().toString(),password.getText().toString());

        //this.finish();
        //endregion
    }

    // Title AsyncTask
    private class Login extends AsyncTask<String, Void, Boolean> {
        Icarus myicarus;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {
            System.setProperty("jsse.enableSNIExtension", "false");
            myicarus = new Icarus(params[0], params[1]);

            return myicarus.login();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(!result){
                TextView error = (TextView) findViewById(R.id.ErrorMsg);
                error.setText(getString(R.string.Error_Login));
                error.setVisibility(View.VISIBLE);
            }else{
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("icarus", myicarus);
                i.putExtras(bundle);
                startActivity(i); //Στην onCreate της MainActivity κάνω retrieve το object Icarus - φτιάξε τα υπόλοιπα, δες τι έκανε και ο Χάρης
                LoginActivity.this.finish();
            }
        }
    }
}
