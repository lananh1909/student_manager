package com.example.sqliteexamples;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class StudentDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        Bundle student = getIntent().getExtras();
        TextView mssv = findViewById(R.id.edit_mssv);
        TextView hoten = findViewById(R.id.edit_name);
        TextView ngaysinh = findViewById(R.id.edit_birthday);
        TextView email = findViewById(R.id.edit_email);
        TextView diachi = findViewById(R.id.edit_address);
        String date = student.getString("ngaysinh");
        SimpleDateFormat sdf3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        try{
            d1 = sdf3.parse(date);
        }catch (Exception e){
            try {
                d1 = dateFormat.parse(date);
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
        }

        ngaysinh.setText(dateFormat.format(d1));
        mssv.setText(student.getString("mssv"));
        hoten.setText(student.getString("hoten"));
        email.setText(student.getString("email"));
        diachi.setText(student.getString("diachi"));
    }
}