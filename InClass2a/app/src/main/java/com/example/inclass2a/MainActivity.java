//Rockford Stoller
//Ryan Swaim
//Group 9

package com.example.inclass2a;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //sets the Title of the Application
        setTitle("BMI Calculator");


        final EditText weightInput = findViewById(R.id.Weight_Input);
        final EditText heightInputFeet = findViewById(R.id.Height_Input_Feet);
        final EditText heightInputInches  = findViewById(R.id.Height_Input_Inches);
        Button calculateButton = findViewById(R.id.Caclulate_Button);
        final TextView bmiResults = findViewById(R.id.BMI_Results);
        final TextView weightResults = findViewById(R.id.Weight_Results);

        //specify an action when the button is pressed
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gets the weight input from the Weight_Input field
                String w = weightInput.getText().toString();
                //gets the height input(ft) from the Height_Input_Feet field
                String hf = heightInputFeet.getText().toString();
                //gets the height input(in) from the Height_Input_Inches field
                String hi = heightInputInches.getText().toString();

                //checks to see if the weight input is empty
                if(w.equals("") || w.equals(".")) {
                    //sets the error message to be displayed if field is empty
                    weightInput.setError("Fill it up!");
                    //creates pop-up "Invalid Inputs" that will show at the bottom of the app
                    Toast.makeText(MainActivity.this, getText(R.string.Invalid_Inputs), Toast.LENGTH_SHORT).show();
                    //checks to see if the height in feet input is empty
                } else if(hf.equals("") || hf.equals(".")) {
                    //sets the error message to be displayed if field is empty
                    heightInputFeet.setError("Fill it up!");
                    //creates pop-up "Invalid Inputs" that will show at the bottom of the app
                    Toast.makeText(MainActivity.this, getText(R.string.Invalid_Inputs), Toast.LENGTH_SHORT).show();
                    //checks to see if the height in inches input is empty
                } else if(hi.equals("") || hi.equals(".")) {
                    //sets the error message to be displayed if field is empty
                    heightInputInches.setError("Fill it up!");
                    //creates pop-up "Invalid Inputs" that will show at the bottom of the app
                    Toast.makeText(MainActivity.this, getText(R.string.Invalid_Inputs), Toast.LENGTH_SHORT).show();
                    //else the input is not blank
                } else {
                    //this will get the input and save it as a float for the weight, height in feet,
                    //and height in inches respectively
                    float weight = Float.parseFloat(weightInput.getText().toString());
                    float heightFeet = Float.parseFloat(heightInputFeet.getText().toString());
                    float heightInches = Float.parseFloat(heightInputInches.getText().toString());

                    //this will convert the height in feet to inches and add together both heights
                    //to give us the total height
                    float heightTotal = (heightFeet * 12) + heightInches;

                    //this is the formula for the BMI it is:
                    //BMI = (Weight in Pounds / (Height in inches x Height in inches)) x 703
                    float bmi = (weight / (heightTotal * heightTotal)) * 703;

                    //this formats the bmi to one decimal place and displays the BMI
                    bmiResults.setText(String.format("Your BMI: %.1f", bmi));
                    //checks to see if the person is underweight if their bmi is under 18.5
                    if (bmi < 18.5) {
                        //displays the message if the person is underweight
                        weightResults.setText("You are underweight");
                        //if the person's bmi is 18.5 or over but less than 25 they are normal weight
                    } else if (bmi < 25) {
                        //displays the message if the person is normal weight
                        weightResults.setText("You are normal weight");
                        //if the person's bmi is 25 or over but less than 30 they are overweight
                    } else if (bmi < 30) {
                        //displays the message if the person is overweight
                        weightResults.setText("You are overweight");
                        //if the person's bmi is 30 or over they are obese
                    } else {
                        //displays the message if the person is obese
                        weightResults.setText("You are Obese");
                    }
                    //creates pop-up "BMI Calculated" that will show at the bottom of the app
                    Toast.makeText(MainActivity.this, getText(R.string.BMI_Calculated), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
