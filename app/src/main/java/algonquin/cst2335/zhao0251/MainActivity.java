package algonquin.cst2335.zhao0251;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.myTextView);

        Button b = findViewById(R.id.myButton);

        EditText et = findViewById(R.id.myEditText);

        b.setOnClickListener(v -> {
            tv.setText("You clicked the button");
            et.setText("You clicked the button");
            b.setText("You clicked the button");
        });

    }
}