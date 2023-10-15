package algonquin.cst2335.zhao0251;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import algonquin.cst2335.zhao0251.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);
        Log.w(TAG, "In onCreate() - Loading Widgets");

        View button = findViewById(R.id.loginButton);
        EditText et = findViewById(R.id.emailEditText);

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("emailAddress", "");
        et.setText(emailAddress);

        button.setOnClickListener( click ->{
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("emailAddress", et.getText().toString());
            editor.putFloat("Hi", 4.5f);
            editor.putInt("Age", 35);
            editor.apply();

           Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
           nextPage.putExtra("EmailAddress", et.getText().toString());
            startActivity(nextPage) ;
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(TAG, "In onStart() - The application is now visible on screen");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "In onResume - The allpication is now responding to user input");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(TAG, "In onPause - The application no longer responds to user input");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG, "In onStop() - The application is no longer visible");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "In onDestroy() - Any memory used by the application is freed");
    }
}
