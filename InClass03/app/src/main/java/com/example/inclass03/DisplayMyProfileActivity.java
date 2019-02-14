package com.example.inclass03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayMyProfileActivity extends AppCompatActivity {

    //store student received from the main activity and used to return that student to the main activity
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_my_profile);

        setTitle("Display My Profile");

        //if an intent was used to start this activity and the intent has extras
        if(getIntent() != null && getIntent().getExtras() != null) {
            //find the display avatar image view
            ImageView imageView = findViewById(R.id.displayAvatar_imageView);

            //create a student with the parcelable extra in the intent
            student = getIntent().getExtras().getParcelable(MainActivity.STUDENT_KEY);

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

            //find the display name text view and assign it the student's first and last name
            TextView nameTextView = findViewById(R.id.displayName_textView);
            String name = student.fname + " " + student.lname;
            nameTextView.setText(getText(R.string.name_display)+ " " + name);

            //find the display student id text view and assign it the ID of the student
            TextView stuIdTextView = findViewById(R.id.displayID_textView);
            String stuId = student.stuId;
            stuIdTextView.setText(getText(R.string.studentID_display) + " " + stuId);

            //find the display department text view and assign it the department of the student
            TextView departTextView = findViewById(R.id.displayDepartment_textView);
            String depart = student.depart;
            departTextView.setText(getText(R.string.department_hint) + " " + depart);
        }

        //if the edit button is clicked, send results with the student back to the main activity and end this activity
        //on Click
        findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create an empty intent
                Intent intent = new Intent();
                //put the value in the intent through extra with a VALUE_KEY created in the MainActivity
                intent.putExtra(MainActivity.STUDENT_KEY, student);

                //return the result
                setResult(RESULT_OK, intent);

                //end this activity and return to the main activity
                finish();
            }
        });
    }
}
