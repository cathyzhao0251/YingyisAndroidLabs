package algonquin.cst2335.zhao0251.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import algonquin.cst2335.zhao0251.data.MainViewModel;
import algonquin.cst2335.zhao0251.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    //ViewBinding class:
    ActivityMainBinding variableBinding;
    MainViewModel vModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//nothing loaded here, no ids present
        vModel = new ViewModelProvider(this).get(MainViewModel.class);
        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());

        //this is the only function call, loads stuff onto screen
        setContentView(variableBinding.getRoot());//loads XML on screen

        //must look for id below
        TextView tv = variableBinding.theTextView; //might return null
        Button b = variableBinding.theButton;
        EditText et = variableBinding.theEditText;

        variableBinding.theCheckbox.setOnCheckedChangeListener(
                (btn, onOrOff ) -> {
                    vModel.onOrOff.postValue(onOrOff);
                } );
        variableBinding.theSwitch.setOnCheckedChangeListener(
                (btn, onOrOff) -> {
                    vModel.onOrOff.postValue(onOrOff);
                } );
        variableBinding.theRadioBtn.setOnCheckedChangeListener(
                (btn, onOrOff) -> {
                    vModel.onOrOff.postValue(onOrOff);
                } );

        variableBinding.theImageBtn.setOnClickListener( clk -> {
            variableBinding.theTextView.setText("You click the image");
        });

        vModel.onOrOff.observe(this, newValue ->{
            variableBinding.theCheckbox.setChecked(newValue);
            variableBinding.theSwitch.setChecked(newValue);
            variableBinding.theRadioBtn.setChecked(newValue);


        });



        //put the string back into the edit text
        et.setText(vModel.userString.getValue());
        vModel.userString.observe(this,
                (s) ->{
            b.setText("Your text is now: " + s);
            tv.setText("Your text is now: " + s);
        });

        b.setOnClickListener( v -> {
            vModel.userString.postValue(et.getText().toString());

        });


    }
}