package com.example.inclass03;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //In-Class Assignment 03
    //Group 9
    //Rockford Stoller
    //Ryan Swain

    //request code for startActivityForResults()
    static final int REQ_CODE = 100;
    static final int DIS_CODE = 101;
    //value key for receiving the results
    static final String VALUE_KEY = "VALUE";
    //student key for passing the information to the display activity
    static final String STUDENT_KEY = "STUDENT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("My Profile");

        //find the image view for the avatar to initially set it's tag to 0
        ImageView image = findViewById(R.id.avatar_imageView);
        image.setTag(0);

        //profile information code -----------------------
        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //find and store the string for first name
                EditText fNameEditText = findViewById(R.id.firstname_editText);
                String fName = fNameEditText.getText().toString();

                //find and store the string for last name
                EditText lNameEditText = findViewById(R.id.lastname_editText);
                String lName = lNameEditText.getText().toString();

                //find and store the string for student id
                EditText stuIdEditText = findViewById(R.id.studentID_editText);
                String stuId = stuIdEditText.getText().toString();

                //find the radio group
                RadioGroup depRadioGroup = findViewById(R.id.department_radioGroup);

                //if the first name is not entered, set error
                if(fName.equals("")) {
                    fNameEditText.setError("Required");
                    //else if the lase name is not entered, set error
                } else if(lName.equals("")) {
                    lNameEditText.setError("Required");
                    //else if the student id is not exactly 9 numbers, set error
                } else if(stuId.length() < 9) {
                    stuIdEditText.setError("9 Digit Student ID Number");
                    //else if no radio group has been selected, set error
                } else if(depRadioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(MainActivity.this, "Select a Department", Toast.LENGTH_SHORT).show();
                    //else all input is valid
                } else {
                    //create explicit intent to go to the display profile activity
                    Intent intent = new Intent(MainActivity.this, DisplayMyProfileActivity.class);

                    //find the image view for the avatar
                    ImageView image = findViewById(R.id.avatar_imageView);

                    //find the radio button that was selected and get the string from it
                    RadioButton depRadionButton = findViewById(depRadioGroup.getCheckedRadioButtonId());
                    String departString = depRadionButton.getText().toString();

                    //create a student object (the initialization of the tag for the image view removed the need for a null check on image.getTag())
                    Student student = new Student((int) image.getTag(), fName, lName, stuId, departString);

                    //put the student object in the intent as an extra with it's key
                    intent.putExtra(STUDENT_KEY, student);

                    //start the display profile activity
                    startActivityForResult(intent, DIS_CODE);
                }
            }
        });

        //avatar code -------------------------------
        findViewById(R.id.avatar_imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create explicit intent to the select avatar activity
                Intent intent = new Intent(MainActivity.this, SelectAvatarActivity.class);
                //start the select avatar activity with the request code to receive results
                startActivityForResult(intent, REQ_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //if the result == the code we sent out to select avatar activity
        if(requestCode == REQ_CODE) {
            //if the result == OK
            if(resultCode == RESULT_OK) {

                //get the int result value returned from the select avatar activity
                int imageNum = data.getExtras().getInt(VALUE_KEY);
                //find the avatar image view
                ImageView imageView = findViewById(R.id.avatar_imageView);

                //if the first avatar was chosen
                if(imageNum == 1) {
                    imageView.setImageDrawable(getDrawable(R.drawable.avatar_f_1));
                    imageView.setTag(1);
                    //else if the second avatar was chosen
                } else if(imageNum == 2) {
                    imageView.setImageDrawable(getDrawable(R.drawable.avatar_f_2));
                    imageView.setTag(2);
                    //else if the third avatar was chosen
                } else if(imageNum == 3) {
                    imageView.setImageDrawable(getDrawable(R.drawable.avatar_f_3));
                    imageView.setTag(3);
                    //else if the fourth avatar was chosen
                } else if(imageNum == 4) {
                    imageView.setImageDrawable(getDrawable(R.drawable.avatar_m_1));
                    imageView.setTag(4);
                    //else if the fifth avatar was chosen
                } else if(imageNum == 5) {
                    imageView.setImageDrawable(getDrawable(R.drawable.avatar_m_2));
                    imageView.setTag(5);
                    //else if the sixth avatar was chosen
                } else if(imageNum == 6) {
                    imageView.setImageDrawable(getDrawable(R.drawable.avatar_m_3));
                    imageView.setTag(6);
                }
            }
            //else if the result == the code we sent out to display my profile activity
        } else if(requestCode == DIS_CODE) {
            //if the result == OK
            if(resultCode == RESULT_OK) {
                //find the avatar image view
                ImageView imageView = findViewById(R.id.avatar_imageView);

                //create a student with the parcelable extra in the result
                Student student = data.getExtras().getParcelable(STUDENT_KEY);

                //check the students image id and assign the correct avatar based upon it
                if(student.imageId == 1) {
                    imageView.setImageDrawable(getDrawable(R.drawable.avatar_f_1));
                } else if(student.imageId == 2) {
                    imageView.setImageDrawable(getDrawable(R.drawable.avatar_f_2));
                } else if(student.imageId == 3) {
                    imageView.setImageDrawable(getDrawable(R.drawable.avatar_f_3));
                } else if(student.imageId == 4) {
                    imageView.setImageDrawable(getDrawable(R.drawable.avatar_m_1));
                } else if(student.imageId == 5) {
                    imageView.setImageDrawable(getDrawable(R.drawable.avatar_m_2));
                } else if(student.imageId == 6) {
                    imageView.setImageDrawable(getDrawable(R.drawable.avatar_m_3));
                } else {
                    imageView.setImageDrawable(getDrawable(R.drawable.select_image));
                }
                //imageView.setImageDrawable(getDrawable(R.drawable.ic_launcher_background));//FOR TESTING

                //find the first name edit text and assign it the student's first name
                EditText fnameEditText = findViewById(R.id.firstname_editText);
                fnameEditText.setText(student.fname);
                //fnameEditText.setText("TEST");//FOR TESTING

                //find the last name edit text and assign it the student's last name
                EditText lnameEditText = findViewById(R.id.lastname_editText);
                lnameEditText.setText(student.lname);
                //lnameEditText.setText("TEST");//FOR TESTING

                //find the display student id text view and assign it the ID of the student
                EditText stuIdEditText = findViewById(R.id.studentID_editText);
                stuIdEditText.setText(student.stuId);
                //stuIdEditText.setText("TEST");//FOR TESTING

                //find the display department text view and assign it the department of the student
                RadioGroup departRadioGroup = findViewById(R.id.department_radioGroup);

                //for loop to test each radio buttons text against the department string attribute of the student to check the correct radio button
                for(int i = 0; i < departRadioGroup.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) departRadioGroup.getChildAt(i);

                    if(rb.getText().toString().equals(student.depart)) {
                        rb.setChecked(true);
                        //rb.setChecked(false);//FOR TESTING
                    }
                }
            }
        }
    }
}
