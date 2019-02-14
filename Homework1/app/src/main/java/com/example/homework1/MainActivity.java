package com.example.homework1;

import android.app.Application;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

//Homework 1
//Group 9
//Rockford Stoller
//Ryan Swaim

public class MainActivity extends AppCompatActivity {

    public double weight;
    public char gender;
    public int ounces;
    public double percentAlcohol;
    public double bacLevel;

    public double totalOuncesTimesAlcohol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize bacLevel, weight, and totalOuncesTimeAlcohol to 0
        bacLevel = 0;
        weight = 0;
        totalOuncesTimesAlcohol = 0;
        //initialize ounces to 1 since it starts with 1 ounce selected
        ounces = 1;

        //find the weight and gender views and the save weight button to collect information
        final EditText weightEditText = findViewById(R.id.weight_editText);
        final Switch genderSwitch = findViewById(R.id.gender_switch);
        genderSwitch.isChecked();
        final Button saveWeightButton = findViewById(R.id.save_weight_button);

        //find the add drink button and progress bar
        final Button addDrinkButton = findViewById(R.id.add_drink_button);
        final ProgressBar bacProgressBar = findViewById(R.id.bac_progressBar);
        final TextView bacLevelTextView = findViewById(R.id.bac_level_number_textView);
        final TextView yourStatusTextView = findViewById(R.id.your_status_output_textView);

        //saving the users weight and gender on button click
        //region
        saveWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(weightEditText.getText().toString().equals("") || weightEditText.getText().toString().equals(".")) {
                    weightEditText.setError("Enter your weight");
                } else {
                    weightEditText.setError(null);
                    weight = Double.parseDouble(weightEditText.getText().toString());

                    if(genderSwitch.isChecked()) {
                        gender = genderSwitch.getTextOn().toString().charAt(0);
                        bacLevel = (totalOuncesTimesAlcohol * 6.24 / (weight * .68));
                    } else {
                        gender = genderSwitch.getTextOff().toString().charAt(0);
                        bacLevel = (totalOuncesTimesAlcohol * 6.24 / (weight * .55));
                    }

                    bacLevelTextView.setText(String.format("%.2f", bacLevel));
                    bacProgressBar.setProgress((int)(bacLevel * 100));

                    if(bacLevel > 0.2) {
                        if(bacLevel > 0.25) {
                            Toast.makeText(MainActivity.this, R.string.no_more_drinks_text, Toast.LENGTH_LONG).show();
                        }
                        yourStatusTextView.setText(R.string.over_the_limit_text);
                    } else if (bacLevel > 0.08) {
                        yourStatusTextView.setText(R.string.be_careful_text);
                    } else {
                        yourStatusTextView.setText(R.string.youre_safe_text);
                    }
                }

                Log.d("demo", "weight = " + weight + " | gender = " + gender);
            }
        });
        //endregion

        //find the radio group for getting the drink size
        final RadioGroup drinkSizeRadioGroup = findViewById(R.id.ounce_radioGroup);

        //choosing a radio button for drink size
        //region
        drinkSizeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = findViewById(checkedId);

                //check the selected radio button id against each ounce button id and assign the correct number
                if(checkedId == R.id.one_ounce_radioButton) {
                    ounces = 1;
                } else if(checkedId == R.id.five_ounce_radioButton) {
                    ounces = 5;
                } else if(checkedId == R.id.twelve_ounce_radioButton) {
                    ounces = 12;
                }

                Log.d("demo", "ounces = " + ounces);
            }
        });
        //endregion

        //find the seek bar and display text view for alcohol percentage
        final SeekBar alcoholPercentSeekbar = findViewById(R.id.alcohol_percent_seekBar);
        final TextView alcoholPercentDisplay = findViewById(R.id.seekbar_percent_textView);

        //initially set the text of the display text view to the progress of the seek bar and assign it to the alcohol percent variable
        alcoholPercentDisplay.setText(alcoholPercentSeekbar.getProgress() * 5 + "%");
        percentAlcohol = (alcoholPercentSeekbar.getProgress() * 5) / (double)100;
        //Log.d("demo", "percentAlcohol = " + percentAlcohol);

        //changing the seek bar for alcohol percent
        //region
        alcoholPercentSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                alcoholPercentDisplay.setText(progress * 5 + "%");
                percentAlcohol = (progress * 5) / (double)100;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("demo", "percentAlcohol = " + percentAlcohol);
            }
        });
        //endregion

        //on add drink button click
        //region
        addDrinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if no weight has been entered and saved
                if(weight == 0) {
                    weightEditText.setError("Enter weight in lbs (and save)");
                } else {
                    //add ounces times alcohol to total
                    totalOuncesTimesAlcohol += ounces * percentAlcohol;

                    //if male
                    if(gender == genderSwitch.getTextOn().toString().charAt(0)) {
                        bacLevel += ((ounces * percentAlcohol) * 6.24 / (weight * .68));
                        //else if female
                    } else {
                        bacLevel += ((ounces * percentAlcohol) * 6.24 / (weight * .55));
                    }

                    bacLevelTextView.setText(String.format("%.2f", bacLevel));
                    bacProgressBar.setProgress((int)(bacLevel * 100));

                    if(bacLevel > 0.2) {
                        yourStatusTextView.setText(R.string.over_the_limit_text);
                    } else if (bacLevel > 0.08) {
                        yourStatusTextView.setText(R.string.be_careful_text);
                    } else {
                        yourStatusTextView.setText(R.string.youre_safe_text);
                    }

                    if(bacLevel >= 0.25) {
                        Toast.makeText(MainActivity.this, R.string.no_more_drinks_text, Toast.LENGTH_LONG).show();
                        saveWeightButton.setEnabled(false);
                        addDrinkButton.setEnabled(false);
                        drinkSizeRadioGroup.getChildAt(0).setEnabled(false);
                        drinkSizeRadioGroup.getChildAt(1).setEnabled(false);
                        drinkSizeRadioGroup.getChildAt(2).setEnabled(false);
                        genderSwitch.setEnabled(false);
                        alcoholPercentSeekbar.setEnabled(false);
                        weightEditText.setEnabled(false);
                    }

                    Log.d("demo", "bacLevel = " + bacLevel);
                }
            }
        });
        //endregion

        //find the reset button
        Button resetButton = findViewById(R.id.reset_button);

        //on reset button click
        //region
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weightEditText.setText("");
                weight = 0;

                genderSwitch.setChecked(false);
                gender = genderSwitch.getTextOff().toString().charAt(0);

                drinkSizeRadioGroup.check(R.id.one_ounce_radioButton);

                alcoholPercentSeekbar.setProgress(1);
                alcoholPercentDisplay.setText(alcoholPercentSeekbar.getProgress() * 5 + "%");
                percentAlcohol = (alcoholPercentSeekbar.getProgress() * 5) / (double)100;

                bacLevel = 0;

                bacProgressBar.setProgress(0);

                totalOuncesTimesAlcohol = 0;

                bacLevelTextView.setText(R.string.zero_point_zero_text);
                yourStatusTextView.setText(R.string.youre_safe_text);


                saveWeightButton.setEnabled(true);
                addDrinkButton.setEnabled(true);
                drinkSizeRadioGroup.getChildAt(0).setEnabled(true);
                drinkSizeRadioGroup.getChildAt(1).setEnabled(true);
                drinkSizeRadioGroup.getChildAt(2).setEnabled(true);
                genderSwitch.setEnabled(true);
                alcoholPercentSeekbar.setEnabled(true);
                weightEditText.setEnabled(true);
            }
        });
        //endregion
    }
}
