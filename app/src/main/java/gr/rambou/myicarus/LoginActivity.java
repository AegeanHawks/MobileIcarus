package gr.rambou.myicarus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
    }

    public void Login_Clicked(View view) {
        //Intent i = new Intent(getApplicationContext(), MainActivity.class);
        //i.putExtra("key", new nick());
        //startActivity(i);
        //this.finish();

        //region Testing change activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
        //endregion
    }
}
