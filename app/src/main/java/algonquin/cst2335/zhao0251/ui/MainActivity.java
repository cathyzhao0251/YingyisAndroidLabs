package algonquin.cst2335.zhao0251.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import algonquin.cst2335.zhao0251.R;
import algonquin.cst2335.zhao0251.data.MainViewModel;
import algonquin.cst2335.zhao0251.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding variableBinding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setContentView(variableBinding.getRoot());

        TextView tv = variableBinding.myTextView;

        Button b = variableBinding.myButton;

        EditText et = variableBinding.myEditText;
        viewModel.userString.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tv.setText(s);
            }
        });
        et.setText( viewModel.userString.getValue() );

        b.setOnClickListener(v -> {
            tv.setText(R.string.button_message);
            String string = et.getText().toString();
            viewModel.userString.postValue( string );
            b.setText("You clicked the button");
        });

    }
}